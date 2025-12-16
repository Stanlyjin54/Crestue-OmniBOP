package com.kuafu.web.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.domin.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.kuafu.common.entity.BaseEntity;
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
public class CustomerInfoPageVO extends BaseEntity {

@JsonProperty("customerInfoId")
    private Integer customerInfoId;
@JsonProperty("customerName")
    private String customerName;
@JsonProperty("phone")
    private String phone;
@JsonProperty("email")
    private String email;
@JsonProperty("address")
    private String address;
@JsonProperty("rfmLevelEnumRfmLevelEnumId1")
    private Integer rfmLevelEnumRfmLevelEnumId1;
@JsonProperty("customerTagEnumCustomerTagEnumId1")
    private Integer customerTagEnumCustomerTagEnumId1;
@JsonProperty("wechatId")
    private String wechatId;
@JsonProperty("wechatDataEncrypted")
    private String wechatDataEncrypted;
@JsonProperty("source")
    private String source;


}
