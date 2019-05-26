package com.pinyougou.order.service;
import java.util.List;

import com.pingyougou.pojogroup.Orders;
import com.pinyougou.pojo.TbOrder;

import com.pinyougou.pojo.TbPayLog;
import entity.Page;
import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface OrderService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbOrder> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbOrder order);
	
	
	/**
	 * 修改
	 */
	public void update(TbOrder order);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbOrder findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbOrder order, int pageNum, int pageSize);
	/*
	* redis中查询日志记录
	* */
	public TbPayLog searcherFromRedis(String userId);
	/*
	 * 支付成功的订单状态修改
	 * */
	public void updateSatus(String out_trade_no,String transaction_id);


	/*
	 * 查询购物列表
	 * */
	public List<Orders> findAllList(String name);

	Page findPageByOrders(Integer pageNo, Integer pageSize,String name);
	/*
	* 查询未付款订单
	* */
	public List<Orders> unPayNo(String name);
	/*
	* 分页查询未付款代码
	* */
	Page findPageByOrdersNO(Integer pageNo, Integer pageSize,String name);
	/*
	 * 查询已经发货待收货订单
	 * */
	public List<Orders> PayOver(String name);
	/*
	 * 分页查询已经发货待收货款代码
	 * */
	Page findPageByOrdersPayOver(Integer pageNo, Integer pageSize,String name);
	/*
	* 查询已经付款,但未发货的订单
	* */
	public List<Orders> PayToSend(String name);
	/*
	 * 查询已经付款,但未发货的订单页面
	 * */
	Page findPageByOrdersPayToSend(Integer pageNo, Integer pageSize,String name);
	/*
	* 查询已经收获,待评价的订单
	* */
	public List<Orders> OrderToEvaluate(String name);
	/*
	* 查询订单已经收货,分页查询待评价的订单
	* */
	Page findPageByOrdersToEvaluate(Integer pageNo, Integer pageSize,String name);
	/*
	* 查询订单Orders对象
	* */
	public Orders findOrders(Long id);
}
