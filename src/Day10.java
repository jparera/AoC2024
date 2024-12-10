import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import util.Lines;
import util.Terminal;

public class Day10 {
    public static void main(String[] args) throws Exception {
        var terminal = Terminal.get();
        var input = Path.of("input10.txt");

        var map = Lines.asCharMatrix(input);

        var part1 = 0;
        var part2 = 0;

        int rows = map.length;
        int cols = map[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c] == '0') {
                    var peaks = new HashSet<Position>();
                    part2 += dfs(map, r, c, -1, peaks);
                    part1 += peaks.size();
                }
            }
        }

        terminal.printf("%d\n", part1);
        terminal.printf("%d\n", part2);
    }

    private static int dfs(char[][] map, int r, int c, int previous, Set<Position> peaks) {
        if (r < 0 || r >= map.length || c < 0 || c >= map[0].length) {
            return 0;
        }
        int current = map[r][c] - '0';
        if (current - previous != 1) {
            return 0;
        }
        if (current == 9) {
            peaks.add(new Position(r, c));
            return 1;
        }
        int count = 0;
        count += dfs(map, r - 1, c, current, peaks);
        count += dfs(map, r + 1, c, current, peaks);
        count += dfs(map, r, c - 1, current, peaks);
        count += dfs(map, r, c + 1, current, peaks);
        return count;
    }

    record Position(int row, int col) {
    }
}
