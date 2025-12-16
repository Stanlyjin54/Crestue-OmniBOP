<template>
  <div class="order-confirm">
    <div class="container">
      <div class="page-header">
        <h1>确认订单</h1>
        <div class="back-cart" @click="goToCart">
          <el-icon><ShoppingCart /></el-icon>
          返回购物车
        </div>
      </div>

      <div class="order-content">
        <!-- 收货地址 -->
        <div class="section">
          <h2>收货地址</h2>
          <div class="address-list">
            <div 
              v-for="address in addresses" 
              :key="address.id"
              class="address-item"
              :class="{ active: selectedAddress?.id === address.id }"
              @click="selectedAddress = address"
            >
              <div class="address-check">
                <el-icon v-if="selectedAddress?.id === address.id"><Check /></el-icon>
              </div>
              <div class="address-info">
                <div class="name-phone">
                  <span class="name">{{ address.name }}</span>
                  <span class="phone">{{ address.phone }}</span>
                </div>
                <div class="address-detail">
                  {{ address.province }} {{ address.city }} {{ address.district }} {{ address.detail }}
                </div>
              </div>
            </div>
            
            <div class="add-address" @click="showAddressDialog = true">
              <el-icon><Plus /></el-icon>
              添加新地址
            </div>
          </div>
        </div>

        <!-- 商品列表 -->
        <div class="section">
          <h2>商品清单</h2>
          <div class="product-list">
            <div v-for="item in orderItems" :key="item.id" class="product-item">
              <img :src="item.image" :alt="item.name" />
              <div class="product-info">
                <h4>{{ item.name }}</h4>
                <p>{{ item.specification }}</p>
              </div>
              <div class="product-price">
                <span class="price">¥{{ item.price.toFixed(2) }}</span>
                <span class="quantity">x{{ item.quantity }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 支付方式 -->
        <div class="section">
          <h2>支付方式</h2>
          <div class="payment-methods">
            <div 
              v-for="method in paymentMethods"
              :key="method.value"
              class="payment-method"
              :class="{ active: selectedPayment === method.value }"
              @click="selectedPayment = method.value"
            >
              <el-icon class="method-icon">
                <component :is="method.icon" />
              </el-icon>
              <span>{{ method.label }}</span>
              <el-icon v-if="selectedPayment === method.value" class="method-check">
                <Check />
              </el-icon>
            </div>
          </div>
        </div>

        <!-- 订单摘要 -->
        <div class="section summary-section">
          <div class="summary-item">
            <span>商品总价</span>
            <span>¥{{ totalAmount.toFixed(2) }}</span>
          </div>
          <div class="summary-item">
            <span>运费</span>
            <span>{{ shippingFee > 0 ? `¥${shippingFee.toFixed(2)}` : '免运费' }}</span>
          </div>
          <div class="summary-item total">
            <span>实付款</span>
            <span class="total-amount">¥{{ finalAmount.toFixed(2) }}</span>
          </div>
        </div>
      </div>

      <!-- 提交按钮 -->
      <div class="order-footer">
        <div class="back-cart" @click="goToCart">
          <el-icon><ArrowLeft /></el-icon>
          返回购物车
        </div>
        <el-button 
          type="primary" 
          size="large"
          :loading="submitLoading"
          @click="submitOrder"
        >
          提交订单 ¥{{ finalAmount.toFixed(2) }}
        </el-button>
      </div>
    </div>

    <!-- 添加地址对话框 -->
    <el-dialog
      v-model="showAddressDialog"
      title="添加新地址"
      width="500px"
    >
      <el-form
        ref="addressFormRef"
        :model="addressForm"
        :rules="addressRules"
        label-width="80px"
      >
        <el-form-item label="收货人" prop="name">
          <el-input v-model="addressForm.name" placeholder="请输入收货人姓名" />
        </el-form-item>
        
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="addressForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        
        <el-form-item label="所在地区" prop="region">
          <el-cascader
            v-model="addressForm.region"
            :options="regionOptions"
            placeholder="请选择省市区"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="详细地址" prop="detail">
          <el-input
            v-model="addressForm.detail"
            type="textarea"
            :rows="3"
            placeholder="请输入详细地址"
          />
        </el-form-item>
        
        <el-form-item label="邮政编码" prop="postalCode">
          <el-input v-model="addressForm.postalCode" placeholder="请输入邮政编码" />
        </el-form-item>
        
        <el-form-item>
          <el-checkbox v-model="addressForm.isDefault">设为默认地址</el-checkbox>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddressDialog = false">取消</el-button>
          <el-button 
            type="primary" 
            :loading="addressSubmitLoading"
            @click="submitAddress"
          >
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  ShoppingCart, 
  Plus, 
  Check, 
  ArrowLeft,
  Wallet,
  CreditCard
} from '@element-plus/icons-vue'
import { getAddressList, addAddress } from '@/api/address'
import { createOrder } from '@/api/order'
import { useCartStore } from '@/stores/cart'
import type { Address, CartItem } from '@/types'

defineOptions({
  name: 'OrderConfirm'
})

const router = useRouter()
const cartStore = useCartStore()

// 响应式状态
const loading = ref(false)
const submitLoading = ref(false)
const showAddressDialog = ref(false)
const addressSubmitLoading = ref(false)

const addresses = ref<Address[]>([])
const selectedAddress = ref<Address | null>(null)
const selectedPayment = ref('wechat')

const orderItems = ref<CartItem[]>([])

// 支付方式选项
const paymentMethods = [
  { value: 'wechat', label: '微信支付', icon: 'Wallet' },
  { value: 'alipay', label: '支付宝', icon: 'CreditCard' }
]

// 地址表单
const addressFormRef = ref()
const addressForm = reactive({
  name: '',
  phone: '',
  region: [] as string[],
  province: '',
  city: '',
  district: '',
  detail: '',
  postalCode: '',
  isDefault: false
})

// 地址表单验证规则
const addressRules = {
  name: [
    { required: true, message: '请输入收货人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度为2-20个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  region: [
    { required: true, message: '请选择省市区', trigger: 'change' }
  ],
  detail: [
    { required: true, message: '请输入详细地址', trigger: 'blur' },
    { min: 5, max: 200, message: '详细地址长度为5-200个字符', trigger: 'blur' }
  ],
  postalCode: [
    { pattern: /^\d{6}$/, message: '请输入正确的邮政编码', trigger: 'blur' }
  ]
}

// 模拟地区数据
const regionOptions = [
  {
    code: '110000',
    name: '北京市',
    children: [
      {
        code: '110100',
        name: '北京市',
        children: [
          { code: '110101', name: '东城区' },
          { code: '110102', name: '西城区' },
          { code: '110105', name: '朝阳区' },
          { code: '110106', name: '丰台区' }
        ]
      }
    ]
  },
  {
    code: '310000',
    name: '上海市',
    children: [
      {
        code: '310100',
        name: '上海市',
        children: [
          { code: '310101', name: '黄浦区' },
          { code: '310104', name: '徐汇区' },
          { code: '310105', name: '长宁区' }
        ]
      }
    ]
  }
]

// 计算属性
const totalAmount = computed(() => {
  return orderItems.value.reduce((sum, item) => sum + (item.price * item.quantity), 0)
})

const shippingFee = computed(() => {
  return totalAmount.value >= 99 ? 0 : 10
})

const finalAmount = computed(() => {
  return totalAmount.value + shippingFee.value
})

// 方法
const fetchAddresses = async () => {
  try {
    loading.value = true
    const response = await getAddressList()
    addresses.value = response.data
    
    // 设置默认地址
    const defaultAddress = addresses.value.find(addr => addr.isDefault)
    if (defaultAddress) {
      selectedAddress.value = defaultAddress
    } else if (addresses.value.length > 0) {
      selectedAddress.value = addresses.value[0]
    }
  } catch (error) {
    ElMessage.error('获取地址列表失败')
  } finally {
    loading.value = false
  }
}

const goToCart = () => {
  router.push({ name: 'Cart' })
}

const submitOrder = async () => {
  if (!selectedAddress.value) {
    ElMessage.warning('请选择收货地址')
    return
  }
  
  if (!selectedPayment.value) {
    ElMessage.warning('请选择支付方式')
    return
  }
  
  try {
    submitLoading.value = true
    
    const orderData = {
      addressId: selectedAddress.value.id,
      paymentMethod: selectedPayment.value,
      items: orderItems.value.map(item => ({
        productId: item.productId,
        skuId: item.skuId,
        quantity: item.quantity,
        price: item.price
      }))
    }
    
    const response = await createOrder(orderData)
    const orderId = response.data.id
    
    // 清空购物车
    await cartStore.clearCart()
    
    // 跳转到支付页面
    router.push({
      name: 'Payment',
      query: { orderId, amount: finalAmount.value }
    })
  } catch (error) {
    ElMessage.error('提交订单失败')
  } finally {
    submitLoading.value = false
  }
}

const submitAddress = async () => {
  try {
    await addressFormRef.value.validate()
    addressSubmitLoading.value = true
    
    // 处理地区数据
    const [provinceCode, cityCode, districtCode] = addressForm.region
    const province = regionOptions.find(p => p.code === provinceCode)?.name || ''
    const city = regionOptions.find(p => p.code === provinceCode)
      ?.children?.find(c => c.code === cityCode)?.name || ''
    const district = regionOptions.find(p => p.code === provinceCode)
      ?.children?.find(c => c.code === cityCode)
      ?.children?.find(d => d.code === districtCode)?.name || ''
    
    const data = {
      ...addressForm,
      province,
      city,
      district
    }
    
    await addAddress(data)
    ElMessage.success('添加地址成功')
    showAddressDialog.value = false
    resetAddressForm()
    await fetchAddresses()
  } catch (error) {
    ElMessage.error('添加地址失败')
  } finally {
    addressSubmitLoading.value = false
  }
}

const resetAddressForm = () => {
  Object.assign(addressForm, {
    name: '',
    phone: '',
    region: [],
    province: '',
    city: '',
    district: '',
    detail: '',
    postalCode: '',
    isDefault: false
  })
}

// 生命周期
onMounted(async () => {
  await fetchAddresses()
  
  // 加载购物车数据
  try {
    const cartItems = await cartStore.fetchCart()
    orderItems.value = cartItems
  } catch (error) {
    ElMessage.error('加载购物车失败')
  }
})
</script>

<style lang="scss" scoped>
.order-confirm {
  min-height: 100vh;
  background: #f5f5f5;
  
  .container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
  }
  
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30px;
    
    h1 {
      margin: 0;
      font-size: 24px;
      color: #333;
    }
    
    .back-cart {
      display: flex;
      align-items: center;
      cursor: pointer;
      color: #666;
      transition: color 0.3s ease;
      
      &:hover {
        color: #409eff;
      }
      
      .el-icon {
        margin-right: 5px;
      }
    }
  }
  
  .order-content {
    display: grid;
    gap: 20px;
  }
  
  .section {
    background: white;
    border-radius: 8px;
    padding: 25px;
    
    h2 {
      margin: 0 0 20px;
      font-size: 18px;
      color: #333;
      font-weight: 500;
    }
  }
}

.address-list {
  display: grid;
  gap: 15px;
}

.address-item {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 20px;
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    border-color: #409eff;
    background: #fafafa;
  }
  
  &.active {
    border-color: #409eff;
    background: #f0f9ff;
  }
  
  .address-info {
    .name-phone {
      display: flex;
      align-items: center;
      gap: 10px;
      margin-bottom: 8px;
      
      .name {
        font-weight: 500;
      }
      
      .phone {
        color: #666;
      }
    }
    
    .address-detail {
      color: #333;
      line-height: 1.5;
    }
  }
  
  .address-check {
    position: absolute;
    top: 10px;
    right: 10px;
    color: #409eff;
  }
}

.add-address {
  border: 2px dashed #ddd;
  border-radius: 8px;
  padding: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #999;
  
  &:hover {
    border-color: #409eff;
    color: #409eff;
  }
  
  .el-icon {
    margin-right: 5px;
  }
}

.product-list {
  .product-item {
    display: flex;
    gap: 15px;
    padding: 15px 0;
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
        margin: 0;
        font-size: 12px;
        color: #999;
      }
    }
    
    .product-price {
      text-align: right;
      
      .price {
        display: block;
        color: #f56c6c;
        font-weight: bold;
        margin-bottom: 5px;
      }
      
      .quantity {
        color: #999;
        font-size: 12px;
      }
    }
  }
}

.payment-methods {
  display: flex;
  gap: 20px;
}

.payment-method {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 150px;
  
  &:hover {
    border-color: #409eff;
  }
  
  &.active {
    border-color: #409eff;
    background: #f0f9ff;
  }
  
  .method-icon {
    color: #409eff;
  }
  
  .method-check {
    margin-left: auto;
    color: #409eff;
  }
}

.summary-section {
  background: #fafafa;
  
  .summary-item {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
    font-size: 14px;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    &.total {
      font-size: 16px;
      font-weight: bold;
      
      .total-amount {
        color: #f56c6c;
        font-size: 20px;
      }
    }
  }
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  
  .back-cart {
    display: flex;
    align-items: center;
    cursor: pointer;
    color: #666;
    transition: color 0.3s ease;
    
    &:hover {
      color: #409eff;
    }
    
    .el-icon {
      margin-right: 5px;
    }
  }
}
</style>