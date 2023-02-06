package com.aliveseven.adminmanage.controller;

import com.aliveseven.adminmanage.common.Result;
import com.aliveseven.adminmanage.entity.Todolist;
import com.aliveseven.adminmanage.service.ITodolistService;
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
 * @since 2023-02-06
 */
@RestController
@RequestMapping("/todolist")
public class TodolistController {

        @Resource
        private ITodolistService itodolistService;

        @PostMapping("/save")
        public Result save(@RequestBody Todolist todolist) {
                return Result.success(itodolistService.saveOrUpdate(todolist));
        }

        @PostMapping("/delete")
        public Result save(@RequestParam Integer id) {
                return Result.success(itodolistService.removeById(id));
        }

        // 批量删除
        @PostMapping("/delete/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids) {
                return Result.success(itodolistService.removeByIds(ids));
        }

        @GetMapping
        public Result findAll() {
                return Result.success(itodolistService.list());
        }

        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(itodolistService.list());
        }

        @GetMapping("/page")
        public Result findPage(@RequestParam("pageNum") Integer pageNum,
        @RequestParam("pageSize") Integer pageSize,
        @RequestParam(value = "name", required = false) String name) {
                IPage<Todolist> page = new Page<>(pageNum , pageSize);
                QueryWrapper<Todolist> queryWrapper = new QueryWrapper<>();
                queryWrapper.like(!Strings.isEmpty(name),"name" , name);

                return Result.success(itodolistService.page(page , queryWrapper));
        }


}

