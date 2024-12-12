import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import util.Lines;
import util.Lines.CharElement;
import util.Lines.Position;
import util.Terminal;

public class Day12 {
    private static final Position[] OFFSETS = {
            new Position(-1, 0),
            new Position(1, 0),
            new Position(0, -1),
            new Position(0, 1)
    };

    public static void main(String[] args) throws Exception {
        var terminal = Terminal.get();
        var input = Path.of("input12.txt");

        var matrix = Lines.asCharMatrixElements(input);
        var field = matrix.elements().stream()
                .collect(Collectors.toMap(CharElement::position, CharElement::value));

        var visited = new HashSet<Position>();
        var regions = new ArrayDeque<Set<Position>>();
        field.keySet().forEach(p -> {
            if (visited.contains(p)) {
                return;
            }
            var stack = new ArrayDeque<Position>();
            stack.push(p);
            visited.add(p);
            regions.push(new HashSet<>());
            while (!stack.isEmpty()) {
                var cp = stack.pop();
                var cv = field.get(cp);
                regions.peek().add(cp);
                for (var offset : OFFSETS) {
                    var np = cp.add(offset);
                    var nv = field.get(np);
                    if (nv == null || nv != cv) {
                        continue;
                    }
                    if (!visited.contains(np)) {
                        visited.add(np);
                        stack.push(np);
                    }
                }
            }
        });

        var part1 = regions.stream().mapToInt(region -> {
            int perimeter = 0;
            for (var cp : region) {
                var cv = field.get(cp);
                for (var offset : OFFSETS) {
                    var np = cp.add(offset);
                    var nv = field.get(np);
                    if (nv == null || nv != cv) {
                        perimeter++;
                    }
                }
            }
            return region.size() * perimeter;
        }).sum();

        var part2 = regions.stream().mapToInt(region -> {
            int sides = 0;
            sides += sides(region, List.of(OFFSETS[0], OFFSETS[1]), OFFSETS[2]);
            sides += sides(region, List.of(OFFSETS[0], OFFSETS[1]), OFFSETS[3]);
            sides += sides(region, List.of(OFFSETS[2], OFFSETS[3]), OFFSETS[0]);
            sides += sides(region, List.of(OFFSETS[2], OFFSETS[3]), OFFSETS[1]);
            return region.size() * sides;
        }).sum();

        terminal.printf("%d\n", part1);
        terminal.printf("%d\n", part2);
    }

    private static int sides(Set<Position> region, List<Position> offsets, Position edge) {
        int sides = 0;
        var visited = new HashSet<Position>();
        for (var p : region) {
            if (visited.contains(p)) {
                continue;
            }
            if (region.contains(p.add(edge))) {
                continue;
            }
            for (var offset : offsets) {
                var cp = p;
                while (region.contains(cp) && !region.contains(cp.add(edge))) {
                    visited.add(cp);
                    cp = cp.add(offset);
                }
            }
            sides++;
        }
        return sides;
    }

}
