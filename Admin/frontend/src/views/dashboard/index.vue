<template>
  <div class="admin-dashboard">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <div class="welcome-text">
          <h1 class="title">欢迎回来</h1>
          <p class="subtitle">管理后台系统</p>
          <div class="divider"></div>
          <p class="quote">"数据驱动决策，智能提升效率"</p>
        </div>
        <div class="welcome-graphic">
          <div class="graphic-circle"></div>
          <div class="graphic-line"></div>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-container">
      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6" 
              v-for="(stat, index) in statistics" 
              :key="index">
        <div class="stat-card" :class="`gradient-${stat.color}`">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon :size="32">
                <component :is="stat.icon" />
              </el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-title">{{ stat.title }}</div>
              <div class="stat-value">{{ stat.value }}</div>
              <div class="stat-trend" :class="stat.trend > 0 ? 'up' : 'down'">
                <el-icon :size="12">
                  <CaretTop v-if="stat.trend > 0" />
                  <CaretBottom v-else />
                </el-icon>
                {{ Math.abs(stat.trend) }}%
              </div>
            </div>
          </div>
          <div class="stat-footer">
            <span>{{ stat.subtitle }}</span>
            <span>{{ stat.time }}</span>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-container">
      <!-- 销售趋势 -->
      <el-col :xs="24" :sm="24" :md="16" :lg="16" :xl="16">
        <div class="chart-card">
          <div class="chart-header">
            <h3>销售趋势分析</h3>
            <el-radio-group v-model="salesPeriod" size="small">
              <el-radio-button label="week">本周</el-radio-button>
              <el-radio-button label="month">本月</el-radio-button>
              <el-radio-button label="year">本年</el-radio-button>
            </el-radio-group>
          </div>
          <div ref="salesChartRef" class="chart-content" style="height: 300px;"></div>
        </div>
      </el-col>

      <!-- 订单状态分布 -->
      <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
        <div class="chart-card">
          <div class="chart-header">
            <h3>订单状态分布</h3>
            <el-tag type="info" size="small">实时</el-tag>
          </div>
          <div ref="orderStatusChartRef" class="chart-content" style="height: 300px;"></div>
        </div>
      </el-col>
    </el-row>

    <!-- 数据表格区域 -->
    <el-row :gutter="20" class="table-container">
      <!-- 待处理订单 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <div class="table-card">
          <div class="card-header">
            <h3>待处理订单</h3>
            <el-button type="primary" link>查看全部</el-button>
          </div>
          <el-table :data="pendingOrders" style="width: 100%" size="small">
            <el-table-column prop="orderNo" label="订单号" width="120">
              <template #default="{ row }">
                <el-link type="primary" :underline="false">{{ row.orderNo }}</el-link>
              </template>
            </el-table-column>
            <el-table-column prop="customer" label="客户" width="100" />
            <el-table-column prop="amount" label="金额" width="80">
              <template #default="{ row }">
                ¥{{ formatNumber(row.amount) }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="getOrderStatusType(row.status)" size="small">
                  {{ row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="120">
              <template #default="{ row }">
                {{ formatRelativeTime(row.createTime) }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>

      <!-- 库存预警 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <div class="table-card">
          <div class="card-header">
            <h3>库存预警</h3>
            <el-button type="primary" link>查看全部</el-button>
          </div>
          <el-table :data="inventoryAlerts" style="width: 100%" size="small">
            <el-table-column prop="productName" label="商品名称" width="120" />
            <el-table-column prop="warehouse" label="仓库" width="80" />
            <el-table-column prop="currentStock" label="当前库存" width="90" />
            <el-table-column prop="minStock" label="最低库存" width="90" />
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="getStockAlertType(row.currentStock, row.minStock)" size="small">
                  {{ getStockAlertText(row.currentStock, row.minStock) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>

    <!-- 快捷操作 -->
    <div class="quick-actions">
      <h3>快捷操作</h3>
      <el-row :gutter="16">
        <el-col :xs="12" :sm="8" :md="6" :lg="4" :xl="4" 
                v-for="(action, index) in quickActions" 
                :key="index">
          <div class="action-item" @click="handleQuickAction(action)">
            <div class="action-icon" :style="{ backgroundColor: action.color }">
              <el-icon :size="24">
                <component :is="action.icon" />
              </el-icon>
            </div>
            <div class="action-text">{{ action.title }}</div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  User, 
  ShoppingCart, 
  Coin, 
  TrendCharts,
  CaretTop,
  CaretBottom,
  Plus,
  Goods,
  OfficeBuilding,
  Document,
  Setting,
  Warning
} from '@element-plus/icons-vue'
import { useAdminChart } from '@/composables/useChart'
import { formatNumber, formatRelativeTime } from '@/utils/format'

const router = useRouter()

// 图表引用
const salesChartRef = ref()
const orderStatusChartRef = ref()

// 图表实例
let salesChart = null
let orderStatusChart = null

// 销售周期
const salesPeriod = ref('month')

// 统计数据
const statistics = reactive([
  {
    title: '总销售额',
    value: '¥2,456,789',
    trend: 12.5,
    subtitle: '较上月',
    time: '实时',
    icon: 'Coin',
    color: 'blue'
  },
  {
    title: '订单总数',
    value: '15,234',
    trend: 8.3,
    subtitle: '较上月',
    time: '实时',
    icon: 'ShoppingCart',
    color: 'green'
  },
  {
    title: '客户总数',
    value: '8,567',
    trend: -2.1,
    subtitle: '较上月',
    time: '实时',
    icon: 'User',
    color: 'purple'
  },
  {
    title: '商品总数',
    value: '3,421',
    trend: 5.7,
    subtitle: '较上月',
    time: '实时',
    icon: 'Goods',
    color: 'orange'
  }
])

// 待处理订单
const pendingOrders = ref([
  {
    orderNo: 'ORD20241201001',
    customer: '阿里巴巴',
    amount: 15680,
    status: '待确认',
    createTime: new Date(Date.now() - 2 * 60 * 60 * 1000)
  },
  {
    orderNo: 'ORD20241201002',
    customer: '京东商城',
    amount: 8932,
    status: '待发货',
    createTime: new Date(Date.now() - 4 * 60 * 60 * 1000)
  },
  {
    orderNo: 'ORD20241201003',
    customer: '拼多多',
    amount: 5643,
    status: '待付款',
    createTime: new Date(Date.now() - 6 * 60 * 60 * 1000)
  },
  {
    orderNo: 'ORD20241201004',
    customer: '苏宁易购',
    amount: 12890,
    status: '待确认',
    createTime: new Date(Date.now() - 8 * 60 * 60 * 1000)
  }
])

// 库存预警
const inventoryAlerts = ref([
  {
    productName: 'iPhone 15 Pro',
    warehouse: '上海仓',
    currentStock: 15,
    minStock: 50
  },
  {
    productName: '华为Mate60',
    warehouse: '北京仓',
    currentStock: 8,
    minStock: 30
  },
  {
    productName: '小米14',
    warehouse: '深圳仓',
    currentStock: 3,
    minStock: 20
  },
  {
    productName: 'OPPO FindX6',
    warehouse: '成都仓',
    currentStock: 12,
    minStock: 25
  }
])

// 快捷操作
const quickActions = ref([
  {
    title: '新增订单',
    icon: 'Plus',
    color: '#409EFF',
    path: '/order/create'
  },
  {
    title: '商品管理',
    icon: 'Goods',
    color: '#67C23A',
    path: '/product'
  },
  {
    title: '客户管理',
    icon: 'User',
    color: '#E6A23C',
    path: '/customer'
  },
  {
    title: '库存查询',
    icon: 'OfficeBuilding',
    color: '#F56C6C',
    path: '/inventory'
  },
  {
    title: '财务报表',
    icon: 'Document',
    color: '#909399',
    path: '/finance'
  },
  {
    title: '系统设置',
    icon: 'Setting',
    color: '#00c0ef',
    path: '/setting'
  }
])

// 获取订单状态标签类型
const getOrderStatusType = (status) => {
  const statusMap = {
    '待付款': 'warning',
    '待确认': 'info',
    '待发货': 'primary',
    '已发货': 'success',
    '已完成': 'success',
    '已取消': 'danger'
  }
  return statusMap[status] || 'info'
}

// 获取库存预警类型
const getStockAlertType = (current, min) => {
  const ratio = current / min
  if (ratio <= 0.3) return 'danger'
  if (ratio <= 0.5) return 'warning'
  return 'success'
}

// 获取库存预警文本
const getStockAlertText = (current, min) => {
  const ratio = current / min
  if (ratio <= 0.3) return '严重不足'
  if (ratio <= 0.5) return '库存偏低'
  return '库存正常'
}

// 处理快捷操作
const handleQuickAction = (action) => {
  if (action.path) {
    router.push(action.path)
  } else {
    ElMessage.info(`点击了${action.title}`)
  }
}

// 初始化销售趋势图表
const initSalesChart = () => {
  const { createSalesTrendChart } = useAdminChart()
  
  // 模拟数据
  const salesData = [
    { date: '2024-01', amount: 120000 },
    { date: '2024-02', amount: 132000 },
    { date: '2024-03', amount: 101000 },
    { date: '2024-04', amount: 134000 },
    { date: '2024-05', amount: 90000 },
    { date: '2024-06', amount: 230000 },
    { date: '2024-07', amount: 210000 },
    { date: '2024-08', amount: 220000 },
    { date: '2024-09', amount: 182000 },
    { date: '2024-10', amount: 191000 },
    { date: '2024-11', amount: 234000 },
    { date: '2024-12', amount: 290000 }
  ]
  
  salesChart = createSalesTrendChart(salesChartRef, salesData)
}

// 初始化订单状态图表
const initOrderStatusChart = () => {
  const { createOrderStatusChart } = useAdminChart()
  
  // 模拟数据
  const orderStatusData = [
    { name: '待付款', value: 35 },
    { name: '待确认', value: 28 },
    { name: '待发货', value: 22 },
    { name: '已发货', value: 15 },
    { name: '已完成', value: 45 },
    { name: '已取消', value: 8 }
  ]
  
  orderStatusChart = createOrderStatusChart(orderStatusChartRef, orderStatusData)
}

// 监听销售周期变化
watch(salesPeriod, (newPeriod) => {
  // 这里可以根据不同周期加载不同的数据
  console.log('销售周期切换为:', newPeriod)
})

onMounted(() => {
  initSalesChart()
  initOrderStatusChart()
})

onUnmounted(() => {
  if (salesChart) salesChart.disposeChart()
  if (orderStatusChart) orderStatusChart.disposeChart()
})
</script>

<style lang="scss" scoped>
.admin-dashboard {
  padding: 20px;
  background-color: var(--theme-background, #f5f7fa);
  min-height: 100vh;
}

// 欢迎区域
.welcome-section {
  margin-bottom: 24px;
  
  .welcome-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 40px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 12px;
    color: white;
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  }
  
  .welcome-text {
    flex: 1;
    
    .title {
      font-size: 2.5rem;
      font-weight: 600;
      margin: 0 0 10px 0;
    }
    
    .subtitle {
      font-size: 1.2rem;
      opacity: 0.9;
      margin: 0 0 20px 0;
    }
    
    .divider {
      width: 60px;
      height: 3px;
      background: rgba(255, 255, 255, 0.8);
      margin: 20px 0;
    }
    
    .quote {
      font-size: 1rem;
      opacity: 0.8;
      font-style: italic;
    }
  }
  
  .welcome-graphic {
    position: relative;
    width: 120px;
    height: 120px;
    
    .graphic-circle {
      position: absolute;
      width: 100px;
      height: 100px;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.1);
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      animation: pulse 3s infinite ease-in-out;
    }
    
    .graphic-line {
      position: absolute;
      width: 80px;
      height: 80px;
      border: 8px solid transparent;
      border-top-color: rgba(255, 255, 255, 0.6);
      border-right-color: rgba(255, 255, 255, 0.6);
      border-radius: 50%;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%) rotate(45deg);
      animation: rotate 8s linear infinite;
    }
  }
}

// 统计卡片
.stats-container {
  margin-bottom: 24px;
  
  .stat-card {
    padding: 24px;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;
    cursor: pointer;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
    }
    
    &.gradient-blue {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      color: white;
    }
    
    &.gradient-green {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      color: white;
    }
    
    &.gradient-purple {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
    }
    
    &.gradient-orange {
      background: linear-gradient(135deg, #f6d365 0%, #fda085 100%);
      color: white;
    }
  }
  
  .stat-content {
    display: flex;
    align-items: center;
    margin-bottom: 16px;
    
    .stat-icon {
      margin-right: 16px;
      opacity: 0.8;
    }
    
    .stat-info {
      flex: 1;
      
      .stat-title {
        font-size: 0.9rem;
        opacity: 0.9;
        margin-bottom: 4px;
      }
      
      .stat-value {
        font-size: 1.8rem;
        font-weight: 600;
        margin-bottom: 4px;
      }
      
      .stat-trend {
        display: flex;
        align-items: center;
        font-size: 0.8rem;
        opacity: 0.8;
        
        &.up {
          color: #52c41a;
        }
        
        &.down {
          color: #ff4d4f;
        }
      }
    }
  }
  
  .stat-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 0.8rem;
    opacity: 0.8;
    padding-top: 12px;
    border-top: 1px solid rgba(255, 255, 255, 0.2);
  }
}

// 图表区域
.chart-container {
  margin-bottom: 24px;
  
  .chart-card {
    background: white;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    height: 100%;
    
    .chart-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      h3 {
        margin: 0;
        font-size: 1.1rem;
        font-weight: 500;
      }
    }
    
    .chart-content {
      width: 100%;
    }
  }
}

// 表格区域
.table-container {
  margin-bottom: 24px;
  
  .table-card {
    background: white;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    height: 100%;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
      
      h3 {
        margin: 0;
        font-size: 1.1rem;
        font-weight: 500;
      }
    }
  }
}

// 快捷操作
.quick-actions {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  
  h3 {
    margin: 0 0 20px 0;
    font-size: 1.1rem;
    font-weight: 500;
  }
  
  .action-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 16px;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s ease;
    
    &:hover {
      background-color: #f5f7fa;
      transform: translateY(-2px);
    }
    
    .action-icon {
      width: 48px;
      height: 48px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 8px;
      color: white;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }
    
    .action-text {
      font-size: 0.9rem;
      color: #606266;
      text-align: center;
    }
  }
}

// 动画
@keyframes pulse {
  0% {
    transform: translate(-50%, -50%) scale(1);
    opacity: 0.6;
  }
  50% {
    transform: translate(-50%, -50%) scale(1.05);
    opacity: 0.9;
  }
  100% {
    transform: translate(-50%, -50%) scale(1);
    opacity: 0.6;
  }
}

@keyframes rotate {
  from {
    transform: translate(-50%, -50%) rotate(0deg);
  }
  to {
    transform: translate(-50%, -50%) rotate(360deg);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .admin-dashboard {
    padding: 16px;
  }
  
  .welcome-section .welcome-content {
    flex-direction: column;
    text-align: center;
    padding: 24px;
    
    .welcome-text .title {
      font-size: 2rem;
    }
  }
  
  .stats-container .stat-card {
    margin-bottom: 16px;
  }
  
  .chart-container,
  .table-container {
    margin-bottom: 16px;
  }
}

// 暗色主题适配
:deep(.theme-dark) {
  .admin-dashboard {
    background-color: #141414;
  }
  
  .chart-card,
  .table-card,
  .quick-actions {
    background-color: #1f1f1f;
    color: #e4e7ed;
  }
  
  .action-item:hover {
    background-color: #2c2c2c !important;
  }
}

:deep(.theme-b2b) {
  .admin-dashboard {
    background-color: #f0f2f5;
  }
  
  .welcome-section .welcome-content {
    background: linear-gradient(135deg, #2c5aa0 0%, #1890ff 100%);
  }
}
</style>