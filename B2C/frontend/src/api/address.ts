import request from './request'

export interface AddressForm {
  id?: number
  name: string
  phone: string
  province: string
  city: string
  district: string
  detail: string
  postalCode?: string
  isDefault: boolean
}

/**
 * 获取地址列表
 */
export const getAddressList = (): Promise<any> => {
  return request.get('/api/user/addresses')
}

/**
 * 新增收货地址
 */
export const addAddress = (data: AddressForm): Promise<any> => {
  return request.post('/api/user/addresses', data)
}

/**
 * 更新收货地址
 */
export const updateAddress = (data: AddressForm): Promise<any> => {
  return request.put(`/api/user/addresses/${data.id}`, data)
}

/**
 * 删除收货地址
 */
export const deleteAddress = (id: number): Promise<any> => {
  return request.delete(`/api/user/addresses/${id}`)
}

/**
 * 设置默认地址
 */
export const setDefaultAddress = (id: number): Promise<any> => {
  return request.put(`/api/user/addresses/${id}/default`)
}