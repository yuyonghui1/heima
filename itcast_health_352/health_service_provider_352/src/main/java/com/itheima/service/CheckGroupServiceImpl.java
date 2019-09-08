package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    CheckGroupDao checkGroupDao;

    /**
     * 步骤
     *      1. 添加检查组的基本信息（主键回显（selectKey））
     *      2. 维护检查组与检查项中间表的关系
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //1. 添加检查组的基本信息（主键回显（selectKey））
        checkGroupDao.add(checkGroup);
        // 2. 维护检查组与检查项中间表的关系
        if(checkGroup.getId() != null && checkitemIds != null && checkitemIds.length > 0 ){
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.set(checkGroup.getId(), checkitemId);
            }
        }
    }

    @Override
    public PageResult queryPage(QueryPageBean queryPageBean) {
        //1. 开始分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //2. 条件查询
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page);
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public Integer[] findCheckItemIdsById(Integer id) {

        return checkGroupDao.findCheckItemIdsById(id);
    }

    /**
     * 步骤
     *  1. 修改检查组的基本数据
     *  2. 维护检查组和检查项的关系
     *         2.1 先删除该检查组与检查项的关系
     *         2.2 维护检查组和检查项新的关系
     * @param checkitemIds
     * @param checkGroup
     */
    @Override
    public void edit(Integer[] checkitemIds, CheckGroup checkGroup) {
        //1. 修改检查组的基本数据
        checkGroupDao.edit(checkGroup);
        // 2. 维护检查组和检查项的关系
        //2.1 先删除该检查组与检查项的关系
        checkGroupDao.delAssociation(checkGroup.getId());
        //2.2 维护检查组和检查项新的关系
        if(checkitemIds != null && checkitemIds.length > 0){
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.set(checkGroup.getId(), checkitemId);
            }
        }
    }

    /**
     * 1. 判断检查组与检查项是否有关系，如果有关系，先删除关系表数据，再继续
     * 2. 判断检查组与套餐是否有关系，如果有关系，抛出异常
     *
     * @param id
     */
    @Override
    @Transactional
    public void delById(Integer id) {
        long count = checkGroupDao.findCheckItemCountById(id);
        if(count > 0){
            checkGroupDao.delAssociation(id);
        }
        long count2 = checkGroupDao.findSetmealCountById(id);
        if(count2 > 0){
            throw new RuntimeException("检查组被套餐关联，不能删除！！");
        }
        //最终删除检查组
        checkGroupDao.delById(id);

    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
