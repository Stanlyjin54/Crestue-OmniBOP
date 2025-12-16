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
import com.kuafu.web.entity.CustomerTagEnum;
import com.kuafu.web.service.ICustomerTagEnumService;
import com.kuafu.web.vo.CustomerTagEnumPageVO;
import com.kuafu.web.vo.CustomerTagEnumVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 客户标签枚举 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("CustomerTagEnumControllerService")
@RequiredArgsConstructor
public class CustomerTagEnumControllerService  {

    private final ICustomerTagEnumService customerTagEnumService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( CustomerTagEnumPageVO pageVO){
        IPage<CustomerTagEnum> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<CustomerTagEnum> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<CustomerTagEnum> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(CustomerTagEnum::getCustomerTagEnumId);

            if(pageVO.getCustomerTagEnumId() != null){
                queryWrapper.eq(CustomerTagEnum::getCustomerTagEnumId, pageVO.getCustomerTagEnumId());
            }


        if(StringUtils.isNotEmpty(pageVO.getTagName())) {
//                        queryWrapper.like(CustomerTagEnum::getTagName, pageVO.getTagName());
                        String s_string = pageVO.getTagName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(CustomerTagEnum::getTagName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }


            if(pageVO.getIsAiGenerated() != null){
                queryWrapper.eq(CustomerTagEnum::getIsAiGenerated, pageVO.getIsAiGenerated());
            }

            return ResultUtils.success(customerTagEnumService.page(page, queryWrapper));
        }


        public BaseResponse list(  CustomerTagEnumVO vo){
            LambdaQueryWrapper<CustomerTagEnum> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getTagName())) {
                            queryWrapper.like(CustomerTagEnum::getTagName, vo.getTagName());
                }
                if(vo.getIsAiGenerated() != null){
                    queryWrapper.eq(CustomerTagEnum::getIsAiGenerated, vo.getIsAiGenerated());
                }
                return ResultUtils.success(customerTagEnumService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  CustomerTagEnumVO vo) {
                CustomerTagEnum entity =  CustomerTagEnum.builder()
                    .tagName(vo.getTagName())
                    .isAiGenerated(vo.getIsAiGenerated())
                .build();








                boolean flag =this.customerTagEnumService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("customer_tag_enum").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getCustomerTagEnumId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( CustomerTagEnumVO vo) {
                CustomerTagEnum entity =  CustomerTagEnum.builder()
                    .customerTagEnumId(vo.getCustomerTagEnumId())
                    .tagName(vo.getTagName())
                    .isAiGenerated(vo.getIsAiGenerated())
                .build();



                        boolean flag = this.customerTagEnumService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getCustomerTagEnumId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( CustomerTagEnumVO vo ) {
                CustomerTagEnum entity = this.customerTagEnumService.getById(vo.getCustomerTagEnumId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( CustomerTagEnumVO vo) {
                boolean flag = this.customerTagEnumService.removeById(vo.getCustomerTagEnumId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }
