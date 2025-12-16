<template>
  <div class="customer-detail-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">客户详情</h1>
          <p class="page-subtitle">{{ customer?.customerName }} - 客户档案信息</p>
        </div>
        <div class="header-actions">
          <el-button @click="handleBack">
            <el-icon><ArrowLeft /></el-icon>
            返回
          </el-button>
          <el-button type="primary" @click="handleEdit">
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-button 
            :type="customer?.status === 'active' ? 'warning' : 'success'"
            @click="handleToggleStatus"
          >
            <el-icon><SwitchButton /></el-icon>
            {{ customer?.status === 'active' ? '停用' : '启用' }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/customer' }">客户管理</el-breadcrumb-item>
      <el-breadcrumb-item>客户详情</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 客户概览卡片 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="overview-card">
            <div class="card-header">
              <h3>客户信息</h3>
              <el-tag :type="getStatusTagType(customer?.status)">
                {{ getStatusLabel(customer?.status) }}
              </el-tag>
            </div>
            <div class="card-content">
              <div class="info-item">
                <span class="label">客户名称：</span>
                <span class="value">{{ customer?.customerName || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">客户类型：</span>
                <span class="value">
                  <el-tag size="small" :type="getTypeTagType(customer?.customerType)">
                    {{ getTypeLabel(customer?.customerType) }}
                  </el-tag>
                </span>
              </div>
              <div class="info-item">
                <span class="label">客户等级：</span>
                <span class="value">
                  <el-tag size="small" :type="getLevelTagType(customer?.level)">
                    {{ getLevelLabel(customer?.level) }}
                  </el-tag>
                </span>
              </div>
              <div class="info-item">
                <span class="label">手机号：</span>
                <span class="value">{{ customer?.phone || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">邮箱：</span>
                <span class="value">{{ customer?.email || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">地址：</span>
                <span class="value">{{ customer?.address || '-' }}</span>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card class="overview-card">
            <div class="card-header">
              <h3>交易统计</h3>
            </div>
            <div class="card-content">
              <div class="stat-item">
                <span class="stat-label">订单总额</span>
                <span class="stat-value">¥{{ formatCurrency(customer?.totalOrderAmount || 0) }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">订单数量</span>
                <span class="stat-value">{{ customer?.orderCount || 0 }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">平均订单金额</span>
                <span class="stat-value">¥{{ formatCurrency((customer?.totalOrderAmount || 0) / (customer?.orderCount || 1)) }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">最后下单时间</span>
                <span class="stat-value">{{ formatDate(customer?.lastOrderDate) }}</span>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card class="overview-card">
            <div class="card-header">
              <h3>客户标签</h3>
            </div>
            <div class="card-content">
              <div class="tags-container">
                <el-tag
                  v-for="tag in customer?.tags || []"
                  :key="tag"
                  size="small"
                  class="tag-item"
                >
                  {{ tag }}
                </el-tag>
                <span v-if="!customer?.tags?.length" class="no-tags">暂无标签</span>
              </div>
              <div class="info-item">
                <span class="label">客户来源：</span>
                <span class="value">{{ getSourceLabel(customer?.source) }}</span>
              </div>
              <div class="info-item">
                <span class="label">RFM分层：</span>
                <span class="value">{{ getRFMLabel(customer?.rfmLevel) }}</span>
              </div>
              <div class="info-item">
                <span class="label">微信号：</span>
                <span class="value">{{ customer?.wechatId || '-' }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 详细信息标签页 -->
    <div class="detail-section">
      <el-card>
        <el-tabs v-model="activeTab">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="basic">
            <div class="detail-content">
              <el-descriptions :column="2" border>
                <el-descriptions-item label="客户ID">{{ customer?.id || '-' }}</el-descriptions-item>
                <el-descriptions-item label="客户名称">{{ customer?.customerName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="客户类型">
                  <el-tag :type="getTypeTagType(customer?.customerType)">
                    {{ getTypeLabel(customer?.customerType) }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="客户等级">
                  <el-tag :type="getLevelTagType(customer?.level)">
                    {{ getLevelLabel(customer?.level) }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="手机号">{{ customer?.phone || '-' }}</el-descriptions-item>
                <el-descriptions-item label="邮箱">{{ customer?.email || '-' }}</el-descriptions-item>
                <el-descriptions-item label="地址" :span="2">{{ customer?.address || '-' }}</el-descriptions-item>
                <el-descriptions-item label="微信号">{{ customer?.wechatId || '-' }}</el-descriptions-item>
                <el-descriptions-item label="客户来源">{{ getSourceLabel(customer?.source) }}</el-descriptions-item>
                <el-descriptions-item label="RFM分层">{{ getRFMLabel(customer?.rfmLevel) }}</el-descriptions-item>
                <el-descriptions-item label="注册时间">{{ formatDateTime(customer?.createdAt) }}</el-descriptions-item>
                <el-descriptions-item label="最后更新">{{ formatDateTime(customer?.updatedAt) }}</el-descriptions-item>
                <el-descriptions-item label="备注" :span="2">{{ customer?.remark || '暂无备注' }}</el-descriptions-item>
              </el-descriptions>
            </div>
          </el-tab-pane>

          <!-- 企业信息（B2B） -->
          <el-tab-pane label="企业信息" name="enterprise" v-if="isEnterprise">
            <div class="detail-content">
              <el-descriptions :column="2" border>
                <el-descriptions-item label="公司名称">{{ customer?.companyName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="企业类型">{{ getEnterpriseTypeLabel(customer?.enterpriseType) }}</el-descriptions-item>
                <el-descriptions-item label="营业执照">{{ customer?.businessLicense || '-' }}</el-descriptions-item>
                <el-descriptions-item label="税号">{{ customer?.taxNumber || '-' }}</el-descriptions-item>
                <el-descriptions-item label="法人代表">{{ customer?.legalRepresentative || '-' }}</el-descriptions-item>
                <el-descriptions-item label="注册资本">¥{{ formatCurrency(customer?.registeredCapital || 0) }}</el-descriptions-item>
                <el-descriptions-item label="成立日期">{{ formatDate(customer?.establishedDate) }}</el-descriptions-item>
                <el-descriptions-item label="员工人数">{{ customer?.employeesCount || 0 }}人</el-descriptions-item>
                <el-descriptions-item label="年营业额">¥{{ formatCurrency(customer?.annualRevenue || 0) }}</el-descriptions-item>
                <el-descriptions-item label="行业类型">{{ getIndustryLabel(customer?.industry) }}</el-descriptions-item>
                <el-descriptions-item label="公司地址" :span="2">{{ customer?.companyAddress || '-' }}</el-descriptions-item>
                <el-descriptions-item label="公司电话">{{ customer?.companyPhone || '-' }}</el-descriptions-item>
                <el-descriptions-item label="公司邮箱">{{ customer?.companyEmail || '-' }}</el-descriptions-item>
                <el-descriptions-item label="公司网址" :span="2">{{ customer?.companyWebsite || '-' }}</el-descriptions-item>
              </el-descriptions>
            </div>
          </el-tab-pane>

          <!-- 信用信息 -->
          <el-tab-pane label="信用信息" name="credit" v-if="isEnterprise">
            <div class="detail-content">
              <el-descriptions :column="2" border>
                <el-descriptions-item label="信用额度">¥{{ formatCurrency(customer?.creditLimit || 0) }}</el-descriptions-item>
                <el-descriptions-item label="已用额度">¥{{ formatCurrency(customer?.creditUsed || 0) }}</el-descriptions-item>
                <el-descriptions-item label="可用额度">¥{{ formatCurrency((customer?.creditLimit || 0) - (customer?.creditUsed || 0)) }}</el-descriptions-item>
                <el-descriptions-item label="信用使用率">{{ ((customer?.creditUsed || 0) / (customer?.creditLimit || 1) * 100).toFixed(2) }}%</el-descriptions-item>
                <el-descriptions-item label="付款方式">{{ getPaymentMethodLabel(customer?.paymentMethod) }}</el-descriptions-item>
                <el-descriptions-item label="账期天数">{{ customer?.paymentTerms || 0 }}天</el-descriptions-item>
              </el-descriptions>
            </div>
          </el-tab-pane>

          <!-- 联系人信息 -->
          <el-tab-pane label="联系人" name="contacts" v-if="isEnterprise">
            <div class="detail-content">
              <div v-if="customer?.contacts?.length" class="contacts-list">
                <el-card v-for="(contact, index) in customer.contacts" :key="index" class="contact-card">
                  <template #header>
                    <div class="contact-header">
                      <span>{{ contact.name }}</span>
                      <el-tag v-if="contact.isPrimary" type="success" size="small">主要联系人</el-tag>
                    </div>
                  </template>
                  <el-descriptions :column="1">
                    <el-descriptions-item label="职位">{{ contact.position || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="手机">{{ contact.phone || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="邮箱">{{ contact.email || '-' }}</el-descriptions-item>
                  </el-descriptions>
                </el-card>
              </div>
              <div v-else class="no-data">
                <el-empty description="暂无联系人信息" />
              </div>
            </div>
          </el-tab-pane>

          <!-- 订单记录 -->
          <el-tab-pane label="订单记录" name="orders">
            <div class="detail-content">
              <div class="orders-section">
                <el-table :data="orders" style="width: 100%">
                  <el-table-column prop="orderNo" label="订单号" min-width="180" />
                  <el-table-column prop="orderDate" label="下单时间" width="160">
                    <template #default="scope">
                      {{ formatDateTime(scope.row.orderDate) }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="status" label="订单状态" width="100">
                    <template #default="scope">
                      <el-tag :type="getOrderStatusType(scope.row.status)">
                        {{ getOrderStatusLabel(scope.row.status) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="totalAmount" label="订单金额" width="120" align="right">
                    <template #default="scope">
                      ¥{{ formatCurrency(scope.row.totalAmount) }}
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="100" fixed="right">
                    <template #default="scope">
                      <el-button type="primary" link @click="viewOrder(scope.row)">
                        查看
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="pagination-container">
                  <el-pagination
                    v-model:current-page="orderPagination.page"
                    v-model:page-size="orderPagination.limit"
                    :total="orderPagination.total"
                    layout="total, prev, pager, next"
                    @current-change="loadOrders"
                  />
                </div>
              </div>
            </div>
          </el-tab-pane>

          <!-- 操作记录 -->
          <el-tab-pane label="操作记录" name="logs">
            <div class="detail-content">
              <div class="logs-section">
                <el-timeline>
                  <el-timeline-item
                    v-for="(log, index) in logs"
                    :key="index"
                    :timestamp="formatDateTime(log.createdAt)"
                    :type="getLogType(log.action)"
                    placement="top"
                  >
                    <el-card>
                      <h4>{{ log.title }}</h4>
                      <p>{{ log.content }}</p>
                      <p class="log-operator">操作人：{{ log.operator }}</p>
                    </el-card>
                  </el-timeline-item>
                </el-timeline>
                <div v-if="!logs.length" class="no-data">
                  <el-empty description="暂无操作记录" />
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useCustomer } from '@/composables/useCustomer'

const route = useRoute()
const router = useRouter()
const { loading, getCustomerDetail, updateCustomerStatus } = useCustomer()

const customer = ref(null)
const activeTab = ref('basic')

// 订单数据
const orders = ref([])
const orderPagination = reactive({
  page: 1,
  limit: 10,
  total: 0
})

// 操作记录
const logs = ref([])

// 计算属性
const isEnterprise = computed(() => {
  return customer.value?.customerType === 'enterprise' || 
         customer.value?.customerType === 'distributor' || 
         customer.value?.customerType === 'supplier'
})

// 获取客户详情
const loadCustomerDetail = async () => {
  try {
    const id = route.params.id
    if (!id) {
      ElMessage.error('客户ID不存在')
      router.push('/customer')
      return
    }

    const data = await getCustomerDetail(id)
    customer.value = data
  } catch (error) {
    ElMessage.error('获取客户详情失败')
    router.push('/customer')
  }
}

// 加载订单数据
const loadOrders = async () => {
  try {
    // 模拟订单数据
    orders.value = Array.from({ length: 8 }, (_, i) => ({
      id: i + 1,
      orderNo: `ORD${String(Date.now()).slice(-6)}${i + 1}`,
      orderDate: new Date(Date.now() - Math.random() * 86400000 * 30).toISOString(),
      status: ['pending', 'confirmed', 'shipped', 'completed', 'cancelled'][Math.floor(Math.random() * 5)],
      totalAmount: Math.floor(Math.random() * 50000) + 1000
    }))
    orderPagination.total = 50
  } catch (error) {
    console.error('加载订单数据失败:', error)
  }
}

// 加载操作记录
const loadLogs = async () => {
  try {
    // 模拟操作记录数据
    logs.value = [
      {
        id: 1,
        title: '客户信息更新',
        content: '更新了客户联系方式和地址信息',
        action: 'update',
        operator: '管理员',
        createdAt: new Date(Date.now() - 86400000 * 2).toISOString()
      },
      {
        id: 2,
        title: '客户状态变更',
        content: '客户状态从"待审核"变更为"活跃"',
        action: 'status',
        operator: '管理员',
        createdAt: new Date(Date.now() - 86400000 * 5).toISOString()
      },
      {
        id: 3,
        title: '客户创建',
        content: '新建客户档案',
        action: 'create',
        operator: '管理员',
        createdAt: new Date(Date.now() - 86400000 * 10).toISOString()
      }
    ]
  } catch (error) {
    console.error('加载操作记录失败:', error)
  }
}

// 返回
const handleBack = () => {
  router.push('/customer')
}

// 编辑
const handleEdit = () => {
  router.push(`/customer/edit/${customer.value.id}`)
}

// 切换状态
const handleToggleStatus = async () => {
  try {
    const newStatus = customer.value.status === 'active' ? 'inactive' : 'active'
    const actionText = customer.value.status === 'active' ? '停用' : '启用'
    
    await ElMessageBox.confirm(
      `确定要${actionText}客户 "${customer.value.customerName}" 吗？`,
      `确认${actionText}`,
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await updateCustomerStatus(customer.value.id, newStatus)
    customer.value.status = newStatus
    ElMessage.success(`${actionText}成功`)
  } catch (error) {
    if (error !== 'cancel') {
      console.error('状态更新失败:', error)
    }
  }
}

// 查看订单详情
const viewOrder = (order) => {
  // 跳转到订单详情页面
  console.log('查看订单:', order)
}

// 工具函数
const getStatusTagType = (status) => {
  const typeMap = {
    active: 'success',
    inactive: 'info',
    suspended: 'warning',
    pending: 'primary',
    blacklisted: 'danger'
  }
  return typeMap[status] || 'info'
}

const getStatusLabel = (status) => {
  const labelMap = {
    active: '活跃',
    inactive: '非活跃',
    suspended: '暂停',
    pending: '待审核',
    blacklisted: '黑名单'
  }
  return labelMap[status] || status
}

const getTypeTagType = (type) => {
  const typeMap = {
    individual: 'info',
    enterprise: 'primary',
    distributor: 'success',
    supplier: 'warning',
    partner: 'danger'
  }
  return typeMap[type] || 'info'
}

const getTypeLabel = (type) => {
  const labelMap = {
    individual: '个人客户',
    enterprise: '企业客户',
    distributor: '经销商',
    supplier: '供应商',
    partner: '合作伙伴'
  }
  return labelMap[type] || type
}

const getLevelTagType = (level) => {
  const typeMap = {
    normal: 'info',
    vip: 'warning',
    svip: 'danger',
    diamond: 'primary',
    black_gold: 'success'
  }
  return typeMap[level] || 'info'
}

const getLevelLabel = (level) => {
  const labelMap = {
    normal: '普通客户',
    vip: 'VIP客户',
    svip: 'SVIP客户',
    diamond: '钻石客户',
    black_gold: '黑金客户'
  }
  return labelMap[level] || level
}

const getSourceLabel = (source) => {
  const labelMap = {
    website: '官网',
    wechat: '微信',
    alipay: '支付宝',
    taobao: '淘宝',
    jd: '京东',
    tiktok: '抖音',
    kuaishou: '快手',
    offline: '线下',
    referral: '推荐',
    other: '其他'
  }
  return labelMap[source] || source || '-'
}

const getRFMLabel = (rfmLevel) => {
  const labelMap = {
    new: '新客户',
    active: '活跃客户',
    wake: '唤醒客户',
    sleep: '睡眠客户',
    lost: '流失客户'
  }
  return labelMap[rfmLevel] || rfmLevel || '-'
}

const getEnterpriseTypeLabel = (type) => {
  const labelMap = {
    state_owned: '国有企业',
    private: '民营企业',
    foreign: '外资企业',
    joint_venture: '合资企业',
    listed: '上市公司',
    startup: '初创企业'
  }
  return labelMap[type] || type || '-'
}

const getIndustryLabel = (industry) => {
  const labelMap = {
    manufacturing: '制造业',
    trading: '贸易业',
    retail: '零售业',
    wholesale: '批发业',
    logistics: '物流业',
    finance: '金融业',
    real_estate: '房地产',
    technology: '科技业',
    healthcare: '医疗健康',
    education: '教育培训',
    food_beverage: '餐饮食品',
    textile: '纺织服装',
    automotive: '汽车汽配',
    construction: '建筑建材',
    agriculture: '农业农产',
    other: '其他'
  }
  return labelMap[industry] || industry || '-'
}

const getPaymentMethodLabel = (method) => {
  const labelMap = {
    cash: '现金支付',
    bank_transfer: '银行转账',
    credit: '账期支付',
    installment: '分期付款',
    alipay: '支付宝',
    wechat: '微信支付'
  }
  return labelMap[method] || method || '-'
}

const getOrderStatusType = (status) => {
  const typeMap = {
    pending: 'warning',
    confirmed: 'primary',
    shipped: 'info',
    completed: 'success',
    cancelled: 'danger'
  }
  return typeMap[status] || 'info'
}

const getOrderStatusLabel = (status) => {
  const labelMap = {
    pending: '待处理',
    confirmed: '已确认',
    shipped: '已发货',
    completed: '已完成',
    cancelled: '已取消'
  }
  return labelMap[status] || status
}

const getLogType = (action) => {
  const typeMap = {
    create: 'primary',
    update: 'warning',
    delete: 'danger',
    status: 'info'
  }
  return typeMap[action] || 'info'
}

const formatCurrency = (amount) => {
  return new Intl.NumberFormat('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(amount)
}

const formatDate = (dateString) => {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleDateString('zh-CN')
}

const formatDateTime = (dateString) => {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleString('zh-CN')
}

// 监听标签页变化
watch(activeTab, (newTab) => {
  if (newTab === 'orders' && orders.value.length === 0) {
    loadOrders()
  } else if (newTab === 'logs' && logs.value.length === 0) {
    loadLogs()
  }
})

// 初始化
onMounted(() => {
  loadCustomerDetail()
})
</script>

<style lang="scss" scoped>
.customer-detail-page {
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

// 概览区域
.overview-section {
  margin-bottom: 20px;

  .overview-card {
    height: 100%;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      h3 {
        margin: 0;
        font-size: 16px;
        color: var(--el-text-color-primary);
      }
    }

    .card-content {
      .info-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 8px 0;
        border-bottom: 1px solid var(--el-border-color-lighter);

        &:last-child {
          border-bottom: none;
        }

        .label {
          color: var(--el-text-color-regular);
          font-size: 14px;
        }

        .value {
          color: var(--el-text-color-primary);
          font-size: 14px;
          font-weight: 500;
        }
      }

      .stat-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px 0;
        border-bottom: 1px solid var(--el-border-color-lighter);

        &:last-child {
          border-bottom: none;
        }

        .stat-label {
          color: var(--el-text-color-regular);
          font-size: 14px;
        }

        .stat-value {
          color: var(--el-text-color-primary);
          font-size: 18px;
          font-weight: bold;
        }
      }

      .tags-container {
        margin-bottom: 16px;
        min-height: 40px;

        .tag-item {
          margin-right: 8px;
          margin-bottom: 8px;
        }

        .no-tags {
          color: var(--el-text-color-placeholder);
          font-size: 14px;
        }
      }
    }
  }
}

// 详细信息
.detail-section {
  .detail-content {
    padding: 20px;
  }

  .contacts-list {
    .contact-card {
      margin-bottom: 16px;

      .contact-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
    }
  }

  .orders-section {
    .pagination-container {
      display: flex;
      justify-content: center;
      margin-top: 20px;
      padding: 20px 0;
    }
  }

  .logs-section {
    padding: 20px;

    .log-operator {
      margin-top: 8px;
      font-size: 12px;
      color: var(--el-text-color-secondary);
    }
  }

  .no-data {
    text-align: center;
    padding: 40px 0;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .customer-detail-page {
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

  .overview-section {
    .el-col {
      margin-bottom: 16px;
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