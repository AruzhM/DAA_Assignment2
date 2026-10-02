package org.example;

import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;
import java.util.Random;


import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;


public class MinHeapTest {

    @Test
    void startsEmpty() {
        MinHeap heap = new MinHeap();

        assertEquals(0, heap.size());
        assertThrows(IllegalStateException.class,
                () -> heap.peekMin());
    }

    @Test
    void tracksMinimumAfterEachInsertion() {
        MinHeap heap = new MinHeap();

        int[] values = {10, 4, 20, -5, -5, 0};
        int[] minimums = {10, 4, 4, -5, -5, -5};

        for (int i = 0; i < values.length; i++) {
            heap.insert(values[i]);

            assertEquals(i + 1, heap.size());
            assertEquals(minimums[i], heap.peekMin());
        }
    }


    @Test
    void growsBeyondInitialCapacity() {
        MinHeap heap = new MinHeap();

        for (int i = 30; i >= 1; i--) {
            heap.insert(i);

            assertEquals(i, heap.peekMin());
        }

        assertEquals(30, heap.size());
    }


    @Test
    void countsInsertionAndPeekOperations() {
        MinHeap heap = new MinHeap();
        heap.insert(10);
        heap.resetMetrics();

        heap.insert(5);

        assertEquals(1L, heap.getSteps());
        assertEquals(1L, heap.getMoves());
        assertEquals(1L, heap.getComparisons());

        heap.resetMetrics();

        assertEquals(5, heap.peekMin());
        assertEquals(1L, heap.getSteps());
        assertEquals(0L, heap.getMoves());
        assertEquals(0L, heap.getComparisons());

        heap.resetMetrics();

        assertEquals(0L, heap.getSteps());
        assertEquals(0L, heap.getMoves());
        assertEquals(0L, heap.getComparisons());
        assertEquals(2, heap.size());
    }


    @Test
    void rejectsExtractionFromEmptyHeap() {
        MinHeap heap = new MinHeap();

        assertThrows(IllegalStateException.class,
                () -> heap.extractMin());

        assertEquals(0, heap.size());
    }


    @Test
    void extractsValuesInSortedOrder() {
        MinHeap heap = new MinHeap();

        int[] values = {10, 4, 20, -5, 4, 0, 8};
        int[] expected = {-5, 0, 4, 4, 8, 10, 20};

        for (int value : values) {
            heap.insert(value);
        }
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], heap.peekMin());
            assertEquals(expected[i], heap.extractMin());
            assertEquals(expected.length - i - 1, heap.size());
        }

        assertThrows(IllegalStateException.class,
                () -> heap.peekMin());
        assertThrows(IllegalStateException.class,
                () -> heap.extractMin());
    }

    @Test
    void countsExtractionOperations() {
        MinHeap heap = new MinHeap();
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        heap.resetMetrics();

        assertEquals(1, heap.extractMin());

        assertEquals(4L, heap.getSteps());
        assertEquals(3L, heap.getMoves());
        assertEquals(1L, heap.getComparisons());

        heap.resetMetrics();

        assertEquals(2, heap.extractMin());
        assertEquals(3L, heap.getSteps());
        assertEquals(2L, heap.getMoves());
        assertEquals(0L, heap.getComparisons());

        heap.resetMetrics();

        assertEquals(3, heap.extractMin());
        assertEquals(1L, heap.getSteps());
        assertEquals(0L, heap.getMoves());
        assertEquals(0L, heap.getComparisons());
    }

    @Test
    void canBeReusedAfterRemovingLastElement() {
        MinHeap heap = new MinHeap();

        heap.insert(7);
        assertEquals(7, heap.extractMin());
        assertEquals(0, heap.size());

        heap.insert(-3);
        heap.insert(9);

        assertEquals(-3, heap.extractMin());
        assertEquals(9, heap.extractMin());
        assertEquals(0, heap.size());
    }


    @Test
    void preservesHeapPropertyAndExtractsInSortedOrder() {
        MinHeap heap = new MinHeap();
        Random random = new Random(42);

        for (int i = 0; i < 1000; i++) {
            heap.insert(random.nextInt(2001) - 1000);

            assertTrue(heap.isValidHeap(),
                    "Heap property broken after insertion " + i);
        }

        int previous = Integer.MIN_VALUE;

        for (int i = 0; i < 1000; i++) {
            int current = heap.extractMin();

            assertTrue(previous <= current,
                    "Extraction order broken at step " + i);
            assertTrue(heap.isValidHeap(),
                    "Heap property broken after extraction " + i);
            assertEquals(999 - i, heap.size());

            previous = current;
        }
    }

    @Test
    void matchesPriorityQueueOnRandomOperations() {
        MinHeap actual = new MinHeap();
        PriorityQueue<Integer> expected = new PriorityQueue<>();
        Random random = new Random(42);

        for (int step = 0; step < 2000; step++) {
            if (expected.isEmpty() || random.nextBoolean()) {
                int value = random.nextInt();

                actual.insert(value);
                expected.add(value);
            } else {
                assertEquals(expected.remove().intValue(),
                        actual.extractMin(),
                        "Extraction mismatch at step " + step);
            }

            assertEquals(expected.size(), actual.size(),
                    "Size mismatch at step " + step);
            assertTrue(actual.isValidHeap(),
                    "Heap property broken at step " + step);

            if (!expected.isEmpty()) {
                assertEquals(expected.peek().intValue(),
                        actual.peekMin(),
                        "Minimum mismatch at step " + step);
            }
        }

        while (!expected.isEmpty()) {
            assertEquals(expected.remove().intValue(),
                    actual.extractMin());
            assertTrue(actual.isValidHeap());
        }

        assertEquals(0, actual.size());
        assertThrows(IllegalStateException.class,
                () -> actual.extractMin());
    }

    @Test
    void buildsHeapFromArrayWithoutChangingInput() {
        MinHeap heap = new MinHeap();

        int[] values = {9, -3, 7, 7, 0, 15, -8};
        int[] original = values.clone();
        int[] sorted = values.clone();
        Arrays.sort(sorted);

        heap.buildHeap(values);

        assertArrayEquals(original, values);
        assertEquals(values.length, heap.size());
        assertTrue(heap.isValidHeap());

        values[0] = Integer.MIN_VALUE;

        for (int value : sorted) {
            assertEquals(value, heap.extractMin());
            assertTrue(heap.isValidHeap());
        }

        assertEquals(0, heap.size());
    }

    @Test
    void buildHeapHandlesEmptyAndSingleElementArrays() {
        MinHeap heap = new MinHeap();

        heap.buildHeap(new int[0]);

        assertEquals(0, heap.size());
        assertTrue(heap.isValidHeap());
        assertThrows(IllegalStateException.class,
                () -> heap.peekMin());

        heap.insert(5);
        assertEquals(5, heap.extractMin());

        heap.buildHeap(new int[]{-7});

        assertEquals(1, heap.size());
        assertEquals(-7, heap.peekMin());
        assertTrue(heap.isValidHeap());
        assertEquals(-7, heap.extractMin());
    }

    @Test
    void buildHeapReplacesContentsAndSupportsInsertion() {
        MinHeap heap = new MinHeap();
        heap.insert(-100);

        heap.buildHeap(new int[]{
                10, 9, 8, 7, 6, 5, 4, 3, 2, 1
        });

        assertEquals(10, heap.size());
        assertEquals(1, heap.peekMin());
        assertTrue(heap.isValidHeap());
        heap.insert(0);

        assertEquals(11, heap.size());
        assertTrue(heap.isValidHeap());

        for (int i = 0; i <= 10; i++) {
            assertEquals(i, heap.extractMin());
            assertTrue(heap.isValidHeap());
        }
        assertEquals(0, heap.size());
    }

    @Test
    void buildsHeapFromRandomValues() {
        MinHeap heap = new MinHeap();
        Random random = new Random(42);

        int[] values = new int[1000];

        for (int i = 0; i < values.length; i++) {
            values[i] = random.nextInt();
        }

        int[] sorted = values.clone();
        Arrays.sort(sorted);

        heap.buildHeap(values);

        assertEquals(values.length, heap.size());
        assertTrue(heap.isValidHeap());

        for (int value : sorted) {
            assertEquals(value, heap.extractMin());
            assertTrue(heap.isValidHeap());
        }

        assertEquals(0, heap.size());
    }

}
