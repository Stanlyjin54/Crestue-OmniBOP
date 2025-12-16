<template>
  <base-wrapper>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">编辑订单</h1>
          <p class="page-description">订单编号：{{ orderForm.orderNo }}</p>
        </div>
        <div class="header-right">
          <el-button @click="goBack">
            <el-icon><ArrowLeft /></el-icon>返回
          </el-button>
          <el-button type="primary" @click="handleSave" :loading="loading">
            <el-icon><Check /></el-icon>保存
          </el-button>
          <el-button type="success" @click="handleSubmit" :loading="loading" v-if="orderForm.status === 'pending'">
            <el-icon><Position /></el-icon>提交
          </el-button>
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/order' }">订单管理</el-breadcrumb-item>
      <el-breadcrumb-item>编辑订单</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 表单内容 -->
    <el-form 
      ref="orderFormRef"
      :model="orderForm" 
      :rules="orderRules"
      label-width="120px"
      class="order-form"
    >
      <!-- 基本信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="section-header">
            <span><el-icon><InfoFilled /></el-icon>基本信息</span>
          </div>
        </template>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="订单编号" prop="orderNo">
              <el-input v-model="orderForm.orderNo" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="订单类型" prop="orderType">
              <el-select v-model="orderForm.orderType" placeholder="请选择订单类型">
                <el-option label="B2B订单" value="b2b" />
                <el-option label="B2C订单" value="b2c" />
                <el-option label="批发订单" value="wholesale" />
                <el-option label="样品订单" value="sample" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="订单状态" prop="status">
              <el-select v-model="orderForm.status" placeholder="请选择订单状态">
                <el-option label="待确认" value="pending" />
                <el-option label="已确认" value="confirmed" />
                <el-option label="处理中" value="processing" />
                <el-option label="已发货" value="shipped" />
                <el-option label="已送达" value="delivered" />
                <el-option label="已完成" value="completed" />
                <el-option label="已取消" value="cancelled" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="下单日期" prop="orderDate">
              <el-date-picker 
                v-model="orderForm.orderDate" 
                type="date" 
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="销售代表" prop="salesRep">
              <el-input v-model="orderForm.salesRep" placeholder="请输入销售代表" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="orderForm.remark" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 客户信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="section-header">
            <span><el-icon><User /></el-icon>客户信息</span>
          </div>
        </template>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="orderForm.customerName" placeholder="请输入客户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input v-model="orderForm.contactPerson" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="orderForm.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="联系邮箱" prop="contactEmail">
              <el-input v-model="orderForm.contactEmail" placeholder="请输入联系邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户等级" prop="customerLevel">
              <el-select v-model="orderForm.customerLevel" placeholder="请选择客户等级">
                <el-option label="普通客户" value="normal" />
                <el-option label="VIP客户" value="vip" />
                <el-option label="钻石客户" value="diamond" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户ID" prop="customerId">
              <el-input v-model="orderForm.customerId" placeholder="请输入客户ID" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 商品信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="section-header">
            <span><el-icon><ShoppingCart /></el-icon>商品信息</span>
            <el-button type="primary" @click="handleAddProduct" size="small">
              <el-icon><Plus /></el-icon>添加商品
            </el-button>
          </div>
        </template>
        <el-table :data="orderForm.items" border style="width: 100%">
          <el-table-column prop="productName" label="商品名称" min-width="200">
            <template #default="{ row, $index }">
              <el-form-item 
                :prop="`items.${$index}.productName`"
                :rules="{ required: true, message: '请选择商品', trigger: 'change' }"
                class="table-form-item"
              >
                <el-select 
                  v-model="row.productName" 
                  placeholder="选择商品"
                  @change="(val) => handleProductChange(val, row)"
                >
                  <el-option
                    v-for="product in productOptions"
                    :key="product.id"
                    :label="product.name"
                    :value="product.name"
                  />
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column prop="specification" label="规格" width="120">
            <template #default="{ row, $index }">
              <el-form-item 
                :prop="`items.${$index}.specification`"
                :rules="{ required: true, message: '请选择规格', trigger: 'change' }"
                class="table-form-item"
              >
                <el-select 
                  v-model="row.specification" 
                  placeholder="选择规格"
                  @change="(val) => handleSpecChange(val, row)"
                >
                  <el-option
                    v-for="spec in getSpecOptions(row)"
                    :key="spec.value"
                    :label="spec.label"
                    :value="spec.value"
                  />
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column prop="unit" label="单位" width="80">
            <template #default="{ row, $index }">
              <el-form-item 
                :prop="`items.${$index}.unit`"
                :rules="{ required: true, message: '请输入单位', trigger: 'blur' }"
                class="table-form-item"
              >
                <el-input v-model="row.unit" placeholder="单位" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column prop="unitPrice" label="单价" width="100" align="right">
            <template #default="{ row, $index }">
              <el-form-item 
                :prop="`items.${$index}.unitPrice`"
                :rules="{ required: true, message: '请输入单价', trigger: 'blur' }"
                class="table-form-item"
              >
                <el-input-number 
                  v-model="row.unitPrice" 
                  :min="0" 
                  :precision="2"
                  @change="calculateItemTotal(row)"
                  style="width: 100%"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="100" align="center">
            <template #default="{ row, $index }">
              <el-form-item 
                :prop="`items.${$index}.quantity`"
                :rules="{ required: true, message: '请输入数量', trigger: 'blur' }"
                class="table-form-item"
              >
                <el-input-number 
                  v-model="row.quantity" 
                  :min="1" 
                  :precision="0"
                  @change="calculateItemTotal(row)"
                  style="width: 100%"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column prop="totalPrice" label="小计" width="120" align="right">
            <template #default="{ row }">
              ¥{{ formatMoney(row.totalPrice) }}
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" min-width="150">
            <template #default="{ row, $index }">
              <el-form-item 
                :prop="`items.${$index}.remark`"
                class="table-form-item"
              >
                <el-input v-model="row.remark" placeholder="备注" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" align="center">
            <template #default="{ $index }">
              <el-button type="danger" link @click="handleRemoveProduct($index)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="total-summary">
          <div class="summary-item">
            <span class="summary-label">商品总额：</span>
            <span class="summary-value">¥{{ formatMoney(productAmount) }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">优惠金额：</span>
            <span class="summary-value discount">-¥{{ formatMoney(orderForm.discountAmount) }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">运费：</span>
            <span class="summary-value">¥{{ formatMoney(orderForm.shippingFee) }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">税费：</span>
            <span class="summary-value">¥{{ formatMoney(orderForm.taxAmount) }}</span>
          </div>
          <div class="summary-item total">
            <span class="summary-label">订单总额：</span>
            <span class="summary-value total">¥{{ formatMoney(totalAmount) }}</span>
          </div>
        </div>
      </el-card>

      <!-- 支付信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="section-header">
            <span><el-icon><Money /></el-icon>支付信息</span>
          </div>
        </template>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="支付方式" prop="paymentMethod">
              <el-select v-model="orderForm.paymentMethod" placeholder="请选择支付方式">
                <el-option label="现金" value="cash" />
                <el-option label="银行转账" value="bank_transfer" />
                <el-option label="账期支付" value="credit" />
                <el-option label="分期付款" value="installment" />
                <el-option label="支付宝" value="alipay" />
                <el-option label="微信支付" value="wechat" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="支付状态" prop="paymentStatus">
              <el-select v-model="orderForm.paymentStatus" placeholder="请选择支付状态">
                <el-option label="未支付" value="unpaid" />
                <el-option label="部分支付" value="partial" />
                <el-option label="已支付" value="paid" />
                <el-option label="已退款" value="refunded" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="支付时间" prop="paymentTime">
              <el-date-picker 
                v-model="orderForm.paymentTime" 
                type="datetime" 
                placeholder="选择支付时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="支付订单号" prop="paymentOrderNo">
              <el-input v-model="orderForm.paymentOrderNo" placeholder="请输入支付订单号" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="已付金额" prop="paidAmount">
              <el-input-number 
                v-model="orderForm.paidAmount" 
                :min="0" 
                :precision="2"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="账期信息" prop="creditInfo">
              <el-input v-model="orderForm.creditInfo" placeholder="请输入账期信息" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 配送信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="section-header">
            <span><el-icon><Truck /></el-icon>配送信息</span>
          </div>
        </template>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="配送方式" prop="shippingMethod">
              <el-select v-model="orderForm.shippingMethod" placeholder="请选择配送方式">
                <el-option label="快递" value="express" />
                <el-option label="物流" value="logistics" />
                <el-option label="自提" value="self_pickup" />
                <el-option label="送货上门" value="delivery" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="物流公司" prop="shippingCompany">
              <el-input v-model="orderForm.shippingCompany" placeholder="请输入物流公司" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="物流单号" prop="trackingNumber">
              <el-input v-model="orderForm.trackingNumber" placeholder="请输入物流单号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="发货时间" prop="shippingDate">
              <el-date-picker 
                v-model="orderForm.shippingDate" 
                type="date" 
                placeholder="选择发货时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预计到达" prop="estimatedDeliveryDate">
              <el-date-picker 
                v-model="orderForm.estimatedDeliveryDate" 
                type="date" 
                placeholder="选择预计到达时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="签收时间" prop="deliveryDate">
              <el-date-picker 
                v-model="orderForm.deliveryDate" 
                type="date" 
                placeholder="选择签收时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="配送地址" prop="shippingAddress">
              <el-input 
                v-model="orderForm.shippingAddress" 
                type="textarea" 
                :rows="2"
                placeholder="请输入详细配送地址"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
    </el-form>

    <!-- 商品选择对话框 -->
    <el-dialog
      v-model="productDialogVisible"
      title="选择商品"
      width="800px"
    >
      <div class="product-search">
        <el-input
          v-model="productSearch.keyword"
          placeholder="搜索商品名称"
          clearable
          @clear="loadProducts"
          @keyup.enter="loadProducts"
        >
          <template #append>
            <el-button @click="loadProducts">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
      </div>
      <el-table 
        :data="productList" 
        border 
        style="width: 100%"
        max-height="400"
      >
        <el-table-column prop="name" label="商品名称" min-width="200" />
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="price" label="单价" width="100" align="right">
          <template #default="{ row }">
            ¥{{ formatMoney(row.price) }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" align="center" />
        <el-table-column label="操作" width="100" align="center">
          <template #default="{ row }">
            <el-button type="primary" link @click="selectProduct(row)">
              选择
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="dialog-footer">
        <el-pagination
          v-model:current-page="productSearch.page"
          v-model:page-size="productSearch.pageSize"
          :total="productSearch.total"
          @current-change="loadProducts"
          layout="prev, pager, next"
        />
      </div>
    </el-dialog>
  </base-wrapper>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  ArrowLeft, Check, Position, InfoFilled, User, ShoppingCart, 
  Money, Truck, Plus, Delete, Search
} from '@element-plus/icons-vue'
import { useOrder } from '@/composables/useOrder'
import { useProduct } from '@/composables/useProduct'

const route = useRoute()
const router = useRouter()
const { getOrderById, updateOrder } = useOrder()
const { getProducts } = useProduct()

// 表单引用
const orderFormRef = ref()

// 数据状态
const loading = ref(false)
const productDialogVisible = ref(false)

// 表单数据
const orderForm = reactive({
  id: '',
  orderNo: '',
  orderType: 'b2b',
  status: 'pending',
  orderDate: new Date(),
  salesRep: '',
  remark: '',
  customerName: '',
  contactPerson: '',
  contactPhone: '',
  contactEmail: '',
  customerLevel: 'normal',
  customerId: '',
  items: [],
  productAmount: 0,
  discountAmount: 0,
  shippingFee: 0,
  taxAmount: 0,
  totalAmount: 0,
  paymentMethod: 'bank_transfer',
  paymentStatus: 'unpaid',
  paymentTime: '',
  paymentOrderNo: '',
  paidAmount: 0,
  creditInfo: '',
  shippingMethod: 'express',
  shippingCompany: '',
  trackingNumber: '',
  shippingDate: '',
  estimatedDeliveryDate: '',
  deliveryDate: '',
  shippingAddress: ''
})

// 表单验证规则
const orderRules = {
  orderNo: [{ required: true, message: '请输入订单编号', trigger: 'blur' }],
  orderType: [{ required: true, message: '请选择订单类型', trigger: 'change' }],
  status: [{ required: true, message: '请选择订单状态', trigger: 'change' }],
  orderDate: [{ required: true, message: '请选择下单日期', trigger: 'change' }],
  customerName: [{ required: true, message: '请输入客户名称', trigger: 'blur' }],
  contactPerson: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
  contactPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  shippingAddress: [{ required: true, message: '请输入配送地址', trigger: 'blur' }]
}

// 商品选项
const productOptions = ref([])
const productList = ref([])
const productSearch = reactive({
  keyword: '',
  page: 1,
  pageSize: 10,
  total: 0
})

// 计算属性
const productAmount = computed(() => {
  return orderForm.items.reduce((total, item) => {
    return total + (Number(item.unitPrice) * Number(item.quantity))
  }, 0)
})

const totalAmount = computed(() => {
  return productAmount.value - orderForm.discountAmount + orderForm.shippingFee + orderForm.taxAmount
})

// 方法
const loadOrderDetail = async () => {
  const orderId = route.params.id
  if (!orderId) {
    ElMessage.error('订单ID不存在')
    router.push('/order')
    return
  }
  
  loading.value = true
  try {
    const res = await getOrderById(orderId)
    const orderData = res.data
    
    // 填充表单数据
    Object.keys(orderForm).forEach(key => {
      if (orderData[key] !== undefined) {
        orderForm[key] = orderData[key]
      }
    })
    
    // 转换日期格式
    if (orderData.orderDate) {
      orderForm.orderDate = new Date(orderData.orderDate)
    }
    if (orderData.paymentTime) {
      orderForm.paymentTime = new Date(orderData.paymentTime)
    }
    if (orderData.shippingDate) {
      orderForm.shippingDate = new Date(orderData.shippingDate)
    }
    if (orderData.estimatedDeliveryDate) {
      orderForm.estimatedDeliveryDate = new Date(orderData.estimatedDeliveryDate)
    }
    if (orderData.deliveryDate) {
      orderForm.deliveryDate = new Date(orderData.deliveryDate)
    }
    
    // 加载商品选项
    loadProductOptions()
  } catch (error) {
    ElMessage.error('加载订单详情失败')
    router.push('/order')
  } finally {
    loading.value = false
  }
}

const loadProductOptions = async () => {
  try {
    const res = await getProducts({ page: 1, pageSize: 100 })
    productOptions.value = res.data.items
  } catch (error) {
    ElMessage.error('加载商品选项失败')
  }
}

const loadProducts = async () => {
  try {
    const res = await getProducts({
      keyword: productSearch.keyword,
      page: productSearch.page,
      pageSize: productSearch.pageSize
    })
    productList.value = res.data.items
    productSearch.total = res.data.total
  } catch (error) {
    ElMessage.error('加载商品列表失败')
  }
}

const getSpecOptions = (item) => {
  if (!item.productName) return []
  
  const product = productOptions.value.find(p => p.name === item.productName)
  if (!product || !product.specs) return []
  
  return product.specs.map(spec => ({
    label: spec.name,
    value: spec.name
  }))
}

const handleAddProduct = () => {
  productDialogVisible.value = true
  loadProducts()
}

const handleRemoveProduct = (index) => {
  orderForm.items.splice(index, 1)
  calculateTotal()
}

const handleProductChange = (productName, item) => {
  const product = productOptions.value.find(p => p.name === productName)
  if (product) {
    item.unit = product.unit
    item.unitPrice = product.price
    item.specification = ''
    calculateItemTotal(item)
  }
}

const handleSpecChange = (specification, item) => {
  // 根据规格调整价格
  const product = productOptions.value.find(p => p.name === item.productName)
  if (product && product.specs) {
    const spec = product.specs.find(s => s.name === specification)
    if (spec && spec.price) {
      item.unitPrice = spec.price
      calculateItemTotal(item)
    }
  }
}

const calculateItemTotal = (item) => {
  item.totalPrice = Number(item.unitPrice) * Number(item.quantity)
  calculateTotal()
}

const calculateTotal = () => {
  orderForm.productAmount = productAmount.value
  orderForm.totalAmount = totalAmount.value
}

const selectProduct = (product) => {
  const newItem = {
    productName: product.name,
    specification: '',
    unit: product.unit,
    unitPrice: product.price,
    quantity: 1,
    totalPrice: product.price,
    remark: ''
  }
  orderForm.items.push(newItem)
  productDialogVisible.value = false
  calculateTotal()
}

const handleSave = async () => {
  await orderFormRef.value.validate(async (valid) => {
    if (!valid) {
      ElMessage.warning('请完善表单信息')
      return
    }
    
    loading.value = true
    try {
      await updateOrder(orderForm.id, orderForm)
      ElMessage.success('订单保存成功')
      router.push('/order')
    } catch (error) {
      ElMessage.error('订单保存失败')
    } finally {
      loading.value = false
    }
  })
}

const handleSubmit = async () => {
  await orderFormRef.value.validate(async (valid) => {
    if (!valid) {
      ElMessage.warning('请完善表单信息')
      return
    }
    
    loading.value = true
    try {
      await updateOrder(orderForm.id, {
        ...orderForm,
        status: 'confirmed'
      })
      ElMessage.success('订单提交成功')
      router.push('/order')
    } catch (error) {
      ElMessage.error('订单提交失败')
    } finally {
      loading.value = false
    }
  })
}

const goBack = () => {
  router.push('/order')
}

const formatMoney = (amount) => {
  return Number(amount).toFixed(2)
}

// 生命周期
onMounted(() => {
  loadOrderDetail()
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

.order-form {
  .form-section {
    margin-bottom: 20px;
    
    .section-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      
      .el-icon {
        margin-right: 8px;
      }
    }
  }
}

.table-form-item {
  margin-bottom: 0;
  
  .el-form-item__error {
    position: absolute;
    top: 100%;
    left: 0;
    padding-top: 2px;
  }
}

.total-summary {
  margin-top: 20px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
  
  .summary-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px 0;
    
    &.total {
      font-size: 18px;
      font-weight: bold;
      border-top: 2px solid #e4e7ed;
      margin-top: 10px;
      padding-top: 15px;
      
      .summary-value {
        color: #f56c6c;
      }
    }
    
    .summary-label {
      color: #606266;
    }
    
    .summary-value {
      color: #303133;
      font-weight: 500;
      
      &.discount {
        color: #67c23a;
      }
    }
  }
}

.product-search {
  margin-bottom: 20px;
}

.dialog-footer {
  margin-top: 20px;
  text-align: center;
}

// 深色主题适配
:deep(.dark) {
  .page-header {
    background: linear-gradient(135deg, #434343 0%, #000000 100%);
  }
  
  .total-summary {
    background: #2b2b2b;
    
    .summary-item {
      .summary-label {
        color: #cfd3dc;
      }
      
      .summary-value {
        color: #e4e7ed;
      }
    }
  }
}
</style>