package com.tzy.tools.markdownview0.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tzy.tools.markdownview0.conf.Setting;
import com.tzy.tools.markdownview0.entity.Category;
import com.tzy.tools.markdownview0.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 主控制器
 * @author lipu@csii.com.cn
 */
@Controller
public class IndexController {
    @Resource
    private FileService fileService;
    @Resource
    private Setting setting;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String showIndex(){
        return "index";
    }

    @RequestMapping(value = "/categorys", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONArray showCategory(){
        //存放目录集合
        ArrayList<Category> categories = new ArrayList<>();
        //遍历,java8的方法
        setting.getMdpath()
                .stream()
                .limit(setting.getMaxCount())
                .collect(() -> categories,(list,v) -> list.add(fileService.iteratorFile(v)), List::addAll);
        //定制序列化
        String result = fileService.toTreeJsonStr(categories);
        return JSON.parseArray(result);
    }
}
