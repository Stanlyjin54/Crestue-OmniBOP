<template>
  <el-dialog
    v-model="dialogVisible"
    title="智能优化建议"
    width="900px"
    :before-close="handleClose"
  >
    <div class="optimization-content">
      <!-- 优化概览 -->
      <div class="overview-section">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span>优化概览</span>
              <el-tag v-if="optimizationStatus" :type="optimizationStatus.type">
                {{ optimizationStatus.label }}
              </el-tag>
            </div>
          </template>
          <div class="optimization-summary">
            <div class="summary-item">
              <div class="summary-label">当前转化率</div>
              <div class="summary-value">{{ currentScenario?.conversionRate || 0 }}%</div>
            </div>
            <div class="summary-item">
              <div class="summary-label">目标转化率</div>
              <div class="summary-value target">{{ (currentScenario?.targetConversionRate || 0) }}%</div>
            </div>
            <div class="summary-item">
              <div class="summary-label">优化潜力</div>
              <div class="summary-value potential">{{ optimizationPotential.toFixed(1) }}%</div>
            </div>
            <div class="summary-item">
              <div class="summary-label">预期提升</div>
              <div class="summary-value improvement">{{ expectedImprovement.toFixed(1) }}%</div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 优化建议 -->
      <div class="suggestions-section">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card shadow="never">
              <template #header>
                <div class="card-header">
                  <span>快速优化</span>
                  <el-tag size="small" type="info">{{ quickSuggestions.length }} 项</el-tag>
                </div>
              </template>
              <div class="suggestions-list">
                <div 
                  v-for="suggestion in quickSuggestions" 
                  :key="suggestion.id"
                  class="suggestion-item"
                  :class="{ 'selected': selectedSuggestions.includes(suggestion.id) }"
                  @click="toggleSuggestion(suggestion.id)"
                >
                  <div class="suggestion-header">
                    <div class="suggestion-title">
                      <el-icon><component :is="suggestion.icon" /></el-icon>
                      <span>{{ suggestion.title }}</span>
                    </div>
                    <el-tag :type="suggestion.priority" size="small">
                      {{ suggestion.priorityText }}
                    </el-tag>
                  </div>
                  <div class="suggestion-description">
                    {{ suggestion.description }}
                  </div>
                  <div class="suggestion-impact">
                    <div class="impact-item">
                      <span class="impact-label">预期提升:</span>
                      <span class="impact-value">{{ suggestion.expectedImpact }}%</span>
                    </div>
                    <div class="impact-item">
                      <span class="impact-label">实施难度:</span>
                      <span class="impact-value">{{ suggestion.difficulty }}</span>
                    </div>
                    <div class="impact-item">
                      <span class="impact-label">预估时间:</span>
                      <span class="impact-value">{{ suggestion.estimatedTime }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>

          <el-col :span="12">
            <el-card shadow="never">
              <template #header>
                <div class="card-header">
                  <span>深度优化</span>
                  <el-tag size="small" type="warning">{{ deepSuggestions.length }} 项</el-tag>
                </div>
              </template>
              <div class="suggestions-list">
                <div 
                  v-for="suggestion in deepSuggestions" 
                  :key="suggestion.id"
                  class="suggestion-item"
                  :class="{ 'selected': selectedSuggestions.includes(suggestion.id) }"
                  @click="toggleSuggestion(suggestion.id)"
                >
                  <div class="suggestion-header">
                    <div class="suggestion-title">
                      <el-icon><component :is="suggestion.icon" /></el-icon>
                      <span>{{ suggestion.title }}</span>
                    </div>
                    <el-tag :type="suggestion.priority" size="small">
                      {{ suggestion.priorityText }}
                    </el-tag>
                  </div>
                  <div class="suggestion-description">
                    {{ suggestion.description }}
                  </div>
                  <div class="suggestion-impact">
                    <div class="impact-item">
                      <span class="impact-label">预期提升:</span>
                      <span class="impact-value">{{ suggestion.expectedImpact }}%</span>
                    </div>
                    <div class="impact-item">
                      <span class="impact-label">实施难度:</span>
                      <span class="impact-value">{{ suggestion.difficulty }}</span>
                    </div>
                    <div class="impact-item">
                      <span class="impact-label">预估时间:</span>
                      <span class="impact-value">{{ suggestion.estimatedTime }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- A/B测试配置 -->
      <div class="abtesting-section" v-if="selectedSuggestions.length > 0">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span>A/B测试配置</span>
              <el-switch v-model="enableABTesting" />
            </div>
          </template>
          <div v-if="enableABTesting" class="abtesting-config">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="测试比例">
                  <el-slider
                    v-model="abTestConfig.testRatio"
                    :min="10"
                    :max="50"
                    :step="5"
                    show-stops
                    show-input
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="测试时长">
                  <el-select v-model="abTestConfig.testDuration" style="width: 100%">
                    <el-option label="3天" value="3d" />
                    <el-option label="7天" value="7d" />
                    <el-option label="14天" value="14d" />
                    <el-option label="30天" value="30d" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="置信度">
                  <el-select v-model="abTestConfig.confidence" style="width: 100%">
                    <el-option label="90%" value="0.9" />
                    <el-option label="95%" value="0.95" />
                    <el-option label="99%" value="0.99" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </div>

      <!-- 预期效果 -->
      <div class="prediction-section">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span>优化效果预测</span>
              <el-button type="default" :icon="TrendCharts" @click="updatePrediction">
                重新计算
              </el-button>
            </div>
          </template>
          <div class="prediction-content">
            <el-row :gutter="20">
              <el-col :span="12">
                <div class="prediction-chart">
                  <div class="chart-header">
                    <span>转化率提升预测</span>
                  </div>
                  <div class="chart-bars">
                    <div class="chart-bar">
                      <div class="bar-label">当前</div>
                      <div class="bar-container">
                        <div class="bar-fill current" :style="{ width: currentConversionRate + '%' }"></div>
                        <div class="bar-value">{{ currentConversionRate }}%</div>
                      </div>
                    </div>
                    <div class="chart-bar">
                      <div class="bar-label">优化后</div>
                      <div class="bar-container">
                        <div class="bar-fill optimized" :style="{ width: predictedConversionRate + '%' }"></div>
                        <div class="bar-value">{{ predictedConversionRate }}%</div>
                      </div>
                    </div>
                  </div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="prediction-metrics">
                  <div class="metric-row">
                    <span class="metric-label">预期月增长</span>
                    <span class="metric-value">+{{ predictedImprovement.toFixed(1) }}%</span>
                  </div>
                  <div class="metric-row">
                    <span class="metric-label">预计新增交易</span>
                    <span class="metric-value">{{ predictedNewTransactions.toLocaleString() }} 笔</span>
                  </div>
                  <div class="metric-row">
                    <span class="metric-label">预计增加收入</span>
                    <span class="metric-value">{{ formatAmount(predictedAdditionalRevenue) }}</span>
                  </div>
                  <div class="metric-row">
                    <span class="metric-label">ROI预期</span>
                    <span class="metric-value">+{{ roiPrediction }}%</span>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button @click="saveOptimization">保存优化方案</el-button>
        <el-button type="primary" @click="applyOptimization" :loading="applying">
          {{ enableABTesting ? '创建A/B测试' : '立即应用' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Setting, TrendCharts, Warning, Check, Clock, Lightning,
  Shield, Star, ArrowUp, ArrowDown, Refresh
} from '@element-plus/icons-vue'
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
const emit = defineEmits(['update:modelValue', 'optimized'])

// 组合式函数
const paymentOptimization = usePaymentOptimization()

// 响应式数据
const loading = ref(false)
const applying = ref(false)
const selectedSuggestions = ref([])
const enableABTesting = ref(true)
const abTestConfig = ref({
  testRatio: 20,
  testDuration: '7d',
  confidence: 0.95
})

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const currentScenario = computed(() => props.scenario)

const optimizationStatus = computed(() => {
  if (!currentScenario.value) return null
  
  const currentRate = currentScenario.value.conversionRate || 0
  const targetRate = currentScenario.value.targetConversionRate || 0
  const gap = targetRate - currentRate
  
  if (gap <= 0) {
    return { type: 'success', label: '已达标' }
  } else if (gap <= 2) {
    return { type: 'warning', label: '接近目标' }
  } else {
    return { type: 'danger', label: '需要优化' }
  }
})

const optimizationPotential = computed(() => {
  if (!currentScenario.value) return 0
  return (currentScenario.value.targetConversionRate || 0) - (currentScenario.value.conversionRate || 0)
})

const expectedImprovement = computed(() => {
  if (!selectedSuggestions.value.length) return 0
  const totalImpact = selectedSuggestions.value.reduce((sum, id) => {
    const suggestion = [...quickSuggestions.value, ...deepSuggestions.value].find(s => s.id === id)
    return sum + (suggestion?.expectedImpact || 0)
  }, 0)
  return Math.min(totalImpact, optimizationPotential.value)
})

const currentConversionRate = computed(() => currentScenario.value?.conversionRate || 0)
const predictedConversionRate = computed(() => (currentConversionRate.value + expectedImprovement.value).toFixed(1))
const predictedImprovement = computed(() => expectedImprovement.value)
const predictedNewTransactions = computed(() => {
  const currentTransactions = currentScenario.value?.transactionVolume || 0
  return Math.floor(currentTransactions * (predictedImprovement.value / 100))
})
const predictedAdditionalRevenue = computed(() => {
  const avgAmount = currentScenario.value?.averageAmount || 0
  return predictedNewTransactions.value * avgAmount
})
const roiPrediction = computed(() => {
  const estimatedCost = selectedSuggestions.value.length * 5000 // 假设每项优化成本5000元
  return predictedAdditionalRevenue.value > 0 ? ((predictedAdditionalRevenue.value - estimatedCost) / estimatedCost * 100).toFixed(1) : 0
})

// 快速优化建议
const quickSuggestions = ref([
  {
    id: 'payment-methods',
    title: '优化支付方式排序',
    description: '根据用户偏好智能调整支付方式显示顺序',
    icon: 'Setting',
    priority: 'success',
    priorityText: '高优先级',
    expectedImpact: 2.5,
    difficulty: '简单',
    estimatedTime: '1-2天'
  },
  {
    id: 'form-fields',
    title: '简化支付表单',
    description: '减少必填字段，启用自动填充功能',
    icon: 'ArrowUp',
    priority: 'success',
    priorityText: '高优先级',
    expectedImpact: 3.2,
    difficulty: '中等',
    estimatedTime: '3-5天'
  },
  {
    id: 'loading-speed',
    title: '提升页面加载速度',
    description: '优化支付页面加载时间，减少用户等待',
    icon: 'Lightning',
    priority: 'warning',
    priorityText: '中优先级',
    expectedImpact: 1.8,
    difficulty: '中等',
    estimatedTime: '5-7天'
  }
])

// 深度优化建议
const deepSuggestions = ref([
  {
    id: 'smart-pricing',
    title: '智能定价推荐',
    description: '基于用户行为分析，提供个性化定价策略',
    icon: 'Star',
    priority: 'warning',
    priorityText: '中优先级',
    expectedImpact: 5.5,
    difficulty: '困难',
    estimatedTime: '2-3周'
  },
  {
    id: 'risk-optimization',
    title: '动态风控调整',
    description: '根据交易行为实时调整风控策略',
    icon: 'Shield',
    priority: 'warning',
    priorityText: '中优先级',
    expectedImpact: 4.2,
    difficulty: '困难',
    estimatedTime: '3-4周'
  },
  {
    id: 'personalization',
    title: '支付体验个性化',
    description: '基于用户画像定制支付流程和界面',
    icon: 'Check',
    priority: 'danger',
    priorityText: '低优先级',
    expectedImpact: 3.8,
    difficulty: '中等',
    estimatedTime: '4-6周'
  }
])

// 方法
const handleClose = () => {
  dialogVisible.value = false
  resetOptimization()
}

const resetOptimization = () => {
  selectedSuggestions.value = []
  enableABTesting.value = true
  abTestConfig.value = {
    testRatio: 20,
    testDuration: '7d',
    confidence: 0.95
  }
}

const toggleSuggestion = (suggestionId) => {
  const index = selectedSuggestions.value.indexOf(suggestionId)
  if (index > -1) {
    selectedSuggestions.value.splice(index, 1)
  } else {
    selectedSuggestions.value.push(suggestionId)
  }
}

const updatePrediction = () => {
  ElMessage.info('正在重新计算优化效果...')
  // 模拟重新计算
  setTimeout(() => {
    ElMessage.success('预测更新完成')
  }, 1000)
}

const saveOptimization = () => {
  if (!selectedSuggestions.value.length) {
    ElMessage.warning('请先选择优化建议')
    return
  }
  
  ElMessage.success('优化方案已保存')
}

const applyOptimization = async () => {
  if (!selectedSuggestions.value.length) {
    ElMessage.warning('请先选择优化建议')
    return
  }

  try {
    applying.value = true
    
    const optimizationData = {
      scenarioId: currentScenario.value.id,
      suggestions: selectedSuggestions.value,
      abTesting: enableABTesting.value,
      config: enableABTesting.value ? abTestConfig.value : null,
      expectedImpact: expectedImprovement.value,
      timestamp: new Date().toISOString()
    }

    // 调用优化API
    await paymentOptimization.optimizePaymentScenario(optimizationData)
    
    ElMessage.success(enableABTesting.value ? 'A/B测试创建成功' : '优化方案应用成功')
    dialogVisible.value = false
    emit('optimized')
    resetOptimization()
  } catch (error) {
    console.error('优化失败:', error)
    ElMessage.error('优化失败，请重试')
  } finally {
    applying.value = false
  }
}

const formatAmount = (amount) => {
  if (amount >= 10000) {
    return (amount / 10000).toFixed(1) + '万'
  }
  return '¥' + amount.toLocaleString()
}
</script>

<style scoped>
.optimization-content {
  max-height: 600px;
  overflow-y: auto;
}

.overview-section {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.optimization-summary {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.summary-item {
  text-align: center;
  padding: 16px;
  border-radius: 8px;
  background-color: #f5f5f5;
}

.summary-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.summary-value {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.summary-value.target {
  color: #E6A23C;
}

.summary-value.potential {
  color: #409EFF;
}

.summary-value.improvement {
  color: #67C23A;
}

.suggestions-section {
  margin-bottom: 20px;
}

.suggestions-list {
  max-height: 300px;
  overflow-y: auto;
}

.suggestion-item {
  padding: 16px;
  border: 1px solid #EBEEF5;
  border-radius: 8px;
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.3s;
}

.suggestion-item:hover {
  border-color: #409EFF;
}

.suggestion-item.selected {
  border-color: #409EFF;
  background-color: #f0f9ff;
}

.suggestion-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.suggestion-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  color: #303133;
}

.suggestion-description {
  font-size: 13px;
  color: #606266;
  margin-bottom: 12px;
  line-height: 1.5;
}

.suggestion-impact {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
}

.impact-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.impact-label {
  color: #909399;
  margin-bottom: 4px;
}

.impact-value {
  color: #303133;
  font-weight: 500;
}

.abtesting-section {
  margin-bottom: 20px;
}

.abtesting-config {
  padding: 16px 0;
}

.prediction-section {
  margin-bottom: 20px;
}

.prediction-content {
  padding: 16px 0;
}

.prediction-chart {
  margin-bottom: 20px;
}

.chart-header {
  font-weight: 500;
  color: #303133;
  margin-bottom: 16px;
}

.chart-bars {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.chart-bar {
  display: flex;
  align-items: center;
  gap: 12px;
}

.bar-label {
  min-width: 60px;
  font-size: 14px;
  color: #606266;
}

.bar-container {
  flex: 1;
  position: relative;
  height: 32px;
  background-color: #f5f5f5;
  border-radius: 16px;
  overflow: hidden;
}

.bar-fill {
  height: 100%;
  transition: width 0.3s ease;
}

.bar-fill.current {
  background-color: #909399;
}

.bar-fill.optimized {
  background-color: #67C23A;
}

.bar-value {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 12px;
  font-weight: 500;
  color: #303133;
}

.prediction-metrics {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.metric-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #EBEEF5;
}

.metric-row:last-child {
  border-bottom: none;
}

.metric-label {
  font-size: 14px;
  color: #606266;
}

.metric-value {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.dialog-footer {
  text-align: right;
}
</style>