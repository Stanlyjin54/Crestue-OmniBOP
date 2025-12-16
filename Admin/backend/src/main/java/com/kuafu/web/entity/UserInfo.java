package com.kuafuweb.web.entity;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuafuweb.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;
import com.kuafuweb.common.annotation.Excel.*;
 import  com.kuafuweb.web.event.BaseLoginVo; 


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
public class UserInfo   extends BaseLoginVo   {
    @TableId(value = "user_info_id", type = IdType.AUTO)
    @JsonProperty(value = "userInfoId")
    @Excel(name = "主键")

    private Integer userInfoId;
    @JsonProperty(value = "phoneNumber")
    @Excel(name = "手机号")
    @ExcelProperty(value = "手机号")
    @TableField(value = "phone_number")

    private String phoneNumber;
    @JsonProperty(value = "username")
    @Excel(name = "用户名")
    @ExcelProperty(value = "用户名")
    @TableField(value = "username")

    private String username;
    @JsonProperty(value = "password")
    @Excel(name = "密码")
    @ExcelProperty(value = "密码")
    @TableField(value = "password")

    private String password;
    @JsonProperty(value = "avatarResourceKey")
    @Excel(name = "头像")
    @ExcelProperty(value = "头像")
    @TableField(value = "avatar_resource_key")

    private String avatarResourceKey;
    @JsonProperty(value = "registrationDate")
    @Excel(name = "注册时间")
    @ExcelProperty(value = "注册时间")
    @TableField(value = "registration_date")

    private Date registrationDate;

    @TableField(exist = false)
    private List<StaticResource> avatar;

}





