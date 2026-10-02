package org.example;

import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class MemoryBenchmark {
    private static final int[] SIZES = {100, 1000, 10000, 100000};

    public static void main(String[] args) throws IOException {
        Path directory = Path.of("results");
        Files.createDirectories(directory);

        String vmDetails =
                "Java version: " + System.getProperty("java.version")
                        + "\nVM: " + System.getProperty("java.vm.name")
                        + "\nOS: " + System.getProperty("os.name")
                        + "\nArchitecture: " + System.getProperty("os.arch")
                        + "\n\n" + VM.current().details();

        System.out.println(vmDetails);

        Files.writeString(
                directory.resolve("jol_vm.txt"),
                vmDetails
        );

        StringBuilder csv = new StringBuilder(
                "structure,n,total_bytes,object_count,bytes_per_element\n"
        );

        StringBuilder footprints = new StringBuilder();

        for (int n : SIZES) {
            DynamicArray array = new DynamicArray();
            MyLinkedList list = new MyLinkedList();
            MinHeap heap = new MinHeap();

            for (int i = 0; i < n; i++) {
                array.add(i);
                list.add(i);
                heap.insert(i);
            }

            measure("DynamicArray", n, array, csv, footprints);
            measure("MyLinkedList", n, list, csv, footprints);
            measure("MinHeap", n, heap, csv, footprints);
        }

        Path output = directory.resolve("memory.csv");

        Files.writeString(output, csv.toString());
        Files.writeString(
                directory.resolve("jol_footprints.txt"),
                footprints.toString()
        );

        System.out.println("Saved: " + output.toAbsolutePath());
    }

    private static void measure(
            String structure,
            int n,
            Object instance,
            StringBuilder csv,
            StringBuilder footprints
    ) {
        GraphLayout layout = GraphLayout.parseInstance(instance);

        long totalBytes = layout.totalSize();
        long objectCount = layout.totalCount();

        String row = String.format(
                Locale.US,
                "%s,%d,%d,%d,%.2f%n",
                structure,
                n,
                totalBytes,
                objectCount,
                totalBytes / (double) n
        );

        csv.append(row);
        System.out.print(row);

        footprints.append(structure)
                .append(", n=")
                .append(n)
                .append("\n")
                .append(layout.toFootprint())
                .append("\n");
    }
}