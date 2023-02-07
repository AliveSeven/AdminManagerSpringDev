package com.aliveseven.adminmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-02-07
 */
@Getter
@Setter
@TableName("sys_project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String start;

    private String end;

    private Integer progress;

    private String tag;

    private Integer userId;


}
