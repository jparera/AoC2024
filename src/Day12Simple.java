import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import util.Lines;
import util.Lines.CharElement;
import util.Lines.Position;
import util.Terminal;

public class Day12Simple {
    private static final Position[] OFFSETS = {
            new Position(0, 1), // R
            new Position(1, 0), // D
            new Position(0, -1), // L
            new Position(-1, 0), // U
    };

    private static final Position[] DIAGONALS = {
            new Position(1, 1), // DR
            new Position(1, -1), // DL
            new Position(-1, -1), // UL
            new Position(-1, 1), // UR
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
            int perimeter = region.stream().mapToInt(p -> {
                int edges = 0;
                var v = field.get(p);
                for (var offset : OFFSETS) {
                    var nv = field.get(p.add(offset));
                    if (nv == null || nv != v) {
                        edges++;
                    }
                }
                return edges;
            }).sum();
            return region.size() * perimeter;
        }).sum();

        var part2 = regions.stream().mapToInt(region -> {
            int sides = region.stream().mapToInt(p -> {
                // R -> D -> L -> U
                var re = !region.contains(p.add(OFFSETS[0]));
                var de = !region.contains(p.add(OFFSETS[1]));
                var le = !region.contains(p.add(OFFSETS[2]));
                var ue = !region.contains(p.add(OFFSETS[3]));

                var dre = !region.contains(p.add(DIAGONALS[0]));
                var dle = !region.contains(p.add(DIAGONALS[1]));
                var ule = !region.contains(p.add(DIAGONALS[2]));
                var ure = !region.contains(p.add(DIAGONALS[3]));

                int corners = 0;
                corners += (re && de) || (!re && dre && !de) ? 1 : 0;
                corners += (de && le) || (!de && dle && !le) ? 1 : 0;
                corners += (le && ue) || (!le && ule && !ue) ? 1 : 0;
                corners += (ue && re) || (!ue && ure && !re) ? 1 : 0;
                return corners;
            }).sum();
            return region.size() * sides;
        }).sum();

        terminal.printf("%d\n", part1);
        terminal.printf("%d\n", part2);
    }
}
