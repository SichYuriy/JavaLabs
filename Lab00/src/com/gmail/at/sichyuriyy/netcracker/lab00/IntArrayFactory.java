package com.gmail.at.sichyuriyy.netcracker.lab00;

import java.util.Arrays;
import java.util.Random;

public class IntArrayFactory {
    
    private int max = 100;
    
    public int[] createSortedAscArr(int length) {
        int []result = new int[length];
        for (int i = 0; i < result.length; i++) {
            result[i] = i + 1;
        }
        return result;
    }
    
    public int[] createSortedAscArrWithRandLastEl(int length) {
        int []result = new int[length];
        for (int i = 0; i < result.length - 1; i++) {
            result[i] = i + 1;
        }
        
        result[length - 1] = (int) (Math.random() * max);
        return result;
    }
    
    public int[] createSortedDescArr(int length) {
        int []result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = result.length - i;
        }
        
        return result;
    }
    
    public int[] createRandArr(int length) {
        Random rand =  new Random();
        int []result = new int[length];
        for (int i = result.length - 1; i >= 0; i--) {
            result[i] = rand.nextInt(max);
        }
        
        return result;
    }
    
    public static void main(String []args) {
        int length = 10;
        IntArrayFactory factory = new IntArrayFactory();
        int []arr1 = factory.createSortedAscArr(length);
        int []arr2 = factory.createSortedDescArr(length);
        int []arr3 = factory.createSortedAscArrWithRandLastEl(length);
        int []arr4 = factory.createRandArr(length);
        
        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr2));
        System.out.println(Arrays.toString(arr3));
        System.out.println(Arrays.toString(arr4));
    }

}
