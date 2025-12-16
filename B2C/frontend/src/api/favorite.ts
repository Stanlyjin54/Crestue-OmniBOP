import request from './request'

// 获取收藏列表
export const getFavoriteList = (params?: any) => {
  return request({
    url: '/favorites',
    method: 'get',
    params
  })
}

// 添加到收藏
export const addToFavorite = (data: { productId: number; skuId?: number }) => {
  return request({
    url: '/favorites',
    method: 'post',
    data
  })
}

// 取消收藏
export const removeFavorite = (productId: number) => {
  return request({
    url: `/favorites/${productId}`,
    method: 'delete'
  })
}

// 批量取消收藏
export const removeFavorites = (productIds: number[]) => {
  return request({
    url: '/favorites/batch',
    method: 'delete',
    data: { productIds }
  })
}

// 添加到购物车
export const addToCart = (data: { productId: number; quantity: number; skuId?: number }) => {
  return request({
    url: '/cart/add',
    method: 'post',
    data
  })
}