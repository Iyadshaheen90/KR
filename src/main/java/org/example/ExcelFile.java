package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressBar;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import javax.swing.*;

/**
 * ToDO
 * using poi xssf to write to, and read from, an xlsx file excel.
 */
public class ExcelFile{
    private File file;
    private int slots;
    private int deleteFromRow;
    private int deleteToRow;
    private int updateFromRow;
    private int updateToRow;
    private String name;
    private ProgressBar pBar;
    private String sheetName;
    private boolean paid;
    private String paymentMethod;
    private ObservableList<ExcelRow> ol = FXCollections.observableArrayList();

    //Constructor of class will be used as a empty constructor for side purposes
    public ExcelFile(){

    }
    //Constructor of class will be used in controller for Updating Payment data
    public ExcelFile(File file, int updateFromRow, int updateToRow, ProgressBar pBar, String sheetName, String paymentMethod, boolean paid) {
        this.file = file;
        this.updateFromRow = updateFromRow;
        this.updateToRow = updateToRow;
        this.pBar = pBar;
        this.sheetName = sheetName;
        this.paymentMethod = paymentMethod;
        this.paid = paid;
    }
    //Constructor of class will be used in controller for writing data
    public ExcelFile(File file, int slots, String name, ProgressBar pBar, String sheetName, boolean paid, String paymentMethod) {
        this.file = file;
        this.name = name;
        this.slots = slots;
        this.pBar = pBar;
        this.sheetName = sheetName;
        this.paid = paid;
        this.paymentMethod = paymentMethod;
    }
    //Constructor of class will be used in controller for deleting rows from and existing sheet
    public ExcelFile(File file, int deleteFromRow, int deleteToRow, ProgressBar pBar, String sheetName) {
        this.file = file;
        this.deleteFromRow = deleteFromRow;
        this.deleteToRow = deleteToRow;
        this.pBar = pBar;
        this.sheetName = sheetName;
    }

    public ObservableList<ExcelRow> getOl() {
        return ol;
    }

    public void initOl(String filePath, String sheet){
        ol.clear();
        try {
            FileInputStream inputStream1 = new FileInputStream((filePath));
            Workbook workbook = WorkbookFactory.create(inputStream1);
            Sheet currentSheet = workbook.getSheetAt(workbook.getSheetIndex(sheet));
            for (int i = 0; i <= currentSheet.getLastRowNum(); i++){
                if(currentSheet.getRow(i)!=null) {
                    ol.add(new ExcelRow(i+1,
                            String.valueOf((int)currentSheet.getRow(i).getCell(0).getNumericCellValue()),
                            currentSheet.getRow(i).getCell(1).getStringCellValue(),
                            currentSheet.getRow(i).getCell(2).getStringCellValue(),
                            currentSheet.getRow(i).getCell(3).getStringCellValue()));
                }
                else{
                    ol.add(new ExcelRow(i+1,
                            "",
                            "",
                            "",
                            ""));
                }
            }

            int occurrences = Collections.frequency(ol, ol.contains("Dex '1'"));
            System.out.println("this contains " + occurrences + " slots of this buyer");
        } catch (IOException | EncryptedDocumentException ex) {
            ex.printStackTrace();
        }
    }

    protected ArrayList<String> getSheets(String filePath){
        System.out.println("accessed get sheets section");
        ArrayList<String> sheets = new ArrayList<>();
        int numberOfSheets;
        try {
            FileInputStream inputStream1 = new FileInputStream((filePath));
            Workbook workbook = WorkbookFactory.create(inputStream1);
            numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i<numberOfSheets; i++){
                sheets.add(workbook.getSheetName(i));
            }
        } catch (IOException | EncryptedDocumentException ex) {
            ex.printStackTrace();
        }
        return sheets;
    }
    public void updateRowsInFile() {
        try {
            System.out.println("i am in Update section");
            FileInputStream inputStream = new FileInputStream((file.getAbsolutePath()));
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(workbook.getSheetIndex(sheetName));
            CellStyle style = workbook.createCellStyle();
            if(!paid) {
                style.setBorderBottom(BorderStyle.THIN);
                style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style.setBorderRight(BorderStyle.THIN);
                style.setRightBorderColor(IndexedColors.BLACK.getIndex());
                style.setBorderTop(BorderStyle.THIN);
                style.setTopBorderColor(IndexedColors.BLACK.getIndex());
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            if(paid){
                style.setBorderBottom(BorderStyle.THIN);
                style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style.setBorderRight(BorderStyle.THIN);
                style.setRightBorderColor(IndexedColors.BLACK.getIndex());
                style.setBorderTop(BorderStyle.THIN);
                style.setTopBorderColor(IndexedColors.BLACK.getIndex());
                style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            System.out.println("updating data in "+ sheetName + " sheet in the workbook");
            if (sheet.getRow(updateFromRow-1)!=null && sheet.getRow(updateToRow-1)!=null ) {
                pBar.setVisible(true);
                SwingWorker<Void, Double> worker = new SwingWorker<>() {
                    int ctr=0;
                    @Override
                    protected Void doInBackground() throws Exception {
                        for (int i = updateFromRow-1; i < updateToRow; i++) {
                            ctr++;
                            sheet.getRow(i).getCell(0).setCellStyle(style);
                            sheet.getRow(i).getCell(1).setCellStyle(style);
                            sheet.getRow(i).getCell(2).setCellStyle(style);
                            if(paid) {
                                sheet.getRow(i).getCell(2).setCellValue("Paid");
                            }
                            if(!paid){
                                sheet.getRow(i).getCell(2).setCellValue("");
                            }
                            sheet.getRow(i).getCell(3).setCellStyle(style);
                            if(paid) {
                                sheet.getRow(i).getCell(3).setCellValue(paymentMethod);
                            }
                            if(!paid){
                                sheet.getRow(i).getCell(3).setCellValue(paymentMethod);
                            }
                            publish((double) (100*ctr)/(updateToRow-updateFromRow + 1));

                        }
                        inputStream.close();
                        FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
                        workbook.write(outputStream);
                        workbook.close();
                        outputStream.close();
                        return null;
                    }
                    @Override
                    protected void process(List<Double> chunks) {
                        for (Double i : chunks){
                            pBar.setProgress(i/100);
                        }
                    }

                    @Override
                    protected void done() {
                        System.out.println("Done");
                        pBar.setVisible(false);
                        pBar.setProgress(0);
                    }
                };
                worker.execute();
            }

            else{
                AlertBox alert = new AlertBox();
                alert.setMessage("Rows range specified does not exist in spreadsheet!");
                alert.displayPopup();
            }

        } catch (IOException | EncryptedDocumentException ex) {
            ex.printStackTrace();
        }
    }

    //TODO under construction ****
    public void deleteRowsInFile() {
        try {
            System.out.println("i am in Delete section");
            FileInputStream inputStream = new FileInputStream((file.getAbsolutePath()));
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(workbook.getSheetIndex(sheetName));
            int lastRow = sheet.getLastRowNum();
            //System.out.println("writing data to "+ sheetName + " sheet in the workbook");
            //if its a new sheet with no rows then we want our row count at -1 so the first entry
            //goes on the first row not the second due to ++rowcount
//            sheet.removeRow(sheet.getRow(1));
            if (sheet.getRow(deleteFromRow-1)!=null && sheet.getRow(deleteToRow-1)!=null ) {
                pBar.setVisible(true);
                SwingWorker<Void, Double> worker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        for (int i = deleteFromRow - 1; i < deleteToRow; i++) {
                            sheet.removeRow(sheet.getRow(i));
                            publish((double) (100 * i) / (deleteToRow - deleteFromRow + 1));
                        }

                        //shifting rows
//                        if(deleteToRow-1!=sheet.getLastRowNum()) {
//                        sheet.shiftRows(deleteToRow, sheet.getLastRowNum(), -((deleteToRow - deleteFromRow) + 1));
//                        }
//                        sheet.shiftRows(deleteToRow,sheet.getLastRowNum(),-((deleteToRow-deleteFromRow)+1));
                        //TODO fix the numbers after shifting is done
                        //delete trailing empty rows
                        System.out.println(sheet.getLastRowNum()+"booya");
                        //update slot numbers with a for loop
                        inputStream.close();
                        FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
                        workbook.write(outputStream);
                        outputStream.close();
                        workbook.close();
                        return null;
                    }
                    @Override
                    protected void process(List<Double> chunks) {
                        for (Double i : chunks){
                            pBar.setProgress(i/100);
                        }
                    }

                    @Override
                    protected void done() {
                        System.out.println("Done");
                        pBar.setVisible(false);
                        pBar.setProgress(0);
                    }
                };
                worker.execute();
            }

            else{
                AlertBox alert = new AlertBox();
                alert.setMessage("Rows range specified does not exist in spreadsheet!");
                alert.displayPopup();
            }

        } catch (IOException | EncryptedDocumentException ex) {
            ex.printStackTrace();
        }
    }
    
    public void writeToSheet()
    {
        pBar.setVisible(true);
        SwingWorker<Void,Double> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground(){
                try {
                    FileInputStream inputStream = new FileInputStream((file.getAbsolutePath()));
                    Workbook workbook = WorkbookFactory.create(inputStream);
                    Sheet sheet = workbook.getSheetAt(workbook.getSheetIndex(sheetName));
                    CellStyle style = workbook.createCellStyle();
                    if(!paid) {
                        style.setBorderBottom(BorderStyle.THIN);
                        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                        style.setBorderRight(BorderStyle.THIN);
                        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
                        style.setBorderTop(BorderStyle.THIN);
                        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
                        style.setFillForegroundColor(IndexedColors.RED.getIndex());
                        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    }
                    else if(paid){
                        style.setBorderBottom(BorderStyle.THIN);
                        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                        style.setBorderRight(BorderStyle.THIN);
                        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
                        style.setBorderTop(BorderStyle.THIN);
                        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
                        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    }
                    System.out.println("writing data to "+ sheetName + " sheet in the workbook");
                    int rowCount = sheet.getLastRowNum();
                    //if its a new sheet with no rows(last row is 0 and is empty) then we want our row count at -1 so the first entry
                    //goes on the first row not the second due to ++rowcount
                    if(rowCount == 0 && sheet.getRow(rowCount)==null)
                    {
                        rowCount = rowCount-1;
                    }

                    int slotNumber;

                    for (int i = 0; i < slots; i++) {
                        slotNumber = i+1;
                        System.out.println(rowCount);
                        Row row = sheet.createRow(++rowCount);
                        int columnCount = 0;
                        Cell cell = row.createCell(columnCount);
                        cell.setCellStyle(style);
                        cell.setCellValue(rowCount+1);
                        cell = row.createCell(1);
                        cell.setCellStyle(style);
                        cell.setCellValue(name + " '" + slotNumber + "'");
                        cell = row.createCell(2);
                        if(paid){
                            cell.setCellValue("Paid");
                        }
                        if(!paid){
                            cell.setCellValue("");
                        }
                        cell.setCellStyle(style);
                        cell = row.createCell(3);
                        cell.setCellValue(paymentMethod);
                        cell.setCellStyle(style);
                        publish((double) (100*slotNumber)/slots);
                    }

                    inputStream.close();
                    FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();

                } catch (IOException | EncryptedDocumentException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void process(List<Double> chunks) {
                for (Double i : chunks){
                    pBar.setProgress(i/100);
                }
            }

            @Override
            protected void done() {
                System.out.println("Done");
                pBar.setVisible(false);
                pBar.setProgress(0);
            }
        };
        worker.execute();
    }
}