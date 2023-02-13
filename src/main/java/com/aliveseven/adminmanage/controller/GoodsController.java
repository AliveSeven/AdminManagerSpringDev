package com.aliveseven.adminmanage.controller;

import com.aliveseven.adminmanage.common.Result;
import com.aliveseven.adminmanage.entity.Goods;
import com.aliveseven.adminmanage.service.IGoodsService;
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
 * @since 2023-02-10
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

        @Resource
        private IGoodsService iGoodsService;

        @Resource
        private IRedisService iRedisService;

        // 新增或者更新
        @PostMapping("/save")
        public Result save(@RequestBody Goods goods) {
                return Result.success(iGoodsService.saveOrUpdate(goods));
        }

        // 根据id，删除数据
        @PostMapping("/delete")
        public Result deleteById(@RequestParam Integer id) {
                return Result.success(iGoodsService.removeById(id));
        }

        // 批量删除
        @PostMapping("/delete/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids) {
                return Result.success(iGoodsService.removeByIds(ids));
        }

        // 使用get方法时，返回查询的role表的全部数据
        @GetMapping
        public Result findAll() {
                return Result.success(iGoodsService.list());
        }

        // 根据id、查询返回数据
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(iGoodsService.getById(id));
        }

        // 分页查询
        @GetMapping("/page")
        public Result findPage(@RequestParam("pageNum") Integer pageNum,
        @RequestParam("pageSize") Integer pageSize,
        @RequestParam(value = "name", required = false) String name) {
                IPage<Goods> page = new Page<>(pageNum , pageSize);
                QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
                queryWrapper.like(!Strings.isEmpty(name),"name" , name);

                return Result.success(iGoodsService.page(page , queryWrapper));
        }


}

