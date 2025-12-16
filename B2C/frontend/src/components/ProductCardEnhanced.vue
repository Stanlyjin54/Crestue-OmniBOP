<template>
  <div class="enhanced-product-card">
    <div class="product-image">
      <img :src="product.mainImage || product.image" :alt="product.name" />
      <div class="product-overlay">
        <el-button type="primary" size="small" @click="goToDetail">
          查看详情
        </el-button>
      </div>
    </div>
    
    <div class="product-info">
      <h3 class="product-name">{{ product.name }}</h3>
      <p class="product-desc">{{ product.description }}</p>
      
      <!-- 价格信息 -->
      <div class="product-price">
        <span class="price">¥{{ formatPrice(product.price) }}</span>
        <span v-if="product.originalPrice && product.originalPrice > product.price" 
              class="original-price">¥{{ formatPrice(product.originalPrice) }}</span>
        <span v-if="getDiscountPercent()" class="discount-badge">
          {{ getDiscountPercent() }}折
        </span>
      </div>

      <!-- 供应商信息 (新增) -->
      <div v-if="product.supplier" class="supplier-info">
        <div class="supplier-name">
          <el-icon><Shop /></el-icon>
          <span>{{ product.supplier.name }}</span>
          <el-rate v-if="product.supplier.rating" 
                   :model-value="product.supplier.rating" 
                   disabled show-score 
                   text-color="#ff9900"
                   score-template="{value}分" 
                   size="small" />
        </div>
        <div class="supplier-location" v-if="product.supplier.location">
          <el-icon><Location /></el-icon>
          <span>{{ formatLocation(product.supplier.location) }}</span>
        </div>
      </div>

      <!-- 产品特色信息 (新增) -->
      <div class="product-features">
        <div class="feature-item" v-if="product.minOrderQuantity && product.minOrderQuantity > 1">
          <el-icon><DocumentCopy /></el-icon>
          <span>起订{{ product.minOrderQuantity }}件</span>
        </div>
        <div class="feature-item" v-if="product.leadTime">
          <el-icon><Timer /></el-icon>
          <span>{{ product.leadTime }}</span>
        </div>
        <div class="feature-item" v-if="product.certifications && product.certifications.length">
          <el-icon><Medal /></el-icon>
          <span>已认证</span>
        </div>
      </div>
      
      <!-- 库存和销售信息 -->
      <div class="product-stats">
        <span class="stock" :class="{ 'low-stock': product.stock < 10 }">
          <el-icon><Box /></el-icon>
          库存 {{ product.stock }}
        </span>
        <span class="sales">
          <el-icon><TrendCharts /></el-icon>
          已售 {{ product.sales || 0 }}
        </span>
      </div>

      <!-- 操作按钮 -->
      <div class="product-actions">
        <el-button type="primary" size="small" @click="addToCart" :disabled="!canAddToCart">
          <el-icon><ShoppingCart /></el-icon>
          加入购物车
        </el-button>
        <el-button size="small" @click="addToFavorites">
          <el-icon><Star /></el-icon>
        </el-button>
        <el-button size="small" @click="compareProduct">
          <el-icon><Share /></el-icon>
          对比
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { ElMessage } from 'element-plus'
// 图标已在main.ts中全局注册，无需单独导入
import type { Product } from '@/types'
import { useCartStore } from '@/stores/cart'

interface Props {
  product: Product & {
    supplier?: {
      id: string | number
      name: string
      rating?: number
      location?: {
        province?: string
        city?: string
        district?: string
        address?: string
      }
    }
    minOrderQuantity?: number
    leadTime?: string
    certifications?: string[]
    mainImage?: string
  }
}

interface Emits {
  (e: 'add-to-cart', product: Props['product']): void
  (e: 'add-to-favorites', product: Props['product']): void
  (e: 'compare', product: Props['product']): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()
const cartStore = useCartStore()

const formatPrice = (price: number | undefined) => {
  return (price || 0).toFixed(2)
}

const formatLocation = (location: any) => {
  if (!location) return ''
  return [location.province, location.city, location.district]
    .filter(Boolean)
    .join(' ')
}

const getDiscountPercent = () => {
  if (props.product.originalPrice && props.product.price) {
    const percent = Math.round((props.product.price / props.product.originalPrice) * 10)
    return percent > 0 && percent < 10 ? percent : null
  }
  return null
}

const canAddToCart = computed(() => {
  return props.product.stock > 0 && 
         (!props.product.minOrderQuantity || props.product.minOrderQuantity <= 1)
})

const goToDetail = () => {
  console.log('跳转到商品详情页:', props.product.id)
}

const addToCart = async () => {
  try {
    await cartStore.addItem(props.product.id, 1)
    ElMessage.success('已添加到购物车')
    emit('add-to-cart', props.product)
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

const addToFavorites = () => {
  emit('add-to-favorites', props.product)
}

const compareProduct = () => {
  emit('compare', props.product)
}
</script>

<style scoped lang="scss">
.enhanced-product-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  border: 1px solid #f0f0f0;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    border-color: #e6f7ff;
  }
}

.product-image {
  position: relative;
  width: 100%;
  height: 220px;
  overflow: hidden;
  background: #f8f9fa;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s ease;
  }

  &:hover img {
    transform: scale(1.05);
  }
}

.product-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.product-image:hover .product-overlay {
  opacity: 1;
}

.product-info {
  padding: 16px;
}

.product-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px;
  line-height: 1.4;
  height: 44px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-desc {
  font-size: 13px;
  color: #666;
  margin: 0 0 12px;
  line-height: 1.4;
  height: 36px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-price {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;

  .price {
    font-size: 20px;
    font-weight: bold;
    color: #ff6b6b;
  }

  .original-price {
    font-size: 14px;
    color: #999;
    text-decoration: line-through;
  }

  .discount-badge {
    background: #ff4757;
    color: white;
    font-size: 12px;
    padding: 2px 6px;
    border-radius: 4px;
    font-weight: 500;
  }
}

.supplier-info {
  background: #f8f9fa;
  padding: 8px 12px;
  border-radius: 6px;
  margin-bottom: 12px;

  .supplier-name {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 13px;
    font-weight: 500;
    color: #333;
    margin-bottom: 4px;

    .el-icon {
      color: #409eff;
    }

    .el-rate {
      margin-left: auto;
    }
  }

  .supplier-location {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 12px;
    color: #666;

    .el-icon {
      color: #909399;
    }
  }
}

.product-features {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
  flex-wrap: wrap;

  .feature-item {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 12px;
    color: #666;
    background: #f0f9ff;
    padding: 4px 8px;
    border-radius: 4px;
    border: 1px solid #e1f3fe;

    .el-icon {
      color: #409eff;
      font-size: 14px;
    }
  }
}

.product-stats {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
  margin-bottom: 12px;
  padding: 8px 0;
  border-top: 1px solid #f0f0f0;

  .stock {
    display: flex;
    align-items: center;
    gap: 4px;

    &.low-stock {
      color: #ff6b6b;
    }

    .el-icon {
      font-size: 14px;
    }
  }

  .sales {
    display: flex;
    align-items: center;
    gap: 4px;

    .el-icon {
      font-size: 14px;
    }
  }
}

.product-actions {
  display: flex;
  gap: 8px;

  .el-button {
    flex: 1;
    height: 32px;
    font-size: 13px;

    .el-icon {
      margin-right: 4px;
    }
  }
}

@media (max-width: 768px) {
  .enhanced-product-card {
    border-radius: 8px;
  }

  .product-image {
    height: 180px;
  }

  .product-info {
    padding: 12px;
  }

  .product-name {
    font-size: 14px;
    height: 38px;
  }

  .product-desc {
    font-size: 12px;
    height: 32px;
  }

  .product-price {
    .price {
      font-size: 18px;
    }

    .original-price {
      font-size: 12px;
    }
  }

  .supplier-info {
    padding: 6px 10px;
    
    .supplier-name {
      font-size: 12px;
    }

    .supplier-location {
      font-size: 11px;
    }
  }

  .product-features {
    gap: 8px;
    
    .feature-item {
      font-size: 11px;
      padding: 3px 6px;
    }
  }

  .product-actions {
    .el-button {
      font-size: 12px;
      height: 30px;
    }
  }
}
</style>