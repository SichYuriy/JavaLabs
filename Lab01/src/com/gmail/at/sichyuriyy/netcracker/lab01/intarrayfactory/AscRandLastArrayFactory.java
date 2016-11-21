package com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory;

/**
 * @author Sych
 *
 */
@IntArrayFactory
public class AscRandLastArrayFactory extends AbstractIntArrayFactory {

    private int max = 1000;

    /**
     * Generate new int array with ascend placed elements and random last element
     * 
     * @see com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory.AbstractIntArrayFactory#create(int)
     */
    @Override
    public int[] create(int length) {
        int[] result = new int[length];
        for (int i = 0; i < result.length - 1; i++) {
            result[i] = i + 1;
        }

        result[length - 1] = (int) (Math.random() * max);
        return result;
    }
}
