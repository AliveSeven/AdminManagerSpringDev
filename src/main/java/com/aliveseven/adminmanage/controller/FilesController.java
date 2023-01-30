package com.aliveseven.adminmanage.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliveseven.adminmanage.common.Constants;
import com.aliveseven.adminmanage.common.Result;
import com.aliveseven.adminmanage.entity.Files;
import com.aliveseven.adminmanage.entity.User;
import com.aliveseven.adminmanage.mapper.FilesMapper;
import com.aliveseven.adminmanage.service.IRedisService;
import com.aliveseven.adminmanage.service.impl.FilesServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传接口
 */
@RestController
@RequestMapping("/file")
public class FilesController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Value("${server.ip}")
    private String serverIp;

    @Resource
    private FilesMapper filesMapper;

    @Resource
    private FilesServiceImpl fileService;

    @Resource
    private IRedisService iRedisService;

    /**
     * 文件上传接口
     * @param file 前端传递过来的文件
     * @return
     * @throws IOException
     */

    @PostMapping("/upload")
    public Result upload(@RequestParam MultipartFile file) throws IOException{
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // 定义一个文件的唯一标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;
        // 上传的文件是   上传文件路径+文件名
        File uploadFile = new File(fileUploadPath + originalFilename);
//        File uploadFile = new File(fileUploadPath + fileUUID);

        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }

        String url;
        // 获取文件的md5
        String md5 = SecureUtil.md5(file.getInputStream());
        // 从数据库查询是否存在相同的记录
        Files dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
            uploadFile.delete();
            Map<String , Object> res = new HashMap<>();
            res.put("url" , url);
            return Result.error(Constants.CODE_701 , "文件已经存在了" , res);
        }else if(size/1024 > 102400){
            return Result.error(Constants.CODE_702 , "文件大小超出限制" , null);
        } else {
            // 上传文件到磁盘
            file.transferTo(uploadFile);
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://" + serverIp + ":8000/file/" + fileUUID;
        }

        // 存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024); // 单位 kb
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        filesMapper.insert(saveFile);


        return Result.success(url);
    }


    /**
     * 删除指定文件夹或文件
     * @param id 文件的id
     * @return Boolean 删除成功返回true，删除失败返回false。
     */
    @PostMapping("/delete/{id}")
    public Result DeleteFileById(@PathVariable Integer id){
        boolean flag = false;
        // 根据id获取文件
        Files files = filesMapper.selectById(id);
        if(files == null){
            return Result.error(Constants.CODE_700 , "文件id不存在");
        } else {
            // 根据文件获取文件名
            String fileName = files.getName();
            File file = new File(fileUploadPath + fileName);
            if(file.isFile() && file.exists()){
                flag = file.delete();
            }
            if(flag){
                fileService.removeById(id);
                files.setIsDelete(true);
                // 返回flag
                return Result.success(true , "删除文件成功");
            } else {
                return Result.error(Constants.CODE_700 , "文件不存在");
            }
        }
    }


    /**
     * 文件下载接口   http://localhost:8000/file/{fileUUID}
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    /**
     * 通过文件的md5查询文件
     * @param md5
     * @return
     */
    private Files getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = filesMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    /**
     * 通过文件id查找文件
     * @param id
     */
    @GetMapping("/detail/{id}")
    private Result getById(@PathVariable("id") Integer id){
        return Result.success(filesMapper.selectById(id));
    }

    // 调用/page接口，参数有PageNum、pageSize，还有其他非必须参数进行模糊查询
    @GetMapping("/page")
    public Result page(@RequestParam("pageNum") Integer pageNum ,
                       @RequestParam("pageSize") Integer pageSize,
                       @RequestParam(value = "name", required = false) String name){
        IPage<Files> page = new Page<>(pageNum , pageSize);
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!Strings.isEmpty(name),"name" , name);

        if(name == ""){
            // 查询缓存
            String filesKey = Constants.FILES_PAGE_KEY + "_" + String.valueOf(pageNum);
            String res = iRedisService.getString(filesKey);
            // 如果缓存存在，拿出来
            if(!StrUtil.isBlank(res)){
                // 把String类型转成JSON类型再返回
                JSONObject data = JSONUtil.parseObj(res);
                Integer nowSize = (Integer) data.get("size");
                if(nowSize != pageSize){
                    // 页码发生变化的时候，清除缓存重新设置
                    iRedisService.flushRedis(filesKey);
                    // 重新查询数据库
                    IPage<Files> filesIPage = fileService.page(page, queryWrapper);
                    // 设置缓存
                    iRedisService.setString(filesKey , JSONUtil.toJsonStr(filesIPage));
                    return Result.success(filesIPage);
                }
                return Result.success(data);
            } else {
                // 如果缓存不存在，查询数据库
                IPage<Files> filesIPage = fileService.page(page, queryWrapper);
                // 设置缓存
                iRedisService.setString(filesKey , JSONUtil.toJsonStr(filesIPage));
                return Result.success(filesIPage);
            }
        }

        return Result.success(fileService.page(page, queryWrapper));

    }



}