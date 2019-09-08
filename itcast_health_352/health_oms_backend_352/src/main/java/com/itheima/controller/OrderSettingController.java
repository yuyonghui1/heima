package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConst;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    OrderSettingService orderSettingService;


    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try {
            List<String[]> stringsList = POIUtils.readExcel(excelFile);
            //把stringsList 的数据转移到List<OrderSetting>中
            List<OrderSetting> orderSettingList = new ArrayList<>();
            for (String[] strings : stringsList) {
                OrderSetting orderSetting = new OrderSetting();
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                //设置预约 日期
                orderSetting.setOrderDate(format.parse(strings[0]));
                //设置可预约人数
                orderSetting.setNumber(Integer.parseInt(strings[1]));

                orderSettingList.add(orderSetting);
            }

            orderSettingService.addOrderSettingList(orderSettingList);

            return new Result(true, MessageConst.IMPORT_ORDERSETTING_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConst.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/findByMonth")
    public Result findByMonth(String month){
        try {
            List<OrderSetting> orderSettingList = orderSettingService.findByMonth(month);
            /**
             * 获取的数据
             * [
             *      {id:1,orderDate:"2019-08-01", number:500， reservations: 1 },
             *      {id:2,orderDate:"2019-08-02", number:500， reservations: 1 },
             *      {id:3,orderDate:"2019-08-03", number:500， reservations: 1 }
             * ]
             * 需要的数据
             *   [
             //     { date: 21, number: 120, reservations: 1 },
             //     { date: 13, number: 120, reservations: 1 },
             //     { date: 24, number: 120, reservations: 120 },
             //     { date: 26, number: 120, reservations: 1 },
             //     { date: 28, number: 120, reservations: 1 }
             // ]
             */
            //定义页面的数据结构
            List<Map<String,Object>> mapList = new ArrayList<>();
            for (OrderSetting orderSetting : orderSettingList) {
                Map<String,Object> map = new HashMap<>();
                Date orderDate = orderSetting.getOrderDate();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
                String date = simpleDateFormat.format(orderDate);
                map.put("date",date);
                map.put("number", orderSetting.getNumber() );
                map.put("reservations", orderSetting.getReservations());
                //添加到mapList中
                mapList.add(map);
            }
            return  new Result(true,MessageConst.GET_ORDERSETTING_SUCCESS, mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,MessageConst.GET_ORDERSETTING_FAIL);
        }
    }


    @RequestMapping("/edit")
    public Result edit(@RequestBody OrderSetting orderSetting){
        try {
            List<OrderSetting> orderSettingList = new ArrayList<>();
            orderSettingList.add(orderSetting);
            orderSettingService.addOrderSettingList(orderSettingList);
            return new Result(true, MessageConst.ORDERSETTING_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConst.ORDERSETTING_FAIL);
        }
    }
}
