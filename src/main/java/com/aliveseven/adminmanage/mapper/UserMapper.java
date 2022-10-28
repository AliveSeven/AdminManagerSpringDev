package com.aliveseven.adminmanage.mapper;

import com.aliveseven.adminmanage.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AliveSeven
 * @since 2022-10-13
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT menu_id from sys_user AS su " +
            "LEFT JOIN sys_role AS sr ON su.role = sr.role " +
            "LEFT JOIN sys_role_menu as srm ON sr.id = srm.role_id " +
            "where su.id = #{id}")
    List<Integer> selectMenuByUserId(@Param("id") Integer id);

}
