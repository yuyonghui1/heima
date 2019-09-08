package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface CheckGroupDao {
    void add(CheckGroup checkGroup);

    void set(@Param("checkGroupId") Integer id, @Param("checkItemId") Integer checkitemId);

    Page<CheckGroup> findByCondition(String queryString);

    CheckGroup findById(Integer id);

    Integer[] findCheckItemIdsById(Integer id);

    void edit(CheckGroup checkGroup);

    void delAssociation(Integer id);

    long findCheckItemCountById(Integer id);

    long findSetmealCountById(Integer id);

    void delById(Integer id);

    List<CheckGroup> findAll();


    List<CheckGroup> findCheckGroupsBySetmealId(Integer setmealId);
}
