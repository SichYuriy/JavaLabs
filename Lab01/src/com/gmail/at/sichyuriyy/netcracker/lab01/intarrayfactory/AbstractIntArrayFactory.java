package com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory;

/**
 * @author Sych
 * @version 1.0.1
 * 
 * 
 */
public abstract class AbstractIntArrayFactory {

    /**
     * @param length array length
     * @return new int array with defined length
     */
    public int[] create(int length) {
        return new int[length];
    }

}
