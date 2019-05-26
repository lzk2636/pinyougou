package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pingyougou.pojogroup.Cart;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.service.cartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service
public class cartServiceImpl implements cartService {
    @Autowired
    private TbItemMapper itemMapper;
    @Override
    public List<Cart> ShopCart(List<Cart> cartList, Long itemId, Integer num) {

       // cartList=new ArrayList<>();
        //1.根据商品SKU ID查询SKU商品信息
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        //2.获取商家ID
        if (item==null){
         throw  new RuntimeException("商家不存在ID");
        }
        if (!item.getStatus().equals("1")){
        throw   new RuntimeException("转台不为1");
        }
        //3.根据商家ID判断购物车列表中是否存在该商家的购物车
        Cart cart = getCart(cartList, item);
        if (cart==null){
            //4.如果购物车列表中不存在该商家的购物车

            //4.1 新建购物车对象 item是从数据库查询得到的!!!
            Cart  carts=new Cart();
            carts.setSellId(item.getSellerId());
            carts.setSellName(item.getSeller());

            List<TbOrderItem> itemList=new ArrayList<>();
            TbOrderItem items = getItemList(item, num);
            itemList.add(items);
            carts.setItemList(itemList);
            cartList.add(carts);
            //4.2 将新建的购物车对象添加到购物车列表
        }else {

            TbOrderItem orderItem = getOrderItem(cart.getItemList(), itemId);
            if (orderItem==null){
                //5.1. 如果没有，新增购物车明细
                cart.getItemList().add(getItemList(item, num));
            }else {
               // 如果有，在原购物车明细上添加数量，更改金额
                orderItem.setNum(orderItem.getNum()+num);
                orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue()*orderItem.getNum()));
                if (orderItem.getNum()<=0){
                    cart.getItemList().remove(orderItem);
                }
                if (cart.getItemList().size()==0){
                    cartList.remove(cart);
                }
            }

        }


        //5.如果购物车列表中存在该商家的购物车
        // 查询购物车明细列表中是否存在该商品
        //5.1. 如果没有，新增购物车明细
        //5.2. 如果有，在原购物车明细上添加数量，更改金额

        return cartList;
    }
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Cart> getRedies(String name) {
        return (List<Cart>) redisTemplate.boundHashOps("itemList").get(name);
    }

    @Override
    public void saveToRedies(List<Cart> itemList, String name) {
      //  System.out.println("saveToRedies="+itemList);
        redisTemplate.boundHashOps("itemList").put(name,itemList);
    }

    @Override
    public List<Cart> itemList(List<Cart> cartList1, List<Cart> cartList2) {
        if (cartList1!=null&& cartList2!=null){
            for (Cart cart : cartList2) {
                for (TbOrderItem orderItem:cart.getItemList()) {
                    cartList1 = ShopCart(cartList1, orderItem.getItemId(), orderItem.getNum());

                }
            }
        }

        return cartList1;
    }

    private TbOrderItem getOrderItem(List<TbOrderItem> orderItemList,Long itemId){
        for (TbOrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().longValue()==itemId.longValue()){
                return orderItem;
            }
        }
        return null;
    }
    private TbOrderItem getItemList(TbItem item,Integer num){

        TbOrderItem orderItem=new TbOrderItem();
        orderItem.setNum(num);
        orderItem.setPicPath(item.getImage());
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setItemId(item.getId());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setPrice(item.getPrice());
        orderItem.setTitle(item.getTitle());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num));

        return orderItem;
    }
    private Cart getCart(List<Cart> cartList, TbItem item){
        if (cartList.size()>0){
            for (Cart cart : cartList) {
                if (cart.getSellId().equals(item.getSellerId())){
                    return cart;
                }

            }
        }

        return null;
    }
}
