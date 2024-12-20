import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import util.Lines;

public class Day03 {
    private static Pattern PART1 = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");

    private static Pattern PART2 = Pattern.compile("do\\(\\)|don't\\(\\)|mul\\((\\d{1,3}),(\\d{1,3})\\)");

    public static void main(String[] args) throws IOException {
        var path = Path.of("input03.txt");
        var lines = Lines.asStrings(path);
        //
        // Part 1
        //
        var part1 = lines.stream().flatMap(Day03::part1).mapToLong(Mul::eval).sum();
        System.out.println(part1);
        //
        // Part 2
        //
        var part2 = 0L;
        var enabled = true;
        var ops = lines.stream().flatMap(Day03::part2).toList();
        for (var op : ops) {
            switch (op) {
                case Enable _:
                    enabled = true;
                    break;
                case Disable _:
                    enabled = false;
                    break;
                case Mul mul:
                    if (enabled) {
                        part2 += mul.eval();
                    }
                    break;
                default:
                    throw new IllegalStateException();
            }
        }
        System.out.println(part2);
    }

    private static Stream<Mul> part1(String line) {
        return PART1.matcher(line).results().map(Mul::of);
    }

    private static Stream<Op> part2(String line) {
        return PART2.matcher(line).results().flatMap(m -> {
            return switch (m.group()) {
                case "don't()" -> Stream.of(Disable.INSTANCE);
                case "do()" -> Stream.of(Enable.INSTANCE);
                default -> Stream.of(Mul.of(m));
            };
        });
    }

    interface Op {
    }

    record Enable() implements Op {
        static Enable INSTANCE = new Enable();
    }

    record Disable() implements Op {
        static Disable INSTANCE = new Disable();
    }

    record Mul(int a, int b) implements Op {
        public static Mul of(MatchResult m) {
            return new Mul(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
        }

        public long eval() {
            return (long) a * (long) b;
        }
    }
}
