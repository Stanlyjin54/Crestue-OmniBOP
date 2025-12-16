package com.kuafu.web.service.impl;

import java.util.List;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.kuafu.web.mapper.PermissionLogMapper;
import com.kuafu.web.entity.PermissionLog;
import com.kuafu.web.service.IPermissionLogService;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 权限变更日志 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("PermissionLog")
public class PermissionLogServiceImpl extends ServiceImpl<PermissionLogMapper, PermissionLog> implements IPermissionLogService {


}
