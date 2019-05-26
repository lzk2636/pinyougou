package com.string.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class sendMessageController {
    @Autowired
    private JmsTemplate template;
    @RequestMapping("/sendMap")
    private void Send(){
        Map map=new HashMap();
        map.put("phoneNumber","15102045416");
        map.put("signName","品优网吧");
        map.put("templateCode","SMS_162521607");
        map.put("pram","{\"codea\":\"123669988\"}");
        template.convertAndSend("sms",map);
    }
}
