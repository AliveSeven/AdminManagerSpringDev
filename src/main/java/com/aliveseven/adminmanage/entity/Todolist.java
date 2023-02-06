package com.aliveseven.adminmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-02-06
 */
@Getter
@Setter
@TableName("sys_todolist")
public class Todolist implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 待办事项id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 待办事项描述
     */
    private String description;

    /**
     * 是否已经完成
     */
    private Boolean done;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 发起人的用户Id
     */
    private Integer userId;

    /**
     * 发起时间
     */
    private String data;


}
