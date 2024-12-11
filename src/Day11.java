import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import util.Lines;
import util.Terminal;

public class Day11 {
    public static void main(String[] args) throws Exception {
        var terminal = Terminal.get();
        var input = Path.of("input11.txt");

        var stones = Lines.asStringLists(input, Pattern.compile("\\s+")).getFirst();

        var part1 = simulate(stones, 25);
        var part2 = simulate(stones, 75);

        terminal.printf("%d\n", part1);
        terminal.printf("%d\n", part2);
    }

    private static long simulate(List<String> stones, int blinks) {
        var counts = new HashMap<Long, long[]>();
        for (var stone : stones) {
            counts.computeIfAbsent(Long.parseLong(stone), _ -> new long[1])[0]++;
        }
        var memo = new HashMap<Long, List<Long>>();
        while (blinks-- > 0) {
            var next = new HashMap<Long, long[]>();
            counts.entrySet().forEach(entry -> {
                var stone = entry.getKey();
                var count = entry.getValue();
                transform(stone, memo).forEach(t -> {
                    next.computeIfAbsent(t, _ -> new long[1])[0] += count[0];
                });
            });
            counts = next;
        }
        return counts.values().stream().mapToLong(c -> c[0]).sum();
    }

    private static List<Long> transform(long stone, Map<Long, List<Long>> memo) {
        var cached = memo.get(stone);
        if (cached != null) {
            return cached;
        }
        List<Long> output;
        if (stone == 0) {
            output = List.of(1L);
        } else {
            var digits = Long.toString(stone);
            if (digits.length() % 2 == 0) {
                var mid = digits.length() >> 1;
                var left = Long.parseLong(digits.substring(0, mid));
                var right = Long.parseLong(digits.substring(mid));
                output = List.of(left, right);
            } else {
                output = List.of(stone * 2024);
            }
        }
        memo.put(stone, output);
        return output;
    }
}
