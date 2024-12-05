import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day05Topological {

    public static void main(String[] args) throws IOException {
        var input = Path.of("input.txt");

        try (var lines = Files.lines(input)) {
            var it = lines.iterator();
            // Rules
            var out = new HashMap<Integer, Set<Integer>>();
            var in = new HashMap<Integer, Set<Integer>>();
            while (it.hasNext()) {
                var line = it.next();
                if (line.isEmpty()) {
                    break;
                }
                var rule = numbers(line).toArray();
                out.computeIfAbsent(rule[0], _ -> new HashSet<Integer>()).add(rule[1]);
                in.computeIfAbsent(rule[1], _ -> new HashSet<Integer>()).add(rule[0]);
            }
            // Updates
            var updates = new ArrayList<int[]>();
            it.forEachRemaining(line -> {
                updates.add(numbers(line).toArray());
            });
            // Topological sort
            int part1 = 0;
            int part2 = 0;
            for (var update : updates) {
                boolean valid = true;
                for (int i = 0; i < update.length; i++) {
                    var in_i = in.getOrDefault(update[i], Set.of());
                    for (int j = i; j < update.length; j++) {
                        if (in_i.contains(update[j])) {
                            valid = false;
                        }
                    }
                }
                if (valid) {
                    part1 += update[update.length >> 1];
                } else {
                    var in_degree = new HashMap<Integer, Integer>();
                    var queue = new ArrayDeque<Integer>();
                    var sorted = new int[update.length];
                    var update_set = toSet(update);
                    int i = 0;
                    for (var v : update) {
                        var in_v = in.getOrDefault(v, Set.of());
                        int degree = intersection(update_set, in_v).size();
                        in_degree.put(v, degree);
                        if (degree == 0) {
                            queue.offer(v);
                        }
                    }
                    while (!queue.isEmpty()) {
                        var v = queue.poll();
                        sorted[i++] = v;
                        for (var next : out.getOrDefault(v, Set.of())) {
                            if (in_degree.containsKey(next)) {
                                int degree = in_degree.get(next);
                                in_degree.put(next, --degree);
                                if (degree == 0) {
                                    queue.offer(next);
                                }
                            }
                        }
                    }
                    part2 += sorted[sorted.length >> 1];
                }
            }
            System.out.println(part1);
            System.out.println(part2);
        }
    }

    private static final Pattern NUMBERS = Pattern.compile("\\d+");

    private static IntStream numbers(String line) {
        return NUMBERS.matcher(line).results().map(MatchResult::group).mapToInt(Integer::parseInt);
    }

    private static Set<Integer> toSet(int[] values) {
        return new HashSet<>(IntStream.of(values).boxed().toList());
    }

    private static <T> Set<T> intersection(Set<T> s1, Set<T> s2) {
        return new HashSet<>(s1.stream().filter(s2::contains).toList());
    }
}
