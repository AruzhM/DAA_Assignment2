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

}
