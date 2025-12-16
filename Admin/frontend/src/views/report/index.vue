<template>
  <div class="report-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">报表分析</h1>
          <p class="page-subtitle">销售、财务、库存等多维度数据分析</p>
        </div>
        <div class="header-actions">
          <el-button type="primary" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出报表
          </el-button>
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>报表分析</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 筛选区域 -->
    <div class="filter-section">
      <el-card>
        <el-form :inline="true" class="filter-form">
          <el-form-item label="报表类型">
            <el-select v-model="filterParams.reportType" placeholder="选择报表类型" style="width: 150px">
              <el-option label="销售报表" :value="REPORT_TYPES.SALES" />
              <el-option label="财务报表" :value="REPORT_TYPES.FINANCE" />
              <el-option label="库存报表" :value="REPORT_TYPES.INVENTORY" />
              <el-option label="客户报表" :value="REPORT_TYPES.CUSTOMER" />
              <el-option label="商品报表" :value="REPORT_TYPES.PRODUCT" />
              <el-option label="运营报表" :value="REPORT_TYPES.OPERATION" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="时间范围">
            <el-select v-model="filterParams.timeRange" placeholder="选择时间范围" style="width: 150px">
              <el-option label="今日" :value="REPORT_TIME_RANGES.TODAY" />
              <el-option label="昨日" :value="REPORT_TIME_RANGES.YESTERDAY" />
              <el-option label="本周" :value="REPORT_TIME_RANGES.THIS_WEEK" />
              <el-option label="上周" :value="REPORT_TIME_RANGES.LAST_WEEK" />
              <el-option label="本月" :value="REPORT_TIME_RANGES.THIS_MONTH" />
              <el-option label="上月" :value="REPORT_TIME_RANGES.LAST_MONTH" />
              <el-option label="本季度" :value="REPORT_TIME_RANGES.THIS_QUARTER" />
              <el-option label="上季度" :value="REPORT_TIME_RANGES.LAST_QUARTER" />
              <el-option label="本年" :value="REPORT_TIME_RANGES.THIS_YEAR" />
              <el-option label="去年" :value="REPORT_TIME_RANGES.LAST_YEAR" />
            </el-select>
          </el-form-item>

          <el-form-item label="图表类型">
            <el-select v-model="filterParams.chartType" placeholder="选择图表类型" style="width: 150px">
              <el-option label="折线图" :value="CHART_TYPES.LINE" />
              <el-option label="柱状图" :value="CHART_TYPES.BAR" />
              <el-option label="饼图" :value="CHART_TYPES.PIE" />
              <el-option label="面积图" :value="CHART_TYPES.AREA" />
              <el-option label="散点图" :value="CHART_TYPES.SCATTER" />
              <el-option label="雷达图" :value="CHART_TYPES.RADAR" />
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleSearch" :loading="loading">
              <el-icon><Search /></el-icon>
              查询
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 汇总统计卡片 -->
    <div class="summary-section" v-if="Object.keys(summaryData).length > 0">
      <el-row :gutter="20">
        <el-col :span="6" v-for="(value, key) in summaryData" :key="key">
          <el-card class="summary-card" :body-style="{ padding: '20px' }">
            <div class="card-content">
              <div class="card-icon" :class="getSummaryIconClass(key)">
                <el-icon :size="24">
                  <component :is="getSummaryIcon(key)" />
                </el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">{{ getSummaryTitle(key) }}</div>
                <div class="card-value">{{ formatSummaryValue(key, value) }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="chart-section">
      <el-card>
        <template #header>
          <div class="chart-header">
            <span class="chart-title">{{ getChartTitle() }}</span>
            <div class="chart-actions">
              <el-button-group>
                <el-button 
                  size="small" 
                  :type="filterParams.chartType === CHART_TYPES.LINE ? 'primary' : ''"
                  @click="changeChartType(CHART_TYPES.LINE)"
                >
                  折线
                </el-button>
                <el-button 
                  size="small" 
                  :type="filterParams.chartType === CHART_TYPES.BAR ? 'primary' : ''"
                  @click="changeChartType(CHART_TYPES.BAR)"
                >
                  柱状
                </el-button>
                <el-button 
                  size="small" 
                  :type="filterParams.chartType === CHART_TYPES.PIE ? 'primary' : ''"
                  @click="changeChartType(CHART_TYPES.PIE)"
                >
                  饼图
                </el-button>
              </el-button-group>
            </div>
          </div>
        </template>
        
        <div class="chart-container" v-loading="loading">
          <div ref="chartRef" class="chart"></div>
        </div>
      </el-card>
    </div>

    <!-- 数据表格 -->
    <div class="table-section">
      <el-card>
        <template #header>
          <div class="table-header">
            <span class="table-title">详细数据</span>
            <div class="table-actions">
              <el-button size="small" @click="handleTableExport">
                <el-icon><Download /></el-icon>
                导出
              </el-button>
            </div>
          </div>
        </template>
        
        <el-table
          :data="tableData"
          style="width: 100%"
          v-loading="loading"
          stripe
          border
        >
          <el-table-column 
            prop="date" 
            label="日期" 
            width="120"
            align="center"
          >
            <template #default="scope">
              {{ formatDate(scope.row.date) }}
            </template>
          </el-table-column>
          
          <el-table-column 
            v-for="column in tableColumns" 
            :key="column.prop"
            :prop="column.prop"
            :label="column.label"
            :width="column.width"
            :align="column.align || 'center'"
          >
            <template #default="scope" v-if="column.formatter">
              {{ column.formatter(scope.row[column.prop]) }}
            </template>
          </el-table-column>
        </el-table>
        
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="pagination.page"
            v-model:page-size="pagination.limit"
            :total="pagination.total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>

    <!-- 快速报表模板 -->
    <div class="template-section">
      <el-card>
        <template #header>
          <div class="template-header">
            <span class="template-title">快速报表</span>
            <span class="template-subtitle">预设的常用报表模板</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="8" v-for="template in reportTemplates" :key="template.id">
            <el-card 
              class="template-card" 
              shadow="hover"
              @click="applyTemplate(template)"
            >
              <div class="template-content">
                <div class="template-icon" :class="getTemplateIconClass(template.type)">
                  <el-icon :size="32">
                    <component :is="getTemplateIcon(template.type)" />
                  </el-icon>
                </div>
                <div class="template-info">
                  <h4 class="template-name">{{ template.name }}</h4>
                  <p class="template-desc">{{ template.description }}</p>
                  <div class="template-tags">
                    <el-tag size="small" :type="getTemplateTagType(template.type)">
                      {{ getTemplateTypeLabel(template.type) }}
                    </el-tag>
                    <el-tag size="small" type="info">
                      {{ getTimeRangeLabel(template.timeRange) }}
                    </el-tag>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { useReport } from '@/composables/useReport'

const {
  loading,
  reportData,
  summaryData,
  chartData,
  getReportData,
  exportReport,
  getReportTemplates,
  REPORT_TYPES,
  REPORT_TIME_RANGES,
  CHART_TYPES
} = useReport()

const chartRef = ref()
let chartInstance = null

// 筛选参数
const filterParams = reactive({
  reportType: REPORT_TYPES.SALES,
  timeRange: REPORT_TIME_RANGES.THIS_MONTH,
  chartType: CHART_TYPES.LINE
})

// 分页参数
const pagination = reactive({
  page: 1,
  limit: 20,
  total: 0
})

// 报表模板
const reportTemplates = ref([])

// 表格数据
const tableData = computed(() => {
  const start = (pagination.page - 1) * pagination.limit
  const end = start + pagination.limit
  return reportData.value.slice(start, end)
})

// 表格列配置
const tableColumns = computed(() => {
  switch (filterParams.reportType) {
    case REPORT_TYPES.SALES:
      return [
        { prop: 'orderCount', label: '订单数', width: 100 },
        { prop: 'orderAmount', label: '订单金额', width: 120, formatter: formatCurrency },
        { prop: 'averageOrderValue', label: '平均订单金额', width: 120, formatter: formatCurrency },
        { prop: 'conversionRate', label: '转化率(%)', width: 100 },
        { prop: 'customerCount', label: '客户数', width: 100 },
        { prop: 'newCustomerCount', label: '新客户数', width: 100 }
      ]
    case REPORT_TYPES.FINANCE:
      return [
        { prop: 'revenue', label: '收入', width: 120, formatter: formatCurrency },
        { prop: 'cost', label: '成本', width: 120, formatter: formatCurrency },
        { prop: 'profit', label: '利润', width: 120, formatter: formatCurrency },
        { prop: 'grossMargin', label: '毛利率(%)', width: 100 },
        { prop: 'netMargin', label: '净利率(%)', width: 100 }
      ]
    case REPORT_TYPES.INVENTORY:
      return [
        { prop: 'stockQuantity', label: '库存数量', width: 120 },
        { prop: 'stockValue', label: '库存价值', width: 120, formatter: formatCurrency },
        { prop: 'turnoverRate', label: '周转率', width: 100 },
        { prop: 'stockoutCount', label: '缺货数', width: 100 },
        { prop: 'overstockCount', label: '超储数', width: 100 },
        { prop: 'inboundQuantity', label: '入库数量', width: 120 },
        { prop: 'outboundQuantity', label: '出库数量', width: 120 }
      ]
    case REPORT_TYPES.CUSTOMER:
      return [
        { prop: 'totalCustomers', label: '总客户数', width: 120 },
        { prop: 'activeCustomers', label: '活跃客户数', width: 120 },
        { prop: 'newCustomers', label: '新客户数', width: 120 },
        { prop: 'lostCustomers', label: '流失客户数', width: 120 },
        { prop: 'customerRetention', label: '客户留存率(%)', width: 130 },
        { prop: 'churnRate', label: '流失率(%)', width: 100 }
      ]
    default:
      return []
  }
})

// 初始化图表
const initChart = () => {
  if (!chartRef.value) return
  
  chartInstance = echarts.init(chartRef.value)
  updateChart()
}

// 更新图表
const updateChart = () => {
  if (!chartInstance || !chartData.value.data) return
  
  const option = getChartOption()
  chartInstance.setOption(option, true)
}

// 获取图表配置
const getChartOption = () => {
  const { xAxis, series, data } = chartData.value
  
  const baseOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {
      data: series.map(s => getMetricLabel(s))
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: filterParams.chartType !== CHART_TYPES.LINE,
      data: xAxis.map(date => formatDate(date))
    },
    yAxis: {
      type: 'value'
    }
  }
  
  if (filterParams.chartType === CHART_TYPES.PIE) {
    return {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [{
        name: '数据分布',
        type: 'pie',
        radius: '50%',
        data: data.map((item, index) => ({
          name: formatDate(item.date),
          value: item[series[0]] || 0
        }))
      }]
    }
  }
  
  return {
    ...baseOption,
    series: series.map(metric => ({
      name: getMetricLabel(metric),
      type: filterParams.chartType,
      data: data.map(item => item[metric] || 0),
      smooth: filterParams.chartType === CHART_TYPES.LINE,
      areaStyle: filterParams.chartType === CHART_TYPES.AREA ? {} : undefined
    }))
  }
}

// 搜索数据
const handleSearch = async () => {
  await getReportData(filterParams.reportType, filterParams.timeRange)
  pagination.total = reportData.value.length
  pagination.page = 1
  
  nextTick(() => {
    updateChart()
  })
}

// 重置筛选
const handleReset = () => {
  filterParams.reportType = REPORT_TYPES.SALES
  filterParams.timeRange = REPORT_TIME_RANGES.THIS_MONTH
  filterParams.chartType = CHART_TYPES.LINE
  handleSearch()
}

// 切换图表类型
const changeChartType = (type) => {
  filterParams.chartType = type
  nextTick(() => {
    updateChart()
  })
}

// 应用模板
const applyTemplate = (template) => {
  filterParams.reportType = template.type
  filterParams.timeRange = template.timeRange
  filterParams.chartType = template.chartType
  handleSearch()
}

// 导出报表
const handleExport = async () => {
  try {
    await exportReport(filterParams.reportType, filterParams.timeRange)
  } catch (error) {
    console.error('导出失败:', error)
  }
}

// 导出表格
const handleTableExport = () => {
  // 实现表格导出逻辑
  console.log('导出表格数据')
}

// 分页变化
const handleSizeChange = (size) => {
  pagination.limit = size
  pagination.page = 1
}

const handleCurrentChange = (page) => {
  pagination.page = page
}

// 工具函数
const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('zh-CN')
}

const formatCurrency = (value) => {
  return `¥${new Intl.NumberFormat('zh-CN').format(value)}`
}

const formatSummaryValue = (key, value) => {
  if (key.toLowerCase().includes('amount') || key.toLowerCase().includes('revenue') || key.toLowerCase().includes('profit')) {
    return formatCurrency(value)
  }
  if (key.toLowerCase().includes('rate') || key.toLowerCase().includes('margin')) {
    return `${value}%`
  }
  return new Intl.NumberFormat('zh-CN').format(value)
}

const getSummaryTitle = (key) => {
  const titleMap = {
    totalOrders: '总订单数',
    totalAmount: '总金额',
    averageOrderValue: '平均订单金额',
    totalCustomers: '总客户数',
    newCustomers: '新客户数',
    repeatCustomers: '回头客数',
    totalRevenue: '总收入',
    totalCost: '总成本',
    totalProfit: '总利润',
    averageGrossMargin: '平均毛利率',
    averageNetMargin: '平均净利率',
    totalStockQuantity: '总库存数量',
    totalStockValue: '总库存价值',
    averageTurnoverRate: '平均周转率',
    totalStockoutCount: '总缺货数',
    totalOverstockCount: '总超储数',
    totalActiveCustomers: '总活跃客户',
    totalNewCustomers: '总新客户',
    totalLostCustomers: '总流失客户',
    averageRetention: '平均留存率',
    averageLifetimeValue: '平均生命周期价值',
    averageSatisfaction: '平均满意度',
    averageChurnRate: '平均流失率'
  }
  return titleMap[key] || key
}

const getSummaryIcon = (key) => {
  const iconMap = {
    totalOrders: 'ShoppingCart',
    totalAmount: 'Money',
    averageOrderValue: 'PriceTag',
    totalCustomers: 'User',
    newCustomers: 'UserFilled',
    repeatCustomers: 'Refresh',
    totalRevenue: 'Coin',
    totalCost: 'Wallet',
    totalProfit: 'TrendCharts',
    averageGrossMargin: 'DataLine',
    averageNetMargin: 'DataAnalysis',
    totalStockQuantity: 'Box',
    totalStockValue: 'Goods',
    averageTurnoverRate: 'RefreshRight',
    totalStockoutCount: 'Warning',
    totalOverstockCount: 'CircleClose',
    totalActiveCustomers: 'Avatar',
    totalNewCustomers: 'UserFilled',
    totalLostCustomers: 'UserFilled',
    averageRetention: 'CircleCheck',
    averageLifetimeValue: 'Coin',
    averageSatisfaction: 'Star',
    averageChurnRate: 'Remove'
  }
  return iconMap[key] || 'DataLine'
}

const getSummaryIconClass = (key) => {
  const classMap = {
    totalOrders: 'icon-orders',
    totalAmount: 'icon-amount',
    averageOrderValue: 'icon-value',
    totalCustomers: 'icon-customers',
    newCustomers: 'icon-new',
    repeatCustomers: 'icon-repeat',
    totalRevenue: 'icon-revenue',
    totalCost: 'icon-cost',
    totalProfit: 'icon-profit',
    averageGrossMargin: 'icon-margin',
    averageNetMargin: 'icon-margin',
    totalStockQuantity: 'icon-quantity',
    totalStockValue: 'icon-value',
    averageTurnoverRate: 'icon-turnover',
    totalStockoutCount: 'icon-stockout',
    totalOverstockCount: 'icon-overstock',
    totalActiveCustomers: 'icon-active',
    totalNewCustomers: 'icon-new',
    totalLostCustomers: 'icon-lost',
    averageRetention: 'icon-retention',
    averageLifetimeValue: 'icon-lifetime',
    averageSatisfaction: 'icon-satisfaction',
    averageChurnRate: 'icon-churn'
  }
  return classMap[key] || 'icon-default'
}

const getChartTitle = () => {
  const typeMap = {
    [REPORT_TYPES.SALES]: '销售趋势分析',
    [REPORT_TYPES.FINANCE]: '财务数据分析',
    [REPORT_TYPES.INVENTORY]: '库存变化趋势',
    [REPORT_TYPES.CUSTOMER]: '客户增长分析'
  }
  return typeMap[filterParams.reportType] || '数据分析'
}

const getMetricLabel = (metric) => {
  const labelMap = {
    orderCount: '订单数',
    orderAmount: '订单金额',
    averageOrderValue: '平均订单金额',
    conversionRate: '转化率',
    customerCount: '客户数',
    newCustomerCount: '新客户数',
    repeatCustomerCount: '回头客数',
    revenue: '收入',
    cost: '成本',
    profit: '利润',
    grossMargin: '毛利率',
    netMargin: '净利率',
    stockQuantity: '库存数量',
    stockValue: '库存价值',
    turnoverRate: '周转率',
    stockoutCount: '缺货数',
    overstockCount: '超储数',
    inboundQuantity: '入库数量',
    outboundQuantity: '出库数量',
    totalCustomers: '总客户数',
    activeCustomers: '活跃客户数',
    newCustomers: '新客户数',
    lostCustomers: '流失客户数',
    customerRetention: '客户留存率',
    customerLifetimeValue: '客户生命周期价值',
    customerSatisfaction: '客户满意度',
    churnRate: '流失率'
  }
  return labelMap[metric] || metric
}

const getTemplateIcon = (type) => {
  const iconMap = {
    [REPORT_TYPES.SALES]: 'TrendCharts',
    [REPORT_TYPES.FINANCE]: 'Money',
    [REPORT_TYPES.INVENTORY]: 'Box',
    [REPORT_TYPES.CUSTOMER]: 'User'
  }
  return iconMap[type] || 'DataLine'
}

const getTemplateIconClass = (type) => {
  const classMap = {
    [REPORT_TYPES.SALES]: 'template-sales',
    [REPORT_TYPES.FINANCE]: 'template-finance',
    [REPORT_TYPES.INVENTORY]: 'template-inventory',
    [REPORT_TYPES.CUSTOMER]: 'template-customer'
  }
  return classMap[type] || 'template-default'
}

const getTemplateTypeLabel = (type) => {
  const labelMap = {
    [REPORT_TYPES.SALES]: '销售',
    [REPORT_TYPES.FINANCE]: '财务',
    [REPORT_TYPES.INVENTORY]: '库存',
    [REPORT_TYPES.CUSTOMER]: '客户'
  }
  return labelMap[type] || '其他'
}

const getTemplateTagType = (type) => {
  const typeMap = {
    [REPORT_TYPES.SALES]: 'success',
    [REPORT_TYPES.FINANCE]: 'warning',
    [REPORT_TYPES.INVENTORY]: 'info',
    [REPORT_TYPES.CUSTOMER]: 'primary'
  }
  return typeMap[type] || 'info'
}

const getTimeRangeLabel = (range) => {
  const labelMap = {
    [REPORT_TIME_RANGES.TODAY]: '今日',
    [REPORT_TIME_RANGES.YESTERDAY]: '昨日',
    [REPORT_TIME_RANGES.THIS_WEEK]: '本周',
    [REPORT_TIME_RANGES.LAST_WEEK]: '上周',
    [REPORT_TIME_RANGES.THIS_MONTH]: '本月',
    [REPORT_TIME_RANGES.LAST_MONTH]: '上月',
    [REPORT_TIME_RANGES.THIS_QUARTER]: '本季度',
    [REPORT_TIME_RANGES.LAST_QUARTER]: '上季度',
    [REPORT_TIME_RANGES.THIS_YEAR]: '本年',
    [REPORT_TIME_RANGES.LAST_YEAR]: '去年'
  }
  return labelMap[range] || range
}

// 监听窗口大小变化
const handleResize = () => {
  if (chartInstance) {
    chartInstance.resize()
  }
}

// 初始化
onMounted(async () => {
  reportTemplates.value = getReportTemplates()
  await handleSearch()
  initChart()
  
  window.addEventListener('resize', handleResize)
})

// 清理
import { onUnmounted } from 'vue'
onUnmounted(() => {
  if (chartInstance) {
    chartInstance.dispose()
  }
  window.removeEventListener('resize', handleResize)
})
</script>

<style lang="scss" scoped>
.report-page {
  padding: 20px;
  background-color: var(--el-bg-color-page);
  min-height: calc(100vh - 84px);
}

// 页面头部
.page-header {
  background: linear-gradient(135deg, #5D5FEF 0%, #4F52CC 100%);
  border-radius: 12px;
  margin-bottom: 20px;
  overflow: hidden;

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 30px;
    height: 100px;

    .title-section {
      .page-title {
        font-size: 24px;
        font-weight: bold;
        color: #FFFFFF;
        margin: 0 0 4px 0;
      }
      
      .page-subtitle {
        font-size: 14px;
        color: #E6E6FF;
        margin: 0;
        opacity: 0.9;
      }
    }

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }
}

// 面包屑
.breadcrumb {
  margin-bottom: 20px;
}

// 筛选区域
.filter-section {
  margin-bottom: 20px;

  .filter-form {
    .el-form-item {
      margin-bottom: 0;
      margin-right: 16px;
    }
  }
}

// 汇总统计卡片
.summary-section {
  margin-bottom: 20px;

  .summary-card {
    border-radius: 8px;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    .card-content {
      display: flex;
      align-items: center;
      gap: 16px;

      .card-icon {
        width: 48px;
        height: 48px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;

        &.icon-orders { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
        &.icon-amount { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
        &.icon-value { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
        &.icon-customers { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
        &.icon-new { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); }
        &.icon-repeat { background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%); }
        &.icon-revenue { background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%); }
        &.icon-cost { background: linear-gradient(135deg, #ff8a80 0%, #ea6100 100%); }
        &.icon-profit { background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%); }
        &.icon-margin { background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%); }
        &.icon-quantity { background: linear-gradient(135deg, #d299c2 0%, #fef9d7 100%); }
        &.icon-value { background: linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%); }
        &.icon-turnover { background: linear-gradient(135deg, #fddb92 0%, #d1fdff 100%); }
        &.icon-stockout { background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%); }
        &.icon-overstock { background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%); }
        &.icon-active { background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%); }
        &.icon-new { background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%); }
        &.icon-lost { background: linear-gradient(135deg, #ff8a80 0%, #ea6100 100%); }
        &.icon-retention { background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%); }
        &.icon-lifetime { background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%); }
        &.icon-satisfaction { background: linear-gradient(135deg, #d299c2 0%, #fef9d7 100%); }
        &.icon-churn { background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%); }
        &.icon-default { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
      }

      .card-info {
        flex: 1;

        .card-title {
          font-size: 14px;
          color: var(--el-text-color-secondary);
          margin-bottom: 4px;
        }

        .card-value {
          font-size: 24px;
          font-weight: bold;
          color: var(--el-text-color-primary);
        }
      }
    }
  }
}

// 图表区域
.chart-section {
  margin-bottom: 20px;

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .chart-title {
      font-size: 16px;
      font-weight: 500;
      color: var(--el-text-color-primary);
    }

    .chart-actions {
      display: flex;
      gap: 8px;
    }
  }

  .chart-container {
    width: 100%;
    height: 400px;
    padding: 20px 0;

    .chart {
      width: 100%;
      height: 100%;
    }
  }
}

// 表格区域
.table-section {
  margin-bottom: 20px;

  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .table-title {
      font-size: 16px;
      font-weight: 500;
      color: var(--el-text-color-primary);
    }

    .table-actions {
      display: flex;
      gap: 8px;
    }
  }

  .pagination-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
    padding: 20px 0;
  }
}

// 模板区域
template-section {
  .template-header {
    display: flex;
    flex-direction: column;
    gap: 4px;

    .template-title {
      font-size: 16px;
      font-weight: bold;
      color: var(--el-text-color-primary);
    }

    .template-subtitle {
      font-size: 12px;
      color: var(--el-text-color-secondary);
    }
  }

  .template-card {
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }

    .template-content {
      display: flex;
      align-items: center;
      gap: 16px;

      .template-icon {
        width: 64px;
        height: 64px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;

        &.template-sales { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
        &.template-finance { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
        &.template-inventory { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
        &.template-customer { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
        &.template-default { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
      }

      .template-info {
        flex: 1;

        .template-name {
          margin: 0 0 8px 0;
          font-size: 16px;
          font-weight: 500;
          color: var(--el-text-color-primary);
        }

        .template-desc {
          margin: 0 0 12px 0;
          font-size: 12px;
          color: var(--el-text-color-secondary);
          line-height: 1.5;
        }

        .template-tags {
          display: flex;
          gap: 8px;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .report-page {
    padding: 10px;
  }

  .page-header {
    .header-content {
      flex-direction: column;
      gap: 16px;
      height: auto;
      padding: 20px;

      .header-actions {
        width: 100%;
        justify-content: center;
      }
    }
  }

  .filter-section {
    .filter-form {
      .el-form-item {
        margin-bottom: 12px;
        width: 100%;
      }
    }
  }

  .summary-section {
    .el-col {
      margin-bottom: 16px;
    }
  }

  .chart-section {
    .chart-container {
      height: 300px;
    }
  }

  .template-section {
    .el-col {
      margin-bottom: 16px;
    }
  }
}

// 深色主题适配
:deep(.dark) {
  .page-header {
    background: linear-gradient(135deg, #4a4cb8 0%, #3d4099 100%);
  }
}
</style>