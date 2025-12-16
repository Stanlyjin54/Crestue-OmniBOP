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
import com.kuafuweb.web.entity.BusinessCardOcr;
import com.kuafuweb.web.service.IBusinessCardOcrService;
import com.kuafuweb.web.vo.BusinessCardOcrPageVO;
import com.kuafuweb.web.vo.BusinessCardOcrVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 名片OCR建档 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/businessCardOcr")
@Api(tags = {"名片OCR建档"})
public class BusinessCardOcrController  {

    private final IBusinessCardOcrService businessCardOcrService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody BusinessCardOcrPageVO pageVO){
        IPage<BusinessCardOcr> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<BusinessCardOcr> queryWrapper = new LambdaQueryWrapper<>();

            if(pageVO.getCustomerInfoCustomerInfoId1() != null){
            queryWrapper.eq(BusinessCardOcr::getCustomerInfoCustomerInfoId1, pageVO.getCustomerInfoCustomerInfoId1());
        }
            if(StringUtils.isNotEmpty(pageVO.getCardImageResourceKey())) {
            queryWrapper.eq(BusinessCardOcr::getCardImageResourceKey, pageVO.getCardImageResourceKey());
        }
            if(StringUtils.isNotEmpty(pageVO.getOcrResult())) {
            queryWrapper.eq(BusinessCardOcr::getOcrResult, pageVO.getOcrResult());
        }
        return ResultUtils.success(businessCardOcrService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody BusinessCardOcrVO vo){
        LambdaQueryWrapper<BusinessCardOcr> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getCustomerInfoCustomerInfoId1() != null){
            queryWrapper.eq(BusinessCardOcr::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
        }
            if(StringUtils.isNotEmpty(vo.getCardImageResourceKey())) {
            queryWrapper.eq(BusinessCardOcr::getCardImageResourceKey, vo.getCardImageResourceKey());
        }
            if(StringUtils.isNotEmpty(vo.getOcrResult())) {
            queryWrapper.eq(BusinessCardOcr::getOcrResult, vo.getOcrResult());
        }
        return ResultUtils.success(businessCardOcrService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<BusinessCardOcr> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(BusinessCardOcr::getBusinessCardOcrId);

        List<BusinessCardOcr> list =businessCardOcrService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getBusinessCardOcrId(), l.getBusinessCardOcrId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody BusinessCardOcrVO vo) {
        BusinessCardOcr entity =  BusinessCardOcr.builder()
                    .customerInfoCustomerInfoId1(vo.getCustomerInfoCustomerInfoId1())
                  .cardImageResourceKey(vo.getCardImageResourceKey() == null ? "default_resource_key" : vo.getCardImageResourceKey())
                    .ocrResult(vo.getOcrResult())
                .build();
        boolean flag =this.businessCardOcrService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("businessCardOcr").data(entity).build());
                }

                final List<StaticResource> CardImageImage = vo.getCardImage();
                if (CardImageImage  != null && !CardImageImage.isEmpty()) {
                    CardImageImage.forEach(i -> {
                        i.setRelateTableColumnName("card_image");
                        i.setRelatedTableName("business_card_ocr");
                        i.setRelatedTableKey(entity.getBusinessCardOcrId());
                        i.setResourcePath(i.getResourceName());
                    });

                    staticResourceService.saveBatch(CardImageImage);

                }

        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody BusinessCardOcrVO vo) {
        BusinessCardOcr entity =  BusinessCardOcr.builder()
                    .businessCardOcrId(vo.getBusinessCardOcrId())
                    .customerInfoCustomerInfoId1(vo.getCustomerInfoCustomerInfoId1())
                    .cardImageResourceKey(vo.getCardImageResourceKey())
                    .ocrResult(vo.getOcrResult())
                .build();
        boolean flag = this.businessCardOcrService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("businessCardOcr").data(entity).build());
                }

                final LambdaQueryWrapper<StaticResource> card_imagequeryWrapper = new LambdaQueryWrapper<>();
                card_imagequeryWrapper
                        .eq(StaticResource::getRelatedTableName,"business_card_ocr")
                        .eq(StaticResource::getRelateTableColumnName,"card_image")
                        .eq(StaticResource::getRelatedTableKey,entity.getBusinessCardOcrId());
                staticResourceService.remove(card_imagequeryWrapper);


                final List<StaticResource> CardImageImage = vo.getCardImage();
                if (CardImageImage  != null && !CardImageImage.isEmpty()) {
                    CardImageImage.forEach(i -> {
                        i.setRelateTableColumnName("card_image");
                        i.setRelatedTableName("business_card_ocr");
                        i.setRelatedTableKey(entity.getBusinessCardOcrId());
                        if(StringUtils.isNotEmpty(i.getResourceName())) {
                            i.setResourcePath(i.getResourceName());
                        }
                    });

                    staticResourceService.saveBatch(CardImageImage);

                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer businessCardOcrId) {
        BusinessCardOcr entity = this.businessCardOcrService.getById(businessCardOcrId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer businessCardOcrId) {
        boolean flag = this.businessCardOcrService.removeById(businessCardOcrId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("businessCardOcr").data( BusinessCardOcr.builder().businessCardOcrId(businessCardOcrId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody BusinessCardOcrPageVO pageVO) {

        LambdaQueryWrapper<BusinessCardOcr> queryWrapper = new LambdaQueryWrapper<>();

                        if(pageVO.getCustomerInfoCustomerInfoId1() != null){
                            queryWrapper.eq(BusinessCardOcr::getCustomerInfoCustomerInfoId1, pageVO.getCustomerInfoCustomerInfoId1());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getCardImageResourceKey())) {
                            queryWrapper.eq(BusinessCardOcr::getCardImageResourceKey, pageVO.getCardImageResourceKey());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getOcrResult())) {
                            queryWrapper.eq(BusinessCardOcr::getOcrResult, pageVO.getOcrResult());
                        }
            List<BusinessCardOcr> list = businessCardOcrService.list(queryWrapper);
            ExcelUtil<BusinessCardOcr> util = new ExcelUtil<>(BusinessCardOcr.class);
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
            excelProvider.importData(file, BusinessCardOcr.class, businessCardOcrService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(BusinessCardOcr.class);
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





