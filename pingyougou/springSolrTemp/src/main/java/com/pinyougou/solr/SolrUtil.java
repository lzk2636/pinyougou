package com.pinyougou.solr;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.stereotype.Component;
import sun.applet.Main;

import java.util.List;
import java.util.Map;

@Component
public class SolrUtil {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private SolrTemplate template;

    public void queryItem() {
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");
        List<TbItem> tbItems = itemMapper.selectByExample(example);
        System.out.println("start");
        for (TbItem tbItem : tbItems) {
            System.out.println(tbItem.getId() + " " + tbItem.getTitle());
            Map<String,String> map = JSON.parseObject(tbItem.getSpec(),Map.class);
            if (map!=null){
                tbItem.setMapSpec(map);
            }

        }
        template.saveBeans(tbItems);
        template.commit();
        System.out.println("fishin");
    }
    public void delete(){
        SolrDataQuery querys=new SimpleQuery("*:*");
        template.delete(querys);
        template.commit();
    }

    public static void main(String[] agrs) {
        ApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil util = (SolrUtil) classPathXmlApplicationContext.getBean("solrUtil");
       util.queryItem();
          //  util.delete();
    }
}
