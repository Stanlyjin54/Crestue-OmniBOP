<template>
  <el-dialog
    v-model="visible"
    :title="dialogTitle"
    width="80%"
    top="5vh"
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
        <el-col :span="8">
          <el-form-item label="订单编号" prop="orderNo">
            <el-input v-model="formData.orderNo" :disabled="mode === 'edit'" placeholder="系统自动生成" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="订单类型" prop="orderType">
            <el-select v-model="formData.orderType" placeholder="请选择订单类型">
              <el-option
                v-for="type in ORDER_TYPES"
                :key="type.value"
                :label="type.label"
                :value="type.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="订单状态" prop="status">
            <el-select v-model="formData.status" placeholder="请选择订单状态">
              <el-option
                v-for="status in ORDER_STATUS"
                :key="status.value"
                :label="status.label"
                :value="status.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="客户" prop="customerId">
            <el-select
              v-model="formData.customerId"
              placeholder="请选择客户"
              filterable
              remote
              :remote-method="searchCustomers"
              @change="handleCustomerChange"
            >
              <el-option
                v-for="customer in customerOptions"
                :key="customer.id"
                :label="customer.name"
                :value="customer.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="销售代表" prop="salesRepId">
            <el-select v-model="formData.salesRepId" placeholder="请选择销售代表">
              <el-option
                v-for="rep in salesRepOptions"
                :key="rep.id"
                :label="rep.name"
                :value="rep.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="下单日期" prop="orderDate">
            <el-date-picker
              v-model="formData.orderDate"
              type="date"
              placeholder="选择日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 商品信息 -->
      <el-divider content-position="left">商品信息</el-divider>
      <div class="product-section">
        <div class="section-header">
          <span>商品清单</span>
          <el-button type="primary" size="small" @click="handleAddProduct">
            <el-icon><Plus /></el-icon>添加商品
          </el-button>
        </div>
        
        <el-table :data="formData.items" border style="width: 100%">
          <el-table-column prop="productName" label="商品名称" min-width="200">
            <template #default="{ row, $index }">
              <el-select
                v-model="row.productId"
                placeholder="请选择商品"
                filterable
                remote
                :remote-method="searchProducts"
                @change="(val) => handleProductChange(val, $index)"
              >
                <el-option
                  v-for="product in productOptions"
                  :key="product.id"
                  :label="product.name"
                  :value="product.id"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="specification" label="规格" width="120">
            <template #default="{ row }">
              <span>{{ row.specification || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="unitPrice" label="单价" width="100" align="right">
            <template #default="{ row }">
              ¥{{ row.unitPrice }}
            </template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="120">
            <template #default="{ row, $index }">
              <el-input-number
                v-model="row.quantity"
                :min="1"
                :max="9999"
                @change="calculateItemTotal($index)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="totalPrice" label="小计" width="120" align="right">
            <template #default="{ row }">
              ¥{{ row.totalPrice }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" align="center">
            <template #default="{ $index }">
              <el-button
                type="danger"
                link
                @click="handleRemoveProduct($index)"
              >
                <el-icon><Delete /></el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 金额信息 -->
      <el-divider content-position="left">金额信息</el-divider>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="商品总额">
            <el-input v-model="amountSummary.productAmount" readonly>
              <template #prefix>¥</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="优惠金额">
            <el-input v-model="formData.discountAmount" @change="calculateTotal">
              <template #prefix>¥</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="运费">
            <el-input v-model="formData.shippingFee" @change="calculateTotal">
              <template #prefix>¥</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="税费">
            <el-input v-model="formData.taxAmount" @change="calculateTotal">
              <template #prefix>¥</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="订单总额">
            <el-input v-model="formData.totalAmount" readonly>
              <template #prefix>¥</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="已付金额">
            <el-input v-model="formData.paidAmount">
              <template #prefix>¥</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 支付信息 -->
      <el-divider content-position="left">支付信息</el-divider>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="支付方式">
            <el-select v-model="formData.paymentMethod" placeholder="请选择支付方式">
              <el-option
                v-for="method in PAYMENT_METHODS"
                :key="method.value"
                :label="method.label"
                :value="method.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="支付状态">
            <el-select v-model="formData.paymentStatus" placeholder="请选择支付状态">
              <el-option label="未支付" value="unpaid" />
              <el-option label="部分支付" value="partial" />
              <el-option label="已支付" value="paid" />
              <el-option label="已退款" value="refunded" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="支付时间">
            <el-date-picker
              v-model="formData.paymentTime"
              type="datetime"
              placeholder="选择支付时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 配送信息 -->
      <el-divider content-position="left">配送信息</el-divider>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="配送方式">
            <el-select v-model="formData.shippingMethod" placeholder="请选择配送方式">
              <el-option
                v-for="method in SHIPPING_METHODS"
                :key="method.value"
                :label="method.label"
                :value="method.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="物流公司">
            <el-input v-model="formData.shippingCompany" placeholder="请输入物流公司" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="物流单号">
            <el-input v-model="formData.trackingNumber" placeholder="请输入物流单号" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="配送地址">
            <el-input
              v-model="formData.shippingAddress"
              type="textarea"
              :rows="2"
              placeholder="请输入配送地址"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备注">
            <el-input
              v-model="formData.remark"
              type="textarea"
              :rows="2"
              placeholder="请输入订单备注"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        确定
      </el-button>
    </template>

    <!-- 商品选择对话框 -->
    <el-dialog
      v-model="productDialogVisible"
      title="选择商品"
      width="70%"
    >
      <div class="product-search">
        <el-input
          v-model="productSearchKey"
          placeholder="请输入商品名称或编号"
          clearable
          @clear="searchProducts"
          @keyup.enter="searchProducts"
        >
          <template #append>
            <el-button @click="searchProducts">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
      </div>
      
      <el-table
        :data="productSearchResults"
        border
        style="width: 100%"
        max-height="400"
        @selection-change="handleProductSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="商品名称" min-width="200" />
        <el-table-column prop="code" label="商品编号" width="120" />
        <el-table-column prop="specification" label="规格" width="120" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="price" label="单价" width="100" align="right">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" align="center" />
      </el-table>
      
      <template #footer>
        <el-button @click="productDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddSelectedProducts">
          添加选中商品
        </el-button>
      </template>
    </el-dialog>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Delete, Search } from '@element-plus/icons-vue'
import { useOrder } from '@/composables/useOrder'
import { useProduct } from '@/composables/useProduct'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  order: {
    type: Object,
    default: null
  },
  mode: {
    type: String,
    default: 'create'
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

const { createOrder, updateOrder, getOrderById } = useOrder()
const { getProductList } = useProduct()

// 表单数据
const formRef = ref(null)
const loading = ref(false)
const formData = reactive({
  orderNo: '',
  orderType: 'b2b',
  status: 'pending',
  customerId: '',
  customerName: '',
  salesRepId: '',
  orderDate: new Date().toISOString().split('T')[0],
  items: [],
  totalAmount: 0,
  discountAmount: 0,
  shippingFee: 0,
  taxAmount: 0,
  paidAmount: 0,
  paymentMethod: '',
  paymentStatus: 'unpaid',
  paymentTime: '',
  shippingMethod: '',
  shippingCompany: '',
  shippingAddress: '',
  trackingNumber: '',
  remark: ''
})

// 金额汇总
const amountSummary = reactive({
  productAmount: 0
})

// 选项数据
const customerOptions = ref([])
const salesRepOptions = ref([])
const productOptions = ref([])

// 商品选择对话框
const productDialogVisible = ref(false)
const productSearchKey = ref('')
const productSearchResults = ref([])
const selectedProducts = ref([])

// 订单状态配置
const ORDER_STATUS = [
  { value: 'pending', label: '待确认' },
  { value: 'confirmed', label: '已确认' },
  { value: 'processing', label: '处理中' },
  { value: 'shipped', label: '已发货' },
  { value: 'delivered', label: '已送达' },
  { value: 'completed', label: '已完成' },
  { value: 'cancelled', label: '已取消' }
]

// 订单类型配置
const ORDER_TYPES = [
  { value: 'b2b', label: 'B2B订单' },
  { value: 'b2c', label: 'B2C订单' },
  { value: 'wholesale', label: '批发订单' },
  { value: 'sample', label: '样品订单' }
]

// 支付方式配置
const PAYMENT_METHODS = [
  { value: 'cash', label: '现金' },
  { value: 'bank_transfer', label: '银行转账' },
  { value: 'credit', label: '账期支付' },
  { value: 'installment', label: '分期付款' },
  { value: 'alipay', label: '支付宝' },
  { value: 'wechat', label: '微信支付' }
]

// 配送方式配置
const SHIPPING_METHODS = [
  { value: 'express', label: '快递' },
  { value: 'logistics', label: '物流' },
  { value: 'self_pickup', label: '自提' },
  { value: 'delivery', label: '送货上门' }
]

// 表单验证规则
const formRules = {
  orderType: [{ required: true, message: '请选择订单类型', trigger: 'change' }],
  status: [{ required: true, message: '请选择订单状态', trigger: 'change' }],
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  orderDate: [{ required: true, message: '请选择下单日期', trigger: 'change' }],
  items: [{ required: true, message: '请添加商品', trigger: 'blur' }]
}

// 计算属性
const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const dialogTitle = computed(() => {
  return props.mode === 'create' ? '新建订单' : '编辑订单'
})

// 监听
watch(() => props.modelValue, (val) => {
  if (val && props.order) {
    loadOrderData()
  } else if (val && props.mode === 'create') {
    resetForm()
  }
})

// 方法
const loadOrderData = async () => {
  if (!props.order?.id) return
  
  loading.value = true
  try {
    const res = await getOrderById(props.order.id)
    const order = res.data
    Object.assign(formData, order)
    calculateTotal()
  } catch (error) {
    ElMessage.error('加载订单数据失败')
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  Object.assign(formData, {
    orderNo: '',
    orderType: 'b2b',
    status: 'pending',
    customerId: '',
    customerName: '',
    salesRepId: '',
    orderDate: new Date().toISOString().split('T')[0],
    items: [],
    totalAmount: 0,
    discountAmount: 0,
    shippingFee: 0,
    taxAmount: 0,
    paidAmount: 0,
    paymentMethod: '',
    paymentStatus: 'unpaid',
    paymentTime: '',
    shippingMethod: '',
    shippingCompany: '',
    shippingAddress: '',
    trackingNumber: '',
    remark: ''
  })
  calculateTotal()
}

const searchCustomers = async (query) => {
  if (!query) return
  
  // 模拟客户搜索
  customerOptions.value = [
    { id: 1, name: '客户A公司', contact: '张经理' },
    { id: 2, name: '客户B公司', contact: '李经理' },
    { id: 3, name: '客户C公司', contact: '王经理' }
  ].filter(item => item.name.includes(query))
}

const handleCustomerChange = (customerId) => {
  const customer = customerOptions.value.find(c => c.id === customerId)
  if (customer) {
    formData.customerName = customer.name
  }
}

const searchProducts = async (query) => {
  if (!query) return
  
  try {
    const res = await getProductList({ keyword: query, pageSize: 20 })
    productOptions.value = res.data.list
    productSearchResults.value = res.data.list
  } catch (error) {
    ElMessage.error('搜索商品失败')
  }
}

const handleProductChange = (productId, index) => {
  const product = productOptions.value.find(p => p.id === productId)
  if (product) {
    const item = formData.items[index]
    item.productName = product.name
    item.specification = product.specification
    item.unit = product.unit
    item.unitPrice = product.price
    item.totalPrice = item.quantity * item.unitPrice
    calculateTotal()
  }
}

const handleAddProduct = () => {
  productDialogVisible.value = true
  searchProducts('')
}

const handleRemoveProduct = (index) => {
  formData.items.splice(index, 1)
  calculateTotal()
}

const calculateItemTotal = (index) => {
  const item = formData.items[index]
  item.totalPrice = item.quantity * item.unitPrice
  calculateTotal()
}

const calculateTotal = () => {
  // 计算商品总额
  amountSummary.productAmount = formData.items.reduce((sum, item) => {
    return sum + (item.quantity * item.unitPrice)
  }, 0)
  
  // 计算订单总额
  formData.totalAmount = amountSummary.productAmount 
    - formData.discountAmount 
    + formData.shippingFee 
    + formData.taxAmount
}

const handleProductSelectionChange = (selection) => {
  selectedProducts.value = selection
}

const handleAddSelectedProducts = () => {
  selectedProducts.value.forEach(product => {
    const existingItem = formData.items.find(item => item.productId === product.id)
    if (!existingItem) {
      formData.items.push({
        productId: product.id,
        productName: product.name,
        specification: product.specification,
        unit: product.unit,
        unitPrice: product.price,
        quantity: 1,
        totalPrice: product.price
      })
    }
  })
  calculateTotal()
  productDialogVisible.value = false
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    if (formData.items.length === 0) {
      ElMessage.warning('请至少添加一个商品')
      return
    }
    
    loading.value = true
    try {
      if (props.mode === 'create') {
        await createOrder(formData)
        ElMessage.success('订单创建成功')
      } else {
        await updateOrder(props.order.id, formData)
        ElMessage.success('订单更新成功')
      }
      
      emit('success')
      visible.value = false
    } catch (error) {
      ElMessage.error(props.mode === 'create' ? '订单创建失败' : '订单更新失败')
    } finally {
      loading.value = false
    }
  })
}

const handleClose = () => {
  formRef.value?.resetFields()
  visible.value = false
}

// 初始化
const initData = () => {
  // 加载销售代表选项
  salesRepOptions.value = [
    { id: 1, name: '销售代表A' },
    { id: 2, name: '销售代表B' },
    { id: 3, name: '销售代表C' }
  ]
}

initData()
</script>

<style lang="scss" scoped>
.product-section {
  margin-bottom: 20px;
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
    
    span {
      font-size: 16px;
      font-weight: 500;
    }
  }
}

.product-search {
  margin-bottom: 15px;
  
  .el-input {
    width: 300px;
  }
}

// 深色主题适配
:deep(.dark) {
  .el-divider__text {
    background-color: #1f1f1f;
    color: #e4e7ed;
  }
}
</style>