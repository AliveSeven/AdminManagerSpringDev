package com.aliveseven.adminmanage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色菜单关系表
 * </p>
 *
 * @author AliveSeven
 * @since 2022-10-22
 */
@Getter
@Setter
@TableName("sys_role_menu")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 菜单id
     */
    private Integer menuId;


}
