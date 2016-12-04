package com.gmail.at.sichyuriyy.netcracker.lab01.sorter;

public class BubbleDownSorterTest extends AbstractIntSorterTest{

    @Override
    protected IntSorter getTestedSorter() {
        return new BubbleDownSorter();
    }

}
