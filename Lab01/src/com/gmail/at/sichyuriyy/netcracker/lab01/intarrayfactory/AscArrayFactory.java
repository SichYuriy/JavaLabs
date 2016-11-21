package com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory;

@IntArrayFactory
public class AscArrayFactory extends AbstractIntArrayFactory {

    @Override
    public int[] create(int length) {
        int[] result = new int[length];
        for (int i = 0; i < result.length; i++) {
            result[i] = i + 1;
        }
        return result;
    }

}
