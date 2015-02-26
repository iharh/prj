package com;

import org.junit.Test;
import org.junit.Ignore;

//import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

//import static org.hamcrest.CoreMatchers.is;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestPoi {
    private static final Logger log = LoggerFactory.getLogger(TestPoi.class);

    private static final int NUM_COL = 500;

    @Test
    public void testAttr() throws Exception {
        log.debug("testAttr");

        XSSFWorkbook wb = new XSSFWorkbook();

        XSSFSheet sheet = wb.createSheet("my sheet");
        assertNotNull(sheet);
        //XSSFSheet s2 = wb.createSheet("my sheet 2");
        //assertNotNull(s2);

        // title
        XSSFRow titleRow = sheet.createRow(0);

        XSSFCell titleRowId = titleRow.createCell(0);
        titleRowId.setCellValue("ROW_ID");

        XSSFCell titleVerb = titleRow.createCell(1);
        titleVerb.setCellValue("VERB");

        for (int i = 1; i <= NUM_COL; ++i) {
            XSSFCell titleAttr = titleRow.createCell(i + 1);
            titleAttr.setCellValue("ATTR_" + i);
        }

        //data
        XSSFRow dataRow = sheet.createRow(1);

        XSSFCell dataRowId = dataRow.createCell(0);
        dataRowId.setCellValue(1);

        XSSFCell dataVerb = dataRow.createCell(1);
        dataVerb.setCellValue("some text");

        for (int i = 1; i <= NUM_COL; ++i) {
            XSSFCell dataAttr = dataRow.createCell(i + 1);
            dataAttr.setCellValue(i);
        }

        wb.write(new FileOutputStream("attr" + NUM_COL + ".xlsx"));
    }

    @Ignore
    public void testVerb() throws Exception {
        log.debug("testVerb");

        XSSFWorkbook wb = new XSSFWorkbook();

        XSSFSheet sheet = wb.createSheet("my sheet");
        assertNotNull(sheet);
        //XSSFSheet s2 = wb.createSheet("my sheet 2");
        //assertNotNull(s2);

        // title
        XSSFRow titleRow = sheet.createRow(0);
        assertNotNull(titleRow);

        XSSFCell titleRowId = titleRow.createCell(0);
        assertNotNull(titleRowId);
        titleRowId.setCellValue("ROW_ID");

        for (int i = 1; i <= NUM_COL; ++i) {
            XSSFCell titleVerb = titleRow.createCell(i);
            titleVerb.setCellValue("VERB_" + i);
        }

        //data
        XSSFRow dataRow = sheet.createRow(1);
        assertNotNull(dataRow);

        XSSFCell dataRowId = dataRow.createCell(0);
        assertNotNull(dataRowId);
        dataRowId.setCellValue(1);

        for (int i = 1; i <= NUM_COL; ++i) {
            XSSFCell dataVerb = dataRow.createCell(i);
            dataVerb.setCellValue("some text " + i);
        }

        wb.write(new FileOutputStream("verb" + NUM_COL + ".xlsx"));
    }
}
