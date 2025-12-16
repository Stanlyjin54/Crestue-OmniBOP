<template>
  <div class="product-list-item">
    <div class="list-item-content">
      <!-- 商品图片 -->
      <div class="product-image">
        <img :src="product.mainImage || product.image" :alt="product.name" />
        <div class="image-overlay">
          <el-button type="primary" size="small" @click="goToDetail">
            查看详情
          </el-button>
        </div>
      </div>
      
      <!-- 商品信息 -->
      <div class="product-info">
        <h3 class="product-name">{{ product.name }}</h3>
        <p class="product-desc">{{ product.description }}</p>
        
        <!-- 供应商信息 -->
        <div v-if="product.supplier" class="supplier-section">
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

        <!-- 产品特色 -->
        <div v-if="hasFeatures" class="features-section">
          <div class="feature-tags">
            <el-tag v-if="product.minOrderQuantity && product.minOrderQuantity > 1" size="small" type="info">
              起订{{ product.minOrderQuantity }}件
            </el-tag>
            <el-tag v-if="product.leadTime" size="small" type="warning">
              {{ product.leadTime }}
            </el-tag>
            <el-tag v-if="product.certifications && product.certifications.length" size="small" type="success">
              已认证
            </el-tag>
          </div>
        </div>
        
        <!-- 价格和库存信息 -->
        <div class="product-meta">
          <div class="price-section">
            <span class="current-price">¥{{ formatPrice(product.price) }}</span>
            <span v-if="product.originalPrice && product.originalPrice > product.price" 
                  class="original-price">¥{{ formatPrice(product.originalPrice) }}</span>
            <span v-if="getDiscountPercent()" class="discount-tag">
              {{ getDiscountPercent() }}折
            </span>
          </div>
          
          <div class="stock-sales">
            <span class="stock" :class="{ 'low-stock': product.stock < 10 }">
              库存: {{ product.stock }}
            </span>
            <span class="sales">
              销量: {{ product.sales || 0 }}
            </span>
          </div>
        </div>
      </div>
      
      <!-- 操作按钮 -->
      <div class="product-actions">
        <el-button type="primary" @click="addToCart" :disabled="!canAddToCart">
          <el-icon><ShoppingCart /></el-icon>
          加入购物车
        </el-button>
        <el-button @click="addToFavorites">
          <el-icon><Star /></el-icon>
          收藏
        </el-button>
        <el-button @click="compareProduct">
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

const hasFeatures = computed(() => {
  return (props.product.minOrderQuantity && props.product.minOrderQuantity > 1) ||
         props.product.leadTime ||
         (props.product.certifications && props.product.certifications.length > 0)
})

const formatPrice = (price: number) => {
  return price?.toFixed(2) || '0.00'
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
  ElMessage.success('已添加到收藏夹')
}

const compareProduct = () => {
  emit('compare', props.product)
  ElMessage.info('已添加到对比列表')
}
</script>

<style scoped lang="scss">
.product-list-item {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  border: 1px solid #f0f0f0;

  &:hover {
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.12);
    transform: translateY(-2px);
    border-color: #e6f7ff;
  }

  .list-item-content {
    display: flex;
    gap: 24px;
    align-items: stretch;
  }
}

.product-image {
  position: relative;
  flex-shrink: 0;
  width: 140px;
  height: 140px;
  border-radius: 12px;
  overflow: hidden;
  background: #f8f9fa;
  border: 1px solid #f0f0f0;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s ease;
  }

  &:hover img {
    transform: scale(1.05);
  }

  .image-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: opacity 0.3s ease;
  }

  &:hover .image-overlay {
    opacity: 1;
  }
}

.product-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.product-name {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0 0 12px;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-desc {
  font-size: 14px;
  color: #666;
  margin: 0 0 16px;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  height: 42px;
}

.supplier-section {
  background: #f8f9fa;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 12px;
  border: 1px solid #e9ecef;

  .supplier-name {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    font-weight: 500;
    color: #333;
    margin-bottom: 6px;

    .el-icon {
      color: #409eff;
      font-size: 16px;
    }

    .el-rate {
      margin-left: auto;
    }
  }

  .supplier-location {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 13px;
    color: #666;

    .el-icon {
      color: #909399;
      font-size: 14px;
    }
  }
}

.features-section {
  margin-bottom: 12px;

  .feature-tags {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
  }
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  flex-wrap: wrap;
  gap: 16px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.price-section {
  display: flex;
  align-items: center;
  gap: 12px;

  .current-price {
    font-size: 24px;
    font-weight: bold;
    color: #ff6b6b;
  }

  .original-price {
    font-size: 16px;
    color: #999;
    text-decoration: line-through;
  }

  .discount-tag {
    background: #ff4757;
    color: white;
    font-size: 12px;
    padding: 2px 6px;
    border-radius: 4px;
    font-weight: 500;
  }
}

.stock-sales {
  display: flex;
  gap: 16px;
  font-size: 13px;

  .stock {
    color: #666;

    &.low-stock {
      color: #ff6b6b;
      font-weight: 500;
    }
  }

  .sales {
    color: #999;
  }
}

.product-actions {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
  align-items: flex-end;

  .el-button {
    min-width: 140px;
    height: 40px;
    font-size: 14px;

    .el-icon {
      margin-right: 6px;
    }
  }
}

@media (max-width: 1024px) {
  .product-list-item {
    padding: 20px;

    .list-item-content {
      gap: 20px;
    }

    .product-image {
      width: 120px;
      height: 120px;
    }

    .product-name {
      font-size: 18px;
    }

    .product-actions {
      .el-button {
        min-width: 120px;
        height: 36px;
        font-size: 13px;
      }
    }
  }
}

@media (max-width: 768px) {
  .product-list-item {
    padding: 16px;

    .list-item-content {
      flex-direction: column;
      gap: 16px;
    }

    .product-image {
      width: 100%;
      height: 200px;
      align-self: center;
    }

    .product-info {
      .product-name {
        font-size: 16px;
      }

      .product-desc {
        font-size: 13px;
        height: 36px;
      }
    }

    .product-meta {
      flex-direction: column;
      align-items: flex-start;
      gap: 12px;
    }

    .product-actions {
      width: 100%;
      flex-direction: row;

      .el-button {
        flex: 1;
        min-width: auto;
        height: 36px;
      }
    }
  }
}

@media (max-width: 480px) {
  .product-list-item {
    padding: 12px;

    .product-image {
      height: 160px;
    }

    .supplier-section {
      padding: 10px;
    }

    .price-section {
      .current-price {
        font-size: 20px;
      }

      .original-price {
        font-size: 14px;
      }
    }

    .stock-sales {
      font-size: 12px;
      gap: 12px;
    }
  }
}
</style>