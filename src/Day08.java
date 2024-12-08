import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import util.Lines;
import util.Terminal;

public class Day08 {
    private static final char EMPTY = '.';

    public static void main(String[] args) throws Exception {
        var terminal = Terminal.get();
        var input = Path.of("input08.txt");

        var map = Lines.asCharMatrix(input);
        var rows = map.length;
        var cols = map[0].length;

        var antennas = new HashMap<Character, List<Position>>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c] != EMPTY) {
                    antennas.computeIfAbsent(map[r][c], _ -> new ArrayList<>()).add(new Position(r, c));
                }
            }
        }

        var antinodes1 = new HashSet<Position>();
        var antinodes2 = new HashSet<Position>();
        for (var entry : antennas.entrySet()) {
            var frequencyAntennas = entry.getValue().toArray(Position[]::new);
            var len = frequencyAntennas.length;
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    if (i < j) {
                        var a1 = frequencyAntennas[i];
                        var a2 = frequencyAntennas[j];

                        int dr = a2.row - a1.row;
                        int dc = a2.col - a1.col;

                        int a1r = a2.row + dr;
                        int a1c = a2.col + dc;

                        int a2r = a1.row - dr;
                        int a2c = a1.col - dc;

                        if (a1r >= 0 && a1r < rows && a1c >= 0 && a1c < cols) {
                            antinodes1.add(new Position(a1r, a1c));
                        }
                        if (a2r >= 0 && a2r < rows && a2c >= 0 && a2c < cols) {
                            antinodes1.add(new Position(a2r, a2c));
                        }

                        antinodes2.add(new Position(a1.row, a1.col));
                        antinodes2.add(new Position(a2.row, a2.col));

                        while (a1r >= 0 && a1r < rows && a1c >= 0 && a1c < cols) {
                            antinodes2.add(new Position(a1r, a1c));
                            a1r += dr;
                            a1c += dc;
                        }
                        while (a2r >= 0 && a2r < rows && a2c >= 0 && a2c < cols) {
                            antinodes2.add(new Position(a2r, a2c));
                            a2r -= dr;
                            a2c -= dc;
                        }
                    }
                }
            }
        }

        var part1 = antinodes1.size();
        var part2 = antinodes2.size();

        terminal.printf("%d\n", part1);
        terminal.printf("%d\n", part2);
    }

    record Position(int row, int col) {
    }
}
