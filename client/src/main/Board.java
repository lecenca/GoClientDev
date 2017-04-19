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
    public static ArrayList<Integer> killed = new ArrayList<>();

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
    public int action(Point p, int color) {
        return action(p.x, p.y, color);
    }

    public int action(int x, int y, int color) {
        if (stones[x][y].type != Stone.None) {
            return Action.INVALID;
        }
        return core.action(x,y,color);
    }

    // Adds a stone in color on the Point (x, y)
    public void add(int x, int y, int color) {
        stones[x][y].setType(color);
        initStone(stones[x][y]);
        update(stones[x][y]);
    }

    // Removes the stone in the Point (x, y)
    public void remove() {
        for(int chain : killed){
            ArrayList<Stone> ss = stoneMap.get(chain);
            for(Stone s : ss){
                s.setType(Stone.None);
                if(s.up() != null){
                    extendLiberty(chainMap.get(s.up()),new Point(s.x,s.y));
                }
                if(s.down() != null){
                    extendLiberty(chainMap.get(s.down()),new Point(s.x,s.y));
                }
                if(s.left() != null){
                    extendLiberty(chainMap.get(s.left()),new Point(s.x,s.y));
                }
                if(s.right() != null){
                    extendLiberty(chainMap.get(s.right()),new Point(s.x,s.y));
                }
                stoneMap.remove(chain);
            }
            ss.clear();
            used[chain] = false;
        }
        killed.clear();
    }

    private void initStone(Stone stone) {
        int chain = 0;
        while (used[chain]) {
            ++chain;
        }
        used[chain] = true;
        chainMap.put(stone, chain);
        ArrayList<Stone> list = new ArrayList<>();
        list.add(stone);
        stoneMap.put(chain, list);
        liberty.put(chain,new TreeSet<Point>(new PointComparator()));
        if(stone.up() != null && stone.up().type == Stone.None){
            extendLiberty(chain, new Point(stone.x,stone.y - 1));
        }
        if(stone.down() != null && stone.down().type == Stone.None){
            extendLiberty(chain, new Point(stone.x,stone.y + 1));
        }
        if(stone.left() != null && stone.left().type == Stone.None){
            extendLiberty(chain, new Point(stone.x - 1,stone.y));
        }
        if(stone.right() != null && stone.right().type == Stone.None){
            extendLiberty(chain, new Point(stone.x + 1,stone.y));
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
            for (Stone s : oldList) {
                newList.add(s);
            }
            for(Point p : oldLib){
                newLib.add(p);
            }
            oldList.clear();
            oldLib.clear();
            used[oldChain] = false;
        }
    }

    private void reduceLiberty(int chain, Point point){
        liberty.get(chain).remove(point);
    }

    private void extendLiberty(int chain, Point point){
        liberty.get(chain).add(point);
    }
}
