<template>
  <base-layout>
    <view class="p-2">
      <!-- 商品基础信息卡片 -->
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4 flex flex-col items-center">
        <image
          v-if="productDetail.images && productDetail.images.length"
          :src="get_resource_url(productDetail.images[0].url)"
          mode="aspectFit"
          class="w-32 h-32 rounded-lg mb-3 object-cover"
        />
        <view class="flex items-center mb-2">
          <text class="text-xl font-bold text-primary-700 mr-2">{{ productDetail.product_name || '商品名称' }}</text>
          <uni-tag
            v-if="productDetail.status_name"
            :text="productDetail.status_name"
            size="normal"
            circle
            :type="getStatusTagType(productDetail.status_name)"
            class="ml-2"
          />
        </view>
        <view class="flex gap-4 mb-2">
          <view v-if="productDetail.brand" class="flex items-center gap-1">
            <uni-icons type="shop" size="18" color="#6b7280" />
            <text class="text-neutral-500 text-sm">{{ productDetail.brand }}</text>
          </view>
          <view v-if="productDetail.category" class="flex items-center gap-1">
            <uni-icons type="list" size="18" color="#6b7280" />
            <text class="text-neutral-500 text-sm">{{ productDetail.category }}</text>
          </view>
          <view v-if="productDetail.unit" class="flex items-center gap-1">
            <uni-icons type="tune" size="18" color="#6b7280" />
            <text class="text-neutral-500 text-sm">{{ productDetail.unit }}</text>
          </view>
        </view>
        <view class="flex gap-4">
          <view v-if="productDetail.barcode" class="flex items-center gap-1">
            <uni-icons type="headphones" size="18" color="#6b7280" />
            <text class="text-neutral-500 text-xs">条码: {{ productDetail.barcode }}</text>
          </view>
          <view v-if="productDetail.specification" class="flex items-center gap-1">
            <uni-icons type="settings" size="18" color="#6b7280" />
            <text class="text-neutral-500 text-xs">规格: {{ productDetail.specification }}</text>
          </view>
        </view>
      </view>

      <!-- 数据看板 -->
      <view class="grid grid-cols-2 gap-4 mb-4">
        <!-- 库存总量 -->
        <view class="bg-white rounded-xl shadow-md p-4 flex flex-col items-center">
          <uni-icons type="cart" size="28" color="#0ea5e9" class="mb-1" />
          <text class="text-lg font-semibold text-primary-700">{{ totalStock }}</text>
          <text class="text-neutral-500 text-xs mt-1">库存总量</text>
        </view>
        <!-- 历史销量 -->
        <view class="bg-white rounded-xl shadow-md p-4 flex flex-col items-center">
          <uni-icons type="wallet" size="28" color="#22c55e" class="mb-1" />
          <text class="text-lg font-semibold text-success-700">{{ totalSales }}</text>
          <text class="text-neutral-500 text-xs mt-1">历史销量</text>
        </view>
        <!-- 毛利率 -->
        <view class="bg-white rounded-xl shadow-md p-4 flex flex-col items-center">
          <uni-icons type="medal" size="28" color="#f59e0b" class="mb-1" />
          <text class="text-lg font-semibold text-warning-700">{{ profitRate }}%</text>
          <text class="text-neutral-500 text-xs mt-1">毛利率</text>
        </view>
        <!-- 库存预警 -->
        <view class="bg-white rounded-xl shadow-md p-4 flex flex-col items-center">
          <uni-icons
            :type="totalStock < (productDetail.min_stock || 0) ? 'notification-filled' : 'checkmarkempty'"
            size="28"
            :color="totalStock < (productDetail.min_stock || 0) ? '#ef4444' : '#22c55e'"
            class="mb-1"
          />
          <text
            class="text-lg font-semibold"
            :class="totalStock < (productDetail.min_stock || 0) ? 'text-danger-500' : 'text-success-700'"
          >
            {{ totalStock < (productDetail.min_stock || 0) ? '预警' : '正常' }}
          </text>
          <text class="text-neutral-500 text-xs mt-1">库存状态</text>
        </view>
      </view>

      <!-- 库存明细卡片 -->
      <view class="bg-white rounded-xl shadow-lg p-2 mb-4">
        <view class="flex items-center mb-2">
          <uni-icons type="list" size="20" color="#0ea5e9" class="mr-1" />
          <text class="text-base font-semibold text-primary-700">库存明细</text>
        </view>
        <view v-if="stockList.length" class="flex flex-col gap-2">
          <view
            v-for="item in stockList"
            :key="item.inventory_record_id"
            class="rounded-lg bg-neutral-50 p-2 flex flex-col shadow-sm"
          >
            <view class="flex justify-between items-center mb-1">
              <text class="font-semibold text-neutral-700">{{ item.warehouse_name || '仓库' }}</text>
              <uni-badge
                :text="'x' + item.quantity"
                type="primary"
                size="normal"
                class="ml-2"
              />
            </view>
            <view class="flex gap-3 flex-wrap text-xs text-neutral-500">
              <view v-if="item.expire_date">
                <uni-icons type="calendar" size="16" color="#6b7280" class="mr-1" />
                效期: {{ formatDate(item.expire_date) }}
              </view>
              <view v-if="item.batch_number">
                <uni-icons type="headphones" size="16" color="#6b7280" class="mr-1" />
                批次: {{ item.batch_number }}
              </view>
              <view v-if="item.serial_number">
                <uni-icons type="settings" size="16" color="#6b7280" class="mr-1" />
                序列号: {{ item.serial_number }}
              </view>
            </view>
          </view>
        </view>
        <view v-else class="text-neutral-400 text-center py-4">
          暂无库存明细
        </view>
      </view>

      <!-- 利润看板 -->
      <view class="bg-white rounded-xl shadow-lg p-2 mb-4">
        <view class="flex items-center mb-2">
          <uni-icons type="medal" size="20" color="#f59e0b" class="mr-1" />
          <text class="text-base font-semibold text-primary-700">利润看板</text>
        </view>
        <view v-if="profitBoard">
          <view class="flex gap-4 flex-wrap">
            <view class="flex flex-col items-center flex-1">
              <text class="text-success-600 text-lg font-bold">{{ profitBoard.profit_amount || 0 }}</text>
              <text class="text-xs text-neutral-500 mt-1">毛利金额</text>
            </view>
            <view class="flex flex-col items-center flex-1">
              <text class="text-warning-600 text-lg font-bold">{{ profitBoard.commission || 0 }}</text>
              <text class="text-xs text-neutral-500 mt-1">平台佣金</text>
            </view>
            <view class="flex flex-col items-center flex-1">
              <text class="text-primary-600 text-lg font-bold">{{ profitBoard.shipping_fee || 0 }}</text>
              <text class="text-xs text-neutral-500 mt-1">运费</text>
            </view>
            <view class="flex flex-col items-center flex-1">
              <text class="text-neutral-700 text-lg font-bold">{{ formatDate(profitBoard.stat_date) }}</text>
              <text class="text-xs text-neutral-500 mt-1">统计日期</text>
            </view>
          </view>
        </view>
        <view v-else class="text-neutral-400 text-center py-4">
          暂无利润数据
        </view>
      </view>

      <!-- 返回按钮 -->
      <button
        class="w-full box-border bg-primary-500 text-white rounded-full py-3 text-base font-semibold shadow-xl mt-2"
        @click="goBack"
      >
        <uni-icons type="back" size="20" color="#fff" class="mr-1" /> 返回库存列表
      </button>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const productDetail = ref({});
const stockList = ref([]);
const profitBoard = ref(null);
const totalStock = ref(0);
const totalSales = ref(0);
const profitRate = ref(0);

// 获取页面参数
const params = ref({});
onLoad((option) => {
  params.value = option || {};
  if (params.value.product_info_id) {
    fetchProductDetail(params.value.product_info_id);
    fetchStockList(params.value.product_info_id);
    fetchProfitBoard(params.value.product_info_id);
    fetchSales(params.value.product_info_id);
  }
});

// 查询商品基本信息
async function fetchProductDetail(product_info_id) {
  const res = await proxy.$cf.table.get({
    table_name: 'product_info',
    param: { product_info_id: Number(product_info_id) }
  });
  if (res.success && res.data) {
    productDetail.value = res.data;
  }
}

// 查询库存明细
async function fetchStockList(product_info_id) {
  const res = await proxy.$cf.table.list({
    table_name: 'inventory_record',
    param: { product_info_product_info_id_1: Number(product_info_id) }
  });
  if (res.success) {
    stockList.value = res.data || [];
    totalStock.value = stockList.value.reduce((sum, item) => sum + (item.quantity || 0), 0);
  }
}

// 查询利润看板
async function fetchProfitBoard(product_info_id) {
  // target_type: 'product_info', target_id: product_info_id
  const res = await proxy.$cf.table.list({
    table_name: 'profit_board',
    param: {
      target_type: 'product_info',
      target_id: Number(product_info_id)
    }
  });
  if (res.success && res.data && res.data.length) {
    // 取最新一条
    profitBoard.value = res.data[0];
    // 计算毛利率
    if (profitBoard.value && profitBoard.value.profit_amount && totalSales.value) {
      profitRate.value = ((profitBoard.value.profit_amount / totalSales.value) * 100).toFixed(2);
    }
  }
}

// 查询历史销量
async function fetchSales(product_info_id) {
  // 历史销量为订单明细中该商品的数量总和
  const res = await proxy.$cf.table.list({
    table_name: 'order_item',
    param: { product_info_product_info_id_1: Number(product_info_id) }
  });
  if (res.success) {
    totalSales.value = res.data.reduce((sum, item) => sum + (item.quantity || 0), 0);
    // 重新计算毛利率
    if (profitBoard.value && profitBoard.value.profit_amount) {
      profitRate.value = ((profitBoard.value.profit_amount / totalSales.value) * 100).toFixed(2);
    }
  }
}

// 返回上一页
function goBack() {
  proxy.$cf.navigate.to({
    url: '/pages/inventory/index',
    type: 'page'
  });
}

// 状态标签样式
function getStatusTagType(status) {
  if (status == '正常') return 'success';
  if (status == '临期') return 'warning';
  if (status == '停售') return 'error';
  return 'default';
}

// 日期格式化
function formatDate(dateStr) {
  if (!dateStr) return '';
  // 支持 YYYY-MM-DD 或 YYYY-MM-DD HH:mm:ss
  return dateStr.split(' ')[0];
}

// 图片资源url
function get_resource_url(path) {
  return proxy.get_resource_url(path);
}
</script>