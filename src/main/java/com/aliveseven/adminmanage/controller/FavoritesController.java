package com.aliveseven.adminmanage.controller;

import com.aliveseven.adminmanage.common.Result;
import com.aliveseven.adminmanage.entity.Favorites;
import com.aliveseven.adminmanage.service.IFavoritesService;
import com.aliveseven.adminmanage.service.IRedisService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author AliveSeven
 * @since 2023-02-09
 */
@RestController
@RequestMapping("/favorites")
public class FavoritesController {

        @Resource
        private IFavoritesService iFavoritesService;

        @Resource
        private IRedisService iRedisService;

        // 新增或者更新
        @PostMapping("/save")
        public Result save(@RequestBody Favorites favorites) {
                return Result.success(iFavoritesService.saveOrUpdate(favorites));
        }

        // 根据id，删除数据
        @PostMapping("/delete")
        public Result deleteById(@RequestParam Integer id) {
                return Result.success(iFavoritesService.removeById(id));
        }

        // 批量删除
        @PostMapping("/delete/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids) {
                return Result.success(iFavoritesService.removeByIds(ids));
        }

        // 使用get方法时，返回查询的role表的全部数据
        @GetMapping
        public Result findAll() {
                return Result.success(iFavoritesService.list());
        }

        // 根据id、查询返回数据
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(iFavoritesService.getById(id));
        }

        // 分页查询
        @GetMapping("/page")
        public Result findPage(@RequestParam("pageNum") Integer pageNum,
        @RequestParam("pageSize") Integer pageSize,
        @RequestParam(value = "name", required = false) String name) {
                IPage<Favorites> page = new Page<>(pageNum , pageSize);
                QueryWrapper<Favorites> queryWrapper = new QueryWrapper<>();
                queryWrapper.like(!Strings.isEmpty(name),"name" , name);

                return Result.success(iFavoritesService.page(page , queryWrapper));
        }

        // 传入userId查询收藏夹
        @GetMapping("/getByUserId")
        public Result findAllByUserId(@RequestParam("userId") Integer userId){
                QueryWrapper<Favorites> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", userId);
                return Result.success(iFavoritesService.list(queryWrapper));
        }


}

