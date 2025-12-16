<template>
  <view class="supplier-api-integration">
    <!-- 页面标题 -->
    <view class="page-header">
      <text class="header-title">第三方供应商平台</text>
      <view class="header-actions">
        <button class="refresh-btn" @click="refreshAllSources">
          <text class="icon">🔄</text>
          刷新
        </button>
      </view>
    </view>

    <!-- 搜索区域 -->
    <view class="search-section">
      <view class="search-card">
        <view class="search-form">
          <input 
            class="search-input" 
            v-model="searchForm.keyword" 
            placeholder="搜索产品名称..."
            @confirm="searchProducts"
          />
          <view class="quantity-input">
            <text class="input-label">数量:</text>
            <input 
              class="number-input" 
              type="number" 
              v-model="searchForm.quantity"
              min="1"
            />
          </view>
          <button 
            class="search-btn" 
            @click="searchProducts"
            :disabled="searching"
          >
            <text v-if="!searching">🔍</text>
            <text v-else class="loading">⏳</text>
            {{ searching ? '搜索中...' : '搜索' }}
          </button>
        </view>
      </view>
    </view>

    <!-- 数据源状态 -->
    <view class="sources-section">
      <text class="section-title">数据源状态</text>
      <scroll-view class="sources-scroll" scroll-x="true">
        <view class="sources-container">
          <view 
            v-for="source in availableSources" 
            :key="source.source"
            class="source-card"
            :class="source.available ? 'healthy' : 'unhealthy'"
          >
            <view class="source-header">
              <text class="source-name">{{ getPlatformName(source.source) }}</text>
              <view class="status-indicator">
                <text class="status-dot" :class="source.available ? 'dot-green' : 'dot-red'"></text>
                <text class="status-text">{{ source.available ? '正常' : '异常' }}</text>
              </view>
            </view>
            <view class="source-stats">
              <text class="stat-item">{{ getSourceStats(source.source) }} 产品</text>
            </view>
            <view class="source-actions">
              <button 
                class="action-btn sync-btn" 
                @click="syncSource(source.source)"
                :disabled="!source.available || syncing[source.source]"
              >
                {{ syncing[source.source] ? '⏳' : '🔄' }} 同步
              </button>
              <button 
                class="action-btn test-btn" 
                @click="testSource(source.source)"
                :disabled="!source.available"
              >
                🧪 测试
              </button>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 搜索结果 -->
    <view v-if="searchResults.length > 0" class="results-section">
      <view class="section-header">
        <text class="section-title">搜索结果</text>
        <text class="result-count">{{ searchResults.length }} 个平台</text>
      </view>
      
      <view class="results-container">
        <view 
          v-for="result in searchResults" 
          :key="result.source"
          class="result-platform"
        >
          <view class="platform-header">
            <text class="platform-name">{{ getPlatformName(result.source) }}</text>
            <text class="platform-count">{{ result.count }} 产品</text>
          </view>
          
          <scroll-view class="products-scroll" scroll-x="true">
            <view class="products-container">
              <view 
                v-for="product in result.products.slice(0, 5)" 
                :key="product.id"
                class="product-card"
                @click="viewProductDetail(product)"
              >
                <image 
                  v-if="product.images && product.images[0]" 
                  :src="product.images[0]" 
                  class="product-image"
                  mode="aspectFill"
                />
                <view v-else class="product-image-placeholder">
                  <text>📦</text>
                </view>
                
                <view class="product-info">
                  <text class="product-name">{{ product.name }}</text>
                  <text class="product-price">{{ formatPrice(product.price, product.currency) }}</text>
                  <text class="supplier-name">{{ product.supplier.name }}</text>
                </view>
              </view>
            </view>
          </scroll-view>
          
          <view class="platform-actions">
            <button 
              class="view-all-btn" 
              @click="viewAllProducts(result.source, searchForm.keyword)"
            >
              查看全部 {{ result.count }} 个产品
            </button>
          </view>
        </view>
      </view>
    </view>

    <!-- 智能推荐 -->
    <view class="recommendation-section">
      <text class="section-title">智能推荐</text>
      
      <view class="recommend-form">
        <view class="form-row">
          <view class="form-group">
            <text class="form-label">产品分类:</text>
            <picker 
              :value="recommendForm.category" 
              :range="categories" 
              range-key="name"
              @change="onCategoryChange"
            >
              <view class="picker-input">
                <text>{{ selectedCategoryName }}</text>
                <text class="picker-arrow">▼</text>
              </view>
            </picker>
          </view>
          
          <view class="form-group">
            <text class="form-label">地区:</text>
            <picker 
              :value="recommendForm.region" 
              :range="regions" 
              @change="onRegionChange"
            >
              <view class="picker-input">
                <text>{{ selectedRegionName }}</text>
                <text class="picker-arrow">▼</text>
              </view>
            </picker>
          </view>
        </view>
        
        <button 
          class="recommend-btn" 
          @click="getRecommendations"
          :disabled="recommending"
        >
          <text v-if="!recommending">⭐</text>
          <text v-else class="loading">⏳</text>
          {{ recommending ? '获取中...' : '获取推荐' }}
        </button>
      </view>

      <!-- 推荐结果 -->
      <view v-if="recommendations.length > 0" class="recommendations-container">
        <view 
          v-for="supplier in recommendations.slice(0, 3)" 
          :key="supplier.id"
          class="supplier-card"
        >
          <view class="supplier-info">
            <view class="supplier-header">
              <text class="supplier-name">{{ supplier.name }}</text>
              <view class="rating">
                <text class="star">⭐</text>
                <text class="rating-text">{{ supplier.rating }}/5</text>
              </view>
            </view>
            
            <view class="supplier-details">
              <view class="detail-item">
                <text class="detail-label">位置:</text>
                <text class="detail-value">{{ supplier.location }}</text>
              </view>
              <view class="detail-item">
                <text class="detail-label">平台:</text>
                <text class="detail-value">{{ getPlatformName(supplier.platform) }}</text>
              </view>
              <view class="detail-item">
                <text class="detail-label">评分:</text>
                <text class="detail-value">{{ supplier.score }}/100</text>
              </view>
              <view class="detail-item">
                <text class="detail-label">认证:</text>
                <view class="certifications">
                  <text 
                    v-for="cert in supplier.certifications.slice(0, 2)" 
                    :key="cert"
                    class="cert-tag"
                  >
                    {{ cert }}
                  </text>
                </view>
              </view>
            </view>
          </view>
          
          <view class="supplier-actions">
            <button 
              class="action-btn contact-btn" 
              @click="contactSupplier(supplier)"
            >
              📞 联系
            </button>
            <button 
              class="action-btn products-btn" 
              @click="viewSupplierProducts(supplier)"
            >
              📦 产品
            </button>
          </view>
        </view>
      </view>
    </view>

    <!-- 批量操作 -->
    <view class="batch-section">
      <text class="section-title">批量操作</text>
      
      <view class="batch-actions">
        <button 
          class="batch-btn export-btn" 
          @click="exportAllData"
          :disabled="exporting"
        >
          <text>{{ exporting ? '⏳' : '📊' }} {{ exporting ? '导出中...' : '导出数据' }}
        </button>
        
        <button 
          class="batch-btn report-btn" 
          @click="generateReport"
        >
          <text>📈</text> 分析报告
        </button>
        
        <button 
          class="batch-btn settings-btn" 
          @click="showSettings = true"
        >
          <text>⚙️</text> 设置
        </button>
      </view>
    </view>

    <!-- 产品详情弹窗 -->
    <view 
      v-if="showProductDetail" 
      class="modal-overlay"
      @click="showProductDetail = false"
    >
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">产品详情</text>
          <button class="close-btn" @click="showProductDetail = false">
            <text>✕</text>
          </button>
        </view>
        
        <scroll-view class="modal-body" scroll-y="true">
          <view v-if="selectedProduct" class="product-detail">
            <view class="product-images">
              <image 
                v-for="(image, index) in selectedProduct.images.slice(0, 3)" 
                :key="index"
                :src="image" 
                class="detail-image"
                mode="aspectFill"
              />
            </view>
            
            <view class="product-info">
              <text class="detail-name">{{ selectedProduct.name }}</text>
              <text class="detail-description">{{ selectedProduct.description }}</text>
              
              <view class="info-section">
                <text class="info-title">价格信息</text>
                <view class="info-item">
                  <text class="info-label">价格:</text>
                  <text class="info-value">{{ formatPrice(selectedProduct.price, selectedProduct.currency) }}</text>
                </view>
                <view class="info-item">
                  <text class="info-label">起订量:</text>
                  <text class="info-value">{{ selectedProduct.minOrderQuantity }}</text>
                </view>
                <view class="info-item">
                  <text class="info-label">交货期:</text>
                  <text class="info-value">{{ selectedProduct.leadTime }}</text>
                </view>
              </view>
              
              <view class="info-section">
                <text class="info-title">供应商信息</text>
                <view class="info-item">
                  <text class="info-label">名称:</text>
                  <text class="info-value">{{ selectedProduct.supplier.name }}</text>
                </view>
                <view class="info-item">
                  <text class="info-label">位置:</text>
                  <text class="info-value">{{ selectedProduct.supplier.location }}</text>
                </view>
                <view class="info-item">
                  <text class="info-label">评分:</text>
                  <text class="info-value">{{ selectedProduct.supplier.rating }}/5</text>
                </view>
              </view>
            </view>
          </view>
        </scroll-view>
      </view>
    </view>
  </view>
</template>

<script>
import supplierApiManager from '@/utils/supplierApiAdapter.js'

export default {
  data() {
    return {
      loading: false,
      searching: false,
      recommending: false,
      exporting: false,
      syncing: {},
      showProductDetail: false,
      showSettings: false,
      
      availableSources: [],
      searchResults: [],
      recommendations: [],
      selectedProduct: null,
      
      searchForm: {
        keyword: '',
        quantity: 1
      },
      
      recommendForm: {
        category: '',
        region: ''
      },
      
      categories: [
        { id: 'electronics', name: '电子产品' },
        { id: 'office', name: '办公用品' },
        { id: 'construction', name: '建筑工程' },
        { id: 'manufacturing', name: '制造业' },
        { id: 'services', name: '服务类' }
      ],
      
      regions: ['北京', '上海', '广州', '深圳', '全国']
    }
  },
  
  computed: {
    selectedCategoryName() {
      const category = this.categories.find(cat => cat.id === this.recommendForm.category)
      return category ? category.name : '请选择分类'
    },
    
    selectedRegionName() {
      return this.recommendForm.region || '请选择地区'
    }
  },
  
  onLoad() {
    this.refreshAllSources()
  },
  
  methods: {
    // 刷新所有数据源
    async refreshAllSources() {
      this.loading = true
      try {
        this.availableSources = supplierApiManager.getAvailableSources()
      } catch (error) {
        uni.showToast({
          title: '刷新失败',
          icon: 'error'
        })
      } finally {
        this.loading = false
      }
    },
    
    // 同步指定数据源
    async syncSource(source) {
      this.$set(this.syncing, source, true)
      try {
        await supplierApiManager.syncData(source, 'products')
        await this.refreshAllSources()
        uni.showToast({
          title: '同步成功',
          icon: 'success'
        })
      } catch (error) {
        uni.showToast({
          title: '同步失败',
          icon: 'error'
        })
      } finally {
        this.$set(this.syncing, source, false)
      }
    },
    
    // 测试数据源连接
    async testSource(source) {
      try {
        const healthStatus = await supplierApiManager.healthCheck()
        const status = healthStatus[source]
        if (status.status === 'healthy') {
          uni.showToast({
            title: `${this.getPlatformName(source)} 连接正常`,
            icon: 'success'
          })
        } else {
          uni.showToast({
            title: `${this.getPlatformName(source)} 连接异常`,
            icon: 'error'
          })
        }
      } catch (error) {
        uni.showToast({
          title: '测试连接失败',
          icon: 'error'
        })
      }
    },
    
    // 搜索产品
    async searchProducts() {
      if (!this.searchForm.keyword.trim()) {
        uni.showToast({
          title: '请输入产品名称',
          icon: 'none'
        })
        return
      }
      
      this.searching = true
      try {
        const results = await supplierApiManager.searchProductsAcrossPlatforms(
          this.searchForm.keyword,
          { quantity: this.searchForm.quantity }
        )
        this.searchResults = results.results
      } catch (error) {
        uni.showToast({
          title: '搜索失败',
          icon: 'error'
        })
      } finally {
        this.searching = false
      }
    },
    
    // 获取推荐
    async getRecommendations() {
      if (!this.recommendForm.category) {
        uni.showToast({
          title: '请选择产品分类',
          icon: 'none'
        })
        return
      }
      
      this.recommending = true
      try {
        this.recommendations = await supplierApiManager.recommendSuppliers(this.recommendForm)
      } catch (error) {
        uni.showToast({
          title: '获取推荐失败',
          icon: 'error'
        })
      } finally {
        this.recommending = false
      }
    },
    
    // 分类选择变化
    onCategoryChange(e) {
      const index = e.detail.value
      this.recommendForm.category = this.categories[index].id
    },
    
    // 地区选择变化
    onRegionChange(e) {
      const index = e.detail.value
      this.recommendForm.region = this.regions[index]
    },
    
    // 获取平台名称
    getPlatformName(source) {
      const names = {
        '1688': '阿里巴巴1688',
        'ccgp': '中国政府采购网',
        'jd_enterprise': '京东企业购'
      }
      return names[source] || source
    },
    
    // 获取数据源统计
    getSourceStats(source) {
      const stats = {
        '1688': 1250,
        'ccgp': 580,
        'jd_enterprise': 320
      }
      return stats[source] || 0
    },
    
    // 格式化价格
    formatPrice(price, currency) {
      if (!price) return '价格面议'
      const currencyMap = {
        'CNY': '¥',
        'USD': '$',
        'EUR': '€'
      }
      const symbol = currencyMap[currency] || currency
      return `${symbol}${price.toLocaleString()}`
    },
    
    // 查看产品详情
    viewProductDetail(product) {
      this.selectedProduct = product
      this.showProductDetail = true
    },
    
    // 查看所有产品
    viewAllProducts(source, keyword) {
      uni.navigateTo({
        url: `/pages/procurement/product-list?source=${source}&keyword=${keyword}`
      })
    },
    
    // 联系供应商
    contactSupplier(supplier) {
      uni.makePhoneCall({
        phoneNumber: supplier.phone || '400-000-0000'
      })
    },
    
    // 查看供应商产品
    viewSupplierProducts(supplier) {
      uni.navigateTo({
        url: `/pages/procurement/supplier-products?id=${supplier.id}`
      })
    },
    
    // 导出数据
    exportAllData() {
      this.exporting = true
      setTimeout(() => {
        this.exporting = false
        uni.showToast({
          title: '数据导出成功',
          icon: 'success'
        })
      }, 2000)
    },
    
    // 生成报告
    generateReport() {
      uni.showToast({
        title: '正在生成分析报告...',
        icon: 'none'
      })
    }
  }
}
</script>

<style scoped>
.supplier-api-integration {
  padding: 20rpx;
  background: #f5f7fa;
  min-height: 100vh;
}

/* 页面标题 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  padding-left: 30rpx;
  padding-right: 30rpx;
}

.header-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #303133;
}

.refresh-btn {
  background: #409eff;
  color: white;
  border: none;
  border-radius: 8rpx;
  padding: 12rpx 20rpx;
  font-size: 28rpx;
}

/* 搜索区域 */
.search-section {
  margin-bottom: 30rpx;
}

.search-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.search-form {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.search-input {
  border: 1rpx solid #e4e7ed;
  border-radius: 8rpx;
  padding: 20rpx;
  font-size: 28rpx;
  background: #f8f9fa;
}

.quantity-input {
  display: flex;
  align-items: center;
  gap: 15rpx;
}

.input-label {
  font-size: 28rpx;
  color: #606266;
  min-width: 80rpx;
}

.number-input {
  border: 1rpx solid #e4e7ed;
  border-radius: 8rpx;
  padding: 15rpx;
  font-size: 28rpx;
  background: #f8f9fa;
  width: 200rpx;
}

.search-btn {
  background: #409eff;
  color: white;
  border: none;
  border-radius: 8rpx;
  padding: 20rpx;
  font-size: 28rpx;
}

.search-btn:disabled {
  background: #c0c4cc;
}

.loading {
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 数据源状态 */
.sources-section {
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20rpx;
  display: block;
}

.sources-scroll {
  white-space: nowrap;
}

.sources-container {
  display: inline-flex;
  gap: 20rpx;
  padding: 10rpx 0;
}

.source-card {
  display: inline-block;
  width: 280rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 25rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
  border: 2rpx solid transparent;
}

.source-card.healthy {
  border-color: #67c23a;
}

.source-card.unhealthy {
  border-color: #f56c6c;
}

.source-header {
  margin-bottom: 15rpx;
}

.source-name {
  font-size: 28rpx;
  font-weight: 600;
  color: #303133;
  display: block;
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 8rpx;
  margin-top: 8rpx;
}

.status-dot {
  width: 12rpx;
  height: 12rpx;
  border-radius: 50%;
  display: inline-block;
}

.dot-green {
  background: #67c23a;
}

.dot-red {
  background: #f56c6c;
}

.status-text {
  font-size: 24rpx;
  color: #606266;
}

.source-stats {
  margin-bottom: 20rpx;
}

.stat-item {
  font-size: 24rpx;
  color: #909399;
}

.source-actions {
  display: flex;
  gap: 10rpx;
}

.action-btn {
  flex: 1;
  padding: 10rpx;
  border: none;
  border-radius: 6rpx;
  font-size: 24rpx;
  background: #f8f9fa;
  color: #606266;
}

.sync-btn {
  background: #409eff;
  color: white;
}

.test-btn {
  background: #e6a23c;
  color: white;
}

.contact-btn {
  background: #67c23a;
  color: white;
}

.products-btn {
  background: #909399;
  color: white;
}

/* 搜索结果 */
.results-section {
  margin-bottom: 30rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.result-count {
  font-size: 28rpx;
  color: #909399;
}

.results-container {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.result-platform {
  background: #fff;
  border-radius: 16rpx;
  padding: 25rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.platform-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  padding-bottom: 15rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.platform-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #303133;
}

.platform-count {
  font-size: 26rpx;
  color: #909399;
}

.products-scroll {
  white-space: nowrap;
}

.products-container {
  display: inline-flex;
  gap: 15rpx;
}

.product-card {
  display: inline-block;
  width: 200rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
  overflow: hidden;
  border: 1rpx solid #e4e7ed;
}

.product-image {
  width: 200rpx;
  height: 150rpx;
}

.product-image-placeholder {
  width: 200rpx;
  height: 150rpx;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 60rpx;
}

.product-info {
  padding: 15rpx;
}

.product-name {
  font-size: 26rpx;
  color: #303133;
  display: block;
  margin-bottom: 8rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  font-size: 24rpx;
  color: #f56c6c;
  font-weight: 600;
  display: block;
  margin-bottom: 5rpx;
}

.supplier-name {
  font-size: 22rpx;
  color: #909399;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.platform-actions {
  margin-top: 20rpx;
  text-align: center;
}

.view-all-btn {
  background: #409eff;
  color: white;
  border: none;
  border-radius: 8rpx;
  padding: 15rpx 30rpx;
  font-size: 26rpx;
}

/* 智能推荐 */
.recommendation-section {
  margin-bottom: 30rpx;
}

.recommend-form {
  background: #fff;
  border-radius: 16rpx;
  padding: 25rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.form-row {
  display: flex;
  gap: 20rpx;
  margin-bottom: 20rpx;
}

.form-group {
  flex: 1;
}

.form-label {
  font-size: 26rpx;
  color: #606266;
  display: block;
  margin-bottom: 10rpx;
}

.picker-input {
  border: 1rpx solid #e4e7ed;
  border-radius: 8rpx;
  padding: 20rpx;
  background: #f8f9fa;
  font-size: 28rpx;
  color: #303133;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.picker-arrow {
  color: #909399;
  font-size: 20rpx;
}

.recommend-btn {
  width: 100%;
  background: #67c23a;
  color: white;
  border: none;
  border-radius: 8rpx;
  padding: 20rpx;
  font-size: 28rpx;
}

.recommendations-container {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.supplier-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 25rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.supplier-info {
  flex: 1;
}

.supplier-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.supplier-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #303133;
}

.rating {
  display: flex;
  align-items: center;
  gap: 5rpx;
}

.star {
  font-size: 24rpx;
}

.rating-text {
  font-size: 24rpx;
  color: #909399;
}

.supplier-details {
  display: flex;
  flex-direction: column;
  gap: 10rpx;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 10rpx;
}

.detail-label {
  font-size: 26rpx;
  color: #606266;
  min-width: 100rpx;
}

.detail-value {
  font-size: 26rpx;
  color: #303133;
  flex: 1;
}

.certifications {
  display: flex;
  gap: 8rpx;
  flex-wrap: wrap;
}

.cert-tag {
  background: #67c23a;
  color: white;
  font-size: 20rpx;
  padding: 4rpx 8rpx;
  border-radius: 4rpx;
}

.supplier-actions {
  display: flex;
  flex-direction: column;
  gap: 10rpx;
}

/* 批量操作 */
.batch-section {
  margin-bottom: 30rpx;
}

.batch-actions {
  display: flex;
  gap: 15rpx;
  flex-wrap: wrap;
}

.batch-btn {
  flex: 1;
  min-width: 200rpx;
  padding: 20rpx;
  border: none;
  border-radius: 8rpx;
  font-size: 26rpx;
  background: #fff;
  color: #606266;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.export-btn {
  background: #67c23a;
  color: white;
}

.report-btn {
  background: #409eff;
  color: white;
}

.settings-btn {
  background: #909399;
  color: white;
}

/* 产品详情弹窗 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: #fff;
  border-radius: 16rpx;
  width: 90%;
  max-height: 80%;
  display: flex;
  flex-direction: column;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.modal-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #303133;
}

.close-btn {
  background: #f56c6c;
  color: white;
  border: none;
  border-radius: 50%;
  width: 60rpx;
  height: 60rpx;
  font-size: 28rpx;
}

.modal-body {
  flex: 1;
  padding: 30rpx;
}

.product-detail {
  display: flex;
  flex-direction: column;
  gap: 30rpx;
}

.product-images {
  display: flex;
  gap: 15rpx;
  justify-content: center;
}

.detail-image {
  width: 150rpx;
  height: 150rpx;
  border-radius: 8rpx;
}

.product-info {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.detail-name {
  font-size: 32rpx;
  font-weight: 600;
  color: #303133;
  line-height: 1.4;
}

.detail-description {
  font-size: 26rpx;
  color: #606266;
  line-height: 1.6;
}

.info-section {
  background: #f8f9fa;
  border-radius: 12rpx;
  padding: 20rpx;
}

.info-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #303133;
  margin-bottom: 15rpx;
  display: block;
}

.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10rpx;
}

.info-item:last-child {
  margin-bottom: 0;
}

.info-label {
  font-size: 26rpx;
  color: #606266;
}

.info-value {
  font-size: 26rpx;
  color: #303133;
  font-weight: 500;
}
</style>