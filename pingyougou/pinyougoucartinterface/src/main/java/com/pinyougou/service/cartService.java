package com.pinyougou.service;

import com.pingyougou.pojogroup.Cart;

import java.util.List;

public interface cartService {
    public List<Cart> ShopCart(List<Cart> cartList, Long itemId, Integer num);

    /*
    * 从Redies中读取数据
    * */
    public List<Cart> getRedies(String name);
    /*
    * 将Redis数据存入
    * */
    public void saveToRedies(List<Cart> itemList,String name);
    /*
    * 合并购物车嗯嗯
    * */
    public List<Cart> itemList(List<Cart>cartList1,List<Cart> cartList2);
}
