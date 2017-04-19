package src.main;

/**
 * Created by 刘俊延 on 2017/4/18.
 */
public class Core {

    public int action(int x, int y, int color){
        Stone up = Board.stones[x][y].up();
        Stone down = Board.stones[x][y].down();
        Stone left = Board.stones[x][y].left();
        Stone right = Board.stones[x][y].right();
        boolean hasLiberty = false;
        boolean hasKilled = false;
        if(up != null){
            if(up.type == Stone.None || (up.type == color && liberty(up) > 1)){
                hasLiberty = true;
            }
            else if (up.type == -color && liberty(up) == 1){
                hasKilled = true;
                Board.killed.add(Board.chainMap.get(up));
            }
        }
        if(down != null){
            if(down.type == Stone.None || (down.type == color && liberty(down) > 1)){
                hasLiberty = true;
            }
            else if (down.type == -color && liberty(down) == 1){
                hasKilled = true;
                Board.killed.add(Board.chainMap.get(down));
            }
        }
        if(left != null){
            if(left.type == Stone.None || (left.type == color && liberty(left) > 1)){
                hasLiberty = true;
            }
            else if (left.type == -color && liberty(left) == 1){
                hasKilled = true;
                Board.killed.add(Board.chainMap.get(left));
            }
        }
        if(right != null){
            if(right.type == Stone.None || (right.type == color && liberty(right) > 1)){
                hasLiberty = true;
            }
            else if (right.type == -color && liberty(right) == 1){
                hasKilled = true;
                Board.killed.add(Board.chainMap.get(right));
            }
        }
        if(hasKilled){
            return Action.KILL;
        }
        if(hasLiberty){
            return Action.PLACE;
        }
        return Action.INVALID;
    }


    public int liberty(Stone stone){
        int chain = Board.chainMap.get(stone);
        System.out.println("Point ("+stone.x+","+stone.y+") in chain "+chain+" has liberty "+Board.liberty.get(chain).size());
        return Board.liberty.get(Board.chainMap.get(stone)).size();
    }

}
