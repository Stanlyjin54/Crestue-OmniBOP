package com.kuafu.web.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.*;
import com.kuafu.common.entity.StaticResource;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kuafu.web.annotation.IsNotNullField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import com.kuafu.common.annotation.*;
/**
 * <p>  采购建议 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("procurement_suggestion")
public class ProcurementSuggestion  {
    @TableId(value = "procurement_suggestion_id", type = IdType.AUTO)
    @JsonProperty("procurement_suggestion_id")
    
    
    
    private Integer procurementSuggestionId;
    @JsonProperty("product_info_product_info_id_1")
    @IsNotNullField(description = "商品ID")
    
    
    @TableField("product_info_product_info_id_1")
    private Integer productInfoProductInfoId1;
    @JsonProperty("suggest_quantity")
    @IsNotNullField(description = "建议采购数量")
    
    
    @TableField("suggest_quantity")
    private Integer suggestQuantity;
    @JsonProperty("reason")
    
    
    @TableField("reason")
    private String reason;
    @JsonProperty("suggest_date")
    @IsNotNullField(description = "建议日期")
    
    
    @TableField("suggest_date")
    private Date suggestDate;





}
