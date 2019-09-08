package com.itheima;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public class MonthDemo {

    public static void main(String[] args) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -11);

        for (int i = 0; i < 12; i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM");
            String str = simpleDateFormat.format(calendar.getTime());
            System.out.println(str);
            calendar.add(Calendar.MONTH, 1);
        }



    }
}
