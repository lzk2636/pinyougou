package com.pinyougou.manager.controller;

import entity.Result;
import entity.PageResult;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Reference
    private BrandService brandService;
    @RequestMapping("find")
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }
    @RequestMapping("findBypage")
    public PageResult bypage(Integer page, Integer size){
        return brandService.findBypage(page ,size);
    }
    @RequestMapping("add")
    public Result Success(@RequestBody TbBrand tbBrand){
        if (tbBrand.getId()!=null){
           return brandService.update(tbBrand);
        }
       return brandService.insert(tbBrand);
    }
    @RequestMapping("findOne")
    public TbBrand findOne(Long id){
        //System.out.println(id);
        return brandService.findOne(id);
    }
    @RequestMapping("delete")
    public Result delete(Long [] ids){
        System.out.println(ids);
        try {
            brandService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }

    }
    @RequestMapping("querys")
    public PageResult bypageWithContion(@RequestBody TbBrand brand, Integer page, Integer size){
        return brandService.findBypageWithcondition(brand,page ,size);
    }
    @RequestMapping("findBrandBylist")
    public List<Map> findBrandBylist(){
        return brandService.findMapList();
    }
}
