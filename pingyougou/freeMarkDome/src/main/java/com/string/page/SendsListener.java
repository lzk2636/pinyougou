package com.string.page;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SendsListener {
    @Autowired
    private SmsUtils smsUtils;
    @JmsListener(destination = "sms")
    public void CollectMessage(Map<String,String> map){
        try {
            SendSmsResponse sendSmsResponse = smsUtils.sendSms(map.get("phoneNumber"), map.get("signName"), map.get("templateCode"),
                    map.get("pram"));
            System.out.println(sendSmsResponse.getCode());
            System.out.println(sendSmsResponse.getMessage());
            System.out.println(sendSmsResponse.getRequestId());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
