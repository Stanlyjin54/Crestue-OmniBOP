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
 * <p>直播带货同步</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveStreamSyncVO  extends BaseEntity {

    @JsonProperty("liveStreamSyncId")
    private Integer liveStreamSyncId;
    @JsonProperty("orderInfoOrderInfoId1")
    private Integer orderInfoOrderInfoId1;
    @JsonProperty("platform")
    private String platform;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("syncTime")
    private Date syncTime;




}
