package com.aliveseven.adminmanage.service;

import com.aliveseven.adminmanage.dto.UserDto;
import com.aliveseven.adminmanage.entity.Menu;
import com.aliveseven.adminmanage.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AliveSeven
 * @since 2022-10-13
 */
public interface IUserService extends IService<User> {

    UserDto login(UserDto userDto);

    UserDto register(UserDto userDto);

    List<Integer> selectMenuIdsByUserId(Integer id);

    List<Menu> selectMenuByUserId(Integer id);
}
