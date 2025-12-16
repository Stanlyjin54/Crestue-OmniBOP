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
 * <p>商机线索-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BusinessOpportunityAllPageVO extends BaseEntity {

@JsonProperty("wechatId")
    private String wechatId;
@JsonProperty("followUpSuggestion")
    private String followUpSuggestion;
@JsonProperty("address")
    private String address;
@JsonProperty("businessOpportunityId")
    private Integer businessOpportunityId;
@JsonProperty("source")
    private String source;
@JsonProperty("customerInfoCustomerInfoId1")
    private Integer customerInfoCustomerInfoId1;
@JsonProperty("phone")
    private String phone;
@JsonProperty("wechatDataEncrypted")
    private String wechatDataEncrypted;
@JsonProperty("opportunityName")
    private String opportunityName;
@JsonProperty("rfmLevelEnumRfmLevelEnumId1")
    private Integer rfmLevelEnumRfmLevelEnumId1;
@JsonProperty("customerTagEnumCustomerTagEnumId1")
    private Integer customerTagEnumCustomerTagEnumId1;
@JsonProperty("customerName")
    private String customerName;
@JsonProperty("email")
    private String email;
@JsonProperty("status")
    private String status;

}
