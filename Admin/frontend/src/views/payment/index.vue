<template>
  <base-wrapper>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">支付管理</h1>
          <p class="page-description">管理所有支付记录，支持账期、分期等多种支付方式</p>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="handleCreate">
            <el-icon><Plus /></el-icon>新建支付
          </el-button>
          <el-button @click="handleExport">
            <el-icon><Download /></el-icon>导出
          </el-button>
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>支付管理</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon success">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-title">今日收入</div>
              <div class="stats-value">¥{{ formatMoney(todayIncome) }}</div>
              <div class="stats-trend up">
                <el-icon><Top /></el-icon>+12.5%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon warning">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-title">待确认支付</div>
              <div class="stats-value">{{ pendingCount }}</div>
              <div class="stats-trend">
                <span style="color: #909399">笔</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon info">
              <el-icon><CreditCard /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-title">账期支付</div>
              <div class="stats-value">¥{{ formatMoney(creditAmount) }}</div>
              <div class="stats-trend down">
                <el-icon><Bottom /></el-icon>-3.2%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon danger">
              <el-icon><Warning /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-title">逾期金额</div>
              <div class="stats-value">¥{{ formatMoney(overdueAmount) }}</div>
              <div class="stats-trend up">
                <el-icon><Top /></el-icon>+8.7%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索筛选区域 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="支付编号">
          <el-input
            v-model="searchForm.paymentNo"
            placeholder="请输入支付编号"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="订单编号">
          <el-input
            v-model="searchForm.orderNo"
            placeholder="请输入订单编号"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="支付方式">
          <el-select v-model="searchForm.paymentMethod" placeholder="请选择支付方式" clearable>
            <el-option label="现金" value="cash" />
            <el-option label="银行转账" value="bank_transfer" />
            <el-option label="账期支付" value="credit" />
            <el-option label="分期付款" value="installment" />
            <el-option label="支付宝" value="alipay" />
            <el-option label="微信支付" value="wechat" />
          </el-select>
        </el-form-item>
        <el-form-item label="支付状态">
          <el-select v-model="searchForm.status" placeholder="请选择支付状态" clearable>
            <el-option label="待支付" value="pending" />
            <el-option label="已支付" value="paid" />
            <el-option label="部分支付" value="partial" />
            <el-option label="已退款" value="refunded" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
        </el-form-item>
        <el-form-item label="支付时间">
          <el-date-picker
            v-model="searchForm.paymentDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮区域 -->
    <div class="action-bar">
      <div class="action-left">
        <el-button type="danger" @click="handleBatchDelete" :disabled="selectedRows.length === 0">
          <el-icon><Delete /></el-icon>批量删除
        </el-button>
        <el-button @click="handleBatchConfirm" :disabled="selectedRows.length === 0">
          <el-icon><CircleCheck /></el-icon>批量确认
        </el-button>
      </div>
      <div class="action-right">
        <el-button @click="handleRefresh">
          <el-icon><Refresh /></el-icon>刷新
        </el-button>
      </div>
    </div>

    <!-- 支付列表 -->
    <el-card class="list-card">
      <el-table
        v-loading="loading"
        :data="paymentList"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="paymentNo" label="支付编号" min-width="180" />
        <el-table-column prop="orderNo" label="订单编号" min-width="180" />
        <el-table-column prop="customerName" label="客户名称" min-width="150" />
        <el-table-column prop="paymentMethod" label="支付方式" width="100">
          <template #default="{ row }">
            <el-tag :type="getPaymentMethodTagType(row.paymentMethod)">
              {{ getPaymentMethodLabel(row.paymentMethod) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="支付金额" width="120" align="right">
          <template #default="{ row }">
            ¥{{ formatMoney(row.amount) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="支付状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="paymentDate" label="支付时间" width="160" />
        <el-table-column prop="dueDate" label="到期时间" width="120">
          <template #default="{ row }">
            <span :class="{ 'text-danger': isOverdue(row.dueDate) }">
              {{ row.dueDate }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="installmentInfo" label="分期信息" width="120">
          <template #default="{ row }">
            <div v-if="row.installmentInfo">
              <div>{{ row.installmentInfo.current }}/{{ row.installmentInfo.total }}</div>
              <div class="text-muted">¥{{ formatMoney(row.installmentInfo.amount) }}</div>
            </div>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">
              <el-icon><View /></el-icon>查看
            </el-button>
            <el-button 
              type="warning" 
              link 
              @click="handleEdit(row)"
              v-if="row.status === 'pending'"
            >
              <el-icon><Edit /></el-icon>编辑
            </el-button>
            <el-button 
              type="success" 
              link 
              @click="handleConfirm(row)"
              v-if="row.status === 'pending'"
            >
              <el-icon><CircleCheck /></el-icon>确认
            </el-button>
            <el-button 
              type="danger" 
              link 
              @click="handleRefund(row)"
              v-if="row.status === 'paid'"
            >
              <el-icon><RefreshLeft /></el-icon>退款
            </el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, row)">
              <el-button type="info" link>
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="print">
                    <el-icon><Printer /></el-icon>打印凭证
                  </el-dropdown-item>
                  <el-dropdown-item command="export">
                    <el-icon><Download /></el-icon>导出详情
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
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 支付表单对话框 -->
    <PaymentFormDialog
      v-model="formDialogVisible"
      :payment-data="currentPayment"
      :mode="formMode"
      @success="handleFormSuccess"
    />

    <!-- 退款对话框 -->
    <el-dialog
      v-model="refundDialogVisible"
      title="退款处理"
      width="500px"
    >
      <el-form :model="refundForm" label-width="100px">
        <el-form-item label="退款金额">
          <el-input-number
            v-model="refundForm.amount"
            :min="0"
            :max="refundForm.maxAmount"
            :precision="2"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="退款原因">
          <el-select v-model="refundForm.reason" placeholder="请选择退款原因" style="width: 100%">
            <el-option label="商品质量问题" value="quality_issue" />
            <el-option label="订单取消" value="order_cancelled" />
            <el-option label="重复支付" value="duplicate_payment" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注说明">
          <el-input
            v-model="refundForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入退款备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="refundDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRefundSubmit" :loading="loading">确定</el-button>
      </template>
    </el-dialog>
  </base-wrapper>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, Download, Search, Refresh, Delete, CircleCheck, 
  View, Edit, RefreshLeft, MoreFilled, Printer, Top, Bottom,
  Money, Clock, CreditCard, Warning
} from '@element-plus/icons-vue'
import { usePayment } from '@/composables/usePayment'
import PaymentFormDialog from './components/PaymentFormDialog.vue'

const router = useRouter()
const { 
  getPayments, 
  deletePayment, 
  confirmPayment, 
  refundPayment,
  getPaymentStats 
} = usePayment()

// 数据状态
const loading = ref(false)
const paymentList = ref([])
const selectedRows = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 统计信息
const todayIncome = ref(0)
const pendingCount = ref(0)
const creditAmount = ref(0)
const overdueAmount = ref(0)

// 搜索表单
const searchForm = reactive({
  paymentNo: '',
  orderNo: '',
  paymentMethod: '',
  status: '',
  paymentDateRange: null
})

// 表单对话框
const formDialogVisible = ref(false)
const formMode = ref('create')
const currentPayment = ref({})

// 退款对话框
const refundDialogVisible = ref(false)
const refundForm = reactive({
  paymentId: '',
  amount: 0,
  maxAmount: 0,
  reason: '',
  remark: ''
})

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

// 方法
const loadPayments = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      ...searchForm
    }
    
    if (searchForm.paymentDateRange && searchForm.paymentDateRange.length === 2) {
      params.startDate = searchForm.paymentDateRange[0]
      params.endDate = searchForm.paymentDateRange[1]
    }
    
    const res = await getPayments(params)
    paymentList.value = res.data.items
    total.value = res.data.total
  } catch (error) {
    ElMessage.error('加载支付列表失败')
  } finally {
    loading.value = false
  }
}

const loadStats = async () => {
  try {
    const res = await getPaymentStats()
    const stats = res.data
    todayIncome.value = stats.todayIncome
    pendingCount.value = stats.pendingCount
    creditAmount.value = stats.creditAmount
    overdueAmount.value = stats.overdueAmount
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadPayments()
}

const handleReset = () => {
  searchForm.paymentNo = ''
  searchForm.orderNo = ''
  searchForm.paymentMethod = ''
  searchForm.status = ''
  searchForm.paymentDateRange = null
  handleSearch()
}

const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

const handleCreate = () => {
  currentPayment.value = {}
  formMode.value = 'create'
  formDialogVisible.value = true
}

const handleView = (row) => {
  router.push(`/payment/detail/${row.id}`)
}

const handleEdit = (row) => {
  currentPayment.value = { ...row }
  formMode.value = 'edit'
  formDialogVisible.value = true
}

const handleConfirm = async (row) => {
  await ElMessageBox.confirm('确定要确认该支付吗？', '确认支付', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  loading.value = true
  try {
    await confirmPayment(row.id)
    ElMessage.success('支付确认成功')
    loadPayments()
    loadStats()
  } catch (error) {
    ElMessage.error('支付确认失败')
  } finally {
    loading.value = false
  }
}

const handleRefund = (row) => {
  refundForm.paymentId = row.id
  refundForm.maxAmount = row.amount
  refundForm.amount = row.amount
  refundDialogVisible.value = true
}

const handleRefundSubmit = async () => {
  if (!refundForm.reason) {
    ElMessage.warning('请选择退款原因')
    return
  }
  
  loading.value = true
  try {
    await refundPayment(refundForm.paymentId, refundForm)
    ElMessage.success('退款处理成功')
    refundDialogVisible.value = false
    loadPayments()
    loadStats()
  } catch (error) {
    ElMessage.error('退款处理失败')
  } finally {
    loading.value = false
  }
}

const handleMoreAction = async (command, row) => {
  switch (command) {
    case 'print':
      ElMessage.info('打印功能开发中')
      break
    case 'export':
      ElMessage.info('导出功能开发中')
      break
    case 'log':
      ElMessage.info('日志功能开发中')
      break
    case 'delete':
      await handleDelete(row)
      break
  }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定要删除该支付记录吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  loading.value = true
  try {
    await deletePayment(row.id)
    ElMessage.success('删除成功')
    loadPayments()
    loadStats()
  } catch (error) {
    ElMessage.error('删除失败')
  } finally {
    loading.value = false
  }
}

const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) return
  
  await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 条支付记录吗？`, '批量删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  loading.value = true
  try {
    for (const row of selectedRows.value) {
      await deletePayment(row.id)
    }
    ElMessage.success('批量删除成功')
    loadPayments()
    loadStats()
  } catch (error) {
    ElMessage.error('批量删除失败')
  } finally {
    loading.value = false
  }
}

const handleBatchConfirm = async () => {
  if (selectedRows.value.length === 0) return
  
  await ElMessageBox.confirm(`确定要确认选中的 ${selectedRows.value.length} 条支付记录吗？`, '批量确认确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  loading.value = true
  try {
    for (const row of selectedRows.value) {
      if (row.status === 'pending') {
        await confirmPayment(row.id)
      }
    }
    ElMessage.success('批量确认成功')
    loadPayments()
    loadStats()
  } catch (error) {
    ElMessage.error('批量确认失败')
  } finally {
    loading.value = false
  }
}

const handleRefresh = () => {
  loadPayments()
  loadStats()
}

const handleExport = () => {
  ElMessage.info('导出功能开发中')
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadPayments()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadPayments()
}

const handleFormSuccess = () => {
  formDialogVisible.value = false
  loadPayments()
  loadStats()
}

// 工具函数
const formatMoney = (amount) => {
  return Number(amount).toFixed(2)
}

const isOverdue = (dueDate) => {
  if (!dueDate) return false
  return new Date(dueDate) < new Date()
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

// 生命周期
onMounted(() => {
  loadPayments()
  loadStats()
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

.stats-row {
  margin-bottom: 20px;
}

.stats-card {
  .stats-content {
    display: flex;
    align-items: center;
  }
  
  .stats-icon {
    width: 48px;
    height: 48px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16px;
    
    &.success {
      background: #f0f9ff;
      color: #67c23a;
    }
    
    &.warning {
      background: #fffbe6;
      color: #e6a23c;
    }
    
    &.info {
      background: #f6f7f8;
      color: #909399;
    }
    
    &.danger {
      background: #fff1f0;
      color: #f56c6c;
    }
    
    .el-icon {
      font-size: 24px;
    }
  }
  
  .stats-info {
    flex: 1;
  }
  
  .stats-title {
    font-size: 14px;
    color: #909399;
    margin-bottom: 8px;
  }
  
  .stats-value {
    font-size: 24px;
    font-weight: bold;
    color: #303133;
    margin-bottom: 4px;
  }
  
  .stats-trend {
    font-size: 12px;
    
    &.up {
      color: #67c23a;
    }
    
    &.down {
      color: #f56c6c;
    }
  }
}

.search-card {
  margin-bottom: 20px;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.list-card {
  .text-danger {
    color: #f56c6c;
  }
  
  .text-muted {
    color: #909399;
    font-size: 12px;
  }
}

.pagination-container {
  margin-top: 20px;
  text-align: center;
}

// 深色主题适配
:deep(.dark) {
  .page-header {
    background: linear-gradient(135deg, #434343 0%, #000000 100%);
  }
  
  .stats-card {
    .stats-icon {
      &.success {
        background: #1d2b1d;
      }
      
      &.warning {
        background: #2b1d1d;
      }
      
      &.info {
        background: #1d1d2b;
      }
      
      &.danger {
        background: #2b1d1d;
      }
    }
    
    .stats-title {
      color: #cfd3dc;
    }
    
    .stats-value {
      color: #e4e7ed;
    }
  }
}
</style>