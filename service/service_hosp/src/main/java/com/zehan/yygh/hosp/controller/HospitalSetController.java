package com.zehan.yygh.hosp.controller;

import com.zehan.yygh.hosp.service.HospitalSetService;
import com.zehan.yygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/hosp/hospitalSet")   // 总路径
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    // 查询医院设置表的所有信息
    @GetMapping("findAll")   // 分路径
    public List<HospitalSet> findAllHospitalSet(){
        return hospitalSetService.list();
    }

    // 删除医院设置
    @DeleteMapping("delete/{id}")
    public boolean removeHospSet(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);   // 这里是逻辑删除 不是真正的删除 => delete_status = 1
        return flag;
    }
}
