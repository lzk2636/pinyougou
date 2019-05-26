package com.pinyougou.sellergoods.service;

import entity.PageResult;
import entity.Result;
import com.pinyougou.pojo.TbBrand;

import java.util.List;
import java.util.Map;

public interface BrandService {
     List<TbBrand> findAll();
     PageResult findBypage(Integer page, Integer size);
     Result insert(TbBrand tbBrand);
     TbBrand findOne(Long id);
     Result update(TbBrand tbBrand);
     public void delete(Long[] ids);
     PageResult findBypageWithcondition(TbBrand brand, Integer page, Integer size);
     List<Map> findMapList();
}
