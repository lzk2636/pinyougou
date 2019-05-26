package com.pinyougou.page.service;

import java.io.IOException;

public interface itemPageService {
    /*
    *  生成商品详细页
    * */
    public boolean genItemHtml(Long goodIds) throws IOException;
    /*
     * 删除页面消息
     * */
    public boolean deletePage(Long[] goodId);
}
