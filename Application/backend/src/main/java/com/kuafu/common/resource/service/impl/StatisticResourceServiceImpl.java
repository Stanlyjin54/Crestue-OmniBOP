package com.kuafu.common.resource.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.common.entity.StaticResource;
import com.kuafu.common.resource.service.IStaticResourceService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


@Slf4j
@Service
public class StatisticResourceServiceImpl extends ServiceImpl<com.kuafu.common.resource.mapper.StaticResourcesMapper,
        StaticResource> implements IStaticResourceService {

    /**
     * 上传文件
     * @param file 文件对象
     * @return 静态资源对象
     */
    @Override
    public StaticResource uploadFile(MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("上传文件不能为空");
            }
            
            // 获取文件名和扩展名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            // 生成唯一文件名
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            
            // 创建上传目录（按日期分类）
            String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String uploadDir = "uploads/" + dateFolder;
            Path uploadPath = Paths.get(uploadDir);
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 保存文件
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);
            
            // 创建静态资源对象
            StaticResource staticResource = new StaticResource();
            staticResource.setResourceName(originalFilename);
            staticResource.setResourcePath(filePath.toString());
            staticResource.setResourceType(fileExtension);
            
            // 保存到数据库
            this.save(staticResource);
            
            log.info("文件上传成功: {}", filePath);
            return staticResource;
            
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }
}
