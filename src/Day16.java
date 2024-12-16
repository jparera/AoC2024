import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import util.Lines;
import util.Terminal;

public class Day16 {
    private static final char START = 'S';

    private static final char END = 'E';

    private static final char WALL = '#';

    public static void main(String[] args) throws Exception {
        var terminal = Terminal.get();
        var input = Path.of("input16.txt");

        var map = Lines.asCharMatrix(input);

        int rows = map.length;
        int cols = map[0].length;

        Vertex start = null;
        Point end = null;
        for (int r = 1; r < rows; r++) {
            for (int c = 1; c < cols; c++) {
                if (map[r][c] == START) {
                    start = new Vertex(new Point(r, c), Direction.E);
                }
                if (map[r][c] == END) {
                    end = new Point(r, c);
                }
            }
        }

        record Frame(Vertex v, int distance) {
        }

        var part1 = Integer.MAX_VALUE;
        var dstart = new HashMap<Vertex, Integer>();
        var dend = new HashMap<Vertex, Integer>();

        var queue = new PriorityQueue<Frame>((l, r) -> Integer.compare(l.distance(), r.distance()));
        queue.offer(new Frame(start, 0));

        while (!queue.isEmpty()) {
            var f = queue.poll();
            var v = f.v();

            if (dstart.containsKey(v)) {
                continue;
            }
            dstart.put(v, f.distance());

            if (v.p().equals(end)) {
                part1 = Math.min(part1, f.distance());
            }

            var vf = v.forward(map);
            if (!vf.isWall(map)) {
                queue.offer(new Frame(vf, f.distance() + 1));
            }
            for (var r : Direction.rotations(v.d())) {
                var vr = new Vertex(v.p(), r);
                queue.offer(new Frame(vr, f.distance() + 1000));
            }
        }

        queue = new PriorityQueue<Frame>((l, r) -> Integer.compare(l.distance(), r.distance()));
        for (var d : Direction.values()) {
            queue.offer(new Frame(new Vertex(end, d), 0));
        }

        while (!queue.isEmpty()) {
            var f = queue.poll();
            var v = f.v();

            if (dend.containsKey(v)) {
                continue;
            }
            dend.put(v, f.distance());

            var vf = v.backward(map);
            if (!vf.isWall(map)) {
                queue.offer(new Frame(vf, f.distance() + 1));
            }
            for (var r : Direction.rotations(v.d())) {
                var vr = new Vertex(v.p(), r);
                queue.offer(new Frame(vr, f.distance() + 1000));
            }
        }

        var optimal = new HashSet<Point>();
        for (int r = 1; r < rows - 1; r++) {
            for (int c = 1; c < cols - 1; c++) {
                for (var d : Direction.values()) {
                    var v = new Vertex(new Point(r, c), d);
                    if (dstart.containsKey(v) && dend.containsKey(v) && dstart.get(v) + dend.get(v) == part1) {
                        optimal.add(v.p());
                    }
                }
            }
        }
        var part2 = optimal.size();

        terminal.println(part1);
        terminal.println(part2);
    }

    enum Direction {
        N(-1, 0),
        E(0, 1),
        S(1, 0),
        W(0, -1);

        private static Set<Direction> ROTATIONS_N = Set.of(E, W);
        private static Set<Direction> ROTATIONS_S = Set.of(W, E);
        private static Set<Direction> ROTATIONS_E = Set.of(S, N);
        private static Set<Direction> ROTATIONS_W = Set.of(N, S);

        private int row;

        private int col;

        Direction(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Direction backward() {
            return Direction.values()[(this.ordinal() + 2) % 4];
        }

        public static Set<Direction> rotations(Direction d) {
            return switch (d) {
                case N -> ROTATIONS_N;
                case S -> ROTATIONS_S;
                case E -> ROTATIONS_E;
                case W -> ROTATIONS_W;
            };
        }
    }

    record Vertex(Point p, Direction d) {
        public String toString() {
            return String.format("(%d, %d, %s)", p.row(), p.col(), d);
        }

        public Vertex forward(char[][] map) {
            return new Vertex(p.move(d), d);
        }

        public Vertex backward(char[][] map) {
            return new Vertex(p.move(d.backward()), d);
        }

        public boolean isWall(char[][] map) {
            return map[p.row()][p.col()] == WALL;
        }
    }

    record Point(int row, int col) {
        public Point move(Direction d) {
            return new Point(row + d.row, col + d.col);
        }

        public String toString() {
            return String.format("(%d, %d)", row, col);
        }
    }
}
