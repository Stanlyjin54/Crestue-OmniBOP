<template>
  <div class="supplier-form-dialog">
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑供应商' : '新增供应商'"
      width="800px"
      :before-close="handleClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        class="supplier-form"
      >
        <!-- 基本信息 -->
        <el-divider content-position="left">
          <el-icon><OfficeBuilding /></el-icon>
          基本信息
        </el-divider>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供应商名称" prop="name">
              <el-input
                v-model="formData.name"
                placeholder="请输入供应商名称"
                :disabled="isEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商代码" prop="code">
              <el-input
                v-model="formData.code"
                placeholder="请输入供应商代码"
                :disabled="isEdit"
              >
                <template #append>
                  <el-button @click="generateCode" :disabled="isEdit">
                    生成
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供应商类型" prop="type">
              <el-select v-model="formData.type" placeholder="请选择供应商类型">
                <el-option
                  v-for="option in supplierTypes"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商等级" prop="level">
              <el-select v-model="formData.level" placeholder="请选择供应商等级">
                <el-option
                  v-for="option in supplierLevels"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="成立时间" prop="establishedDate">
              <el-date-picker
                v-model="formData.establishedDate"
                type="date"
                placeholder="请选择成立时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="注册资本" prop="registeredCapital">
              <el-input
                v-model="formData.registeredCapital"
                placeholder="请输入注册资本"
              >
                <template #append>万元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="经营范围" prop="businessScope">
          <el-input
            v-model="formData.businessScope"
            type="textarea"
            :rows="3"
            placeholder="请输入经营范围"
          />
        </el-form-item>

        <el-form-item label="公司简介" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="4"
            placeholder="请输入公司简介"
          />
        </el-form-item>

        <!-- 联系方式 -->
        <el-divider content-position="left">
          <el-icon><Phone /></el-icon>
          联系方式
        </el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input
                v-model="formData.contactPerson"
                placeholder="请输入联系人姓名"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位" prop="position">
              <el-input
                v-model="formData.position"
                placeholder="请输入联系人职位"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input
                v-model="formData.phone"
                placeholder="请输入联系电话"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电子邮箱" prop="email">
              <el-input
                v-model="formData.email"
                placeholder="请输入电子邮箱"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="传真" prop="fax">
              <el-input
                v-model="formData.fax"
                placeholder="请输入传真号码"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="网站" prop="website">
              <el-input
                v-model="formData.website"
                placeholder="请输入公司网站"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="详细地址" prop="address">
          <el-input
            v-model="formData.address"
            placeholder="请输入详细地址"
          />
        </el-form-item>

        <!-- 资质信息 -->
        <el-divider content-position="left">
          <el-icon><Document /></el-icon>
          资质信息
        </el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="营业执照号" prop="businessLicense">
              <el-input
                v-model="formData.businessLicense"
                placeholder="请输入营业执照号"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="税务登记号" prop="taxNumber">
              <el-input
                v-model="formData.taxNumber"
                placeholder="请输入税务登记号"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="组织机构代码" prop="organizationCode">
              <el-input
                v-model="formData.organizationCode"
                placeholder="请输入组织机构代码"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开户银行" prop="bankName">
              <el-input
                v-model="formData.bankName"
                placeholder="请输入开户银行"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="银行账号" prop="bankAccount">
              <el-input
                v-model="formData.bankAccount"
                placeholder="请输入银行账号"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="信用等级" prop="creditRating">
              <el-select v-model="formData.creditRating" placeholder="请选择信用等级">
                <el-option
                  v-for="option in creditRatings"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 业务信息 -->
        <el-divider content-position="left">
          <el-icon><ShoppingCart /></el-icon>
          业务信息
        </el-divider>

        <el-form-item label="供应产品类别" prop="productCategories">
          <el-select
            v-model="formData.productCategories"
            multiple
            placeholder="请选择供应产品类别"
            style="width: 100%"
          >
            <el-option
              v-for="option in productCategories"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-form-item>

        <el-row :gutter="20">
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
          <el-col :span="12">
            <el-form-item label="最小订单金额" prop="minOrderAmount">
              <el-input
                v-model="formData.minOrderAmount"
                placeholder="请输入最小订单金额"
              >
                <template #append>元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="notes">
          <el-input
            v-model="formData.notes"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleClose">取消</el-button>
          <el-button @click="handlePreview">预览</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            {{ isEdit ? '更新' : '创建' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog
      v-model="previewVisible"
      title="供应商信息预览"
      width="600px"
    >
      <div class="supplier-preview">
        <div class="preview-section">
          <h4>基本信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="供应商名称">{{ formData.name }}</el-descriptions-item>
            <el-descriptions-item label="供应商代码">{{ formData.code }}</el-descriptions-item>
            <el-descriptions-item label="供应商类型">{{ getTypeLabel(formData.type) }}</el-descriptions-item>
            <el-descriptions-item label="供应商等级">{{ getLevelLabel(formData.level) }}</el-descriptions-item>
            <el-descriptions-item label="成立时间">{{ formatDate(formData.establishedDate) }}</el-descriptions-item>
            <el-descriptions-item label="注册资本">{{ formData.registeredCapital }}万元</el-descriptions-item>
            <el-descriptions-item label="经营范围" :span="2">{{ formData.businessScope }}</el-descriptions-item>
            <el-descriptions-item label="公司简介" :span="2">{{ formData.description }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="preview-section">
          <h4>联系方式</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="联系人">{{ formData.contactPerson }}</el-descriptions-item>
            <el-descriptions-item label="职位">{{ formData.position }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ formData.phone }}</el-descriptions-item>
            <el-descriptions-item label="电子邮箱">{{ formData.email }}</el-descriptions-item>
            <el-descriptions-item label="传真">{{ formData.fax }}</el-descriptions-item>
            <el-descriptions-item label="网站">{{ formData.website }}</el-descriptions-item>
            <el-descriptions-item label="详细地址" :span="2">{{ formData.address }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="preview-section">
          <h4>资质信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="营业执照号">{{ formData.businessLicense }}</el-descriptions-item>
            <el-descriptions-item label="税务登记号">{{ formData.taxNumber }}</el-descriptions-item>
            <el-descriptions-item label="组织机构代码">{{ formData.organizationCode }}</el-descriptions-item>
            <el-descriptions-item label="信用等级">{{ getCreditRatingLabel(formData.creditRating) }}</el-descriptions-item>
            <el-descriptions-item label="开户银行">{{ formData.bankName }}</el-descriptions-item>
            <el-descriptions-item label="银行账号">{{ formData.bankAccount }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="preview-section">
          <h4>业务信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="供应产品类别" :span="2">
              <el-tag
                v-for="category in formData.productCategories"
                :key="category"
                class="category-tag"
              >
                {{ getCategoryLabel(category) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="账期天数">{{ formData.paymentTerms }}天</el-descriptions-item>
            <el-descriptions-item label="最小订单金额">{{ formData.minOrderAmount }}元</el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ formData.notes }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { OfficeBuilding, Phone, Document, ShoppingCart } from '@element-plus/icons-vue'
import { useProcurementAdvanced } from '@/composables/useProcurementAdvanced'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  supplier: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:visible', 'success'])

// 组合式函数
const {
  createSupplier,
  updateSupplier
} = useProcurementAdvanced()

// 响应式数据
const formRef = ref()
const dialogVisible = computed({
  get: () => props.visible,
  set: (val) => emit('update:visible', val)
})

const formData = reactive({
  name: '',
  code: '',
  type: '',
  level: '',
  establishedDate: '',
  registeredCapital: '',
  businessScope: '',
  description: '',
  contactPerson: '',
  position: '',
  phone: '',
  email: '',
  fax: '',
  website: '',
  address: '',
  businessLicense: '',
  taxNumber: '',
  organizationCode: '',
  bankName: '',
  bankAccount: '',
  creditRating: '',
  productCategories: [],
  paymentTerms: 30,
  minOrderAmount: '',
  notes: ''
})

const submitting = ref(false)
const previewVisible = ref(false)

// 计算属性
const isEdit = computed(() => !!props.supplier)

// 配置数据
const supplierTypes = [
  { label: '制造商', value: 'manufacturer' },
  { label: '经销商', value: 'distributor' },
  { label: '代理商', value: 'agent' },
  { label: '批发商', value: 'wholesaler' },
  { label: '零售商', value: 'retailer' },
  { label: '服务商', value: 'service_provider' }
]

const supplierLevels = [
  { label: '战略合作伙伴', value: 'strategic' },
  { label: '核心供应商', value: 'core' },
  { label: '重要供应商', value: 'important' },
  { label: '普通供应商', value: 'normal' }
]

const creditRatings = [
  { label: 'AAA', value: 'AAA' },
  { label: 'AA', value: 'AA' },
  { label: 'A', value: 'A' },
  { label: 'BBB', value: 'BBB' },
  { label: 'BB', value: 'BB' },
  { label: 'B', value: 'B' }
]

const productCategories = [
  { label: '电子元器件', value: 'electronics' },
  { label: '机械配件', value: 'mechanical' },
  { label: '化工原料', value: 'chemical' },
  { label: '纺织品', value: 'textile' },
  { label: '食品原料', value: 'food' },
  { label: '包装材料', value: 'packaging' },
  { label: '办公用品', value: 'office' },
  { label: '服务类', value: 'service' }
]

// 表单验证规则
const formRules = {
  name: [
    { required: true, message: '请输入供应商名称', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入供应商代码', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择供应商类型', trigger: 'change' }
  ],
  level: [
    { required: true, message: '请选择供应商等级', trigger: 'change' }
  ],
  contactPerson: [
    { required: true, message: '请输入联系人', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入电子邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

// 监听供应商数据变化
watch(() => props.supplier, (newSupplier) => {
  if (newSupplier) {
    Object.assign(formData, newSupplier)
  } else {
    resetForm()
  }
}, { immediate: true })

// 工具方法
const generateCode = () => {
  const prefix = 'SUP'
  const timestamp = Date.now().toString().slice(-6)
  formData.code = `${prefix}${timestamp}`
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

const getTypeLabel = (value) => {
  const item = supplierTypes.find(item => item.value === value)
  return item ? item.label : '-'
}

const getLevelLabel = (value) => {
  const item = supplierLevels.find(item => item.value === value)
  return item ? item.label : '-'
}

const getCreditRatingLabel = (value) => {
  const item = creditRatings.find(item => item.value === value)
  return item ? item.label : '-'
}

const getCategoryLabel = (value) => {
  const item = productCategories.find(item => item.value === value)
  return item ? item.label : '-'
}

const resetForm = () => {
  Object.assign(formData, {
    name: '',
    code: '',
    type: '',
    level: '',
    establishedDate: '',
    registeredCapital: '',
    businessScope: '',
    description: '',
    contactPerson: '',
    position: '',
    phone: '',
    email: '',
    fax: '',
    website: '',
    address: '',
    businessLicense: '',
    taxNumber: '',
    organizationCode: '',
    bankName: '',
    bankAccount: '',
    creditRating: '',
    productCategories: [],
    paymentTerms: 30,
    minOrderAmount: '',
    notes: ''
  })
  formRef.value?.clearValidate()
}

// 事件处理
const handleClose = () => {
  dialogVisible.value = false
  resetForm()
}

const handlePreview = () => {
  previewVisible.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true

    const supplierData = { ...formData }
    
    if (isEdit.value) {
      await updateSupplier(props.supplier.id, supplierData)
      ElMessage.success('供应商信息更新成功')
    } else {
      await createSupplier(supplierData)
      ElMessage.success('供应商创建成功')
    }

    emit('success')
    handleClose()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败，请重试')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.supplier-form-dialog {
  .supplier-form {
    max-height: 600px;
    overflow-y: auto;
    padding-right: 10px;
  }

  .dialog-footer {
    text-align: right;
  }

  .preview-section {
    margin-bottom: 24px;

    h4 {
      margin-bottom: 16px;
      color: #303133;
      font-weight: 600;
    }

    .category-tag {
      margin-right: 8px;
      margin-bottom: 4px;
    }
  }
}

:deep(.el-dialog__body) {
  padding: 20px 30px;
}

:deep(.el-divider__text) {
  color: #606266;
  font-weight: 600;
}

:deep(.el-select) {
  width: 100%;
}

:deep(.el-input-number) {
  width: 100%;
}
</style>