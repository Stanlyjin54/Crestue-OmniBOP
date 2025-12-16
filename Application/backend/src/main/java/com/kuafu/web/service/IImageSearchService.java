package com.kuafuweb.web.service;

import java.util.Map;

/**
 * <p> 图片全网信息获取 服务类 </p>
 *
 * @author kuafuai
 * @description 图片全网信息获取服务，提供图片识别、相似图片搜索、图片来源分析等功能
 * @date 2025/06/18
 */
public interface IImageSearchService {

    /**
     * 获取图片的全网信息
     * @param imagePath 图片路径
     * @return 图片全网信息，包含OCR识别结果、相似图片、来源分析等
     */
    Map<String, Object> getImageGlobalInfo(String imagePath);

    /**
     * 根据图片URL获取全网信息
     * @param imageUrl 图片URL
     * @return 图片全网信息
     */
    Map<String, Object> getImageGlobalInfoByUrl(String imageUrl);

    /**
     * 获取图片的OCR文字识别结果
     * @param imagePath 图片路径
     * @return OCR识别结果
     */
    String getImageOcrResult(String imagePath);

    /**
     * 搜索相似图片
     * @param imagePath 图片路径
     * @return 相似图片列表
     */
    Map<String, Object> searchSimilarImages(String imagePath);

    /**
     * 分析图片来源
     * @param imagePath 图片路径
     * @return 图片来源信息
     */
    Map<String, Object> analyzeImageSource(String imagePath);

    /**
     * 识别图片中的物体
     * @param imagePath 图片路径
     * @return 物体识别结果
     */
    Map<String, Object> recognizeImageObjects(String imagePath);
}