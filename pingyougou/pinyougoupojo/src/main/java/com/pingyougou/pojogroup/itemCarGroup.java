package com.pingyougou.pojogroup;

import com.pinyougou.pojo.TbItemCat;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class itemCarGroup implements Serializable {
    private TbItemCat itemCat;
    private Map map=new HashMap();

    public TbItemCat getItemCat() {
        return itemCat;
    }

    public void setItemCat(TbItemCat itemCat) {
        this.itemCat = itemCat;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
