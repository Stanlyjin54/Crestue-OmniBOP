<template>
  <el-dialog
    v-model="dialogVisible"
    title="AI助手配置"
    width="800px"
    :close-on-click-modal="false"
  >
    <div class="config-container">
      <el-form
        ref="configFormRef"
        :model="configForm"
        :rules="configRules"
        label-width="120px"
      >
        <!-- 基础配置 -->
        <el-card shadow="never" class="config-section">
          <template #header>
            <span class="section-title">
              <el-icon><Setting /></el-icon>
              基础配置
            </span>
          </template>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="模型选择" prop="model">
                <el-select v-model="configForm.model" placeholder="请选择AI模型" style="width: 100%">
                  <el-option
                    v-for="model in availableModels"
                    :key="model.value"
                    :label="model.label"
                    :value="model.value"
                  >
                    <div class="model-option">
                      <span>{{ model.label }}</span>
                      <el-tag size="small" :type="model.type === 'premium' ? 'danger' : 'info'">
                        {{ model.type === 'premium' ? '高级' : '标准' }}
                      </el-tag>
                    </div>
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            
            <el-col :span="12">
              <el-form-item label="API版本" prop="apiVersion">
                <el-select v-model="configForm.apiVersion" placeholder="请选择API版本" style="width: 100%">
                  <el-option
                    v-for="version in apiVersions"
                    :key="version.value"
                    :label="version.label"
                    :value="version.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="温度参数" prop="temperature">
                <div class="slider-container">
                  <el-slider
                    v-model="configForm.temperature"
                    :min="0"
                    :max="2"
                    :step="0.1"
                    show-input
                    :show-input-controls="false"
                  />
                  <div class="slider-hint">
                    <span>精确</span>
                    <span>平衡</span>
                    <span>创意</span>
                  </div>
                </div>
              </el-form-item>
            </el-col>
            
            <el-col :span="12">
              <el-form-item label="最大令牌数" prop="maxTokens">
                <el-input-number
                  v-model="configForm.maxTokens"
                  :min="100"
                  :max="4000"
                  :step="100"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="响应超时" prop="timeout">
                <el-input-number
                  v-model="configForm.timeout"
                  :min="5"
                  :max="300"
                  :step="5"
                  style="width: 100%"
                >
                  <template #suffix>秒</template>
                </el-input-number>
              </el-form-item>
            </el-col>
            
            <el-col :span="12">
              <el-form-item label="并发限制" prop="maxConcurrent">
                <el-input-number
                  v-model="configForm.maxConcurrent"
                  :min="1"
                  :max="50"
                  :step="1"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 功能配置 -->
        <el-card shadow="never" class="config-section">
          <template #header>
            <span class="section-title">
              <el-icon><ChatDotRound /></el-icon>
              功能配置
            </span>
          </template>
          
          <el-form-item label="启用功能">
            <el-checkbox-group v-model="configForm.enabledFeatures">
              <el-checkbox
                v-for="feature in availableFeatures"
                :key="feature.key"
                :label="feature.key"
              >
                <div class="feature-option">
                  <span>{{ feature.name }}</span>
                  <el-tooltip :content="feature.description" placement="top">
                    <el-icon class="feature-info"><QuestionFilled /></el-icon>
                  </el-tooltip>
                </div>
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="文本生成长度">
                <el-slider
                  v-model="configForm.textGenerationLength"
                  :min="100"
                  :max="2000"
                  :step="50"
                  show-input
                  :show-input-controls="false"
                />
              </el-form-item>
            </el-col>
            
            <el-col :span="12">
              <el-form-item label="上下文窗口">
                <el-select v-model="configForm.contextWindow" placeholder="请选择上下文窗口" style="width: 100%">
                  <el-option
                    v-for="window in contextWindows"
                    :key="window.value"
                    :label="window.label"
                    :value="window.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 智能采购建议配置 -->
        <el-card shadow="never" class="config-section">
          <template #header>
            <span class="section-title">
              <el-icon><ShoppingCart /></el-icon>
              智能采购建议配置
            </span>
          </template>
          
          <el-form-item label="建议算法">
            <el-radio-group v-model="configForm.procurementAlgorithm">
              <el-radio
                v-for="algorithm in procurementAlgorithms"
                :key="algorithm.value"
                :label="algorithm.value"
              >
                {{ algorithm.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="价格敏感度">
                <el-slider
                  v-model="configForm.priceSensitivity"
                  :min="0"
                  :max="100"
                  :step="5"
                  show-input
                  :show-input-controls="false"
                />
              </el-form-item>
            </el-col>
            
            <el-col :span="12">
              <el-form-item label="质量权重">
                <el-slider
                  v-model="configForm.qualityWeight"
                  :min="0"
                  :max="100"
                  :step="5"
                  show-input
                  :show-input-controls="false"
                />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="供应商筛选">
            <el-checkbox-group v-model="configForm.supplierFilters">
              <el-checkbox
                v-for="filter in supplierFilterOptions"
                :key="filter.value"
                :label="filter.value"
              >
                {{ filter.label }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-card>

        <!-- 智能分析配置 -->
        <el-card shadow="never" class="config-section">
          <template #header>
            <span class="section-title">
              <el-icon><DataAnalysis /></el-icon>
              智能分析配置
            </span>
          </template>
          
          <el-form-item label="分析维度">
            <el-checkbox-group v-model="configForm.analysisDimensions">
              <el-checkbox
                v-for="dimension in analysisDimensionOptions"
                :key="dimension.value"
                :label="dimension.value"
              >
                {{ dimension.label }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="分析周期">
                <el-select v-model="configForm.analysisPeriod" placeholder="请选择分析周期" style="width: 100%">
                  <el-option
                    v-for="period in analysisPeriodOptions"
                    :key="period.value"
                    :label="period.label"
                    :value="period.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            
            <el-col :span="12">
              <el-form-item label="报告模板">
                <el-select v-model="configForm.reportTemplate" placeholder="请选择报告模板" style="width: 100%">
                  <el-option
                    v-for="template in reportTemplateOptions"
                    :key="template.value"
                    :label="template.label"
                    :value="template.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 社媒营销配置 -->
        <el-card shadow="never" class="config-section">
          <template #header>
            <span class="section-title">
              <el-icon><Share /></el-icon>
              社媒营销配置
            </span>
          </template>
          
          <el-form-item label="营销平台">
            <el-checkbox-group v-model="configForm.marketingPlatforms">
              <el-checkbox
                v-for="platform in marketingPlatformOptions"
                :key="platform.value"
                :label="platform.value"
              >
                <div class="platform-option">
                  <span>{{ platform.name }}</span>
                  <el-tag size="small" :type="platform.status === 'connected' ? 'success' : 'warning'">
                    {{ platform.status === 'connected' ? '已连接' : '未连接' }}
                  </el-tag>
                </div>
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          
          <el-form-item label="内容策略">
            <el-select v-model="configForm.contentStrategy" placeholder="请选择内容策略" style="width: 100%">
              <el-option
                v-for="strategy in contentStrategyOptions"
                :key="strategy.value"
                :label="strategy.label"
                :value="strategy.value"
              />
            </el-select>
          </el-form-item>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="发布频率">
                <el-input-number
                  v-model="configForm.publishFrequency"
                  :min="1"
                  :max="24"
                  :step="1"
                  style="width: 100%"
                >
                  <template #suffix>次/天</template>
                </el-input-number>
              </el-form-item>
            </el-col>
            
            <el-col :span="12">
              <el-form-item label="互动阈值">
                <el-input-number
                  v-model="configForm.engagementThreshold"
                  :min="0"
                  :max="100"
                  :step="5"
                  style="width: 100%"
                >
                  <template #suffix>%</template>
                </el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 支付优化配置 -->
        <el-card shadow="never" class="config-section">
          <template #header>
            <span class="section-title">
              <el-icon><CreditCard /></el-icon>
              支付优化配置
            </span>
          </template>
          
          <el-form-item label="支付场景">
            <el-checkbox-group v-model="configForm.paymentScenarios">
              <el-checkbox
                v-for="scenario in paymentScenarioOptions"
                :key="scenario.value"
                :label="scenario.value"
              >
                {{ scenario.label }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="成功率权重">
                <el-slider
                  v-model="configForm.successRateWeight"
                  :min="0"
                  :max="100"
                  :step="5"
                  show-input
                  :show-input-controls="false"
                />
              </el-form-item>
            </el-col>
            
            <el-col :span="12">
              <el-form-item label="成本权重">
                <el-slider
                  v-model="configForm.costWeight"
                  :min="0"
                  :max="100"
                  :step="5"
                  show-input
                  :show-input-controls="false"
                />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="支付渠道优先级">
            <el-transfer
              v-model="configForm.paymentChannelPriority"
              :data="paymentChannelOptions"
              :titles="['可选渠道', '优先级渠道']"
            />
          </el-form-item>
        </el-card>

        <!-- 性能监控配置 -->
        <el-card shadow="never" class="config-section">
          <template #header>
            <span class="section-title">
              <el-icon><Monitor /></el-icon>
              性能监控配置
            </span>
          </template>
          
          <el-form-item label="监控指标">
            <el-checkbox-group v-model="configForm.monitoringMetrics">
              <el-checkbox
                v-for="metric in monitoringMetricOptions"
                :key="metric.value"
                :label="metric.value"
              >
                {{ metric.label }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="监控频率">
                <el-select v-model="configForm.monitoringFrequency" placeholder="请选择监控频率" style="width: 100%">
                  <el-option
                    v-for="frequency in monitoringFrequencyOptions"
                    :key="frequency.value"
                    :label="frequency.label"
                    :value="frequency.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            
            <el-col :span="12">
              <el-form-item label="告警阈值">
                <el-input-number
                  v-model="configForm.alertThreshold"
                  :min="50"
                  :max="99"
                  :step="1"
                  style="width: 100%"
                >
                  <template #suffix>%</template>
                </el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="告警通知">
            <el-checkbox-group v-model="configForm.alertNotifications">
              <el-checkbox
                v-for="notification in alertNotificationOptions"
                :key="notification.value"
                :label="notification.value"
              >
                {{ notification.label }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-card>

        <!-- 高级配置 -->
        <el-card shadow="never" class="config-section">
          <template #header>
            <span class="section-title">
              <el-icon><Tools /></el-icon>
              高级配置
            </span>
          </template>
          
          <el-form-item label="日志级别">
            <el-select v-model="configForm.logLevel" placeholder="请选择日志级别" style="width: 100%">
              <el-option
                v-for="level in logLevelOptions"
                :key="level.value"
                :label="level.label"
                :value="level.value"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="缓存策略">
            <el-select v-model="configForm.cacheStrategy" placeholder="请选择缓存策略" style="width: 100%">
              <el-option
                v-for="strategy in cacheStrategyOptions"
                :key="strategy.value"
                :label="strategy.label"
                :value="strategy.value"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="负载均衡">
            <el-switch
              v-model="configForm.enableLoadBalancing"
              active-text="启用"
              inactive-text="禁用"
            />
          </el-form-item>
          
          <el-form-item label="安全设置">
            <el-checkbox v-model="configForm.enableEncryption">启用数据加密</el-checkbox>
            <el-checkbox v-model="configForm.enableAuditLog">启用审计日志</el-checkbox>
            <el-checkbox v-model="configForm.enableRateLimit">启用请求限制</el-checkbox>
          </el-form-item>
        </el-card>
      </el-form>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleReset">重置</el-button>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button 
          type="primary" 
          :loading="loading"
          @click="handleSave"
        >
          保存配置
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Setting, ChatDotRound, QuestionFilled, ShoppingCart, DataAnalysis,
  Share, CreditCard, Monitor, Tools
} from '@element-plus/icons-vue'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  config: {
    type: Object,
    default: () => ({})
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'updated'])

// 响应式数据
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const configFormRef = ref()
const loading = ref(false)

// 表单数据
const configForm = ref({
  model: 'gpt-3.5-turbo',
  apiVersion: 'v1',
  temperature: 0.7,
  maxTokens: 1000,
  timeout: 30,
  maxConcurrent: 10,
  enabledFeatures: ['CHAT', 'TEXT_GENERATION', 'TRANSLATION'],
  textGenerationLength: 500,
  contextWindow: '4096',
  procurementAlgorithm: 'cost_based',
  priceSensitivity: 50,
  qualityWeight: 80,
  supplierFilters: ['certified', 'high_rating'],
  analysisDimensions: ['performance', 'cost', 'quality'],
  analysisPeriod: 'weekly',
  reportTemplate: 'standard',
  marketingPlatforms: ['wechat', 'weibo'],
  contentStrategy: 'educational',
  publishFrequency: 3,
  engagementThreshold: 20,
  paymentScenarios: ['b2c', 'b2b'],
  successRateWeight: 70,
  costWeight: 30,
  paymentChannelPriority: ['alipay', 'wechat_pay'],
  monitoringMetrics: ['response_time', 'success_rate', 'error_rate'],
  monitoringFrequency: '5min',
  alertThreshold: 90,
  alertNotifications: ['email', 'webhook'],
  logLevel: 'info',
  cacheStrategy: 'redis',
  enableLoadBalancing: true,
  enableEncryption: true,
  enableAuditLog: true,
  enableRateLimit: true,
  ...props.config
})

// 表单验证规则
const configRules = {
  model: [
    { required: true, message: '请选择AI模型', trigger: 'change' }
  ],
  temperature: [
    { type: 'number', min: 0, max: 2, message: '温度参数应在0-2之间', trigger: 'blur' }
  ],
  maxTokens: [
    { type: 'number', min: 100, max: 4000, message: '最大令牌数应在100-4000之间', trigger: 'blur' }
  ],
  timeout: [
    { type: 'number', min: 5, max: 300, message: '响应超时应在5-300秒之间', trigger: 'blur' }
  ]
}

// 配置选项
const availableModels = [
  { label: 'GPT-3.5 Turbo', value: 'gpt-3.5-turbo', type: 'standard' },
  { label: 'GPT-4', value: 'gpt-4', type: 'premium' },
  { label: 'Claude-3', value: 'claude-3', type: 'premium' },
  { label: '文心一言', value: 'wenxin-yiyan', type: 'standard' },
  { label: '通义千问', value: 'tongyi-qianwen', type: 'standard' }
]

const apiVersions = [
  { label: 'v1', value: 'v1' },
  { label: 'v2', value: 'v2' },
  { label: 'beta', value: 'beta' }
]

const availableFeatures = [
  { key: 'CHAT', name: '聊天对话', description: '支持自然语言对话交互' },
  { key: 'TEXT_GENERATION', name: '文本生成', description: '自动生成各类文本内容' },
  { key: 'TRANSLATION', name: '翻译', description: '多语言翻译功能' },
  { key: 'SUMMARIZATION', name: '总结摘要', description: '自动生成内容摘要' },
  { key: 'ANALYSIS', name: '数据分析', description: '智能数据分析功能' },
  { key: 'RECOMMENDATION', name: '智能推荐', description: '个性化推荐系统' },
  { key: 'OCR', name: '文字识别', description: '图片文字识别功能' },
  { key: 'SENTIMENT', name: '情感分析', description: '文本情感分析功能' }
]

const contextWindows = [
  { label: '2K tokens', value: '2048' },
  { label: '4K tokens', value: '4096' },
  { label: '8K tokens', value: '8192' },
  { label: '16K tokens', value: '16384' }
]

const procurementAlgorithms = [
  { label: '基于成本', value: 'cost_based' },
  { label: '基于质量', value: 'quality_based' },
  { label: '综合评估', value: 'comprehensive' },
  { label: '机器学习', value: 'ml_based' }
]

const supplierFilterOptions = [
  { label: '认证供应商', value: 'certified' },
  { label: '高评分供应商', value: 'high_rating' },
  { label: '本地供应商', value: 'local' },
  { label: '长期合作', value: 'long_term' }
]

const analysisDimensionOptions = [
  { label: '性能分析', value: 'performance' },
  { label: '成本分析', value: 'cost' },
  { label: '质量分析', value: 'quality' },
  { label: '风险分析', value: 'risk' },
  { label: '趋势分析', value: 'trend' }
]

const analysisPeriodOptions = [
  { label: '每日', value: 'daily' },
  { label: '每周', value: 'weekly' },
  { label: '每月', value: 'monthly' },
  { label: '每季度', value: 'quarterly' }
]

const reportTemplateOptions = [
  { label: '标准模板', value: 'standard' },
  { label: '详细模板', value: 'detailed' },
  { label: '简洁模板', value: 'concise' },
  { label: '自定义模板', value: 'custom' }
]

const marketingPlatformOptions = [
  { name: '微信', value: 'wechat', status: 'connected' },
  { name: '微博', value: 'weibo', status: 'connected' },
  { name: '抖音', value: 'douyin', status: 'connected' },
  { name: '小红书', value: 'xiaohongshu', status: 'connected' },
  { name: 'LinkedIn', value: 'linkedin', status: 'disconnected' },
  { name: 'Twitter', value: 'twitter', status: 'disconnected' }
]

const contentStrategyOptions = [
  { label: '教育内容', value: 'educational' },
  { label: '娱乐内容', value: 'entertainment' },
  { label: '产品推广', value: 'promotional' },
  { label: '品牌建设', value: 'branding' }
]

const paymentScenarioOptions = [
  { label: 'B2C支付', value: 'b2c' },
  { label: 'B2B支付', value: 'b2b' },
  { label: '跨境支付', value: 'cross_border' },
  { label: '批量支付', value: 'batch' }
]

const paymentChannelOptions = [
  { key: 'alipay', label: '支付宝' },
  { key: 'wechat_pay', label: '微信支付' },
  { key: 'unionpay', label: '银联支付' },
  { key: 'paypal', label: 'PayPal' },
  { key: 'stripe', label: 'Stripe' }
]

const monitoringMetricOptions = [
  { label: '响应时间', value: 'response_time' },
  { label: '成功率', value: 'success_rate' },
  { label: '错误率', value: 'error_rate' },
  { label: '并发数', value: 'concurrent_requests' },
  { label: '内存使用', value: 'memory_usage' }
]

const monitoringFrequencyOptions = [
  { label: '1分钟', value: '1min' },
  { label: '5分钟', value: '5min' },
  { label: '15分钟', value: '15min' },
  { label: '1小时', value: '1hour' }
]

const alertNotificationOptions = [
  { label: '邮件通知', value: 'email' },
  { label: '短信通知', value: 'sms' },
  { label: 'Webhook', value: 'webhook' },
  { label: '钉钉群', value: 'dingtalk' }
]

const logLevelOptions = [
  { label: 'DEBUG', value: 'debug' },
  { label: 'INFO', value: 'info' },
  { label: 'WARN', value: 'warn' },
  { label: 'ERROR', value: 'error' }
]

const cacheStrategyOptions = [
  { label: 'Redis缓存', value: 'redis' },
  { label: '内存缓存', value: 'memory' },
  { label: '文件缓存', value: 'file' },
  { label: '无缓存', value: 'none' }
]

// 方法
const handleSave = async () => {
  try {
    await configFormRef.value.validate()
    loading.value = true
    
    // 模拟保存配置
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success('配置保存成功')
    emit('updated', configForm.value)
    dialogVisible.value = false
  } catch (error) {
    ElMessage.error('配置保存失败')
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  configFormRef.value.resetFields()
}

// 监听配置变化
watch(() => props.config, (newConfig) => {
  Object.assign(configForm.value, newConfig)
}, { deep: true })
</script>

<style scoped>
.config-container {
  max-height: 600px;
  overflow-y: auto;
  padding: 0 10px;
}

.config-section {
  margin-bottom: 20px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: #303133;
}

.model-option,
.platform-option,
.feature-option {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.feature-option {
  gap: 8px;
}

.feature-info {
  color: #909399;
  cursor: help;
}

.slider-container {
  width: 100%;
}

.slider-hint {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

:deep(.el-transfer-panel) {
  width: 300px;
}

:deep(.el-transfer-panel__list) {
  height: 200px;
}
</style>