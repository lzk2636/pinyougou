package com.pinyougou.manager.controller;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.pingyougou.pojogroup.Goods;

import com.pinyougou.pojo.TbItem;

import com.pinyougou.sellergoods.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods){
		try {
			goodsService.add(goods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@Autowired
	private Destination queueSolrDeleteDestination;
	@Autowired
	private Destination topicPageDestination;
	@RequestMapping("/delete")
	public Result delete(final Long [] ids){
		try {
			if (ids.length>0){
				goodsService.delete(ids);
				//searcherServices.delete(Arrays.asList(ids));
				jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createObjectMessage(ids);
					}
				});
				//删除页面消息信息
				jmsTemplate.send(topicPageDestination, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createObjectMessage(ids);
					}
				});
			}


			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}
	/*
	*
	* 审核订单状态
	* */

	@Reference
	private ItemService itemService;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private Destination queueSolrDestination;
	@Autowired
	private Destination topicSolrDestination;
	@RequestMapping("/status")
	public Result setStatus(Long[] id, String status) {
		try {
			goodsService.setStatus(id, status);
			itemService.updateStateById(id, status);
			if ("1".equals(status)){
				System.out.println("1");
				List<TbItem> itemListByGoodsIdandStatus = goodsService.findItemListByGoodsIdandStatus(id, status);
				if (itemListByGoodsIdandStatus!=null){
					System.out.println(itemListByGoodsIdandStatus.size());
				}
				final String jsonString = JSON.toJSONString(itemListByGoodsIdandStatus);
				if (itemListByGoodsIdandStatus.size()>0){
					jmsTemplate.send(queueSolrDestination, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {

							return session.createTextMessage(jsonString);
						}
					});
				}

                //调用搜索接口实现数据批量导入
                /*if (itemListByGoodsIdandStatus.size()>0){
                    searcherServices.importData(itemListByGoodsIdandStatus);
                }else {
                    System.out.println("没有数据导入");
                }*/
                //生成页面FreeMarker
				for (final Long goodsId : id) {
				//	pageService.genItemHtml(goodsId);
					jmsTemplate.send(topicSolrDestination, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(goodsId+"");
						}
					});
				}

			}
			return new Result(true,"成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"失败");
		}
	}
	/*@Reference
	private itemPageService pageService;
	@RequestMapping("/html")
	public boolean genItemHtml(Long goodsId) throws IOException {
		return pageService.genItemHtml(goodsId);
	}*/
}
