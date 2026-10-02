package org.example;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicArrayTest {
    @Test
    void startsEmpty() {
        DynamicArray array = new DynamicArray();

        assertEquals(0, array.size());
        assertThrows(IndexOutOfBoundsException.class,
                () -> array.get(0));
    }

    @Test
    void preservesValuesAfterGrowing() {
        DynamicArray array = new DynamicArray();

        for (int i = 0; i < 25; i++) {
            array.add(i * 3);
        }

        assertEquals(25, array.size());

        for (int i = 0; i < 25; i++) {
            assertEquals(i * 3, array.get(i));
        }
    }

    @Test
    void rejectsInvalidIndices() {
        DynamicArray array = new DynamicArray();
        array.add(7);

        assertThrows(IndexOutOfBoundsException.class,
                () -> array.get(-1));
        assertThrows(IndexOutOfBoundsException.class,
                () -> array.get(1));
    }


    @Test
    void insertsAtBeginningMiddleAndEnd() {
        DynamicArray array = new DynamicArray();

        array.add(0, 20);
        array.add(0, 10);
        array.add(1, 15);
        array.add(3, 30);

        assertEquals(4, array.size());
        assertEquals(10, array.get(0));
        assertEquals(15, array.get(1));
        assertEquals(20, array.get(2));
        assertEquals(30, array.get(3));
    }

    @Test
    void insertsWhenArrayIsFull() {
        DynamicArray array = new DynamicArray();

        for (int i = 0; i < 10; i++) {
            array.add(i);
        }

        array.add(5, 99);

        assertEquals(11, array.size());

        for (int i = 0; i < 5; i++) {
            assertEquals(i, array.get(i));
        }

        assertEquals(99, array.get(5));

        for (int i = 6; i < 11; i++) {
            assertEquals(i - 1, array.get(i));
        }
    }

    @Test
    void rejectsInvalidInsertionIndices() {
        DynamicArray array = new DynamicArray();

        assertThrows(IndexOutOfBoundsException.class,
                () -> array.add(-1, 99));
        assertThrows(IndexOutOfBoundsException.class,
                () -> array.add(1, 99));

        assertEquals(0, array.size());
    }

    @Test
    void removesFromMiddleBeginningAndEnd() {
        DynamicArray array = new DynamicArray();
        array.add(10);
        array.add(20);
        array.add(30);
        array.add(40);

        assertEquals(20, array.remove(1));
        assertEquals(3, array.size());
        assertEquals(10, array.get(0));
        assertEquals(30, array.get(1));
        assertEquals(40, array.get(2));


        assertEquals(10, array.remove(0));
        assertEquals(40, array.remove(1));
        assertEquals(1, array.size());
        assertEquals(30, array.get(0));


        assertEquals(30, array.remove(0));
        assertEquals(0, array.size());

        array.add(50);
        assertEquals(50, array.get(0));
    }

    @Test
    void searchesOnlyStoredElements() {
        DynamicArray array = new DynamicArray();

        assertFalse(array.contains(0));

        array.add(-5);
        array.add(7);
        array.add(7);

        assertTrue(array.contains(-5));
        assertTrue(array.contains(7));
        assertFalse(array.contains(99));

        array.remove(1);
        assertTrue(array.contains(7));

        array.remove(1);
        assertFalse(array.contains(7));
    }


    @Test
    void rejectsInvalidRemovalIndices() {
        DynamicArray array = new DynamicArray();

        assertThrows(IndexOutOfBoundsException.class,
                () -> array.remove(0));

        array.add(10);

        assertThrows(IndexOutOfBoundsException.class,
                () -> array.remove(-1));
        assertThrows(IndexOutOfBoundsException.class,
                () -> array.remove(1));

        assertEquals(1, array.size());
        assertEquals(10, array.get(0));
    }

    @Test
    void countsAccessAndSearchOperations() {
        DynamicArray array = new DynamicArray();
        array.add(10);
        array.add(20);
        array.add(30);
        array.resetMetrics();


        assertEquals(20, array.get(1));
        assertEquals(1L, array.getSteps());
        assertEquals(0L, array.getMoves());
        assertEquals(0L, array.getComparisons());

        array.resetMetrics();

        assertTrue(array.contains(20));
        assertEquals(2L, array.getSteps());
        assertEquals(2L, array.getComparisons());

        array.resetMetrics();

        assertFalse(array.contains(99));
        assertEquals(3L, array.getSteps());
        assertEquals(3L, array.getComparisons());
    }

    @Test
    void countsInsertionAndRemovalOperations() {
        DynamicArray array = new DynamicArray();
        array.add(10);
        array.add(20);
        array.add(30);
        array.resetMetrics();

        array.add(1, 99);

        assertEquals(2L, array.getSteps());
        assertEquals(2L, array.getMoves());
        assertEquals(0L, array.getComparisons());

        array.resetMetrics();

        assertEquals(99, array.remove(1));

        assertEquals(3L, array.getSteps());
        assertEquals(2L, array.getMoves());
        assertEquals(0L, array.getComparisons());
    }

    @Test
    void countsResizingAndResetsMetrics() {
        DynamicArray array = new DynamicArray();

        for (int i = 0; i < 10; i++) {
            array.add(i);
        }

        array.resetMetrics();
        array.add(10);

        assertEquals(10L, array.getSteps());
        assertEquals(10L, array.getMoves());
        assertEquals(0L, array.getComparisons());

        array.contains(0);
        array.resetMetrics();

        assertEquals(0L, array.getSteps());
        assertEquals(0L, array.getMoves());
        assertEquals(0L, array.getComparisons());
        assertEquals(11, array.size());
    }

    @Test
    void matchesArrayListOnRandomOperations() {
        DynamicArray actual = new DynamicArray();
        ArrayList<Integer> expected = new ArrayList<>();
        Random random = new Random(42);

        for (int step = 0; step < 2000; step++) {
            int operation = random.nextInt(5);
            int value = random.nextInt(101) - 50;

            if (operation == 0) {
                actual.add(value);
                expected.add(value);


            } else if (operation == 1) {
                int index = random.nextInt(expected.size() + 1);

                actual.add(index, value);
                expected.add(index, value);

            } else if (operation == 2 && !expected.isEmpty()) {
                int index = random.nextInt(expected.size());

                int expectedRemoved = expected.remove(index);
                assertEquals(expectedRemoved, actual.remove(index));

            } else if (operation == 3 && !expected.isEmpty()) {
                int index = random.nextInt(expected.size());

                assertEquals(expected.get(index).intValue(),
                        actual.get(index));

            } else {
                assertEquals(expected.contains(value),
                        actual.contains(value));
            }

            assertEquals(expected.size(), actual.size(),
                    "Size mismatch at step " + step);

            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i).intValue(), actual.get(i),
                        "Value mismatch at step " + step + ", index " + i);
            }
        }
    }

}