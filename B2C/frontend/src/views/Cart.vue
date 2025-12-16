<template>
  <div class="cart-page">
    <div class="page-header">
      <h1>购物车</h1>
      <el-button 
        v-if="cartStore.cartItems.length > 0"
        type="text" 
        @click="handleClearCart"
      >
        清空购物车
      </el-button>
    </div>

    <div v-if="cartStore.loading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>

    <div v-else-if="cartStore.cartItems.length === 0" class="empty-container">
      <el-empty description="购物车是空的">
        <el-button type="primary" @click="goToProducts">去购物</el-button>
      </el-empty>
    </div>

    <div v-else class="cart-content">
      <!-- 购物车商品列表 -->
      <div class="cart-items">
        <div class="cart-item" v-for="item in cartStore.cartItems" :key="item.id">
          <div class="item-select">
            <el-checkbox 
              :model-value="cartStore.isItemSelected(item.id)"
              @change="() => handleSelectItem(item.id)"
            />
          </div>
          
          <div class="item-image">
            <img :src="item.productImage" :alt="item.productName" />
          </div>
          
          <div class="item-info">
            <h3 class="item-name">{{ item.productName }}</h3>
            <p class="item-specs" v-if="item.skuName">{{ item.skuName }}</p>
            <p class="item-price">¥{{ item.price }}</p>
          </div>
          
          <div class="item-quantity">
            <el-input-number
              v-model="item.quantity"
              :min="1"
              :max="item.stock"
              @change="(val) => handleQuantityChange(item.id, val)"
            />
          </div>
          
          <div class="item-total">
            ¥{{ (item.price * item.quantity).toFixed(2) }}
          </div>
          
          <div class="item-actions">
            <el-button type="text" @click="handleRemoveItem(item.id)">删除</el-button>
          </div>
        </div>
      </div>

      <!-- 购物车底部操作栏 -->
      <div class="cart-footer">
        <div class="footer-left">
          <el-checkbox
            :model-value="cartStore.isAllSelected"
            @change="() => handleSelectAll()"
          >
            全选
          </el-checkbox>
          <span class="selected-info">
            已选 {{ cartStore.selectedItemsCount }} 件商品
          </span>
        </div>
        
        <div class="footer-right">
          <div class="total-info">
            <span class="total-label">合计：</span>
            <span class="total-price">¥{{ cartStore.totalAmount.toFixed(2) }}</span>
          </div>
          <el-button 
            type="primary" 
            size="large"
            :disabled="!cartStore.hasSelectedItems"
            @click="handleCheckout"
          >
            去结算 ({{ cartStore.selectedItemsCount }})
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useCartStore } from '@/stores/cart'

const router = useRouter()
const cartStore = useCartStore()

// 生命周期
onMounted(() => {
  cartStore.fetchCart()
})

// 方法
const handleSelectItem = (itemId: number) => {
  cartStore.toggleItemSelection(itemId)
}

const handleSelectAll = () => {
  cartStore.toggleAllSelection()
}

const handleQuantityChange = async (itemId: number, quantity: number | undefined) => {
  try {
    if (quantity) {
      await cartStore.updateItem(itemId, quantity)
    }
  } catch (error) {
    ElMessage.error('更新数量失败')
  }
}

const handleRemoveItem = async (itemId: number) => {
  try {
    await ElMessageBox.confirm('确定要删除这个商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await cartStore.removeItem(itemId)
    ElMessage.success('删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleClearCart = async () => {
  try {
    await ElMessageBox.confirm('确定要清空购物车吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await cartStore.clearCart()
    ElMessage.success('购物车已清空')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const handleCheckout = () => {
  if (cartStore.hasSelectedItems) {
    router.push({
      name: 'Payment',
      query: { 
        amount: cartStore.totalAmount
      }
    })
  }
}

const goToProducts = () => {
  router.push('/products')
}
</script>

<style scoped lang="scss">
.cart-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  
  h1 {
    font-size: 24px;
    font-weight: 600;
    color: #333;
    margin: 0;
  }
}

.loading-container {
  padding: 40px 0;
}

.empty-container {
  padding: 60px 0;
}

.cart-content {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.cart-items {
  padding: 20px;
}

.cart-item {
  display: flex;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
  
  &:last-child {
    border-bottom: none;
  }
  
  .item-select {
    margin-right: 15px;
  }
  
  .item-image {
    width: 80px;
    height: 80px;
    margin-right: 15px;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      border-radius: 4px;
    }
  }
  
  .item-info {
    flex: 1;
    margin-right: 20px;
    
    .item-name {
      font-size: 16px;
      font-weight: 500;
      color: #333;
      margin: 0 0 8px;
    }
    
    .item-specs {
      font-size: 14px;
      color: #999;
      margin: 0 0 8px;
    }
    
    .item-price {
      font-size: 16px;
      font-weight: bold;
      color: #ff6b6b;
    }
  }
  
  .item-quantity {
    margin-right: 20px;
    min-width: 120px;
  }
  
  .item-total {
    font-size: 16px;
    font-weight: bold;
    color: #ff6b6b;
    margin-right: 20px;
    min-width: 80px;
    text-align: right;
  }
  
  .item-actions {
    min-width: 60px;
  }
}

.cart-footer {
  background: #f8f9fa;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .footer-left {
    display: flex;
    align-items: center;
    gap: 20px;
    
    .selected-info {
      color: #666;
      font-size: 14px;
    }
  }
  
  .footer-right {
    display: flex;
    align-items: center;
    gap: 20px;
    
    .total-info {
      display: flex;
      align-items: center;
      gap: 8px;
      
      .total-label {
        color: #666;
        font-size: 14px;
      }
      
      .total-price {
        font-size: 24px;
        font-weight: bold;
        color: #ff6b6b;
      }
    }
  }
}

@media (max-width: 768px) {
  .cart-page {
    padding: 15px;
  }
  
  .page-header {
    h1 {
      font-size: 20px;
    }
  }
  
  .cart-item {
    flex-wrap: wrap;
    padding: 15px 0;
    
    .item-select {
      order: 1;
      margin-right: 10px;
    }
    
    .item-image {
      order: 2;
      width: 60px;
      height: 60px;
      margin-right: 10px;
    }
    
    .item-info {
      order: 3;
      flex: 1;
      min-width: 200px;
      margin-right: 0;
      margin-bottom: 10px;
      
      .item-name {
        font-size: 14px;
      }
      
      .item-price {
        font-size: 14px;
      }
    }
    
    .item-quantity {
      order: 4;
      margin-right: 10px;
      min-width: 100px;
    }
    
    .item-total {
      order: 5;
      margin-right: 10px;
      font-size: 14px;
      min-width: 60px;
    }
    
    .item-actions {
      order: 6;
      min-width: 40px;
    }
  }
  
  .cart-footer {
    flex-direction: column;
    gap: 15px;
    
    .footer-left {
      width: 100%;
      justify-content: space-between;
    }
    
    .footer-right {
      width: 100%;
      justify-content: space-between;
      
      .total-price {
        font-size: 20px;
      }
    }
  }
}
</style>