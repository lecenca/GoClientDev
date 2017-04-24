package src.main;

/**
 * Created by 刘俊延 on 2017/4/18.
 */
public class Core {

    private static boolean hasLiberty;
    private static boolean hasKilled;

    public static int action(int x, int y, int color) {
        Stone up = Board.stones[x][y].up();
        Stone down = Board.stones[x][y].down();
        Stone left = Board.stones[x][y].left();
        Stone right = Board.stones[x][y].right();
        hasLiberty = false;
        hasKilled = false;
        if (up != null) {
            actionWith(up, color);
        }
        if (down != null) {
            actionWith(down, color);
        }
        if (left != null) {
            actionWith(left, color);
        }
        if (right != null) {
            actionWith(right, color);
        }
        return hasKilled ? Action.KILL : hasLiberty ? Action.PLACE : Action.INVALID;
    }

    private static void actionWith(Stone stone, int color) {
        if (stone.color == Stone.None || (stone.color == color && liberty(stone) > 1)) {
            hasLiberty = true;
        } else if (stone.color == -color && liberty(stone) == 1) {
            hasKilled = true;
            Board.killed.add(Board.chainMap.get(stone));
        }
    }

    public static int liberty(Stone stone) {
        int chain = Board.chainMap.get(stone);
        System.out.println("Point (" + stone.x + "," + stone.y + ") in chain " + chain + " has liberty " + Board.liberty.get(chain).size());
        return Board.liberty.get(Board.chainMap.get(stone)).size();
    }

}
