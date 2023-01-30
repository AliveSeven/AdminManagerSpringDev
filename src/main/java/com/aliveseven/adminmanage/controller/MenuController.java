package com.aliveseven.adminmanage.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliveseven.adminmanage.common.Constants;
import com.aliveseven.adminmanage.common.Result;
import com.aliveseven.adminmanage.entity.Menu;
import com.aliveseven.adminmanage.entity.User;
import com.aliveseven.adminmanage.service.IMenuService;
import com.aliveseven.adminmanage.service.IRedisService;
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
@RequestMapping("/menu")
public class MenuController {

        @Resource
        private IMenuService menuService;

        @Resource
        private IRedisService iRedisService;

        // 调用/menu接口，使用get方法时，返回查询的Menu全部数据
        @GetMapping
        public Result findAll() {
                return Result.success(menuService.list());
        }

        // 根据菜单id、查询返回数据
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(menuService.getById(id));
        }

        // 新增或者更新
        @PostMapping
        public Result save(@RequestBody Menu menu) {
                // Redis模糊搜索批量删除缓存
                iRedisService.deleteByPre(Constants.MENU_PAGE_KEY);
                return Result.success(menuService.saveOrUpdate(menu));
        }

        // 删除数据
        @PostMapping("/delete")
        public Result delete(@RequestParam Integer id) {
                // Redis模糊搜索批量删除缓存
                iRedisService.deleteByPre(Constants.MENU_PAGE_KEY);
                return Result.success(menuService.removeById(id));
        }

        // 批量删除
        @PostMapping("/delete/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids) {
                // Redis模糊搜索批量删除缓存
                iRedisService.deleteByPre(Constants.MENU_PAGE_KEY);
                return Result.success(menuService.removeByIds(ids));
        }

        // 调用/page接口，参数有PageNum、pageSize，还有其他非必须参数进行模糊查询
        @GetMapping("/page")
        public Result page(@RequestParam("pageNum") Integer pageNum ,
                           @RequestParam("pageSize") Integer pageSize,
                           @RequestParam(value = "name", required = false) String name){
                IPage<Menu> page = new Page<>(pageNum , pageSize);
                QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
                queryWrapper.like(!Strings.isEmpty(name),"username" , name);

                if(name == ""){
                        // 查询缓存
                        String menuKey = Constants.MENU_PAGE_KEY + "_" + String.valueOf(pageNum);
                        String res = iRedisService.getString(menuKey);
                        // 如果缓存存在，不为空，拿出来
                        if(!StrUtil.isBlank(res)){
                                // 把String类型转成JSON类型再返回
                                JSONObject data = JSONUtil.parseObj(res);
                                Integer nowSize = (Integer) data.get("size");
                                if(nowSize != pageSize){
                                        // 页码发生变化的时候，清除缓存重新设置
                                        iRedisService.flushRedis(menuKey);
                                        // 重新查询数据库
                                        IPage<Menu> menuIPage = menuService.page(page, queryWrapper);
                                        // 设置缓存
                                        iRedisService.setString(menuKey , JSONUtil.toJsonStr(menuIPage));
                                        return Result.success(menuIPage);
                                }
                                return Result.success(data);
                        } else {
                                // 如果缓存不存在，查询数据库
                                IPage<Menu> menuIPage = menuService.page(page, queryWrapper);
                                // 设置缓存
                                iRedisService.setString(menuKey , JSONUtil.toJsonStr(menuIPage));
                                return Result.success(menuIPage);
                        }
                }

                return Result.success(menuService.page(page , queryWrapper));
        }
}

