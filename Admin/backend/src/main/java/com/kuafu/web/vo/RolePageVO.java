package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>角色-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RolePageVO extends PageRequest {

    @JsonProperty(value = "roleId")
    private Integer roleId;
    @JsonProperty(value = "roleName")
    private String roleName;
    @JsonProperty(value = "roleDesc")
    private String roleDesc;
    @JsonProperty(value = "createTime")
    private Date createTime;

}





