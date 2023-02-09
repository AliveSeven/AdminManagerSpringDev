package com.aliveseven.adminmanage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author AliveSeven
 * @since 2023-02-09
 */
@Getter
@Setter
@TableName("sys_favorites")
public class Favorites implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏夹id
     */
    private Integer id;

    /**
     * 收藏夹名称
     */
    private String name;

    /**
     * 收藏夹封面
     */
    private String cover;

    /**
     * 收藏夹描述
     */
    private String description;

    /**
     * 收藏夹所属用户
     */
    private Integer userId;

    /**
     * 收藏夹类型
     */
    private String type;


}
