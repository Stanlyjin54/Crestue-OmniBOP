<template>
  <base-layout>
    <view class="p-2">
      <view class="flex items-center justify-between mb-4">
        <view class="flex items-center gap-2">
          <uni-icons type="wallet-filled" size="28" color="#0ea5e9"/>
          <text class="text-xl font-bold text-primary-700">订单详情</text>
        </view>
      </view>
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <view class="flex items-center gap-3 mb-2">
          <uni-tag :text="orderDetail.order_status_enum_order_status_enum_id_1_status_name || '未知状态'" type="primary" size="normal" circle />
          <text class="text-base font-semibold text-primary-700">订单编号：</text>
          <text class="text-base text-neutral-700">{{ orderDetail.order_no }}</text>
        </view>
        <view class="flex flex-wrap gap-4 text-neutral-600 text-sm">
          <view>
            <text class="font-medium">下单时间：</text>
            <text>{{ formatDatetime(orderDetail.order_date) }}</text>
          </view>
          <view>
            <text class="font-medium">订单总金额：</text>
            <text class="text-success-600 font-bold">￥{{ orderDetail.total_amount }}</text>
          </view>
          <view v-if="orderDetail.profit_amount !== undefined && orderDetail.profit_amount !== null">
            <text class="font-medium">利润：</text>
            <text class="text-primary-500">￥{{ orderDetail.profit_amount }}</text>
          </view>
          <view v-if="orderDetail.shipping_fee !== undefined && orderDetail.shipping_fee !== null">
            <text class="font-medium">运费：</text>
            <text>￥{{ orderDetail.shipping_fee }}</text>
          </view>
        </view>
      </view>
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4 flex items-center gap-4">
        <uni-icons type="person-filled" size="28" color="#4b5563"/>
        <view class="flex-1">
          <view class="flex items-center gap-2 mb-1">
            <text class="font-semibold text-primary-700">客户：</text>
            <text class="text-base">{{ orderDetail.customer_name || '-' }}</text>
          </view>
          <view class="flex flex-wrap gap-4 text-neutral-600 text-sm">
            <view v-if="orderDetail.phone">
              <uni-icons type="phone" size="16" color="#0ea5e9"/>
              <text class="ml-1">{{ orderDetail.phone }}</text>
            </view>
            <view v-if="orderDetail.email">
              <uni-icons type="email" size="16" color="#0ea5e9"/>
              <text class="ml-1">{{ orderDetail.email }}</text>
            </view>
            <view v-if="orderDetail.address">
              <uni-icons type="location" size="16" color="#0ea5e9"/>
              <text class="ml-1">{{ orderDetail.address }}</text>
            </view>
          </view>
        </view>
      </view>
      <view class="mb-4">
        <view class="flex items-center gap-2 mb-2">
          <uni-icons type="cart-filled" size="22" color="#22c55e"/>
          <text class="text-lg font-semibold text-primary-700">商品明细</text>
        </view>
        <view v-if="orderItems.length > 0" class="grid grid-cols-1 gap-4">
          <view v-for="item in orderItems" :key="item.order_item_id" class="bg-white rounded-lg shadow-md p-3 flex gap-3 items-center">
            <image v-if="item.product_images && item.product_images.length > 0" :src="get_resource_url(item.product_images[0].url)" class="w-16 h-16 rounded-lg object-cover" mode="aspectFill"/>
            <view class="flex-1">
              <view class="flex items-center gap-2 mb-1">
                <text class="font-semibold text-primary-700">{{ item.product_name }}</text>
                <uni-tag :text="item.product_status_enum_product_status_enum_id_1_status_name || '—'" size="small" type="primary"/>
              </view>
              <view class="text-sm text-neutral-600 mb-1">
                <text>规格：{{ item.product_specification || '-' }}</text>
                <text class="ml-4">品牌：{{ item.product_brand || '-' }}</text>
              </view>
              <view class="flex gap-3 text-sm text-neutral-700">
                <text>数量：<text class="font-bold">{{ item.quantity }}</text></text>
                <text>单价：<text class="text-success-600">￥{{ item.unit_price }}</text></text>
                <text>总价：<text class="text-primary-500 font-bold">￥{{ item.total_price }}</text></text>
              </view>
            </view>
          </view>
        </view>
        <view v-else class="text-neutral-400 text-center py-4">暂无商品明细</view>
      </view>
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <view class="flex items-center gap-2 mb-2">
          <uni-icons type="settings-filled" size="20" color="#0ea5e9"/>
          <text class="text-lg font-semibold text-primary-700">订单操作</text>
        </view>
        <view class="flex gap-3 flex-wrap">
          <button v-if="showPayBtn" class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm" @click="payOrder">
            <uni-icons type="wallet" size="18" color="#fff" class="mr-1"/>去支付
          </button>
        </view>
      </view>
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <view class="flex items-center gap-2 mb-2">
          <uni-icons type="tune-filled" size="20" color="#0ea5e9"/>
          <text class="text-lg font-semibold text-primary-700">订单进度</text>
        </view>
        <view class="flex flex-col gap-2 text-sm text-neutral-700">
          <view>
            <text>下单时间：</text>
            <text>{{ formatDatetime(orderDetail.order_date) }}</text>
          </view>
          <view v-if="orderDetail.payment_date">
            <text>支付时间：</text>
            <text>{{ formatDatetime(orderDetail.payment_date) }}</text>
          </view>
          <view v-if="orderDetail.delivery_date">
            <text>发货时间：</text>
            <text>{{ formatDatetime(orderDetail.delivery_date) }}</text>
          </view>
          <view v-if="orderDetail.receipt_date">
            <text>收货时间：</text>
            <text>{{ formatDatetime(orderDetail.receipt_date) }}</text>
          </view>
          <view v-if="orderDetail.order_status_enum_order_status_enum_id_1_status_name">
            <text>当前状态：</text>
            <text class="font-bold text-primary-500">{{ orderDetail.order_status_enum_order_status_enum_id_1_status_name }}</text>
          </view>
        </view>
      </view>
      <view>
        <base-pay-panel ref="pay_panel"
                        v-if="orderDetail.order_info_id"
                        :price="orderDetail.total_amount"
                        defaultChannel="mock"
                        :productId="orderDetail.order_info_id"
                        :userId="orderDetail.user_info_user_info_id_1"
                        :productSubject="orderDetail.order_no"
                        :orderNo="orderDetail.order_info_id"
                        orderTableName="order_info"
                        orderTypeFiled="general_orders"
                        @paySuccess="paySuccess"/>
      </view>
    </view>
  </base-layout>
</template>
<script setup>
const { proxy } = getCurrentInstance();
const orderDetail = ref({});
const orderItems = ref([]);
const showPayBtn = ref(false);
function formatDatetime(dt) { if (!dt) return '-'; return dt.length > 10 ? dt.substring(0, 16).replace('T', ' ') : dt; }
function get_resource_url(path) { return proxy.get_resource_url(path); }
async function fetchOrderDetail(order_info_id) {
  const res = await proxy.$cf.table.get({ table_name: 'order_info', param: { order_info_id } });
  if (!res.success || !res.data) { proxy.$cf.toast({ message: '订单不存在', level: 'error' }); return; }
  orderDetail.value = res.data;
  const itemRes = await proxy.$cf.table.list({ table_name: 'order_item', param: { order_info_order_info_id_1: order_info_id } });
  if (itemRes.success) {
    const items = [];
    for (const item of itemRes.data) {
      let prod = {};
      if (item.product_info_product_info_id_1) {
        const prodRes = await proxy.$cf.table.get({ table_name: 'product_info', param: { product_info_id: item.product_info_product_info_id_1 } });
        if (prodRes.success && prodRes.data) { prod = prodRes.data; }
      }
      items.push({ ...item, product_name: prod.product_name, product_images: prod.images || [], product_specification: prod.specification, product_brand: prod.brand, product_status_enum_product_status_enum_id_1_status_name: prod.product_status_enum_product_status_enum_id_1_status_name });
    }
    orderItems.value = items;
  }
  await updateOrderStatusBtn(order_info_id);
}
async function updateOrderStatusBtn(order_info_id) {
  const res = await proxy.$cf.payment.getPayOrderMessage({ orderNo: order_info_id });
  showPayBtn.value = false;
  if (!res.success || !res.data) return;
  if (res.data.orderStatus === undefined || res.data.orderStatus === 'PENDING' || res.data.orderStatus === '待付款') showPayBtn.value = true;
}
function payOrder() { proxy.$refs.pay_panel.show(); }
async function paySuccess(paymentOrderId) {
  await proxy.$cf.toast({ message: '支付成功', level: 'success' });
  fetchOrderDetail(orderDetail.value.order_info_id);
}
onLoad((option) => { if (option && option.order_info_id) { fetchOrderDetail(Number(option.order_info_id)); } });
</script>