package com.kuafuweb.web.controller;

import java.util.List;
import java.io.IOException;
import java.util.stream.Collectors;
import com.kuafuweb.common.util.excel.ExcelProvider;
import org.apache.poi.ss.usermodel.Workbook;
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
import org.springframework.web.bind.annotation.RequestPart;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.SelectVO;
import com.kuafuweb.common.util.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;
import com.kuafuweb.common.sensitive.SensitiveFilter;
import com.kuafuweb.common.domin.StaticResource;
import com.kuafuweb.common.resource.service.IStaticResourceService;

import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletResponse;
import com.kuafuweb.common.domin.BaseResponse;
import com.kuafuweb.common.domin.ErrorCode;
import com.kuafuweb.common.domin.ResultUtils;
import com.kuafuweb.common.util.StringUtils;
import com.kuafuweb.web.entity.OrderInfo;
import com.kuafuweb.web.service.IOrderInfoService;
import com.kuafuweb.web.vo.OrderInfoPageVO;
import com.kuafuweb.web.vo.OrderInfoVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 订单信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/orderInfo")
@Api(tags = {"订单信息"})
public class OrderInfoController  {

    private final IOrderInfoService orderInfoService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody OrderInfoPageVO pageVO){
        IPage<OrderInfo> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(pageVO.getOrderNo())) {
            queryWrapper.eq(OrderInfo::getOrderNo, pageVO.getOrderNo());
        }
            if(pageVO.getCustomerInfoCustomerInfoId1() != null){
            queryWrapper.eq(OrderInfo::getCustomerInfoCustomerInfoId1, pageVO.getCustomerInfoCustomerInfoId1());
        }
            if(pageVO.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(OrderInfo::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
        }
            if(pageVO.getOrderStatusEnumOrderStatusEnumId1() != null){
            queryWrapper.eq(OrderInfo::getOrderStatusEnumOrderStatusEnumId1, pageVO.getOrderStatusEnumOrderStatusEnumId1());
        }
            if(pageVO.getTotalAmount() != null){
            queryWrapper.eq(OrderInfo::getTotalAmount, pageVO.getTotalAmount());
        }
            if(pageVO.getProfitAmount() != null){
            queryWrapper.eq(OrderInfo::getProfitAmount, pageVO.getProfitAmount());
        }
            if(pageVO.getCommission() != null){
            queryWrapper.eq(OrderInfo::getCommission, pageVO.getCommission());
        }
            if(pageVO.getShippingFee() != null){
            queryWrapper.eq(OrderInfo::getShippingFee, pageVO.getShippingFee());
        }
            if(pageVO.getOrderDate() != null){
            queryWrapper.eq(OrderInfo::getOrderDate, pageVO.getOrderDate());
        }
            if(StringUtils.isNotEmpty(pageVO.getGeneralOrders())) {
            queryWrapper.eq(OrderInfo::getGeneralOrders, pageVO.getGeneralOrders());
        }
        return ResultUtils.success(orderInfoService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody OrderInfoVO vo){
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getOrderNo())) {
            queryWrapper.eq(OrderInfo::getOrderNo, vo.getOrderNo());
        }
                if(vo.getCustomerInfoCustomerInfoId1() != null){
            queryWrapper.eq(OrderInfo::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
        }
                if(vo.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(OrderInfo::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
        }
                if(vo.getOrderStatusEnumOrderStatusEnumId1() != null){
            queryWrapper.eq(OrderInfo::getOrderStatusEnumOrderStatusEnumId1, vo.getOrderStatusEnumOrderStatusEnumId1());
        }
                if(vo.getTotalAmount() != null){
            queryWrapper.eq(OrderInfo::getTotalAmount, vo.getTotalAmount());
        }
                if(vo.getProfitAmount() != null){
            queryWrapper.eq(OrderInfo::getProfitAmount, vo.getProfitAmount());
        }
                if(vo.getCommission() != null){
            queryWrapper.eq(OrderInfo::getCommission, vo.getCommission());
        }
                if(vo.getShippingFee() != null){
            queryWrapper.eq(OrderInfo::getShippingFee, vo.getShippingFee());
        }
                if(vo.getOrderDate() != null){
            queryWrapper.eq(OrderInfo::getOrderDate, vo.getOrderDate());
        }
            if(StringUtils.isNotEmpty(vo.getGeneralOrders())) {
            queryWrapper.eq(OrderInfo::getGeneralOrders, vo.getGeneralOrders());
        }
        return ResultUtils.success(orderInfoService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(OrderInfo::getOrderInfoId);

        List<OrderInfo> list =orderInfoService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getOrderInfoId(), l.getOrderInfoId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody OrderInfoVO vo) {
        OrderInfo entity =  OrderInfo.builder()
                    .orderNo(vo.getOrderNo())
                    .customerInfoCustomerInfoId1(vo.getCustomerInfoCustomerInfoId1())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .orderStatusEnumOrderStatusEnumId1(vo.getOrderStatusEnumOrderStatusEnumId1())
                    .totalAmount(vo.getTotalAmount())
                    .profitAmount(vo.getProfitAmount())
                    .commission(vo.getCommission())
                    .shippingFee(vo.getShippingFee())
                    .orderDate(vo.getOrderDate())
                    .generalOrders(vo.getGeneralOrders())
                .build();
        boolean flag =this.orderInfoService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("orderInfo").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody OrderInfoVO vo) {
        OrderInfo entity =  OrderInfo.builder()
                    .orderInfoId(vo.getOrderInfoId())
                    .orderNo(vo.getOrderNo())
                    .customerInfoCustomerInfoId1(vo.getCustomerInfoCustomerInfoId1())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .orderStatusEnumOrderStatusEnumId1(vo.getOrderStatusEnumOrderStatusEnumId1())
                    .totalAmount(vo.getTotalAmount())
                    .profitAmount(vo.getProfitAmount())
                    .commission(vo.getCommission())
                    .shippingFee(vo.getShippingFee())
                    .orderDate(vo.getOrderDate())
                    .generalOrders(vo.getGeneralOrders())
                .build();
        boolean flag = this.orderInfoService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("orderInfo").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer orderInfoId) {
        OrderInfo entity = this.orderInfoService.getById(orderInfoId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer orderInfoId) {
        boolean flag = this.orderInfoService.removeById(orderInfoId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("orderInfo").data( OrderInfo.builder().orderInfoId(orderInfoId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody OrderInfoPageVO pageVO) {

        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();

                    if(StringUtils.isNotEmpty(pageVO.getOrderNo())) {
                            queryWrapper.eq(OrderInfo::getOrderNo, pageVO.getOrderNo());
                        }
                        if(pageVO.getCustomerInfoCustomerInfoId1() != null){
                            queryWrapper.eq(OrderInfo::getCustomerInfoCustomerInfoId1, pageVO.getCustomerInfoCustomerInfoId1());
                        }
                        if(pageVO.getUserInfoUserInfoId1() != null){
                            queryWrapper.eq(OrderInfo::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
                        }
                        if(pageVO.getOrderStatusEnumOrderStatusEnumId1() != null){
                            queryWrapper.eq(OrderInfo::getOrderStatusEnumOrderStatusEnumId1, pageVO.getOrderStatusEnumOrderStatusEnumId1());
                        }
                        if(pageVO.getTotalAmount() != null){
                            queryWrapper.eq(OrderInfo::getTotalAmount, pageVO.getTotalAmount());
                        }
                        if(pageVO.getProfitAmount() != null){
                            queryWrapper.eq(OrderInfo::getProfitAmount, pageVO.getProfitAmount());
                        }
                        if(pageVO.getCommission() != null){
                            queryWrapper.eq(OrderInfo::getCommission, pageVO.getCommission());
                        }
                        if(pageVO.getShippingFee() != null){
                            queryWrapper.eq(OrderInfo::getShippingFee, pageVO.getShippingFee());
                        }
                        if(pageVO.getOrderDate() != null){
                            queryWrapper.eq(OrderInfo::getOrderDate, pageVO.getOrderDate());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getGeneralOrders())) {
                            queryWrapper.eq(OrderInfo::getGeneralOrders, pageVO.getGeneralOrders());
                        }
            List<OrderInfo> list = orderInfoService.list(queryWrapper);
            ExcelUtil<OrderInfo> util = new ExcelUtil<>(OrderInfo.class);
            util.exportExcel(response, list, "数据");
        }

    /**
     * 导入excel
     * @param file
     * @return
     */
    @PostMapping("/import")
    public BaseResponse importExcel(@RequestPart(name = "file") MultipartFile file)
    {
            excelProvider.importData(file, OrderInfo.class, orderInfoService::saveBatch);
            return ResultUtils.success("导入成功");
    }

    /**
     * 下载 excel 模版
     * @param response
     * @throws IOException
     */
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
                    try {
                        Workbook workbook = excelProvider.downloadExcelTemplate(OrderInfo.class);
                        // 返回文件流
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.setCharacterEncoding("utf-8");
                        workbook.write(response.getOutputStream());
                        workbook.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
    }


            }





