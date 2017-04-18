package src.main;

import java.awt.Point;
import java.util.*;

/**
 * Created by touhoudoge on 2017/4/16.
 */
public class Board {

    class PointComparator implements Comparator{
        @Override
        public int compare(Object o1, Object o2) {
            Point p1 = (Point)o1;
            Point p2 = (Point)o2;
            int res = Integer.compare(p1.x, p2.x);
            if(res == 0){
                res = Integer.compare(p1.y,p2.y);
            }
            return res;
        }
    }

    public static Stone[][] stones = new Stone[19][19];
    public static Map<Stone, Integer> chainMap = new HashMap<>();
    public static Map<Integer, ArrayList<Stone>> stoneMap = new HashMap<>();
    public static Map<Integer, TreeSet<Point>> liberty = new TreeMap<>();

    private static boolean[] used = new boolean[361];

    private Core core = new Core();

    public Board() {
        for (int i = 0; i < 19; ++i) {
            for (int j = 0; j < 19; ++j) {
                stones[i][j] = new Stone(i, j);
            }
        }
    }

    public Stone get(Point p) {
        return get(p.x, p.y);
    }

    public Stone get(int x, int y) {
        return stones[x][y];
    }

    // Checks if the stones in color can be placed in the Point p.
    public boolean check(Point p, int color) {
        return check(p.x, p.y, color);
    }

    public boolean check(int x, int y, int color) {
        if (stones[x][y].getType() != Stone.None) {
            return false;
        }
        if(!core.hasLiberty(x, y, color)){
            return false;
        }
        return true;
    }

    // Adds a stone in color on the Point (x, y)
    public void add(int x, int y, int color) {
        stones[x][y].setType(color);
        initChainId(stones[x][y]);
        update(stones[x][y]);
        System.out.println("Point:("+x+","+y+") chain "+chainMap.get(stones[x][y])+" has liberty "+core.liberty(stones[x][y]));
    }

    // Removes the stone in the Point (x, y)
    public void remove(int x, int y) {
        stones[x][y].setType(Stone.None);
    }

    private void initChainId(Stone stone) {
        int i = 0;
        while (used[i]) {
            ++i;
        }
        used[i] = true;
        chainMap.put(stone, i);
        stoneMap.put(i, new ArrayList<>());
        liberty.put(i,new TreeSet<Point>(new PointComparator()));
        if(stone.up() != null && stone.up().type == Stone.None){
            extendLiberty(i, new Point(stone.x,stone.y - 1));
        }
        if(stone.down() != null && stone.down().type == Stone.None){
            extendLiberty(i, new Point(stone.x,stone.y + 1));
        }
        if(stone.left() != null && stone.left().type == Stone.None){
            extendLiberty(i, new Point(stone.x - 1,stone.y));
        }
        if(stone.right() != null && stone.right().type == Stone.None){
            extendLiberty(i, new Point(stone.x + 1,stone.y));
        }
    }

    private void update(Stone stone) {
        if(stone.up() != null){
            if(stone.up().type != Stone.None){
                reduceLiberty(chainMap.get(stone.up()),new Point(stone.x,stone.y));
            }
            if(stone.type == stone.up().type){
                link(stone, stone.up());
            }
        }
        if(stone.down() != null){
            if(stone.down().type != Stone.None){
                reduceLiberty(chainMap.get(stone.down()),new Point(stone.x,stone.y));
            }
            if(stone.type == stone.down().type){
                link(stone, stone.down());
            }
        }
        if(stone.left() != null){
            if(stone.left().type != Stone.None){
                reduceLiberty(chainMap.get(stone.left()),new Point(stone.x,stone.y));
            }
            if(stone.type == stone.left().type){
                link(stone, stone.left());
            }
        }
        if(stone.right() != null){
            if(stone.right().type != Stone.None){
                reduceLiberty(chainMap.get(stone.right()),new Point(stone.x,stone.y));
            }
            if(stone.type == stone.right().type){
                link(stone, stone.right());
            }
        }
    }

    private void link(Stone stone, Stone neighbor) {
        int newChain = chainMap.get(stone);
        int oldChain = chainMap.get(neighbor);
        if (newChain != oldChain) {
            ArrayList<Stone> oldList = stoneMap.get(oldChain);
            ArrayList<Stone> newList = stoneMap.get(newChain);
            TreeSet<Point> oldLib = liberty.get(oldChain);
            TreeSet<Point> newLib = liberty.get(newChain);
            newList.add(stone);
            for (Stone s : oldList) {
                newList.add(s);
            }
            for(Point p : oldLib){
                newLib.add(p);
            }
            oldList.clear();
            oldLib.clear();
        }
    }

    private void reduceLiberty(int chain, Point point){
        liberty.get(chain).remove(point);
    }

    private void extendLiberty(int chain, Point point){
        liberty.get(chain).add(point);
    }
}
