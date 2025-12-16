# 阿里云函数计算(FC)快速部署脚本

## 1. 安装阿里云CLI

首先需要安装阿里云CLI工具：

```bash
# Windows (使用PowerShell)
Invoke-WebRequest -Uri "https://aliyuncli.alicdn.com/aliyun-cli-windows-latest-amd64.zip" -OutFile "aliyun-cli.zip"
Expand-Archive -Path "aliyun-cli.zip" -DestinationPath "."
# 将aliyun.exe添加到PATH环境变量

# macOS
brew install aliyun-cli

# Linux
curl -fsSL https://aliyuncli.alicdn.com/install.sh | bash
```

## 2. 配置阿里云CLI

```bash
# 配置访问凭证
aliyun configure
# 输入Access Key ID
# 输入Access Key Secret
# 输入默认区域ID (例如: cn-hangzhou)
# 输入默认输出格式 (json)
```

## 3. 创建FC部署脚本

创建 `deploy_fc.ps1` (PowerShell脚本) 或 `deploy_fc.sh` (Bash脚本):

```powershell
# deploy_fc.ps1 - Windows PowerShell版本

param(
    [Parameter(Mandatory=$true)]
    [string]$Region,
    
    [Parameter(Mandatory=$true)]
    [string]$ServiceName,
    
    [Parameter(Mandatory=$true)]
    [string]$ProjectPath,
    
    [Parameter(Mandatory=$true)]
    [string]$BackendType,  # "admin" 或 "application"
    
    [Parameter(Mandatory=$false)]
    [string]$Profile = "default"
)

# 设置变量
$FunctionName = "$($BackendType)-backend"
$JarPath = "$ProjectPath\target\app-0.0.1-SNAPSHOT.jar"
$Description = "$($BackendType) backend service for CRM system"

# 检查JAR文件是否存在
if (-not (Test-Path $JarPath)) {
    Write-Error "JAR文件不存在: $JarPath"
    Write-Host "请先运行 'mvn clean package -DskipTests' 构建项目"
    exit 1
}

# 创建临时目录
$tempDir = New-TemporaryFile | %{ rm $_; mkdir $_ }

# 复制JAR文件到临时目录
Copy-Item $JarPath "$tempDir\app.jar"

# 创建bootstrap文件
$bootstrapContent = @"
#!/bin/bash
java -jar /code/app.jar
"@
Set-Content -Path "$tempDir\bootstrap" -Value $bootstrapContent

# 创建部署包
$zipPath = "$tempDir\deployment.zip"
Compress-Archive -Path "$tempDir\*" -DestinationPath $zipPath

# 创建FC服务
Write-Host "创建FC服务: $ServiceName"
aliyun fc POST /services/$ServiceName --region $Region --profile $Profile --body "{}" --quiet

# 创建函数
$functionConfig = @{
    functionName = $FunctionName
    description = $Description
    runtime = "java17"
    handler = "com.kuafuweb.app.Application::handleRequest"
    memorySize = 1024
    timeout = 60
    environmentVariables = @{
        PORT = if ($BackendType -eq "admin") { "8080" } else { "6789" }
        DB_TYPE = "mysql"
        # 添加其他必要的环境变量
    }
} | ConvertTo-Json -Depth 10

Write-Host "创建函数: $FunctionName"
aliyun fc POST /services/$ServiceName/functions --region $Region --profile $Profile --body $functionConfig --quiet

# 上传代码
Write-Host "上传代码包"
aliyun fc PUT /services/$ServiceName/functions/$FunctionName/code --region $Region --profile $Profile --body "{ \"code\": { \"zipFile\": \"$(ConvertTo-Base64 -Path $zipPath)\" } }" --quiet

# 创建HTTP触发器
$triggerConfig = @{
    triggerName = "http-trigger"
    triggerType = "http"
    triggerConfig = @{
        authType = "anonymous"
        methods = @("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "PATCH")
    }
} | ConvertTo-Json -Depth 10

Write-Host "创建HTTP触发器"
aliyun fc POST /services/$ServiceName/functions/$FunctionName/triggers --region $Region --profile $Profile --body $triggerConfig --quiet

# 获取函数信息
$functionInfo = aliyun fc GET /services/$ServiceName/functions/$FunctionName --region $Region --profile $Profile | ConvertFrom-Json
$triggerUrl = $functionInfo.triggers[0].url

Write-Host "部署完成!"
Write-Host "函数URL: $triggerUrl"
Write-Host "请记得配置环境变量，特别是数据库和Redis连接信息"

# 清理临时文件
Remove-Item -Recurse -Force $tempDir
```

```bash
# deploy_fc.sh - Linux/macOS版本

#!/bin/bash

# 参数检查
if [ "$#" -lt 4 ]; then
    echo "用法: $0 <Region> <ServiceName> <ProjectPath> <BackendType> [Profile]"
    echo "示例: $0 cn-hangzhou crm-service /path/to/project admin"
    exit 1
fi

Region=$1
ServiceName=$2
ProjectPath=$3
BackendType=$4
Profile=${5:-default}

# 设置变量
FunctionName="${BackendType}-backend"
JarPath="${ProjectPath}/target/app-0.0.1-SNAPSHOT.jar"
Description="${BackendType} backend service for CRM system"

# 检查JAR文件是否存在
if [ ! -f "$JarPath" ]; then
    echo "错误: JAR文件不存在: $JarPath"
    echo "请先运行 'mvn clean package -DskipTests' 构建项目"
    exit 1
fi

# 创建临时目录
temp_dir=$(mktemp -d)

# 复制JAR文件到临时目录
cp "$JarPath" "$temp_dir/app.jar"

# 创建bootstrap文件
cat > "$temp_dir/bootstrap" << EOF
#!/bin/bash
java -jar /code/app.jar
EOF

chmod +x "$temp_dir/bootstrap"

# 创建部署包
zip_path="${temp_dir}/deployment.zip"
cd "$temp_dir"
zip -r "$zip_path" ./*
cd - > /dev/null

# 创建FC服务
echo "创建FC服务: $ServiceName"
aliyun fc POST "/services/$ServiceName" --region "$Region" --profile "$Profile" --body "{}" --quiet

# 创建函数
function_config=$(cat << EOF
{
    "functionName": "$FunctionName",
    "description": "$Description",
    "runtime": "java17",
    "handler": "com.kuafuweb.app.Application::handleRequest",
    "memorySize": 1024,
    "timeout": 60,
    "environmentVariables": {
        "PORT": "$([ "$BackendType" = "admin" ] && echo "8080" || echo "6789")",
        "DB_TYPE": "mysql"
    }
}
EOF
)

echo "创建函数: $FunctionName"
aliyun fc POST "/services/$ServiceName/functions" --region "$Region" --profile "$Profile" --body "$function_config" --quiet

# 上传代码
echo "上传代码包"
code_base64=$(base64 -i "$zip_path")
aliyun fc PUT "/services/$ServiceName/functions/$FunctionName/code" --region "$Region" --profile "$Profile" --body "{ \"code\": { \"zipFile\": \"$code_base64\" } }" --quiet

# 创建HTTP触发器
trigger_config=$(cat << EOF
{
    "triggerName": "http-trigger",
    "triggerType": "http",
    "triggerConfig": {
        "authType": "anonymous",
        "methods": ["GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "PATCH"]
    }
}
EOF
)

echo "创建HTTP触发器"
aliyun fc POST "/services/$ServiceName/functions/$FunctionName/triggers" --region "$Region" --profile "$Profile" --body "$trigger_config" --quiet

# 获取函数信息
function_info=$(aliyun fc GET "/services/$ServiceName/functions/$FunctionName" --region "$Region" --profile "$Profile")
trigger_url=$(echo "$function_info" | python3 -c "import sys, json; print(json.load(sys.stdin)['triggers'][0]['url'])")

echo "部署完成!"
echo "函数URL: $trigger_url"
echo "请记得配置环境变量，特别是数据库和Redis连接信息"

# 清理临时文件
rm -rf "$temp_dir"
```

## 4. 使用脚本部署

### Windows PowerShell
```powershell
# 部署Admin后端
.\deploy_fc.ps1 -Region "cn-hangzhou" -ServiceName "crm-service" -ProjectPath "d:\Crestue_scrm\Admin\backend" -BackendType "admin"

# 部署Application后端
.\deploy_fc.ps1 -Region "cn-hangzhou" -ServiceName "crm-service" -ProjectPath "d:\Crestue_scrm\Application\backend" -BackendType "application"
```

### Linux/macOS
```bash
# 部署Admin后端
chmod +x deploy_fc.sh
./deploy_fc.sh cn-hangzhou crm-service /path/to/Admin/backend admin

# 部署Application后端
./deploy_fc.sh cn-hangzhou crm-service /path/to/Application/backend application
```

## 5. 配置环境变量

部署完成后，需要在FC控制台配置以下环境变量：

### 必要的环境变量
```bash
# 数据库配置
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
```

### Application后端额外的环境变量
```bash
# 微信支付配置
WX_PAY_BACK_URL=https://your-fc-url.com/pay/callback/wechat
WX_MCH_SERIAL_NO=your-serial-no
WX_APP_ID=your-app-id
WX_MCH_ID=your-mch-id
WX_API_V3_KEY=your-api-v3-key
WX_PRIVATE_KEY=your-private-key

# 支付宝支付配置
ALIPAY_APP_ID=your-alipay-app-id
ALIPAY_PRIVATE_KEY=your-alipay-private-key
ALIPAY_PUBLIC_KEY=your-alipay-public-key
ALIPAY_SERVER_URL=https://openapi.alipay.com/gateway.do
```

## 6. 测试部署

部署完成后，可以通过以下方式测试：

```bash
# 获取函数信息
aliyun fc GET /services/crm-service/functions/admin-backend --region cn-hangzhou

# 测试函数
curl -X GET "https://your-fc-url.com/api/health"
```

## 7. 常见问题

### 7.1 冷启动问题
FC存在冷启动问题，可以通过以下方式缓解：
1. 增加内存规格
2. 设置预留实例
3. 使用定时触发器定期调用函数

### 7.2 网络连接问题
如果需要访问外部网络（如支付回调），需要：
1. 配置VPC
2. 配置NAT网关
3. 设置安全组规则

### 7.3 文件存储问题
FC是临时的，不适合存储持久化文件：
1. 使用阿里云OSS存储文件
2. 使用FC提供的临时目录 `/tmp` 存储临时文件

## 8. 成本优化

1. **合理设置内存**: 根据实际需求调整内存规格
2. **使用预留实例**: 对于高频访问的函数，使用预留实例可以降低冷启动延迟
3. **优化代码**: 减少函数执行时间，降低成本
4. **使用按量付费**: 测试阶段使用按量付费，生产环境可以考虑包年包月

## 9. 监控和日志

1. **查看日志**: 在FC控制台查看函数执行日志
2. **监控指标**: 设置云监控告警，监控函数执行情况
3. **错误处理**: 配置死信队列处理函数执行失败的情况

## 10. 自动化部署

可以将部署脚本集成到CI/CD流水线中，实现自动化部署：

```yaml
# GitHub Actions 示例
name: Deploy to FC

on:
  push:
    branches: [ main ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    
    - name: Setup Java
      uses: actions/setup-java@v2
      with:
        java-version: '8'
        distribution: 'adopt'
    
    - name: Build with Maven
      run: mvn clean package -DskipTests
    
    - name: Setup Aliyun CLI
      run: |
        curl -fsSL https://aliyuncli.alicdn.com/install.sh | bash
        aliyun configure --mode AK --region cn-hangzhou --access-key-id ${{ secrets.ALIYUN_ACCESS_KEY_ID }} --access-key-secret ${{ secrets.ALIYUN_ACCESS_KEY_SECRET }}
    
    - name: Deploy to FC
      run: ./deploy_fc.sh cn-hangzhou crm-service ${{ github.workspace }}/backend admin
```

这样，每次代码更新后，就会自动构建并部署到FC。