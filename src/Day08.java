import java.nio.file.Path;
import java.util.HashSet;
import java.util.stream.Collectors;

import util.Lines;
import util.Lines.CharElement;
import util.Lines.Position;
import util.Terminal;

public class Day08 {
    private static final char EMPTY = '.';

    public static void main(String[] args) throws Exception {
        var terminal = Terminal.get();
        var input = Path.of("input08.txt");

        var matrix = Lines.asCharMatrixElements(input);

        var antennas = matrix.elements().stream()
                .filter(e -> e.value() != EMPTY)
                .collect(Collectors.groupingBy(CharElement::value,
                        Collectors.mapping(CharElement::position, Collectors.toList())));

        var antinodes1 = new HashSet<Position>();
        var antinodes2 = new HashSet<Position>();

        antennas.values().forEach(positions -> {
            var len = positions.size();
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    if (i == j) {
                        continue;
                    }
                    var a1 = positions.get(i);
                    var a2 = positions.get(j);

                    var diff = a2.substract(a1);

                    var antinode = a2.add(diff);

                    if (matrix.contains(antinode)) {
                        antinodes1.add(antinode);
                    }

                    antinodes2.add(a2);
                    while (matrix.contains(antinode)) {
                        antinodes2.add(antinode);
                        antinode = antinode.add(diff);
                    }
                }
            }
        });

        var part1 = antinodes1.size();
        var part2 = antinodes2.size();

        terminal.printf("%d\n", part1);
        terminal.printf("%d\n", part2);
    }
}
