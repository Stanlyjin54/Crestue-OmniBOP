<template>
  <div class="social-media-marketing">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">
            <el-icon><Share /></el-icon>
            社会媒体营销
          </h1>
          <p class="page-description">管理和分析多平台社交媒体营销活动，优化品牌推广效果</p>
        </div>
        <div class="action-section">
          <el-button
            v-if="hasPermission('social_media:create')"
            type="primary"
            :icon="Plus"
            @click="showCreateDialog = true"
          >
            创建活动
          </el-button>
          <el-button
            v-if="hasPermission('social_media:analytics')"
            type="default"
            :icon="DataAnalysis"
            @click="showAnalyticsDialog = true"
          >
            数据分析
          </el-button>
          <el-button
            v-if="hasPermission('social_media:content')"
            type="default"
            :icon="Collection"
            @click="showContentDialog = true"
          >
            内容库
          </el-button>
          <el-button
            v-if="hasPermission('social_media:export')"
            type="default"
            :icon="Download"
            @click="handleExport"
          >
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <div class="breadcrumb-section">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item to="/admin">首页</el-breadcrumb-item>
        <el-breadcrumb-item>社会媒体营销</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-section">
      <el-row :gutter="20">
        <el-col :span="6" v-for="(stat, key) in statisticsCards" :key="key">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon" :style="{ color: stat.color }">
                <el-icon><component :is="stat.icon" /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stat.value }}</div>
                <div class="stat-label">{{ stat.label }}</div>
                <div class="stat-unit">{{ stat.unit }}</div>
              </div>
            </div>
            <div v-if="stat.trend" class="stat-trend">
              <el-icon :color="stat.trend.color">
                <component :is="stat.trend.icon" />
              </el-icon>
              <span :style="{ color: stat.trend.color }">{{ stat.trend.value }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表分析区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card class="chart-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span>平台分布</span>
                <el-button-group>
                  <el-button
                    v-for="period in timePeriods"
                    :key="period.value"
                    :type="selectedPeriod === period.value ? 'primary' : 'default'"
                    size="small"
                    @click="selectedPeriod = period.value"
                  >
                    {{ period.label }}
                  </el-button>
                </el-button-group>
              </div>
            </template>
            <div ref="platformChartRef" class="chart-container"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="chart-card" shadow="hover">
            <template #header>
              <span>趋势分析</span>
            </template>
            <div ref="trendChartRef" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="filter-section">
      <el-card shadow="never">
        <el-form :model="filterForm" :inline="true" class="filter-form">
          <el-form-item label="活动名称">
            <el-input
              v-model="filterForm.keyword"
              placeholder="输入活动名称或关键词"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="平台">
            <el-select
              v-model="filterForm.platform"
              placeholder="选择平台"
              clearable
              style="width: 150px"
            >
              <el-option
                v-for="platform in socialMediaMarketing.campaignConfig.platforms"
                :key="platform.id"
                :label="platform.name"
                :value="platform.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="活动类型">
            <el-select
              v-model="filterForm.campaignType"
              placeholder="选择活动类型"
              clearable
              style="width: 150px"
            >
              <el-option
                v-for="type in socialMediaMarketing.campaignConfig.campaignTypes"
                :key="type.value"
                :label="type.label"
                :value="type.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="filterForm.status"
              placeholder="选择状态"
              clearable
              style="width: 120px"
            >
              <el-option
                v-for="status in socialMediaMarketing.campaignConfig.statuses"
                :key="status.value"
                :label="status.label"
                :value="status.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="filterForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              style="width: 240px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleSearch">
              搜索
            </el-button>
            <el-button :icon="Refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 营销活动列表表格 -->
    <div class="table-section">
      <el-card shadow="never">
        <template #header>
          <div class="card-header">
            <span>营销活动</span>
            <div class="table-actions">
              <el-button
                v-if="hasPermission('social_media:delete')"
                type="danger"
                size="small"
                :icon="Delete"
                :disabled="!selectedRows.length"
                @click="handleBatchDelete"
              >
                批量删除 ({{ selectedRows.length }})
              </el-button>
            </div>
          </div>
        </template>

        <el-table
          ref="tableRef"
          v-loading="socialMediaMarketing.loading"
          :data="socialMediaMarketing.filteredCampaigns"
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
          class="social-table"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column
            prop="name"
            label="活动名称"
            min-width="200"
            show-overflow-tooltip
          />
          <el-table-column
            prop="type"
            label="活动类型"
            width="120"
          >
            <template #default="{ row }">
              <el-tag size="small">
                {{ getCampaignTypeLabel(row.type) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="platforms"
            label="平台"
            width="150"
          >
            <template #default="{ row }">
              <div class="platform-tags">
                <el-tag
                  v-for="platform in row.platforms"
                  :key="platform"
                  size="small"
                  :style="{ 
                    backgroundColor: getPlatformColor(platform),
                    borderColor: getPlatformColor(platform),
                    color: 'white'
                  }"
                >
                  {{ getPlatformName(platform) }}
                </el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="status"
            label="状态"
            width="100"
          >
            <template #default="{ row }">
              <el-tag
                :type="socialMediaMarketing.getStatusType(row.status)"
                size="small"
              >
                {{ getStatusLabel(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="budget"
            label="预算"
            width="120"
          >
            <template #default="{ row }">
              {{ socialMediaMarketing.formatBudget(row.budget) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="metrics.reach"
            label="触达"
            width="100"
            sortable="custom"
          >
            <template #default="{ row }">
              {{ socialMediaMarketing.formatNumber(row.metrics?.reach || 0) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="metrics.engagement"
            label="互动"
            width="100"
            sortable="custom"
          >
            <template #default="{ row }">
              {{ socialMediaMarketing.formatNumber(row.metrics?.engagement || 0) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="metrics.ctr"
            label="点击率"
            width="100"
            sortable="custom"
          >
            <template #default="{ row }">
              {{ (row.metrics?.ctr || 0).toFixed(2) }}%
            </template>
          </el-table-column>
          <el-table-column
            prop="startDate"
            label="开始日期"
            width="120"
            sortable="custom"
          />
          <el-table-column
            prop="endDate"
            label="结束日期"
            width="120"
            sortable="custom"
          />
          <el-table-column
            label="操作"
            width="220"
            fixed="right"
          >
            <template #default="{ row }">
              <el-button
                size="small"
                :icon="View"
                @click="handleViewDetail(row)"
              >
                查看
              </el-button>
              <el-button
                size="small"
                :icon="Edit"
                @click="handleEdit(row)"
              >
                编辑
              </el-button>
              <el-dropdown @command="(command) => handleCommand(command, row)">
                <el-button size="small">
                  更多<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="analytics">数据分析</el-dropdown-item>
                    <el-dropdown-item command="content">内容管理</el-dropdown-item>
                    <el-dropdown-item 
                      v-if="row.status === 'active'"
                      command="pause"
                    >
                      暂停活动
                    </el-dropdown-item>
                    <el-dropdown-item 
                      v-if="row.status === 'paused'"
                      command="resume"
                    >
                      恢复活动
                    </el-dropdown-item>
                    <el-dropdown-item 
                      v-if="row.status !== 'completed' && row.status !== 'cancelled'"
                      command="complete"
                    >
                      完成活动
                    </el-dropdown-item>
                    <el-dropdown-item 
                      v-if="hasPermission('social_media:delete')"
                      command="delete"
                      divided
                    >
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-section">
          <el-pagination
            v-model:current-page="socialMediaMarketing.pagination.currentPage"
            v-model:page-size="socialMediaMarketing.pagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="socialMediaMarketing.pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handlePageSizeChange"
            @current-change="handleCurrentPageChange"
          />
        </div>
      </el-card>
    </div>

    <!-- 创建/编辑活动对话框 -->
    <CampaignFormDialog
      v-model="showCreateDialog"
      :campaign="selectedCampaign"
      @updated="handleCampaignUpdated"
    />

    <!-- 数据分析对话框 -->
    <AnalyticsDialog
      v-model="showAnalyticsDialog"
      :data="analyticsData"
    />

    <!-- 活动详情对话框 -->
    <CampaignDetailDialog
      v-model="showDetailDialog"
      :campaign="selectedCampaign"
      @updated="handleCampaignUpdated"
    />

    <!-- 内容管理对话框 -->
    <ContentLibraryDialog
      v-model="showContentDialog"
      :campaign="selectedCampaign"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Share, Plus, DataAnalysis, Download, Search, Refresh, Delete,
  View, Edit, ArrowDown, Collection
} from '@element-plus/icons-vue'
import { useSocialMediaMarketing } from '@/composables/useSocialMediaMarketing'

// 组合式函数
const socialMediaMarketing = useSocialMediaMarketing()

// 响应式数据
const tableRef = ref()
const selectedRows = ref([])
const selectedCampaign = ref(null)
const showCreateDialog = ref(false)
const showAnalyticsDialog = ref(false)
const showDetailDialog = ref(false)
const showContentDialog = ref(false)
const analyticsData = ref({})

// 筛选表单
const filterForm = ref({
  keyword: '',
  platform: '',
  campaignType: '',
  status: '',
  dateRange: []
})

// 时间周期选项
const timePeriods = [
  { label: '7天', value: '7d' },
  { label: '30天', value: '30d' },
  { label: '90天', value: '90d' }
]

const selectedPeriod = ref('30d')

// 统计卡片配置
const statisticsCards = computed(() => {
  const analytics = socialMediaMarketing.analytics.overview || {}
  return {
    totalCampaigns: {
      label: '总活动数',
      value: analytics.totalCampaigns || 0,
      icon: 'Campaign',
      color: '#409EFF',
      unit: '个',
      trend: {
        icon: 'ArrowUp',
        color: '#67C23A',
        value: '+12.5%'
      }
    },
    activeCampaigns: {
      label: '活跃活动',
      value: analytics.activeCampaigns || 0,
      icon: 'VideoPlay',
      color: '#67C23A',
      unit: '个',
      trend: {
        icon: 'ArrowUp',
        color: '#67C23A',
        value: '+8.3%'
      }
    },
    totalReach: {
      label: '总触达',
      value: socialMediaMarketing.formatNumber(analytics.totalReach || 0),
      icon: 'View',
      color: '#E6A23C',
      unit: '人次',
      trend: {
        icon: 'ArrowUp',
        color: '#67C23A',
        value: '+15.2%'
      }
    },
    totalEngagement: {
      label: '总互动',
      value: socialMediaMarketing.formatNumber(analytics.totalEngagement || 0),
      icon: 'ChatDotRound',
      color: '#F56C6C',
      unit: '次',
      trend: {
        icon: 'ArrowUp',
        color: '#67C23A',
        value: '+22.1%'
      }
    },
    averageEngagementRate: {
      label: '平均互动率',
      value: (analytics.averageEngagementRate || 0).toFixed(2),
      icon: 'Percentage',
      color: '#909399',
      unit: '%',
      trend: {
        icon: 'ArrowUp',
        color: '#67C23A',
        value: '+3.8%'
      }
    },
    totalBudget: {
      label: '总预算',
      value: socialMediaMarketing.formatBudget(analytics.totalBudget || 0),
      icon: 'Money',
      color: '#00C853',
      unit: '',
      trend: {
        icon: 'ArrowUp',
        color: '#67C23A',
        value: '+18.7%'
      }
    }
  }
})

// 权限检查函数
const hasPermission = (permission) => {
  // 模拟权限检查
  return true
}

// 工具方法
const getCampaignTypeLabel = (type) => {
  const typeMap = {
    awareness: '品牌认知',
    engagement: '用户互动',
    conversion: '转化推广',
    retention: '用户留存',
    viral: '病毒营销'
  }
  return typeMap[type] || type
}

const getStatusLabel = (status) => {
  const statusMap = {
    draft: '草稿',
    scheduled: '已排期',
    active: '进行中',
    paused: '已暂停',
    completed: '已完成',
    cancelled: '已取消'
  }
  return statusMap[status] || status
}

const getPlatformName = (platformId) => {
  const platform = socialMediaMarketing.getPlatformInfo(platformId)
  return platform ? platform.name : platformId
}

const getPlatformColor = (platformId) => {
  const platform = socialMediaMarketing.getPlatformInfo(platformId)
  return platform ? platform.color : '#909399'
}

// 方法
const handleSearch = () => {
  Object.assign(socialMediaMarketing.filterParams, filterForm.value)
  socialMediaMarketing.fetchCampaigns()
}

const handleReset = () => {
  Object.assign(filterForm.value, {
    keyword: '',
    platform: '',
    campaignType: '',
    status: '',
    dateRange: []
  })
  Object.assign(socialMediaMarketing.filterParams, filterForm.value)
  socialMediaMarketing.fetchCampaigns()
}

const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

const handleSortChange = ({ prop, order }) => {
  const params = {
    sortBy: prop,
    sortOrder: order === 'ascending' ? 'asc' : 'desc'
  }
  socialMediaMarketing.fetchCampaigns(params)
}

const handlePageSizeChange = (size) => {
  socialMediaMarketing.pagination.pageSize = size
  socialMediaMarketing.fetchCampaigns()
}

const handleCurrentPageChange = (page) => {
  socialMediaMarketing.pagination.currentPage = page
  socialMediaMarketing.fetchCampaigns()
}

const handleViewDetail = (campaign) => {
  selectedCampaign.value = campaign
  showDetailDialog.value = true
}

const handleEdit = (campaign) => {
  selectedCampaign.value = { ...campaign }
  showCreateDialog.value = true
}

const handleCommand = async (command, campaign) => {
  switch (command) {
    case 'analytics':
      analyticsData.value = campaign
      showAnalyticsDialog.value = true
      break
    case 'content':
      selectedCampaign.value = campaign
      showContentDialog.value = true
      break
    case 'pause':
      await socialMediaMarketing.updateCampaign(campaign.id, { status: 'paused' })
      break
    case 'resume':
      await socialMediaMarketing.updateCampaign(campaign.id, { status: 'active' })
      break
    case 'complete':
      await socialMediaMarketing.updateCampaign(campaign.id, { status: 'completed' })
      break
    case 'delete':
      try {
        await ElMessageBox.confirm(
          `确定要删除活动"${campaign.name}"吗？`,
          '确认删除',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        await socialMediaMarketing.deleteCampaign(campaign.id)
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
      break
  }
}

const handleBatchDelete = async () => {
  if (!selectedRows.value.length) return
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 个活动吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const ids = selectedRows.value.map(row => row.id)
    await socialMediaMarketing.batchDeleteCampaigns(ids)
    selectedRows.value = []
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量删除失败')
    }
  }
}

const handleExport = async () => {
  try {
    await socialMediaMarketing.exportCampaigns(filterForm.value)
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

const handleCampaignUpdated = () => {
  socialMediaMarketing.fetchCampaigns()
}

// 加载图表数据
const loadChartData = async () => {
  try {
    await socialMediaMarketing.fetchAnalytics({
      period: selectedPeriod.value
    })
    await nextTick()
    renderCharts()
  } catch (error) {
    console.error('加载图表数据失败:', error)
  }
}

// 渲染图表（示例实现）
const renderCharts = () => {
  console.log('渲染图表数据:', socialMediaMarketing.analytics)
}

// 生命周期
onMounted(async () => {
  try {
    await Promise.all([
      socialMediaMarketing.fetchCampaigns(),
      socialMediaMarketing.fetchAnalytics(),
      socialMediaMarketing.fetchContentLibrary(),
      socialMediaMarketing.fetchAudienceData()
    ])
  } catch (error) {
    console.error('初始化失败:', error)
  }
})
</script>

<style scoped>
.social-media-marketing {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  background: white;
  padding: 24px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.title-section {
  flex: 1;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.page-description {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.action-section {
  display: flex;
  gap: 12px;
}

.breadcrumb-section {
  margin-bottom: 20px;
}

.statistics-section {
  margin-bottom: 20px;
}

.stat-card {
  border: none;
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  font-size: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: #f5f5f5;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 2px;
}

.stat-unit {
  font-size: 12px;
  color: #909399;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  margin-top: 8px;
}

.charts-section {
  margin-bottom: 20px;
}

.chart-card {
  border: none;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  height: 300px;
  width: 100%;
}

.filter-section {
  margin-bottom: 20px;
}

.filter-form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.table-section {
  margin-bottom: 20px;
}

.table-actions {
  display: flex;
  gap: 12px;
}

.social-table {
  width: 100%;
}

.platform-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.platform-tags .el-tag {
  font-size: 11px;
  padding: 2px 6px;
}

.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>