package src.main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by touhoudoge on 2017/4/16.
 */
public class Board {

    public static Stone[][] stones = new Stone[19][19];
    public static Map<Stone, Integer> chainMap = new HashMap<>();
    public static Map<Integer, HashSet<Stone>> stoneMap = new HashMap<>();
    public static Map<Integer, HashSet<Point>> liberty = new HashMap<>();
    public static HashSet<Integer> killed = new HashSet<>();
    public static Stone[] maybeKo = new Stone[2];

    private static boolean[] used = new boolean[361];

    private Core core = new Core();

    public Board() {
        for (int i = 0; i < 19; ++i) {
            for (int j = 0; j < 19; ++j) {
                stones[i][j] = new Stone(i, j);
            }
        }
        maybeKo[0] = new Stone(-1, -1);
        maybeKo[1] = new Stone(-1, -1);
    }

    public static void reset() {
        for (int i = 0; i < 19; ++i) {
            for (int j = 0; j < 19; ++j) {
                stones[i][j] = new Stone(i, j);
            }
        }
        chainMap.clear();
        stoneMap.clear();
        liberty.clear();
        killed.clear();
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
        if (stones[x][y].color != Stone.None) {
            return Action.INVALID;
        }
        return Core.action(x, y, color);
    }

    // Adds a stone at the Point (x, y)
    public void add(int x, int y, int color) {
        stones[x][y].setColor(color);
        initStone(stones[x][y]);
        update(stones[x][y]);
        System.out.println("Board add stone(" + x + "," + y + ") in chain " + chainMap.get(stones[x][y])
                + " has liberty " + Core.liberty(stones[x][y]));
    }

    // Removes the stones that were killed.
    public void remove() {
        for (int chain : killed) {
            HashSet<Stone> ss = stoneMap.get(chain);
            for (Stone s : ss) {
                System.out.println("Board remove stone(" + s.x + "," + s.y + ") in chain " + chainMap.get(s));
                if (s.up() != null && s.up().color == -s.color) {
                    extendLiberty(chainMap.get(s.up()), new Point(s.x, s.y));
                }
                if (s.down() != null && s.down().color == -s.color) {
                    extendLiberty(chainMap.get(s.down()), new Point(s.x, s.y));
                }
                if (s.left() != null && s.left().color == -s.color) {
                    extendLiberty(chainMap.get(s.left()), new Point(s.x, s.y));
                }
                if (s.right() != null && s.right().color == -s.color) {
                    extendLiberty(chainMap.get(s.right()), new Point(s.x, s.y));
                }
                s.setColor(Stone.None);
                chainMap.remove(s);
            }
            stoneMap.remove(chain);
            liberty.remove(chain);
            used[chain] = false;
        }
        killed.clear();
    }

    // Adds the stones that were killed in the killed.
    public void addKilled(ArrayList<HashMap> list) {
        for (HashMap stone : list) {
            killed.add(chainMap.get(stones[(int) stone.get("x")][(int) stone.get("y")]));
        }
    }

    // Sets some point that look like ko.
    public void setKoPoint(int x1, int y1, int color, int x2, int y2) {
        maybeKo[0].x = x1;
        maybeKo[0].y = y1;
        maybeKo[0].color = color;
        maybeKo[1].x = x2;
        maybeKo[1].y = y2;
        maybeKo[1].color = -color;
    }

    // Initializes a new stone for chainMap, stoneMap and liberty.
    private void initStone(Stone stone) {
        // Initializes the chain where the stone is.
        int chain = 0;
        while (used[chain]) {
            ++chain;
        }
        used[chain] = true;
        chainMap.put(stone, chain);

        // Initializes the stonesList of the chain where the stone is.
        HashSet<Stone> stonesList = new HashSet<>();
        stonesList.add(stone);
        stoneMap.put(chain, stonesList);

        // Initializes the liberty of the stone.
        liberty.put(chain, new HashSet<>());
        if (stone.up() != null && stone.up().color == Stone.None) {
            extendLiberty(chain, new Point(stone.x, stone.y - 1));
        }
        if (stone.down() != null && stone.down().color == Stone.None) {
            extendLiberty(chain, new Point(stone.x, stone.y + 1));
        }
        if (stone.left() != null && stone.left().color == Stone.None) {
            extendLiberty(chain, new Point(stone.x - 1, stone.y));
        }
        if (stone.right() != null && stone.right().color == Stone.None) {
            extendLiberty(chain, new Point(stone.x + 1, stone.y));
        }
    }

    // Updates the informations of the nerghbor of the stone.
    private void update(Stone stone) {
        if (stone.up() != null && stone.up().color != Stone.None) {
            updateNeighbor(stone, stone.up());
        }
        if (stone.down() != null && stone.down().color != Stone.None) {
            updateNeighbor(stone, stone.down());
        }
        if (stone.left() != null && stone.left().color != Stone.None) {
            updateNeighbor(stone, stone.left());
        }
        if (stone.right() != null && stone.right().color != Stone.None) {
            updateNeighbor(stone, stone.right());
        }
    }

    private void updateNeighbor(Stone stone, Stone neighbor) {
        reduceLiberty(chainMap.get(neighbor), new Point(stone.x, stone.y));
        if (stone.color == neighbor.color) {
            link(stone, neighbor);
        }
    }

    private void link(Stone stone, Stone neighbor) {
        int oldChain = chainMap.get(neighbor);
        int newChain = chainMap.get(stone);
        if (newChain != oldChain) {
            HashSet<Stone> oldStoneSet = stoneMap.get(oldChain);
            HashSet<Stone> newStoneSet = stoneMap.get(newChain);
            HashSet<Point> oldPointSet = liberty.get(oldChain);
            HashSet<Point> newPointSet = liberty.get(newChain);
            for (Stone s : oldStoneSet) {
                chainMap.put(s, newChain);
                newStoneSet.add(s);
            }
            for (Point p : oldPointSet) {
                newPointSet.add(p);
            }
            stoneMap.remove(oldChain);
            liberty.remove(oldChain);
            used[oldChain] = false;
        }
    }

    private void reduceLiberty(int chain, Point point) {
        liberty.get(chain).remove(point);
    }

    private void extendLiberty(int chain, Point point) {
        liberty.get(chain).add(point);
    }
}
