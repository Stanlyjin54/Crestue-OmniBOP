<template>
  <base-layout>
    <!-- 弹窗形式，内容居中 -->
    <uni-popup ref="popup" type="center">
      <view class="bg-white p-4 w-80 rounded-xl shadow-xl">
        <view class="mb-4 flex items-center gap-2">
          <uni-icons type="compose" size="24" color="#4b5563" />
          <text class="text-lg font-bold text-primary-600">编辑订单</text>
        </view>
        <uni-forms :modelValue="formData" label-position="top" ref="orderForm">
          <uni-forms-item name="order_no" label="订单编号" required>
            <uni-easyinput
              v-model="formData.order_no"
              type="text"
              placeholder="请输入订单编号"
              clearable
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item name="customer_info_customer_info_id_1" label="客户" required>
            <uni-data-select
              v-model="formData.customer_info_customer_info_id_1"
              :localdata="customerList"
              placeholder="请选择客户"
              class="box-border"
            ></uni-data-select>
          </uni-forms-item>
          <uni-forms-item name="user_info_user_info_id_1" label="下单用户" required>
            <uni-data-select
              v-model="formData.user_info_user_info_id_1"
              :localdata="userList"
              placeholder="请选择下单用户"
              class="box-border"
            ></uni-data-select>
          </uni-forms-item>
          <uni-forms-item name="order_status_enum_order_status_enum_id_1" label="订单状态" required>
            <uni-data-select
              v-model="formData.order_status_enum_order_status_enum_id_1"
              :localdata="orderStatusList"
              placeholder="请选择订单状态"
              class="box-border"
            ></uni-data-select>
          </uni-forms-item>
          <uni-forms-item name="total_amount" label="订单总金额" required>
            <uni-easyinput
              v-model="formData.total_amount"
              type="number"
              placeholder="请输入订单总金额"
              clearable
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item name="profit_amount" label="利润金额">
            <uni-easyinput
              v-model="formData.profit_amount"
              type="number"
              placeholder="请输入利润金额"
              clearable
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item name="order_date" label="下单时间" required>
            <uni-datetime-picker
              type="datetime"
              v-model="formData.order_date"
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item name="shipping_fee" label="运费">
            <uni-easyinput
              v-model="formData.shipping_fee"
              type="number"
              placeholder="请输入运费"
              clearable
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item name="commission" label="平台佣金">
            <uni-easyinput
              v-model="formData.commission"
              type="number"
              placeholder="请输入平台佣金"
              clearable
              class="box-border"
            />
          </uni-forms-item>
        </uni-forms>
        <view class="flex gap-4 mt-6">
          <button
            class="flex-1 bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-md"
            @click="submitForm"
          >
            保存
          </button>
          <button
            class="flex-1 bg-neutral-200 text-primary-600 rounded-full px-4 py-2 text-sm"
            @click="closePopup"
          >
            取消
          </button>
        </view>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const formData = ref({
  order_info_id: null,
  order_no: '',
  customer_info_customer_info_id_1: '',
  user_info_user_info_id_1: '',
  order_status_enum_order_status_enum_id_1: '',
  total_amount: '',
  profit_amount: '',
  commission: '',
  shipping_fee: '',
  order_date: '',
});

const customerList = ref([]);
const userList = ref([]);
const orderStatusList = ref([]);

const emits = defineEmits(['refresh', 'close']);

// 获取下拉数据
async function fetchSelectData() {
  // 客户列表
  const customerRes = await proxy.$cf.table.list({
    table_name: 'customer_info',
    param: {},
  });
  if (customerRes.success) {
    customerList.value = customerRes.data.map(item => ({
      value: item.customer_info_id,
      text: item.customer_name
    }));
  }
  // 用户列表
  const userRes = await proxy.$cf.table.list({
    table_name: 'user_info',
    param: {},
  });
  if (userRes.success) {
    userList.value = userRes.data.map(item => ({
      value: item.user_info_id,
      text: item.username
    }));
  }
  // 订单状态
  const statusRes = await proxy.$cf.table.list({
    table_name: 'order_status_enum',
    param: {},
  });
  if (statusRes.success) {
    orderStatusList.value = statusRes.data.map(item => ({
      value: item.order_status_enum_id,
      text: item.status_name
    }));
  }
}

// 获取订单详情
async function fetchOrderDetail(order_info_id) {
  const res = await proxy.$cf.table.get({
    table_name: 'order_info',
    param: { order_info_id }
  });
  if (res.success && res.data) {
    // 只赋值允许编辑的字段
    formData.value = {
      order_info_id: res.data.order_info_id,
      order_no: res.data.order_no,
      customer_info_customer_info_id_1: res.data.customer_info_customer_info_id_1,
      user_info_user_info_id_1: res.data.user_info_user_info_id_1,
      order_status_enum_order_status_enum_id_1: res.data.order_status_enum_order_status_enum_id_1,
      total_amount: res.data.total_amount,
      profit_amount: res.data.profit_amount,
      commission: res.data.commission,
      shipping_fee: res.data.shipping_fee,
      order_date: res.data.order_date,
    };
  }
}

// 弹窗显示（供父页面调用）
function open(order_info_id) {
  fetchSelectData();
  if (order_info_id) {
    fetchOrderDetail(order_info_id);
  }
  proxy.$refs.popup.open();
}

// 关闭弹窗
function closePopup() {
  proxy.$refs.popup.close();
  emits('close');
}

// 表单提交
async function submitForm() {
  // 校验必填
  if (!formData.value.order_no || !formData.value.customer_info_customer_info_id_1 ||
      !formData.value.user_info_user_info_id_1 || !formData.value.order_status_enum_order_status_enum_id_1 ||
      !formData.value.total_amount || !formData.value.order_date) {
    await proxy.$cf.toast({ message: '请填写所有必填项', level: 'error' });
    return;
  }
  // 金额校验
  if (Number(formData.value.total_amount) < 0) {
    await proxy.$cf.toast({ message: '订单金额不能为负数', level: 'error' });
    return;
  }
  // 时间格式校验
  if (!/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(formData.value.order_date)) {
    await proxy.$cf.toast({ message: '下单时间格式不正确', level: 'error' });
    return;
  }
  // 提交
  const updateRes = await proxy.$cf.table.update({
    table_name: 'order_info',
    param: { ...formData.value }
  });
  if (updateRes.success) {
    await proxy.$cf.toast({ message: '订单信息已保存', level: 'success' });
    closePopup();
    emits('refresh'); // 通知父页面刷新
  } else {
    await proxy.$cf.toast({ message: updateRes.message || '保存失败', level: 'error' });
  }
}

// 页面参数获取
onLoad((option) => {
  if (option && option.order_info_id) {
    open(option.order_info_id);
  } else {
    open();
  }
});

// 供父页面调用open方法
defineExpose({ open });

</script>