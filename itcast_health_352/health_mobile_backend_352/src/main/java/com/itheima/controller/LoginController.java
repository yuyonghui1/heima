package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConst;
import com.itheima.constant.RedisConst;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    JedisPool jedisPool;

    @Reference
    MemberService memberService;


    /**
     * 步骤
     *  1.从参数中获取手机号，redis中获取验证码，从参数中获取验证码
     *  2. 校验验证码
     *      如果校验失败，返回登录失败的结果
     *      如果校验成功，判断是否是会员，如果不是会员，自动注册
     *      把手机号码存储到cookies（用来跟踪你的动向）
     *
     *
     * @param map
     * @return
     */
    @RequestMapping("/check")
    public Result check(@RequestBody Map<String,String> map, HttpServletResponse response){
        //1.从参数中获取手机号，redis中获取验证码，从参数中获取验证码
        //从参数中获取手机号
        String telephone = map.get("telephone");
        //从参数中获取验证码
        String validateCodeInParam = map.get("validateCode");
        //redis中获取验证码
        String validateCodeInRedis = jedisPool.getResource().get(RedisConst.SENDTYPE_LOGIN + "-" + telephone);
        if(!(validateCodeInParam != null && validateCodeInParam.equals(validateCodeInRedis))){
            return new Result(false, MessageConst.VALIDATECODE_ERROR);
        }
        //判断是否是会员，如果不是会员，自动注册
        Member member = memberService.findByTelephone(telephone);
        if(member == null){
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }
        //把手机号码存储到cookies（用来跟踪你的动向）
        Cookie cookie = new Cookie("login_telephone" , telephone );
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new Result(true,MessageConst.LOGIN_SUCCESS);
    }
}
