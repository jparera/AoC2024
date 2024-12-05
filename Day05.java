import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day05 {
    private static final Pattern NUMBERS = Pattern.compile("\\d+");

    public static void main(String[] args) throws IOException {
        var input = Path.of("input.txt");

        try (var lines = Files.lines(input)) {
            var it = lines.iterator();

            var rules = new HashMap<Integer, Set<Integer>>();
            while (it.hasNext()) {
                var line = it.next();
                if (line.isEmpty()) {
                    break;
                }
                var rule = numbers(line).toArray();
                rules.computeIfAbsent(rule[0], _ -> new HashSet<Integer>()).add(rule[1]);
            }

            var updates = new ArrayList<int[]>();
            it.forEachRemaining(line -> {
                updates.add(lineToUpdate(line));
            });

            int part1 = updates.stream()
                    .filter(isRightOrderPredicate(rules))
                    .mapToInt(Day05::middle)
                    .sum();
            System.out.println(part1);

            int part2 = updates.stream()
                    .filter(Predicate.not(isRightOrderPredicate(rules)))
                    .map(sortFunction(rules))
                    .mapToInt(Day05::middle)
                    .sum();
            System.out.println(part2);
        }
    }

    private static int[] lineToUpdate(String line) {
        return numbers(line).toArray();
    }

    private static Predicate<int[]> isRightOrderPredicate(Map<Integer, Set<Integer>> rules) {
        var sort = sortFunction(rules);
        return update -> {
            return Arrays.equals(update, sort.apply(update));
        };
    }

    private static Function<int[], int[]> sortFunction(Map<Integer, Set<Integer>> rules) {
        return update -> {
            return IntStream.of(update).boxed()
                    .sorted((l, r) -> {
                        var rr = rules.get(l);
                        if (rr != null) {
                            return rr.contains(r) ? -1 : 0;
                        } else {
                            var lr = rules.get(r);
                            return lr != null && lr.contains(l) ? 1 : 0;
                        }
                    })
                    .mapToInt(Integer::intValue).toArray();
        };
    }

    private static int middle(int[] update) {
        int mid = update.length >>> 1;
        return update[mid];
    }

    private static IntStream numbers(String line) {
        return NUMBERS.matcher(line).results().map(MatchResult::group).mapToInt(Integer::parseInt);
    }
}
