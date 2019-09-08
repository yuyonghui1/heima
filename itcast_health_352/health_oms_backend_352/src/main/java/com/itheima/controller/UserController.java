package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConst;
import com.itheima.entity.Result;
import com.itheima.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Reference
    UserService userService;

    /**
     * 获取用户名
     * @return
     */
    @RequestMapping("/findUsername")
    @ResponseBody
    public Result findUsername(HttpServletRequest request){
        try {
            //获取安全框架的上下文对象
            SecurityContext securityContext = SecurityContextHolder.getContext();
//        获取认证对象
            Authentication authentication = securityContext.getAuthentication();
            //获取User对象
            Object principal = authentication.getPrincipal();
            User user = (User) principal;
            //获取用户名
            String username = user.getUsername();
            return new Result(true, MessageConst.GET_USERNAME_SUCCESS, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConst.GET_USERNAME_FAIL);
    }


    @RequestMapping("/doSuccess")
    public String doSuccess(){
        return "redirect:http://localhost:83/pages/main.html";
    }
    @RequestMapping("/doFail")
    public String doFail(){
        return "redirect:http://localhost:83/login.html?param=loginError";
    }

}
