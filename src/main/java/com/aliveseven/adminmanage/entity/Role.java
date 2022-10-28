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
@TableName("sys_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 唯一标识
     */
    private String role;


}
