package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.SysUser;
import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    UserService userService;

    /**
     * 根据用户名加载用户信息
     *      1. 根据用户名从数据库查询用户信息（包含权限和角色）
     *      2. 封装UserDetails 返回安全框架
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1. 根据用户名从数据库查询用户信息（包含权限和角色）
        SysUser sysUser = userService.findByUsername(username);
        //2. 封装UserDetails 返回安全框架
        if(sysUser != null){
            //创建权限列表
            List<GrantedAuthority> authorityList = new ArrayList<>();
            for (Role role : sysUser.getRoles()) {
                for (Permission permission : role.getPermissions()) {
                    //创建权限对象
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission.getKeyword());
                    //添加到权限集合中
                    authorityList.add(authority);
                }
            }


            /**
             * 参数1： 用户名
             * ··2： 密码
             * ··3： 权限列表
             */
            User user = new User(sysUser.getUsername(),sysUser.getPassword(),authorityList);
            return user;
        }
        return null;
    }
}
