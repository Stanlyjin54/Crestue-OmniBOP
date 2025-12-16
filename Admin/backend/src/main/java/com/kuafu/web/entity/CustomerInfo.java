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
@TableName("customer_info")
public class CustomerInfo    {
    @TableId(value = "customer_info_id", type = IdType.AUTO)
    @JsonProperty(value = "customerInfoId")
    @Excel(name = "主键")

    private Integer customerInfoId;
    @JsonProperty(value = "customerName")
    @Excel(name = "客户名称")
    @ExcelProperty(value = "客户名称")
    @TableField(value = "customer_name")

    private String customerName;
    @JsonProperty(value = "phone")
    @Excel(name = "手机号")
    @ExcelProperty(value = "手机号")
    @TableField(value = "phone")

    private String phone;
    @JsonProperty(value = "email")
    @Excel(name = "邮箱")
    @ExcelProperty(value = "邮箱")
    @TableField(value = "email")

    private String email;
    @JsonProperty(value = "address")
    @Excel(name = "地址")
    @ExcelProperty(value = "地址")
    @TableField(value = "address")

    private String address;
    @JsonProperty(value = "rfmLevelEnumRfmLevelEnumId1")
    @Excel(name = "RFM分层")
    @ExcelProperty(value = "RFM分层")
    @TableField(value = "rfm_level_enum_rfm_level_enum_id_1")

    private Integer rfmLevelEnumRfmLevelEnumId1;
    @JsonProperty(value = "customerTagEnumCustomerTagEnumId1")
    @Excel(name = "客户标签")
    @ExcelProperty(value = "客户标签")
    @TableField(value = "customer_tag_enum_customer_tag_enum_id_1")

    private Integer customerTagEnumCustomerTagEnumId1;
    @JsonProperty(value = "wechatId")
    @Excel(name = "微信号")
    @ExcelProperty(value = "微信号")
    @TableField(value = "wechat_id")

    private String wechatId;
    @JsonProperty(value = "wechatDataEncrypted")
    @Excel(name = "加密微信数据")
    @ExcelProperty(value = "加密微信数据")
    @TableField(value = "wechat_data_encrypted")

    private String wechatDataEncrypted;
    @JsonProperty(value = "source")
    @Excel(name = "客户来源")
    @ExcelProperty(value = "客户来源")
    @TableField(value = "source")

    private String source;


}





