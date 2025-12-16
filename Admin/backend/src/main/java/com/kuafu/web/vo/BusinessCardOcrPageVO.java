package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>名片OCR建档-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BusinessCardOcrPageVO extends PageRequest {

    @JsonProperty(value = "businessCardOcrId")
    private Integer businessCardOcrId;
    @JsonProperty(value = "customerInfoCustomerInfoId1")
    private Integer customerInfoCustomerInfoId1;
    @JsonProperty(value = "cardImageResourceKey")
    private String cardImageResourceKey;
    @JsonProperty(value = "ocrResult")
    private String ocrResult;

}





