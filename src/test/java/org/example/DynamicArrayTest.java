package org.example;
import org.junit.jupiter.api.Test;

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

}
