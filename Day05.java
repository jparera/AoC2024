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
            // Rules
            var lt = new HashMap<Integer, Set<Integer>>();
            var gt = new HashMap<Integer, Set<Integer>>();
            while (it.hasNext()) {
                var line = it.next();
                if (line.isEmpty()) {
                    break;
                }
                var rule = numbers(line).toArray();
                lt.computeIfAbsent(rule[0], _ -> new HashSet<Integer>()).add(rule[1]);
                gt.computeIfAbsent(rule[1], _ -> new HashSet<Integer>()).add(rule[0]);
            }
            // Updates
            var updates = new ArrayList<int[]>();
            it.forEachRemaining(line -> {
                updates.add(lineToUpdate(line));
            });
            // Solutions
            var sort = sortFunction(lt, gt);

            int part1 = updates.stream()
                    .filter(isRightOrderPredicate(sort))
                    .mapToInt(Day05::middle)
                    .sum();
            System.out.println(part1);

            int part2 = updates.stream()
                    .filter(Predicate.not(isRightOrderPredicate(sort)))
                    .map(sort)
                    .mapToInt(Day05::middle)
                    .sum();
            System.out.println(part2);
        }
    }

    private static int[] lineToUpdate(String line) {
        return numbers(line).toArray();
    }

    private static Predicate<int[]> isRightOrderPredicate(Function<int[], int[]> sort) {
        return update -> {
            return Arrays.equals(update, sort.apply(update));
        };
    }

    private static Function<int[], int[]> sortFunction(Map<Integer, Set<Integer>> lt, Map<Integer, Set<Integer>> gt) {
        return update -> {
            var len = update.length;
            update = Arrays.copyOf(update, len);
            for (int i = 1; i < len; i++) {
                if (lt.get(update[i]) == null && gt.get(update[i]) == null) {
                    continue;
                }
                int j = i;
                int k = j - 1;
                while (j >= 0 && k >= 0) {
                    if (!hasRule(update[j], update[k], lt, gt)) {
                        k--;
                    } else if (compare(update[j], update[k], lt, gt) < 0) {
                        int tmp = update[j];
                        update[j] = update[k];
                        update[k] = tmp;
                        j = k;
                        k = j - 1;
                    } else {
                        break;
                    }
                }
            }
            return update;
        };
    }

    private static boolean hasRule(int l, int r, Map<Integer, Set<Integer>> lt, Map<Integer, Set<Integer>> gt) {
        return lt.getOrDefault(l, Set.of()).contains(r) || gt.getOrDefault(l, Set.of()).contains(r);
    }

    private static int compare(int l, int r, Map<Integer, Set<Integer>> lt, Map<Integer, Set<Integer>> gt) {
        var lt_l = lt.get(l);
        if (lt_l != null && lt_l.contains(r)) {
            return -1;
        }
        var gt_l = gt.get(l);
        if (gt_l != null && gt_l.contains(r)) {
            return 1;
        }
        throw new IllegalStateException();
    }

    private static int middle(int[] update) {
        int mid = update.length >>> 1;
        return update[mid];
    }

    private static IntStream numbers(String line) {
        return NUMBERS.matcher(line).results().map(MatchResult::group).mapToInt(Integer::parseInt);
    }
}