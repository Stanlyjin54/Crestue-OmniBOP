<template>
  <base-layout>
    <view class="p-2">
      <!-- 广告详情卡片 -->
      <view v-if="adDetail" class="bg-white rounded-xl shadow-lg p-4 mb-4 flex flex-col items-center">
        <!-- 广告图片 -->
        <image
          v-if="adDetail.ad_image && adDetail.ad_image.length > 0"
          :src="get_resource_url(adDetail.ad_image[0].url)"
          mode="aspectFit"
          class="w-full max-w-xs h-48 rounded-lg object-cover mb-4 shadow-md"
        />
        <!-- 标题 -->
        <text class="block text-2xl font-bold text-primary-700 mb-2 text-center">{{ adDetail.ad_title }}</text>
        <!-- CPC费用（如有） -->
        <view v-if="adDetail.cpc_fee !== undefined && adDetail.cpc_fee !== null" class="flex items-center mb-2">
          <uni-tag text="CPC费用" type="warning" size="small" class="mr-2"/>
          <text class="text-warning-500 font-semibold text-lg">{{ adDetail.cpc_fee }}</text>
        </view>
        <!-- 跳转链接 -->
        <view v-if="adDetail.ad_url" class="w-full flex justify-center mt-4">
          <button
            class="bg-primary-500 text-white rounded-full px-8 py-2 text-base font-semibold flex items-center gap-2 shadow-md active:scale-95 transition"
            @click="handleJump"
          >
            <uni-icons type="forward" color="#fff" size="22"/>
            跳转广告链接
          </button>
        </view>
      </view>
      <!-- 加载中或无数据提示 -->
      <view v-else class="flex flex-col items-center justify-center h-64">
        <uni-icons type="spinner-cycle" size="36" color="#9ca3af" class="mb-2 animate-spin"/>
        <text class="text-neutral-400 text-base">正在加载广告详情...</text>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const adDetail = ref(null);
const routeParams = ref({});

// 获取页面参数
onLoad(async (option) => {
  routeParams.value = option || {};
  if (!routeParams.value.ad_recommend_info_id) {
    proxy.$cf.toast({ message: '广告ID缺失', level: 'error' });
    return;
  }
  await fetchAdDetail(routeParams.value.ad_recommend_info_id);
});

// 获取广告详情
async function fetchAdDetail(id) {
  proxy.$cf.loading.showLoading({ title: '加载中' });
  const res = await proxy.$cf.table.get({
    table_name: 'ad_recommend_info',
    param: { ad_recommend_info_id: Number(id) }
  });
  proxy.$cf.loading.hideLoading();
  if (res.success && res.data) {
    adDetail.value = res.data;
  } else {
    adDetail.value = null;
    proxy.$cf.toast({ message: res.message || '广告详情加载失败', level: 'error' });
  }
}

// 跳转外部链接
async function handleJump() {
  if (!adDetail.value || !adDetail.value.ad_url) {
    proxy.$cf.toast({ message: '无有效跳转链接', level: 'warning' });
    return;
  }
  await proxy.$cf.navigate.to({
    url: adDetail.value.ad_url,
    type: 'external'
  });
}

// 图片资源url辅助函数
function get_resource_url(url) {
  return proxy.get_resource_url(url);
}
</script>