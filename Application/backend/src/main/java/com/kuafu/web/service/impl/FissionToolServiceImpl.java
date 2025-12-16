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

import com.kuafu.web.mapper.FissionToolMapper;
import com.kuafu.web.entity.FissionTool;
import com.kuafu.web.service.IFissionToolService;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 裂变工具 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("FissionTool")
public class FissionToolServiceImpl extends ServiceImpl<FissionToolMapper, FissionTool> implements IFissionToolService {


}
