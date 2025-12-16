<template>
  <view class="b2b-dashboard">
    <!-- 头部统计卡片 -->
    <view class="stats-cards">
      <view 
        v-for="(stat, index) in statsData" 
        :key="index"
        class="stat-card b2b-card b2b-fade-in"
        :style="{ animationDelay: `${index * 0.1}s` }"
      >
        <view class="stat-icon" :class="`bg-${stat.color}`">
          <u-icon :name="stat.icon" size="48rpx" color="white" />
        </view>
        <view class="stat-content">
          <text class="stat-value">{{ formatNumber(stat.value) }}</text>
          <text class="stat-label">{{ stat.label }}</text>
          <view class="stat-trend" :class="stat.trend > 0 ? 'trend-up' : 'trend-down'">
            <u-icon :name="stat.trend > 0 ? 'arrow-up' : 'arrow-down'" size="24rpx" />
            <text>{{ Math.abs(stat.trend) }}%</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 快捷操作区域 -->
    <view class="quick-actions">
      <view class="section-title">
        <text class="title">快捷操作</text>
        <text class="subtitle">Quick Actions</text>
      </view>
      <view class="action-grid">
        <view 
          v-for="(action, index) in quickActions" 
          :key="index"
          class="action-item b2b-card b2b-bounce"
          :style="{ animationDelay: `${index * 0.1}s` }"
          @tap="handleQuickAction(action)"
        >
          <view class="action-icon" :class="`bg-${action.color}`">
            <u-icon :name="action.icon" size="48rpx" color="white" />
          </view>
          <text class="action-title">{{ action.title }}</text>
          <text class="action-desc">{{ action.desc }}</text>
        </view>
      </view>
    </view>

    <!-- 数据图表区域 -->
    <view class="charts-section">
      <view class="section-title">
        <text class="title">数据分析</text>
        <text class="subtitle">Data Analytics</text>
      </view>
      <view class="charts-grid">
        <view class="chart-card b2b-card b2b-slide-up">
          <view class="chart-header">
            <text class="chart-title">销售趋势</text>
            <view class="chart-controls">
              <text 
                v-for="period in chartPeriods" 
                :key="period.value"
                class="control-item"
                :class="{ active: currentPeriod === period.value }"
                @tap="switchPeriod(period.value)"
              >{{ period.label }}</text>
            </view>
          </view>
          <view class="chart-content">
            <canvas canvas-id="salesChart" class="chart-canvas" />
          </view>
        </view>
        
        <view class="chart-card b2b-card b2b-slide-up" style="animation-delay: 0.2s">
          <view class="chart-header">
            <text class="chart-title">产品分布</text>
          </view>
          <view class="chart-content">
            <canvas canvas-id="productChart" class="chart-canvas" />
          </view>
        </view>
      </view>
    </view>

    <!-- 最近活动 -->
    <view class="recent-activity">
      <view class="section-title">
        <text class="title">最近活动</text>
        <text class="subtitle">Recent Activity</text>
      </view>
      <view class="activity-list">
        <view 
          v-for="(activity, index) in recentActivities" 
          :key="index"
          class="activity-item b2b-card b2b-fade-in"
          :style="{ animationDelay: `${index * 0.1}s` }"
        >
          <view class="activity-icon" :class="`bg-${activity.color}`">
            <u-icon :name="activity.icon" size="32rpx" color="white" />
          </view>
          <view class="activity-content">
            <text class="activity-title">{{ activity.title }}</text>
            <text class="activity-desc">{{ activity.desc }}</text>
            <text class="activity-time">{{ formatTime(activity.time) }}</text>
          </view>
          <view class="activity-status" :class="`status-${activity.status}`">
            <text>{{ getStatusText(activity.status) }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 待办事项 -->
    <view class="todo-section">
      <view class="section-title">
        <text class="title">待办事项</text>
        <text class="subtitle">To-Do List</text>
      </view>
      <view class="todo-list">
        <view 
          v-for="(todo, index) in todoList" 
          :key="index"
          class="todo-item b2b-card"
          :class="{ completed: todo.completed }"
        >
          <view class="todo-checkbox" @tap="toggleTodo(todo)">
            <u-icon 
              :name="todo.completed ? 'checkbox-mark' : 'checkbox-blank'" 
              :color="todo.completed ? 'var(--b2b-success)' : 'var(--b2b-text-tertiary)'"
              size="32rpx"
            />
          </view>
          <view class="todo-content">
            <text class="todo-title" :class="{ completed: todo.completed }">{{ todo.title }}</text>
            <text class="todo-deadline">截止: {{ formatDate(todo.deadline) }}</text>
          </view>
          <view class="todo-priority" :class="`priority-${todo.priority}`">
            <text>{{ getPriorityText(todo.priority) }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import { useTheme } from '@/composables/useTheme';
import { useChart } from '@/composables/useChart';
import { formatNumber, formatTime, formatDate } from '@/utils/format';

// 主题配置
const { theme, toggleTheme } = useTheme();

// 图表配置
const { initChart, updateChart, destroyChart } = useChart();

// 统计数据
const statsData = ref([
  {
    label: '今日订单',
    value: 156,
    trend: 12.5,
    icon: 'order',
    color: 'primary'
  },
  {
    label: '今日销售额',
    value: 128500,
    trend: 8.3,
    icon: 'rmb',
    color: 'success'
  },
  {
    label: '新增客户',
    value: 23,
    trend: -2.1,
    icon: 'account',
    color: 'warning'
  },
  {
    label: '库存预警',
    value: 8,
    trend: 15.7,
    icon: 'warning',
    color: 'danger'
  }
]);

// 快捷操作
const quickActions = ref([
  {
    title: '新建订单',
    desc: '创建新的B2B订单',
    icon: 'plus',
    color: 'primary',
    action: 'createOrder'
  },
  {
    title: '客户管理',
    desc: '查看和管理客户',
    icon: 'account',
    color: 'success',
    action: 'customerManage'
  },
  {
    title: '商品目录',
    desc: '浏览商品和价格',
    icon: 'grid',
    color: 'warning',
    action: 'productCatalog'
  },
  {
    title: '库存查询',
    desc: '查看库存状态',
    icon: 'bag',
    color: 'danger',
    action: 'inventoryQuery'
  },
  {
    title: '价格策略',
    desc: '配置价格规则',
    icon: 'price-tag',
    color: 'info',
    action: 'priceStrategy'
  },
  {
    title: '支付条款',
    desc: '管理支付条件',
    icon: 'wallet',
    color: 'primary',
    action: 'paymentTerms'
  }
]);

// 图表周期
const chartPeriods = ref([
  { label: '日', value: 'day' },
  { label: '周', value: 'week' },
  { label: '月', value: 'month' },
  { label: '年', value: 'year' }
]);

const currentPeriod = ref('week');

// 最近活动
const recentActivities = ref([
  {
    title: '新订单 #B202401001',
    desc: '客户: 华为技术有限公司',
    time: Date.now() - 1000 * 60 * 5,
    icon: 'order',
    color: 'primary',
    status: 'success'
  },
  {
    title: '客户注册审核',
    desc: '客户: 小米科技有限公司',
    time: Date.now() - 1000 * 60 * 15,
    icon: 'account',
    color: 'success',
    status: 'pending'
  },
  {
    title: '库存预警',
    desc: '商品: iPhone 15 Pro 库存不足',
    time: Date.now() - 1000 * 60 * 30,
    icon: 'warning',
    color: 'danger',
    status: 'warning'
  },
  {
    title: '价格调整',
    desc: '商品: MacBook Pro 价格更新',
    time: Date.now() - 1000 * 60 * 60,
    icon: 'price-tag',
    color: 'warning',
    status: 'info'
  }
]);

// 待办事项
const todoList = ref([
  {
    id: 1,
    title: '审核新客户注册申请',
    deadline: Date.now() + 1000 * 60 * 60 * 24,
    priority: 'high',
    completed: false
  },
  {
    id: 2,
    title: '处理库存预警商品',
    deadline: Date.now() + 1000 * 60 * 60 * 12,
    priority: 'urgent',
    completed: false
  },
  {
    id: 3,
    title: '更新产品价格策略',
    deadline: Date.now() + 1000 * 60 * 60 * 48,
    priority: 'medium',
    completed: true
  },
  {
    id: 4,
    title: '跟进客户反馈',
    deadline: Date.now() + 1000 * 60 * 60 * 36,
    priority: 'low',
    completed: false
  }
]);

// 生命周期
onMounted(() => {
  initDashboard();
  loadData();
  startTimer();
});

onUnmounted(() => {
  destroyChart('salesChart');
  destroyChart('productChart');
  stopTimer();
});

// 初始化仪表板
const initDashboard = () => {
  // 初始化销售趋势图表
  initChart('salesChart', {
    type: 'line',
    data: {
      labels: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
      datasets: [{
        label: '销售额',
        data: [12000, 19000, 15000, 25000, 22000, 30000, 28000],
        borderColor: '#409eff',
        backgroundColor: 'rgba(64, 158, 255, 0.1)',
        tension: 0.4
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: false
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          grid: {
            color: 'var(--b2b-border-secondary)'
          },
          ticks: {
            color: 'var(--b2b-text-secondary)'
          }
        },
        x: {
          grid: {
            display: false
          },
          ticks: {
            color: 'var(--b2b-text-secondary)'
          }
        }
      }
    }
  });

  // 初始化产品分布图表
  initChart('productChart', {
    type: 'doughnut',
    data: {
      labels: ['手机', '电脑', '配件', '其他'],
      datasets: [{
        data: [35, 25, 20, 20],
        backgroundColor: [
          '#409eff',
          '#67c23a',
          '#e6a23c',
          '#f56c6c'
        ],
        borderWidth: 0
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'bottom',
          labels: {
            color: 'var(--b2b-text-secondary)',
            padding: 20
          }
        }
      }
    }
  });
};

// 加载数据
const loadData = async () => {
  try {
    // 这里可以调用API获取实时数据
    console.log('Loading dashboard data...');
  } catch (error) {
    console.error('Failed to load dashboard data:', error);
  }
};

// 定时器
let timer = null;
const startTimer = () => {
  timer = setInterval(() => {
    // 更新实时数据
    updateStats();
  }, 30000); // 30秒更新一次
};

const stopTimer = () => {
  if (timer) {
    clearInterval(timer);
    timer = null;
  }
};

// 更新统计数据
const updateStats = () => {
  // 模拟数据更新
  statsData.value.forEach(stat => {
    const change = (Math.random() - 0.5) * 2;
    stat.value = Math.max(0, stat.value + Math.floor(change));
    stat.trend = (Math.random() - 0.5) * 20;
  });
};

// 处理快捷操作
const handleQuickAction = (action) => {
  console.log('Quick action:', action.action);
  
  // 根据操作类型跳转到对应页面
  const routes = {
    createOrder: '/pages/b2b/order/create',
    customerManage: '/pages/b2b/customer/index',
    productCatalog: '/pages/b2b/product/catalog',
    inventoryQuery: '/pages/b2b/inventory/query',
    priceStrategy: '/pages/b2b/price/strategy',
    paymentTerms: '/pages/b2b/payment/terms'
  };
  
  if (routes[action.action]) {
    uni.navigateTo({
      url: routes[action.action]
    });
  }
};

// 切换图表周期
const switchPeriod = (period) => {
  currentPeriod.value = period;
  
  // 更新图表数据
  updateChartData(period);
};

// 更新图表数据
const updateChartData = (period) => {
  const salesData = getChartDataByPeriod(period);
  updateChart('salesChart', salesData);
};

// 根据周期获取图表数据
const getChartDataByPeriod = (period) => {
  const dataMap = {
    day: {
      labels: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00', '24:00'],
      data: [2000, 1500, 3000, 4500, 3800, 5200, 2800]
    },
    week: {
      labels: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
      data: [12000, 19000, 15000, 25000, 22000, 30000, 28000]
    },
    month: {
      labels: ['第1周', '第2周', '第3周', '第4周'],
      data: [45000, 52000, 48000, 61000]
    },
    year: {
      labels: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
      data: [45000, 52000, 48000, 61000, 58000, 72000, 65000, 69000, 71000, 68000, 75000, 82000]
    }
  };
  
  return dataMap[period] || dataMap.week;
};

// 切换待办事项状态
const toggleTodo = (todo) => {
  todo.completed = !todo.completed;
};

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    success: '成功',
    pending: '待处理',
    warning: '警告',
    info: '信息'
  };
  return statusMap[status] || '未知';
};

// 获取优先级文本
const getPriorityText = (priority) => {
  const priorityMap = {
    urgent: '紧急',
    high: '高',
    medium: '中',
    low: '低'
  };
  return priorityMap[priority] || '中';
};

// 页面跳转
const navigateTo = (url) => {
  uni.navigateTo({ url });
};
</script>

<style lang="scss" scoped>
.b2b-dashboard {
  padding: var(--b2b-spacing-md);
  background-color: var(--b2b-bg-secondary);
  min-height: 100vh;
}

// 统计卡片
.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300rpx, 1fr));
  gap: var(--b2b-spacing-md);
  margin-bottom: var(--b2b-spacing-xl);
}

.stat-card {
  display: flex;
  align-items: center;
  padding: var(--b2b-spacing-lg);
  background: linear-gradient(135deg, var(--b2b-bg-primary), var(--b2b-bg-secondary));
  
  .stat-icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: var(--b2b-spacing-md);
    box-shadow: var(--b2b-shadow);
  }
  
  .stat-content {
    flex: 1;
    
    .stat-value {
      display: block;
      font-size: var(--b2b-font-size-2xl);
      font-weight: 700;
      color: var(--b2b-text-primary);
      margin-bottom: var(--b2b-spacing-xs);
    }
    
    .stat-label {
      display: block;
      font-size: var(--b2b-font-size-sm);
      color: var(--b2b-text-secondary);
      margin-bottom: var(--b2b-spacing-xs);
    }
    
    .stat-trend {
      display: flex;
      align-items: center;
      font-size: var(--b2b-font-size-xs);
      
      &.trend-up {
        color: var(--b2b-success);
      }
      
      &.trend-down {
        color: var(--b2b-danger);
      }
    }
  }
}

// 快捷操作
.quick-actions {
  margin-bottom: var(--b2b-spacing-xl);
}

.section-title {
  display: flex;
  align-items: baseline;
  margin-bottom: var(--b2b-spacing-md);
  
  .title {
    font-size: var(--b2b-font-size-lg);
    font-weight: 600;
    color: var(--b2b-text-primary);
    margin-right: var(--b2b-spacing-sm);
  }
  
  .subtitle {
    font-size: var(--b2b-font-size-sm);
    color: var(--b2b-text-tertiary);
    text-transform: uppercase;
    letter-spacing: 1rpx;
  }
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200rpx, 1fr));
  gap: var(--b2b-spacing-md);
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--b2b-spacing-lg);
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-4rpx);
    box-shadow: var(--b2b-shadow-heavy);
  }
  
  .action-icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: var(--b2b-spacing-md);
    box-shadow: var(--b2b-shadow);
  }
  
  .action-title {
    font-size: var(--b2b-font-size-base);
    font-weight: 600;
    color: var(--b2b-text-primary);
    margin-bottom: var(--b2b-spacing-xs);
  }
  
  .action-desc {
    font-size: var(--b2b-font-size-xs);
    color: var(--b2b-text-secondary);
    line-height: 1.4;
  }
}

// 图表区域
.charts-section {
  margin-bottom: var(--b2b-spacing-xl);
}

.charts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400rpx, 1fr));
  gap: var(--b2b-spacing-md);
}

.chart-card {
  padding: var(--b2b-spacing-md);
  
  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: var(--b2b-spacing-md);
    
    .chart-title {
      font-size: var(--b2b-font-size-lg);
      font-weight: 600;
      color: var(--b2b-text-primary);
    }
    
    .chart-controls {
      display: flex;
      gap: var(--b2b-spacing-sm);
      
      .control-item {
        padding: var(--b2b-spacing-xs) var(--b2b-spacing-sm);
        font-size: var(--b2b-font-size-xs);
        color: var(--b2b-text-secondary);
        background-color: var(--b2b-bg-secondary);
        border-radius: calc(var(--b2b-border-radius) / 2);
        cursor: pointer;
        transition: all 0.3s ease;
        
        &:hover,
        &.active {
          background-color: var(--b2b-primary);
          color: white;
        }
      }
    }
  }
  
  .chart-content {
    height: 300rpx;
    
    .chart-canvas {
      width: 100%;
      height: 100%;
    }
  }
}

// 最近活动
.recent-activity {
  margin-bottom: var(--b2b-spacing-xl);
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: var(--b2b-spacing-md);
}

.activity-item {
  display: flex;
  align-items: center;
  padding: var(--b2b-spacing-md);
  
  .activity-icon {
    width: 60rpx;
    height: 60rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: var(--b2b-spacing-md);
    flex-shrink: 0;
  }
  
  .activity-content {
    flex: 1;
    
    .activity-title {
      display: block;
      font-size: var(--b2b-font-size-base);
      font-weight: 600;
      color: var(--b2b-text-primary);
      margin-bottom: var(--b2b-spacing-xs);
    }
    
    .activity-desc {
      display: block;
      font-size: var(--b2b-font-size-sm);
      color: var(--b2b-text-secondary);
      margin-bottom: var(--b2b-spacing-xs);
    }
    
    .activity-time {
      display: block;
      font-size: var(--b2b-font-size-xs);
      color: var(--b2b-text-tertiary);
    }
  }
  
  .activity-status {
    padding: var(--b2b-spacing-xs) var(--b2b-spacing-sm);
    border-radius: calc(var(--b2b-border-radius) / 2);
    font-size: var(--b2b-font-size-xs);
    font-weight: 500;
    
    &.status-success {
      background-color: rgba(103, 194, 58, 0.1);
      color: var(--b2b-success);
    }
    
    &.status-pending {
      background-color: rgba(230, 162, 60, 0.1);
      color: var(--b2b-warning);
    }
    
    &.status-warning {
      background-color: rgba(245, 108, 108, 0.1);
      color: var(--b2b-danger);
    }
    
    &.status-info {
      background-color: rgba(144, 147, 153, 0.1);
      color: var(--b2b-info);
    }
  }
}

// 待办事项
.todo-section {
  margin-bottom: var(--b2b-spacing-xl);
}

.todo-list {
  display: flex;
  flex-direction: column;
  gap: var(--b2b-spacing-md);
}

.todo-item {
  display: flex;
  align-items: center;
  padding: var(--b2b-spacing-md);
  
  &.completed {
    opacity: 0.6;
    
    .todo-title {
      text-decoration: line-through;
    }
  }
  
  .todo-checkbox {
    margin-right: var(--b2b-spacing-md);
    cursor: pointer;
  }
  
  .todo-content {
    flex: 1;
    
    .todo-title {
      display: block;
      font-size: var(--b2b-font-size-base);
      font-weight: 500;
      color: var(--b2b-text-primary);
      margin-bottom: var(--b2b-spacing-xs);
      
      &.completed {
        color: var(--b2b-text-tertiary);
      }
    }
    
    .todo-deadline {
      display: block;
      font-size: var(--b2b-font-size-xs);
      color: var(--b2b-text-secondary);
    }
  }
  
  .todo-priority {
    padding: var(--b2b-spacing-xs) var(--b2b-spacing-sm);
    border-radius: calc(var(--b2b-border-radius) / 2);
    font-size: var(--b2b-font-size-xs);
    font-weight: 500;
    
    &.priority-urgent {
      background-color: rgba(245, 108, 108, 0.1);
      color: var(--b2b-danger);
    }
    
    &.priority-high {
      background-color: rgba(230, 162, 60, 0.1);
      color: var(--b2b-warning);
    }
    
    &.priority-medium {
      background-color: rgba(64, 158, 255, 0.1);
      color: var(--b2b-primary);
    }
    
    &.priority-low {
      background-color: rgba(103, 194, 58, 0.1);
      color: var(--b2b-success);
    }
  }
}

// 背景色工具类
.bg-primary { background-color: var(--b2b-primary) !important; }
.bg-success { background-color: var(--b2b-success) !important; }
.bg-warning { background-color: var(--b2b-warning) !important; }
.bg-danger { background-color: var(--b2b-danger) !important; }
.bg-info { background-color: var(--b2b-info) !important; }

// 响应式设计
@media (max-width: 768px) {
  .stats-cards {
    grid-template-columns: repeat(auto-fit, minmax(250rpx, 1fr));
  }
  
  .charts-grid {
    grid-template-columns: 1fr;
  }
  
  .action-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .b2b-dashboard {
    padding: var(--b2b-spacing-sm);
  }
  
  .stats-cards {
    grid-template-columns: 1fr;
  }
  
  .action-grid {
    grid-template-columns: 1fr;
  }
}
</style>