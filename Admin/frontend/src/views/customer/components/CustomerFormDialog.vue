<template>
  <el-dialog
    v-model="dialogVisible"
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
      v-loading="loading"
    >
      <!-- 基本信息 -->
      <el-divider content-position="left">基本信息</el-divider>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="客户名称" prop="customerName">
            <el-input v-model="formData.customerName" placeholder="请输入客户名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="客户类型" prop="customerType">
            <el-select v-model="formData.customerType" placeholder="请选择客户类型" style="width: 100%">
              <el-option label="个人客户" value="individual" />
              <el-option label="企业客户" value="enterprise" />
              <el-option label="经销商" value="distributor" />
              <el-option label="供应商" value="supplier" />
              <el-option label="合作伙伴" value="partner" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="formData.phone" placeholder="请输入手机号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="formData.email" placeholder="请输入邮箱" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="客户状态" prop="status">
            <el-select v-model="formData.status" placeholder="请选择客户状态" style="width: 100%">
              <el-option label="活跃" value="active" />
              <el-option label="非活跃" value="inactive" />
              <el-option label="暂停" value="suspended" />
              <el-option label="待审核" value="pending" />
              <el-option label="黑名单" value="blacklisted" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="客户等级" prop="level">
            <el-select v-model="formData.level" placeholder="请选择客户等级" style="width: 100%">
              <el-option label="普通客户" value="normal" />
              <el-option label="VIP客户" value="vip" />
              <el-option label="SVIP客户" value="svip" />
              <el-option label="钻石客户" value="diamond" />
              <el-option label="黑金客户" value="black_gold" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="客户来源" prop="source">
            <el-select v-model="formData.source" placeholder="请选择客户来源" style="width: 100%">
              <el-option label="官网" value="website" />
              <el-option label="微信" value="wechat" />
              <el-option label="支付宝" value="alipay" />
              <el-option label="淘宝" value="taobao" />
              <el-option label="京东" value="jd" />
              <el-option label="抖音" value="tiktok" />
              <el-option label="快手" value="kuaishou" />
              <el-option label="线下" value="offline" />
              <el-option label="推荐" value="referral" />
              <el-option label="其他" value="other" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="RFM分层" prop="rfmLevel">
            <el-select v-model="formData.rfmLevel" placeholder="请选择RFM分层" style="width: 100%">
              <el-option label="新客户" value="new" />
              <el-option label="活跃客户" value="active" />
              <el-option label="唤醒客户" value="wake" />
              <el-option label="睡眠客户" value="sleep" />
              <el-option label="流失客户" value="lost" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="地址" prop="address">
        <el-input v-model="formData.address" placeholder="请输入详细地址" />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="微信号" prop="wechatId">
            <el-input v-model="formData.wechatId" placeholder="请输入微信号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="客户标签" prop="tags">
            <el-select
              v-model="formData.tags"
              multiple
              filterable
              allow-create
              placeholder="请选择或输入标签"
              style="width: 100%"
            >
              <el-option label="重要客户" value="重要客户" />
              <el-option label="长期合作" value="长期合作" />
              <el-option label="高价值客户" value="高价值客户" />
              <el-option label="新客户" value="新客户" />
              <el-option label="潜力客户" value="潜力客户" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 企业信息（B2B） -->
      <template v-if="isEnterprise">
        <el-divider content-position="left">企业信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="公司名称" prop="companyName">
              <el-input v-model="formData.companyName" placeholder="请输入公司名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="企业类型" prop="enterpriseType">
              <el-select v-model="formData.enterpriseType" placeholder="请选择企业类型" style="width: 100%">
                <el-option label="国有企业" value="state_owned" />
                <el-option label="民营企业" value="private" />
                <el-option label="外资企业" value="foreign" />
                <el-option label="合资企业" value="joint_venture" />
                <el-option label="上市公司" value="listed" />
                <el-option label="初创企业" value="startup" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="营业执照" prop="businessLicense">
              <el-input v-model="formData.businessLicense" placeholder="请输入营业执照号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="税号" prop="taxNumber">
              <el-input v-model="formData.taxNumber" placeholder="请输入税号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="法人代表" prop="legalRepresentative">
              <el-input v-model="formData.legalRepresentative" placeholder="请输入法人代表" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="注册资本" prop="registeredCapital">
              <el-input-number
                v-model="formData.registeredCapital"
                :min="0"
                :precision="2"
                :step="10000"
                placeholder="请输入注册资本"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="成立日期" prop="establishedDate">
              <el-date-picker
                v-model="formData.establishedDate"
                type="date"
                placeholder="请选择成立日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="员工人数" prop="employeesCount">
              <el-input-number
                v-model="formData.employeesCount"
                :min="0"
                :step="10"
                placeholder="请输入员工人数"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="年营业额" prop="annualRevenue">
              <el-input-number
                v-model="formData.annualRevenue"
                :min="0"
                :precision="2"
                :step="10000"
                placeholder="请输入年营业额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="行业类型" prop="industry">
              <el-select v-model="formData.industry" placeholder="请选择行业类型" style="width: 100%">
                <el-option label="制造业" value="manufacturing" />
                <el-option label="贸易业" value="trading" />
                <el-option label="零售业" value="retail" />
                <el-option label="批发业" value="wholesale" />
                <el-option label="物流业" value="logistics" />
                <el-option label="金融业" value="finance" />
                <el-option label="房地产" value="real_estate" />
                <el-option label="科技业" value="technology" />
                <el-option label="医疗健康" value="healthcare" />
                <el-option label="教育培训" value="education" />
                <el-option label="餐饮食品" value="food_beverage" />
                <el-option label="纺织服装" value="textile" />
                <el-option label="汽车汽配" value="automotive" />
                <el-option label="建筑建材" value="construction" />
                <el-option label="农业农产" value="agriculture" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="公司地址" prop="companyAddress">
          <el-input v-model="formData.companyAddress" placeholder="请输入公司地址" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="公司电话" prop="companyPhone">
              <el-input v-model="formData.companyPhone" placeholder="请输入公司电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司邮箱" prop="companyEmail">
              <el-input v-model="formData.companyEmail" placeholder="请输入公司邮箱" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="公司网址" prop="companyWebsite">
          <el-input v-model="formData.companyWebsite" placeholder="请输入公司网址" />
        </el-form-item>
      </template>

      <!-- 信用信息 -->
      <template v-if="isEnterprise">
        <el-divider content-position="left">信用信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="信用额度" prop="creditLimit">
              <el-input-number
                v-model="formData.creditLimit"
                :min="0"
                :precision="2"
                :step="1000"
                placeholder="请输入信用额度"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账期天数" prop="paymentTerms">
              <el-input-number
                v-model="formData.paymentTerms"
                :min="0"
                :max="365"
                placeholder="请输入账期天数"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="付款方式" prop="paymentMethod">
          <el-radio-group v-model="formData.paymentMethod">
            <el-radio label="cash">现金支付</el-radio>
            <el-radio label="bank_transfer">银行转账</el-radio>
            <el-radio label="credit">账期支付</el-radio>
            <el-radio label="installment">分期付款</el-radio>
            <el-radio label="alipay">支付宝</el-radio>
            <el-radio label="wechat">微信支付</el-radio>
          </el-radio-group>
        </el-form-item>
      </template>

      <!-- 联系人信息 -->
      <template v-if="isEnterprise">
        <el-divider content-position="left">主要联系人</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人姓名" prop="contactName">
              <el-input v-model="formData.contactName" placeholder="请输入联系人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位" prop="contactPosition">
              <el-input v-model="formData.contactPosition" placeholder="请输入职位" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人手机" prop="contactPhone">
              <el-input v-model="formData.contactPhone" placeholder="请输入联系人手机" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人邮箱" prop="contactEmail">
              <el-input v-model="formData.contactEmail" placeholder="请输入联系人邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
      </template>

      <!-- 备注信息 -->
      <el-divider content-position="left">备注信息</el-divider>
      <el-form-item label="备注" prop="remark">
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
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { useCustomer } from '@/composables/useCustomer'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  type: {
    type: String,
    default: 'create' // create, edit
  },
  customer: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

const { createCustomer, updateCustomer } = useCustomer()

const loading = ref(false)
const formRef = ref()

// 表单数据
const formData = reactive({
  // 基本信息
  customerName: '',
  customerType: 'individual',
  phone: '',
  email: '',
  address: '',
  status: 'active',
  level: 'normal',
  source: '',
  rfmLevel: 'new',
  wechatId: '',
  tags: [],
  remark: '',
  
  // 企业信息
  companyName: '',
  enterpriseType: '',
  businessLicense: '',
  taxNumber: '',
  legalRepresentative: '',
  registeredCapital: 0,
  establishedDate: '',
  companyAddress: '',
  companyPhone: '',
  companyEmail: '',
  companyWebsite: '',
  employeesCount: 0,
  annualRevenue: 0,
  industry: '',
  
  // 信用信息
  creditLimit: 0,
  creditUsed: 0,
  paymentMethod: 'bank_transfer',
  paymentTerms: 30,
  
  // 联系人信息
  contactName: '',
  contactPosition: '',
  contactPhone: '',
  contactEmail: ''
})

// 计算属性（必须在表单规则之前定义）
const isEnterprise = computed(() => {
  return formData.customerType === 'enterprise' || 
         formData.customerType === 'distributor' || 
         formData.customerType === 'supplier'
})

// 表单验证规则
const formRules = {
  customerName: [
    { required: true, message: '请输入客户名称', trigger: 'blur' },
    { min: 2, max: 50, message: '客户名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  customerType: [
    { required: true, message: '请选择客户类型', trigger: 'change' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择客户状态', trigger: 'change' }
  ],
  level: [
    { required: true, message: '请选择客户等级', trigger: 'change' }
  ],
  companyName: [
    { required: isEnterprise.value, message: '请输入公司名称', trigger: 'blur' }
  ],
  businessLicense: [
    { required: isEnterprise.value, message: '请输入营业执照号', trigger: 'blur' }
  ],
  taxNumber: [
    { required: isEnterprise.value, message: '请输入税号', trigger: 'blur' }
  ],
  contactName: [
    { required: isEnterprise.value, message: '请输入联系人姓名', trigger: 'blur' }
  ],
  contactPhone: [
    { required: isEnterprise.value, pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  contactEmail: [
    { required: isEnterprise.value, type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const dialogTitle = computed(() => {
  return props.type === 'create' ? '新建客户' : '编辑客户'
})

// 监听客户类型变化，重置企业相关字段
watch(() => formData.customerType, (newType) => {
  if (!isEnterprise.value) {
    // 重置企业相关字段
    formData.companyName = ''
    formData.enterpriseType = ''
    formData.businessLicense = ''
    formData.taxNumber = ''
    formData.legalRepresentative = ''
    formData.registeredCapital = 0
    formData.establishedDate = ''
    formData.companyAddress = ''
    formData.companyPhone = ''
    formData.companyEmail = ''
    formData.companyWebsite = ''
    formData.employeesCount = 0
    formData.annualRevenue = 0
    formData.industry = ''
    formData.creditLimit = 0
    formData.creditUsed = 0
    formData.paymentMethod = 'bank_transfer'
    formData.paymentTerms = 30
    formData.contactName = ''
    formData.contactPosition = ''
    formData.contactPhone = ''
    formData.contactEmail = ''
  }
})

// 监听对话框显示状态
watch(() => props.modelValue, (visible) => {
  if (visible) {
    nextTick(() => {
      initForm()
    })
  }
})

// 初始化表单
const initForm = () => {
  if (props.type === 'edit' && props.customer) {
    // 编辑模式，填充现有数据
    Object.keys(formData).forEach(key => {
      if (props.customer[key] !== undefined) {
        formData[key] = props.customer[key]
      }
    })
  } else {
    // 创建模式，重置表单
    resetForm()
  }
}

// 重置表单
const resetForm = () => {
  Object.keys(formData).forEach(key => {
    if (key === 'tags') {
      formData[key] = []
    } else if (key === 'customerType') {
      formData[key] = 'individual'
    } else if (key === 'status') {
      formData[key] = 'active'
    } else if (key === 'level') {
      formData[key] = 'normal'
    } else if (key === 'rfmLevel') {
      formData[key] = 'new'
    } else if (key === 'paymentMethod') {
      formData[key] = 'bank_transfer'
    } else if (key === 'paymentTerms') {
      formData[key] = 30
    } else if (typeof formData[key] === 'number') {
      formData[key] = 0
    } else {
      formData[key] = ''
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    // 构建提交数据
    const submitData = { ...formData }
    
    // 根据类型调用不同的API
    if (props.type === 'create') {
      await createCustomer(submitData)
    } else {
      await updateCustomer(props.customer.id, submitData)
    }

    ElMessage.success(props.type === 'create' ? '客户创建成功' : '客户更新成功')
    emit('success')
    handleClose()
  } catch (error) {
    if (error !== false) {
      console.error('表单提交失败:', error)
      ElMessage.error('表单提交失败，请检查输入内容')
    }
  } finally {
    loading.value = false
  }
}

// 关闭对话框
const handleClose = () => {
  dialogVisible.value = false
  resetForm()
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

:deep(.el-divider__text) {
  background-color: var(--el-bg-color);
  color: var(--el-text-color-primary);
  font-weight: 600;
}

:deep(.el-form-item) {
  margin-bottom: 18px;
}

:deep(.el-input-number) {
  width: 100%;
}
</style>