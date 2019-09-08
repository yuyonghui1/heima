package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    PermissionDao permissionDao;

    /**
     * 1. 在mybatis中实现一对多的查询
     * 2. 根据用户名查询用户对象
     *      根据用户id查询角色对象
     *      根据每个角色的id查询相应的权限对象
     *      组合数据
     * @param username
     * @return
     */
    @Override
    public SysUser findByUsername(String username) {
        //根据用户名查询用户对象
        SysUser sysUser = userDao.findByUsername(username);
        //根据用户id查询角色对象
        Set<Role> roleSet = roleDao.findRolesByUserId(sysUser.getId());
        //根据每个角色的id查询相应的权限对象
        for (Role role : roleSet) {
            Set<Permission> permissionSet = permissionDao.findPermissionsByRoleId(role.getId());
            //把权限列表添加到角色中
            role.setPermissions(permissionSet);
        }
        //把角色列表添加到用户对象中
        sysUser.setRoles(roleSet);
        return sysUser;
    }
}
