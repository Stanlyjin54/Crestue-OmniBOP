package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>用户角色关联</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleVO  {

     @JsonProperty(value = "userRoleId")
    private Integer userRoleId;
     @JsonProperty(value = "userInfoId")
    private Integer userInfoId;
     @JsonProperty(value = "roleId")
    private Integer roleId;


}





