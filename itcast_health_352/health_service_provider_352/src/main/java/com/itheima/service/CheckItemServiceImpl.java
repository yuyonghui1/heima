package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //1.开始分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //2. 根据条件查询
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());
        //3. 创建PageResult
        PageResult pageResult = new PageResult(page.getTotal(),page);
        return pageResult;
    }

    /**
     * 1. 是否被检查组关联
     *  如果被检查组关联，提示不能删除
     *  如果没有被关联，直接删除
     * @param id
     */
    @Transactional
    @Override
    public void delById(Integer id) {
        long count = checkItemDao.findCountById(id);
        if(count > 0) {
            //说明被关联
            throw new RuntimeException("该检查项被检查组关联，不能删除！！");
        }else {
            checkItemDao.delById(id);
        }

    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
