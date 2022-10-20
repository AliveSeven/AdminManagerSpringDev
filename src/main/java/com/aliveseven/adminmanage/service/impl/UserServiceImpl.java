package com.aliveseven.adminmanage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.aliveseven.adminmanage.common.Constants;
import com.aliveseven.adminmanage.common.RoleEnum;
import com.aliveseven.adminmanage.dto.UserDto;
import com.aliveseven.adminmanage.entity.User;
import com.aliveseven.adminmanage.exception.ServiceException;
import com.aliveseven.adminmanage.mapper.UserMapper;
import com.aliveseven.adminmanage.service.IUserService;
import com.aliveseven.adminmanage.utils.TokenUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author AliveSeven
 * @since 2022-10-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    // 登录业务
    public UserDto login(UserDto userDto){
        // 用户密码 md5加密
        User one = getUserInfo(userDto);
        if(one != null){
            // 将数据库里面查到的one对象的信息，头像之类的copy给userDto对象返回出去
            BeanUtil.copyProperties(one , userDto ,true);
            // 设置token
            String token = TokenUtils.genToken(one.getId().toString() , one.getPassword());
            userDto.setToken(token);
            userDto.setPassword(SecureUtil.md5(userDto.getPassword()));
            return userDto;
        } else {
            throw new ServiceException(Constants.CODE_600 , "用户名或密码错误");
        }
    }

    // 注册业务
    public UserDto register(UserDto userDto){
        // 获取传过来的数据，调用方法查看数据是否存在了
        User one = getUserInfo(userDto);
        if(one == null){  // 如果数据没有在表中存在
            one = new User();
            BeanUtil.copyProperties(userDto, one, true);
            // 默认一个普通用户的角色
            one.setRole(RoleEnum.ROLE_STUDENT.toString());
            if (one.getNickname() == null) {
                one.setNickname(one.getUsername());
            }
            save(one);  // 把 copy完之后的用户对象存储到数据库
            // 用户密码 md5加密
            userDto.setPassword(SecureUtil.md5(userDto.getPassword()));
            return userDto;
        } else {
            throw new ServiceException(Constants.CODE_600 , "用户名或密码错误");
        }
    }

    private User getUserInfo(UserDto userDto){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username" , userDto.getUsername());
        queryWrapper.eq("password" , userDto.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        }catch (Exception e){
            log.error(e.toString());
            throw new ServiceException(Constants.CODE_500 , "系统错误");
        }
        return one;
    }
}
