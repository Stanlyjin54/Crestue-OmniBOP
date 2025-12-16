/**
 * B2B图表组合式函数
 * 提供图表初始化、数据更新、销毁等功能
 */

import { ref, reactive, onUnmounted } from 'vue';

// 图表实例存储
const chartInstances = reactive({});

// 图表配置模板
const chartConfigs = {
  // 折线图配置
  line: {
    type: 'line',
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: true,
          position: 'top',
          labels: {
            color: 'var(--b2b-text-secondary)',
            font: {
              size: 24
            }
          }
        },
        tooltip: {
          backgroundColor: 'var(--b2b-bg-primary)',
          titleColor: 'var(--b2b-text-primary)',
          bodyColor: 'var(--b2b-text-secondary)',
          borderColor: 'var(--b2b-border-primary)',
          borderWidth: 2,
          cornerRadius: 12,
          displayColors: true
        }
      },
      scales: {
        x: {
          grid: {
            color: 'var(--b2b-border-secondary)',
            drawBorder: false
          },
          ticks: {
            color: 'var(--b2b-text-secondary)',
            font: {
              size: 20
            }
          }
        },
        y: {
          grid: {
            color: 'var(--b2b-border-secondary)',
            drawBorder: false
          },
          ticks: {
            color: 'var(--b2b-text-secondary)',
            font: {
              size: 20
            }
          }
        }
      },
      elements: {
        line: {
          tension: 0.4,
          borderWidth: 4
        },
        point: {
          radius: 6,
          hoverRadius: 8,
          borderWidth: 2
        }
      }
    }
  },
  
  // 柱状图配置
  bar: {
    type: 'bar',
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: true,
          position: 'top',
          labels: {
            color: 'var(--b2b-text-secondary)',
            font: {
              size: 24
            }
          }
        },
        tooltip: {
          backgroundColor: 'var(--b2b-bg-primary)',
          titleColor: 'var(--b2b-text-primary)',
          bodyColor: 'var(--b2b-text-secondary)',
          borderColor: 'var(--b2b-border-primary)',
          borderWidth: 2,
          cornerRadius: 12,
          displayColors: true
        }
      },
      scales: {
        x: {
          grid: {
            display: false
          },
          ticks: {
            color: 'var(--b2b-text-secondary)',
            font: {
              size: 20
            }
          }
        },
        y: {
          grid: {
            color: 'var(--b2b-border-secondary)',
            drawBorder: false
          },
          ticks: {
            color: 'var(--b2b-text-secondary)',
            font: {
              size: 20
            }
          }
        }
      },
      elements: {
        bar: {
          borderRadius: 8,
          borderWidth: 0
        }
      }
    }
  },
  
  // 饼图配置
  pie: {
    type: 'pie',
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: true,
          position: 'bottom',
          labels: {
            color: 'var(--b2b-text-secondary)',
            font: {
              size: 24
            },
            padding: 20
          }
        },
        tooltip: {
          backgroundColor: 'var(--b2b-bg-primary)',
          titleColor: 'var(--b2b-text-primary)',
          bodyColor: 'var(--b2b-text-secondary)',
          borderColor: 'var(--b2b-border-primary)',
          borderWidth: 2,
          cornerRadius: 12,
          displayColors: true,
          callbacks: {
            label: function(context) {
              const label = context.label || '';
              const value = context.parsed;
              const total = context.dataset.data.reduce((a, b) => a + b, 0);
              const percentage = ((value / total) * 100).toFixed(1);
              return `${label}: ${value} (${percentage}%)`;
            }
          }
        }
      },
      elements: {
        arc: {
          borderWidth: 2,
          borderColor: 'var(--b2b-bg-primary)'
        }
      }
    }
  },
  
  // 环形图配置
  doughnut: {
    type: 'doughnut',
    options: {
      responsive: true,
      maintainAspectRatio: false,
      cutout: '60%',
      plugins: {
        legend: {
          display: true,
          position: 'bottom',
          labels: {
            color: 'var(--b2b-text-secondary)',
            font: {
              size: 24
            },
            padding: 20
          }
        },
        tooltip: {
          backgroundColor: 'var(--b2b-bg-primary)',
          titleColor: 'var(--b2b-text-primary)',
          bodyColor: 'var(--b2b-text-secondary)',
          borderColor: 'var(--b2b-border-primary)',
          borderWidth: 2,
          cornerRadius: 12,
          displayColors: true,
          callbacks: {
            label: function(context) {
              const label = context.label || '';
              const value = context.parsed;
              const total = context.dataset.data.reduce((a, b) => a + b, 0);
              const percentage = ((value / total) * 100).toFixed(1);
              return `${label}: ${value} (${percentage}%)`;
            }
          }
        }
      },
      elements: {
        arc: {
          borderWidth: 2,
          borderColor: 'var(--b2b-bg-primary)'
        }
      }
    }
  },
  
  // 雷达图配置
  radar: {
    type: 'radar',
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: true,
          position: 'top',
          labels: {
            color: 'var(--b2b-text-secondary)',
            font: {
              size: 24
            }
          }
        },
        tooltip: {
          backgroundColor: 'var(--b2b-bg-primary)',
          titleColor: 'var(--b2b-text-primary)',
          bodyColor: 'var(--b2b-text-secondary)',
          borderColor: 'var(--b2b-border-primary)',
          borderWidth: 2,
          cornerRadius: 12,
          displayColors: true
        }
      },
      scales: {
        r: {
          grid: {
            color: 'var(--b2b-border-secondary)'
          },
          angleLines: {
            color: 'var(--b2b-border-secondary)'
          },
          pointLabels: {
            color: 'var(--b2b-text-secondary)',
            font: {
              size: 20
            }
          },
          ticks: {
            display: false
          }
        }
      },
      elements: {
        line: {
          borderWidth: 3
        },
        point: {
          radius: 4,
          hoverRadius: 6
        }
      }
    }
  }
};

// B2B默认颜色配置
const b2bColors = {
  primary: '#409eff',
  secondary: '#67c23a',
  warning: '#e6a23c',
  danger: '#f56c6c',
  info: '#909399',
  success: '#67c23a',
  gradient: ['#409eff', '#67c23a', '#e6a23c', '#f56c6c']
};

// 初始化图表
const initChart = (canvasId, config) => {
  try {
    // 获取画布上下文
    const query = uni.createSelectorQuery();
    query.select(`#${canvasId}`).fields({ node: true, size: true });
    query.exec((res) => {
      if (!res[0]) return;
      
      const canvas = res[0].node;
      const ctx = canvas.getContext('2d');
      
      // 设置画布尺寸
      const dpr = uni.getSystemInfoSync().pixelRatio;
      canvas.width = res[0].width * dpr;
      canvas.height = res[0].height * dpr;
      ctx.scale(dpr, dpr);
      
      // 合并配置
      const mergedConfig = mergeChartConfig(config);
      
      // 创建图表实例
      const chart = new Chart(ctx, mergedConfig);
      
      // 存储图表实例
      chartInstances[canvasId] = {
        chart,
        canvas,
        ctx,
        config: mergedConfig
      };
      
      console.log(`Chart ${canvasId} initialized successfully`);
    });
  } catch (error) {
    console.error(`Failed to initialize chart ${canvasId}:`, error);
  }
};

// 合并图表配置
const mergeChartConfig = (userConfig) => {
  const baseConfig = chartConfigs[userConfig.type] || chartConfigs.line;
  
  return {
    ...baseConfig,
    ...userConfig,
    options: {
      ...baseConfig.options,
      ...userConfig.options,
      plugins: {
        ...baseConfig.options.plugins,
        ...userConfig.options.plugins
      },
      scales: {
        ...baseConfig.options.scales,
        ...userConfig.options.scales
      }
    }
  };
};

// 更新图表数据
const updateChart = (canvasId, newData) => {
  const chartInstance = chartInstances[canvasId];
  if (!chartInstance) {
    console.warn(`Chart ${canvasId} not found`);
    return;
  }
  
  try {
    const { chart } = chartInstance;
    
    // 更新数据
    if (newData.labels) {
      chart.data.labels = newData.labels;
    }
    
    if (newData.datasets) {
      chart.data.datasets = newData.datasets;
    }
    
    // 更新配置
    if (newData.options) {
      Object.assign(chart.options, newData.options);
    }
    
    // 重新渲染图表
    chart.update('active');
    
    console.log(`Chart ${canvasId} updated successfully`);
  } catch (error) {
    console.error(`Failed to update chart ${canvasId}:`, error);
  }
};

// 销毁图表
const destroyChart = (canvasId) => {
  const chartInstance = chartInstances[canvasId];
  if (!chartInstance) {
    return;
  }
  
  try {
    const { chart } = chartInstance;
    chart.destroy();
    delete chartInstances[canvasId];
    
    console.log(`Chart ${canvasId} destroyed successfully`);
  } catch (error) {
    console.error(`Failed to destroy chart ${canvasId}:`, error);
  }
};

// 重新调整图表尺寸
const resizeChart = (canvasId) => {
  const chartInstance = chartInstances[canvasId];
  if (!chartInstance) {
    return;
  }
  
  try {
    const { chart, canvas } = chartInstance;
    
    // 获取新的尺寸
    const query = uni.createSelectorQuery();
    query.select(`#${canvasId}`).boundingClientRect();
    query.exec((res) => {
      if (res[0]) {
        const dpr = uni.getSystemInfoSync().pixelRatio;
        canvas.width = res[0].width * dpr;
        canvas.height = res[0].height * dpr;
        
        chart.resize();
      }
    });
  } catch (error) {
    console.error(`Failed to resize chart ${canvasId}:`, error);
  }
};

// 生成图表数据
const generateChartData = (type, options = {}) => {
  const {
    labels = [],
    datasets = 1,
    dataPoints = 7,
    minValue = 0,
    maxValue = 100,
    colors = b2bColors.gradient
  } = options;
  
  const chartData = {
    labels: labels.length > 0 ? labels : generateLabels(dataPoints, type),
    datasets: []
  };
  
  for (let i = 0; i < datasets; i++) {
    const dataset = {
      label: `数据集 ${i + 1}`,
      data: generateRandomData(dataPoints, minValue, maxValue),
      backgroundColor: colors[i % colors.length],
      borderColor: colors[i % colors.length],
      borderWidth: 2,
      fill: type === 'line' ? false : true
    };
    
    // 特殊配置
    if (type === 'line') {
      dataset.tension = 0.4;
      dataset.pointRadius = 6;
      dataset.pointHoverRadius = 8;
    } else if (type === 'bar') {
      dataset.borderRadius = 8;
    } else if (type === 'radar') {
      dataset.fill = true;
      dataset.backgroundColor = colors[i % colors.length] + '20';
    }
    
    chartData.datasets.push(dataset);
  }
  
  return chartData;
};

// 生成标签
const generateLabels = (count, type) => {
  const labels = [];
  
  if (type === 'line' || type === 'bar') {
    for (let i = 1; i <= count; i++) {
      labels.push(`标签${i}`);
    }
  } else if (type === 'pie' || type === 'doughnut') {
    const categories = ['产品A', '产品B', '产品C', '产品D', '产品E', '产品F'];
    for (let i = 0; i < count; i++) {
      labels.push(categories[i % categories.length]);
    }
  } else if (type === 'radar') {
    const dimensions = ['质量', '价格', '服务', '速度', '创新', '可靠性'];
    for (let i = 0; i < count; i++) {
      labels.push(dimensions[i % dimensions.length]);
    }
  }
  
  return labels;
};

// 生成随机数据
const generateRandomData = (count, min, max) => {
  const data = [];
  for (let i = 0; i < count; i++) {
    data.push(Math.floor(Math.random() * (max - min + 1)) + min);
  }
  return data;
};

// 创建销售趋势图表
const createSalesTrendChart = (canvasId, data, options = {}) => {
  const chartData = {
    labels: data.labels || ['1月', '2月', '3月', '4月', '5月', '6月'],
    datasets: [{
      label: '销售额',
      data: data.values || [12000, 19000, 15000, 25000, 22000, 30000],
      borderColor: b2bColors.primary,
      backgroundColor: b2bColors.primary + '20',
      tension: 0.4,
      fill: true
    }]
  };
  
  const config = {
    type: 'line',
    data: chartData,
    options: {
      ...chartConfigs.line.options,
      ...options
    }
  };
  
  initChart(canvasId, config);
};

// 创建产品销售占比图表
const createProductSalesChart = (canvasId, data, options = {}) => {
  const chartData = {
    labels: data.labels || ['手机', '电脑', '配件', '其他'],
    datasets: [{
      data: data.values || [35, 25, 20, 20],
      backgroundColor: [
        b2bColors.primary,
        b2bColors.success,
        b2bColors.warning,
        b2bColors.danger
      ],
      borderWidth: 0
    }]
  };
  
  const config = {
    type: 'doughnut',
    data: chartData,
    options: {
      ...chartConfigs.doughnut.options,
      ...options
    }
  };
  
  initChart(canvasId, config);
};

// 创建客户分析雷达图
const createCustomerRadarChart = (canvasId, data, options = {}) => {
  const chartData = {
    labels: data.labels || ['质量', '价格', '服务', '速度', '创新', '可靠性'],
    datasets: [{
      label: '客户满意度',
      data: data.values || [85, 90, 88, 92, 85, 95],
      borderColor: b2bColors.primary,
      backgroundColor: b2bColors.primary + '30',
      pointBackgroundColor: b2bColors.primary,
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: b2bColors.primary
    }]
  };
  
  const config = {
    type: 'radar',
    data: chartData,
    options: {
      ...chartConfigs.radar.options,
      ...options
    }
  };
  
  initChart(canvasId, config);
};

// 清理所有图表
const cleanupCharts = () => {
  Object.keys(chartInstances).forEach(canvasId => {
    destroyChart(canvasId);
  });
};

// 生命周期管理
onUnmounted(() => {
  cleanupCharts();
});

// 组合式函数
export function useChart() {
  return {
    // 基础功能
    initChart,
    updateChart,
    destroyChart,
    resizeChart,
    
    // 数据生成
    generateChartData,
    generateLabels,
    generateRandomData,
    
    // 预设图表
    createSalesTrendChart,
    createProductSalesChart,
    createCustomerRadarChart,
    
    // 工具函数
    cleanupCharts,
    mergeChartConfig,
    
    // 状态
    chartInstances,
    chartConfigs,
    b2bColors
  };
}

// 导出工具函数
export const chartUtils = {
  // 格式化图表数据
  formatChartData(data, type = 'number') {
    if (type === 'currency') {
      return data.map(value => `¥${value.toLocaleString()}`);
    } else if (type === 'percentage') {
      return data.map(value => `${value}%`);
    } else if (type === 'number') {
      return data.map(value => value.toLocaleString());
    }
    return data;
  },
  
  // 生成时间序列数据
  generateTimeSeriesData(startDate, endDate, interval = 'day') {
    const data = [];
    const labels = [];
    let current = new Date(startDate);
    
    while (current <= endDate) {
      labels.push(current.toLocaleDateString());
      data.push(Math.floor(Math.random() * 1000) + 100);
      
      if (interval === 'day') {
        current.setDate(current.getDate() + 1);
      } else if (interval === 'week') {
        current.setDate(current.getDate() + 7);
      } else if (interval === 'month') {
        current.setMonth(current.getMonth() + 1);
      }
    }
    
    return { labels, data };
  },
  
  // 计算数据趋势
  calculateTrend(data) {
    if (data.length < 2) return 0;
    
    const first = data[0];
    const last = data[data.length - 1];
    const change = last - first;
    const percentage = (change / first) * 100;
    
    return {
      change,
      percentage: percentage.toFixed(2),
      trend: percentage > 0 ? 'up' : percentage < 0 ? 'down' : 'stable'
    };
  },
  
  // 获取颜色方案
  getColorScheme(count, type = 'gradient') {
    const schemes = {
      gradient: [
        '#409eff', '#67c23a', '#e6a23c', '#f56c6c',
        '#909399', '#00d4aa', '#ff6b6b', '#4ecdc4'
      ],
      pastel: [
        '#a8d8ea', '#aa96da', '#fcbad3', '#ffffd2',
        '#84dcc6', '#ff9a8b', '#a8e6cf', '#dcedc1'
      ],
      vibrant: [
        '#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4',
        '#ffeaa7', '#dda0dd', '#98d8c8', '#f7dc6f'
      ]
    };
    
    const colors = schemes[type] || schemes.gradient;
    return colors.slice(0, count);
  }
};