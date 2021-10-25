package com.zehan.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zehan.yygh.common.result.Result;
import com.zehan.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    // 根据ID查询子数据列表
    List<Dict> findChildData(Long id);
    // 导出用户字典
    void exportDictData(HttpServletResponse response);
    // 导入数据字典
    Result importDictData(MultipartFile file);
}
