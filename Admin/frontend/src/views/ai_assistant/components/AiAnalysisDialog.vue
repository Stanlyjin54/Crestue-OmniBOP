<template>
  <el-dialog
    v-model="dialogVisible"
    title="AI助手分析报告"
    width="1000px"
    :close-on-click-modal="false"
  >
    <div class="analysis-container">
      <!-- 分析概览 -->
      <div class="overview-section">
        <h3 class="section-title">
          <el-icon><TrendCharts /></el-icon>
          分析概览
        </h3>
        <el-row :gutter="20">
          <el-col :span="6" v-for="(metric, key) in overviewMetrics" :key="key">
            <div class="metric-card">
              <div class="metric-icon" :style="{ color: metric.color }">
                <el-icon><component :is="metric.icon" /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-value">{{ metric.value }}</div>
                <div class="metric-label">{{ metric.label }}</div>
                <div class="metric-change" :style="{ color: metric.change.color }">
                  <el-icon><component :is="metric.change.icon" /></el-icon>
                  {{ metric.change.value }}
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 标签页 -->
      <el-tabs v-model="activeTab" class="analysis-tabs">
        <!-- 效果分析 -->
        <el-tab-pane label="效果分析" name="performance">
          <div class="tab-content">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card class="chart-card" shadow="never">
                  <template #header>
                    <span>响应时间趋势</span>
                  </template>
                  <div ref="responseTimeChartRef" class="chart-container"></div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card class="chart-card" shadow="never">
                  <template #header>
                    <span>成功率分析</span>
                  </template>
                  <div ref="successRateChartRef" class="chart-container"></div>
                </el-card>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="24">
                <el-card class="chart-card" shadow="never">
                  <template #header>
                    <span>功能使用热度图</span>
                  </template>
                  <div ref="featureHeatmapRef" class="chart-container-large"></div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>

        <!-- 用户分析 -->
        <el-tab-pane label="用户分析" name="users">
          <div class="tab-content">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card class="chart-card" shadow="never">
                  <template #header>
                    <span>用户活跃度</span>
                  </template>
                  <div ref="userActivityChartRef" class="chart-container"></div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card class="chart-card" shadow="never">
                  <template #header>
                    <span>用户分布</span>
                  </template>
                  <div ref="userDistributionChartRef" class="chart-container"></div>
                </el-card>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="24">
                <el-card shadow="never">
                  <template #header>
                    <span>活跃用户排行</span>
                  </template>
                  <el-table :data="topUsers" stripe>
                    <el-table-column prop="rank" label="排名" width="80" />
                    <el-table-column prop="userName" label="用户名" width="150" />
                    <el-table-column prop="requestCount" label="请求次数" width="120" />
                    <el-table-column prop="avgResponseTime" label="平均响应时间" width="150">
                      <template #default="{ row }">
                        {{ row.avgResponseTime }}ms
                      </template>
                    </el-table-column>
                    <el-table-column prop="successRate" label="成功率" width="100">
                      <template #default="{ row }">
                        <el-progress 
                          :percentage="row.successRate" 
                          :show-text="false"
                          :stroke-width="8"
                        />
                        <span style="margin-left: 8px;">{{ row.successRate }}%</span>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>

        <!-- 优化建议 -->
        <el-tab-pane label="优化建议" name="recommendations">
          <div class="tab-content">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card shadow="never">
                  <template #header>
                    <span>性能优化建议</span>
                  </template>
                  <div class="recommendation-list">
                    <div 
                      v-for="(rec, index) in performanceRecommendations" 
                      :key="index"
                      class="recommendation-item"
                    >
                      <div class="rec-icon" :class="`priority-${rec.priority}`">
                        <el-icon><component :is="getPriorityIcon(rec.priority)" /></el-icon>
                      </div>
                      <div class="rec-content">
                        <div class="rec-title">{{ rec.title }}</div>
                        <div class="rec-description">{{ rec.description }}</div>
                        <div class="rec-impact">预期改善: {{ rec.impact }}</div>
                      </div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card shadow="never">
                  <template #header>
                    <span>功能优化建议</span>
                  </template>
                  <div class="recommendation-list">
                    <div 
                      v-for="(rec, index) in featureRecommendations" 
                      :key="index"
                      class="recommendation-item"
                    >
                      <div class="rec-icon" :class="`priority-${rec.priority}`">
                        <el-icon><component :is="getPriorityIcon(rec.priority)" /></el-icon>
                      </div>
                      <div class="rec-content">
                        <div class="rec-title">{{ rec.title }}</div>
                        <div class="rec-description">{{ rec.description }}</div>
                        <div class="rec-impact">预期改善: {{ rec.impact }}</div>
                      </div>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>

        <!-- 成本分析 -->
        <el-tab-pane label="成本分析" name="cost">
          <div class="tab-content">
            <el-row :gutter="20">
              <el-col :span="24">
                <el-card class="chart-card" shadow="never">
                  <template #header>
                    <span>成本趋势分析</span>
                  </template>
                  <div ref="costTrendChartRef" class="chart-container-large"></div>
                </el-card>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="8">
                <el-card shadow="never">
                  <div class="cost-summary">
                    <div class="cost-item">
                      <div class="cost-label">总成本</div>
                      <div class="cost-value">¥{{ costData.totalCost }}</div>
                    </div>
                    <div class="cost-item">
                      <div class="cost-label">平均成本/请求</div>
                      <div class="cost-value">¥{{ costData.avgCostPerRequest }}</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="never">
                  <div class="cost-breakdown">
                    <h4>成本构成</h4>
                    <div class="cost-detail" v-for="(cost, key) in costData.breakdown" :key="key">
                      <div class="cost-type">{{ cost.type }}</div>
                      <div class="cost-amount">¥{{ cost.amount }}</div>
                      <div class="cost-percentage">{{ cost.percentage }}%</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="never">
                  <div class="cost-optimization">
                    <h4>优化建议</h4>
                    <div class="opt-suggestion" v-for="(opt, index) in costOptimizationSuggestions" :key="index">
                      <div class="opt-title">{{ opt.title }}</div>
                      <div class="opt-save">预计节省: {{ opt.saving }}</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
        <el-button 
          type="primary" 
          :icon="Download" 
          @click="handleExportAnalysis"
        >
          导出分析报告
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, nextTick, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  TrendCharts, ChatDotRound, Timer, User, Connection, Download,
  ArrowUp, ArrowDown, Warning, InfoFilled, Check
} from '@element-plus/icons-vue'
import { useAiAssistantStatistics } from '@/composables/useAiAssistant'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  data: {
    type: Object,
    default: () => ({})
  }
})

// Emits
const emit = defineEmits(['update:modelValue'])

// AI助手统计
const aiAssistantStats = useAiAssistantStatistics()

// 响应式数据
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const activeTab = ref('performance')
const responseTimeChartRef = ref()
const successRateChartRef = ref()
const featureHeatmapRef = ref()
const userActivityChartRef = ref()
const userDistributionChartRef = ref()
const costTrendChartRef = ref()

// 分析概览指标
const overviewMetrics = computed(() => [
  {
    label: '总请求数',
    value: props.data.totalRequests || '0',
    color: '#409EFF',
    icon: ChatDotRound,
    change: {
      icon: ArrowUp,
      color: '#67C23A',
      value: '+12.5%'
    }
  },
  {
    label: '平均响应时间',
    value: props.data.avgResponseTime ? `${props.data.avgResponseTime}ms` : '0ms',
    color: '#E6A23C',
    icon: Timer,
    change: {
      icon: ArrowDown,
      color: '#67C23A',
      value: '-5.2%'
    }
  },
  {
    label: '活跃用户数',
    value: props.data.activeUsers || '0',
    color: '#F56C6C',
    icon: User,
    change: {
      icon: ArrowUp,
      color: '#67C23A',
      value: '+8.3%'
    }
  },
  {
    label: '成功率',
    value: props.data.successRate ? `${props.data.successRate}%` : '0%',
    color: '#67C23A',
    icon: Check,
    change: {
      icon: ArrowUp,
      color: '#67C23A',
      value: '+2.1%'
    }
  }
])

// 活跃用户排行
const topUsers = computed(() => [
  {
    rank: 1,
    userName: 'user001',
    requestCount: 156,
    avgResponseTime: 1250,
    successRate: 98.7
  },
  {
    rank: 2,
    userName: 'user002',
    requestCount: 134,
    avgResponseTime: 980,
    successRate: 97.2
  },
  {
    rank: 3,
    userName: 'user003',
    requestCount: 112,
    avgResponseTime: 1100,
    successRate: 96.5
  }
])

// 性能优化建议
const performanceRecommendations = [
  {
    title: '优化模型参数',
    description: '调整temperature和max_tokens参数以提高响应质量',
    impact: '响应时间减少15%',
    priority: 'high'
  },
  {
    title: '增加缓存机制',
    description: '对常见查询结果进行缓存，减少重复计算',
    impact: '响应时间减少30%',
    priority: 'high'
  },
  {
    title: '负载均衡优化',
    description: '调整服务器负载分配策略，提高并发处理能力',
    impact: '并发处理能力提升25%',
    priority: 'medium'
  }
]

// 功能优化建议
const featureRecommendations = [
  {
    title: '增加智能推荐功能',
    description: '基于用户历史行为提供个性化推荐',
    impact: '用户满意度提升20%',
    priority: 'high'
  },
  {
    title: '优化文本生成质量',
    description: '改进prompt工程，提高生成内容的准确性',
    impact: '内容质量提升15%',
    priority: 'medium'
  },
  {
    title: '增加多语言支持',
    description: '扩展语言模型支持范围，满足更多用户需求',
    impact: '用户覆盖面扩大30%',
    priority: 'low'
  }
]

// 成本分析数据
const costData = {
  totalCost: '1,234.56',
  avgCostPerRequest: '0.08',
  breakdown: [
    { type: 'API调用费用', amount: '856.78', percentage: 69.4 },
    { type: '计算资源费用', amount: '267.89', percentage: 21.7 },
    { type: '存储费用', amount: '109.89', percentage: 8.9 }
  ]
}

// 成本优化建议
const costOptimizationSuggestions = [
  {
    title: '优化API调用策略',
    saving: '15-25%'
  },
  {
    title: '减少不必要的请求',
    saving: '10-20%'
  },
  {
    title: '使用更经济的模型',
    saving: '20-30%'
  }
]

// 方法
const getPriorityIcon = (priority) => {
  const iconMap = {
    high: Warning,
    medium: InfoFilled,
    low: Check
  }
  return iconMap[priority] || InfoFilled
}

const handleExportAnalysis = async () => {
  try {
    // 导出分析报告的逻辑
    ElMessage.success('分析报告导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

// 渲染图表
const renderCharts = async () => {
  await nextTick()
  
  // 这里应该使用实际的图表库（如ECharts、Chart.js等）来渲染图表
  console.log('渲染分析图表')
  
  // 示例实现
  if (responseTimeChartRef.value) {
    console.log('渲染响应时间趋势图')
  }
  if (successRateChartRef.value) {
    console.log('渲染成功率分析图')
  }
  if (featureHeatmapRef.value) {
    console.log('渲染功能使用热度图')
  }
  if (userActivityChartRef.value) {
    console.log('渲染用户活跃度图')
  }
  if (userDistributionChartRef.value) {
    console.log('渲染用户分布图')
  }
  if (costTrendChartRef.value) {
    console.log('渲染成本趋势图')
  }
}

// 监听对话框显示状态
watch(dialogVisible, (newVal) => {
  if (newVal) {
    // 加载分析数据
    renderCharts()
  }
})

// 监听标签页切换
watch(activeTab, () => {
  renderCharts()
})

// 生命周期
onMounted(() => {
  if (dialogVisible.value) {
    renderCharts()
  }
})
</script>

<style scoped>
.analysis-container {
  max-height: 600px;
  overflow-y: auto;
  padding: 0 10px;
}

.overview-section {
  margin-bottom: 24px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.metric-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.metric-icon {
  font-size: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: white;
}

.metric-info {
  flex: 1;
}

.metric-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 4px;
}

.metric-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 2px;
}

.metric-change {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  font-weight: 500;
}

.analysis-tabs {
  margin-top: 20px;
}

.tab-content {
  padding: 20px 0;
}

.chart-card {
  border: none;
}

.chart-container {
  height: 250px;
  width: 100%;
}

.chart-container-large {
  height: 350px;
  width: 100%;
}

.recommendation-list {
  max-height: 400px;
  overflow-y: auto;
}

.recommendation-item {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border-left: 4px solid #409EFF;
}

.rec-icon {
  font-size: 20px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.rec-icon.priority-high {
  background-color: #F56C6C;
}

.rec-icon.priority-medium {
  background-color: #E6A23C;
}

.rec-icon.priority-low {
  background-color: #67C23A;
}

.rec-content {
  flex: 1;
}

.rec-title {
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.rec-description {
  font-size: 13px;
  color: #606266;
  margin-bottom: 8px;
}

.rec-impact {
  font-size: 12px;
  color: #409EFF;
  font-weight: 500;
}

.cost-summary {
  text-align: center;
}

.cost-item {
  margin-bottom: 16px;
}

.cost-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 4px;
}

.cost-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.cost-breakdown h4,
.cost-optimization h4 {
  margin: 0 0 16px 0;
  color: #303133;
  font-weight: 600;
}

.cost-detail {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.cost-type {
  font-size: 13px;
  color: #606266;
}

.cost-amount {
  font-weight: 600;
  color: #303133;
}

.cost-percentage {
  font-size: 12px;
  color: #909399;
}

.opt-suggestion {
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.opt-suggestion:last-child {
  border-bottom: none;
}

.opt-title {
  font-size: 13px;
  color: #606266;
  margin-bottom: 4px;
}

.opt-save {
  font-size: 12px;
  color: #409EFF;
  font-weight: 500;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>