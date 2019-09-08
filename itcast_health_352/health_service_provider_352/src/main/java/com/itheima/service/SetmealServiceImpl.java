package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConst;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;

    @Autowired
    JedisPool jedisPool;

    /**
     * 步骤
     *  1. 添加基本信息（回显主键）
     *  2. 维护套餐和检查组的关系
     * @param checkgroupIds
     * @param setmeal
     */
    @Override
    public void add(Integer[] checkgroupIds, Setmeal setmeal) {
        //1. 添加基本信息（回显主键）
        setmealDao.add(setmeal);
        //2. 维护套餐和检查组的关系
        if(setmeal.getId() != null && checkgroupIds != null && checkgroupIds.length > 0){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.set(setmeal.getId(), checkgroupId);
            }
        }
        // 说明添加到数据库中，把图片名称存储redis
        jedisPool.getResource().sadd(RedisConst.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
    }

    @Override
    public PageResult queryPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Setmeal> page = setmealDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }
    /**
     * 1. 根据id查询套餐信息， 在mybatis中映射查询检查组和检查项
     *
     *
     * 2. 根据id查询套餐信息
     *      根据套餐id查询检查组信息
     *      根据每个检查组的id查询检查项信息
     *     数据组合
     *
     *
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailsById(Integer id) {
        return setmealDao.findDetailsById(id);
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    /**
     * setmealCount:[
     *                      {value :100, name: '直接访问'},
     *                      {value :100, name:'邮件营销'}
     *              ]
     * @return
     */
    @Override
    public List<Map<String, Object>> getSetemalReport() {
        return setmealDao.findSetmealReport();
    }
}
