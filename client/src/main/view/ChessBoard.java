package src.main.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import src.main.*;
import src.main.communication.Encoder;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

public class ChessBoard implements Initializable {

    private Timer player1TimerController;
    private Timer player2TimerController;
    private Board board = new Board();
    private Circle[][] stonesCircle = new Circle[19][19];
    private Label[][] steps = new Label[19][19];
    private int color = -1;

    @FXML
    private Pane chessPane;

    private static final int borderGap = 20;
    private static final int stoneGap = 30;
    private static final int xLen = 18 * stoneGap + 2 * borderGap;
    private static final int yLen = 18 * stoneGap + 2 * borderGap;
    private static final int stoneRadius = (stoneGap - 2) / 2;

    private Point pixel = new Point();
    private Point index = new Point();

    @FXML
    private void onClick(MouseEvent event) {
        /****************************  release ****************************/
        if (Client.getGameController().isBegin() && Client.getGameController().getTurn() == this.color) {
            getPixelPos(event);
            int action = action();
            if (action != Type.Action.INVALID) {
                String msg = Encoder.actionRequest(action, color, index.x, index.y);
                System.out.println(msg);
                place(index.x, index.y, color);
                if (action == Type.Action.KILL) {
                    remove();
                    board.remove();
                }
            }
        }
        /****************************  release ****************************/
        /****************************  test ****************************/
        if (Client.getGameController().isBegin()) {
            getPixelPos(event);
            int action = action();
            if (action != Type.Action.INVALID) {
                String jsonmsg = Encoder.actionRequest(action, color, index.x, index.y);
                System.out.println(jsonmsg);
                place(index.x, index.y, color);
                if (action == Type.Action.KILL) {
                    remove();
                    board.remove();
                }
                if (color == -1) {
                    player2TimerController.pause();
                    player1TimerController.start();
                } else {
                    player1TimerController.pause();
                    player2TimerController.start();
                }
                color = -color;
            }
        }
        /****************************  test ****************************/
    }

    public void place(int x, int y, int color) {
        Circle stone = stonesCircle[x][y];
        Label step = steps[x][y];
        System.out.println("ChessBoard Place: (" + x + "," + y + ")");
        if (color == Stone.Black) {
            stone.setFill(Color.color(0.1, 0.1, 0.1));
            step.setTextFill(Color.WHITE);
        } else {
            stone.setFill(Color.color(0.97, 0.98, 0.98));
            step.setTextFill(Color.BLACK);
        }
        stone.setLayoutX(pixel.x);
        stone.setLayoutY(pixel.y);
        stone.setRadius(stoneRadius);
        stone.setEffect(new Lighting());
        chessPane.getChildren().add(stone);
        board.add(x, y, color);
        step.setText(Integer.toString(Board.stones[x][y].step));
        step.setPrefSize(24, 12);
        step.setLayoutX(pixel.x - 12);
        step.setLayoutY(pixel.y - 8);
        step.setAlignment(Pos.BASELINE_CENTER);
        if (Client.getGameController().isShowStep()) {
            chessPane.getChildren().add(step);
        }
        // reverse turns
        if (Client.getGameController().getTurn() == Stone.Black) {
            player2TimerController.pause();
            player1TimerController.start();
        } else {
            player1TimerController.pause();
            player2TimerController.start();
        }
        Client.getGameController().takeTurns();
        // judge enable
        if (getStep() >= 100) {
            Client.getGameController().judgeEnable();
            if (getStep() >= 360) {
                Client.getGameController().gameOver();
            }
        }
    }

    public void remove() {
        for (int chain : Board.dead) {
            HashSet<Stone> stones = Board.stonesMap.get(chain);
            System.out.print("ChessBoard remove chain " + chain + " : ");
            for (Stone s : stones) {
                System.out.print("Stone(" + s.x + "," + s.y + ") ");
                chessPane.getChildren().remove(stonesCircle[s.x][s.y]);
                if (Client.getGameController().isShowStep()) {
                    chessPane.getChildren().remove(steps[s.x][s.y]);
                }
            }
            System.out.println();
        }
    }

    public void showStep() {
        for (int i = 0; i < 19; ++i) {
            for (int j = 0; j < 19; ++j) {
                if (Board.stones[i][j].color != Stone.None) {
                    chessPane.getChildren().add(steps[i][j]);
                }
            }
        }
    }

    public void hideStep() {
        for (int i = 0; i < 19; ++i) {
            for (int j = 0; j < 19; ++j) {
                if (Board.stones[i][j].color != Stone.None) {
                    chessPane.getChildren().remove(steps[i][j]);
                }
            }
        }
    }

    public int getStep() {
        return Board.step;
    }

    public ArrayList<Number> getPlayerPoint() {
        return Core.scoring(board);
    }

    public void setTimer(Timer timer01, Timer timer02) {
        this.player1TimerController = timer01;
        this.player2TimerController = timer02;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void clear() {
        for (int i = 0; i < 19; ++i) {
            for (int j = 0; j < 19; ++j) {
                if (Board.stones[i][j].color != Stone.None) {
                    chessPane.getChildren().remove(stonesCircle[i][j]);
                    chessPane.getChildren().remove(steps[i][j]);
                }
            }
        }
        board.clear();
    }

    private void getPixelPos(MouseEvent event) {
        pixel.setLocation(event.getX(), event.getY());
    }

    private void getIndexPos() {
        index.setLocation((pixel.x - borderGap) / stoneGap, (pixel.y - borderGap) / stoneGap);
    }

    private int action() {
        if (pixel.x < borderGap - stoneRadius || pixel.x > xLen - borderGap + stoneRadius
                || pixel.y < borderGap - stoneRadius || pixel.y > yLen - borderGap + stoneRadius) {
            return Type.Action.INVALID;
        }
        int gridX = (pixel.x - borderGap) % stoneGap;
        int gridY = (pixel.y - borderGap) % stoneGap;
        int indexX = (pixel.x - borderGap) / stoneGap;
        int indexY = (pixel.y - borderGap) / stoneGap;
        if (gridX < stoneRadius && gridY < stoneRadius) {
            pixel.x = indexX * stoneGap + borderGap;
            pixel.y = indexY * stoneGap + borderGap;
        } else if (gridX < stoneRadius && gridY > stoneGap - stoneRadius) {
            pixel.x = indexX * stoneGap + borderGap;
            pixel.y = (indexY + 1) * stoneGap + borderGap;
        } else if (gridX > stoneGap - stoneRadius && gridY < stoneRadius) {
            pixel.x = (indexX + 1) * stoneGap + borderGap;
            pixel.y = indexY * stoneGap + borderGap;
        } else if (gridX > stoneGap - stoneRadius && gridY > stoneGap - stoneRadius) {
            pixel.x = (indexX + 1) * stoneGap + borderGap;
            pixel.y = (indexY + 1) * stoneGap + borderGap;
        } else {
            return Type.Action.INVALID;
        }
        getIndexPos();
        return board.action(index, color);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /****** 要的 ******/
        /*
        drawBoard();
        drawLine();
        drawStar();
        */
        /******* 要的 ******/
        /*******************/
        Image boardPicture = new Image("resources/image/chessBoard.png");
        ImageView boardView = new ImageView(boardPicture);
        boardView.setFitWidth(580);
        boardView.setFitHeight(580);
        chessPane.getChildren().add(boardView);
        drawLine();
        drawStar();
        /*******************/
        initStonesCircle();
        initStepsLable();
    }

    private void drawBoard() {
        Rectangle rec = new Rectangle(0, 0, xLen, yLen);
        rec.setFill(Color.rgb(249, 214, 91));
        chessPane.getChildren().add(rec);
    }

    private void drawLine() {
        Line line;
        for (int i = 0; i < 19; ++i) {
            line = new Line(borderGap, i * stoneGap + borderGap, xLen - borderGap, i * stoneGap + borderGap);
            chessPane.getChildren().add(line);
        }
        for (int i = 0; i < 19; ++i) {
            line = new Line(i * stoneGap + borderGap, borderGap, i * stoneGap + borderGap, yLen - borderGap);
            line.setStroke(Color.BLACK);
            chessPane.getChildren().add(line);
        }
    }

    private void drawStar() {
        int x = 3;
        int y;
        Circle circle;
        for (int i = 0; i < 3; ++i) {
            y = 3;
            for (int j = 0; j < 3; ++j) {
                circle = new Circle();
                circle.setFill(Color.BLACK);
                circle.setRadius(3);
                circle.setLayoutX(x * stoneGap + borderGap);
                circle.setLayoutY(y * stoneGap + borderGap);
                chessPane.getChildren().add(circle);
                y = y + 6;
            }
            x = x + 6;
        }
    }

    private void initStonesCircle() {
        for (int i = 0; i < 19; ++i) {
            for (int j = 0; j < 19; ++j) {
                stonesCircle[i][j] = new Circle();
            }
        }
    }

    private void initStepsLable() {
        for (int i = 0; i < 19; ++i) {
            for (int j = 0; j < 19; ++j) {
                steps[i][j] = new Label();
            }
        }
    }
}
