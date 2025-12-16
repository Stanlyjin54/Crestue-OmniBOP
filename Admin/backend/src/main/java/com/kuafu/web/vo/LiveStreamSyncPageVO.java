package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>直播带货同步-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LiveStreamSyncPageVO extends PageRequest {

    @JsonProperty(value = "liveStreamSyncId")
    private Integer liveStreamSyncId;
    @JsonProperty(value = "orderInfoOrderInfoId1")
    private Integer orderInfoOrderInfoId1;
    @JsonProperty(value = "platform")
    private String platform;
    @JsonProperty(value = "syncTime")
    private Date syncTime;

}





