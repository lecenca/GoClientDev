package src.main;

/**
 * Created by touhoudoge on 2017/4/16.
 */
public class Board {

    private Stone[][] stone = new Stone[19][19];

    public Board(){
        for(int i = 0; i < 19; ++i){
            for(int j = 0; j < 19; ++j){
                stone[i][j] = new Stone();
            }
        }
    }

    public void chessMoves(int x,int y,int color){

    }

    public Stone get(int x, int y){
        return stone[x][y];
    }

    public boolean check(int x, int y, int color){
        if(stone[x][y].getType() != Stone.None){
            return false;
        }
        stone[x][y].setType(color);
        return true;
    }
}
