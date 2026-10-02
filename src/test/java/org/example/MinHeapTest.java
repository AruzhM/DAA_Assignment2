package org.example;

import org.junit.jupiter.api.Test;

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

}
