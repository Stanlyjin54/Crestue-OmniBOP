package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>采购信息</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseInfoVO  {

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





