import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

public class Day06Simple {
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

            int rows = map.length;
            int cols = map[0].length;

            int srow = 0;
            int scol = 0;
            int soffset = 0;

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (map[r][c] == GUARD) {
                        srow = r;
                        scol = c;
                    }
                }
            }

            int row = srow;
            int col = scol;
            int offset = soffset;
            var visited = new HashSet<Position>();
            while (true) {
                visited.add(new Position(row, col));
                var o = OFFSETS[offset];
                int nr = row + o[0];
                int nc = col + o[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    break;
                }
                if (map[nr][nc] == BLOCK) {
                    offset = (offset + 1) % OFFSETS.length;
                } else {
                    row = nr;
                    col = nc;
                }
            }
            System.out.println(visited.size());

            int loops = 0;
            var states = new HashSet<State>();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    states.clear();
                    if (map[r][c] == UNBLOCK) {
                        map[r][c] = BLOCK;
                        row = srow;
                        col = scol;
                        offset = soffset;
                        while (true) {
                            if (!states.add(new State(row, col, offset))) {
                                loops++;
                                break;
                            }
                            var o = OFFSETS[offset];
                            int nr = row + o[0];
                            int nc = col + o[1];
                            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                                break;
                            }
                            if (map[nr][nc] == BLOCK) {
                                offset = (offset + 1) % OFFSETS.length;
                            } else {
                                row = nr;
                                col = nc;
                            }
                        }
                        map[r][c] = UNBLOCK;
                    }
                }
            }
            System.out.println(loops);
        }
    }

    record Position (int row, int col) {}
    record State (int row, int col, int offset) {}
}
