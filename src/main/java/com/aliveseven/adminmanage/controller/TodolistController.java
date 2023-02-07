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
        private ITodolistService iTodolistService;

        // 新增或者更新
        @PostMapping("/save")
        public Result save(@RequestBody Todolist todolist) {
                return Result.success(iTodolistService.saveOrUpdate(todolist));
        }

        // 根据id，删除数据
        @PostMapping("/delete")
        public Result save(@RequestParam Integer id) {
                return Result.success(iTodolistService.removeById(id));
        }

        // 批量删除
        @PostMapping("/delete/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids) {
                return Result.success(iTodolistService.removeByIds(ids));
        }

        // 使用get方法时，返回查询的role表的全部数据
        @GetMapping
        public Result findAll() {
                return Result.success(iTodolistService.list());
        }

        // 根据id、查询返回数据
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(iTodolistService.getById(id));
        }

        // 分页查询，要传入userId表示查询当前用户的代办事项
        @GetMapping("/page")
        public Result page(@RequestParam("pageNum") Integer pageNum,
        @RequestParam("pageSize") Integer pageSize,
        @RequestParam("userId") Integer userId,
        @RequestParam(value = "description", required = false) String description) {
                IPage<Todolist> page = new Page<>(pageNum , pageSize);
                QueryWrapper<Todolist> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", userId);
                queryWrapper.like(!Strings.isEmpty(description),"description" , description);

                return Result.success(iTodolistService.page(page , queryWrapper));
        }

        // 传入userId查询待办事项
        @GetMapping("/getByUserId")
        public Result findAllByUserId(@RequestParam("userId") Integer userId){
                QueryWrapper<Todolist> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", userId);
                return Result.success(iTodolistService.list(queryWrapper));
        }


}

