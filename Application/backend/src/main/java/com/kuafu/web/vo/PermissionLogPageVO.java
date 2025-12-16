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
 * <p>权限变更日志-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PermissionLogPageVO extends BaseEntity {

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
