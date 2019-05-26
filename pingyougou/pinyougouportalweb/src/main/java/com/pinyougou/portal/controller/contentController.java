package com.pinyougou.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.pojo.TbContent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
public class contentController {
    @Reference
    private ContentService contentService;
   @RequestMapping("/findCategoryId")
    public List<TbContent> findCategoryId(Long categoryId) {
       return contentService.findCategoryId(categoryId);
   }
}

