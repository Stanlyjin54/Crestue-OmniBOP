package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>客户标签枚举-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerTagEnumPageVO extends PageRequest {

    @JsonProperty(value = "customerTagEnumId")
    private Integer customerTagEnumId;
    @JsonProperty(value = "tagName")
    private String tagName;
    @JsonProperty(value = "isAiGenerated")
    private Boolean isAiGenerated;

}





