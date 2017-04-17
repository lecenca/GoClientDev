package src.main.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import src.main.Board;
import src.main.Stone;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by touhoudoge on 2017/4/7.
 */
public class ChessBox implements Initializable {

    private Timer timer;
    private Board board = new Board();
    private Circle[][] stone = new Circle[19][19];
    private int turns = -1;
    @FXML
    private Pane chessPane;

    private static final int borderGap = 20;
    private static final int stoneGap = 30;
    private static final int xLen = 18 * stoneGap + 2 * borderGap;
    private static final int yLen = 18 * stoneGap + 2 * borderGap;
    private static final int stoneRadius = 10;

    private int posX;
    private int posY;
    private int idxX;
    private int idxY;

    @FXML
    private void onClick(MouseEvent event) {
        getPixelPos(event);
        if (isClickVaild()) {
            System.out.println("px=" + posX + ", py=" + posY);
            System.out.println("iX=" + idxX + ", iY=" + idxY);
            board.chessMoves(idxX, idxY, turns);
            place(stone[idxX][idxY], turns);
            timer.start();
            turns = -turns;
        }
    }

    private void getPixelPos(MouseEvent event){
        posX = (int) event.getX();
        posY = (int) event.getY();
    }

    private void getIndexPos(){
        idxX = (posX - borderGap) / stoneGap;
        idxY = (posY - borderGap) / stoneGap;
    }

    private void place(Circle stone, int color){
        if(color == Stone.Black){
            stone.setFill(Color.BLACK);
        }
        else{
            stone.setFill(Color.WHITE);
        }
        stone.setLayoutX(posX);
        stone.setLayoutY(posY);
        stone.setRadius(stoneRadius);
        chessPane.getChildren().add(stone);
    }

    private void remove(Circle stone){
        board.get(idxX,idxY).setType(Stone.None);
        chessPane.getChildren().remove(stone);
    }

    private boolean isClickVaild() {
        if (posX < borderGap || posX > xLen - borderGap
                || posY < borderGap || posY > yLen - borderGap) {
            return false;
        }
        if ((posX - borderGap) % stoneGap < 8 && (posY - borderGap) % stoneGap < 8) {
            posX = (posX - borderGap) / stoneGap * stoneGap + borderGap;
            posY = (posY - borderGap) / stoneGap * stoneGap + borderGap;
        } else if ((posX - borderGap) % stoneGap < 8 && (posY - borderGap) % stoneGap > 22) {
            posX = (posX - borderGap) / stoneGap * stoneGap + borderGap;
            posY = ((posY - borderGap) / stoneGap + 1) * stoneGap + borderGap;
        } else if ((posX - borderGap) % stoneGap > 22 && (posY - borderGap) % stoneGap < 8) {
            posX = ((posX - borderGap) / stoneGap + 1) * stoneGap + borderGap;
            posY = (posY - borderGap) / stoneGap * stoneGap + borderGap;
        } else if ((posX - borderGap) % stoneGap > 22 && (posY - borderGap) % stoneGap > 22) {
            posX = ((posX - borderGap) / stoneGap + 1) * stoneGap + borderGap;
            posY = ((posY - borderGap) / stoneGap + 1) * stoneGap + borderGap;
        } else {
            return false;
        }
        getIndexPos();
        if (!board.check(idxX, idxY, turns)) {
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawBoard();
        drawLine();
        drawStar();
        initStoneCircle();
    }

    private void drawBoard() {
        Rectangle rec = new Rectangle(0, 0, 560, 560);
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

    private void initStoneCircle(){
        for(int i = 0; i < 19; ++i){
            for(int j = 0; j < 19; ++j){
                stone[i][j] = new Circle();
            }
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

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
