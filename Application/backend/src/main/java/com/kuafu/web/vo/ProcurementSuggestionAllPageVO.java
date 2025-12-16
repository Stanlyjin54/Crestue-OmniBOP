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
 * <p>采购建议-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProcurementSuggestionAllPageVO extends BaseEntity {

@JsonProperty("reason")
    private String reason;
@JsonProperty("productStatusEnumProductStatusEnumId1")
    private Integer productStatusEnumProductStatusEnumId1;
@JsonProperty("specification")
    private String specification;
@JsonProperty("serialNumber")
    private String serialNumber;
@JsonProperty("productInfoProductInfoId1")
    private Integer productInfoProductInfoId1;
@JsonProperty("productName")
    private String productName;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("suggestDate")
    private Date suggestDate;
@JsonProperty("suggestQuantity")
    private Integer suggestQuantity;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("shelfLife")
    private Date shelfLife;
@JsonProperty("unit")
    private String unit;
@JsonProperty("minStock")
    private Integer minStock;
@JsonProperty("category")
    private String category;
@JsonProperty("procurementSuggestionId")
    private Integer procurementSuggestionId;
@JsonProperty("barcode")
    private String barcode;
@JsonProperty("brand")
    private String brand;

}
