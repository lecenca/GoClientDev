/******************************************************************************
 * This file is licensed under the Unlicense. See License.txt for details.
 *
 * Author:
 *   Alinshans (https://github.com/Alinshans/GoClientDev)
 *   zengxingbin (https://github.com/zengxingbin/GoClientDev)
 *   lecenca (https://github.com/lecenca/GoClientDev)
 *
 * Copyright (c) 2017. All rights reserved.
 *****************************************************************************/

package src.main;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Core {

    private static boolean hasLiberty;
    private static boolean hasKilled;
    private static boolean hasKo;

    public static int action(int x, int y, int color) {
        Stone up = Board.stones[x][y].up();
        Stone down = Board.stones[x][y].down();
        Stone left = Board.stones[x][y].left();
        Stone right = Board.stones[x][y].right();
        hasLiberty = false;
        hasKilled = false;
        hasKo = false;
        if (up != null) {
            actionWith(x, y, up, color);
        }
        if (down != null) {
            actionWith(x, y, down, color);
        }
        if (left != null) {
            actionWith(x, y, left, color);
        }
        if (right != null) {
            actionWith(x, y, right, color);
        }
        if (hasKo) {
            return Type.Action.INVALID;
        }
        if (hasKilled) {
            SetPossibleKo(x, y);
            return Type.Action.KILL;
        }
        return hasLiberty ? Type.Action.PLACE : Type.Action.INVALID;
    }

    public static ArrayList<Number> scoring() {
        double blackEyes = 0;
        double whiteEyes = 0;
        Set<Point> blackLiberty = new HashSet<>();
        Set<Point> whiteLiberty = new HashSet<>();
        for (int chain : Board.stonesMap.keySet()) {
            if (Board.stonesMap.get(chain).size() != 0) {
                for (Stone stone : Board.stonesMap.get(chain)) {
                    if (stone.color == Stone.Black) {
                        blackEyes += (double) Board.stonesMap.get(chain).size();
                        for (Point p : Board.libertyMap.get(chain)) {
                            blackLiberty.add(p);
                        }
                    } else if (stone.color == Stone.White) {
                        whiteEyes += (double) Board.stonesMap.get(chain).size();
                        for (Point p : Board.libertyMap.get(chain)) {
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
        result.add(whiteEyes + (double) whiteLiberty.size() - common);
        result.add(blackEyes + (double) blackLiberty.size() - common);
        return result;
    }

    public static void SetPossibleKo(int x, int y) {
        if (Board.dead.size() == 1) {
            for (int chain : Board.dead) {
                if (Board.stonesMap.get(chain).size() == 1) {
                    for (Stone stone : Board.stonesMap.get(chain)) {
                        Board.maybeKo[0].x = x;
                        Board.maybeKo[0].y = y;
                        Board.maybeKo[0].step = Board.step;
                        Board.maybeKo[0].color = -stone.color;
                        Board.maybeKo[1] = stone;
                    }
                    break;
                }
                break;
            }
        }
    }

    private static void actionWith(int x, int y, Stone stone, int color) {
        if (stone.color == Stone.None || (stone.color == color && liberty(stone) > 1)) {
            hasLiberty = true;
        } else if (stone.color == -color && liberty(stone) == 1) {
            if (!isKo(x, y, stone)) {
                hasKilled = true;
                Board.dead.add(Board.chainMap.get(stone));
            }
        }
    }

    private static int liberty(Stone stone) {
        return Board.libertyMap.get(Board.chainMap.get(stone)).size();
    }

    private static boolean isKo(int x, int y, Stone stone) {
        if (x == Board.maybeKo[1].x && y == Board.maybeKo[1].y
                && stone.x == Board.maybeKo[0].x && stone.y == Board.maybeKo[0].y
                && stone.color == Board.maybeKo[0].color
                && stone.step == Board.step - 1
                && Board.stonesMap.get(Board.chainMap.get(stone)).size() == 1) {
            hasKo = true;
            return true;
        }
        return false;
    }

}
