package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>角色</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleVO  {

     @JsonProperty(value = "roleId")
    private Integer roleId;
     @JsonProperty(value = "roleName")
    private String roleName;
     @JsonProperty(value = "roleDesc")
    private String roleDesc;
     @JsonProperty(value = "createTime")
    private Date createTime;


}





