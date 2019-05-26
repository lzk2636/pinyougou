package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.search.service.searcherService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/search")
public class searcherController {
    @Reference
    private searcherService service;
    @RequestMapping("/requestMap")
    public Map<String,Object> getMap(@RequestBody Map keyMap){
        return service.findAll(keyMap);
    }
}
