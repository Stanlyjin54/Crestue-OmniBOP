import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'

// 商品状态枚举
export const PRODUCT_STATUS = {
  ACTIVE: { label: '在售', value: 'active', type: 'success' },
  INACTIVE: { label: '停售', value: 'inactive', type: 'info' },
  OUT_OF_STOCK: { label: '缺货', value: 'out_of_stock', type: 'warning' },
  DISCONTINUED: { label: '停产', value: 'discontinued', type: 'danger' }
}

// 商品规格类型
export const SPEC_TYPES = {
  COLOR: 'color',
  SIZE: 'size',
  WEIGHT: 'weight',
  MATERIAL: 'material',
  OTHER: 'other'
}

// 库存预警级别
export const STOCK_ALERT_LEVELS = {
  SAFE: { label: '充足', value: 'safe', color: '#67C23A' },
  WARNING: { label: '预警', value: 'warning', color: '#E6A23C' },
  DANGER: { label: '危险', value: 'danger', color: '#F56C6C' }
}

export function useProduct() {
  const loading = ref(false)
  const products = ref([])
  const categories = ref([])
  const brands = ref([])
  
  // 商品表单数据
  const productForm = reactive({
    id: null,
    name: '',
    barcode: '',
    category_id: '',
    brand_id: '',
    unit: '',
    description: '',
    images: [],
    serial_number: '',
    shelf_life: null,
    safety_stock: 0,
    status: 'active',
    specifications: [],
    skus: []
  })

  // 规格管理
  const specifications = ref([])
  
  // SKU管理
  const skus = ref([])
  
  // 库存管理
  const inventory = reactive({
    total_stock: 0,
    available_stock: 0,
    locked_stock: 0,
    warehouses: []
  })

  // 获取商品列表
  const fetchProducts = async (params = {}) => {
    loading.value = true
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      products.value = [
        {
          id: 1,
          name: 'iPhone 15 Pro Max',
          barcode: '1234567890123',
          category: '手机',
          brand: 'Apple',
          unit: '台',
          price: 9999,
          stock: 150,
          safety_stock: 20,
          status: 'active',
          images: ['https://via.placeholder.com/150'],
          specifications: [
            { name: '颜色', value: '深空黑色', type: 'color' },
            { name: '存储', value: '256GB', type: 'other' }
          ]
        },
        {
          id: 2,
          name: 'MacBook Pro 16英寸',
          barcode: '1234567890124',
          category: '笔记本',
          brand: 'Apple',
          unit: '台',
          price: 19999,
          stock: 80,
          safety_stock: 15,
          status: 'active',
          images: ['https://via.placeholder.com/150'],
          specifications: [
            { name: '处理器', value: 'M3 Pro', type: 'other' },
            { name: '内存', value: '18GB', type: 'other' }
          ]
        }
      ]
    } catch (error) {
      ElMessage.error('获取商品列表失败')
      console.error(error)
    } finally {
      loading.value = false
    }
  }

  // 获取商品分类
  const fetchCategories = async () => {
    try {
      // 模拟API调用
      categories.value = [
        { id: 1, name: '手机', code: 'mobile' },
        { id: 2, name: '笔记本', code: 'laptop' },
        { id: 3, name: '平板', code: 'tablet' },
        { id: 4, name: '配件', code: 'accessory' }
      ]
    } catch (error) {
      console.error('获取分类失败', error)
    }
  }

  // 获取品牌列表
  const fetchBrands = async () => {
    try {
      // 模拟API调用
      brands.value = [
        { id: 1, name: 'Apple', logo: 'https://via.placeholder.com/50' },
        { id: 2, name: 'Samsung', logo: 'https://via.placeholder.com/50' },
        { id: 3, name: 'Huawei', logo: 'https://via.placeholder.com/50' },
        { id: 4, name: 'Xiaomi', logo: 'https://via.placeholder.com/50' }
      ]
    } catch (error) {
      console.error('获取品牌失败', error)
    }
  }

  // 创建商品
  const createProduct = async (productData) => {
    try {
      loading.value = true
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      const newProduct = {
        id: Date.now(),
        ...productData,
        created_at: new Date().toISOString()
      }
      
      products.value.unshift(newProduct)
      ElMessage.success('商品创建成功')
      return newProduct
    } catch (error) {
      ElMessage.error('商品创建失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 更新商品
  const updateProduct = async (id, productData) => {
    try {
      loading.value = true
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      const index = products.value.findIndex(p => p.id === id)
      if (index !== -1) {
        products.value[index] = { ...products.value[index], ...productData }
      }
      
      ElMessage.success('商品更新成功')
    } catch (error) {
      ElMessage.error('商品更新失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 删除商品
  const deleteProduct = async (id) => {
    try {
      loading.value = true
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      products.value = products.value.filter(p => p.id !== id)
      ElMessage.success('商品删除成功')
    } catch (error) {
      ElMessage.error('商品删除失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 添加规格
  const addSpecification = (spec) => {
    specifications.value.push({
      id: Date.now(),
      name: spec.name,
      type: spec.type || SPEC_TYPES.OTHER,
      values: spec.values || [],
      required: spec.required || false
    })
  }

  // 删除规格
  const removeSpecification = (id) => {
    specifications.value = specifications.value.filter(s => s.id !== id)
  }

  // 生成SKU
  const generateSkus = () => {
    if (specifications.value.length === 0) {
      skus.value = [{
        id: Date.now(),
        code: productForm.barcode || '',
        specifications: [],
        price: 0,
        stock: 0,
        image: ''
      }]
      return
    }

    // 简单的SKU生成逻辑
    const specCombinations = []
    specifications.value.forEach(spec => {
      if (spec.values.length > 0) {
        specCombinations.push(spec.values.map(v => ({
          name: spec.name,
          value: v,
          type: spec.type
        })))
      }
    })

    if (specCombinations.length === 0) return

    // 生成组合
    const combinations = specCombinations.reduce((acc, curr) => {
      const result = []
      acc.forEach(item => {
        curr.forEach(spec => {
          result.push([...item, spec])
        })
      })
      return result
    }, [[]])

    skus.value = combinations.map((combo, index) => ({
      id: Date.now() + index,
      code: `${productForm.barcode}-${index + 1}`,
      specifications: combo,
      price: 0,
      stock: 0,
      image: ''
    }))
  }

  // 检查库存预警
  const checkStockAlert = (product) => {
    if (product.stock <= 0) return STOCK_ALERT_LEVELS.DANGER
    if (product.stock <= product.safety_stock) return STOCK_ALERT_LEVELS.WARNING
    return STOCK_ALERT_LEVELS.SAFE
  }

  // 获取库存预警状态
  const getStockAlertStatus = (stock, safetyStock) => {
    if (stock <= 0) return STOCK_ALERT_LEVELS.DANGER
    if (stock <= safetyStock) return STOCK_ALERT_LEVELS.WARNING
    return STOCK_ALERT_LEVELS.SAFE
  }

  // 计算库存状态
  const calculateStockStatus = computed(() => {
    return products.value.map(product => ({
      ...product,
      stock_status: getStockAlertStatus(product.stock, product.safety_stock)
    }))
  })

  // 获取库存统计
  const getStockStatistics = computed(() => {
    const total = products.value.length
    const lowStock = products.value.filter(p => p.stock <= p.safety_stock).length
    const outOfStock = products.value.filter(p => p.stock <= 0).length
    
    return {
      total,
      low_stock: lowStock,
      out_of_stock: outOfStock,
      normal: total - lowStock - outOfStock
    }
  })

  // 重置表单
  const resetForm = () => {
    Object.assign(productForm, {
      id: null,
      name: '',
      barcode: '',
      category_id: '',
      brand_id: '',
      unit: '',
      description: '',
      images: [],
      serial_number: '',
      shelf_life: null,
      safety_stock: 0,
      status: 'active',
      specifications: [],
      skus: []
    })
    specifications.value = []
    skus.value = []
  }

  return {
    // 状态
    loading,
    products,
    categories,
    brands,
    productForm,
    specifications,
    skus,
    inventory,
    
    // 计算属性
    calculateStockStatus,
    getStockStatistics,
    
    // 方法
    fetchProducts,
    fetchCategories,
    fetchBrands,
    createProduct,
    updateProduct,
    deleteProduct,
    addSpecification,
    removeSpecification,
    generateSkus,
    checkStockAlert,
    getStockAlertStatus,
    resetForm
  }
}