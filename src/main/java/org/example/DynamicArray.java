package org.example;

public class DynamicArray {

    private int[] data = new int[10];
    private int size = 0;

    public void add(int x) {
        if (size == data.length) {
            int[] newData = new int[data.length * 2];


            for (int i = 0; i < size; i++) {
                newData[i] = data[i];
                steps++;
                moves++;
            }

            data = newData;
        }

        data[size] = x;
        size++;
    }


    public int get(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        steps++;
        return data[index];
    }

    public int size() {
        return size;
    }


    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        if (size == data.length) {
            int[] newData = new int[data.length * 2];

            for (int i = 0; i < size; i++) {
                newData[i] = data[i];
                steps++;
                moves++;
            }

            data = newData;
        }


        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
            steps++;
            moves++;
        }

        data[index] = x;
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        int removed = data[index];
        steps++;

        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
            steps++;
            moves++;
        }

        size--;
        return removed;
    }

    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            steps++;
            comparisons++;

            if (data[i] == x) {
                return true;
            }
        }

        return false;
    }

    private long steps = 0;
    private long moves = 0;
    private long comparisons = 0;

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