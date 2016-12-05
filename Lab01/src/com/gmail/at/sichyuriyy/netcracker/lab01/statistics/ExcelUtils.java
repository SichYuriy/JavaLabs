package com.gmail.at.sichyuriyy.netcracker.lab01.statistics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gmail.at.sichyuriyy.netcracker.lab01.Main;

public class ExcelUtils {
    
    public ArrayList<Integer> readRowOfIntegers(String fileName, int rowNum, int sheetNum) throws IOException {
        ArrayList<Integer> result = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(fileName));
                XSSFWorkbook workBook = new XSSFWorkbook(fis)) {
            XSSFSheet sheet = workBook.getSheetAt(sheetNum);
            Row row = sheet.getRow(rowNum);
            for (Cell cell : row) {
                result.add((int) cell.getNumericCellValue());
            }
        }
        return result;
    }
    
    public void writeSheetToFile(String fileName, String sheetName, List<List<Double>> sorterTimeList,
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
