package com.zn.gmall.all.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: 赵念
 * @create-date: 2023/2/11/14:14
 */
@Controller
public class PassPortController {

    @RequestMapping("/login.html")
    public String toLoginPage(
            // 接收请求参数
            @RequestParam("originUrl") String originUrl,

            Model model) {

        // 将请求参数值存入请求域
        model.addAttribute("originUrl", originUrl);

        // 返回逻辑视图
        return "login";
    }

}
