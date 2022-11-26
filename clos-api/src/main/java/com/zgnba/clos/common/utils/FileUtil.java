package com.zgnba.clos.common.utils;

import cn.hutool.core.date.DateUtil;
import com.zgnba.clos.exception.ClosException;
import com.zgnba.clos.exception.ClosExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
public class FileUtil {

    public static String upload(MultipartFile multipartFile, String filePath, String filename) {
        //判断文件是否为空 isEmpty
        if (multipartFile == null) {
            log.warn("文件上传失败: {}", "文件为空");
            throw new ClosException(ClosExceptionCode.FILE_ERROR);
        }
        //获取文件的原名称 getOriginalFilename
        String OriginalFilename = multipartFile.getOriginalFilename();
        //获取时间和文件的扩展名，拼接成一个全新的文件名； 用时间来命名是为了避免文件名冲突
        String fileName = DateUtil.today() + filename + "." + OriginalFilename.substring(OriginalFilename.lastIndexOf(".") + 1);
        //新建一个目录（文件夹）
        File dest = new File(filePath + fileName);
        //判断filePath目录是否存在，如不存在，就新建一个
        if (!dest.exists()) {
            dest.getParentFile().mkdirs(); //新建一个目录
        }
        try {
            //文件输出
            multipartFile.transferTo(dest);
        } catch (Exception e) {
            e.printStackTrace();
            //拷贝失败要有提示
            log.warn("文件上传失败: {}", "上传失败");
            throw new ClosException(ClosExceptionCode.FILE_ERROR);
        }
        log.info("文件上传成功");
        return filePath + fileName;
    }

    public static void delete(String filePath, String fileNames) {
        String[] name = fileNames.split(",");//多个文件，逗号分割
        for (String fileName : name) {
            File file = new File(filePath + fileName);
            if (file.exists() && file.isFile()) {
                try {
                    file.delete();
                    log.info("文件删除成功: {}", fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                    //删除失败要有提示
                    log.warn("文件删除失败");
                    throw new ClosException(ClosExceptionCode.FILE_ERROR);
                }
            }
            log.info("没有删除文件");
        }
    }
}
