package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.itemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
public class pageDeleteListener implements MessageListener {
    @Autowired
    private itemPageService pageService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage= (ObjectMessage) message;
        try {
            Long[] id= (Long[]) objectMessage.getObject();
            pageService.deletePage(id);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
