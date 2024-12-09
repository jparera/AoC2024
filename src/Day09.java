import java.nio.file.Path;

import util.Lines;
import util.Terminal;

public class Day09 {
    public static void main(String[] args) throws Exception {
        var terminal = Terminal.get();
        var input = Path.of("input09.txt");

        var diskmap = Lines.asCharMatrix(input)[0];
        var part1 = part1(diskmap);
        var part2 = part2(diskmap);

        terminal.printf("%d\n", part1);
        terminal.printf("%d\n", part2);
    }

    private static long part1(char[] diskmap) {
        var fs = fs(diskmap);
        for (int i = 0, j = fs.length - 1; i < j;) {
            if (fs[i] != 0) {
                i++;
                continue;
            }
            if (fs[j] == 0) {
                j--;
                continue;
            }
            fs[i++] = fs[j];
            fs[j--] = 0;
        }
        return checksum(fs);
    }

    private static long part2(char[] diskmap) {
        var fs = fs(diskmap);

        var len = diskmap.length;
        var offset = new int[len];
        var length = new int[len];
        int blocks = 0;
        for (int i = 0; i < len; i++) {
            offset[i] = blocks;
            length[i] = diskmap[i] - '0';
            blocks += length[i];
        }

        for (int i = (len / 2) * 2; i >= 0; i -= 2) {
            int il = length[i];
            int id = (i >> 1) + 1;
            for (int j = 1; j < i; j += 2) {
                if (il <= length[j]) {
                    for (int k = 0; k < il; k++) {
                        fs[offset[i] + k] = 0;
                        fs[offset[j] + k] = id;
                    }
                    length[j] -= il;
                    offset[j] += il;
                    break;
                }
            }
        }
        return checksum(fs);
    }

    private static int[] fs(char[] diskmap) {
        int len = diskmap.length;
        int blocks = 0;
        for (int i = 0; i < len; i++) {
            blocks += diskmap[i] - '0';
        }
        var fs = new int[blocks];
        for (int i = 0, j = 0; i < len; i++) {
            int count = diskmap[i] - '0';
            int id = i % 2 == 0 ? (i >> 1) + 1 : 0;
            while (count-- > 0) {
                fs[j++] = id;
            }
        }
        return fs;
    }

    private static long checksum(int[] fs) {
        var checksum = 0L;
        for (int i = 0; i < fs.length; i++) {
            if (fs[i] != 0) {
                checksum += i * (fs[i] - 1L);
            }
        }
        return checksum;
    }
}
