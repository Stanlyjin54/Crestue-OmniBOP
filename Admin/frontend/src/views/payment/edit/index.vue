<template>
  <base-wrapper>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">编辑支付</h1>
          <p class="page-description">编辑支付记录信息</p>
        </div>
        <div class="header-right">
          <el-button @click="goBack">
            <el-icon><ArrowLeft /></el-icon>返回
          </el-button>
          <el-button type="primary" @click="handleSave" :loading="loading">
            <el-icon><Check /></el-icon>保存
          </el-button>
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/payment' }">支付管理</el-breadcrumb-item>
      <el-breadcrumb-item>编辑支付</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 编辑表单 -->
    <el-card class="edit-card">
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
            <el-form-item label="支付编号" prop="paymentNo">
              <el-input v-model="formData.paymentNo" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单编号" prop="orderNo">
              <el-input v-model="formData.orderNo" placeholder="请输入订单编号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="formData.customerName" placeholder="请输入客户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="支付状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择支付状态" style="width: 100%">
                <el-option label="待支付" value="pending" />
                <el-option label="已支付" value="paid" />
                <el-option label="部分支付" value="partial" />
                <el-option label="已退款" value="refunded" />
                <el-option label="已取消" value="cancelled" />
              </el-select>
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

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="支付时间" prop="paymentDate">
              <el-date-picker
                v-model="formData.paymentDate"
                type="datetime"
                placeholder="请选择支付时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期时间" prop="dueDate">
              <el-date-picker
                v-model="formData.dueDate"
                type="date"
                placeholder="请选择到期时间"
                style="width: 100%"
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
              <div class="installment-actions">
                <el-button type="primary" size="small" @click="generateInstallmentPlan">
                  <el-icon><Refresh /></el-icon>重新生成计划
                </el-button>
                <el-button size="small" @click="addInstallment">
                  <el-icon><Plus /></el-icon>添加期数
                </el-button>
              </div>
              <el-table :data="installmentPlan" border style="width: 100%" class="installment-table">
                <el-table-column type="index" label="期数" width="80" />
                <el-table-column label="应还日期" width="180">
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
                <el-table-column label="状态" width="120">
                  <template #default="{ row }">
                    <el-select v-model="row.status" placeholder="状态" style="width: 100%">
                      <el-option label="待还款" value="pending" />
                      <el-option label="已还款" value="paid" />
                      <el-option label="逾期" value="overdue" />
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column label="实还日期" width="180">
                  <template #default="{ row }">
                    <el-date-picker
                      v-model="row.paidDate"
                      type="date"
                      placeholder="选择日期"
                      style="width: 100%"
                      clearable
                    />
                  </template>
                </el-table-column>
                <el-table-column label="备注">
                  <template #default="{ row }">
                    <el-input v-model="row.remark" placeholder="备注信息" />
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="80" fixed="right">
                  <template #default="{ $index }">
                    <el-button type="danger" link @click="removeInstallment($index)">
                      <el-icon><Delete /></el-icon>
                    </el-button>
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
            :rows="4"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
    </el-card>
  </base-wrapper>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft, Check, Plus, Delete, Refresh } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

// 表单引用
const formRef = ref()

// 加载状态
const loading = ref(false)

// 表单数据
const formData = reactive({
  id: '',
  paymentNo: '',
  orderNo: '',
  customerName: '',
  paymentMethod: '',
  amount: 0,
  status: 'pending',
  paymentDate: '',
  dueDate: '',
  creditDays: 30,
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
  status: [
    { required: true, message: '请选择支付状态', trigger: 'change' }
  ],
  paymentDate: [
    { required: true, message: '请选择支付时间', trigger: 'change' }
  ],
  creditDays: [
    { required: true, message: '请输入账期天数', trigger: 'blur' }
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

// 方法
const loadPaymentData = async () => {
  const paymentId = route.params.id
  if (!paymentId) {
    ElMessage.error('支付ID不存在')
    return
  }
  
  loading.value = true
  try {
    // 这里调用API获取支付详情
    console.log('加载支付详情:', paymentId)
    
    // 模拟数据加载
    setTimeout(() => {
      // 更新表单数据
      Object.assign(formData, {
        id: paymentId,
        paymentNo: 'PAY202312010001',
        orderNo: 'ORD202312010001',
        customerName: '某某公司',
        paymentMethod: 'credit',
        amount: 50000,
        status: 'pending',
        paymentDate: '2023-12-01 10:30:00',
        dueDate: '2024-01-01',
        creditDays: 30,
        installmentPeriods: 3,
        downPayment: 0,
        bankName: '',
        bankAccount: '',
        accountName: '',
        transactionNo: '',
        vouchers: [
          { name: '支付凭证1.jpg', url: 'https://via.placeholder.com/300x200' }
        ],
        remark: '这是一笔账期支付，请在到期前完成付款。'
      })
      
      // 如果有分期计划，设置分期计划
      if (formData.paymentMethod === 'installment') {
        generateInstallmentPlan()
      }
    }, 500)
  } catch (error) {
    ElMessage.error('加载支付数据失败')
  } finally {
    loading.value = false
  }
}

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
      status: 'pending',
      paidDate: '',
      remark: `第${i + 1}期`
    })
  }
  
  installmentPlan.value = plan
}

const addInstallment = () => {
  const lastPeriod = installmentPlan.value[installmentPlan.value.length - 1]
  const newDueDate = new Date(lastPeriod.dueDate)
  newDueDate.setMonth(newDueDate.getMonth() + 1)
  
  installmentPlan.value.push({
    period: installmentPlan.value.length + 1,
    dueDate: newDueDate.toISOString().split('T')[0],
    amount: 0,
    status: 'pending',
    paidDate: '',
    remark: `第${installmentPlan.value.length + 1}期`
  })
}

const removeInstallment = (index) => {
  ElMessageBox.confirm('确定要删除该分期吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    installmentPlan.value.splice(index, 1)
    // 重新排序期数
    installmentPlan.value.forEach((item, i) => {
      item.period = i + 1
      item.remark = `第${i + 1}期`
    })
  })
}

const handleSave = async () => {
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
      console.log('更新支付数据:', submitData)
      
      ElMessage.success('保存成功')
      router.push(`/payment/detail/${formData.id}`)
    } catch (error) {
      ElMessage.error('保存失败')
    } finally {
      loading.value = false
    }
  })
}

const goBack = () => {
  router.push('/payment')
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

// 监听支付方式变化
watch(() => formData.paymentMethod, (newMethod) => {
  if (newMethod === 'installment' && formData.installmentPeriods > 0) {
    generateInstallmentPlan()
  }
})

// 生命周期
onMounted(() => {
  loadPaymentData()
})
</script>

<style lang="scss" scoped>
.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  
  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .page-title {
    font-size: 24px;
    font-weight: bold;
    color: #fff;
    margin: 0 0 8px 0;
  }
  
  .page-description {
    font-size: 14px;
    color: rgba(255, 255, 255, 0.8);
    margin: 0;
  }
}

.breadcrumb {
  margin-bottom: 20px;
}

.edit-card {
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
  
  .installment-actions {
    margin-bottom: 16px;
    display: flex;
    gap: 12px;
  }
  
  .installment-table {
    :deep(.el-table__cell) {
      padding: 8px 0;
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
}

// 深色主题适配
:deep(.dark) {
  .page-header {
    background: linear-gradient(135deg, #434343 0%, #000000 100%);
  }
}
</style>