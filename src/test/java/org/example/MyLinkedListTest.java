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


    @Test
    void insertsAtBeginningMiddleAndEnd() {
        MyLinkedList list = new MyLinkedList();

        list.add(0, 20);
        list.add(0, 10);
        list.add(1, 15);
        list.add(3, 30);
        list.add(40);


        assertEquals(5, list.size());
        assertEquals(10, list.get(0));
        assertEquals(15, list.get(1));
        assertEquals(20, list.get(2));
        assertEquals(30, list.get(3));
        assertEquals(40, list.get(4));
    }

    @Test
    void rejectsInvalidInsertionIndices() {
        MyLinkedList list = new MyLinkedList();
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(-1, 99));
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(1, 99));

        list.add(10);

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(2, 99));

        assertEquals(1, list.size());
        assertEquals(10, list.get(0));
    }


    @Test
    void countsIndexedInsertionOperations() {
        MyLinkedList list = new MyLinkedList();
        list.add(10);
        list.add(20);
        list.add(30);

        list.resetMetrics();
        list.add(0, 5);

        assertEquals(0L, list.getSteps());
        assertEquals(2L, list.getMoves());
        assertEquals(0L, list.getComparisons());

        list.resetMetrics();
        list.add(2, 15);

        assertEquals(1L, list.getSteps());
        assertEquals(2L, list.getMoves());
        assertEquals(0L, list.getComparisons());

        list.resetMetrics();
        list.add(list.size(), 40);

        assertEquals(0L, list.getSteps());
        assertEquals(2L, list.getMoves());
        assertEquals(0L, list.getComparisons());
    }


    @Test
    void removesFromMiddleBeginningAndEnd() {
        MyLinkedList list = new MyLinkedList();
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);

        assertEquals(20, list.remove(1));
        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(1));
        assertEquals(40, list.get(2));

        assertEquals(40, list.remove(2));

        list.add(50);
        assertEquals(50, list.get(2));

        assertEquals(10, list.remove(0));
        assertEquals(30, list.remove(0));
        assertEquals(50, list.remove(0));
        assertEquals(0, list.size());

        list.add(60);
        assertEquals(1, list.size());
        assertEquals(60, list.get(0));
    }

    @Test
    void rejectsInvalidRemovalIndices() {
        MyLinkedList list = new MyLinkedList();

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.remove(0));

        list.add(10);

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.remove(1));

        assertEquals(1, list.size());
        assertEquals(10, list.get(0));
    }


    @Test
    void countsRemovalOperations() {
        MyLinkedList list = new MyLinkedList();
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);

        list.resetMetrics();
        assertEquals(30, list.remove(2));

        assertEquals(2L, list.getSteps());
        assertEquals(1L, list.getMoves());
        assertEquals(0L, list.getComparisons());

        list.resetMetrics();
        assertEquals(40, list.remove(2));

        assertEquals(2L, list.getSteps());
        assertEquals(2L, list.getMoves());

        list.resetMetrics();
        assertEquals(10, list.remove(0));

        assertEquals(1L, list.getSteps());
        assertEquals(1L, list.getMoves());

        list.resetMetrics();
        assertEquals(20, list.remove(0));

        assertEquals(1L, list.getSteps());
        assertEquals(2L, list.getMoves());
        assertEquals(0, list.size());
    }


}