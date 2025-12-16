<template>
  <div class="orders-container">
    <div class="orders-header">
      <h2>我的订单</h2>
      <div class="order-tabs">
        <el-radio-group v-model="activeTab" @change="handleTabChange">
          <el-radio-button label="all">全部</el-radio-button>
          <el-radio-button label="pending">待付款</el-radio-button>
          <el-radio-button label="paid">待发货</el-radio-button>
          <el-radio-button label="shipped">待收货</el-radio-button>
          <el-radio-button label="completed">已完成</el-radio-button>
          <el-radio-button label="cancelled">已取消</el-radio-button>
        </el-radio-group>
      </div>
    </div>
    
    <div class="orders-content" v-loading="loading">
      <div v-if="orders.length === 0" class="empty-orders">
        <el-empty description="暂无订单" />
      </div>
      
      <div v-else class="order-list">
        <div v-for="order in orders" :key="order.id" class="order-item">
          <div class="order-header">
            <div class="order-info">
              <span>订单号：{{ order.orderNo }}</span>
              <span>下单时间：{{ formatDate(order.createdAt) }}</span>
            </div>
            <div class="order-status">
              <el-tag :type="getStatusType(order.status)">
                {{ getStatusText(order.status) }}
              </el-tag>
            </div>
          </div>
          
          <div class="order-products">
            <div v-for="item in order.items" :key="item.id" class="product-item">
              <img :src="item.productImage" :alt="item.productName" />
              <div class="product-info">
                <h4>{{ item.productName }}</h4>
                <p>{{ item.skuName }}</p>
                <p>￥{{ item.price }} × {{ item.quantity }}</p>
              </div>
            </div>
          </div>
          
          <div class="order-footer">
            <div class="order-amount">
              共{{ getTotalQuantity(order.items) }}件商品 总计：
              <span class="total-price">￥{{ order.totalAmount }}</span>
            </div>
            <div class="order-actions">
              <el-button 
                v-if="order.status === 'pending'" 
                type="primary" 
                @click="handlePay(order)"
              >
                立即付款
              </el-button>
              <el-button 
                v-if="order.status === 'shipped'" 
                type="success" 
                @click="handleConfirm(order)"
              >
                确认收货
              </el-button>
              <el-button 
                v-if="canCancel(order.status)" 
                @click="handleCancel(order)"
              >
                取消订单
              </el-button>
              <el-button @click="viewOrderDetail(order)">
                查看详情
              </el-button>
            </div>
          </div>
        </div>
      </div>
      
      <div v-if="total > 0" class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderList, cancelOrder, confirmOrder } from '@/api/order'
import type { Order } from '@/types'

const router = useRouter()

const loading = ref(false)
const activeTab = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const orders = ref<Order[]>([])

const getStatusType = (status: string): "primary" | "success" | "warning" | "info" | "danger" => {
  const statusMap: Record<string, "primary" | "success" | "warning" | "info" | "danger"> = {
    pending: 'warning',
    paid: 'info',
    shipped: 'primary',
    completed: 'success',
    cancelled: 'danger'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status: string): string => {
  const statusMap: Record<string, string> = {
    pending: '待付款',
    paid: '待发货',
    shipped: '待收货',
    completed: '已完成',
    cancelled: '已取消'
  }
  return statusMap[status] || '未知状态'
}

const formatDate = (date: string) => {
  return new Date(date).toLocaleString('zh-CN')
}

const getTotalQuantity = (items: any[]) => {
  return items.reduce((sum, item) => sum + item.quantity, 0)
}

const canCancel = (status: string) => {
  return ['pending', 'paid'].includes(status)
}

const fetchOrders = async () => {
  try {
    loading.value = true
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      status: activeTab.value === 'all' ? undefined : activeTab.value
    }
    const response = await getOrderList(params)
    orders.value = response.list
    total.value = response.total
  } catch (error) {
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  currentPage.value = 1
  fetchOrders()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  fetchOrders()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchOrders()
}

const handlePay = (order: Order) => {
  router.push({
    name: 'OrderPayment',
    query: { orderId: order.id }
  })
}

const handleConfirm = async (order: Order) => {
  try {
    await ElMessageBox.confirm('确认已收到商品？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    loading.value = true
    await confirmOrder(Number(order.id))
    ElMessage.success('确认收货成功')
    fetchOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('确认收货失败')
    }
  } finally {
    loading.value = false
  }
}

const handleCancel = async (order: Order) => {
  try {
    await ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    loading.value = true
    await cancelOrder(Number(order.id))
    ElMessage.success('订单取消成功')
    fetchOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('订单取消失败')
    }
  } finally {
    loading.value = false
  }
}

const viewOrderDetail = (order: Order) => {
  router.push({
    name: 'OrderDetail',
    params: { id: order.id }
  })
}

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped lang="scss">
.orders-container {
  padding: 20px;
}

.orders-header {
  margin-bottom: 20px;
  
  h2 {
    margin: 0 0 20px;
    font-size: 24px;
    font-weight: 500;
  }
}

.order-tabs {
  margin-bottom: 20px;
}

.empty-orders {
  padding: 60px 0;
  text-align: center;
}

.order-list {
  .order-item {
    border: 1px solid #eee;
    border-radius: 8px;
    margin-bottom: 20px;
    background: #fff;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background: #f8f9fa;
  border-bottom: 1px solid #eee;
  
  .order-info {
    display: flex;
    gap: 20px;
    font-size: 14px;
    color: #666;
  }
}

.order-products {
  padding: 20px;
  
  .product-item {
    display: flex;
    gap: 15px;
    padding: 10px 0;
    border-bottom: 1px solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
    
    img {
      width: 80px;
      height: 80px;
      object-fit: cover;
      border-radius: 4px;
    }
    
    .product-info {
      flex: 1;
      
      h4 {
        margin: 0 0 5px;
        font-size: 14px;
        font-weight: normal;
      }
      
      p {
        margin: 5px 0;
        font-size: 12px;
        color: #999;
      }
    }
  }
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-top: 1px solid #eee;
  background: #fafafa;
  
  .order-amount {
    font-size: 14px;
    color: #666;
    
    .total-price {
      color: #f56c6c;
      font-size: 18px;
      font-weight: bold;
    }
  }
  
  .order-actions {
    display: flex;
    gap: 10px;
  }
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}
</style>