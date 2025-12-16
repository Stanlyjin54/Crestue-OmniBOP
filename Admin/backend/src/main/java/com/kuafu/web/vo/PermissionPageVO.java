package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>权限-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PermissionPageVO extends PageRequest {

    @JsonProperty(value = "permissionId")
    private Integer permissionId;
    @JsonProperty(value = "permissionName")
    private String permissionName;
    @JsonProperty(value = "permissionDesc")
    private String permissionDesc;
    @JsonProperty(value = "featureCode")
    private String featureCode;

}





