package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConst;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
public class OrderServiceImpl implements  OrderService{

    @Autowired
    OrderSettingDao orderSettingDao;
    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderDao orderDao;

    /**
     * 步骤分析
     *  1. 是否进行了预约设置，如果没有进行预约设置， 返回错误结果
     *      根据日期查询预约设置对象，判断是否为null
     *  2. 判断是否预约已满
     *      已预约人数大于等于可预约人数， 预约已满，返回错误结果
     *      已预约人数小于可预约人数，继续
     *  3. 判断是否是会员
     *      根据手机号查询会员，如果存在，是会员，继续
     *      如果不存在，不是会员，自动注册为会员
     *  4. 判断是否重复预约（ 该会员预约该日期，预约了改套餐）
     *      如果重复预约，返回错误结果
     *      如果没有重复预约，继续
     *  5. 添加预约信息
     *  6. 修改已预约人数
     * @param map
     * @return
     */
    @Override
    public Result addOrder(Map<String, Object> map) {
        //获取日期
        String orderDateStr = String.valueOf(map.get("orderDate"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date orderDate = null;
        try {
            orderDate = format.parse(orderDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("日期转换失败");
        }
        //获取手机号码
        String telephone = String.valueOf(map.get("telephone"));
        //获取套餐id
        Integer setmealId = Integer.parseInt(String.valueOf(map.get("setmealId")));
        // 1. 是否进行了预约设置，
        //获取预约设置对象
        OrderSetting orderSettingDb = orderSettingDao.findByOrderDate(orderDate);
        if(orderSettingDb == null){
            return new Result(false, MessageConst.SELECTED_DATE_CANNOT_ORDER);
        }
        //2. 判断是否预约已满
        if(orderSettingDb.getReservations() >= orderSettingDb.getNumber()){
            return new Result(false, MessageConst.ORDER_FULL);
        }
        //3. 判断是否是会员
        Member member = memberDao.findByTelephone(telephone);
        //说明不是会员，自动注册为会员
        if(member == null){
            member = new Member();
            member.setName(String.valueOf(map.get("name")));
            member.setSex(String.valueOf(map.get("sex")));
            member.setIdCard(String.valueOf(map.get("idCard")));
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            //添加会员，回显主键
            memberDao.add(member);
        }else{
            // 4. 判断是否重复预约
            //该会员在该日期预约了该套餐===重复预约
            //条件：预约日期， 会员id，  套餐id

            Order condition = new Order();
            condition.setOrderDate(orderDate);
            condition.setSetmealId(setmealId);
            condition.setMemberId(member.getId());

            long count = orderDao.findByByOrder(condition);
            if(count > 0){
                return new Result(false, MessageConst.HAS_ORDERED);
            }
        }
        //5. 添加预约信息
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(orderDate);
        order.setOrderType(String.valueOf(map.get("orderType")));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(setmealId);
        orderDao.add(order);
        //6. 修改已预约人数
        orderSettingDb.setReservations(orderSettingDb.getReservations() + 1);
        orderSettingDao.edit(orderSettingDb);

        return new Result(true,MessageConst.ORDER_SUCCESS, order);
    }

    @Override
    public Map<String, Object> findById(Integer id) {
        return orderDao.findById(id);
    }


}
