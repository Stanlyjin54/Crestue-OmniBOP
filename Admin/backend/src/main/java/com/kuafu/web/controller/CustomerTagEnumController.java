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
import com.kuafuweb.web.entity.CustomerTagEnum;
import com.kuafuweb.web.service.ICustomerTagEnumService;
import com.kuafuweb.web.vo.CustomerTagEnumPageVO;
import com.kuafuweb.web.vo.CustomerTagEnumVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 客户标签枚举 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/customerTagEnum")
@Api(tags = {"客户标签枚举"})
public class CustomerTagEnumController  {

    private final ICustomerTagEnumService customerTagEnumService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody CustomerTagEnumPageVO pageVO){
        IPage<CustomerTagEnum> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<CustomerTagEnum> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(pageVO.getTagName())) {
            queryWrapper.like(CustomerTagEnum::getTagName, pageVO.getTagName());
        }
            if(pageVO.getIsAiGenerated() != null){
            queryWrapper.eq(CustomerTagEnum::getIsAiGenerated, pageVO.getIsAiGenerated());
        }
        return ResultUtils.success(customerTagEnumService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody CustomerTagEnumVO vo){
        LambdaQueryWrapper<CustomerTagEnum> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getTagName())) {
            queryWrapper.like(CustomerTagEnum::getTagName, vo.getTagName());
        }
                if(vo.getIsAiGenerated() != null){
            queryWrapper.eq(CustomerTagEnum::getIsAiGenerated, vo.getIsAiGenerated());
        }
        return ResultUtils.success(customerTagEnumService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<CustomerTagEnum> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(CustomerTagEnum::getCustomerTagEnumId);

        List<CustomerTagEnum> list =customerTagEnumService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getCustomerTagEnumId(), l.getCustomerTagEnumId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody CustomerTagEnumVO vo) {
        CustomerTagEnum entity =  CustomerTagEnum.builder()
                    .tagName(vo.getTagName())
                    .isAiGenerated(vo.getIsAiGenerated())
                .build();
        boolean flag =this.customerTagEnumService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("customerTagEnum").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody CustomerTagEnumVO vo) {
        CustomerTagEnum entity =  CustomerTagEnum.builder()
                    .customerTagEnumId(vo.getCustomerTagEnumId())
                    .tagName(vo.getTagName())
                    .isAiGenerated(vo.getIsAiGenerated())
                .build();
        boolean flag = this.customerTagEnumService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("customerTagEnum").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer customerTagEnumId) {
        CustomerTagEnum entity = this.customerTagEnumService.getById(customerTagEnumId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer customerTagEnumId) {
        boolean flag = this.customerTagEnumService.removeById(customerTagEnumId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("customerTagEnum").data( CustomerTagEnum.builder().customerTagEnumId(customerTagEnumId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody CustomerTagEnumPageVO pageVO) {

        LambdaQueryWrapper<CustomerTagEnum> queryWrapper = new LambdaQueryWrapper<>();

                    if(StringUtils.isNotEmpty(pageVO.getTagName())) {
                                    queryWrapper.like(CustomerTagEnum::getTagName, pageVO.getTagName());
                        }
                        if(pageVO.getIsAiGenerated() != null){
                            queryWrapper.eq(CustomerTagEnum::getIsAiGenerated, pageVO.getIsAiGenerated());
                        }
            List<CustomerTagEnum> list = customerTagEnumService.list(queryWrapper);
            ExcelUtil<CustomerTagEnum> util = new ExcelUtil<>(CustomerTagEnum.class);
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
            excelProvider.importData(file, CustomerTagEnum.class, customerTagEnumService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(CustomerTagEnum.class);
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





