package com.kuafuweb.common.storage.impl;

import com.kuafuweb.common.config.AppConfig;
import com.kuafuweb.common.file.FileUploadUtils;
import com.kuafuweb.common.storage.StorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service("LocalStorageServiceImpl")
@ConditionalOnProperty(name = "storage.type", havingValue = "local")
public class LocalStorageServiceImpl implements StorageService {
    @Override
    public String upload(MultipartFile file) {
        try {

            String filePath = AppConfig.getUploadPath();
            return FileUploadUtils.upload(filePath, file);
            
        } catch (IOException e) {
            throw new RuntimeException("文件上传报错");
        }
    }
}





