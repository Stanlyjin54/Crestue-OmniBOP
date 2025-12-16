<template>
  <base-layout>
    <view class="p-2">
      <view class="text-2xl font-bold text-primary-700 mb-4">创建销售订单</view>
      <uni-forms :modelValue="orderForm" label-position="top">
        <uni-forms-item name="customer_info_customer_info_id_1" label="客户" required>
          <uni-data-select v-model="orderForm.customer_info_customer_info_id_1" :localdata="customerOptions" placeholder="请选择客户" clear />
        </uni-forms-item>
        <uni-forms-item name="remark" label="备注">
          <uni-easyinput v-model="orderForm.remark" type="text" placeholder="订单备注(可选)" clearable />
        </uni-forms-item>
      </uni-forms>
      <view class="mb-4 mt-2">
        <view class="flex items-center mb-2">
          <text class="font-bold text-lg text-primary-700">商品明细</text>
          <button class="ml-auto bg-primary-500 text-white rounded-full px-3 py-1 text-sm flex items-center gap-1" @click="addItem">
            <uni-icons type="plus" size="18" /> 添加商品
          </button>
        </view>
        <view v-for="(item, idx) in items" :key="idx" class="bg-white rounded-lg shadow-md p-3 mb-3 flex items-center gap-2">
          <uni-data-select class="flex-1" v-model="item.product_info_product_info_id_1" :localdata="productOptions" placeholder="选择商品" clear />
          <uni-easyinput class="w-20 box-border" type="number" v-model="item.quantity" placeholder="数量" min="1" />
          <text class="text-neutral-500">单价: {{ getProductPrice(item.product_info_product_info_id_1) }}</text>
          <button class="bg-danger-500 text-white rounded-full px-2 py-1 text-xs ml-2" @click="removeItem(idx)">
            <uni-icons type="close" size="16" />
          </button>
        </view>
      </view>
      <view class="flex items-center justify-between mb-4">
        <text class="text-lg font-bold text-primary-700">订单总金额：</text>
        <text class="text-success-600 text-xl font-bold">￥{{ totalAmount }}</text>
      </view>
      <button class="w-full bg-primary-500 text-white rounded-full py-3 text-base font-bold shadow-xl" @click="submitOrder" :loading="loading">
        提交订单并支付
      </button>
    </view>
  </base-layout>
</template>
<script setup>
const { proxy } = getCurrentInstance();
const orderForm = ref({ customer_info_customer_info_id_1: '', remark: '' });
const items = ref([{ product_info_product_info_id_1: '', quantity: 1 }]);
const customerOptions = ref([]);
const productOptions = ref([]);
const productMap = ref({});
const loading = ref(false);
const totalAmount = computed(() => {
  return items.value.reduce((sum, item) => {
    const prod = productMap.value[item.product_info_product_info_id_1];
    const price = prod && prod.unit_price ? Number(prod.unit_price) : 0;
    const qty = Number(item.quantity) || 0;
    return sum + price * qty;
  }, 0).toFixed(2);
});
function getProductPrice(pid) {
  const prod = productMap.value[pid];
  return prod && prod.unit_price ? prod.unit_price : '--';
}
async function fetchCustomers() {
  const res = await proxy.$cf.table.list({ table_name: 'customer_info', param: {} });
  if (res.success) {
    customerOptions.value = res.data.map(item => ({ value: item.customer_info_id, text: item.customer_name }));
  }
}
async function fetchProducts() {
  const res = await proxy.$cf.table.list({ table_name: 'product_info', param: {} });
  if (res.success) {
    productOptions.value = res.data.map(item => ({ value: item.product_info_id, text: item.product_name }));
    // 假定每个商品有一个默认单价，这里用一个map存储
    productMap.value = {};
    res.data.forEach(item => { productMap.value[item.product_info_id] = { ...item, unit_price: item.unit_price || 100 }; });
  }
}
function addItem() { items.value.push({ product_info_product_info_id_1: '', quantity: 1 }); }
function removeItem(idx) { items.value.splice(idx, 1); }
async function submitOrder() {
  if (loading.value) return;
  if (!orderForm.value.customer_info_customer_info_id_1) {
    await proxy.$cf.toast({ message: '请选择客户', level: 'error' }); return;
  }
  if (items.value.length === 0 || items.value.some(i => !i.product_info_product_info_id_1 || !i.quantity || Number(i.quantity) <= 0)) {
    await proxy.$cf.toast({ message: '请添加完整的商品明细', level: 'error' }); return;
  }
  loading.value = true;
  // 生成订单编号
  const now = new Date();
  const pad = n => n.toString().padStart(2, '0');
  const orderNo = 'ORD' + now.getFullYear() + pad(now.getMonth() + 1) + pad(now.getDate()) + pad(now.getHours()) + pad(now.getMinutes()) + pad(now.getSeconds()) + Math.floor(Math.random() * 1000);
  // 获取当前用户
  let userRes = await proxy.$cf.login.getLoginUser();
  if (!userRes.success) { await proxy.$cf.toast({ message: '请先登录', level: 'error' }); loading.value = false; return; }
  // 新增订单主表
  const addRes = await proxy.$cf.table.add({
    table_name: 'order_info',
    param: {
      order_no: orderNo,
      customer_info_customer_info_id_1: orderForm.value.customer_info_customer_info_id_1,
      user_info_user_info_id_1: userRes.data.user_info_id,
      order_status_enum_order_status_enum_id_1: 1,
      total_amount: totalAmount.value,
      order_date: `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())} ${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`
    }
  });
  if (!addRes.success) { await proxy.$cf.toast({ message: addRes.message || '下单失败', level: 'error' }); loading.value = false; return; }
  // 新增订单明细表
  for (const item of items.value) {
    await proxy.$cf.table.add({
      table_name: 'order_item',
      param: {
        order_info_order_info_id_1: addRes.data,
        product_info_product_info_id_1: item.product_info_product_info_id_1,
        quantity: item.quantity,
        unit_price: getProductPrice(item.product_info_product_info_id_1),
        total_price: (getProductPrice(item.product_info_product_info_id_1) * item.quantity).toFixed(2)
      }
    });
  }
  loading.value = false;
  // 跳转到支付页面
  proxy.$cf.navigate.to({ url: `/pages/order_pay/index?order_info_id=${addRes.data}`, type: 'page' });
}
onLoad(() => { fetchCustomers(); fetchProducts(); });
</script>