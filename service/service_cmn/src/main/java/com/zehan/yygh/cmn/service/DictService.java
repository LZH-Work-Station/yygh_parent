package com.zehan.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zehan.yygh.model.cmn.Dict;

import java.util.List;

public interface DictService extends IService<Dict> {
    // 根据ID查询子数据列表
    List<Dict> findChildData(Long id);
}
