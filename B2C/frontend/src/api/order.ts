import { request } from './request'
import type { PageResponse, PageRequest, Order, Address } from '@/types'

// 创建订单
export const createOrder = (data: {
  items: Array<{
    productId: number
    quantity: number
    skuId?: number
    price?: number
  }>
  addressId: number
  paymentMethod?: string
  remark?: string
  couponId?: number
}): Promise<{ id: number; orderNo: string }> => {
  return request.post('/order/create', data)
}

// 获取订单列表
export const getOrderList = (params: {
  status?: string
} & PageRequest): Promise<PageResponse<Order>> => {
  return request.get('/order/list', { params })
}

// 获取订单详情
export const getOrderDetail = (orderId: number): Promise<Order> => {
  return request.get(`/order/${orderId}`)
}

// 取消订单
export const cancelOrder = (orderId: number, reason?: string): Promise<void> => {
  return request.put(`/order/${orderId}/cancel`, { reason })
}

// 确认收货
export const confirmOrder = (orderId: number): Promise<void> => {
  return request.put(`/order/${orderId}/confirm`)
}

// 删除订单
export const deleteOrder = (orderId: number): Promise<void> => {
  return request.delete(`/order/${orderId}`)
}

// 获取收货地址列表
export const getAddressList = (): Promise<Address[]> => {
  return request.get('/user/addresses')
}

// 添加收货地址
export const addAddress = (data: Omit<Address, 'id' | 'userId' | 'createdAt'>): Promise<Address> => {
  return request.post('/user/addresses', data)
}

// 更新收货地址
export const updateAddress = (addressId: number, data: Partial<Address>): Promise<void> => {
  return request.put(`/user/addresses/${addressId}`, data)
}

// 删除收货地址
export const deleteAddress = (addressId: number): Promise<void> => {
  return request.delete(`/user/addresses/${addressId}`)
}

// 设置默认地址
export const setDefaultAddress = (addressId: number): Promise<void> => {
  return request.put(`/user/addresses/${addressId}/default`)
}