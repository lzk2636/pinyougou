package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pingyougou.pojogroup.Cart;
import com.pingyougou.pojogroup.Orders;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbOrder;
import entity.Page;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @RequestMapping("findAll")
    public List<Orders> findAll(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderService.findAllList(name);
    }
    @RequestMapping("/byPage")
    public Page findByPage(Integer rows, Integer size){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            return  orderService.findPageByOrders(rows,size,name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping("/payNo")
    public  Page findByPayNo(Integer rows,Integer size){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            return  orderService.findPageByOrdersNO(rows,size,name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping("/payOver")
    public  Page findByPayOver(Integer rows,Integer size){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            return  orderService.findPageByOrdersPayOver(rows,size,name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping("/payToSend")
    public  Page findByPayToSend(Integer rows,Integer size){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            return  orderService.findPageByOrdersPayToSend(rows,size,name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }






    @RequestMapping("findOne")
    public TbOrder findOne(Long id){
      return   orderService.findOne(id);
    }


    /*
    * 查询某个对象Orders
    * */
    @RequestMapping("findOnes")
    public Orders findOnes(Long id){
        return orderService.findOrders(id);
    }
    /*
    * 修改订单为22、已付款，
    * */
    @RequestMapping("updateOne")
    public Result update(Long id){
        TbOrder tbOrder = orderService.findOne(id);
        if (tbOrder!=null){
            tbOrder.setStatus("2");
            tbOrder.setPaymentTime(new Date());
        }

        try {
            orderService.update(tbOrder);
            return new Result(true,"订单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"下单失败");
        }
    }
    /*
    * 修改订单为7的待评价
    * */
    @RequestMapping("updateStatusToSeven")
    public Result updateOrderSeven(Long id){
        TbOrder tbOrder = orderService.findOne(id);
        if (tbOrder!=null){
            tbOrder.setStatus("7");
            tbOrder.setEndTime(new Date());
        }

        try {
            orderService.update(tbOrder);
            return new Result(true,"订单状态修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"订单状态修改失败");
        }
    }
    /*
    * 订单状态3--->4
    * */
    @RequestMapping("updateOneOrderStatus")
    public Result updateOrderToSend(Long id){
        TbOrder tbOrder = findOne(id);
        if (tbOrder!=null){
            tbOrder.setStatus("4");
            tbOrder.setConsignTime(new Date());
        }
        try {
            orderService.update(tbOrder);
            return new Result(true,"提醒发货成功!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"提醒发货失败!!!!");
        }
    }


    /*
    * 分页查询待评价订单
    * */
    @RequestMapping("/evaluate")
    public Page findByOrdersForEvaluate(Integer rows,Integer size){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderService.findPageByOrdersToEvaluate(rows,size,name);
    }
}
