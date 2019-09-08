package com.itheima.controller;

import com.itheima.constant.MessageConst;
import com.itheima.constant.RedisConst;
import com.itheima.entity.Result;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    JedisPool jedisPool;


    /**
     * 为预约发送验证码
     * 步骤：
     *      1. 生成验证码
     *      2. 发送验证码到客户手机
     *      3. 存储到redis
     *
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        try {
            //1. 生成验证码
            Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
            //2. 发送验证码到客户手机
            //Short Message Send
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, String.valueOf(validateCode));
            //3. 存储到redis
            //setex ：带有定时的键值对
            jedisPool.getResource().setex(RedisConst.SENDTYPE_ORDER + "-" + telephone ,5 * 60, String.valueOf(validateCode));
            return new Result(true, MessageConst.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConst.SEND_VALIDATECODE_FAIL);
        }
    }

    /**
     * 步骤
     *   1. 生成验证码（ValidateCodeUtils）
     *   2. 发送验证码到手机端（SMSUtils）
     *   3. 存储验证码到redis
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){

        try {
//        1. 生成验证码（ValidateCodeUtils）
            Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
//        2. 发送验证码到手机端（SMSUtils）
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE , telephone, String.valueOf(validateCode) );
//        3. 存储验证码到redis
            jedisPool.getResource().setex(RedisConst.SENDTYPE_LOGIN + "-" + telephone, 5 * 60, String.valueOf(validateCode));
            return new Result(true,MessageConst.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.SEND_VALIDATECODE_FAIL);
        }
    }

}
