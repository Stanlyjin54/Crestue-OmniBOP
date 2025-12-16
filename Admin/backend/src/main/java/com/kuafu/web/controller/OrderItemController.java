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
import com.kuafuweb.web.entity.OrderItem;
import com.kuafuweb.web.service.IOrderItemService;
import com.kuafuweb.web.vo.OrderItemPageVO;
import com.kuafuweb.web.vo.OrderItemVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 订单商品明细 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/orderItem")
@Api(tags = {"订单商品明细"})
public class OrderItemController  {

    private final IOrderItemService orderItemService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody OrderItemPageVO pageVO){
        IPage<OrderItem> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<OrderItem> queryWrapper = new LambdaQueryWrapper<>();

            if(pageVO.getOrderInfoOrderInfoId1() != null){
            queryWrapper.eq(OrderItem::getOrderInfoOrderInfoId1, pageVO.getOrderInfoOrderInfoId1());
        }
            if(pageVO.getProductInfoProductInfoId1() != null){
            queryWrapper.eq(OrderItem::getProductInfoProductInfoId1, pageVO.getProductInfoProductInfoId1());
        }
            if(pageVO.getQuantity() != null){
            queryWrapper.eq(OrderItem::getQuantity, pageVO.getQuantity());
        }
            if(pageVO.getUnitPrice() != null){
            queryWrapper.eq(OrderItem::getUnitPrice, pageVO.getUnitPrice());
        }
            if(pageVO.getTotalPrice() != null){
            queryWrapper.eq(OrderItem::getTotalPrice, pageVO.getTotalPrice());
        }
        return ResultUtils.success(orderItemService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody OrderItemVO vo){
        LambdaQueryWrapper<OrderItem> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getOrderInfoOrderInfoId1() != null){
            queryWrapper.eq(OrderItem::getOrderInfoOrderInfoId1, vo.getOrderInfoOrderInfoId1());
        }
                if(vo.getProductInfoProductInfoId1() != null){
            queryWrapper.eq(OrderItem::getProductInfoProductInfoId1, vo.getProductInfoProductInfoId1());
        }
                if(vo.getQuantity() != null){
            queryWrapper.eq(OrderItem::getQuantity, vo.getQuantity());
        }
                if(vo.getUnitPrice() != null){
            queryWrapper.eq(OrderItem::getUnitPrice, vo.getUnitPrice());
        }
                if(vo.getTotalPrice() != null){
            queryWrapper.eq(OrderItem::getTotalPrice, vo.getTotalPrice());
        }
        return ResultUtils.success(orderItemService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<OrderItem> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(OrderItem::getOrderItemId);

        List<OrderItem> list =orderItemService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getOrderItemId(), l.getOrderItemId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody OrderItemVO vo) {
        OrderItem entity =  OrderItem.builder()
                    .orderInfoOrderInfoId1(vo.getOrderInfoOrderInfoId1())
                    .productInfoProductInfoId1(vo.getProductInfoProductInfoId1())
                    .quantity(vo.getQuantity())
                    .unitPrice(vo.getUnitPrice())
                    .totalPrice(vo.getTotalPrice())
                .build();
        boolean flag =this.orderItemService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("orderItem").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody OrderItemVO vo) {
        OrderItem entity =  OrderItem.builder()
                    .orderItemId(vo.getOrderItemId())
                    .orderInfoOrderInfoId1(vo.getOrderInfoOrderInfoId1())
                    .productInfoProductInfoId1(vo.getProductInfoProductInfoId1())
                    .quantity(vo.getQuantity())
                    .unitPrice(vo.getUnitPrice())
                    .totalPrice(vo.getTotalPrice())
                .build();
        boolean flag = this.orderItemService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("orderItem").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer orderItemId) {
        OrderItem entity = this.orderItemService.getById(orderItemId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer orderItemId) {
        boolean flag = this.orderItemService.removeById(orderItemId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("orderItem").data( OrderItem.builder().orderItemId(orderItemId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody OrderItemPageVO pageVO) {

        LambdaQueryWrapper<OrderItem> queryWrapper = new LambdaQueryWrapper<>();

                        if(pageVO.getOrderInfoOrderInfoId1() != null){
                            queryWrapper.eq(OrderItem::getOrderInfoOrderInfoId1, pageVO.getOrderInfoOrderInfoId1());
                        }
                        if(pageVO.getProductInfoProductInfoId1() != null){
                            queryWrapper.eq(OrderItem::getProductInfoProductInfoId1, pageVO.getProductInfoProductInfoId1());
                        }
                        if(pageVO.getQuantity() != null){
                            queryWrapper.eq(OrderItem::getQuantity, pageVO.getQuantity());
                        }
                        if(pageVO.getUnitPrice() != null){
                            queryWrapper.eq(OrderItem::getUnitPrice, pageVO.getUnitPrice());
                        }
                        if(pageVO.getTotalPrice() != null){
                            queryWrapper.eq(OrderItem::getTotalPrice, pageVO.getTotalPrice());
                        }
            List<OrderItem> list = orderItemService.list(queryWrapper);
            ExcelUtil<OrderItem> util = new ExcelUtil<>(OrderItem.class);
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
            excelProvider.importData(file, OrderItem.class, orderItemService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(OrderItem.class);
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





