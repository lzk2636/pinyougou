package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbAreasMapper;
import com.pinyougou.mapper.TbCitiesMapper;
import com.pinyougou.mapper.TbProvincesMapper;
import com.pinyougou.pojo.*;
import com.pinyougou.user.service.ProvincesService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class ProvincesServiceImpl implements ProvincesService {
    @Autowired
    private TbProvincesMapper tbProvincesMapper;
    @Autowired
    private TbCitiesMapper citiesMapper;
    @Override
    public List<TbProvinces> findProvinceList() {
        return tbProvincesMapper.selectByExample(null);
    }

    @Override
    public List<TbCities> findCitiesList(Long id) {
        TbCitiesExample example=new TbCitiesExample();
        TbCitiesExample.Criteria criteria = example.createCriteria();
        criteria.andProvinceidEqualTo(id+"");

        return citiesMapper.selectByExample(example);
    }
    @Autowired
    private TbAreasMapper tbAreasMapper;
    @Override
    public List<TbAreas> findAreasList(Long id) {
        TbAreasExample example=new TbAreasExample();
        TbAreasExample.Criteria criteria = example.createCriteria();
        criteria.andCityidEqualTo(id+"");
        //cityid
        return tbAreasMapper.selectByExample(example);
    }
}
