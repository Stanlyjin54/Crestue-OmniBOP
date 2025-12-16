<template>
  <div class="customer-edit-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">{{ isEdit ? '编辑客户' : '新建客户' }}</h1>
          <p class="page-subtitle">{{ isEdit ? '编辑客户档案信息' : '创建新的客户档案' }}</p>
        </div>
        <div class="header-actions">
          <el-button @click="handleBack">
            <el-icon><ArrowLeft /></el-icon>
            返回
          </el-button>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            <el-icon><Check /></el-icon>
            保存
          </el-button>
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/customer' }">客户管理</el-breadcrumb-item>
      <el-breadcrumb-item>{{ isEdit ? '编辑客户' : '新建客户' }}</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 表单区域 -->
    <div class="form-section">
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        class="customer-form"
      >
        <!-- 基本信息 -->
        <el-card class="form-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">基本信息</span>
              <span class="card-subtitle">客户的基本档案信息</span>
            </div>
          </template>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="客户名称" prop="customerName">
                <el-input
                  v-model="formData.customerName"
                  placeholder="请输入客户名称"
                  maxlength="50"
                  show-word-limit
                />
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
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="手机号" prop="phone">
                <el-input
                  v-model="formData.phone"
                  placeholder="请输入手机号"
                  maxlength="11"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="邮箱" prop="email">
                <el-input
                  v-model="formData.email"
                  placeholder="请输入邮箱地址"
                  maxlength="100"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="微信号" prop="wechatId">
                <el-input
                  v-model="formData.wechatId"
                  placeholder="请输入微信号"
                  maxlength="50"
                />
              </el-form-item>
            </el-col>
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
          </el-row>

          <el-form-item label="详细地址" prop="address">
            <el-input
              v-model="formData.address"
              type="textarea"
              :rows="2"
              placeholder="请输入详细地址"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>

          <el-form-item label="客户标签" prop="tags">
            <el-tag
              v-for="tag in formData.tags"
              :key="tag"
              closable
              @close="removeTag(tag)"
              class="tag-item"
            >
              {{ tag }}
            </el-tag>
            <el-input
              v-if="inputVisible"
              ref="tagInputRef"
              v-model="inputValue"
              class="tag-input"
              size="small"
              @keyup.enter="handleInputConfirm"
              @blur="handleInputConfirm"
            />
            <el-button v-else class="button-new-tag" size="small" @click="showInput">
              <el-icon><Plus /></el-icon>
              添加标签
            </el-button>
          </el-form-item>

          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              type="textarea"
              :rows="3"
              placeholder="请输入备注信息"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>
        </el-card>

        <!-- 企业信息（B2B） -->
        <el-card class="form-card" v-if="isEnterprise">
          <template #header>
            <div class="card-header">
              <span class="card-title">企业信息</span>
              <span class="card-subtitle">企业客户专属信息</span>
            </div>
          </template>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="公司名称" prop="companyName">
                <el-input
                  v-model="formData.companyName"
                  placeholder="请输入公司名称"
                  maxlength="100"
                  show-word-limit
                />
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
                <el-input
                  v-model="formData.businessLicense"
                  placeholder="请输入营业执照号码"
                  maxlength="50"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="税号" prop="taxNumber">
                <el-input
                  v-model="formData.taxNumber"
                  placeholder="请输入税号"
                  maxlength="50"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="法人代表" prop="legalRepresentative">
                <el-input
                  v-model="formData.legalRepresentative"
                  placeholder="请输入法人代表姓名"
                  maxlength="20"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="注册资本" prop="registeredCapital">
                <el-input-number
                  v-model="formData.registeredCapital"
                  :min="0"
                  :max="100000000"
                  :precision="0"
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
                  :max="1000000"
                  :precision="0"
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
                  :max="10000000000"
                  :precision="0"
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
            <el-input
              v-model="formData.companyAddress"
              type="textarea"
              :rows="2"
              placeholder="请输入公司详细地址"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="公司电话" prop="companyPhone">
                <el-input
                  v-model="formData.companyPhone"
                  placeholder="请输入公司电话"
                  maxlength="20"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="公司邮箱" prop="companyEmail">
                <el-input
                  v-model="formData.companyEmail"
                  placeholder="请输入公司邮箱"
                  maxlength="100"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="公司网址" prop="companyWebsite">
            <el-input
              v-model="formData.companyWebsite"
              placeholder="请输入公司网址，如：https://www.example.com"
              maxlength="200"
            />
          </el-form-item>
        </el-card>

        <!-- 信用信息 -->
        <el-card class="form-card" v-if="isEnterprise">
          <template #header>
            <div class="card-header">
              <span class="card-title">信用信息</span>
              <span class="card-subtitle">企业客户信用相关信息</span>
            </div>
          </template>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="信用额度" prop="creditLimit">
                <el-input-number
                  v-model="formData.creditLimit"
                  :min="0"
                  :max="100000000"
                  :precision="2"
                  :step="1000"
                  placeholder="请输入信用额度"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="付款方式" prop="paymentMethod">
                <el-select v-model="formData.paymentMethod" placeholder="请选择付款方式" style="width: 100%">
                  <el-option label="现金支付" value="cash" />
                  <el-option label="银行转账" value="bank_transfer" />
                  <el-option label="账期支付" value="credit" />
                  <el-option label="分期付款" value="installment" />
                  <el-option label="支付宝" value="alipay" />
                  <el-option label="微信支付" value="wechat" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="账期天数" prop="paymentTerms">
            <el-input-number
              v-model="formData.paymentTerms"
              :min="0"
              :max="365"
              :precision="0"
              placeholder="请输入账期天数"
              style="width: 100%"
            />
          </el-form-item>
        </el-card>

        <!-- 联系人信息 -->
        <el-card class="form-card" v-if="isEnterprise">
          <template #header>
            <div class="card-header">
              <span class="card-title">联系人信息</span>
              <span class="card-subtitle">企业客户联系人信息</span>
            </div>
          </template>
          
          <div class="contacts-section">
            <div class="contacts-header">
              <span>联系人列表</span>
              <el-button type="primary" size="small" @click="addContact">
                <el-icon><Plus /></el-icon>
                添加联系人
              </el-button>
            </div>
            
            <el-table :data="formData.contacts" style="width: 100%">
              <el-table-column prop="name" label="姓名" width="120">
                <template #default="scope">
                  <el-input v-model="scope.row.name" placeholder="姓名" />
                </template>
              </el-table-column>
              <el-table-column prop="position" label="职位" width="120">
                <template #default="scope">
                  <el-input v-model="scope.row.position" placeholder="职位" />
                </template>
              </el-table-column>
              <el-table-column prop="phone" label="手机" width="150">
                <template #default="scope">
                  <el-input v-model="scope.row.phone" placeholder="手机号" />
                </template>
              </el-table-column>
              <el-table-column prop="email" label="邮箱" min-width="150">
                <template #default="scope">
                  <el-input v-model="scope.row.email" placeholder="邮箱" />
                </template>
              </el-table-column>
              <el-table-column prop="isPrimary" label="主要联系人" width="100" align="center">
                <template #default="scope">
                  <el-checkbox v-model="scope.row.isPrimary" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="80" fixed="right">
                <template #default="scope">
                  <el-button type="danger" link @click="removeContact(scope.$index)">
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, nextTick, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useCustomer } from '@/composables/useCustomer'

const route = useRoute()
const router = useRouter()
const { loading, getCustomerDetail, createCustomer, updateCustomer } = useCustomer()

const formRef = ref()
const tagInputRef = ref()

// 标签输入相关
const inputVisible = ref(false)
const inputValue = ref('')

// 表单数据
const formData = reactive({
  customerName: '',
  customerType: 'individual',
  level: 'normal',
  status: 'active',
  phone: '',
  email: '',
  wechatId: '',
  source: '',
  address: '',
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
  employeesCount: 0,
  annualRevenue: 0,
  industry: '',
  companyAddress: '',
  companyPhone: '',
  companyEmail: '',
  companyWebsite: '',
  // 信用信息
  creditLimit: 0,
  paymentMethod: '',
  paymentTerms: 0,
  // 联系人
  contacts: []
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
  level: [
    { required: true, message: '请选择客户等级', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择客户状态', trigger: 'change' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  companyName: [
    { required: isEnterprise.value, message: '请输入公司名称', trigger: 'blur' }
  ],
  businessLicense: [
    { required: isEnterprise.value, message: '请输入营业执照号码', trigger: 'blur' }
  ],
  taxNumber: [
    { required: isEnterprise.value, message: '请输入税号', trigger: 'blur' }
  ],
  companyEmail: [
    { type: 'email', message: '请输入正确的公司邮箱地址', trigger: 'blur' }
  ],
  companyWebsite: [
    { type: 'url', message: '请输入正确的网址格式', trigger: 'blur' }
  ]
}

// 计算属性
const isEdit = computed(() => !!route.params.id)
const isEnterprise = computed(() => {
  return formData.customerType === 'enterprise' || 
         formData.customerType === 'distributor' || 
         formData.customerType === 'supplier'
})

// 标签相关方法
const removeTag = (tag) => {
  const index = formData.tags.indexOf(tag)
  if (index > -1) {
    formData.tags.splice(index, 1)
  }
}

const showInput = () => {
  inputVisible.value = true
  nextTick(() => {
    tagInputRef.value?.focus()
  })
}

const handleInputConfirm = () => {
  if (inputValue.value) {
    formData.tags.push(inputValue.value)
  }
  inputVisible.value = false
  inputValue.value = ''
}

// 联系人相关方法
const addContact = () => {
  formData.contacts.push({
    name: '',
    position: '',
    phone: '',
    email: '',
    isPrimary: false
  })
}

const removeContact = (index) => {
  formData.contacts.splice(index, 1)
}

// 返回
const handleBack = () => {
  router.push('/customer')
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    // 验证联系人数据
    if (isEnterprise.value && formData.contacts.length > 0) {
      const invalidContacts = formData.contacts.filter(contact => 
        !contact.name || !contact.phone
      )
      if (invalidContacts.length > 0) {
        ElMessage.error('请完善联系人信息，姓名和手机号不能为空')
        return
      }
    }
    
    const submitData = { ...formData }
    
    if (isEdit.value) {
      await updateCustomer(route.params.id, submitData)
      ElMessage.success('客户信息更新成功')
    } else {
      await createCustomer(submitData)
      ElMessage.success('客户创建成功')
    }
    
    router.push('/customer')
  } catch (error) {
    if (error === false) {
      // 表单验证失败
      ElMessage.error('请检查表单填写是否正确')
    } else {
      console.error('提交失败:', error)
      ElMessage.error(isEdit.value ? '客户信息更新失败' : '客户创建失败')
    }
  }
}

// 加载客户详情（编辑模式）
const loadCustomerDetail = async () => {
  if (!isEdit.value) return
  
  try {
    const data = await getCustomerDetail(route.params.id)
    // 填充表单数据
    Object.keys(formData).forEach(key => {
      if (data[key] !== undefined) {
        if (key === 'tags' && Array.isArray(data[key])) {
          formData[key] = [...data[key]]
        } else if (key === 'contacts' && Array.isArray(data[key])) {
          formData[key] = [...data[key]]
        } else {
          formData[key] = data[key]
        }
      }
    })
  } catch (error) {
    ElMessage.error('加载客户详情失败')
    router.push('/customer')
  }
}

// 初始化
onMounted(() => {
  loadCustomerDetail()
})
</script>

<style lang="scss" scoped>
.customer-edit-page {
  padding: 20px;
  background-color: var(--el-bg-color-page);
  min-height: calc(100vh - 84px);
}

// 页面头部
.page-header {
  background: linear-gradient(135deg, #5D5FEF 0%, #4F52CC 100%);
  border-radius: 12px;
  margin-bottom: 20px;
  overflow: hidden;

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 30px;
    height: 100px;

    .title-section {
      .page-title {
        font-size: 24px;
        font-weight: bold;
        color: #FFFFFF;
        margin: 0 0 4px 0;
      }
      
      .page-subtitle {
        font-size: 14px;
        color: #E6E6FF;
        margin: 0;
        opacity: 0.9;
      }
    }

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }
}

// 面包屑
.breadcrumb {
  margin-bottom: 20px;
}

// 表单区域
.form-section {
  .form-card {
    margin-bottom: 20px;
    border-radius: 8px;

    .card-header {
      display: flex;
      flex-direction: column;
      gap: 4px;

      .card-title {
        font-size: 16px;
        font-weight: bold;
        color: var(--el-text-color-primary);
      }

      .card-subtitle {
        font-size: 12px;
        color: var(--el-text-color-secondary);
      }
    }
  }

  .customer-form {
    .el-form-item {
      margin-bottom: 20px;
    }

    .tag-item {
      margin-right: 8px;
      margin-bottom: 8px;
    }

    .tag-input {
      width: 90px;
      margin-right: 8px;
      vertical-align: bottom;
    }

    .button-new-tag {
      height: 24px;
      padding: 0 8px;
      margin-bottom: 8px;
    }

    // 联系人区域
    .contacts-section {
      .contacts-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 16px;
        padding-bottom: 12px;
        border-bottom: 1px solid var(--el-border-color-lighter);

        span {
          font-size: 14px;
          font-weight: 500;
          color: var(--el-text-color-primary);
        }
      }

      .el-table {
        .el-input {
          width: 100%;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .customer-edit-page {
    padding: 10px;
  }

  .page-header {
    .header-content {
      flex-direction: column;
      gap: 16px;
      height: auto;
      padding: 20px;

      .header-actions {
        width: 100%;
        justify-content: center;
      }
    }
  }

  .form-section {
    .customer-form {
      .el-row {
        .el-col {
          margin-bottom: 0;
        }
      }
    }
  }
}

// 深色主题适配
:deep(.dark) {
  .page-header {
    background: linear-gradient(135deg, #4a4cb8 0%, #3d4099 100%);
  }
}
</style>