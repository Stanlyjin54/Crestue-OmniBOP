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
 * <p>权限变更日志</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionLogVO  extends BaseEntity {

    @JsonProperty("permissionLogId")
    private Integer permissionLogId;
    @JsonProperty("userInfoId")
    private Integer userInfoId;
    @JsonProperty("actionType")
    private String actionType;
    @JsonProperty("targetObject")
    private String targetObject;
    @JsonProperty("changeContent")
    private String changeContent;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("actionTime")
    private Date actionTime;




}
