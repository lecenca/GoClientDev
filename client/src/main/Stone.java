package src.main;

/**
 * Created by 刘俊延 on 2017/4/17.
 */
public class Stone {

    public static final int Black = -1;
    public static final int White = 1;
    public static final int None = 0;

    private int type;

    public Stone() {
        this.type = this.None;
    }

    public Stone(int t) {
        this.type = t;
    }

    public int getType() {
        return type;
    }

    public void setType(int t) {
        this.type = t;
    }
}
