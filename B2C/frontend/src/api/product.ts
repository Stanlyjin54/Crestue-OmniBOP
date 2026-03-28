import { request } from './request'
import type { PageRequest, PageResponse } from '@/types'
import type { Product, ProductSku, Category, Review } from '@/types'

// 获取商品列表
export const getProductList = async (params: {
  categoryId?: number
  keyword?: string
  sort?: string
  order?: 'asc' | 'desc'
  minPrice?: number
  maxPrice?: number
} & PageRequest): Promise<PageResponse<Product>> => {
  const requestData = {
    table: 'product_info',
    method: 'page',
    current: params.page || 1,
    pageSize: params.pageSize || 10,
    productName: params.keyword,
    category: params.categoryId?.toString()
  }
  const response = await request.post('/api/data/invoke', requestData)
  // 适配后端返回的数据结构（MyBatis-Plus 的 IPage）
  return {
    list: response.records || [],
    total: response.total || 0,
    current: response.current || 1,
    size: response.size || 10
  }
}

// 获取商品详情
export const getProductDetail = async (id: number): Promise<Product> => {
  const requestData = {
    table: 'product_info',
    method: 'get',
    productInfoId: id
  }
  const response = await request.post('/api/data/invoke', requestData)
  return response
}

// 获取商品SKU列表
export const getProductSkus = (productId: number): Promise<{ skus: ProductSku[]; specs: { id: number; name: string; values: string[] }[] }> => {
  return request.get(`/product/${productId}/skus`)
}

// 获取商品分类列表
export const getCategoryList = async (): Promise<Category[]> => {
  const requestData = {
    table: 'category_info',
    method: 'list'
  }
  const response = await request.post('/api/data/invoke', requestData)
  return response || []
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