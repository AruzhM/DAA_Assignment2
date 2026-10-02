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


    public void add(int index,int x) {

            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index: " + index);
            }

            if (index == size){
                add(x);
                return;
            }

            Node newNode = new Node(x);

            if (index == 0) {
                newNode.next = head;
                moves++;

                head = newNode;
                moves++;
            } else {
                Node previous = head;

                for(int i = 0; i < index - 1; i++) {
                    previous = previous.next;
                    steps++;
                }

                newNode.next = previous.next;
                moves++;

                previous.next = newNode;
                moves++;
            }

            size++;
    }

    public int remove(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("index: " + index);

            }

            Node removed;

            if (index == 0) {
                removed = head;
                head = head.next;
                steps++;
                moves++;

                if (size == 1) {
                    tail = null;
                    moves++;
                }
            } else {
                Node previous = head;

                for (int i = 0; i < index -1; i++) {
                    previous = previous.next;
                    steps++;
                }

                removed = previous.next;
                steps++;

                previous.next = removed.next;
                moves++;

                if (removed == tail) {
                    tail = previous;
                    moves++;
                }
            }

            size--;
            return removed.value;
    }
}
