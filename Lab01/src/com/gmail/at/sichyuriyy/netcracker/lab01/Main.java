package com.gmail.at.sichyuriyy.netcracker.lab01;

import java.io.IOException;

import com.gmail.at.sichyuriyy.netcracker.lab01.statistics.StatisticsCollector;

public class Main {

    static final int INPUT_SHEET_NUM = 0;
    static final int SIZE_LIST_ROW_NUM = 1;
    static final int REPEAT_LIST_ROW_NUM = 3;
    static final String EXCEL_FILE_PATH = "SortStatistics.xlsx";

    public static void main(String[] args) throws IOException {
        
        StatisticsCollector statisticsCollector = new StatisticsCollector();
        statisticsCollector.generateStatistics(EXCEL_FILE_PATH, EXCEL_FILE_PATH);

    }

    

    

    

}
