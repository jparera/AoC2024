import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashSet;

import util.Lines;
import util.Terminal;

public class Day18 {
    private static final int SIZE = 71;

    private static final int PART1_BYTES = 1024;

    public static void main(String[] args) throws Exception {
        var terminal = Terminal.get();
        var input = Path.of("input18.txt");

        var bytes = Lines.asIntMatrix(input);

        var grid = new HashSet<Point>();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                grid.add(new Point(x, y));
            }
        }

        for (int i = 0; i < PART1_BYTES - 1; i++) {
            grid.remove(new Point(bytes[i][0], bytes[i][1]));
        }

        var start = new Point(0, 0);
        var end = new Point(SIZE - 1, SIZE - 1);

        var part1 = 0;
        Point part2 = null;
        for (int i = PART1_BYTES - 1; i < bytes.length; i++) {
            var flake = new Point(bytes[i][0], bytes[i][1]);
            grid.remove(flake);

            var visited = new HashSet<Point>();
            var queue = new ArrayDeque<Point>();
            queue.offer(start);

            var distance = 0;
            var found = false;
            end: while (!queue.isEmpty()) {
                var len = queue.size();
                while (len-- > 0) {
                    var point = queue.poll();
                    if (visited.contains(point)) {
                        continue;
                    }
                    visited.add(point);
                    if (end.equals(point)) {
                        if (i < PART1_BYTES) {
                            part1 = distance;
                        }
                        found = true;
                        break end;
                    }
                    for (var offset : OFFSETS) {
                        var next = point.add(offset);
                        if (grid.contains(next)) {
                            queue.add(next);
                        }
                    }
                }
                distance++;
            }
            if (!found) {
                part2 = flake;
                break;
            }
        }

        terminal.println(part1);
        terminal.println(part2);
    }

    private static final Point[] OFFSETS = {
            new Point(-1, 0), new Point(1, 0),
            new Point(0, -1), new Point(0, 1),
    };

    record Point(int x, int y) {
        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }

        public Point add(Point p) {
            return new Point(x + p.x, y + p.y);
        }
    };
}
