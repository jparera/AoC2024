import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Set;

import util.Numbers;
import util.Sets;
import util.Strings;

public class Day05Topological {
    public static void main(String[] args) throws IOException {
        var input = Path.of("input.txt");
        var blocks = Strings.blocks(input);
        // Rules
        var out = new HashMap<Integer, Set<Integer>>();
        var in = new HashMap<Integer, Set<Integer>>();
        for (var rule : Numbers.array(blocks.get(0))) {
            out.computeIfAbsent(rule[0], Sets::computeHashSet).add(rule[1]);
            in.computeIfAbsent(rule[1], Sets::computeHashSet).add(rule[0]);
        }
        // Updates
        var updates = Numbers.array(blocks.get(1));
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
                var update_set = Sets.toSet(update);
                int i = 0;
                for (var v : update) {
                    var in_v = in.getOrDefault(v, Set.of());
                    int degree = Sets.intersection(update_set, in_v).size();
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
