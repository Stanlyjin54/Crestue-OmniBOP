package com.kuafu.web.vo;

import com.kuafu.common.domin.PageRequest;
import com.kuafu.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import com.kuafu.common.entity.StaticResource;

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
public class BusinessCardOcrAllPageVO extends BaseEntity {

@JsonProperty("wechatId")
    private String wechatId;
@JsonProperty("address")
    private String address;
@JsonProperty("customerInfoCustomerInfoId1")
    private Integer customerInfoCustomerInfoId1;
@JsonProperty("phone")
    private String phone;
@JsonProperty("wechatDataEncrypted")
    private String wechatDataEncrypted;
@JsonProperty("businessCardOcrId")
    private Integer businessCardOcrId;
@JsonProperty("rfmLevelEnumRfmLevelEnumId1")
    private Integer rfmLevelEnumRfmLevelEnumId1;
@JsonProperty("customerTagEnumCustomerTagEnumId1")
    private Integer customerTagEnumCustomerTagEnumId1;
@JsonProperty("customerName")
    private String customerName;
@JsonProperty("source")
    private String source;
@JsonProperty("ocrResult")
    private String ocrResult;
@JsonProperty("email")
    private String email;

}
