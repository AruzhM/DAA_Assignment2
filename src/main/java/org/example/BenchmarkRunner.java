package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class BenchmarkRunner {
    private static final int[] SIZES = {100, 1000, 10000, 100000};
    private static final int RUNS = 5;
    private static final int GET_COUNT = 10000;

    private static volatile long sink;

    private record Measurement(
            long nanos,
            long steps,
            long moves,
            long comparisons
    ) {
    }

    public static void main(String[] args) throws IOException {
        StringBuilder csv = new StringBuilder(
                "workload,variant,structure,n,time_ms,steps,moves,comparisons\n"
        );

        for (int n : SIZES) {
            int[] indices = createIndices(n);

            benchmarkW1("DynamicArray", n, indices, csv);
            benchmarkW1("MyLinkedList", n, indices, csv);

            int[] queries = createQueries(n);

            benchmarkW2("DynamicArray", n, queries, csv);
            benchmarkW2("MyLinkedList", n, queries, csv);
        }

        Path output = Path.of("results", "results.csv");
        Files.createDirectories(output.getParent());
        Files.writeString(output, csv.toString());

        System.out.println("Saved: " + output.toAbsolutePath());
    }

    private static int[] createIndices(int n) {
        Random random = new Random(42);
        int[] indices = new int[GET_COUNT];

        for (int i = 0; i < indices.length; i++) {
            indices[i] = random.nextInt(n);
        }

        return indices;
    }
    private static IntList createList(String structure, int n) {
        IntList list;

        if (structure.equals("DynamicArray")) {
            list = new DynamicArray();
        } else if (structure.equals("MyLinkedList")) {
            list = new MyLinkedList();
        } else {
            throw new IllegalArgumentException(
                    "Unknown structure: " + structure
            );
        }
        for (int i = 0; i < n; i++) {
            list.add(i);
        }

        list.resetMetrics();
        return list;
    }

    private static Measurement runW1(
            String structure, int n, int[] indices
    ) {
        IntList list = createList(structure, n);
        long checksum = 0;

        long start = System.nanoTime();

        for (int index : indices) {
            checksum += list.get(index);
        }

        long elapsed = System.nanoTime() - start;
        sink = checksum;

        long expectedChecksum = 0;

        for (int index : indices) {
            expectedChecksum += index;
        }

        if (checksum != expectedChecksum) {
            throw new IllegalStateException("W1 checksum mismatch");
        }

        long expectedSteps = structure.equals("DynamicArray")
                ? indices.length
                : expectedChecksum;

        if (list.getSteps() != expectedSteps
                || list.getMoves() != 0
                || list.getComparisons() != 0) {
           throw new IllegalStateException("W1 metrics mismatch");
        }

        return new Measurement(
                elapsed,
                list.getSteps(),
                list.getMoves(),
                list.getComparisons()
        );
    }

    private static void benchmarkW1(
            String structure, int n, int[] indices,
            StringBuilder csv
    ) {
        for (int warmup = 0; warmup < 5; warmup++) {
            runW1(structure, n, indices);
        }

        Measurement[] results = new Measurement[RUNS];

        for (int run = 0; run < RUNS; run++) {
            results[run] = runW1(structure, n, indices);
        }


        Arrays.sort(results,
                (a, b) -> Long.compare(a.nanos(), b.nanos()));

        Measurement median = results[RUNS / 2];

        String row = String.format(
                Locale.US,
                "W1,-,%s,%d,%.6f,%d,%d,%d%n",
                structure,
                n,
                median.nanos() / 1_000_000.0,
                median.steps(),
                median.moves(),
                median.comparisons()
        );

        csv.append(row);
        System.out.print(row);
    }

    private static int[] createQueries(int n) {
        Random random = new Random(42);
        int[] queries = new int[1000];

        for (int i = 0; i < 500; i++) {
            queries[i] = random.nextInt(n);
            queries[i + 500] = n + random.nextInt(n);
        }

        for (int i = queries.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);

            int temporary = queries[i];
            queries[i] = queries[j];
            queries[j] = temporary;
        }

        return queries;
    }
    private static Measurement runW2(
            String structure, int n, int[] queries
    ) {
        IntList list = createList(structure, n);
        int found = 0;

        long start = System.nanoTime();

        for (int value : queries) {
            if (list.contains(value)) {
                found++;
            }
        }
        long elapsed = System.nanoTime() - start;
        sink = found;

        if (found != 500) {
            throw new IllegalStateException("W2 result mismatch");
        }

        long expectedComparisons = 0;
        long expectedListSteps = 0;

        for (int value : queries) {
            if (value < n) {
                expectedComparisons += value + 1L;
                expectedListSteps += value;
            } else {
                expectedComparisons += n;
                expectedListSteps += n;
            }
        }

        long expectedSteps = structure.equals("DynamicArray")
                ? expectedComparisons
                : expectedListSteps;

        if (list.getSteps() != expectedSteps
                || list.getMoves() != 0
                || list.getComparisons() != expectedComparisons) {
            throw new IllegalStateException("W2 metrics mismatch");
        }

        return new Measurement(
                elapsed,
                list.getSteps(),
                list.getMoves(),
                list.getComparisons()
        );
    }

    private static void benchmarkW2(
            String structure, int n, int[] queries,
            StringBuilder csv
    ) {
        for (int warmup = 0; warmup < 5; warmup++) {
            runW2(structure, n, queries);
        }

        Measurement[] results = new Measurement[RUNS];

        for (int run = 0; run < RUNS; run++) {
            results[run] = runW2(structure, n, queries);
        }

        Arrays.sort(results,
                (a, b) -> Long.compare(a.nanos(), b.nanos()));

        Measurement median = results[RUNS / 2];

        String row = String.format(
                Locale.US,
                "W2,-,%s,%d,%.6f,%d,%d,%d%n",
                structure,
                n,
                median.nanos() / 1_000_000.0,
                median.steps(),
                median.moves(),
                median.comparisons()
        );

        csv.append(row);
        System.out.print(row);
    }
}


