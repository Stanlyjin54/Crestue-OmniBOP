import request from './request'
import type { CartItem } from '@/types'

// 购物车相关API
export const getCartList = (): Promise<CartItem[]> =>
  request.get('/api/cart')

export const addToCart = (data: { productId: number; skuId?: number; quantity: number }): Promise<CartItem> =>
  request.post('/api/cart', data)

export const updateCartItem = (id: number, data: { quantity: number; selected?: boolean }): Promise<CartItem> =>
  request.put(`/api/cart/${id}`, data)

export const deleteCartItem = (id: number): Promise<void> =>
  request.delete(`/api/cart/${id}`)

export const removeFromCart = deleteCartItem // 别名，保持兼容性
export const clearCart = (): Promise<void> =>
  request.delete('/api/cart/clear')

export const selectAllCartItems = (selected: boolean): Promise<void> =>
  request.put('/api/cart/select-all', { selected })

export const updateCartItemSelected = (selectedItemIds: number[]): Promise<void> =>
  request.put('/api/cart/select', { selectedItemIds })
