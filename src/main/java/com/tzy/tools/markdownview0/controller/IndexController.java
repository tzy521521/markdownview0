package com.tzy.tools.markdownview0.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 主控制器
 * @author lipu@csii.com.cn
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public String showIndex(){
        return "index";
    }
}
