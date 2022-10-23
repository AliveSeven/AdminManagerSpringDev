package com.aliveseven.adminmanage.dto;

import com.aliveseven.adminmanage.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * 接受前端登录请求的参数
 */
@Data
public class UserDto {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private String address;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;
}