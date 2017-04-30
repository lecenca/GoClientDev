package src.main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Board {

    public static Stone[][] stones = new Stone[19][19];
    public static Map<Stone, Integer> chainMap = new HashMap<>();
    public static Map<Integer, HashSet<Stone>> stonesMap = new HashMap<>();
    public static Map<Integer, HashSet<Point>> libertyMap = new HashMap<>();
    public static HashSet<Integer> dead = new HashSet<>();
    public static Stone[] maybeKo = new Stone[2];

    private static int step;
    private static boolean[] used = new boolean[361];

    public Board() {
        for (int i = 0; i < 19; ++i) {
            for (int j = 0; j < 19; ++j) {
                stones[i][j] = new Stone(i, j);
            }
        }
        maybeKo[0] = new Stone(-1, -1);
        maybeKo[1] = new Stone(-1, -1);
    }

    public static void clear() {
        for (int i = 0; i < 19; ++i) {
            for (int j = 0; j < 19; ++j) {
                stones[i][j].color = Stone.None;
            }
        }
        chainMap.clear();
        stonesMap.clear();
        libertyMap.clear();
        dead.clear();
    }

    // Checks if the stones in color can be placed in the Point p.
    public int action(Point p, int color) {
        return action(p.x, p.y, color);
    }

    public int action(int x, int y, int color) {
        if (stones[x][y].color != Stone.None) {
            return Type.Action.INVALID;
        }
        return Core.action(this, x, y, color);
    }

    // Adds a stone at the Point (x, y)
    public void add(int x, int y, int color) {
        stones[x][y].color = color;
        stones[x][y].step = step;
        step += 2;
        initStone(stones[x][y]);
        update(stones[x][y]);
        System.out.println("Board add stone(" + x + "," + y + ") in chain " + chainMap.get(stones[x][y])
                + " has libertyMap " + Core.liberty(this, stones[x][y]));
    }

    // Removes the stones that were dead.
    public void remove() {
        for (int chain : dead) {
            HashSet<Stone> ss = stonesMap.get(chain);
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
                s.color = Stone.None;
                chainMap.remove(s);
            }
            stonesMap.remove(chain);
            libertyMap.remove(chain);
            used[chain] = false;
        }
        dead.clear();
    }

    // Adds the stones that were dead in the dead.
    public void addKilled(ArrayList<HashMap> list) {
        for (HashMap stone : list) {
            dead.add(chainMap.get(stones[(int) stone.get("x")][(int) stone.get("y")]));
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

    // Initializes a new stone for chainMap, stonesMap and libertyMap.
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
        stonesMap.put(chain, stonesList);

        // Initializes the libertyMap of the stone.
        libertyMap.put(chain, new HashSet<>());
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
            connect(stone, neighbor);
        }
    }

    private void connect(Stone stone, Stone neighbor) {
        int oldChain = chainMap.get(neighbor);
        int newChain = chainMap.get(stone);
        if (newChain != oldChain) {
            HashSet<Stone> oldStoneSet = stonesMap.get(oldChain);
            HashSet<Stone> newStoneSet = stonesMap.get(newChain);
            HashSet<Point> oldPointSet = libertyMap.get(oldChain);
            HashSet<Point> newPointSet = libertyMap.get(newChain);
            for (Stone s : oldStoneSet) {
                chainMap.put(s, newChain);
                newStoneSet.add(s);
            }
            for (Point p : oldPointSet) {
                newPointSet.add(p);
            }
            stonesMap.remove(oldChain);
            libertyMap.remove(oldChain);
            used[oldChain] = false;
        }
    }

    private void reduceLiberty(int chain, Point point) {
        libertyMap.get(chain).remove(point);
    }

    private void extendLiberty(int chain, Point point) {
        libertyMap.get(chain).add(point);
    }
}
