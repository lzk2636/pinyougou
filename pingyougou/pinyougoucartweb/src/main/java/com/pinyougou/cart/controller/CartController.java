package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pingyougou.pojogroup.Cart;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.service.cartService;
import com.pinyougou.utils.CookieUtil;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Reference
    private cartService service;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/cartCookies")
    private List<Cart> getFCookies() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("获取名字:" + name);
        String cartList = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if (cartList == null || cartList.equals("")) {
            cartList = "[]";
        }

        List<Cart> list = JSON.parseArray(cartList, Cart.class);
        if (name.equals("anonymousUser")) {//没登录的情形下,存入本地购物车
            System.out.println("从cookies中读取数据>>>>>>>>>");
            return list;
        } else {//登录后
            System.out.println("从RedIE中读取数据>>>>>>>");
            List<Cart> redies = service.getRedies(name);
            System.out.println(list.size()+"本地购物车>>>>");
            if (list.size()>0){//如果本地购物车大于0
                redies = service.itemList(list, redies);//合并购物车
                CookieUtil.deleteCookie(request,response,"cartList");//清除本地购物车
                service.saveToRedies(redies,name);//合并购物车存入redies
                System.out.println("合并购物车存入redies");
                return redies;
            }
            if (redies == null) {

              redies=new ArrayList<>();
            }
            return redies;
        }



    }


    @RequestMapping("/cartList")
    public Result findCartList(Long itemId, Integer num) {
        response.setHeader("Access-Control-Allow-Origin","http://localhost:9105");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //System.out.println("获取名字:" + name);
        List<Cart> fCookies = getFCookies();

        try {


            if (name.equals("anonymousUser")) {

                //提取Cookies到service中
                fCookies = service.ShopCart(fCookies, itemId, num);
                //获取cookies
                String jsonString = JSON.toJSONString(fCookies);
              //  System.out.println(jsonString);
                CookieUtil.setCookie(request, response, "cartList", jsonString, 3600 * 24 * 7, "UTF-8");

                //将提取的service中重新放入cookies中
                System.out.println("将Cookies中的数据放入Service");
            } else {

                fCookies = service.ShopCart(fCookies, itemId, num);
                service.saveToRedies(fCookies, name);
                System.out.println("将Cookise中的数据放入Redise");
            }
            return new Result(true, "购物车添加成功!!!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "购物车出错了");
        }


    }


}
