package com.lyzx.poi;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class PoiTest {


    @Test
    public void test1() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File("/Users/xiang/Downloads/excel1.xlsx"));
        System.out.println(workbook.getNumberOfSheets());
    }
}
