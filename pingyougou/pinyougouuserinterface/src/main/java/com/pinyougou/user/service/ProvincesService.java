package com.pinyougou.user.service;

import com.pinyougou.pojo.TbAreas;
import com.pinyougou.pojo.TbCities;
import com.pinyougou.pojo.TbProvinces;

import java.util.List;

public interface ProvincesService {
    List<TbProvinces> findProvinceList();

    List<TbCities> findCitiesList(Long id);

    List<TbAreas> findAreasList(Long id);
}
