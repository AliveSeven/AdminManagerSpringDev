package com.aliveseven.adminmanage.mapper;

import com.aliveseven.adminmanage.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AliveSeven
 * @since 2022-10-19
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from sys_role where role = #{role}")
    Integer selectByRole(@Param("role") String role);

}
