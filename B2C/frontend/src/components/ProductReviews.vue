<template>
  <div class="product-reviews">
    <div class="reviews-header">
      <div class="reviews-summary">
        <div class="rating-score">
          <span class="score">{{ averageRating }}</span>
          <el-rate 
            v-model="averageRating" 
            disabled 
            show-score
            score-template="{value}分"
          />
        </div>
        <div class="rating-count">
          共{{ totalReviews }}条评价
        </div>
      </div>
      <div class="reviews-filter">
        <el-radio-group v-model="filterType" size="small">
          <el-radio-button label="all">全部</el-radio-button>
          <el-radio-button label="good">好评</el-radio-button>
          <el-radio-button label="medium">中评</el-radio-button>
          <el-radio-button label="bad">差评</el-radio-button>
          <el-radio-button label="image">有图</el-radio-button>
        </el-radio-group>
      </div>
    </div>

    <div class="reviews-list">
      <div v-for="review in filteredReviews" :key="review.id" class="review-item">
        <div class="review-user">
          <el-avatar :src="review.userAvatar" :size="40">
            {{ review.userName.charAt(0) }}
          </el-avatar>
          <div class="user-info">
            <div class="user-name">{{ review.userName }}</div>
            <div class="review-meta">
              <el-rate v-model="review.rating" disabled size="small" />
              <span class="review-date">{{ formatDate(review.createdAt) }}</span>
            </div>
          </div>
        </div>
        
        <div class="review-content">
          <div class="review-text">{{ review.content }}</div>
          <div class="review-images" v-if="review.images && review.images.length > 0">
            <div 
              v-for="(image, index) in review.images" 
              :key="index"
              class="review-image"
              @click="openImageViewer(review.images, index)"
            >
              <img :src="image" :alt="`评价图片${index + 1}`" />
            </div>
          </div>
          
          <div class="review-specs" v-if="review.specs">
            <span class="spec-item" v-for="(value, key) in review.specs" :key="key">
              {{ key }}: {{ value }}
            </span>
          </div>
        </div>
        
        <div class="review-actions">
          <el-button 
            type="text" 
            size="small"
            :class="{ 'is-liked': review.isLiked }"
            @click="toggleLike(review)"
          >
            <el-icon><Pointer /></el-icon>
            有用 ({{ review.likeCount }})
          </el-button>
          <el-button type="text" size="small" @click="replyReview()">
            <el-icon><ChatLineRound /></el-icon>
            回复
          </el-button>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div class="empty-state" v-if="!loading && filteredReviews.length === 0">
      <el-empty description="暂无评价" />
    </div>

    <!-- 加载更多 -->
    <div class="load-more" v-if="hasMore && !loading">
      <el-button type="primary" plain @click="loadMore" :loading="loadingMore">
        加载更多
      </el-button>
    </div>

    <!-- 图片查看器 -->
    <el-image-viewer
      v-if="showImageViewer"
      :url-list="currentImages"
      :initial-index="currentImageIndex"
      @close="showImageViewer = false"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
// 图标已在main.ts中全局注册，无需单独导入
import { getProductReviews, likeReview } from '@/api/product'
import { formatDate } from '@/utils'
import type { Review } from '@/types'

export interface Props {
  productId: number
}

const props = defineProps<Props>()

const loading = ref(false)
const loadingMore = ref(false)
const reviews = ref<Review[]>([])
const filterType = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showImageViewer = ref(false)
const currentImages = ref<string[]>([])
const currentImageIndex = ref(0)

const averageRating = computed(() => {
  if (reviews.value.length === 0) return 0
  const sum = reviews.value.reduce((acc: number, review: Review) => acc + review.rating, 0)
  return Math.round((sum / reviews.value.length) * 10) / 10
})

const totalReviews = computed(() => reviews.value.length)

const filteredReviews = computed(() => {
  if (filterType.value === 'all') {
    return reviews.value
  }
  
  return reviews.value.filter((review: Review) => {
    switch (filterType.value) {
      case 'good':
        return review.rating >= 4
      case 'medium':
        return review.rating === 3
      case 'bad':
        return review.rating <= 2
      case 'image':
        return review.images && review.images.length > 0
      default:
        return true
    }
  })
})

const hasMore = computed(() => reviews.value.length < total.value)

// 加载评价列表
const loadReviews = async (isLoadMore = false) => {
  try {
    if (isLoadMore) {
      loadingMore.value = true
    } else {
      loading.value = true
    }
    
    const res = await getProductReviews(props.productId, {
      page: currentPage.value,
      pageSize: pageSize.value,
      filterType: filterType.value
    } as any)
    
    if (isLoadMore) {
      reviews.value.push(...res.list)
    } else {
      reviews.value = res.list
    }
    
    total.value = res.total
  } catch (error: any) {
    ElMessage.error('加载评价失败')
    console.error('加载评价失败:', error)
  } finally {
    loading.value = false
    loadingMore.value = false
  }
}

// 加载更多
const loadMore = () => {
  currentPage.value++
  loadReviews(true)
}

// 切换筛选
const handleFilterChange = () => {
  currentPage.value = 1
  reviews.value = []
  loadReviews()
}

// 切换点赞
const toggleLike = async (review: Review) => {
  try {
    await likeReview(review.id)
    review.isLiked = !review.isLiked
    review.likeCount += review.isLiked ? 1 : -1
  } catch (error) {
    ElMessage.error('操作失败')
    console.error('点赞失败:', error)
  }
}

// 回复评价
const replyReview = () => {
  // 回复功能实现
  ElMessage.info('回复功能开发中...')
}

// 打开图片查看器
const openImageViewer = (images: string[], index: number) => {
  currentImages.value = images
  currentImageIndex.value = index
  showImageViewer.value = true
}

// 监听筛选变化
watch(filterType, handleFilterChange)

onMounted(() => {
  loadReviews()
})
</script>

<style scoped lang="scss">
.product-reviews {
  padding: 20px 0;
}

.reviews-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
  
  .reviews-summary {
    display: flex;
    align-items: center;
    gap: 30px;
    
    .rating-score {
      display: flex;
      align-items: center;
      gap: 10px;
      
      .score {
        font-size: 32px;
        font-weight: 600;
        color: #ff6b6b;
      }
    }
    
    .rating-count {
      color: #666;
      font-size: 14px;
    }
  }
}

.reviews-list {
  .review-item {
    padding: 20px 0;
    border-bottom: 1px solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
  }
  
  .review-user {
    display: flex;
    align-items: center;
    gap: 15px;
    margin-bottom: 15px;
    
    .user-info {
      flex: 1;
      
      .user-name {
        font-weight: 500;
        margin-bottom: 5px;
      }
      
      .review-meta {
        display: flex;
        align-items: center;
        gap: 15px;
        
        .review-date {
          color: #999;
          font-size: 12px;
        }
      }
    }
  }
  
  .review-content {
    margin-left: 55px;
    
    .review-text {
      line-height: 1.6;
      margin-bottom: 15px;
      color: #333;
    }
    
    .review-images {
      display: flex;
      gap: 10px;
      margin-bottom: 15px;
      
      .review-image {
        width: 100px;
        height: 100px;
        border-radius: 4px;
        overflow: hidden;
        cursor: pointer;
        border: 1px solid #eee;
        
        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
        
        &:hover {
          border-color: #409eff;
        }
      }
    }
    
    .review-specs {
      margin-bottom: 15px;
      
      .spec-item {
        display: inline-block;
        background: #f8f9fa;
        color: #666;
        padding: 4px 8px;
        border-radius: 4px;
        font-size: 12px;
        margin-right: 10px;
      }
    }
  }
  
  .review-actions {
    margin-left: 55px;
    
    .el-button {
      margin-right: 15px;
      
      &.is-liked {
        color: #ff6b6b;
      }
    }
  }
}

.empty-state {
  padding: 40px 0;
}

.load-more {
  text-align: center;
  padding: 20px 0;
}

// 响应式设计
@media (max-width: 768px) {
  .reviews-header {
    flex-direction: column;
    gap: 20px;
    align-items: flex-start;
  }
  
  .review-content,
  .review-actions {
    margin-left: 0;
  }
  
  .review-images {
    .review-image {
      width: 80px;
      height: 80px;
    }
  }
}
</style>