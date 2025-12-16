/**
 * 管理后台图表组合式函数
 * 基于B2B图表系统进行定制化
 */

import { ref, onMounted, onUnmounted, watch, computed } from 'vue'
import * as echarts from 'echarts'

// 图表实例管理
const chartInstances = new Map()

/**
 * 基础图表组合式函数
 */
export function useChart(containerRef, options = {}) {
  const chartInstance = ref(null)
  const isLoading = ref(false)
  const chartData = ref([])

  // 初始化图表
  const initChart = () => {
    if (!containerRef.value) return

    // 销毁已存在的实例
    if (chartInstance.value) {
      chartInstance.value.dispose()
    }

    chartInstance.value = echarts.init(containerRef.value)
    
    // 设置基础配置
    const baseOption = getBaseOption(options.type)
    const mergedOption = mergeOptions(baseOption, options)
    
    chartInstance.value.setOption(mergedOption)
    
    // 添加到实例管理
    chartInstances.set(containerRef.value, chartInstance.value)
    
    // 监听窗口大小变化
    window.addEventListener('resize', handleResize)
  }

  // 更新图表数据
  const updateChart = (newData, newOptions = {}) => {
    if (!chartInstance.value) return
    
    chartData.value = newData
    isLoading.value = false
    
    const updatedOption = mergeOptions(
      chartInstance.value.getOption(),
      { ...newOptions, series: [{ ...newOptions.series?.[0], data: newData }] }
    )
    
    chartInstance.value.setOption(updatedOption, true)
  }

  // 销毁图表
  const disposeChart = () => {
    if (chartInstance.value) {
      chartInstance.value.dispose()
      chartInstances.delete(containerRef.value)
      chartInstance.value = null
    }
    window.removeEventListener('resize', handleResize)
  }

  // 处理窗口大小变化
  const handleResize = () => {
    if (chartInstance.value) {
      chartInstance.value.resize()
    }
  }

  // 获取基础配置
  const getBaseOption = (type) => {
    const baseOptions = {
      line: getLineChartBaseOption(),
      bar: getBarChartBaseOption(),
      pie: getPieChartBaseOption(),
      ring: getRingChartBaseOption(),
      radar: getRadarChartBaseOption()
    }
    return baseOptions[type] || getLineChartBaseOption()
  }

  // 合并配置选项
  const mergeOptions = (base, custom) => {
    return deepMerge(base, custom)
  }

  // 深度合并对象
  const deepMerge = (target, source) => {
    const result = { ...target }
    for (const key in source) {
      if (source[key] && typeof source[key] === 'object' && !Array.isArray(source[key])) {
        result[key] = deepMerge(result[key] || {}, source[key])
      } else {
        result[key] = source[key]
      }
    }
    return result
  }

  onMounted(() => {
    initChart()
  })

  onUnmounted(() => {
    disposeChart()
  })

  return {
    chartInstance,
    isLoading,
    chartData,
    updateChart,
    disposeChart
  }
}

/**
 * 管理后台专用图表组合式函数
 */
export function useAdminChart() {
  // 销售趋势图表
  const createSalesTrendChart = (containerRef, data = []) => {
    return useChart(containerRef, {
      type: 'line',
      title: {
        text: '销售趋势分析',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis',
        formatter: '{b}<br/>{a}: {c}万元'
      },
      xAxis: {
        type: 'category',
        data: data.map(item => item.date)
      },
      yAxis: {
        type: 'value',
        name: '销售额(万元)'
      },
      series: [{
        name: '销售额',
        type: 'line',
        data: data.map(item => item.amount),
        smooth: true,
        areaStyle: {
          opacity: 0.3
        }
      }]
    })
  }

  // 订单状态分布图表
  const createOrderStatusChart = (containerRef, data = []) => {
    return useChart(containerRef, {
      type: 'pie',
      title: {
        text: '订单状态分布',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [{
        name: '订单状态',
        type: 'pie',
        radius: ['40%', '70%'],
        data: data,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }]
    })
  }

  // 产品销量排行图表
  const createProductSalesChart = (containerRef, data = []) => {
    return useChart(containerRef, {
      type: 'bar',
      title: {
        text: '产品销量排行',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      xAxis: {
        type: 'value',
        name: '销量'
      },
      yAxis: {
        type: 'category',
        data: data.map(item => item.name)
      },
      series: [{
        name: '销量',
        type: 'bar',
        data: data.map(item => item.sales),
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#83bff6' },
            { offset: 0.5, color: '#188df0' },
            { offset: 1, color: '#188df0' }
          ])
        }
      }]
    })
  }

  // 客户增长趋势图表
  const createCustomerGrowthChart = (containerRef, data = []) => {
    return useChart(containerRef, {
      type: 'line',
      title: {
        text: '客户增长趋势',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['新增客户', '活跃客户'],
        top: 30
      },
      xAxis: {
        type: 'category',
        data: data.map(item => item.month)
      },
      yAxis: {
        type: 'value',
        name: '客户数量'
      },
      series: [
        {
          name: '新增客户',
          type: 'line',
          data: data.map(item => item.newCustomers),
          smooth: true
        },
        {
          name: '活跃客户',
          type: 'line',
          data: data.map(item => item.activeCustomers),
          smooth: true
        }
      ]
    })
  }

  // 库存预警图表
  const createInventoryAlertChart = (containerRef, data = []) => {
    return useChart(containerRef, {
      type: 'bar',
      title: {
        text: '库存预警分析',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      xAxis: {
        type: 'category',
        data: data.map(item => item.warehouse)
      },
      yAxis: {
        type: 'value',
        name: '预警商品数'
      },
      series: [{
        name: '预警商品',
        type: 'bar',
        data: data.map(item => item.alertCount),
        itemStyle: {
          color: function(params) {
            const colors = ['#ff4d4f', '#faad14', '#52c41a']
            return params.value > 20 ? colors[0] : params.value > 10 ? colors[1] : colors[2]
          }
        }
      }]
    })
  }

  return {
    createSalesTrendChart,
    createOrderStatusChart,
    createProductSalesChart,
    createCustomerGrowthChart,
    createInventoryAlertChart
  }
}

/**
 * 基础图表配置模板
 */
function getLineChartBaseOption() {
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'line'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false
    },
    yAxis: {
      type: 'value'
    }
  }
}

function getBarChartBaseOption() {
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category'
    },
    yAxis: {
      type: 'value'
    }
  }
}

function getPieChartBaseOption() {
  return {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    }
  }
}

function getRingChartBaseOption() {
  return {
    ...getPieChartBaseOption(),
    series: [{
      type: 'pie',
      radius: ['40%', '70%']
    }]
  }
}

function getRadarChartBaseOption() {
  return {
    tooltip: {},
    radar: {
      indicator: []
    }
  }
}

/**
 * 工具函数
 */
export const chartUtils = {
  // 格式化数字
  formatNumber: (num) => {
    if (num >= 10000) {
      return (num / 10000).toFixed(1) + '万'
    }
    return num.toString()
  },

  // 格式化货币
  formatCurrency: (num) => {
    return '¥' + num.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
  },

  // 生成时间序列
  generateTimeSeries: (startDate, endDate, interval = 'day') => {
    const dates = []
    const start = new Date(startDate)
    const end = new Date(endDate)
    
    while (start <= end) {
      dates.push(start.toISOString().split('T')[0])
      if (interval === 'day') {
        start.setDate(start.getDate() + 1)
      } else if (interval === 'week') {
        start.setDate(start.getDate() + 7)
      } else if (interval === 'month') {
        start.setMonth(start.getMonth() + 1)
      }
    }
    
    return dates
  },

  // 计算趋势
  calculateTrend: (data) => {
    if (data.length < 2) return 0
    const first = data[0]
    const last = data[data.length - 1]
    return ((last - first) / first * 100).toFixed(2)
  },

  // 获取颜色方案
  getColorScheme: (count) => {
    const colors = [
      '#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399',
      '#00c0ef', '#f39c12', '#00a65a', '#f56954', '#d2d6de'
    ]
    return colors.slice(0, count)
  }
}