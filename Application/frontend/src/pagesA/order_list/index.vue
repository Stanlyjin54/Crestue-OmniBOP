<template>
  <base-layout>
    <view class="p-2">
      <view class="flex items-center justify-between mb-4">
        <text class="text-xl font-bold text-primary-700">订单列表</text>
        <button class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-lg flex items-center gap-1" @click="goCreateOrder">
          <uni-icons type="plus" size="20" />
          创建订单
        </button>
      </view>
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4 flex flex-col gap-4">
        <view class="flex gap-3">
          <uni-easyinput class="flex-1 box-border" type="text" v-model="searchForm.order_no" prefixIcon="search" placeholder="订单号" clearable @input="handleInput" />
          <uni-easyinput class="flex-1 box-border" type="text" v-model="searchForm.customer_name" prefixIcon="person" placeholder="客户名称" clearable @input="handleInput" />
        </view>
        <view class="flex gap-3">
          <uni-easyinput class="flex-1 box-border" type="text" v-model="searchForm.username" prefixIcon="person" placeholder="下单人" clearable @input="handleInput" />
          <uni-data-select class="flex-1" v-model="searchForm.order_status_enum_order_status_enum_id_1" :localdata="orderStatusOptions" clear placeholder="全部状态" @change="handleFilter" ></uni-data-select>
        </view>
        <view class="flex gap-3 items-center">
          <uni-datetime-picker class="flex-1" type="date" v-model="searchForm.order_date_start" placeholder="下单起始日期" @change="handleFilter" />
          <text class="mx-1 text-neutral-400">-</text>
          <uni-datetime-picker class="flex-1" type="date" v-model="searchForm.order_date_end" placeholder="下单截止日期" @change="handleFilter" />
        </view>
        <button class="w-full bg-primary-500 text-white rounded-full py-2 text-base font-bold shadow" @click="searchOrders">
          <uni-icons type="search" class="mr-1" /> 搜索
        </button>
      </view>
      <view>
        <view v-if="loading" class="flex justify-center items-center py-12">
          <uni-icons type="spinner-cycle" size="36" color="#4b5563" class="animate-spin" />
        </view>
        <view v-else-if="orders.length == 0" class="text-neutral-400 text-center py-12">
          <uni-icons type="list" size="40" color="#9ca3af" class="mb-2" />
          <view>暂无订单数据</view>
        </view>
        <view v-else>
          <view v-for="(item, idx) in orders" :key="item.order_info_id" class="bg-white rounded-xl shadow-lg p-4 mb-4 flex flex-col gap-2" @click="goOrderDetail(item.order_info_id)">
            <view class="flex justify-between items-center">
              <view class="flex items-center gap-2">
                <uni-icons type="wallet-filled" color="#0ea5e9" size="22" />
                <text class="font-bold text-lg text-primary-700">
                  {{ item.order_no }}
                </text>
                <uni-tag :text="item.status_name" :type="getStatusTagType(item.order_status_enum_order_status_enum_id_1)" size="normal" circle class="ml-2" />
              </view>
            </view>
            <view class="flex gap-3 items-center mt-1">
              <uni-icons type="person" color="#6b7280" size="18" />
              <text class="text-neutral-700">{{ item.customer_name }}</text>
              <uni-badge v-if="item.username" :text="item.username" type="primary" size="small" class="ml-2" />
            </view>
            <view class="flex gap-3 items-center">
              <uni-icons type="calendar" color="#6b7280" size="18" />
              <text class="text-neutral-500 text-sm">{{ item.order_date }}</text>
            </view>
            <view class="flex gap-3 items-center">
              <uni-icons type="wallet" color="#22c55e" size="18" />
              <text class="text-success-600 font-bold">￥{{ item.total_amount }}</text>
            </view>
          </view>
          <view v-if="hasMore" class="flex justify-center my-4">
            <button class="bg-neutral-200 text-neutral-700 rounded-full px-6 py-2 text-sm" @click="loadMore">加载更多</button>
          </view>
          <view v-else-if="orders.length > 0" class="text-center text-neutral-400 text-xs my-4">已加载全部订单</view>
        </view>
      </view>
    </view>
  </base-layout>
</template>
<script setup>
const { proxy } = getCurrentInstance();
const searchForm = ref({ order_no: '', customer_name: '', username: '', order_status_enum_order_status_enum_id_1: '', order_date_start: '', order_date_end: '' });
const orderStatusOptions = ref([]);
const orders = ref([]);
const loading = ref(false);
const pageInfo = ref({ current: 1, pageSize: 10 });
const hasMore = ref(true);
async function fetchOrderStatusOptions() {
  const res = await proxy.$cf.table.list({ table_name: "order_status_enum", param: {} });
  if (res.success) {
    orderStatusOptions.value = res.data.map(item => ({ value: item.order_status_enum_id, text: item.status_name }));
  }
}
async function fetchOrders(reset = false) {
  if (reset) { pageInfo.value.current = 1; orders.value = []; hasMore.value = true; }
  loading.value = true;
  const param = { current: pageInfo.value.current, pageSize: pageInfo.value.pageSize };
  if (searchForm.value.order_no) param.order_no = searchForm.value.order_no;
  if (searchForm.value.customer_name) param.customer_name = searchForm.value.customer_name;
  if (searchForm.value.username) param.username = searchForm.value.username;
  if (searchForm.value.order_status_enum_order_status_enum_id_1) param.order_status_enum_order_status_enum_id_1 = searchForm.value.order_status_enum_order_status_enum_id_1;
  if (searchForm.value.order_date_start) param.order_date = searchForm.value.order_date_start;
  if (searchForm.value.order_date_end) param.order_date = searchForm.value.order_date_end;
  const res = await proxy.$cf.table.page({ table_name: "order_info", param });
  loading.value = false;
  if (res.success) {
    if (pageInfo.value.current == 1) { orders.value = res.data.records; } else { orders.value = orders.value.concat(res.data.records); }
    hasMore.value = orders.value.length < res.data.total;
  } else { proxy.$cf.toast({ message: res.message || "加载失败", level: "error" }); }
}
function searchOrders() { fetchOrders(true); }
function loadMore() { if (!hasMore.value) return; pageInfo.value.current += 1; fetchOrders(); }
function handleInput() {}
function handleFilter() { fetchOrders(true); }
function goOrderDetail(order_info_id) { proxy.$cf.navigate.to({ url: `/pages/order_detail/index?order_info_id=${order_info_id}`, type: "page" }); }
function goCreateOrder() { proxy.$cf.navigate.to({ url: '/pages/order_create/index', type: 'page' }); }
function getStatusTagType(statusId) {
  switch (statusId) {
    case 1: return "warning";
    case 2: return "primary";
    case 3: return "success";
    case 4: return "success";
    case 5: return "success";
    case 6: return "success";
    case 7: return "error";
    case 8: return "error";
    default: return "default";
  }
}
onLoad(() => { fetchOrderStatusOptions(); fetchOrders(true); });
</script>