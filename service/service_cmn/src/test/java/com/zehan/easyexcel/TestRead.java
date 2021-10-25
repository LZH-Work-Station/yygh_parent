package com.zehan.easyexcel;


import com.alibaba.excel.EasyExcel;

public class TestRead {
    public static void main(String[] args) {
        ExcelListener excelListener = new ExcelListener();
        String fileName = "D:\\Project\\尚医通项目\\笔记\\笔记\\test.xlsx";
        EasyExcel.read(fileName, UserData.class, new ExcelListener()).sheet().doRead();
    }
}
