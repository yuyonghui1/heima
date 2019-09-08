package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    /**
     * 需要的数据
     *      months: [2018.10,.....,2019.9]
     *      memberCount: [100,.....,1000]
     * @return
     */
    @Override
    public Map<String, Object> getMemberReport() {

        //获取最近的12个月
        List<String> months = new ArrayList<>();
        List<Long> memberCount = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -11);

        for (int i = 0; i < 12; i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            String str = simpleDateFormat.format(calendar.getTime());
            calendar.add(Calendar.MONTH, 1);
            months.add(str);
        }
        //memberCount:每个会员数量
        for (String date : months) {
            // 2018-10-31
            date += "-31";
            long count = memberDao.findByBeforeDate(date);
            memberCount.add(count);
        }

        //组合数据
        Map<String,Object> map = new HashMap<>();
        map.put("months",months);
        map.put("memberCount",memberCount);

        return map;
    }
}
