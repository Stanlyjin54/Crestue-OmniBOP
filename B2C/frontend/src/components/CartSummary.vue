<template>
  <div class="cart-summary">
    <div class="cart-icon" @click="goToCart">
      <el-badge :value="cartTotal" :hidden="cartTotal === 0">
        <el-icon size="24"><ShoppingCart /></el-icon>
      </el-badge>
    </div>
    <div class="cart-info">
      <div class="total-price">¥{{ cartTotalPrice.toFixed(2) }}</div>
      <div class="delivery-info">配送费¥{{ deliveryFee }}</div>
    </div>
    <el-button 
      type="primary" 
      :disabled="cartTotalPrice === 0"
      @click="goToCheckout"
    >
      去结算
    </el-button>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
// 图标已在main.ts中全局注册，无需单独导入
import { useCartStore } from '@/stores/cart'

const cartStore = useCartStore()
const router = useRouter()

const cartTotal = computed(() => cartStore.totalCount)
const cartTotalPrice = computed(() => cartStore.totalAmount)
const deliveryFee = computed(() => cartTotalPrice.value >= 99 ? 0 : 10)

const goToCart = () => {
  router.push('/cart')
}

const goToCheckout = () => {
  if (cartTotal.value > 0) {
    router.push('/checkout')
  }
}
</script>

<style scoped lang="scss">
.cart-summary {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  border-top: 1px solid #e4e7ed;
  padding: 10px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.cart-icon {
  position: relative;
  cursor: pointer;
  padding: 10px;
  
  &:hover {
    opacity: 0.8;
  }
}

.cart-info {
  flex: 1;
  margin-left: 15px;
  
  .total-price {
    font-size: 18px;
    font-weight: bold;
    color: #ff6b6b;
  }
  
  .delivery-info {
    font-size: 12px;
    color: #999;
  }
}

@media (max-width: 768px) {
  .cart-summary {
    padding: 8px 15px;
  }
  
  .cart-info {
    .total-price {
      font-size: 16px;
    }
  }
}
</style>