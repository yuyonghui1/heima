package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao;

    /**
     *  1. 根据预约日期查询预设置对象
             *  如果存在，则修改
             *      在修改时需要判断，已预约人数是否大于可预约人数
             *              如果大于可以预约人数，不能修改
             *              如果小于可预约人数，可以修改
             *  如果不存在： 则添加
     * @param orderSettingList
     */
    @Override
    public void addOrderSettingList(List<OrderSetting> orderSettingList) {
        if(orderSettingList != null && orderSettingList.size() > 0){
                for (OrderSetting orderSetting : orderSettingList) {
                    //根据预约日期查询预设置对象
                    OrderSetting orderSettingDb = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
                    // 如果存在，则修改
                    if (orderSettingDb != null){
                        //在修改时需要判断，已预约人数(数据库中)是否大于可预约人数（用户添加的）
                        //如果大于可以预约人数，不能修改
                        if(orderSettingDb.getReservations() > orderSetting.getNumber()){
                            throw new RuntimeException("可预约人数不能小于已预约人数");
                        }
                        //如果小于可预约人数，可以修改
                        else{
                            orderSettingDao.edit(orderSetting);
                        }
                    }
                    //如果不存在： 则添加
                    else{
                        orderSettingDao.add(orderSetting);
                    }

                }
        }
    }

    @Override
    public List<OrderSetting> findByMonth(String month) {
        //month = 2019-08
        String startDate = month + "-01";
        String endDate = month + "-31";
        Map<String ,String> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);

        List<OrderSetting> orderSettingList = orderSettingDao.findByMonth(map);

        return orderSettingList;
    }
}
