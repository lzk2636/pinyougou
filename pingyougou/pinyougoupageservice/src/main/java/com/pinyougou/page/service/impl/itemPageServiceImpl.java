package com.pinyougou.page.service.impl;


import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.itemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class itemPageServiceImpl implements itemPageService {
    @Value("${pagedir}")
    private String itemUrl;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Autowired
    private TbGoodsMapper tbGoodsMapper;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbItemMapper itemMapper;
    @Override
    public boolean genItemHtml(Long goodIds)  {
        try {
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(goodIds);
            TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(goodIds);
            String Category1 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id()).getName();
            String Category2= itemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id()).getName();
            String Category3 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName();
            TbItemExample example=new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(goodIds);
            criteria.andStatusEqualTo("1");
            example.setOrderByClause("is_default desc");
            List<TbItem> tbItems = itemMapper.selectByExample(example);

            Map map=new HashMap();
            map.put("tbItems",tbItems);
            map.put("goods",tbGoods);
            map.put("goodsDesc",tbGoodsDesc);
            map.put("category1",Category1);
            map.put("category2",Category2);
            map.put("category3",Category3);
            Writer out=new FileWriter(itemUrl+goodIds+".html");
            template.process(map,out);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deletePage(Long[] goodId) {
        for (Long aLong : goodId) {
            try {
                new File(itemUrl+aLong+".html").delete();
                System.out.println("页面删除成功");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }
}
