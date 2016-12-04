package com.gmail.at.sichyuriyy.netcracker.lab01.sorter;

public class InsertionSorterTest extends AbstractIntSorterTest {

    @Override
    protected IntSorter getTestedSorter() {
        return new InsertionSorter();
    }

}
