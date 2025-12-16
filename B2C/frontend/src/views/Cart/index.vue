<template>
  <div class="cart-container">
    <div class="cart-header">
      <h2>我的购物车</h2>
      <el-button 
        type="text" 
        @click="handleClearCart"
        :disabled="cartStore.cartItems.length === 0"
      >
        清空购物车
      </el-button>
    </div>
    
    <div v-if="cartStore.loading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>
    
    <div v-else-if="cartStore.cartItems.length === 0" class="empty-container">
      <el-empty description="购物车是空的">
        <el-button type="primary" @click="router.push('/home')">
          去购物
        </el-button>
      </el-empty>
    </div>
    
    <div v-else class="cart-content">
      <!-- 购物车商品列表 -->
      <div class="cart-items">
        <div class="cart-item-header">
          <el-checkbox
            v-model="selectAll"
            @change="handleSelectAll"
          >
            全选
          </el-checkbox>
          <span class="header-info">商品信息</span>
          <span class="header-price">单价</span>
          <span class="header-quantity">数量</span>
          <span class="header-total">小计</span>
          <span class="header-action">操作</span>
        </div>
        
        <div class="cart-item-list">
          <div 
            v-for="item in cartStore.cartItems" 
            :key="item.id"
            class="cart-item"
          >
            <div class="item-select">
              <el-checkbox
                v-model="item.selected"
                @change="handleItemSelect()"
              />
            </div>
            
            <div class="item-info">
              <img :src="item.productImage" :alt="item.productName" />
              <div class="item-details">
                <h4 class="item-name">{{ item.productName }}</h4>
                <p v-if="item.skuName" class="item-sku">
                  {{ item.skuName }}
                </p>
              </div>
            </div>
            
            <div class="item-price">
              ¥{{ item.price }}
            </div>
            
            <div class="item-quantity">
              <el-input-number
                v-model="item.quantity"
                :min="1"
                :max="item.stock"
                @change="handleQuantityChange(item)"
              />
            </div>
            
            <div class="item-total">
              ¥{{ (item.price * item.quantity).toFixed(2) }}
            </div>
            
            <div class="item-action">
              <el-button 
                type="text" 
                @click="handleRemoveItem(item)"
                style="color: #ef4444"
              >
                删除
              </el-button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 结算栏 -->
      <div class="cart-summary">
        <div class="summary-left">
          <span>已选商品 {{ cartStore.selectedItemsCount }} 件</span>
        </div>
        
        <div class="summary-right">
          <div class="summary-info">
            <span>合计：</span>
            <span class="total-price">¥{{ cartStore.totalAmount.toFixed(2) }}</span>
          </div>
          
          <el-button 
            type="primary" 
            size="large"
            @click="handleCheckout"
            :disabled="cartStore.selectedItems.length === 0"
          >
            去结算
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useCartStore } from '@/stores/cart'
import type { CartItem } from '@/types'

const router = useRouter()
const cartStore = useCartStore()

// 计算属性
const selectAll = computed({
  get: () => cartStore.isAllSelected,
  set: () => {
    cartStore.toggleAllSelection()
  }
})

// 方法


const handleSelectAll = () => {
  cartStore.toggleAllSelection()
}

const handleItemSelect = () => {
  // 选中状态已在v-model中更新
}

const handleQuantityChange = async (item: CartItem) => {
  try {
    await cartStore.updateItem(item.id, item.quantity)
    ElMessage.success('更新数量成功')
  } catch (error) {
    ElMessage.error('更新数量失败')
    // 重新获取购物车数据
    await cartStore.fetchCart()
  }
}

const handleRemoveItem = async (item: CartItem) => {
  try {
    await ElMessageBox.confirm('确定要删除这个商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await cartStore.removeItem(item.id)
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
      ElMessage.error('清空购物车失败')
    }
  }
}

const handleCheckout = () => {
  if (cartStore.selectedItems.length === 0) {
    ElMessage.warning('请选择要结算的商品')
    return
  }
  
  // 跳转到订单确认页面
  router.push('/order/confirm')
}

// 初始化
onMounted(async () => {
  await cartStore.fetchCart()
})
</script>

<style scoped lang="scss">
.cart-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background: white;
  border-radius: 12px;
  min-height: calc(100vh - 200px);
}

.cart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e2e8f0;
  
  h2 {
    font-size: 24px;
    font-weight: 600;
    color: #1e293b;
  }
}

.loading-container,
.empty-container {
  padding: 60px 20px;
}

.cart-content {
  .cart-items {
    margin-bottom: 24px;
    
    .cart-item-header {
      display: grid;
      grid-template-columns: 60px 1fr 120px 150px 120px 80px;
      gap: 16px;
      padding: 16px;
      background: #f8fafc;
      border-radius: 8px;
      font-weight: 500;
      color: #475569;
      
      @media (max-width: 768px) {
        display: none;
      }
    }
    
    .cart-item-list {
      .cart-item {
        display: grid;
        grid-template-columns: 60px 1fr 120px 150px 120px 80px;
        gap: 16px;
        align-items: center;
        padding: 16px;
        border-bottom: 1px solid #f1f5f9;
        
        @media (max-width: 768px) {
          grid-template-columns: 1fr;
          gap: 12px;
          padding: 16px 12px;
        }
        
        &:hover {
          background: #f8fafc;
        }
        
        .item-select {
          @media (max-width: 768px) {
            position: absolute;
            top: 16px;
            left: 12px;
          }
        }
        
        .item-info {
          display: flex;
          align-items: center;
          gap: 16px;
          
          @media (max-width: 768px) {
            padding-left: 32px;
            position: relative;
          }
          
          img {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 8px;
            
            @media (max-width: 768px) {
              width: 60px;
              height: 60px;
            }
          }
          
          .item-details {
            flex: 1;
            
            .item-name {
              font-size: 16px;
              font-weight: 500;
              color: #1e293b;
              margin-bottom: 4px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
            
            .item-sku {
              font-size: 12px;
              color: #64748b;
            }
          }
        }
        
        .item-price {
          font-size: 16px;
          font-weight: 500;
          color: #1e293b;
          
          @media (max-width: 768px) {
            font-size: 14px;
            color: #ef4444;
          }
        }
        
        .item-quantity {
          @media (max-width: 768px) {
            display: flex;
            justify-content: space-between;
            align-items: center;
            
            &::before {
              content: '数量：';
              color: #64748b;
              font-size: 14px;
            }
          }
        }
        
        .item-total {
          font-size: 16px;
          font-weight: bold;
          color: #ef4444;
          
          @media (max-width: 768px) {
            font-size: 14px;
            text-align: right;
          }
        }
        
        .item-action {
          @media (max-width: 768px) {
            position: absolute;
            top: 16px;
            right: 12px;
          }
        }
      }
    }
  }
  
  .cart-summary {
    position: sticky;
    bottom: 0;
    background: white;
    border: 1px solid #e2e8f0;
    border-radius: 8px;
    padding: 16px 24px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 -2px 8px rgba(0,0,0,0.1);
    
    @media (max-width: 768px) {
      flex-direction: column;
      gap: 16px;
      padding: 16px;
    }
    
    .summary-left {
      color: #64748b;
      font-size: 14px;
    }
    
    .summary-right {
      display: flex;
      align-items: center;
      gap: 24px;
      
      @media (max-width: 768px) {
        width: 100%;
        justify-content: space-between;
      }
      
      .summary-info {
        display: flex;
        align-items: center;
        gap: 8px;
        
        .total-price {
          font-size: 20px;
          font-weight: bold;
          color: #ef4444;
          
          @media (max-width: 768px) {
            font-size: 18px;
          }
        }
      }
      
      .el-button {
        min-width: 120px;
        
        @media (max-width: 768px) {
          min-width: 100px;
        }
      }
    }
  }
}
</style>