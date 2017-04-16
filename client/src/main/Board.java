package src.main;

/**
 * Created by touhoudoge on 2017/4/16.
 */
public class Board {

    // -1 is black, 1 is white
    private int[][] board = new int[19][19];

    public Board(){
        for(int i = 0;i<19;++i){
            for(int j = 0;j<19;++j){
                board[i][j] = 0;
            }
        }
    }

    public void chessMoves(int x,int y,int color){

    }
}
