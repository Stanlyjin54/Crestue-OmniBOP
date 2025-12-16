<template>
  <base-layout>
    <view class="p-2">
      <!-- 顶部标题与新增按钮 -->
      <view class="flex items-center justify-between mb-4">
        <text class="text-xl font-bold text-primary-700">商机线索</text>
        <button class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-lg flex items-center gap-1"
                @click="openEditPopup()">
          <uni-icons type="plus" size="20" />
          新增
        </button>
      </view>

      <!-- 客户筛选（如果页面参数存在customer_info_id则不显示） -->
      <view v-if="!customerInfoId" class="mb-4">
        <uni-easyinput
          class="box-border"
          v-model="searchKeyword"
          type="text"
          clearable
          prefixIcon="search"
          placeholder="搜索商机名称/客户名称"
          @input="onSearchInput"
        />
      </view>

      <!-- 商机线索列表 -->
      <view>
        <view v-if="loading" class="flex items-center justify-center py-10">
          <uni-icons type="spinner-cycle" size="36" color="#4b5563" class="animate-spin" />
        </view>
        <view v-else>
          <view v-if="opportunityList.length == 0" class="text-center text-neutral-400 py-12">
            <uni-icons type="info" size="32" color="#9ca3af" class="mb-2" />
            <text>暂无商机线索</text>
          </view>
          <view class="columns-1 sm:columns-2 gap-4">
            <view v-for="item in opportunityList" :key="item.business_opportunity_id"
                  class="bg-white rounded-xl shadow-lg p-4 mb-4 break-inside-avoid flex flex-col gap-2">
              <!-- 商机名称与操作 -->
              <view class="flex items-center justify-between mb-1">
                <view class="flex items-center gap-2">
                  <uni-icons type="compose" size="22" color="#0ea5e9" />
                  <text class="font-semibold text-lg text-primary-700">{{ item.opportunity_name }}</text>
                </view>
                <button class="bg-secondary-500 text-white rounded-full px-3 py-1 text-xs flex items-center gap-1"
                        @click="openEditPopup(item)">
                  <uni-icons type="compose" size="16" />
                  编辑
                </button>
              </view>
              <!-- 客户信息 -->
              <view class="flex items-center gap-2 mb-1">
                <uni-icons type="person" size="18" color="#4b5563" />
                <text class="text-neutral-600 text-sm">{{ item.customer_name || '未关联客户' }}</text>
              </view>
              <!-- 商机状态 -->
              <view class="flex items-center gap-2 mb-1">
                <uni-tag :text="item.status" type="primary" size="small" circle />
              </view>
              <!-- AI跟进建议 -->
              <view v-if="item.follow_up_suggestion" class="bg-secondary-50 rounded-lg p-2 mt-1">
                <view class="flex items-center gap-1 mb-1">
                  <uni-icons type="chatbubble" size="16" color="#0ea5e9" />
                  <text class="text-xs text-secondary-700">AI建议</text>
                </view>
                <text class="text-sm text-neutral-700 leading-relaxed">{{ item.follow_up_suggestion }}</text>
              </view>
            </view>
          </view>
          <!-- 加载更多 -->
          <view v-if="hasMore" class="flex justify-center my-4">
            <button class="bg-primary-100 text-primary-600 rounded-full px-6 py-2 text-sm"
                    @click="loadMore">
              加载更多
            </button>
          </view>
        </view>
      </view>
    </view>

    <!-- 商机编辑/新增弹窗 -->
    <uni-popup ref="editPopup" type="center">
      <view class="bg-white p-4 w-80 rounded-xl shadow-xl">
        <text class="block text-lg font-bold text-primary-700 mb-4">{{ editForm.business_opportunity_id ? '编辑商机' : '新增商机' }}</text>
        <uni-forms ref="editFormRef" :modelValue="editForm" label-position="top" label-width="100%">
          <!-- 客户选择（如果页面参数有customer_info_id则隐藏） -->
          <uni-forms-item v-if="!customerInfoId" name="customer_info_customer_info_id_1" label="关联客户" required>
            <uni-data-select
              v-model="editForm.customer_info_customer_info_id_1"
              :localdata="customerSelectList"
              placeholder="请选择客户"
              clear
            />
          </uni-forms-item>
          <!-- 商机名称 -->
          <uni-forms-item name="opportunity_name" label="商机名称" required>
            <uni-easyinput type="text" v-model="editForm.opportunity_name" placeholder="请输入商机名称" clearable />
          </uni-forms-item>
          <!-- 商机状态 -->
          <uni-forms-item name="status" label="商机状态" required>
            <uni-easyinput type="text" v-model="editForm.status" placeholder="请输入商机状态，如：跟进中/已成交/已关闭" clearable />
          </uni-forms-item>
        </uni-forms>
        <view class="flex gap-4 mt-4">
          <button class="flex-1 bg-primary-500 text-white rounded-full py-2 text-sm" @click="submitEdit">
            保存
          </button>
          <button class="flex-1 bg-neutral-200 text-neutral-700 rounded-full py-2 text-sm" @click="closeEditPopup">
            取消
          </button>
        </view>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const opportunityList = ref([]);
const loading = ref(false);
const hasMore = ref(true);
const pageInfo = ref({ current: 1, pageSize: 10 });
const total = ref(0);

const searchKeyword = ref('');
const customerInfoId = ref(null);

const editPopup = ref();
const editFormRef = ref();
const editForm = ref({
  business_opportunity_id: null,
  customer_info_customer_info_id_1: null,
  opportunity_name: '',
  status: ''
});

const customerSelectList = ref([]); // 客户下拉

// 页面初始化
onLoad(async (option) => {
  if (option && option.customer_info_id) {
    customerInfoId.value = Number(option.customer_info_id);
  }
  await fetchCustomerSelectList();
  await fetchOpportunityList(true);
});

// 搜索
function onSearchInput() {
  pageInfo.value.current = 1;
  fetchOpportunityList(true);
}

// 获取客户下拉（仅用于新增/编辑时选择客户）
async function fetchCustomerSelectList() {
  const res = await proxy.$cf.table.list({
    table_name: 'customer_info',
    param: {}
  });
  if (res.success) {
    customerSelectList.value = res.data.map(item => ({
      value: item.customer_info_id,
      text: item.customer_name
    }));
  }
}

// 获取商机线索列表
async function fetchOpportunityList(reset = false) {
  loading.value = true;
  if (reset) {
    opportunityList.value = [];
    pageInfo.value.current = 1;
    hasMore.value = true;
  }
  const param = {
    current: pageInfo.value.current,
    pageSize: pageInfo.value.pageSize
  };
  if (customerInfoId.value) {
    param.customer_info_customer_info_id_1 = customerInfoId.value;
  }
  if (searchKeyword.value && searchKeyword.value.trim() !== '') {
    param.opportunity_name = searchKeyword.value.trim();
  }
  const res = await proxy.$cf.table.page({
    table_name: 'business_opportunity',
    param
  });
  loading.value = false;
  if (res.success) {
    const records = res.data.records || [];
    if (reset) {
      opportunityList.value = records;
    } else {
      opportunityList.value = opportunityList.value.concat(records);
    }
    total.value = res.data.total;
    hasMore.value = opportunityList.value.length < total.value;
  } else {
    proxy.$cf.toast({ message: res.message || '加载失败', level: 'error' });
  }
}

// 加载更多
function loadMore() {
  if (!hasMore.value) return;
  pageInfo.value.current += 1;
  fetchOpportunityList();
}

// 打开编辑/新增弹窗
function openEditPopup(item = null) {
  if (item) {
    editForm.value = {
      business_opportunity_id: item.business_opportunity_id,
      customer_info_customer_info_id_1: item.customer_info_customer_info_id_1 || customerInfoId.value || null,
      opportunity_name: item.opportunity_name,
      status: item.status
    };
  } else {
    editForm.value = {
      business_opportunity_id: null,
      customer_info_customer_info_id_1: customerInfoId.value || null,
      opportunity_name: '',
      status: ''
    };
  }
  proxy.$refs.editPopup.open();
}

// 关闭弹窗
function closeEditPopup() {
  proxy.$refs.editPopup.close();
}

// 保存编辑/新增
async function submitEdit() {
  // 校验必填
  if (!editForm.value.opportunity_name || !editForm.value.status ||
      (!editForm.value.customer_info_customer_info_id_1 && !customerInfoId.value)) {
    proxy.$cf.toast({ message: '请填写完整信息', level: 'warning' });
    return;
  }
  let param = {
    customer_info_customer_info_id_1: editForm.value.customer_info_customer_info_id_1 || customerInfoId.value,
    opportunity_name: editForm.value.opportunity_name,
    status: editForm.value.status
  };
  let res;
  if (editForm.value.business_opportunity_id) {
    // 编辑
    param.business_opportunity_id = editForm.value.business_opportunity_id;
    res = await proxy.$cf.table.update({
      table_name: 'business_opportunity',
      param
    });
  } else {
    // 新增
    res = await proxy.$cf.table.add({
      table_name: 'business_opportunity',
      param
    });
  }
  if (res.success) {
    proxy.$cf.toast({ message: '保存成功', level: 'success' });
    closeEditPopup();
    fetchOpportunityList(true);
  } else {
    proxy.$cf.toast({ message: res.message || '保存失败', level: 'error' });
  }
}
</script>