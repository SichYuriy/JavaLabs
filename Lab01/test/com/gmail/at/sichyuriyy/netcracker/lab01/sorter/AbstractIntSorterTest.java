package com.gmail.at.sichyuriyy.netcracker.lab01.sorter;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;


public abstract class AbstractIntSorterTest {
    
    
    private int[] sortedAscArr = { 1, 2, 3, 4, 5, 6, 8, 9, 11 };
    private int[] sortedDescArr = { 11, 9, 8, 6, 5, 4, 3, 2, 1 };
    private int[] constArr = { 2, 2, 2, 2, 2, 2 };
    private int[] randArr = { 1, 4, 2, 6, 1, 2, 3 };
    private String sortedAscArrRes = "[1, 2, 3, 4, 5, 6, 8, 9, 11]";
    private String sortedDescArrRes = "[1, 2, 3, 4, 5, 6, 8, 9, 11]";
    private String constArrRes = "[2, 2, 2, 2, 2, 2]";
    private String randArrRes = "[1, 1, 2, 2, 3, 4, 6]";
    
    protected abstract IntSorter getTestedSorter();
    
    @Test
    public void testSort_AscArr() {
        int[] arr = Arrays.copyOf(sortedAscArr, sortedAscArr.length);
        IntSorter intSorter = getTestedSorter();
        intSorter.sort(arr);
        
        String actual = Arrays.toString(arr);
        String expected = sortedAscArrRes;
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSort_DescArr() {
        int[] arr = Arrays.copyOf(sortedDescArr, sortedDescArr.length);
        IntSorter intSorter = getTestedSorter();
        intSorter.sort(arr);
        
        String actual = Arrays.toString(arr);
        String expected = sortedDescArrRes;
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSort_ConstArr() {
        int[] arr = Arrays.copyOf(constArr, constArr.length);
        IntSorter intSorter = getTestedSorter();
        intSorter.sort(arr);
        
        String actual = Arrays.toString(arr);
        String expected = constArrRes;
        
        assertEquals(expected, actual);
    }
    
   
    
    @Test
    public void testSort_RandArr() {
        int[] arr = Arrays.copyOf(randArr, randArr.length);
        IntSorter intSorter = getTestedSorter();
        intSorter.sort(arr);
        
        String actual = Arrays.toString(arr);
        String expected = randArrRes;
        
        assertEquals(expected, actual);
    }

}
