<template>
  <div class="contract-form-dialog">
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑合同' : '新增合同'"
      width="900px"
      :before-close="handleClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        class="contract-form"
      >
        <!-- 基本信息 -->
        <el-divider content-position="left">
          <el-icon><Document /></el-icon>
          基本信息
        </el-divider>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合同编号" prop="contractNumber">
              <el-input
                v-model="formData.contractNumber"
                placeholder="请输入合同编号"
                :disabled="isEdit"
              >
                <template #append>
                  <el-button @click="generateContractNumber" :disabled="isEdit">
                    生成
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同名称" prop="name">
              <el-input
                v-model="formData.name"
                placeholder="请输入合同名称"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合同类型" prop="type">
              <el-select v-model="formData.type" placeholder="请选择合同类型">
                <el-option
                  v-for="option in contractTypes"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择合同状态">
                <el-option
                  v-for="option in contractStatuses"
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
            <el-form-item label="合同版本" prop="version">
              <el-input
                v-model="formData.version"
                placeholder="请输入合同版本"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保密级别" prop="confidentialityLevel">
              <el-select v-model="formData.confidentialityLevel" placeholder="请选择保密级别">
                <el-option
                  v-for="option in confidentialityLevels"
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
            <el-form-item label="签署日期" prop="signDate">
              <el-date-picker
                v-model="formData.signDate"
                type="date"
                placeholder="请选择签署日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="formData.effectiveDate"
                type="date"
                placeholder="请选择生效日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="到期日期" prop="expiryDate">
              <el-date-picker
                v-model="formData.expiryDate"
                type="date"
                placeholder="请选择到期日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="自动续约" prop="autoRenewal">
              <el-switch
                v-model="formData.autoRenewal"
                active-text="是"
                inactive-text="否"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="合同描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入合同描述"
          />
        </el-form-item>

        <!-- 合同双方 -->
        <el-divider content-position="left">
          <el-icon><User /></el-icon>
          合同双方
        </el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="甲方（我方）" prop="partyA">
              <el-select v-model="formData.partyA" placeholder="请选择甲方">
                <el-option
                  v-for="company in companies"
                  :key="company.id"
                  :label="company.name"
                  :value="company.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="乙方（供应商）" prop="partyB">
              <el-select v-model="formData.partyB" placeholder="请选择乙方">
                <el-option
                  v-for="supplier in suppliers"
                  :key="supplier.id"
                  :label="supplier.name"
                  :value="supplier.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="甲方负责人" prop="partyAContact">
              <el-input
                v-model="formData.partyAContact"
                placeholder="请输入甲方负责人"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="乙方负责人" prop="partyBContact">
              <el-input
                v-model="formData.partyBContact"
                placeholder="请输入乙方负责人"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 财务信息 -->
        <el-divider content-position="left">
          <el-icon><Money /></el-icon>
          财务信息
        </el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合同总金额" prop="totalAmount">
              <el-input
                v-model="formData.totalAmount"
                placeholder="请输入合同总金额"
              >
                <template #append>元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currency">
              <el-select v-model="formData.currency" placeholder="请选择币种">
                <el-option
                  v-for="option in currencies"
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
            <el-form-item label="付款方式" prop="paymentMethod">
              <el-select v-model="formData.paymentMethod" placeholder="请选择付款方式">
                <el-option
                  v-for="option in paymentMethods"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款条件" prop="paymentTerms">
              <el-select v-model="formData.paymentTerms" placeholder="请选择付款条件">
                <el-option
                  v-for="option in paymentTerms"
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
            <el-form-item label="发票类型" prop="invoiceType">
              <el-select v-model="formData.invoiceType" placeholder="请选择发票类型">
                <el-option
                  v-for="option in invoiceTypes"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="税率" prop="taxRate">
              <el-select v-model="formData.taxRate" placeholder="请选择税率">
                <el-option
                  v-for="option in taxRates"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 合同条款 -->
        <el-divider content-position="left">
          <el-icon><List /></el-icon>
          合同条款
        </el-divider>

        <el-form-item label="交付条款" prop="deliveryTerms">
          <el-input
            v-model="formData.deliveryTerms"
            type="textarea"
            :rows="2"
            placeholder="请输入交付条款"
          />
        </el-form-item>

        <el-form-item label="质量标准" prop="qualityStandards">
          <el-input
            v-model="formData.qualityStandards"
            type="textarea"
            :rows="2"
            placeholder="请输入质量标准"
          />
        </el-form-item>

        <el-form-item label="违约责任" prop="penaltyTerms">
          <el-input
            v-model="formData.penaltyTerms"
            type="textarea"
            :rows="2"
            placeholder="请输入违约责任条款"
          />
        </el-form-item>

        <el-form-item label="争议解决" prop="disputeResolution">
          <el-input
            v-model="formData.disputeResolution"
            type="textarea"
            :rows="2"
            placeholder="请输入争议解决方式"
          />
        </el-form-item>

        <el-form-item label="其他条款" prop="otherTerms">
          <el-input
            v-model="formData.otherTerms"
            type="textarea"
            :rows="3"
            placeholder="请输入其他重要条款"
          />
        </el-form-item>

        <!-- 附件管理 -->
        <el-divider content-position="left">
          <el-icon><Paperclip /></el-icon>
          附件管理
        </el-divider>

        <el-form-item label="合同附件" prop="attachments">
          <el-upload
            v-model:file-list="fileList"
            :action="uploadAction"
            :headers="uploadHeaders"
            :on-success="handleUploadSuccess"
            :on-remove="handleUploadRemove"
            :before-upload="beforeUpload"
            multiple
            :limit="10"
          >
            <el-button type="primary">
              <el-icon><Upload /></el-icon>
              上传附件
            </el-button>
            <template #tip>
              <div class="el-upload__tip">
                支持pdf/doc/docx/xls/xlsx格式，单个文件不超过10MB
              </div>
            </template>
          </el-upload>
        </el-form-item>

        <!-- 备注 -->
        <el-divider content-position="left">
          <el-icon><EditPen /></el-icon>
          备注信息
        </el-divider>

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
      title="合同信息预览"
      width="800px"
    >
      <div class="contract-preview">
        <div class="preview-section">
          <h4>基本信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="合同编号">{{ formData.contractNumber }}</el-descriptions-item>
            <el-descriptions-item label="合同名称">{{ formData.name }}</el-descriptions-item>
            <el-descriptions-item label="合同类型">{{ getTypeLabel(formData.type) }}</el-descriptions-item>
            <el-descriptions-item label="合同状态">{{ getStatusLabel(formData.status) }}</el-descriptions-item>
            <el-descriptions-item label="合同版本">{{ formData.version }}</el-descriptions-item>
            <el-descriptions-item label="保密级别">{{ getConfidentialityLabel(formData.confidentialityLevel) }}</el-descriptions-item>
            <el-descriptions-item label="签署日期">{{ formatDate(formData.signDate) }}</el-descriptions-item>
            <el-descriptions-item label="生效日期">{{ formatDate(formData.effectiveDate) }}</el-descriptions-item>
            <el-descriptions-item label="到期日期">{{ formatDate(formData.expiryDate) }}</el-descriptions-item>
            <el-descriptions-item label="自动续约">{{ formData.autoRenewal ? '是' : '否' }}</el-descriptions-item>
            <el-descriptions-item label="合同描述" :span="2">{{ formData.description }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="preview-section">
          <h4>合同双方</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="甲方">{{ getCompanyLabel(formData.partyA) }}</el-descriptions-item>
            <el-descriptions-item label="乙方">{{ getSupplierLabel(formData.partyB) }}</el-descriptions-item>
            <el-descriptions-item label="甲方负责人">{{ formData.partyAContact }}</el-descriptions-item>
            <el-descriptions-item label="乙方负责人">{{ formData.partyBContact }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="preview-section">
          <h4>财务信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="合同总金额">{{ formatAmount(formData.totalAmount) }}</el-descriptions-item>
            <el-descriptions-item label="币种">{{ getCurrencyLabel(formData.currency) }}</el-descriptions-item>
            <el-descriptions-item label="付款方式">{{ getPaymentMethodLabel(formData.paymentMethod) }}</el-descriptions-item>
            <el-descriptions-item label="付款条件">{{ getPaymentTermsLabel(formData.paymentTerms) }}</el-descriptions-item>
            <el-descriptions-item label="发票类型">{{ getInvoiceTypeLabel(formData.invoiceType) }}</el-descriptions-item>
            <el-descriptions-item label="税率">{{ formData.taxRate }}%</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="preview-section">
          <h4>合同条款</h4>
          <div class="terms-content">
            <div class="term-item">
              <strong>交付条款：</strong>
              <p>{{ formData.deliveryTerms || '-' }}</p>
            </div>
            <div class="term-item">
              <strong>质量标准：</strong>
              <p>{{ formData.qualityStandards || '-' }}</p>
            </div>
            <div class="term-item">
              <strong>违约责任：</strong>
              <p>{{ formData.penaltyTerms || '-' }}</p>
            </div>
            <div class="term-item">
              <strong>争议解决：</strong>
              <p>{{ formData.disputeResolution || '-' }}</p>
            </div>
            <div class="term-item">
              <strong>其他条款：</strong>
              <p>{{ formData.otherTerms || '-' }}</p>
            </div>
          </div>
        </div>

        <div class="preview-section">
          <h4>附件列表</h4>
          <div v-if="fileList.length === 0" class="no-files">暂无附件</div>
          <div v-else class="file-list">
            <div v-for="file in fileList" :key="file.uid" class="file-item">
              <el-icon><Document /></el-icon>
              <span>{{ file.name }}</span>
              <span class="file-size">({{ formatFileSize(file.size) }})</span>
            </div>
          </div>
        </div>

        <div class="preview-section">
          <h4>备注信息</h4>
          <p>{{ formData.notes || '-' }}</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Document, User, Money, List, Paperclip, Upload, EditPen 
} from '@element-plus/icons-vue'
import { useProcurementAdvanced } from '@/composables/useProcurementAdvanced'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  contract: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:visible', 'success'])

// 组合式函数
const {
  createContract,
  updateContract,
  getSuppliers
} = useProcurementAdvanced()

// 响应式数据
const formRef = ref()
const dialogVisible = computed({
  get: () => props.visible,
  set: (val) => emit('update:visible', val)
})

const formData = reactive({
  contractNumber: '',
  name: '',
  type: '',
  status: '',
  version: '1.0',
  confidentialityLevel: '',
  signDate: '',
  effectiveDate: '',
  expiryDate: '',
  autoRenewal: false,
  description: '',
  partyA: '',
  partyB: '',
  partyAContact: '',
  partyBContact: '',
  totalAmount: '',
  currency: 'CNY',
  paymentMethod: '',
  paymentTerms: '',
  invoiceType: '',
  taxRate: 13,
  deliveryTerms: '',
  qualityStandards: '',
  penaltyTerms: '',
  disputeResolution: '',
  otherTerms: '',
  attachments: [],
  notes: ''
})

const submitting = ref(false)
const previewVisible = ref(false)
const fileList = ref([])

// 计算属性
const isEdit = computed(() => !!props.contract)

// 配置数据
const contractTypes = [
  { label: '采购合同', value: 'purchase' },
  { label: '框架协议', value: 'framework' },
  { label: '服务合同', value: 'service' },
  { label: '战略合作协议', value: 'strategic' },
  { label: '保密协议', value: 'confidentiality' },
  { label: '其他', value: 'other' }
]

const contractStatuses = [
  { label: '草稿', value: 'draft' },
  { label: '待签署', value: 'pending_signature' },
  { label: '已签署', value: 'signed' },
  { label: '执行中', value: 'active' },
  { label: '已到期', value: 'expired' },
  { label: '已终止', value: 'terminated' },
  { label: '已取消', value: 'cancelled' }
]

const confidentialityLevels = [
  { label: '公开', value: 'public' },
  { label: '内部', value: 'internal' },
  { label: '机密', value: 'confidential' },
  { label: '绝密', value: 'top_secret' }
]

const currencies = [
  { label: '人民币 (CNY)', value: 'CNY' },
  { label: '美元 (USD)', value: 'USD' },
  { label: '欧元 (EUR)', value: 'EUR' },
  { label: '日元 (JPY)', value: 'JPY' }
]

const paymentMethods = [
  { label: '银行转账', value: 'bank_transfer' },
  { label: '现金支付', value: 'cash' },
  { label: '支票支付', value: 'check' },
  { label: '承兑汇票', value: 'acceptance' },
  { label: '信用证', value: 'letter_of_credit' }
]

const paymentTerms = [
  { label: '货到付款', value: 'cod' },
  { label: '预付款', value: 'advance' },
  { label: '30天账期', value: 'net_30' },
  { label: '60天账期', value: 'net_60' },
  { label: '90天账期', value: 'net_90' }
]

const invoiceTypes = [
  { label: '增值税专用发票', value: 'vat_special' },
  { label: '增值税普通发票', value: 'vat_normal' },
  { label: '电子发票', value: 'electronic' },
  { label: '收据', value: 'receipt' }
]

const taxRates = [
  { label: '0%', value: 0 },
  { label: '3%', value: 3 },
  { label: '6%', value: 6 },
  { label: '9%', value: 9 },
  { label: '13%', value: 13 }
]

const companies = [
  { id: 1, name: '奇优网络科技有限公司' },
  { id: 2, name: '奇优信息技术有限公司' }
]

const suppliers = ref([])

// 上传配置
const uploadAction = '/api/upload/contract'
const uploadHeaders = {
  'Authorization': `Bearer ${localStorage.getItem('token')}`
}

// 表单验证规则
const formRules = {
  contractNumber: [
    { required: true, message: '请输入合同编号', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入合同名称', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择合同类型', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择合同状态', trigger: 'change' }
  ],
  signDate: [
    { required: true, message: '请选择签署日期', trigger: 'change' }
  ],
  effectiveDate: [
    { required: true, message: '请选择生效日期', trigger: 'change' }
  ],
  partyA: [
    { required: true, message: '请选择甲方', trigger: 'change' }
  ],
  partyB: [
    { required: true, message: '请选择乙方', trigger: 'change' }
  ],
  totalAmount: [
    { required: true, message: '请输入合同总金额', trigger: 'blur' },
    { pattern: /^\d+(\.\d{1,2})?$/, message: '请输入正确的金额格式', trigger: 'blur' }
  ]
}

// 初始化数据
onMounted(() => {
  loadSuppliers()
})

// 监听合同数据变化
watch(() => props.contract, (newContract) => {
  if (newContract) {
    Object.assign(formData, newContract)
    fileList.value = newContract.attachments || []
  } else {
    resetForm()
  }
}, { immediate: true })

// 工具方法
const loadSuppliers = async () => {
  try {
    const result = await getSuppliers()
    suppliers.value = result || []
  } catch (error) {
    console.error('加载供应商列表失败:', error)
  }
}

const generateContractNumber = () => {
  const prefix = 'CT'
  const timestamp = Date.now().toString().slice(-6)
  const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0')
  formData.contractNumber = `${prefix}${timestamp}${random}`
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

const formatAmount = (amount) => {
  if (!amount) return '-'
  return `¥${Number(amount).toLocaleString()}`
}

const formatFileSize = (size) => {
  if (!size) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB']
  let unitIndex = 0
  let fileSize = size
  while (fileSize >= 1024 && unitIndex < units.length - 1) {
    fileSize /= 1024
    unitIndex++
  }
  return `${fileSize.toFixed(1)} ${units[unitIndex]}`
}

const getTypeLabel = (value) => {
  const item = contractTypes.find(item => item.value === value)
  return item ? item.label : '-'
}

const getStatusLabel = (value) => {
  const item = contractStatuses.find(item => item.value === value)
  return item ? item.label : '-'
}

const getConfidentialityLabel = (value) => {
  const item = confidentialityLevels.find(item => item.value === value)
  return item ? item.label : '-'
}

const getCurrencyLabel = (value) => {
  const item = currencies.find(item => item.value === value)
  return item ? item.label : '-'
}

const getPaymentMethodLabel = (value) => {
  const item = paymentMethods.find(item => item.value === value)
  return item ? item.label : '-'
}

const getPaymentTermsLabel = (value) => {
  const item = paymentTerms.find(item => item.value === value)
  return item ? item.label : '-'
}

const getInvoiceTypeLabel = (value) => {
  const item = invoiceTypes.find(item => item.value === value)
  return item ? item.label : '-'
}

const getCompanyLabel = (id) => {
  const item = companies.find(item => item.id === id)
  return item ? item.name : '-'
}

const getSupplierLabel = (id) => {
  const item = suppliers.value.find(item => item.id === id)
  return item ? item.name : '-'
}

const resetForm = () => {
  Object.assign(formData, {
    contractNumber: '',
    name: '',
    type: '',
    status: '',
    version: '1.0',
    confidentialityLevel: '',
    signDate: '',
    effectiveDate: '',
    expiryDate: '',
    autoRenewal: false,
    description: '',
    partyA: '',
    partyB: '',
    partyAContact: '',
    partyBContact: '',
    totalAmount: '',
    currency: 'CNY',
    paymentMethod: '',
    paymentTerms: '',
    invoiceType: '',
    taxRate: 13,
    deliveryTerms: '',
    qualityStandards: '',
    penaltyTerms: '',
    disputeResolution: '',
    otherTerms: '',
    attachments: [],
    notes: ''
  })
  fileList.value = []
  formRef.value?.clearValidate()
}

// 文件上传处理
const handleUploadSuccess = (response, file) => {
  file.url = response.url
  formData.attachments.push({
    name: file.name,
    url: response.url,
    size: file.size
  })
  ElMessage.success('文件上传成功')
}

const handleUploadRemove = (file) => {
  const index = formData.attachments.findIndex(item => item.name === file.name)
  if (index > -1) {
    formData.attachments.splice(index, 1)
  }
}

const beforeUpload = (file) => {
  const isValidType = ['application/pdf', 'application/msword', 
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    'application/vnd.ms-excel',
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
  ].includes(file.type)
  
  const isLt10M = file.size / 1024 / 1024 < 10

  if (!isValidType) {
    ElMessage.error('只能上传 pdf/doc/docx/xls/xlsx 格式的文件!')
    return false
  }
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过 10MB!')
    return false
  }
  return true
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

    const contractData = { 
      ...formData,
      attachments: fileList.value
    }
    
    if (isEdit.value) {
      await updateContract(props.contract.id, contractData)
      ElMessage.success('合同信息更新成功')
    } else {
      await createContract(contractData)
      ElMessage.success('合同创建成功')
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
.contract-form-dialog {
  .contract-form {
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

    .terms-content {
      .term-item {
        margin-bottom: 16px;

        strong {
          display: block;
          margin-bottom: 8px;
          color: #606266;
        }

        p {
          margin: 0;
          padding: 12px;
          background-color: #f5f7fa;
          border-radius: 4px;
          line-height: 1.6;
        }
      }
    }

    .no-files {
      text-align: center;
      color: #909399;
      padding: 20px;
    }

    .file-list {
      .file-item {
        display: flex;
        align-items: center;
        padding: 8px 12px;
        border: 1px solid #e4e7ed;
        border-radius: 4px;
        margin-bottom: 8px;

        .el-icon {
          margin-right: 8px;
          color: #409eff;
        }

        .file-size {
          margin-left: auto;
          color: #909399;
          font-size: 12px;
        }
      }
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

:deep(.el-upload) {
  width: 100%;
}
</style>