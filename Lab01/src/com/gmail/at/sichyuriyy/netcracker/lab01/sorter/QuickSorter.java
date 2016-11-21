package com.gmail.at.sichyuriyy.netcracker.lab01.sorter;

/**
 * @author Sych
 *
 */
public class QuickSorter implements IntSorter {

    /**
     * sort int array using quick sort
     * 
     * @see com.gmail.at.sichyuriyy.netcracker.lab01.sorter.IntSorter#sort(int[])
     */
    @Override
    public void sort(int[] arr) {
        quickSort(arr, 0, arr.length);
    }

    /**
     * @param arr array to sort
     * @param from start index including
     * @param to end index excluding
     */
    private void quickSort(int[] arr, int from, int to) {
        if (to - from < 2) {
            return;
        }
        int mid = partition(arr, from, to);
        quickSort(arr, from, mid);
        quickSort(arr, mid + 1, to);
    }

    /**
     * @param arr array to partition
     * @param from start index including
     * @param to end index excluding
     * @return index of pivot element
     */
    private int partition(int[] arr, int from, int to) {
        int size = to - from;
        int midIndex = from + (int) (Math.random() * size);
        int midVal = arr[midIndex];
        swap(arr, midIndex, to - 1);
        int swapIndex = from;
        for (int i = from; i < to - 1; i++) {
            if (arr[i] < midVal) {
                swap(arr, swapIndex, i);
                swapIndex++;
            }
        }
        swap(arr, swapIndex, to - 1);

        return swapIndex;
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
