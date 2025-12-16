import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'

// 订单状态枚举
export const ORDER_STATUS = {
  PENDING: { label: '待确认', value: 'pending', type: 'warning' },
  CONFIRMED: { label: '已确认', value: 'confirmed', type: 'primary' },
  PROCESSING: { label: '处理中', value: 'processing', type: 'info' },
  SHIPPED: { label: '已发货', value: 'shipped', type: 'primary' },
  DELIVERED: { label: '已送达', value: 'delivered', type: 'success' },
  COMPLETED: { label: '已完成', value: 'completed', type: 'success' },
  CANCELLED: { label: '已取消', value: 'cancelled', type: 'danger' },
  REFUNDED: { label: '已退款', value: 'refunded', type: 'danger' }
}

// 订单类型
export const ORDER_TYPES = {
  B2B: { label: 'B2B订单', value: 'b2b', type: 'primary' },
  B2C: { label: 'B2C订单', value: 'b2c', type: 'success' },
  PROCUREMENT: { label: '采购订单', value: 'procurement', type: 'warning' },
  TRANSFER: { label: '调拨订单', value: 'transfer', type: 'info' }
}

// 支付方式
export const PAYMENT_METHODS = {
  CREDIT: { label: '账期支付', value: 'credit', type: 'primary' },
  INSTALLMENT: { label: '分期付款', value: 'installment', type: 'warning' },
  CASH: { label: '现金支付', value: 'cash', type: 'success' },
  TRANSFER: { label: '银行转账', value: 'transfer', type: 'info' },
  ONLINE: { label: '在线支付', value: 'online', type: 'primary' }
}

// 发货方式
export const SHIPPING_METHODS = {
  SELF_PICKUP: { label: '自提', value: 'self_pickup', type: 'info' },
  EXPRESS: { label: '快递', value: 'express', type: 'primary' },
  LOGISTICS: { label: '物流', value: 'logistics', type: 'warning' },
  SAME_DAY: { label: '当日达', value: 'same_day', type: 'success' },
  NEXT_DAY: { label: '次日达', value: 'next_day', type: 'success' }
}

export function useOrder() {
  const loading = ref(false)
  const orders = ref([])
  const orderItems = ref([])
  
  // 订单查询表单
  const orderForm = reactive({
    order_number: '',
    customer_name: '',
    status: '',
    order_type: '',
    payment_method: '',
    date_range: [],
    amount_min: '',
    amount_max: ''
  })

  // 订单统计
  const orderStats = reactive({
    total_orders: 0,
    pending_orders: 0,
    processing_orders: 0,
    completed_orders: 0,
    cancelled_orders: 0,
    total_amount: 0,
    today_orders: 0,
    today_amount: 0
  })

  // 订单详情
  const orderDetail = ref(null)

  // 获取订单列表
  const fetchOrders = async (params = {}) => {
    loading.value = true
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      orders.value = [
        {
          id: 1,
          order_number: 'B2B202401150001',
          customer_name: '华为技术有限公司',
          customer_phone: '13800138000',
          customer_email: 'purchase@huawei.com',
          order_type: 'b2b',
          status: 'confirmed',
          payment_method: 'credit',
          payment_status: 'pending',
          shipping_method: 'logistics',
          total_amount: 150000,
          paid_amount: 0,
          unpaid_amount: 150000,
          order_date: '2024-01-15',
          delivery_date: '2024-01-25',
          delivery_address: '深圳市南山区华为基地',
          remark: '大客户订单，优先处理',
          items_count: 3,
          created_at: '2024-01-15 10:30:00',
          updated_at: '2024-01-15 10:30:00'
        },
        {
          id: 2,
          order_number: 'B2C202401150002',
          customer_name: '张三',
          customer_phone: '13900139000',
          customer_email: 'zhangsan@example.com',
          order_type: 'b2c',
          status: 'processing',
          payment_method: 'online',
          payment_status: 'paid',
          shipping_method: 'express',
          total_amount: 2999,
          paid_amount: 2999,
          unpaid_amount: 0,
          order_date: '2024-01-15',
          delivery_date: '2024-01-17',
          delivery_address: '北京市朝阳区建国门外大街1号',
          remark: '客户要求尽快发货',
          items_count: 2,
          created_at: '2024-01-15 14:20:00',
          updated_at: '2024-01-15 14:20:00'
        },
        {
          id: 3,
          order_number: 'PROC202401150003',
          customer_name: '苹果贸易(上海)有限公司',
          customer_phone: '13700137000',
          customer_email: 'order@apple.com',
          order_type: 'procurement',
          status: 'pending',
          payment_method: 'transfer',
          payment_status: 'pending',
          shipping_method: 'logistics',
          total_amount: 500000,
          paid_amount: 0,
          unpaid_amount: 500000,
          order_date: '2024-01-15',
          delivery_date: '2024-02-15',
          delivery_address: '上海市浦东新区苹果园区',
          remark: '季度采购订单',
          items_count: 5,
          created_at: '2024-01-15 16:45:00',
          updated_at: '2024-01-15 16:45:00'
        }
      ]
      
      // 更新统计
      updateOrderStats()
    } catch (error) {
      ElMessage.error('获取订单列表失败')
      console.error(error)
    } finally {
      loading.value = false
    }
  }

  // 获取订单详情
  const fetchOrderDetail = async (orderId) => {
    try {
      loading.value = true
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      orderDetail.value = {
        id: orderId,
        order_number: 'B2B202401150001',
        customer_name: '华为技术有限公司',
        customer_phone: '13800138000',
        customer_email: 'purchase@huawei.com',
        order_type: 'b2b',
        status: 'confirmed',
        payment_method: 'credit',
        payment_status: 'pending',
        shipping_method: 'logistics',
        total_amount: 150000,
        paid_amount: 0,
        unpaid_amount: 150000,
        order_date: '2024-01-15',
        delivery_date: '2024-01-25',
        delivery_address: '深圳市南山区华为基地',
        remark: '大客户订单，优先处理',
        items: [
          {
            id: 1,
            product_name: 'iPhone 15 Pro Max',
            product_barcode: '1234567890123',
            quantity: 50,
            unit_price: 2000,
            total_price: 100000,
            specifications: '深空黑色, 256GB',
            remark: '大客户专享价格'
          },
          {
            id: 2,
            product_name: 'MacBook Pro 16英寸',
            product_barcode: '1234567890124',
            quantity: 10,
            unit_price: 4000,
            total_price: 40000,
            specifications: 'M3 Pro, 18GB, 512GB',
            remark: '定制配置'
          },
          {
            id: 3,
            product_name: 'AirPods Pro',
            product_barcode: '1234567890125',
            quantity: 100,
            unit_price: 100,
            total_price: 10000,
            specifications: '第二代',
            remark: '批量采购'
          }
        ],
        payment_terms: {
          credit_days: 30,
          credit_amount: 150000,
          first_payment: 0,
          first_payment_date: null,
          installment_count: 0
        },
        delivery_schedule: {
          first_batch_date: '2024-01-20',
          complete_date: '2024-01-25',
          partial_delivery: true
        },
        created_at: '2024-01-15 10:30:00',
        updated_at: '2024-01-15 10:30:00'
      }
    } catch (error) {
      ElMessage.error('获取订单详情失败')
      console.error(error)
    } finally {
      loading.value = false
    }
  }

  // 创建订单
  const createOrder = async (orderData) => {
    try {
      loading.value = true
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      const newOrder = {
        id: Date.now(),
        order_number: generateOrderNumber(orderData.order_type),
        ...orderData,
        status: 'pending',
        created_at: new Date().toISOString()
      }
      
      orders.value.unshift(newOrder)
      ElMessage.success('订单创建成功')
      return newOrder
    } catch (error) {
      ElMessage.error('订单创建失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 更新订单状态
  const updateOrderStatus = async (orderId, status, remark = '') => {
    try {
      loading.value = true
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      const order = orders.value.find(o => o.id === orderId)
      if (order) {
        order.status = status
        order.remark = remark || order.remark
        order.updated_at = new Date().toISOString()
      }
      
      ElMessage.success('订单状态更新成功')
    } catch (error) {
      ElMessage.error('订单状态更新失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 生成订单号
  const generateOrderNumber = (orderType) => {
    const prefix = {
      'b2b': 'B2B',
      'b2c': 'B2C',
      'procurement': 'PROC',
      'transfer': 'TRANS'
    }[orderType] || 'ORDER'
    
    const date = new Date().toISOString().slice(0, 10).replace(/-/g, '')
    const random = Math.floor(Math.random() * 10000).toString().padStart(4, '0')
    
    return `${prefix}${date}${random}`
  }

  // 更新订单统计
  const updateOrderStats = () => {
    const today = new Date().toISOString().slice(0, 10)
    const todayOrders = orders.value.filter(order => 
      order.created_at.startsWith(today)
    )
    
    orderStats.total_orders = orders.value.length
    orderStats.pending_orders = orders.value.filter(o => o.status === 'pending').length
    orderStats.processing_orders = orders.value.filter(o => o.status === 'processing').length
    orderStats.completed_orders = orders.value.filter(o => o.status === 'completed').length
    orderStats.cancelled_orders = orders.value.filter(o => o.status === 'cancelled').length
    orderStats.total_amount = orders.value.reduce((sum, order) => sum + order.total_amount, 0)
    orderStats.today_orders = todayOrders.length
    orderStats.today_amount = todayOrders.reduce((sum, order) => sum + order.total_amount, 0)
  }

  // 获取订单统计
  const getOrderStatistics = computed(() => {
    return {
      total: orderStats.total_orders,
      pending: orderStats.pending_orders,
      processing: orderStats.processing_orders,
      completed: orderStats.completed_orders,
      cancelled: orderStats.cancelled_orders,
      completion_rate: orderStats.total_orders > 0 ? 
        Math.round((orderStats.completed_orders / orderStats.total_orders) * 100) : 0
    }
  })

  // 获取销售统计
  const getSalesStatistics = computed(() => {
    return {
      total_amount: orderStats.total_amount,
      today_amount: orderStats.today_amount,
      average_order_value: orderStats.total_orders > 0 ? 
        Math.round(orderStats.total_amount / orderStats.total_orders) : 0
    }
  })

  // 获取订单类型分布
  const getOrderTypeDistribution = computed(() => {
    const distribution = {}
    Object.values(ORDER_TYPES).forEach(type => {
      distribution[type.value] = orders.value.filter(o => o.order_type === type.value).length
    })
    return distribution
  })

  // 检查订单状态
  const checkOrderStatus = (order) => {
    if (order.status === 'completed') return { label: '已完成', type: 'success' }
    if (order.status === 'cancelled') return { label: '已取消', type: 'danger' }
    if (order.status === 'refunded') return { label: '已退款', type: 'info' }
    if (order.payment_status === 'paid') return { label: '已付款', type: 'success' }
    if (order.payment_status === 'pending') return { label: '待付款', type: 'warning' }
    return { label: '处理中', type: 'primary' }
  }

  // 计算订单金额
  const calculateOrderAmount = (order) => {
    return {
      total: order.total_amount,
      paid: order.paid_amount,
      unpaid: order.unpaid_amount,
      paid_rate: order.total_amount > 0 ? 
        Math.round((order.paid_amount / order.total_amount) * 100) : 0
    }
  }

  // 重置表单
  const resetOrderForm = () => {
    Object.assign(orderForm, {
      order_number: '',
      customer_name: '',
      status: '',
      order_type: '',
      payment_method: '',
      date_range: [],
      amount_min: '',
      amount_max: ''
    })
  }

  return {
    // 状态
    loading,
    orders,
    orderItems,
    orderForm,
    orderStats,
    orderDetail,
    
    // 计算属性
    getOrderStatistics,
    getSalesStatistics,
    getOrderTypeDistribution,
    
    // 方法
    fetchOrders,
    fetchOrderDetail,
    createOrder,
    updateOrderStatus,
    generateOrderNumber,
    checkOrderStatus,
    calculateOrderAmount,
    resetOrderForm
  }
}