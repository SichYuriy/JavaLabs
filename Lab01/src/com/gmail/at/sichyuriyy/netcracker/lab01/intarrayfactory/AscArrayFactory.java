package com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory;

/**
 * @author Sych
 *
 */
@IntArrayFactory
public class AscArrayFactory extends AbstractIntArrayFactory {

    /**
     * Generate new array with ascend placed elements
     * 
     * @see com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory.AbstractIntArrayFactory#create(int)
     */
    @Override
    public int[] create(int length) {
        int[] result = new int[length];
        for (int i = 0; i < result.length; i++) {
            result[i] = i + 1;
        }
        return result;
    }

}
