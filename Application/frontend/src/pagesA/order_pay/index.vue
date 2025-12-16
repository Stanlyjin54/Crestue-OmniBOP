<template>
  <base-layout>
    <view class="p-2">
      <view class="text-2xl font-bold text-primary-700 mb-4">订单支付</view>
      
      <!-- 订单信息卡片 -->
      <view v-if="orderDetail" class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <view class="flex items-center gap-2 mb-2">
          <uni-icons type="wallet" size="24" color="#0ea5e9" />
          <text class="text-lg font-bold text-primary-700">订单编号：{{ orderDetail.order_no }}</text>
        </view>
        <view class="flex items-center gap-2 mb-2">
          <uni-icons type="person" size="20" color="#4b5563" />
          <text class="text-base">客户：{{ orderDetail.customer_name }}</text>
        </view>
        <view class="flex items-center gap-2 mb-2">
          <uni-icons type="wallet" size="20" color="#22c55e" />
          <text class="text-base">应付金额：</text>
          <text class="text-success-600 text-xl font-bold">￥{{ orderDetail.total_amount }}</text>
        </view>
        <view v-if="countdown > 0" class="flex items-center gap-2 mb-2">
          <uni-icons type="clock" size="20" color="#f59e0b" />
          <text class="text-orange-500 font-bold">支付剩余时间：{{ formatCountdown }}</text>
        </view>
      </view>
      
      <view v-else class="flex flex-col items-center justify-center h-32">
        <uni-icons type="spinner-cycle" size="32" color="#9ca3af" class="animate-spin" />
        <text class="text-neutral-400 mt-2">正在加载订单信息...</text>
      </view>
      
      <!-- 支付方式选择 -->
      <view v-if="orderDetail" class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <view class="text-lg font-bold text-gray-800 mb-3">选择支付方式</view>
        <radio-group @change="onPaymentMethodChange">
          <view class="space-y-2">
            <label
                v-for="method in availablePaymentMethods"
                :key="method.value"
                class="flex items-center justify-between p-3 rounded-lg border"
                :class="selectedPaymentMethod === method.value ? 'border-primary-500 bg-primary-50' : 'border-gray-300'"
            >
              <view class="flex items-center gap-3">
                <uni-icons :type="method.icon" size="24" :color="method.color" />
                <text class="text-gray-700">{{ method.label }}</text>
              </view>
              <radio
                  class="text-primary-500"
                  :value="method.value"
                  :checked="selectedPaymentMethod === method.value"
              />
            </label>
          </view>
        </radio-group>
      </view>
      
      <!-- 支付二维码 -->
      <view v-if="qrCode" class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <view class="text-center mb-4">
          <text class="text-lg font-bold text-gray-800">请使用{{ paymentMethodText }}扫描二维码支付</text>
        </view>
        <view class="flex justify-center mb-3">
          <image :src="qrCode" mode="aspectFit" class="w-48 h-48 border border-gray-200 rounded-lg" />
        </view>
        <view class="text-center">
          <text class="text-sm text-gray-500">支付二维码有效时间15分钟</text>
        </view>
      </view>
      
      <!-- 支付状态轮询提示 -->
      <view v-if="isPolling" class="bg-blue-50 border border-blue-200 rounded-lg p-3 mb-4">
        <view class="flex items-center gap-2">
          <uni-icons type="spinner-cycle" size="16" color="#3b82f6" class="animate-spin" />
          <text class="text-blue-700 text-sm">正在检测支付状态...</text>
        </view>
      </view>
      
      <!-- 支付按钮 -->
      <view v-if="orderDetail" class="space-y-3">
        <button
            class="w-full py-3 bg-primary-500 text-white text-center text-lg rounded-xl hover:bg-primary-600 transition-colors disabled:opacity-50"
            @click="handlePay"
            :disabled="loading"
        >
          {{ loading ? '处理中...' : '立即支付' }}
        </button>
        <button
            class="w-full py-2 text-gray-500 text-center text-base hover:text-gray-700 transition-colors"
            @click="goToOrderList"
        >
          返回订单列表
        </button>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { getCurrentInstance } from 'vue'

const { proxy } = getCurrentInstance()

// 状态变量
const orderDetail = ref(null)
const loading = ref(false)
const countdown = ref(900) // 15分钟倒计时
const selectedPaymentMethod = ref('mock')
const availablePaymentMethods = ref([
  { label: '虚拟支付', value: 'mock', icon: 'wallet', color: '#22c55e', enable: true },
  { label: '微信支付', value: 'wechat', icon: 'weixin', color: '#07c160', enable: false },
  { label: '支付宝', value: 'alipay', icon: 'zhifubao', color: '#1677ff', enable: false }
])
const qrCode = ref('')
const isPolling = ref(false)
const pollingInterval = ref(null)

// 计算属性
const paymentMethodText = computed(() => {
  const method = availablePaymentMethods.value.find(m => m.value === selectedPaymentMethod.value)
  return method ? method.label : '未知支付'
})

const formatCountdown = computed(() => {
  const minutes = Math.floor(countdown.value / 60)
  const seconds = countdown.value % 60
  return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`
})

// 生命周期
onLoad(async (option) => {
  if (option && option.order_info_id) {
    await fetchOrderDetail(option.order_info_id)
    await fetchAvailablePaymentMethods()
    startCountdown()
  }
})

onUnmounted(() => {
  stopPolling()
  stopCountdown()
})

// 方法
async function fetchOrderDetail(order_info_id) {
  try {
    const res = await proxy.$cf.table.get({ 
      table_name: 'order_info', 
      param: { order_info_id: Number(order_info_id) } 
    })
    if (res.success && res.data) {
      orderDetail.value = res.data
    } else {
      proxy.$cf.toast({ message: res.message || '订单加载失败', level: 'error' })
    }
  } catch (error) {
    proxy.$cf.toast({ message: '获取订单详情失败', level: 'error' })
  }
}

async function fetchAvailablePaymentMethods() {
  try {
    const res = await proxy.$cf.payment.payMethod()
    if (res.success && res.data) {
      availablePaymentMethods.value.forEach(method => {
        method.enable = res.data.includes(method.value)
      })
    }
  } catch (error) {
    console.error('获取支付方式失败:', error)
  }
}

function onPaymentMethodChange(e) {
  selectedPaymentMethod.value = e.detail.value
  // 停止之前的轮询
  stopPolling()
  // 清除二维码
  qrCode.value = ''
}

async function handlePay() {
  if (!orderDetail.value) return
  
  loading.value = true
  try {
    // 创建支付订单
    const payChannel = getPayChannel(selectedPaymentMethod.value)
    const requestBody = {
      totalPrice: orderDetail.value.total_amount,
      productId: orderDetail.value.order_info_id,
      userId: orderDetail.value.user_info_user_info_id_1,
      payChanel: payChannel,
      productSubject: orderDetail.value.order_no,
      tableName: 'order_info',
      fieldName: 'general_orders',
      orderNo: orderDetail.value.order_info_id
    }
    
    const res = await proxy.$cf.payment.createOrders(requestBody)
    if (!res.success) {
      proxy.$cf.toast({ message: res.message || '支付订单创建失败', level: 'error' })
      return
    }
    
    const payParam = res.data
    
    if (selectedPaymentMethod.value === 'mock') {
      // 模拟支付
      await handleMockPayment(payParam.paymentOrderId)
    } else if (selectedPaymentMethod.value === 'wechat') {
      // 微信支付
      await handleWechatPayment(payParam)
    } else if (selectedPaymentMethod.value === 'alipay') {
      // 支付宝支付
      await handleAlipayPayment(payParam)
    }
    
  } catch (error) {
    proxy.$cf.toast({ message: '支付处理失败', level: 'error' })
    console.error('支付错误:', error)
  } finally {
    loading.value = false
  }
}

function getPayChannel(method) {
  // #ifdef MP-WEIXIN
  if (method === 'wechat') return 'wx_jsapi'
  // #endif
  // #ifdef H5
  if (method === 'wechat') return 'wx_jsapi_mp'
  // #endif
  return method
}

async function handleMockPayment(paymentOrderId) {
  uni.showLoading({ title: '支付中', mask: true })
  try {
    await proxy.$cf.payment.payCallback('mock', paymentOrderId)
    uni.hideLoading()
    paySuccess(paymentOrderId)
  } catch (error) {
    uni.hideLoading()
    proxy.$cf.toast({ message: '支付失败', level: 'error' })
  }
}

async function handleWechatPayment(payParam) {
  // #ifdef MP-WEIXIN
  uni.requestPayment({
    provider: 'wxpay',
    ...payParam,
    success: (res) => {
      paySuccess(payParam.paymentOrderId)
    },
    fail: (err) => {
      proxy.$cf.toast({ message: '支付失败', level: 'error' })
      console.error('微信支付失败:', err)
    }
  })
  // #endif
  
  // #ifdef H5
  if (isWeixinBrowser()) {
    // 微信公众号支付
    callWeChatPay(payParam)
  } else {
    // 显示支付二维码
    qrCode.value = payParam.qrCode
    startPaymentPolling(payParam.paymentOrderId)
  }
  // #endif
}

async function handleAlipayPayment(payParam) {
  // 显示支付二维码
  qrCode.value = payParam.qrCode
  startPaymentPolling(payParam.paymentOrderId)
}

function callWeChatPay(param) {
  function onBridgeReady() {
    WeixinJSBridge.invoke('getBrandWCPayRequest', param, function(res) {
      if (res.err_msg === "get_brand_wcpay_request:ok") {
        paySuccess(param.paymentOrderId)
      } else {
        proxy.$cf.toast({ message: '支付失败', level: 'error' })
      }
    })
  }
  
  if (typeof WeixinJSBridge === "undefined") {
    document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false)
  } else {
    onBridgeReady()
  }
}

function isWeixinBrowser() {
  return typeof window !== 'undefined' && 
         window.navigator.userAgent.toLowerCase().includes('micromessenger')
}

function startPaymentPolling(paymentOrderId) {
  isPolling.value = true
  // 立即检查一次
  checkPaymentStatus(paymentOrderId)
  // 每3秒轮询一次
  pollingInterval.value = setInterval(() => {
    checkPaymentStatus(paymentOrderId)
  }, 3000)
}

async function checkPaymentStatus(paymentOrderId) {
  try {
    const res = await proxy.$cf.payment.checkPaymentStatus(paymentOrderId)
    if (res.success && res.data) {
      if (res.data.status === 'paid') {
        stopPolling()
        paySuccess(paymentOrderId)
      } else if (res.data.status === 'failed') {
        stopPolling()
        proxy.$cf.toast({ message: '支付失败', level: 'error' })
      }
    }
  } catch (error) {
    console.error('检查支付状态失败:', error)
  }
}

function stopPolling() {
  isPolling.value = false
  if (pollingInterval.value) {
    clearInterval(pollingInterval.value)
    pollingInterval.value = null
  }
}

function startCountdown() {
  const interval = setInterval(() => {
    if (countdown.value > 0) {
      countdown.value--
    } else {
      stopCountdown()
      proxy.$cf.toast({ message: '支付超时，请重新下单', level: 'warning' })
      setTimeout(() => {
        goToOrderList()
      }, 2000)
    }
  }, 1000)
  
  // 保存到全局以便清理
  proxy.$countdownInterval = interval
}

function stopCountdown() {
  if (proxy.$countdownInterval) {
    clearInterval(proxy.$countdownInterval)
    proxy.$countdownInterval = null
  }
}

function paySuccess(paymentOrderId) {
  proxy.$cf.toast({ message: '支付成功，正在跳转', level: 'success' })
  setTimeout(() => {
    proxy.$cf.navigate.to({ 
      url: `/pagesA/order_detail/index?order_info_id=${orderDetail.value.order_info_id}`, 
      type: 'page' 
    })
  }, 1500)
}

function goToOrderList() {
  proxy.$cf.navigate.to({ 
    url: '/pagesA/order_list/index', 
    type: 'page' 
  })
}
</script>

<style scoped>
/* 页面样式已在全局样式中定义 */
</style>