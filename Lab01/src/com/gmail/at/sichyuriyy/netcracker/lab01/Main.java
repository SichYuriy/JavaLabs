package com.gmail.at.sichyuriyy.netcracker.lab01;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.reflections.Reflections;

import com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory.AbstractIntArrayFactory;
import com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory.IntArrayFactory;
import com.gmail.at.sichyuriyy.netcracker.lab01.sorter.IntSorter;
import com.gmail.at.sichyuriyy.netcracker.lab01.statistics.SortAnalyser;

import javafx.util.Pair;

public class Main {

    static final int INPUT_SHEET_NUM = 0;
    static final int SIZE_LIST_ROW_NUM = 1;
    static final int REPEAT_LIST_ROW_NUM = 3;
    static final String EXCEL_FILE_PATH = "SortStatistics.xlsx";

    public static void main(String[] args) throws IOException {
        List<Pair<Integer, Integer>> sizeRepeatPairs = getExcelInput(EXCEL_FILE_PATH);
        SortAnalyser analyser = new SortAnalyser(sizeRepeatPairs);
        List<IntSorter> sorters = getAllSorters();
        List<AbstractIntArrayFactory> factories = getAllFactories();

        for (AbstractIntArrayFactory factory : factories) {
            List<List<Double>> sorterTimeList = new ArrayList<>();
            List<String> sorterNameList = new ArrayList<>();
            for (IntSorter sorter : sorters) {
                sorterTimeList.add(analyser.getTime(factory, sorter)
                        .stream()
                        .map((val) -> {return (double) val / 10e6;})
                        .collect(Collectors.toList()));
                sorterNameList.add(sorter.getClass().getSimpleName());
            }
            List<Integer> sizeList = sizeRepeatPairs
                    .stream()
                    .map((val)->val.getKey()).
                    collect(Collectors.toList());
            writeSheetToFile(EXCEL_FILE_PATH, factory.getClass().getSimpleName(), sorterTimeList, sizeList,
                    sorterNameList);
        }

    }

    public static List<Pair<Integer, Integer>> getExcelInput(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(new File(fileName));
        ArrayList<Integer> sizeList = new ArrayList<>();
        ArrayList<Integer> repeatList = new ArrayList<>();
        XSSFWorkbook workBook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workBook.getSheetAt(INPUT_SHEET_NUM);
        Row row = sheet.getRow(SIZE_LIST_ROW_NUM);
        for (Cell cell : row) {
            sizeList.add((int) cell.getNumericCellValue());
        }
        row = sheet.getRow(REPEAT_LIST_ROW_NUM);
        for (Cell cell : row) {
            repeatList.add((int) cell.getNumericCellValue());
        }
        workBook.close();
        if (sizeList.size() != repeatList.size()) {
            throw new IllegalArgumentException("size args length must be equal to repeat args length");
        }
        List<Pair<Integer, Integer>> result = new ArrayList<>();
        for (int i = 0; i < sizeList.size(); i++) {
            result.add(new Pair<>(sizeList.get(i), repeatList.get(i)));
        }
        return result;
    }

    public static List<IntSorter> getAllSorters() {
        List<IntSorter> sorters = new ArrayList<>();
//        ReflectionUtils reflectionUtils = new ReflectionUtils();
//        List<Class<?>> clazzes = reflectionUtils.findAllSubTypes("com.gmail.at.sichyuriyy.netcracker.lab01",
//                IntSorter.class);
        
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

    public static List<AbstractIntArrayFactory> getAllFactories() {
        List<AbstractIntArrayFactory> factories = new ArrayList<>();
//        ReflectionUtils reflectionUtils = new ReflectionUtils();
//        List<Class<?>> clazzes = reflectionUtils.findAllAnnotatedClasses("com.gmail.at.sichyuriyy.netcracker.lab01",
//                IntArrayFactory.class);
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

    public static void writeSheetToFile(String fileName, String sheetName, List<List<Double>> sorterTimeList,
            List<Integer> sizeList, List<String> sorterNameList) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(fileName)));
                FileOutputStream fos = new FileOutputStream(new File(fileName))) {
            System.out.println(sheetName);
            Row row;
            int rowIndex;
            int cellIndex;
            
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }
            row = sheet.createRow(0);
            cellIndex = 1;
            for (Integer size : sizeList) {
                row.createCell(cellIndex++).setCellValue(size);
            }
            
            rowIndex = 1;
            for (int i = 0; i < sorterTimeList.size(); i++) {
                cellIndex = 0;
                row = sheet.createRow(rowIndex++);
                row.createCell(cellIndex++).setCellValue(sorterNameList.get(i));
                for (Double time : sorterTimeList.get(i)) {
                    row.createCell(cellIndex++).setCellValue(time);
                }
            }
            
            workbook.write(fos);

        } catch (FileNotFoundException e) {
            System.out.println(fileName + " - file not found");
            Logger.getLogger(Main.class).error(e);
        } catch (IOException e) {
            Logger.getLogger(Main.class).error(e);
        }
    }

}
