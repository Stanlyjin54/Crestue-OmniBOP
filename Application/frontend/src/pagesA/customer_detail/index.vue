<template>
  <base-layout>
    <view class="p-2">
      <!-- 客户基础信息卡片 -->
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4 flex gap-4 items-center">
        <image
          v-if="customer.avatar && customer.avatar.length"
          :src="get_resource_url(customer.avatar[0].url)"
          class="w-16 h-16 rounded-full object-cover border border-neutral-200"
          mode="aspectFill"
        />
        <view class="flex-1">
          <view class="flex items-center gap-2 mb-2">
            <text class="text-xl font-bold text-primary-700">{{ customer.customer_name }}</text>
            <uni-tag
              v-if="rfmLevel"
              :text="rfmLevel.level_name"
              type="primary"
              size="normal"
              circle="true"
              class="ml-1"
            />
            <button
              class="ml-auto bg-primary-500 text-white rounded-full px-4 py-1 text-sm"
              @click="openEditPopup"
            >
              <uni-icons type="compose" size="18" class="mr-1" />编辑
            </button>
          </view>
          <view class="flex items-center gap-4 mb-1">
            <uni-icons type="phone" size="18" class="text-primary-500" />
            <text class="text-base text-neutral-700">{{ customer.phone || '未填写' }}</text>
          </view>
          <view class="flex items-center gap-4">
            <uni-icons type="email" size="18" class="text-primary-500" />
            <text class="text-base text-neutral-700">{{ customer.email || '未填写' }}</text>
          </view>
        </view>
      </view>

      <!-- 标签与活跃度 -->
      <view class="bg-white rounded-xl shadow-md p-4 mb-4 flex flex-col gap-2">
        <view class="flex items-center gap-2 mb-2">
          <uni-icons type="headphones" size="20" class="text-success-500" />
          <text class="font-semibold text-base text-primary-700">标签</text>
        </view>
        <view class="flex flex-wrap gap-2 mb-2">
          <uni-tag
            v-for="tag in tags"
            :key="tag.customer_tag_enum_id"
            :text="tag.tag_name"
            :type="tag.is_ai_generated ? 'royal' : 'success'"
            size="normal"
            circle="true"
          />
          <text v-if="!tags.length" class="text-neutral-400">无</text>
        </view>
        <view class="flex items-center gap-2">
          <uni-icons type="fire" size="20" class="text-warning-500" />
          <text class="font-semibold text-base text-primary-700">活跃度</text>
          <uni-badge
            v-if="rfmLevel"
            :text="rfmLevel.level_name"
            type="warning"
            size="normal"
            class="ml-2"
          />
        </view>
      </view>

      <!-- 客户详细信息 -->
      <view class="bg-white rounded-xl shadow-md p-4 mb-4">
        <view class="flex items-center gap-2 mb-2">
          <uni-icons type="info" size="20" class="text-primary-500" />
          <text class="font-semibold text-base text-primary-700">详细信息</text>
        </view>
        <view class="flex flex-col gap-2 text-neutral-700 text-sm">
          <view>
            <text class="font-semibold">地址：</text>
            <text>{{ customer.address || '未填写' }}</text>
          </view>
          <view>
            <text class="font-semibold">微信号：</text>
            <text>{{ customer.wechat_id || '未填写' }}</text>
          </view>
          <view>
            <text class="font-semibold">客户来源：</text>
            <text>{{ customer.source || '未填写' }}</text>
          </view>
        </view>
      </view>

      <!-- 商机线索 -->
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <view class="flex items-center gap-2 mb-3">
          <uni-icons type="gift" size="20" class="text-primary-500" />
          <text class="font-semibold text-base text-primary-700">商机线索</text>
          <button
            class="ml-auto bg-primary-500 text-white rounded-full px-3 py-1 text-xs"
            @click="gotoBusinessOpportunity"
          >
            <uni-icons type="forward" size="16" class="mr-1" />全部商机
          </button>
        </view>
        <view v-if="opportunityList.length" class="flex flex-col gap-3">
          <view
            v-for="item in opportunityList"
            :key="item.business_opportunity_id"
            class="bg-neutral-50 rounded-lg shadow p-3 flex flex-col gap-1"
          >
            <view class="flex items-center gap-2">
              <text class="font-semibold text-primary-700">{{ item.opportunity_name }}</text>
              <uni-tag :text="item.status" size="small" type="primary" circle="true" />
            </view>
            <text class="text-neutral-500 text-xs">AI建议：{{ item.follow_up_suggestion || '无' }}</text>
          </view>
        </view>
        <view v-else class="text-neutral-400 text-sm">暂无商机线索</view>
      </view>
    </view>

    <!-- 编辑客户弹窗 -->
    <uni-popup ref="editPopup" type="center">
      <view class="bg-white p-4 w-80 rounded-xl">
        <uni-forms :modelValue="editForm" label-position="top">
          <uni-forms-item name="customer_name" label="客户名称" required>
            <uni-easyinput v-model="editForm.customer_name" type="text" placeholder="请输入客户名称" />
          </uni-forms-item>
          <uni-forms-item name="phone" label="手机号">
            <uni-easyinput v-model="editForm.phone" type="text" placeholder="请输入手机号" />
          </uni-forms-item>
          <uni-forms-item name="email" label="邮箱">
            <uni-easyinput v-model="editForm.email" type="text" placeholder="请输入邮箱" />
          </uni-forms-item>
          <uni-forms-item name="address" label="地址">
            <uni-easyinput v-model="editForm.address" type="text" placeholder="请输入地址" />
          </uni-forms-item>
          <uni-forms-item name="rfm_level_enum_rfm_level_enum_id_1" label="RFM分层">
            <uni-data-select
              v-model="editForm.rfm_level_enum_rfm_level_enum_id_1"
              :localdata="rfmLevelOptions"
              placeholder="请选择RFM分层"
              clear
            />
          </uni-forms-item>
          <uni-forms-item name="customer_tag_enum_customer_tag_enum_id_1" label="客户标签">
            <uni-data-select
              v-model="editForm.customer_tag_enum_customer_tag_enum_id_1"
              :localdata="customerTagOptions"
              placeholder="请选择客户标签"
              clear
            />
          </uni-forms-item>
          <uni-forms-item name="wechat_id" label="微信号">
            <uni-easyinput v-model="editForm.wechat_id" type="text" placeholder="请输入微信号" />
          </uni-forms-item>
          <uni-forms-item name="source" label="客户来源">
            <uni-easyinput v-model="editForm.source" type="text" placeholder="请输入客户来源" />
          </uni-forms-item>
        </uni-forms>
        <view class="flex gap-4 mt-4">
          <button class="flex-1 bg-primary-500 text-white rounded-full py-2" @click="submitEdit">保存</button>
          <button class="flex-1 bg-neutral-200 text-primary-700 rounded-full py-2" @click="closeEditPopup">取消</button>
        </view>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const customer = ref({});
const rfmLevel = ref(null);
const tags = ref([]);
const opportunityList = ref([]);

const rfmLevelOptions = ref([]);
const customerTagOptions = ref([]);

const editForm = ref({});
const editPopup = ref(null);

function get_resource_url(url) {
  return proxy.get_resource_url(url);
}

// 获取客户详情
async function fetchCustomerDetail(id) {
  const res = await proxy.$cf.table.get({
    table_name: 'customer_info',
    param: { customer_info_id: id }
  });
  if (res.success && res.data) {
    customer.value = res.data;

    // RFM分层
    if (res.data.rfm_level_enum_rfm_level_enum_id_1) {
      rfmLevel.value = {
        rfm_level_enum_id: res.data.rfm_level_enum_rfm_level_enum_id_1,
        level_name: res.data.level_name
      };
    } else {
      rfmLevel.value = null;
    }

    // 标签
    if (res.data.customer_tag_enum_customer_tag_enum_id_1) {
      // 查询标签枚举表
      const tagRes = await proxy.$cf.table.get({
        table_name: 'customer_tag_enum',
        param: { customer_tag_enum_id: res.data.customer_tag_enum_customer_tag_enum_id_1 }
      });
      tags.value = tagRes.success && tagRes.data ? [tagRes.data] : [];
    } else {
      tags.value = [];
    }
  }
}

// 获取RFM分层选项
async function fetchRfmLevelOptions() {
  const res = await proxy.$cf.table.list({
    table_name: 'rfm_level_enum',
    param: {}
  });
  if (res.success && res.data) {
    rfmLevelOptions.value = res.data.map(item => ({
      value: item.rfm_level_enum_id,
      text: item.level_name
    }));
  }
}

// 获取客户标签选项
async function fetchCustomerTagOptions() {
  const res = await proxy.$cf.table.list({
    table_name: 'customer_tag_enum',
    param: {}
  });
  if (res.success && res.data) {
    customerTagOptions.value = res.data.map(item => ({
      value: item.customer_tag_enum_id,
      text: item.tag_name
    }));
  }
}

// 获取商机线索
async function fetchOpportunities(customerId) {
  const res = await proxy.$cf.table.list({
    table_name: 'business_opportunity',
    param: { customer_info_customer_info_id_1: customerId }
  });
  if (res.success && res.data) {
    opportunityList.value = res.data;
  }
}

// 编辑弹窗
function openEditPopup() {
  editForm.value = {
    customer_info_id: customer.value.customer_info_id,
    customer_name: customer.value.customer_name || '',
    phone: customer.value.phone || '',
    email: customer.value.email || '',
    address: customer.value.address || '',
    rfm_level_enum_rfm_level_enum_id_1: customer.value.rfm_level_enum_rfm_level_enum_id_1 || '',
    customer_tag_enum_customer_tag_enum_id_1: customer.value.customer_tag_enum_customer_tag_enum_id_1 || '',
    wechat_id: customer.value.wechat_id || '',
    source: customer.value.source || ''
  };
  proxy.$refs.editPopup.open();
}

function closeEditPopup() {
  proxy.$refs.editPopup.close();
}

// 保存编辑
async function submitEdit() {
  if (!editForm.value.customer_name) {
    await proxy.$cf.toast({ message: '客户名称不能为空', level: 'error' });
    return;
  }
  const res = await proxy.$cf.table.update({
    table_name: 'customer_info',
    param: { ...editForm.value }
  });
  if (res.success) {
    await proxy.$cf.toast({ message: '保存成功', level: 'success' });
    closeEditPopup();
    fetchCustomerDetail(editForm.value.customer_info_id);
  } else {
    await proxy.$cf.toast({ message: res.message || '保存失败', level: 'error' });
  }
}

// 跳转商机线索页面
function gotoBusinessOpportunity() {
  proxy.$cf.navigate.to({
    url: `/pages/business_opportunity/index?customer_info_id=${customer.value.customer_info_id}`,
    type: 'page'
  });
}

onLoad(async (options) => {
  const customerId = options.customer_info_id;
  await fetchCustomerDetail(customerId);
  await fetchRfmLevelOptions();
  await fetchCustomerTagOptions();
  await fetchOpportunities(customerId);
});
</script>