package src.main.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import src.main.Action;
import src.main.Board;
import src.main.Stone;


import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by touhoudoge on 2017/4/7.
 */
public class ChessBox implements Initializable {

    private Timer timer;
    private Board board = new Board();
    private Circle[][] stonesPicture = new Circle[19][19];
    private int turns = -1;

    @FXML
    private Pane chessPane;

    private static final int borderGap = 20;
    private static final int stoneGap = 30;
    private static final int xLen = 18 * stoneGap + 2 * borderGap;
    private static final int yLen = 18 * stoneGap + 2 * borderGap;
    private static final int stoneRadius = 10;

    private Point pixel = new Point();
    private Point index = new Point();

    @FXML
    private void onClick(MouseEvent event) {
        getPixelPos(event);
        int action = action();
        if (action != Action.INVALID) {
            //System.out.println("iX=" + index.x + ", iY=" + index.y);
            if(action == Action.KILL){
                place(stonesPicture[index.x][index.y], turns);
                for(int chain : Board.killed){
                    remove(chain);
                }
                board.remove();
            }
            else{
                place(stonesPicture[index.x][index.y], turns);
            }
            timer.start();
            turns = -turns;
        }
    }

    private void getPixelPos(MouseEvent event){
        pixel.setLocation(event.getX(),event.getY());
    }

    private void getIndexPos(){
        index.setLocation((pixel.x - borderGap)/stoneGap,(pixel.y - borderGap)/stoneGap);
    }

    private void place(Circle stone, int color){
        System.out.println("Place: ("+index.x+","+index.y+")");
        board.add(index.x, index.y, turns);
        if(color == Stone.Black){
            stone.setFill(Color.BLACK);
        }
        else{
            stone.setFill(Color.WHITE);
        }
        stone.setLayoutX(pixel.x);
        stone.setLayoutY(pixel.y);
        stone.setRadius(stoneRadius);
        chessPane.getChildren().add(stone);
    }

    private void remove(int chain){
        ArrayList<Stone> stones = Board.stoneMap.get(chain);
        System.out.print("Kill chain " + chain +" : ");
        for(Stone s : stones){
            System.out.print("("+s.x+","+s.y+") ");
            chessPane.getChildren().remove(stonesPicture[s.x][s.y]);
        }
        System.out.println();
    }

    private int action() {
        if (pixel.x < borderGap - 8 || pixel.x > xLen - borderGap + 8
                || pixel.y < borderGap - 8 || pixel.y > yLen - borderGap + 8) {
            return Action.INVALID;
        }
        int gridX = (pixel.x - borderGap) % stoneGap;
        int gridY = (pixel.y - borderGap) % stoneGap;
        int indexX = (pixel.x - borderGap) / stoneGap;
        int indexY = (pixel.y - borderGap) / stoneGap;
        if (gridX < 8 && gridY < 8) {
            pixel.x = indexX * stoneGap + borderGap;
            pixel.y = indexY * stoneGap + borderGap;
        } else if (gridX < 8 && gridY > 22) {
            pixel.x = indexX * stoneGap + borderGap;
            pixel.y = (indexY + 1) * stoneGap + borderGap;
        } else if (gridX > 22 && gridY < 8) {
            pixel.x = (indexX + 1) * stoneGap + borderGap;
            pixel.y = indexY * stoneGap + borderGap;
        } else if (gridX > 22 && gridY > 22) {
            pixel.x = (indexX + 1) * stoneGap + borderGap;
            pixel.y = (indexY + 1) * stoneGap + borderGap;
        } else {
            return Action.INVALID;
        }
        getIndexPos();
        return board.action(index, turns);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawBoard();
        drawLine();
        drawStar();
        initStones();
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

    private void initStones(){
        for(int i = 0; i < 19; ++i){
            for(int j = 0; j < 19; ++j){
                stonesPicture[i][j] = new Circle();
            }
        }
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
