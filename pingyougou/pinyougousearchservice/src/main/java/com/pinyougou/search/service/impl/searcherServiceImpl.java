package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.searcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service(timeout = 5000)
public class searcherServiceImpl implements searcherService {
    @Autowired
    private SolrTemplate template;
    @Override
    public Map<String, Object> findAll(Map ItemMap) {
        Map<String,Object> map=new HashMap<>();
      /*  if (!"".equals(ItemMap.get("keywords"))){
         String keyword= (String) ItemMap.get("keywords");
         ItemMap.put("keywords",keyword.replace(" ",""));
            map.putAll(Highlight(ItemMap));
        }*/
        map.putAll(Highlight(ItemMap));
        List<String> list = queryCategory(ItemMap);
        map.put("categoryKey",list);
        String category = (String) ItemMap.get("category");
        if ("".equals(category)){
            if (list.size()>0){//为空的话默认第一的商品分类category
                map.putAll(selectBrandAndSpec(list.get(0)));
            }
        }else {
            map.putAll(selectBrandAndSpec(category));
        }



        return map;
    }
    /*
    * 导入数据
    * */

    @Override
    public void importData(List list) {
        template.saveBeans(list);
        template.commit();
    }

    @Override
    public void delete(List list) {
        SolrDataQuery query=new SimpleQuery("*:*");
        Criteria criteria=new Criteria("item_goodsid").in(list);
        query.addCriteria(criteria);
        template.delete(query);
        template.commit();
    }

    private Map Highlight(Map ItemMap){
        Map map=new HashMap();

        HighlightQuery query=new SimpleHighlightQuery();
        HighlightOptions highlightOptions=new HighlightOptions().addField("item_title");
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        highlightOptions.setSimplePostfix("</em>");
        query.setHighlightOptions(highlightOptions);
        Criteria critria=new Criteria("item_keywords").is(ItemMap.get("keywords"));
        query.addCriteria(critria);
        //1.1分类过滤
        if (!"".equals(ItemMap.get("category"))){
            FilterQuery filterQuery=new SimpleFilterQuery();
            Criteria critrias=new Criteria("item_category").is(ItemMap.get("category"));
            filterQuery.addCriteria(critrias);
            query.addFilterQuery(filterQuery);
        }
        //1.2过滤商品名称
        if (!"".equals(ItemMap.get("brand"))){
            FilterQuery filterQuery=new SimpleFilterQuery();
            Criteria critrias=new Criteria("item_brand").is(ItemMap.get("brand"));
            filterQuery.addCriteria(critrias);
            query.addFilterQuery(filterQuery);
        }
        //1.3过滤规格
        if (ItemMap.get("spec")!=null){
            Map<String,String> spec = (Map<String, String>) ItemMap.get("spec");
            for (String key : spec.keySet()) {
                FilterQuery filterQuery=new SimpleFilterQuery();
                Criteria critrias=new Criteria("item_spec_"+key).is(spec.get(key));
                filterQuery.addCriteria(critrias);
                query.addFilterQuery(filterQuery);

            }

        }
        //1.4价格过滤
        if (!"".equals(ItemMap.get("price"))){
            String price = (String) ItemMap.get("price");
            String[] split = price.split("-");
            if (!split[0].equals("0")){
                FilterQuery filterQuery=new SimpleFilterQuery();
                Criteria critrias=new Criteria("item_price").greaterThanEqual(split[0]);
                filterQuery.addCriteria(critrias);
                query.addFilterQuery(filterQuery);
            }
            if (!split[1].equals("*")){
                FilterQuery filterQuery=new SimpleFilterQuery();
                Criteria critrias=new Criteria("item_price").lessThanEqual(split[1]);
                filterQuery.addCriteria(critrias);
                query.addFilterQuery(filterQuery);
            }
        }
        //1.5按价格进行排序
        String sort = (String) ItemMap.get("sort");
        String sortField = (String) ItemMap.get("sortField");
     //   System.out.println("sort="+sort+"sortField="+sortField);
        if (sort!=null && !sort.equals("")){
            if (sort.equals("ASC")){
             //   System.out.println("sort="+sort+"sortField="+sortField);
                Sort sorts=new Sort(Sort.Direction.ASC,"item_"+sortField);
                query.addSort(sorts);
            }
            if (sort.equals("DESC")){
            //    System.out.println("sort1="+sort+"sortField1="+sortField);
                Sort sorts=new Sort(Sort.Direction.DESC,"item_"+sortField);
                query.addSort(sorts);
            }
        }
        //分页查询最后
        Integer pageSize = (Integer) ItemMap.get("pageSize");
        Integer pageNum = (Integer) ItemMap.get("pageNum");
        if (pageNum==null){
            pageNum=1;
        }
        if (pageSize==null){
            pageSize=20;
        }

        query.setOffset((pageNum-1)*pageSize);//设置前序
        query.setRows(pageSize);//设置每页行数
        HighlightPage<TbItem> page = template.queryForHighlightPage(query, TbItem.class);
        List<HighlightEntry<TbItem>> highlighted = page.getHighlighted();
        for (HighlightEntry<TbItem> entry : highlighted) {
            List<HighlightEntry.Highlight> highlights = entry.getHighlights();
            if (highlights.size()>0&& highlights.get(0).getSnipplets().size()>0){
                TbItem entitys = entry.getEntity();
                entitys.setTitle(highlights.get(0).getSnipplets().get(0));
            }
            map.put("rows",page.getContent());
            map.put("totalPage",page.getTotalPages());//获取总页数
            map.put("totalSize",page.getTotalElements());

        }

        return map;
    }
    private List<String> queryCategory(Map ItemMap){
        List<String > list=new ArrayList<>();
        Query query=new SimpleQuery("*:*");
        Criteria critria=new Criteria("item_keywords").is(ItemMap.get("keywords"));
        query.addCriteria(critria);
        GroupOptions queryGategory=new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(queryGategory);

        GroupPage<TbItem> tbItems = template.queryForGroupPage(query, TbItem.class);
        GroupResult<TbItem> item_category = tbItems.getGroupResult("item_category");
        Page<GroupEntry<TbItem>> groupEntries = item_category.getGroupEntries();//分组入口
        List<GroupEntry<TbItem>> content = groupEntries.getContent();//内容
        for (GroupEntry<TbItem> tbItemGroupEntry : content) {
            System.out.println(tbItemGroupEntry.getGroupValue());
            list.add(tbItemGroupEntry.getGroupValue());
        }
        System.out.println(list.size()+"oooooo");
        return list;
    }
    /*
    * 查询缓存的商品分类和规格数据
    * */
    @Autowired
    private RedisTemplate templates;
    private Map selectBrandAndSpec(String category){
        Map map=new HashMap();
        Long  itemCatId = (Long) templates.boundHashOps("itemCat").get(category);
        if (itemCatId!=null){
            List brandList = (List) templates.boundHashOps("brand").get(itemCatId);
            List specList = (List) templates.boundHashOps("spec").get(itemCatId);
            if (brandList.size()>0){
                map.put("brand",brandList);
            }
            if (specList.size()>0){
                map.put("spec",specList);

            }
        }
        return map;
    }

}
