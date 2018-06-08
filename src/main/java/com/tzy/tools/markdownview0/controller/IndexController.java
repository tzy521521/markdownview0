package com.tzy.tools.markdownview0.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 主控制器
 * @author lipu@csii.com.cn
 */
@Controller
public class IndexController {
    @RequestMapping()
    public String showIndex(){
        return "index";
    }
}
