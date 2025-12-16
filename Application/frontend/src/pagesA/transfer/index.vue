<template>
  <base-layout>
    <view class="p-2">
      <!-- 标题与发起调拨按钮 -->
      <view class="flex items-center justify-between mb-4">
        <text class="text-xl font-bold text-primary-700">调拨管理</text>
        <button class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-lg flex items-center gap-1"
                @click="openEditPopup()">
          <uni-icons type="plus" size="20" color="#fff" />
          发起调拨
        </button>
      </view>

      <!-- 筛选区 -->
      <view class="bg-white rounded-lg shadow-md p-2 mb-4">
        <view class="flex flex-wrap gap-3">
          <view class="flex-1 min-w-[120px]">
            <uni-data-select v-model="filters.fromWarehouse" :localdata="warehouseSelectList"
              placeholder="调出仓库" clear />
          </view>
          <view class="flex-1 min-w-[120px]">
            <uni-data-select v-model="filters.toWarehouse" :localdata="warehouseSelectList"
              placeholder="调入仓库" clear />
          </view>
          <view class="flex-1 min-w-[120px]">
            <uni-data-select v-model="filters.status" :localdata="statusSelectList"
              placeholder="调拨状态" clear />
          </view>
          <view class="flex-1 min-w-[120px]">
            <uni-easyinput type="text" v-model="filters.productName" placeholder="商品名称" clearable />
          </view>
          <button class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm"
                  @click="fetchList(true)">
            <uni-icons type="search" size="18" color="#fff" />
            搜索
          </button>
        </view>
      </view>

      <!-- 调拨列表 -->
      <view>
        <view v-if="list.length == 0" class="text-center text-neutral-400 py-10">
          <uni-icons type="compose" size="48" color="#d1d5db" />
          <view class="mt-2">暂无调拨记录</view>
        </view>
        <view v-else class="columns-1 md:columns-2 gap-4">
          <view v-for="item in list" :key="item.transfer_info_id"
            class="bg-white rounded-xl shadow-lg p-3 mb-4 break-inside-avoid flex flex-col gap-2">
            <!-- 卡片头部 -->
            <view class="flex items-center justify-between">
              <view class="flex items-center gap-2">
                <uni-tag :text="item.status_name"
                         :type="statusTagType(item.transfer_status_enum_transfer_status_enum_id_1)"
                         size="normal" circle />
                <text class="font-bold text-primary-700 text-base">{{ item.product_name }}</text>
              </view>
              <button class="text-secondary-500 px-2 py-1 rounded-full text-xs"
                      @click="openEditPopup(item)">
                <uni-icons type="compose" size="18" color="#0ea5e9" />
                编辑
              </button>
            </view>
            <!-- 商品与仓库信息 -->
            <view class="flex flex-wrap gap-2 text-sm text-neutral-600">
              <view class="flex items-center gap-1">
                <uni-icons type="shop" size="16" color="#4b5563" />
                <text>调出: {{ item.from_warehouse_name }}</text>
              </view>
              <view class="flex items-center gap-1">
                <uni-icons type="shop" size="16" color="#4b5563" />
                <text>调入: {{ item.to_warehouse_name }}</text>
              </view>
              <view class="flex items-center gap-1">
                <uni-icons type="cart" size="16" color="#4b5563" />
                <text>数量: {{ item.quantity }}</text>
              </view>
            </view>
            <!-- 时间与申请人 -->
            <view class="flex flex-wrap gap-2 text-xs text-neutral-400">
              <view>
                <uni-icons type="calendar" size="14" color="#9ca3af" />
                申请: {{ item.apply_date }}
              </view>
              <view v-if="item.confirm_date">
                <uni-icons type="calendar" size="14" color="#9ca3af" />
                确认: {{ item.confirm_date }}
              </view>
              <view>
                <uni-icons type="person" size="14" color="#9ca3af" />
                申请人: {{ item.username || '-' }}
              </view>
              <view v-if="item.confirm_user_info_user_info_id_2">
                <uni-icons type="person" size="14" color="#9ca3af" />
                确认人: {{ item.confirm_username || '-' }}
              </view>
            </view>
          </view>
        </view>
        <!-- 加载更多 -->
        <view v-if="hasMore" class="text-center my-4">
          <button class="bg-secondary-500 text-white rounded-full px-6 py-2 text-sm shadow"
                  @click="fetchList(false)">
            加载更多
          </button>
        </view>
      </view>
    </view>

    <!-- 编辑/新增弹窗 -->
    <uni-popup ref="editPopup" type="center">
      <view class="bg-white p-4 w-80 rounded-xl shadow-xl">
        <view class="flex items-center justify-between mb-2">
          <text class="font-bold text-lg text-primary-700">
            {{ editForm.transfer_info_id ? '编辑调拨' : '发起调拨' }}
          </text>
          <uni-icons type="close" size="22" color="#6b7280" @click="closeEditPopup" />
        </view>
        <uni-forms ref="editFormRef" :modelValue="editForm" label-position="top">
          <uni-forms-item name="product_info_product_info_id_1" label="商品" required>
            <uni-data-select v-model="editForm.product_info_product_info_id_1"
              :localdata="productSelectList" placeholder="请选择商品" />
          </uni-forms-item>
          <uni-forms-item name="from_warehouse_info_warehouse_info_id_1" label="调出仓库" required>
            <uni-data-select v-model="editForm.from_warehouse_info_warehouse_info_id_1"
              :localdata="warehouseSelectList" placeholder="请选择调出仓库" />
          </uni-forms-item>
          <uni-forms-item name="to_warehouse_info_warehouse_info_id_2" label="调入仓库" required>
            <uni-data-select v-model="editForm.to_warehouse_info_warehouse_info_id_2"
              :localdata="warehouseSelectList" placeholder="请选择调入仓库" />
          </uni-forms-item>
          <uni-forms-item name="quantity" label="调拨数量" required>
            <uni-easyinput type="number" v-model="editForm.quantity" placeholder="请输入调拨数量" />
          </uni-forms-item>
          <uni-forms-item name="transfer_status_enum_transfer_status_enum_id_1" label="调拨状态" required>
            <uni-data-select v-model="editForm.transfer_status_enum_transfer_status_enum_id_1"
              :localdata="statusSelectList" placeholder="请选择状态" />
          </uni-forms-item>
        </uni-forms>
        <view class="flex gap-3 mt-4">
          <button class="flex-1 bg-primary-500 text-white rounded-full px-4 py-2 text-sm"
                  @click="submitEdit">
            保存
          </button>
          <button class="flex-1 bg-neutral-200 text-primary-700 rounded-full px-4 py-2 text-sm"
                  @click="closeEditPopup">
            取消
          </button>
        </view>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

// 列表数据与分页
const list = ref([]);
const pageInfo = ref({ current: 1, pageSize: 10 });
const hasMore = ref(true);

// 筛选
const filters = ref({
  fromWarehouse: '',
  toWarehouse: '',
  status: '',
  productName: ''
});

// 下拉选项
const warehouseSelectList = ref([]);
const productSelectList = ref([]);
const statusSelectList = ref([]);

// 编辑弹窗
const editPopup = ref();
const editFormRef = ref();
const editForm = ref({
  transfer_info_id: null,
  product_info_product_info_id_1: '',
  from_warehouse_info_warehouse_info_id_1: '',
  to_warehouse_info_warehouse_info_id_2: '',
  quantity: '',
  transfer_status_enum_transfer_status_enum_id_1: '',
});

// 当前用户
const currentUserId = ref(null);

// 状态tag颜色
function statusTagType(statusId) {
  if (statusId == 1) return 'warning';
  if (statusId == 2) return 'success';
  if (statusId == 3) return 'primary';
  return 'default';
}

// 拉取下拉数据
async function fetchSelectOptions() {
  // 仓库
  const warehouseRes = await proxy.$cf.table.list({
    table_name: 'warehouse_info',
    param: {}
  });
  warehouseSelectList.value = (warehouseRes.success && warehouseRes.data)
    ? warehouseRes.data.map(w => ({ value: w.warehouse_info_id, text: w.warehouse_name }))
    : [];
  // 商品
  const productRes = await proxy.$cf.table.list({
    table_name: 'product_info',
    param: {}
  });
  productSelectList.value = (productRes.success && productRes.data)
    ? productRes.data.map(p => ({ value: p.product_info_id, text: p.product_name }))
    : [];
  // 状态
  const statusRes = await proxy.$cf.table.list({
    table_name: 'transfer_status_enum',
    param: {}
  });
  statusSelectList.value = (statusRes.success && statusRes.data)
    ? statusRes.data.map(s => ({ value: s.transfer_status_enum_id, text: s.status_name }))
    : [];
}

// 拉取调拨列表
async function fetchList(reset = false) {
  if (reset) {
    pageInfo.value.current = 1;
    list.value = [];
    hasMore.value = true;
  }
  const param = {
    current: pageInfo.value.current,
    pageSize: pageInfo.value.pageSize
  };
  if (filters.value.fromWarehouse) param.from_warehouse_info_warehouse_info_id_1 = filters.value.fromWarehouse;
  if (filters.value.toWarehouse) param.to_warehouse_info_warehouse_info_id_2 = filters.value.toWarehouse;
  if (filters.value.status) param.transfer_status_enum_transfer_status_enum_id_1 = filters.value.status;
  if (filters.value.productName) param.product_name = filters.value.productName;

  const res = await proxy.$cf.table.page({
    table_name: 'transfer_info',
    param
  });
  if (res.success && res.data && Array.isArray(res.data.records)) {
    // 追加数据
    const records = res.data.records;
    if (reset) list.value = [];
    // 平铺引用字段
    list.value = list.value.concat(records.map(item => ({
      ...item
    })));
    // 判断是否还有更多
    hasMore.value = res.data.records.length == pageInfo.value.pageSize;
    if (hasMore.value) pageInfo.value.current += 1;
  } else {
    if (reset) list.value = [];
    hasMore.value = false;
  }
}

// 打开编辑弹窗
function openEditPopup(item = null) {
  if (item) {
    editForm.value = {
      transfer_info_id: item.transfer_info_id,
      product_info_product_info_id_1: item.product_info_product_info_id_1,
      from_warehouse_info_warehouse_info_id_1: item.from_warehouse_info_warehouse_info_id_1,
      to_warehouse_info_warehouse_info_id_2: item.to_warehouse_info_warehouse_info_id_2,
      quantity: item.quantity,
      transfer_status_enum_transfer_status_enum_id_1: item.transfer_status_enum_transfer_status_enum_id_1,
    };
  } else {
    editForm.value = {
      transfer_info_id: null,
      product_info_product_info_id_1: '',
      from_warehouse_info_warehouse_info_id_1: '',
      to_warehouse_info_warehouse_info_id_2: '',
      quantity: '',
      transfer_status_enum_transfer_status_enum_id_1: '',
    };
  }
  proxy.$refs.editPopup.open();
}

// 关闭弹窗
function closeEditPopup() {
  proxy.$refs.editPopup.close();
}

// 保存调拨
async function submitEdit() {
  // 校验
  if (!editForm.value.product_info_product_info_id_1 ||
      !editForm.value.from_warehouse_info_warehouse_info_id_1 ||
      !editForm.value.to_warehouse_info_warehouse_info_id_2 ||
      !editForm.value.quantity ||
      !editForm.value.transfer_status_enum_transfer_status_enum_id_1) {
    proxy.$cf.toast({ message: '请填写完整信息', level: 'error' });
    return;
  }
  if (editForm.value.from_warehouse_info_warehouse_info_id_1 == editForm.value.to_warehouse_info_warehouse_info_id_2) {
    proxy.$cf.toast({ message: '调出仓库与调入仓库不能相同', level: 'error' });
    return;
  }
  let now = new Date();
  let pad = n => n.toString().padStart(2, '0');
  let nowStr = `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())} ${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`;
  // 获取当前用户
  let userRes = await proxy.$cf.login.getLoginUser();
  if (!userRes.success || !userRes.data) {
    proxy.$cf.toast({ message: '无法获取当前用户信息', level: 'error' });
    return;
  }
  // 新增或编辑
  let param = {
    product_info_product_info_id_1: editForm.value.product_info_product_info_id_1,
    from_warehouse_info_warehouse_info_id_1: editForm.value.from_warehouse_info_warehouse_info_id_1,
    to_warehouse_info_warehouse_info_id_2: editForm.value.to_warehouse_info_warehouse_info_id_2,
    quantity: Number(editForm.value.quantity),
    transfer_status_enum_transfer_status_enum_id_1: editForm.value.transfer_status_enum_transfer_status_enum_id_1,
  };
  if (editForm.value.transfer_info_id) {
    // 编辑
    param.transfer_info_id = editForm.value.transfer_info_id;
    // 不覆盖申请人和申请时间
    const res = await proxy.$cf.table.update({
      table_name: 'transfer_info',
      param
    });
    if (res.success) {
      proxy.$cf.toast({ message: '保存成功', level: 'success' });
      closeEditPopup();
      fetchList(true);
    } else {
      proxy.$cf.toast({ message: res.message || '保存失败', level: 'error' });
    }
  } else {
    // 新增
    param.apply_user_info_user_info_id_1 = userRes.data.user_info_id;
    param.apply_date = nowStr;
    const res = await proxy.$cf.table.add({
      table_name: 'transfer_info',
      param
    });
    if (res.success) {
      proxy.$cf.toast({ message: '发起调拨成功', level: 'success' });
      closeEditPopup();
      fetchList(true);
    } else {
      proxy.$cf.toast({ message: res.message || '发起失败', level: 'error' });
    }
  }
}

// 页面初始化
onLoad(async () => {
  proxy.$refs = proxy.$refs || {};
  await fetchSelectOptions();
  await fetchList(true);
});
</script>