<template>
  <base-layout>
    <view class="p-2">
      <!-- 标题 -->
      <view class="mb-4">
        <text class="block text-2xl font-bold text-primary-700 mb-1">智能采购建议</text>
        <text class="block text-base text-neutral-500">基于库存与销售智能分析，助你高效补货</text>
      </view>

      <!-- 建议列表 -->
      <view v-if="suggestions.length > 0" class="columns-1 md:columns-2 gap-4">
        <view
          v-for="item in suggestions"
          :key="item.procurement_suggestion_id"
          class="bg-white rounded-xl shadow-lg p-3 mb-4 break-inside-avoid flex flex-col"
        >
          <view class="flex gap-3 items-center mb-2">
            <image
              v-if="item.product_image"
              :src="item.product_image"
              class="w-16 h-16 rounded-lg object-cover border border-neutral-200"
              mode="aspectFill"
            />
            <view class="flex-1">
              <text class="block text-lg font-semibold text-primary-700">
                {{ item.product_name || '未知商品' }}
              </text>
              <uni-tag
                v-if="item.suggest_quantity > 0"
                :text="'建议采购 ' + item.suggest_quantity + ' 件'"
                size="normal"
                type="primary"
                class="mt-1"
              />
            </view>
          </view>
          <view class="mb-2">
            <text class="text-neutral-500 text-sm">建议原因：</text>
            <text class="text-neutral-700 text-sm">{{ item.reason || '无' }}</text>
          </view>
          <view class="flex items-center justify-between mt-2">
            <text class="text-xs text-neutral-400">建议日期：{{ item.suggest_date }}</text>
            <button
              class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-md"
              @click="goToPurchase"
            >
              <uni-icons type="cart" class="mr-1" size="18" color="#fff" />去采购
            </button>
          </view>
        </view>
      </view>
      <view v-else class="flex flex-col items-center justify-center mt-16">
        <image src="https://www.codeflying.net/preview/flat-lay-food.jpg" class="w-32 h-32 rounded-xl mb-4" mode="aspectFill" />
        <text class="text-neutral-400 text-base">暂无采购建议，库存充足~</text>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const suggestions = ref([]);

// 获取建议及商品信息
async function fetchSuggestions() {
  // 1. 查询所有采购建议
  const res = await proxy.$cf.table.list({
    table_name: 'procurement_suggestion',
    param: {}
  });
  if (!res.success) {
    proxy.$cf.toast({ message: res.message || '获取采购建议失败', level: 'error' });
    return;
  }
  const suggestionList = res.data || [];

  // 2. 批量获取商品信息（只查用到的字段）
  // 取所有商品ID
  const productIds = suggestionList.map(item => item.product_info_product_info_id_1).filter(Boolean);
  let productMap = {};
  if (productIds.length > 0) {
    // 由于API只支持等值查询，需循环获取
    let allProducts = [];
    for (let pid of [...new Set(productIds)]) {
      const prodRes = await proxy.$cf.table.get({
        table_name: 'product_info',
        param: { product_info_id: pid }
      });
      if (prodRes.success && prodRes.data) {
        allProducts.push(prodRes.data);
      }
    }
    // 映射ID->商品
    productMap = Object.fromEntries(
      allProducts.map(p => [p.product_info_id, p])
    );
  }

  // 3. 合成展示数据
  suggestions.value = suggestionList.map(item => {
    const product = productMap[item.product_info_product_info_id_1] || {};
    let product_image = '';
    if (product.images && Array.isArray(product.images) && product.images.length > 0) {
      product_image = proxy.get_resource_url(product.images[0].url);
    }
    return {
      procurement_suggestion_id: item.procurement_suggestion_id,
      product_name: product.product_name || '',
      product_image,
      suggest_quantity: item.suggest_quantity,
      reason: item.reason,
      suggest_date: item.suggest_date
    };
  });
}

// 跳转到采购列表页面
function goToPurchase() {
  proxy.$cf.navigate.to({
    url: '/pages/purchase/index',
    type: 'page'
  });
}

onLoad(() => {
  fetchSuggestions();
});
</script>