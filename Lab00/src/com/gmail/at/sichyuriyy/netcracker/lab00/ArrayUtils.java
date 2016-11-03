package com.gmail.at.sichyuriyy.netcracker.lab00;

import java.util.Arrays;

public class ArrayUtils {

    public static void bulbSortUp(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }
    }

    public static void bulbSortDown(int[] arr) {
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

    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 0 && arr[j] < arr[j - 1]; j--) {
                swap(arr, j, j - 1);
            }
        }
    }

    public static void mergeSort(int[] arr) {
        if (arr.length < 2) {
            return;
        }
        int mid = arr.length / 2;
        int []buff = new int[arr.length];
        mergeSort(arr, 0, mid, buff);
        mergeSort(arr, mid, arr.length, buff);
        merge(arr, 0, arr.length, buff);

    }

    private static void mergeSort(int[] arr, int from, int to, int[] buff) {
        if (to - from < 2) {
            return;
        }
        int mid = (from + to) / 2;
        mergeSort(arr, from, mid, buff);
        mergeSort(arr, mid, to, buff);
        merge(arr, from, to, buff);
    }

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
    
    public static void quickSort(int []arr) {
       quickSort(arr, 0, arr.length);
    }
    
    private static void quickSort(int []arr, int from, int to) {
        if (to - from < 2) {
            return;
        }
        int mid = partition(arr, from, to);
        quickSort(arr, from, mid);
        quickSort(arr, mid + 1, to);
    }
    
    private static int partition(int []arr, int from, int to) {
        int size = to - from;
        int midIndex = from +  (int)(Math.random() * size);
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
    
    public static void sort(int []arr) {
        Arrays.sort(arr);
    }

}
