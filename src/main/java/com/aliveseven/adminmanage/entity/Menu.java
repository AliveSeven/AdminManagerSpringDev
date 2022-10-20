package com.aliveseven.adminmanage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author AliveSeven
 * @since 2022-10-19
 */
@Getter
@Setter
@TableName("sys_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单id
     */
    private Integer id;

    private String name;

    private String path;

    private String icon;

    private String description;

    private String isHide;


}
