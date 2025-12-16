package com.kuafu.web.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.domin.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.kuafu.common.entity.BaseEntity;
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
public class FissionToolPageVO extends BaseEntity {

@JsonProperty("fissionToolId")
    private Integer fissionToolId;
@JsonProperty("toolType")
    private String toolType;
@JsonProperty("title")
    private String title;
@JsonProperty("config")
    private String config;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("startDate")
    private Date startDate;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("endDate")
    private Date endDate;


}
