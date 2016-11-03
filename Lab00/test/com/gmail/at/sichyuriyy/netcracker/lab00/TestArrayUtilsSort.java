package com.gmail.at.sichyuriyy.netcracker.lab00;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TestArrayUtilsSort {

    private int[] sortedAscArr = { 1, 2, 3, 4, 5, 6 };
    private int[] sortedDescArr = { 6, 5, 4, 3, 2, 1 };
    private int[] constArr = { 2, 2, 2, 2, 2, 2 };
    private int[] randArr = { 1, 4, 2, 6, 1, 2, 3 };
    private String sortedAscArrRes = "[1, 2, 3, 4, 5, 6]";
    private String sortedDescArrRes = "[1, 2, 3, 4, 5, 6]";
    private String constArrRes = "[2, 2, 2, 2, 2, 2]";
    private String randArrRes = "[1, 1, 2, 2, 3, 4, 6]";

    // BulbSortUp

    @Test
    public void testBulbSortUpAscArr() {
        int[] arr = Arrays.copyOf(sortedAscArr, sortedAscArr.length);
        ArrayUtils.bulbSortUp(arr);
        assertEquals(sortedAscArrRes, Arrays.toString(arr));
    }

    @Test
    public void testBulbSortUpDesArr() {
        int[] arr = Arrays.copyOf(sortedDescArr, sortedDescArr.length);
        ArrayUtils.bulbSortUp(arr);
        assertEquals(sortedDescArrRes, Arrays.toString(arr));
    }

    @Test
    public void testBulbSortUpConstArr() {
        int[] arr = Arrays.copyOf(constArr, constArr.length);
        ArrayUtils.bulbSortUp(arr);
        assertEquals(constArrRes, Arrays.toString(arr));
    }

    @Test
    public void testBulbSortUpRandArr() {
        int[] arr = Arrays.copyOf(randArr, randArr.length);
        ArrayUtils.bulbSortUp(arr);
        assertEquals(randArrRes, Arrays.toString(arr));
    }

    // BulbSortDown

    @Test
    public void testBulbSortDownAscArr() {
        int[] arr = Arrays.copyOf(sortedAscArr, sortedAscArr.length);
        ArrayUtils.bulbSortDown(arr);
        assertEquals(sortedAscArrRes, Arrays.toString(arr));
    }

    @Test
    public void testBulbSortDownDesArr() {
        int[] arr = Arrays.copyOf(sortedDescArr, sortedDescArr.length);
        ArrayUtils.bulbSortDown(arr);
        assertEquals(sortedDescArrRes, Arrays.toString(arr));
    }

    @Test
    public void testBulbSortDownConstArr() {
        int[] arr = Arrays.copyOf(constArr, constArr.length);
        ArrayUtils.bulbSortDown(arr);
        assertEquals(constArrRes, Arrays.toString(arr));
    }

    @Test
    public void testBulbSortDownRandArr() {
        int[] arr = Arrays.copyOf(randArr, randArr.length);
        ArrayUtils.bulbSortDown(arr);
        assertEquals(randArrRes, Arrays.toString(arr));
    }
    
    // InsertionSort

    @Test
    public void testInsertionSortAscArr() {
        int[] arr = Arrays.copyOf(sortedAscArr, sortedAscArr.length);
        ArrayUtils.insertionSort(arr);
        assertEquals(sortedAscArrRes, Arrays.toString(arr));
    }

    @Test
    public void testInsertionSortDesArr() {
        int[] arr = Arrays.copyOf(sortedDescArr, sortedDescArr.length);
        ArrayUtils.insertionSort(arr);
        assertEquals(sortedDescArrRes, Arrays.toString(arr));
    }

    @Test
    public void testInsertionSortConstArr() {
        int[] arr = Arrays.copyOf(constArr, constArr.length);
        ArrayUtils.insertionSort(arr);
        assertEquals(constArrRes, Arrays.toString(arr));
    }

    @Test
    public void testInsertionSortRandArr() {
        int[] arr = Arrays.copyOf(randArr, randArr.length);
        ArrayUtils.insertionSort(arr);
        assertEquals(randArrRes, Arrays.toString(arr));
    }
    
    // MergeSort
    
    @Test
    public void testMergeSortAscArr() {
        int[] arr = Arrays.copyOf(sortedAscArr, sortedAscArr.length);
        ArrayUtils.mergeSort(arr);
        assertEquals(sortedAscArrRes, Arrays.toString(arr));
    }

    @Test
    public void testMergeSortDesArr() {
        int[] arr = Arrays.copyOf(sortedDescArr, sortedDescArr.length);
        ArrayUtils.mergeSort(arr);
        assertEquals(sortedDescArrRes, Arrays.toString(arr));
    }

    @Test
    public void testMergeSortConstArr() {
        int[] arr = Arrays.copyOf(constArr, constArr.length);
        ArrayUtils.mergeSort(arr);
        assertEquals(constArrRes, Arrays.toString(arr));
    }

    @Test
    public void testMergeSortRandArr() {
        int[] arr = Arrays.copyOf(randArr, randArr.length);
        ArrayUtils.mergeSort(arr);
        assertEquals(randArrRes, Arrays.toString(arr));
    }
    
    // QuickSort
    
    @Test
    public void testQuickSortAscArr() {
        int[] arr = Arrays.copyOf(sortedAscArr, sortedAscArr.length);
        ArrayUtils.quickSort(arr);
        assertEquals(sortedAscArrRes, Arrays.toString(arr));
    }

    @Test
    public void testQuickSortDesArr() {
        int[] arr = Arrays.copyOf(sortedDescArr, sortedDescArr.length);
        ArrayUtils.quickSort(arr);
        assertEquals(sortedDescArrRes, Arrays.toString(arr));
    }

    @Test
    public void testQuickSortConstArr() {
        int[] arr = Arrays.copyOf(constArr, constArr.length);
        ArrayUtils.quickSort(arr);
        assertEquals(constArrRes, Arrays.toString(arr));
    }

    @Test
    public void testQuickSortRandArr() {
        int[] arr = Arrays.copyOf(randArr, randArr.length);
        ArrayUtils.quickSort(arr);
        assertEquals(randArrRes, Arrays.toString(arr));
    }
    
    // StandartSort
    
    @Test
    public void testStandartSortAscArr() {
        int[] arr = Arrays.copyOf(sortedAscArr, sortedAscArr.length);
        ArrayUtils.sort(arr);
        assertEquals(sortedAscArrRes, Arrays.toString(arr));
    }

    @Test
    public void testStandartSortDesArr() {
        int[] arr = Arrays.copyOf(sortedDescArr, sortedDescArr.length);
        ArrayUtils.sort(arr);
        assertEquals(sortedDescArrRes, Arrays.toString(arr));
    }

    @Test
    public void testStandartSortConstArr() {
        int[] arr = Arrays.copyOf(constArr, constArr.length);
        ArrayUtils.sort(arr);
        assertEquals(constArrRes, Arrays.toString(arr));
    }

    @Test
    public void testStandartSortRandArr() {
        int[] arr = Arrays.copyOf(randArr, randArr.length);
        ArrayUtils.sort(arr);
        assertEquals(randArrRes, Arrays.toString(arr));
    }
    

}
