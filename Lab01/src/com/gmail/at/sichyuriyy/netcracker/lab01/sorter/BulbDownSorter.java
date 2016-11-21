package com.gmail.at.sichyuriyy.netcracker.lab01.sorter;

public class BulbDownSorter implements IntSorter {

    @Override
    public void sort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = arr.length - 1; j > i; j--) {
                if (arr[j] < arr[j - 1]) {
                    swap(arr, j, j - 1);
                }
            }
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int p = arr[i];
        arr[i] = arr[j];
        arr[j] = p;
    }

}
