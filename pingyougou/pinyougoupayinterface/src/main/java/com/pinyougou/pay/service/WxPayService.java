package com.pinyougou.pay.service;

import java.util.Map;

public interface WxPayService {
    public Map WxNative(String out_trade_no,String total_fee);

    /*
    * 查询支付状态
    * */
    public Map queryStatus(String out_trade_no);

}
