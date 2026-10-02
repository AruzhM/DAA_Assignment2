package org.example;

public class DynamicArray {
    private int[] data = new int[10];
    private int size = 0;

    public void add(int x) {
        if (size == data.length) {
            int[] newData = new int[data.length * 2];

            for (int i = 0; i < size; i++) {
                newData[i] = data[i];
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
            }

            data = newData;
        }

        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }

        data[index] = x;
        size++;
    }

}