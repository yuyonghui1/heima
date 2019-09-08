package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConst;
import com.itheima.constant.RedisConst;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    OrderService orderService;

    @Autowired
    JedisPool jedisPool;

    /**
     * 步骤
     *   1. 校验验证码
     *          1）从redis中获取验证码
     *          2）从参数中获取验证码
     *          3） 开始校验， 如果校验成功，继续预约，如果校验失败，返回校验失败
     *   2. 设置预约方式
     *   3. 开始预约
     *
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map<String, Object> map){
        //获取手机号码
        Object telephoneObj = map.get("telephone");
        String telephone = String.valueOf(telephoneObj);
        //1. 校验验证码
       // *  1）从redis中获取验证码
        String validateCodeInRedis = jedisPool.getResource().get(RedisConst.SENDTYPE_ORDER + "-" + telephone);
       //*   2）从参数中获取验证码
        String validateCodeInParam = String.valueOf(map.get("validateCode"));
       // *  3） 开始校验， 如果校验成功，继续预约，如果校验失败，
        if(!(validateCodeInParam != null && validateCodeInParam.equals(validateCodeInRedis))){
            return new Result(false, MessageConst.VALIDATECODE_ERROR);
        }
        // 2. 设置预约方式
        map.put("orderType", Order.ORDERTYPE_WEIXIN);
        // 3. 开始预约
        Result result = orderService.addOrder(map);
        if(result.isFlag()){
            System.out.println("预约成功");
            //预约成功后，发送成功的通知短信给客户
        }
        return result;
    }

    /**
     * 需要的数据
     *      体检人：
     *      预约日期：
     *      预约的套餐：
     *      预约类型：
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map<String,Object> map =  orderService.findById(id);
            Date orderDate = (Date) map.get("orderDate");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String orderDateStr = simpleDateFormat.format(orderDate);
            map.put("orderDate", orderDateStr);
            return new Result(true,MessageConst.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.QUERY_ORDER_FAIL);
        }
    }
}
