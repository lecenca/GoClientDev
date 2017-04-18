package src.main;

/**
 * Created by 刘俊延 on 2017/4/18.
 */
public class Core {

    public boolean hasLiberty(int x, int y, int color){
        Stone up = Board.stones[x][y].up();
        Stone down = Board.stones[x][y].down();
        Stone left = Board.stones[x][y].left();
        Stone right = Board.stones[x][y].right();
        if(up != null && (up.type == Stone.None || (up.type == color && liberty(up) > 1))){
            return true;
        }
        if(down != null && (down.type == Stone.None || (down.type == color && liberty(down) > 1))){
            return true;
        }
        if(left != null && (left.type == Stone.None || (left.type == color && liberty(left) > 1))){
            return true;
        }
        if(right != null && (right.type == Stone.None || (right.type == color && liberty(right) > 1))){
            return true;
        }
        // TODO: 处理落子无气，下了之后周围对方棋子也无气
        return false;
    }

    public int liberty(Stone stone){
        return Board.liberty.get(Board.chainMap.get(stone)).size();
    }

}
