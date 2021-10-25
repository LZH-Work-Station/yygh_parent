package com.zehan.yygh.cmn.controller;

import com.zehan.yygh.cmn.service.DictService;
import com.zehan.yygh.common.result.Result;
import com.zehan.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class DictController {
    @Autowired
    private DictService dictService;

    // 导出数据字典接口
    @GetMapping("exportData")
    // 这个response是为了实现下载
    public void exportDict(HttpServletResponse response){
        dictService.exportDictData(response);
    }

    // 导入数据字典
    @PostMapping("importData")
    public Result importDict(MultipartFile file){
        dictService.importDictData(file);
        return Result.ok();
    }

    // 根据父节点ID查询子数据列表
    @ApiOperation(value = "根据父节点ID查询子数据列表")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }
}
