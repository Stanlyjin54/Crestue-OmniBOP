<template>
  <div class="product-card" @click="goToDetail">
    <!-- 商品图片 -->
    <div class="product-image-wrapper">
      <el-image
        ref="imageRef"
        :data-src="product.mainImage"
        :alt="product.name"
        fit="cover"
        class="product-image"
        lazy
      >
        <template #placeholder>
          <div class="image-placeholder">
            <el-icon><Picture /></el-icon>
          </div>
        </template>
        <template #error>
          <div class="image-error">
            <el-icon><Picture /></el-icon>
            <span>图片加载失败</span>
          </div>
        </template>
      </el-image>
      
      <!-- 商品标签 -->
      <div class="product-tags" v-if="product.tags && product.tags.length">
        <span
          v-for="tag in product.tags.slice(0, 2)"
          :key="tag"
          class="product-tag"
          :class="`tag-${tag.toLowerCase()}`"
        >
          {{ tag }}
        </span>
      </div>
      
      <!-- 收藏按钮 -->
      <div class="favorite-btn" @click.stop="toggleFavorite">
        <el-icon :size="18" :class="{ 'is-favorite': isFavorite }">
          <StarFilled v-if="isFavorite" />
          <Star v-else />
        </el-icon>
      </div>
    </div>

    <!-- 商品信息 -->
    <div class="product-info">
      <h3 class="product-name" :title="product.name">
        {{ product.name }}
      </h3>
      
      <p class="product-desc" :title="product.description">
        {{ product.description }}
      </p>
      
      <!-- 商品规格 -->
      <div class="product-specs" v-if="product.specs && product.specs.length">
        <span
          v-for="spec in product.specs.slice(0, 2)"
          :key="spec.name"
          class="spec-item"
        >
          {{ spec.name }}: {{ spec.values.join(', ') }}
        </span>
      </div>
      
      <!-- 价格信息 -->
      <div class="product-price">
        <span class="price-current">
          <span class="price-symbol">¥</span>
          <span class="price-integer">{{ formatPrice(product.price).integer }}</span>
          <span class="price-decimal">.{{ formatPrice(product.price).decimal }}</span>
        </span>
        <span class="price-original" v-if="product.originalPrice">
          ¥{{ product.originalPrice }}
        </span>
        <span class="price-discount" v-if="getDiscount()">
          {{ getDiscount() }}折
        </span>
      </div>
      
      <!-- 商品统计 -->
      <div class="product-stats">
        <span class="stat-item">
          <el-icon><Star /></el-icon>
          {{ product.rating || 0 }}
        </span>
        <span class="stat-item">
          <el-icon><ChatDotRound /></el-icon>
          {{ product.reviewCount || 0 }}
        </span>
        <span class="stat-item">
          <el-icon><ShoppingCart /></el-icon>
          {{ product.sales || 0 }}
        </span>
      </div>
      
      <!-- 操作按钮 -->
      <div class="product-actions">
        <el-button
          type="primary"
          size="small"
          @click.stop="addToCart"
          :loading="cartLoading"
        >
          <el-icon><ShoppingCart /></el-icon>
          加入购物车
        </el-button>
        <el-button
          type="warning"
          size="small"
          @click.stop="buyNow"
          :loading="buyLoading"
        >
          立即购买
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
// 图标已在main.ts中全局注册，无需单独导入
import type { Product } from '@/types'
import { useCartStore } from '@/stores/cart'
import { useLazyLoad } from '@/utils'

export interface Props {
  product: Product
}

const props = defineProps<Props>()

const router = useRouter()
const cartStore = useCartStore()
const { observe, unobserve } = useLazyLoad()

const cartLoading = ref(false)
const buyLoading = ref(false)
const isFavorite = ref(false)
const imageRef = ref<HTMLImageElement>()

// 格式化价格
const formatPrice = (price: number) => {
  const priceStr = price.toFixed(2)
  const [integer, decimal] = priceStr.split('.')
  return {
    integer,
    decimal
  }
}

// 计算折扣
const getDiscount = () => {
  if (props.product.originalPrice && props.product.originalPrice > props.product.price) {
    const discount = Math.round((props.product.price / props.product.originalPrice) * 10 * 10) / 10
    return discount
  }
  return null
}

// 跳转到商品详情
const goToDetail = () => {
  router.push({
    name: 'ProductDetail',
    params: { id: props.product.id }
  })
}

// 切换收藏状态
const toggleFavorite = async () => {
  try {
    // 这里调用收藏API
    // await toggleFavoriteProduct(props.product.id)
    isFavorite.value = !isFavorite.value
    ElMessage.success(isFavorite.value ? '已添加到收藏' : '已取消收藏')
  } catch (error) {
    console.error('收藏操作失败:', error)
    ElMessage.error('操作失败，请稍后重试')
  }
}

// 添加到购物车
const addToCart = async () => {
  try {
    cartLoading.value = true
    await cartStore.addItem(props.product.id, 1)
    ElMessage.success('已添加到购物车')
  } catch (error) {
    console.error('添加到购物车失败:', error)
    ElMessage.error('添加失败，请稍后重试')
  } finally {
    cartLoading.value = false
  }
}

// 生命周期
onMounted(() => {
  nextTick(() => {
    const img = imageRef.value
    if (img && props.product.mainImage) {
      img.dataset.src = props.product.mainImage
      observe(img)
    }
  })
})

onUnmounted(() => {
  if (imageRef.value) {
    unobserve(imageRef.value)
  }
})

// 立即购买
const buyNow = async () => {
  try {
    buyLoading.value = true
    // 先添加到购物车
    await cartStore.addItem(props.product.id, 1)
    // 跳转到订单确认页
    router.push({
      name: 'OrderConfirm'
    })
  } catch (error) {
    console.error('立即购买失败:', error)
    ElMessage.error('操作失败，请稍后重试')
  } finally {
    buyLoading.value = false
  }
}
</script>

<style scoped lang="scss">
.product-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
    
    .product-actions {
      opacity: 1;
    }
  }
}

.product-image-wrapper {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
  
  .product-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s ease;
  }
  
  &:hover .product-image {
    transform: scale(1.05);
  }
  
  .image-placeholder,
  .image-error {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    background: #f5f7fa;
    color: #c0c4cc;
    
    .el-icon {
      font-size: 48px;
      margin-bottom: 8px;
    }
    
    span {
      font-size: 12px;
    }
  }
  
  .image-error {
    color: #f56c6c;
  }
}

.product-tags {
  position: absolute;
  top: 8px;
  left: 8px;
  display: flex;
  gap: 4px;
  
  .product-tag {
    padding: 2px 6px;
    font-size: 10px;
    font-weight: 500;
    border-radius: 4px;
    color: #fff;
    
    &.tag-new {
      background: #67c23a;
    }
    
    &.tag-hot {
      background: #f56c6c;
    }
    
    &.tag-sale {
      background: #e6a23c;
    }
    
    &.tag-recommend {
      background: #409eff;
    }
  }
}

.favorite-btn {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 32px;
  height: 32px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    background: #fff;
    transform: scale(1.1);
  }
  
  .el-icon {
    color: #c0c4cc;
    transition: color 0.3s ease;
    
    &.is-favorite {
      color: #f56c6c;
    }
  }
}

.product-info {
  padding: 16px;
}

.product-name {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-desc {
  margin: 0 0 12px;
  font-size: 13px;
  color: #606266;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-specs {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 12px;
  
  .spec-item {
    padding: 2px 6px;
    background: #f0f2f5;
    border-radius: 4px;
    font-size: 11px;
    color: #606266;
  }
}

.product-price {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  
  .price-current {
    display: flex;
    align-items: baseline;
    color: #f56c6c;
    
    .price-symbol {
      font-size: 14px;
      margin-right: 2px;
    }
    
    .price-integer {
      font-size: 20px;
      font-weight: 600;
    }
    
    .price-decimal {
      font-size: 14px;
    }
  }
  
  .price-original {
    font-size: 13px;
    color: #c0c4cc;
    text-decoration: line-through;
  }
  
  .price-discount {
    padding: 2px 4px;
    background: #fef0f0;
    color: #f56c6c;
    font-size: 11px;
    border-radius: 4px;
  }
}

.product-stats {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
  padding: 8px 0;
  border-top: 1px solid #f0f2f5;
  border-bottom: 1px solid #f0f2f5;
  
  .stat-item {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 12px;
    color: #909399;
    
    .el-icon {
      font-size: 14px;
    }
  }
}

.product-actions {
  display: flex;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.3s ease;
  
  .el-button {
    flex: 1;
    
    .el-icon {
      margin-right: 4px;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .product-card {
    .product-image-wrapper {
      height: 160px;
    }
    
    .product-info {
      padding: 12px;
    }
    
    .product-name {
      font-size: 14px;
    }
    
    .product-desc {
      font-size: 12px;
      -webkit-line-clamp: 1;
    }
    
    .product-actions {
      opacity: 1;
      
      .el-button {
        font-size: 12px;
        padding: 6px 8px;
      }
    }
  }
}

@media (max-width: 480px) {
  .product-card {
    .product-image-wrapper {
      height: 140px;
    }
    
    .product-actions {
      flex-direction: column;
      
      .el-button {
        width: 100%;
      }
    }
  }
}
</style>