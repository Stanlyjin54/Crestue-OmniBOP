<template>
  <base-layout>
    <!-- 客户编辑弹窗 -->
    <uni-popup ref="editPopup" type="center">
      <view class="bg-white p-4 w-80 rounded-xl shadow-xl">
        <view class="flex items-center mb-4">
          <uni-icons type="person" size="24" class="mr-2 text-primary-500"/>
          <text class="text-lg font-bold text-primary-700 flex-1">编辑客户信息</text>
          <uni-icons type="close" size="22" class="text-neutral-400" @click="handleClose"/>
        </view>
        <uni-forms
          ref="formRef"
          :modelValue="formData"
          label-position="top"
          class="mb-2"
        >
          <uni-forms-item required label="客户名称" name="customer_name">
            <uni-easyinput
              type="text"
              v-model="formData.customer_name"
              placeholder="请输入客户名称"
              clearable
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item label="手机号" name="phone">
            <uni-easyinput
              type="text"
              v-model="formData.phone"
              placeholder="请输入手机号"
              clearable
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item label="邮箱" name="email">
            <uni-easyinput
              type="text"
              v-model="formData.email"
              placeholder="请输入邮箱"
              clearable
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item label="地址" name="address">
            <uni-easyinput
              type="text"
              v-model="formData.address"
              placeholder="请输入地址"
              clearable
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item label="RFM分层" name="rfm_level_enum_rfm_level_enum_id_1">
            <uni-data-select
              v-model="formData.rfm_level_enum_rfm_level_enum_id_1"
              :localdata="rfmLevelOptions"
              clear
              placeholder="请选择分层"
            />
          </uni-forms-item>
          <uni-forms-item label="客户标签" name="customer_tag_enum_customer_tag_enum_id_1">
            <uni-data-select
              v-model="formData.customer_tag_enum_customer_tag_enum_id_1"
              :localdata="tagOptions"
              clear
              placeholder="请选择标签"
            />
          </uni-forms-item>
          <uni-forms-item label="微信号" name="wechat_id">
            <uni-easyinput
              type="text"
              v-model="formData.wechat_id"
              placeholder="请输入微信号"
              clearable
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item label="客户来源" name="source">
            <uni-easyinput
              type="text"
              v-model="formData.source"
              placeholder="请输入客户来源"
              clearable
              class="box-border"
            />
          </uni-forms-item>
        </uni-forms>

        <view class="flex gap-3 mt-4">
          <button
            class="flex-1 bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-md"
            @click="handleSave"
          >保存</button>
          <button
            class="flex-1 bg-neutral-200 text-primary-700 rounded-full px-4 py-2 text-sm"
            @click="handleClose"
          >取消</button>
        </view>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const formData = ref({
  customer_info_id: null,
  customer_name: "",
  phone: "",
  email: "",
  address: "",
  rfm_level_enum_rfm_level_enum_id_1: "",
  customer_tag_enum_customer_tag_enum_id_1: "",
  wechat_id: "",
  source: ""
});

// 下拉选项
const rfmLevelOptions = ref([]);
const tagOptions = ref([]);

// 打开弹窗并回显数据
function openPopup(customer_info_id) {
  // 获取枚举数据
  fetchEnums();
  // 获取客户详情
  if (customer_info_id) {
    proxy.$cf.table.get({
      table_name: "customer_info",
      param: { customer_info_id }
    }).then(res => {
      if (res.success && res.data) {
        // 只取允许编辑的字段
        Object.assign(formData.value, {
          customer_info_id: res.data.customer_info_id,
          customer_name: res.data.customer_name || "",
          phone: res.data.phone || "",
          email: res.data.email || "",
          address: res.data.address || "",
          rfm_level_enum_rfm_level_enum_id_1: res.data.rfm_level_enum_rfm_level_enum_id_1 || "",
          customer_tag_enum_customer_tag_enum_id_1: res.data.customer_tag_enum_customer_tag_enum_id_1 || "",
          wechat_id: res.data.wechat_id || "",
          source: res.data.source || ""
        });
        proxy.$refs.editPopup.open();
      } else {
        proxy.$cf.toast({ message: res.message || "获取客户信息失败", level: "error" });
      }
    });
  } else {
    // 新增场景
    Object.assign(formData.value, {
      customer_info_id: null,
      customer_name: "",
      phone: "",
      email: "",
      address: "",
      rfm_level_enum_rfm_level_enum_id_1: "",
      customer_tag_enum_customer_tag_enum_id_1: "",
      wechat_id: "",
      source: ""
    });
    proxy.$refs.editPopup.open();
  }
}

// 获取枚举数据
async function fetchEnums() {
  // RFM分层
  const rfmRes = await proxy.$cf.table.list({
    table_name: "rfm_level_enum",
    param: {}
  });
  rfmLevelOptions.value = (rfmRes.success && rfmRes.data)
    ? rfmRes.data.map(item => ({
        value: item.rfm_level_enum_id,
        text: item.level_name
      }))
    : [];
  // 客户标签
  const tagRes = await proxy.$cf.table.list({
    table_name: "customer_tag_enum",
    param: {}
  });
  tagOptions.value = (tagRes.success && tagRes.data)
    ? tagRes.data.map(item => ({
        value: item.customer_tag_enum_id,
        text: item.tag_name
      }))
    : [];
}

// 关闭弹窗
function handleClose() {
  proxy.$refs.editPopup.close();
}

// 保存客户信息
async function handleSave() {
  // 校验必填
  if (!formData.value.customer_name) {
    proxy.$cf.toast({ message: "客户名称不能为空", level: "error" });
    return;
  }
  let isEdit = !!formData.value.customer_info_id;
  let saveRes;
  if (isEdit) {
    // 编辑
    saveRes = await proxy.$cf.table.update({
      table_name: "customer_info",
      param: {
        customer_info_id: formData.value.customer_info_id,
        customer_name: formData.value.customer_name,
        phone: formData.value.phone,
        email: formData.value.email,
        address: formData.value.address,
        rfm_level_enum_rfm_level_enum_id_1: formData.value.rfm_level_enum_rfm_level_enum_id_1 || null,
        customer_tag_enum_customer_tag_enum_id_1: formData.value.customer_tag_enum_customer_tag_enum_id_1 || null,
        wechat_id: formData.value.wechat_id,
        source: formData.value.source
      }
    });
  } else {
    // 新增
    saveRes = await proxy.$cf.table.add({
      table_name: "customer_info",
      param: {
        customer_name: formData.value.customer_name,
        phone: formData.value.phone,
        email: formData.value.email,
        address: formData.value.address,
        rfm_level_enum_rfm_level_enum_id_1: formData.value.rfm_level_enum_rfm_level_enum_id_1 || null,
        customer_tag_enum_customer_tag_enum_id_1: formData.value.customer_tag_enum_customer_tag_enum_id_1 || null,
        wechat_id: formData.value.wechat_id,
        source: formData.value.source
      }
    });
  }
  if (saveRes.success) {
    proxy.$cf.toast({ message: "保存成功", level: "success" });
    proxy.$refs.editPopup.close();
    // 通知父页面刷新客户列表（可用事件/全局变量等，具体由父页面实现，这里emit事件）
    proxy.$emit && proxy.$emit("refreshList");
  } else {
    proxy.$cf.toast({ message: saveRes.message || "保存失败", level: "error" });
  }
}

// 对外暴露openPopup方法
defineExpose({
  openPopup
});
</script>