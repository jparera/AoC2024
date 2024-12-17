import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import util.Lines;
import util.Numbers;
import util.Terminal;

public class Day17 {

    public static void main(String[] args) throws Exception {
        var terminal = Terminal.get();
        var input = Path.of("input17.txt");

        var blocks = Lines.asBlocks(input);
        var register = blocks.get(0).stream()
                .mapToLong(line -> Numbers.asLongStream(line).findFirst().orElse(0))
                .toArray();
        var program = blocks.get(1).stream().flatMapToInt(Numbers::asIntStream).toArray();

        var cpu = new CPU();
        var part1 = output(cpu.execute(register, program));
        terminal.println(part1);

        var part2 = 0L;
        var indices = new long[program.length];
        for (int j = program.length - 1; j >= 0;) {
            var found = false;
            var mask = (1L << (3 * (j + 1))) - 1;
            part2 &= ~mask;
            for (; indices[j] < 8 && !found; indices[j]++) {
                var v = indices[j] << (3 * j);
                register[CPU.A] = part2 + v;
                var o = cpu.execute(register, program);
                if (j < o.length && o[j] == program[j]) {
                    part2 += v;
                    found = true;
                }
            }
            if (found) {
                j--;
            } else {
                indices[j] = 0;
                j++;
            }
        }
        register[CPU.A] = part2;
        terminal.print(program);
        terminal.print(cpu.execute(register, program));
        terminal.println(part2);
    }

    private static String output(int[] output) {
        return IntStream.of(output).mapToObj(Integer::toString).collect(Collectors.joining(","));
    }

    private static class CPU {
        static final int A = 0;
        static final int B = 1;
        static final int C = 2;

        private long[] register = new long[3];

        private List<Integer> output = new ArrayList<>();

        private int ip = 0;

        private int jmp = -1;

        public int[] execute(long[] register, int[] program) {
            this.output.clear();
            this.ip = 0;
            this.register[A] = register[A];
            this.register[B] = register[B];
            this.register[C] = register[C];
            while (ip < program.length) {
                int op = program[ip];
                int operand = program[ip + 1];
                switch (op) {
                    case 0 -> adv(operand);
                    case 1 -> bxl(operand);
                    case 2 -> bst(operand);
                    case 3 -> jnz(operand);
                    case 4 -> bxc(operand);
                    case 5 -> out(operand);
                    case 6 -> bdv(operand);
                    case 7 -> cdv(operand);
                    default -> throw new IllegalArgumentException();
                }
                ip = jmp == -1 ? ip + 2 : jmp;
                jmp = -1;
            }
            return output.stream().mapToInt(Integer::intValue).toArray();
        }

        private void adv(int operand) {
            register[A] >>>= combo(operand);
        }

        private void bdv(int operand) {
            register[B] = register[A] >>> combo(operand);
        }

        private void cdv(int operand) {
            register[C] = register[A] >>> combo(operand);
        }

        private void bxl(int operand) {
            register[B] ^= ((long) operand);
        }

        private void bst(int operand) {
            register[B] = combo(operand) & 7;
        }

        private void jnz(int operand) {
            if (register[A] == 0)
                return;
            jmp = operand;
        }

        private void bxc(int operand) {
            register[B] ^= register[C];
        }

        private void out(int operand) {
            output.add((int) (combo(operand) & 7));
        }

        private long combo(int operand) {
            return switch (operand) {
                case 4 -> register[A];
                case 5 -> register[B];
                case 6 -> register[C];
                case 7 -> throw new IllegalArgumentException();
                default -> operand;
            };
        }
    }
}
