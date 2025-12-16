<template>
  <base-layout>
    <view class="p-2">
      <!-- 标题区 -->
      <view class="flex items-center justify-between mb-4">
        <view class="flex items-center gap-2">
          <uni-icons type="shop" size="28" color="#0ea5e9" />
          <text class="text-xl font-bold text-primary-700">采购单详情</text>
        </view>
        <button
          class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-md"
          @click="openEdit"
        >
          <uni-icons type="compose" size="20" color="#fff" class="mr-1" />
          编辑
        </button>
      </view>

      <!-- 采购单基础信息卡片 -->
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <view class="flex items-center gap-3 mb-2">
          <uni-tag :text="detail.purchase_no || '--'" type="primary" size="normal" circle />
          <uni-tag
            :text="detail.status_name || '--'"
            :type="statusTagType(detail.status_name)"
            size="normal"
            circle
          />
        </view>
        <view class="flex flex-col gap-2">
          <view class="flex justify-between">
            <text class="text-neutral-500">采购员</text>
            <text class="font-semibold text-neutral-800">{{ detail.username || '--' }}</text>
          </view>
          <view class="flex justify-between">
            <text class="text-neutral-500">采购总金额</text>
            <text class="font-semibold text-success-600">￥{{ formatAmount(detail.total_amount) }}</text>
          </view>
          <view class="flex justify-between">
            <text class="text-neutral-500">供应商</text>
            <text class="font-semibold text-neutral-800">{{ detail.supplier || '--' }}</text>
          </view>
          <view class="flex justify-between">
            <text class="text-neutral-500">采购时间</text>
            <text class="font-semibold text-neutral-800">{{ formatDate(detail.purchase_date) }}</text>
          </view>
        </view>
      </view>

      <!-- 状态流程卡片 -->
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4 flex flex-col gap-2">
        <view class="flex items-center gap-2 mb-2">
          <uni-icons type="tune" size="22" color="#0ea5e9" />
          <text class="font-semibold text-primary-700">采购状态</text>
        </view>
        <view class="flex gap-3">
          <uni-tag
            v-for="status in statusEnumList"
            :key="status.purchase_status_enum_id"
            :text="status.status_name"
            :type="statusTagType(status.status_name)"
            size="small"
            circle
            :custom-style="status.status_name == detail.status_name ? 'opacity:1' : 'opacity:0.5'"
          />
        </view>
      </view>

      <!-- 采购单视觉配图 -->
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4 flex items-center justify-center">
        <image
          src="https://www.codeflying.net/preview/business-meeting.jpg"
          mode="aspectFit"
          class="w-full h-32 rounded-lg object-cover"
        />
      </view>
    </view>

    <!-- 编辑弹窗 -->
    <uni-popup ref="editPopup" type="center">
      <view class="bg-white p-4 w-80 rounded-xl">
        <view class="flex items-center gap-2 mb-4">
          <uni-icons type="compose" size="22" color="#0ea5e9" />
          <text class="text-lg font-bold text-primary-700">编辑采购单</text>
        </view>
        <uni-forms :modelValue="editForm" label-position="top">
          <uni-forms-item name="purchase_no" label="采购单号" required>
            <uni-easyinput
              type="text"
              v-model="editForm.purchase_no"
              placeholder="请输入采购单号"
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item name="supplier" label="供应商">
            <uni-easyinput
              type="text"
              v-model="editForm.supplier"
              placeholder="请输入供应商"
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item name="total_amount" label="采购总金额" required>
            <uni-easyinput
              type="text"
              v-model="editForm.total_amount"
              placeholder="请输入采购总金额"
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item name="purchase_date" label="采购时间" required>
            <uni-datetime-picker type="datetime" v-model="editForm.purchase_date" />
          </uni-forms-item>
          <uni-forms-item name="purchase_status_enum_purchase_status_enum_id_1" label="采购状态" required>
            <uni-data-select
              v-model="editForm.purchase_status_enum_purchase_status_enum_id_1"
              :localdata="statusEnumOptions"
              clear
            />
          </uni-forms-item>
        </uni-forms>
        <button
          class="w-full box-border bg-primary-500 text-white rounded-full px-4 py-2 text-sm mt-4"
          @click="submitEdit"
        >保存</button>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const detail = ref({});
const statusEnumList = ref([]);
const statusEnumOptions = ref([]);
const editForm = ref({});
const purchase_info_id = ref(null);

// 获取页面参数
onLoad(async (option) => {
  if (option && option.purchase_info_id) {
    purchase_info_id.value = Number(option.purchase_info_id);
    await fetchDetail();
    await fetchStatusEnum();
  }
});

// 获取采购单详情
async function fetchDetail() {
  const res = await proxy.$cf.table.get({
    table_name: "purchase_info",
    param: { purchase_info_id: purchase_info_id.value }
  });
  if (res.success && res.data) {
    detail.value = { ...res.data };
    // 编辑表单初始化
    editForm.value = {
      purchase_info_id: res.data.purchase_info_id,
      purchase_no: res.data.purchase_no,
      supplier: res.data.supplier,
      total_amount: res.data.total_amount,
      purchase_date: res.data.purchase_date,
      purchase_status_enum_purchase_status_enum_id_1: res.data.purchase_status_enum_purchase_status_enum_id_1,
    };
  } else {
    proxy.$cf.toast({ message: res.message || "获取采购单详情失败", level: "error" });
  }
}

// 获取采购状态枚举
async function fetchStatusEnum() {
  const res = await proxy.$cf.table.list({
    table_name: "purchase_status_enum",
    param: {}
  });
  if (res.success && Array.isArray(res.data)) {
    statusEnumList.value = res.data;
    statusEnumOptions.value = res.data.map(item => ({
      value: item.purchase_status_enum_id,
      text: item.status_name
    }));
  }
}

// 打开编辑弹窗
function openEdit() {
  proxy.$refs.editPopup.open();
}

// 关闭编辑弹窗
function closeEdit() {
  proxy.$refs.editPopup.close();
}

// 保存编辑
async function submitEdit() {
  // 校验必填
  if (!editForm.value.purchase_no || !editForm.value.total_amount || !editForm.value.purchase_date || !editForm.value.purchase_status_enum_purchase_status_enum_id_1) {
    proxy.$cf.toast({ message: "请填写完整信息", level: "error" });
    return;
  }
  // 更新数据
  const res = await proxy.$cf.table.update({
    table_name: "purchase_info",
    param: {
      purchase_info_id: editForm.value.purchase_info_id,
      purchase_no: editForm.value.purchase_no,
      supplier: editForm.value.supplier,
      total_amount: editForm.value.total_amount,
      purchase_date: editForm.value.purchase_date,
      purchase_status_enum_purchase_status_enum_id_1: editForm.value.purchase_status_enum_purchase_status_enum_id_1
    }
  });
  if (res.success) {
    proxy.$cf.toast({ message: "保存成功", level: "success" });
    closeEdit();
    await fetchDetail();
  } else {
    proxy.$cf.toast({ message: res.message || "保存失败", level: "error" });
  }
}

// 金额格式化
function formatAmount(amount) {
  if (amount == undefined || amount == null) return "--";
  return Number(amount).toFixed(2);
}

// 日期格式化
function formatDate(val) {
  if (!val) return "--";
  // 支持 YYYY-MM-DD HH:mm:ss
  if (typeof val == "string" && val.length >= 10) {
    return val.replace("T", " ").substring(0, 19);
  }
  return val;
}

// 状态tag类型
function statusTagType(statusName) {
  if (!statusName) return "default";
  if (statusName.includes("完成") || statusName.includes("到货")) return "success";
  if (statusName.includes("下单")) return "primary";
  if (statusName.includes("待")) return "warning";
  return "default";
}
</script>