package com.aliveseven.adminmanage.common;

public interface Constants {

    String CODE_200 = "200"; //成功
    String CODE_401 = "401";  // 权限不足
    String CODE_400 = "400";  // 参数错误
    String CODE_500 = "500"; // 系统错误
    String CODE_600 = "600"; // 其他业务异常
    String CODE_700 = "700"; // 文件不存在，或者删除失败
    String CODE_701 = "701"; // 文件已存在
    String CODE_702 = "702"; // 文件大小超出限制

    String DICT_TYPE_ICON = "icon";

    String FILES_PAGE_KEY = "FILES_PAGE"; // Redis缓存文件查询
    String ROLE_PAGE_KEY = "ROLE_PAGE"; // Redis缓存角色查询
    String MENU_PAGE_KEY = "MENU_PAGE"; // Redis缓存菜单查询
    String USER_PAGE_KEY = "USER_PAGE"; // Redis缓存用户查询
    String Todolist_PAGE_KEY = "Todolist_PAGE"; // Redis缓存待办事项查询
}
