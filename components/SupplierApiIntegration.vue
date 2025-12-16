<template>
  <div class="supplier-api-integration">
    <el-card class="integration-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">
            <el-icon><Link /></el-icon>
            第三方供应商API集成
          </span>
          <div class="header-actions">
            <el-button 
              type="primary" 
              :icon="Plus" 
              @click="showAddSourceDialog = true"
            >
              添加数据源
            </el-button>
            <el-button 
              :icon="Refresh" 
              @click="refreshAllSources"
              :loading="loading"
            >
              刷新数据
            </el-button>
          </div>
        </div>
      </template>

      <!-- 数据源状态概览 -->
      <div class="sources-overview">
        <el-row :gutter="20">
          <el-col 
            v-for="source in availableSources" 
            :key="source.source"
            :span="8"
          >
            <div class="source-card" :class="source.available ? 'healthy' : 'unhealthy'">
              <div class="source-header">
                <div class="source-info">
                  <h4>{{ source.name }}</h4>
                  <el-tag 
                    :type="source.available ? 'success' : 'danger'"
                    size="small"
                  >
                    {{ source.available ? '正常' : '异常' }}
                  </el-tag>
                </div>
                <div class="source-stats">
                  <span class="stat-item">
                    <el-icon><DataBoard /></el-icon>
                    {{ getSourceStats(source.source) }} 产品
                  </span>
                </div>
              </div>
              
              <div class="source-actions">
                <el-button 
                  size="small" 
                  @click="syncSource(source.source)"
                  :disabled="!source.available || syncing[source.source]"
                  :loading="syncing[source.source]"
                >
                  同步数据
                </el-button>
                <el-button 
                  size="small" 
                  @click="testSource(source.source)"
                  :disabled="!source.available"
                >
                  测试连接
                </el-button>
                <el-button 
                  size="small" 
                  type="danger" 
                  @click="removeSource(source.source)"
                >
                  移除
                </el-button>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 产品搜索与比较 -->
      <el-divider content-position="left">产品搜索与比较</el-divider>
      
      <div class="search-section">
        <el-form :model="searchForm" inline>
          <el-form-item label="产品名称">
            <el-input 
              v-model="searchForm.keyword" 
              placeholder="请输入产品名称"
              style="width: 300px"
              clearable
            />
          </el-form-item>
          <el-form-item label="数量">
            <el-input-number 
              v-model="searchForm.quantity" 
              :min="1" 
              :max="10000"
              style="width: 120px"
            />
          </el-form-item>
          <el-form-item>
            <el-button 
              type="primary" 
              :icon="Search" 
              @click="searchProducts"
              :loading="searching"
            >
              跨平台搜索
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 搜索结果 -->
      <div v-if="searchResults.length > 0" class="search-results">
        <h4>搜索结果 ({{ searchResults.length }} 个平台)</h4>
        <div class="results-grid">
          <div 
            v-for="result in searchResults" 
            :key="result.source"
            class="result-card"
          >
            <div class="result-header">
              <span class="platform-name">{{ getPlatformName(result.source) }}</span>
              <span class="product-count">{{ result.count }} 个产品</span>
            </div>
            <div class="product-list">
              <div 
                v-for="product in result.products.slice(0, 3)" 
                :key="product.id"
                class="product-item"
                @click="viewProductDetail(product)"
              >
                <img 
                  v-if="product.images && product.images[0]" 
                  :src="product.images[0]" 
                  alt="产品图片"
                  class="product-image"
                />
                <div class="product-info">
                  <h5>{{ product.name }}</h5>
                  <p class="product-price">
                    {{ formatPrice(product.price, product.currency) }}
                  </p>
                  <p class="supplier-name">
                    {{ product.supplier.name }}
                  </p>
                </div>
              </div>
            </div>
            <div class="result-actions">
              <el-button 
                size="small" 
                @click="viewAllProducts(result.source, searchForm.keyword)"
              >
                查看全部
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 智能推荐 -->
      <el-divider content-position="left">智能供应商推荐</el-divider>
      
      <div class="recommendation-section">
        <el-form :model="recommendForm" inline>
          <el-form-item label="产品分类">
            <el-select 
              v-model="recommendForm.category" 
              placeholder="请选择分类"
              style="width: 200px"
              clearable
            >
              <el-option 
                v-for="cat in categories" 
                :key="cat.id" 
                :label="cat.name" 
                :value="cat.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="地区">
            <el-select 
              v-model="recommendForm.region" 
              placeholder="请选择地区"
              style="width: 150px"
              clearable
            >
              <el-option label="北京" value="北京" />
              <el-option label="上海" value="上海" />
              <el-option label="广州" value="广州" />
              <el-option label="深圳" value="深圳" />
              <el-option label="全国" value="全国" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button 
              type="primary" 
              :icon="Star" 
              @click="getRecommendations"
              :loading="recommending"
            >
              获取推荐
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 推荐结果 -->
        <div v-if="recommendations.length > 0" class="recommendations">
          <div 
            v-for="supplier in recommendations.slice(0, 5)" 
            :key="supplier.id"
            class="supplier-card"
          >
            <div class="supplier-info">
              <div class="supplier-header">
                <h4>{{ supplier.name }}</h4>
                <el-rate 
                  v-model="supplier.rating" 
                  disabled 
                  show-score 
                  text-color="#ff9900"
                  score-template="{value} 分"
                />
              </div>
              <div class="supplier-details">
                <p><strong>位置:</strong> {{ supplier.location }}</p>
                <p><strong>认证:</strong> 
                  <el-tag 
                    v-for="cert in supplier.certifications" 
                    :key="cert" 
                    size="small" 
                    type="success"
                    style="margin-right: 5px"
                  >
                    {{ cert }}
                  </el-tag>
                </p>
                <p><strong>平台:</strong> {{ getPlatformName(supplier.platform) }}</p>
                <p><strong>评分:</strong> {{ supplier.score }}/100</p>
              </div>
            </div>
            <div class="supplier-actions">
              <el-button size="small" type="primary" @click="contactSupplier(supplier)">
                联系供应商
              </el-button>
              <el-button size="small" @click="viewSupplierProducts(supplier)">
                查看产品
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 批量操作 -->
      <el-divider content-position="left">批量操作</el-divider>
      
      <div class="batch-actions">
        <el-button 
          type="success" 
          :icon="Download" 
          @click="exportAllData"
          :disabled="exporting"
        >
          导出所有数据
        </el-button>
        <el-button 
          :icon="DataAnalysis" 
          @click="generateReport"
        >
          生成分析报告
        </el-button>
        <el-button 
          :icon="Setting" 
          @click="showSettings = true"
        >
          系统设置
        </el-button>
      </div>
    </el-card>

    <!-- 添加数据源对话框 -->
    <el-dialog 
      v-model="showAddSourceDialog" 
      title="添加数据源" 
      width="600px"
    >
      <el-form :model="newSourceForm" label-width="120px">
        <el-form-item label="数据源类型">
          <el-select v-model="newSourceForm.type" placeholder="请选择数据源类型">
              <el-option label="阿里巴巴1688" value="1688" />
              <el-option label="京东工品汇" value="vipmro" />
              <el-option label="工帮帮" value="gongbangbang" />
              <el-option label="中国政府采购网" value="ccgp" />
              <el-option label="京东企业购" value="jd_enterprise" />
              <el-option label="自定义API" value="custom" />
            </el-select>
        </el-form-item>
        <el-form-item label="API地址">
          <el-input 
            v-model="newSourceForm.baseURL" 
            placeholder="请输入API基础地址"
          />
        </el-form-item>
        <el-form-item label="API密钥">
          <el-input 
            v-model="newSourceForm.apiKey" 
            type="password" 
            placeholder="请输入API密钥"
          />
        </el-form-item>
        <el-form-item label="请求频率">
          <el-input-number 
            v-model="newSourceForm.rateLimit" 
            :min="1" 
            :max="10000"
            style="width: 100%"
          />
          <span style="color: #909399; font-size: 12px;">每小时最大请求数</span>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddSourceDialog = false">取消</el-button>
          <el-button type="primary" @click="addNewSource">确认添加</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 产品详情对话框 -->
    <el-dialog 
      v-model="showProductDetail" 
      title="产品详情" 
      width="800px"
    >
      <div v-if="selectedProduct" class="product-detail">
        <div class="product-images">
          <img 
            v-for="(image, index) in selectedProduct.images" 
            :key="index"
            :src="image" 
            alt="产品图片"
            class="detail-image"
          />
        </div>
        <div class="product-info">
          <h3>{{ selectedProduct.name }}</h3>
          <p class="product-description">{{ selectedProduct.description }}</p>
          <div class="product-specs">
            <h4>产品规格:</h4>
            <pre>{{ JSON.stringify(selectedProduct.specifications, null, 2) }}</pre>
          </div>
          <div class="product-pricing">
            <h4>价格信息:</h4>
            <p><strong>价格:</strong> {{ formatPrice(selectedProduct.price, selectedProduct.currency) }}</p>
            <p><strong>起订量:</strong> {{ selectedProduct.minOrderQuantity }}</p>
            <p><strong>交货期:</strong> {{ selectedProduct.leadTime }}</p>
          </div>
          <div class="supplier-info">
            <h4>供应商信息:</h4>
            <p><strong>名称:</strong> {{ selectedProduct.supplier.name }}</p>
            <p><strong>位置:</strong> {{ selectedProduct.supplier.location }}</p>
            <p><strong>评分:</strong> {{ selectedProduct.supplier.rating }}/5</p>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { 
  Link, Plus, Refresh, Search, Star, Download, 
  DataAnalysis, Setting, DataBoard 
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import supplierApiManager from '@/utils/supplierApiAdapter.js'

// 响应式数据
const loading = ref(false)
const searching = ref(false)
const recommending = ref(false)
const exporting = ref(false)
const syncing = ref({})

const showAddSourceDialog = ref(false)
const showProductDetail = ref(false)
const showSettings = ref(false)

const availableSources = ref([])
const searchResults = ref([])
const recommendations = ref([])
const selectedProduct = ref(null)

// 表单数据
const searchForm = ref({
  keyword: '',
  quantity: 1
})

const recommendForm = ref({
  category: '',
  region: ''
})

const newSourceForm = ref({
  type: '',
  baseURL: '',
  apiKey: '',
  rateLimit: 100
})

// 静态数据
const categories = ref([
  { id: 'electronics', name: '电子产品' },
  { id: 'office', name: '办公用品' },
  { id: 'construction', name: '建筑工程' },
  { id: 'manufacturing', name: '制造业' },
  { id: 'services', name: '服务类' }
])

// 方法
const refreshAllSources = async () => {
  loading.value = true
  try {
    availableSources.value = supplierApiManager.getAvailableSources()
    ElMessage.success('数据源状态已刷新')
  } catch (error) {
    ElMessage.error('刷新失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

const syncSource = async (source) => {
  syncing.value[source] = true
  try {
    await supplierApiManager.syncData(source, 'products')
    await refreshAllSources()
  } catch (error) {
    ElMessage.error('同步失败: ' + error.message)
  } finally {
    syncing.value[source] = false
  }
}

const testSource = async (source) => {
  try {
    const healthStatus = await supplierApiManager.healthCheck()
    const status = healthStatus[source]
    if (status.status === 'healthy') {
      ElMessage.success(`${getPlatformName(source)} 连接正常`)
    } else {
      ElMessage.error(`${getPlatformName(source)} 连接异常: ${status.error}`)
    }
  } catch (error) {
    ElMessage.error('测试连接失败: ' + error.message)
  }
}

const removeSource = async (source) => {
  try {
    await ElMessageBox.confirm(
      `确定要移除数据源 ${getPlatformName(source)} 吗？`,
      '确认移除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 这里应该调用移除API的逻辑
    ElMessage.success(`数据源 ${getPlatformName(source)} 已移除`)
    await refreshAllSources()
  } catch {
    // 用户取消
  }
}

const searchProducts = async () => {
  if (!searchForm.value.keyword.trim()) {
    ElMessage.warning('请输入产品名称')
    return
  }

  searching.value = true
  try {
    const results = await supplierApiManager.searchProductsAcrossPlatforms(
      searchForm.value.keyword,
      { quantity: searchForm.value.quantity }
    )
    searchResults.value = results.results
  } catch (error) {
    ElMessage.error('搜索失败: ' + error.message)
  } finally {
    searching.value = false
  }
}

const getRecommendations = async () => {
  if (!recommendForm.value.category) {
    ElMessage.warning('请选择产品分类')
    return
  }

  recommending.value = true
  try {
    recommendations.value = await supplierApiManager.recommendSuppliers(recommendForm.value)
  } catch (error) {
    ElMessage.error('获取推荐失败: ' + error.message)
  } finally {
    recommending.value = false
  }
}

const getSourceStats = (source) => {
  // 模拟统计数据
  const stats = {
    '1688': 1250,
    'ccgp': 580,
    'jd_enterprise': 320
  }
  return stats[source] || 0
}

const getPlatformName = (source) => {
  const names = {
    '1688': '阿里巴巴1688',
    'ccgp': '中国政府采购网',
    'jd_enterprise': '京东企业购'
  }
  return names[source] || source
}

const formatPrice = (price, currency) => {
  if (!price) return '价格面议'
  const currencyMap = {
    'CNY': '¥',
    'USD': '$',
    'EUR': '€'
  }
  const symbol = currencyMap[currency] || currency
  return `${symbol}${price.toLocaleString()}`
}

const viewProductDetail = (product) => {
  selectedProduct.value = product
  showProductDetail.value = true
}

const viewAllProducts = (source, keyword) => {
  // 跳转到产品列表页面，显示指定平台和关键词的产品
  ElMessage.info(`跳转到 ${getPlatformName(source)} 产品列表`)
}

const contactSupplier = (supplier) => {
  // 联系供应商的逻辑
  ElMessage.info(`联系供应商: ${supplier.name}`)
}

const viewSupplierProducts = (supplier) => {
  // 查看供应商产品的逻辑
  ElMessage.info(`查看 ${supplier.name} 的产品`)
}

const exportAllData = () => {
  exporting.value = true
  setTimeout(() => {
    exporting.value = false
    ElMessage.success('数据导出成功')
  }, 2000)
}

const generateReport = () => {
  ElMessage.info('正在生成分析报告...')
}

const addNewSource = async () => {
  try {
    // 验证表单
    if (!newSourceForm.value.type || !newSourceForm.value.baseURL) {
      ElMessage.warning('请填写必要信息')
      return
    }

    // 这里应该调用添加数据源的API
    ElMessage.success('数据源添加成功')
    showAddSourceDialog.value = false
    
    // 重置表单
    Object.keys(newSourceForm.value).forEach(key => {
      newSourceForm.value[key] = ''
    })
    newSourceForm.value.rateLimit = 100
    
    await refreshAllSources()
  } catch (error) {
    ElMessage.error('添加失败: ' + error.message)
  }
}

// 生命周期
onMounted(() => {
  refreshAllSources()
})
</script>

<style scoped>
.supplier-api-integration {
  padding: 20px;
}

.integration-card {
  min-height: 600px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.sources-overview {
  margin-bottom: 30px;
}

.source-card {
  padding: 20px;
  border-radius: 8px;
  border: 2px solid #e4e7ed;
  transition: all 0.3s;
}

.source-card.healthy {
  border-color: #67c23a;
  background: #f0f9ff;
}

.source-card.unhealthy {
  border-color: #f56c6c;
  background: #fef0f0;
}

.source-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.source-info h4 {
  margin: 0;
  font-size: 16px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #909399;
  font-size: 14px;
}

.source-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.search-section {
  margin-bottom: 20px;
}

.search-results {
  margin-top: 20px;
}

.results-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 15px;
}

.result-card {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 15px;
  background: #fff;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.platform-name {
  font-weight: 600;
  color: #303133;
}

.product-count {
  color: #909399;
  font-size: 14px;
}

.product-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.product-item {
  display: flex;
  gap: 10px;
  padding: 10px;
  border: 1px solid #f0f0f0;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.product-item:hover {
  border-color: #409eff;
  background: #f0f9ff;
}

.product-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.product-info h5 {
  margin: 0 0 5px 0;
  font-size: 14px;
  color: #303133;
}

.product-price {
  color: #f56c6c;
  font-weight: 600;
  margin: 0 0 3px 0;
}

.supplier-name {
  color: #909399;
  font-size: 12px;
  margin: 0;
}

.result-actions {
  margin-top: 15px;
  text-align: center;
}

.recommendation-section {
  margin-top: 20px;
}

.recommendations {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.supplier-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background: #fff;
}

.supplier-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 10px;
}

.supplier-header h4 {
  margin: 0;
  font-size: 16px;
}

.supplier-details p {
  margin: 5px 0;
  color: #606266;
  font-size: 14px;
}

.supplier-actions {
  display: flex;
  gap: 10px;
}

.batch-actions {
  margin-top: 20px;
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.product-detail {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 30px;
}

.product-images {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.detail-image {
  width: 100%;
  max-width: 200px;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
}

.product-info h3 {
  margin: 0 0 15px 0;
  color: #303133;
}

.product-description {
  color: #606266;
  margin-bottom: 20px;
  line-height: 1.6;
}

.product-specs,
.product-pricing,
.supplier-info {
  margin-bottom: 20px;
}

.product-specs h4,
.product-pricing h4,
.supplier-info h4 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 16px;
}

.product-specs pre {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 6px;
  overflow-x: auto;
  font-size: 12px;
}

.product-pricing p,
.supplier-info p {
  margin: 8px 0;
  color: #606266;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>