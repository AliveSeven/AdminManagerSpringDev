package com.aliveseven.adminmanage.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.aliveseven.adminmanage.common.Constants;
import com.aliveseven.adminmanage.common.Result;
import com.aliveseven.adminmanage.dto.UserDto;
import com.aliveseven.adminmanage.entity.User;
import com.aliveseven.adminmanage.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author AliveSeven
 * @since 2022-10-13
 */
@RestController
@RequestMapping("/user")
public class UserController {

        @Resource
        private IUserService userService;

        // 调用/user接口，使用get方法时，返回查询的User全部数据
        @GetMapping
        public Result findAll() {
                return Result.success(userService.list());
        }

        // 根据用户id、查询返回数据
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(userService.getById(id));
        }

        // 调用/user/save接口，使用post方法时，返回新增或者更新User数据
        @PostMapping("/save")
        public Result save(@RequestBody User user) {
            // 新增或者更新用户数据
            return Result.success(userService.saveOrUpdate(user));
        }

        // 删除数据
        @PostMapping("/delete")
        public Result delete(@RequestParam Integer id) {
                return Result.success(userService.removeById(id));
        }

        // 批量删除
        @PostMapping("/delete/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids) {
                return Result.success(userService.removeByIds(ids));
        }

        // 调用/page接口，参数有PageNum、pageSize，还有其他非必须参数进行模糊查询
        @GetMapping("/page")
        public Result page(@RequestParam("pageNum") Integer pageNum ,
                                @RequestParam("pageSize") Integer pageSize,
                                @RequestParam(value = "username", required = false) String username,
                                @RequestParam(value = "email", required = false) String email,
                                @RequestParam(value = "phone", required = false) String phone){
                IPage<User> page = new Page<>(pageNum , pageSize);
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.like(!Strings.isEmpty(username),"username" , username);
                queryWrapper.like(!Strings.isEmpty(email),"email" , email);
                queryWrapper.like(!Strings.isEmpty(phone),"phone", phone);

                return Result.success(userService.page(page , queryWrapper));
        }

        /**
         * 导出接口
         */
        @GetMapping("/export")
        public void export(HttpServletResponse response) throws Exception {
                // 从数据库查询出所有的数据
                List<User> list = userService.list();
                // 通过工具类创建writer 写出到磁盘路径
                // ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
                // 在内存操作，写出到浏览器
                ExcelWriter writer = ExcelUtil.getWriter(true);
                //自定义标题别名
                writer.addHeaderAlias("username", "用户名");
                writer.addHeaderAlias("password", "密码");
                writer.addHeaderAlias("nickname", "昵称");
                writer.addHeaderAlias("email", "邮箱");
                writer.addHeaderAlias("phone", "电话");
                writer.addHeaderAlias("address", "地址");
                writer.addHeaderAlias("createTime", "创建时间");
                writer.addHeaderAlias("avatarUrl", "头像");

                // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
                writer.write(list, true);

                // 设置浏览器响应的格式
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
                String fileName = URLEncoder.encode("用户信息", "UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
                ServletOutputStream out = response.getOutputStream();
                writer.flush(out, true);
                out.close();
                writer.close();

        }

        /**
         * excel 导入
         * @param file
         * @throws Exception
         */
        @PostMapping("/import")
        public Result imp(MultipartFile file) throws Exception {
                InputStream inputStream = file.getInputStream();
                ExcelReader excelReader = ExcelUtil.getReader(inputStream);
                // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来

                excelReader.addHeaderAlias("用户名", "username");
                excelReader.addHeaderAlias("密码", "password");
                excelReader.addHeaderAlias("昵称", "nickname");
                excelReader.addHeaderAlias("邮箱", "email");
                excelReader.addHeaderAlias("电话", "phone");
                excelReader.addHeaderAlias("地址", "address");
                excelReader.addHeaderAlias("头像", "avatarUrl");

                List<User> users = excelReader.readAll(User.class);

                excelReader.close();

                // 保存到表中
                userService.saveBatch(users);
                return Result.success(true);
        }


        /**
         * 登录接口
         */
        @PostMapping("/login")
        public Result login(@RequestBody UserDto userDto){
                String username = userDto.getUsername();
                String password = userDto.getPassword();
                if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
                        return Result.error(Constants.CODE_400, "参数错误");
                }
                UserDto dto = userService.login(userDto);
                return Result.success(dto);
        }

        /**
         * 验证用户的token是否过期了
         */
        @PostMapping("/token")
        public Result getTokenExpiration(@RequestParam String token){
                return Result.success(userService.getTokenExpiration(token));
        }

        /**
         * 注册接口
         */
        @PostMapping("/register")
        public Result register(@RequestBody UserDto userDto) {
                String username = userDto.getUsername();
                String password = userDto.getPassword();
                if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
                        return Result.error(Constants.CODE_400, "参数错误");
                }
                return Result.success(userService.register(userDto));
        }

        /**
         * 传入用户id查询用户的菜单信息
         */
        @GetMapping("/roleMenu/{id}")
        public Result getRoleMenu(@PathVariable Integer id){
               return Result.success(userService.selectMenuByUserId(id));
        }
}

