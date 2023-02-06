package com.aliveseven.adminmanage.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliveseven.adminmanage.common.Constants;
import com.aliveseven.adminmanage.common.Result;
import com.aliveseven.adminmanage.entity.Role;
import com.aliveseven.adminmanage.service.IRedisService;
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
        private IRoleService iRoleService;

        @Resource
        private IRedisService iRedisService;

        // 使用get方法时，返回查询的role表的全部数据
        @GetMapping
        public Result findAll() {
                return Result.success(iRoleService.list());
        }

        // 根据角色id、查询返回数据
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(iRoleService.getById(id));
        }

        // 新增或者更新
        @PostMapping("/save")
        public Result save(@RequestBody Role role) {
                // 新增或者更新角色数据
                // Redis模糊搜索批量删除缓存
                iRedisService.deleteByPre(Constants.ROLE_PAGE_KEY);
                return Result.success(iRoleService.saveOrUpdate(role));
        }

        // 删除数据
        @PostMapping("/delete")
        public Result delete(@RequestParam Integer id) {
                // Redis模糊搜索批量删除缓存
                iRedisService.deleteByPre(Constants.ROLE_PAGE_KEY);
                return Result.success(iRoleService.removeById(id));
        }

        // 批量删除
        @PostMapping("/delete/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids) {
                // Redis模糊搜索批量删除缓存
                iRedisService.deleteByPre(Constants.ROLE_PAGE_KEY);
                return Result.success(iRoleService.removeByIds(ids));
        }

        // 调用/page接口，参数有PageNum、pageSize，还有其他非必须参数进行模糊查询
        @GetMapping("/page")
        public Result page(@RequestParam("pageNum") Integer pageNum ,
                           @RequestParam("pageSize") Integer pageSize,
                           @RequestParam(value = "name", required = false) String name){
                QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
                queryWrapper.like(!Strings.isEmpty(name),"name" , name);
                // queryWrapper.orderByDesc("id");

                if(name == "" || name == null){
                        // 查询缓存
                        String roleKey = Constants.ROLE_PAGE_KEY + "_" + String.valueOf(pageNum);
                        String res = iRedisService.getString(roleKey);
                        // 如果缓存存在，拿出来
                        if(!StrUtil.isBlank(res)){
                                // 把String类型转成JSON类型再返回
                                JSONObject data = JSONUtil.parseObj(res);
                                Integer nowSize = (Integer) data.get("size");
                                if(nowSize != pageSize){
                                        // 页码发生变化的时候，清除缓存重新设置
                                        iRedisService.flushRedis(roleKey);
                                        // 重新查询数据库
                                        IPage<Role> roleIPage = iRoleService.page(new Page<>(pageNum, pageSize), queryWrapper);
                                        // 设置缓存
                                        iRedisService.setString(roleKey , JSONUtil.toJsonStr(roleIPage));
                                        return Result.success(roleIPage);
                                }
                                return Result.success(data);
                        } else {
                                // 如果缓存不存在，查询数据库
                                IPage<Role> roleIPage = iRoleService.page(new Page<>(pageNum, pageSize), queryWrapper);
                                // 设置缓存
                                iRedisService.setString(roleKey , JSONUtil.toJsonStr(roleIPage));
                                return Result.success(roleIPage);
                        }
                }

                return Result.success(iRoleService.page(new Page<>(pageNum, pageSize), queryWrapper));
        }

        /**
         * 绑定角色和菜单的关系
         * @param roleId 角色id
         * @param menuIds 菜单id数组
         * @return
         */
        @PostMapping("/roleMenu/{roleId}")
        public Result setRoleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
                return Result.success(iRoleService.setRoleMenu(roleId, menuIds));
        }
        
        
        @GetMapping("/roleMenu/{roleId}")
        public Result getRoleMenu(@PathVariable Integer roleId){
                return Result.success(iRoleService.getRoleMenu(roleId));
        }


}

