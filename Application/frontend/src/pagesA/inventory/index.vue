<template>
  <base-layout>
    <!-- 一级容器 -->
    <view class="p-2 w-full box-border">
      <!-- 顶部快捷区 -->
      <view class="flex gap-3 mb-4">
        <!-- 扫码录入 -->
        <button
          class="flex-1 bg-primary-500 text-white rounded-full px-4 py-2 flex items-center justify-center shadow-lg"
          @click="goScan"
        >
          <uni-icons type="scan" size="22" class="mr-2" />
          扫码录入
        </button>
        <!-- 采购建议快捷入口 -->
        <button
          class="flex-1 bg-success-500 text-white rounded-full px-4 py-2 flex items-center justify-center shadow-lg"
          @click="goProcurementSuggestion"
        >
          <uni-icons type="cart" size="22" class="mr-2" />
          采购建议
        </button>
        <!-- 筛选 -->
        <button
          class="flex-1 bg-secondary-500 text-white rounded-full px-4 py-2 flex items-center justify-center shadow-lg"
          @click="openFilterPopup"
        >
          <uni-icons type="tune" size="22" class="mr-2" />
          筛选
        </button>
      </view>

      <!-- 智能采购建议预警区（只展示前2条，更多跳转） -->
      <view v-if="procurementSuggestionList.length > 0" class="mb-4">
        <view class="flex items-center mb-2">
          <uni-icons type="notification" color="#f59e0b" size="20" class="mr-1" />
          <text class="font-bold text-base text-warning-600">智能采购建议</text>
        </view>
        <view class="grid grid-cols-2 gap-4">
          <view
            v-for="item in procurementSuggestionList.slice(0,2)"
            :key="item.procurement_suggestion_id"
            class="bg-white rounded-xl shadow-lg p-3 flex flex-col gap-1"
          >
            <text class="font-bold text-primary-700">{{ item.product_name }}</text>
            <view class="flex items-center gap-2">
              <uni-tag text="建议采购" type="success" size="small" />
              <text class="text-success-600">数量：{{ item.suggest_quantity }}</text>
            </view>
            <text class="text-xs text-neutral-400">建议日期：{{ item.suggest_date }}</text>
            <text class="text-xs text-neutral-400" v-if="item.reason">原因：{{ item.reason }}</text>
          </view>
        </view>
        <view class="flex justify-end mt-2">
          <button
            class="bg-primary-500 text-white rounded-full px-3 py-1 text-sm"
            @click="goProcurementSuggestion"
          >查看更多</button>
        </view>
      </view>

      <!-- 库存商品列表 -->
      <view class="mb-2 flex items-center gap-2">
        <uni-icons type="list" size="20" color="#4b5563" />
        <text class="font-bold text-lg text-primary-700">库存商品列表</text>
        <text class="text-neutral-400 text-xs ml-2">共{{ total }}项</text>
      </view>

      <!-- 搜索框 -->
      <view class="mb-4">
        <uni-easyinput
          v-model="searchKeyword"
          type="text"
          clearable
          prefixIcon="search"
          placeholder="搜索商品名称、条码、仓库"
          class="w-full box-border"
          @change="onSearch"
        />
      </view>

      <!-- 商品库存卡片列表 -->
      <view class="grid grid-cols-1 gap-4">
        <view
          v-for="item in inventoryList"
          :key="item.inventory_record_id"
          class="bg-white rounded-xl shadow-lg p-3 flex flex-col gap-2"
          @click="goProductDetail(item.product_info_product_info_id_1)"
        >
          <view class="flex items-center gap-3">
            <!-- 商品图片 -->
            <image
              v-if="item.images && item.images.length > 0"
              :src="get_resource_url(item.images[0].url)"
              class="w-16 h-16 object-cover rounded-lg shadow"
              mode="aspectFill"
            />
            <view v-else class="w-16 h-16 bg-neutral-100 flex items-center justify-center rounded-lg">
              <uni-icons type="image" size="32" color="#9ca3af" />
            </view>
            <view class="flex-1 flex flex-col gap-1">
              <text class="font-bold text-base text-primary-800">{{ item.product_name }}</text>
              <view class="flex gap-2 flex-wrap">
                <uni-tag v-if="item.category" :text="item.category" size="small" type="primary" circle />
                <uni-tag v-if="item.brand" :text="item.brand" size="small" type="success" circle />
                <uni-tag v-if="item.warehouse_name" :text="item.warehouse_name" size="small" type="default" circle />
              </view>
              <view class="flex gap-2 items-center mt-1">
                <uni-badge
                  v-if="item.quantity <= (item.min_stock || 5)"
                  text="预警"
                  type="warning"
                  size="normal"
                  absolute="rightTop"
                  custom-style="margin-right:0.5rem;"
                />
                <text class="text-success-600 font-bold">库存：{{ item.quantity }}</text>
                <text class="text-neutral-400 text-xs">单位：{{ item.unit || '--' }}</text>
              </view>
            </view>
          </view>
          <view class="flex gap-3 text-xs text-neutral-500 mt-2">
            <text>批次号：{{ item.batch_number || '--' }}</text>
            <text>序列号：{{ item.serial_number || '--' }}</text>
          </view>
          <view class="flex gap-3 text-xs text-neutral-500">
            <text>效期：{{ item.expire_date || '--' }}</text>
            <text v-if="item.shelf_life">保质期：{{ item.shelf_life }}</text>
          </view>
        </view>
      </view>

      <!-- 加载更多 -->
      <view v-if="hasMore" class="flex justify-center my-4">
        <button
          class="bg-secondary-500 text-white rounded-full px-6 py-2 text-sm"
          @click="loadMore"
        >
          加载更多
        </button>
      </view>
      <view v-else-if="inventoryList.length > 0" class="flex justify-center my-4 text-neutral-400 text-xs">
        没有更多了
      </view>
      <view v-else class="flex flex-col items-center justify-center my-8 text-neutral-400">
        <uni-icons type="info" size="36" color="#d1d5db" />
        <text class="mt-2">暂无库存记录</text>
      </view>
    </view>

    <!-- 筛选弹窗 -->
    <uni-popup ref="filterPopup" type="center">
      <view class="bg-white p-4 w-80 rounded-xl">
        <text class="font-bold text-lg mb-3 block text-primary-700">筛选条件</text>
        <uni-forms :modelValue="filterForm" label-position="top">
          <uni-forms-item name="warehouse_info_warehouse_info_id_1" label="仓库">
            <uni-data-select
              v-model="filterForm.warehouse_info_warehouse_info_id_1"
              :localdata="warehouseOptions"
              clear
              placement="bottom"
            />
          </uni-forms-item>
          <uni-forms-item name="product_status_enum_product_status_enum_id_1" label="商品状态">
            <uni-data-select
              v-model="filterForm.product_status_enum_product_status_enum_id_1"
              :localdata="statusOptions"
              clear
              placement="bottom"
            />
          </uni-forms-item>
        </uni-forms>
        <view class="flex gap-3 mt-4">
          <button
            class="flex-1 bg-neutral-300 text-primary-700 rounded-full px-3 py-2"
            @click="resetFilter"
          >重置</button>
          <button
            class="flex-1 bg-primary-500 text-white rounded-full px-3 py-2"
            @click="applyFilter"
          >确定</button>
        </view>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

// 分页信息
const pageInfo = ref({
  current: 1,
  pageSize: 10,
});
const total = ref(0);
const hasMore = ref(true);

// 搜索与筛选
const searchKeyword = ref('');
const filterForm = ref({
  warehouse_info_warehouse_info_id_1: '',
  product_status_enum_product_status_enum_id_1: '',
});

// 库存商品列表
const inventoryList = ref([]);

// 仓库下拉
const warehouseOptions = ref([]);
// 商品状态下拉（枚举 product_status_enum）
const statusOptions = ref([]);

// 智能采购建议
const procurementSuggestionList = ref([]);

// 资源图片url辅助
function get_resource_url(url) {
  return proxy.get_resource_url(url);
}

// 加载仓库下拉
async function fetchWarehouseOptions() {
  const res = await proxy.$cf.table.list({
    table_name: 'warehouse_info',
    param: {},
  });
  if (res.success) {
    warehouseOptions.value = res.data.map(item => ({
      value: item.warehouse_info_id,
      text: item.warehouse_name,
    }));
  }
}

// 加载商品状态下拉
async function fetchStatusOptions() {
  const res = await proxy.$cf.table.list({
    table_name: 'product_status_enum',
    param: {},
  });
  if (res.success) {
    statusOptions.value = res.data.map(item => ({
      value: item.product_status_enum_id,
      text: item.status_name,
    }));
  }
}

// 加载智能采购建议
async function fetchProcurementSuggestion() {
  const res = await proxy.$cf.table.page({
    table_name: 'procurement_suggestion',
    param: {
      current: 1,
      pageSize: 2,
    },
  });
  if (res.success) {
    procurementSuggestionList.value = res.data.records.map(item => ({
      ...item,
      ...item, // product_name等字段已平铺
    }));
  }
}

// 加载库存商品列表
async function fetchInventoryList(isReset = false) {
  if (isReset) {
    pageInfo.value.current = 1;
    inventoryList.value = [];
    hasMore.value = true;
  }
  const param = {
    current: pageInfo.value.current,
    pageSize: pageInfo.value.pageSize,
  };
  // 搜索
  if (searchKeyword.value) {
    param.product_name = searchKeyword.value;
  }
  // 仓库筛选
  if (filterForm.value.warehouse_info_warehouse_info_id_1) {
    param.warehouse_info_warehouse_info_id_1 = filterForm.value.warehouse_info_warehouse_info_id_1;
  }
  // 商品状态筛选
  if (filterForm.value.product_status_enum_product_status_enum_id_1) {
    param.product_status_enum_product_status_enum_id_1 = filterForm.value.product_status_enum_product_status_enum_id_1;
  }
  // 查询
  const res = await proxy.$cf.table.page({
    table_name: 'inventory_record',
    param,
  });
  if (res.success) {
    // 需要补充商品、仓库、图片等信息
    const records = res.data.records;
    total.value = res.data.total;
    hasMore.value = pageInfo.value.current * pageInfo.value.pageSize < res.data.total;
    // 平铺商品和仓库字段
    inventoryList.value = isReset ? [] : inventoryList.value;
    for (const rec of records) {
      // 补充商品信息
      const productRes = await proxy.$cf.table.get({
        table_name: 'product_info',
        param: { product_info_id: rec.product_info_product_info_id_1 },
      });
      let product = {};
      if (productRes.success) {
        product = productRes.data;
      }
      // 补充仓库信息
      const warehouseRes = await proxy.$cf.table.get({
        table_name: 'warehouse_info',
        param: { warehouse_info_id: rec.warehouse_info_warehouse_info_id_1 },
      });
      let warehouse = {};
      if (warehouseRes.success) {
        warehouse = warehouseRes.data;
      }
      inventoryList.value.push({
        ...rec,
        ...product,
        warehouse_name: warehouse.warehouse_name,
      });
    }
  }
}

function loadMore() {
  if (!hasMore.value) return;
  pageInfo.value.current += 1;
  fetchInventoryList();
}

// 搜索
function onSearch() {
  pageInfo.value.current = 1;
  inventoryList.value = [];
  fetchInventoryList(true);
}

// 筛选弹窗
function openFilterPopup() {
  proxy.$refs.filterPopup.open();
}
function resetFilter() {
  filterForm.value = {
    warehouse_info_warehouse_info_id_1: '',
    product_status_enum_product_status_enum_id_1: '',
  };
}
function applyFilter() {
  proxy.$refs.filterPopup.close();
  fetchInventoryList(true);
}

// 跳转扫码录入
function goScan() {
  proxy.$cf.navigate.to({
    url: '/pages/inventory_scan/index',
    type: 'page',
  });
}
// 跳转采购建议
function goProcurementSuggestion() {
  proxy.$cf.navigate.to({
    url: '/pages/procurement_suggestion/index',
    type: 'page',
  });
}
// 跳转商品详情
function goProductDetail(product_info_id) {
  proxy.$cf.navigate.to({
    url: `/pages/product_detail/index?product_info_id=${product_info_id}`,
    type: 'page',
  });
}

onLoad(async () => {
  await fetchWarehouseOptions();
  await fetchStatusOptions();
  await fetchProcurementSuggestion();
  await fetchInventoryList(true);
});
</script>