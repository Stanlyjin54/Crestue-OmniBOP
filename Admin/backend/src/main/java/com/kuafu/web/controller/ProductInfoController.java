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
import com.kuafuweb.web.entity.ProductInfo;
import com.kuafuweb.web.service.IProductInfoService;
import com.kuafuweb.web.vo.ProductInfoPageVO;
import com.kuafuweb.web.vo.ProductInfoVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 商品信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/productInfo")
@Api(tags = {"商品信息"})
public class ProductInfoController  {

    private final IProductInfoService productInfoService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody ProductInfoPageVO pageVO){
        IPage<ProductInfo> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<ProductInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(pageVO.getProductName())) {
            queryWrapper.like(ProductInfo::getProductName, pageVO.getProductName());
        }
            if(StringUtils.isNotEmpty(pageVO.getBarcode())) {
            queryWrapper.eq(ProductInfo::getBarcode, pageVO.getBarcode());
        }
            if(StringUtils.isNotEmpty(pageVO.getCategory())) {
            queryWrapper.eq(ProductInfo::getCategory, pageVO.getCategory());
        }
            if(StringUtils.isNotEmpty(pageVO.getBrand())) {
            queryWrapper.eq(ProductInfo::getBrand, pageVO.getBrand());
        }
            if(StringUtils.isNotEmpty(pageVO.getSpecification())) {
            queryWrapper.eq(ProductInfo::getSpecification, pageVO.getSpecification());
        }
            if(StringUtils.isNotEmpty(pageVO.getUnit())) {
            queryWrapper.eq(ProductInfo::getUnit, pageVO.getUnit());
        }
            if(StringUtils.isNotEmpty(pageVO.getImagesResourceKey())) {
            queryWrapper.eq(ProductInfo::getImagesResourceKey, pageVO.getImagesResourceKey());
        }
            if(StringUtils.isNotEmpty(pageVO.getSerialNumber())) {
            queryWrapper.eq(ProductInfo::getSerialNumber, pageVO.getSerialNumber());
        }
            if(pageVO.getShelfLife() != null){
            queryWrapper.eq(ProductInfo::getShelfLife, pageVO.getShelfLife());
        }
            if(pageVO.getMinStock() != null){
            queryWrapper.eq(ProductInfo::getMinStock, pageVO.getMinStock());
        }
            if(pageVO.getProductStatusEnumProductStatusEnumId1() != null){
            queryWrapper.eq(ProductInfo::getProductStatusEnumProductStatusEnumId1, pageVO.getProductStatusEnumProductStatusEnumId1());
        }
        return ResultUtils.success(productInfoService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody ProductInfoVO vo){
        LambdaQueryWrapper<ProductInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getProductName())) {
            queryWrapper.like(ProductInfo::getProductName, vo.getProductName());
        }
            if(StringUtils.isNotEmpty(vo.getBarcode())) {
            queryWrapper.eq(ProductInfo::getBarcode, vo.getBarcode());
        }
            if(StringUtils.isNotEmpty(vo.getCategory())) {
            queryWrapper.eq(ProductInfo::getCategory, vo.getCategory());
        }
            if(StringUtils.isNotEmpty(vo.getBrand())) {
            queryWrapper.eq(ProductInfo::getBrand, vo.getBrand());
        }
            if(StringUtils.isNotEmpty(vo.getSpecification())) {
            queryWrapper.eq(ProductInfo::getSpecification, vo.getSpecification());
        }
            if(StringUtils.isNotEmpty(vo.getUnit())) {
            queryWrapper.eq(ProductInfo::getUnit, vo.getUnit());
        }
            if(StringUtils.isNotEmpty(vo.getImagesResourceKey())) {
            queryWrapper.eq(ProductInfo::getImagesResourceKey, vo.getImagesResourceKey());
        }
            if(StringUtils.isNotEmpty(vo.getSerialNumber())) {
            queryWrapper.eq(ProductInfo::getSerialNumber, vo.getSerialNumber());
        }
                if(vo.getShelfLife() != null){
            queryWrapper.eq(ProductInfo::getShelfLife, vo.getShelfLife());
        }
                if(vo.getMinStock() != null){
            queryWrapper.eq(ProductInfo::getMinStock, vo.getMinStock());
        }
                if(vo.getProductStatusEnumProductStatusEnumId1() != null){
            queryWrapper.eq(ProductInfo::getProductStatusEnumProductStatusEnumId1, vo.getProductStatusEnumProductStatusEnumId1());
        }
        return ResultUtils.success(productInfoService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<ProductInfo> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(ProductInfo::getProductInfoId);

        List<ProductInfo> list =productInfoService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getProductInfoId(), l.getProductInfoId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody ProductInfoVO vo) {
        ProductInfo entity =  ProductInfo.builder()
                    .productName(vo.getProductName())
                    .barcode(vo.getBarcode())
                    .category(vo.getCategory())
                    .brand(vo.getBrand())
                    .specification(vo.getSpecification())
                    .unit(vo.getUnit())
                  .imagesResourceKey(vo.getImagesResourceKey() == null ? "default_resource_key" : vo.getImagesResourceKey())
                    .serialNumber(vo.getSerialNumber())
                    .shelfLife(vo.getShelfLife())
                    .minStock(vo.getMinStock())
                    .productStatusEnumProductStatusEnumId1(vo.getProductStatusEnumProductStatusEnumId1())
                .build();
        boolean flag =this.productInfoService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("productInfo").data(entity).build());
                }

                final List<StaticResource> ImagesImage = vo.getImages();
                if (ImagesImage  != null && !ImagesImage.isEmpty()) {
                    ImagesImage.forEach(i -> {
                        i.setRelateTableColumnName("images");
                        i.setRelatedTableName("product_info");
                        i.setRelatedTableKey(entity.getProductInfoId());
                        i.setResourcePath(i.getResourceName());
                    });

                    staticResourceService.saveBatch(ImagesImage);

                }

        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody ProductInfoVO vo) {
        ProductInfo entity =  ProductInfo.builder()
                    .productInfoId(vo.getProductInfoId())
                    .productName(vo.getProductName())
                    .barcode(vo.getBarcode())
                    .category(vo.getCategory())
                    .brand(vo.getBrand())
                    .specification(vo.getSpecification())
                    .unit(vo.getUnit())
                    .imagesResourceKey(vo.getImagesResourceKey())
                    .serialNumber(vo.getSerialNumber())
                    .shelfLife(vo.getShelfLife())
                    .minStock(vo.getMinStock())
                    .productStatusEnumProductStatusEnumId1(vo.getProductStatusEnumProductStatusEnumId1())
                .build();
        boolean flag = this.productInfoService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("productInfo").data(entity).build());
                }

                final LambdaQueryWrapper<StaticResource> imagesqueryWrapper = new LambdaQueryWrapper<>();
                imagesqueryWrapper
                        .eq(StaticResource::getRelatedTableName,"product_info")
                        .eq(StaticResource::getRelateTableColumnName,"images")
                        .eq(StaticResource::getRelatedTableKey,entity.getProductInfoId());
                staticResourceService.remove(imagesqueryWrapper);


                final List<StaticResource> ImagesImage = vo.getImages();
                if (ImagesImage  != null && !ImagesImage.isEmpty()) {
                    ImagesImage.forEach(i -> {
                        i.setRelateTableColumnName("images");
                        i.setRelatedTableName("product_info");
                        i.setRelatedTableKey(entity.getProductInfoId());
                        if(StringUtils.isNotEmpty(i.getResourceName())) {
                            i.setResourcePath(i.getResourceName());
                        }
                    });

                    staticResourceService.saveBatch(ImagesImage);

                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer productInfoId) {
        ProductInfo entity = this.productInfoService.getById(productInfoId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer productInfoId) {
        boolean flag = this.productInfoService.removeById(productInfoId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("productInfo").data( ProductInfo.builder().productInfoId(productInfoId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody ProductInfoPageVO pageVO) {

        LambdaQueryWrapper<ProductInfo> queryWrapper = new LambdaQueryWrapper<>();

                    if(StringUtils.isNotEmpty(pageVO.getProductName())) {
                                    queryWrapper.like(ProductInfo::getProductName, pageVO.getProductName());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getBarcode())) {
                            queryWrapper.eq(ProductInfo::getBarcode, pageVO.getBarcode());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getCategory())) {
                            queryWrapper.eq(ProductInfo::getCategory, pageVO.getCategory());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getBrand())) {
                            queryWrapper.eq(ProductInfo::getBrand, pageVO.getBrand());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getSpecification())) {
                            queryWrapper.eq(ProductInfo::getSpecification, pageVO.getSpecification());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getUnit())) {
                            queryWrapper.eq(ProductInfo::getUnit, pageVO.getUnit());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getImagesResourceKey())) {
                            queryWrapper.eq(ProductInfo::getImagesResourceKey, pageVO.getImagesResourceKey());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getSerialNumber())) {
                            queryWrapper.eq(ProductInfo::getSerialNumber, pageVO.getSerialNumber());
                        }
                        if(pageVO.getShelfLife() != null){
                            queryWrapper.eq(ProductInfo::getShelfLife, pageVO.getShelfLife());
                        }
                        if(pageVO.getMinStock() != null){
                            queryWrapper.eq(ProductInfo::getMinStock, pageVO.getMinStock());
                        }
                        if(pageVO.getProductStatusEnumProductStatusEnumId1() != null){
                            queryWrapper.eq(ProductInfo::getProductStatusEnumProductStatusEnumId1, pageVO.getProductStatusEnumProductStatusEnumId1());
                        }
            List<ProductInfo> list = productInfoService.list(queryWrapper);
            ExcelUtil<ProductInfo> util = new ExcelUtil<>(ProductInfo.class);
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
            excelProvider.importData(file, ProductInfo.class, productInfoService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(ProductInfo.class);
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





