import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;

import util.Lines;
import util.Terminal;

public class Day20 {
    private static final char WALL = '#';

    private static final char START = 'S';

    public static void main(String[] args) throws Exception {
        var terminal = Terminal.get();
        var input = Path.of("input20.txt");
        var map = Lines.asCharMatrix(input);

        var distances = new HashMap<Point, Integer>();

        int rows = map.length;
        int cols = map[0].length;

        Point start = null;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c] != WALL) {
                    var p = new Point(r, c);
                    if (map[r][c] == START) {
                        start = p;
                    }
                    distances.put(p, 0);
                }
            }
        }

        var path = distances.keySet();
        var v = new HashSet<Point>();
        var s = new ArrayDeque<Point>();
        s.push(start);
        v.add(start);
        int distance = 0;
        while (!s.isEmpty()) {
            var c = s.pop();
            distances.put(c, distance);
            for (var offset : OFFSETS) {
                var n = c.add(offset);
                if (!v.contains(n) && path.contains(n)) {
                    v.add(n);
                    s.push(n);
                    break;
                }
            }
            distance++;
        }

        int part1 = 0;
        int part2 = 0;
        for (var src : path) {
            for (var dst : path) {
                var d = src.distance(dst);
                if (d < 2) {
                    continue;
                }
                if (d == 2) {
                    int saved = distances.get(dst) - distances.get(src) - 2;
                    if (saved >= 100) {
                        part1++;
                    }
                }
                if (d <= 20) {
                    int saved = distances.get(dst) - distances.get(src) - d;
                    if (saved >= 100) {
                        part2++;
                    }
                }
            }
        }

        terminal.println(part1);
        terminal.println(part2);
    }

    private static final Point[] OFFSETS = {
            new Point(-1, 0), new Point(0, 1), new Point(1, 0), new Point(0, -1),
    };

    record Point(int row, int col) {
        Point add(Point p) {
            return new Point(row + p.row, col + p.col);
        }

        int distance(Point o) {
            return Math.abs(row - o.row) + Math.abs(col - o.col);
        }
    }
}
