package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>权限</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionVO  {

     @JsonProperty(value = "permissionId")
    private Integer permissionId;
     @JsonProperty(value = "permissionName")
    private String permissionName;
     @JsonProperty(value = "permissionDesc")
    private String permissionDesc;
     @JsonProperty(value = "featureCode")
    private String featureCode;


}





