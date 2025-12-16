package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.kuafu.common.entity.StaticResource;
import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.entity.BaseEntity;
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
public class PurchaseInfoVO  extends BaseEntity {

    @JsonProperty("purchaseInfoId")
    private Integer purchaseInfoId;
    @JsonProperty("purchaseNo")
    private String purchaseNo;
    @JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonProperty("supplier")
    private String supplier;
    @JsonProperty("totalAmount")
    private Double totalAmount;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("purchaseDate")
    private Date purchaseDate;
    @JsonProperty("purchaseStatusEnumPurchaseStatusEnumId1")
    private Integer purchaseStatusEnumPurchaseStatusEnumId1;




}
