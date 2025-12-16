package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class PermissionLogPageVO extends PageRequest {

    @JsonProperty(value = "permissionLogId")
    private Integer permissionLogId;
    @JsonProperty(value = "userInfoId")
    private Integer userInfoId;
    @JsonProperty(value = "actionType")
    private String actionType;
    @JsonProperty(value = "targetObject")
    private String targetObject;
    @JsonProperty(value = "changeContent")
    private String changeContent;
    @JsonProperty(value = "actionTime")
    private Date actionTime;

}





