import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.Lines;
import util.Numbers;
import util.Terminal;

public class Day14 {
    private static final int ROWS = 103;
    private static final int COLS = 101;

    public static void main(String[] args) throws Exception {
        var terminal = Terminal.get();
        var input = Path.of("input14.txt");

        var robots = Lines.asStrings(input).stream().map(Robot::of).toList();

        var part1 = part1(robots);
        var part2 = part2(robots);

        terminal.println(part1);
        terminal.println(part2);
    }

    private static long part1(List<Robot> robots) {
        int qcols = (COLS - 1) >> 1;
        int qrows = (ROWS - 1) >> 1;
        var qs = new int[4];
        for (var robot : robots) {
            var p = robot.move(100);
            if (p.x() == qcols || p.y() == qrows) {
                continue;
            }
            var qc = p.x() > qcols ? 1 : 0;
            var qr = p.y() > qrows ? 2 : 0;
            var q = qc + qr;
            qs[q]++;
        }
        var factor = 1L;
        for (var q : qs) {
            factor *= q;
        }
        return factor;
    }

    private static long part2(List<Robot> robots) {
        int t = 0;
        while (t < 100_000) {
            t++;
            var positions = new HashSet<Position>();
            for (var robot : robots) {
                positions.add(robot.move(t));
            }
            if (candidate(positions, robots)) {
                return t;
            }
        }
        return 0;
    }

    private static boolean candidate(Set<Position> positions, List<Robot> robots) {
        var visited = new HashSet<Position>();
        for (var p : positions) {
            int count = dfs(positions, p, visited);
            if (count >= robots.size() / 3) {
                return true;
            }
        }
        return false;
    }

    private static int dfs(Set<Position> positions, Position p, Set<Position> visited) {
        if (!positions.contains(p) || visited.contains(p)) {
            return 0;
        }
        visited.add(p);
        int count = 1;
        count += dfs(positions, p.add(-1, 0), visited);
        count += dfs(positions, p.add(1, 0), visited);
        count += dfs(positions, p.add(0, 1), visited);
        count += dfs(positions, p.add(0, -1), visited);
        return count;
    }

    record Position(int x, int y) {
        Position add(int x, int y) {
            return new Position(this.x + x, this.y + y);
        }
    }

    private static class Robot {
        private Position p;

        private Position v;

        private Robot(Position p, Position v) {
            this.p = p;
            this.v = v;
        }

        public static Robot of(String line) {
            var v = Numbers.asIntStream(line).toArray();
            return new Robot(new Position( v[0], v[1] ), new Position(v[2], v[3]));
        }

        private Position move(int t) {
            var x = mod(p.x() + t * v.x(), COLS);
            var y = mod(p.y() + t * v.y(), ROWS);
            return new Position(x, y);
        }

        private static int mod(int value, int modulo) {
            var r = value % modulo;
            return r < 0 ? r + modulo : r;
        }
    }
}
