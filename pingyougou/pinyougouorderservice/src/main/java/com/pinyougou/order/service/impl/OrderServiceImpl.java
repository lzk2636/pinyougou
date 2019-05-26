package com.pinyougou.order.service.impl;
import java.math.BigDecimal;
import java.util.*;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pingyougou.pojogroup.Cart;
import com.pingyougou.pojogroup.Orders;
import com.pinyougou.mapper.TbOrderItemMapper;
import com.pinyougou.mapper.TbPayLogMapper;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.*;
import com.pinyougou.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;

import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.pojo.TbOrderExample.Criteria;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<TbOrder> tbOrders = orderMapper.selectByExample(null);
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private IdWorker idwork;
	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;
	@Autowired
	private TbPayLogMapper payLogMapper;
	/**
	 * 增加
	 */
	@Override
	public void add(TbOrder order) {
		List<Cart> itemList = (List<Cart>) redisTemplate.boundHashOps("itemList").get(order.getUserId());
		List<String> orderList=new ArrayList();
		Long orderId=idwork.nextId();
		double menoryAll=0;
		for (Cart cart : itemList) {
			TbOrder tbOrder=new TbOrder();//新创建订单对象

			orderList.add(orderId+"");
			tbOrder.setOrderId(orderId);//订单ID
			tbOrder.setPaymentType(order.getPaymentType());
			///tbOrder.setSellerId(cart.getSellId());
			tbOrder.setReceiver(order.getReceiver());//收货人
			tbOrder.setReceiverAreaName(order.getReceiverAreaName());//地址
			tbOrder.setReceiverMobile(order.getReceiverMobile());//手机号
			//用户名
			tbOrder.setUserId(order.getUserId());
			//状态：未付款
			tbOrder.setStatus("1");
			//订单创建日期
			tbOrder.setCreateTime(new Date());
			//订单更新日期
			tbOrder.setUpdateTime(new Date());
			//订单来源
			tbOrder.setSourceType(order.getSourceType());
			//商家ID
			//System.out.println("商家ID=");
			tbOrder.setSellerId(cart.getSellId());


			List<TbOrderItem> cartItemList = cart.getItemList();
			double monery=0;
			for (TbOrderItem orderItem : cartItemList) {

				orderItem.setId(idwork.nextId());

				orderItem.setSellerId(cart.getSellId());
			//	orderItem.setItemId();
				monery+=orderItem.getTotalFee().doubleValue();
				orderItem.setOrderId(orderId);
				System.out.println("orderId="+orderId);
				tbOrderItemMapper.insert(orderItem);
				menoryAll+=monery;
			}

			tbOrder.setPayment(new BigDecimal(monery));
			orderMapper.insert(tbOrder);
			if ("1".equals(tbOrder.getPaymentType())){
				TbPayLog recorder=new TbPayLog();
				recorder.setCreateTime(new Date());
				recorder.setOutTradeNo(idwork.nextId()+"");
				recorder.setOrderList(orderList.toString().replace("[","").replace("]",""));
				recorder.setTradeState("0");
				recorder.setPayType("1");
				recorder.setTotalFee((long)(menoryAll));
				recorder.setUserId(order.getUserId());
				payLogMapper.insert(recorder);
				redisTemplate.boundHashOps("payLog").put(order.getUserId(),recorder);
			}

		}

	redisTemplate.boundHashOps("itemList").delete(order.getUserId());
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbOrder order){
		orderMapper.updateByPrimaryKey(order);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbOrder findOne(Long id){
		return orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderExample example=new TbOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(order!=null){			
						if(order.getPaymentType()!=null && order.getPaymentType().length()>0){
				criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(order.getPostFee()!=null && order.getPostFee().length()>0){
				criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(order.getStatus()!=null && order.getStatus().length()>0){
				criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(order.getShippingName()!=null && order.getShippingName().length()>0){
				criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(order.getShippingCode()!=null && order.getShippingCode().length()>0){
				criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(order.getUserId()!=null && order.getUserId().length()>0){
				criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(order.getBuyerMessage()!=null && order.getBuyerMessage().length()>0){
				criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(order.getBuyerNick()!=null && order.getBuyerNick().length()>0){
				criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(order.getBuyerRate()!=null && order.getBuyerRate().length()>0){
				criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(order.getReceiverAreaName()!=null && order.getReceiverAreaName().length()>0){
				criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(order.getReceiverMobile()!=null && order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(order.getReceiverZipCode()!=null && order.getReceiverZipCode().length()>0){
				criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(order.getReceiver()!=null && order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(order.getInvoiceType()!=null && order.getInvoiceType().length()>0){
				criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(order.getSourceType()!=null && order.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(order.getSellerId()!=null && order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public TbPayLog searcherFromRedis(String userId) {
		TbPayLog paylog = (TbPayLog) redisTemplate.boundHashOps("payLog").get(userId);
		return paylog;
	}

	@Override
	public void updateSatus(String out_trade_no, String transaction_id) {
		System.out.println("updateSatus"+out_trade_no);



		TbPayLog tbPayLog = payLogMapper.selectByPrimaryKey(out_trade_no);
		System.out.println("updateSatus"+tbPayLog);
		if (tbPayLog!=null){
			tbPayLog.setPayTime(new Date());
			tbPayLog.setTradeState("1");
			tbPayLog.setTransactionId(transaction_id);
			payLogMapper.updateByPrimaryKey(tbPayLog);
			String orderList = tbPayLog.getOrderList();
			if (orderList!=null){
				String[] split = orderList.split(",");
				for (String s : split) {
					TbOrder tbOrder = orderMapper.selectByPrimaryKey(Long.valueOf(s));
					tbOrder.setStatus("1");
					tbOrder.setUpdateTime(new Date());
					orderMapper.updateByPrimaryKey(tbOrder);
				}
				redisTemplate.boundHashOps("payLog").delete(tbPayLog.getUserId());
			}

		}

	}
	@Autowired
	private TbOrderItemMapper orderItemMapper;

	@Override
	public List<Orders> findAllList(String name) {
		return findAllByOrders(name,"",null);
	}
	public List<Orders> findAllByOrders(String name,String status,List<String> statusArrays){
		List<Orders> ordersList=new ArrayList<>();

		TbOrderExample example=new TbOrderExample();
		Criteria criteria1 = example.createCriteria();
		criteria1.andUserIdEqualTo(name);
		if (!status.equals("")){
			criteria1.andStatusEqualTo(status);
		}
		if (statusArrays!=null&&statusArrays.size()>0){
			criteria1.andStatusIn(statusArrays);
		}


		List<TbOrder> tbOrders = orderMapper.selectByExample(example);

		for (TbOrder tbOrder : tbOrders) {
			Orders orders=new Orders();
			orders.setTbOrder(tbOrder);
			TbOrderItemExample exmple=new TbOrderItemExample();
			TbOrderItemExample.Criteria criteria = exmple.createCriteria();
			criteria.andOrderIdEqualTo(tbOrder.getOrderId());
			List<TbOrderItem> tbOrderItems = orderItemMapper.selectByExample(exmple);
			orders.setOrderItems(tbOrderItems);
			ordersList.add(orders);
		}


		return ordersList;
	}
		/*
		* controller直接条用方法
		* */
	@Override
	public entity.Page findPageByOrders(Integer pageNo, Integer pageSize,String name) {
		List<Orders> ordersList = findAllList(name);
		return resultPage(pageNo,pageSize,ordersList);
	}
	/*
	* page分页工具方法进行分装
	* */
	public entity.Page resultPage(Integer pageNo,Integer pageSize,List<Orders> ordersList){
		entity.Page page=new entity.Page();

		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecord(ordersList.size());
		List<Orders> ordersSet=new ArrayList<>();
		Integer size=null;
		if (Integer.valueOf(page.getEnd())>ordersList.size()){
			size=ordersList.size();
		}else {
			size=Integer.valueOf(page.getEnd());
		}
		for (int i = Integer.valueOf(page.getStart()); i <size ; i++) {
			ordersSet.add(ordersList.get(i));
		}


		page.setRecords(ordersSet);
		return page;
	}
	@Override
	public List<Orders> unPayNo(String name) {
		return findAllByOrders(name,"1",null);
	}

	@Override
	public entity.Page findPageByOrdersNO(Integer pageNo, Integer pageSize, String name) {
		List<Orders> ordersList = unPayNo(name);
		return resultPage(pageNo,pageSize,ordersList);
	}

	@Override
	public List<Orders> PayOver(String name) {

		return findAllByOrders(name,"4",null);
	}

	@Override
	public entity.Page findPageByOrdersPayOver(Integer pageNo, Integer pageSize, String name) {
		List<Orders> ordersList = PayOver(name);
		return resultPage(pageNo,pageSize,ordersList);
	}

	@Override
	public List<Orders> PayToSend(String name) {
		List<String> arrays=new ArrayList<>();
		arrays.add("2");
		arrays.add("3");
		return findAllByOrders(name,"",arrays);
	}

	@Override
	public entity.Page findPageByOrdersPayToSend(Integer pageNo, Integer pageSize, String name) {
		List<Orders> ordersList = PayToSend(name);
		return resultPage(pageNo,pageSize,ordersList);
	}

	@Override
	public List<Orders> OrderToEvaluate(String name) {
		return findAllByOrders(name,"7",null);
	}

	@Override
	public entity.Page findPageByOrdersToEvaluate(Integer pageNo, Integer pageSize, String name) {
		List<Orders> ordersList = OrderToEvaluate(name);
		return resultPage(pageNo,pageSize,ordersList);
	}

    @Override
    public Orders findOrders(Long id) {
	    Orders orders=new Orders();
        TbOrder tbOrder = orderMapper.selectByPrimaryKey(id);
        orders.setTbOrder(tbOrder);
        TbOrderItemExample example=new TbOrderItemExample();
        TbOrderItemExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(tbOrder.getOrderId());
        List<TbOrderItem> tbOrderItems = orderItemMapper.selectByExample(example);
        orders.setOrderItems(tbOrderItems);
        return orders;
    }

}
