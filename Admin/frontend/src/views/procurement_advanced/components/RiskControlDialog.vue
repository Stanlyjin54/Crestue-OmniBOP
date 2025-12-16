<template>
  <div class="risk-control-dialog">
    <el-dialog
      v-model="dialogVisible"
      title="采购风险控制"
      width="1400px"
      :before-close="handleClose"
    >
      <!-- 筛选条件 -->
      <div class="risk-filters">
        <el-row :gutter="20">
          <el-col :span="4">
            <el-select v-model="riskCategory" placeholder="风险类别" @change="handleRiskCategoryChange">
              <el-option
                v-for="option in riskCategories"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-select v-model="riskLevel" placeholder="风险等级" @change="handleRiskLevelChange">
              <el-option
                v-for="option in riskLevels"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-select v-model="riskStatus" placeholder="风险状态" @change="handleRiskStatusChange">
              <el-option
                v-for="option in riskStatuses"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-select v-model="supplierFilter" placeholder="供应商筛选" clearable @change="handleSupplierChange">
              <el-option
                v-for="supplier in supplierOptions"
                :key="supplier.value"
                :label="supplier.label"
                :value="supplier.value"
              />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-select v-model="timeRange" placeholder="时间范围" @change="handleTimeRangeChange">
              <el-option
                v-for="option in timeRanges"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-button-group>
              <el-button @click="handleRefresh">
                <el-icon><Refresh /></el-icon>
                刷新
              </el-button>
              <el-button @click="handleExport">
                <el-icon><Download /></el-icon>
                导出
              </el-button>
              <el-button @click="handleRiskAssessment">
                <el-icon><Warning /></el-icon>
                风险评估
              </el-button>
            </el-button-group>
          </el-col>
        </el-row>
      </div>

      <div class="risk-content">
        <!-- 风险概览 -->
        <div class="risk-overview">
          <h3>
            <el-icon><Warning /></el-icon>
            风险概览
          </h3>
          <el-row :gutter="20">
            <el-col :span="6" v-for="metric in riskMetrics" :key="metric.key">
              <div class="metric-card">
                <div class="metric-icon" :style="{ backgroundColor: metric.color }">
                  <el-icon><component :is="metric.icon" /></el-icon>
                </div>
                <div class="metric-content">
                  <div class="metric-value">{{ metric.value }}</div>
                  <div class="metric-label">{{ metric.label }}</div>
                  <div class="metric-trend" :class="metric.trend > 0 ? 'negative' : 'positive'">
                    <el-icon><ArrowUp v-if="metric.trend > 0" /><ArrowDown v-else /></el-icon>
                    {{ Math.abs(metric.trend) }}%
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 风险分析图表 -->
        <div class="risk-charts-section">
          <el-tabs v-model="activeChartTab" type="border-card">
            <!-- 风险分布 -->
            <el-tab-pane label="风险分布" name="distribution">
              <div class="chart-container">
                <div class="chart-header">
                  <h4>风险分布分析</h4>
                  <el-radio-group v-model="distributionChartType" size="small">
                    <el-radio-button label="category">按类别</el-radio-button>
                    <el-radio-button label="level">按等级</el-radio-button>
                    <el-radio-button label="source">按来源</el-radio-button>
                  </el-radio-group>
                </div>
                <div class="chart-content">
                  <div v-if="distributionChartType === 'category'" class="category-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><PieChart /></el-icon>
                        <p>风险类别分布图表</p>
                        <small>展示各类风险的占比情况</small>
                      </div>
                    </div>
                  </div>
                  <div v-else-if="distributionChartType === 'level'" class="level-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><Histogram /></el-icon>
                        <p>风险等级分布图表</p>
                        <small>展示高、中、低风险分布</small>
                      </div>
                    </div>
                  </div>
                  <div v-else class="source-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><DataLine /></el-icon>
                        <p>风险来源分布图表</p>
                        <small>展示不同来源的风险分布</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 风险趋势 -->
            <el-tab-pane label="风险趋势" name="trend">
              <div class="chart-container">
                <div class="chart-header">
                  <h4>风险趋势分析</h4>
                  <el-radio-group v-model="trendChartType" size="small">
                    <el-radio-button label="overall">总体趋势</el-radio-button>
                    <el-radio-button label="category">分类趋势</el-radio-button>
                    <el-radio-button label="prediction">风险预测</el-radio-button>
                  </el-radio-group>
                </div>
                <div class="chart-content">
                  <div v-if="trendChartType === 'overall'" class="overall-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><TrendCharts /></el-icon>
                        <p>总体风险趋势图表</p>
                        <small>展示风险数量的时间变化</small>
                      </div>
                    </div>
                  </div>
                  <div v-else-if="trendChartType === 'category'" class="category-trend-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><DataLine /></el-icon>
                        <p>分类风险趋势图表</p>
                        <small>展示不同类别风险的变化</small>
                      </div>
                    </div>
                  </div>
                  <div v-else class="prediction-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><MagicStick /></el-icon>
                        <p>风险预测图表</p>
                        <small>基于历史数据预测未来风险</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 风险热力图 -->
            <el-tab-pane label="风险热力图" name="heatmap">
              <div class="chart-container">
                <div class="chart-header">
                  <h4>风险热力分析</h4>
                  <el-radio-group v-model="heatmapChartType" size="small">
                    <el-radio-button label="supplier">供应商风险</el-radio-button>
                    <el-radio-button label="category">产品风险</el-radio-button>
                    <el-radio-button label="region">地区风险</el-radio-button>
                  </el-radio-group>
                </div>
                <div class="chart-content">
                  <div v-if="heatmapChartType === 'supplier'" class="supplier-heatmap">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><Grid /></el-icon>
                        <p>供应商风险热力图</p>
                        <small>展示各供应商的风险等级</small>
                      </div>
                    </div>
                  </div>
                  <div v-else-if="heatmapChartType === 'category'" class="category-heatmap">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><Grid /></el-icon>
                        <p>产品风险热力图</p>
                        <small>展示各类产品的风险分布</small>
                      </div>
                    </div>
                  </div>
                  <div v-else class="region-heatmap">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><Grid /></el-icon>
                        <p>地区风险热力图</p>
                        <small>展示不同地区的风险分布</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>

        <!-- 风险列表 -->
        <div class="risk-list-section">
          <div class="section-header">
            <h3>
              <el-icon><List /></el-icon>
              风险清单
            </h3>
            <div class="section-actions">
              <el-button type="primary" @click="handleAddRisk">
                <el-icon><Plus /></el-icon>
                添加风险
              </el-button>
              <el-button @click="handleBatchActions">
                <el-icon><Operation /></el-icon>
                批量操作
              </el-button>
            </div>
          </div>
          
          <el-table 
            :data="riskData" 
            stripe 
            style="width: 100%" 
            :height="400"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column prop="riskId" label="风险ID" width="100" />
            <el-table-column prop="title" label="风险标题" width="200" show-overflow-tooltip />
            <el-table-column prop="category" label="风险类别" width="120">
              <template #default="{ row }">
                <el-tag :type="getCategoryTagType(row.category)">
                  {{ row.category }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="level" label="风险等级" width="100">
              <template #default="{ row }">
                <el-tag :type="getLevelTagType(row.level)">
                  {{ row.level }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="probability" label="发生概率" width="120">
              <template #default="{ row }">
                <el-progress 
                  :percentage="row.probability" 
                  :stroke-width="8"
                  :color="getProgressColor(row.probability)"
                />
              </template>
            </el-table-column>
            <el-table-column prop="impact" label="影响程度" width="120">
              <template #default="{ row }">
                <el-progress 
                  :percentage="row.impact" 
                  :stroke-width="8"
                  :color="getProgressColor(row.impact)"
                />
              </template>
            </el-table-column>
            <el-table-column prop="supplier" label="相关供应商" width="150" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusTagType(row.status)">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="owner" label="责任人" width="120" />
            <el-table-column prop="dueDate" label="截止日期" width="120" />
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button size="small" @click="handleViewRisk(row)">
                  查看
                </el-button>
                <el-button size="small" @click="handleEditRisk(row)">
                  编辑
                </el-button>
                <el-button size="small" type="success" @click="handleResolveRisk(row)">
                  解决
                </el-button>
                <el-dropdown>
                  <el-button size="small">
                    更多<el-icon class="el-icon--right"><ArrowDown /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item @click="handleAssignRisk(row)">分配</el-dropdown-item>
                      <el-dropdown-item @click="handleCommentRisk(row)">评论</el-dropdown-item>
                      <el-dropdown-item @click="handleDocumentRisk(row)">文档</el-dropdown-item>
                      <el-dropdown-item @click="handleDeleteRisk(row)" divided>删除</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <div class="pagination-container">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[10, 20, 50, 100]"
              :total="totalRisks"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>

        <!-- 风险预警 -->
        <div class="risk-alerts-section">
          <h3>
            <el-icon><Bell /></el-icon>
            风险预警
          </h3>
          <div class="alerts-container">
            <div 
              v-for="alert in riskAlerts" 
              :key="alert.id" 
              class="alert-card"
              :class="alert.severity"
            >
              <div class="alert-header">
                <el-icon :class="['alert-icon', alert.severity]">
                  <component :is="getAlertIcon(alert.severity)" />
                </el-icon>
                <span class="alert-title">{{ alert.title }}</span>
                <span class="alert-time">{{ alert.time }}</span>
              </div>
              <div class="alert-content">
                <p>{{ alert.message }}</p>
                <div class="alert-actions">
                  <el-button size="small" @click="handleAlertAction(alert, 'view')">
                    查看详情
                  </el-button>
                  <el-button size="small" @click="handleAlertAction(alert, 'acknowledge')">
                    确认
                  </el-button>
                  <el-button size="small" @click="handleAlertAction(alert, 'escalate')">
                    升级
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 风险应对措施 -->
        <div class="risk-mitigation-section">
          <h3>
            <el-icon><Shield /></el-icon>
            风险应对措施
          </h3>
          <div class="mitigation-grid">
            <div v-for="mitigation in mitigationPlans" :key="mitigation.id" class="mitigation-card">
              <div class="mitigation-header">
                <h4>{{ mitigation.title }}</h4>
                <el-tag :type="mitigation.status === 'completed' ? 'success' : 'warning'">
                  {{ mitigation.status === 'completed' ? '已完成' : '进行中' }}
                </el-tag>
              </div>
              <div class="mitigation-content">
                <p class="mitigation-description">{{ mitigation.description }}</p>
                <div class="mitigation-metrics">
                  <div class="metric">
                    <span class="metric-label">成本节约:</span>
                    <span class="metric-value">¥{{ mitigation.costSaving.toLocaleString() }}</span>
                  </div>
                  <div class="metric">
                    <span class="metric-label">完成进度:</span>
                    <el-progress :percentage="mitigation.progress" :stroke-width="6" />
                  </div>
                </div>
                <div class="mitigation-actions">
                  <el-button size="small" @click="handleMitigationAction(mitigation, 'view')">
                    查看详情
                  </el-button>
                  <el-button size="small" @click="handleMitigationAction(mitigation, 'edit')">
                    编辑
                  </el-button>
                  <el-button size="small" type="success" @click="handleMitigationAction(mitigation, 'complete')">
                    完成
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Refresh, Download, Warning, PieChart, Histogram, DataLine, TrendCharts, 
  MagicStick, Grid, List, Plus, Operation, Bell, Shield, ArrowDown
} from '@element-plus/icons-vue'
import { useProcurementAdvanced } from '@/composables/useProcurementAdvanced'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:visible'])

// 组合式函数
const {
  getRiskAnalysis,
  exportRiskData,
  createRiskAssessment
} = useProcurementAdvanced()

// 响应式数据
const dialogVisible = computed({
  get: () => props.visible,
  set: (val) => emit('update:visible', val)
})

// 筛选条件
const riskCategory = ref('')
const riskLevel = ref('')
const riskStatus = ref('')
const supplierFilter = ref('')
const timeRange = ref('month')

// 图表配置
const activeChartTab = ref('distribution')
const distributionChartType = ref('category')
const trendChartType = ref('overall')
const heatmapChartType = ref('supplier')

// 表格配置
const currentPage = ref(1)
const pageSize = ref(20)
const totalRisks = ref(0)
const selectedRisks = ref([])

// 配置数据
const riskCategories = [
  { label: '全部类别', value: '' },
  { label: '财务风险', value: 'financial' },
  { label: '供应链风险', value: 'supply_chain' },
  { label: '质量风险', value: 'quality' },
  { label: '合规风险', value: 'compliance' },
  { label: '操作风险', value: 'operational' },
  { label: '市场风险', value: 'market' }
]

const riskLevels = [
  { label: '全部等级', value: '' },
  { label: '高风险', value: 'high' },
  { label: '中风险', value: 'medium' },
  { label: '低风险', value: 'low' }
]

const riskStatuses = [
  { label: '全部状态', value: '' },
  { label: '监控中', value: 'monitoring' },
  { label: '处理中', value: 'processing' },
  { label: '已解决', value: 'resolved' },
  { label: '已关闭', value: 'closed' }
]

const supplierOptions = ref([
  { label: '供应商A', value: 'supplier_a' },
  { label: '供应商B', value: 'supplier_b' },
  { label: '供应商C', value: 'supplier_c' }
])

const timeRanges = [
  { label: '最近一周', value: 'week' },
  { label: '最近一月', value: 'month' },
  { label: '最近三月', value: 'quarter' },
  { label: '最近一年', value: 'year' }
]

// 数据
const riskMetrics = ref([
  {
    key: 'totalRisks',
    label: '总风险数',
    value: 23,
    trend: 15.2,
    color: '#f56c6c',
    icon: 'Warning'
  },
  {
    key: 'highRisks',
    label: '高风险',
    value: 5,
    trend: 25.0,
    color: '#e6a23c',
    icon: 'WarningFilled'
  },
  {
    key: 'resolvedRisks',
    label: '已解决',
    value: 18,
    trend: -8.3,
    color: '#67c23a',
    icon: 'CircleCheck'
  },
  {
    key: 'riskScore',
    label: '风险评分',
    value: 6.8,
    trend: 12.1,
    color: '#409eff',
    icon: 'TrendCharts'
  }
])

const riskData = ref([
  {
    riskId: 'R001',
    title: '供应商C财务状况恶化',
    category: '财务风险',
    level: '高',
    probability: 85,
    impact: 90,
    supplier: '供应商C',
    status: 'monitoring',
    owner: '张三',
    dueDate: '2024-02-15'
  },
  {
    riskId: 'R002',
    title: '主要原材料价格波动',
    category: '市场风险',
    level: '中',
    probability: 65,
    impact: 70,
    supplier: '供应商A',
    status: 'processing',
    owner: '李四',
    dueDate: '2024-02-28'
  }
])

const riskAlerts = ref([
  {
    id: 1,
    severity: 'critical',
    title: '高风险预警',
    message: '供应商C的风险等级已升级为高风险，建议立即采取应对措施',
    time: '2小时前'
  },
  {
    id: 2,
    severity: 'warning',
    title: '风险趋势预警',
    message: '本月财务风险数量环比增长30%，需要关注',
    time: '4小时前'
  }
])

const mitigationPlans = ref([
  {
    id: 1,
    title: '建立供应商风险预警系统',
    description: '通过系统自动化监控供应商财务指标，及时发现潜在风险',
    status: 'processing',
    costSaving: 150000,
    progress: 75
  },
  {
    id: 2,
    title: '多元化供应商策略',
    description: '增加备用供应商数量，降低对单一供应商的依赖',
    status: 'completed',
    costSaving: 200000,
    progress: 100
  }
])

// 工具方法
const getCategoryTagType = (category) => {
  const typeMap = {
    '财务风险': 'danger',
    '供应链风险': 'warning',
    '质量风险': 'primary',
    '合规风险': 'info',
    '操作风险': 'success',
    '市场风险': 'warning'
  }
  return typeMap[category] || 'info'
}

const getLevelTagType = (level) => {
  const levelMap = {
    '高': 'danger',
    '中': 'warning',
    '低': 'success'
  }
  return levelMap[level] || 'info'
}

const getStatusTagType = (status) => {
  const statusMap = {
    'monitoring': 'warning',
    'processing': 'primary',
    'resolved': 'success',
    'closed': 'info'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    'monitoring': '监控中',
    'processing': '处理中',
    'resolved': '已解决',
    'closed': '已关闭'
  }
  return statusMap[status] || status
}

const getProgressColor = (percentage) => {
  if (percentage >= 80) return '#f56c6c'
  if (percentage >= 60) return '#e6a23c'
  if (percentage >= 40) return '#409eff'
  return '#67c23a'
}

const getAlertIcon = (severity) => {
  const iconMap = {
    'critical': 'WarningFilled',
    'warning': 'Warning',
    'info': 'InfoFilled'
  }
  return iconMap[severity] || 'InfoFilled'
}

// 事件处理
const handleClose = () => {
  dialogVisible.value = false
}

const handleRiskCategoryChange = () => {
  loadRiskData()
}

const handleRiskLevelChange = () => {
  loadRiskData()
}

const handleRiskStatusChange = () => {
  loadRiskData()
}

const handleSupplierChange = () => {
  loadRiskData()
}

const handleTimeRangeChange = () => {
  loadRiskData()
}

const handleRefresh = async () => {
  try {
    await loadRiskData()
    ElMessage.success('数据刷新成功')
  } catch (error) {
    ElMessage.error('数据刷新失败')
  }
}

const handleExport = async () => {
  try {
    await exportRiskData({
      riskCategory: riskCategory.value,
      riskLevel: riskLevel.value,
      riskStatus: riskStatus.value,
      supplierFilter: supplierFilter.value,
      timeRange: timeRange.value
    })
    ElMessage.success('数据导出成功')
  } catch (error) {
    ElMessage.error('数据导出失败')
  }
}

const handleRiskAssessment = async () => {
  try {
    await createRiskAssessment()
    ElMessage.success('风险评估完成')
  } catch (error) {
    ElMessage.error('风险评估失败')
  }
}

const handleAddRisk = () => {
  console.log('添加风险')
  ElMessage.info('添加风险功能')
}

const handleBatchActions = () => {
  console.log('批量操作', selectedRisks.value)
  ElMessage.info('批量操作功能')
}

const handleSelectionChange = (selection) => {
  selectedRisks.value = selection
}

const handleViewRisk = (risk) => {
  console.log('查看风险', risk)
  ElMessage.info(`查看风险: ${risk.title}`)
}

const handleEditRisk = (risk) => {
  console.log('编辑风险', risk)
  ElMessage.info(`编辑风险: ${risk.title}`)
}

const handleResolveRisk = (risk) => {
  ElMessageBox.confirm('确定要解决这个风险吗？', '确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    risk.status = 'resolved'
    ElMessage.success('风险已解决')
  }).catch(() => {})
}

const handleAssignRisk = (risk) => {
  console.log('分配风险', risk)
  ElMessage.info('分配风险功能')
}

const handleCommentRisk = (risk) => {
  console.log('评论风险', risk)
  ElMessage.info('评论风险功能')
}

const handleDocumentRisk = (risk) => {
  console.log('文档风险', risk)
  ElMessage.info('文档风险功能')
}

const handleDeleteRisk = (risk) => {
  ElMessageBox.confirm('确定要删除这个风险吗？', '确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = riskData.value.findIndex(item => item.riskId === risk.riskId)
    if (index > -1) {
      riskData.value.splice(index, 1)
      ElMessage.success('风险已删除')
    }
  }).catch(() => {})
}

const handleAlertAction = (alert, action) => {
  console.log(`风险预警操作: ${action}`, alert)
  ElMessage.info(`执行操作: ${action}`)
}

const handleMitigationAction = (mitigation, action) => {
  console.log(`应对措施操作: ${action}`, mitigation)
  ElMessage.info(`执行操作: ${action}`)
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadRiskData()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadRiskData()
}

const loadRiskData = async () => {
  try {
    // 这里应该调用实际的API
    const params = {
      riskCategory: riskCategory.value,
      riskLevel: riskLevel.value,
      riskStatus: riskStatus.value,
      supplierFilter: supplierFilter.value,
      timeRange: timeRange.value,
      page: currentPage.value,
      pageSize: pageSize.value
    }
    
    // 模拟数据加载
    console.log('加载风险数据:', params)
    totalRisks.value = 50 // 模拟总数据量
  } catch (error) {
    console.error('加载风险数据失败:', error)
  }
}

// 初始化
onMounted(() => {
  loadRiskData()
})
</script>

<style scoped>
.risk-control-dialog {
  .risk-filters {
    margin-bottom: 20px;
    padding: 16px;
    background-color: #f5f7fa;
    border-radius: 6px;
  }

  .risk-content {
    .risk-overview {
      margin-bottom: 30px;

      h3 {
        margin-bottom: 20px;
        color: #303133;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 8px;
      }

      .metric-card {
        display: flex;
        align-items: center;
        padding: 20px;
        background: white;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        transition: transform 0.2s;

        &:hover {
          transform: translateY(-2px);
        }

        .metric-icon {
          width: 48px;
          height: 48px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          color: white;
          font-size: 20px;
        }

        .metric-content {
          flex: 1;

          .metric-value {
            font-size: 24px;
            font-weight: bold;
            color: #303133;
            margin-bottom: 4px;
          }

          .metric-label {
            font-size: 14px;
            color: #606266;
            margin-bottom: 4px;
          }

          .metric-trend {
            font-size: 12px;
            display: flex;
            align-items: center;
            gap: 4px;

            &.positive {
              color: #67c23a;
            }

            &.negative {
              color: #f56c6c;
            }
          }
        }
      }
    }

    .risk-charts-section {
      margin-bottom: 30px;

      .chart-container {
        .chart-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;

          h4 {
            color: #303133;
            font-weight: 600;
          }
        }

        .chart-content {
          .chart-canvas {
            height: 400px;
            background: #f5f7fa;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 2px dashed #dcdfe6;

            .placeholder-chart {
              text-align: center;
              color: #909399;

              .el-icon {
                font-size: 48px;
                margin-bottom: 16px;
              }

              p {
                font-size: 18px;
                font-weight: 600;
                margin-bottom: 8px;
              }

              small {
                font-size: 14px;
              }
            }
          }
        }
      }
    }

    .risk-list-section {
      margin-bottom: 30px;

      .section-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;

        h3 {
          color: #303133;
          font-weight: 600;
          display: flex;
          align-items: center;
          gap: 8px;
        }

        .section-actions {
          display: flex;
          gap: 12px;
        }
      }

      .pagination-container {
        margin-top: 20px;
        display: flex;
        justify-content: center;
      }
    }

    .risk-alerts-section {
      margin-bottom: 30px;

      h3 {
        margin-bottom: 20px;
        color: #303133;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 8px;
      }

      .alerts-container {
        display: flex;
        flex-direction: column;
        gap: 12px;

        .alert-card {
          background: white;
          border-radius: 8px;
          padding: 16px;
          border-left: 4px solid #909399;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

          &.critical {
            border-left-color: #f56c6c;
            background-color: #fef0f0;
          }

          &.warning {
            border-left-color: #e6a23c;
            background-color: #fdf6ec;
          }

          &.info {
            border-left-color: #409eff;
            background-color: #f0f9ff;
          }

          .alert-header {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 12px;

            .alert-icon {
              font-size: 20px;

              &.critical {
                color: #f56c6c;
              }

              &.warning {
                color: #e6a23c;
              }

              &.info {
                color: #409eff;
              }
            }

            .alert-title {
              font-weight: 600;
              color: #303133;
              flex: 1;
            }

            .alert-time {
              font-size: 12px;
              color: #909399;
            }
          }

          .alert-content {
            p {
              margin: 0 0 12px 0;
              color: #606266;
              line-height: 1.6;
            }

            .alert-actions {
              display: flex;
              gap: 8px;
            }
          }
        }
      }
    }

    .risk-mitigation-section {
      h3 {
        margin-bottom: 20px;
        color: #303133;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 8px;
      }

      .mitigation-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
        gap: 20px;

        .mitigation-card {
          background: white;
          border-radius: 8px;
          padding: 20px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
          transition: transform 0.2s;

          &:hover {
            transform: translateY(-2px);
          }

          .mitigation-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 16px;

            h4 {
              color: #303133;
              font-weight: 600;
              margin: 0;
            }
          }

          .mitigation-content {
            .mitigation-description {
              margin: 0 0 16px 0;
              color: #606266;
              line-height: 1.6;
            }

            .mitigation-metrics {
              margin-bottom: 16px;

              .metric {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 8px;

                .metric-label {
                  font-size: 14px;
                  color: #909399;
                }

                .metric-value {
                  font-size: 16px;
                  font-weight: 600;
                  color: #67c23a;
                }
              }
            }

            .mitigation-actions {
              display: flex;
              gap: 8px;
              flex-wrap: wrap;
            }
          }
        }
      }
    }
  }
}

:deep(.el-dialog__body) {
  padding: 20px 30px;
}

:deep(.el-tabs--border-card) {
  border: 1px solid #e4e7ed;
  border-radius: 6px;
}

:deep(.el-progress-bar__outer) {
  border-radius: 4px;
}

:deep(.el-dropdown-menu) {
  min-width: 120px;
}
</style>