import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'

// 库存操作类型
export const INVENTORY_OPERATIONS = {
  INBOUND: { label: '入库', value: 'inbound', type: 'success' },
  OUTBOUND: { label: '出库', value: 'outbound', type: 'danger' },
  TRANSFER: { label: '调拨', value: 'transfer', type: 'warning' },
  ADJUSTMENT: { label: '调整', value: 'adjustment', type: 'info' },
  CHECK: { label: '盘点', value: 'check', type: 'primary' }
}

// 库存状态
export const INVENTORY_STATUS = {
  AVAILABLE: { label: '可用', value: 'available', type: 'success' },
  LOCKED: { label: '锁定', value: 'locked', type: 'warning' },
  DAMAGED: { label: '损坏', value: 'damaged', type: 'danger' },
  EXPIRED: { label: '过期', value: 'expired', type: 'info' }
}

// 仓库类型
export const WAREHOUSE_TYPES = {
  CENTRAL: { label: '中心仓', value: 'central', type: 'primary' },
  REGIONAL: { label: '区域仓', value: 'regional', type: 'success' },
  FRONT: { label: '前置仓', value: 'front', type: 'warning' },
  VENDOR: { label: '供应商仓', value: 'vendor', type: 'info' }
}

// 库存预警级别
export const STOCK_ALERT_LEVELS = {
  SAFE: { label: '充足', value: 'safe', color: '#67C23A', threshold: 0.3 },
  WARNING: { label: '预警', value: 'warning', color: '#E6A23C', threshold: 0.2 },
  DANGER: { label: '危险', value: 'danger', color: '#F56C6C', threshold: 0.1 }
}

export function useInventory() {
  const loading = ref(false)
  const warehouses = ref([])
  const inventoryRecords = ref([])
  const stockAlerts = ref([])
  
  // 库存查询表单
  const inventoryForm = reactive({
    product_id: '',
    warehouse_id: '',
    batch_number: '',
    status: '',
    date_range: [],
    operation_type: ''
  })

  // 库存统计
  const inventoryStats = reactive({
    total_stock: 0,
    available_stock: 0,
    locked_stock: 0,
    total_value: 0,
    alert_count: 0
  })

  // 多仓库库存
  const multiWarehouseStock = ref([])

  // 获取仓库列表
  const fetchWarehouses = async () => {
    try {
      loading.value = true
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      warehouses.value = [
        {
          id: 1,
          name: '中心仓库',
          code: 'WH001',
          type: 'central',
          address: '北京市朝阳区',
          manager: '张三',
          phone: '13800138000',
          status: 'active',
          capacity: 10000,
          used_capacity: 7500,
          utilization_rate: 75
        },
        {
          id: 2,
          name: '华东仓库',
          code: 'WH002',
          type: 'regional',
          address: '上海市浦东新区',
          manager: '李四',
          phone: '13900139000',
          status: 'active',
          capacity: 8000,
          used_capacity: 5600,
          utilization_rate: 70
        },
        {
          id: 3,
          name: '华南仓库',
          code: 'WH003',
          type: 'regional',
          address: '深圳市南山区',
          manager: '王五',
          phone: '13700137000',
          status: 'active',
          capacity: 6000,
          used_capacity: 4800,
          utilization_rate: 80
        }
      ]
    } catch (error) {
      ElMessage.error('获取仓库列表失败')
      console.error(error)
    } finally {
      loading.value = false
    }
  }

  // 获取库存记录
  const fetchInventoryRecords = async (params = {}) => {
    try {
      loading.value = true
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      inventoryRecords.value = [
        {
          id: 1,
          product_name: 'iPhone 15 Pro Max',
          product_barcode: '1234567890123',
          warehouse_name: '中心仓库',
          operation_type: 'inbound',
          quantity: 100,
          before_stock: 50,
          after_stock: 150,
          batch_number: 'BATCH001',
          expiry_date: '2025-12-31',
          operator: '管理员',
          operate_time: '2024-01-15 10:30:00',
          remark: '采购入库'
        },
        {
          id: 2,
          product_name: 'MacBook Pro 16英寸',
          product_barcode: '1234567890124',
          warehouse_name: '华东仓库',
          operation_type: 'outbound',
          quantity: 20,
          before_stock: 80,
          after_stock: 60,
          batch_number: 'BATCH002',
          expiry_date: '2025-12-31',
          operator: '李四',
          operate_time: '2024-01-14 15:20:00',
          remark: '销售出库'
        }
      ]
    } catch (error) {
      ElMessage.error('获取库存记录失败')
      console.error(error)
    } finally {
      loading.value = false
    }
  }

  // 获取库存预警
  const fetchStockAlerts = async () => {
    try {
      loading.value = true
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      stockAlerts.value = [
        {
          id: 1,
          product_name: 'iPhone 15 Pro Max',
          product_barcode: '1234567890123',
          current_stock: 5,
          safety_stock: 20,
          alert_level: 'danger',
          warehouse_name: '中心仓库',
          last_inbound_date: '2024-01-10',
          suggested_quantity: 50,
          urgency: 'high'
        },
        {
          id: 2,
          product_name: 'AirPods Pro',
          product_barcode: '1234567890125',
          current_stock: 15,
          safety_stock: 30,
          alert_level: 'warning',
          warehouse_name: '华东仓库',
          last_inbound_date: '2024-01-12',
          suggested_quantity: 30,
          urgency: 'medium'
        }
      ]
      
      inventoryStats.alert_count = stockAlerts.value.length
    } catch (error) {
      ElMessage.error('获取库存预警失败')
      console.error(error)
    } finally {
      loading.value = false
    }
  }

  // 获取多仓库库存
  const fetchMultiWarehouseStock = async (productId) => {
    try {
      loading.value = true
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      multiWarehouseStock.value = [
        {
          warehouse_id: 1,
          warehouse_name: '中心仓库',
          total_stock: 150,
          available_stock: 140,
          locked_stock: 10,
          safety_stock: 20,
          alert_level: 'safe',
          last_update: '2024-01-15 10:00:00'
        },
        {
          warehouse_id: 2,
          warehouse_name: '华东仓库',
          total_stock: 80,
          available_stock: 75,
          locked_stock: 5,
          safety_stock: 15,
          alert_level: 'safe',
          last_update: '2024-01-15 09:30:00'
        }
      ]
    } catch (error) {
      ElMessage.error('获取多仓库库存失败')
      console.error(error)
    } finally {
      loading.value = false
    }
  }

  // 创建库存操作
  const createInventoryOperation = async (operationData) => {
    try {
      loading.value = true
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      const newRecord = {
        id: Date.now(),
        ...operationData,
        operate_time: new Date().toISOString()
      }
      
      inventoryRecords.value.unshift(newRecord)
      ElMessage.success('库存操作成功')
      return newRecord
    } catch (error) {
      ElMessage.error('库存操作失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 库存调拨
  const transferStock = async (transferData) => {
    try {
      loading.value = true
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1500))
      
      const { from_warehouse_id, to_warehouse_id, product_id, quantity } = transferData
      
      // 更新库存记录
      const fromRecord = {
        id: Date.now(),
        product_id,
        warehouse_name: warehouses.value.find(w => w.id === from_warehouse_id)?.name,
        operation_type: 'transfer_out',
        quantity: -quantity,
        operator: '管理员',
        operate_time: new Date().toISOString(),
        remark: `调拨到${warehouses.value.find(w => w.id === to_warehouse_id)?.name}`
      }
      
      const toRecord = {
        id: Date.now() + 1,
        product_id,
        warehouse_name: warehouses.value.find(w => w.id === to_warehouse_id)?.name,
        operation_type: 'transfer_in',
        quantity,
        operator: '管理员',
        operate_time: new Date().toISOString(),
        remark: `从${warehouses.value.find(w => w.id === from_warehouse_id)?.name}调拨`
      }
      
      inventoryRecords.value.unshift(toRecord)
      inventoryRecords.value.unshift(fromRecord)
      
      ElMessage.success('库存调拨成功')
    } catch (error) {
      ElMessage.error('库存调拨失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 库存盘点
  const checkInventory = async (checkData) => {
    try {
      loading.value = true
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 2000))
      
      const { product_id, warehouse_id, actual_quantity, system_quantity } = checkData
      const difference = actual_quantity - system_quantity
      
      const checkRecord = {
        id: Date.now(),
        product_id,
        warehouse_name: warehouses.value.find(w => w.id === warehouse_id)?.name,
        operation_type: 'check',
        quantity: difference,
        before_stock: system_quantity,
        after_stock: actual_quantity,
        operator: '管理员',
        operate_time: new Date().toISOString(),
        remark: difference > 0 ? `盘盈${difference}` : `盘亏${Math.abs(difference)}`
      }
      
      inventoryRecords.value.unshift(checkRecord)
      ElMessage.success('库存盘点完成')
    } catch (error) {
      ElMessage.error('库存盘点失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 获取库存统计
  const getInventoryStatistics = computed(() => {
    const totalStock = inventoryRecords.value.reduce((sum, record) => {
      return record.operation_type === 'inbound' ? sum + record.quantity : sum
    }, 0)
    
    const availableStock = totalStock - inventoryStats.locked_stock
    
    return {
      total_stock: totalStock,
      available_stock: availableStock,
      locked_stock: inventoryStats.locked_stock,
      utilization_rate: totalStock > 0 ? Math.round((availableStock / totalStock) * 100) : 0
    }
  })

  // 获取预警统计
  const getAlertStatistics = computed(() => {
    const alerts = stockAlerts.value
    const dangerCount = alerts.filter(a => a.alert_level === 'danger').length
    const warningCount = alerts.filter(a => a.alert_level === 'warning').length
    
    return {
      total: alerts.length,
      danger: dangerCount,
      warning: warningCount,
      danger_rate: alerts.length > 0 ? Math.round((dangerCount / alerts.length) * 100) : 0
    }
  })

  // 检查库存预警级别
  const checkStockAlertLevel = (currentStock, safetyStock) => {
    if (currentStock <= 0) return STOCK_ALERT_LEVELS.DANGER
    if (currentStock <= safetyStock * STOCK_ALERT_LEVELS.DANGER.threshold) return STOCK_ALERT_LEVELS.DANGER
    if (currentStock <= safetyStock * STOCK_ALERT_LEVELS.WARNING.threshold) return STOCK_ALERT_LEVELS.WARNING
    return STOCK_ALERT_LEVELS.SAFE
  }

  // 获取库存价值
  const calculateInventoryValue = (stock, price) => {
    return stock * price
  }

  // 重置表单
  const resetInventoryForm = () => {
    Object.assign(inventoryForm, {
      product_id: '',
      warehouse_id: '',
      batch_number: '',
      status: '',
      date_range: [],
      operation_type: ''
    })
  }

  return {
    // 状态
    loading,
    warehouses,
    inventoryRecords,
    stockAlerts,
    inventoryForm,
    inventoryStats,
    multiWarehouseStock,
    
    // 计算属性
    getInventoryStatistics,
    getAlertStatistics,
    
    // 方法
    fetchWarehouses,
    fetchInventoryRecords,
    fetchStockAlerts,
    fetchMultiWarehouseStock,
    createInventoryOperation,
    transferStock,
    checkInventory,
    checkStockAlertLevel,
    calculateInventoryValue,
    resetInventoryForm
  }
}