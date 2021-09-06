package com.zehan.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zehan.yygh.common.config.com.zehan.yygh.utils.MD5;
import com.zehan.yygh.common.result.Result;
import com.zehan.yygh.hosp.service.HospitalSetService;
import com.zehan.yygh.model.hosp.Hospital;
import com.zehan.yygh.model.hosp.HospitalSet;
import com.zehan.yygh.vo.hosp.HospitalQueryVo;
import com.zehan.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(value = "/admin/hosp/hospitalSet")   // 总路径
@Api(tags = "医院设置管理")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    // 查询医院设置表的所有信息
    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("findAll")   // 分路径
    public Result findAllHospitalSet(){
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    // 删除医院设置
    @DeleteMapping("delete/{id}")
    @ApiOperation(value = "逻辑删除医院设置")
    public Result removeHospSet(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);   // 这里是逻辑删除 不是真正的删除 => delete_status = 1
        if (flag){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    // 条件查询带分页
    @PostMapping("findPage/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        // 创建配置对象 传递当前页 每一页记录数  来自于Mybatis plus
        Page<HospitalSet> page = new Page<>(current, limit);
        // 调用方法实现分页
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();

        String hostname = hospitalSetQueryVo.getHosname();
        String hostcode = hospitalSetQueryVo.getHoscode();
        if(!StringUtils.isEmpty(hostname)) wrapper.like("hosname", hostname);  // 模糊查询
        if(!StringUtils.isEmpty(hostcode)) wrapper.eq("hoscode", hostcode);    // equal查询

        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, wrapper);

        return Result.ok(hospitalSetPage);
    }

    // 添加医院设置
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){
         // 设置状态 1 使用中 0 不能使用
        hospitalSet.setStatus(1);
        // 设置签名密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));
        boolean save = hospitalSetService.save(hospitalSet);
        if(save){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    // 5. 根据id获取医院设置
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    // 6.修改医院设置
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if(flag){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }
    // 批量删除医院设置
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<String> idList){
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    // 医院设置锁定和解锁 status如果是1 代表解锁，如果是0 就不能更改
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status){
        // 根据id查询出医院设置的信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        // 设置状态
        hospitalSet.setStatus(status);
        // 修改
        hospitalSetService.updateById(hospitalSet);

        return Result.ok();
    }

    // 发送签名密钥
    @PutMapping("sendKey/{id}")
    public Result sendSignKey(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signkey = hospitalSet.getSignKey();
        String hascode = hospitalSet.getHoscode();

        return Result.ok("signKey: " + signkey + "   " + "hascode: " + hascode);
    }
}
