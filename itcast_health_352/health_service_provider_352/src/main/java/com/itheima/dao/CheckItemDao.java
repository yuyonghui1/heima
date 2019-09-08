package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface CheckItemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> findByCondition(String queryString);

    long findCountById(Integer id);

    void delById(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    List<CheckItem> findCheckItemsByCheckGroupId(Integer checkGroupId);
}
