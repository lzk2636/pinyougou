package com.pinyougou.search.service.impl;

import com.pinyougou.search.service.searcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Arrays;


public class DeleteListener implements MessageListener {
    @Autowired
    private searcherService service;
    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage= (ObjectMessage) message;
        try {
            Long[]ids= (Long[]) objectMessage.getObject();
            System.out.println(ids[0]);
            service.delete(Arrays.asList(ids));
            System.out.println("删除成功");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
