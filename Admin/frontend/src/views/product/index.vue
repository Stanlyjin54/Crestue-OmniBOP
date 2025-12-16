<template>
  <base-wrapper>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">商品管理</h1>
          <p class="page-description">商品信息、规格、库存、价格管理</p>
        </div>
        <div class="header-right">
          <img src="@/assets/logo/document.png" height="120px" />
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="m-20">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 搜索和筛选区域 -->
      <el-card class="search-card">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="商品名称">
            <el-input v-model="searchForm.name" placeholder="请输入商品名称" clearable />
          </el-form-item>
          <el-form-item label="商品分类">
            <el-select v-model="searchForm.category" placeholder="请选择分类" clearable>
              <el-option
                v-for="category in categories"
                :key="category.id"
                :label="category.name"
                :value="category.code"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="品牌">
            <el-select v-model="searchForm.brand" placeholder="请选择品牌" clearable>
              <el-option
                v-for="brand in brands"
                :key="brand.id"
                :label="brand.name"
                :value="brand.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="库存状态">
            <el-select v-model="searchForm.stock_status" placeholder="请选择状态" clearable>
              <el-option label="正常" value="safe" />
              <el-option label="库存预警" value="warning" />
              <el-option label="缺货" value="danger" />
            </el-select>
          </el-form-item>
          <el-form-item label="商品状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
              <el-option
                v-for="status in Object.values(PRODUCT_STATUS)"
                :key="status.value"
                :label="status.label"
                :value="status.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 统计卡片 -->
      <el-row :gutter="20" class="stat-row">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-icon total">
                <el-icon><Goods /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-number">{{ getStockStatistics.total }}</div>
                <div class="stat-label">商品总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-icon normal">
                <el-icon><CircleCheck /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-number">{{ getStockStatistics.normal }}</div>
                <div class="stat-label">库存正常</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-icon warning">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-number">{{ getStockStatistics.low_stock }}</div>
                <div class="stat-label">库存预警</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-icon danger">
                <el-icon><CircleClose /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-number">{{ getStockStatistics.out_of_stock }}</div>
                <div class="stat-label">缺货商品</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 操作按钮 -->
      <el-card class="operation-card">
        <div class="operation-header">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增商品
          </el-button>
          <el-button @click="handleImport">
            <el-icon><Upload /></el-icon>
            批量导入
          </el-button>
          <el-button @click="handleExport">
            <el-icon><Download /></el-icon>
            导出数据
          </el-button>
        </div>
      </el-card>

      <!-- 商品列表 -->
      <el-card class="table-card">
        <el-table
          v-loading="loading"
          :data="filteredProducts"
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column label="商品图片" width="100">
            <template #default="{ row }">
              <el-image
                :src="row.images[0]"
                :preview-src-list="row.images"
                fit="cover"
                style="width: 60px; height: 60px; border-radius: 4px;"
              >
                <template #error>
                  <div class="image-slot">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="商品名称" min-width="200" show-overflow-tooltip />
          <el-table-column prop="barcode" label="条形码" width="150" />
          <el-table-column prop="category" label="分类" width="120" />
          <el-table-column prop="brand" label="品牌" width="120" />
          <el-table-column label="规格" width="200">
            <template #default="{ row }">
              <div v-for="spec in row.specifications" :key="spec.name" class="spec-item">
                <span class="spec-name">{{ spec.name }}:</span>
                <span class="spec-value">{{ spec.value }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="price" label="价格" width="120">
            <template #default="{ row }">
              <span class="price-text">¥{{ formatCurrency(row.price) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="库存" width="120">
            <template #default="{ row }">
              <div class="stock-info">
                <span class="stock-number">{{ row.stock }}</span>
                <el-tag
                  :type="getStockAlertStatus(row.stock, row.safety_stock).value"
                  size="small"
                  class="stock-tag"
                >
                  {{ getStockAlertStatus(row.stock, row.safety_stock).label }}
                </el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="PRODUCT_STATUS[row.status]?.type">
                {{ PRODUCT_STATUS[row.status]?.label }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
              <el-button type="warning" link @click="handleStock(row)">库存</el-button>
              <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

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
      </el-card>
    </div>

    <!-- 商品表单对话框 -->
    <ProductFormDialog
      v-model="dialogVisible"
      :product="currentProduct"
      :categories="categories"
      :brands="brands"
      @submit="handleSubmit"
    />
  </base-wrapper>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useProduct, PRODUCT_STATUS } from '@/composables/useProduct'
import { formatCurrency } from '@/utils/format'
import ProductFormDialog from './components/ProductFormDialog.vue'

// 组合式函数
const {
  loading,
  products,
  categories,
  brands,
  fetchProducts,
  fetchCategories,
  fetchBrands,
  createProduct,
  updateProduct,
  deleteProduct,
  getStockAlertStatus,
  getStockStatistics
} = useProduct()

// 搜索表单
const searchForm = reactive({
  name: '',
  category: '',
  brand: '',
  stock_status: '',
  status: ''
})

// 分页配置
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 对话框状态
const dialogVisible = ref(false)
const currentProduct = ref(null)
const selectedProducts = ref([])

// 筛选后的商品列表
const filteredProducts = computed(() => {
  let filtered = products.value

  if (searchForm.name) {
    filtered = filtered.filter(p => p.name.includes(searchForm.name))
  }
  if (searchForm.category) {
    filtered = filtered.filter(p => p.category === searchForm.category)
  }
  if (searchForm.brand) {
    filtered = filtered.filter(p => p.brand_id === searchForm.brand)
  }
  if (searchForm.stock_status) {
    filtered = filtered.filter(p => {
      const status = getStockAlertStatus(p.stock, p.safety_stock)
      return status.value === searchForm.stock_status
    })
  }
  if (searchForm.status) {
    filtered = filtered.filter(p => p.status === searchForm.status)
  }

  return filtered
})

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1
}

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    name: '',
    category: '',
    brand: '',
    stock_status: '',
    status: ''
  })
  handleSearch()
}

// 新增商品
const handleAdd = () => {
  currentProduct.value = null
  dialogVisible.value = true
}

// 编辑商品
const handleEdit = (row) => {
  currentProduct.value = { ...row }
  dialogVisible.value = true
}

// 库存管理
const handleStock = (row) => {
  ElMessage.info('库存管理功能开发中...')
}

// 删除商品
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除商品 "${row.name}" 吗？`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    deleteProduct(row.id)
  })
}

// 批量导入
const handleImport = () => {
  ElMessage.info('批量导入功能开发中...')
}

// 导出数据
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 表格选择
const handleSelectionChange = (selection) => {
  selectedProducts.value = selection
}

// 分页处理
const handleSizeChange = (size) => {
  pagination.pageSize = size
}

const handleCurrentChange = (page) => {
  pagination.currentPage = page
}

// 提交表单
const handleSubmit = async (formData) => {
  try {
    if (formData.id) {
      await updateProduct(formData.id, formData)
    } else {
      await createProduct(formData)
    }
    dialogVisible.value = false
    await fetchProducts()
  } catch (error) {
    console.error('提交失败:', error)
  }
}

// 初始化
onMounted(async () => {
  await Promise.all([
    fetchProducts(),
    fetchCategories(),
    fetchBrands()
  ])
})
</script>

<style lang="scss" scoped>
.page-header {
  background: linear-gradient(135deg, #5D5FEF 0%, #8B5CF6 100%);
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
      font-size: 24px;
      font-weight: bold;
      color: #FFFFFF;
      margin-bottom: 8px;
    }
    
    .page-description {
      font-size: 14px;
      color: #E0E0E0;
      margin: 0;
    }
  }
}

.main-content {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
  
  .search-form {
    .el-form-item {
      margin-bottom: 0;
    }
  }
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  .stat-item {
    display: flex;
    align-items: center;
    
    .stat-icon {
      width: 48px;
      height: 48px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      
      &.total {
        background: #E6F7FF;
        color: #1890FF;
      }
      
      &.normal {
        background: #F6FFED;
        color: #52C41A;
      }
      
      &.warning {
        background: #FFF7E6;
        color: #FAAD14;
      }
      
      &.danger {
        background: #FFF1F0;
        color: #FF4D4F;
      }
    }
    
    .stat-content {
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

.operation-card {
  margin-bottom: 20px;
  
  .operation-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.table-card {
  .spec-item {
    display: flex;
    align-items: center;
    margin-bottom: 4px;
    
    .spec-name {
      font-weight: 500;
      margin-right: 4px;
      color: #606266;
    }
    
    .spec-value {
      color: #303133;
    }
  }
  
  .price-text {
    font-weight: bold;
    color: #F56C6C;
  }
  
  .stock-info {
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .stock-number {
      font-weight: bold;
      margin-bottom: 4px;
    }
    
    .stock-tag {
      font-size: 12px;
    }
  }
  
  .image-slot {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    background: #F5F7FA;
    color: #909399;
    font-size: 20px;
  }
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

// 响应式设计
@media (max-width: 768px) {
  .search-form {
    .el-form-item {
      width: 100%;
      margin-right: 0;
    }
  }
  
  .stat-row {
    .el-col {
      margin-bottom: 10px;
    }
  }
  
  .operation-header {
    flex-wrap: wrap;
    gap: 10px;
  }
}
</style>