<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? '编辑支付场景' : '创建支付场景'"
    width="800px"
    :before-close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="120px"
      class="scenario-form"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="场景名称" prop="name">
            <el-input
              v-model="form.name"
              placeholder="请输入场景名称"
              :maxlength="50"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="场景类型" prop="type">
            <el-select
              v-model="form.type"
              placeholder="选择场景类型"
              style="width: 100%"
            >
              <el-option
                v-for="type in paymentOptimization.scenarioConfig.scenarioTypes"
                :key="type.value"
                :label="type.label"
                :value="type.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="场景描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          placeholder="请输入场景描述"
          :rows="3"
          :maxlength="200"
          show-word-limit
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="业务类型" prop="businessType">
            <el-select
              v-model="form.businessType"
              placeholder="选择业务类型"
              style="width: 100%"
            >
              <el-option
                v-for="business in paymentOptimization.scenarioConfig.businessTypes"
                :key="business.value"
                :label="business.label"
                :value="business.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="目标市场" prop="targetMarket">
            <el-select
              v-model="form.targetMarket"
              placeholder="选择目标市场"
              style="width: 100%"
            >
              <el-option label="国内" value="domestic" />
              <el-option label="海外" value="overseas" />
              <el-option label="全球" value="global" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预期转化率" prop="targetConversionRate">
            <el-input-number
              v-model="form.targetConversionRate"
              :min="0"
              :max="100"
              :step="0.1"
              :precision="2"
              style="width: 100%"
              placeholder="%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="平均订单金额" prop="averageOrderValue">
            <el-input-number
              v-model="form.averageOrderValue"
              :min="0"
              :step="100"
              style="width: 100%"
              placeholder="元"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="支持的支付方式" prop="supportedMethods">
        <el-checkbox-group v-model="form.supportedMethods">
          <el-checkbox
            v-for="method in paymentOptimization.scenarioConfig.paymentMethods"
            :key="method.id"
            :label="method.id"
          >
            <div class="method-option">
              <span class="method-name">{{ method.name }}</span>
              <span class="method-fee">费率: {{ method.fee }}%</span>
            </div>
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="支付流程配置">
        <el-collapse v-model="activeCollapses">
          <el-collapse-item title="支付流程" name="paymentFlow">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="支付页面优化">
                  <el-switch v-model="form.config.paymentPageOptimization" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="一键支付">
                  <el-switch v-model="form.config.oneClickPayment" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="自动填充">
                  <el-switch v-model="form.config.autoFill" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="支付提醒">
                  <el-switch v-model="form.config.paymentReminder" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-collapse-item>

          <el-collapse-item title="风控配置" name="riskControl">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="风控级别">
                  <el-select v-model="form.config.riskLevel" style="width: 100%">
                    <el-option label="低" value="low" />
                    <el-option label="中" value="medium" />
                    <el-option label="高" value="high" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="验证码">
                  <el-switch v-model="form.config.verificationCode" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="交易限额">
                  <el-input-number
                    v-model="form.config.transactionLimit"
                    :min="0"
                    style="width: 100%"
                    placeholder="元"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="白名单检查">
                  <el-switch v-model="form.config.whitelistCheck" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-collapse-item>

          <el-collapse-item title="优化策略" name="optimization">
            <el-form-item label="A/B测试">
              <el-switch v-model="form.config.abTesting" />
            </el-form-item>
            <el-form-item label="智能推荐">
              <el-switch v-model="form.config.smartRecommendation" />
            </el-form-item>
            <el-form-item label="个性化定价">
              <el-switch v-model="form.config.personalizedPricing" />
            </el-form-item>
            <el-form-item label="优惠推荐">
              <el-switch v-model="form.config.offerRecommendation" />
            </el-form-item>
          </el-collapse-item>
        </el-collapse>
      </el-form-item>

      <el-form-item label="业务规则" prop="businessRules">
        <el-input
          v-model="form.businessRules"
          type="textarea"
          placeholder="请输入业务规则"
          :rows="3"
          :maxlength="500"
          show-word-limit
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button @click="handlePreview">预览</el-button>
        <el-button type="primary" @click="handleSave" :loading="loading">
          {{ isEdit ? '更新' : '创建' }}
        </el-button>
      </div>
    </template>

    <!-- 预览对话框 -->
    <el-dialog
      v-model="showPreview"
      title="支付场景预览"
      width="600px"
      append-to-body
    >
      <div class="preview-content">
        <div class="preview-section">
          <h4>基本信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="场景名称">
              {{ form.name }}
            </el-descriptions-item>
            <el-descriptions-item label="场景类型">
              {{ getScenarioTypeLabel(form.type) }}
            </el-descriptions-item>
            <el-descriptions-item label="业务类型">
              {{ getBusinessTypeLabel(form.businessType) }}
            </el-descriptions-item>
            <el-descriptions-item label="目标市场">
              {{ getTargetMarketLabel(form.targetMarket) }}
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="preview-section">
          <h4>支付方式</h4>
          <div class="payment-methods">
            <el-tag
              v-for="methodId in form.supportedMethods"
              :key="methodId"
              type="success"
              style="margin: 4px"
            >
              {{ getPaymentMethodName(methodId) }}
            </el-tag>
          </div>
        </div>

        <div class="preview-section">
          <h4>配置概览</h4>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="支付流程优化">
              {{ form.config.paymentPageOptimization ? '开启' : '关闭' }}
            </el-descriptions-item>
            <el-descriptions-item label="风控级别">
              {{ getRiskLevelLabel(form.config.riskLevel) }}
            </el-descriptions-item>
            <el-descriptions-item label="A/B测试">
              {{ form.config.abTesting ? '开启' : '关闭' }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>

      <template #footer>
        <el-button @click="showPreview = false">关闭</el-button>
      </template>
    </el-dialog>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { usePaymentOptimization } from '@/composables/usePaymentOptimization'

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
const emit = defineEmits(['update:modelValue', 'updated'])

// 组合式函数
const paymentOptimization = usePaymentOptimization()

// 响应式数据
const formRef = ref()
const loading = ref(false)
const activeCollapses = ref(['paymentFlow'])
const showPreview = ref(false)

// 表单数据
const form = ref({
  name: '',
  type: '',
  description: '',
  businessType: '',
  targetMarket: '',
  targetConversionRate: 0,
  averageOrderValue: 0,
  supportedMethods: [],
  config: {
    paymentPageOptimization: true,
    oneClickPayment: false,
    autoFill: true,
    paymentReminder: true,
    riskLevel: 'medium',
    verificationCode: true,
    transactionLimit: 0,
    whitelistCheck: false,
    abTesting: false,
    smartRecommendation: true,
    personalizedPricing: false,
    offerRecommendation: true
  },
  businessRules: ''
})

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const isEdit = computed(() => !!props.scenario?.id)

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入场景名称', trigger: 'blur' },
    { min: 2, max: 50, message: '名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择场景类型', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入场景描述', trigger: 'blur' },
    { min: 10, max: 200, message: '描述长度在 10 到 200 个字符', trigger: 'blur' }
  ],
  businessType: [
    { required: true, message: '请选择业务类型', trigger: 'change' }
  ],
  supportedMethods: [
    { type: 'array', required: true, message: '请至少选择一种支付方式', trigger: 'change' }
  ]
}

// 工具方法
const getScenarioTypeLabel = (value) => {
  const type = paymentOptimization.scenarioConfig.scenarioTypes.find(t => t.value === value)
  return type ? type.label : value
}

const getBusinessTypeLabel = (value) => {
  const typeMap = {
    ecommerce: '电商购物',
    subscription: '订阅服务',
    education: '在线教育',
    healthcare: '医疗服务',
    travel: '旅游出行',
    entertainment: '娱乐服务',
    real_estate: '房地产',
    automotive: '汽车服务'
  }
  return typeMap[value] || value
}

const getTargetMarketLabel = (value) => {
  const marketMap = {
    domestic: '国内',
    overseas: '海外',
    global: '全球'
  }
  return marketMap[value] || value
}

const getRiskLevelLabel = (value) => {
  const levelMap = {
    low: '低',
    medium: '中',
    high: '高'
  }
  return levelMap[value] || value
}

const getPaymentMethodName = (methodId) => {
  const method = paymentOptimization.scenarioConfig.paymentMethods.find(m => m.id === methodId)
  return method ? method.name : methodId
}

// 方法
const resetForm = () => {
  form.value = {
    name: '',
    type: '',
    description: '',
    businessType: '',
    targetMarket: '',
    targetConversionRate: 0,
    averageOrderValue: 0,
    supportedMethods: [],
    config: {
      paymentPageOptimization: true,
      oneClickPayment: false,
      autoFill: true,
      paymentReminder: true,
      riskLevel: 'medium',
      verificationCode: true,
      transactionLimit: 0,
      whitelistCheck: false,
      abTesting: false,
      smartRecommendation: true,
      personalizedPricing: false,
      offerRecommendation: true
    },
    businessRules: ''
  }
  nextTick(() => {
    if (formRef.value) {
      formRef.value.clearValidate()
    }
  })
}

const loadScenarioData = () => {
  if (props.scenario) {
    form.value = {
      ...props.scenario,
      config: {
        ...form.value.config,
        ...props.scenario.config
      }
    }
  }
}

const handleClose = () => {
  dialogVisible.value = false
  resetForm()
}

const handlePreview = () => {
  showPreview.value = true
}

const handleSave = async () => {
  if (!formRef.value) return

  try {
    const valid = await formRef.value.validate()
    if (!valid) return

    loading.value = true

    const scenarioData = {
      ...form.value,
      updatedAt: new Date().toISOString()
    }

    if (isEdit.value) {
      await paymentOptimization.updatePaymentScenario(props.scenario.id, scenarioData)
    } else {
      scenarioData.id = 'scenario_' + Date.now()
      scenarioData.createdAt = new Date().toISOString()
      await paymentOptimization.createPaymentScenario(scenarioData)
    }

    ElMessage.success(isEdit.value ? '场景更新成功' : '场景创建成功')
    dialogVisible.value = false
    emit('updated')
    resetForm()
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
  } finally {
    loading.value = false
  }
}

// 监听器
watch(
  () => props.modelValue,
  (newVal) => {
    if (newVal) {
      loadScenarioData()
    }
  }
)

watch(
  () => props.scenario,
  () => {
    if (props.modelValue) {
      loadScenarioData()
    }
  }
)
</script>

<style scoped>
.scenario-form {
  max-height: 500px;
  overflow-y: auto;
}

.method-option {
  display: flex;
  flex-direction: column;
}

.method-name {
  font-weight: 500;
}

.method-fee {
  font-size: 12px;
  color: #909399;
}

.dialog-footer {
  text-align: right;
}

.preview-content {
  max-height: 400px;
  overflow-y: auto;
}

.preview-section {
  margin-bottom: 24px;
}

.preview-section h4 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 500;
}

.payment-methods {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
</style>