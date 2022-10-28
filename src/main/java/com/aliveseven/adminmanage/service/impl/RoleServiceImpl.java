package com.aliveseven.adminmanage.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aliveseven.adminmanage.entity.Menu;
import com.aliveseven.adminmanage.entity.Role;
import com.aliveseven.adminmanage.entity.RoleMenu;
import com.aliveseven.adminmanage.mapper.RoleMapper;
import com.aliveseven.adminmanage.mapper.RoleMenuMapper;
import com.aliveseven.adminmanage.service.IMenuService;
import com.aliveseven.adminmanage.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author AliveSeven
 * @since 2022-10-19
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;


    @Transactional  // 使用了 JDBC 的事务来进行事务控制的
    @Override
    public Boolean setRoleMenu(Integer roleId, List<Integer> menuIds) {
        // 先删除当前角色id所有的绑定关系
        roleMenuMapper.deleteByRoleId(roleId);
        // 再把前端传过来的菜单id数组绑定到当前的这个角色id上去
//        List<Integer> menuIdsCopy = CollUtil.newArrayList(menuIds);
        for (Integer menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
        return true;
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}
