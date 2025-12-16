package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>角色权限关联</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionVO  {

     @JsonProperty(value = "rolePermissionId")
    private Integer rolePermissionId;
     @JsonProperty(value = "roleId")
    private Integer roleId;
     @JsonProperty(value = "permissionId")
    private Integer permissionId;


}





