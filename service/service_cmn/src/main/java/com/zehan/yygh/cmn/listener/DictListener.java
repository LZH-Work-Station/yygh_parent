package com.zehan.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.zehan.yygh.cmn.mapper.DictMapper;
import com.zehan.yygh.model.cmn.Dict;
import com.zehan.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;


public class DictListener extends AnalysisEventListener<DictEeVo> {
    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper){
        this.dictMapper = dictMapper;
    }

    // 一行一行的读取, 在数据库中insert read到的数据
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo, dict);
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
