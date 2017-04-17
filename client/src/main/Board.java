package src.main;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by touhoudoge on 2017/4/16.
 */
public class Board {

    private Stone[][] board = new Stone[19][19];

    public Board(){
        for(int i = 0; i < 19; ++i){
            for(int j = 0; j < 19; ++j){
                board[i][j] = new Stone();
            }
        }
    }

    public void chessMoves(int x,int y,int color){

    }

    public void draw(Circle circle, int posX, int posY, int color){
        if(color == Stone.Black){
            circle.setFill(Color.BLACK);
        }
        else{
            circle.setFill(Color.WHITE);
        }
        circle.setLayoutX(posX);
        circle.setLayoutY(posY);
        circle.setRadius(10);
    }

    public boolean check(int x, int y, int color){
        if(board[x][y].getType() != Stone.None){
            return false;
        }
        board[x][y].setType(color);
        return true;
    }
}
