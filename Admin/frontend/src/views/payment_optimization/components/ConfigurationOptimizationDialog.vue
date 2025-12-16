<template>
  <el-dialog
    v-model="dialogVisible"
    title="支付配置优化"
    width="900px"
    :before-close="handleClose"
  >
    <div class="optimization-config-content">
      <el-form 
        ref="configFormRef"
        :model="configForm"
        :rules="configRules"
        label-width="120px"
      >
        <!-- 支付方式优化 -->
        <div class="config-section">
          <h3>支付方式优化</h3>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="支付方式排序" prop="paymentOrderOptimization">
                <el-switch 
                  v-model="configForm.paymentOrderOptimization"
                  active-text="智能排序"
                  inactive-text="默认排序"
                />
                <div class="form-tip">
                  根据用户行为数据自动优化支付方式显示顺序
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="隐藏低效方式" prop="hideLowPerformance">
                <el-switch 
                  v-model="configForm.hideLowPerformance"
                  active-text="隐藏"
                  inactive-text="显示"
                />
                <div class="form-tip">
                  隐藏使用率低于5%的支付方式
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="智能推荐" prop="smartRecommendation">
                <el-switch 
                  v-model="configForm.smartRecommendation"
                  active-text="开启"
                  inactive-text="关闭"
                />
                <div class="form-tip">
                  基于用户历史偏好推荐支付方式
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="推荐阈值" prop="recommendationThreshold">
                <el-slider 
                  v-model="configForm.recommendationThreshold"
                  :min="1"
                  :max="10"
                  :step="1"
                  show-stops
                  show-input
                  :show-input-controls="false"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 支付方式配置 -->
          <el-form-item label="支付方式配置" prop="paymentMethods">
            <div class="payment-methods-config">
              <div 
                v-for="(method, index) in configForm.paymentMethods" 
                :key="method.id"
                class="method-config-item"
              >
                <el-row :gutter="16" align="middle">
                  <el-col :span="8">
                    <div class="method-info">
                      <el-icon class="method-icon">
                        <component :is="getPaymentIcon(method.id)" />
                      </el-icon>
                      <span class="method-name">{{ method.name }}</span>
                    </div>
                  </el-col>
                  <el-col :span="4">
                    <el-switch 
                      v-model="method.enabled"
                      size="small"
                    />
                  </el-col>
                  <el-col :span="6">
                    <el-select 
                      v-model="method.displayOrder" 
                      size="small"
                      style="width: 100%"
                    >
                      <el-option 
                        v-for="i in configForm.paymentMethods.length" 
                        :key="i" 
                        :label="`第${i}位`" 
                        :value="i"
                      />
                    </el-select>
                  </el-col>
                  <el-col :span="4">
                    <el-input-number 
                      v-model="method.priority" 
                      :min="1" 
                      :max="10"
                      size="small"
                      style="width: 100%"
                    />
                  </el-col>
                  <el-col :span="2">
                    <el-button 
                      type="danger" 
                      size="small" 
                      circle
                      @click="removePaymentMethod(index)"
                    >
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </el-col>
                </el-row>
              </div>
            </div>
            <el-button type="primary" @click="addPaymentMethod">
              添加支付方式
            </el-button>
          </el-form-item>
        </div>

        <!-- 表单优化 -->
        <div class="config-section">
          <h3>表单优化</h3>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="自动填充" prop="autoFill">
                <el-switch 
                  v-model="configForm.autoFill"
                  active-text="开启"
                  inactive-text="关闭"
                />
                <div class="form-tip">
                  自动填充用户已保存的支付信息
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="智能验证" prop="smartValidation">
                <el-switch 
                  v-model="configForm.smartValidation"
                  active-text="开启"
                  inactive-text="关闭"
                />
                <div class="form-tip">
                  实时验证字段格式，减少错误提交
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="字段最小化" prop="fieldMinimization">
                <el-switch 
                  v-model="configForm.fieldMinimization"
                  active-text="开启"
                  inactive-text="关闭"
                />
                <div class="form-tip">
                  减少非必要字段，提升填写效率
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="渐进式显示" prop="progressiveDisplay">
                <el-switch 
                  v-model="configForm.progressiveDisplay"
                  active-text="开启"
                  inactive-text="关闭"
                />
                <div class="form-tip">
                  根据选择动态显示相关字段
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 表单字段配置 -->
          <el-form-item label="字段配置" prop="formFields">
            <div class="form-fields-config">
              <el-table 
                :data="configForm.formFields" 
                border 
                size="small"
                style="width: 100%"
              >
                <el-table-column prop="name" label="字段名称" width="120">
                  <template #default="scope">
                    <el-input 
                      v-model="scope.row.name" 
                      size="small"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="type" label="字段类型" width="100">
                  <template #default="scope">
                    <el-select 
                      v-model="scope.row.type" 
                      size="small"
                      style="width: 100%"
                    >
                      <el-option label="文本" value="text" />
                      <el-option label="数字" value="number" />
                      <el-option label="邮箱" value="email" />
                      <el-option label="电话" value="phone" />
                      <el-option label="选择" value="select" />
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="required" label="必填" width="60">
                  <template #default="scope">
                    <el-switch 
                      v-model="scope.row.required" 
                      size="small"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="autoFill" label="自动填充" width="80">
                  <template #default="scope">
                    <el-switch 
                      v-model="scope.row.autoFill" 
                      size="small"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="placeholder" label="占位符">
                  <template #default="scope">
                    <el-input 
                      v-model="scope.row.placeholder" 
                      size="small"
                    />
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="60">
                  <template #default="scope">
                    <el-button 
                      type="danger" 
                      size="small" 
                      circle
                      @click="removeFormField(scope.$index)"
                    >
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-button 
                type="primary" 
                size="small" 
                @click="addFormField"
                style="margin-top: 8px"
              >
                添加字段
              </el-button>
            </div>
          </el-form-item>
        </div>

        <!-- 风控优化 -->
        <div class="config-section">
          <h3>风控优化</h3>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="智能风控" prop="smartRiskControl">
                <el-switch 
                  v-model="configForm.smartRiskControl"
                  active-text="开启"
                  inactive-text="关闭"
                />
                <div class="form-tip">
                  基于AI模型进行智能风险评估
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="动态风控" prop="dynamicRiskControl">
                <el-switch 
                  v-model="configForm.dynamicRiskControl"
                  active-text="开启"
                  inactive-text="关闭"
                />
                <div class="form-tip">
                  根据交易特征动态调整风控级别
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="白名单策略" prop="whitelistStrategy">
                <el-select 
                  v-model="configForm.whitelistStrategy"
                  style="width: 100%"
                >
                  <el-option label="严格模式" value="strict" />
                  <el-option label="标准模式" value="standard" />
                  <el-option label="宽松模式" value="loose" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="验证强度" prop="verificationStrength">
                <el-slider 
                  v-model="configForm.verificationStrength"
                  :min="1"
                  :max="5"
                  :step="1"
                  show-stops
                  show-input
                  :show-input-controls="false"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 风险规则配置 -->
          <el-form-item label="风险规则" prop="riskRules">
            <div class="risk-rules-config">
              <div 
                v-for="(rule, index) in configForm.riskRules" 
                :key="index"
                class="risk-rule-item"
              >
                <el-row :gutter="16" align="middle">
                  <el-col :span="6">
                    <el-select 
                      v-model="rule.condition" 
                      size="small"
                      style="width: 100%"
                    >
                      <el-option label="金额范围" value="amount" />
                      <el-option label="交易频率" value="frequency" />
                      <el-option label="设备指纹" value="device" />
                      <el-option label="IP地址" value="ip" />
                    </el-select>
                  </el-col>
                  <el-col :span="4">
                    <el-select 
                      v-model="rule.operator" 
                      size="small"
                      style="width: 100%"
                    >
                      <el-option label="大于" value=">" />
                      <el-option label="小于" value="<" />
                      <el-option label="等于" value="=" />
                      <el-option label="包含" value="contains" />
                    </el-select>
                  </el-col>
                  <el-col :span="6">
                    <el-input 
                      v-model="rule.value" 
                      size="small"
                      placeholder="规则值"
                    />
                  </el-col>
                  <el-col :span="6">
                    <el-select 
                      v-model="rule.action" 
                      size="small"
                      style="width: 100%"
                    >
                      <el-option label="放行" value="allow" />
                      <el-option label="验证" value="verify" />
                      <el-option label="拒绝" value="reject" />
                      <el-option label="人工审核" value="manual" />
                    </el-select>
                  </el-col>
                  <el-col :span="2">
                    <el-button 
                      type="danger" 
                      size="small" 
                      circle
                      @click="removeRiskRule(index)"
                    >
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </el-col>
                </el-row>
              </div>
            </div>
            <el-button type="primary" @click="addRiskRule">
              添加规则
            </el-button>
          </el-form-item>
        </div>

        <!-- 用户体验优化 -->
        <div class="config-section">
          <h3>用户体验优化</h3>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="一键支付" prop="oneClickPayment">
                <el-switch 
                  v-model="configForm.oneClickPayment"
                  active-text="开启"
                  inactive-text="关闭"
                />
                <div class="form-tip">
                  保存的用户可直接一键支付
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="支付提醒" prop="paymentReminder">
                <el-switch 
                  v-model="configForm.paymentReminder"
                  active-text="开启"
                  inactive-text="关闭"
                />
                <div class="form-tip">
                  支付失败后自动发送提醒
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="错误提示优化" prop="errorMessageOptimization">
                <el-switch 
                  v-model="configForm.errorMessageOptimization"
                  active-text="开启"
                  inactive-text="关闭"
                />
                <div class="form-tip">
                  提供更友好的错误提示信息
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="加载动画" prop="loadingAnimation">
                <el-switch 
                  v-model="configForm.loadingAnimation"
                  active-text="开启"
                  inactive-text="关闭"
                />
                <div class="form-tip">
                  显示支付处理中的动画效果
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 个性化配置 -->
          <el-form-item label="个性化设置" prop="personalization">
            <el-checkbox-group v-model="configForm.personalization">
              <el-checkbox label="rememberPayment">记住支付偏好</el-checkbox>
              <el-checkbox label="autoCurrency">自动货币转换</el-checkbox>
              <el-checkbox label="quickAmount">快捷金额选择</el-checkbox>
              <el-checkbox label="paymentHistory">显示支付历史</el-checkbox>
              <el-checkbox label="receiptOption">电子发票选项</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </div>

        <!-- 性能优化 -->
        <div class="config-section">
          <h3>性能优化</h3>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="预加载" prop="preload">
                <el-switch 
                  v-model="configForm.preload"
                  active-text="开启"
                  inactive-text="关闭"
                />
                <div class="form-tip">
                  预加载支付接口和资源
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="缓存策略" prop="cacheStrategy">
                <el-select 
                  v-model="configForm.cacheStrategy"
                  style="width: 100%"
                >
                  <el-option label="不缓存" value="none" />
                  <el-option label="短期缓存" value="short" />
                  <el-option label="长期缓存" value="long" />
                  <el-option label="智能缓存" value="smart" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="并发控制" prop="concurrencyControl">
                <el-switch 
                  v-model="configForm.concurrencyControl"
                  active-text="开启"
                  inactive-text="关闭"
                />
                <div class="form-tip">
                  控制并发请求数量
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="超时设置" prop="timeoutSetting">
                <el-input-number 
                  v-model="configForm.timeoutSetting" 
                  :min="5" 
                  :max="60"
                  :step="5"
                  style="width: 100%"
                />
                <span class="input-suffix">秒</span>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button @click="resetForm">重置</el-button>
        <el-button @click="previewConfig">预览</el-button>
        <el-button type="primary" @click="saveConfig" :loading="saving">
          保存配置
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, reactive, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'

// 组件props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  scenario: {
    type: Object,
    default: null
  }
})

// 组件emit
const emit = defineEmits(['update:modelValue', 'saved'])

// 响应式数据
const saving = ref(false)
const configFormRef = ref()
const configForm = reactive({
  paymentOrderOptimization: true,
  hideLowPerformance: true,
  smartRecommendation: true,
  recommendationThreshold: 7,
  paymentMethods: [
    {
      id: 'alipay',
      name: '支付宝',
      enabled: true,
      displayOrder: 1,
      priority: 10
    },
    {
      id: 'wechat',
      name: '微信支付',
      enabled: true,
      displayOrder: 2,
      priority: 9
    },
    {
      id: 'bank_card',
      name: '银行卡',
      enabled: true,
      displayOrder: 3,
      priority: 8
    },
    {
      id: 'credit_card',
      name: '信用卡',
      enabled: true,
      displayOrder: 4,
      priority: 7
    }
  ],
  autoFill: true,
  smartValidation: true,
  fieldMinimization: true,
  progressiveDisplay: true,
  formFields: [
    { name: '手机号', type: 'phone', required: true, autoFill: true, placeholder: '请输入手机号' },
    { name: '邮箱', type: 'email', required: false, autoFill: true, placeholder: '请输入邮箱' },
    { name: '姓名', type: 'text', required: true, autoFill: true, placeholder: '请输入姓名' }
  ],
  smartRiskControl: true,
  dynamicRiskControl: true,
  whitelistStrategy: 'standard',
  verificationStrength: 3,
  riskRules: [
    { condition: 'amount', operator: '>', value: '10000', action: 'verify' },
    { condition: 'frequency', operator: '>', value: '10', action: 'verify' }
  ],
  oneClickPayment: true,
  paymentReminder: true,
  errorMessageOptimization: true,
  loadingAnimation: true,
  personalization: ['rememberPayment', 'quickAmount'],
  preload: true,
  cacheStrategy: 'smart',
  concurrencyControl: true,
  timeoutSetting: 30
})

// 表单验证规则
const configRules = {
  recommendationThreshold: [
    { required: true, message: '请设置推荐阈值', trigger: 'change' }
  ],
  verificationStrength: [
    { required: true, message: '请设置验证强度', trigger: 'change' }
  ],
  timeoutSetting: [
    { required: true, message: '请设置超时时间', trigger: 'change' }
  ]
}

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// 工具方法
const getPaymentIcon = (methodId) => {
  const iconMap = {
    alipay: 'CreditCard',
    wechat: 'MobilePhone',
    bank_card: 'CreditCard',
    credit_card: 'CreditCard',
    other: 'More'
  }
  return iconMap[methodId] || 'CreditCard'
}

// 支付方式管理方法
const addPaymentMethod = () => {
  ElMessageBox.prompt('请输入支付方式名称', '添加支付方式', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /.+/,
    inputErrorMessage: '支付方式名称不能为空'
  }).then(({ value }) => {
    configForm.paymentMethods.push({
      id: Date.now(),
      name: value,
      enabled: true,
      displayOrder: configForm.paymentMethods.length + 1,
      priority: 5
    })
  }).catch(() => {
    // 用户取消
  })
}

const removePaymentMethod = (index) => {
  configForm.paymentMethods.splice(index, 1)
}

// 表单字段管理方法
const addFormField = () => {
  configForm.formFields.push({
    name: '新字段',
    type: 'text',
    required: false,
    autoFill: false,
    placeholder: ''
  })
}

const removeFormField = (index) => {
  configForm.formFields.splice(index, 1)
}

// 风险规则管理方法
const addRiskRule = () => {
  configForm.riskRules.push({
    condition: 'amount',
    operator: '>',
    value: '',
    action: 'verify'
  })
}

const removeRiskRule = (index) => {
  configForm.riskRules.splice(index, 1)
}

// 表单操作方法
const handleClose = () => {
  dialogVisible.value = false
}

const resetForm = () => {
  // 重置表单逻辑
  ElMessage.success('配置已重置')
}

const previewConfig = () => {
  ElMessage.info('配置预览功能开发中...')
}

const saveConfig = async () => {
  try {
    saving.value = true
    
    // 验证表单
    await configFormRef.value.validate()
    
    // 保存配置
    // 这里应该调用API保存配置
    await new Promise(resolve => setTimeout(resolve, 1000)) // 模拟API调用
    
    ElMessage.success('配置保存成功')
    emit('saved', configForm)
    dialogVisible.value = false
    
  } catch (error) {
    ElMessage.error('保存失败，请检查配置')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.optimization-config-content {
  max-height: 600px;
  overflow-y: auto;
  padding: 0 8px;
}

.config-section {
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 1px solid #EBEEF5;
}

.config-section:last-child {
  border-bottom: none;
}

.config-section h3 {
  margin: 0 0 20px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  line-height: 1.4;
}

.payment-methods-config {
  margin-bottom: 16px;
}

.method-config-item {
  margin-bottom: 12px;
  padding: 12px;
  border: 1px solid #EBEEF5;
  border-radius: 6px;
  background-color: #fafafa;
}

.method-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.method-icon {
  font-size: 16px;
  color: #409EFF;
}

.method-name {
  font-weight: 500;
  color: #303133;
}

.form-fields-config {
  margin-bottom: 16px;
}

.risk-rules-config {
  margin-bottom: 16px;
}

.risk-rule-item {
  margin-bottom: 12px;
  padding: 12px;
  border: 1px solid #EBEEF5;
  border-radius: 6px;
  background-color: #fafafa;
}

.input-suffix {
  margin-left: 8px;
  color: #606266;
  font-size: 14px;
}

.dialog-footer {
  text-align: right;
}
</style>