package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConst;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    MemberService memberService;

    @Reference
    SetmealService setmealService;

    @Reference
    ReportService reportService;


    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        try {
            Map<String,Object> map = memberService.getMemberReport();
            return new Result(true, MessageConst.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConst.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try {
            List<Map<String,Object>> setmealCount = setmealService.getSetemalReport();
            //取出集合中的name值赋值另外一个集合
            List<Object> setmealNames = new ArrayList<>();
            for (Map<String, Object> map : setmealCount) {
                Object name = map.get("name");
                setmealNames.add(name);
            }

            //组合数据
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("setmealNames", setmealNames);
            resultMap.put("setmealCount",setmealCount );
            return new Result(true,MessageConst.GET_SETMEAL_COUNT_REPORT_SUCCESS, resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {

            Map<String,Object> map =  reportService.getBusinessReportData();
            return new Result(true,MessageConst.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.GET_BUSINESS_REPORT_FAIL);
        }

    }

    /**
     * 导出数据到excel中
     *  1. 获取数据， 调用service获取数据
     *  2. 把数据写入到excel中
     *  3. 把excel文件下载到本地
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    public String exportBusinessReport(HttpServletResponse response){
        try {
            //1. 获取数据， 调用service获取数据
            Map<String,Object> map =  reportService.getBusinessReportData();
//            reportDate:null,   统计日期（当前时间）
            String reportDate = String.valueOf(map.get("reportDate"));
//            todayNewMember :0, 今日新增会员
            String todayNewMember = String.valueOf(map.get("todayNewMember"));
//            totalMember :0,    总会员数
            String totalMember = String.valueOf(map.get("totalMember"));
//            thisWeekNewMember:0,  本周新增会员， 本周一以后注册的会员
            String thisWeekNewMember = String.valueOf(map.get("thisWeekNewMember"));
//            thisMonthNewMember :0,  本月新增会员，本月1号以后注册的会员
            String thisMonthNewMember = String.valueOf(map.get("thisMonthNewMember"));
//            todayOrderNumber :0,    今日预约人数，今日应该来了多少（）
            String todayOrderNumber = String.valueOf(map.get("todayOrderNumber"));
//            todayVisitsNumber :0,   今日到诊人数， 今日实际来了多少
            String todayVisitsNumber = String.valueOf(map.get("todayVisitsNumber"));
//            thisWeekOrderNumber :0,  本周预约人数， 本周应该来多少人（从本周一到本周日预约来多少人）
            String thisWeekOrderNumber = String.valueOf(map.get("thisWeekOrderNumber"));
//            thisWeekVisitsNumber :0, 本周到诊人数， 本周实际类了多少人（从本周一至今实际来了多少人）
            String thisWeekVisitsNumber = String.valueOf(map.get("thisWeekVisitsNumber"));
//            thisMonthOrderNumber :0, 本月预约人数， 本月应该来多少人（从本月1号到最后一天预约来多少人）
            String thisMonthOrderNumber = String.valueOf(map.get("thisMonthOrderNumber"));
//            thisMonthVisitsNumber :0,本月到诊人数，本月实际类了多少人（从本月1号至今实际来了多少人）
            String thisMonthVisitsNumber = String.valueOf(map.get("thisMonthVisitsNumber"));
//            hotSetmeal
            List<Map<String,Object>> hotSetmeal = (List<Map<String, Object>>) map.get("hotSetmeal");
            //2. 把数据写入到excel中
            //获取模板文件的流对象
            InputStream inputStream = ReportController.class.getResourceAsStream("/template/report_template.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            //获取工作表对象
            XSSFSheet sheet = workbook.getSheetAt(0);
            //设置 统计日期
            //获取行对象
            XSSFRow row = sheet.getRow(2);
            //获取单元格对象
            XSSFCell cell = row.getCell(5);
            cell.setCellValue(reportDate);
            //设置新增会员数
            //获取行对象
            row = sheet.getRow(4);
            //获取单元格对象
            cell = row.getCell(5);
            cell.setCellValue(todayNewMember);

            //设置总会员数
            //获取行对象
            row = sheet.getRow(4);
            //获取单元格对象
            cell = row.getCell(7);
            cell.setCellValue(totalMember);

            //设置本周新增会员数
            //获取行对象
            row = sheet.getRow(5);
            //获取单元格对象
            cell = row.getCell(5);
            cell.setCellValue(thisWeekNewMember);

            //设置本月新增会员数
            //获取行对象
            row = sheet.getRow(5);
            //获取单元格对象
            cell = row.getCell(7);
            cell.setCellValue(thisMonthNewMember);

            //设置今日预约人数
            //获取行对象
            row = sheet.getRow(7);
            //获取单元格对象
            cell = row.getCell(5);
            cell.setCellValue(todayOrderNumber);

            //设置今日到诊数
            //获取行对象
            row = sheet.getRow(7);
            //获取单元格对象
            cell = row.getCell(7);
            cell.setCellValue(todayVisitsNumber);


            //设置本周预约数
            //获取行对象
            row = sheet.getRow(8);
            //获取单元格对象
            cell = row.getCell(5);
            cell.setCellValue(thisWeekOrderNumber);

            //设置本周到诊数
            //获取行对象
            row = sheet.getRow(8);
            //获取单元格对象
            cell = row.getCell(7);
            cell.setCellValue(thisWeekVisitsNumber);

            //设置本月预约人数
            //获取行对象
            row = sheet.getRow(9);
            //获取单元格对象
            cell = row.getCell(5);
            cell.setCellValue(thisMonthOrderNumber);

            //设置本月到诊数
            //获取行对象
            row = sheet.getRow(9);
            //获取单元格对象
            cell = row.getCell(7);
            cell.setCellValue(thisMonthVisitsNumber);

            int rowNum = 12;
            //设置热门套餐
            for (Map<String, Object> setmeal : hotSetmeal) {
                //套餐设置到指定的位置
                //获取行
                row = sheet.getRow(rowNum);
                //获取行
                cell = row.getCell(4);
                cell.setCellValue(String.valueOf(setmeal.get("name")));

                cell = row.getCell(5);
                cell.setCellValue(String.valueOf(setmeal.get("setmeal_count")));

                cell = row.getCell(6);
                cell.setCellValue(String.valueOf(setmeal.get("proportion")));
                rowNum ++ ;
            }
            // 通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition","attachment;filename="+reportDate+"_report.xlsx");
            workbook.write(out);
            out.flush();
            out.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
