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
import com.kuafuweb.web.entity.PurchaseInfo;
import com.kuafuweb.web.service.IPurchaseInfoService;
import com.kuafuweb.web.vo.PurchaseInfoPageVO;
import com.kuafuweb.web.vo.PurchaseInfoVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 采购信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/purchaseInfo")
@Api(tags = {"采购信息"})
public class PurchaseInfoController  {

    private final IPurchaseInfoService purchaseInfoService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody PurchaseInfoPageVO pageVO){
        IPage<PurchaseInfo> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<PurchaseInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(pageVO.getPurchaseNo())) {
            queryWrapper.eq(PurchaseInfo::getPurchaseNo, pageVO.getPurchaseNo());
        }
            if(pageVO.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(PurchaseInfo::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
        }
            if(StringUtils.isNotEmpty(pageVO.getSupplier())) {
            queryWrapper.eq(PurchaseInfo::getSupplier, pageVO.getSupplier());
        }
            if(pageVO.getTotalAmount() != null){
            queryWrapper.eq(PurchaseInfo::getTotalAmount, pageVO.getTotalAmount());
        }
            if(pageVO.getPurchaseDate() != null){
            queryWrapper.eq(PurchaseInfo::getPurchaseDate, pageVO.getPurchaseDate());
        }
            if(pageVO.getPurchaseStatusEnumPurchaseStatusEnumId1() != null){
            queryWrapper.eq(PurchaseInfo::getPurchaseStatusEnumPurchaseStatusEnumId1, pageVO.getPurchaseStatusEnumPurchaseStatusEnumId1());
        }
        return ResultUtils.success(purchaseInfoService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody PurchaseInfoVO vo){
        LambdaQueryWrapper<PurchaseInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getPurchaseNo())) {
            queryWrapper.eq(PurchaseInfo::getPurchaseNo, vo.getPurchaseNo());
        }
                if(vo.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(PurchaseInfo::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
        }
            if(StringUtils.isNotEmpty(vo.getSupplier())) {
            queryWrapper.eq(PurchaseInfo::getSupplier, vo.getSupplier());
        }
                if(vo.getTotalAmount() != null){
            queryWrapper.eq(PurchaseInfo::getTotalAmount, vo.getTotalAmount());
        }
                if(vo.getPurchaseDate() != null){
            queryWrapper.eq(PurchaseInfo::getPurchaseDate, vo.getPurchaseDate());
        }
                if(vo.getPurchaseStatusEnumPurchaseStatusEnumId1() != null){
            queryWrapper.eq(PurchaseInfo::getPurchaseStatusEnumPurchaseStatusEnumId1, vo.getPurchaseStatusEnumPurchaseStatusEnumId1());
        }
        return ResultUtils.success(purchaseInfoService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<PurchaseInfo> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(PurchaseInfo::getPurchaseInfoId);

        List<PurchaseInfo> list =purchaseInfoService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getPurchaseInfoId(), l.getPurchaseInfoId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody PurchaseInfoVO vo) {
        PurchaseInfo entity =  PurchaseInfo.builder()
                    .purchaseNo(vo.getPurchaseNo())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .supplier(vo.getSupplier())
                    .totalAmount(vo.getTotalAmount())
                    .purchaseDate(vo.getPurchaseDate())
                    .purchaseStatusEnumPurchaseStatusEnumId1(vo.getPurchaseStatusEnumPurchaseStatusEnumId1())
                .build();
        boolean flag =this.purchaseInfoService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("purchaseInfo").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody PurchaseInfoVO vo) {
        PurchaseInfo entity =  PurchaseInfo.builder()
                    .purchaseInfoId(vo.getPurchaseInfoId())
                    .purchaseNo(vo.getPurchaseNo())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .supplier(vo.getSupplier())
                    .totalAmount(vo.getTotalAmount())
                    .purchaseDate(vo.getPurchaseDate())
                    .purchaseStatusEnumPurchaseStatusEnumId1(vo.getPurchaseStatusEnumPurchaseStatusEnumId1())
                .build();
        boolean flag = this.purchaseInfoService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("purchaseInfo").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer purchaseInfoId) {
        PurchaseInfo entity = this.purchaseInfoService.getById(purchaseInfoId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer purchaseInfoId) {
        boolean flag = this.purchaseInfoService.removeById(purchaseInfoId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("purchaseInfo").data( PurchaseInfo.builder().purchaseInfoId(purchaseInfoId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody PurchaseInfoPageVO pageVO) {

        LambdaQueryWrapper<PurchaseInfo> queryWrapper = new LambdaQueryWrapper<>();

                    if(StringUtils.isNotEmpty(pageVO.getPurchaseNo())) {
                            queryWrapper.eq(PurchaseInfo::getPurchaseNo, pageVO.getPurchaseNo());
                        }
                        if(pageVO.getUserInfoUserInfoId1() != null){
                            queryWrapper.eq(PurchaseInfo::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getSupplier())) {
                            queryWrapper.eq(PurchaseInfo::getSupplier, pageVO.getSupplier());
                        }
                        if(pageVO.getTotalAmount() != null){
                            queryWrapper.eq(PurchaseInfo::getTotalAmount, pageVO.getTotalAmount());
                        }
                        if(pageVO.getPurchaseDate() != null){
                            queryWrapper.eq(PurchaseInfo::getPurchaseDate, pageVO.getPurchaseDate());
                        }
                        if(pageVO.getPurchaseStatusEnumPurchaseStatusEnumId1() != null){
                            queryWrapper.eq(PurchaseInfo::getPurchaseStatusEnumPurchaseStatusEnumId1, pageVO.getPurchaseStatusEnumPurchaseStatusEnumId1());
                        }
            List<PurchaseInfo> list = purchaseInfoService.list(queryWrapper);
            ExcelUtil<PurchaseInfo> util = new ExcelUtil<>(PurchaseInfo.class);
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
            excelProvider.importData(file, PurchaseInfo.class, purchaseInfoService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(PurchaseInfo.class);
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





