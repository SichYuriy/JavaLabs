package com.gmail.at.sichyuriyy.netcracker.lab01.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.reflections.Reflections;

import com.gmail.at.sichyuriyy.netcracker.lab01.Main;
import com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory.AbstractIntArrayFactory;
import com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory.IntArrayFactory;
import com.gmail.at.sichyuriyy.netcracker.lab01.sorter.IntSorter;

import javafx.util.Pair;

public class SortAnalyser {
    
    private List<Pair<Integer, Integer>> sizeRepeatPairs;
    
    public SortAnalyser(List<Pair<Integer, Integer>> sizeRepeatPairs) {
        this.sizeRepeatPairs = sizeRepeatPairs;
    }
    
    
    
    public List<Long> getTime(AbstractIntArrayFactory factory, IntSorter sorter) {
        List<Long> result = new ArrayList<>();
        for (Pair<Integer, Integer> pair : sizeRepeatPairs) {
            result.add(StatUtils.getSortingTime(factory, sorter, pair.getKey(), pair.getValue()));
        }
        return result;
    }
    
    

}
