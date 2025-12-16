package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class CustomerInfoPageVO extends PageRequest {

    @JsonProperty(value = "customerInfoId")
    private Integer customerInfoId;
    @JsonProperty(value = "customerName")
    private String customerName;
    @JsonProperty(value = "phone")
    private String phone;
    @JsonProperty(value = "email")
    private String email;
    @JsonProperty(value = "address")
    private String address;
    @JsonProperty(value = "rfmLevelEnumRfmLevelEnumId1")
    private Integer rfmLevelEnumRfmLevelEnumId1;
    @JsonProperty(value = "customerTagEnumCustomerTagEnumId1")
    private Integer customerTagEnumCustomerTagEnumId1;
    @JsonProperty(value = "wechatId")
    private String wechatId;
    @JsonProperty(value = "wechatDataEncrypted")
    private String wechatDataEncrypted;
    @JsonProperty(value = "source")
    private String source;

}





