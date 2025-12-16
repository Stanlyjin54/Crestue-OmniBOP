package com.kuafu.web.controller;

import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.kuafu.common.resource.service.IStaticResourceService;
import com.kuafu.common.event.EventVo;
import com.kuafu.common.event.MyEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.transaction.support.TransactionTemplate;
import com.kuafu.common.sensitive.SensitiveFilter;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.web.entity.ProductStatusEnum;
import com.kuafu.web.service.IProductStatusEnumService;
import com.kuafu.web.vo.ProductStatusEnumPageVO;
import com.kuafu.web.vo.ProductStatusEnumVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 商品状态枚举 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("ProductStatusEnumControllerService")
@RequiredArgsConstructor
public class ProductStatusEnumControllerService  {

    private final IProductStatusEnumService productStatusEnumService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( ProductStatusEnumPageVO pageVO){
        IPage<ProductStatusEnum> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<ProductStatusEnum> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<ProductStatusEnum> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(ProductStatusEnum::getProductStatusEnumId);

            if(pageVO.getProductStatusEnumId() != null){
                queryWrapper.eq(ProductStatusEnum::getProductStatusEnumId, pageVO.getProductStatusEnumId());
            }


        if(StringUtils.isNotEmpty(pageVO.getStatusName())) {
//                        queryWrapper.like(ProductStatusEnum::getStatusName, pageVO.getStatusName());
                        String s_string = pageVO.getStatusName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(ProductStatusEnum::getStatusName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

            return ResultUtils.success(productStatusEnumService.page(page, queryWrapper));
        }


        public BaseResponse list(  ProductStatusEnumVO vo){
            LambdaQueryWrapper<ProductStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getStatusName())) {
                            queryWrapper.like(ProductStatusEnum::getStatusName, vo.getStatusName());
                }
                return ResultUtils.success(productStatusEnumService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  ProductStatusEnumVO vo) {
                ProductStatusEnum entity =  ProductStatusEnum.builder()
                    .statusName(vo.getStatusName())
                .build();








                boolean flag =this.productStatusEnumService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("product_status_enum").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getProductStatusEnumId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( ProductStatusEnumVO vo) {
                ProductStatusEnum entity =  ProductStatusEnum.builder()
                    .productStatusEnumId(vo.getProductStatusEnumId())
                    .statusName(vo.getStatusName())
                .build();



                        boolean flag = this.productStatusEnumService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getProductStatusEnumId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( ProductStatusEnumVO vo ) {
                ProductStatusEnum entity = this.productStatusEnumService.getById(vo.getProductStatusEnumId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( ProductStatusEnumVO vo) {
                boolean flag = this.productStatusEnumService.removeById(vo.getProductStatusEnumId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }
