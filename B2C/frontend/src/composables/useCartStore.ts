import { ref, computed } from 'vue'
import { addToCart, getCartList, updateCartItem, deleteCartItem, clearCart, updateCartItemSelected } from '@/api/cart'
import type { CartItem } from '@/types'

export const useCartStore = () => {
  // 状态
  const cartItems = ref<CartItem[]>([])
  const loading = ref(false)
  const selectedItems = ref<number[]>([])

  // 计算属性
  const totalItems = computed(() => {
    return cartItems.value.reduce((total, item) => total + item.quantity, 0)
  })

  const totalAmount = computed(() => {
    return cartItems.value
      .filter(item => selectedItems.value.includes(item.id))
      .reduce((total, item) => total + item.price * item.quantity, 0)
  })

  const isAllSelected = computed(() => {
    return cartItems.value.length > 0 && selectedItems.value.length === cartItems.value.length
  })

  // 获取购物车列表
  const fetchCartItems = async () => {
    try {
      loading.value = true
      const response = await getCartList()
      cartItems.value = response
      selectedItems.value = cartItems.value.filter(item => item.selected).map(item => item.id)
      return response
    } catch (error) {
      console.error('获取购物车失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  // 添加到购物车
  const addItem = async (params: { productId: number; quantity: number; specs?: Record<string, string> }) => {
    try {
      const response = await addToCart(params)
      await fetchCartItems() // 重新获取购物车数据
      return response
    } catch (error) {
      console.error('添加到购物车失败:', error)
      throw error
    }
  }

  // 更新购物车商品
  const updateItem = async (cartItemId: number, quantity: number) => {
    try {
      const response = await updateCartItem(cartItemId, { quantity })
      await fetchCartItems() // 重新获取购物车数据
      return response
    } catch (error) {
      console.error('更新购物车失败:', error)
      throw error
    }
  }

  // 删除购物车商品
  const removeItem = async (cartItemId: number) => {
    try {
      const response = await deleteCartItem(cartItemId)
      await fetchCartItems() // 重新获取购物车数据
      return response
    } catch (error) {
      console.error('删除购物车商品失败:', error)
      throw error
    }
  }

  // 清空购物车
  const clearAll = async () => {
    try {
      const response = await clearCart()
      cartItems.value = []
      selectedItems.value = []
      return response
    } catch (error) {
      console.error('清空购物车失败:', error)
      throw error
    }
  }

  // 切换商品选中状态
  const toggleItemSelected = async (cartItemId: number) => {
    const isSelected = selectedItems.value.includes(cartItemId)
    const newSelectedItems = isSelected
      ? selectedItems.value.filter(id => id !== cartItemId)
      : [...selectedItems.value, cartItemId]
    
    try {
      await updateCartItemSelected(newSelectedItems)
      selectedItems.value = newSelectedItems
    } catch (error) {
      console.error('更新选中状态失败:', error)
      throw error
    }
  }

  // 全选/取消全选
  const toggleAllSelected = async () => {
    const newSelectedStatus = !isAllSelected.value
    const newSelectedItems = newSelectedStatus
      ? cartItems.value.map(item => item.id)
      : []
    
    try {
      await updateCartItemSelected(newSelectedItems)
      selectedItems.value = newSelectedItems
    } catch (error) {
      console.error('更新全选状态失败:', error)
      throw error
    }
  }

  // 获取选中的商品
  const getSelectedItems = computed(() => {
    return cartItems.value.filter(item => selectedItems.value.includes(item.id))
  })

  // 检查商品是否在购物车中
  const isInCart = (productId: number) => {
    return cartItems.value.some(item => item.productId === productId)
  }

  // 获取购物车中商品的数量
  const getItemQuantity = (productId: number) => {
    const item = cartItems.value.find(item => item.productId === productId)
    return item ? item.quantity : 0
  }

  return {
    // 状态
    cartItems,
    loading,
    selectedItems,
    
    // 计算属性
    totalItems,
    totalAmount,
    isAllSelected,
    getSelectedItems,
    isInCart,
    getItemQuantity,
    
    // 方法
    fetchCartItems,
    addItem,
    updateItem,
    removeItem,
    clearAll,
    toggleItemSelected,
    toggleAllSelected
  }
}