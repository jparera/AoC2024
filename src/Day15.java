import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.Lines;
import util.Terminal;

public class Day15 {
    private static final char BOX = 'O';

    private static final char EMPTY = '.';

    private static final char ROBOT = '@';

    private static final char WALL = '#';

    private static final char L_BIG_BOX = '[';

    private static final char R_BIG_BOX = ']';

    public static void main(String[] args) throws IOException {
        var terminal = Terminal.get();
        var input = Path.of("test.txt");

        terminal.println(part1(input));
        terminal.println(part2(input));
    }

    private static long part1(Path input) throws IOException {
        var blocks = Lines.asBlocks(input);
        var map = blocks.get(0).stream().map(String::toCharArray).toArray(char[][]::new);
        var moves = blocks.get(1).stream().reduce("", String::concat);

        var rows = map.length;
        var cols = map[0].length;

        Position p = null;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c] == ROBOT) {
                    p = new Position(r, c);
                    map[r][c] = EMPTY;
                }
            }
        }

        for (char move : moves.toCharArray()) {
            var offset = offset(move);
            var np = p.add(offset);

            if (map[np.row()][np.col()] == EMPTY) {
                p = np;
            } else if (map[np.row()][np.col()] == BOX) {
                moveBoxes(np, offset, map);
                if (map[np.row()][np.col()] == EMPTY) {
                    p = np;
                }
            }
        }
        var coords = 0L;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c] == BOX) {
                    coords += 100 * r + c;
                }
            }
        }
        return coords;
    }

    private static void moveBoxes(Position box, Offset offset, char[][] map) {
        int r = box.row();
        int c = box.col();
        while (map[r][c] == BOX) {
            r += offset.row();
            c += offset.col();
        }
        if (map[r][c] != EMPTY) {
            return;
        }
        while (r != box.row() || c != box.col()) {
            int pr = r - offset.row();
            int pc = c - offset.col();
            map[r][c] = map[pr][pc];
            r = pr;
            c = pc;
        }
        map[r][c] = EMPTY;
    }

    private static long part2(Path input) throws IOException {
        var blocks = Lines.asBlocks(input);
        var smap = blocks.get(0).stream().map(String::toCharArray).toArray(char[][]::new);
        var moves = blocks.get(1).stream().reduce("", String::concat);

        Position p = null;
        var map = new char[smap.length][smap[0].length << 1];
        for (int r = 0; r < smap.length; r++) {
            for (int c = 0, bc = 0; c < smap[0].length; c++, bc += 2) {
                if (smap[r][c] == ROBOT) {
                    map[r][bc] = EMPTY;
                    map[r][bc + 1] = EMPTY;
                    p = new Position(r, bc);
                } else if (smap[r][c] == BOX) {
                    map[r][bc] = L_BIG_BOX;
                    map[r][bc + 1] = R_BIG_BOX;
                } else {
                    map[r][bc] = smap[r][c];
                    map[r][bc + 1] = smap[r][c];
                }
            }
        }

        var rows = map.length;
        var cols = map[0].length;
        for (char move : moves.toCharArray()) {
            var offset = offset(move);
            var np = p.add(offset);
            if (map[np.row()][np.col()] == EMPTY) {
                p = np;
            } else if (isBigBox(np, map)) {
                moveBigBoxes(np, offset, map);
                if (map[np.row()][np.col()] == EMPTY) {
                    p = np;
                }
            }
        }

        var coords = 0L;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c] == L_BIG_BOX) {
                    coords += 100 * r + c;
                }
            }
        }
        return coords;
    }

    private static void moveBigBoxes(Position box, Offset offset, char[][] map) {
        if (Offset.L.equals(offset) && Offset.R.equals(offset)) {
            int r = box.row();
            int c = box.col();
            while (map[r][c] == L_BIG_BOX || map[r][c] == R_BIG_BOX) {
                c += offset.col();
            }
            if (map[r][c] != EMPTY) {
                return;
            }
            while (c != box.col()) {
                var nc = c - offset.col();
                map[r][c] = map[r][nc];
                c = nc;
            }
            map[r][c] = EMPTY;
        } else {
            var visited = new HashMap<Position, Character>();
            var stack = new ArrayDeque<Position>();

            var parts = bigBoxParts(box, map);
            for (var part : parts) {
                visited.put(part, map[part.row()][part.col()]);
                stack.push(part);
            }

            boolean wall = false;
            wall: while (!stack.isEmpty()) {
                var p = stack.pop();
                parts = bigBoxParts(p, map);
                for (var part : parts) {
                    visited.put(part, map[part.row()][part.col()]);
                    var next = part.add(offset);
                    if (map[next.row()][next.col()] == WALL) {
                        wall = true;
                        break wall;
                    }
                    if (!visited.containsKey(next) && isBigBox(next, map)) {
                        visited.put(next, map[next.row()][next.col()]);
                        stack.add(next);
                    }
                }
            }

            if (!wall) {
                for (var p : visited.keySet()) {
                    map[p.row()][p.col()] = EMPTY;
                }
                for (var e : visited.entrySet()) {
                    var p = e.getKey();
                    map[p.row() + offset.row()][p.col() + offset.col()] = e.getValue();
                }
            }
        }
    }

    private static List<Position> bigBoxParts(Position p, char[][] map) {
        var parts = new ArrayList<Position>();
        if (map[p.row()][p.col()] == L_BIG_BOX) {
            parts.add(p);
            parts.add(p.add(Offset.R));
        } else {
            parts.add(p.add(Offset.L));
            parts.add(p);
        }
        return parts;
    }

    private static boolean isBigBox(Position p, char[][] map) {
        var c = map[p.row()][p.col()];
        return c == L_BIG_BOX || c == R_BIG_BOX;
    }

    private static Offset offset(char c) {
        return switch (c) {
            case '^' -> Offset.U;
            case 'v' -> Offset.D;
            case '<' -> Offset.L;
            case '>' -> Offset.R;
            default -> throw new IllegalArgumentException();
        };
    }

    record Position(int row, int col) {
        public Position add(Offset offset) {
            return new Position(row + offset.row(), col + offset.col());
        }
    }

    record Offset(int row, int col) {
        static Offset U = new Offset(-1, 0);
        static Offset D = new Offset(1, 0);
        static Offset L = new Offset(0, -1);
        static Offset R = new Offset(0, 1);
    }
}
