package com.tzy.tools.markdownview0.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author lipu@csii.com.cn
 */
public class CookieUtil {
    /**
     * 写入cookies
     *
     * @param name  键
     * @param value 值
     */
    public static void addCookie(HttpServletResponse response, String name, String value) {
        try {
            name = URLEncoder.encode(name, "UTF-8");
            value = URLEncoder.encode(value, "UTF-8");
            Cookie cookie = new Cookie(name, value);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            // do nothing
        }
    }
}
