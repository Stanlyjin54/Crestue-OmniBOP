<template>
  <base-layout>
    <view class="p-2">
      <!-- 顶部标题与新增按钮 -->
      <view class="flex items-center justify-between mb-4">
        <text class="text-xl font-bold text-primary-700">采购单管理</text>
        <button
          class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-lg flex items-center gap-1"
          @click="openAddPopup"
        >
          <uni-icons type="plus" size="20" color="#fff" />
          新增采购单
        </button>
      </view>

      <!-- 快捷入口 -->
      <view class="grid grid-cols-3 gap-4 mb-4">
        <button
          class="bg-success-500 text-white rounded-xl shadow-md flex flex-col items-center py-3"
          @click="oneClickPurchase"
        >
          <uni-icons type="cart" size="28" color="#fff" />
          <text class="mt-1 text-xs">一键采购</text>
        </button>
        <button
          class="bg-warning-500 text-white rounded-xl shadow-md flex flex-col items-center py-3"
          @click="priceCompare"
        >
          <uni-icons type="tune" size="28" color="#fff" />
          <text class="mt-1 text-xs">采购比价</text>
        </button>
        <button
          class="bg-secondary-500 text-white rounded-xl shadow-md flex flex-col items-center py-3"
          @click="openFinance"
        >
          <uni-icons type="wallet" size="28" color="#fff" />
          <text class="mt-1 text-xs">供应链金融</text>
        </button>
      </view>

      <!-- 搜索与筛选 -->
      <view class="flex gap-3 mb-4">
        <uni-easyinput
          v-model="searchForm.purchase_no"
          class="flex-1 box-border"
          type="text"
          clearable
          prefixIcon="search"
          placeholder="采购单号"
          @input="searchPurchaseList"
        />
        <uni-data-select
          v-model="searchForm.purchase_status_enum_purchase_status_enum_id_1"
          :localdata="purchaseStatusOptions"
          class="flex-1 box-border"
          clear
          placeholder="采购状态"
          @change="searchPurchaseList"
        />
      </view>

      <!-- 采购单列表 -->
      <view v-if="purchaseList.length > 0">
        <view
          v-for="item in purchaseList"
          :key="item.purchase_info_id"
          class="bg-white rounded-xl shadow-lg p-3 mb-4 flex flex-col"
        >
          <view class="flex items-center justify-between mb-1">
            <view class="flex items-center gap-2">
              <uni-tag
                :text="getPurchaseStatusName(item.purchase_status_enum_purchase_status_enum_id_1)"
                type="primary"
                size="small"
                circle
              />
              <text class="font-semibold text-base text-primary-700">
                {{ item.purchase_no }}
              </text>
            </view>
            <view class="flex gap-2">
              <uni-icons
                type="compose"
                size="22"
                color="#4b5563"
                class="cursor-pointer"
                @click="openEditPopup(item)"
              />
              <uni-icons
                type="forward"
                size="22"
                color="#0ea5e9"
                class="cursor-pointer"
                @click="goDetail(item)"
              />
            </view>
          </view>
          <view class="flex flex-wrap gap-4 mb-2">
            <view class="flex items-center gap-1">
              <uni-icons type="person" size="18" color="#6b7280" />
              <text class="text-sm text-neutral-700">{{ item.username || '—' }}</text>
            </view>
            <view class="flex items-center gap-1">
              <uni-icons type="calendar" size="18" color="#6b7280" />
              <text class="text-sm text-neutral-700">{{ item.purchase_date }}</text>
            </view>
            <view class="flex items-center gap-1">
              <uni-icons type="wallet" size="18" color="#6b7280" />
              <text class="text-sm text-neutral-700">￥{{ item.total_amount }}</text>
            </view>
            <view v-if="item.supplier" class="flex items-center gap-1">
              <uni-icons type="shop" size="18" color="#6b7280" />
              <text class="text-sm text-neutral-700">{{ item.supplier }}</text>
            </view>
          </view>
        </view>
        <!-- 加载更多 -->
        <view v-if="hasMore" class="flex justify-center items-center py-2">
          <button
            class="bg-secondary-500 text-white rounded-full px-6 py-2 text-sm"
            @click="loadMore"
          >
            加载更多
          </button>
        </view>
      </view>
      <view v-else class="flex flex-col items-center justify-center py-16">
        <image
          src="https://www.codeflying.net/preview/ai-robot.jpg"
          class="w-24 h-24 mb-4 rounded-full object-cover"
          mode="aspectFill"
        />
        <text class="text-neutral-400 text-base">暂无采购单数据</text>
      </view>
    </view>

    <!-- 新增/编辑采购单弹窗 -->
    <uni-popup ref="editPopupRef" type="center">
      <view class="bg-white p-4 w-80 rounded-xl shadow-xl">
        <text class="block text-lg font-bold text-primary-700 mb-4">
          {{ editForm.purchase_info_id ? '编辑采购单' : '新增采购单' }}
        </text>
        <uni-forms
          ref="purchaseFormRef"
          :modelValue="editForm"
          label-position="top"
          label-width="100%"
        >
          <uni-forms-item name="purchase_no" label="采购单号" required>
            <uni-easyinput
              v-model="editForm.purchase_no"
              type="text"
              clearable
              placeholder="请输入采购单号"
            />
          </uni-forms-item>
          <uni-forms-item name="supplier" label="供应商">
            <uni-easyinput
              v-model="editForm.supplier"
              type="text"
              clearable
              placeholder="请输入供应商"
            />
          </uni-forms-item>
          <uni-forms-item name="total_amount" label="采购总金额" required>
            <uni-easyinput
              v-model="editForm.total_amount"
              type="number"
              clearable
              placeholder="请输入采购总金额"
            />
          </uni-forms-item>
          <uni-forms-item name="purchase_date" label="采购时间" required>
            <uni-datetime-picker
              type="datetime"
              v-model="editForm.purchase_date"
              placeholder="请选择采购时间"
            />
          </uni-forms-item>
          <uni-forms-item name="purchase_status_enum_purchase_status_enum_id_1" label="采购状态" required>
            <uni-data-select
              v-model="editForm.purchase_status_enum_purchase_status_enum_id_1"
              :localdata="purchaseStatusOptions"
              placeholder="请选择采购状态"
            />
          </uni-forms-item>
        </uni-forms>
        <view class="flex gap-3 mt-6">
          <button
            class="flex-1 bg-primary-500 text-white rounded-full py-2 text-sm"
            @click="submitPurchaseForm"
          >
            保存
          </button>
          <button
            class="flex-1 bg-neutral-200 text-primary-500 rounded-full py-2 text-sm"
            @click="closeEditPopup"
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

const purchaseList = ref([]);
const purchaseStatusOptions = ref([]);
const pageInfo = ref({
  current: 1,
  pageSize: 10,
  total: 0,
});
const hasMore = ref(true);

const searchForm = ref({
  purchase_no: '',
  purchase_status_enum_purchase_status_enum_id_1: '',
});

const editForm = ref({
  purchase_info_id: null,
  purchase_no: '',
  supplier: '',
  total_amount: '',
  purchase_date: '',
  purchase_status_enum_purchase_status_enum_id_1: '',
});

const editPopupRef = ref(null);
const purchaseFormRef = ref(null);

// 获取采购状态枚举
async function fetchPurchaseStatusOptions() {
  const res = await proxy.$cf.table.list({
    table_name: 'purchase_status_enum',
    param: {},
  });
  if (res.success) {
    purchaseStatusOptions.value = res.data.map((item) => ({
      value: item.purchase_status_enum_id,
      text: item.status_name,
    }));
  }
}

// 获取采购单列表
async function fetchPurchaseList(reset = false) {
  if (reset) {
    pageInfo.value.current = 1;
    purchaseList.value = [];
    hasMore.value = true;
  }
  const param = {
    current: pageInfo.value.current,
    pageSize: pageInfo.value.pageSize,
  };
  if (searchForm.value.purchase_no) {
    param.purchase_no = searchForm.value.purchase_no;
  }
  if (searchForm.value.purchase_status_enum_purchase_status_enum_id_1) {
    param.purchase_status_enum_purchase_status_enum_id_1 = searchForm.value.purchase_status_enum_purchase_status_enum_id_1;
  }
  const res = await proxy.$cf.table.page({
    table_name: 'purchase_info',
    param,
  });
  if (res.success) {
    if (pageInfo.value.current == 1) {
      purchaseList.value = res.data.records;
    } else {
      purchaseList.value = purchaseList.value.concat(res.data.records);
    }
    pageInfo.value.total = res.data.total;
    hasMore.value = purchaseList.value.length < res.data.total;
  }
}

// 搜索采购单
function searchPurchaseList() {
  fetchPurchaseList(true);
}

// 加载更多
function loadMore() {
  if (!hasMore.value) return;
  pageInfo.value.current += 1;
  fetchPurchaseList();
}

// 获取采购状态名称
function getPurchaseStatusName(id) {
  const found = purchaseStatusOptions.value.find((item) => item.value == id);
  return found ? found.text : '未知';
}

// 打开新增采购单弹窗
async function openAddPopup() {
  // 清空表单
  editForm.value = {
    purchase_info_id: null,
    purchase_no: '',
    supplier: '',
    total_amount: '',
    purchase_date: '',
    purchase_status_enum_purchase_status_enum_id_1: '',
  };
  await proxy.$cf.interactWithComponent({
    refName: 'editPopupRef',
    action: 'open',
    parameters: 'center',
  });
}

// 打开编辑采购单弹窗
async function openEditPopup(item) {
  editForm.value = {
    purchase_info_id: item.purchase_info_id,
    purchase_no: item.purchase_no,
    supplier: item.supplier,
    total_amount: item.total_amount,
    purchase_date: item.purchase_date,
    purchase_status_enum_purchase_status_enum_id_1: item.purchase_status_enum_purchase_status_enum_id_1,
  };
  await proxy.$cf.interactWithComponent({
    refName: 'editPopupRef',
    action: 'open',
    parameters: 'center',
  });
}

// 关闭弹窗
function closeEditPopup() {
  proxy.$refs.editPopupRef.close();
}

// 提交表单（新增/编辑）
async function submitPurchaseForm() {
  // 表单校验
  const form = editForm.value;
  if (!form.purchase_no || !form.total_amount || !form.purchase_date || !form.purchase_status_enum_purchase_status_enum_id_1) {
    proxy.$cf.toast({ message: '请填写所有必填项', level: 'error' });
    return;
  }
  // 获取当前用户
  let userRes = await proxy.$cf.login.getLoginUser();
  if (!userRes.success) {
    proxy.$cf.toast({ message: '无法获取当前用户', level: 'error' });
    return;
  }
  const userId = userRes.data.user_info_id;

  let res;
  if (form.purchase_info_id) {
    // 编辑
    res = await proxy.$cf.table.update({
      table_name: 'purchase_info',
      param: {
        purchase_info_id: form.purchase_info_id,
        purchase_no: form.purchase_no,
        supplier: form.supplier,
        total_amount: form.total_amount,
        purchase_date: form.purchase_date,
        purchase_status_enum_purchase_status_enum_id_1: form.purchase_status_enum_purchase_status_enum_id_1,
        user_info_user_info_id_1: userId,
      },
    });
  } else {
    // 新增
    res = await proxy.$cf.table.add({
      table_name: 'purchase_info',
      param: {
        purchase_no: form.purchase_no,
        supplier: form.supplier,
        total_amount: form.total_amount,
        purchase_date: form.purchase_date,
        purchase_status_enum_purchase_status_enum_id_1: form.purchase_status_enum_purchase_status_enum_id_1,
        user_info_user_info_id_1: userId,
      },
    });
  }
  if (res.success) {
    proxy.$cf.toast({ message: '保存成功', level: 'success' });
    closeEditPopup();
    fetchPurchaseList(true);
  } else {
    proxy.$cf.toast({ message: res.message || '保存失败', level: 'error' });
  }
}

// 跳转采购详情
function goDetail(item) {
  proxy.$cf.navigate.to({
    url: `/pages/purchase_detail/index?purchase_info_id=${item.purchase_info_id}`,
    type: 'page',
  });
}

// 一键采购
function oneClickPurchase() {
  proxy.$cf.toast({ message: '一键采购功能请在采购详情中操作', level: 'success' });
}

// 采购比价
function priceCompare() {
  proxy.$cf.toast({ message: '采购比价功能请在采购详情中操作', level: 'success' });
}

// 供应链金融
function openFinance() {
  proxy.$cf.toast({ message: '供应链金融功能请在采购详情中操作', level: 'success' });
}

onLoad(() => {
  fetchPurchaseStatusOptions();
  fetchPurchaseList(true);
});
</script>