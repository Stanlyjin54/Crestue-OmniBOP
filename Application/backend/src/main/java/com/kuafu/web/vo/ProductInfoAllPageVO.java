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
 * <p>商品信息-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductInfoAllPageVO extends BaseEntity {

@JsonProperty("unit")
    private String unit;
@JsonProperty("productInfoId")
    private Integer productInfoId;
@JsonProperty("productStatusEnumProductStatusEnumId1")
    private Integer productStatusEnumProductStatusEnumId1;
@JsonProperty("statusName")
    private String statusName;
@JsonProperty("specification")
    private String specification;
@JsonProperty("serialNumber")
    private String serialNumber;
@JsonProperty("minStock")
    private Integer minStock;
@JsonProperty("category")
    private String category;
@JsonProperty("productName")
    private String productName;
@JsonProperty("barcode")
    private String barcode;
@JsonProperty("brand")
    private String brand;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("shelfLife")
    private Date shelfLife;

}
