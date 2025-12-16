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
import com.kuafuweb.web.entity.InventoryRecord;
import com.kuafuweb.web.service.IInventoryRecordService;
import com.kuafuweb.web.vo.InventoryRecordPageVO;
import com.kuafuweb.web.vo.InventoryRecordVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 库存记录 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/inventoryRecord")
@Api(tags = {"库存记录"})
public class InventoryRecordController  {

    private final IInventoryRecordService inventoryRecordService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody InventoryRecordPageVO pageVO){
        IPage<InventoryRecord> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<InventoryRecord> queryWrapper = new LambdaQueryWrapper<>();

            if(pageVO.getProductInfoProductInfoId1() != null){
            queryWrapper.eq(InventoryRecord::getProductInfoProductInfoId1, pageVO.getProductInfoProductInfoId1());
        }
            if(pageVO.getWarehouseInfoWarehouseInfoId1() != null){
            queryWrapper.eq(InventoryRecord::getWarehouseInfoWarehouseInfoId1, pageVO.getWarehouseInfoWarehouseInfoId1());
        }
            if(pageVO.getQuantity() != null){
            queryWrapper.eq(InventoryRecord::getQuantity, pageVO.getQuantity());
        }
            if(StringUtils.isNotEmpty(pageVO.getBatchNumber())) {
            queryWrapper.eq(InventoryRecord::getBatchNumber, pageVO.getBatchNumber());
        }
            if(pageVO.getExpireDate() != null){
            queryWrapper.eq(InventoryRecord::getExpireDate, pageVO.getExpireDate());
        }
            if(StringUtils.isNotEmpty(pageVO.getSerialNumber())) {
            queryWrapper.eq(InventoryRecord::getSerialNumber, pageVO.getSerialNumber());
        }
        return ResultUtils.success(inventoryRecordService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody InventoryRecordVO vo){
        LambdaQueryWrapper<InventoryRecord> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getProductInfoProductInfoId1() != null){
            queryWrapper.eq(InventoryRecord::getProductInfoProductInfoId1, vo.getProductInfoProductInfoId1());
        }
                if(vo.getWarehouseInfoWarehouseInfoId1() != null){
            queryWrapper.eq(InventoryRecord::getWarehouseInfoWarehouseInfoId1, vo.getWarehouseInfoWarehouseInfoId1());
        }
                if(vo.getQuantity() != null){
            queryWrapper.eq(InventoryRecord::getQuantity, vo.getQuantity());
        }
            if(StringUtils.isNotEmpty(vo.getBatchNumber())) {
            queryWrapper.eq(InventoryRecord::getBatchNumber, vo.getBatchNumber());
        }
                if(vo.getExpireDate() != null){
            queryWrapper.eq(InventoryRecord::getExpireDate, vo.getExpireDate());
        }
            if(StringUtils.isNotEmpty(vo.getSerialNumber())) {
            queryWrapper.eq(InventoryRecord::getSerialNumber, vo.getSerialNumber());
        }
        return ResultUtils.success(inventoryRecordService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<InventoryRecord> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(InventoryRecord::getInventoryRecordId);

        List<InventoryRecord> list =inventoryRecordService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getInventoryRecordId(), l.getInventoryRecordId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody InventoryRecordVO vo) {
        InventoryRecord entity =  InventoryRecord.builder()
                    .productInfoProductInfoId1(vo.getProductInfoProductInfoId1())
                    .warehouseInfoWarehouseInfoId1(vo.getWarehouseInfoWarehouseInfoId1())
                    .quantity(vo.getQuantity())
                    .batchNumber(vo.getBatchNumber())
                    .expireDate(vo.getExpireDate())
                    .serialNumber(vo.getSerialNumber())
                .build();
        boolean flag =this.inventoryRecordService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("inventoryRecord").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody InventoryRecordVO vo) {
        InventoryRecord entity =  InventoryRecord.builder()
                    .inventoryRecordId(vo.getInventoryRecordId())
                    .productInfoProductInfoId1(vo.getProductInfoProductInfoId1())
                    .warehouseInfoWarehouseInfoId1(vo.getWarehouseInfoWarehouseInfoId1())
                    .quantity(vo.getQuantity())
                    .batchNumber(vo.getBatchNumber())
                    .expireDate(vo.getExpireDate())
                    .serialNumber(vo.getSerialNumber())
                .build();
        boolean flag = this.inventoryRecordService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("inventoryRecord").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer inventoryRecordId) {
        InventoryRecord entity = this.inventoryRecordService.getById(inventoryRecordId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer inventoryRecordId) {
        boolean flag = this.inventoryRecordService.removeById(inventoryRecordId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("inventoryRecord").data( InventoryRecord.builder().inventoryRecordId(inventoryRecordId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody InventoryRecordPageVO pageVO) {

        LambdaQueryWrapper<InventoryRecord> queryWrapper = new LambdaQueryWrapper<>();

                        if(pageVO.getProductInfoProductInfoId1() != null){
                            queryWrapper.eq(InventoryRecord::getProductInfoProductInfoId1, pageVO.getProductInfoProductInfoId1());
                        }
                        if(pageVO.getWarehouseInfoWarehouseInfoId1() != null){
                            queryWrapper.eq(InventoryRecord::getWarehouseInfoWarehouseInfoId1, pageVO.getWarehouseInfoWarehouseInfoId1());
                        }
                        if(pageVO.getQuantity() != null){
                            queryWrapper.eq(InventoryRecord::getQuantity, pageVO.getQuantity());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getBatchNumber())) {
                            queryWrapper.eq(InventoryRecord::getBatchNumber, pageVO.getBatchNumber());
                        }
                        if(pageVO.getExpireDate() != null){
                            queryWrapper.eq(InventoryRecord::getExpireDate, pageVO.getExpireDate());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getSerialNumber())) {
                            queryWrapper.eq(InventoryRecord::getSerialNumber, pageVO.getSerialNumber());
                        }
            List<InventoryRecord> list = inventoryRecordService.list(queryWrapper);
            ExcelUtil<InventoryRecord> util = new ExcelUtil<>(InventoryRecord.class);
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
            excelProvider.importData(file, InventoryRecord.class, inventoryRecordService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(InventoryRecord.class);
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





