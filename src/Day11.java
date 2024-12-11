import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        var counts = stones.stream()
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()));
        while (blinks-- > 0) {
            counts = counts.entrySet().stream()
                    .flatMap(Day11::transform)
                    .collect(Collectors.groupingBy(
                            Map.Entry::getKey,
                            Collectors.summingLong(Map.Entry::getValue)));
        }
        return counts.values().stream().mapToLong(Long::longValue).sum();
    }

    private static Stream<Map.Entry<String, Long>> transform(Map.Entry<String, Long> stone) {
        var number = stone.getKey();
        var count = stone.getValue();
        if ("0".equals(number)) {
            return Stream.of(Map.entry("1", count));
        } else if (number.length() % 2 == 0) {
            var mid = number.length() >> 1;
            var left = Long.toString(Long.parseLong(number.substring(0, mid)));
            var right = Long.toString(Long.parseLong(number.substring(mid)));
            return Stream.of(Map.entry(left, count), Map.entry(right, count));
        } else {
            return Stream.of(Map.entry(Long.toString(Long.parseLong(number) * 2024), count));
        }
    }
}
