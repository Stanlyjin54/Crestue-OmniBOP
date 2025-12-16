package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>广告推荐-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdRecommendInfoPageVO extends PageRequest {

    @JsonProperty(value = "adRecommendInfoId")
    private Integer adRecommendInfoId;
    @JsonProperty(value = "adTitle")
    private String adTitle;
    @JsonProperty(value = "adImageResourceKey")
    private String adImageResourceKey;
    @JsonProperty(value = "adUrl")
    private String adUrl;
    @JsonProperty(value = "cpcFee")
    private Double cpcFee;

}





