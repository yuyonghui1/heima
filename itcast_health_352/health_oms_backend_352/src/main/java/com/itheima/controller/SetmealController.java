package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConst;
import com.itheima.constant.RedisConst;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;


    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile){

        try {
            //获取uuid，作为文件名
            String uuid = UUID.randomUUID().toString().replace("-", "");
            //截取文件的后缀名
            String originalFilename = imgFile.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

            String serverImgName = uuid + suffix;
            //文件上传到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),serverImgName);
            //把图片名称存储到redis
            jedisPool.getResource().sadd(RedisConst.SETMEAL_PIC_RESOURCES , serverImgName);
            return new Result(true, MessageConst.PIC_UPLOAD_SUCCESS,serverImgName );
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConst.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(Integer[] checkgroupIds, @RequestBody Setmeal setmeal){
        try {
            setmealService.add(checkgroupIds, setmeal);

            return new Result(true, MessageConst.ADD_SETMEAL_SUCCESS );
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConst.ADD_SETMEAL_FAIL);
        }
    }


    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.queryPage(queryPageBean);
    }
}
