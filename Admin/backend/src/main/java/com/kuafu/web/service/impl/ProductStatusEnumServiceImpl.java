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

import com.kuafuweb.web.mapper.ProductStatusEnumMapper;
import com.kuafuweb.web.entity.ProductStatusEnum;
import com.kuafuweb.web.service.IProductStatusEnumService;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 商品状态枚举 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("ProductStatusEnum")
public class ProductStatusEnumServiceImpl extends ServiceImpl<ProductStatusEnumMapper, ProductStatusEnum> implements IProductStatusEnumService {


}





