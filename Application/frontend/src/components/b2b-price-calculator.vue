/**
 * B2B价格计算器组件
 * 支持阶梯定价、批发价格、客户等级折扣等
 */

<template>
  <view class="b2b-price-calculator">
    <!-- 商品信息 -->
    <view class="product-info-section">
      <view class="product-header">
        <image class="product-image" :src="productInfo.product_image" mode="aspectFill" />
        <view class="product-basic">
          <text class="product-name">{{ productInfo.product_name }}</text>
          <text class="product-code">{{ productInfo.product_code }}</text>
          <text class="product-spec">{{ productInfo.product_spec }}</text>
        </view>
      </view>
    </view>

    <!-- 客户信息 -->
    <view class="customer-info-section">
      <view class="customer-header">
        <text class="customer-name">{{ customerInfo.customer_name }}</text>
        <uni-tag :text="getCustomerLevelText(customerInfo.customer_level)" :type="getCustomerLevelColor(customerInfo.customer_level)" />
      </view>
      <view class="customer-meta">
        <text class="customer-type">{{ getCustomerTypeText(customerInfo.customer_type) }}</text>
        <text class="customer-channel">{{ customerInfo.channel_name }}</text>
      </view>
    </view>

    <!-- 数量输入 -->
    <view class="quantity-section">
      <view class="quantity-header">
        <text class="section-title">购买数量</text>
        <text class="inventory-info">库存: {{ productInfo.inventory_quantity }} {{ productInfo.unit }}</text>
      </view>
      <view class="quantity-input">
        <view class="quantity-control">
          <button class="quantity-btn" @click="decreaseQuantity">-</button>
          <input class="quantity-field" type="number" v-model.number="quantity" @input="handleQuantityChange" />
          <button class="quantity-btn" @click="increaseQuantity">+</button>
        </view>
        <text class="unit">{{ productInfo.unit }}</text>
      </view>
      <view v-if="quantity > productInfo.inventory_quantity" class="inventory-warning">
        <uni-icons type="warning" color="#f56c6c" />
        <text>购买数量超过库存</text>
      </view>
    </view>

    <!-- 价格阶梯 -->
    <view v-if="priceLadder.length > 0" class="price-ladder-section">
      <view class="section-header">
        <text class="section-title">价格阶梯</text>
        <text class="current-ladder">当前档位: {{ currentLadderText }}</text>
      </view>
      <view class="ladder-table">
        <view class="ladder-header">
          <text class="header-cell">数量范围</text>
          <text class="header-cell">单价</text>
          <text class="header-cell">折扣</text>
        </view>
        <view v-for="(ladder, index) in priceLadder" :key="index" 
              class="ladder-row" 
              :class="{ 'current-row': isCurrentLadder(ladder) }">
          <text class="cell">{{ formatLadderRange(ladder) }}</text>
          <text class="cell price">¥{{ formatMoney(ladder.unit_price) }}</text>
          <text class="cell">{{ formatDiscount(ladder.discount) }}</text>
        </view>
      </view>
    </view>

    <!-- 价格计算详情 -->
    <view class="price-detail-section">
      <view class="section-header">
        <text class="section-title">价格详情</text>
      </view>
      
      <view class="price-breakdown">
        <view class="price-item">
          <text class="price-label">标准单价</text>
          <text class="price-value">¥{{ formatMoney(basePrice) }}</text>
        </view>
        
        <view v-if="ladderDiscount > 0" class="price-item">
          <text class="price-label">阶梯优惠</text>
          <text class="price-value discount">-¥{{ formatMoney(ladderDiscount) }}</text>
        </view>
        
        <view v-if="customerDiscount > 0" class="price-item">
          <text class="price-label">客户等级折扣</text>
          <text class="price-value discount">-¥{{ formatMoney(customerDiscount) }}</text>
        </view>
        
        <view v-if="channelDiscount > 0" class="price-item">
          <text class="price-label">渠道折扣</text>
          <text class="price-value discount">-¥{{ formatMoney(channelDiscount) }}</text>
        </view>
        
        <view v-if="promotionDiscount > 0" class="price-item">
          <text class="price-label">促销折扣</text>
          <text class="price-value discount">-¥{{ formatMoney(promotionDiscount) }}</text>
        </view>
        
        <view class="price-divider"></view>
        
        <view class="price-item total">
          <text class="price-label">最终单价</text>
          <text class="price-value final-price">¥{{ formatMoney(finalUnitPrice) }}</text>
        </view>
        
        <view class="price-item total">
          <text class="price-label">小计</text>
          <text class="price-value final-amount">¥{{ formatMoney(subtotal) }}</text>
        </view>
        
        <view v-if="taxAmount > 0" class="price-item">
          <text class="price-label">税费</text>
          <text class="price-value">¥{{ formatMoney(taxAmount) }}</text>
        </view>
        
        <view class="price-item grand-total">
          <text class="price-label">总计</text>
          <text class="price-value grand-total-amount">¥{{ formatMoney(totalAmount) }}</text>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-section">
      <button class="action-btn secondary" @click="addToCart">加入购物车</button>
      <button class="action-btn primary" @click="createOrder">立即下单</button>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { getB2BProductPrice, getB2BPriceLadder, getB2BCustomerDiscount } from '@/api/b2b/product.js';
import { getB2BPriceStrategy } from '@/api/b2b/price-strategy.js';

const props = defineProps({
  productId: {
    type: Number,
    required: true
  },
  customerId: {
    type: Number,
    required: true
  }
});

const emit = defineEmits(['price-calculated', 'add-to-cart', 'create-order']);

// 数据状态
const quantity = ref(1);
const productInfo = ref({});
const customerInfo = ref({});
const priceLadder = ref([]);
const currentLadder = ref(null);

// 价格计算
const basePrice = ref(0);
const ladderDiscount = ref(0);
const customerDiscount = ref(0);
const channelDiscount = ref(0);
const promotionDiscount = ref(0);
const taxRate = ref(0);

// 生命周期
onMounted(() => {
  loadPriceData();
});

// 计算属性
const currentLadderText = computed(() => {
  if (!currentLadder.value) return '无';
  return formatLadderRange(currentLadder.value);
});

const finalUnitPrice = computed(() => {
  return basePrice.value - ladderDiscount.value - customerDiscount.value - channelDiscount.value - promotionDiscount.value;
});

const subtotal = computed(() => {
  return finalUnitPrice.value * quantity.value;
});

const taxAmount = computed(() => {
  return subtotal.value * taxRate.value;
});

const totalAmount = computed(() => {
  return subtotal.value + taxAmount.value;
});

// 方法
async function loadPriceData() {
  try {
    // 加载商品信息
    await loadProductInfo();
    
    // 加载客户信息
    await loadCustomerInfo();
    
    // 加载价格策略
    await loadPriceStrategy();
    
    // 计算价格
    calculatePrice();
  } catch (error) {
    console.error('加载价格数据失败:', error);
  }
}

async function loadProductInfo() {
  try {
    const res = await getB2BProductPrice(props.productId, props.customerId);
    if (res.success) {
      productInfo.value = res.data;
      basePrice.value = res.data.base_price;
    }
  } catch (error) {
    console.error('加载商品信息失败:', error);
  }
}

async function loadCustomerInfo() {
  try {
    const res = await getB2BCustomerDiscount(props.customerId);
    if (res.success) {
      customerInfo.value = res.data;
    }
  } catch (error) {
    console.error('加载客户信息失败:', error);
  }
}

async function loadPriceStrategy() {
  try {
    const res = await getB2BPriceStrategy({
      product_id: props.productId,
      customer_id: props.customerId
    });
    if (res.success) {
      const strategy = res.data;
      priceLadder.value = strategy.price_ladder || [];
      taxRate.value = strategy.tax_rate || 0;
    }
  } catch (error) {
    console.error('加载价格策略失败:', error);
  }
}

function calculatePrice() {
  // 重置折扣
  ladderDiscount.value = 0;
  customerDiscount.value = 0;
  channelDiscount.value = 0;
  promotionDiscount.value = 0;
  
  // 计算阶梯价格
  calculateLadderPrice();
  
  // 计算客户折扣
  calculateCustomerDiscount();
  
  // 计算渠道折扣
  calculateChannelDiscount();
  
  // 计算促销折扣
  calculatePromotionDiscount();
  
  // 触发价格计算完成事件
  emit('price-calculated', {
    quantity: quantity.value,
    unitPrice: finalUnitPrice.value,
    totalAmount: totalAmount.value,
    discounts: {
      ladder: ladderDiscount.value,
      customer: customerDiscount.value,
      channel: channelDiscount.value,
      promotion: promotionDiscount.value
    }
  });
}

function calculateLadderPrice() {
  if (priceLadder.value.length === 0) return;
  
  // 找到当前数量对应的阶梯
  const ladder = priceLadder.value.find(item => {
    return quantity.value >= item.min_quantity && 
           (item.max_quantity === 0 || quantity.value <= item.max_quantity);
  });
  
  if (ladder) {
    currentLadder.value = ladder;
    const ladderUnitPrice = ladder.unit_price;
    ladderDiscount.value = Math.max(0, basePrice.value - ladderUnitPrice);
  }
}

function calculateCustomerDiscount() {
  // 根据客户等级计算折扣
  const discountRate = getCustomerDiscountRate();
  if (discountRate > 0) {
    const currentBasePrice = basePrice.value - ladderDiscount.value;
    customerDiscount.value = currentBasePrice * discountRate;
  }
}

function calculateChannelDiscount() {
  // 根据渠道计算折扣
  const discountRate = getChannelDiscountRate();
  if (discountRate > 0) {
    const currentBasePrice = basePrice.value - ladderDiscount.value - customerDiscount.value;
    channelDiscount.value = currentBasePrice * discountRate;
  }
}

function calculatePromotionDiscount() {
  // 根据促销活动计算折扣
  const discountRate = getPromotionDiscountRate();
  if (discountRate > 0) {
    const currentBasePrice = basePrice.value - ladderDiscount.value - customerDiscount.value - channelDiscount.value;
    promotionDiscount.value = currentBasePrice * discountRate;
  }
}

function getCustomerDiscountRate() {
  const levelMap = {
    'A': 0.05,  // 5%
    'B': 0.03,  // 3%
    'C': 0.01,  // 1%
    'VIP': 0.10  // 10%
  };
  return levelMap[customerInfo.value.customer_level] || 0;
}

function getChannelDiscountRate() {
  const channelMap = {
    'distributor': 0.05,  // 经销商 5%
    'agent': 0.03,        // 代理商 3%
    'retail': 0.01,       // 零售商 1%
    'direct': 0           // 直销 0%
  };
  return channelMap[customerInfo.value.channel_type] || 0;
}

function getPromotionDiscountRate() {
  // 这里可以根据当前促销活动返回折扣率
  return 0;
}

function handleQuantityChange() {
  if (quantity.value < 1) quantity.value = 1;
  calculatePrice();
}

function decreaseQuantity() {
  if (quantity.value > 1) {
    quantity.value--;
    calculatePrice();
  }
}

function increaseQuantity() {
  quantity.value++;
  calculatePrice();
}

function isCurrentLadder(ladder) {
  return currentLadder.value && currentLadder.value.min_quantity === ladder.min_quantity;
}

function formatLadderRange(ladder) {
  if (ladder.max_quantity === 0) {
    return `≥${ladder.min_quantity}`;
  }
  return `${ladder.min_quantity}-${ladder.max_quantity}`;
}

function formatDiscount(discount) {
  if (discount >= 1) {
    return `${discount}折`;
  }
  return `${(discount * 100).toFixed(1)}%`;
}

function formatMoney(amount) {
  return Number(amount).toFixed(2);
}

function addToCart() {
  emit('add-to-cart', {
    productId: props.productId,
    quantity: quantity.value,
    unitPrice: finalUnitPrice.value,
    totalAmount: totalAmount.value
  });
}

function createOrder() {
  emit('create-order', {
    productId: props.productId,
    quantity: quantity.value,
    unitPrice: finalUnitPrice.value,
    totalAmount: totalAmount.value
  });
}

// 暴露方法
defineExpose({
  getPriceInfo: () => ({
    quantity: quantity.value,
    unitPrice: finalUnitPrice.value,
    totalAmount: totalAmount.value,
    discounts: {
      ladder: ladderDiscount.value,
      customer: customerDiscount.value,
      channel: channelDiscount.value,
      promotion: promotionDiscount.value
    }
  })
});
</script>

<style lang="scss" scoped>
.b2b-price-calculator {
  padding: 20rpx;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.product-info-section,
.customer-info-section,
.quantity-section,
.price-ladder-section,
.price-detail-section {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
}

.product-header {
  display: flex;
  align-items: center;
  
  .product-image {
    width: 120rpx;
    height: 120rpx;
    border-radius: 8rpx;
    margin-right: 20rpx;
  }
  
  .product-basic {
    flex: 1;
    
    .product-name {
      display: block;
      color: #303133;
      font-size: 32rpx;
      font-weight: bold;
      margin-bottom: 8rpx;
    }
    
    .product-code {
      display: block;
      color: #909399;
      font-size: 24rpx;
      margin-bottom: 4rpx;
    }
    
    .product-spec {
      display: block;
      color: #606266;
      font-size: 24rpx;
    }
  }
}

.customer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
  
  .customer-name {
    color: #303133;
    font-size: 32rpx;
    font-weight: bold;
  }
}

.customer-meta {
  display: flex;
  gap: 20rpx;
  
  .customer-type,
  .customer-channel {
    color: #606266;
    font-size: 24rpx;
  }
}

.quantity-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  
  .section-title {
    color: #303133;
    font-size: 32rpx;
    font-weight: bold;
  }
  
  .inventory-info {
    color: #909399;
    font-size: 24rpx;
  }
}

.quantity-input {
  display: flex;
  align-items: center;
  gap: 20rpx;
  
  .quantity-control {
    display: flex;
    align-items: center;
    border: 2rpx solid #dcdfe6;
    border-radius: 8rpx;
    
    .quantity-btn {
      width: 60rpx;
      height: 60rpx;
      background-color: #f5f7fa;
      border: none;
      font-size: 32rpx;
      color: #606266;
      
      &:active {
        background-color: #e4e7ed;
      }
    }
    
    .quantity-field {
      width: 100rpx;
      height: 60rpx;
      text-align: center;
      border: none;
      border-left: 2rpx solid #dcdfe6;
      border-right: 2rpx solid #dcdfe6;
      font-size: 28rpx;
    }
  }
  
  .unit {
    color: #606266;
    font-size: 24rpx;
  }
}

.inventory-warning {
  display: flex;
  align-items: center;
  margin-top: 16rpx;
  color: #f56c6c;
  font-size: 24rpx;
  
  text {
    margin-left: 8rpx;
  }
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  
  .section-title {
    color: #303133;
    font-size: 32rpx;
    font-weight: bold;
  }
  
  .current-ladder {
    color: #409eff;
    font-size: 24rpx;
  }
}

.ladder-table {
  border: 2rpx solid #e4e7ed;
  border-radius: 8rpx;
  
  .ladder-header {
    display: flex;
    background-color: #f5f7fa;
    padding: 20rpx;
    border-bottom: 2rpx solid #e4e7ed;
    
    .header-cell {
      flex: 1;
      color: #606266;
      font-size: 24rpx;
      font-weight: bold;
      text-align: center;
    }
  }
  
  .ladder-row {
    display: flex;
    padding: 20rpx;
    border-bottom: 2rpx solid #e4e7ed;
    
    &:last-child {
      border-bottom: none;
    }
    
    &.current-row {
      background-color: #f0f9ff;
      
      .cell {
        color: #409eff;
        font-weight: bold;
      }
    }
    
    .cell {
      flex: 1;
      color: #303133;
      font-size: 24rpx;
      text-align: center;
      
      &.price {
        color: #f56c6c;
        font-weight: bold;
      }
    }
  }
}

.price-breakdown {
  .price-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16rpx 0;
    
    .price-label {
      color: #606266;
      font-size: 28rpx;
    }
    
    .price-value {
      color: #303133;
      font-size: 28rpx;
      
      &.discount {
        color: #67c23a;
      }
      
      &.final-price {
        color: #f56c6c;
        font-weight: bold;
      }
      
      &.final-amount {
        color: #f56c6c;
        font-weight: bold;
      }
      
      &.grand-total-amount {
        color: #f56c6c;
        font-size: 36rpx;
        font-weight: bold;
      }
    }
    
    &.total {
      border-top: 2rpx solid #e4e7ed;
      margin-top: 16rpx;
      padding-top: 16rpx;
    }
    
    &.grand-total {
      background-color: #fef0f0;
      padding: 20rpx;
      border-radius: 8rpx;
      margin-top: 16rpx;
    }
  }
  
  .price-divider {
    height: 2rpx;
    background-color: #e4e7ed;
    margin: 16rpx 0;
  }
}

.action-section {
  display: flex;
  gap: 20rpx;
  padding: 20rpx 0;
  
  .action-btn {
    flex: 1;
    padding: 24rpx;
    border-radius: 8rpx;
    font-size: 28rpx;
    
    &.primary {
      background-color: #409eff;
      color: white;
    }
    
    &.secondary {
      background-color: #f5f7fa;
      color: #606266;
      border: 2rpx solid #dcdfe6;
    }
  }
}
</style>