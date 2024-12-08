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

    public static IntMatrix asIntMatrixElements(Path file) throws IOException {
        var matrix = asIntMatrix(file);
        var rows = matrix.length;
        var cols = matrix[0].length;
        var elements = new ArrayList<IntElement>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                elements.add(new IntElement(new Position(r, c), matrix[r][c]));
            }
        }
        return new IntMatrix(rows, cols, matrix, elements);
    }

    public static long[][] asLongMatrix(Path file) throws IOException {
        return asLongArrays(file).stream().toArray(long[][]::new);
    }

    public static LongMatrix asLongMatrixElements(Path file) throws IOException {
        var matrix = asLongMatrix(file);
        var rows = matrix.length;
        var cols = matrix[0].length;
        var elements = new ArrayList<LongElement>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                elements.add(new LongElement(new Position(r, c), matrix[r][c]));
            }
        }
        return new LongMatrix(rows, cols, matrix, elements);
    }

    public static char[][] asCharMatrix(Path file) throws IOException {
        try (var lines = Files.lines(file)) {
            return Strings.asCharMatrix(lines);
        }
    }

    public static CharMatrix asCharMatrixElements(Path file) throws IOException {
        var matrix = asCharMatrix(file);
        var rows = matrix.length;
        var cols = matrix[0].length;
        var elements = new ArrayList<CharElement>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                elements.add(new CharElement(new Position(r, c), matrix[r][c]));
            }
        }
        return new CharMatrix(rows, cols, matrix, elements);
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

    public record Position(int row, int col) {
        public Position add(Position p) {
            return new Position(row + p.row, col + p.col);
        }

        public Position substract(Position p) {
            return new Position(row - p.row, col - p.col);
        }
    }

    public record IntElement(Position position, int value) {
    }

    public record LongElement(Position position, long value) {
    }

    public record CharElement(Position position, long value) {

    }

    public static class Matrix<E> {
        private int rows;

        private int cols;

        private List<E> elements;

        Matrix(int rows, int cols, List<E> elements) {
            this.rows = rows;
            this.cols = cols;
            this.elements = new ArrayList<>(elements);
        }

        public boolean contains(Position p) {
            return p.row >= 0 && p.row < rows && p.col >= 0 && p.col < cols;
        }

        public int rows() {
            return rows;
        }

        public int cols() {
            return cols;
        }

        public List<E> elements() {
            return elements;
        }
    }

    public static class IntMatrix extends Matrix<IntElement> {
        private int[][] matrix;

        public IntMatrix(int rows, int cols, int[][] matrix, List<IntElement> elements) {
            super(rows, cols, elements);
            this.matrix = matrix;
        }

        public int[][] matrix() {
            return matrix;
        }
    }

    public static class LongMatrix extends Matrix<LongElement> {
        private long[][] matrix;

        public LongMatrix(int rows, int cols, long[][] matrix, List<LongElement> elements) {
            super(rows, cols, elements);
            this.matrix = matrix;
        }

        public long[][] matrix() {
            return matrix;
        }
    }

    public static class CharMatrix extends Matrix<CharElement> {
        private char[][] matrix;

        public CharMatrix(int rows, int cols, char[][] matrix, List<CharElement> elements) {
            super(rows, cols, elements);
            this.matrix = matrix;
        }

        public char[][] matrix() {
            return matrix;
        }
    }
}
