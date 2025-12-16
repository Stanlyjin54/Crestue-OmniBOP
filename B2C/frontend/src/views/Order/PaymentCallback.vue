<template>
  <div class="payment-callback-container">
    <div class="callback-status">
      <el-result
        :icon="resultIcon"
        :title="resultTitle"
        :sub-title="resultSubtitle"
      >
        <template #extra>
          <el-button type="primary" @click="goToOrderDetail">查看订单详情</el-button>
          <el-button @click="goToOrderList">返回订单列表</el-button>
        </template>
      </el-result>
    </div>
    
    <div class="callback-details" v-if="callbackData">
      <el-card>
        <template #header>
          <span>回调详情</span>
        </template>
        <div class="detail-item">
          <span class="label">订单号：</span>
          <span class="value">{{ callbackData.orderNo }}</span>
        </div>
        <div class="detail-item">
          <span class="label">支付状态：</span>
          <el-tag :type="getStatusType(callbackData.status)">
            {{ getStatusText(callbackData.status) }}
          </el-tag>
        </div>
        <div class="detail-item" v-if="callbackData.paymentTime">
          <span class="label">支付时间：</span>
          <span class="value">{{ formatDate(callbackData.paymentTime) }}</span>
        </div>
        <div class="detail-item" v-if="callbackData.transactionId">
          <span class="label">交易号：</span>
          <span class="value">{{ callbackData.transactionId }}</span>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { handlePaymentCallback } from '@/api/payment'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const callbackData = ref<any>(null)
const resultIcon = ref<'success' | 'warning' | 'error'>('success')
const resultTitle = ref('')
const resultSubtitle = ref('')

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString('zh-CN')
}

const getStatusType = (status: string) => {
  switch (status) {
    case 'paid':
      return 'success'
    case 'failed':
      return 'danger'
    case 'pending':
      return 'warning'
    default:
      return 'info'
  }
}

const getStatusText = (status: string) => {
  switch (status) {
    case 'paid':
      return '支付成功'
    case 'failed':
      return '支付失败'
    case 'pending':
      return '待支付'
    default:
      return '未知状态'
  }
}

const processCallback = async () => {
  try {
    loading.value = true
    
    // 获取回调参数
    const callbackParams = {
      ...route.query,
      ...route.params
    }
    
    // 处理支付回调
    const response = await handlePaymentCallback(callbackParams)
    
    if (response.data.success) {
      callbackData.value = response.data
      resultIcon.value = 'success'
      resultTitle.value = '支付成功'
      resultSubtitle.value = '您的订单支付已完成，我们将尽快为您处理订单'
      
      ElMessage.success('支付成功！')
    } else {
      callbackData.value = response.data
      resultIcon.value = 'error'
      resultTitle.value = '支付失败'
      resultSubtitle.value = '支付过程中出现错误，请重新支付或联系客服'
      
      ElMessage.error('支付失败')
    }
  } catch (error) {
    console.error('处理支付回调失败:', error)
    resultIcon.value = 'warning'
    resultTitle.value = '处理异常'
    resultSubtitle.value = '支付回调处理出现异常，请联系客服处理'
    
    ElMessage.error('处理支付回调失败')
  } finally {
    loading.value = false
  }
}

const goToOrderDetail = () => {
  if (callbackData.value?.orderId) {
    router.push(`/order/detail/${callbackData.value.orderId}`)
  } else {
    router.push('/order/list')
  }
}

const goToOrderList = () => {
  router.push('/order/list')
}

onMounted(() => {
  processCallback()
})
</script>

<style scoped lang="scss">
.payment-callback-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
}

.callback-status {
  margin-bottom: 30px;
}

.callback-details {
  .detail-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .label {
      color: #666;
      font-weight: 500;
    }
    
    .value {
      color: #333;
      font-weight: bold;
    }
  }
}
</style>