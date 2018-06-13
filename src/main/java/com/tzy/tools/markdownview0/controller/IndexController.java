package com.tzy.tools.markdownview0.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tzy.tools.markdownview0.conf.Setting;
import com.tzy.tools.markdownview0.entity.Article;
import com.tzy.tools.markdownview0.entity.Category;
import com.tzy.tools.markdownview0.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
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

    /**
     * 获取文件目录
     *
     * @return 获取的文件目录集合
     */
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

    /**
     * 获取单篇文章
     *
     * @param path 该文章路径
     * @return 该文章实体
     * @throws IOException 抛出异常
     */
    @RequestMapping(value = "/article", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Article showArticle(String path) throws IOException {
        File file = new File(path);
        String result = setting.getWelecome();
        if (path.endsWith("md")){
            result = FileUtils.readFileToString(file, "UTF-8");
        }
        StringBuilder builder = new StringBuilder(result);
        builder.append("  \r\n\n\n  [toc]");
        Article article = new Article();
        article.setContent(builder.toString());
        return article;
    }

    /**
     * 文件删除
     *
     * @param path 要删除的文件
     * @return 首页
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject deleteFile(String path){
        JSONObject result = new JSONObject();
        if (!StringUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists() && file.delete()) {
                result.put("status", 0);
                result.put("msg", "SUCCESS");
            }
        } else {
            result.put("status", 400);
            result.put("msg", "NOT FOUND");
        }
        return result;
    }
}
