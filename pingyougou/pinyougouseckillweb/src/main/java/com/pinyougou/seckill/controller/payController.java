package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.WxPayService;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillGoodsService;
import com.pinyougou.seckill.service.SeckillOrderService;
import com.pinyougou.utils.IdWorker;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class payController {
    @Reference
    private WxPayService wxPayService;
    @Reference
    private SeckillOrderService orderService;
    @RequestMapping("/native")
    private Map ResultMap() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //到redis查询支付日志
        TbSeckillOrder seckill = orderService.findSeckill(name);
        if (seckill != null) {
             return wxPayService.WxNative(seckill.getId() + "", (seckill.getMoney().doubleValue())+"");
        } else {
            System.out.println("123355");
            return new HashMap();

        }

    }
    @RequestMapping("/status")
    private Result getStatus(String out_trade_no)  {
        System.out.println(out_trade_no+"status");
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Result result;
        int x=0;
       while (true){
           Map map = wxPayService.queryStatus(out_trade_no);
           if (map==null){
               result=new Result(false,"交易放生错误");
               break;
           }

           try {
               Thread.sleep(3000);
               if(map.get("trade_state").equals("SUCCESS")){
                   orderService.saveFromRedisToDB(Long.valueOf(out_trade_no),name,new IdWorker().nextId()+"");
                   result=new Result(true,"交易成功");
                   break;
               }
           } catch (InterruptedException e) {
               e.printStackTrace();
           }

           x++;
           if (x==100){
               result=new Result(false,"超时异常");
               orderService.deteleFromRedies(name, Long.valueOf(out_trade_no));
               break;
           }
       }
       return result;
    }
}
