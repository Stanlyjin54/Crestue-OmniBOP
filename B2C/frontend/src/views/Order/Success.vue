<template>
  <div class="order-success-container">
    <div class="success-content">
      <div class="success-icon">
        <el-icon color="#67c23a" :size="64">
          <CircleCheck />
        </el-icon>
      </div>
      
      <h2 class="success-title">订单支付成功！</h2>
      
      <div class="success-info">
        <div class="info-item">
          <span>订单号：</span>
          <span>{{ orderInfo.orderNo }}</span>
        </div>
        <div class="info-item">
          <span>支付金额：</span>
          <span class="amount">￥{{ orderInfo.amount }}</span>
        </div>
        <div class="info-item">
          <span>支付方式：</span>
          <span>{{ orderInfo.paymentMethod }}</span>
        </div>
        <div class="info-item">
          <span>支付时间：</span>
          <span>{{ orderInfo.paymentTime }}</span>
        </div>
      </div>
      
      <div class="success-actions">
        <el-button type="primary" @click="goToOrderDetail">
          查看订单详情
        </el-button>
        <el-button @click="goToProductList">
          继续购物
        </el-button>
      </div>
    </div>
    
    <div class="recommend-products" v-if="recommendProducts.length > 0">
      <h3>为您推荐</h3>
      <div class="products-grid">
        <div 
          v-for="product in recommendProducts" 
          :key="product.id"
          class="product-card"
          @click="goToProduct(product.id)"
        >
          <img :src="product.image" :alt="product.name" />
          <div class="product-info">
            <h4>{{ product.name }}</h4>
            <div class="product-price">￥{{ product.price }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
// 图标已在main.ts中全局注册，无需单独导入
import { getOrderDetail } from '@/api/order'
import { getProductList } from '@/api/product'

const router = useRouter()
const route = useRoute()

const loading = ref(false)

const orderInfo = reactive({
  id: 0,
  orderNo: '',
  amount: 0,
  paymentMethod: '',
  paymentTime: ''
})

const recommendProducts = ref<any[]>([])

const fetchOrderDetail = async () => {
  try {
    loading.value = true
    const orderId = route.query.orderId as string
    if (!orderId) {
      ElMessage.error('订单ID不存在')
      router.push({ name: 'Home' })
      return
    }
    
    const response = await getOrderDetail(parseInt(orderId))
    Object.assign(orderInfo, {
      id: response.id,
      orderNo: response.orderNo,
      amount: response.totalAmount,
      paymentMethod: response.paymentMethod === 'wechat' ? '微信支付' : '支付宝',
      paymentTime: new Date(response.paymentTime).toLocaleString('zh-CN')
    })
    
    // 获取推荐商品
    await fetchRecommendProducts()
  } catch (error) {
    ElMessage.error('获取订单详情失败')
    router.push({ name: 'Home' })
  } finally {
    loading.value = false
  }
}

const fetchRecommendProducts = async () => {
  try {
    const response = await getProductList({
      page: 1,
      pageSize: 4
    })
    recommendProducts.value = response.list
  } catch (error) {
    console.error('获取推荐商品失败', error)
  }
}

const goToOrderDetail = () => {
  router.push({
    name: 'OrderDetail',
    params: { id: orderInfo.id }
  })
}

const goToProductList = () => {
  router.push({ name: 'Home' })
}

const goToProduct = (productId: number) => {
  router.push({
    name: 'ProductDetail',
    params: { id: productId }
  })
}

onMounted(() => {
  fetchOrderDetail()
})
</script>

<style scoped lang="scss">
.order-success-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.success-content {
  background: #fff;
  border-radius: 8px;
  padding: 60px 40px;
  text-align: center;
  margin-bottom: 40px;
}

.success-icon {
  margin-bottom: 20px;
}

.success-title {
  margin: 0 0 30px;
  font-size: 28px;
  font-weight: 500;
  color: #67c23a;
}

.success-info {
  margin-bottom: 40px;
  
  .info-item {
    display: flex;
    justify-content: center;
    margin-bottom: 15px;
    font-size: 16px;
    
    span:first-child {
      color: #666;
      margin-right: 10px;
    }
    
    .amount {
      color: #f56c6c;
      font-weight: bold;
      font-size: 20px;
    }
  }
}

.success-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.recommend-products {
  background: #fff;
  border-radius: 8px;
  padding: 30px;
  
  h3 {
    margin: 0 0 20px;
    font-size: 20px;
    font-weight: 500;
    text-align: center;
  }
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.product-card {
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }
  
  img {
    width: 100%;
    height: 200px;
    object-fit: cover;
  }
  
  .product-info {
    padding: 15px;
    
    h4 {
      margin: 0 0 10px;
      font-size: 14px;
      font-weight: normal;
      height: 40px;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }
    
    .product-price {
      color: #f56c6c;
      font-size: 18px;
      font-weight: bold;
    }
  }
}
</style>