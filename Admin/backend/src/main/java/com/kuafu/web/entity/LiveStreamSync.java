package com.kuafuweb.web.entity;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuafuweb.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;
import com.kuafuweb.common.annotation.Excel.*;



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
public class LiveStreamSync    {
    @TableId(value = "live_stream_sync_id", type = IdType.AUTO)
    @JsonProperty(value = "liveStreamSyncId")
    @Excel(name = "主键")

    private Integer liveStreamSyncId;
    @JsonProperty(value = "orderInfoOrderInfoId1")
    @Excel(name = "订单ID")
    @ExcelProperty(value = "订单ID")
    @TableField(value = "order_info_order_info_id_1")

    private Integer orderInfoOrderInfoId1;
    @JsonProperty(value = "platform")
    @Excel(name = "平台名称")
    @ExcelProperty(value = "平台名称")
    @TableField(value = "platform")

    private String platform;
    @JsonProperty(value = "syncTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "同步时间")
    @ExcelProperty(value = "同步时间")
    @TableField(value = "sync_time")

    private Date syncTime;


}





