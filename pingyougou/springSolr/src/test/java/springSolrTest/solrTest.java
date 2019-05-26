package springSolrTest;

import org.apache.solr.client.solrj.response.UpdateResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springSolr.pojo.TbItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-solr.xml")
public class solrTest {
    @Autowired
    private SolrTemplate template;
    @Test
    public void add(){
        TbItem tbItem=new TbItem();
        tbItem.setId(1L);
        tbItem.setBrand("Redmi");
        tbItem.setCategory("手机");
        tbItem.setPrice(new BigDecimal(666.00));
        tbItem.setGoodsId(2L);

      template.saveBean(tbItem);
      template.commit();
    }
    @Test
    public void findById(){
        TbItem byId = template.getById(1L, TbItem.class);
        System.out.println(byId.getBrand());
    }
    @Test
     public void deleteById(){
        template.deleteById("1");
        template.commit();
    }
    @Test
    public void insertList(){
        List<TbItem> list=new ArrayList();
        for (int i = 0; i<100 ; i++) {
            TbItem tbItem=new TbItem();
            tbItem.setId(i+1L);
            tbItem.setBrand("Redmi"+i);
            tbItem.setCategory("手机"+i);
            tbItem.setPrice(new BigDecimal(666.00+i));
            tbItem.setGoodsId(2L+i);
            list.add(tbItem);
        }
        template.saveBeans(list);
        template.commit();
    }
    @Test
    public void queryList(){
        Query query=new SimpleQuery("*:*");
        query.setOffset(10);
        query.setRows(20);
        ScoredPage<TbItem> items = template.queryForPage(query, TbItem.class);
        List<TbItem> content = items.getContent();
        for (TbItem tbItem : content) {
            System.out.println(tbItem.getBrand()+" "+tbItem.getTitle()+" "+tbItem.getCategory());
        }
        System.out.println("总记录数获取="+items.getTotalElements());
        System.out.println("总页数获取="+items.getTotalPages());
    }
    @Test
    public void queryIn(){
        Query query=new SimpleQuery("*:*");
        Criteria criteria=new Criteria("item_brand").contains("2");
        criteria=criteria.and("item_category").contains("2");
        query.addCriteria(criteria);

        ScoredPage<TbItem> items = template.queryForPage(query, TbItem.class);
        List<TbItem> content = items.getContent();
        for (TbItem tbItem : content) {
            System.out.println(tbItem.getBrand()+" "+tbItem.getTitle()+" "+tbItem.getCategory());
        }
        System.out.println("总记录数获取="+items.getTotalElements());
        System.out.println("总页数获取="+items.getTotalPages());
    }
    @Test
    public void deleteAll(){
        Query query=new SimpleQuery("*:*");
        UpdateResponse delete = template.delete(query);
        template.commit();
    }
}
