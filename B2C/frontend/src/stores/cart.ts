import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { 
  getCartList, 
  addToCart, 
  updateCartItem, 
  removeFromCart, 
  clearCart as clearCartApi 
} from '@/api'
import type { CartItem } from '@/types'

export const useCartStore = defineStore('cart', () => {
  // 状态
  const cartItems = ref<CartItem[]>([])
  const loading = ref(false)
  const selectedItems = ref<number[]>([])

  // 计算属性
  const totalCount = computed(() => 
    cartItems.value.reduce((sum, item) => sum + item.quantity, 0)
  )

  const totalAmount = computed(() => 
    cartItems.value
      .filter(item => selectedItems.value.includes(item.id))
      .reduce((sum, item) => {
        return sum + (item.price * item.quantity)
      }, 0)
  )

  const isAllSelected = computed(() => 
    cartItems.value.length > 0 && 
    selectedItems.value.length === cartItems.value.length
  )

  const hasSelectedItems = computed(() => selectedItems.value.length > 0)

  const selectedItemsCount = computed(() => selectedItems.value.length)

  // 获取购物车列表
  const fetchCart = async () => {
    try {
      loading.value = true
      const response = await getCartList()
      cartItems.value = response || []
      return response || []
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  // 添加商品到购物车
  const addItem = async (productId: number, quantity: number = 1, skuId?: number) => {
    try {
      loading.value = true
      const response = await addToCart({ productId, quantity, skuId: skuId || 0 })
      
      // 更新本地购物车列表
      const cartItem = response
      if (cartItem) {
        cartItems.value.push(cartItem)
      }
      
      return cartItem
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  // 更新购物车商品数量
  const updateItem = async (cartItemId: number, quantity: number) => {
    try {
      loading.value = true
      const response = await updateCartItem(cartItemId, { quantity })
      
      // 更新本地数据
      const item = cartItems.value.find(item => item.id === cartItemId)
      if (item) {
        item.quantity = quantity
      }
      
      return response
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  // 删除购物车商品
  const removeItem = async (cartItemId: number) => {
    try {
      loading.value = true
      const response = await removeFromCart(cartItemId)
      
      // 从本地数据中移除
      const index = cartItems.value.findIndex(item => item.id === cartItemId)
      if (index > -1) {
        cartItems.value.splice(index, 1)
      }
      
      // 从选中列表中移除
      const selectedIndex = selectedItems.value.indexOf(cartItemId)
      if (selectedIndex > -1) {
        selectedItems.value.splice(selectedIndex, 1)
      }
      
      return response
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  // 清空购物车
  const clearCart = async () => {
    try {
      loading.value = true
      const response = await clearCartApi()
      
      cartItems.value = []
      selectedItems.value = []
      
      return response
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  // 切换商品选中状态
  const toggleItemSelection = (cartItemId: number) => {
    const index = selectedItems.value.indexOf(cartItemId)
    if (index > -1) {
      selectedItems.value.splice(index, 1)
    } else {
      selectedItems.value.push(cartItemId)
    }
  }

  // 全选/取消全选
  const toggleAllSelection = () => {
    if (isAllSelected.value) {
      selectedItems.value = []
    } else {
      selectedItems.value = cartItems.value.map(item => item.id)
    }
  }

  // 获取选中的商品
  const getSelectedItems = computed(() => 
    cartItems.value.filter(item => selectedItems.value.includes(item.id))
  )

  // 设置选中的商品
  const setSelectedItems = (itemIds: number[]) => {
    selectedItems.value = itemIds
  }

  // 检查商品是否已选中
  const isItemSelected = (cartItemId: number) =>
    selectedItems.value.includes(cartItemId)

  // 更新购物车商品数量（用于外部调用后刷新）
  const updateCartCount = () => {
    return fetchCart()
  }

  return {
    // 状态
    cartItems,
    loading,
    selectedItems,
    
    // 计算属性
    totalCount,
    totalAmount,
    isAllSelected,
    hasSelectedItems,
    selectedItemsCount,
    getSelectedItems,
    
    // 方法
    fetchCart,
    addItem,
    updateItem,
    removeItem,
    clearCart,
    toggleItemSelection,
    toggleAllSelection,
    setSelectedItems,
    isItemSelected,
    updateCartCount
  }
})