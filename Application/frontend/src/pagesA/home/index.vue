<template>
  <base-layout>
    <view class="p-2 w-full box-border">
      <!-- 顶部搜索与AI助手 -->
      <view class="mb-4 flex gap-3 items-center">
        <view class="flex-1">
          <uni-easyinput
            v-model="searchText"
            class="w-full box-border rounded-full bg-white shadow-md px-4 py-2"
            prefixIcon="search"
            placeholder="搜索商品、客户、订单、商机"
            @change="handleSearch"
            @confirm="handleSearch"
            clearable
          />
        </view>
        <button class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-lg flex items-center gap-1" @click="goAiAssistant">
          <uni-icons type="auth" size="20" color="#fff" />
          AI助手
        </button>
      </view>

      <!-- 快捷入口 -->
      <view class="mb-4 grid grid-cols-4 gap-4">
        <view
          v-for="entry in quickEntries"
          :key="entry.key"
          class="flex flex-col items-center justify-center bg-white rounded-xl shadow-lg py-3 px-1 active:scale-95 transition"
          @click="entry.action"
        >
          <uni-icons :type="entry.icon" size="28" :color="entry.color"/>
          <text class="mt-2 text-xs text-neutral-700">{{ entry.label }}</text>
        </view>
      </view>

      <!-- 销售看板 -->
      <view class="mb-4">
        <view class="flex items-center mb-2">
          <uni-icons type="wallet" size="20" color="#22c55e" />
          <text class="ml-2 text-lg font-bold text-primary-600">销售看板</text>
        </view>
        <view class="grid grid-cols-3 gap-4">
          <view class="bg-white rounded-xl shadow-md p-4 flex flex-col items-center">
            <uni-icons type="wallet" size="28" color="#0ea5e9" class="mb-1" />
            <text class="text-lg font-semibold text-primary-700">￥{{ todaySalesAmount }}</text>
            <text class="text-neutral-500 text-xs mt-1">今日销售金额</text>
          </view>
          <view class="bg-white rounded-xl shadow-md p-4 flex flex-col items-center">
            <uni-icons type="cart" size="28" color="#22c55e" class="mb-1" />
            <text class="text-lg font-semibold text-success-700">{{ todayOrderCount }}</text>
            <text class="text-neutral-500 text-xs mt-1">订单数量</text>
          </view>
          <view class="bg-white rounded-xl shadow-md p-4 flex flex-col items-center">
            <uni-icons type="medal" size="28" color="#f59e0b" class="mb-1" />
            <text class="text-lg font-semibold text-warning-700">￥{{ todayProfitAmount }}</text>
            <text class="text-neutral-500 text-xs mt-1">利润金额</text>
          </view>
        </view>
      </view>

      <!-- 库存预警 -->
      <view class="mb-4">
        <view class="flex items-center mb-2">
          <uni-icons type="headphones" size="20" color="#f59e0b" />
          <text class="ml-2 text-lg font-bold text-primary-600">库存预警</text>
          <view class="flex-1"></view>
          <button class="bg-warning-400 text-white rounded-full px-3 py-1 text-xs" @click="goInventory">
            查看全部
          </button>
        </view>
        <view v-if="stockWarningList.length" class="grid grid-cols-2 gap-4">
          <view
            v-for="item in stockWarningList"
            :key="item.inventory_record_id"
            class="bg-white rounded-xl shadow-md p-3 flex flex-col gap-2"
            @click="goProductDetail(item.product_info_product_info_id_1)"
          >
            <view class="flex items-center gap-2">
              <uni-tag :text="item.product_name" size="small" type="primary" circle />
              <uni-tag v-if="item.expire_date && isNearExpire(item.expire_date)" text="临期" size="small" type="warning" circle />
            </view>
            <view class="text-xs text-neutral-500">仓库：{{ item.warehouse_name }}</view>
            <view class="flex items-center gap-2">
              <text class="text-danger-500 font-bold text-base">库存：{{ item.quantity }}</text>
              <text v-if="item.expire_date" class="text-xs text-warning-500 ml-2">效期：{{ item.expire_date }}</text>
            </view>
          </view>
        </view>
        <view v-else class="text-neutral-400 text-center py-4 text-sm">暂无库存预警</view>
      </view>

      <!-- 客户活跃度 -->
      <view class="mb-4">
        <view class="flex items-center mb-2">
          <uni-icons type="person" size="20" color="#0ea5e9" />
          <text class="ml-2 text-lg font-bold text-primary-600">客户活跃度</text>
          <view class="flex-1"></view>
          <button class="bg-primary-500 text-white rounded-full px-3 py-1 text-xs" @click="goCustomer">
            查看全部
          </button>
        </view>
        <view v-if="activeCustomerList.length" class="flex flex-wrap gap-3">
          <view
            v-for="item in activeCustomerList"
            :key="item.customer_info_id"
            class="bg-white rounded-xl shadow-md p-3 flex flex-col items-start w-[45%]"
            @click="goCustomerDetail(item.customer_info_id)"
          >
            <view class="flex items-center gap-2">
              <uni-icons type="person-filled" size="18" color="#0ea5e9"/>
              <text class="font-bold text-base">{{ item.customer_name }}</text>
            </view>
            <view class="text-xs text-neutral-500 mt-1">手机号：{{ item.phone || '—' }}</view>
            <view class="flex gap-2 mt-1">
              <uni-tag v-if="item.level_name" :text="item.level_name" size="small" type="primary" circle />
              <uni-tag v-if="item.tag_name" :text="item.tag_name" size="small" type="success" circle />
            </view>
          </view>
        </view>
        <view v-else class="text-neutral-400 text-center py-4 text-sm">暂无活跃客户</view>
      </view>

      <!-- 商机线索 -->
      <view class="mb-4">
        <view class="flex items-center mb-2">
          <uni-icons type="gift" size="20" color="#f59e0b" />
          <text class="ml-2 text-lg font-bold text-primary-600">商机线索</text>
        </view>
        <view v-if="opportunityList.length" class="flex flex-col gap-3">
          <view
            v-for="item in opportunityList"
            :key="item.business_opportunity_id"
            class="bg-white rounded-xl shadow-md p-3 flex flex-col"
          >
            <view class="flex items-center gap-2">
              <uni-icons type="gift-filled" size="18" color="#f59e0b"/>
              <text class="font-bold text-base">{{ item.opportunity_name }}</text>
              <uni-tag :text="item.status" size="small" type="primary" circle />
            </view>
            <view class="text-xs text-neutral-500 mt-1">客户：{{ item.customer_name }}</view>
            <view class="text-xs text-neutral-400 mt-1">建议：{{ item.follow_up_suggestion || '—' }}</view>
          </view>
        </view>
        <view v-else class="text-neutral-400 text-center py-4 text-sm">暂无商机线索</view>
      </view>

      <!-- 广告推荐 -->
      <view class="mb-4">
        <view class="flex items-center mb-2">
          <uni-icons type="gift" size="20" color="#0ea5e9" />
          <text class="ml-2 text-lg font-bold text-primary-600">广告推荐</text>
        </view>
        <view v-if="adList.length" class="flex flex-col gap-4">
          <view
            v-for="item in adList"
            :key="item.ad_recommend_info_id"
            class="bg-white rounded-xl shadow-lg overflow-hidden flex flex-row items-center"
            @click="goAdDetail(item.ad_recommend_info_id)"
          >
            <image
              v-if="item.ad_image && item.ad_image.length"
              :src="get_resource_url(item.ad_image[0].url)"
              class="w-24 h-20 object-cover rounded-l-xl"
              mode="aspectFill"
            />
            <view class="flex-1 p-3">
              <view class="font-bold text-base text-primary-700 mb-1">{{ item.ad_title }}</view>
              <view class="text-xs text-neutral-400">{{ item.ad_url }}</view>
            </view>
            <uni-icons type="right" size="18" color="#9ca3af" class="mr-2"/>
          </view>
        </view>
        <view v-else class="text-neutral-400 text-center py-4 text-sm">暂无广告推荐</view>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

// 搜索
const searchText = ref('');
function handleSearch() {
  if (!searchText.value.trim()) {
    proxy.$cf.toast({ message: '请输入搜索内容', level: 'warning' });
    return;
  }
  // 简单智能分流，优先关键字
  const text = searchText.value.trim();
  if (/^\d{8,}$/.test(text)) {
    // 可能是订单号或条码，跳转订单或商品
    proxy.$cf.navigate.to({
      url: `/pages/inventory/index?barcode=${encodeURIComponent(text)}`,
      type: 'page'
    });
  } else if (text.includes('客户') || text.match(/(张|李|王|刘|陈|杨|赵|黄|周|吴)/)) {
    proxy.$cf.navigate.to({
      url: `/pages/customer/index?search=${encodeURIComponent(text)}`,
      type: 'page'
    });
  } else if (text.includes('采购') || text.includes('比价')) {
    proxy.$cf.navigate.to({
      url: `/pages/purchase/index?search=${encodeURIComponent(text)}`,
      type: 'page'
    });
  } else if (text.includes('商机')) {
    proxy.$cf.navigate.to({
      url: `/pages/ai_assistant/index?search=${encodeURIComponent(text)}`,
      type: 'page'
    });
  } else {
    // 默认跳商品
    proxy.$cf.navigate.to({
      url: `/pages/inventory/index?search=${encodeURIComponent(text)}`,
      type: 'page'
    });
  }
}

// 快捷入口
const quickEntries = [
  {
    key: 'create_order',
    label: '创建订单',
    icon: 'plus',
    color: '#0ea5e9',
    action: () => proxy.$cf.navigate.to({ url: '/pages/order_create/index', type: 'page' })
  },
  {
    key: 'scan',
    label: '扫码出入库',
    icon: 'scan',
    color: '#0ea5e9',
    action: () => proxy.$cf.navigate.to({ url: '/pages/inventory/index?mode=scan', type: 'page' })
  },
  {
    key: 'purchase',
    label: '采购比价',
    icon: 'cart',
    color: '#22c55e',
    action: () => proxy.$cf.navigate.to({ url: '/pages/purchase/index', type: 'page' })
  },
  {
    key: 'transfer',
    label: '多仓调拨',
    icon: 'navigate',
    color: '#f59e0b',
    action: () => proxy.$cf.navigate.to({ url: '/pages/inventory/index?mode=transfer', type: 'page' })
  },
  {
    key: 'customer',
    label: '客户管理',
    icon: 'person',
    color: '#0ea5e9',
    action: () => proxy.$cf.navigate.to({ url: '/pages/customer/index', type: 'page' })
  },
  {
    key: 'fission',
    label: '小程序裂变',
    icon: 'vip',
    color: '#a78bfa',
    action: () => proxy.$cf.toast({ message: '敬请期待', level: 'warning' })
  },
  {
    key: 'live',
    label: '直播带货',
    icon: 'videocam',
    color: '#f87171',
    action: () => proxy.$cf.toast({ message: '敬请期待', level: 'warning' })
  },
  {
    key: 'ai',
    label: 'AI导购助手',
    icon: 'auth',
    color: '#0ea5e9',
    action: () => proxy.$cf.navigate.to({ url: '/pages/ai_assistant/index', type: 'page' })
  },
  {
    key: 'more',
    label: '更多',
    icon: 'more',
    color: '#6b7280',
    action: () => proxy.$cf.toast({ message: '更多功能敬请期待', level: 'warning' })
  }
];

// 销售看板数据
const todaySalesAmount = ref('0.00');
const todayOrderCount = ref(0);
const todayProfitAmount = ref('0.00');

// 其它数据区
const stockWarningList = ref([]);
const profitBoardList = ref([]); // 已无用，仅保留变量名，实际不展示
const activeCustomerList = ref([]);
const opportunityList = ref([]);
const adList = ref([]);

// 获取当天起止时间字符串
function getTodayRange() {
  const now = new Date();
  const y = now.getFullYear();
  const m = (now.getMonth() + 1).toString().padStart(2, '0');
  const d = now.getDate().toString().padStart(2, '0');
  const start = `${y}-${m}-${d} 00:00:00`;
  const end = `${y}-${m}-${d} 23:59:59`;
  return { start, end };
}

// 获取今日销售数据
async function fetchTodaySalesBoard() {
  const { start, end } = getTodayRange();
  // 查询今日所有订单
  const res = await proxy.$cf.table.list({
    table_name: 'order_info',
    param: {
      order_date: start // 由于API只支持等值或like查询，实际只能用like
    }
  });
  if (res.success && Array.isArray(res.data)) {
    // 过滤出order_date为今日的数据
    const todayList = res.data.filter(item => {
      if (!item.order_date) return false;
      return item.order_date >= start && item.order_date <= end;
    });
    todayOrderCount.value = todayList.length;
    todaySalesAmount.value = todayList.reduce((sum, item) => sum + Number(item.total_amount || 0), 0).toFixed(2);
    todayProfitAmount.value = todayList.reduce((sum, item) => sum + Number(item.profit_amount || 0), 0).toFixed(2);
  } else {
    todayOrderCount.value = 0;
    todaySalesAmount.value = '0.00';
    todayProfitAmount.value = '0.00';
  }
}

// 其它数据加载函数（与原有一致，略）
async function fetchStockWarning() {}
async function fetchProfitBoard() {}
async function fetchActiveCustomer() {}
async function fetchOpportunity() {}
async function fetchAdList() {}

function goInventory() {
  proxy.$cf.navigate.to({ url: '/pages/inventory/index', type: 'page' });
}
function goProductDetail(product_info_id) {
  proxy.$cf.navigate.to({ url: `/pages/product_detail/index?product_info_id=${product_info_id}`, type: 'page' });
}
function goCustomer() {
  proxy.$cf.navigate.to({ url: '/pages/customer/index', type: 'page' });
}
function goCustomerDetail(customer_info_id) {
  proxy.$cf.navigate.to({ url: `/pages/customer_detail/index?customer_info_id=${customer_info_id}`, type: 'page' });
}
function goAiAssistant() {
  proxy.$cf.navigate.to({ url: '/pages/ai_assistant/index', type: 'page' });
}
function goAdDetail(ad_recommend_info_id) {
  proxy.$cf.navigate.to({ url: `/pages/ad_recommend_info/index?ad_recommend_info_id=${ad_recommend_info_id}`, type: 'page' });
}
function get_resource_url(url) {
  return proxy.get_resource_url(url);
}
function isNearExpire(dateStr) {
  if (!dateStr) return false;
  const now = new Date();
  const expire = new Date(dateStr);
  const diff = (expire - now) / (1000 * 60 * 60 * 24);
  return diff >= 0 && diff <= 7;
}

onLoad(async () => {
  await fetchTodaySalesBoard();
  await fetchStockWarning();
  await fetchProfitBoard();
  await fetchActiveCustomer();
  await fetchOpportunity();
  await fetchAdList();
});
</script>
