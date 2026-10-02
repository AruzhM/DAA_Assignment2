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

}
