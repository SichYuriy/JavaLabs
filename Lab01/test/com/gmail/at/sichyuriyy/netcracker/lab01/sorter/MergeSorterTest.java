package com.gmail.at.sichyuriyy.netcracker.lab01.sorter;

public class MergeSorterTest extends AbstractIntSorterTest {

    @Override
    protected IntSorter getTestedSorter() {
        return new MergeSorter();
    }

}
