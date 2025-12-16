package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class PurchaseInfoPageVO extends PageRequest {

    @JsonProperty(value = "purchaseInfoId")
    private Integer purchaseInfoId;
    @JsonProperty(value = "purchaseNo")
    private String purchaseNo;
    @JsonProperty(value = "userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonProperty(value = "supplier")
    private String supplier;
    @JsonProperty(value = "totalAmount")
    private Double totalAmount;
    @JsonProperty(value = "purchaseDate")
    private Date purchaseDate;
    @JsonProperty(value = "purchaseStatusEnumPurchaseStatusEnumId1")
    private Integer purchaseStatusEnumPurchaseStatusEnumId1;

}





