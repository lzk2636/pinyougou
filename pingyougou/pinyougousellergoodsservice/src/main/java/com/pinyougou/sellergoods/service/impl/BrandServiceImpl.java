package com.pinyougou.sellergoods.service.impl;

import entity.PageResult;
import entity.Result;
import com.alibaba.dubbo.config.annotation.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {
    @Autowired
private TbBrandMapper mapper;
    public List<TbBrand> findAll() {
        return mapper.selectByExample(null);
    }

    @Override
    public PageResult findBypage(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        Page<TbBrand> list = (Page<TbBrand>) mapper.selectByExample(null);
        return new PageResult(list.getTotal(),list.getResult());
    }

    @Override
    public Result insert(TbBrand tbBrand) {
        try {
            mapper.insertSelective(tbBrand);
            return new Result(true,"插入成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"插入失败");
        }

    }

    @Override
    public TbBrand findOne(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public Result update(TbBrand tbBrand) {
        try {
            mapper.updateByPrimaryKeySelective(tbBrand);
            return new Result(true,"更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"更新失败");
        }


    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            mapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public PageResult findBypageWithcondition(TbBrand brand, Integer page, Integer size) {
        PageHelper.startPage(page,size);
        TbBrandExample example=new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        if (brand!=null){
            if (brand.getName()!=null&&brand.getName().length()>0){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if (brand.getFirstChar()!=null&&brand.getFirstChar().length()>0){
                criteria.andFirstCharLike("%"+brand.getFirstChar()+"%");
            }
        }


        Page<TbBrand> list = (Page<TbBrand>) mapper.selectByExample(example);
        return new PageResult(list.getTotal(),list.getResult());
    }

    @Override
    public List<Map> findMapList() {
        return mapper.findMapList();
    }
}
