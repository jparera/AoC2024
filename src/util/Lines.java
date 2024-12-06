package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Lines {
    public static List<List<String>> asBlocks(Path file) throws IOException {
        var blocks = new ArrayList<List<String>>();
        blocks.add(new ArrayList<>());
        try (var lines = Files.lines(file)) {
            var it = lines.iterator();
            while (it.hasNext()) {
                var line = it.next();
                if (line.isEmpty()) {
                    blocks.add(new ArrayList<>());
                    continue;
                }
                blocks.getLast().add(line);
            }
        }
        return blocks;
    }

    public static List<String> asStrings(Path file) throws IOException {
        try (var lines = Files.lines(file)) {
            return lines.toList();
        }
    }

    public static int[][] asIntMatrix(Path file) throws IOException {
        return asIntArrays(file).stream().toArray(int[][]::new);
    }

    public static long[][] asLongMatrix(Path file) throws IOException {
        return asLongArrays(file).stream().toArray(long[][]::new);
    }

    public static char[][] asCharMatrix(Path file) throws IOException {
        try (var lines = Files.lines(file)) {
            return Strings.asCharMatrix(lines);
        }
    }

    public static List<int[]> asIntArrays(Path file) throws IOException {
        try (var lines = Files.lines(file)) {
            return Numbers.asIntArrays(lines).toList();
        }
    }

    public static List<long[]> asLongArrays(Path file) throws IOException {
        try (var lines = Files.lines(file)) {
            return Numbers.asLongArrays(lines).toList();
        }
    }

    public static List<List<Integer>> asIntegerLists(Path file) throws IOException {
        return asIntArrays(file).stream().map(l -> IntStream.of(l).boxed().toList()).toList();
    }

    public static List<List<Long>> asLongLists(Path file) throws IOException {
        return asLongArrays(file).stream().map(l -> LongStream.of(l).boxed().toList()).toList();
    }
}
