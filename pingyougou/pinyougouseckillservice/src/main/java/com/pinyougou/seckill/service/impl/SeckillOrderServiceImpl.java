package com.pinyougou.seckill.service.impl;
import java.util.Date;
import java.util.List;

import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.seckill.service.SeckillOrderService;
import com.pinyougou.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSeckillOrderMapper;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.pojo.TbSeckillOrderExample;
import com.pinyougou.pojo.TbSeckillOrderExample.Criteria;


import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

	@Autowired
	private TbSeckillOrderMapper seckillOrderMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSeckillOrder> findAll() {
		return seckillOrderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSeckillOrder> page=(Page<TbSeckillOrder>) seckillOrderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbSeckillOrder seckillOrder) {
		seckillOrderMapper.insert(seckillOrder);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbSeckillOrder seckillOrder){
		seckillOrderMapper.updateByPrimaryKey(seckillOrder);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbSeckillOrder findOne(Long id){
		return seckillOrderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			seckillOrderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbSeckillOrder seckillOrder, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSeckillOrderExample example=new TbSeckillOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(seckillOrder!=null){			
						if(seckillOrder.getUserId()!=null && seckillOrder.getUserId().length()>0){
				criteria.andUserIdLike("%"+seckillOrder.getUserId()+"%");
			}
			if(seckillOrder.getSellerId()!=null && seckillOrder.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+seckillOrder.getSellerId()+"%");
			}
			if(seckillOrder.getStatus()!=null && seckillOrder.getStatus().length()>0){
				criteria.andStatusLike("%"+seckillOrder.getStatus()+"%");
			}
			if(seckillOrder.getReceiverAddress()!=null && seckillOrder.getReceiverAddress().length()>0){
				criteria.andReceiverAddressLike("%"+seckillOrder.getReceiverAddress()+"%");
			}
			if(seckillOrder.getReceiverMobile()!=null && seckillOrder.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+seckillOrder.getReceiverMobile()+"%");
			}
			if(seckillOrder.getReceiver()!=null && seckillOrder.getReceiver().length()>0){
				criteria.andReceiverLike("%"+seckillOrder.getReceiver()+"%");
			}
			if(seckillOrder.getTransactionId()!=null && seckillOrder.getTransactionId().length()>0){
				criteria.andTransactionIdLike("%"+seckillOrder.getTransactionId()+"%");
			}
	
		}
		
		Page<TbSeckillOrder> page= (Page<TbSeckillOrder>)seckillOrderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;
	@Override
	public void seckillOrder(Long seckillId, String userId) {
	TbSeckillGoods tbSeckillGoods= (TbSeckillGoods) redisTemplate.boundHashOps("seckill").get(seckillId);
	if (tbSeckillGoods==null){
		throw new RuntimeException("商品找不到???");
	}
	if (tbSeckillGoods.getStockCount()<=0){
		throw new RuntimeException("秒杀结束!!!!!");
	}
		tbSeckillGoods.setStockCount(tbSeckillGoods.getStockCount()-1);
		redisTemplate.boundHashOps("seckill").put(seckillId,tbSeckillGoods);
	if (tbSeckillGoods.getStockCount()==0){
		seckillGoodsMapper.updateByPrimaryKey(tbSeckillGoods);
		redisTemplate.boundHashOps("seckill").delete(seckillId);
	}

		Long id=new IdWorker().nextId();
		TbSeckillOrder order=new TbSeckillOrder();
		order.setId(id);
		order.setCreateTime(new Date());
		order.setMoney(tbSeckillGoods.getCostPrice());
		order.setSellerId(tbSeckillGoods.getSellerId());
		System.out.println("sssssssssssssssssss_+"+tbSeckillGoods.getSellerId());
		order.setSeckillId(seckillId);
		order.setStatus("0");
		redisTemplate.boundHashOps("seckillOrder").put(userId,order);

	}

	@Override
	public TbSeckillOrder findSeckill(String seckId) {
		return (TbSeckillOrder) redisTemplate.boundHashOps("seckillOrder").get(seckId);
	}

	@Override
	public void saveFromRedisToDB(Long seckillId, String userId, String transaction_id) {


		TbSeckillOrder tbSeckillOrder = findSeckill(userId);

		if (tbSeckillOrder==null){
			throw new RuntimeException("秒杀订单并不存在");
		}
		if (tbSeckillOrder.getId().longValue()!=seckillId){
			throw new RuntimeException("并不是同一个订单!!!!!");
		}
		tbSeckillOrder.setId(seckillId);
		tbSeckillOrder.setStatus("1");
		tbSeckillOrder.setTransactionId(transaction_id);
		tbSeckillOrder.setPayTime(new Date());
		tbSeckillOrder.setUserId(userId);
		seckillOrderMapper.insert(tbSeckillOrder);
		redisTemplate.boundHashOps("seckillOrder").delete(userId);
	}

	@Override
	public void deteleFromRedies(String userId, Long orderid) {

		TbSeckillOrder tbSeckillOrder = findSeckill(userId);
		if (tbSeckillOrder!=null&&orderid==tbSeckillOrder.getId().longValue()){
			redisTemplate.boundHashOps("seckillOrder").delete(userId);//购物订单取消
		TbSeckillGoods tbSeckillGoods= (TbSeckillGoods) redisTemplate.boundHashOps("seckill").get(tbSeckillOrder.getSeckillId());
		if (tbSeckillGoods!=null){
			tbSeckillGoods.setStockCount(tbSeckillGoods.getStockCount()+1);
			redisTemplate.boundHashOps("seckill").put(tbSeckillOrder.getSeckillId(),tbSeckillGoods);
		}else{
			tbSeckillGoods=new TbSeckillGoods();
			tbSeckillGoods.setId(tbSeckillOrder.getSeckillId());
			tbSeckillGoods.setStockCount(1);
			redisTemplate.boundHashOps("seckill").put(tbSeckillOrder.getSeckillId(),tbSeckillGoods);
		}
		}
		System.out.println("订单超时,重新下单>>>>");
	}

}
