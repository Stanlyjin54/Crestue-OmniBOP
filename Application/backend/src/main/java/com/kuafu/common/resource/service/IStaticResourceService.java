package com.kuafu.common.resource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuafu.common.entity.StaticResource;
import org.springframework.web.multipart.MultipartFile;

public interface IStaticResourceService extends IService<StaticResource> {
    /**
     * 上传文件
     * @param file 文件对象
     * @return 静态资源对象
     */
    StaticResource uploadFile(MultipartFile file);
}
