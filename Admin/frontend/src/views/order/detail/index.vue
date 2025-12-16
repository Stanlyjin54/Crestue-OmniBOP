<template>
  <base-wrapper>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">订单详情</h1>
          <p class="page-description">订单编号：{{ orderDetail.orderNo }}</p>
        </div>
        <div class="header-right">
          <el-button @click="goBack">
            <el-icon><ArrowLeft /></el-icon>返回
          </el-button>
          <el-button type="primary" @click="handleEdit">
            <el-icon><Edit /></el-icon>编辑
          </el-button>
          <el-button 
            type="success" 
            @click="handlePrint"
            v-if="orderDetail.status !== 'cancelled'"
          >
            <el-icon><Printer /></el-icon>打印
          </el-button>
          <el-dropdown @command="handleMoreAction">
            <el-button type="info">
              更多<el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="duplicate">复制订单</el-dropdown-item>
                <el-dropdown-item command="refund" v-if="orderDetail.paymentStatus === 'paid'">申请退款</el-dropdown-item>
                <el-dropdown-item command="cancel" divided v-if="orderDetail.status === 'pending'">取消订单</el-dropdown-item>
                <el-dropdown-item command="delete" style="color: #f56c6c">删除订单</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/order' }">订单管理</el-breadcrumb-item>
      <el-breadcrumb-item>订单详情</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 订单状态流程 -->
    <div class="status-flow-container">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>订单状态</span>
            <el-tag :type="getStatusTagType(orderDetail.status)" size="large">
              {{ getStatusLabel(orderDetail.status) }}
            </el-tag>
          </div>
        </template>
        <div class="status-flow">
          <div 
            v-for="(step, index) in statusSteps" 
            :key="step.value"
            class="status-step"
            :class="{ active: isStepActive(step.value), completed: isStepCompleted(step.value) }"
          >
            <div class="step-icon">
              <el-icon><component :is="step.icon" /></el-icon>
            </div>
            <div class="step-content">
              <div class="step-title">{{ step.label }}</div>
              <div class="step-time">{{ getStepTime(step.value) }}</div>
            </div>
            <div class="step-arrow" v-if="index < statusSteps.length - 1">
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 订单信息 -->
    <el-row :gutter="20" class="info-container">
      <!-- 基本信息 -->
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span><el-icon><InfoFilled /></el-icon>基本信息</span>
            </div>
          </template>
          <div class="info-list">
            <div class="info-item">
              <span class="info-label">订单编号：</span>
              <span class="info-value">{{ orderDetail.orderNo }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">订单类型：</span>
              <el-tag :type="getOrderTypeTag(orderDetail.orderType)">
                {{ getOrderTypeLabel(orderDetail.orderType) }}
              </el-tag>
            </div>
            <div class="info-item">
              <span class="info-label">下单时间：</span>
              <span class="info-value">{{ orderDetail.orderDate }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">销售代表：</span>
              <span class="info-value">{{ orderDetail.salesRep }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">备注：</span>
              <span class="info-value">{{ orderDetail.remark || '无' }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 客户信息 -->
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span><el-icon><User /></el-icon>客户信息</span>
            </div>
          </template>
          <div class="info-list">
            <div class="info-item">
              <span class="info-label">客户名称：</span>
              <span class="info-value">{{ orderDetail.customerName }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">联系人：</span>
              <span class="info-value">{{ orderDetail.contactPerson }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">联系电话：</span>
              <span class="info-value">{{ orderDetail.contactPhone }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">邮箱：</span>
              <span class="info-value">{{ orderDetail.contactEmail }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">客户等级：</span>
              <el-tag>{{ orderDetail.customerLevel }}</el-tag>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 支付信息 -->
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span><el-icon><Money /></el-icon>支付信息</span>
            </div>
          </template>
          <div class="info-list">
            <div class="info-item">
              <span class="info-label">支付方式：</span>
              <span class="info-value">{{ getPaymentMethodLabel(orderDetail.paymentMethod) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">支付状态：</span>
              <el-tag :type="getPaymentStatusTag(orderDetail.paymentStatus)">
                {{ getPaymentStatusLabel(orderDetail.paymentStatus) }}
              </el-tag>
            </div>
            <div class="info-item">
              <span class="info-label">支付时间：</span>
              <span class="info-value">{{ orderDetail.paymentTime || '未支付' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">支付订单号：</span>
              <span class="info-value">{{ orderDetail.paymentOrderNo || '无' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">账期信息：</span>
              <span class="info-value">{{ orderDetail.creditInfo || '无' }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 商品清单 -->
    <el-card class="product-card">
      <template #header>
        <div class="card-header">
          <span><el-icon><ShoppingCart /></el-icon>商品清单</span>
          <span class="item-count">共 {{ orderDetail.items?.length || 0 }} 件商品</span>
        </div>
      </template>
      <el-table :data="orderDetail.items" border style="width: 100%">
        <el-table-column prop="productName" label="商品名称" min-width="200" />
        <el-table-column prop="specification" label="规格" width="120" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="unitPrice" label="单价" width="100" align="right">
          <template #default="{ row }">
            ¥{{ formatMoney(row.unitPrice) }}
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="100" align="center" />
        <el-table-column prop="totalPrice" label="小计" width="120" align="right">
          <template #default="{ row }">
            ¥{{ formatMoney(row.totalPrice) }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" />
      </el-table>
    </el-card>

    <!-- 金额汇总 -->
    <el-card class="amount-card">
      <template #header>
        <div class="card-header">
          <span><el-icon><Calculator /></el-icon>金额汇总</span>
        </div>
      </template>
      <div class="amount-summary">
        <div class="amount-row">
          <span class="amount-label">商品总额：</span>
          <span class="amount-value">¥{{ formatMoney(orderDetail.productAmount) }}</span>
        </div>
        <div class="amount-row">
          <span class="amount-label">优惠金额：</span>
          <span class="amount-value discount">-¥{{ formatMoney(orderDetail.discountAmount) }}</span>
        </div>
        <div class="amount-row">
          <span class="amount-label">运费：</span>
          <span class="amount-value">¥{{ formatMoney(orderDetail.shippingFee) }}</span>
        </div>
        <div class="amount-row">
          <span class="amount-label">税费：</span>
          <span class="amount-value">¥{{ formatMoney(orderDetail.taxAmount) }}</span>
        </div>
        <div class="amount-row total">
          <span class="amount-label">订单总额：</span>
          <span class="amount-value total">¥{{ formatMoney(orderDetail.totalAmount) }}</span>
        </div>
        <div class="amount-row">
          <span class="amount-label">已付金额：</span>
          <span class="amount-value paid">¥{{ formatMoney(orderDetail.paidAmount) }}</span>
        </div>
        <div class="amount-row" v-if="orderDetail.totalAmount > orderDetail.paidAmount">
          <span class="amount-label">待付金额：</span>
          <span class="amount-value unpaid">¥{{ formatMoney(orderDetail.totalAmount - orderDetail.paidAmount) }}</span>
        </div>
      </div>
    </el-card>

    <!-- 配送信息 -->
    <el-card class="shipping-card">
      <template #header>
        <div class="card-header">
          <span><el-icon><Truck /></el-icon>配送信息</span>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-list">
            <div class="info-item">
              <span class="info-label">配送方式：</span>
              <span class="info-value">{{ getShippingMethodLabel(orderDetail.shippingMethod) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">物流公司：</span>
              <span class="info-value">{{ orderDetail.shippingCompany || '待分配' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">物流单号：</span>
              <span class="info-value">{{ orderDetail.trackingNumber || '待生成' }}</span>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-list">
            <div class="info-item">
              <span class="info-label">发货时间：</span>
              <span class="info-value">{{ orderDetail.shippingDate || '未发货' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">预计到达：</span>
              <span class="info-value">{{ orderDetail.estimatedDeliveryDate || '待确认' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">签收时间：</span>
              <span class="info-value">{{ orderDetail.deliveryDate || '未签收' }}</span>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <span class="info-label">配送地址：</span>
            <span class="info-value">{{ orderDetail.shippingAddress }}</span>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 操作记录 -->
    <el-card class="log-card">
      <template #header>
        <div class="card-header">
          <span><el-icon><Clock /></el-icon>操作记录</span>
        </div>
      </template>
      <el-timeline>
        <el-timeline-item
          v-for="(log, index) in orderLogs"
          :key="index"
          :timestamp="log.timestamp"
          :type="log.type"
          :color="log.color"
          :icon="log.icon"
        >
          <div class="log-content">
            <div class="log-title">{{ log.title }}</div>
            <div class="log-description">{{ log.description }}</div>
            <div class="log-operator">操作人：{{ log.operator }}</div>
          </div>
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <!-- 状态更新对话框 -->
    <el-dialog
      v-model="statusDialogVisible"
      title="更新订单状态"
      width="500px"
    >
      <el-form :model="statusForm" label-width="100px">
        <el-form-item label="新状态">
          <el-select v-model="statusForm.status" placeholder="请选择新状态">
            <el-option
              v-for="status in availableStatuses"
              :key="status.value"
              :label="status.label"
              :value="status.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="statusForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入状态变更备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="statusDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleStatusUpdate">确定</el-button>
      </template>
    </el-dialog>
  </base-wrapper>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  ArrowLeft, Edit, Printer, ArrowDown, InfoFilled, User, Money, 
  ShoppingCart, Calculator, Truck, Clock, CircleCheck, Loading, 
  Warning, CircleClose, ArrowRight
} from '@element-plus/icons-vue'
import { useOrder } from '@/composables/useOrder'

const route = useRoute()
const router = useRouter()
const { getOrderById, updateOrder } = useOrder()

// 数据状态
const loading = ref(false)
const orderDetail = ref({})
const orderLogs = ref([])

// 状态更新对话框
const statusDialogVisible = ref(false)
const statusForm = reactive({
  status: '',
  remark: ''
})

// 订单状态流程步骤
const statusSteps = [
  { value: 'pending', label: '待确认', icon: 'Warning', color: '#e6a23c' },
  { value: 'confirmed', label: '已确认', icon: 'CircleCheck', color: '#67c23a' },
  { value: 'processing', label: '处理中', icon: 'Loading', color: '#409eff' },
  { value: 'shipped', label: '已发货', icon: 'Truck', color: '#409eff' },
  { value: 'delivered', label: '已送达', icon: 'CircleCheck', color: '#67c23a' },
  { value: 'completed', label: '已完成', icon: 'CircleCheck', color: '#67c23a' }
]

// 订单状态配置
const ORDER_STATUS = [
  { value: 'pending', label: '待确认' },
  { value: 'confirmed', label: '已确认' },
  { value: 'processing', label: '处理中' },
  { value: 'shipped', label: '已发货' },
  { value: 'delivered', label: '已送达' },
  { value: 'completed', label: '已完成' },
  { value: 'cancelled', label: '已取消' }
]

// 订单类型配置
const ORDER_TYPES = [
  { value: 'b2b', label: 'B2B订单' },
  { value: 'b2c', label: 'B2C订单' },
  { value: 'wholesale', label: '批发订单' },
  { value: 'sample', label: '样品订单' }
]

// 支付方式配置
const PAYMENT_METHODS = [
  { value: 'cash', label: '现金' },
  { value: 'bank_transfer', label: '银行转账' },
  { value: 'credit', label: '账期支付' },
  { value: 'installment', label: '分期付款' },
  { value: 'alipay', label: '支付宝' },
  { value: 'wechat', label: '微信支付' }
]

// 配送方式配置
const SHIPPING_METHODS = [
  { value: 'express', label: '快递' },
  { value: 'logistics', label: '物流' },
  { value: 'self_pickup', label: '自提' },
  { value: 'delivery', label: '送货上门' }
]

// 计算属性
const availableStatuses = computed(() => {
  if (!orderDetail.value.status) return []
  const currentStatus = orderDetail.value.status
  // 根据当前状态返回可用的下一步状态
  const statusFlow = {
    pending: ['confirmed', 'cancelled'],
    confirmed: ['processing', 'cancelled'],
    processing: ['shipped', 'cancelled'],
    shipped: ['delivered'],
    delivered: ['completed'],
    completed: [],
    cancelled: []
  }
  return ORDER_STATUS.filter(status => 
    statusFlow[currentStatus]?.includes(status.value)
  )
})

// 方法
const loadOrderDetail = async () => {
  const orderId = route.params.id
  if (!orderId) {
    ElMessage.error('订单ID不存在')
    router.push('/order')
    return
  }
  
  loading.value = true
  try {
    const res = await getOrderById(orderId)
    orderDetail.value = res.data
    loadOrderLogs()
  } catch (error) {
    ElMessage.error('加载订单详情失败')
    router.push('/order')
  } finally {
    loading.value = false
  }
}

const loadOrderLogs = () => {
  // 模拟操作记录数据
  orderLogs.value = [
    {
      timestamp: orderDetail.value.orderDate + ' 10:30:00',
      title: '订单创建',
      description: '客户下单，订单状态：待确认',
      operator: orderDetail.value.customerName,
      type: 'primary',
      color: '#409eff',
      icon: 'CircleCheck'
    },
    {
      timestamp: orderDetail.value.orderDate + ' 14:20:00',
      title: '订单确认',
      description: '销售代表确认订单，状态更新为：已确认',
      operator: orderDetail.value.salesRep,
      type: 'success',
      color: '#67c23a',
      icon: 'CircleCheck'
    }
  ]
  
  // 根据订单状态添加相应的日志
  if (orderDetail.value.status === 'processing') {
    orderLogs.value.push({
      timestamp: orderDetail.value.processingDate || orderDetail.value.orderDate + ' 16:45:00',
      title: '订单处理',
      description: '订单进入处理流程，状态更新为：处理中',
      operator: '系统',
      type: 'warning',
      color: '#e6a23c',
      icon: 'Loading'
    })
  }
  
  if (orderDetail.value.status === 'shipped') {
    orderLogs.value.push({
      timestamp: orderDetail.value.shippingDate || orderDetail.value.orderDate + ' 09:15:00',
      title: '订单发货',
      description: '订单已发货，状态更新为：已发货',
      operator: '仓库管理员',
      type: 'primary',
      color: '#409eff',
      icon: 'Truck'
    })
  }
}

const goBack = () => {
  router.push('/order')
}

const handleEdit = () => {
  router.push(`/order/edit/${orderDetail.value.id}`)
}

const handlePrint = () => {
  window.print()
}

const handleMoreAction = async (command) => {
  switch (command) {
    case 'duplicate':
      ElMessage.info('复制订单功能开发中')
      break
    case 'refund':
      ElMessage.info('退款功能开发中')
      break
    case 'cancel':
      await handleCancel()
      break
    case 'delete':
      await handleDelete()
      break
  }
}

const handleCancel = async () => {
  await ElMessageBox.confirm('确定要取消该订单吗？', '取消确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  try {
    await updateOrder(orderDetail.value.id, { status: 'cancelled' })
    ElMessage.success('订单取消成功')
    loadOrderDetail()
  } catch (error) {
    ElMessage.error('订单取消失败')
  }
}

const handleDelete = async () => {
  await ElMessageBox.confirm('确定要删除该订单吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  try {
    // await deleteOrder(orderDetail.value.id)
    ElMessage.success('订单删除成功')
    router.push('/order')
  } catch (error) {
    ElMessage.error('订单删除失败')
  }
}

const handleStatusUpdate = async () => {
  if (!statusForm.status) {
    ElMessage.warning('请选择新状态')
    return
  }
  
  try {
    await updateOrder(orderDetail.value.id, {
      status: statusForm.status,
      remark: statusForm.remark
    })
    ElMessage.success('状态更新成功')
    statusDialogVisible.value = false
    loadOrderDetail()
  } catch (error) {
    ElMessage.error('状态更新失败')
  }
}

// 工具函数
const formatMoney = (amount) => {
  return Number(amount).toFixed(2)
}

const isStepActive = (stepValue) => {
  return orderDetail.value.status === stepValue
}

const isStepCompleted = (stepValue) => {
  const statusOrder = ['pending', 'confirmed', 'processing', 'shipped', 'delivered', 'completed']
  const currentIndex = statusOrder.indexOf(orderDetail.value.status)
  const stepIndex = statusOrder.indexOf(stepValue)
  return stepIndex <= currentIndex
}

const getStepTime = (stepValue) => {
  const timeMap = {
    pending: orderDetail.value.orderDate,
    confirmed: orderDetail.value.confirmedDate,
    processing: orderDetail.value.processingDate,
    shipped: orderDetail.value.shippingDate,
    delivered: orderDetail.value.deliveryDate,
    completed: orderDetail.value.completedDate
  }
  return timeMap[stepValue] || '待处理'
}

const getStatusTagType = (status) => {
  const tagTypes = {
    pending: 'warning',
    confirmed: 'primary',
    processing: 'info',
    shipped: 'info',
    delivered: 'success',
    completed: 'success',
    cancelled: 'danger'
  }
  return tagTypes[status] || 'info'
}

const getStatusLabel = (status) => {
  const statusMap = ORDER_STATUS.reduce((acc, item) => {
    acc[item.value] = item.label
    return acc
  }, {})
  return statusMap[status] || status
}

const getOrderTypeTag = (type) => {
  const tagTypes = {
    b2b: 'primary',
    b2c: 'success',
    wholesale: 'warning',
    sample: 'info'
  }
  return tagTypes[type] || 'info'
}

const getOrderTypeLabel = (type) => {
  const typeMap = ORDER_TYPES.reduce((acc, item) => {
    acc[item.value] = item.label
    return acc
  }, {})
  return typeMap[type] || type
}

const getPaymentStatusTag = (status) => {
  const tagTypes = {
    unpaid: 'danger',
    partial: 'warning',
    paid: 'success',
    refunded: 'info'
  }
  return tagTypes[status] || 'info'
}

const getPaymentStatusLabel = (status) => {
  const labels = {
    unpaid: '未支付',
    partial: '部分支付',
    paid: '已支付',
    refunded: '已退款'
  }
  return labels[status] || status
}

const getPaymentMethodLabel = (method) => {
  const methodMap = PAYMENT_METHODS.reduce((acc, item) => {
    acc[item.value] = item.label
    return acc
  }, {})
  return methodMap[method] || method
}

const getShippingMethodLabel = (method) => {
  const methodMap = SHIPPING_METHODS.reduce((acc, item) => {
    acc[item.value] = item.label
    return acc
  }, {})
  return methodMap[method] || method
}

// 生命周期
onMounted(() => {
  loadOrderDetail()
})
</script>

<style lang="scss" scoped>
.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  
  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .page-title {
    font-size: 24px;
    font-weight: bold;
    color: #fff;
    margin: 0 0 8px 0;
  }
  
  .page-description {
    font-size: 14px;
    color: rgba(255, 255, 255, 0.8);
    margin: 0;
  }
}

.breadcrumb {
  margin-bottom: 20px;
}

.status-flow-container {
  margin-bottom: 20px;
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .status-flow {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20px 0;
  }
  
  .status-step {
    display: flex;
    align-items: center;
    position: relative;
    
    &.active {
      .step-icon {
        background: #409eff;
        color: white;
      }
      
      .step-title {
        color: #409eff;
        font-weight: bold;
      }
    }
    
    &.completed {
      .step-icon {
        background: #67c23a;
        color: white;
      }
      
      .step-title {
        color: #67c23a;
      }
    }
    
    &:not(.active):not(.completed) {
      .step-icon {
        background: #f5f7fa;
        color: #c0c4cc;
      }
      
      .step-title {
        color: #909399;
      }
    }
    
    .step-icon {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 10px;
      
      .el-icon {
        font-size: 20px;
      }
    }
    
    .step-content {
      .step-title {
        font-size: 14px;
        margin-bottom: 4px;
      }
      
      .step-time {
        font-size: 12px;
        color: #909399;
      }
    }
    
    .step-arrow {
      margin: 0 20px;
      color: #c0c4cc;
      
      .el-icon {
        font-size: 16px;
      }
    }
  }
}

.info-container {
  margin-bottom: 20px;
}

.info-list {
  .info-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 0;
    border-bottom: 1px solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .info-label {
      color: #606266;
      font-weight: 500;
    }
    
    .info-value {
      color: #303133;
    }
  }
}

.product-card,
.amount-card,
.shipping-card,
.log-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  font-weight: bold;
  
  .el-icon {
    margin-right: 8px;
  }
  
  .item-count {
    margin-left: auto;
    color: #909399;
    font-size: 14px;
    font-weight: normal;
  }
}

.amount-summary {
  padding: 20px;
  background: #fafafa;
  border-radius: 4px;
  
  .amount-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px 0;
    
    &.total {
      font-size: 18px;
      font-weight: bold;
      border-top: 2px solid #e4e7ed;
      margin-top: 10px;
      padding-top: 15px;
      
      .amount-value {
        color: #f56c6c;
      }
    }
    
    .amount-label {
      color: #606266;
    }
    
    .amount-value {
      color: #303133;
      font-weight: 500;
      
      &.discount {
        color: #67c23a;
      }
      
      &.paid {
        color: #409eff;
      }
      
      &.unpaid {
        color: #f56c6c;
      }
    }
  }
}

.log-content {
  .log-title {
    font-weight: bold;
    margin-bottom: 5px;
  }
  
  .log-description {
    color: #606266;
    margin-bottom: 5px;
  }
  
  .log-operator {
    color: #909399;
    font-size: 12px;
  }
}

// 深色主题适配
:deep(.dark) {
  .page-header {
    background: linear-gradient(135deg, #434343 0%, #000000 100%);
  }
  
  .info-list {
    .info-item {
      border-bottom-color: #4c4d4f;
      
      .info-label {
        color: #cfd3dc;
      }
      
      .info-value {
        color: #e4e7ed;
      }
    }
  }
  
  .amount-summary {
    background: #2b2b2b;
    
    .amount-row {
      .amount-label {
        color: #cfd3dc;
      }
      
      .amount-value {
        color: #e4e7ed;
      }
    }
  }
}

// 打印样式
@media print {
  .page-header,
  .breadcrumb,
  .el-card__header {
    display: none !important;
  }
  
  .el-card {
    border: 1px solid #e4e7ed !important;
    margin-bottom: 10px !important;
    box-shadow: none !important;
  }
}
</style>