package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

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
public class PermissionLogVO  {

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





