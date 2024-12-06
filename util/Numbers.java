package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Numbers {
    private static final Pattern NUMBERS = Pattern.compile("\\d+");

    public static List<int[]> array(Path file) throws IOException {
        try (var lines = Files.lines(file)) {
            return array(lines).toList();
        }
    }

    public static List<List<Integer>> list(Path file) throws IOException {
        return array(file).stream().map(l -> IntStream.of(l).boxed().toList()).toList();
    }

    public static Stream<int[]> array(Stream<? extends String> lines) {
        return intStream(lines).map(IntStream::toArray);
    }

    public static List<int[]> array(Collection<? extends String> lines) {
        return intStream(lines.stream()).map(IntStream::toArray).toList();
    }

    public static Stream<IntStream> intStream(Stream<? extends String> lines) {
        return lines.map(Numbers::list);
    }

    public static IntStream list(String line) {
        return NUMBERS.matcher(line).results().map(MatchResult::group).mapToInt(Integer::parseInt);
    }
}
