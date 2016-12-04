package com.gmail.at.sichyuriyy.netcracker.lab01.statistics;

import com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory.AbstractIntArrayFactory;
import com.gmail.at.sichyuriyy.netcracker.lab01.sorter.IntSorter;

public class StatUtils {
    
    public static long getSortingTime(AbstractIntArrayFactory factory, IntSorter sorter, int size, int repeat) {
        long totalTime = 0;
        for (int i = 0; i < repeat; i++) {
            int []arr = factory.create(size);
            long startTime = System.nanoTime();
            sorter.sort(arr);
            long endTime = System.nanoTime();
            totalTime += endTime - startTime;
        }
        long result = totalTime / repeat;
        return result;
    }

}
