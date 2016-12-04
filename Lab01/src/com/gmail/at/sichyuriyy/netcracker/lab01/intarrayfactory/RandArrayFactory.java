package com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory;

import java.util.Random;


/**
 * @author Sych
 *
 */
@IntArrayFactory
public class RandArrayFactory extends AbstractIntArrayFactory {

    private int max = 1000_000;

    /**
     * Generate new array with random elements
     * 
     * @see com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory.AbstractIntArrayFactory#create(int)
     */
    @Override
    public int[] create(int length) {
        Random rand = new Random();
        int[] result = new int[length];
        for (int i = result.length - 1; i >= 0; i--) {
            result[i] = rand.nextInt(max);
        }

        return result;
    }

}
