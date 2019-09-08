package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface OrderDao {
    long findByByOrder(Order condition);

    void add(Order order);

    Map<String,Object> findById(Integer id);

    /**
     * 今日预约数
     * @param todayDate
     * @return
     */
    long findTodayOrderNumber(String todayDate);

    /**
     * 今日到诊数
     * @param todayDate
     * @return
     */
    long findTodayVisitsNumber(String todayDate);

    /**
     * 某两个日期间的预约人数
     * @param startDate
     * @param endDate
     * @return
     */
    long findOrderNumberByDate(String startDate, String endDate);

    /**
     * 某日期之后到诊数
     * @param date
     * @return
     */
    long findVisitsNumberByAfterDate(String date);
}
