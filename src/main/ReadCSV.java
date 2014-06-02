/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/**
 *
 * @author Joshepw
 */
public class ReadCSV {
    public void printCSV(String filename){
        List sheetData = new ArrayList();
        FileInputStream file = null;
        
        try {
            file = new FileInputStream(filename);
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator rows = sheet.rowIterator();
            
            while (rows.hasNext()) {
                HSSFRow row = (HSSFRow) rows.next();
                Iterator cells = row.cellIterator();
 
                List data = new ArrayList();
                while (cells.hasNext()) {
                    HSSFCell cell = (HSSFCell) cells.next();
                    data.add(cell);
                }
                sheetData.add(data);
            }
        } catch (IOException e) {
            System.out.println("ERROR : " + e.getMessage());
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException ex) {
                    System.out.println("ERROR : " + ex.getMessage());
                }
            }
        }
        showExcelData(sheetData);
    }
    
    private void showExcelData(List sheetData) {
        for (int i = 0; i < sheetData.size(); i++) {
            List list = (List) sheetData.get(i);
            for (int j = 0; j < list.size(); j++) {
                HSSFCell cell = (HSSFCell) list.get(j);
                String pd = (cell.getCellType()==0)? "" + ((int) cell.getNumericCellValue()) : cell.toString();
                System.out.print(pd);
                if (j < list.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("");
        }
    }
    
    public List[] getExelData(String filename) {
        List sheetData = new ArrayList();
        FileInputStream file = null;
        
        try {
            file = new FileInputStream(filename);
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator rows = sheet.rowIterator();
            
            while (rows.hasNext()) {
                HSSFRow row = (HSSFRow) rows.next();
                Iterator cells = row.cellIterator();
 
                List data = new ArrayList();
                while (cells.hasNext()) {
                    HSSFCell cell = (HSSFCell) cells.next();
                    data.add(cell);
                }
                sheetData.add(data);
            }
        } catch (IOException e) {
            System.out.println("ERROR : " + e.getMessage());
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException ex) {
                    System.out.println("ERROR : " + ex.getMessage());
                }
            }
        }
        
        List[] stringData = new ArrayList[sheetData.size()];        
        for (int i = 0; i < sheetData.size(); i++) {
            List list = (List) sheetData.get(i);
            stringData[i] = new ArrayList();
            for (int j = 0; j < list.size(); j++) {
                HSSFCell cell = (HSSFCell) list.get(j);
                String pd = (cell.getCellType()==0)? "" + ((int) cell.getNumericCellValue()) : cell.toString();
                stringData[i].add(pd);
            }
        }
        return stringData;
    }
}
