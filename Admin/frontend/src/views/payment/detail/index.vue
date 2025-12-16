<template>
  <base-wrapper>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">支付详情</h1>
          <p class="page-description">查看支付记录的详细信息</p>
        </div>
        <div class="header-right">
          <el-button @click="goBack">
            <el-icon><ArrowLeft /></el-icon>返回
          </el-button>
          <el-button type="primary" @click="handlePrint">
            <el-icon><Printer /></el-icon>打印
          </el-button>
          <el-button type="success" @click="handleEdit" v-if="paymentData.status === 'pending'">
            <el-icon><Edit /></el-icon>编辑
          </el-button>
          <el-dropdown @command="handleMoreAction">
            <el-button>
              <el-icon><MoreFilled /></el-icon>更多
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="export">
                  <el-icon><Download /></el-icon>导出
                </el-dropdown-item>
                <el-dropdown-item command="log" divided>
                  <el-icon><Clock /></el-icon>查看日志
                </el-dropdown-item>
                <el-dropdown-item command="delete" style="color: #f56c6c">
                  <el-icon><Delete /></el-icon>删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/payment' }">支付管理</el-breadcrumb-item>
      <el-breadcrumb-item>支付详情</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 支付状态流程 -->
    <el-card class="status-flow-card">
      <div class="status-flow">
        <div 
          class="status-item" 
          v-for="(status, index) in statusFlow" 
          :key="status.value"
          :class="{ 
            active: isStatusActive(status.value),
            completed: isStatusCompleted(status.value)
          }"
        >
          <div class="status-icon">
            <el-icon :size="24">
              <component :is="status.icon" />
            </el-icon>
          </div>
          <div class="status-content">
            <div class="status-title">{{ status.label }}</div>
            <div class="status-time" v-if="getStatusTime(status.value)">
              {{ getStatusTime(status.value) }}
            </div>
          </div>
          <div class="status-arrow" v-if="index < statusFlow.length - 1">
            <el-icon><ArrowRight /></el-icon>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 基本信息卡片 -->
    <el-card class="info-card">
      <template #header>
        <div class="card-header">
          <span><el-icon><Document /></el-icon>基本信息</span>
        </div>
      </template>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="支付编号">
          <span class="info-value">{{ paymentData.paymentNo }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="订单编号">
          <span class="info-value">{{ paymentData.orderNo }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="客户名称">
          <span class="info-value">{{ paymentData.customerName }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="支付方式">
          <el-tag :type="getPaymentMethodTagType(paymentData.paymentMethod)">
            {{ getPaymentMethodLabel(paymentData.paymentMethod) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="支付金额">
          <span class="info-value amount">¥{{ formatMoney(paymentData.amount) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="支付状态">
          <el-tag :type="getStatusTagType(paymentData.status)">
            {{ getStatusLabel(paymentData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="支付时间">
          <span class="info-value">{{ paymentData.paymentDate }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="到期时间">
          <span class="info-value" :class="{ 'text-danger': isOverdue(paymentData.dueDate) }">
            {{ paymentData.dueDate }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          <span class="info-value">{{ paymentData.createdAt }}</span>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 账期/分期信息 -->
    <el-card class="info-card" v-if="paymentData.paymentMethod === 'credit' || paymentData.paymentMethod === 'installment'">
      <template #header>
        <div class="card-header">
          <span>
            <el-icon><Calendar /></el-icon>
            {{ paymentData.paymentMethod === 'credit' ? '账期信息' : '分期信息' }}
          </span>
        </div>
      </template>
      
      <!-- 账期信息 -->
      <template v-if="paymentData.paymentMethod === 'credit'">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="账期天数">
            <span class="info-value">{{ paymentData.creditDays }} 天</span>
          </el-descriptions-item>
          <el-descriptions-item label="逾期天数">
            <span class="info-value" :class="{ 'text-danger': paymentData.overdueDays > 0 }">
              {{ paymentData.overdueDays > 0 ? paymentData.overdueDays + ' 天' : '无逾期' }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="逾期利息">
            <span class="info-value">¥{{ formatMoney(paymentData.overdueInterest || 0) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="应还总额">
            <span class="info-value amount">¥{{ formatMoney(paymentData.totalAmount || paymentData.amount) }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </template>
      
      <!-- 分期信息 -->
      <template v-if="paymentData.paymentMethod === 'installment'">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="分期期数">
            <span class="info-value">{{ paymentData.installmentInfo?.total || 0 }} 期</span>
          </el-descriptions-item>
          <el-descriptions-item label="当前期数">
            <span class="info-value">{{ paymentData.installmentInfo?.current || 0 }} 期</span>
          </el-descriptions-item>
          <el-descriptions-item label="首付金额">
            <span class="info-value">¥{{ formatMoney(paymentData.downPayment || 0) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="每期金额">
            <span class="info-value">¥{{ formatMoney(paymentData.installmentInfo?.perAmount || 0) }}</span>
          </el-descriptions-item>
        </el-descriptions>
        
        <!-- 分期计划表 -->
        <div class="installment-table" v-if="paymentData.installmentPlan && paymentData.installmentPlan.length > 0">
          <h4>分期还款计划</h4>
          <el-table :data="paymentData.installmentPlan" border style="width: 100%">
            <el-table-column type="index" label="期数" width="80" />
            <el-table-column prop="dueDate" label="应还日期" width="150" />
            <el-table-column prop="amount" label="应还金额" width="120">
              <template #default="{ row }">
                ¥{{ formatMoney(row.amount) }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getInstallmentStatusType(row.status)">
                  {{ getInstallmentStatusLabel(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="paidDate" label="实还日期" width="150" />
            <el-table-column prop="remark" label="备注" />
          </el-table>
        </div>
      </template>
    </el-card>

    <!-- 银行信息 -->
    <el-card class="info-card" v-if="paymentData.paymentMethod === 'bank_transfer'">
      <template #header>
        <div class="card-header">
          <span><el-icon><Bank /></el-icon>银行信息</span>
        </div>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="银行名称">
          <span class="info-value">{{ paymentData.bankName }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="银行账号">
          <span class="info-value">{{ paymentData.bankAccount }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="账户名称">
          <span class="info-value">{{ paymentData.accountName }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="转账流水号">
          <span class="info-value">{{ paymentData.transactionNo }}</span>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 支付凭证 -->
    <el-card class="info-card" v-if="paymentData.vouchers && paymentData.vouchers.length > 0">
      <template #header>
        <div class="card-header">
          <span><el-icon><Picture /></el-icon>支付凭证</span>
        </div>
      </template>
      <div class="voucher-gallery">
        <div 
          class="voucher-item" 
          v-for="(voucher, index) in paymentData.vouchers" 
          :key="index"
          @click="previewVoucher(voucher.url)"
        >
          <img :src="voucher.url" :alt="voucher.name" />
          <div class="voucher-name">{{ voucher.name }}</div>
        </div>
      </div>
    </el-card>

    <!-- 备注信息 -->
    <el-card class="info-card" v-if="paymentData.remark">
      <template #header>
        <div class="card-header">
          <span><el-icon><ChatLineRound /></el-icon>备注信息</span>
        </div>
      </template>
      <div class="remark-content">
        {{ paymentData.remark }}
      </div>
    </el-card>

    <!-- 操作记录 -->
    <el-card class="info-card">
      <template #header>
        <div class="card-header">
          <span><el-icon><Clock /></el-icon>操作记录</span>
        </div>
      </template>
      <el-timeline>
        <el-timeline-item
          v-for="(log, index) in operationLogs"
          :key="index"
          :timestamp="log.timestamp"
          :type="getLogType(log.type)"
          placement="top"
        >
          <el-card>
            <h4>{{ log.title }}</h4>
            <p>{{ log.content }}</p>
            <p class="log-operator">操作人：{{ log.operator }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </base-wrapper>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  ArrowLeft, Printer, Edit, MoreFilled, Download, 
  Delete, Clock, Document, Calendar, Bank, Picture, 
  ChatLineRound, CircleCheck, RefreshLeft, Money, 
  Warning, Check, Close
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

// 支付数据
const paymentData = reactive({
  id: '',
  paymentNo: 'PAY202312010001',
  orderNo: 'ORD202312010001',
  customerName: '某某公司',
  paymentMethod: 'credit',
  amount: 50000,
  status: 'pending',
  paymentDate: '2023-12-01',
  dueDate: '2024-01-01',
  createdAt: '2023-12-01 10:30:00',
  creditDays: 30,
  overdueDays: 0,
  overdueInterest: 0,
  totalAmount: 50000,
  installmentInfo: null,
  installmentPlan: [],
  bankName: '',
  bankAccount: '',
  accountName: '',
  transactionNo: '',
  vouchers: [
    { name: '支付凭证1.jpg', url: 'https://via.placeholder.com/300x200' },
    { name: '支付凭证2.jpg', url: 'https://via.placeholder.com/300x200' }
  ],
  remark: '这是一笔账期支付，请在到期前完成付款。'
})

// 操作记录
const operationLogs = ref([
  {
    timestamp: '2023-12-01 10:30:00',
    type: 'create',
    title: '创建支付记录',
    content: '创建了支付记录，支付方式：账期支付，金额：¥50,000.00',
    operator: '张三'
  },
  {
    timestamp: '2023-12-01 10:35:00',
    type: 'update',
    title: '更新支付信息',
    content: '更新了支付凭证信息',
    operator: '李四'
  }
])

// 状态流程
const statusFlow = [
  { value: 'pending', label: '待支付', icon: 'Clock' },
  { value: 'paid', label: '已支付', icon: 'CircleCheck' },
  { value: 'refunded', label: '已退款', icon: 'RefreshLeft' }
]

// 支付方式配置
const PAYMENT_METHODS = [
  { value: 'cash', label: '现金', tagType: 'success' },
  { value: 'bank_transfer', label: '银行转账', tagType: 'primary' },
  { value: 'credit', label: '账期支付', tagType: 'warning' },
  { value: 'installment', label: '分期付款', tagType: 'info' },
  { value: 'alipay', label: '支付宝', tagType: 'primary' },
  { value: 'wechat', label: '微信支付', tagType: 'success' }
]

// 支付状态配置
const PAYMENT_STATUS = [
  { value: 'pending', label: '待支付', tagType: 'warning' },
  { value: 'paid', label: '已支付', tagType: 'success' },
  { value: 'partial', label: '部分支付', tagType: 'info' },
  { value: 'refunded', label: '已退款', tagType: 'info' },
  { value: 'cancelled', label: '已取消', tagType: 'danger' }
]

// 分期状态配置
const INSTALLMENT_STATUS = [
  { value: 'pending', label: '待还款', tagType: 'warning' },
  { value: 'paid', label: '已还款', tagType: 'success' },
  { value: 'overdue', label: '逾期', tagType: 'danger' }
]

// 方法
const loadPaymentDetail = async () => {
  const paymentId = route.params.id
  if (!paymentId) {
    ElMessage.error('支付ID不存在')
    return
  }
  
  // 这里调用API获取支付详情
  console.log('加载支付详情:', paymentId)
  
  // 模拟数据加载
  setTimeout(() => {
    // 更新支付数据
    Object.assign(paymentData, {
      id: paymentId,
      // ... 其他数据
    })
  }, 500)
}

const goBack = () => {
  router.push('/payment')
}

const handlePrint = () => {
  window.print()
}

const handleEdit = () => {
  router.push(`/payment/edit/${paymentData.id}`)
}

const handleMoreAction = async (command) => {
  switch (command) {
    case 'export':
      ElMessage.info('导出功能开发中')
      break
    case 'log':
      ElMessage.info('日志功能开发中')
      break
    case 'delete':
      await handleDelete()
      break
  }
}

const handleDelete = async () => {
  await ElMessageBox.confirm('确定要删除该支付记录吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  // 这里调用API删除支付记录
  console.log('删除支付记录:', paymentData.id)
  ElMessage.success('删除成功')
  goBack()
}

const previewVoucher = (url) => {
  window.open(url, '_blank')
}

// 工具函数
const formatMoney = (amount) => {
  return Number(amount).toFixed(2)
}

const isOverdue = (dueDate) => {
  if (!dueDate) return false
  return new Date(dueDate) < new Date()
}

const isStatusActive = (status) => {
  return paymentData.status === status
}

const isStatusCompleted = (status) => {
  const statusOrder = ['pending', 'paid', 'refunded']
  const currentIndex = statusOrder.indexOf(paymentData.status)
  const targetIndex = statusOrder.indexOf(status)
  return targetIndex < currentIndex
}

const getStatusTime = (status) => {
  // 根据状态返回对应的时间
  const timeMap = {
    'pending': paymentData.createdAt,
    'paid': paymentData.paymentDate,
    'refunded': paymentData.refundDate
  }
  return timeMap[status] || ''
}

const getPaymentMethodLabel = (method) => {
  const methodMap = PAYMENT_METHODS.reduce((acc, item) => {
    acc[item.value] = item.label
    return acc
  }, {})
  return methodMap[method] || method
}

const getPaymentMethodTagType = (method) => {
  const methodTagMap = PAYMENT_METHODS.reduce((acc, item) => {
    acc[item.value] = item.tagType
    return acc
  }, {})
  return methodTagMap[method] || 'info'
}

const getStatusLabel = (status) => {
  const statusMap = PAYMENT_STATUS.reduce((acc, item) => {
    acc[item.value] = item.label
    return acc
  }, {})
  return statusMap[status] || status
}

const getStatusTagType = (status) => {
  const statusTagMap = PAYMENT_STATUS.reduce((acc, item) => {
    acc[item.value] = item.tagType
    return acc
  }, {})
  return statusTagMap[status] || 'info'
}

const getInstallmentStatusLabel = (status) => {
  const statusMap = INSTALLMENT_STATUS.reduce((acc, item) => {
    acc[item.value] = item.label
    return acc
  }, {})
  return statusMap[status] || status
}

const getInstallmentStatusType = (status) => {
  const statusTagMap = INSTALLMENT_STATUS.reduce((acc, item) => {
    acc[item.value] = item.tagType
    return acc
  }, {})
  return statusTagMap[status] || 'info'
}

const getLogType = (type) => {
  const typeMap = {
    'create': 'primary',
    'update': 'warning',
    'delete': 'danger',
    'confirm': 'success'
  }
  return typeMap[type] || 'info'
}

// 生命周期
onMounted(() => {
  loadPaymentDetail()
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

.status-flow-card {
  margin-bottom: 20px;
  
  .status-flow {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 0;
  }
  
  .status-item {
    display: flex;
    align-items: center;
    position: relative;
    
    &.active {
      .status-icon {
        background: #409eff;
        color: #fff;
      }
      
      .status-title {
        color: #409eff;
        font-weight: bold;
      }
    }
    
    &.completed {
      .status-icon {
        background: #67c23a;
        color: #fff;
      }
      
      .status-title {
        color: #67c23a;
      }
    }
    
    &:not(.active):not(.completed) {
      .status-icon {
        background: #f5f7fa;
        color: #909399;
      }
      
      .status-title {
        color: #909399;
      }
    }
  }
  
  .status-icon {
    width: 48px;
    height: 48px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 12px;
  }
  
  .status-content {
    flex: 1;
  }
  
  .status-title {
    font-size: 16px;
    margin-bottom: 4px;
  }
  
  .status-time {
    font-size: 12px;
    color: #909399;
  }
  
  .status-arrow {
    margin: 0 20px;
    color: #dcdfe6;
  }
}

.info-card {
  margin-bottom: 20px;
  
  .card-header {
    display: flex;
    align-items: center;
    font-weight: bold;
    
    .el-icon {
      margin-right: 8px;
    }
  }
  
  .info-value {
    font-weight: bold;
    color: #303133;
    
    &.amount {
      color: #f56c6c;
      font-size: 16px;
    }
  }
  
  .text-danger {
    color: #f56c6c;
  }
  
  .installment-table {
    margin-top: 20px;
    
    h4 {
      margin-bottom: 16px;
      color: #303133;
    }
  }
  
  .voucher-gallery {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
  }
  
  .voucher-item {
    width: 200px;
    height: 150px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    overflow: hidden;
    cursor: pointer;
    transition: all 0.3s;
    
    &:hover {
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    }
    
    img {
      width: 100%;
      height: 120px;
      object-fit: cover;
    }
    
    .voucher-name {
      padding: 8px;
      font-size: 12px;
      text-align: center;
      background: #f5f7fa;
      color: #606266;
    }
  }
  
  .remark-content {
    padding: 16px;
    background: #f5f7fa;
    border-radius: 4px;
    color: #606266;
    line-height: 1.6;
  }
  
  .log-operator {
    margin-top: 8px;
    font-size: 12px;
    color: #909399;
  }
}

// 深色主题适配
:deep(.dark) {
  .page-header {
    background: linear-gradient(135deg, #434343 0%, #000000 100%);
  }
  
  .status-flow-card {
    .status-item {
      &:not(.active):not(.completed) {
        .status-icon {
          background: #1f1f1f;
        }
      }
    }
  }
  
  .info-card {
    .info-value {
      color: #e4e7ed;
    }
    
    .remark-content {
      background: #1f1f1f;
    }
  }
}

// 打印样式
@media print {
  .page-header,
  .breadcrumb,
  .el-card__header {
    display: none;
  }
  
  .el-card {
    border: 1px solid #dcdfe6 !important;
    margin-bottom: 20px !important;
    break-inside: avoid;
  }
}
</style>