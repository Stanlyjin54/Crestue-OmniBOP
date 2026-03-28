package com.kuafu.web.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.*;
import com.kuafu.common.entity.StaticResource;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kuafu.web.annotation.IsNotNullField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import com.kuafu.common.annotation.*;
/**
 * <p>  用户信息 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_info")
public class UserInfo  {
    @TableId(value = "user_info_id", type = IdType.AUTO)
    @JsonProperty("user_info_id")
    
    
    
    private Integer userInfoId;
    @JsonProperty("phone_number")
    @IsNotNullField(description = "手机号")
    
    
    @TableField("phone_number")
    private String phoneNumber;
    @JsonProperty("username")
    @IsNotNullField(description = "用户名")
    
    
    @TableField("username")
    private String username;
    @JsonProperty("password")
    @IsNotNullField(description = "密码")
    
    
    @TableField("password")
    private String password;
    @JsonProperty("registration_date")
    
    
    @TableField("registration_date")
    private Date registrationDate;

    @JsonProperty("email")
    @TableField("email")
    private String email;

    @JsonProperty("role")
    @TableField("role")
    private String role;

    /**
     * 用户类型：consumer-消费者(B2C), operator-经营者(Application)
     */
    @JsonProperty("user_type")
    @TableField("user_type")
    private String userType;



@TableField(exist = false)
@JsonProperty("avatar")
private List<StaticResource> avatar;



}
