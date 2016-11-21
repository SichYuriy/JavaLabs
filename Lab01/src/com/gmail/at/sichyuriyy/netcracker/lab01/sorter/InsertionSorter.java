package com.gmail.at.sichyuriyy.netcracker.lab01.sorter;

/**
 * @author Sych
 *
 */
public class InsertionSorter implements IntSorter {

    /**
     * sort int array using insertion sort
     * 
     * @see com.gmail.at.sichyuriyy.netcracker.lab01.sorter.IntSorter#sort(int[])
     */
    @Override
    public void sort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 0 && arr[j] < arr[j - 1]; j--) {
                swap(arr, j, j - 1);
            }
        }
    }

    /**
     * @param arr
     * @param i
     * @param j
     */
    private void swap(int[] arr, int i, int j) {
        int p = arr[i];
        arr[i] = arr[j];
        arr[j] = p;
    }

}
