<template>
  <base-layout>
    <view class="p-2">
      <!-- 标题 -->
      <view class="flex items-center mb-4">
        <uni-icons type="headphones" size="28" color="#4b5563" class="mr-2"/>
        <text class="text-2xl font-bold text-primary-600">名片OCR建档</text>
      </view>

      <!-- 步骤引导 -->
      <view class="flex items-center gap-2 mb-4">
        <uni-icons type="upload" size="20" color="#0ea5e9"/>
        <text class="text-base text-secondary-700">1. 上传名片图片</text>
        <uni-icons type="arrow-right" size="18" color="#9ca3af"/>
        <uni-icons type="search" size="20" color="#0ea5e9"/>
        <text class="text-base text-secondary-700">2. 自动识别</text>
        <uni-icons type="arrow-right" size="18" color="#9ca3af"/>
        <uni-icons type="compose" size="20" color="#0ea5e9"/>
        <text class="text-base text-secondary-700">3. 补充并建档</text>
      </view>

      <!-- 卡片区域 -->
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <!-- 名片图片上传 -->
        <view class="mb-4">
          <text class="block text-base font-semibold mb-2 text-neutral-700">名片图片</text>
          <base-upload
            :limit="1"
            mode="grid"
            :initialFiles="cardImageFiles"
            @success="onCardImageUploadSuccess"
            @delete="onCardImageDelete"
          ></base-upload>
        </view>

        <!-- OCR识别结果展示 -->
        <view v-if="ocrResult" class="mb-4">
          <text class="block text-base font-semibold mb-2 text-neutral-700">OCR识别结果</text>
          <view class="bg-neutral-50 rounded-lg p-2 min-h-12 text-sm text-neutral-600 whitespace-pre-line">
            {{ ocrResult }}
          </view>
        </view>

        <!-- 客户信息表单 -->
        <uni-forms :modelValue="customerForm" label-position="top" class="mb-2">
          <uni-forms-item required label="客户名称" name="customer_name">
            <uni-easyinput
              type="text"
              v-model="customerForm.customer_name"
              placeholder="请输入客户名称"
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item label="手机号" name="phone">
            <uni-easyinput
              type="text"
              v-model="customerForm.phone"
              placeholder="请输入手机号"
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item label="邮箱" name="email">
            <uni-easyinput
              type="text"
              v-model="customerForm.email"
              placeholder="请输入邮箱"
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item label="地址" name="address">
            <uni-easyinput
              type="text"
              v-model="customerForm.address"
              placeholder="请输入地址"
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item label="微信号" name="wechat_id">
            <uni-easyinput
              type="text"
              v-model="customerForm.wechat_id"
              placeholder="请输入微信号"
              class="box-border"
            />
          </uni-forms-item>
        </uni-forms>

        <!-- 保存按钮 -->
        <button
          class="w-full box-border bg-primary-500 text-white rounded-full px-4 py-2 text-base font-semibold mt-2"
          @click="handleSave"
          :disabled="!canSave"
        >
          <uni-icons type="checkmarkempty" size="20" class="mr-1"/>
          保存建档
        </button>
      </view>

      <!-- 识别失败提示 -->
      <view v-if="ocrError" class="bg-warning-100 text-warning-700 rounded-lg p-3 mb-4 flex items-center gap-2">
        <uni-icons type="info" color="#f59e0b" size="20"/>
        <text>{{ ocrError }}</text>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const cardImageFiles = ref([]);
const cardImageUrl = ref(""); // 上传后图片的url（去base_url）
const ocrResult = ref("");
const ocrError = ref("");
const customerForm = ref({
  customer_name: "",
  phone: "",
  email: "",
  address: "",
  wechat_id: ""
});
const canSave = computed(() =>
  cardImageUrl.value &&
  customerForm.value.customer_name &&
  !saving.value
);
const saving = ref(false);

// 上传名片图片
async function onCardImageUploadSuccess(item) {
  if (item.success) {
    cardImageFiles.value = [{ url: proxy.get_resource_url(item.data.url) }];
    cardImageUrl.value = item.data.url;
    ocrError.value = "";
    // 调用OCR识别
    proxy.$cf.loading.showLoading({ title: "识别中" });
    try {
      const res = await proxy.$cf.image.pic2word({ url: item.data.url });
      proxy.$cf.loading.hideLoading();
      if (res.success) {
        ocrResult.value = res.data;
        // 尝试智能解析客户名、手机号、邮箱等（简单规则，实际可用AI更智能）
        parseOcrToForm(res.data);
      } else {
        ocrResult.value = "";
        ocrError.value = res.message || "识别失败，请手动填写客户信息";
      }
    } catch (e) {
      proxy.$cf.loading.hideLoading();
      ocrResult.value = "";
      ocrError.value = "识别失败，请手动填写客户信息";
    }
  }
}

function onCardImageDelete(e) {
  cardImageFiles.value = [];
  cardImageUrl.value = "";
  ocrResult.value = "";
  ocrError.value = "";
  // 清空表单
  customerForm.value = {
    customer_name: "",
    phone: "",
    email: "",
    address: "",
    wechat_id: ""
  };
}

// 简单正则解析OCR内容到表单
function parseOcrToForm(text) {
  // 客户名（取第一行或“姓名”字段）
  let nameMatch = text.match(/(姓名|Name)[:：]?\s*([^\n\r，, ]+)/);
  customerForm.value.customer_name = nameMatch ? nameMatch[2] : text.split('\n')[0].trim();
  // 手机号
  let phoneMatch = text.match(/(1\d{10})/);
  customerForm.value.phone = phoneMatch ? phoneMatch[1] : "";
  // 邮箱
  let emailMatch = text.match(/([a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+)/);
  customerForm.value.email = emailMatch ? emailMatch[1] : "";
  // 地址
  let addressMatch = text.match(/(地址|Add)[:：]?\s*([^\n\r]+)/);
  customerForm.value.address = addressMatch ? addressMatch[2].trim() : "";
  // 微信号
  let wechatMatch = text.match(/(微信|WeChat|wechat)[:：]?\s*([^\n\r，, ]+)/i);
  customerForm.value.wechat_id = wechatMatch ? wechatMatch[2] : "";
}

// 保存建档
async function handleSave() {
  if (!canSave.value) return;
  saving.value = true;
  proxy.$cf.loading.showLoading({ title: "保存中" });
  try {
    // 1. 保存客户信息
    const customerRes = await proxy.$cf.table.add({
      table_name: "customer_info",
      param: {
        customer_name: customerForm.value.customer_name,
        phone: customerForm.value.phone,
        email: customerForm.value.email,
        address: customerForm.value.address,
        wechat_id: customerForm.value.wechat_id
      }
    });
    if (!customerRes.success) {
      proxy.$cf.loading.hideLoading();
      saving.value = false;
      await proxy.$cf.toast({ message: customerRes.message || "客户信息保存失败", level: "error" });
      return;
    }
    // 2. 保存OCR记录
    const ocrRes = await proxy.$cf.table.add({
      table_name: "business_card_ocr",
      param: {
        customer_info_customer_info_id_1: customerRes.data,
        card_image: [{ url: cardImageUrl.value }],
        ocr_result: ocrResult.value || ""
      }
    });
    proxy.$cf.loading.hideLoading();
    if (ocrRes.success) {
      await proxy.$cf.toast({ message: "建档成功", level: "success" });
      // 跳转客户列表页面
      proxy.$cf.navigate.to({ url: "/pages/customer/index", type: "page" });
    } else {
      saving.value = false;
      await proxy.$cf.toast({ message: ocrRes.message || "建档失败", level: "error" });
    }
  } catch (e) {
    proxy.$cf.loading.hideLoading();
    saving.value = false;
    await proxy.$cf.toast({ message: "保存失败", level: "error" });
  }
  saving.value = false;
}
</script>