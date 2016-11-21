package com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory;


@IntArrayFactory
public class DescArrayFactory extends AbstractIntArrayFactory {

    @Override
    public int[] create(int length) {
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = result.length - i;
        }

        return result;
    }

}
