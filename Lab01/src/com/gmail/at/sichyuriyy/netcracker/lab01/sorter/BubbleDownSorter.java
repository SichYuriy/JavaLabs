package com.gmail.at.sichyuriyy.netcracker.lab01.sorter;

/**
 * @author Sych
 *
 */
public class BubbleDownSorter implements IntSorter {

    /**
     * sort int array using bulb sort
     * 
     * @see com.gmail.at.sichyuriyy.netcracker.lab01.sorter.IntSorter#sort(int[])
     */
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
