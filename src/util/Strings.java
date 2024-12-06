package util;

import java.util.stream.Stream;

public class Strings {
    public static char[][] asCharMatrix(Stream<? extends String> lines) {
        return lines.map(String::toCharArray).toArray(char[][]::new);
    }
}
