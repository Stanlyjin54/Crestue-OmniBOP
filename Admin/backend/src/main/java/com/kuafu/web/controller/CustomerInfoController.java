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
import com.kuafuweb.web.entity.CustomerInfo;
import com.kuafuweb.web.service.ICustomerInfoService;
import com.kuafuweb.web.vo.CustomerInfoPageVO;
import com.kuafuweb.web.vo.CustomerInfoVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 客户信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/customerInfo")
@Api(tags = {"客户信息"})
public class CustomerInfoController  {

    private final ICustomerInfoService customerInfoService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody CustomerInfoPageVO pageVO){
        IPage<CustomerInfo> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<CustomerInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(pageVO.getCustomerName())) {
            queryWrapper.like(CustomerInfo::getCustomerName, pageVO.getCustomerName());
        }
            if(StringUtils.isNotEmpty(pageVO.getPhone())) {
            queryWrapper.eq(CustomerInfo::getPhone, pageVO.getPhone());
        }
            if(StringUtils.isNotEmpty(pageVO.getEmail())) {
            queryWrapper.eq(CustomerInfo::getEmail, pageVO.getEmail());
        }
            if(StringUtils.isNotEmpty(pageVO.getAddress())) {
            queryWrapper.eq(CustomerInfo::getAddress, pageVO.getAddress());
        }
            if(pageVO.getRfmLevelEnumRfmLevelEnumId1() != null){
            queryWrapper.eq(CustomerInfo::getRfmLevelEnumRfmLevelEnumId1, pageVO.getRfmLevelEnumRfmLevelEnumId1());
        }
            if(pageVO.getCustomerTagEnumCustomerTagEnumId1() != null){
            queryWrapper.eq(CustomerInfo::getCustomerTagEnumCustomerTagEnumId1, pageVO.getCustomerTagEnumCustomerTagEnumId1());
        }
            if(StringUtils.isNotEmpty(pageVO.getWechatId())) {
            queryWrapper.eq(CustomerInfo::getWechatId, pageVO.getWechatId());
        }
            if(StringUtils.isNotEmpty(pageVO.getWechatDataEncrypted())) {
            queryWrapper.eq(CustomerInfo::getWechatDataEncrypted, pageVO.getWechatDataEncrypted());
        }
            if(StringUtils.isNotEmpty(pageVO.getSource())) {
            queryWrapper.eq(CustomerInfo::getSource, pageVO.getSource());
        }
        return ResultUtils.success(customerInfoService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody CustomerInfoVO vo){
        LambdaQueryWrapper<CustomerInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getCustomerName())) {
            queryWrapper.like(CustomerInfo::getCustomerName, vo.getCustomerName());
        }
            if(StringUtils.isNotEmpty(vo.getPhone())) {
            queryWrapper.eq(CustomerInfo::getPhone, vo.getPhone());
        }
            if(StringUtils.isNotEmpty(vo.getEmail())) {
            queryWrapper.eq(CustomerInfo::getEmail, vo.getEmail());
        }
            if(StringUtils.isNotEmpty(vo.getAddress())) {
            queryWrapper.eq(CustomerInfo::getAddress, vo.getAddress());
        }
                if(vo.getRfmLevelEnumRfmLevelEnumId1() != null){
            queryWrapper.eq(CustomerInfo::getRfmLevelEnumRfmLevelEnumId1, vo.getRfmLevelEnumRfmLevelEnumId1());
        }
                if(vo.getCustomerTagEnumCustomerTagEnumId1() != null){
            queryWrapper.eq(CustomerInfo::getCustomerTagEnumCustomerTagEnumId1, vo.getCustomerTagEnumCustomerTagEnumId1());
        }
            if(StringUtils.isNotEmpty(vo.getWechatId())) {
            queryWrapper.eq(CustomerInfo::getWechatId, vo.getWechatId());
        }
            if(StringUtils.isNotEmpty(vo.getWechatDataEncrypted())) {
            queryWrapper.eq(CustomerInfo::getWechatDataEncrypted, vo.getWechatDataEncrypted());
        }
            if(StringUtils.isNotEmpty(vo.getSource())) {
            queryWrapper.eq(CustomerInfo::getSource, vo.getSource());
        }
        return ResultUtils.success(customerInfoService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<CustomerInfo> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(CustomerInfo::getCustomerInfoId);

        List<CustomerInfo> list =customerInfoService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getCustomerInfoId(), l.getCustomerInfoId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody CustomerInfoVO vo) {
        CustomerInfo entity =  CustomerInfo.builder()
                    .customerName(vo.getCustomerName())
                    .phone(vo.getPhone())
                    .email(vo.getEmail())
                    .address(vo.getAddress())
                    .rfmLevelEnumRfmLevelEnumId1(vo.getRfmLevelEnumRfmLevelEnumId1())
                    .customerTagEnumCustomerTagEnumId1(vo.getCustomerTagEnumCustomerTagEnumId1())
                    .wechatId(vo.getWechatId())
                    .wechatDataEncrypted(vo.getWechatDataEncrypted())
                    .source(vo.getSource())
                .build();
        boolean flag =this.customerInfoService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("customerInfo").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody CustomerInfoVO vo) {
        CustomerInfo entity =  CustomerInfo.builder()
                    .customerInfoId(vo.getCustomerInfoId())
                    .customerName(vo.getCustomerName())
                    .phone(vo.getPhone())
                    .email(vo.getEmail())
                    .address(vo.getAddress())
                    .rfmLevelEnumRfmLevelEnumId1(vo.getRfmLevelEnumRfmLevelEnumId1())
                    .customerTagEnumCustomerTagEnumId1(vo.getCustomerTagEnumCustomerTagEnumId1())
                    .wechatId(vo.getWechatId())
                    .wechatDataEncrypted(vo.getWechatDataEncrypted())
                    .source(vo.getSource())
                .build();
        boolean flag = this.customerInfoService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("customerInfo").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer customerInfoId) {
        CustomerInfo entity = this.customerInfoService.getById(customerInfoId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer customerInfoId) {
        boolean flag = this.customerInfoService.removeById(customerInfoId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("customerInfo").data( CustomerInfo.builder().customerInfoId(customerInfoId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody CustomerInfoPageVO pageVO) {

        LambdaQueryWrapper<CustomerInfo> queryWrapper = new LambdaQueryWrapper<>();

                    if(StringUtils.isNotEmpty(pageVO.getCustomerName())) {
                                    queryWrapper.like(CustomerInfo::getCustomerName, pageVO.getCustomerName());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getPhone())) {
                            queryWrapper.eq(CustomerInfo::getPhone, pageVO.getPhone());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getEmail())) {
                            queryWrapper.eq(CustomerInfo::getEmail, pageVO.getEmail());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getAddress())) {
                            queryWrapper.eq(CustomerInfo::getAddress, pageVO.getAddress());
                        }
                        if(pageVO.getRfmLevelEnumRfmLevelEnumId1() != null){
                            queryWrapper.eq(CustomerInfo::getRfmLevelEnumRfmLevelEnumId1, pageVO.getRfmLevelEnumRfmLevelEnumId1());
                        }
                        if(pageVO.getCustomerTagEnumCustomerTagEnumId1() != null){
                            queryWrapper.eq(CustomerInfo::getCustomerTagEnumCustomerTagEnumId1, pageVO.getCustomerTagEnumCustomerTagEnumId1());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getWechatId())) {
                            queryWrapper.eq(CustomerInfo::getWechatId, pageVO.getWechatId());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getWechatDataEncrypted())) {
                            queryWrapper.eq(CustomerInfo::getWechatDataEncrypted, pageVO.getWechatDataEncrypted());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getSource())) {
                            queryWrapper.eq(CustomerInfo::getSource, pageVO.getSource());
                        }
            List<CustomerInfo> list = customerInfoService.list(queryWrapper);
            ExcelUtil<CustomerInfo> util = new ExcelUtil<>(CustomerInfo.class);
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
            excelProvider.importData(file, CustomerInfo.class, customerInfoService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(CustomerInfo.class);
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





