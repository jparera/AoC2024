import java.nio.file.Path;
import java.util.Arrays;

import util.Lines;
import util.Terminal;

public class Day07 {
    public static void main(String[] args) throws Exception {
        var input = Path.of("input07.txt");
        var lines = Lines.asLongMatrix(input);

        var terminal = Terminal.get();

        var part1 = Arrays.stream(lines).filter(Day07::valid1).mapToLong(values -> values[0]).sum();
        var part2 = Arrays.stream(lines).filter(Day07::valid2).mapToLong(values -> values[0]).sum();

        terminal.printf("%d\n", part1);
        terminal.printf("%d\n", part2);
    }

    private static boolean valid1(long[] values) {
        return valid(values, 2, values[1], false);
    }

    private static boolean valid2(long[] values) {
        return valid(values, 2, values[1], true);
    }

    private static boolean valid(long[] values, int i, long result, boolean part2) {
        if (i >= values.length) {
            return result == values[0];
        }
        if (result > values[0]) {
            return false;
        }
        var valid = false;
        if (valid |= valid(values, i + 1, result + values[i], part2))
            return true;
        if (valid |= valid(values, i + 1, result * values[i], part2))
            return true;
        if (part2 && (valid |= valid(values, i + 1, concat(result, values[i]), part2)))
            return true;
        return false;
    }

    private static long concat(long v1, long v2) {
        return Long.parseLong(Long.toString(v1) + v2);
    }
}
