package com.aliveseven.adminmanage.controller;

import com.aliveseven.adminmanage.common.Result;
import com.aliveseven.adminmanage.entity.Project;
import com.aliveseven.adminmanage.service.IProjectService;
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
 * @since 2023-02-07
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

        @Resource
        private IProjectService iProjectService;

        @Resource
        private IRedisService iRedisService;

        // 新增或者更新
        @PostMapping("/save")
        public Result save(@RequestBody Project project) {
                return Result.success(iProjectService.saveOrUpdate(project));
        }

        // 根据id，删除数据
        @PostMapping("/delete")
        public Result save(@RequestParam Integer id) {
                return Result.success(iProjectService.removeById(id));
        }

        // 批量删除
        @PostMapping("/delete/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids) {
                return Result.success(iProjectService.removeByIds(ids));
        }

        // 使用get方法时，返回查询的role表的全部数据
        @GetMapping
        public Result findAll() {
                return Result.success(iProjectService.list());
        }

        // 根据id、查询返回数据
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(iProjectService.getById(id));
        }

        // 分页查询，要传入userId表示查询当前用户的项目
        @GetMapping("/page")
        public Result page(@RequestParam("pageNum") Integer pageNum,
                           @RequestParam("pageSize") Integer pageSize,
                           @RequestParam("userId") Integer userId,
                           @RequestParam(value = "name", required = false) String name) {
                IPage<Project> page = new Page<>(pageNum , pageSize);
                QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", userId);
                queryWrapper.like(!Strings.isEmpty(name),"name" , name);

                return Result.success(iProjectService.page(page , queryWrapper));
        }

        // 传入userId查询项目
        @GetMapping("/getByUserId")
        public Result findAllByUserId(@RequestParam("userId") Integer userId){
                QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", userId);
                return Result.success(iProjectService.list(queryWrapper));
        }


}

