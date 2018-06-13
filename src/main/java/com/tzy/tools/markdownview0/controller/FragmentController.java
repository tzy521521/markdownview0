package com.tzy.tools.markdownview0.controller;

import com.tzy.tools.markdownview0.conf.Setting;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * 返回片段
 * @author lipu@csii.com.cn
 */
@Controller
@RequestMapping("/fragment")
public class FragmentController {
    private static final String FILE_PATH_COOKIE_NAME = "filepath";
    @Resource
    private Setting setting;

    /**
     * 返回对比页面
     */
    @RequestMapping(value = "/compare", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String compare() {
        return "fragment/compare :: compare";
    }
}
