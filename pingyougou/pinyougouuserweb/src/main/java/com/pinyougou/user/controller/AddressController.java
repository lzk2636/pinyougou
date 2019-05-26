package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbAreas;
import com.pinyougou.pojo.TbCities;
import com.pinyougou.pojo.TbProvinces;
import com.pinyougou.user.service.ProvincesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("address")
public class AddressController {
    @Reference
    private ProvincesService service;
    @RequestMapping("provinces")
    public List<TbProvinces> findList(){
        return service.findProvinceList();
    }
    @RequestMapping("cities")
    public List<TbCities> findCities(Long id){
        return service.findCitiesList(id);
    }
    @RequestMapping("ares")
    public List<TbAreas> findAreas(Long id){
        return service.findAreasList(id);
    }
}
