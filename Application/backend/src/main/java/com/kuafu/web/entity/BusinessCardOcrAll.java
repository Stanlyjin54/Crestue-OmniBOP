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
 * <p>  名片OCR建档 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessCardOcrAll {
    @TableField(value="ci.wechat_id")
    @JsonProperty("wechat_id")
    private String wechatId;
    @TableField(value="ci.address")
    @JsonProperty("address")
    private String address;
    @TableField(value="bco.customer_info_customer_info_id_1")
    @JsonProperty("customer_info_customer_info_id_1")
    private Integer customerInfoCustomerInfoId1;
    @TableField(value="ci.phone")
    @JsonProperty("phone")
    private String phone;
    @TableField(value="ci.wechat_data_encrypted")
    @JsonProperty("wechat_data_encrypted")
    private String wechatDataEncrypted;
    @TableField(value="bco.business_card_ocr_id")
    @JsonProperty("business_card_ocr_id")
    private Integer businessCardOcrId;
    @TableField(value="ci.rfm_level_enum_rfm_level_enum_id_1")
    @JsonProperty("rfm_level_enum_rfm_level_enum_id_1")
    private Integer rfmLevelEnumRfmLevelEnumId1;
    @TableField(value="ci.customer_tag_enum_customer_tag_enum_id_1")
    @JsonProperty("customer_tag_enum_customer_tag_enum_id_1")
    private Integer customerTagEnumCustomerTagEnumId1;
    @TableField(value="ci.customer_name")
    @JsonProperty("customer_name")
    private String customerName;
    @TableField(value="ci.source")
    @JsonProperty("source")
    private String source;
    @TableField(value="bco.ocr_result")
    @JsonProperty("ocr_result")
    private String ocrResult;
    @TableField(value="ci.email")
    @JsonProperty("email")
    private String email;


@TableField(value="card_image")
@JsonProperty("card_image")
private List<StaticResource> cardImage;







}
