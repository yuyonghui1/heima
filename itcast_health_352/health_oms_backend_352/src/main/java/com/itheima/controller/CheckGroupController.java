package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConst;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
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
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    CheckGroupService checkGroupService;


    @RequestMapping("/add")
    public Result add(Integer[] checkitemIds , @RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.add(checkGroup,checkitemIds);
            return new Result(true, MessageConst.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.ADD_CHECKGROUP_FAIL);
        }
    }
//    @RequestMapping("/add")
//    public Result add(@RequestBody Map<String, Object> map){
//        JSONObject jsonObject = (JSONObject) map.get("checkGroup");
//        //JSONObject包json串转换为对象
//        CheckGroup checkGroup = jsonObject.toJavaObject(CheckGroup.class);
//        System.out.println(checkGroup);
//
//        JSONArray jsonArray = (JSONArray) map.get("checkitemIds");
//        //JSONArray包json串转换为数组对象
//        Integer[] checkItemIds = jsonArray.toArray(new Integer[]{});
//        System.out.println(Arrays.toString(checkItemIds));
//        return null;
//    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkGroupService.queryPage(queryPageBean);
        return pageResult;
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true,MessageConst.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findCheckItemIdsById")
    public Result findCheckItemIdsById(Integer id){
        try {
            Integer[] checkItemIds = checkGroupService.findCheckItemIdsById(id);
            return new Result(true,MessageConst.QUERY_CHECKGROUP_SUCCESS, checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(Integer[] checkitemIds, @RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.edit(checkitemIds, checkGroup);
            return new Result(true,MessageConst.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.EDIT_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/delById")
    public Result delById(Integer id){
        try {
            checkGroupService.delById(id);
            return new Result(true,MessageConst.DELETE_CHECKGROUP_SUCCESS);
        }catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.DELETE_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckGroup> checkGroupList =  checkGroupService.findAll();
            return new Result(true,MessageConst.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConst.QUERY_CHECKGROUP_FAIL);
        }
    }
}
