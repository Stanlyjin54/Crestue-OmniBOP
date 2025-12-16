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
import com.kuafuweb.web.entity.AdRecommendInfo;
import com.kuafuweb.web.service.IAdRecommendInfoService;
import com.kuafuweb.web.vo.AdRecommendInfoPageVO;
import com.kuafuweb.web.vo.AdRecommendInfoVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 广告推荐 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/adRecommendInfo")
@Api(tags = {"广告推荐"})
public class AdRecommendInfoController  {

    private final IAdRecommendInfoService adRecommendInfoService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody AdRecommendInfoPageVO pageVO){
        IPage<AdRecommendInfo> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<AdRecommendInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(pageVO.getAdTitle())) {
            queryWrapper.eq(AdRecommendInfo::getAdTitle, pageVO.getAdTitle());
        }
            if(StringUtils.isNotEmpty(pageVO.getAdImageResourceKey())) {
            queryWrapper.eq(AdRecommendInfo::getAdImageResourceKey, pageVO.getAdImageResourceKey());
        }
            if(StringUtils.isNotEmpty(pageVO.getAdUrl())) {
            queryWrapper.eq(AdRecommendInfo::getAdUrl, pageVO.getAdUrl());
        }
            if(pageVO.getCpcFee() != null){
            queryWrapper.eq(AdRecommendInfo::getCpcFee, pageVO.getCpcFee());
        }
        return ResultUtils.success(adRecommendInfoService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody AdRecommendInfoVO vo){
        LambdaQueryWrapper<AdRecommendInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getAdTitle())) {
            queryWrapper.eq(AdRecommendInfo::getAdTitle, vo.getAdTitle());
        }
            if(StringUtils.isNotEmpty(vo.getAdImageResourceKey())) {
            queryWrapper.eq(AdRecommendInfo::getAdImageResourceKey, vo.getAdImageResourceKey());
        }
            if(StringUtils.isNotEmpty(vo.getAdUrl())) {
            queryWrapper.eq(AdRecommendInfo::getAdUrl, vo.getAdUrl());
        }
                if(vo.getCpcFee() != null){
            queryWrapper.eq(AdRecommendInfo::getCpcFee, vo.getCpcFee());
        }
        return ResultUtils.success(adRecommendInfoService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<AdRecommendInfo> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(AdRecommendInfo::getAdRecommendInfoId);

        List<AdRecommendInfo> list =adRecommendInfoService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getAdRecommendInfoId(), l.getAdRecommendInfoId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody AdRecommendInfoVO vo) {
        AdRecommendInfo entity =  AdRecommendInfo.builder()
                    .adTitle(vo.getAdTitle())
                  .adImageResourceKey(vo.getAdImageResourceKey() == null ? "default_resource_key" : vo.getAdImageResourceKey())
                    .adUrl(vo.getAdUrl())
                    .cpcFee(vo.getCpcFee())
                .build();
        boolean flag =this.adRecommendInfoService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("adRecommendInfo").data(entity).build());
                }

                final List<StaticResource> AdImageImage = vo.getAdImage();
                if (AdImageImage  != null && !AdImageImage.isEmpty()) {
                    AdImageImage.forEach(i -> {
                        i.setRelateTableColumnName("ad_image");
                        i.setRelatedTableName("ad_recommend_info");
                        i.setRelatedTableKey(entity.getAdRecommendInfoId());
                        i.setResourcePath(i.getResourceName());
                    });

                    staticResourceService.saveBatch(AdImageImage);

                }

        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody AdRecommendInfoVO vo) {
        AdRecommendInfo entity =  AdRecommendInfo.builder()
                    .adRecommendInfoId(vo.getAdRecommendInfoId())
                    .adTitle(vo.getAdTitle())
                    .adImageResourceKey(vo.getAdImageResourceKey())
                    .adUrl(vo.getAdUrl())
                    .cpcFee(vo.getCpcFee())
                .build();
        boolean flag = this.adRecommendInfoService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("adRecommendInfo").data(entity).build());
                }

                final LambdaQueryWrapper<StaticResource> ad_imagequeryWrapper = new LambdaQueryWrapper<>();
                ad_imagequeryWrapper
                        .eq(StaticResource::getRelatedTableName,"ad_recommend_info")
                        .eq(StaticResource::getRelateTableColumnName,"ad_image")
                        .eq(StaticResource::getRelatedTableKey,entity.getAdRecommendInfoId());
                staticResourceService.remove(ad_imagequeryWrapper);


                final List<StaticResource> AdImageImage = vo.getAdImage();
                if (AdImageImage  != null && !AdImageImage.isEmpty()) {
                    AdImageImage.forEach(i -> {
                        i.setRelateTableColumnName("ad_image");
                        i.setRelatedTableName("ad_recommend_info");
                        i.setRelatedTableKey(entity.getAdRecommendInfoId());
                        if(StringUtils.isNotEmpty(i.getResourceName())) {
                            i.setResourcePath(i.getResourceName());
                        }
                    });

                    staticResourceService.saveBatch(AdImageImage);

                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer adRecommendInfoId) {
        AdRecommendInfo entity = this.adRecommendInfoService.getById(adRecommendInfoId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer adRecommendInfoId) {
        boolean flag = this.adRecommendInfoService.removeById(adRecommendInfoId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("adRecommendInfo").data( AdRecommendInfo.builder().adRecommendInfoId(adRecommendInfoId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody AdRecommendInfoPageVO pageVO) {

        LambdaQueryWrapper<AdRecommendInfo> queryWrapper = new LambdaQueryWrapper<>();

                    if(StringUtils.isNotEmpty(pageVO.getAdTitle())) {
                            queryWrapper.eq(AdRecommendInfo::getAdTitle, pageVO.getAdTitle());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getAdImageResourceKey())) {
                            queryWrapper.eq(AdRecommendInfo::getAdImageResourceKey, pageVO.getAdImageResourceKey());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getAdUrl())) {
                            queryWrapper.eq(AdRecommendInfo::getAdUrl, pageVO.getAdUrl());
                        }
                        if(pageVO.getCpcFee() != null){
                            queryWrapper.eq(AdRecommendInfo::getCpcFee, pageVO.getCpcFee());
                        }
            List<AdRecommendInfo> list = adRecommendInfoService.list(queryWrapper);
            ExcelUtil<AdRecommendInfo> util = new ExcelUtil<>(AdRecommendInfo.class);
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
            excelProvider.importData(file, AdRecommendInfo.class, adRecommendInfoService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(AdRecommendInfo.class);
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





