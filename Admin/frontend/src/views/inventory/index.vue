<template>
  <base-wrapper>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div>
          <h1 class="page-title">库存管理</h1>
          <p class="page-subtitle">多仓库库存管理、库存预警、批次管理</p>
        </div>
        <img src="@/assets/logo/inventory.png" alt="库存管理" class="header-image" />
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>库存管理</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 统计卡片 -->
    <div class="stats-container">
      <el-row :gutter="20">
        <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <el-icon><Box /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalProducts }}</div>
              <div class="stat-label">总商品数</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
          <div class="stat-card">
            <div class="stat-icon low-stock">
              <el-icon><Warning /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.lowStockItems }}</div>
              <div class="stat-label">低库存商品</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
          <div class="stat-card">
            <div class="stat-icon out-of-stock">
              <el-icon><CircleClose /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.outOfStockItems }}</div>
              <div class="stat-label">缺货商品</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
          <div class="stat-card">
            <div class="stat-icon expired">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.expiredItems }}</div>
              <div class="stat-label">即将过期</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索和筛选区域 -->
    <div class="filter-container">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.productName" placeholder="请输入商品名称" clearable />
        </el-form-item>
        <el-form-item label="仓库">
          <el-select v-model="searchForm.warehouseId" placeholder="请选择仓库" clearable>
            <el-option label="全部仓库" value="" />
            <el-option v-for="warehouse in warehouses" :key="warehouse.id" :label="warehouse.name" :value="warehouse.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="库存状态">
          <el-select v-model="searchForm.stockStatus" placeholder="请选择状态" clearable>
            <el-option label="全部状态" value="" />
            <el-option label="正常" value="normal" />
            <el-option label="低库存" value="low" />
            <el-option label="缺货" value="out" />
            <el-option label="过期" value="expired" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-container">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增库存
      </el-button>
      <el-button @click="handleBatchAdjust">
        <el-icon><Edit /></el-icon>
        批量调整
      </el-button>
      <el-button @click="handleExport">
        <el-icon><Download /></el-icon>
        导出数据
      </el-button>
      <el-button type="warning" @click="handleStockWarning">
        <el-icon><Warning /></el-icon>
        库存预警
      </el-button>
    </div>

    <!-- 库存列表 -->
    <div class="table-container">
      <el-table
        :data="inventoryList"
        v-loading="loading"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="productCode" label="商品编码" width="120" />
        <el-table-column prop="productName" label="商品名称" min-width="200" />
        <el-table-column prop="warehouseName" label="仓库" width="120" />
        <el-table-column prop="quantity" label="库存数量" width="100" align="center">
          <template #default="{ row }">
            <span :class="getStockStatusClass(row)">{{ row.quantity }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="availableQuantity" label="可用库存" width="100" align="center" />
        <el-table-column prop="lockedQuantity" label="锁定库存" width="100" align="center" />
        <el-table-column prop="minStock" label="最低库存" width="100" align="center" />
        <el-table-column prop="maxStock" label="最高库存" width="100" align="center" />
        <el-table-column prop="batchNumber" label="批次号" width="120" />
        <el-table-column prop="expireDate" label="有效期" width="120">
          <template #default="{ row }">
            <span :class="{ 'text-danger': isExpiringSoon(row.expireDate) }">
              {{ row.expireDate }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdateTime" label="更新时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">
              <el-icon><View /></el-icon>
              查看
            </el-button>
            <el-button type="primary" link @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="warning" link @click="handleAdjust(row)">
              <el-icon><Plus /></el-icon>
              调整
            </el-button>
            <el-dropdown @command="(cmd) => handleCommand(cmd, row)">
              <el-button type="primary" link>
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="transfer">
                    <el-icon><Switch /></el-icon>
                    库存调拨
                  </el-dropdown-item>
                  <el-dropdown-item command="check">
                    <el-icon><Check /></el-icon>
                    库存盘点
                  </el-dropdown-item>
                  <el-dropdown-item command="history">
                    <el-icon><Clock /></el-icon>
                    历史记录
                  </el-dropdown-item>
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

    <!-- 库存调整对话框 -->
    <el-dialog
      v-model="adjustDialog.visible"
      :title="adjustDialog.title"
      width="600px"
      @close="handleAdjustClose"
    >
      <el-form ref="adjustFormRef" :model="adjustForm" :rules="adjustRules" label-width="100px">
        <el-form-item label="商品名称">
          <el-input v-model="adjustForm.productName" disabled />
        </el-form-item>
        <el-form-item label="当前库存">
          <el-input v-model="adjustForm.currentQuantity" disabled />
        </el-form-item>
        <el-form-item label="调整类型" prop="adjustType">
          <el-radio-group v-model="adjustForm.adjustType">
            <el-radio label="increase">增加库存</el-radio>
            <el-radio label="decrease">减少库存</el-radio>
            <el-radio label="set">设置库存</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="调整数量" prop="adjustQuantity">
          <el-input-number v-model="adjustForm.adjustQuantity" :min="0" :precision="0" />
        </el-form-item>
        <el-form-item label="调整原因" prop="reason">
          <el-input v-model="adjustForm.reason" type="textarea" :rows="3" placeholder="请输入调整原因" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="adjustForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleAdjustClose">取消</el-button>
        <el-button type="primary" @click="handleAdjustSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 库存预警对话框 -->
    <el-dialog
      v-model="warningDialog.visible"
      title="库存预警设置"
      width="800px"
    >
      <el-table :data="warningItems" border stripe>
        <el-table-column prop="productCode" label="商品编码" width="120" />
        <el-table-column prop="productName" label="商品名称" min-width="200" />
        <el-table-column prop="currentStock" label="当前库存" width="100" align="center" />
        <el-table-column prop="minStock" label="最低库存" width="100" align="center" />
        <el-table-column prop="status" label="预警状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getWarningTagType(row.status)">{{ getWarningStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleSetWarning(row)">
              设置预警
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </base-wrapper>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useInventory } from '@/composables/useInventory'

const router = useRouter()
const { getInventoryList, getInventoryStats, adjustInventory, getWarehouses } = useInventory()

// 数据
const loading = ref(false)
const inventoryList = ref([])
const warehouses = ref([])
const selectedRows = ref([])
const stats = ref({
  totalProducts: 0,
  lowStockItems: 0,
  outOfStockItems: 0,
  expiredItems: 0
})

// 搜索表单
const searchForm = reactive({
  productName: '',
  warehouseId: '',
  stockStatus: ''
})

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 调整对话框
const adjustDialog = reactive({
  visible: false,
  title: '库存调整',
  isEdit: false
})

const adjustForm = reactive({
  id: '',
  productName: '',
  currentQuantity: 0,
  adjustType: 'increase',
  adjustQuantity: 0,
  reason: '',
  remark: ''
})

const adjustRules = {
  adjustType: [{ required: true, message: '请选择调整类型', trigger: 'change' }],
  adjustQuantity: [{ required: true, message: '请输入调整数量', trigger: 'blur' }],
  reason: [{ required: true, message: '请输入调整原因', trigger: 'blur' }]
}

// 预警对话框
const warningDialog = reactive({
  visible: false
})
const warningItems = ref([])

// 生命周期
onMounted(async () => {
  await loadData()
  await loadWarehouses()
})

// 方法
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.currentPage,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    
    const [inventoryRes, statsRes] = await Promise.all([
      getInventoryList(params),
      getInventoryStats()
    ])
    
    if (inventoryRes.success) {
      inventoryList.value = inventoryRes.data.items
      pagination.total = inventoryRes.data.total
    }
    
    if (statsRes.success) {
      stats.value = statsRes.data
    }
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const loadWarehouses = async () => {
  try {
    const res = await getWarehouses()
    if (res.success) {
      warehouses.value = res.data
    }
  } catch (error) {
    console.error('加载仓库数据失败', error)
  }
}

const handleSearch = () => {
  pagination.currentPage = 1
  loadData()
}

const handleReset = () => {
  searchForm.productName = ''
  searchForm.warehouseId = ''
  searchForm.stockStatus = ''
  handleSearch()
}

const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  loadData()
}

const handleCurrentChange = (page) => {
  pagination.currentPage = page
  loadData()
}

const handleAdd = () => {
  router.push('/inventory/add')
}

const handleView = (row) => {
  router.push(`/inventory/detail/${row.id}`)
}

const handleEdit = (row) => {
  router.push(`/inventory/edit/${row.id}`)
}

const handleAdjust = (row) => {
  adjustDialog.visible = true
  adjustDialog.title = '库存调整'
  adjustForm.id = row.id
  adjustForm.productName = row.productName
  adjustForm.currentQuantity = row.quantity
}

const handleAdjustClose = () => {
  adjustDialog.visible = false
  adjustForm.id = ''
  adjustForm.productName = ''
  adjustForm.currentQuantity = 0
  adjustForm.adjustType = 'increase'
  adjustForm.adjustQuantity = 0
  adjustForm.reason = ''
  adjustForm.remark = ''
}

const handleAdjustSubmit = async () => {
  try {
    const res = await adjustInventory(adjustForm.id, {
      adjustType: adjustForm.adjustType,
      quantity: adjustForm.adjustQuantity,
      reason: adjustForm.reason,
      remark: adjustForm.remark
    })
    
    if (res.success) {
      ElMessage.success('库存调整成功')
      handleAdjustClose()
      loadData()
    }
  } catch (error) {
    ElMessage.error('库存调整失败')
  }
}

const handleBatchAdjust = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要调整的库存记录')
    return
  }
  // 批量调整逻辑
  ElMessage.info('批量调整功能开发中')
}

const handleExport = () => {
  // 导出逻辑
  ElMessage.info('导出功能开发中')
}

const handleStockWarning = async () => {
  warningDialog.visible = true
  // 加载预警数据
  warningItems.value = inventoryList.value.filter(item => {
    return item.quantity <= item.minStock || isExpiringSoon(item.expireDate)
  })
}

const handleCommand = (command, row) => {
  switch (command) {
    case 'transfer':
      router.push(`/inventory/transfer/${row.id}`)
      break
    case 'check':
      router.push(`/inventory/check/${row.id}`)
      break
    case 'history':
      router.push(`/inventory/history/${row.id}`)
      break
  }
}

const getStockStatusClass = (row) => {
  if (row.quantity <= 0) return 'text-danger'
  if (row.quantity <= row.minStock) return 'text-warning'
  return 'text-success'
}

const isExpiringSoon = (expireDate) => {
  if (!expireDate) return false
  const today = new Date()
  const expire = new Date(expireDate)
  const diffTime = expire - today
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  return diffDays <= 30 // 30天内过期
}

const getWarningTagType = (status) => {
  switch (status) {
    case 'low': return 'warning'
    case 'out': return 'danger'
    case 'expired': return 'info'
    default: return 'info'
  }
}

const getWarningStatusText = (status) => {
  switch (status) {
    case 'low': return '低库存'
    case 'out': return '缺货'
    case 'expired': return '即将过期'
    default: return '正常'
  }
}

const handleSetWarning = (row) => {
  // 设置预警逻辑
  ElMessage.info('预警设置功能开发中')
}
</script>

<style lang="scss" scoped>
.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  height: 140px;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  
  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    
    .page-title {
      color: #fff;
      font-size: 24px;
      font-weight: bold;
      margin: 0;
    }
    
    .page-subtitle {
      color: rgba(255, 255, 255, 0.8);
      font-size: 14px;
      margin: 4px 0 0 0;
    }
    
    .header-image {
      height: 130px;
      opacity: 0.8;
    }
  }
}

.breadcrumb {
  margin: 20px;
}

.stats-container {
  padding: 0 20px;
  margin-bottom: 20px;
  
  .stat-card {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;
      
      &.total {
        background: #e6f7ff;
        color: #1890ff;
      }
      
      &.low-stock {
        background: #fff7e6;
        color: #fa8c16;
      }
      
      &.out-of-stock {
        background: #ffe6e6;
        color: #f5222d;
      }
      
      &.expired {
        background: #f6ffed;
        color: #52c41a;
      }
      
      .el-icon {
        font-size: 24px;
      }
    }
    
    .stat-content {
      .stat-value {
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
  padding: 0 20px;
  margin-bottom: 20px;
  
  .search-form {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }
}

.action-container {
  padding: 0 20px;
  margin-bottom: 20px;
  
  .el-button {
    margin-right: 10px;
  }
}

.table-container {
  padding: 0 20px;
  margin-bottom: 20px;
  
  .el-table {
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
    .text-success {
      color: #67c23a;
      font-weight: bold;
    }
    
    .text-warning {
      color: #e6a23c;
      font-weight: bold;
    }
    
    .text-danger {
      color: #f56c6c;
      font-weight: bold;
    }
  }
}

.pagination-container {
  padding: 0 20px;
  display: flex;
  justify-content: center;
}

@media (max-width: 768px) {
  .stats-container {
    .stat-card {
      padding: 15px;
      
      .stat-icon {
        width: 40px;
        height: 40px;
        margin-right: 10px;
        
        .el-icon {
          font-size: 18px;
        }
      }
      
      .stat-content {
        .stat-value {
          font-size: 18px;
        }
        
        .stat-label {
          font-size: 12px;
        }
      }
    }
  }
  
  .filter-container {
    .search-form {
      .el-form-item {
        margin-bottom: 10px;
        width: 100%;
        
        .el-form-item__content {
          width: 100%;
        }
      }
    }
  }
}

// 深色主题适配
@media (prefers-color-scheme: dark) {
  .stats-container {
    .stat-card {
      background: #1f1f1f;
      
      .stat-content {
        .stat-value {
          color: #e8e8e8;
        }
        
        .stat-label {
          color: #a0a0a0;
        }
      }
    }
  }
  
  .filter-container {
    .search-form {
      background: #1f1f1f;
    }
  }
  
  .table-container {
    .el-table {
      background: #1f1f1f;
    }
  }
}
</style>