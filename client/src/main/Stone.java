package src.main;

import java.awt.Point;

/**
 * Created by 刘俊延 on 2017/4/18.
 */
public class Stone {

    public static final int None = 0;
    public static final int Black = -1;
    public static final int White = 1;

    public int x;
    public int y;
    public int type;

    public Stone(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = this.None;
    }

    public Stone(int x, int y, int t) {
        this.x = x;
        this.y = y;
        this.type = t;
    }

    public void setType(int t) {
        this.type = t;
    }

    public Stone up() {
        if (y == 0) {
            return null;
        }
        return Board.stones[x][y - 1];
    }

    public Stone down() {
        if (y == 18) {
            return null;
        }
        return Board.stones[x][y + 1];
    }

    public Stone left() {
        if (x == 0) {
            return null;
        }
        return Board.stones[x - 1][y];
    }

    public Stone right() {
        if (x == 18) {
            return null;
        }
        return Board.stones[x + 1][y];
    }

}
