package src.main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Core {

    private static boolean hasLiberty;
    private static boolean hasKilled;
    private static boolean hasKo;

    public static int action(Board board, int x, int y, int color) {
        Stone up = board.stones[x][y].up();
        Stone down = board.stones[x][y].down();
        Stone left = board.stones[x][y].left();
        Stone right = board.stones[x][y].right();
        hasLiberty = false;
        hasKilled = false;
        hasKo = false;
        if (up != null) {
            actionWith(board, x, y, up, color);
        }
        if (down != null) {
            actionWith(board, x, y, down, color);
        }
        if (left != null) {
            actionWith(board, x, y, left, color);
        }
        if (right != null) {
            actionWith(board, x, y, right, color);
        }
        if (hasKo) {
            return Type.Action.INVALID;
        }
        return hasKilled ? Type.Action.KILL : hasLiberty ? Type.Action.PLACE : Type.Action.INVALID;
    }

    public static ArrayList<Number> scoring(Board board) {
        double blackEyes = 0;
        double whiteEyes = 0;
        Set<Point> blackLiberty = new HashSet<>();
        Set<Point> whiteLiberty = new HashSet<>();
        for (int chain : board.stoneMap.keySet()) {
            if (board.stoneMap.get(chain).size() != 0) {
                for (Stone stone : board.stoneMap.get(chain)) {
                    if (stone.color == Stone.Black) {
                        blackEyes += (double) board.stoneMap.get(chain).size();
                        for (Point p : board.liberty.get(chain)) {
                            blackLiberty.add(p);
                        }
                    } else if (stone.color == Stone.White) {
                        whiteEyes += (double) board.stoneMap.get(chain).size();
                        for (Point p : board.liberty.get(chain)) {
                            whiteLiberty.add(p);
                        }
                    }
                    break;
                }
            }
        }
        double common = 0;
        for (Point p : blackLiberty) {
            if (whiteLiberty.contains(p)) {
                common += 0.5;
            }
        }
        ArrayList<Number> result = new ArrayList<>();
        result.add(blackEyes + (double) blackLiberty.size() - common);
        result.add(whiteEyes + (double) whiteLiberty.size() - common);
        return result;
    }

    private static void actionWith(Board board, int x, int y, Stone stone, int color) {
        if (stone.color == Stone.None || (stone.color == color && liberty(board, stone) > 1)) {
            hasLiberty = true;
        } else if (stone.color == -color && liberty(board, stone) == 1) {
            if (!isKo(board, x, y, stone)) {
                hasKilled = true;
                board.killed.add(board.chainMap.get(stone));
            }
        }
    }

    private static boolean isKo(Board board, int x, int y, Stone stone) {
        if (x == board.maybeKo[1].x && y == board.maybeKo[1].y
                && stone.x == board.maybeKo[0].x && stone.y == board.maybeKo[0].y
                && stone.color == board.maybeKo[0].color
                && board.stoneMap.get(board.chainMap.get(stone)).size() == 1) {
            hasKo = true;
            return true;
        }
        return false;
    }

    public static int liberty(Board board, Stone stone) {
        int chain = board.chainMap.get(stone);
        System.out.println("Point (" + stone.x + "," + stone.y + ") in chain " + chain + " has liberty " + board.liberty.get(chain).size());
        return board.liberty.get(board.chainMap.get(stone)).size();
    }

}
