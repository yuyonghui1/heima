package com.itheima;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public class PoiDemo {
    /**
     * 写入excel
     * @param args
     */
    public static void main(String[] args) throws IOException {
        //创建工作薄对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表对象
        XSSFSheet sheet = workbook.createSheet("test_352");
        //创建行对象
        XSSFRow row = sheet.createRow(0);
        //创建单元格
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("username");
        XSSFCell cell2 = row.createCell(1);
        cell2.setCellValue("age");

        //创建行对象
        XSSFRow row1 = sheet.createRow(1);
        //创建单元格
        XSSFCell cell3 = row1.createCell(0);
        cell3.setCellValue("zhangsan");
        XSSFCell cell4 = row1.createCell(1);
        cell4.setCellValue(18);

        FileOutputStream outputStream = new FileOutputStream("C:\\Users\\sun\\Desktop\\test2.xlsx");

        workbook.write(outputStream);

        //关闭流
        outputStream.flush();
        outputStream.close();
        workbook.close();


    }
    /**
     * 读取excel
     * @param args
     * @throws IOException
     */
    public static void main2(String[] args) throws IOException {
        //1. 获取工作薄对象（excel对象）
        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\sun\\Desktop\\test.xlsx");
        //2. 获取工作表对象
        XSSFSheet sheet2 = workbook.getSheetAt(0);
        System.out.println(sheet2);

        for (Row row : sheet2) {
            for (Cell cell : row) {
                System.out.println(cell);
            }
        }
    }

    public static void main1(String[] args) throws IOException {
        //1. 工作薄对象（excel对象）
        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\sun\\Desktop\\test.xlsx");
        //2. 获取工作表对象
//        XSSFSheet sheet1 = workbook.getSheet("Sheet1");
////        System.out.println(sheet1);
        XSSFSheet sheet2 = workbook.getSheetAt(0);
        System.out.println(sheet2);
        //3. 获取行对象
        //根据索引获取行对象（行索引 从0 开始）
//        XSSFRow row = sheet2.getRow(0);
//        System.out.println(row);
        //获取最后一行行号
//        int lastRowNum = sheet2.getLastRowNum();
//        System.out.println(lastRowNum);
        for (int i = 0; i <= sheet2.getLastRowNum(); i++) {
            XSSFRow row = sheet2.getRow(i);
//            System.out.println(row);
            //row.getLastCellNum(): 获取有多少个单元格
            for (int j = 0; j < row.getLastCellNum() ; j++) {
                //4. 获取单元格对象
                Cell cell = row.getCell(j);
                //5. 获取单元格的数据
                int cellType = cell.getCellType();
                //如果是数值类型，修改为字符串类型
                if(cellType == Cell.CELL_TYPE_NUMERIC){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                }

                System.out.println(cell.getStringCellValue());
            }
        }





    }
}
