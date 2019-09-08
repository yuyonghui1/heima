package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member);

    Map<String,Object> getMemberReport();
}
