import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day01 {
    public static void main(String[] args) throws IOException {
        var input = Path.of("input.txt");
        var left = new ArrayList<Integer>();
        var right = new ArrayList<Integer>();
        try (var lines = Files.lines(input)) {
            for (var line : lines.toList()) {
                var locations = line.split("\s+");
                left.add(Integer.parseInt(locations[0]));
                right.add(Integer.parseInt(locations[1]));
            }
        }
        left.sort(null);
        right.sort(null);
        long sum = 0;
        var l = left.iterator();
        var r = right.iterator();
        var h = new HashMap<Integer, Integer>();
        while (l.hasNext()) {
            var ln = l.next();
            var rn = r.next();
            h.compute(rn, (_, v) -> v == null ? 1 : v + 1);
            var diff = Math.abs(ln - rn);
            System.out.println(diff);
            sum += diff;
        }
        long score = 0;
        l = left.iterator();
        while (l.hasNext()) {
            var ln = l.next();
            score += ln * h.getOrDefault(ln, 0);
        }
        System.out.println(sum);
        System.out.println(score);
    }
}
