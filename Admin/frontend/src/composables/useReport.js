import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'

// 报表类型枚举
export const REPORT_TYPES = {
  SALES: 'sales',
  FINANCE: 'finance', 
  INVENTORY: 'inventory',
  CUSTOMER: 'customer',
  PRODUCT: 'product',
  OPERATION: 'operation'
}

// 报表时间范围枚举
export const REPORT_TIME_RANGES = {
  TODAY: 'today',
  YESTERDAY: 'yesterday',
  THIS_WEEK: 'this_week',
  LAST_WEEK: 'last_week',
  THIS_MONTH: 'this_month',
  LAST_MONTH: 'last_month',
  THIS_QUARTER: 'this_quarter',
  LAST_QUARTER: 'last_quarter',
  THIS_YEAR: 'this_year',
  LAST_YEAR: 'last_year',
  CUSTOM: 'custom'
}

// 图表类型枚举
export const CHART_TYPES = {
  LINE: 'line',
  BAR: 'bar',
  PIE: 'pie',
  AREA: 'area',
  SCATTER: 'scatter',
  RADAR: 'radar',
  GAUGE: 'gauge'
}

// 销售报表指标
export const SALES_METRICS = {
  ORDER_COUNT: 'order_count',
  ORDER_AMOUNT: 'order_amount',
  AVERAGE_ORDER_VALUE: 'average_order_value',
  CONVERSION_RATE: 'conversion_rate',
  REFUND_RATE: 'refund_rate',
  CUSTOMER_COUNT: 'customer_count',
  NEW_CUSTOMER_COUNT: 'new_customer_count',
  REPEAT_CUSTOMER_COUNT: 'repeat_customer_count'
}

// 财务报表指标
export const FINANCE_METRICS = {
  REVENUE: 'revenue',
  PROFIT: 'profit',
  COST: 'cost',
  GROSS_MARGIN: 'gross_margin',
  NET_MARGIN: 'net_margin',
  ACCOUNTS_RECEIVABLE: 'accounts_receivable',
  ACCOUNTS_PAYABLE: 'accounts_payable',
  CASH_FLOW: 'cash_flow'
}

// 库存报表指标
export const INVENTORY_METRICS = {
  STOCK_QUANTITY: 'stock_quantity',
  STOCK_VALUE: 'stock_value',
  TURNOVER_RATE: 'turnover_rate',
  STOCKOUT_COUNT: 'stockout_count',
  OVERSTOCK_COUNT: 'overstock_count',
  EXPIRED_COUNT: 'expired_count',
  INBOUND_QUANTITY: 'inbound_quantity',
  OUTBOUND_QUANTITY: 'outbound_quantity'
}

// 客户报表指标
export const CUSTOMER_METRICS = {
  TOTAL_CUSTOMERS: 'total_customers',
  ACTIVE_CUSTOMERS: 'active_customers',
  NEW_CUSTOMERS: 'new_customers',
  LOST_CUSTOMERS: 'lost_customers',
  CUSTOMER_RETENTION: 'customer_retention',
  CUSTOMER_LIFETIME_VALUE: 'customer_lifetime_value',
  CUSTOMER_SATISFACTION: 'customer_satisfaction',
  CHURN_RATE: 'churn_rate'
}

// 模拟API函数
const mockApiCall = (delay = 500) => {
  return new Promise(resolve => setTimeout(resolve, delay))
}

// 获取时间范围
const getTimeRange = (range) => {
  const now = new Date()
  const start = new Date()
  const end = new Date()

  switch (range) {
    case REPORT_TIME_RANGES.TODAY:
      start.setHours(0, 0, 0, 0)
      end.setHours(23, 59, 59, 999)
      break
    case REPORT_TIME_RANGES.YESTERDAY:
      start.setDate(now.getDate() - 1)
      start.setHours(0, 0, 0, 0)
      end.setDate(now.getDate() - 1)
      end.setHours(23, 59, 59, 999)
      break
    case REPORT_TIME_RANGES.THIS_WEEK:
      start.setDate(now.getDate() - now.getDay())
      start.setHours(0, 0, 0, 0)
      break
    case REPORT_TIME_RANGES.LAST_WEEK:
      start.setDate(now.getDate() - now.getDay() - 7)
      start.setHours(0, 0, 0, 0)
      end.setDate(now.getDate() - now.getDay() - 1)
      end.setHours(23, 59, 59, 999)
      break
    case REPORT_TIME_RANGES.THIS_MONTH:
      start.setDate(1)
      start.setHours(0, 0, 0, 0)
      break
    case REPORT_TIME_RANGES.LAST_MONTH:
      start.setMonth(now.getMonth() - 1, 1)
      start.setHours(0, 0, 0, 0)
      end.setDate(0)
      end.setHours(23, 59, 59, 999)
      break
    case REPORT_TIME_RANGES.THIS_QUARTER:
      const quarter = Math.floor(now.getMonth() / 3)
      start.setMonth(quarter * 3, 1)
      start.setHours(0, 0, 0, 0)
      break
    case REPORT_TIME_RANGES.LAST_QUARTER:
      const lastQuarter = Math.floor((now.getMonth() - 3) / 3)
      start.setMonth(lastQuarter * 3, 1)
      start.setHours(0, 0, 0, 0)
      end.setMonth((lastQuarter + 1) * 3, 0)
      end.setHours(23, 59, 59, 999)
      break
    case REPORT_TIME_RANGES.THIS_YEAR:
      start.setMonth(0, 1)
      start.setHours(0, 0, 0, 0)
      break
    case REPORT_TIME_RANGES.LAST_YEAR:
      start.setFullYear(now.getFullYear() - 1, 0, 1)
      start.setHours(0, 0, 0, 0)
      end.setFullYear(now.getFullYear() - 1, 11, 31)
      end.setHours(23, 59, 59, 999)
      break
  }

  return { start, end }
}

// 生成模拟数据
const generateMockData = (type, range, metrics) => {
  const { start, end } = getTimeRange(range)
  const data = []
  const days = Math.ceil((end - start) / (1000 * 60 * 60 * 24))

  if (type === REPORT_TYPES.SALES) {
    for (let i = 0; i <= days; i++) {
      const date = new Date(start)
      date.setDate(start.getDate() + i)
      
      const baseValue = 10000 + Math.random() * 20000
      const weekendMultiplier = date.getDay() === 0 || date.getDay() === 6 ? 0.7 : 1
      
      data.push({
        date: date.toISOString().split('T')[0],
        orderCount: Math.floor((50 + Math.random() * 100) * weekendMultiplier),
        orderAmount: Math.floor(baseValue * weekendMultiplier),
        averageOrderValue: Math.floor(baseValue / (50 + Math.random() * 100)),
        conversionRate: (2 + Math.random() * 3).toFixed(2),
        refundRate: (0.5 + Math.random() * 2).toFixed(2),
        customerCount: Math.floor((30 + Math.random() * 70) * weekendMultiplier),
        newCustomerCount: Math.floor((10 + Math.random() * 30) * weekendMultiplier),
        repeatCustomerCount: Math.floor((20 + Math.random() * 40) * weekendMultiplier)
      })
    }
  } else if (type === REPORT_TYPES.FINANCE) {
    for (let i = 0; i <= days; i++) {
      const date = new Date(start)
      date.setDate(start.getDate() + i)
      
      const revenue = 50000 + Math.random() * 100000
      const cost = revenue * (0.6 + Math.random() * 0.2)
      
      data.push({
        date: date.toISOString().split('T')[0],
        revenue: Math.floor(revenue),
        cost: Math.floor(cost),
        profit: Math.floor(revenue - cost),
        grossMargin: ((revenue - cost) / revenue * 100).toFixed(2),
        netMargin: ((revenue - cost) / revenue * 100 - Math.random() * 5).toFixed(2),
        accountsReceivable: Math.floor(10000 + Math.random() * 50000),
        accountsPayable: Math.floor(5000 + Math.random() * 30000),
        cashFlow: Math.floor(-10000 + Math.random() * 20000)
      })
    }
  } else if (type === REPORT_TYPES.INVENTORY) {
    for (let i = 0; i <= days; i++) {
      const date = new Date(start)
      date.setDate(start.getDate() + i)
      
      data.push({
        date: date.toISOString().split('T')[0],
        stockQuantity: Math.floor(1000 + Math.random() * 5000),
        stockValue: Math.floor(50000 + Math.random() * 200000),
        turnoverRate: (2 + Math.random() * 8).toFixed(2),
        stockoutCount: Math.floor(Math.random() * 20),
        overstockCount: Math.floor(Math.random() * 50),
        expiredCount: Math.floor(Math.random() * 10),
        inboundQuantity: Math.floor(100 + Math.random() * 500),
        outboundQuantity: Math.floor(200 + Math.random() * 800)
      })
    }
  } else if (type === REPORT_TYPES.CUSTOMER) {
    for (let i = 0; i <= days; i++) {
      const date = new Date(start)
      date.setDate(start.getDate() + i)
      
      data.push({
        date: date.toISOString().split('T')[0],
        totalCustomers: Math.floor(1000 + i * 10 + Math.random() * 50),
        activeCustomers: Math.floor(800 + i * 8 + Math.random() * 40),
        newCustomers: Math.floor(20 + Math.random() * 30),
        lostCustomers: Math.floor(5 + Math.random() * 15),
        customerRetention: (85 + Math.random() * 10).toFixed(2),
        customerLifetimeValue: Math.floor(1000 + Math.random() * 2000),
        customerSatisfaction: (4 + Math.random()).toFixed(2),
        churnRate: (2 + Math.random() * 3).toFixed(2)
      })
    }
  }

  return data
}

// 生成汇总数据
const generateSummaryData = (type, range) => {
  const data = generateMockData(type, range)
  
  if (type === REPORT_TYPES.SALES) {
    const summary = {
      totalOrders: 0,
      totalAmount: 0,
      averageOrderValue: 0,
      totalCustomers: 0,
      newCustomers: 0,
      repeatCustomers: 0
    }
    
    data.forEach(item => {
      summary.totalOrders += item.orderCount
      summary.totalAmount += item.orderAmount
      summary.totalCustomers += item.customerCount
      summary.newCustomers += item.newCustomerCount
      summary.repeatCustomers += item.repeatCustomerCount
    })
    
    summary.averageOrderValue = Math.floor(summary.totalAmount / summary.totalOrders)
    
    return summary
  } else if (type === REPORT_TYPES.FINANCE) {
    const summary = {
      totalRevenue: 0,
      totalCost: 0,
      totalProfit: 0,
      averageGrossMargin: 0,
      averageNetMargin: 0,
      totalAccountsReceivable: 0,
      totalAccountsPayable: 0
    }
    
    let grossMarginSum = 0
    let netMarginSum = 0
    
    data.forEach(item => {
      summary.totalRevenue += item.revenue
      summary.totalCost += item.cost
      summary.totalProfit += item.profit
      summary.totalAccountsReceivable += item.accountsReceivable
      summary.totalAccountsPayable += item.accountsPayable
      grossMarginSum += parseFloat(item.grossMargin)
      netMarginSum += parseFloat(item.netMargin)
    })
    
    summary.averageGrossMargin = (grossMarginSum / data.length).toFixed(2)
    summary.averageNetMargin = (netMarginSum / data.length).toFixed(2)
    
    return summary
  } else if (type === REPORT_TYPES.INVENTORY) {
    const summary = {
      totalStockQuantity: 0,
      totalStockValue: 0,
      averageTurnoverRate: 0,
      totalStockoutCount: 0,
      totalOverstockCount: 0,
      totalExpiredCount: 0,
      totalInboundQuantity: 0,
      totalOutboundQuantity: 0
    }
    
    let turnoverRateSum = 0
    
    data.forEach(item => {
      summary.totalStockQuantity += item.stockQuantity
      summary.totalStockValue += item.stockValue
      summary.totalStockoutCount += item.stockoutCount
      summary.totalOverstockCount += item.overstockCount
      summary.totalExpiredCount += item.expiredCount
      summary.totalInboundQuantity += item.inboundQuantity
      summary.totalOutboundQuantity += item.outboundQuantity
      turnoverRateSum += parseFloat(item.turnoverRate)
    })
    
    summary.averageTurnoverRate = (turnoverRateSum / data.length).toFixed(2)
    
    return summary
  } else if (type === REPORT_TYPES.CUSTOMER) {
    const summary = {
      totalCustomers: 0,
      totalActiveCustomers: 0,
      totalNewCustomers: 0,
      totalLostCustomers: 0,
      averageRetention: 0,
      averageLifetimeValue: 0,
      averageSatisfaction: 0,
      averageChurnRate: 0
    }
    
    let retentionSum = 0
    let lifetimeValueSum = 0
    let satisfactionSum = 0
    let churnRateSum = 0
    
    data.forEach(item => {
      summary.totalCustomers = Math.max(summary.totalCustomers, item.totalCustomers)
      summary.totalActiveCustomers = Math.max(summary.totalActiveCustomers, item.activeCustomers)
      summary.totalNewCustomers += item.newCustomers
      summary.totalLostCustomers += item.lostCustomers
      retentionSum += parseFloat(item.customerRetention)
      lifetimeValueSum += item.customerLifetimeValue
      satisfactionSum += parseFloat(item.customerSatisfaction)
      churnRateSum += parseFloat(item.churnRate)
    })
    
    summary.averageRetention = (retentionSum / data.length).toFixed(2)
    summary.averageLifetimeValue = Math.floor(lifetimeValueSum / data.length)
    summary.averageSatisfaction = (satisfactionSum / data.length).toFixed(2)
    summary.averageChurnRate = (churnRateSum / data.length).toFixed(2)
    
    return summary
  }
  
  return {}
}

// 组合式函数
export function useReport() {
  const loading = ref(false)
  const reportData = ref([])
  const summaryData = ref({})
  const chartData = ref({})
  
  // 获取报表数据
  const getReportData = async (type, range, metrics = []) => {
    loading.value = true
    try {
      await mockApiCall()
      
      const data = generateMockData(type, range, metrics)
      reportData.value = data
      summaryData.value = generateSummaryData(type, range)
      
      // 生成图表数据
      chartData.value = {
        xAxis: data.map(item => item.date),
        series: metrics.length > 0 ? metrics : getDefaultMetrics(type),
        data: data
      }
      
      return data
    } catch (error) {
      ElMessage.error('获取报表数据失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  // 获取默认指标
  const getDefaultMetrics = (type) => {
    switch (type) {
      case REPORT_TYPES.SALES:
        return ['orderCount', 'orderAmount', 'averageOrderValue']
      case REPORT_TYPES.FINANCE:
        return ['revenue', 'profit', 'grossMargin']
      case REPORT_TYPES.INVENTORY:
        return ['stockQuantity', 'stockValue', 'turnoverRate']
      case REPORT_TYPES.CUSTOMER:
        return ['totalCustomers', 'activeCustomers', 'newCustomers']
      default:
        return []
    }
  }
  
  // 导出报表
  const exportReport = async (type, range, format = 'excel') => {
    loading.value = true
    try {
      await mockApiCall(1000)
      
      // 模拟导出
      const data = generateMockData(type, range)
      const summary = generateSummaryData(type, range)
      
      // 这里应该调用实际的导出API
      const exportData = {
        type,
        range,
        data,
        summary,
        exportTime: new Date().toISOString()
      }
      
      ElMessage.success('报表导出成功')
      return exportData
    } catch (error) {
      ElMessage.error('报表导出失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  // 获取报表模板
  const getReportTemplates = () => {
    return [
      {
        id: 1,
        name: '销售日报',
        type: REPORT_TYPES.SALES,
        timeRange: REPORT_TIME_RANGES.TODAY,
        metrics: ['orderCount', 'orderAmount', 'conversionRate'],
        chartType: CHART_TYPES.LINE,
        description: '每日销售数据统计'
      },
      {
        id: 2,
        name: '财务月报',
        type: REPORT_TYPES.FINANCE,
        timeRange: REPORT_TIME_RANGES.THIS_MONTH,
        metrics: ['revenue', 'profit', 'grossMargin'],
        chartType: CHART_TYPES.BAR,
        description: '月度财务数据分析'
      },
      {
        id: 3,
        name: '库存周报',
        type: REPORT_TYPES.INVENTORY,
        timeRange: REPORT_TIME_RANGES.THIS_WEEK,
        metrics: ['stockQuantity', 'turnoverRate', 'stockoutCount'],
        chartType: CHART_TYPES.AREA,
        description: '每周库存情况统计'
      },
      {
        id: 4,
        name: '客户分析',
        type: REPORT_TYPES.CUSTOMER,
        timeRange: REPORT_TIME_RANGES.THIS_MONTH,
        metrics: ['totalCustomers', 'newCustomers', 'customerRetention'],
        chartType: CHART_TYPES.PIE,
        description: '客户增长和留存分析'
      }
    ]
  }
  
  return {
    loading,
    reportData,
    summaryData,
    chartData,
    getReportData,
    exportReport,
    getReportTemplates,
    REPORT_TYPES,
    REPORT_TIME_RANGES,
    CHART_TYPES,
    SALES_METRICS,
    FINANCE_METRICS,
    INVENTORY_METRICS,
    CUSTOMER_METRICS
  }
}