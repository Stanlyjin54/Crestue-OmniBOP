package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>用户信息</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO  {

     @JsonProperty(value = "userInfoId")
    private Integer userInfoId;
     @JsonProperty(value = "phoneNumber")
    private String phoneNumber;
     @JsonProperty(value = "username")
    private String username;
     @JsonProperty(value = "password")
    private String password;
     @JsonProperty(value = "avatarResourceKey")
    private String avatarResourceKey;
     @JsonProperty(value = "registrationDate")
    private Date registrationDate;

    @JsonProperty("avatar")
    private List<StaticResource> avatar;

}





