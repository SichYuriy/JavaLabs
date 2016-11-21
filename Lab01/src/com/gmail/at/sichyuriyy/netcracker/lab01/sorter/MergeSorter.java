package com.gmail.at.sichyuriyy.netcracker.lab01.sorter;

/**
 * @author Sych
 *
 */
public class MergeSorter implements IntSorter {

    /**
     * sort int array using merge sort
     * 
     * @see com.gmail.at.sichyuriyy.netcracker.lab01.sorter.IntSorter#sort(int[])
     */
    @Override
    public void sort(int[] arr) {
        if (arr.length < 2) {
            return;
        }
        int mid = arr.length / 2;
        int[] buff = new int[arr.length];
        mergeSort(arr, 0, mid, buff);
        mergeSort(arr, mid, arr.length, buff);
        merge(arr, 0, arr.length, buff);

    }

    /**
     * @param arr source array to sort
     * @param from start index including
     * @param to end index excluding
     * @param buff buffer array
     */
    private void mergeSort(int[] arr, int from, int to, int[] buff) {
        if (to - from < 2) {
            return;
        }
        int mid = (from + to) / 2;
        mergeSort(arr, from, mid, buff);
        mergeSort(arr, mid, to, buff);
        merge(arr, from, to, buff);
    }

    /**
     * @param arr source array to merge
     * @param from start index including
     * @param to end index excluding
     */
    private static void merge(int arr[], int from, int to, int[] buff) {
        int mid = (from + to) / 2;
        int leftIndex = from;
        int rightIndex = mid;
        int buffIndex = 0;
        while (leftIndex < mid && rightIndex < to) {
            if (arr[leftIndex] < arr[rightIndex]) {
                buff[buffIndex] = arr[leftIndex];
                leftIndex++;
            } else {
                buff[buffIndex] = arr[rightIndex];
                rightIndex++;
            }
            buffIndex++;
        }

        if (leftIndex == mid) {
            for (; rightIndex < to; rightIndex++) {
                buff[buffIndex] = arr[rightIndex];
                buffIndex++;
            }
        } else if (rightIndex == to) {
            for (; leftIndex < mid; leftIndex++) {
                buff[buffIndex] = arr[leftIndex];
                buffIndex++;
            }
        }

        System.arraycopy(buff, 0, arr, from, to - from);

    }

}
