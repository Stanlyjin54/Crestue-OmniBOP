/**
 * B2B库存查询组件
 * 支持多仓库库存查询、库存预警、库存调拨等
 */

<template>
  <view class="b2b-inventory-query">
    <!-- 搜索区域 -->
    <view class="search-section">
      <view class="search-bar">
        <uni-search-bar 
          v-model="searchKeyword" 
          placeholder="搜索商品名称、编码、SKU"
          @search="handleSearch"
          @clear="handleClear"
        />
      </view>
      
      <view class="filter-options">
        <picker mode="selector" :range="warehouseOptions" @change="handleWarehouseChange">
          <view class="filter-item">
            <text>{{ selectedWarehouse ? selectedWarehouse.warehouse_name : '全部仓库' }}</text>
            <uni-icons type="arrowdown" size="12" />
          </view>
        </picker>
        
        <picker mode="selector" :range="categoryOptions" @change="handleCategoryChange">
          <view class="filter-item">
            <text>{{ selectedCategory ? selectedCategory.category_name : '全部分类' }}</text>
            <uni-icons type="arrowdown" size="12" />
          </view>
        </picker>
        
        <picker mode="selector" :range="stockStatusOptions" @change="handleStockStatusChange">
          <view class="filter-item">
            <text>{{ selectedStockStatus ? selectedStockStatus.text : '库存状态' }}</text>
            <uni-icons type="arrowdown" size="12" />
          </view>
        </picker>
      </view>
    </view>

    <!-- 统计信息 -->
    <view class="stats-section">
      <view class="stat-card">
        <text class="stat-label">总库存</text>
        <text class="stat-value">{{ formatNumber(totalStats.total_quantity) }}</text>
      </view>
      <view class="stat-card">
        <text class="stat-label">预警商品</text>
        <text class="stat-value warning">{{ totalStats.warning_count }}</text>
      </view>
      <view class="stat-card">
        <text class="stat-label">缺货商品</text>
        <text class="stat-value danger">{{ totalStats.out_of_stock_count }}</text>
      </view>
    </view>

    <!-- 库存列表 -->
    <view class="inventory-list-section">
      <view v-if="inventoryList.length > 0" class="inventory-list">
        <view v-for="item in inventoryList" :key="item.inventory_id" class="inventory-item">
          <view class="item-header">
            <image class="product-image" :src="item.product_image" mode="aspectFill" />
            <view class="product-info">
              <text class="product-name">{{ item.product_name }}</text>
              <text class="product-code">{{ item.product_code }}</text>
              <text class="product-spec">{{ item.product_spec }}</text>
            </view>
            <view class="stock-status" :class="getStockStatusClass(item)">
              <text class="status-text">{{ getStockStatusText(item) }}</text>
            </view>
          </view>
          
          <view class="item-details">
            <view class="detail-row">
              <text class="detail-label">仓库：</text>
              <text class="detail-value">{{ item.warehouse_name }}</text>
            </view>
            <view class="detail-row">
              <text class="detail-label">可用库存：</text>
              <text class="detail-value quantity">{{ item.available_quantity }} {{ item.unit }}</text>
            </view>
            <view class="detail-row">
              <text class="detail-label">冻结库存：</text>
              <text class="detail-value">{{ item.frozen_quantity }} {{ item.unit }}</text>
            </view>
            <view class="detail-row">
              <text class="detail-label">安全库存：</text>
              <text class="detail-value">{{ item.safety_stock }} {{ item.unit }}</text>
            </view>
            <view v-if="item.warning_message" class="detail-row warning">
              <uni-icons type="warning" size="16" color="#f56c6c" />
              <text class="warning-text">{{ item.warning_message }}</text>
            </view>
          </view>
          
          <view class="item-actions">
            <button class="action-btn" @click="viewInventoryDetail(item)">详情</button>
            <button class="action-btn" @click="viewInventoryLog(item)">流水</button>
            <button 
              v-if="item.available_quantity < item.safety_stock" 
              class="action-btn warning"
              @click="handleStockWarning(item)"
            >
              预警
            </button>
            <button 
              class="action-btn primary"
              @click="transferInventory(item)"
            >
              调拨
            </button>
          </view>
        </view>
      </view>
      
      <view v-else class="empty-state">
        <uni-icons type="search" size="64" color="#909399" />
        <text class="empty-text">暂无库存数据</text>
      </view>
    </view>

    <!-- 批量操作 -->
    <view class="batch-actions" v-if="selectedItems.length > 0">
      <view class="selection-info">
        <text>已选择 {{ selectedItems.length }} 个商品</text>
      </view>
      <view class="action-buttons">
        <button class="batch-btn" @click="batchExport">导出</button>
        <button class="batch-btn" @click="batchPrint">打印</button>
        <button class="batch-btn warning" @click="batchSetSafetyStock">设置安全库存</button>
      </view>
    </view>

    <!-- 库存调拨弹窗 -->
    <uni-popup ref="transferPopup" type="bottom">
      <view class="transfer-popup">
        <view class="popup-header">
          <text class="popup-title">库存调拨</text>
          <uni-icons type="closeempty" size="24" @click="closeTransferPopup" />
        </view>
        
        <view class="transfer-form">
          <view class="form-item">
            <text class="form-label">调出仓库</text>
            <text class="form-value">{{ transferForm.source_warehouse_name }}</text>
          </view>
          
          <view class="form-item">
            <text class="form-label">商品信息</text>
            <view class="product-info">
              <image class="product-image" :src="transferForm.product_image" mode="aspectFill" />
              <view class="product-detail">
                <text class="product-name">{{ transferForm.product_name }}</text>
                <text class="product-code">{{ transferForm.product_code }}</text>
              </view>
            </view>
          </view>
          
          <view class="form-item">
            <text class="form-label">可用库存</text>
            <text class="form-value">{{ transferForm.available_quantity }} {{ transferForm.unit }}</text>
          </view>
          
          <view class="form-item">
            <text class="form-label">调入仓库</text>
            <picker mode="selector" :range="targetWarehouseOptions" @change="handleTargetWarehouseChange">
              <view class="picker-value">
                <text>{{ transferForm.target_warehouse_name || '请选择调入仓库' }}</text>
                <uni-icons type="arrowright" size="16" />
              </view>
            </picker>
          </view>
          
          <view class="form-item">
            <text class="form-label">调拨数量</text>
            <input 
              class="form-input" 
              type="number" 
              v-model.number="transferForm.transfer_quantity"
              :max="transferForm.available_quantity"
              placeholder="请输入调拨数量"
            />
          </view>
          
          <view class="form-item">
            <text class="form-label">备注</text>
            <textarea 
              class="form-textarea" 
              v-model="transferForm.remark"
              placeholder="请输入调拨备注"
            />
          </view>
        </view>
        
        <view class="popup-actions">
          <button class="cancel-btn" @click="closeTransferPopup">取消</button>
          <button class="confirm-btn" @click="submitTransfer">确认调拨</button>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { getB2BInventoryList, getB2BWarehouseList, getB2BProductCategoryList, createB2BInventoryTransfer } from '@/api/b2b/inventory.js';

// 数据状态
const searchKeyword = ref('');
const inventoryList = ref([]);
const warehouseList = ref([]);
const categoryList = ref([]);
const selectedItems = ref([]);
const totalStats = ref({
  total_quantity: 0,
  warning_count: 0,
  out_of_stock_count: 0
});

// 筛选条件
const selectedWarehouse = ref(null);
const selectedCategory = ref(null);
const selectedStockStatus = ref(null);

// 调拨表单
const transferForm = ref({
  source_warehouse_id: '',
  source_warehouse_name: '',
  target_warehouse_id: '',
  target_warehouse_name: '',
  product_id: '',
  product_name: '',
  product_code: '',
  product_image: '',
  available_quantity: 0,
  transfer_quantity: 1,
  unit: '',
  remark: ''
});

// 选项数据
const stockStatusOptions = [
  { value: 'all', text: '全部状态' },
  { value: 'sufficient', text: '库存充足' },
  { value: 'warning', text: '库存预警' },
  { value: 'out_of_stock', text: '缺货' }
];

// 计算属性
const warehouseOptions = computed(() => {
  return warehouseList.value.map(item => item.warehouse_name);
});

const categoryOptions = computed(() => {
  return categoryList.value.map(item => item.category_name);
});

const targetWarehouseOptions = computed(() => {
  return warehouseList.value
    .filter(item => item.warehouse_id !== transferForm.value.source_warehouse_id)
    .map(item => item.warehouse_name);
});

// 生命周期
onMounted(() => {
  initData();
});

// 方法
async function initData() {
  await Promise.all([
    loadWarehouseList(),
    loadCategoryList(),
    loadInventoryList()
  ]);
}

async function loadInventoryList() {
  try {
    const params = {
      keyword: searchKeyword.value,
      warehouse_id: selectedWarehouse.value?.warehouse_id,
      category_id: selectedCategory.value?.category_id,
      stock_status: selectedStockStatus.value?.value,
      page: 1,
      page_size: 20
    };
    
    const res = await getB2BInventoryList(params);
    if (res.success) {
      inventoryList.value = res.data.records || [];
      totalStats.value = res.data.stats || {};
    }
  } catch (error) {
    console.error('加载库存列表失败:', error);
  }
}

async function loadWarehouseList() {
  try {
    const res = await getB2BWarehouseList();
    if (res.success) {
      warehouseList.value = res.data || [];
    }
  } catch (error) {
    console.error('加载仓库列表失败:', error);
  }
}

async function loadCategoryList() {
  try {
    const res = await getB2BProductCategoryList();
    if (res.success) {
      categoryList.value = res.data || [];
    }
  } catch (error) {
    console.error('加载商品分类失败:', error);
  }
}

function handleSearch() {
  loadInventoryList();
}

function handleClear() {
  searchKeyword.value = '';
  loadInventoryList();
}

function handleWarehouseChange(e) {
  const index = e.detail.value;
  selectedWarehouse.value = warehouseList.value[index] || null;
  loadInventoryList();
}

function handleCategoryChange(e) {
  const index = e.detail.value;
  selectedCategory.value = categoryList.value[index] || null;
  loadInventoryList();
}

function handleStockStatusChange(e) {
  const index = e.detail.value;
  selectedStockStatus.value = stockStatusOptions[index] || null;
  loadInventoryList();
}

function getStockStatusClass(item) {
  if (item.available_quantity === 0) {
    return 'out-of-stock';
  } else if (item.available_quantity < item.safety_stock) {
    return 'warning';
  } else {
    return 'sufficient';
  }
}

function getStockStatusText(item) {
  if (item.available_quantity === 0) {
    return '缺货';
  } else if (item.available_quantity < item.safety_stock) {
    return '库存预警';
  } else {
    return '库存充足';
  }
}

function formatNumber(num) {
  return num.toLocaleString();
}

function viewInventoryDetail(item) {
  // 跳转到库存详情页面
  uni.navigateTo({
    url: `/pagesA/inventory_detail/index?inventoryId=${item.inventory_id}`
  });
}

function viewInventoryLog(item) {
  // 跳转到库存流水页面
  uni.navigateTo({
    url: `/pagesA/inventory_log/index?productId=${item.product_id}&warehouseId=${item.warehouse_id}`
  });
}

function handleStockWarning(item) {
  // 处理库存预警
  uni.showModal({
    title: '库存预警',
    content: `${item.product_name} 库存不足，是否立即补货？`,
    success: (res) => {
      if (res.confirm) {
        // 跳转到补货页面
        uni.navigateTo({
          url: `/pagesA/purchase_order/create?productId=${item.product_id}`
        });
      }
    }
  });
}

function transferInventory(item) {
  // 打开调拨弹窗
  transferForm.value = {
    source_warehouse_id: item.warehouse_id,
    source_warehouse_name: item.warehouse_name,
    target_warehouse_id: '',
    target_warehouse_name: '',
    product_id: item.product_id,
    product_name: item.product_name,
    product_code: item.product_code,
    product_image: item.product_image,
    available_quantity: item.available_quantity,
    transfer_quantity: 1,
    unit: item.unit,
    remark: ''
  };
  
  this.$refs.transferPopup.open();
}

function handleTargetWarehouseChange(e) {
  const index = e.detail.value;
  const warehouse = warehouseList.value.filter(item => item.warehouse_id !== transferForm.value.source_warehouse_id)[index];
  if (warehouse) {
    transferForm.value.target_warehouse_id = warehouse.warehouse_id;
    transferForm.value.target_warehouse_name = warehouse.warehouse_name;
  }
}

function closeTransferPopup() {
  this.$refs.transferPopup.close();
}

async function submitTransfer() {
  if (!transferForm.value.target_warehouse_id) {
    uni.showToast({
      title: '请选择调入仓库',
      icon: 'none'
    });
    return;
  }
  
  if (!transferForm.value.transfer_quantity || transferForm.value.transfer_quantity <= 0) {
    uni.showToast({
      title: '请输入有效的调拨数量',
      icon: 'none'
    });
    return;
  }
  
  if (transferForm.value.transfer_quantity > transferForm.value.available_quantity) {
    uni.showToast({
      title: '调拨数量不能超过可用库存',
      icon: 'none'
    });
    return;
  }
  
  try {
    const res = await createB2BInventoryTransfer(transferForm.value);
    if (res.success) {
      uni.showToast({
        title: '调拨成功',
        icon: 'success'
      });
      closeTransferPopup();
      loadInventoryList();
    }
  } catch (error) {
    console.error('库存调拨失败:', error);
    uni.showToast({
      title: '调拨失败',
      icon: 'none'
    });
  }
}

function batchExport() {
  // 批量导出
  if (selectedItems.value.length === 0) {
    uni.showToast({
      title: '请选择要导出的商品',
      icon: 'none'
    });
    return;
  }
  
  // 这里可以实现导出逻辑
  uni.showToast({
    title: '导出功能开发中',
    icon: 'none'
  });
}

function batchPrint() {
  // 批量打印
  if (selectedItems.value.length === 0) {
    uni.showToast({
      title: '请选择要打印的商品',
      icon: 'none'
    });
    return;
  }
  
  // 这里可以实现打印逻辑
  uni.showToast({
    title: '打印功能开发中',
    icon: 'none'
  });
}

function batchSetSafetyStock() {
  // 批量设置安全库存
  if (selectedItems.value.length === 0) {
    uni.showToast({
      title: '请选择要设置的商品',
      icon: 'none'
    });
    return;
  }
  
  // 这里可以实现批量设置安全库存逻辑
  uni.showToast({
    title: '批量设置功能开发中',
    icon: 'none'
  });
}
</script>

<style lang="scss" scoped>
.b2b-inventory-query {
  padding: 20rpx;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.search-section {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
}

.filter-options {
  display: flex;
  gap: 20rpx;
  margin-top: 20rpx;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
  padding: 16rpx 24rpx;
  background-color: #f5f7fa;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #606266;
}

.stats-section {
  display: flex;
  gap: 20rpx;
  margin-bottom: 20rpx;
}

.stat-card {
  flex: 1;
  background-color: #fff;
  border-radius: 12rpx;
  padding: 24rpx;
  text-align: center;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
  
  .stat-label {
    display: block;
    color: #909399;
    font-size: 24rpx;
    margin-bottom: 8rpx;
  }
  
  .stat-value {
    display: block;
    color: #303133;
    font-size: 32rpx;
    font-weight: bold;
    
    &.warning {
      color: #e6a23c;
    }
    
    &.danger {
      color: #f56c6c;
    }
  }
}

.inventory-list-section {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
}

.inventory-item {
  padding: 20rpx;
  border-bottom: 2rpx solid #e4e7ed;
  
  &:last-child {
    border-bottom: none;
  }
}

.item-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20rpx;
}

.product-image {
  width: 80rpx;
  height: 80rpx;
  border-radius: 8rpx;
  margin-right: 20rpx;
}

.product-info {
  flex: 1;
  
  .product-name {
    display: block;
    color: #303133;
    font-size: 28rpx;
    font-weight: 500;
    margin-bottom: 8rpx;
  }
  
  .product-code {
    display: block;
    color: #909399;
    font-size: 24rpx;
    margin-bottom: 4rpx;
  }
  
  .product-spec {
    display: block;
    color: #606266;
    font-size: 24rpx;
  }
}

.stock-status {
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  
  &.sufficient {
    background-color: #f0f9ff;
    color: #409eff;
  }
  
  &.warning {
    background-color: #fdf6ec;
    color: #e6a23c;
  }
  
  &.out-of-stock {
    background-color: #fef0f0;
    color: #f56c6c;
  }
}

.item-details {
  margin-bottom: 20rpx;
}

.detail-row {
  display: flex;
  align-items: center;
  margin-bottom: 12rpx;
  
  &:last-child {
    margin-bottom: 0;
  }
  
  .detail-label {
    color: #909399;
    font-size: 24rpx;
    min-width: 120rpx;
  }
  
  .detail-value {
    color: #606266;
    font-size: 24rpx;
    
    &.quantity {
      color: #409eff;
      font-weight: bold;
    }
  }
  
  &.warning {
    background-color: #fef0f0;
    padding: 12rpx;
    border-radius: 8rpx;
    
    .warning-text {
      color: #f56c6c;
      font-size: 24rpx;
      margin-left: 8rpx;
    }
  }
}

.item-actions {
  display: flex;
  gap: 16rpx;
}

.action-btn {
  padding: 16rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  background-color: #f5f7fa;
  color: #606266;
  border: none;
  
  &.warning {
    background-color: #fdf6ec;
    color: #e6a23c;
  }
  
  &.primary {
    background-color: #409eff;
    color: white;
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 80rpx 0;
  
  .empty-text {
    color: #909399;
    font-size: 28rpx;
    margin-top: 20rpx;
  }
}

.batch-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #fff;
  padding: 20rpx;
  box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.selection-info {
  color: #303133;
  font-size: 28rpx;
}

.action-buttons {
  display: flex;
  gap: 16rpx;
}

.batch-btn {
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  background-color: #f5f7fa;
  color: #606266;
  border: none;
  
  &.warning {
    background-color: #fdf6ec;
    color: #e6a23c;
  }
}

.transfer-popup {
  background-color: #fff;
  border-radius: 24rpx 24rpx 0 0;
  padding: 40rpx;
  max-height: 80vh;
  overflow-y: auto;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40rpx;
  
  .popup-title {
    color: #303133;
    font-size: 32rpx;
    font-weight: bold;
  }
}

.transfer-form {
  margin-bottom: 40rpx;
}

.form-item {
  margin-bottom: 30rpx;
  
  .form-label {
    display: block;
    color: #606266;
    font-size: 28rpx;
    margin-bottom: 16rpx;
  }
  
  .form-value {
    color: #303133;
    font-size: 28rpx;
    font-weight: 500;
  }
  
  .product-info {
    display: flex;
    align-items: center;
    
    .product-image {
      width: 80rpx;
      height: 80rpx;
      border-radius: 8rpx;
      margin-right: 20rpx;
    }
    
    .product-detail {
      .product-name {
        display: block;
        color: #303133;
        font-size: 28rpx;
        font-weight: 500;
        margin-bottom: 8rpx;
      }
      
      .product-code {
        display: block;
        color: #909399;
        font-size: 24rpx;
      }
    }
  }
  
  .picker-value {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx;
    background-color: #f5f7fa;
    border-radius: 8rpx;
    color: #303133;
    font-size: 28rpx;
  }
  
  .form-input {
    width: 100%;
    padding: 20rpx;
    background-color: #f5f7fa;
    border-radius: 8rpx;
    border: none;
    font-size: 28rpx;
  }
  
  .form-textarea {
    width: 100%;
    min-height: 120rpx;
    padding: 20rpx;
    background-color: #f5f7fa;
    border-radius: 8rpx;
    border: none;
    font-size: 28rpx;
  }
}

.popup-actions {
  display: flex;
  gap: 20rpx;
  
  .cancel-btn,
  .confirm-btn {
    flex: 1;
    padding: 24rpx;
    border-radius: 8rpx;
    font-size: 28rpx;
  }
  
  .cancel-btn {
    background-color: #f5f7fa;
    color: #606266;
    border: none;
  }
  
  .confirm-btn {
    background-color: #409eff;
    color: white;
    border: none;
  }
}
</style>