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
import com.kuafuweb.web.entity.ProfitBoard;
import com.kuafuweb.web.service.IProfitBoardService;
import com.kuafuweb.web.vo.ProfitBoardPageVO;
import com.kuafuweb.web.vo.ProfitBoardVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 利润看板 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/profitBoard")
@Api(tags = {"利润看板"})
public class ProfitBoardController  {

    private final IProfitBoardService profitBoardService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody ProfitBoardPageVO pageVO){
        IPage<ProfitBoard> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<ProfitBoard> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(pageVO.getTargetType())) {
            queryWrapper.eq(ProfitBoard::getTargetType, pageVO.getTargetType());
        }
            if(pageVO.getTargetId() != null){
            queryWrapper.eq(ProfitBoard::getTargetId, pageVO.getTargetId());
        }
            if(pageVO.getProfitAmount() != null){
            queryWrapper.eq(ProfitBoard::getProfitAmount, pageVO.getProfitAmount());
        }
            if(pageVO.getCommission() != null){
            queryWrapper.eq(ProfitBoard::getCommission, pageVO.getCommission());
        }
            if(pageVO.getShippingFee() != null){
            queryWrapper.eq(ProfitBoard::getShippingFee, pageVO.getShippingFee());
        }
            if(pageVO.getStatDate() != null){
            queryWrapper.eq(ProfitBoard::getStatDate, pageVO.getStatDate());
        }
        return ResultUtils.success(profitBoardService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody ProfitBoardVO vo){
        LambdaQueryWrapper<ProfitBoard> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getTargetType())) {
            queryWrapper.eq(ProfitBoard::getTargetType, vo.getTargetType());
        }
                if(vo.getTargetId() != null){
            queryWrapper.eq(ProfitBoard::getTargetId, vo.getTargetId());
        }
                if(vo.getProfitAmount() != null){
            queryWrapper.eq(ProfitBoard::getProfitAmount, vo.getProfitAmount());
        }
                if(vo.getCommission() != null){
            queryWrapper.eq(ProfitBoard::getCommission, vo.getCommission());
        }
                if(vo.getShippingFee() != null){
            queryWrapper.eq(ProfitBoard::getShippingFee, vo.getShippingFee());
        }
                if(vo.getStatDate() != null){
            queryWrapper.eq(ProfitBoard::getStatDate, vo.getStatDate());
        }
        return ResultUtils.success(profitBoardService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<ProfitBoard> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(ProfitBoard::getProfitBoardId);

        List<ProfitBoard> list =profitBoardService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getProfitBoardId(), l.getProfitBoardId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody ProfitBoardVO vo) {
        ProfitBoard entity =  ProfitBoard.builder()
                    .targetType(vo.getTargetType())
                    .targetId(vo.getTargetId())
                    .profitAmount(vo.getProfitAmount())
                    .commission(vo.getCommission())
                    .shippingFee(vo.getShippingFee())
                    .statDate(vo.getStatDate())
                .build();
        boolean flag =this.profitBoardService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("profitBoard").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody ProfitBoardVO vo) {
        ProfitBoard entity =  ProfitBoard.builder()
                    .profitBoardId(vo.getProfitBoardId())
                    .targetType(vo.getTargetType())
                    .targetId(vo.getTargetId())
                    .profitAmount(vo.getProfitAmount())
                    .commission(vo.getCommission())
                    .shippingFee(vo.getShippingFee())
                    .statDate(vo.getStatDate())
                .build();
        boolean flag = this.profitBoardService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("profitBoard").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer profitBoardId) {
        ProfitBoard entity = this.profitBoardService.getById(profitBoardId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer profitBoardId) {
        boolean flag = this.profitBoardService.removeById(profitBoardId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("profitBoard").data( ProfitBoard.builder().profitBoardId(profitBoardId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody ProfitBoardPageVO pageVO) {

        LambdaQueryWrapper<ProfitBoard> queryWrapper = new LambdaQueryWrapper<>();

                    if(StringUtils.isNotEmpty(pageVO.getTargetType())) {
                            queryWrapper.eq(ProfitBoard::getTargetType, pageVO.getTargetType());
                        }
                        if(pageVO.getTargetId() != null){
                            queryWrapper.eq(ProfitBoard::getTargetId, pageVO.getTargetId());
                        }
                        if(pageVO.getProfitAmount() != null){
                            queryWrapper.eq(ProfitBoard::getProfitAmount, pageVO.getProfitAmount());
                        }
                        if(pageVO.getCommission() != null){
                            queryWrapper.eq(ProfitBoard::getCommission, pageVO.getCommission());
                        }
                        if(pageVO.getShippingFee() != null){
                            queryWrapper.eq(ProfitBoard::getShippingFee, pageVO.getShippingFee());
                        }
                        if(pageVO.getStatDate() != null){
                            queryWrapper.eq(ProfitBoard::getStatDate, pageVO.getStatDate());
                        }
            List<ProfitBoard> list = profitBoardService.list(queryWrapper);
            ExcelUtil<ProfitBoard> util = new ExcelUtil<>(ProfitBoard.class);
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
            excelProvider.importData(file, ProfitBoard.class, profitBoardService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(ProfitBoard.class);
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





