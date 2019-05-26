package com.pinyougou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.searcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;


public class SolrListener implements MessageListener {
    @Autowired
    private searcherService searcherService;
    @Override
    public void onMessage(Message message) {
        TextMessage messageTest= (TextMessage) message;
        try {
            String text = messageTest.getText();
            System.out.println("solr="+text);
            List<TbItem> tbItems = JSON.parseArray(text, TbItem.class);
            searcherService.importData(tbItems);
            System.out.println("solr 导入数据成功");
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
