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

        var ids = new int[len];
        var size = new int[len];
        var offset = new int[len];
        int blocks = 0;
        for (int i = 0, id = 1; i < len;) {
            ids[i] = id++;
            size[i] = diskmap[i] - '0';
            offset[i] = blocks;
            blocks += size[i++];
            if (i < len) {
                size[i] = diskmap[i] - '0';
                offset[i] = blocks;
                blocks += size[i++];
            }
        }

        for (int i = (len / 2) * 2; i >= 0; i -= 2) {
            int s = size[i];
            int id = ids[i];
            for (int j = 1; j < i; j += 2) {
                if (s <= size[j]) {
                    for (int k = 0; k < s; k++) {
                        fs[offset[i] + k] = 0;
                    }
                    for (int k = 0; k < s; k++) {
                        fs[offset[j] + k] = id;
                    }
                    size[j] -= s;
                    offset[j] += s;
                    break;
                }
            }
        }
        return checksum(fs);
    }

    private static int[] fs(char[] diskmap) {
        int len = diskmap.length;
        int blocks = 0;
        for (int i = 0; i < len;) {
            blocks += diskmap[i++] - '0';
            if (i < len) {
                blocks += diskmap[i++] - '0';
            }
        }
        var fs = new int[blocks];
        for (int i = 0, b = 0, id = 1; i < len; id++) {
            int count = diskmap[i++] - '0';
            while (count-- > 0) {
                fs[b++] = id;
            }
            if (i < len) {
                b += diskmap[i++] - '0';
            }
        }
        return fs;
    }

    private static long checksum(int[] fs) {
        var checksum = 0L;
        for (int i = 0; i < fs.length; i++) {
            if (fs[i] != 0) {
                checksum += i * (fs[i] - 1);
            }
        }
        return checksum;
    }
}
