package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.SetmealDao;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    SetmealDao setmealDao;


    /**
     *   reportDate:null,   统计日期（当前时间）
         todayNewMember :0, 今日新增会员
         totalMember :0,    总会员数
         thisWeekNewMember:0,  本周新增会员， 本周一以后注册的会员
         thisMonthNewMember :0,  本月新增会员，本月1号以后注册的会员
         todayOrderNumber :0,    今日预约人数，今日应该来了多少（）
         todayVisitsNumber :0,   今日到诊人数， 今日实际来了多少
         thisWeekOrderNumber :0,  本周预约人数， 本周应该来多少人（从本周一到本周日预约来多少人）
         thisWeekVisitsNumber :0, 本周到诊人数， 本周实际类了多少人（从本周一至今实际来了多少人）
         thisMonthOrderNumber :0, 本月预约人数， 本月应该来多少人（从本月1号到最后一天预约来多少人）
         thisMonthVisitsNumber :0,本月到诊人数，本月实际类了多少人（从本月1号至今实际来了多少人）
         hotSetmeal :[
             {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
             {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
        ]
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        //获取今日日期
        String todayDate = DateUtils.parseDate2String(new Date());
        //获取本周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获取本周日的日期
        String thisWeekSunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
        //获取本月1号的日期
        String thisMonthFirstDay = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        //获取本月最后一天的日期
        String thisMonthLastDay = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());

        //reportDate:null,   统计日期（当前时间）
        String reportDate = todayDate;
        //todayNewMember :0, 今日新增会员
        long todayNewMember = memberDao.findTodayNewMember(todayDate);
        //totalMember :0,    总会员数
        long totalMember = memberDao.findTotalMember();
        //thisWeekNewMember:0,  本周新增会员， 本周一以后注册的会员
        long thisWeekNewMember = memberDao.findNewMemberByAfterDate(thisWeekMonday);
        //thisMonthNewMember :0,  本月新增会员，本月1号以后注册的会员
        long thisMonthNewMember = memberDao.findNewMemberByAfterDate(thisMonthFirstDay);
        //todayOrderNumber :0,    今日预约人数，今日应该来了多少（）
        long todayOrderNumber = orderDao.findTodayOrderNumber(todayDate);
        // todayVisitsNumber :0,   今日到诊人数， 今日实际来了多少
        long todayVisitsNumber = orderDao.findTodayVisitsNumber(todayDate);
        //thisWeekOrderNumber :0,  本周预约人数， 本周应该来多少人（从本周一到本周日预约来多少人）
        long thisWeekOrderNumber = orderDao.findOrderNumberByDate(thisWeekMonday,thisWeekSunday);
        //thisWeekVisitsNumber :0, 本周到诊人数， 本周实际来了多少人（从本周一至今实际来了多少人）
        long  thisWeekVisitsNumber = orderDao.findVisitsNumberByAfterDate(thisWeekMonday);
        //thisMonthOrderNumber :0, 本月预约人数， 本月应该来多少人（从本月1号到最后一天预约来多少人）
        long thisMonthOrderNumber = orderDao.findOrderNumberByDate(thisMonthFirstDay,thisMonthLastDay);
        //thisMonthVisitsNumber :0,本月到诊人数，本月实际类了多少人（从本月1号至今实际来了多少人）
        long  thisMonthVisitsNumber = orderDao.findVisitsNumberByAfterDate(thisMonthFirstDay);
        //hotSetmeal: 热门套餐(查询排名最高的两个)
        List<Map<String,Object>> hotSetmeal = setmealDao.getHotSetmeal();

        //把数据存储到map集合中
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("reportDate",reportDate);
        resultMap.put("todayNewMember",todayNewMember);
        resultMap.put("totalMember",totalMember);
        resultMap.put("thisWeekNewMember",thisWeekNewMember);
        resultMap.put("thisMonthNewMember",thisMonthNewMember);
        resultMap.put("todayOrderNumber",todayOrderNumber);
        resultMap.put("todayVisitsNumber",todayVisitsNumber);
        resultMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        resultMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        resultMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        resultMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        resultMap.put("hotSetmeal",hotSetmeal);


        return resultMap;
    }
}
