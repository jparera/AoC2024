import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import util.Numbers;

public class Day01 {
    public static void main(String[] args) throws IOException {
        var input = Path.of("input.txt");

        var left = new ArrayList<Integer>();
        var right = new ArrayList<Integer>();
        for (var line : Numbers.list(input)) {
            left.add(line.get(0));
            right.add(line.get(1));
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
