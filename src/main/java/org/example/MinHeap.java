package org.example;

public class MinHeap {
    private int[] data = new int[10];
    private int size = 0;

    private long steps = 0;
    private long moves = 0;
    private long comparisons = 0;

    public int size() {
        return size;
    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }

        steps++;
        return data[0];
    }

    public long getSteps() {
        return steps;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getMoves() {
        return moves;
    }

    public void resetMetrics() {
        steps = 0;
        moves = 0;
        comparisons = 0;
    }

    public void insert(int x) {
        if (size == data.length) {
            int[] newData = new int[data.length * 2];

            for (int i = 0; i < size; i++) {
                newData[i] = data[i];
                steps++;
                moves++;
            }
            data = newData;
        }
        int index = size;
        size++;

        while (index > 0) {
            int parent = (index - 1) / 2;

            int parentValue = data[parent];
            steps++;
            comparisons++;

            if (parentValue <= x) {
                break;
            }

            data[index] = parentValue;
            moves++;

            index = parent;
        }

        data[index] = x;
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }

        int minimum = data[0];
        steps++;
        size--;

        if (size > 0) {
            data[0] = data[size];
            steps++;
            moves++;

            siftDown(0);
        }
        return minimum;
    }

    private void siftDown(int index) {
        int value = data[index];
        steps++;

        while (index < size / 2) {
            int left = 2 * index + 1;
            int right = left + 1;

            int smallerChild = left;
            int smallerValue = data[left];
            steps++;

            if (right < size) {
                int rightValue = data[right];
                steps++;
                comparisons++;

                if (rightValue < smallerValue) {
                    smallerChild = right;
                    smallerValue = rightValue;
                }
            }

            comparisons++;

            if (value <= smallerValue) {
                break;
            }

            data[index] = smallerValue;
            moves++;

            index = smallerChild;
        }

        data[index] = value;
        moves++;
    }

    boolean isValidHeap() {
        for (int child = 1; child < size; child++) {
            int parent = (child - 1) / 2;

            if (data[parent] > data[child]) {
                return false;
            }
        }

        return true;
    }
}

