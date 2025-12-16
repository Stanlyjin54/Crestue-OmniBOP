/**
 * B2B客户详情组件
 * 包含客户基本信息、信用信息、交易统计等
 */

<template>
  <view class="b2b-customer-detail">
    <!-- 客户基本信息 -->
    <view class="customer-info-section">
      <view class="section-header">
        <text class="section-title">客户信息</text>
        <button class="edit-btn" @click="handleEdit" size="mini">编辑</button>
      </view>
      
      <view class="info-grid">
        <view class="info-item">
          <text class="label">客户名称</text>
          <text class="value">{{ customerInfo.customer_name }}</text>
        </view>
        <view class="info-item">
          <text class="label">客户类型</text>
          <text class="value">{{ getCustomerTypeText(customerInfo.customer_type) }}</text>
        </view>
        <view class="info-item">
          <text class="label">客户等级</text>
          <uni-tag :text="getCustomerLevelText(customerInfo.customer_level)" :type="getCustomerLevelColor(customerInfo.customer_level)" />
        </view>
        <view class="info-item">
          <text class="label">联系方式</text>
          <text class="value">{{ customerInfo.contact_phone }}</text>
        </view>
        <view class="info-item">
          <text class="label">邮箱地址</text>
          <text class="value">{{ customerInfo.contact_email }}</text>
        </view>
        <view class="info-item">
          <text class="label">公司地址</text>
          <text class="value">{{ customerInfo.company_address }}</text>
        </view>
        <view class="info-item">
          <text class="label">所属行业</text>
          <text class="value">{{ customerInfo.industry }}</text>
        </view>
        <view class="info-item">
          <text class="label">客户标签</text>
          <view class="tags-container">
            <uni-tag v-for="tag in customerInfo.tags" :key="tag" :text="tag" size="small" />
          </view>
        </view>
      </view>
    </view>

    <!-- 信用信息 -->
    <view class="credit-info-section">
      <view class="section-header">
        <text class="section-title">信用信息</text>
        <button class="refresh-btn" @click="refreshCreditInfo" size="mini">刷新</button>
      </view>
      
      <view class="credit-cards">
        <view class="credit-card">
          <view class="card-header">
            <text class="card-title">信用额度</text>
            <uni-icons type="wallet" size="24" color="#409eff" />
          </view>
          <view class="card-content">
            <text class="credit-amount">¥{{ formatMoney(creditInfo.credit_limit) }}</text>
            <text class="credit-status">{{ getCreditStatusText(creditInfo.credit_status) }}</text>
          </view>
        </view>
        
        <view class="credit-card">
          <view class="card-header">
            <text class="card-title">已用额度</text>
            <uni-icons type="minus" size="24" color="#f56c6c" />
          </view>
          <view class="card-content">
            <text class="credit-used">¥{{ formatMoney(creditInfo.used_credit) }}</text>
            <text class="credit-ratio">{{ getCreditRatio() }}%</text>
          </view>
        </view>
        
        <view class="credit-card">
          <view class="card-header">
            <text class="card-title">可用额度</text>
            <uni-icons type="plus" size="24" color="#67c23a" />
          </view>
          <view class="card-content">
            <text class="credit-available">¥{{ formatMoney(creditInfo.available_credit) }}</text>
            <text class="available-status">{{ getAvailableStatusText() }}</text>
          </view>
        </view>
      </view>
      
      <view v-if="creditInfo.warning_message" class="credit-warning">
        <uni-icons type="warning" color="#f56c6c" />
        <text class="warning-text">{{ creditInfo.warning_message }}</text>
      </view>
    </view>

    <!-- 交易统计 -->
    <view class="transaction-stats-section">
      <view class="section-header">
        <text class="section-title">交易统计</text>
        <picker mode="date" fields="month" @change="handleMonthChange">
          <text class="month-selector">{{ selectedMonth }} <uni-icons type="arrowdown" size="12" /></text>
        </picker>
      </view>
      
      <view class="stats-grid">
        <view class="stat-item">
          <text class="stat-label">本月订单</text>
          <text class="stat-value">{{ transactionStats.monthly_orders }}</text>
        </view>
        <view class="stat-item">
          <text class="stat-label">本月金额</text>
          <text class="stat-value">¥{{ formatMoney(transactionStats.monthly_amount) }}</text>
        </view>
        <view class="stat-item">
          <text class="stat-label">累计订单</text>
          <text class="stat-value">{{ transactionStats.total_orders }}</text>
        </view>
        <view class="stat-item">
          <text class="stat-label">累计金额</text>
          <text class="stat-value">¥{{ formatMoney(transactionStats.total_amount) }}</text>
        </view>
        <view class="stat-item">
          <text class="stat-label">平均订单</text>
          <text class="stat-value">¥{{ formatMoney(transactionStats.avg_order_amount) }}</text>
        </view>
        <view class="stat-item">
          <text class="stat-label">最后交易</text>
          <text class="stat-value">{{ formatDate(transactionStats.last_transaction_date) }}</text>
        </view>
      </view>
    </view>

    <!-- 最近订单 -->
    <view class="recent-orders-section">
      <view class="section-header">
        <text class="section-title">最近订单</text>
        <text class="view-all" @click="viewAllOrders">查看全部 ></text>
      </view>
      
      <view v-if="recentOrders.length > 0" class="orders-list">
        <view v-for="order in recentOrders" :key="order.order_id" class="order-item" @click="viewOrderDetail(order)">
          <view class="order-header">
            <text class="order-no">{{ order.order_no }}</text>
            <uni-tag :text="getOrderStatusText(order.order_status)" :type="getOrderStatusColor(order.order_status)" size="small" />
          </view>
          <view class="order-info">
            <text class="order-date">{{ formatDate(order.order_date) }}</text>
            <text class="order-amount">¥{{ formatMoney(order.total_amount) }}</text>
          </view>
        </view>
      </view>
      <view v-else class="empty-orders">
        <text>暂无订单记录</text>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-buttons">
      <button class="action-btn primary" @click="createOrder">创建订单</button>
      <button class="action-btn secondary" @click="viewTransactionHistory">交易历史</button>
      <button class="action-btn secondary" @click="viewCreditHistory">信用记录</button>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { getB2BCustomerDetail, getB2BCustomerCreditInfo, getB2BCustomerTransactionStats, getB2BCustomerRecentOrders } from '@/api/b2b/customer.js';

const props = defineProps({
  customerId: {
    type: Number,
    required: true
  }
});

const emit = defineEmits(['edit-customer', 'create-order', 'view-orders']);

// 数据状态
const customerInfo = ref({});
const creditInfo = ref({});
const transactionStats = ref({});
const recentOrders = ref([]);
const selectedMonth = ref('');

// 生命周期
onMounted(() => {
  initData();
});

// 方法
async function initData() {
  await Promise.all([
    loadCustomerInfo(),
    loadCreditInfo(),
    loadTransactionStats(),
    loadRecentOrders()
  ]);
}

async function loadCustomerInfo() {
  try {
    const res = await getB2BCustomerDetail(props.customerId);
    if (res.success) {
      customerInfo.value = res.data;
    }
  } catch (error) {
    console.error('加载客户信息失败:', error);
  }
}

async function loadCreditInfo() {
  try {
    const res = await getB2BCustomerCreditInfo(props.customerId);
    if (res.success) {
      creditInfo.value = res.data;
    }
  } catch (error) {
    console.error('加载信用信息失败:', error);
  }
}

async function loadTransactionStats() {
  try {
    const currentMonth = new Date().toISOString().slice(0, 7);
    selectedMonth.value = currentMonth;
    
    const res = await getB2BCustomerTransactionStats(props.customerId, currentMonth);
    if (res.success) {
      transactionStats.value = res.data;
    }
  } catch (error) {
    console.error('加载交易统计失败:', error);
  }
}

async function loadRecentOrders() {
  try {
    const res = await getB2BCustomerRecentOrders(props.customerId, 5);
    if (res.success) {
      recentOrders.value = res.data.records || [];
    }
  } catch (error) {
    console.error('加载最近订单失败:', error);
  }
}

async function refreshCreditInfo() {
  await loadCreditInfo();
}

async function handleMonthChange(e) {
  selectedMonth.value = e.detail.value;
  try {
    const res = await getB2BCustomerTransactionStats(props.customerId, selectedMonth.value);
    if (res.success) {
      transactionStats.value = res.data;
    }
  } catch (error) {
    console.error('加载月度交易统计失败:', error);
  }
}

function handleEdit() {
  emit('edit-customer', customerInfo.value);
}

function createOrder() {
  emit('create-order', customerInfo.value);
}

function viewAllOrders() {
  emit('view-orders', customerInfo.value);
}

function viewOrderDetail(order) {
  // 跳转到订单详情页面
  uni.navigateTo({
    url: `/pagesA/order_detail/index?orderId=${order.order_id}`
  });
}

function viewTransactionHistory() {
  // 跳转到交易历史页面
  uni.navigateTo({
    url: `/pagesA/customer_transaction/index?customerId=${props.customerId}`
  });
}

function viewCreditHistory() {
  // 跳转到信用记录页面
  uni.navigateTo({
    url: `/pagesA/customer_credit_history/index?customerId=${props.customerId}`
  });
}

// 工具函数
function getCustomerTypeText(type) {
  const typeMap = {
    'enterprise': '企业客户',
    'individual': '个人客户',
    'distributor': '经销商',
    'agent': '代理商'
  };
  return typeMap[type] || type;
}

function getCustomerLevelText(level) {
  const levelMap = {
    'A': 'A级客户',
    'B': 'B级客户',
    'C': 'C级客户',
    'VIP': 'VIP客户'
  };
  return levelMap[level] || level;
}

function getCustomerLevelColor(level) {
  const colorMap = {
    'A': 'success',
    'B': 'primary',
    'C': 'warning',
    'VIP': 'error'
  };
  return colorMap[level] || 'default';
}

function getCreditStatusText(status) {
  const statusMap = {
    'active': '正常',
    'suspended': '暂停',
    'expired': '过期',
    'frozen': '冻结'
  };
  return statusMap[status] || status;
}

function getCreditRatio() {
  if (!creditInfo.value.credit_limit) return 0;
  return Math.round((creditInfo.value.used_credit / creditInfo.value.credit_limit) * 100);
}

function getAvailableStatusText() {
  const ratio = getCreditRatio();
  if (ratio >= 90) return '额度紧张';
  if (ratio >= 70) return '额度充足';
  return '额度充足';
}

function getOrderStatusText(status) {
  const statusMap = {
    'pending': '待确认',
    'confirmed': '已确认',
    'processing': '处理中',
    'shipped': '已发货',
    'completed': '已完成',
    'cancelled': '已取消'
  };
  return statusMap[status] || status;
}

function getOrderStatusColor(status) {
  const colorMap = {
    'pending': 'warning',
    'confirmed': 'primary',
    'processing': 'info',
    'shipped': 'info',
    'completed': 'success',
    'cancelled': 'default'
  };
  return colorMap[status] || 'default';
}

function formatMoney(amount) {
  return Number(amount).toFixed(2);
}

function formatDate(dateStr) {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  return date.toLocaleDateString('zh-CN');
}
</script>

<style lang="scss" scoped>
.b2b-customer-detail {
  padding: 20rpx;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.customer-info-section,
.credit-info-section,
.transaction-stats-section,
.recent-orders-section {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
  
  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
  }
  
  .edit-btn,
  .refresh-btn {
    background-color: #409eff;
    color: white;
    border: none;
    font-size: 24rpx;
  }
  
  .month-selector {
    color: #409eff;
    font-size: 28rpx;
    cursor: pointer;
  }
  
  .view-all {
    color: #409eff;
    font-size: 28rpx;
    cursor: pointer;
  }
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  
  .info-item {
    display: flex;
    flex-direction: column;
    
    .label {
      color: #909399;
      font-size: 24rpx;
      margin-bottom: 8rpx;
    }
    
    .value {
      color: #303133;
      font-size: 28rpx;
      font-weight: 500;
    }
    
    .tags-container {
      display: flex;
      flex-wrap: wrap;
      gap: 8rpx;
    }
  }
}

.credit-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20rpx;
  margin-bottom: 20rpx;
}

.credit-card {
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 24rpx;
  text-align: center;
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;
    
    .card-title {
      color: #606266;
      font-size: 24rpx;
    }
  }
  
  .card-content {
    .credit-amount {
      color: #67c23a;
      font-size: 36rpx;
      font-weight: bold;
      display: block;
      margin-bottom: 8rpx;
    }
    
    .credit-used {
      color: #f56c6c;
      font-size: 36rpx;
      font-weight: bold;
      display: block;
      margin-bottom: 8rpx;
    }
    
    .credit-available {
      color: #409eff;
      font-size: 36rpx;
      font-weight: bold;
      display: block;
      margin-bottom: 8rpx;
    }
    
    .credit-status,
    .credit-ratio,
    .available-status {
      color: #909399;
      font-size: 24rpx;
    }
  }
}

.credit-warning {
  display: flex;
  align-items: center;
  background-color: #fef0f0;
  padding: 16rpx;
  border-radius: 8rpx;
  
  .warning-text {
    color: #f56c6c;
    font-size: 28rpx;
    margin-left: 8rpx;
  }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20rpx;
  
  .stat-item {
    text-align: center;
    padding: 20rpx;
    background-color: #f5f7fa;
    border-radius: 8rpx;
    
    .stat-label {
      color: #909399;
      font-size: 24rpx;
      display: block;
      margin-bottom: 8rpx;
    }
    
    .stat-value {
      color: #303133;
      font-size: 32rpx;
      font-weight: bold;
    }
  }
}

.orders-list {
  .order-item {
    padding: 20rpx;
    background-color: #f5f7fa;
    border-radius: 8rpx;
    margin-bottom: 16rpx;
    cursor: pointer;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .order-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12rpx;
      
      .order-no {
        color: #303133;
        font-size: 28rpx;
        font-weight: 500;
      }
    }
    
    .order-info {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .order-date {
        color: #909399;
        font-size: 24rpx;
      }
      
      .order-amount {
        color: #f56c6c;
        font-size: 28rpx;
        font-weight: bold;
      }
    }
  }
}

.empty-orders {
  text-align: center;
  padding: 40rpx;
  color: #909399;
  font-size: 28rpx;
}

.action-buttons {
  display: flex;
  gap: 20rpx;
  padding: 20rpx 0;
  
  .action-btn {
    flex: 1;
    padding: 24rpx;
    border-radius: 8rpx;
    font-size: 28rpx;
    
    &.primary {
      background-color: #409eff;
      color: white;
    }
    
    &.secondary {
      background-color: #f5f7fa;
      color: #606266;
      border: 2rpx solid #dcdfe6;
    }
  }
}
</style>