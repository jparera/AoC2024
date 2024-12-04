import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day02 {
    private static Pattern NUMBER = Pattern.compile("\\d+");

    public static void main(String[] args) throws IOException {
        var path = Path.of("input.txt");
        try (var lines = Files.lines(path)) {
            System.out.println(lines.map(Day02::part1).filter(v -> v).count());
        }
        try (var lines = Files.lines(path)) {
            System.out.println(lines.map(Day02::part2).filter(v -> v).count());
        }
    }

    private static boolean part1(String line) {
        var levels = numbers(line).toArray();
        return safe(levels);
    }

    private static boolean part2(String line) {
        var levels = numbers(line).toArray();
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

    private static IntStream numbers(String line) {
        return NUMBER.matcher(line).results().map(MatchResult::group).mapToInt(Integer::parseInt);
    }
}
