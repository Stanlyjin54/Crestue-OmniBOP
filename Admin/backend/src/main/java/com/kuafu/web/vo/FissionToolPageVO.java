package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>裂变工具-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FissionToolPageVO extends PageRequest {

    @JsonProperty(value = "fissionToolId")
    private Integer fissionToolId;
    @JsonProperty(value = "toolType")
    private String toolType;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "config")
    private String config;
    @JsonProperty(value = "startDate")
    private Date startDate;
    @JsonProperty(value = "endDate")
    private Date endDate;

}





