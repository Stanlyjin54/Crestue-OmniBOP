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
 * <p>客户信息-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerInfoAllPageVO extends BaseEntity {

@JsonProperty("wechatId")
    private String wechatId;
@JsonProperty("address")
    private String address;
@JsonProperty("customerInfoId")
    private Integer customerInfoId;
@JsonProperty("tagName")
    private String tagName;
@JsonProperty("source")
    private String source;
@JsonProperty("levelName")
    private String levelName;
@JsonProperty("phone")
    private String phone;
@JsonProperty("wechatDataEncrypted")
    private String wechatDataEncrypted;
@JsonProperty("isAiGenerated")
    private Boolean isAiGenerated;
@JsonProperty("rfmLevelEnumRfmLevelEnumId1")
    private Integer rfmLevelEnumRfmLevelEnumId1;
@JsonProperty("customerTagEnumCustomerTagEnumId1")
    private Integer customerTagEnumCustomerTagEnumId1;
@JsonProperty("customerName")
    private String customerName;
@JsonProperty("email")
    private String email;

}
