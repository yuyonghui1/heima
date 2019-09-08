package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConst;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    CheckItemService checkItemService;


    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
            return  new Result(true, MessageConst.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConst.ADD_CHECKITEM_FAIL);
        }
    }
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.findPage(queryPageBean);
        return pageResult;
    }

    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    @RequestMapping("/delById")
    public Result delById(Integer id){
        try {
            checkItemService.delById(id);
            return  new Result(true, MessageConst.DELETE_CHECKITEM_SUCCESS);
        }catch (RuntimeException e) {
            e.printStackTrace();
            return  new Result(false, e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConst.DELETE_CHECKITEM_FAIL);
        }
    }
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckItem checkItem = checkItemService.findById(id);
            return  new Result(true, MessageConst.QUERY_CHECKITEM_SUCCESS, checkItem);
        }catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConst.QUERY_CHECKITEM_FAIL);
        }

    }

    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
            return  new Result(true, MessageConst.EDIT_CHECKITEM_SUCCESS);
        }catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConst.EDIT_CHECKITEM_FAIL);
        }
    }
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckItem> checkItemList =  checkItemService.findAll();
            return new Result(true,MessageConst.QUERY_CHECKITEM_SUCCESS, checkItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.QUERY_CHECKITEM_FAIL);
        }
    }
}
