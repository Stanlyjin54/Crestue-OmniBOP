package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>广告推荐</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdRecommendInfoVO  {

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

    @JsonProperty("adImage")
    private List<StaticResource> adImage;

}





