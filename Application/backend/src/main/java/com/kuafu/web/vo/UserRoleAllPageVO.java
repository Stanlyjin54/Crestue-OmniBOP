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
 * <p>用户角色关联-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRoleAllPageVO extends BaseEntity {

@JsonProperty("roleName")
    private String roleName;
@JsonProperty("userRoleId")
    private Integer userRoleId;
@JsonProperty("password")
    private String password;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("registrationDate")
    private Date registrationDate;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("createTime")
    private Date createTime;
@JsonProperty("roleId")
    private Integer roleId;
@JsonProperty("phoneNumber")
    private String phoneNumber;
@JsonProperty("roleDesc")
    private String roleDesc;
@JsonProperty("userInfoId")
    private Integer userInfoId;
@JsonProperty("username")
    private String username;

}
