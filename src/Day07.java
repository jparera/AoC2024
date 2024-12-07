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
        return valid1(values, 2, values[1]);
    }

    private static boolean valid2(long[] values) {
        return valid2(values, 2, values[1]);
    }

    private static boolean valid1(long[] values, int i, long result) {
        if (i >= values.length) {
            return result == values[0];
        }
        if (result > values[0]) {
            return false;
        }
        var valid = false;
        if (valid |= valid1(values, i + 1, result + values[i]))
            return true;
        if (valid |= valid1(values, i + 1, result * values[i]))
            return true;
        return false;
    }

    private static boolean valid2(long[] values, int i, long result) {
        if (i >= values.length) {
            return result == values[0];
        }
        if (result > values[0]) {
            return false;
        }
        var valid = false;
        if (valid |= valid2(values, i + 1, result + values[i]))
            return true;
        if (valid |= valid2(values, i + 1, result * values[i]))
            return true;
        if (valid |= valid2(values, i + 1, Long.parseLong(Long.toString(result) + values[i])))
            return true;
        return false;
    }
}
