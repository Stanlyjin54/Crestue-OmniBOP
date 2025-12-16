<template>
  <el-dialog
    v-model="dialogVisible"
    title="价格计算测试"
    width="700px"
    :before-close="handleClose"
  >
    <el-form :model="calculationForm" label-width="120px" class="calculation-form">
      <!-- 基本信息 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="原价" prop="originalPrice">
            <el-input-number
              v-model="calculationForm.originalPrice"
              :min="0"
              :precision="2"
              placeholder="请输入原价"
              class="full-width"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="购买数量" prop="quantity">
            <el-input-number
              v-model="calculationForm.quantity"
              :min="1"
              placeholder="请输入购买数量"
              class="full-width"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="用户类型" prop="userType">
            <el-select v-model="calculationForm.userType" placeholder="请选择用户类型" class="full-width">
              <el-option label="个人用户" value="INDIVIDUAL" />
              <el-option label="企业用户" value="ENTERPRISE" />
              <el-option label="新用户" value="NEW_USER" />
              <el-option label="VIP用户" value="VIP" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="销售渠道" prop="channelType">
            <el-select v-model="calculationForm.channelType" placeholder="请选择销售渠道" class="full-width">
              <el-option label="B2C零售" value="B2C" />
              <el-option label="B2B批发" value="B2B" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 策略选择 -->
      <el-divider content-position="left">适用策略</el-divider>
      
      <el-form-item label="选择策略">
        <el-select
          v-model="calculationForm.selectedStrategies"
          multiple
          filterable
          placeholder="请选择要测试的策略"
          class="full-width"
        >
          <el-option
            v-for="strategy in availableStrategies"
            :key="strategy.id"
            :label="`${strategy.strategyName} (${getStrategyTypeLabel(strategy.strategyType)})`"
            :value="strategy.id"
          />
        </el-select>
      </el-form-item>

      <!-- 计算结果 -->
      <el-divider content-position="left">计算结果</el-divider>
      
      <div v-if="calculationResult" class="calculation-result">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="result-item">
              <div class="result-label">原价总计</div>
              <div class="result-value original">¥{{ calculationResult.originalTotal.toFixed(2) }}</div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="result-item">
              <div class="result-label">优惠金额</div>
              <div class="result-value discount">-¥{{ calculationResult.discountAmount.toFixed(2) }}</div>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="result-item">
              <div class="result-label">实付金额</div>
              <div class="result-value final">¥{{ calculationResult.finalPrice.toFixed(2) }}</div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="result-item">
              <div class="result-label">折扣力度</div>
              <div class="result-value rate">
                {{ (calculationResult.discountRate * 100).toFixed(1) }}折
              </div>
            </div>
          </el-col>
        </el-row>

        <!-- 策略详情 -->
        <div v-if="calculationResult.strategyDetails" class="strategy-details">
          <h4>策略详情</h4>
          <div
            v-for="detail in calculationResult.strategyDetails"
            :key="detail.strategyId"
            class="strategy-detail-item"
          >
            <div class="strategy-name">{{ detail.strategyName }}</div>
            <div class="strategy-info">
              <span class="strategy-type">{{ getStrategyTypeLabel(detail.strategyType) }}</span>
              <span class="strategy-discount">{{ detail.discountDescription }}</span>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="no-result">
        请填写信息并选择策略进行计算
      </div>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleCalculate">
          <el-icon><Calculator /></el-icon>
          计算价格
        </el-button>
        <el-button @click="handleReset">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
        <el-button type="primary" @click="handleClose">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Calculator, Refresh } from '@element-plus/icons-vue'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  strategyList: {
    type: Array,
    default: () => []
  }
})

// Emits
const emit = defineEmits(['update:modelValue'])

// 响应式数据
const calculationForm = reactive({
  originalPrice: 100,
  quantity: 1,
  userType: 'INDIVIDUAL',
  channelType: 'B2C',
  selectedStrategies: []
})

const calculationResult = ref(null)

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const availableStrategies = computed(() => {
  return props.strategyList.filter(strategy => {
    // 过滤激活的策略
    if (strategy.status !== 'ACTIVE') return false
    
    // 检查渠道匹配
    if (strategy.channelType !== 'ALL' && strategy.channelType !== calculationForm.channelType) {
      return false
    }
    
    // 检查用户类型匹配
    if (strategy.userType !== calculationForm.userType) return false
    
    return true
  })
})

// 方法
const handleCalculate = () => {
  if (!calculationForm.originalPrice || calculationForm.originalPrice <= 0) {
    ElMessage.error('请输入有效的原价')
    return
  }
  
  if (!calculationForm.quantity || calculationForm.quantity <= 0) {
    ElMessage.error('请输入有效的购买数量')
    return
  }
  
  if (calculationForm.selectedStrategies.length === 0) {
    ElMessage.warning('请至少选择一个策略进行计算')
    return
  }
  
  // 执行价格计算
  performCalculation()
}

const performCalculation = () => {
  const originalTotal = calculationForm.originalPrice * calculationForm.quantity
  let totalDiscount = 0
  const strategyDetails = []
  
  // 获取选中的策略
  const selectedStrategies = props.strategyList.filter(s => 
    calculationForm.selectedStrategies.includes(s.id)
  )
  
  // 按优先级排序
  selectedStrategies.sort((a, b) => a.priority - b.priority)
  
  // 计算每个策略的优惠
  selectedStrategies.forEach(strategy => {
    const detail = calculateStrategyDiscount(strategy, calculationForm)
    if (detail.discountAmount > 0) {
      totalDiscount += detail.discountAmount
      strategyDetails.push(detail)
    }
  })
  
  // 确保折扣不超过原价
  const finalDiscount = Math.min(totalDiscount, originalTotal)
  const finalPrice = originalTotal - finalDiscount
  
  calculationResult.value = {
    originalTotal,
    discountAmount: finalDiscount,
    finalPrice,
    discountRate: originalTotal > 0 ? finalPrice / originalTotal : 1,
    strategyDetails
  }
}

const calculateStrategyDiscount = (strategy, form) => {
  const { originalPrice, quantity } = form
  let discountAmount = 0
  let discountDescription = ''
  
  switch (strategy.strategyType) {
    case 'DISCOUNT':
      if (!strategy.minQuantity || quantity >= strategy.minQuantity) {
        if (!strategy.maxQuantity || quantity <= strategy.maxQuantity) {
          discountAmount = originalPrice * quantity * (1 - strategy.discountRate)
          discountDescription = `${(strategy.discountRate * 100).toFixed(1)}折优惠`
        }
      }
      break
      
    case 'FIXED_AMOUNT':
      if (!strategy.minQuantity || quantity >= strategy.minQuantity) {
        discountAmount = strategy.fixedAmount
        discountDescription = `固定优惠¥${strategy.fixedAmount}`
      }
      break
      
    case 'TIERED':
      // 找到匹配的阶梯
      const matchedTier = strategy.tieredPrices?.find(tier => 
        quantity >= tier.quantity
      )
      if (matchedTier) {
        discountAmount = originalPrice * quantity * (1 - matchedTier.discountRate)
        discountDescription = `${(matchedTier.discountRate * 100).toFixed(1)}折阶梯优惠`
      }
      break
      
    case 'BUY_X_GET_Y':
      if (quantity >= strategy.buyQuantity) {
        const freeItems = Math.floor(quantity / strategy.buyQuantity) * strategy.getQuantity
        discountAmount = freeItems * originalPrice
        discountDescription = `买${strategy.buyQuantity}送${strategy.getQuantity}`
      }
      break
      
    case 'BUNDLE':
      // 捆绑销售逻辑
      discountAmount = originalPrice * 0.1 // 假设10%捆绑优惠
      discountDescription = '捆绑销售优惠'
      break
  }
  
  return {
    strategyId: strategy.id,
    strategyName: strategy.strategyName,
    strategyType: strategy.strategyType,
    discountAmount,
    discountDescription
  }
}

const handleReset = () => {
  Object.assign(calculationForm, {
    originalPrice: 100,
    quantity: 1,
    userType: 'INDIVIDUAL',
    channelType: 'B2C',
    selectedStrategies: []
  })
  
  calculationResult.value = null
}

const handleClose = () => {
  dialogVisible.value = false
  handleReset()
}

const getStrategyTypeLabel = (type) => {
  const typeMap = {
    'DISCOUNT': '折扣策略',
    'FIXED_AMOUNT': '固定金额',
    'TIERED': '阶梯价格',
    'BUY_X_GET_Y': '买X送Y',
    'BUNDLE': '捆绑销售'
  }
  return typeMap[type] || type
}

// 监听器
watch(() => calculationForm.selectedStrategies, () => {
  if (calculationResult.value) {
    handleCalculate()
  }
})

watch(() => [calculationForm.originalPrice, calculationForm.quantity, calculationForm.userType, calculationForm.channelType], () => {
  if (calculationResult.value) {
    handleCalculate()
  }
})
</script>

<style scoped>
.calculation-form {
  @apply max-h-96 overflow-y-auto;
}

.full-width {
  @apply w-full;
}

.calculation-result {
  @apply space-y-4;
}

.result-item {
  @apply text-center p-4 bg-gray-50 rounded-lg;
}

.result-label {
  @apply text-sm text-gray-600 mb-1;
}

.result-value {
  @apply text-lg font-semibold;
}

.result-value.original {
  @apply text-gray-600;
}

.result-value.discount {
  @apply text-red-500;
}

.result-value.final {
  @apply text-green-600 text-xl;
}

.result-value.rate {
  @apply text-blue-600;
}

.strategy-details {
  @apply mt-4 p-4 bg-blue-50 rounded-lg;
}

.strategy-details h4 {
  @apply text-sm font-semibold text-gray-700 mb-2;
}

.strategy-detail-item {
  @apply p-2 border-b border-blue-200 last:border-b-0;
}

.strategy-name {
  @apply font-medium text-gray-800;
}

.strategy-info {
  @apply flex items-center justify-between text-sm text-gray-600 mt-1;
}

.strategy-type {
  @apply px-2 py-1 bg-blue-100 text-blue-800 rounded;
}

.strategy-discount {
  @apply text-green-600;
}

.no-result {
  @apply text-center text-gray-500 py-8;
}

.dialog-footer {
  @apply flex justify-end gap-2;
}
</style>