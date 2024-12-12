import java.nio.file.Path;
import java.util.List;

import util.Lines;
import util.Numbers;
import util.Terminal;

public class Day13 {
    public static void main(String[] args) throws Exception {
        var terminal = Terminal.get();
        var input = Path.of("input13.txt");

        var machines = Lines.asBlocks(input).stream().map(Machine::of).toList();

        var part1 = machines.stream().mapToLong(Machine::part1).sum();
        var part2 = machines.stream().mapToLong(Machine::part2).sum();

        terminal.println(part1);
        terminal.println(part2);
    }

    record Button(int x, int y) {
        static Button of(String line) {
            var offsets = Numbers.asIntStream(line).toArray();
            return new Button(offsets[0], offsets[1]);
        }
    }

    record Price(long x, long y) {
        static Price of(String line) {
            var offsets = Numbers.asIntStream(line).toArray();
            return new Price(offsets[0], offsets[1]);
        }
    }

    record Machine(Button a, Button b, Price price) {
        static Machine of(List<String> lines) {
            var a = Button.of(lines.get(0));
            var b = Button.of(lines.get(1));
            var price = Price.of(lines.get(2));
            return new Machine(a, b, price);
        }

        public long part1() {
            return solve(0);
        }

        public long part2() {
            return solve(10_000_000_000_000L);
        }

        private long solve(long offset) {
            var px = this.price.x + offset;
            var py = this.price.y + offset;
            // Solve b
            var b1 = this.a.y * px - this.a.x * py;
            var b2 = this.a.y * this.b.x - this.a.x * this.b.y;
            if (b1 % b2 != 0) {
                return 0;
            }
            var b = b1 / b2;
            // Solve a
            var a1 = px - this.b.x * b;
            var a2 = this.a.x;
            if (a1 % a2 != 0) {
                return 0;
            }
            var a = a1 / a2;
            return 3 * a + b;
        }

        public long bruteforce() {
            for (int a = 0; a < 100; a++) {
                for (int b = 0; b < 100; b++) {
                    var x = a * this.a.x + b * this.b.x;
                    var y = a * this.a.y + b * this.b.y;
                    if (x == this.price.x && y == this.price.y) {
                        return 3 * a + b;
                    }
                }
            }
            return 0;
        }
    }
}
