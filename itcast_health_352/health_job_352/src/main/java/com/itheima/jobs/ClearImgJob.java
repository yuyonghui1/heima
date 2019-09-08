package com.itheima.jobs;

import com.itheima.constant.RedisConst;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public class ClearImgJob {


    @Autowired
    JedisPool jedisPool;

    /**
     * 清理垃圾图片
     * 步骤：
     *      1. 找出redis集合中两个集合的差值，确定垃圾图片名称
     *      2. 调用qiniu工具类删除垃圾图片（七牛服务器）
     *          redis'中垃圾图片名称也需要删除
     */
    public void clear(){
        //1. 找出redis集合中两个集合的差值，确定垃圾图片名称
        Set<String> imgNames = jedisPool.getResource().sdiff(RedisConst.SETMEAL_PIC_RESOURCES, RedisConst.SETMEAL_PIC_DB_RESOURCES);
        // 2. 调用qiniu工具类删除垃圾图片（七牛服务器）
        //              redis'中垃圾图片名称也需要删除
        for (String imgName : imgNames) {
            QiniuUtils.deleteFileFromQiniu(imgName);
            jedisPool.getResource().srem(RedisConst.SETMEAL_PIC_RESOURCES, imgName);
        }
    }
}
