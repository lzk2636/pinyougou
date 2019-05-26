package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

public interface searcherService {
    public Map<String,Object> findAll(Map ItemMap);
    /*
    * 导入数据solr
    * */
    public void importData(List list);
    /*
    * 删除导入数据
    * */
    public void delete(List list);

}
