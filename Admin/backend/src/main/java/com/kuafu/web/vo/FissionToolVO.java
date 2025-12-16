package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>裂变工具</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FissionToolVO  {

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





