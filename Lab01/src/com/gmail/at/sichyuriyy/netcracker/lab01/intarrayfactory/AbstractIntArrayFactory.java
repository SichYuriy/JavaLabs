package com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory;

public abstract class AbstractIntArrayFactory {

    public int[] create(int length) {
        return new int[length];
    }

}
