import java.io.IOException;
import java.nio.file.Path;

import util.Lines;

public class Day02 {
    public static void main(String[] args) throws IOException {
        var path = Path.of("input02.txt");

        var lines = Lines.asNumberArrays(path);

        var part1 = lines.stream().map(Day02::part1).filter(v -> v).count();
        var part2 = lines.stream().map(Day02::part2).filter(v -> v).count();

        System.out.println(part1);
        System.out.println(part2);
    }

    private static boolean part1(int[] levels) {
        return safe(levels);
    }

    private static boolean part2(int[] levels) {
        if (safe(levels)) {
            return true;
        }
        var fixed = new int[levels.length - 1];
        for (int i = 0; i < levels.length; i++) {
            int c = 0;
            for (int j = 0; j < levels.length; j++) {
                if (j != i) {
                    fixed[c++] = levels[j];
                }
            }
            if (safe(fixed)) {
                return true;
            }
        }
        return false;
    }

    private static boolean safe(int[] levels) {
        int increasing = 0;
        int diff = 0;
        for (int i = 1; i < levels.length; i++) {
            int inc = levels[i] - levels[i - 1];
            if (inc > 0) {
                increasing++;
            }
            int d = Math.abs(inc);
            if (d > 0 && d < 4) {
                diff++;
            }
        }
        return (increasing == 0 || increasing == levels.length - 1) && diff == levels.length - 1;
    }
}
