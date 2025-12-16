<template>
  <div class="order-payment-container">
    <div class="order-steps">
      <el-steps :active="2" align-center>
        <el-step title="确认订单" />
        <el-step title="支付订单" />
        <el-step title="完成支付" />
      </el-steps>
    </div>
    
    <div class="payment-content">
      <div class="payment-info">
        <div class="order-info">
          <h3>订单信息</h3>
          <div class="info-item">
            <span>订单号：</span>
            <span>{{ orderInfo.orderNo }}</span>
          </div>
          <div class="info-item">
            <span>订单金额：</span>
            <span class="amount">￥{{ orderInfo.amount }}</span>
          </div>
        </div>
        
        <div class="payment-method">
        <h3>支付方式</h3>
        <div class="method-selector" v-if="availablePaymentMethods.length > 1">
          <el-radio-group v-model="orderInfo.paymentMethod" @change="onPaymentMethodChange">
            <el-radio label="wechat" v-if="availablePaymentMethods.includes('wechat')">
              <el-icon :size="20"><Wallet /></el-icon>
              微信支付
            </el-radio>
            <el-radio label="alipay" v-if="availablePaymentMethods.includes('alipay')">
              <el-icon :size="20"><CreditCard /></el-icon>
              支付宝支付
            </el-radio>
            <el-radio label="mock" v-if="availablePaymentMethods.includes('mock')">
              <el-icon :size="20"><CreditCard /></el-icon>
              模拟支付
            </el-radio>
          </el-radio-group>
        </div>
        <div class="method-info" v-else>
          <el-icon :size="24">
            <component :is="paymentMethodIcon" />
          </el-icon>
          <span>{{ paymentMethodText }}</span>
        </div>
      </div>
        
        <div class="payment-countdown" v-if="countdown > 0">
          <el-icon><Clock /></el-icon>
          <span>支付剩余时间：{{ formatCountdown }}</span>
        </div>
      </div>
      
      <div class="payment-qr" v-if="qrCode">
        <h3>请使用{{ paymentMethodText }}扫描二维码支付</h3>
        <div class="qr-container">
          <img :src="qrCode" alt="支付二维码" />
        </div>
        <p class="qr-tips">支付二维码有效时间15分钟</p>
      </div>
      
      <div class="payment-actions">
        <el-button @click="goToOrderList">查看订单列表</el-button>
        <el-button type="primary" @click="checkPaymentStatus" :loading="loading">
          我已支付
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
// 图标已在main.ts中全局注册，无需单独导入
import { createPayment, checkPayment, getAvailablePaymentMethods } from '@/api/payment'
import { getOrderDetail } from '@/api/order'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const countdown = ref(900) // 15分钟倒计时
const countdownInterval = ref<number | null>(null)
const paymentPollingInterval = ref<number | null>(null)

const orderInfo = reactive({
  id: 0,
  orderNo: '',
  amount: 0,
  paymentMethod: 'wechat'
})

const availablePaymentMethods = ref<string[]>([])

const qrCode = ref('')

const paymentMethodIcon = computed(() => {
  switch (orderInfo.paymentMethod) {
    case 'wechat':
      return 'Wallet'
    case 'alipay':
      return 'CreditCard'
    case 'mock':
      return 'CreditCard'
    default:
      return 'CreditCard'
  }
})

const paymentMethodText = computed(() => {
  switch (orderInfo.paymentMethod) {
    case 'wechat':
      return '微信'
    case 'alipay':
      return '支付宝'
    case 'mock':
      return '模拟支付'
    default:
      return '未知支付'
  }
})

const formatCountdown = computed(() => {
  const minutes = Math.floor(countdown.value / 60)
  const seconds = countdown.value % 60
  return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`
})

const startCountdown = () => {
  countdownInterval.value = setInterval(() => {
    if (countdown.value > 0) {
      countdown.value--
    } else {
      stopCountdown()
      ElMessage.warning('支付超时，请重新下单')
      router.push({ name: 'Cart' })
    }
  }, 1000)
}

const stopCountdown = () => {
  if (countdownInterval.value) {
    clearInterval(countdownInterval.value)
    countdownInterval.value = null
  }
}

const fetchOrderDetail = async () => {
  try {
    loading.value = true
    const orderId = route.query.orderId as string
    if (!orderId) {
      ElMessage.error('订单ID不存在')
      router.push({ name: 'Cart' })
      return
    }
    
    const response = await getOrderDetail(parseInt(orderId))
    Object.assign(orderInfo, response)
    
    // 获取可用的支付方式
    await fetchAvailablePaymentMethods()
    
    // 创建支付二维码
    await createPaymentQr()
  } catch (error) {
    ElMessage.error('获取订单详情失败')
    router.push({ name: 'Cart' })
  } finally {
    loading.value = false
  }
}

const createPaymentQr = async () => {
  try {
    const response = await createPayment({
      orderId: orderInfo.id,
      paymentMethod: orderInfo.paymentMethod as 'wechat' | 'alipay',
      amount: orderInfo.amount
    })
    
    qrCode.value = response.data.qrCode || ''
    startCountdown()
    // 启动支付状态轮询
    startPaymentPolling()
  } catch (error) {
    ElMessage.error('生成支付二维码失败')
  }
}

const checkPaymentStatus = async () => {
  if (!orderInfo.id) return
  
  try {
    loading.value = true
    const response = await checkPayment(orderInfo.id)
    
    if (response.data.status === 'paid') {
      stopCountdown()
      stopPaymentPolling()
      ElMessage.success('支付成功！')
      router.push('/order/success')
    } else if (response.data.status === 'failed') {
      stopCountdown()
      stopPaymentPolling()
      ElMessage.error('支付失败，请重新支付')
      router.push('/order/failed')
    } else {
      ElMessage.info('订单未支付，请完成支付')
    }
  } catch (error) {
    ElMessage.error('查询支付状态失败')
  } finally {
    loading.value = false
  }
}

// 启动支付状态轮询
const startPaymentPolling = () => {
  // 先清除现有的轮询
  stopPaymentPolling()
  
  // 立即检查一次
  checkPaymentStatus()
  
  // 每3秒轮询一次
  paymentPollingInterval.value = setInterval(() => {
    checkPaymentStatus()
  }, 3000)
}

// 停止支付状态轮询
const stopPaymentPolling = () => {
  if (paymentPollingInterval.value) {
    clearInterval(paymentPollingInterval.value)
    paymentPollingInterval.value = null
  }
}

const fetchAvailablePaymentMethods = async () => {
  try {
    const response = await getAvailablePaymentMethods()
    availablePaymentMethods.value = response.data
    
    // 如果当前支付方式不在可用列表中，使用第一个可用的支付方式
    if (!availablePaymentMethods.value.includes(orderInfo.paymentMethod)) {
      orderInfo.paymentMethod = availablePaymentMethods.value[0] || 'wechat'
    }
  } catch (error) {
    console.error('获取可用支付方式失败:', error)
    // 使用默认支付方式
    availablePaymentMethods.value = ['wechat', 'alipay', 'mock']
  }
}

const onPaymentMethodChange = async () => {
  // 支付方式改变时重新生成支付二维码
  // 停止之前的轮询
  stopPaymentPolling()
  await createPaymentQr()
}

const goToOrderList = () => {
  router.push({ name: 'UserOrders' })
}

onMounted(() => {
  fetchOrderDetail()
})

onUnmounted(() => {
  stopCountdown()
  // 清除支付状态轮询
  stopPaymentPolling()
})
</script>

<style scoped lang="scss">
.order-payment-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.order-steps {
  margin-bottom: 40px;
  padding: 20px 0;
  background: #fff;
  border-radius: 8px;
}

.payment-content {
  background: #fff;
  border-radius: 8px;
  padding: 30px;
}

.payment-info {
  margin-bottom: 30px;
}

.order-info {
  margin-bottom: 25px;
  
  h3 {
    margin-bottom: 15px;
    color: #333;
    font-size: 16px;
  }
}

.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  padding: 8px 0;
  
  span:first-child {
    color: #666;
  }
  
  .amount {
    color: #ff6b35;
    font-size: 18px;
    font-weight: bold;
  }
}

.payment-method {
  margin-bottom: 25px;
  
  h3 {
    margin-bottom: 15px;
    color: #333;
    font-size: 16px;
  }
}

.method-selector {
  .el-radio-group {
    display: flex;
    flex-direction: column;
    gap: 15px;
  }
  
  .el-radio {
    display: flex;
    align-items: center;
    padding: 12px;
    border: 1px solid #e4e7ed;
    border-radius: 6px;
    transition: all 0.3s;
    
    &:hover {
      border-color: #409eff;
      background-color: #f5f7fa;
    }
    
    &.is-checked {
      border-color: #409eff;
      background-color: #ecf5ff;
    }
    
    .el-icon {
      margin-right: 8px;
      color: #409eff;
    }
  }
}

.method-info {
  display: flex;
  align-items: center;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 6px;
  
  .el-icon {
    margin-right: 10px;
    color: #409eff;
  }
  
  span {
    font-size: 14px;
    color: #333;
  }
}

.payment-countdown {
  display: flex;
  align-items: center;
  padding: 15px;
  background: #fef0f0;
  border-radius: 6px;
  color: #f56c6c;
  
  .el-icon {
    margin-right: 8px;
  }
}

.payment-qr {
  background: #fff;
  border-radius: 8px;
  padding: 40px;
  text-align: center;
}

.payment-info {
  margin-bottom: 40px;
  
  h3 {
    margin: 0 0 20px;
    font-size: 18px;
    font-weight: 500;
  }
  
  .info-item {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
    font-size: 14px;
    
    .amount {
      color: #f56c6c;
      font-size: 20px;
      font-weight: bold;
    }
  }
  
  .payment-method {
    margin: 30px 0;
    
    .method-info {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 10px;
      font-size: 16px;
    }
  }
  
  .payment-countdown {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    color: #f56c6c;
    font-size: 16px;
    font-weight: bold;
  }
}

.payment-qr {
  margin-bottom: 40px;
  
  h3 {
    margin: 0 0 20px;
    font-size: 16px;
    font-weight: normal;
  }
  
  .qr-container {
    margin: 20px 0;
    
    img {
      width: 200px;
      height: 200px;
      border: 1px solid #eee;
      border-radius: 8px;
    }
  }
  
  .qr-tips {
    color: #999;
    font-size: 14px;
    margin: 0;
  }
}

.payment-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
}

.payment-actions .el-button {
  min-width: 120px;
}

@media (max-width: 768px) {
  .payment-content {
    padding: 20px;
  }
  
  .payment-qr {
    padding: 20px;
  }
  
  .payment-qr .qr-container img {
    width: 160px;
    height: 160px;
  }
  
  .payment-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .payment-actions .el-button {
    width: 100%;
    max-width: 200px;
  }
}
</style>