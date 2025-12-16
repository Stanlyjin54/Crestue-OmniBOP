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
 * <p>  客户信息 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfoAll {
    @TableField(value="ci.wechat_id")
    @JsonProperty("wechat_id")
    private String wechatId;
    @TableField(value="ci.address")
    @JsonProperty("address")
    private String address;
    @TableField(value="ci.customer_info_id")
    @JsonProperty("customer_info_id")
    private Integer customerInfoId;
    @TableField(value="cte.tag_name")
    @JsonProperty("tag_name")
    private String tagName;
    @TableField(value="ci.source")
    @JsonProperty("source")
    private String source;
    @TableField(value="rle.level_name")
    @JsonProperty("level_name")
    private String levelName;
    @TableField(value="ci.phone")
    @JsonProperty("phone")
    private String phone;
    @TableField(value="ci.wechat_data_encrypted")
    @JsonProperty("wechat_data_encrypted")
    private String wechatDataEncrypted;
    @TableField(value="cte.is_ai_generated")
    @JsonProperty("is_ai_generated")
    private Boolean isAiGenerated;
    @TableField(value="ci.rfm_level_enum_rfm_level_enum_id_1")
    @JsonProperty("rfm_level_enum_rfm_level_enum_id_1")
    private Integer rfmLevelEnumRfmLevelEnumId1;
    @TableField(value="ci.customer_tag_enum_customer_tag_enum_id_1")
    @JsonProperty("customer_tag_enum_customer_tag_enum_id_1")
    private Integer customerTagEnumCustomerTagEnumId1;
    @TableField(value="ci.customer_name")
    @JsonProperty("customer_name")
    private String customerName;
    @TableField(value="ci.email")
    @JsonProperty("email")
    private String email;









}
