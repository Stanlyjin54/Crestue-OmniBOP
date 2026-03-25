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

import com.kuafu.web.mapper.BusinessOpportunityMapper;
import com.kuafu.web.entity.BusinessOpportunity;
import com.kuafu.web.service.IBusinessOpportunityService;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 商机线索 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("BusinessOpportunity")
public class BusinessOpportunityServiceImpl extends ServiceImpl<BusinessOpportunityMapper, BusinessOpportunity> implements IBusinessOpportunityService {


}
