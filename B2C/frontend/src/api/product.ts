import { request } from './request'
import type { PageRequest, PageResponse } from '@/types'
import type { Product, ProductSku, Category, Review } from '@/types'

// 获取商品列表
export const getProductList = (params: {
  categoryId?: number
  keyword?: string
  sort?: string
  order?: 'asc' | 'desc'
  minPrice?: number
  maxPrice?: number
} & PageRequest): Promise<PageResponse<Product>> => {
  return request.get('/product/list', { params })
}

// 获取商品详情
export const getProductDetail = (id: number): Promise<Product> => {
  return request.get(`/product/${id}`)
}

// 获取商品SKU列表
export const getProductSkus = (productId: number): Promise<ProductSku[]> => {
  return request.get(`/product/${productId}/skus`)
}

// 获取商品分类列表
export const getCategoryList = (): Promise<Category[]> => {
  return request.get('/category/list')
}

// 获取商品分类列表（别名）
export const getProductCategories = getCategoryList

// 获取商品评价列表
export const getProductReviews = (productId: number, params?: {
  page?: number
  pageSize?: number
  filterType?: 'all' | 'positive' | 'negative'
}): Promise<PageResponse<Review>> => {
  return request.get(`/product/${productId}/reviews`, { params })
}

// 点赞评价
export const likeReview = (reviewId: number): Promise<void> => {
  return request.post(`/reviews/${reviewId}/like`)
}