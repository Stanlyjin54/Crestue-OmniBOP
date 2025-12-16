package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.kuafu.common.entity.StaticResource;
import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.entity.BaseEntity;
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
public class AdRecommendInfoVO  extends BaseEntity {

    @JsonProperty("adRecommendInfoId")
    private Integer adRecommendInfoId;
    @JsonProperty("adTitle")
    private String adTitle;
    @JsonProperty("adUrl")
    private String adUrl;
    @JsonProperty("cpcFee")
    private Double cpcFee;


    private List<StaticResource> adImage;


}
