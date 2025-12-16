package com.kuafuweb.web.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.*;
import com.kuafuweb.common.entity.StaticResource;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kuafuweb.web.annotation.IsNotNullField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import com.kuafu.common.annotation.*;
/**
 * <p>  利润看板 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("profit_board")
public class ProfitBoard  {
    @TableId(value = "profit_board_id", type = IdType.AUTO)
    @JsonProperty("profit_board_id")
    
    
    
    private Integer profitBoardId;
    @JsonProperty("target_type")
    @IsNotNullField(description = "维度类型")
    
    
    @TableField("target_type")
    private String targetType;
    @JsonProperty("target_id")
    @IsNotNullField(description = "维度ID")
    
    
    @TableField("target_id")
    private Integer targetId;
    @JsonProperty("profit_amount")
    @IsNotNullField(description = "毛利金额")
    
    
    @TableField("profit_amount")
    private Double profitAmount;
    @JsonProperty("commission")
    
    
    @TableField("commission")
    private Double commission;
    @JsonProperty("shipping_fee")
    
    
    @TableField("shipping_fee")
    private Double shippingFee;
    @JsonProperty("stat_date")
    @IsNotNullField(description = "统计日期")
    
    
    @TableField("stat_date")
    private Date statDate;





}
