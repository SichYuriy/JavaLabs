package com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory;


@IntArrayFactory
public class AscRandLastArrayFactory extends AbstractIntArrayFactory {
    
    private int max = 1000;
    
    @Override 
    public int[] create(int length) {
        int []result = new int[length];
        for (int i = 0; i < result.length - 1; i++) {
            result[i] = i + 1;
        }
        
        result[length - 1] = (int) (Math.random() * max);
        return result;
    }
}
