package com.gmail.at.sichyuriyy.netcracker.lab01.statistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.reflections.Reflections;

import com.gmail.at.sichyuriyy.netcracker.lab01.Main;
import com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory.AbstractIntArrayFactory;
import com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory.IntArrayFactory;
import com.gmail.at.sichyuriyy.netcracker.lab01.sorter.IntSorter;

import javafx.util.Pair;

public class StatisticsCollector {

    static final int INPUT_SHEET_NUM = 0;
    static final int SIZE_LIST_ROW_NUM = 1;
    static final int REPEAT_LIST_ROW_NUM = 3;

    public void generateStatistics(String inputFile, String outputFile) throws IOException {
        List<Pair<Integer, Integer>> sizeRepeatPairs = getExcelInput(inputFile);
        SortAnalyser analyser = new SortAnalyser(sizeRepeatPairs);
        List<IntSorter> sorters = getAllSorters();
        List<AbstractIntArrayFactory> factories = getAllFactories();

        for (AbstractIntArrayFactory factory : factories) {
            List<List<Double>> sorterTimeList = new ArrayList<>();
            List<String> sorterNameList = new ArrayList<>();
            for (IntSorter sorter : sorters) {
                sorterTimeList.add(analyser.getTime(factory, sorter).stream().map((val) -> {
                    return (double) val / 10e6;
                }).collect(Collectors.toList()));
                sorterNameList.add(sorter.getClass().getSimpleName());
            }
            List<Integer> sizeList = sizeRepeatPairs.stream().map((val) -> val.getKey()).collect(Collectors.toList());
            ExcelUtils excelUtils = new ExcelUtils();
            excelUtils.writeSheetToFile(outputFile, factory.getClass().getSimpleName(), sorterTimeList, sizeList,
                    sorterNameList);
        }
    }

    public List<Pair<Integer, Integer>> getExcelInput(String fileName) throws IOException {
        ExcelUtils excelUtils = new ExcelUtils();
        ArrayList<Integer> sizeList = excelUtils.readRowOfIntegers(fileName, SIZE_LIST_ROW_NUM, INPUT_SHEET_NUM);
        ArrayList<Integer> repeatList = excelUtils.readRowOfIntegers(fileName, REPEAT_LIST_ROW_NUM, INPUT_SHEET_NUM);

        if (sizeList.size() != repeatList.size()) {
            throw new IllegalArgumentException("size args length must be equal to repeat args length");
        }
        List<Pair<Integer, Integer>> result = new ArrayList<>();
        for (int i = 0; i < sizeList.size(); i++) {
            result.add(new Pair<>(sizeList.get(i), repeatList.get(i)));
        }
        return result;
    }

    private List<IntSorter> getAllSorters() {
        List<IntSorter> sorters = new ArrayList<>();
        // ReflectionUtils reflectionUtils = new ReflectionUtils();
        // List<Class<?>> clazzes =
        // reflectionUtils.findAllSubTypes("com.gmail.at.sichyuriyy.netcracker.lab01",
        // IntSorter.class);

        Reflections reflections = new Reflections("com.gmail.at.sichyuriyy.netcracker.lab01.sorter");
        Set<Class<? extends IntSorter>> clazzes = reflections.getSubTypesOf(IntSorter.class);

        for (Class<? extends IntSorter> clazz : clazzes) {
            try {
                sorters.add(clazz.newInstance());
            } catch (InstantiationException e) {
                System.out.println(clazz.getName() + " cann't be casted to IntSorter");
                Logger.getLogger(Main.class).error(e);
            } catch (IllegalAccessException e) {
                System.out.println(clazz.getName() + " - nullary constructor is not accessible");
                Logger.getLogger(Main.class).error(e);
            }
        }
        return sorters;
    }

    private List<AbstractIntArrayFactory> getAllFactories() {
        List<AbstractIntArrayFactory> factories = new ArrayList<>();
        // ReflectionUtils reflectionUtils = new ReflectionUtils();
        // List<Class<?>> clazzes =
        // reflectionUtils.findAllAnnotatedClasses("com.gmail.at.sichyuriyy.netcracker.lab01",
        // IntArrayFactory.class);
        Reflections reflections = new Reflections("com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory");
        Set<Class<?>> clazzes = reflections.getTypesAnnotatedWith(IntArrayFactory.class);
        for (Class<?> clazz : clazzes) {
            try {
                factories.add((AbstractIntArrayFactory) clazz.newInstance());
            } catch (InstantiationException e) {
                System.out.println(clazz.getName() + " cann't be casted to AbstractIntArrayFactory");
                Logger.getLogger(Main.class).error(e);
            } catch (IllegalAccessException e) {
                System.out.println(clazz.getName() + " - nullary constructor is not accessible");
                Logger.getLogger(Main.class).error(e);
            }
        }
        return factories;
    }

}
