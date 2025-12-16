package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>直播带货同步</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveStreamSyncVO  {

     @JsonProperty(value = "liveStreamSyncId")
    private Integer liveStreamSyncId;
     @JsonProperty(value = "orderInfoOrderInfoId1")
    private Integer orderInfoOrderInfoId1;
     @JsonProperty(value = "platform")
    private String platform;
     @JsonProperty(value = "syncTime")
    private Date syncTime;


}





