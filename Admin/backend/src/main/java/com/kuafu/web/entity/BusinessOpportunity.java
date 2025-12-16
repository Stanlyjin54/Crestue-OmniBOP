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
 * <p>  商机线索 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("business_opportunity")
public class BusinessOpportunity    {
    @TableId(value = "business_opportunity_id", type = IdType.AUTO)
    @JsonProperty(value = "businessOpportunityId")
    @Excel(name = "主键")

    private Integer businessOpportunityId;
    @JsonProperty(value = "customerInfoCustomerInfoId1")
    @Excel(name = "客户ID")
    @ExcelProperty(value = "客户ID")
    @TableField(value = "customer_info_customer_info_id_1")

    private Integer customerInfoCustomerInfoId1;
    @JsonProperty(value = "opportunityName")
    @Excel(name = "商机名称")
    @ExcelProperty(value = "商机名称")
    @TableField(value = "opportunity_name")

    private String opportunityName;
    @JsonProperty(value = "status")
    @Excel(name = "商机状态")
    @ExcelProperty(value = "商机状态")
    @TableField(value = "status")

    private String status;
    @JsonProperty(value = "followUpSuggestion")
    @Excel(name = "AI跟进建议")
    @ExcelProperty(value = "AI跟进建议")
    @TableField(value = "follow_up_suggestion")

    private String followUpSuggestion;


}





