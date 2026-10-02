package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;


public class HeapBuildBenchmark {
    private static final int[] SIZES = {100, 1000, 10000, 100000};
    private static final int RUNS = 5;

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
                "input,method,n,time_ms,steps,moves,comparisons\n"
        );

        for (int n : SIZES) {
            int[] randomValues = new int[n];
            int[] descendingValues = new int[n];
            Random random = new Random(42);

            for (int i = 0; i < n; i++) {
                randomValues[i] = random.nextInt();
                descendingValues[i] = n - i;
            }

            benchmark("random", "insert", randomValues, csv);
            benchmark("random", "floyd", randomValues, csv);

            benchmark("descending", "insert", descendingValues, csv);
            benchmark("descending", "floyd", descendingValues, csv);
        }

        Path output = Path.of("results", "heap_build.csv");
        Files.createDirectories(output.getParent());
        Files.writeString(output, csv.toString());

        System.out.println("Saved: " + output.toAbsolutePath());

    }
    private static Measurement run(
            String method, int[] values, int[] expected

    ) {
        MinHeap heap = new MinHeap();
        long elapsed;

        if (method.equals("insert")) {
            long start = System.nanoTime();

            for (int value : values) {
                heap.insert(value);
            }

            elapsed = System.nanoTime() - start;
        } else if (method.equals("floyd")) {
            long start = System.nanoTime();

            heap.buildHeap(values);

            elapsed = System.nanoTime() - start;
        } else {
            throw new IllegalArgumentException(
                    "Unknown method: " + method
            );
        }
        Measurement result = new Measurement(
                elapsed,
                heap.getSteps(),
                heap.getMoves(),
                heap.getComparisons()
        );

        if (heap.size() != values.length || !heap.isValidHeap()) {
            throw new IllegalStateException("Invalid constructed heap");
        }

        long checksum = 0;

        for (int value : expected) {
            int actual = heap.extractMin();

            if (actual != value) {
                throw new IllegalStateException(
                        "Heap construction contents mismatch"
                );
            }

            checksum += actual;
        }

        sink = checksum;

        if (heap.size() != 0) {
            throw new IllegalStateException("Heap is not empty");
        }

        return result;
    }

    private static void benchmark(
            String input, String method, int[] values,
            StringBuilder csv
    ) {
        int[] expected = values.clone();
        Arrays.sort(expected);

        for (int warmup = 0; warmup < 5; warmup++) {
            run(method, values, expected);
        }

        Measurement[] results = new Measurement[RUNS];

        for (int i = 0; i < RUNS; i++) {
            results[i] = run(method, values, expected);
        }

        Arrays.sort(results,
                (a, b) -> Long.compare(a.nanos(), b.nanos()));

        Measurement median = results[RUNS / 2];

        String row = String.format(
                Locale.US,
                "%s,%s,%d,%.6f,%d,%d,%d%n",
                input,
                method,
                values.length,
                median.nanos() / 1_000_000.0,
                median.steps(),
                median.moves(),
                median.comparisons()
        );

        csv.append(row);
        System.out.print(row);
    }
}



