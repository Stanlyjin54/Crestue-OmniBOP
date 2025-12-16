/**
 * B2B支付条款组件
 * 支持账期、分期付款、信用支付等企业支付方式
 */

<template>
  <view class="b2b-payment-terms">
    <!-- 支付条款选择 -->
    <view class="payment-terms-selector">
      <view class="section-title">支付条款</view>
      <uni-data-select
        v-model="selectedTermsId"
        :localdata="paymentTermsOptions"
        placeholder="请选择支付条款"
        @change="handleTermsChange"
      />
    </view>

    <!-- 支付条款详情 -->
    <view v-if="selectedTerms" class="terms-detail">
      <view class="terms-info">
        <view class="info-item">
          <text class="label">条款名称：</text>
          <text class="value">{{ selectedTerms.terms_name }}</text>
        </view>
        <view class="info-item">
          <text class="label">条款类型：</text>
          <uni-tag :text="getTermsTypeText(selectedTerms.terms_type)" :type="getTermsTypeColor(selectedTerms.terms_type)" />
        </view>
        <view v-if="selectedTerms.credit_limit" class="info-item">
          <text class="label">信用额度：</text>
          <text class="value">¥{{ formatMoney(selectedTerms.credit_limit) }}</text>
        </view>
        <view v-if="selectedTerms.credit_period" class="info-item">
          <text class="label">账期天数：</text>
          <text class="value">{{ selectedTerms.credit_period }}天</text>
        </view>
        <view v-if="selectedTerms.installment_periods" class="info-item">
          <text class="label">分期期数：</text>
          <text class="value">{{ selectedTerms.installment_periods }}期</text>
        </view>
        <view v-if="selectedTerms.down_payment_ratio" class="info-item">
          <text class="label">首付比例：</text>
          <text class="value">{{ (selectedTerms.down_payment_ratio * 100).toFixed(1) }}%</text>
        </view>
        <view v-if="selectedTerms.interest_rate" class="info-item">
          <text class="label">利率：</text>
          <text class="value">{{ (selectedTerms.interest_rate * 100).toFixed(2) }}%</text>
        </view>
      </view>
    </view>

    <!-- 分期付款计划 -->
    <view v-if="selectedTerms.terms_type === 'installment' && installmentPlan" class="installment-plan">
      <view class="section-title">分期付款计划</view>
      <view class="plan-summary">
        <view class="summary-item">
          <text class="label">总金额：</text>
          <text class="value">¥{{ formatMoney(totalAmount) }}</text>
        </view>
        <view class="summary-item">
          <text class="label">首付金额：</text>
          <text class="value">¥{{ formatMoney(installmentPlan.down_payment) }}</text>
        </view>
        <view class="summary-item">
          <text class="label">分期金额：</text>
          <text class="value">¥{{ formatMoney(installmentPlan.installment_amount) }}</text>
        </view>
        <view class="summary-item">
          <text class="label">每期金额：</text>
          <text class="value">¥{{ formatMoney(installmentPlan.per_installment) }}</text>
        </view>
      </view>
      <view class="installment-schedule">
        <view v-for="(item, index) in installmentPlan.schedule" :key="index" class="schedule-item">
          <view class="schedule-header">
            <text class="period">第{{ index + 1 }}期</text>
            <text class="amount">¥{{ formatMoney(item.amount) }}</text>
          </view>
          <view class="schedule-detail">
            <text class="due-date">到期日：{{ formatDate(item.due_date) }}</text>
            <uni-tag :text="getPaymentStatusText(item.status)" :type="getPaymentStatusColor(item.status)" />
          </view>
        </view>
      </view>
    </view>

    <!-- 信用支付信息 -->
    <view v-if="selectedTerms.terms_type === 'credit' && creditInfo" class="credit-info">
      <view class="section-title">信用支付信息</view>
      <view class="credit-summary">
        <view class="summary-item">
          <text class="label">可用额度：</text>
          <text class="value credit-amount">¥{{ formatMoney(creditInfo.available_credit) }}</text>
        </view>
        <view class="summary-item">
          <text class="label">已用额度：</text>
          <text class="value">¥{{ formatMoney(creditInfo.used_credit) }}</text>
        </view>
        <view class="summary-item">
          <text class="label">总额度：</text>
          <text class="value">¥{{ formatMoney(creditInfo.total_credit) }}</text>
        </view>
      </view>
      <view v-if="creditInfo.warning_message" class="credit-warning">
        <uni-icons type="warning" color="#f56c6c" />
        <text class="warning-text">{{ creditInfo.warning_message }}</text>
      </view>
    </view>

    <!-- 账期支付信息 -->
    <view v-if="selectedTerms.terms_type === 'period'" class="period-info">
      <view class="section-title">账期支付信息</view>
      <view class="period-summary">
        <view class="summary-item">
          <text class="label">账期天数：</text>
          <text class="value">{{ selectedTerms.credit_period }}天</text>
        </view>
        <view class="summary-item">
          <text class="label">到期日期：</text>
          <text class="value">{{ formatDate(dueDate) }}</text>
        </view>
        <view class="summary-item">
          <text class="label">应付金额：</text>
          <text class="value amount">¥{{ formatMoney(totalAmount) }}</text>
        </view>
      </view>
    </view>

    <!-- 支付能力检查 -->
    <view v-if="showPaymentAbilityCheck" class="payment-ability-check">
      <button 
        class="check-button" 
        @click="checkPaymentAbility"
        :loading="checkingAbility"
      >
        检查支付能力
      </button>
      <view v-if="paymentAbilityResult" class="ability-result">
        <view v-if="paymentAbilityResult.can_pay" class="result-success">
          <uni-icons type="checkmarkempty" color="#67c23a" />
          <text>支付能力充足</text>
        </view>
        <view v-else class="result-fail">
          <uni-icons type="closeempty" color="#f56c6c" />
          <text>{{ paymentAbilityResult.message }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { getB2BPaymentTermsList, checkCustomerPaymentAbility } from '@/api/b2b/payment-terms.js';

const props = defineProps({
  customerId: {
    type: Number,
    required: true
  },
  totalAmount: {
    type: Number,
    required: true
  },
  showPaymentAbilityCheck: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['terms-change', 'ability-check']);

// 数据状态
const selectedTermsId = ref(null);
const paymentTermsList = ref([]);
const creditInfo = ref(null);
const installmentPlan = ref(null);
const paymentAbilityResult = ref(null);
const checkingAbility = ref(false);

// 计算属性
const selectedTerms = computed(() => {
  return paymentTermsList.value.find(item => item.terms_id === selectedTermsId.value);
});

const paymentTermsOptions = computed(() => {
  return paymentTermsList.value.map(item => ({
    value: item.terms_id,
    text: item.terms_name
  }));
});

const dueDate = computed(() => {
  if (!selectedTerms.value || !selectedTerms.value.credit_period) {
    return null;
  }
  const date = new Date();
  date.setDate(date.getDate() + selectedTerms.value.credit_period);
  return date;
});

// 方法
async function loadPaymentTerms() {
  try {
    const res = await getB2BPaymentTermsList({
      status: 'active',
      customer_id: props.customerId
    });
    if (res.success) {
      paymentTermsList.value = res.data.records || [];
      // 如果有默认条款，自动选择
      const defaultTerms = paymentTermsList.value.find(item => item.is_default);
      if (defaultTerms && !selectedTermsId.value) {
        selectedTermsId.value = defaultTerms.terms_id;
        handleTermsChange(defaultTerms.terms_id);
      }
    }
  } catch (error) {
    console.error('加载支付条款失败:', error);
  }
}

function handleTermsChange(termsId) {
  const terms = paymentTermsList.value.find(item => item.terms_id === termsId);
  if (terms) {
    // 根据不同条款类型处理
    if (terms.terms_type === 'installment') {
      calculateInstallmentPlan(terms);
    } else if (terms.terms_type === 'credit') {
      loadCustomerCreditInfo();
    }
    emit('terms-change', terms);
  }
}

function calculateInstallmentPlan(terms) {
  const downPayment = props.totalAmount * (terms.down_payment_ratio || 0);
  const installmentAmount = props.totalAmount - downPayment;
  const perInstallment = installmentAmount / (terms.installment_periods || 1);
  
  const schedule = [];
  for (let i = 0; i < (terms.installment_periods || 1); i++) {
    const dueDate = new Date();
    dueDate.setMonth(dueDate.getMonth() + i + 1);
    schedule.push({
      period: i + 1,
      amount: perInstallment,
      due_date: dueDate.toISOString().split('T')[0],
      status: 'pending'
    });
  }
  
  installmentPlan.value = {
    down_payment: downPayment,
    installment_amount: installmentAmount,
    per_installment: perInstallment,
    schedule: schedule
  };
}

async function loadCustomerCreditInfo() {
  try {
    // 这里可以调用获取客户信用信息的API
    // const res = await getCustomerCreditInfo(props.customerId);
    // if (res.success) {
    //   creditInfo.value = res.data;
    // }
    
    // 模拟数据
    creditInfo.value = {
      total_credit: 100000,
      used_credit: 35000,
      available_credit: 65000,
      warning_message: null
    };
    
    if (props.totalAmount > creditInfo.value.available_credit) {
      creditInfo.value.warning_message = '订单金额超过可用信用额度';
    }
  } catch (error) {
    console.error('加载客户信用信息失败:', error);
  }
}

async function checkPaymentAbility() {
  if (!selectedTerms.value) {
    return;
  }
  
  checkingAbility.value = true;
  try {
    const res = await checkCustomerPaymentAbility(
      props.customerId,
      props.totalAmount,
      selectedTerms.value.terms_type
    );
    if (res.success) {
      paymentAbilityResult.value = res.data;
      emit('ability-check', res.data);
    }
  } catch (error) {
    console.error('检查支付能力失败:', error);
  } finally {
    checkingAbility.value = false;
  }
}

// 工具函数
function getTermsTypeText(type) {
  const typeMap = {
    'credit': '信用支付',
    'installment': '分期付款',
    'period': '账期支付',
    'combined': '组合支付'
  };
  return typeMap[type] || type;
}

function getTermsTypeColor(type) {
  const colorMap = {
    'credit': 'primary',
    'installment': 'success',
    'period': 'warning',
    'combined': 'info'
  };
  return colorMap[type] || 'default';
}

function getPaymentStatusText(status) {
  const statusMap = {
    'pending': '待支付',
    'paid': '已支付',
    'overdue': '已逾期',
    'cancelled': '已取消'
  };
  return statusMap[status] || status;
}

function getPaymentStatusColor(status) {
  const colorMap = {
    'pending': 'warning',
    'paid': 'success',
    'overdue': 'error',
    'cancelled': 'default'
  };
  return colorMap[status] || 'default';
}

function formatMoney(amount) {
  return Number(amount).toFixed(2);
}

function formatDate(dateStr) {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  return date.toLocaleDateString('zh-CN');
}

// 监听属性变化
watch(() => props.customerId, (newVal) => {
  if (newVal) {
    loadPaymentTerms();
  }
}, { immediate: true });

watch(() => props.totalAmount, (newVal) => {
  if (selectedTerms.value && selectedTerms.value.terms_type === 'installment') {
    calculateInstallmentPlan(selectedTerms.value);
  }
  if (creditInfo.value && newVal > creditInfo.value.available_credit) {
    creditInfo.value.warning_message = '订单金额超过可用信用额度';
  } else if (creditInfo.value) {
    creditInfo.value.warning_message = null;
  }
});

// 暴露方法
defineExpose({
  getSelectedTerms: () => selectedTerms.value,
  checkPaymentAbility
});
</script>

<style lang="scss" scoped>
.b2b-payment-terms {
  padding: 20rpx;
  background-color: #fff;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.payment-terms-selector {
  margin-bottom: 30rpx;
}

.terms-detail {
  background-color: #f5f7fa;
  padding: 20rpx;
  border-radius: 8rpx;
  margin-bottom: 30rpx;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 16rpx;
  
  &:last-child {
    margin-bottom: 0;
  }
  
  .label {
    color: #606266;
    font-size: 28rpx;
    min-width: 140rpx;
  }
  
  .value {
    color: #303133;
    font-size: 28rpx;
    font-weight: 500;
  }
}

.installment-plan {
  margin-bottom: 30rpx;
}

.plan-summary {
  background-color: #f0f9ff;
  padding: 20rpx;
  border-radius: 8rpx;
  margin-bottom: 20rpx;
  
  .summary-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .label {
      color: #606266;
      font-size: 28rpx;
    }
    
    .value {
      color: #303133;
      font-size: 28rpx;
      font-weight: bold;
      
      &.credit-amount {
        color: #67c23a;
      }
    }
  }
}

.installment-schedule {
  .schedule-item {
    background-color: #fafafa;
    padding: 20rpx;
    border-radius: 8rpx;
    margin-bottom: 16rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .schedule-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8rpx;
    
    .period {
      color: #303133;
      font-size: 28rpx;
      font-weight: 500;
    }
    
    .amount {
      color: #f56c6c;
      font-size: 32rpx;
      font-weight: bold;
    }
  }
  
  .schedule-detail {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .due-date {
      color: #909399;
      font-size: 24rpx;
    }
  }
}

.credit-info {
  margin-bottom: 30rpx;
}

.credit-summary {
  background-color: #f0f9ff;
  padding: 20rpx;
  border-radius: 8rpx;
  margin-bottom: 20rpx;
}

.credit-warning {
  display: flex;
  align-items: center;
  background-color: #fef0f0;
  padding: 16rpx;
  border-radius: 8rpx;
  
  .warning-text {
    color: #f56c6c;
    font-size: 28rpx;
    margin-left: 8rpx;
  }
}

.period-info {
  margin-bottom: 30rpx;
}

.period-summary {
  background-color: #f0f9ff;
  padding: 20rpx;
  border-radius: 8rpx;
  
  .amount {
    color: #f56c6c;
    font-weight: bold;
  }
}

.payment-ability-check {
  text-align: center;
  
  .check-button {
    background-color: #409eff;
    color: white;
    padding: 20rpx 40rpx;
    border-radius: 8rpx;
    font-size: 28rpx;
    margin-bottom: 20rpx;
  }
  
  .ability-result {
    padding: 20rpx;
    border-radius: 8rpx;
    
    .result-success {
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #f0f9ff;
      color: #67c23a;
      padding: 16rpx;
      border-radius: 8rpx;
    }
    
    .result-fail {
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #fef0f0;
      color: #f56c6c;
      padding: 16rpx;
      border-radius: 8rpx;
    }
  }
}
</style>