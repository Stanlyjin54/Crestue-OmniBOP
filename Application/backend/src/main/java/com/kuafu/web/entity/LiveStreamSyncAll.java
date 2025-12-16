package com.kuafu.web.entity;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



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
public class LiveStreamSyncAll {
    @TableField(value="oi.order_no")
    @JsonProperty("order_no")
    private String orderNo;
    @TableField(value="oi.user_info_user_info_id_1")
    @JsonProperty("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;
    @TableField(value="lss.sync_time")
    @JsonProperty("sync_time")
    private Date syncTime;
    @TableField(value="oi.profit_amount")
    @JsonProperty("profit_amount")
    private Double profitAmount;
    @TableField(value="lss.live_stream_sync_id")
    @JsonProperty("live_stream_sync_id")
    private Integer liveStreamSyncId;
    @TableField(value="lss.platform")
    @JsonProperty("platform")
    private String platform;
    @TableField(value="oi.order_status_enum_order_status_enum_id_1")
    @JsonProperty("order_status_enum_order_status_enum_id_1")
    private Integer orderStatusEnumOrderStatusEnumId1;
    @TableField(value="lss.order_info_order_info_id_1")
    @JsonProperty("order_info_order_info_id_1")
    private Integer orderInfoOrderInfoId1;
    @TableField(value="oi.shipping_fee")
    @JsonProperty("shipping_fee")
    private Double shippingFee;
    @TableField(value="oi.order_date")
    @JsonProperty("order_date")
    private Date orderDate;
    @TableField(value="oi.general_orders")
    @JsonProperty("general_orders")
    private String generalOrders;
    @TableField(value="oi.customer_info_customer_info_id_1")
    @JsonProperty("customer_info_customer_info_id_1")
    private Integer customerInfoCustomerInfoId1;
    @TableField(value="oi.total_amount")
    @JsonProperty("total_amount")
    private Double totalAmount;
    @TableField(value="oi.commission")
    @JsonProperty("commission")
    private Double commission;









}
