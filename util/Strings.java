package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Strings {
    public static List<List<String>> blocks(Path file) throws IOException {
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

    public static List<String> list(Path file) throws IOException {
        try (var lines = Files.lines(file)) {
            return lines.toList();
        }
    }

    public static char[][] matrix(Path file) throws IOException {
        try (var lines = Files.lines(file)) {
            return matrix(lines);
        }
    }

    public static char[][] matrix(Stream<? extends String> lines) {
        return lines.map(String::toCharArray).toArray(char[][]::new);
    }
}
