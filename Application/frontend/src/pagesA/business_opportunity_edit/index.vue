<template>
  <base-layout>
    <!-- 商机编辑弹窗（中心弹窗） -->
    <uni-popup ref="editPopup" type="center">
      <view class="bg-white p-4 w-80 rounded-xl shadow-xl">
        <view class="mb-4 flex items-center gap-2">
          <uni-icons type="compose" size="24" color="#007BFF"/>
          <text class="text-lg font-bold text-primary-600">编辑商机线索</text>
        </view>
        <uni-forms
          ref="formRef"
          :modelValue="formData"
          label-position="top"
          class="mb-2"
        >
          <!-- 客户选择 -->
          <uni-forms-item name="customer_info_customer_info_id_1" label="客户" required>
            <uni-data-select
              v-model="formData.customer_info_customer_info_id_1"
              :localdata="customerOptions"
              placeholder="请选择客户"
              clear
            />
          </uni-forms-item>
          <!-- 商机名称 -->
          <uni-forms-item name="opportunity_name" label="商机名称" required>
            <uni-easyinput
              v-model="formData.opportunity_name"
              type="text"
              placeholder="请输入商机名称"
              clearable
              class="box-border"
            />
          </uni-forms-item>
          <!-- 商机状态 -->
          <uni-forms-item name="status" label="商机状态" required>
            <uni-data-select
              v-model="formData.status"
              :localdata="statusOptions"
              placeholder="请选择商机状态"
              clear
            />
          </uni-forms-item>
          <!-- AI跟进建议（只读） -->
          <uni-forms-item name="follow_up_suggestion" label="AI跟进建议">
            <uni-easyinput
              v-model="formData.follow_up_suggestion"
              type="textarea"
              placeholder="AI建议将自动生成"
              disabled
              class="box-border"
              :autoHeight="true"
            />
          </uni-forms-item>
        </uni-forms>
        <!-- 操作按钮 -->
        <view class="flex gap-4 mt-4">
          <button
            class="flex-1 bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-md"
            @click="handleSubmit"
            :loading="loading"
          >保存</button>
          <button
            class="flex-1 bg-neutral-200 text-primary-600 rounded-full px-4 py-2 text-sm"
            @click="closePopup"
          >取消</button>
        </view>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

// 表单数据
const formData = ref({
  business_opportunity_id: null,
  customer_info_customer_info_id_1: '',
  opportunity_name: '',
  status: '',
  follow_up_suggestion: ''
});

// 客户下拉选项
const customerOptions = ref([]);
// 商机状态选项（自定义，实际可根据业务调整）
const statusOptions = ref([
  { value: '初步接洽', text: '初步接洽' },
  { value: '需求确认', text: '需求确认' },
  { value: '方案报价', text: '方案报价' },
  { value: '合同谈判', text: '合同谈判' },
  { value: '赢单', text: '赢单' },
  { value: '输单', text: '输单' }
]);

const loading = ref(false);
const formRef = ref(null);

// 弹窗控制
const editPopup = ref(null);

// 打开弹窗并初始化（外部调用时传入business_opportunity_id，若为null则为新增）
function openPopup(business_opportunity_id = null) {
  resetForm();
  if (business_opportunity_id) {
    // 编辑模式，加载数据
    loadDetail(business_opportunity_id);
  }
  proxy.$refs.editPopup.open();
}

// 关闭弹窗
function closePopup() {
  proxy.$refs.editPopup.close();
}

// 重置表单
function resetForm() {
  formData.value = {
    business_opportunity_id: null,
    customer_info_customer_info_id_1: '',
    opportunity_name: '',
    status: '',
    follow_up_suggestion: ''
  };
}

// 加载客户下拉数据
async function loadCustomerOptions() {
  const res = await proxy.$cf.table.list({
    table_name: 'customer_info',
    param: {}
  });
  if (res.success) {
    customerOptions.value = res.data.map(item => ({
      value: item.customer_info_id,
      text: item.customer_name
    }));
  }
}

// 加载商机详情
async function loadDetail(business_opportunity_id) {
  loading.value = true;
  const res = await proxy.$cf.table.get({
    table_name: 'business_opportunity',
    param: { business_opportunity_id }
  });
  if (res.success && res.data) {
    formData.value = {
      business_opportunity_id: res.data.business_opportunity_id,
      customer_info_customer_info_id_1: res.data.customer_info_customer_info_id_1,
      opportunity_name: res.data.opportunity_name,
      status: res.data.status,
      follow_up_suggestion: res.data.follow_up_suggestion || ''
    };
  }
  loading.value = false;
}

// 提交表单
async function handleSubmit() {
  // 校验必填字段
  if (!formData.value.customer_info_customer_info_id_1) {
    proxy.$cf.toast({ message: '请选择客户', level: 'error' });
    return;
  }
  if (!formData.value.opportunity_name) {
    proxy.$cf.toast({ message: '请输入商机名称', level: 'error' });
    return;
  }
  if (!formData.value.status) {
    proxy.$cf.toast({ message: '请选择商机状态', level: 'error' });
    return;
  }
  loading.value = true;

  // AI字段生成（如状态或名称有变更时，自动生成AI建议）
  let aiSuggestion = '';
  try {
    const aiRes = await proxy.$cf.text.text2text({
      prompt: "你是销售专家，请根据客户和商机状态给出专业的跟进建议，直接输出",
      text: `客户：${getCustomerName(formData.value.customer_info_customer_info_id_1)}，商机名称：${formData.value.opportunity_name}，状态：${formData.value.status}`
    });
    if (aiRes.success) {
      aiSuggestion = aiRes.data;
    }
  } catch (e) {
    aiSuggestion = '';
  }
  formData.value.follow_up_suggestion = aiSuggestion;

  // 新增或编辑
  let res;
  if (formData.value.business_opportunity_id) {
    // 编辑
    res = await proxy.$cf.table.update({
      table_name: 'business_opportunity',
      param: {
        business_opportunity_id: formData.value.business_opportunity_id,
        customer_info_customer_info_id_1: formData.value.customer_info_customer_info_id_1,
        opportunity_name: formData.value.opportunity_name,
        status: formData.value.status,
        follow_up_suggestion: formData.value.follow_up_suggestion
      }
    });
  } else {
    // 新增
    res = await proxy.$cf.table.add({
      table_name: 'business_opportunity',
      param: {
        customer_info_customer_info_id_1: formData.value.customer_info_customer_info_id_1,
        opportunity_name: formData.value.opportunity_name,
        status: formData.value.status,
        follow_up_suggestion: formData.value.follow_up_suggestion
      }
    });
  }

  loading.value = false;
  if (res && res.success) {
    proxy.$cf.toast({ message: '保存成功', level: 'success' });
    closePopup();
    // 跳转并刷新商机线索页面
    proxy.$cf.navigate.to({
      url: '/pages/business_opportunity/index',
      type: 'page'
    });
  } else {
    proxy.$cf.toast({ message: res.message || '保存失败', level: 'error' });
  }
}

// 根据客户ID获取客户名称
function getCustomerName(customerId) {
  const found = customerOptions.value.find(item => item.value == customerId);
  return found ? found.text : '';
}

// 生命周期：页面加载时加载客户下拉
onLoad((option) => {
  loadCustomerOptions();
  // 如果有传递 business_opportunity_id，则自动打开弹窗并加载数据
  if (option && option.business_opportunity_id) {
    openPopup(Number(option.business_opportunity_id));
  }
});

// 暴露openPopup方法供外部调用（如页面/父组件通过ref调用弹窗）
defineExpose({ openPopup });

</script>