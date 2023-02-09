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
 * @since 2023-02-09
 */
@Getter
@Setter
@TableName("sys_favorites_video")
public class FavoritesVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 视频id
     */
    private Integer id;

    /**
     * 所属收藏夹id
     */
    private Integer pid;

    /**
     * 视频封面
     */
    private String cover;

    /**
     * 视频播放链接
     */
    private String url;

    /**
     * 收藏时间
     */
    private String data;

    /**
     * 视频描述
     */
    private String description;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频的发布者
     */
    private String author;

    /**
     * 视频收藏数
     */
    private Integer collectionNum;

    /**
     * 视频播放量
     */
    private Integer playNum;


}
