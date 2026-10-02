package org.example;

public class MyLinkedList {

    private static class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }
        private Node head;
        private Node tail;
        private int size = 0;

        private long steps = 0;
        private long moves = 0;
        private long comparisons = 0;

        public int size() {
            return size;
        }


    public void add(int x) {
        Node newNode = new Node(x);

        if (head == null) {
            head = newNode;
            moves++;
        } else {
            tail.next = newNode;
            moves++;
        }

        tail = newNode;
        moves++;
        size++;
    }

    public int get(int index) {
        if (index < 0 ||index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        Node current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
            steps++;
        }

        return current.value;
    }

    public long getSteps() {
        return steps;
    }

    public long getMoves() {
        return moves;
    }

    public long getComparisons() {
        return comparisons;
    }

    public void resetMetrics() {
        steps = 0;
        moves = 0;
        comparisons = 0;
    }
}
