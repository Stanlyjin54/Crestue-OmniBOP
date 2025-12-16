<template>
  <div class="product-detail">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: `/category/${product?.categoryId}` }">{{ product?.categoryName }}</el-breadcrumb-item>
      <el-breadcrumb-item>{{ product?.name }}</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="product-container" v-if="product && !loading">
      <!-- 商品图片展示 -->
      <div class="product-images">
        <div class="main-image">
          <img :src="currentImage" :alt="product.name" @error="handleImageError" />
        </div>
        <div class="thumbnail-list">
          <div 
            v-for="(image, index) in product.images" 
            :key="index"
            class="thumbnail-item"
            :class="{ active: currentImageIndex === index }"
            @click="currentImageIndex = index"
          >
            <img :src="image" :alt="`${product.name}-${index}`" />
          </div>
        </div>
      </div>

      <!-- 商品信息 -->
      <div class="product-info">
        <h1 class="product-title">{{ product.name }}</h1>
        <p class="product-subtitle">{{ product.subtitle }}</p>
        
        <div class="price-section">
          <div class="price-main">
            <span class="price-symbol">¥</span>
            <span class="price-integer">{{ formatPrice(product.price).split('.')[0] }}</span>
            <span class="price-decimal">.{{ formatPrice(product.price).split('.')[1] }}</span>
          </div>
          <div class="price-original" v-if="product.originalPrice > product.price">
            原价：¥{{ formatPrice(product.originalPrice) }}
          </div>
          <div class="discount-badge" v-if="product.discount > 0">
            {{ product.discount }}折
          </div>
        </div>

        <div class="product-meta">
          <div class="meta-item">
            <span class="meta-label">品牌：</span>
            <span class="meta-value">{{ product.brand || '自营品牌' }}</span>
          </div>
          <div class="meta-item">
            <span class="meta-label">库存：</span>
            <span class="meta-value">{{ product.stock }}件</span>
          </div>
          <div class="meta-item">
            <span class="meta-label">销量：</span>
            <span class="meta-value">{{ product.sales }}件</span>
          </div>
        </div>

        <!-- SKU选择 -->
        <div class="sku-section" v-if="skus.length > 0">
          <div v-for="(spec, specIndex) in specs" :key="specIndex" class="spec-group">
            <div class="spec-label">{{ spec.name }}：</div>
            <div class="spec-options">
              <div
                v-for="(option, optionIndex) in spec.options"
                :key="optionIndex"
                class="spec-option"
                :class="{ 
                  active: selectedSpecs[spec.name] === option,
                  disabled: isSpecDisabled(spec.name, option)
                }"
                @click="selectSpec(spec.name, option)"
              >
                {{ option }}
              </div>
            </div>
          </div>
        </div>

        <!-- 数量选择 -->
        <div class="quantity-section">
          <span class="quantity-label">数量：</span>
          <el-input-number 
            v-model="quantity" 
            :min="1" 
            :max="product.stock"
            :disabled="product.stock === 0"
            @change="handleQuantityChange"
          />
          <span class="stock-info">库存{{ product.stock }}件</span>
        </div>

        <!-- 操作按钮 -->
        <div class="action-buttons">
          <el-button 
            type="primary" 
            size="large" 
            class="buy-now"
            :disabled="product.stock === 0"
            @click="handleBuyNow"
          >
            立即购买
          </el-button>
          <el-button 
            type="warning" 
            size="large" 
            class="add-to-cart"
            :disabled="product.stock === 0"
            @click="handleAddToCart"
          >
            <el-icon><ShoppingCart /></el-icon>
            加入购物车
          </el-button>
          <el-button 
            :type="isFavorited ? 'danger' : 'default'" 
            size="large" 
            class="favorite"
            @click="toggleFavorite"
          >
            <el-icon><Star :class="{ 'is-favorite': isFavorited }" /></el-icon>
            {{ isFavorited ? '已收藏' : '收藏' }}
          </el-button>
        </div>

        <!-- 服务保障 -->
        <div class="service-guarantees">
          <div class="guarantee-item">
            <el-icon><CircleCheck /></el-icon>
            正品保障
          </div>
          <div class="guarantee-item">
            <el-icon><CircleCheck /></el-icon>
            七天无理由退换
          </div>
          <div class="guarantee-item">
            <el-icon><CircleCheck /></el-icon>
            售后保障
          </div>
        </div>
      </div>
    </div>

    <!-- 加载状态 -->
    <div class="loading-container" v-if="loading">
      <el-skeleton :rows="5" animated />
    </div>

    <!-- 商品详情内容 -->
    <div class="product-content" v-if="product && !loading">
      <el-tabs v-model="activeTab" class="content-tabs">
        <el-tab-pane label="商品详情" name="detail">
          <div class="detail-content" v-html="product.detail"></div>
        </el-tab-pane>
        <el-tab-pane label="规格参数" name="specs">
          <div class="specs-table">
            <table>
              <tr v-for="(value, key) in product.attributes" :key="key">
                <td class="spec-label">{{ key }}</td>
                <td class="spec-value">{{ value }}</td>
              </tr>
            </table>
          </div>
        </el-tab-pane>
        <el-tab-pane label="用户评价" name="reviews">
          <ProductReviews :product-id="productId" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ShoppingCart, Star, CircleCheck } from '@element-plus/icons-vue'
import { getProductDetail, getProductSkus } from '@/api/product'
import { addToCart } from '@/api/cart'
import { toggleFavorite as toggleFavoriteApi } from '@/api/user'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import { formatPrice } from '@/utils'
import ProductReviews from '@/components/ProductReviews.vue'
import type { Product, Sku, Spec } from '@/types'

defineOptions({
  name: 'ProductDetail'
})

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

const productId = computed(() => Number(route.params.id))
const loading = ref(true)
const product = ref<Product | null>(null)
const skus = ref<Sku[]>([])
const specs = ref<Spec[]>([])
const currentImageIndex = ref(0)
const quantity = ref(1)
const selectedSpecs = ref<Record<string, string>>({})
const activeTab = ref('detail')
const isFavorited = ref(false)

// 计算属性
const currentImage = computed(() => {
  if (!product.value || !product.value.images || product.value.images.length === 0) {
    return '/placeholder-image.jpg'
  }
  return product.value.images[currentImageIndex.value] || product.value.images[0]
})

// 监听商品ID变化
watch(productId, (newId) => {
  if (newId) {
    loadProductDetail()
  }
}, { immediate: true })

// 加载商品详情
async function loadProductDetail() {
  try {
    loading.value = true
    const data = await getProductDetail(productId.value)
    product.value = data
    
    // 加载SKU信息
    if (data.hasSku) {
      const skuData = await getProductSkus(productId.value)
      skus.value = skuData.skus
      specs.value = skuData.specs
    }
    
    // 检查是否已收藏
    if (userStore.isLoggedIn) {
      // 检查收藏状态
      // isFavorited.value = await checkFavoriteStatus(productId.value)
    }
    
  } catch (error) {
    console.error('加载商品详情失败:', error)
    ElMessage.error('加载商品详情失败')
  } finally {
    loading.value = false
  }
}

// 选择规格
function selectSpec(specName: string, option: string) {
  if (isSpecDisabled(specName, option)) return
  
  selectedSpecs.value[specName] = option
  updateSkuInfo()
}

// 检查规格是否禁用
function isSpecDisabled(specName: string, option: string): boolean {
  if (skus.value.length === 0) return false
  
  const testSpecs = { ...selectedSpecs.value, [specName]: option }
  return !skus.value.some(sku => {
    return Object.entries(testSpecs).every(([key, value]) => 
      sku.specs[key] === value
    )
  })
}

// 更新SKU信息
function updateSkuInfo() {
  if (skus.value.length === 0) return
  
  const currentSpecs = selectedSpecs.value
  const matchedSku = skus.value.find(sku => {
    return Object.entries(currentSpecs).every(([key, value]) => 
      sku.specs[key] === value
    )
  })
  
  if (matchedSku) {
    product.value!.price = matchedSku.price
    product.value!.stock = matchedSku.stock
  }
}

// 数量变化处理
function handleQuantityChange(value: number | undefined) {
  if (!value || value < 1) {
    quantity.value = 1
  } else if (product.value && value > product.value.stock) {
    quantity.value = product.value.stock
    ElMessage.warning(`库存不足，最多只能购买${product.value.stock}件`)
  }
}

// 立即购买
async function handleBuyNow() {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  if (!product.value) return
  
  try {
    // 添加到购物车
    await addToCart({
      productId: product.value.id,
      quantity: quantity.value,
      specs: selectedSpecs.value
    })
    
    // 跳转到结算页面
    router.push('/checkout')
  } catch (error) {
    console.error('立即购买失败:', error)
    ElMessage.error('立即购买失败')
  }
}

// 加入购物车
async function handleAddToCart() {
  if (!product.value) return
  
  try {
    await addToCart({
      productId: product.value.id,
      quantity: quantity.value,
      specs: selectedSpecs.value
    })
    
    ElMessage.success('已加入购物车')
    cartStore.updateCartCount()
  } catch (error) {
    console.error('加入购物车失败:', error)
    ElMessage.error('加入购物车失败')
  }
}

// 切换收藏
async function toggleFavorite() {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  try {
    await toggleFavoriteApi(productId.value)
    isFavorited.value = !isFavorited.value
    ElMessage.success(isFavorited.value ? '已加入收藏' : '已取消收藏')
  } catch (error) {
    console.error('收藏操作失败:', error)
    ElMessage.error('收藏操作失败')
  }
}

// 图片加载错误处理
function handleImageError(event: Event) {
  const img = event.target as HTMLImageElement
  img.src = '/placeholder-image.jpg'
}
</script>

<style scoped lang="scss">
.product-detail {
  min-height: 100vh;
  background: #f5f5f5;
}

.breadcrumb {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  margin-bottom: 20px;
}

.product-container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
}

.product-images {
  .main-image {
    width: 100%;
    height: 400px;
    margin-bottom: 15px;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      border-radius: 8px;
    }
  }
  
  .thumbnail-list {
    display: flex;
    gap: 10px;
    overflow-x: auto;
    
    .thumbnail-item {
      width: 80px;
      height: 80px;
      flex-shrink: 0;
      cursor: pointer;
      border: 2px solid transparent;
      border-radius: 4px;
      overflow: hidden;
      transition: border-color 0.3s;
      
      &.active {
        border-color: #409eff;
      }
      
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
  }
}

.product-info {
  .product-title {
    font-size: 24px;
    font-weight: bold;
    margin: 0 0 10px;
    color: #333;
  }
  
  .product-subtitle {
    color: #666;
    margin-bottom: 20px;
  }
  
  .price-section {
    margin-bottom: 20px;
    
    .price-main {
      display: flex;
      align-items: baseline;
      margin-bottom: 5px;
      
      .price-symbol {
        font-size: 18px;
        color: #f56c6c;
      }
      
      .price-integer {
        font-size: 32px;
        font-weight: bold;
        color: #f56c6c;
      }
      
      .price-decimal {
        font-size: 18px;
        color: #f56c6c;
      }
    }
    
    .price-original {
      color: #999;
      text-decoration: line-through;
      margin-bottom: 5px;
    }
    
    .discount-badge {
      display: inline-block;
      background: #f56c6c;
      color: white;
      padding: 2px 8px;
      border-radius: 4px;
      font-size: 12px;
    }
  }
  
  .product-meta {
    margin-bottom: 20px;
    
    .meta-item {
      margin-bottom: 8px;
      color: #333;
    }
    
    .meta-label {
      color: #666;
      margin-right: 10px;
    }
  }
  
  .sku-section {
    margin-bottom: 20px;
    
    .spec-group {
      margin-bottom: 15px;
      
      .spec-label {
        margin-bottom: 8px;
        color: #333;
      }
      
      .spec-options {
        display: flex;
        gap: 10px;
        flex-wrap: wrap;
        
        .spec-option {
          padding: 8px 16px;
          border: 1px solid #ddd;
          border-radius: 4px;
          cursor: pointer;
          transition: all 0.3s;
          
          &:hover {
            border-color: #409eff;
          }
          
          &.active {
            border-color: #409eff;
            background: #409eff;
            color: white;
          }
          
          &.disabled {
            opacity: 0.5;
            cursor: not-allowed;
            
            &:hover {
              border-color: #ddd;
            }
          }
        }
      }
    }
  }
  
  .quantity-section {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    
    .quantity-label {
      margin-right: 15px;
      color: #333;
    }
    
    .stock-info {
      margin-left: 15px;
      color: #666;
      font-size: 14px;
    }
  }
  
  .action-buttons {
    display: flex;
    gap: 15px;
    margin-bottom: 20px;
    
    .el-button {
      flex: 1;
      
      &.buy-now {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border: none;
        
        &:hover {
          opacity: 0.9;
        }
      }
      
      &.add-to-cart {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        border: none;
        
        &:hover {
          opacity: 0.9;
        }
      }
      
      &.favorite {
        .is-favorite {
          color: #ff6b6b;
        }
      }
    }
  }
  
  .service-guarantees {
    display: flex;
    gap: 20px;
    padding-top: 20px;
    border-top: 1px solid #eee;
    
    .guarantee-item {
      display: flex;
      align-items: center;
      gap: 5px;
      color: #666;
      font-size: 14px;
      
      .el-icon {
        color: #67c23a;
      }
    }
  }
}

.loading-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.product-content {
  max-width: 1200px;
  margin: 0 auto;
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  
  .content-tabs {
    :deep(.el-tabs__item) {
      font-size: 16px;
      padding: 0 30px;
    }
  }
  
  .detail-content {
    padding: 20px 0;
    line-height: 1.8;
    
    img {
      max-width: 100%;
      height: auto;
    }
  }
  
  .specs-table {
    padding: 20px 0;
    
    table {
      width: 100%;
      border-collapse: collapse;
      
      tr {
        border-bottom: 1px solid #eee;
        
        &:hover {
          background: #f8f9fa;
        }
      }
      
      td {
        padding: 12px 20px;
        
        &.spec-label {
          background: #f8f9fa;
          color: #666;
          width: 150px;
          font-weight: 500;
        }
        
        &.spec-value {
          color: #333;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .product-container {
    grid-template-columns: 1fr;
    gap: 20px;
    padding: 20px;
  }
  
  .product-images .main-image {
    height: 300px;
  }
  
  .action-buttons {
    flex-direction: column;
    
    .el-button {
      width: 100%;
    }
  }
  
  .service-guarantees {
    flex-direction: column;
    gap: 10px;
  }
}
</style>