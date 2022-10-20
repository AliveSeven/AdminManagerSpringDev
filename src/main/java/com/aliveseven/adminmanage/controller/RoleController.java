package com.aliveseven.adminmanage.controller;

import com.aliveseven.adminmanage.common.Result;
import com.aliveseven.adminmanage.entity.Role;
import com.aliveseven.adminmanage.entity.User;
import com.aliveseven.adminmanage.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author AliveSeven
 * @since 2022-10-19
 */
@RestController
@RequestMapping("/role")
public class RoleController {

        @Resource
        private IRoleService roleService;

        // 调用/role接口，使用get方法时，返回查询的role表的全部数据
        @GetMapping
        public Result findAll() {
                return Result.success(roleService.list());
        }

        // 根据角色id、查询返回数据
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(roleService.getById(id));
        }

        // 新增或者更新
        @PostMapping
        public Result save(@RequestBody Role role) {
                roleService.saveOrUpdate(role);
                return Result.success();
        }

        // 删除数据
        @PostMapping("/delete")
        public Result delete(@RequestParam Integer id) {
                return Result.success(roleService.removeById(id));
        }

        // 批量删除
        @PostMapping("/delete/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids) {
                return Result.success(roleService.removeByIds(ids));
        }

        // 调用/page接口，参数有PageNum、pageSize，还有其他非必须参数进行模糊查询
        @GetMapping("/page")
        public Result page(@RequestParam("pageNum") Integer pageNum ,
                           @RequestParam("pageSize") Integer pageSize,
                           @RequestParam(value = "name", required = false) String name){
                QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
                queryWrapper.like(!Strings.isEmpty(name),"name" , name);
                queryWrapper.orderByDesc("id");
                return Result.success(roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
        }

        /**
         * 绑定角色和菜单的关系
         * @param roleId 角色id
         * @param menuIds 菜单id数组
         * @return
         */


}

