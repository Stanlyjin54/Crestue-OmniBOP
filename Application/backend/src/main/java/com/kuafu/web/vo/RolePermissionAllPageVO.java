package com.kuafu.web.vo;

import com.kuafu.common.domin.PageRequest;
import com.kuafu.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import com.kuafu.common.entity.StaticResource;

/**
 * <p>角色权限关联-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RolePermissionAllPageVO extends BaseEntity {

@JsonProperty("roleName")
    private String roleName;
@JsonProperty("permissionDesc")
    private String permissionDesc;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("createTime")
    private Date createTime;
@JsonProperty("roleId")
    private Integer roleId;
@JsonProperty("roleDesc")
    private String roleDesc;
@JsonProperty("permissionId")
    private Integer permissionId;
@JsonProperty("permissionName")
    private String permissionName;
@JsonProperty("featureCode")
    private String featureCode;
@JsonProperty("rolePermissionId")
    private Integer rolePermissionId;

}
