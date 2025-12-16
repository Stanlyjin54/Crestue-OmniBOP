package com.kuafuweb.web.service.impl;

import java.util.List;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.kuafuweb.web.mapper.PurchaseInfoMapper;
import com.kuafuweb.web.entity.PurchaseInfo;
import com.kuafuweb.web.service.IPurchaseInfoService;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 采购信息 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("PurchaseInfo")
public class PurchaseInfoServiceImpl extends ServiceImpl<PurchaseInfoMapper, PurchaseInfo> implements IPurchaseInfoService {


}





