package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>客户信息</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfoVO  {

     @JsonProperty(value = "customerInfoId")
    private Integer customerInfoId;
     @JsonProperty(value = "customerName")
    private String customerName;
     @JsonProperty(value = "phone")
    private String phone;
     @JsonProperty(value = "email")
    private String email;
     @JsonProperty(value = "address")
    private String address;
     @JsonProperty(value = "rfmLevelEnumRfmLevelEnumId1")
    private Integer rfmLevelEnumRfmLevelEnumId1;
     @JsonProperty(value = "customerTagEnumCustomerTagEnumId1")
    private Integer customerTagEnumCustomerTagEnumId1;
     @JsonProperty(value = "wechatId")
    private String wechatId;
     @JsonProperty(value = "wechatDataEncrypted")
    private String wechatDataEncrypted;
     @JsonProperty(value = "source")
    private String source;


}





