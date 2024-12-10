import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import util.Lines;
import util.Lines.CharElement;
import util.Lines.Position;
import util.Terminal;

public class Day10Iterative {
    public static void main(String[] args) throws Exception {
        var terminal = Terminal.get();
        var input = Path.of("input10.txt");

        var matrix = Lines.asCharMatrixElements(input);
        var elements = matrix.elements();
        var starts = elements.stream().filter(e -> e.value() == '0').map(CharElement::position).toList();
        var map = elements.stream().collect(Collectors.toMap(CharElement::position, CharElement::value));

        var part1 = 0;
        var part2 = 0;

        var offsets = List.of(new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } });
        var peaks = new HashSet<Position>();
        var stack = new ArrayDeque<Position>();
        starts.forEach(stack::push);
        while (!stack.isEmpty()) {
            var position = stack.pop();
            var value = map.get(position);
            if (value == null) {
                continue;
            }
            if (value == '0') {
                part1 += peaks.size();
                peaks = new HashSet<Position>();
            } else if (value == '9') {
                peaks.add(position);
                part2++;
                continue;
            }
            for (var offset : offsets) {
                var next = new Position(position.row() + offset[0], position.col() + offset[1]);
                if (map.getOrDefault(next, '0') - value == 1) {
                    stack.push(next);
                }
            }
        }
        part1 += peaks.size();

        terminal.printf("%d\n", part1);
        terminal.printf("%d\n", part2);
    }
}
