package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>名片OCR建档</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessCardOcrVO  {

     @JsonProperty(value = "businessCardOcrId")
    private Integer businessCardOcrId;
     @JsonProperty(value = "customerInfoCustomerInfoId1")
    private Integer customerInfoCustomerInfoId1;
     @JsonProperty(value = "cardImageResourceKey")
    private String cardImageResourceKey;
     @JsonProperty(value = "ocrResult")
    private String ocrResult;

    @JsonProperty("cardImage")
    private List<StaticResource> cardImage;

}





