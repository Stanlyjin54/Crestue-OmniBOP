import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getProductList, getProductDetail } from '@/api/product'
import type { Product, ProductQuery } from '@/types'

export const useProductStore = defineStore('product', () => {
  // 状态
  const products = ref<Product[]>([])
  const currentProduct = ref<Product | null>(null)
  const loading = ref(false)
  const total = ref(0)
  const page = ref(1)
  const pageSize = ref(12)
  
  // 筛选条件
  const searchQuery = ref('')
  const categoryId = ref<number | null>(null)
  const sortBy = ref('id')
  const sortOrder = ref<'asc' | 'desc'>('desc')
  const priceRange = ref<[number, number] | null>(null)
  const brand = ref<string | null>(null)

  // 计算属性
  const hasProducts = computed(() => products.value.length > 0)
  const totalPages = computed(() => Math.ceil(total.value / pageSize.value))

  // 获取商品列表
  const fetchProducts = async (query?: ProductQuery) => {
    try {
      loading.value = true
      
      const params: ProductQuery = {
        page: page.value,
        pageSize: pageSize.value,
        search: searchQuery.value || undefined,
        categoryId: categoryId.value || undefined,
        sort: sortBy.value,
        order: sortOrder.value,
        minPrice: priceRange.value?.[0],
        maxPrice: priceRange.value?.[1],
        brand: brand.value || undefined,
        ...query
      }
      
      const response = await getProductList(params)
      products.value = response.list
      total.value = response.total
      
      return response
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  // 获取商品详情
  const fetchProductDetail = async (id: number) => {
    try {
      loading.value = true
      const response = await getProductDetail(id)
      currentProduct.value = response
      return response
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  // 搜索商品
  const searchProducts = async (query: string) => {
    searchQuery.value = query
    page.value = 1 // 重置页码
    return await fetchProducts()
  }

  // 按分类筛选
  const filterByCategory = async (category: number | null) => {
    categoryId.value = category
    page.value = 1 // 重置页码
    return await fetchProducts()
  }

  // 排序
  const sortProducts = async (field: string, order: 'asc' | 'desc') => {
    sortBy.value = field
    sortOrder.value = order
    return await fetchProducts()
  }

  // 按价格筛选
  const filterByPrice = async (minPrice: number, maxPrice: number) => {
    priceRange.value = [minPrice, maxPrice]
    page.value = 1 // 重置页码
    return await fetchProducts()
  }

  // 按品牌筛选
  const filterByBrand = async (brandName: string | null) => {
    brand.value = brandName
    page.value = 1 // 重置页码
    return await fetchProducts()
  }

  // 分页
  const changePage = async (newPage: number) => {
    page.value = newPage
    return await fetchProducts()
  }

  // 改变每页数量
  const changePageSize = async (size: number) => {
    pageSize.value = size
    page.value = 1 // 重置页码
    return await fetchProducts()
  }

  // 重置筛选条件
  const resetFilters = () => {
    searchQuery.value = ''
    categoryId.value = null
    sortBy.value = 'id'
    sortOrder.value = 'desc'
    priceRange.value = null
    brand.value = null
    page.value = 1
    pageSize.value = 12
  }

  // 清空商品数据
  const clearProducts = () => {
    products.value = []
    currentProduct.value = null
    total.value = 0
    page.value = 1
  }

  // 获取推荐商品
  const fetchRecommendProducts = async (type: 'hot' | 'new' | 'discount', limit?: number) => {
    try {
      loading.value = true
      // 使用通用的商品列表API，添加推荐条件
      const params = {
        page: 1,
        pageSize: limit || 8,
        keyword: type
      }
      const response = await getProductList(params)
      products.value = response.list
      return response
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  return {
    // 状态
    products,
    currentProduct,
    loading,
    total,
    page,
    pageSize,
    searchQuery,
    categoryId,
    sortBy,
    sortOrder,
    priceRange,
    brand,
    
    // 计算属性
    hasProducts,
    totalPages,
    
    // 方法
    fetchProducts,
    fetchProductDetail,
    searchProducts,
    filterByCategory,
    sortProducts,
    filterByPrice,
    filterByBrand,
    changePage,
    changePageSize,
    resetFilters,
    clearProducts,
    fetchRecommendProducts
  }
})