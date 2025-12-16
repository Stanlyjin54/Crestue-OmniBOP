import { ref } from 'vue'
import { getProductList, getProductDetail } from '@/api/product'
import type { Product, ProductQuery } from '@/types'

export const useProductStore = () => {
  // 状态
  const products = ref<Product[]>([])
  const productDetail = ref<Product | null>(null)
  const loading = ref(false)
  const currentPage = ref(1)
  const pageSize = ref(12)
  const total = ref(0)
  const searchKeyword = ref('')
  const categoryId = ref<number | undefined>(undefined)
  const sort = ref('default')
  const order = ref<'asc' | 'desc'>('desc')

  // 获取商品列表
  const fetchProducts = async (params?: Partial<ProductQuery>) => {
    try {
      loading.value = true
      const queryParams = {
        page: currentPage.value,
        pageSize: pageSize.value,
        keyword: searchKeyword.value,
        categoryId: categoryId.value,
        sort: sort.value,
        order: order.value,
        ...params
      }
      
      const response = await getProductList(queryParams)
      products.value = response.list
      total.value = response.total
      
      return response
    } catch (error) {
      console.error('获取商品列表失败:', error)
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
      productDetail.value = response
      
      return response
    } catch (error) {
      console.error('获取商品详情失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  // 搜索商品
  const searchProducts = async (keyword: string) => {
    searchKeyword.value = keyword
    currentPage.value = 1
    await fetchProducts()
  }

  // 按分类筛选
  const filterByCategory = async (id: number | null | undefined) => {
    categoryId.value = id ?? undefined
    currentPage.value = 1
    await fetchProducts()
  }

  // 排序
  const sortProducts = async (by: string, orderType: 'asc' | 'desc' = 'desc') => {
    sort.value = by
    order.value = orderType
    await fetchProducts()
  }

  // 分页
  const changePage = async (page: number) => {
    currentPage.value = page
    await fetchProducts()
  }

  // 重置筛选条件
  const resetFilters = () => {
    searchKeyword.value = ''
    categoryId.value = undefined
    sort.value = 'default'
    order.value = 'desc'
    currentPage.value = 1
  }

  return {
    // 状态
    products,
    productDetail,
    loading,
    currentPage,
    pageSize,
    total,
    searchKeyword,
    categoryId,
    sort,
    order,
    
    // 方法
    fetchProducts,
    fetchProductDetail,
    searchProducts,
    filterByCategory,
    sortProducts,
    changePage,
    resetFilters
  }
}