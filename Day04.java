import java.io.IOException;
import java.nio.file.Path;

import util.Lines;

public class Day04 {
    private static final char[] XMAS = "XMAS".toCharArray();

    public static void main(String[] args) throws IOException {
        var path = Path.of("input04.txt");
        var map = Lines.asCharMatrix(path);
        var cols = map.length;
        var rows = map[0].length;
        int part1 = 0;
        int part2 = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                part1 += xmas(r, c, map);
                part2 += crossmas(r, c, map);
            }
        }
        System.out.println(part1);
        System.out.println(part2);
    }

    private static int crossmas(int r, int c, char[][] map) {
        if (map[r][c] != 'A') {
            return 0;
        }
        int count = 0;
        if (is('M', r - 1, c - 1, map) && is('S', r + 1, c + 1, map)) {
            count++;
        } else if (is('S', r - 1, c - 1, map) && is('M', r + 1, c + 1, map)) {
            count++;
        }
        if (is('M', r - 1, c + 1, map) && is('S', r + 1, c - 1, map)) {
            count++;
        } else if (is('S', r - 1, c + 1, map) && is('M', r + 1, c - 1, map)) {
            count++;
        }
        return count == 2 ? 1 : 0;
    }

    private static boolean is(char l, int r, int c, char[][] map) {
        if (r < 0 || r >= map.length || c < 0 || c >= map[0].length) {
            return false;
        }
        return map[r][c] == l;
    }

    private static final int[][] OFFSETS = {
            { -1, -1 }, { -1, 0 }, { -1, 1 },
            { 0, -1 }, { 0, 1 },
            { 1, -1 }, { 1, 0 }, { 1, 1 },
    };

    private static int xmas(int r, int c, char[][] map) {
        if (map[r][c] != XMAS[0]) {
            return 0;
        }
        int count = 0;
        for (var offset : OFFSETS) {
            count += search(1, r + offset[0], c + offset[1], offset, map);
        }
        return count;
    }

    private static int search(int l, int r, int c, int[] offset, char[][] map) {
        if (l == XMAS.length) {
            return 1;
        }
        if (r < 0 || r >= map.length || c < 0 || c >= map[0].length) {
            return 0;
        }
        if (map[r][c] != XMAS[l]) {
            return 0;
        }
        return search(l + 1, r + offset[0], c + offset[1], offset, map);
    }
}
