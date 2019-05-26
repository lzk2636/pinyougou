package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.itemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
public class pageListener implements MessageListener {
    @Autowired
    private itemPageService pageService;
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage= (TextMessage) message;
        try {
            String text = textMessage.getText();
            System.out.println(text);
            pageService.genItemHtml(Long.valueOf(text));
            System.out.println("页面生成结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
