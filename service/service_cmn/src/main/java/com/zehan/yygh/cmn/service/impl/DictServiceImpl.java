package com.zehan.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zehan.yygh.cmn.listener.DictListener;
import com.zehan.yygh.cmn.mapper.DictMapper;
import com.zehan.yygh.cmn.service.DictService;
import com.zehan.yygh.common.result.Result;
import com.zehan.yygh.model.cmn.Dict;
import com.zehan.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")  // 下面return的list集合dicts会加到缓存中，第二次查的时候会查缓存
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Dict> dicts = baseMapper.selectList(wrapper);
        // 设置 子数据的hasChildren
        // 本来我们需要一直递归来得到数据字典里面的所有数据，但是由于前端的element中已经写好了这个步骤
        // 我们可以规定hasChildren这个参数，然后set进去，前端看到数据字典中有数据的hasChildren为true
        // 就会继续发送请求得到该数据的子数据直到所有的数据都被得到
        for(Dict dict: dicts){
            Long dictId = dict.getId();
            boolean isChild = this.isChildren(dictId);
            dict.setHasChildren(isChild);
        }
        return dicts;
    }

    @Override
    // 这里的response会把我们的dict封装进response里面返回给用户
    public void exportDictData(HttpServletResponse response) {
        // 设置下载的response头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        // 查询数据库
        List<Dict> dictList = baseMapper.selectList(null);

        List<DictEeVo> dictEeVoList = new ArrayList<>();

        for(Dict dict: dictList){
            DictEeVo dictEeVo = new DictEeVo();
            /*
                相当于:
                    dictEeVo.setId(dict.getId())
             */
            BeanUtils.copyProperties(dict, dictEeVo);
            dictEeVoList.add(dictEeVo);
        }
        try {
            // 导出excel
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict")
                    .doWrite(dictEeVoList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 导入数据字典，添加数据
    @Override
    @CacheEvict(value = "dict", allEntries=true)  // 当我们添加数据之后 缓存需要更改 所以先清空之前的缓存
    public Result importDictData(MultipartFile file) {
        try {
            // 通过listener来一边读取一边调用invoke方法 写入到数据库内
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet().doRead();
            return Result.ok();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail();
        }
    }


    
    // 判断id下面是否有子数据
    private boolean isChildren(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }
}
