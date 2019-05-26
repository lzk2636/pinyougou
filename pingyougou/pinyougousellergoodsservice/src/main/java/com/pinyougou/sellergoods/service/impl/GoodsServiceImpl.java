package com.pinyougou.sellergoods.service.impl;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.pingyougou.pojogroup.Goods;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import org.springframework.transaction.annotation.Transactional;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	@Autowired
	private TbGoodsDescMapper tbGoodsDescMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbTypeTemplateMapper typeTemplateMapper;
	@Autowired
	private TbBrandMapper brandMapper;
	@Autowired
	private TbSellerMapper sellerMapper;

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {

		goods.getGoods().setAuditStatus("0");
		goodsMapper.insert(goods.getGoods());
		goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
		tbGoodsDescMapper.insert(goods.getGoodsDesc());
		setItemAdd(goods);

	}

	private void setItemAdd(Goods goods) {

		if ("1".equals(goods.getGoods().getIsEnableSpec())) {
			//List TbItem
			List<TbItem> tbItems = goods.getTbItems();
			String name = goods.getGoods().getGoodsName();
			for (TbItem tbItem : tbItems) {
				String title = name;
				//{"机身内存":"16G","网络":"移动2G"}
				Map<String, Object> map = JSON.parseObject(tbItem.getSpec());
				for (String maps : map.keySet()) {

					title += ", " + map.get(maps);

				}
				tbItem.setTitle(title);
				addSave(goods, tbItem);
				itemMapper.insert(tbItem);
			}
		} else {
			TbItem item = new TbItem();
			item.setTitle(goods.getGoods().getGoodsName());
			item.setSpec("{}");
			item.setStatus("0");
			item.setPrice(goods.getGoods().getPrice());
			item.setNum(9999);
			item.setIsDefault("0");
			addSave(goods, item);
			itemMapper.insert(item);
		}
	}

	private void addSave(Goods goods, TbItem tbItem) {
		Long brandId = goods.getGoods().getBrandId();//获取brandId==>name
		Long typeTemplateId = goods.getGoods().getTypeTemplateId();
		TbTypeTemplate type = typeTemplateMapper.selectByPrimaryKey(typeTemplateId);//获取type==>name
		TbBrand tbBrand = brandMapper.selectByPrimaryKey(brandId);
		String sellerId = goods.getGoods().getSellerId();

		TbSeller tbSeller = sellerMapper.selectByPrimaryKey(sellerId);


		String itemImages = goods.getGoodsDesc().getItemImages();

		tbItem.setGoodsId(goods.getGoods().getId());
		System.out.println(goods.getGoods().getCategory3Id());
		tbItem.setCategoryid(goods.getGoods().getCategory3Id());
		tbItem.setCreateTime(new Date());
		tbItem.setUpdateTime(new Date());
		tbItem.setBrand(tbBrand.getName());
		tbItem.setCategory(type.getName());
		tbItem.setSellerId(sellerId);
		tbItem.setSeller(tbSeller.getName());
		List<Map> mapList = JSON.parseArray(itemImages, Map.class);
		if (mapList.size() > 0) {
			tbItem.setImage((String) mapList.get(0).get("url"));
		}
	}

	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods) {

		goodsMapper.updateByPrimaryKey(goods.getGoods());
		tbGoodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());
		//先删除记录
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goods.getGoods().getId());
		itemMapper.deleteByExample(example);
		setItemAdd(goods);
	}

	/**
	 * 根据ID获取实体
	 *
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id) {
		Goods goods = new Goods();
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		goods.setGoods(tbGoods);
		TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(id);
		goods.setGoodsDesc(tbGoodsDesc);
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);
		List<TbItem> tbItems = itemMapper.selectByExample(example);
		goods.setTbItems(tbItems);
		return goods;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setIsDelete("1");
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
	}


	@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbGoodsExample example = new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeleteIsNull();//逻辑删除商品

		if (goods != null) {
			if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
				//	criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
				criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
			}
			if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
				criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
			}
			if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
				criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
			}
			if (goods.getCaption() != null && goods.getCaption().length() > 0) {
				criteria.andCaptionLike("%" + goods.getCaption() + "%");
			}
			if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
				criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
			}
			if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
				criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
			}
			if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
				criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
			}

		}

		Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public void setStatus(Long[] id, String status) {
		for (Long aLong : id) {
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(aLong);
			tbGoods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
	}

	@Override
	public void setIsMarketable(Long[] id, String isMarket) throws Exception {
		for (Long ids : id) {
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(ids);
			String auditStatus = tbGoods.getAuditStatus();
			if (auditStatus.equals("1")) {
				tbGoods.setIsMarketable(isMarket);
				goodsMapper.updateByPrimaryKey(tbGoods);
			} else {
				Exception e = new Exception();//创建异常对象
				throw e;//抛出异常
			}

		}
	}

	@Override
	public List<TbItem> findItemListByGoodsIdandStatus(Long[] goodIds, String status) {
		if (goodIds.length > 0 && !"".equals(status)) {
			TbItemExample example = new TbItemExample();
			TbItemExample.Criteria criteria = example.createCriteria();
			criteria.andStatusEqualTo("1");
			criteria.andGoodsIdIn(Arrays.asList(goodIds));
			return itemMapper.selectByExample(example);
		}
		return null;

	}


}
