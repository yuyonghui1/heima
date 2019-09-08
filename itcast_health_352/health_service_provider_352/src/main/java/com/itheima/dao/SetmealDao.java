package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface SetmealDao {
    void add(Setmeal setmeal);

    void set(Integer setmealId, Integer checkgroupId);

    Page<Setmeal> findByCondition(String queryString);

    List<Setmeal> findAll();

    Setmeal findDetailsById(Integer id);

    Setmeal findById(Integer id);

    List<Map<String,Object>> findSetmealReport();

    /**
     * 热门套餐
     * @return
     */
    List<Map<String,Object>> getHotSetmeal();
}
