package com.zehan.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

// 通过这个类进行监听，实现一行一行的读取excel中的内容
public class ExcelListener extends AnalysisEventListener<UserData> {
    // 一行一行读取数据
    @Override
    public void invoke(UserData userData, AnalysisContext analysisContext) {
        System.out.println(userData);
    }
    // 获得表头
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息: " + headMap);
    }

    // 读取之后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
