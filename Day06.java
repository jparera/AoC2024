import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

public class Day06 {
    private static final char GUARD = '^';

    private static final char BLOCK = '#';

    private static final char UNBLOCK = '.';

    private static final int[][] OFFSETS = {
            { -1, 0 }, // UP
            { 0, 1 }, // RIGHT
            { 1, 0 }, // DOWN
            { 0, -1 }, // LEFT
    };

    public static void main(String[] args) throws IOException {
        var input = Path.of("input.txt");

        try (var lines = Files.lines(input)) {
            var map = lines.map(String::toCharArray).toArray(char[][]::new);

            var guard = Guard.start(map);
            //
            // Part 1
            //
            var visited = new HashSet<Position>();
            visited.add(guard.position());
            while (guard.move()) {
                visited.add(guard.position());
            }
            System.out.println(visited.size());
            //
            // Part 2
            //
            int loops = 0;
            for (int r = 0; r < map.length; r++) {
                for (int c = 0; c < map[0].length; c++) {
                    if (map[r][c] == UNBLOCK) {
                        map[r][c] = BLOCK;
                        var states = new HashSet<State>();
                        guard.restart();
                        while (guard.move()) {
                            if (!states.add(guard.state())) {
                                loops++;
                                break;
                            }
                        }
                        map[r][c] = UNBLOCK;
                    }
                }
            }

            System.out.println(loops);
        }
    }

    record Position(int row, int col) {
    }

    record State(int row, int col, int offset) {
    }

    static class Guard {
        private int row;

        private int col;

        private int offset;

        private State start;

        private char[][] map;

        Guard(int row, int col, char[][] map, int offset) {
            this.row = row;
            this.col = col;
            this.offset = offset;
            this.start = new State(row, col, offset);
            this.map = map;
        }

        public static Guard start(char[][] map) {
            for (int r = 0; r < map.length; r++) {
                for (int c = 0; c < map[0].length; c++) {
                    if (map[r][c] == GUARD) {
                        return new Guard(r, c, map, 0);
                    }
                }
            }
            throw new IllegalArgumentException();
        }

        public Position position() {
            return new Position(row, col);
        }

        public State state() {
            return new State(row, col, offset);
        }

        public void restart() {
            this.row = start.row();
            this.col = start.col();
            this.offset = start.offset();
        }

        public boolean move() {
            var o = OFFSETS[offset];
            var nr = row + o[0];
            var nc = col + o[1];
            if (nr < 0 || nr >= map.length || nc < 0 || nc >= map[0].length) {
                return false;
            }
            if (map[nr][nc] == BLOCK) {
                offset = (offset + 1) % OFFSETS.length;
            } else {
                row = nr;
                col = nc;
            }
            return true;
        }
    }
}
