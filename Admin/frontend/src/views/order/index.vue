<template>
  <base-wrapper>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">订单管理</h1>
          <p class="page-description">管理B2B订单流程,支持批量处理、状态跟踪和财务对账</p>
        </div>
        <div class="header-right">
          <img src="@/assets/logo/document.png" height="120px" />
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>订单管理</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 统计卡片 -->
    <div class="stats-container">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ orderStats.pending }}</div>
              <div class="stat-label">待处理订单</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon processing">
              <el-icon><Loading /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ orderStats.processing }}</div>
              <div class="stat-label">处理中订单</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon completed">
              <el-icon><CircleCheck /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ orderStats.completed }}</div>
              <div class="stat-label">已完成订单</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon amount">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">¥{{ formatMoney(orderStats.totalAmount) }}</div>
              <div class="stat-label">今日订单总额</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索和筛选区域 -->
    <div class="filter-container">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="订单编号">
          <el-input
            v-model="filterForm.orderNo"
            placeholder="请输入订单编号"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input
            v-model="filterForm.customerName"
            placeholder="请输入客户名称"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select
            v-model="filterForm.status"
            placeholder="请选择订单状态"
            clearable
            @change="handleSearch"
          >
            <el-option
              v-for="status in ORDER_STATUS"
              :key="status.value"
              :label="status.label"
              :value="status.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="支付状态">
          <el-select
            v-model="filterForm.paymentStatus"
            placeholder="请选择支付状态"
            clearable
            @change="handleSearch"
          >
            <el-option label="未支付" value="unpaid" />
            <el-option label="部分支付" value="partial" />
            <el-option label="已支付" value="paid" />
            <el-option label="已退款" value="refunded" />
          </el-select>
        </el-form-item>
        <el-form-item label="下单时间">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            @change="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>重置
          </el-button>
          <el-button type="success" @click="handleExport">
            <el-icon><Download /></el-icon>导出
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-container">
      <el-button type="primary" @click="handleCreate">
        <el-icon><Plus /></el-icon>新建订单
      </el-button>
      <el-button type="warning" @click="handleBatchProcess" :disabled="selectedOrders.length === 0">
        <el-icon><Operation /></el-icon>批量处理
      </el-button>
      <el-button type="info" @click="handleBulkUpdateStatus" :disabled="selectedOrders.length === 0">
        <el-icon><Edit /></el-icon>批量更新状态
      </el-button>
      <el-button type="danger" @click="handleBulkDelete" :disabled="selectedOrders.length === 0">
        <el-icon><Delete /></el-icon>批量删除
      </el-button>
    </div>

    <!-- 订单列表 -->
    <div class="table-container">
      <el-table
        :data="orderList"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="orderNo" label="订单编号" width="180" fixed="left">
          <template #default="{ row }">
            <el-link type="primary" @click="handleViewDetail(row)">{{ row.orderNo }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="customerName" label="客户名称" width="150" />
        <el-table-column prop="orderType" label="订单类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getOrderTypeTag(row.orderType)">
              {{ getOrderTypeLabel(row.orderType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="paymentStatus" label="支付状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getPaymentStatusTag(row.paymentStatus)">
              {{ getPaymentStatusLabel(row.paymentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="订单金额" width="120" align="right">
          <template #default="{ row }">
            ¥{{ formatMoney(row.totalAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="paidAmount" label="已付金额" width="120" align="right">
          <template #default="{ row }">
            ¥{{ formatMoney(row.paidAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="orderDate" label="下单时间" width="160" />
        <el-table-column prop="deliveryDate" label="发货时间" width="160" />
        <el-table-column prop="salesRep" label="销售代表" width="120" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleViewDetail(row)">查看</el-button>
            <el-button type="warning" link @click="handleEdit(row)">编辑</el-button>
            <el-button 
              type="success" 
              link 
              @click="handleUpdateStatus(row)"
              v-if="row.status !== 'completed' && row.status !== 'cancelled'"
            >
              更新状态
            </el-button>
            <el-dropdown trigger="click" @command="(cmd) => handleCommand(cmd, row)">
              <el-button type="info" link>
                更多<el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="print">打印订单</el-dropdown-item>
                  <el-dropdown-item command="duplicate">复制订单</el-dropdown-item>
                  <el-dropdown-item command="refund" v-if="row.paymentStatus === 'paid'">申请退款</el-dropdown-item>
                  <el-dropdown-item command="cancel" divided v-if="row.status === 'pending'">取消订单</el-dropdown-item>
                  <el-dropdown-item command="delete" style="color: #f56c6c">删除订单</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 订单表单对话框 -->
    <OrderFormDialog
      v-model="dialogVisible"
      :order="currentOrder"
      :mode="dialogMode"
      @success="handleFormSuccess"
    />

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
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Search, Refresh, Plus, Download, Operation, Edit, Delete, 
  Clock, Loading, CircleCheck, Money, ArrowDown 
} from '@element-plus/icons-vue'
import { useOrder } from '@/composables/useOrder'
import OrderFormDialog from './components/OrderFormDialog.vue'

const router = useRouter()
const { 
  getOrderList, createOrder, updateOrder, deleteOrder, 
  getOrderStats, exportOrders, bulkUpdateStatus 
} = useOrder()

// 数据状态
const loading = ref(false)
const orderList = ref([])
const selectedOrders = ref([])
const orderStats = ref({
  pending: 0,
  processing: 0,
  completed: 0,
  totalAmount: 0
})

// 筛选表单
const filterForm = reactive({
  orderNo: '',
  customerName: '',
  status: '',
  paymentStatus: '',
  dateRange: null
})

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 对话框状态
const dialogVisible = ref(false)
const dialogMode = ref('create')
const currentOrder = ref(null)
const statusDialogVisible = ref(false)
const statusForm = reactive({
  status: '',
  remark: ''
})

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

// 计算属性
const availableStatuses = computed(() => {
  if (!currentOrder.value) return []
  const currentStatus = currentOrder.value.status
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
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.currentPage,
      pageSize: pagination.pageSize,
      ...filterForm,
      startDate: filterForm.dateRange?.[0],
      endDate: filterForm.dateRange?.[1]
    }
    const res = await getOrderList(params)
    orderList.value = res.data.list
    pagination.total = res.data.total
    
    // 加载统计数据
    const stats = await getOrderStats()
    orderStats.value = stats.data
  } catch (error) {
    ElMessage.error('加载订单数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.currentPage = 1
  loadData()
}

const handleReset = () => {
  Object.assign(filterForm, {
    orderNo: '',
    customerName: '',
    status: '',
    paymentStatus: '',
    dateRange: null
  })
  handleSearch()
}

const handleExport = async () => {
  try {
    await exportOrders(filterForm)
    ElMessage.success('订单导出成功')
  } catch (error) {
    ElMessage.error('订单导出失败')
  }
}

const handleCreate = () => {
  dialogMode.value = 'create'
  currentOrder.value = null
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogMode.value = 'edit'
  currentOrder.value = { ...row }
  dialogVisible.value = true
}

const handleViewDetail = (row) => {
  router.push(`/order/detail/${row.id}`)
}

const handleUpdateStatus = (row) => {
  currentOrder.value = row
  statusForm.status = ''
  statusForm.remark = ''
  statusDialogVisible.value = true
}

const handleStatusUpdate = async () => {
  if (!statusForm.status) {
    ElMessage.warning('请选择新状态')
    return
  }
  
  try {
    await updateOrder(currentOrder.value.id, {
      status: statusForm.status,
      remark: statusForm.remark
    })
    ElMessage.success('状态更新成功')
    statusDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('状态更新失败')
  }
}

const handleBatchProcess = () => {
  ElMessage.info('批量处理功能开发中')
}

const handleBulkUpdateStatus = () => {
  if (selectedOrders.value.length === 0) {
    ElMessage.warning('请选择要更新的订单')
    return
  }
  ElMessage.info('批量更新状态功能开发中')
}

const handleBulkDelete = async () => {
  if (selectedOrders.value.length === 0) {
    ElMessage.warning('请选择要删除的订单')
    return
  }
  
  await ElMessageBox.confirm(
    `确定要删除选中的 ${selectedOrders.value.length} 个订单吗？`,
    '批量删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
  
  try {
    const ids = selectedOrders.value.map(order => order.id)
    await Promise.all(ids.map(id => deleteOrder(id)))
    ElMessage.success('批量删除成功')
    loadData()
  } catch (error) {
    ElMessage.error('批量删除失败')
  }
}

const handleSelectionChange = (selection) => {
  selectedOrders.value = selection
}

const handleCommand = async (command, row) => {
  switch (command) {
    case 'print':
      ElMessage.info('打印功能开发中')
      break
    case 'duplicate':
      ElMessage.info('复制功能开发中')
      break
    case 'refund':
      ElMessage.info('退款功能开发中')
      break
    case 'cancel':
      await handleCancel(row)
      break
    case 'delete':
      await handleDelete(row)
      break
  }
}

const handleCancel = async (row) => {
  await ElMessageBox.confirm('确定要取消该订单吗？', '取消确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  try {
    await updateOrder(row.id, { status: 'cancelled' })
    ElMessage.success('订单取消成功')
    loadData()
  } catch (error) {
    ElMessage.error('订单取消失败')
  }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定要删除该订单吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  try {
    await deleteOrder(row.id)
    ElMessage.success('订单删除成功')
    loadData()
  } catch (error) {
    ElMessage.error('订单删除失败')
  }
}

const handleFormSuccess = () => {
  dialogVisible.value = false
  loadData()
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
  loadData()
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
  loadData()
}

// 工具函数
const formatMoney = (amount) => {
  return Number(amount).toFixed(2)
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

const getStatusTagType = (status) => {
  const tagTypes = {
    pending: 'info',
    confirmed: 'primary',
    processing: 'warning',
    shipped: 'warning',
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

// 生命周期
onMounted(() => {
  loadData()
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

.stats-container {
  margin-bottom: 20px;
  
  .stat-card {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
    
    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      
      &.pending {
        background: #e6f7ff;
        color: #1890ff;
      }
      
      &.processing {
        background: #fff7e6;
        color: #fa8c16;
      }
      
      &.completed {
        background: #f6ffed;
        color: #52c41a;
      }
      
      &.amount {
        background: #f9f0ff;
        color: #722ed1;
      }
      
      .el-icon {
        font-size: 24px;
      }
    }
    
    .stat-content {
      flex: 1;
      
      .stat-number {
        font-size: 24px;
        font-weight: bold;
        color: #303133;
        margin-bottom: 4px;
      }
      
      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.filter-container {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.action-container {
  margin-bottom: 20px;
}

.table-container {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

// 响应式设计
@media (max-width: 768px) {
  .stats-container {
    .el-col {
      margin-bottom: 10px;
    }
  }
  
  .filter-form {
    .el-form-item {
      margin-right: 10px;
      margin-bottom: 10px;
    }
  }
}

// 深色主题适配
:deep(.dark) {
  .stat-card {
    background: #1f1f1f;
    
    .stat-number {
      color: #e4e7ed;
    }
  }
  
  .filter-container,
  .table-container {
    background: #1f1f1f;
  }
}
</style>