import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

// 支付状态枚举
export const PAYMENT_STATUS = {
  PENDING: 'pending',      // 待支付
  PAID: 'paid',           // 已支付
  PARTIAL: 'partial',     // 部分支付
  REFUNDED: 'refunded',   // 已退款
  CANCELLED: 'cancelled'  // 已取消
}

// 支付方式枚举
export const PAYMENT_METHODS = {
  CASH: 'cash',              // 现金
  BANK_TRANSFER: 'bank_transfer', // 银行转账
  CREDIT: 'credit',          // 账期支付
  INSTALLMENT: 'installment',   // 分期付款
  ALIPAY: 'alipay',          // 支付宝
  WECHAT: 'wechat'           // 微信支付
}

// 退款原因枚举
export const REFUND_REASONS = {
  QUALITY_ISSUE: 'quality_issue',     // 商品质量问题
  ORDER_CANCELLED: 'order_cancelled', // 订单取消
  DUPLICATE_PAYMENT: 'duplicate_payment', // 重复支付
  OTHER: 'other'                      // 其他
}

// 分期状态枚举
export const INSTALLMENT_STATUS = {
  PENDING: 'pending',   // 待还款
  PAID: 'paid',        // 已还款
  OVERDUE: 'overdue'   // 逾期
}

// 使用组合式函数
export function usePayment() {
  // 状态管理
  const payments = ref([])
  const currentPayment = ref(null)
  const loading = ref(false)
  const error = ref(null)

  // 计算属性
  const totalPayments = computed(() => payments.value.length)
  const pendingPayments = computed(() => 
    payments.value.filter(p => p.status === PAYMENT_STATUS.PENDING)
  )
  const completedPayments = computed(() => 
    payments.value.filter(p => p.status === PAYMENT_STATUS.PAID)
  )
  const overduePayments = computed(() => 
    payments.value.filter(p => p.status === PAYMENT_STATUS.PENDING && isOverdue(p.dueDate))
  )

  // 工具函数
  const isOverdue = (dueDate) => {
    if (!dueDate) return false
    return new Date(dueDate) < new Date()
  }

  const calculateOverdueInterest = (amount, overdueDays, rate = 0.05) => {
    return amount * overdueDays * rate / 100
  }

  const generatePaymentNo = () => {
    const now = new Date()
    const dateStr = now.toISOString().slice(0, 10).replace(/-/g, '')
    const randomStr = Math.random().toString(36).substr(2, 6).toUpperCase()
    return `PAY${dateStr}${randomStr}`
  }

  const generateInstallmentPlan = (totalAmount, periods, downPayment = 0) => {
    const remainingAmount = totalAmount - downPayment
    if (periods <= 0 || remainingAmount <= 0) return []
    
    const perPeriodAmount = Math.round((remainingAmount / periods) * 100) / 100
    const plan = []
    
    for (let i = 0; i < periods; i++) {
      const dueDate = new Date()
      dueDate.setMonth(dueDate.getMonth() + i + 1)
      
      plan.push({
        period: i + 1,
        dueDate: dueDate.toISOString().split('T')[0],
        amount: i === periods - 1 ? remainingAmount - (perPeriodAmount * (periods - 1)) : perPeriodAmount,
        status: INSTALLMENT_STATUS.PENDING,
        paidDate: '',
        remark: `第${i + 1}期`
      })
    }
    
    return plan
  }

  // API 函数
  const getPayments = async (params = {}) => {
    loading.value = true
    error.value = null
    
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      // 模拟数据
      const mockPayments = [
        {
          id: 1,
          paymentNo: 'PAY202312010001',
          orderNo: 'ORD202312010001',
          customerName: '某某公司',
          paymentMethod: PAYMENT_METHODS.CREDIT,
          amount: 50000,
          status: PAYMENT_STATUS.PENDING,
          paymentDate: '2023-12-01',
          dueDate: '2024-01-01',
          createdAt: '2023-12-01 10:30:00',
          creditDays: 30,
          overdueDays: 0,
          overdueInterest: 0,
          totalAmount: 50000,
          installmentInfo: null,
          installmentPlan: [],
          bankName: '',
          bankAccount: '',
          accountName: '',
          transactionNo: '',
          vouchers: [],
          remark: '账期支付'
        },
        {
          id: 2,
          paymentNo: 'PAY202312020001',
          orderNo: 'ORD202312020001',
          customerName: '某某商贸',
          paymentMethod: PAYMENT_METHODS.INSTALLMENT,
          amount: 30000,
          status: PAYMENT_STATUS.PARTIAL,
          paymentDate: '2023-12-02',
          dueDate: '2024-02-02',
          createdAt: '2023-12-02 14:20:00',
          creditDays: 0,
          overdueDays: 0,
          overdueInterest: 0,
          totalAmount: 30000,
          installmentInfo: {
            current: 1,
            total: 3,
            perAmount: 10000
          },
          installmentPlan: generateInstallmentPlan(30000, 3, 0),
          bankName: '',
          bankAccount: '',
          accountName: '',
          transactionNo: '',
          vouchers: [],
          remark: '分期付款'
        },
        {
          id: 3,
          paymentNo: 'PAY202312030001',
          orderNo: 'ORD202312030001',
          customerName: '某某科技',
          paymentMethod: PAYMENT_METHODS.BANK_TRANSFER,
          amount: 80000,
          status: PAYMENT_STATUS.PAID,
          paymentDate: '2023-12-03',
          dueDate: '2023-12-03',
          createdAt: '2023-12-03 09:15:00',
          creditDays: 0,
          overdueDays: 0,
          overdueInterest: 0,
          totalAmount: 80000,
          installmentInfo: null,
          installmentPlan: [],
          bankName: '中国工商银行',
          bankAccount: '6222021234567890123',
          accountName: '某某科技有限公司',
          transactionNo: 'TRX202312030001',
          vouchers: [
            { name: '转账凭证1.jpg', url: '/uploads/voucher1.jpg' },
            { name: '转账凭证2.jpg', url: '/uploads/voucher2.jpg' }
          ],
          remark: '银行转账支付'
        }
      ]
      
      // 模拟分页和筛选
      let result = mockPayments
      
      if (params.paymentNo) {
        result = result.filter(p => p.paymentNo.includes(params.paymentNo))
      }
      
      if (params.orderNo) {
        result = result.filter(p => p.orderNo.includes(params.orderNo))
      }
      
      if (params.paymentMethod) {
        result = result.filter(p => p.paymentMethod === params.paymentMethod)
      }
      
      if (params.status) {
        result = result.filter(p => p.status === params.status)
      }
      
      if (params.startDate && params.endDate) {
        result = result.filter(p => 
          p.paymentDate >= params.startDate && p.paymentDate <= params.endDate
        )
      }
      
      const total = result.length
      const page = params.page || 1
      const pageSize = params.pageSize || 10
      const start = (page - 1) * pageSize
      const end = start + pageSize
      
      const paginatedResult = result.slice(start, end)
      
      return {
        success: true,
        data: {
          items: paginatedResult,
          total,
          page,
          pageSize
        }
      }
    } catch (err) {
      error.value = err.message
      ElMessage.error('获取支付列表失败')
      throw err
    } finally {
      loading.value = false
    }
  }

  const getPaymentById = async (id) => {
    loading.value = true
    error.value = null
    
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 300))
      
      // 模拟数据
      const payment = {
        id,
        paymentNo: 'PAY202312010001',
        orderNo: 'ORD202312010001',
        customerName: '某某公司',
        paymentMethod: PAYMENT_METHODS.CREDIT,
        amount: 50000,
        status: PAYMENT_STATUS.PENDING,
        paymentDate: '2023-12-01',
        dueDate: '2024-01-01',
        createdAt: '2023-12-01 10:30:00',
        updatedAt: '2023-12-01 10:30:00',
        creditDays: 30,
        overdueDays: 0,
        overdueInterest: 0,
        totalAmount: 50000,
        installmentInfo: null,
        installmentPlan: [],
        bankName: '',
        bankAccount: '',
        accountName: '',
        transactionNo: '',
        vouchers: [],
        remark: '账期支付'
      }
      
      currentPayment.value = payment
      return {
        success: true,
        data: payment
      }
    } catch (err) {
      error.value = err.message
      ElMessage.error('获取支付详情失败')
      throw err
    } finally {
      loading.value = false
    }
  }

  const createPayment = async (paymentData) => {
    loading.value = true
    error.value = null
    
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      // 生成支付编号
      const paymentNo = generatePaymentNo()
      
      const newPayment = {
        id: Date.now(),
        paymentNo,
        ...paymentData,
        status: PAYMENT_STATUS.PENDING,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
      
      // 处理分期计划
      if (paymentData.paymentMethod === PAYMENT_METHODS.INSTALLMENT && paymentData.installmentPeriods) {
        newPayment.installmentPlan = generateInstallmentPlan(
          paymentData.amount,
          paymentData.installmentPeriods,
          paymentData.downPayment || 0
        )
        newPayment.installmentInfo = {
          current: 0,
          total: paymentData.installmentPeriods,
          perAmount: Math.round(((paymentData.amount - (paymentData.downPayment || 0)) / paymentData.installmentPeriods) * 100) / 100
        }
      }
      
      payments.value.unshift(newPayment)
      
      ElMessage.success('支付记录创建成功')
      return {
        success: true,
        data: newPayment
      }
    } catch (err) {
      error.value = err.message
      ElMessage.error('创建支付记录失败')
      throw err
    } finally {
      loading.value = false
    }
  }

  const updatePayment = async (id, paymentData) => {
    loading.value = true
    error.value = null
    
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      const index = payments.value.findIndex(p => p.id === id)
      if (index !== -1) {
        payments.value[index] = {
          ...payments.value[index],
          ...paymentData,
          updatedAt: new Date().toISOString()
        }
        
        // 更新当前支付数据
        if (currentPayment.value && currentPayment.value.id === id) {
          currentPayment.value = payments.value[index]
        }
      }
      
      ElMessage.success('支付记录更新成功')
      return {
        success: true,
        data: payments.value[index]
      }
    } catch (err) {
      error.value = err.message
      ElMessage.error('更新支付记录失败')
      throw err
    } finally {
      loading.value = false
    }
  }

  const deletePayment = async (id) => {
    loading.value = true
    error.value = null
    
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 300))
      
      const index = payments.value.findIndex(p => p.id === id)
      if (index !== -1) {
        payments.value.splice(index, 1)
      }
      
      if (currentPayment.value && currentPayment.value.id === id) {
        currentPayment.value = null
      }
      
      ElMessage.success('支付记录删除成功')
      return {
        success: true
      }
    } catch (err) {
      error.value = err.message
      ElMessage.error('删除支付记录失败')
      throw err
    } finally {
      loading.value = false
    }
  }

  const confirmPayment = async (id) => {
    loading.value = true
    error.value = null
    
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 400))
      
      const index = payments.value.findIndex(p => p.id === id)
      if (index !== -1) {
        payments.value[index].status = PAYMENT_STATUS.PAID
        payments.value[index].paymentDate = new Date().toISOString().split('T')[0]
        payments.value[index].updatedAt = new Date().toISOString()
        
        // 更新当前支付数据
        if (currentPayment.value && currentPayment.value.id === id) {
          currentPayment.value = payments.value[index]
        }
      }
      
      ElMessage.success('支付确认成功')
      return {
        success: true,
        data: payments.value[index]
      }
    } catch (err) {
      error.value = err.message
      ElMessage.error('支付确认失败')
      throw err
    } finally {
      loading.value = false
    }
  }

  const refundPayment = async (id, refundData) => {
    loading.value = true
    error.value = null
    
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      const index = payments.value.findIndex(p => p.id === id)
      if (index !== -1) {
        payments.value[index].status = PAYMENT_STATUS.REFUNDED
        payments.value[index].refundAmount = refundData.amount
        payments.value[index].refundReason = refundData.reason
        payments.value[index].refundRemark = refundData.remark
        payments.value[index].refundDate = new Date().toISOString().split('T')[0]
        payments.value[index].updatedAt = new Date().toISOString()
        
        // 更新当前支付数据
        if (currentPayment.value && currentPayment.value.id === id) {
          currentPayment.value = payments.value[index]
        }
      }
      
      ElMessage.success('退款处理成功')
      return {
        success: true,
        data: payments.value[index]
      }
    } catch (err) {
      error.value = err.message
      ElMessage.error('退款处理失败')
      throw err
    } finally {
      loading.value = false
    }
  }

  const getPaymentStats = async () => {
    loading.value = true
    error.value = null
    
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 300))
      
      // 计算统计数据
      const today = new Date().toISOString().split('T')[0]
      const todayIncome = payments.value
        .filter(p => p.status === PAYMENT_STATUS.PAID && p.paymentDate === today)
        .reduce((sum, p) => sum + p.amount, 0)
      
      const pendingCount = payments.value.filter(p => p.status === PAYMENT_STATUS.PENDING).length
      
      const creditAmount = payments.value
        .filter(p => p.paymentMethod === PAYMENT_METHODS.CREDIT && p.status === PAYMENT_STATUS.PENDING)
        .reduce((sum, p) => sum + p.amount, 0)
      
      const overdueAmount = payments.value
        .filter(p => p.status === PAYMENT_STATUS.PENDING && isOverdue(p.dueDate))
        .reduce((sum, p) => sum + p.amount, 0)
      
      return {
        success: true,
        data: {
          todayIncome,
          pendingCount,
          creditAmount,
          overdueAmount
        }
      }
    } catch (err) {
      error.value = err.message
      ElMessage.error('获取支付统计失败')
      throw err
    } finally {
      loading.value = false
    }
  }

  // 分期相关函数
  const getInstallmentByPaymentId = async (paymentId) => {
    loading.value = true
    error.value = null
    
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 300))
      
      const payment = payments.value.find(p => p.id === paymentId)
      if (!payment || !payment.installmentPlan) {
        return {
          success: true,
          data: []
        }
      }
      
      return {
        success: true,
        data: payment.installmentPlan
      }
    } catch (err) {
      error.value = err.message
      ElMessage.error('获取分期计划失败')
      throw err
    } finally {
      loading.value = false
    }
  }

  const updateInstallmentStatus = async (paymentId, period, status, paidDate = null) => {
    loading.value = true
    error.value = null
    
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 400))
      
      const payment = payments.value.find(p => p.id === paymentId)
      if (!payment || !payment.installmentPlan) {
        throw new Error('支付记录或分期计划不存在')
      }
      
      const installment = payment.installmentPlan.find(i => i.period === period)
      if (!installment) {
        throw new Error('分期记录不存在')
      }
      
      installment.status = status
      installment.paidDate = paidDate || (status === INSTALLMENT_STATUS.PAID ? new Date().toISOString().split('T')[0] : '')
      
      // 更新分期信息
      const paidPeriods = payment.installmentPlan.filter(i => i.status === INSTALLMENT_STATUS.PAID).length
      payment.installmentInfo.current = paidPeriods
      
      ElMessage.success('分期状态更新成功')
      return {
        success: true,
        data: installment
      }
    } catch (err) {
      error.value = err.message
      ElMessage.error('更新分期状态失败')
      throw err
    } finally {
      loading.value = false
    }
  }

  // 账期相关函数
  const checkCreditOverdue = async () => {
    loading.value = true
    error.value = null
    
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 300))
      
      const overduePayments = payments.value.filter(p => 
        p.paymentMethod === PAYMENT_METHODS.CREDIT && 
        p.status === PAYMENT_STATUS.PENDING && 
        isOverdue(p.dueDate)
      )
      
      // 更新逾期信息
      overduePayments.forEach(payment => {
        const overdueDays = Math.floor((new Date() - new Date(payment.dueDate)) / (1000 * 60 * 60 * 24))
        payment.overdueDays = overdueDays
        payment.overdueInterest = calculateOverdueInterest(payment.amount, overdueDays)
        payment.totalAmount = payment.amount + payment.overdueInterest
      })
      
      return {
        success: true,
        data: overduePayments
      }
    } catch (err) {
      error.value = err.message
      ElMessage.error('检查账期逾期失败')
      throw err
    } finally {
      loading.value = false
    }
  }

  return {
    // 状态
    payments,
    currentPayment,
    loading,
    error,
    
    // 计算属性
    totalPayments,
    pendingPayments,
    completedPayments,
    overduePayments,
    
    // 工具函数
    isOverdue,
    calculateOverdueInterest,
    generatePaymentNo,
    generateInstallmentPlan,
    
    // API 函数
    getPayments,
    getPaymentById,
    createPayment,
    updatePayment,
    deletePayment,
    confirmPayment,
    refundPayment,
    getPaymentStats,
    
    // 分期相关
    getInstallmentByPaymentId,
    updateInstallmentStatus,
    
    // 账期相关
    checkCreditOverdue,
    
    // 枚举常量
    PAYMENT_STATUS,
    PAYMENT_METHODS,
    REFUND_REASONS,
    INSTALLMENT_STATUS
  }
}