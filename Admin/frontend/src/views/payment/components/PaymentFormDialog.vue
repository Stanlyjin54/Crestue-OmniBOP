<template>
  <el-dialog
    v-model="visible"
    :title="dialogTitle"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      class="payment-form"
    >
      <!-- 基本信息 -->
      <el-divider content-position="left">基本信息</el-divider>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="订单编号" prop="orderNo">
            <el-input v-model="formData.orderNo" placeholder="请输入订单编号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="客户名称" prop="customerName">
            <el-input v-model="formData.customerName" placeholder="请输入客户名称" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="支付方式" prop="paymentMethod">
            <el-select v-model="formData.paymentMethod" placeholder="请选择支付方式" style="width: 100%">
              <el-option label="现金" value="cash" />
              <el-option label="银行转账" value="bank_transfer" />
              <el-option label="账期支付" value="credit" />
              <el-option label="分期付款" value="installment" />
              <el-option label="支付宝" value="alipay" />
              <el-option label="微信支付" value="wechat" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="支付金额" prop="amount">
            <el-input-number
              v-model="formData.amount"
              :min="0"
              :precision="2"
              style="width: 100%"
              placeholder="请输入支付金额"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 账期/分期信息 -->
      <template v-if="formData.paymentMethod === 'credit' || formData.paymentMethod === 'installment'">
        <el-divider content-position="left">{{ formData.paymentMethod === 'credit' ? '账期信息' : '分期信息' }}</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账期天数" prop="creditDays" v-if="formData.paymentMethod === 'credit'">
              <el-input-number
                v-model="formData.creditDays"
                :min="1"
                :max="365"
                style="width: 100%"
                placeholder="请输入账期天数"
              />
            </el-form-item>
            <el-form-item label="分期期数" prop="installmentPeriods" v-if="formData.paymentMethod === 'installment'">
              <el-input-number
                v-model="formData.installmentPeriods"
                :min="2"
                :max="24"
                style="width: 100%"
                placeholder="请输入分期期数"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期时间" prop="dueDate" v-if="formData.paymentMethod === 'credit'">
              <el-date-picker
                v-model="formData.dueDate"
                type="date"
                placeholder="请选择到期时间"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="首付金额" prop="downPayment" v-if="formData.paymentMethod === 'installment'">
              <el-input-number
                v-model="formData.downPayment"
                :min="0"
                :max="formData.amount"
                :precision="2"
                style="width: 100%"
                placeholder="请输入首付金额"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 分期计划 -->
        <template v-if="formData.paymentMethod === 'installment' && formData.installmentPeriods > 0">
          <el-form-item label="分期计划">
            <el-table :data="installmentPlan" border style="width: 100%">
              <el-table-column type="index" label="期数" width="80" />
              <el-table-column label="应还日期" width="150">
                <template #default="{ row }">
                  <el-date-picker
                    v-model="row.dueDate"
                    type="date"
                    placeholder="选择日期"
                    style="width: 100%"
                  />
                </template>
              </el-table-column>
              <el-table-column label="应还金额" width="150">
                <template #default="{ row }">
                  <el-input-number
                    v-model="row.amount"
                    :min="0"
                    :precision="2"
                    style="width: 100%"
                  />
                </template>
              </el-table-column>
              <el-table-column label="备注">
                <template #default="{ row }">
                  <el-input v-model="row.remark" placeholder="备注信息" />
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
        </template>
      </template>

      <!-- 银行信息 -->
      <template v-if="formData.paymentMethod === 'bank_transfer'">
        <el-divider content-position="left">银行信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="银行名称" prop="bankName">
              <el-input v-model="formData.bankName" placeholder="请输入银行名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行账号" prop="bankAccount">
              <el-input v-model="formData.bankAccount" placeholder="请输入银行账号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账户名称" prop="accountName">
              <el-input v-model="formData.accountName" placeholder="请输入账户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="转账流水号" prop="transactionNo">
              <el-input v-model="formData.transactionNo" placeholder="请输入转账流水号" />
            </el-form-item>
          </el-col>
        </el-row>
      </template>

      <!-- 支付凭证 -->
      <el-divider content-position="left">支付凭证</el-divider>
      <el-form-item label="支付凭证">
        <el-upload
          class="upload-demo"
          action="/api/upload"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :on-success="handleUploadSuccess"
          :before-upload="beforeUpload"
          :limit="5"
          :file-list="formData.vouchers"
          list-type="picture-card"
        >
          <el-icon><Plus /></el-icon>
          <div class="el-upload__text">上传凭证</div>
        </el-upload>
      </el-form-item>

      <!-- 备注信息 -->
      <el-divider content-position="left">备注信息</el-divider>
      <el-form-item label="备注">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="loading">
          {{ mode === 'edit' ? '更新' : '创建' }}
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  paymentData: {
    type: Object,
    default: () => ({})
  },
  mode: {
    type: String,
    default: 'create' // create, edit
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

// 表单引用
const formRef = ref()

// 表单数据
const formData = reactive({
  orderNo: '',
  customerName: '',
  paymentMethod: '',
  amount: 0,
  creditDays: 30,
  dueDate: '',
  installmentPeriods: 3,
  downPayment: 0,
  bankName: '',
  bankAccount: '',
  accountName: '',
  transactionNo: '',
  vouchers: [],
  remark: ''
})

// 分期计划
const installmentPlan = ref([])

// 加载状态
const loading = ref(false)

// 计算属性
const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const dialogTitle = computed(() => {
  return props.mode === 'edit' ? '编辑支付' : '新建支付'
})

// 表单验证规则
const formRules = {
  orderNo: [
    { required: true, message: '请输入订单编号', trigger: 'blur' }
  ],
  customerName: [
    { required: true, message: '请输入客户名称', trigger: 'blur' }
  ],
  paymentMethod: [
    { required: true, message: '请选择支付方式', trigger: 'change' }
  ],
  amount: [
    { required: true, message: '请输入支付金额', trigger: 'blur' },
    { type: 'number', min: 0.01, message: '支付金额必须大于0', trigger: 'blur' }
  ],
  creditDays: [
    { required: true, message: '请输入账期天数', trigger: 'blur' }
  ],
  dueDate: [
    { required: true, message: '请选择到期时间', trigger: 'change' }
  ],
  installmentPeriods: [
    { required: true, message: '请输入分期期数', trigger: 'blur' }
  ],
  downPayment: [
    { required: true, message: '请输入首付金额', trigger: 'blur' }
  ],
  bankName: [
    { required: true, message: '请输入银行名称', trigger: 'blur' }
  ],
  bankAccount: [
    { required: true, message: '请输入银行账号', trigger: 'blur' }
  ],
  accountName: [
    { required: true, message: '请输入账户名称', trigger: 'blur' }
  ],
  transactionNo: [
    { required: true, message: '请输入转账流水号', trigger: 'blur' }
  ]
}

// 监听支付方式变化，生成分期计划
watch(() => formData.paymentMethod, (newMethod) => {
  if (newMethod === 'installment' && formData.installmentPeriods > 0) {
    generateInstallmentPlan()
  }
})

watch(() => formData.installmentPeriods, () => {
  if (formData.paymentMethod === 'installment') {
    generateInstallmentPlan()
  }
})

watch(() => formData.amount, () => {
  if (formData.paymentMethod === 'installment') {
    generateInstallmentPlan()
  }
})

// 方法
const generateInstallmentPlan = () => {
  const periods = formData.installmentPeriods
  const totalAmount = formData.amount
  const downPayment = formData.downPayment || 0
  const remainingAmount = totalAmount - downPayment
  
  if (periods <= 0 || remainingAmount <= 0) return
  
  const perPeriodAmount = Math.round((remainingAmount / periods) * 100) / 100
  const plan = []
  
  for (let i = 0; i < periods; i++) {
    const dueDate = new Date()
    dueDate.setMonth(dueDate.getMonth() + i + 1)
    
    plan.push({
      period: i + 1,
      dueDate: dueDate.toISOString().split('T')[0],
      amount: i === periods - 1 ? remainingAmount - (perPeriodAmount * (periods - 1)) : perPeriodAmount,
      remark: `第${i + 1}期`
    })
  }
  
  installmentPlan.value = plan
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    try {
      // 构建提交数据
      const submitData = {
        ...formData,
        installmentPlan: formData.paymentMethod === 'installment' ? installmentPlan.value : null
      }
      
      // 这里调用API保存数据
      console.log('提交数据:', submitData)
      
      ElMessage.success(props.mode === 'edit' ? '更新成功' : '创建成功')
      emit('success')
      handleClose()
    } catch (error) {
      ElMessage.error(props.mode === 'edit' ? '更新失败' : '创建失败')
    } finally {
      loading.value = false
    }
  })
}

const handleClose = () => {
  visible.value = false
  resetForm()
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  
  // 重置表单数据
  Object.assign(formData, {
    orderNo: '',
    customerName: '',
    paymentMethod: '',
    amount: 0,
    creditDays: 30,
    dueDate: '',
    installmentPeriods: 3,
    downPayment: 0,
    bankName: '',
    bankAccount: '',
    accountName: '',
    transactionNo: '',
    vouchers: [],
    remark: ''
  })
  
  installmentPlan.value = []
}

const handleUploadSuccess = (response, file, fileList) => {
  formData.vouchers = fileList.map(item => ({
    name: item.name,
    url: item.response?.url || item.url
  }))
}

const handleRemove = (file, fileList) => {
  formData.vouchers = fileList.map(item => ({
    name: item.name,
    url: item.response?.url || item.url
  }))
}

const handlePreview = (file) => {
  window.open(file.url, '_blank')
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2
  
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 初始化数据
const initFormData = () => {
  if (props.paymentData && Object.keys(props.paymentData).length > 0) {
    Object.assign(formData, props.paymentData)
    
    // 如果有分期计划，设置分期计划
    if (props.paymentData.installmentPlan) {
      installmentPlan.value = props.paymentData.installmentPlan
    }
  }
}

// 监听对话框显示
watch(() => props.modelValue, (val) => {
  if (val) {
    nextTick(() => {
      initFormData()
    })
  }
})
</script>

<style lang="scss" scoped>
.payment-form {
  .el-divider {
    margin: 24px 0 16px 0;
    
    :deep(.el-divider__text) {
      background: var(--el-bg-color);
      padding: 0 16px;
      font-weight: bold;
    }
  }
}

.upload-demo {
  :deep(.el-upload--picture-card) {
    width: 100px;
    height: 100px;
  }
  
  :deep(.el-upload-list--picture-card .el-upload-list__item) {
    width: 100px;
    height: 100px;
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>