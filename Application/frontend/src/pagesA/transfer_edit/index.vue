<template>
  <base-layout>
    <!-- 调拨编辑弹窗 -->
    <uni-popup ref="editPopup" type="center">
      <view class="bg-white p-4 w-80 rounded-xl shadow-lg">
        <view class="mb-4 flex items-center gap-2">
          <uni-icons type="tune-filled" color="#4b5563" size="28" />
          <text class="font-bold text-lg text-primary-600">编辑调拨信息</text>
        </view>
        <uni-forms
          ref="formRef"
          :modelValue="formData"
          label-position="top"
          class="mb-2"
        >
          <uni-forms-item name="from_warehouse_info_warehouse_info_id_1" label="调出仓库" required>
            <uni-data-select
              v-model="formData.from_warehouse_info_warehouse_info_id_1"
              :localdata="warehouseList"
              :clear="false"
              placeholder="请选择调出仓库"
            />
          </uni-forms-item>
          <uni-forms-item name="to_warehouse_info_warehouse_info_id_2" label="调入仓库" required>
            <uni-data-select
              v-model="formData.to_warehouse_info_warehouse_info_id_2"
              :localdata="warehouseList"
              :clear="false"
              placeholder="请选择调入仓库"
            />
          </uni-forms-item>
          <uni-forms-item name="product_info_product_info_id_1" label="商品" required>
            <uni-data-select
              v-model="formData.product_info_product_info_id_1"
              :localdata="productList"
              :clear="false"
              placeholder="请选择商品"
            />
          </uni-forms-item>
          <uni-forms-item name="quantity" label="调拨数量" required>
            <uni-easyinput
              type="number"
              v-model="formData.quantity"
              clearable
              placeholder="请输入调拨数量"
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item name="transfer_status_enum_transfer_status_enum_id_1" label="调拨状态" required>
            <uni-data-select
              v-model="formData.transfer_status_enum_transfer_status_enum_id_1"
              :localdata="statusList"
              :clear="false"
              placeholder="请选择调拨状态"
            />
          </uni-forms-item>
          <uni-forms-item name="apply_date" label="申请时间" required>
            <uni-datetime-picker
              type="datetime"
              v-model="formData.apply_date"
              placeholder="请选择申请时间"
            />
          </uni-forms-item>
          <uni-forms-item name="confirm_user_info_user_info_id_2" label="确认人">
            <uni-easyinput
              type="text"
              v-model="formData.confirm_user_name"
              :disabled="true"
              placeholder="确认人（自动填充）"
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item name="confirm_date" label="确认时间">
            <uni-datetime-picker
              type="datetime"
              v-model="formData.confirm_date"
              placeholder="请选择确认时间"
            />
          </uni-forms-item>
        </uni-forms>
        <view class="flex gap-3 mt-4">
          <button
            class="flex-1 bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow"
            @click="onSubmit"
          >保存</button>
          <button
            class="flex-1 bg-neutral-200 text-neutral-700 rounded-full px-4 py-2 text-sm"
            @click="closePopup"
          >取消</button>
        </view>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const formData = ref({
  transfer_info_id: null,
  from_warehouse_info_warehouse_info_id_1: '',
  to_warehouse_info_warehouse_info_id_2: '',
  product_info_product_info_id_1: '',
  quantity: '',
  transfer_status_enum_transfer_status_enum_id_1: '',
  apply_user_info_user_info_id_1: '',
  apply_date: '',
  confirm_user_info_user_info_id_2: '',
  confirm_user_name: '',
  confirm_date: ''
});

const warehouseList = ref([]);
const productList = ref([]);
const statusList = ref([]);
const formRef = ref(null);

// 打开弹窗并初始化
const open = async (transfer_info_id = null) => {
  await fetchSelectOptions();
  resetForm();
  if (transfer_info_id) {
    await fetchDetail(transfer_info_id);
  } else {
    // 新建时自动填充申请人和时间
    let userRes = await proxy.$cf.login.getLoginUser();
    if (userRes.success && userRes.data) {
      formData.value.apply_user_info_user_info_id_1 = userRes.data.user_info_id;
    }
    formData.value.apply_date = getNowDatetime();
  }
  proxy.$refs.editPopup.open();
};

// 关闭弹窗
const closePopup = () => {
  proxy.$refs.editPopup.close();
};

// 获取下拉选项
async function fetchSelectOptions() {
  // 仓库
  const warehouseRes = await proxy.$cf.table.list({
    table_name: 'warehouse_info',
    param: {}
  });
  warehouseList.value = (warehouseRes.success && warehouseRes.data)
    ? warehouseRes.data.map(item => ({
        value: item.warehouse_info_id,
        text: item.warehouse_name
      }))
    : [];
  // 商品
  const productRes = await proxy.$cf.table.list({
    table_name: 'product_info',
    param: {}
  });
  productList.value = (productRes.success && productRes.data)
    ? productRes.data.map(item => ({
        value: item.product_info_id,
        text: item.product_name
      }))
    : [];
  // 状态
  const statusRes = await proxy.$cf.table.list({
    table_name: 'transfer_status_enum',
    param: {}
  });
  statusList.value = (statusRes.success && statusRes.data)
    ? statusRes.data.map(item => ({
        value: item.transfer_status_enum_id,
        text: item.status_name
      }))
    : [];
}

// 获取调拨详情
async function fetchDetail(transfer_info_id) {
  const res = await proxy.$cf.table.get({
    table_name: 'transfer_info',
    param: { transfer_info_id }
  });
  if (res.success && res.data) {
    Object.assign(formData.value, res.data);
    // 查询确认人用户名（如果有）
    if (res.data.confirm_user_info_user_info_id_2) {
      const userRes = await proxy.$cf.table.get({
        table_name: 'user_info',
        param: { user_info_id: res.data.confirm_user_info_user_info_id_2 }
      });
      if (userRes.success && userRes.data) {
        formData.value.confirm_user_name = userRes.data.username || '';
      }
    }
  }
}

// 重置表单
function resetForm() {
  formData.value = {
    transfer_info_id: null,
    from_warehouse_info_warehouse_info_id_1: '',
    to_warehouse_info_warehouse_info_id_2: '',
    product_info_product_info_id_1: '',
    quantity: '',
    transfer_status_enum_transfer_status_enum_id_1: '',
    apply_user_info_user_info_id_1: '',
    apply_date: '',
    confirm_user_info_user_info_id_2: '',
    confirm_user_name: '',
    confirm_date: ''
  };
}

// 获取当前时间字符串
function getNowDatetime() {
  const now = new Date();
  const pad = n => n.toString().padStart(2, '0');
  return `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())} ${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`;
}

// 保存
async function onSubmit() {
  // 校验必填
  if (
    !formData.value.from_warehouse_info_warehouse_info_id_1 ||
    !formData.value.to_warehouse_info_warehouse_info_id_2 ||
    !formData.value.product_info_product_info_id_1 ||
    !formData.value.quantity ||
    !formData.value.transfer_status_enum_transfer_status_enum_id_1 ||
    !formData.value.apply_user_info_user_info_id_1 ||
    !formData.value.apply_date
  ) {
    await proxy.$cf.toast({ message: '请填写完整必填信息', level: 'error' });
    return;
  }
  // 不能调拨到同一仓库
  if (
    formData.value.from_warehouse_info_warehouse_info_id_1 ==
    formData.value.to_warehouse_info_warehouse_info_id_2
  ) {
    await proxy.$cf.toast({ message: '调出仓库与调入仓库不能相同', level: 'error' });
    return;
  }
  let param = { ...formData.value };
  // confirm_user_info_user_info_id_2 仅传ID
  if (param.confirm_user_name) delete param.confirm_user_name;
  let apiRes;
  if (param.transfer_info_id) {
    // 编辑
    apiRes = await proxy.$cf.table.update({
      table_name: 'transfer_info',
      param
    });
  } else {
    // 新增
    apiRes = await proxy.$cf.table.add({
      table_name: 'transfer_info',
      param
    });
  }
  if (apiRes.success) {
    await proxy.$cf.toast({ message: '保存成功', level: 'success' });
    closePopup();
    // 通知主页面刷新列表
    proxy.$emit('refresh');
  } else {
    await proxy.$cf.toast({ message: apiRes.message || '保存失败', level: 'error' });
  }
}

// 页面参数接收
onLoad((option) => {
  // 若有id参数，自动打开弹窗并加载数据
  if (option && option.transfer_info_id) {
    open(Number(option.transfer_info_id));
  }
});

// 暴露 open 方法供父页面调用
defineExpose({ open, closePopup });
</script>