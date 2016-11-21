package com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory;


/**
 * @author Sych
 *
 */
@IntArrayFactory
public class DescArrayFactory extends AbstractIntArrayFactory {

    /**
     * Generate new array with descend placed elements
     * 
     * @see com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory.AbstractIntArrayFactory#create(int)
     */
    @Override
    public int[] create(int length) {
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = result.length - i;
        }

        return result;
    }

}
