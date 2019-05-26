package com.pingyougou.pojogroup;

import com.pinyougou.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {
    private String sellId;//商家的id
    private String sellName;//商家的名称
    private List<TbOrderItem> itemList;//商家明细列表,购物单选项

    public String getSellId() {
        return sellId;
    }

    public void setSellId(String sellId) {
        this.sellId = sellId;
    }

    public String getSellName() {
        return sellName;
    }

    public void setSellName(String sellName) {
        this.sellName = sellName;
    }

    public List<TbOrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<TbOrderItem> itemList) {
        this.itemList = itemList;
    }
}
