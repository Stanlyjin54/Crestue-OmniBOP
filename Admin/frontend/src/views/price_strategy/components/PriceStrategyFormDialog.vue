<template>
  <el-dialog
    v-model="dialogVisible"
    :title="dialogTitle"
    width="800px"
    :before-close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      class="strategy-form"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="策略名称" prop="strategyName">
            <el-input
              v-model="formData.strategyName"
              placeholder="请输入策略名称"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="策略类型" prop="strategyType">
            <el-select
              v-model="formData.strategyType"
              placeholder="请选择策略类型"
              @change="handleStrategyTypeChange"
            >
              <el-option
                v-for="type in STRATEGY_TYPES"
                :key="type.value"
                :label="type.label"
                :value="type.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="适用渠道" prop="channelType">
            <el-select
              v-model="formData.channelType"
              placeholder="请选择适用渠道"
            >
              <el-option label="B2C零售" value="B2C" />
              <el-option label="B2B批发" value="B2B" />
              <el-option label="全渠道" value="ALL" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用户类型" prop="userType">
            <el-select
              v-model="formData.userType"
              placeholder="请选择用户类型"
            >
              <el-option label="个人用户" value="INDIVIDUAL" />
              <el-option label="企业用户" value="ENTERPRISE" />
              <el-option label="新用户" value="NEW_USER" />
              <el-option label="VIP用户" value="VIP" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="优先级" prop="priority">
            <el-input-number
              v-model="formData.priority"
              :min="1"
              :max="100"
              placeholder="优先级"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select v-model="formData.status" placeholder="请选择状态">
              <el-option label="激活" value="ACTIVE" />
              <el-option label="停用" value="INACTIVE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 数量限制条件 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="最小数量">
            <el-input-number
              v-model="formData.minQuantity"
              :min="1"
              placeholder="最小购买数量"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最大数量">
            <el-input-number
              v-model="formData.maxQuantity"
              :min="1"
              placeholder="最大购买数量"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 价格策略参数 -->
      <el-divider content-position="left">价格策略参数</el-divider>

      <!-- 折扣策略 -->
      <template v-if="formData.strategyType === 'DISCOUNT'">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="折扣率" prop="discountRate">
              <div class="discount-input">
                <el-input-number
                  v-model="discountPercentage"
                  :min="0"
                  :max="100"
                  :precision="1"
                  @change="handleDiscountChange"
                />
                <span class="suffix">%</span>
                <span class="description">
                  {{ formData.discountRate ? (formData.discountRate * 100).toFixed(1) + '折' : '请输入折扣率' }}
                </span>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </template>

      <!-- 固定金额策略 -->
      <template v-if="formData.strategyType === 'FIXED_AMOUNT'">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="固定金额" prop="fixedAmount">
              <div class="amount-input">
                <el-input-number
                  v-model="formData.fixedAmount"
                  :min="0"
                  :precision="2"
                  placeholder="固定金额"
                />
                <span class="suffix">元</span>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </template>

      <!-- 阶梯价格策略 -->
      <template v-if="formData.strategyType === 'TIERED'">
        <el-form-item label="阶梯价格" prop="tieredPrices">
          <div class="tiered-prices">
            <div
              v-for="(tier, index) in formData.tieredPrices"
              :key="index"
              class="tier-row"
            >
              <el-input-number
                v-model="tier.quantity"
                :min="1"
                placeholder="数量"
              />
              <span class="tier-separator">件</span>
              <el-input-number
                v-model="tier.discountRate"
                :min="0"
                :max="1"
                :precision="3"
                placeholder="折扣率"
              />
              <span class="tier-separator">折扣</span>
              <el-button
                type="danger"
                link
                @click="removeTier(index)"
                v-if="formData.tieredPrices.length > 1"
              >
                删除
              </el-button>
            </div>
            <el-button type="primary" link @click="addTier">
              <el-icon><Plus /></el-icon>
              添加阶梯
            </el-button>
          </div>
        </el-form-item>
      </template>

      <!-- 买X送Y策略 -->
      <template v-if="formData.strategyType === 'BUY_X_GET_Y'">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="购买数量" prop="buyQuantity">
              <el-input-number
                v-model="formData.buyQuantity"
                :min="1"
                placeholder="购买数量"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="赠送数量" prop="getQuantity">
              <el-input-number
                v-model="formData.getQuantity"
                :min="1"
                placeholder="赠送数量"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </template>

      <!-- 捆绑销售策略 -->
      <template v-if="formData.strategyType === 'BUNDLE'">
        <el-form-item label="捆绑产品" prop="bundleProducts">
          <div class="bundle-products">
            <div
              v-for="(product, index) in formData.bundleProducts"
              :key="index"
              class="bundle-row"
            >
              <el-input
                v-model="product.productName"
                placeholder="产品名称"
                class="product-name"
              />
              <el-input-number
                v-model="product.quantity"
                :min="1"
                placeholder="数量"
                class="product-quantity"
              />
              <el-input-number
                v-model="product.price"
                :min="0"
                :precision="2"
                placeholder="价格"
                class="product-price"
              />
              <el-button
                type="danger"
                link
                @click="removeBundleProduct(index)"
                v-if="formData.bundleProducts.length > 1"
              >
                删除
              </el-button>
            </div>
            <el-button type="primary" link @click="addBundleProduct">
              <el-icon><Plus /></el-icon>
              添加产品
            </el-button>
          </div>
        </el-form-item>
      </template>

      <!-- 时间范围 -->
      <el-divider content-position="left">生效时间</el-divider>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="开始时间" prop="startTime">
            <el-date-picker
              v-model="formData.startTime"
              type="datetime"
              placeholder="选择开始时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束时间" prop="endTime">
            <el-date-picker
              v-model="formData.endTime"
              type="datetime"
              placeholder="选择结束时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 策略描述 -->
      <el-form-item label="策略描述">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入策略描述"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          {{ mode === 'view' ? '确定' : '确定' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  strategy: {
    type: Object,
    default: null
  },
  mode: {
    type: String,
    default: 'create' // create, edit, view, clone
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'submit'])

// 响应式数据
const formRef = ref(null)
const submitting = ref(false)

// 策略类型配置
const STRATEGY_TYPES = [
  { value: 'DISCOUNT', label: '折扣策略' },
  { value: 'FIXED_AMOUNT', label: '固定金额' },
  { value: 'TIERED', label: '阶梯价格' },
  { value: 'BUY_X_GET_Y', label: '买X送Y' },
  { value: 'BUNDLE', label: '捆绑销售' }
]

// 表单数据
const formData = reactive({
  strategyName: '',
  strategyType: 'DISCOUNT',
  channelType: 'B2C',
  userType: 'INDIVIDUAL',
  priority: 1,
  status: 'ACTIVE',
  minQuantity: null,
  maxQuantity: null,
  discountRate: 0.9, // 折扣率
  fixedAmount: null, // 固定金额
  buyQuantity: null, // 购买数量（买X送Y）
  getQuantity: null, // 赠送数量（买X送Y）
  tieredPrices: [{ quantity: 1, discountRate: 0.9 }], // 阶梯价格
  bundleProducts: [{ productName: '', quantity: 1, price: 0 }], // 捆绑产品
  startTime: '',
  endTime: '',
  description: ''
})

// 表单验证规则
const formRules = {
  strategyName: [
    { required: true, message: '请输入策略名称', trigger: 'blur' }
  ],
  strategyType: [
    { required: true, message: '请选择策略类型', trigger: 'change' }
  ],
  channelType: [
    { required: true, message: '请选择适用渠道', trigger: 'change' }
  ],
  userType: [
    { required: true, message: '请选择用户类型', trigger: 'change' }
  ],
  priority: [
    { required: true, message: '请设置优先级', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ]
}

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const dialogTitle = computed(() => {
  const titleMap = {
    create: '新建价格策略',
    edit: '编辑价格策略',
    view: '查看价格策略',
    clone: '复制价格策略'
  }
  return titleMap[props.mode] || '价格策略'
})

const discountPercentage = computed({
  get: () => formData.discountRate ? (formData.discountRate * 100) : 0,
  set: (value) => {
    formData.discountRate = value / 100
  }
})

// 方法
const handleStrategyTypeChange = (type) => {
  // 清理特定策略类型的数据
  formData.discountRate = type === 'DISCOUNT' ? 0.9 : null
  formData.fixedAmount = type === 'FIXED_AMOUNT' ? 0 : null
  formData.buyQuantity = type === 'BUY_X_GET_Y' ? 1 : null
  formData.getQuantity = type === 'BUY_X_GET_Y' ? 1 : null
  formData.tieredPrices = type === 'TIERED' ? [{ quantity: 1, discountRate: 0.9 }] : []
  formData.bundleProducts = type === 'BUNDLE' ? [{ productName: '', quantity: 1, price: 0 }] : []
}

const handleDiscountChange = (value) => {
  formData.discountRate = value / 100
}

const addTier = () => {
  formData.tieredPrices.push({
    quantity: formData.tieredPrices.length + 1,
    discountRate: 0.9
  })
}

const removeTier = (index) => {
  formData.tieredPrices.splice(index, 1)
}

const addBundleProduct = () => {
  formData.bundleProducts.push({
    productName: '',
    quantity: 1,
    price: 0
  })
}

const removeBundleProduct = (index) => {
  formData.bundleProducts.splice(index, 1)
}

const handleClose = () => {
  dialogVisible.value = false
  resetForm()
}

const resetForm = () => {
  Object.assign(formData, {
    strategyName: '',
    strategyType: 'DISCOUNT',
    channelType: 'B2C',
    userType: 'INDIVIDUAL',
    priority: 1,
    status: 'ACTIVE',
    minQuantity: null,
    maxQuantity: null,
    discountRate: 0.9,
    fixedAmount: null,
    buyQuantity: null,
    getQuantity: null,
    tieredPrices: [{ quantity: 1, discountRate: 0.9 }],
    bundleProducts: [{ productName: '', quantity: 1, price: 0 }],
    startTime: '',
    endTime: '',
    description: ''
  })
  
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

const handleSubmit = async () => {
  if (props.mode === 'view') {
    handleClose()
    return
  }
  
  try {
    await formRef.value.validate()
    
    // 验证时间范围
    if (formData.startTime && formData.endTime && formData.startTime >= formData.endTime) {
      ElMessage.error('结束时间必须晚于开始时间')
      return
    }
    
    // 验证数量限制
    if (formData.minQuantity && formData.maxQuantity && formData.minQuantity >= formData.maxQuantity) {
      ElMessage.error('最大数量必须大于最小数量')
      return
    }
    
    submitting.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    const submitData = {
      ...formData,
      id: props.strategy?.id
    }
    
    emit('submit', submitData)
    ElMessage.success('保存成功')
    handleClose()
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    submitting.value = false
  }
}

// 监听器
watch(() => props.strategy, (newStrategy) => {
  if (newStrategy && (props.mode === 'edit' || props.mode === 'view' || props.mode === 'clone')) {
    Object.assign(formData, newStrategy)
    
    // 确保数组字段正确初始化
    if (!formData.tieredPrices || formData.tieredPrices.length === 0) {
      formData.tieredPrices = [{ quantity: 1, discountRate: 0.9 }]
    }
    
    if (!formData.bundleProducts || formData.bundleProducts.length === 0) {
      formData.bundleProducts = [{ productName: '', quantity: 1, price: 0 }]
    }
  }
}, { immediate: true })

watch(dialogVisible, (newValue) => {
  if (!newValue) {
    resetForm()
  }
})
</script>

<style scoped>
.strategy-form {
  @apply max-h-96 overflow-y-auto;
}

.discount-input,
.amount-input {
  @apply flex items-center gap-2;
}

.discount-input .suffix,
.amount-input .suffix {
  @apply text-gray-500 text-sm;
}

.description {
  @apply text-sm text-gray-500;
}

.tiered-prices {
  @apply space-y-3;
}

.tier-row {
  @apply flex items-center gap-2;
}

.tier-separator {
  @apply text-gray-500 text-sm;
}

.bundle-products {
  @apply space-y-3;
}

.bundle-row {
  @apply flex items-center gap-2;
}

.product-name {
  @apply flex-1;
}

.product-quantity,
.product-price {
  @apply w-24;
}

.dialog-footer {
  @apply flex justify-end gap-2;
}
</style>