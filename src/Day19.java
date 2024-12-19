import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import util.Lines;
import util.Terminal;

public class Day19 {
    public static void main(String[] args) throws Exception {
        var terminal = Terminal.get();
        var input = Path.of("input19.txt");
        var blocks = Lines.asBlocks(input);

        var patterns = blocks.get(0).stream()
                .flatMap(line -> Arrays.stream(line.split(", ")))
                .collect(Collectors.toSet());
        var designs = blocks.get(1);

        var memo = new ConcurrentHashMap<String, Long>();

        var part1 = designs.stream().parallel()
                .mapToLong(design -> valid(design, 0, patterns, memo))
                .filter(i -> i > 0)
                .count();
        var part2 = designs.stream().parallel()
                .mapToLong(design -> valid(design, 0, patterns, memo))
                .sum();

        terminal.println(part1);
        terminal.println(part2);
    }

    private static long valid(String design, int from, Set<String> patterns, Map<String, Long> memo) {
        var cached = memo.get(design.substring(from));
        if (cached != null) {
            return cached;
        }

        if (from >= design.length()) {
            return 1;
        }
        long count = 0;
        for (int i = from + 1; i <= design.length(); i++) {
            var s = design.substring(from, i);
            if (patterns.contains(s)) {
                count += valid(design, i, patterns, memo);
            }
        }
        memo.put(design.substring(from), count);
        return count;
    }
}
