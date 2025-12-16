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

import com.kuafu.web.mapper.PurchaseStatusEnumMapper;
import com.kuafu.web.entity.PurchaseStatusEnum;
import com.kuafu.web.service.IPurchaseStatusEnumService;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 采购状态枚举 服务实现类 </p>
 *
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("PurchaseStatusEnum")
public class PurchaseStatusEnumServiceImpl extends ServiceImpl<PurchaseStatusEnumMapper, PurchaseStatusEnum> implements IPurchaseStatusEnumService {


}
