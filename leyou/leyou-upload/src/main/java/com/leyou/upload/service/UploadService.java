package com.leyou.upload.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {
    //添加白名单
    private static final List<String> CONTENT_TYPE = Arrays.asList("image/gif", "image/jpeg", "image/png");
    //记录日志
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    public String uploadImage(MultipartFile file) {
        //获取原始文件对象
        String originalFilename = file.getOriginalFilename();
        try {
            //检验文件类型
            String contentType = file.getContentType();
            if (!CONTENT_TYPE.contains(contentType)) {
                LOGGER.info("文件类型不合法:{}", originalFilename);
                return null;
            }
            //校验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                LOGGER.info("文件内容不合法:{}", originalFilename);
                return null;
            }
            //保存到服务器
            file.transferTo(new File("D:\\IdeaProjects\\leyou-manage-web\\image\\" + originalFilename));
            //返回url，进行回显
            return "http://image.leyou.com/" + originalFilename;
        } catch (IOException e) {
            LOGGER.info("服务器内部错误:" + originalFilename);
            e.printStackTrace();
        }
        return null;
    }
}
