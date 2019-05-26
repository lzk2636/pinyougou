package com.pinyougou.task;

import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SeckillTask {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TbSeckillGoodsMapper tbSeckillGoodsMapper;
    /**
     * 刷新秒杀商品
     */
    @Scheduled(cron = "0 * * * * ?")
    public void refreshSeckillGoods() {
        System.out.println("执行了任务调度" + new Date());
        List foods =new ArrayList(redisTemplate.boundHashOps("seckill").keys());
        System.out.println(foods);
        TbSeckillGoodsExample example = new TbSeckillGoodsExample();
        TbSeckillGoodsExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");
        criteria.andStartTimeLessThanOrEqualTo(new Date());
        criteria.andEndTimeGreaterThanOrEqualTo(new Date());
        criteria.andStockCountGreaterThan(0);
        criteria.andIdNotIn(foods);
        List<TbSeckillGoods> tbSeckillGoods = tbSeckillGoodsMapper.selectByExample(example);

        for (TbSeckillGoods food : tbSeckillGoods) {
                redisTemplate.boundHashOps("seckill").put(food.getId(), food);
            }
            System.out.println("将" + tbSeckillGoods.size() + "条商品装入缓存");
        }
    @Scheduled(cron = "* * * * * ?")
    private void deleteSeckillGoods(){
        List <TbSeckillGoods> tbSeckillGoods = redisTemplate.boundHashOps("seckill").values();
        for (TbSeckillGoods tbSeckillGood : tbSeckillGoods) {
            if (tbSeckillGood.getEndTime().getTime() < new Date().getTime()){
                redisTemplate.boundHashOps("seckill").delete(tbSeckillGood.getId());
                tbSeckillGoodsMapper.updateByPrimaryKey(tbSeckillGood);
                System.out.println("数据删除:"+tbSeckillGood.getId());
            }
        }
       // System.out.println("秒杀结束????");
    }

}
