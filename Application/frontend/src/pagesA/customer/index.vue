<template>
  <base-layout>
    <view class="p-2">
      <!-- 顶部搜索和筛选 -->
      <view class="mb-4 flex gap-3 items-center">
        <view class="flex-1">
          <uni-easyinput
            class="box-border"
            v-model="searchKeyword"
            placeholder="搜索客户名称/手机号"
            type="text"
            clearable
            prefixIcon="search"
            @input="handleSearch"
          />
        </view>
        <button
          class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm flex items-center gap-1"
          @click="openAddCustomer"
        >
          <uni-icons type="plus" size="18" />
          新增
        </button>
        <button
          class="bg-success-500 text-white rounded-full px-4 py-2 text-sm flex items-center gap-1"
          @click="goToOcrPage"
        >
          <uni-icons type="image" size="18" />
          名片OCR
        </button>
      </view>

      <!-- 筛选分层/标签 -->
      <view class="flex gap-3 mb-4">
        <uni-data-select
          class="flex-1"
          v-model="filterRfmLevel"
          :localdata="rfmLevels"
          clear
          placeholder="RFM分层"
          @change="handleFilter"
        />
        <uni-data-select
          class="flex-1"
          v-model="filterTag"
          :localdata="customerTags"
          clear
          placeholder="客户标签"
          @change="handleFilter"
        />
      </view>

      <!-- 客户列表 -->
      <view>
        <view
          v-for="item in customerList"
          :key="item.customer_info_id"
          class="bg-white rounded-xl shadow-lg p-4 mb-4 flex items-center gap-4"
        >
          <view class="flex-1 min-w-0" @click="goToDetail(item.customer_info_id)">
            <view class="flex items-center gap-2 mb-1">
              <text class="text-lg font-bold truncate">{{ item.customer_name }}</text>
              <uni-tag
                v-if="item.level_name"
                :text="item.level_name"
                size="small"
                type="primary"
                circle
              />
              <uni-tag
                v-if="item.tag_name"
                :text="item.tag_name"
                size="small"
                type="success"
                circle
              />
            </view>
            <view class="flex items-center gap-2 text-neutral-500 text-sm mb-1">
              <uni-icons type="phone" size="16" />
              <text>{{ item.phone || '无手机号' }}</text>
              <uni-icons v-if="item.email" type="email" size="16" class="ml-2" />
              <text v-if="item.email">{{ item.email }}</text>
            </view>
            <view v-if="item.address" class="text-neutral-400 text-xs truncate">
              <uni-icons type="location" size="14" />
              <text>{{ item.address }}</text>
            </view>
          </view>
          <view class="flex flex-col gap-2 items-end">
            <button
              class="bg-secondary-500 text-white rounded-full px-3 py-1 text-xs flex items-center"
              @click.stop="openEditCustomer(item)"
            >
              <uni-icons type="compose" size="16" />
              编辑
            </button>
          </view>
        </view>
        <!-- 空状态 -->
        <view v-if="!loading && customerList.length == 0" class="text-center py-10 text-neutral-400">
          <uni-icons type="person" size="48" color="#d1d5db" />
          <view class="mt-2">暂无客户数据</view>
        </view>
        <!-- 加载更多 -->
        <view v-if="hasMore" class="text-center py-2">
          <button
            class="bg-primary-500 text-white rounded-full px-6 py-1 text-sm"
            @click="loadMore"
          >加载更多</button>
        </view>
      </view>
    </view>

    <!-- 客户编辑弹窗 -->
    <uni-popup ref="editPopup" type="center">
      <view class="bg-white p-4 w-80 rounded-xl shadow-lg">
        <view class="text-lg font-bold mb-4">{{ editForm.customer_info_id ? '编辑客户' : '新增客户' }}</view>
        <uni-forms :modelValue="editForm" label-position="top">
          <uni-forms-item name="customer_name" label="客户名称" required>
            <uni-easyinput type="text" v-model="editForm.customer_name" placeholder="请输入客户名称" />
          </uni-forms-item>
          <uni-forms-item name="phone" label="手机号">
            <uni-easyinput type="text" v-model="editForm.phone" placeholder="请输入手机号" />
          </uni-forms-item>
          <uni-forms-item name="email" label="邮箱">
            <uni-easyinput type="text" v-model="editForm.email" placeholder="请输入邮箱" />
          </uni-forms-item>
          <uni-forms-item name="address" label="地址">
            <uni-easyinput type="text" v-model="editForm.address" placeholder="请输入地址" />
          </uni-forms-item>
          <uni-forms-item name="rfm_level_enum_rfm_level_enum_id_1" label="RFM分层">
            <uni-data-select
              v-model="editForm.rfm_level_enum_rfm_level_enum_id_1"
              :localdata="rfmLevels"
              clear
              placeholder="请选择分层"
            />
          </uni-forms-item>
          <uni-forms-item name="customer_tag_enum_customer_tag_enum_id_1" label="客户标签">
            <uni-data-select
              v-model="editForm.customer_tag_enum_customer_tag_enum_id_1"
              :localdata="customerTags"
              clear
              placeholder="请选择标签"
            />
          </uni-forms-item>
        </uni-forms>
        <view class="flex gap-3 mt-6">
          <button
            class="flex-1 bg-primary-500 text-white rounded-full py-2"
            @click="submitEdit"
          >保存</button>
          <button
            class="flex-1 bg-neutral-200 text-neutral-600 rounded-full py-2"
            @click="closeEditPopup"
          >取消</button>
        </view>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const customerList = ref([]);
const pageInfo = ref({ current: 1, pageSize: 10 });
const hasMore = ref(true);
const loading = ref(false);

const searchKeyword = ref('');
const filterRfmLevel = ref('');
const filterTag = ref('');

const rfmLevels = ref([]);
const customerTags = ref([]);

const editForm = ref({
  customer_info_id: null,
  customer_name: '',
  phone: '',
  email: '',
  address: '',
  rfm_level_enum_rfm_level_enum_id_1: '',
  customer_tag_enum_customer_tag_enum_id_1: ''
});

// 获取分层/标签枚举
async function fetchEnums() {
  // RFM分层
  const rfmRes = await proxy.$cf.table.list({
    table_name: 'rfm_level_enum',
    param: {}
  });
  if (rfmRes.success) {
    rfmLevels.value = rfmRes.data.map(i => ({
      value: i.rfm_level_enum_id,
      text: i.level_name
    }));
  }
  // 客户标签
  const tagRes = await proxy.$cf.table.list({
    table_name: 'customer_tag_enum',
    param: {}
  });
  if (tagRes.success) {
    customerTags.value = tagRes.data.map(i => ({
      value: i.customer_tag_enum_id,
      text: i.tag_name
    }));
  }
}

// 查询客户列表
async function fetchCustomerList(reset = false) {
  if (loading.value) return;
  loading.value = true;
  if (reset) {
    pageInfo.value.current = 1;
    customerList.value = [];
    hasMore.value = true;
  }
  // 构造查询参数
  const param = {
    current: pageInfo.value.current,
    pageSize: pageInfo.value.pageSize
  };
  if (searchKeyword.value) {
    param.customer_name = searchKeyword.value;
  }
  if (filterRfmLevel.value) {
    param.rfm_level_enum_rfm_level_enum_id_1 = filterRfmLevel.value;
  }
  if (filterTag.value) {
    param.customer_tag_enum_customer_tag_enum_id_1 = filterTag.value;
  }
  const res = await proxy.$cf.table.page({
    table_name: 'customer_info',
    param
  });
  loading.value = false;
  if (res.success) {
    if (pageInfo.value.current == 1) {
      customerList.value = res.data.records;
    } else {
      customerList.value = customerList.value.concat(res.data.records);
    }
    hasMore.value = res.data.records.length == pageInfo.value.pageSize;
  }
}

// 搜索/筛选
function handleSearch() {
  fetchCustomerList(true);
}
function handleFilter() {
  fetchCustomerList(true);
}

// 加载更多
function loadMore() {
  if (!hasMore.value) return;
  pageInfo.value.current += 1;
  fetchCustomerList();
}

// 跳转客户详情
function goToDetail(id) {
  proxy.$cf.navigate.to({
    url: `/pages/customer_detail/index?customer_info_id=${id}`,
    type: 'page'
  });
}

// 打开新增客户弹窗
function openAddCustomer() {
  Object.assign(editForm.value, {
    customer_info_id: null,
    customer_name: '',
    phone: '',
    email: '',
    address: '',
    rfm_level_enum_rfm_level_enum_id_1: '',
    customer_tag_enum_customer_tag_enum_id_1: ''
  });
  proxy.$refs.editPopup.open();
}

// 打开编辑客户弹窗
function openEditCustomer(item) {
  Object.assign(editForm.value, {
    customer_info_id: item.customer_info_id,
    customer_name: item.customer_name,
    phone: item.phone,
    email: item.email,
    address: item.address,
    rfm_level_enum_rfm_level_enum_id_1: item.rfm_level_enum_rfm_level_enum_id_1 || '',
    customer_tag_enum_customer_tag_enum_id_1: item.customer_tag_enum_customer_tag_enum_id_1 || ''
  });
  proxy.$refs.editPopup.open();
}

// 关闭弹窗
function closeEditPopup() {
  proxy.$refs.editPopup.close();
}

// 保存客户
async function submitEdit() {
  if (!editForm.value.customer_name) {
    await proxy.$cf.toast({ message: '客户名称不能为空', level: 'error' });
    return;
  }
  let param = {
    customer_name: editForm.value.customer_name,
    phone: editForm.value.phone,
    email: editForm.value.email,
    address: editForm.value.address,
    rfm_level_enum_rfm_level_enum_id_1: editForm.value.rfm_level_enum_rfm_level_enum_id_1 || null,
    customer_tag_enum_customer_tag_enum_id_1: editForm.value.customer_tag_enum_customer_tag_enum_id_1 || null
  };
  let res;
  if (editForm.value.customer_info_id) {
    // 更新
    param.customer_info_id = editForm.value.customer_info_id;
    res = await proxy.$cf.table.update({
      table_name: 'customer_info',
      param
    });
  } else {
    // 新增
    res = await proxy.$cf.table.add({
      table_name: 'customer_info',
      param
    });
  }
  if (res.success) {
    await proxy.$cf.toast({ message: '保存成功', level: 'success' });
    closeEditPopup();
    fetchCustomerList(true);
  } else {
    await proxy.$cf.toast({ message: res.message || '保存失败', level: 'error' });
  }
}

// 跳转到名片OCR建档页面
function goToOcrPage() {
  proxy.$cf.navigate.to({
    url: '/pages/business_card_ocr/index',
    type: 'page'
  });
}

// 页面初始化
onLoad(() => {
  fetchEnums();
  fetchCustomerList(true);
});
</script>