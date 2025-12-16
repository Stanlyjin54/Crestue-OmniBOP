<template>
  <base-layout>
    <!-- 弹窗内容 -->
    <uni-popup ref="editPopup" type="center">
      <view class="bg-white p-4 w-80 rounded-xl shadow-lg">
        <view class="mb-4">
          <text class="block text-lg font-bold text-primary-700 mb-1">编辑采购单</text>
          <text class="block text-secondary-500 text-xs">请完善采购单信息</text>
        </view>
        <uni-forms ref="formRef" :modelValue="formData" label-position="top">
          <!-- 采购单号 -->
          <uni-forms-item name="purchase_no" label="采购单号" required>
            <uni-easyinput
              class="box-border"
              type="text"
              v-model="formData.purchase_no"
              placeholder="请输入采购单号"
              clearable
            />
          </uni-forms-item>
          <!-- 采购员（当前登录用户） -->
          <uni-forms-item name="user_info_user_info_id_1" label="采购员" required>
            <uni-easyinput
              class="box-border"
              type="text"
              v-model="formData.username"
              :disabled="true"
              placeholder="采购员"
              prefixIcon="person"
            />
          </uni-forms-item>
          <!-- 供应商 -->
          <uni-forms-item name="supplier" label="供应商">
            <uni-easyinput
              class="box-border"
              type="text"
              v-model="formData.supplier"
              placeholder="请输入供应商"
              clearable
            />
          </uni-forms-item>
          <!-- 采购总金额 -->
          <uni-forms-item name="total_amount" label="采购总金额" required>
            <uni-easyinput
              class="box-border"
              type="number"
              v-model="formData.total_amount"
              placeholder="请输入采购总金额"
              clearable
            />
          </uni-forms-item>
          <!-- 采购时间 -->
          <uni-forms-item name="purchase_date" label="采购时间" required>
            <uni-datetime-picker
              type="datetime"
              v-model="formData.purchase_date"
            />
          </uni-forms-item>
          <!-- 采购状态 -->
          <uni-forms-item name="purchase_status_enum_purchase_status_enum_id_1" label="采购状态" required>
            <uni-data-select
              v-model="formData.purchase_status_enum_purchase_status_enum_id_1"
              :localdata="statusOptions"
              placeholder="请选择采购状态"
              clear
            />
          </uni-forms-item>
        </uni-forms>
        <view class="flex gap-3 mt-6">
          <button
            class="flex-1 bg-primary-500 text-white rounded-full px-4 py-2 text-sm"
            @click="handleSave"
          >
            <uni-icons type="checkmarkempty" class="mr-1" />保存
          </button>
          <button
            class="flex-1 bg-neutral-200 text-primary-700 rounded-full px-4 py-2 text-sm"
            @click="closePopup"
          >
            <uni-icons type="close" class="mr-1" />取消
          </button>
        </view>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const formRef = ref();
const formData = ref({
  purchase_info_id: null,
  purchase_no: "",
  user_info_user_info_id_1: null,
  username: "",
  supplier: "",
  total_amount: "",
  purchase_date: "",
  purchase_status_enum_purchase_status_enum_id_1: null,
});

const statusOptions = ref([]); // 采购状态下拉数据

// 打开弹窗并加载数据
async function openPopup(purchase_info_id) {
  // 获取采购状态枚举
  await fetchStatusOptions();
  // 获取采购单详情
  if (purchase_info_id) {
    const res = await proxy.$cf.table.get({
      table_name: "purchase_info",
      param: { purchase_info_id },
    });
    if (res.success && res.data) {
      Object.assign(formData.value, res.data);
      // 采购员
      if (res.data.user_info_user_info_id_1) {
        formData.value.user_info_user_info_id_1 = res.data.user_info_user_info_id_1;
        formData.value.username = res.data.username || "";
      } else {
        await setCurrentUser();
      }
      // 采购时间格式校正
      if (res.data.purchase_date) {
        formData.value.purchase_date = res.data.purchase_date;
      }
    }
  } else {
    // 新增，自动填充采购员
    await setCurrentUser();
    formData.value.purchase_date = getNowDatetime();
  }
  proxy.$refs.editPopup.open();
}

// 获取采购状态选项
async function fetchStatusOptions() {
  const res = await proxy.$cf.table.list({
    table_name: "purchase_status_enum",
    param: {},
  });
  if (res.success) {
    statusOptions.value = (res.data || []).map((item) => ({
      value: item.purchase_status_enum_id,
      text: item.status_name,
    }));
  }
}

// 自动填充当前登录用户为采购员
async function setCurrentUser() {
  const userRes = await proxy.$cf.login.getLoginUser();
  if (userRes.success && userRes.data) {
    formData.value.user_info_user_info_id_1 = userRes.data.user_info_id;
    formData.value.username = userRes.data.username;
  }
}

// 保存采购单
async function handleSave() {
  // 校验
  if (!formData.value.purchase_no) {
    proxy.$cf.toast({ message: "采购单号不能为空", level: "error" });
    return;
  }
  if (!formData.value.user_info_user_info_id_1) {
    proxy.$cf.toast({ message: "采购员不能为空", level: "error" });
    return;
  }
  if (!formData.value.total_amount) {
    proxy.$cf.toast({ message: "采购总金额不能为空", level: "error" });
    return;
  }
  if (!formData.value.purchase_date) {
    proxy.$cf.toast({ message: "采购时间不能为空", level: "error" });
    return;
  }
  if (!formData.value.purchase_status_enum_purchase_status_enum_id_1) {
    proxy.$cf.toast({ message: "请选择采购状态", level: "error" });
    return;
  }

  // 组装参数
  const param = {
    purchase_info_id: formData.value.purchase_info_id,
    purchase_no: formData.value.purchase_no,
    user_info_user_info_id_1: formData.value.user_info_user_info_id_1,
    supplier: formData.value.supplier,
    total_amount: formData.value.total_amount,
    purchase_date: formatDatetime(formData.value.purchase_date),
    purchase_status_enum_purchase_status_enum_id_1: formData.value.purchase_status_enum_purchase_status_enum_id_1,
  };

  let res;
  if (param.purchase_info_id) {
    // 编辑
    res = await proxy.$cf.table.update({
      table_name: "purchase_info",
      param,
    });
  } else {
    // 新增
    res = await proxy.$cf.table.add({
      table_name: "purchase_info",
      param,
    });
  }

  if (res && res.success) {
    proxy.$cf.toast({ message: "保存成功", level: "success" });
    closePopup(true);
  } else {
    proxy.$cf.toast({ message: res.message || "保存失败", level: "error" });
  }
}

// 关闭弹窗
function closePopup(refresh = false) {
  proxy.$refs.editPopup.close();
  // 通知父页面刷新采购列表
  if (refresh) {
    // 触发自定义事件
    proxy.$emit && proxy.$emit("refreshList");
  }
}

// 获取当前时间字符串（YYYY-MM-DD HH:mm:ss）
function getNowDatetime() {
  const now = new Date();
  const pad = (n) => n.toString().padStart(2, "0");
  return (
    now.getFullYear() +
    "-" +
    pad(now.getMonth() + 1) +
    "-" +
    pad(now.getDate()) +
    " " +
    pad(now.getHours()) +
    ":" +
    pad(now.getMinutes()) +
    ":" +
    pad(now.getSeconds())
  );
}

// 规范化日期时间格式
function formatDatetime(val) {
  if (!val) return "";
  // 如果已经是YYYY-MM-DD HH:mm:ss则直接返回
  if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(val)) {
    return val;
  }
  // 兼容uni-datetime-picker返回的格式
  const d = new Date(val);
  if (isNaN(d.getTime())) return "";
  const pad = (n) => n.toString().padStart(2, "0");
  return (
    d.getFullYear() +
    "-" +
    pad(d.getMonth() + 1) +
    "-" +
    pad(d.getDate()) +
    " " +
    pad(d.getHours()) +
    ":" +
    pad(d.getMinutes()) +
    ":" +
    pad(d.getSeconds())
  );
}

// 页面初始化：由父页面调用openPopup(purchase_info_id)
defineExpose({ openPopup });

</script>