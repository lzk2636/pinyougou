package com.pingyougou.pojogroup;

import com.pinyougou.pojo.TbTypeTemplate;

import java.util.HashMap;
import java.util.Map;

public class TbTypeTemplateGrop {
    private TbTypeTemplate tbTypeTemplate;
    private Map map=new HashMap();

    public TbTypeTemplate getTbTypeTemplate() {
        return tbTypeTemplate;
    }

    public void setTbTypeTemplate(TbTypeTemplate tbTypeTemplate) {
        this.tbTypeTemplate = tbTypeTemplate;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
