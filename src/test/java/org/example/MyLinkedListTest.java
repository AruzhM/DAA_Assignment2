package org.example;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyLinkedListTest {

    @Test
    void startsEmpty() {
        MyLinkedList list = new MyLinkedList();

        assertEquals(0, list.size());
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.get(0));
    }

    @Test
    void appendsValuesInOrder() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);
        assertEquals(1, list.size());
        assertEquals(10, list.get(0));

        list.add(-5);
        list.add(10);

        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(-5, list.get(1));
        assertEquals(10, list.get(2));
    }


    @Test
    void rejectsInvalidIndices() {
        MyLinkedList list = new MyLinkedList();
        list.add(7);

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.get(1));
    }



    @Test
    void countsLinkUpdatesAndTraversal() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);
        assertEquals(2L, list.getMoves());

        list.resetMetrics();
        list.add(20);
        list.add(30);

        assertEquals(4L, list.getMoves());
        assertEquals(0L, list.getSteps());

        list.resetMetrics();

        assertEquals(30, list.get(2));
        assertEquals(2L, list.getSteps());
        assertEquals(0L, list.getMoves());
        assertEquals(0L, list.getComparisons());

        list.resetMetrics();

        assertEquals(0L, list.getSteps());
        assertEquals(0L, list.getMoves());
        assertEquals(0L, list.getComparisons());
        assertEquals(3, list.size());
    }

}
