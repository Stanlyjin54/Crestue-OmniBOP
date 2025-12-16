<template>
  <base-layout>
    <view class="p-2">
      <!-- 标题 -->
      <view class="mb-4 flex items-center gap-2">
        <uni-icons type="scan" color="#0ea5e9" size="28" />
        <text class="text-xl font-bold text-primary-700">扫码录入</text>
      </view>

      <!-- 步骤提示 -->
      <view class="mb-4 bg-white rounded-xl shadow-lg p-3 flex items-center gap-2">
        <uni-icons type="info" color="#4b5563" size="22" />
        <text class="text-base text-neutral-700">请先扫码获取商品信息，再完善其他信息后提交。</text>
      </view>

      <!-- 扫码按钮 -->
      <view class="mb-4 flex justify-center">
        <button class="bg-primary-500 text-white rounded-full px-6 py-2 text-base font-semibold shadow-md flex items-center gap-2"
          @click="handleScan">
          <uni-icons type="scan" color="#fff" size="22" />
          扫码
        </button>
      </view>

      <!-- 表单 -->
      <view class="bg-white rounded-xl shadow-lg p-4 w-full box-border">
        <uni-forms ref="formRef" :modelValue="formData" label-position="top">
          <!-- 商品选择 -->
          <uni-forms-item name="product_info_product_info_id_1" label="商品" required>
            <uni-data-select
              v-model="formData.product_info_product_info_id_1"
              :localdata="productOptions"
              placeholder="请选择商品"
              clear
              placement="bottom"
            />
          </uni-forms-item>
          <!-- 仓库选择 -->
          <uni-forms-item name="warehouse_info_warehouse_info_id_1" label="仓库" required>
            <uni-data-select
              v-model="formData.warehouse_info_warehouse_info_id_1"
              :localdata="warehouseOptions"
              placeholder="请选择仓库"
              clear
              placement="bottom"
            />
          </uni-forms-item>
          <!-- 数量 -->
          <uni-forms-item name="quantity" label="数量" required>
            <uni-easyinput
              v-model="formData.quantity"
              type="number"
              placeholder="请输入数量"
              clearable
              class="box-border"
            />
          </uni-forms-item>
          <!-- 批次号 -->
          <uni-forms-item name="batch_number" label="批次号">
            <uni-easyinput
              v-model="formData.batch_number"
              type="text"
              placeholder="可选，输入批次号"
              clearable
              class="box-border"
            />
          </uni-forms-item>
          <!-- 效期 -->
          <uni-forms-item name="expire_date" label="效期">
            <uni-datetime-picker
              type="date"
              v-model="formData.expire_date"
              placeholder="请选择效期"
            />
          </uni-forms-item>
          <!-- 序列号 -->
          <uni-forms-item name="serial_number" label="序列号">
            <uni-easyinput
              v-model="formData.serial_number"
              type="text"
              placeholder="可选，输入序列号"
              clearable
              class="box-border"
            />
          </uni-forms-item>
        </uni-forms>

        <!-- 提交按钮 -->
        <view class="mt-6 flex justify-center">
          <button
            class="bg-success-500 text-white rounded-full px-8 py-2 text-base font-semibold shadow-lg flex items-center gap-2"
            @click="handleSubmit"
            :loading="loading"
          >
            <uni-icons type="checkmarkempty" color="#fff" size="22" />
            提交
          </button>
        </view>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

// 表单数据
const formData = ref({
  product_info_product_info_id_1: '',
  warehouse_info_warehouse_info_id_1: '',
  quantity: '',
  batch_number: '',
  expire_date: '',
  serial_number: ''
});
const loading = ref(false);

// 商品和仓库下拉数据
const productOptions = ref([]);
const warehouseOptions = ref([]);

// 表单ref
const formRef = ref(null);

// 页面加载时拉取商品和仓库选项
onLoad(async () => {
  await fetchProductOptions();
  await fetchWarehouseOptions();
});

// 拉取商品下拉
async function fetchProductOptions() {
  const res = await proxy.$cf.table.list({
    table_name: 'product_info',
    param: {}
  });
  if (res.success) {
    productOptions.value = (res.data || []).map(item => ({
      value: item.product_info_id,
      text: item.product_name + (item.barcode ? `（${item.barcode}）` : '')
    }));
  }
}

// 拉取仓库下拉
async function fetchWarehouseOptions() {
  const res = await proxy.$cf.table.list({
    table_name: 'warehouse_info',
    param: {}
  });
  if (res.success) {
    warehouseOptions.value = (res.data || []).map(item => ({
      value: item.warehouse_info_id,
      text: item.warehouse_name
    }));
  }
}

// 扫码
async function handleScan() {
  try {
    const scanRes = await proxy.$cf.qr.scanCode({ onlyFromCamera: false });
    if (!scanRes.success || !scanRes.result) {
      await proxy.$cf.toast({ message: '扫码失败，请重试', level: 'error' });
      return;
    }
    // 假定扫码内容为条形码，查找商品
    const productRes = await proxy.$cf.table.list({
      table_name: 'product_info',
      param: { barcode: scanRes.result }
    });
    if (productRes.success && productRes.data && productRes.data.length > 0) {
      formData.value.product_info_product_info_id_1 = productRes.data[0].product_info_id;
      await proxy.$cf.toast({ message: `已识别商品：${productRes.data[0].product_name}`, level: 'success' });
    } else {
      await proxy.$cf.toast({ message: '未找到对应商品，请手动选择', level: 'warning' });
    }
  } catch (e) {
    await proxy.$cf.toast({ message: '扫码异常', level: 'error' });
  }
}

// 表单校验&提交
async function handleSubmit() {
  if (loading.value) return;
  // 校验必填项
  if (!formData.value.product_info_product_info_id_1) {
    await proxy.$cf.toast({ message: '请选择商品', level: 'error' }); return;
  }
  if (!formData.value.warehouse_info_warehouse_info_id_1) {
    await proxy.$cf.toast({ message: '请选择仓库', level: 'error' }); return;
  }
  if (!formData.value.quantity || isNaN(Number(formData.value.quantity)) || Number(formData.value.quantity) <= 0) {
    await proxy.$cf.toast({ message: '请输入正确的数量', level: 'error' }); return;
  }
  // 效期格式化
  let expireDate = formData.value.expire_date;
  if (expireDate && !/^\d{4}-\d{2}-\d{2}$/.test(expireDate)) {
    await proxy.$cf.toast({ message: '效期格式不正确', level: 'error' }); return;
  }
  loading.value = true;
  // 提交
  const addRes = await proxy.$cf.table.add({
    table_name: 'inventory_record',
    param: {
      product_info_product_info_id_1: formData.value.product_info_product_info_id_1,
      warehouse_info_warehouse_info_id_1: formData.value.warehouse_info_warehouse_info_id_1,
      quantity: Number(formData.value.quantity),
      batch_number: formData.value.batch_number || '',
      expire_date: expireDate || '',
      serial_number: formData.value.serial_number || ''
    }
  });
  loading.value = false;
  if (addRes.success) {
    await proxy.$cf.toast({ message: '录入成功', level: 'success' });
    // 重置表单
    formData.value = {
      product_info_product_info_id_1: '',
      warehouse_info_warehouse_info_id_1: '',
      quantity: '',
      batch_number: '',
      expire_date: '',
      serial_number: ''
    };
    // 返回库存列表页
    proxy.$cf.navigate.to({ url: '/pages/inventory/index', type: 'page' });
  } else {
    await proxy.$cf.toast({ message: addRes.message || '录入失败', level: 'error' });
  }
}
</script>