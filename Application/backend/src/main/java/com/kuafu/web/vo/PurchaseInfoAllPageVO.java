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
 * <p>采购信息-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PurchaseInfoAllPageVO extends BaseEntity {

@JsonProperty("purchaseNo")
    private String purchaseNo;
@JsonProperty("purchaseInfoId")
    private Integer purchaseInfoId;
@JsonProperty("purchaseStatusEnumPurchaseStatusEnumId1")
    private Integer purchaseStatusEnumPurchaseStatusEnumId1;
@JsonProperty("password")
    private String password;
@JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("registrationDate")
    private Date registrationDate;
@JsonProperty("totalAmount")
    private Double totalAmount;
@JsonProperty("statusName")
    private String statusName;
@JsonProperty("supplier")
    private String supplier;
@JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("purchaseDate")
    private Date purchaseDate;
@JsonProperty("username")
    private String username;

}
