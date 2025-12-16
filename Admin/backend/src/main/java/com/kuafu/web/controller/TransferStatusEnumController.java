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
import com.kuafuweb.web.entity.TransferStatusEnum;
import com.kuafuweb.web.service.ITransferStatusEnumService;
import com.kuafuweb.web.vo.TransferStatusEnumPageVO;
import com.kuafuweb.web.vo.TransferStatusEnumVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 调拨状态枚举 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/transferStatusEnum")
@Api(tags = {"调拨状态枚举"})
public class TransferStatusEnumController  {

    private final ITransferStatusEnumService transferStatusEnumService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody TransferStatusEnumPageVO pageVO){
        IPage<TransferStatusEnum> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<TransferStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(pageVO.getStatusName())) {
            queryWrapper.like(TransferStatusEnum::getStatusName, pageVO.getStatusName());
        }
        return ResultUtils.success(transferStatusEnumService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody TransferStatusEnumVO vo){
        LambdaQueryWrapper<TransferStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getStatusName())) {
            queryWrapper.like(TransferStatusEnum::getStatusName, vo.getStatusName());
        }
        return ResultUtils.success(transferStatusEnumService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<TransferStatusEnum> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(TransferStatusEnum::getTransferStatusEnumId);

        List<TransferStatusEnum> list =transferStatusEnumService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getTransferStatusEnumId(), l.getTransferStatusEnumId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody TransferStatusEnumVO vo) {
        TransferStatusEnum entity =  TransferStatusEnum.builder()
                    .statusName(vo.getStatusName())
                .build();
        boolean flag =this.transferStatusEnumService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("transferStatusEnum").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody TransferStatusEnumVO vo) {
        TransferStatusEnum entity =  TransferStatusEnum.builder()
                    .transferStatusEnumId(vo.getTransferStatusEnumId())
                    .statusName(vo.getStatusName())
                .build();
        boolean flag = this.transferStatusEnumService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("transferStatusEnum").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer transferStatusEnumId) {
        TransferStatusEnum entity = this.transferStatusEnumService.getById(transferStatusEnumId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer transferStatusEnumId) {
        boolean flag = this.transferStatusEnumService.removeById(transferStatusEnumId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("transferStatusEnum").data( TransferStatusEnum.builder().transferStatusEnumId(transferStatusEnumId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody TransferStatusEnumPageVO pageVO) {

        LambdaQueryWrapper<TransferStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

                    if(StringUtils.isNotEmpty(pageVO.getStatusName())) {
                                    queryWrapper.like(TransferStatusEnum::getStatusName, pageVO.getStatusName());
                        }
            List<TransferStatusEnum> list = transferStatusEnumService.list(queryWrapper);
            ExcelUtil<TransferStatusEnum> util = new ExcelUtil<>(TransferStatusEnum.class);
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
            excelProvider.importData(file, TransferStatusEnum.class, transferStatusEnumService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(TransferStatusEnum.class);
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





