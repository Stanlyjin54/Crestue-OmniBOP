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
 * <p>  订单状态枚举 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_status_enum")
public class OrderStatusEnum  {
    @TableId(value = "order_status_enum_id", type = IdType.AUTO)
    @JsonProperty("order_status_enum_id")
    
    
    
    private Integer orderStatusEnumId;
    @JsonProperty("status_name")
    @IsNotNullField(description = "状态名称")
    
    
    @TableField("status_name")
    private String statusName;





}
