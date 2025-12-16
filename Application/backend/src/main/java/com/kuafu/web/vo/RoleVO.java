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
 * <p>角色</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleVO  extends BaseEntity {

    @JsonProperty("roleId")
    private Integer roleId;
    @JsonProperty("roleName")
    private String roleName;
    @JsonProperty("roleDesc")
    private String roleDesc;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("createTime")
    private Date createTime;




}
