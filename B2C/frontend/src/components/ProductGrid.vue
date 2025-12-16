<template>
  <div class="product-grid">
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="3" animated />
    </div>
    
    <div v-else-if="!products || products.length === 0" class="empty-container">
      <el-empty description="暂无商品" />
    </div>
    
    <div v-else class="products-container">
      <div 
        v-for="product in products" 
        :key="product.id"
        class="product-card"
        @click="handleProductClick(product)"
      >
        <div class="product-image">
          <img 
            v-lazy="product.images[0]" 
            :alt="product.name"
            @error="handleImageError"
          />
          <div v-if="product.originalPrice" class="discount-badge">
            {{ calculateDiscount(product.price, product.originalPrice) }}折
          </div>
        </div>
        
        <div class="product-info">
          <h4 class="product-name">{{ product.name }}</h4>
          <p class="product-desc">{{ product.description }}</p>
          
          <div class="product-price">
            <span class="price">¥{{ product.price }}</span>
            <span v-if="product.originalPrice" class="original-price">
              ¥{{ product.originalPrice }}
            </span>
          </div>
          
          <div class="product-stats">
            <span class="sales">已售{{ formatQuantity(product.sales) }}</span>
            <el-rate
              v-model="product.rating"
              disabled
              size="small"
              show-score
              :score-template="`${product.reviewCount}条评价`"
            />
          </div>
          
          <div class="product-actions">
            <el-button 
              type="primary" 
              size="small"
              @click.stop="handleAddToCart(product)"
              :disabled="product.stock === 0"
            >
              <el-icon><ShoppingCart /></el-icon>
              加入购物车
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus'
// 图标已在main.ts中全局注册，无需单独导入
import type { Product } from '@/types'
import { useCartStore } from '@/stores/cart'
import { formatQuantity } from '@/utils'

export interface Props {
  products: Product[]
  loading?: boolean
}

export interface Emits {
  (e: 'product-click', product: Product): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()
const cartStore = useCartStore()

// 计算折扣
const calculateDiscount = (price: number, originalPrice: number): string => {
  const discount = (price / originalPrice * 10).toFixed(1)
  return discount
}

// 处理商品点击
const handleProductClick = (product: Product) => {
  emit('product-click', product)
}

// 处理添加到购物车
const handleAddToCart = async (product: Product) => {
  try {
    await cartStore.addItem(product.id, 1)
    ElMessage.success('添加购物车成功')
  } catch (error) {
    ElMessage.error('添加购物车失败')
  }
}

// 处理图片加载错误
const handleImageError = (event: Event) => {
  const img = event.target as HTMLImageElement
  img.src = '/images/placeholder.png'
}
</script>

<style scoped lang="scss">
.product-grid {
  width: 100%;
}

.loading-container {
  padding: 40px 20px;
}

.empty-container {
  padding: 60px 20px;
}

.products-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  
  @media (max-width: 768px) {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: 12px;
  }
}

.product-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
  cursor: pointer;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0,0,0,0.15);
  }
  
  .product-image {
    position: relative;
    width: 100%;
    height: 280px;
    overflow: hidden;
    
    @media (max-width: 768px) {
      height: 160px;
    }
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }
    
    &:hover img {
      transform: scale(1.05);
    }
    
    .discount-badge {
      position: absolute;
      top: 12px;
      right: 12px;
      background: #ef4444;
      color: white;
      padding: 4px 8px;
      border-radius: 4px;
      font-size: 12px;
      font-weight: bold;
    }
  }
  
  .product-info {
    padding: 16px;
    
    .product-name {
      font-size: 16px;
      font-weight: 500;
      color: #1e293b;
      margin-bottom: 8px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    
    .product-desc {
      font-size: 14px;
      color: #64748b;
      margin-bottom: 12px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    
    .product-price {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 12px;
      
      .price {
        font-size: 18px;
        font-weight: bold;
        color: #ef4444;
      }
      
      .original-price {
        font-size: 14px;
        color: #94a3b8;
        text-decoration: line-through;
      }
    }
    
    .product-stats {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
      
      .sales {
        font-size: 12px;
        color: #64748b;
      }
      
      :deep(.el-rate) {
        font-size: 12px;
        
        .el-rate__text {
          font-size: 12px;
          color: #64748b;
          margin-left: 4px;
        }
      }
    }
    
    .product-actions {
      .el-button {
        width: 100%;
        
        &.is-disabled {
          background-color: #f1f5f9;
          border-color: #e2e8f0;
          color: #94a3b8;
        }
      }
    }
  }
}
</style>