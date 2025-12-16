# 阿里云函数计算(FC)部署配置 - Application后端

## 1. 函数计算配置

### 函数基本信息
- 函数名称: application-backend
- 运行时: Java 17
- 函数入口: com.kuafuweb.app.Application::handleRequest
- 内存规格: 1024MB (可根据需要调整)
- 超时时间: 60秒

### 环境变量配置
```bash
# 服务端口
PORT=6789

# 数据库配置
DB_TYPE=mysql
MYSQL_HOST=your-rds-host.mysql.rds.aliyuncs.com
MYSQL_PORT=3306
MYSQL_DATABASE=your-database
MYSQL_USERNAME=your-username
MYSQL_PASSWORD=your-password

# Redis配置
REDIS_HOST=your-redis-host.redis.rds.aliyuncs.com
REDIS_PORT=6379
REDIS_PASSWORD=your-redis-password
REDIS_DATABASE=3

# 缓存配置
CACHE_TYPE=redis

# 文件上传路径
UPLOAD_PATH=/tmp/upload

# 短信配置
SMS_DEBUG=true
CODE_TIMEOUT=5

# 微信支付配置
WX_PAY_BACK_URL=https://your-fc-url.com/pay/callback/wechat
WX_MCH_SERIAL_NO=your-serial-no
WX_APP_ID=your-app-id
WX_MP_APP_ID=your-mp-app-id
WX_MCH_ID=your-mch-id
WX_API_V3_KEY=your-api-v3-key
WX_PRIVATE_KEY=your-private-key
WX_IS_TEST=1
WX_TEST_AMOUNT=0.01
WX_ENABLE=true
MOCK_ENABLE=true
WX_ORDER_PRE_KEY=000000-

# 支付宝支付配置
ALIPAY_ENABLE=true
ALIPAY_APP_ID=your-alipay-app-id
ALIPAY_PRIVATE_KEY=your-alipay-private-key
ALIPAY_PUBLIC_KEY=your-alipay-public-key
ALIPAY_ALIPAY_PUBLIC_KEY=your-alipay-alipay-public-key
ALIPAY_SERVER_URL=https://openapi.alipay.com/gateway.do
ALIPAY_FORMAT=json
ALIPAY_CHARSET=utf-8
ALIPAY_SIGN_TYPE=RSA2
ALIPAY_PAY_BACK_URL=https://your-fc-url.com/pay/callback/alipay
ALIPAY_IS_TEST=1
ALIPAY_TEST_AMOUNT=0.01

# LLM配置
LLM_ENABLE=false
VECTOR_PROVIDER=ali
VECTOR_COLLECT_NAME=test
VECTOR_KEY=your-vector-key
EMBEDDING_PROVIDER=ali
EMBEDDING_KEY=your-embedding-key
CHAT_PROVIDER=dify
CHAT_DIFY_KEY=your-dify-key
CHAT_DIDY_API=your-dify-api
CHAT_DEFAULT_PROMPT=你是一名小助手

# 消息配置
MESSAGE_ENABLE=false
MESSAGE_BASE_URL=your-message-url
MESSAGE_APP_ID=your-app-id

# 存储配置
STORAGE_TYPE=local
S3_ACCESS_KEY=your-s3-key
S3_SECRET_KEY=your-s3-secret
S3_ENDPOINT=https://obs.cn-east-3.myhuaweicloud.com
S3_DOMAIN=https://your-domain.obs.cn-east-3.myhuaweicloud.com
S3_REGION=cn-east-3
S3_BUCKET_NAME=your-bucket-name

# OCR配置
OCR_TESSDATA=tessdata

# 图片搜索API配置
IMAGE_SEARCH_ENABLE=false
IMAGE_SEARCH_KEY=your-image-search-key
IMAGE_SEARCH_URL=your-image-search-url
IMAGE_SEARCH_TIMEOUT=30000
```

## 2. 部署步骤

### 2.1 打包应用
```bash
cd Application/backend
mvn clean package -DskipTests
```

### 2.2 创建函数计算服务
1. 登录阿里云函数计算控制台
2. 创建服务，命名为 `crm-service` (与Admin后端共享同一服务)
3. 创建函数，选择"自定义运行时"
4. 运行时选择 Java 17

### 2.3 上传代码
1. 将 `target/app-0.0.1-SNAPSHOT.jar` 上传
2. 创建 `bootstrap` 文件，内容如下：
```bash
#!/bin/bash
java -jar /code/app-0.0.1-SNAPSHOT.jar
```

### 2.4 配置触发器
1. 添加 HTTP 触发器
2. 请求方法: ANY
3. 认证方式: 匿名 (或根据需要选择其他方式)

## 3. 代码适配

### 3.1 创建FC适配器类
需要在项目中创建一个FC适配器类，用于处理FC的请求格式：

```java
package com.kuafuweb.app.fc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ApplicationFcAdapter {
    private static ConfigurableApplicationContext context;

    static {
        try {
            // 启动Spring Boot应用
            context = SpringApplication.run(Application.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        // 处理FC请求
        JSONObject request = parseRequest(inputStream);
        String path = request.getString("path");
        String method = request.getString("httpMethod");
        JSONObject headers = request.getJSONObject("headers");
        JSONObject body = request.getJSONObject("body");
        
        // 构建Spring MVC请求
        HttpServletRequest servletRequest = createMockRequest(path, method, headers, body);
        HttpServletResponse servletResponse = createMockResponse();
        
        // 处理请求
        try {
            // 这里需要根据实际的Spring MVC Dispatcher来处理请求
            // 简化示例，实际实现会更复杂
            ResponseEntity<String> response = handleRequest(servletRequest);
            
            // 构建FC响应
            JSONObject fcResponse = new JSONObject();
            fcResponse.put("statusCode", response.getStatusCodeValue());
            fcResponse.put("body", response.getBody());
            
            outputStream.write(fcResponse.toJSONString().getBytes());
        } catch (Exception e) {
            // 错误处理
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("statusCode", 500);
            errorResponse.put("body", "Internal Server Error");
            
            outputStream.write(errorResponse.toJSONString().getBytes());
        }
    }
    
    private static JSONObject parseRequest(InputStream inputStream) {
        // 解析FC请求格式
        // 实际实现需要根据FC的具体请求格式来解析
        return new JSONObject();
    }
    
    private static HttpServletRequest createMockRequest(String path, String method, JSONObject headers, JSONObject body) {
        // 创建模拟的HttpServletRequest
        // 实际实现会更复杂
        return null;
    }
    
    private static HttpServletResponse createMockResponse() {
        // 创建模拟的HttpServletResponse
        // 实际实现会更复杂
        return null;
    }
    
    private static ResponseEntity<String> handleRequest(HttpServletRequest request) {
        // 使用Spring MVC的DispatcherServlet处理请求
        // 实际实现会更复杂
        return ResponseEntity.ok("Hello from FC");
    }
}
```

### 3.2 修改启动类
可能需要修改启动类以适应FC环境：

```java
package com.kuafuweb.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
```

## 4. 支付回调处理注意事项

由于FC的特殊性，支付回调处理需要特别注意：

1. **回调URL配置**: 确保微信支付和支付宝的回调URL指向FC函数的HTTP触发器地址
2. **网络连接**: FC可能无法直接访问外部网络，需要配置VPC和NAT网关
3. **IP白名单**: 微信支付可能需要配置IP白名单，FC的出口IP可能不固定

## 5. 文件存储处理

FC是临时的，不适合存储持久化文件：

1. **上传文件**: 建议直接上传到阿里云OSS
2. **临时文件**: 可以使用FC提供的临时目录 `/tmp`
3. **静态资源**: 建议使用CDN加速

## 6. 测试部署

部署完成后，可以通过以下方式测试：
1. 在FC控制台测试
2. 使用Postman等工具测试API
3. 测试支付回调功能
4. 配置自定义域名后通过浏览器测试

## 7. 成本估算

FC计费方式:
- 调用次数: 0.000016元/万次
- 执行时间: 0.000011元/GB*秒
- 公网流量: 0.5元/GB

测试阶段预估成本极低，月费用可能在几元以内。

## 8. 与Admin后端的区别

Application后端相比Admin后端有以下特殊配置：
1. 支付功能: 需要配置微信支付和支付宝回调
2. 短信功能: 需要配置阿里云短信服务
3. OCR功能: 需要配置OCR相关参数
4. 图片搜索: 需要配置图片搜索API

这些功能在FC环境中可能需要额外的网络配置或权限设置。