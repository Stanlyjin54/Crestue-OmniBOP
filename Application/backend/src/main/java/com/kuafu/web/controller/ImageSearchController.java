package com.kuafu.web.controller;

import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.resource.service.IStaticResourceService;
import com.kuafu.common.entity.StaticResource;
import com.kuafu.web.service.IImageSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p> 图片全网信息获取 接口 </p>
 *
 * @author kuafuai
 * @description 用于获取图片的全网相关信息，包括图片识别、相似图片搜索、图片来源分析等
 * @date 2025/06/18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/imageSearch")
@Api(tags = {"图片全网信息获取"})
public class ImageSearchController {

    private final IImageSearchService imageSearchService;
    private final IStaticResourceService staticResourceService;

    /**
     * 上传图片并获取全网信息
     * @param file 图片文件
     * @return 图片全网信息
     */
    @PostMapping("/uploadAndSearch")
    @ApiOperation("上传图片并获取全网信息")
    public BaseResponse uploadAndSearch(@RequestPart("file") MultipartFile file) {
        try {
            // 保存图片资源
            StaticResource staticResource = staticResourceService.uploadFile(file);
            
            // 获取图片全网信息
            Map<String, Object> result = imageSearchService.getImageGlobalInfo(staticResource.getResourcePath());
            
            return ResultUtils.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "图片全网信息获取失败：" + e.getMessage());
        }
    }

    /**
     * 根据图片URL获取全网信息
     * @param imageUrl 图片URL
     * @return 图片全网信息
     */
    @PostMapping("/searchByUrl")
    @ApiOperation("根据图片URL获取全网信息")
    public BaseResponse searchByUrl(@RequestBody Map<String, String> request) {
        try {
            String imageUrl = request.get("imageUrl");
            if (imageUrl == null || imageUrl.isEmpty()) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "图片URL不能为空");
            }
            
            // 获取图片全网信息
            Map<String, Object> result = imageSearchService.getImageGlobalInfoByUrl(imageUrl);
            
            return ResultUtils.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "图片全网信息获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取图片的OCR文字识别结果
     * @param file 图片文件
     * @return OCR识别结果
     */
    @PostMapping("/ocr")
    @ApiOperation("获取图片的OCR文字识别结果")
    public BaseResponse ocr(@RequestPart("file") MultipartFile file) {
        try {
            // 保存图片资源
            StaticResource staticResource = staticResourceService.uploadFile(file);
            
            // 执行OCR识别
            String ocrResult = imageSearchService.getImageOcrResult(staticResource.getResourcePath());
            
            return ResultUtils.success(ocrResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "图片OCR识别失败：" + e.getMessage());
        }
    }
}