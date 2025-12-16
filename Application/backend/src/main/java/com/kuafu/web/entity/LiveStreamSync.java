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
 * <p>  直播带货同步 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("live_stream_sync")
public class LiveStreamSync  {
    @TableId(value = "live_stream_sync_id", type = IdType.AUTO)
    @JsonProperty("live_stream_sync_id")
    
    
    
    private Integer liveStreamSyncId;
    @JsonProperty("order_info_order_info_id_1")
    @IsNotNullField(description = "订单ID")
    
    
    @TableField("order_info_order_info_id_1")
    private Integer orderInfoOrderInfoId1;
    @JsonProperty("platform")
    @IsNotNullField(description = "平台名称")
    
    
    @TableField("platform")
    private String platform;
    @JsonProperty("sync_time")
    @IsNotNullField(description = "同步时间")
    
    
    @TableField("sync_time")
    private Date syncTime;





}
