<template>
  <div class="supplier-detail-dialog">
    <el-dialog
      v-model="dialogVisible"
      title="供应商详情"
      width="1200px"
      :before-close="handleClose"
    >
      <div class="supplier-detail-content">
        <!-- 供应商基本信息 -->
        <div class="supplier-header">
          <div class="supplier-info">
            <div class="supplier-avatar">
              <el-avatar :size="80" :src="supplier.avatar">
                <el-icon><User /></el-icon>
              </el-avatar>
            </div>
            <div class="supplier-basic">
              <h2 class="supplier-name">{{ supplier.name }}</h2>
              <p class="supplier-code">供应商代码: {{ supplier.code }}</p>
              <div class="supplier-tags">
                <el-tag :type="getStatusTagType(supplier.status)" size="large">
                  {{ getStatusText(supplier.status) }}
                </el-tag>
                <el-tag :type="getCategoryTagType(supplier.category)" size="large">
                  {{ supplier.category }}
                </el-tag>
                <el-tag v-if="supplier.isKey" type="warning" size="large">
                  核心供应商
                </el-tag>
              </div>
            </div>
          </div>
          <div class="supplier-actions">
            <el-button type="primary" @click="handleEditSupplier">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button @click="handleContactSupplier">
              <el-icon><Phone /></el-icon>
              联系
            </el-button>
            <el-button @click="handleViewContracts">
              <el-icon><Document /></el-icon>
              合同
            </el-button>
            <el-dropdown>
              <el-button>
                更多<el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleViewHistory">历史记录</el-dropdown-item>
                  <el-dropdown-item @click="handleGenerateReport">生成报告</el-dropdown-item>
                  <el-dropdown-item @click="handleExportData">导出数据</el-dropdown-item>
                  <el-dropdown-item @click="handleRiskAssessment" divided>风险评估</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>

        <!-- 关键指标 -->
        <div class="key-metrics">
          <h3>
            <el-icon><DataAnalysis /></el-icon>
            关键指标
          </h3>
          <el-row :gutter="20">
            <el-col :span="6" v-for="metric in keyMetrics" :key="metric.key">
              <div class="metric-card">
                <div class="metric-icon" :style="{ backgroundColor: metric.color }">
                  <el-icon><component :is="metric.icon" /></el-icon>
                </div>
                <div class="metric-content">
                  <div class="metric-value">{{ formatValue(metric.value, metric.type) }}</div>
                  <div class="metric-label">{{ metric.label }}</div>
                  <div class="metric-trend" :class="metric.trend > 0 ? 'positive' : 'negative'">
                    <el-icon><ArrowUp v-if="metric.trend > 0" /><ArrowDown v-else /></el-icon>
                    {{ Math.abs(metric.trend) }}%
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 详细信息标签页 -->
        <div class="detail-tabs">
          <el-tabs v-model="activeTab" type="border-card">
            <!-- 基本信息 -->
            <el-tab-pane label="基本信息" name="basic">
              <div class="basic-info-section">
                <el-row :gutter="20">
                  <el-col :span="12">
                    <div class="info-group">
                      <h4>企业信息</h4>
                      <div class="info-item">
                        <span class="info-label">企业全称:</span>
                        <span class="info-value">{{ supplier.enterpriseName }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">注册地址:</span>
                        <span class="info-value">{{ supplier.address }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">成立时间:</span>
                        <span class="info-value">{{ supplier.establishedDate }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">注册资本:</span>
                        <span class="info-value">{{ supplier.registeredCapital }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">经营范围:</span>
                        <span class="info-value">{{ supplier.businessScope }}</span>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="12">
                    <div class="info-group">
                      <h4>联系信息</h4>
                      <div class="info-item">
                        <span class="info-label">联系人:</span>
                        <span class="info-value">{{ supplier.contactPerson }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">联系电话:</span>
                        <span class="info-value">{{ supplier.phone }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">邮箱地址:</span>
                        <span class="info-value">{{ supplier.email }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">传真号码:</span>
                        <span class="info-value">{{ supplier.fax || '未设置' }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">网站地址:</span>
                        <span class="info-value">{{ supplier.website || '未设置' }}</span>
                      </div>
                    </div>
                  </el-col>
                </el-row>

                <el-row :gutter="20" style="margin-top: 20px;">
                  <el-col :span="12">
                    <div class="info-group">
                      <h4>资质信息</h4>
                      <div class="info-item">
                        <span class="info-label">营业执照:</span>
                        <span class="info-value">{{ supplier.businessLicense }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">税务登记:</span>
                        <span class="info-value">{{ supplier.taxRegistration }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">组织机构代码:</span>
                        <span class="info-value">{{ supplier.organizationCode }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">资质证书:</span>
                        <div class="cert-list">
                          <el-tag 
                            v-for="cert in supplier.certifications" 
                            :key="cert"
                            type="success"
                            size="small"
                            style="margin-right: 8px; margin-bottom: 4px;"
                          >
                            {{ cert }}
                          </el-tag>
                        </div>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="12">
                    <div class="info-group">
                      <h4>业务信息</h4>
                      <div class="info-item">
                        <span class="info-label">主营业务:</span>
                        <span class="info-value">{{ supplier.mainBusiness }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">产品类别:</span>
                        <div class="product-tags">
                          <el-tag 
                            v-for="product in supplier.products" 
                            :key="product"
                            type="primary"
                            size="small"
                            style="margin-right: 8px; margin-bottom: 4px;"
                          >
                            {{ product }}
                          </el-tag>
                        </div>
                      </div>
                      <div class="info-item">
                        <span class="info-label">年营业额:</span>
                        <span class="info-value">{{ supplier.annualRevenue }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">员工规模:</span>
                        <span class="info-value">{{ supplier.employeeCount }}</span>
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </el-tab-pane>

            <!-- 交易记录 -->
            <el-tab-pane label="交易记录" name="transactions">
              <div class="transactions-section">
                <div class="section-header">
                  <h4>交易概览</h4>
                  <el-date-picker
                    v-model="transactionDateRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    format="YYYY-MM-DD"
                    value-format="YYYY-MM-DD"
                    @change="handleTransactionDateChange"
                  />
                </div>
                
                <el-table :data="transactionData" stripe style="width: 100%" :height="400">
                  <el-table-column prop="orderId" label="订单编号" width="120" />
                  <el-table-column prop="orderDate" label="订单日期" width="120" />
                  <el-table-column prop="productName" label="产品名称" width="150" />
                  <el-table-column prop="quantity" label="数量" width="80" />
                  <el-table-column prop="unitPrice" label="单价" width="100">
                    <template #default="{ row }">
                      ¥{{ row.unitPrice.toFixed(2) }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="totalAmount" label="总金额" width="120">
                    <template #default="{ row }">
                      ¥{{ row.totalAmount.toLocaleString() }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="deliveryDate" label="交付日期" width="120" />
                  <el-table-column prop="status" label="状态" width="100">
                    <template #default="{ row }">
                      <el-tag :type="getTransactionStatusType(row.status)">
                        {{ getTransactionStatusText(row.status) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="paymentStatus" label="付款状态" width="100">
                    <template #default="{ row }">
                      <el-tag :type="getPaymentStatusType(row.paymentStatus)">
                        {{ getPaymentStatusText(row.paymentStatus) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="120" fixed="right">
                    <template #default="{ row }">
                      <el-button size="small" @click="handleViewOrder(row)">
                        查看
                      </el-button>
                      <el-button size="small" @click="handleDownloadInvoice(row)">
                        发票
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>

            <!-- 绩效评估 -->
            <el-tab-pane label="绩效评估" name="performance">
              <div class="performance-section">
                <el-row :gutter="20">
                  <el-col :span="12">
                    <div class="performance-chart">
                      <h4>绩效评分趋势</h4>
                      <div class="chart-placeholder">
                        <el-icon><TrendCharts /></el-icon>
                        <p>绩效趋势图表</p>
                        <small>显示供应商各项绩效指标的变化趋势</small>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="12">
                    <div class="performance-metrics">
                      <h4>各项评分</h4>
                      <div class="score-item">
                        <span class="score-label">质量评分:</span>
                        <el-rate v-model="performanceScores.quality" disabled show-score />
                      </div>
                      <div class="score-item">
                        <span class="score-label">交付评分:</span>
                        <el-rate v-model="performanceScores.delivery" disabled show-score />
                      </div>
                      <div class="score-item">
                        <span class="score-label">服务评分:</span>
                        <el-rate v-model="performanceScores.service" disabled show-score />
                      </div>
                      <div class="score-item">
                        <span class="score-label">成本评分:</span>
                        <el-rate v-model="performanceScores.cost" disabled show-score />
                      </div>
                      <div class="score-item">
                        <span class="score-label">综合评分:</span>
                        <el-rate v-model="performanceScores.overall" disabled show-score />
                      </div>
                    </div>
                  </el-col>
                </el-row>

                <div class="performance-details" style="margin-top: 30px;">
                  <h4>详细指标</h4>
                  <el-row :gutter="20">
                    <el-col :span="8" v-for="detail in performanceDetails" :key="detail.key">
                      <div class="detail-card">
                        <div class="detail-header">
                          <span class="detail-label">{{ detail.label }}</span>
                          <el-tag :type="detail.status === 'good' ? 'success' : 'warning'" size="small">
                            {{ detail.status === 'good' ? '良好' : '需改进' }}
                          </el-tag>
                        <div class                        </div>
="detail-value">{{ detail.value }}</div>
                        <div class="detail-description">{{ detail.description }}</div>
                      </div>
                    </el-col>
                  </el-row>
                </div>
              </div>
            </el-tab-pane>

            <!-- 风险评估 -->
            <el-tab-pane label="风险评估" name="risk">
              <div class="risk-section">
                <div class="risk-overview">
                  <div class="risk-score-card">
                    <div class="risk-score">
                      <div class="score-circle" :style="{ 
                        background: `conic-gradient(#f56c6c ${riskScore * 3.6}deg, #f0f0f0 0deg)`
                      }">
                        <div class="score-inner">
                          <span class="score-value">{{ riskScore }}</span>
                          <span class="score-label">风险评分</span>
                        </div>
                      </div>
                    </div>
                    <div class="risk-description">
                      <h4>风险等级: {{ getRiskLevelText(riskLevel) }}</h4>
                      <p>{{ riskDescription }}</p>
                    </div>
                  </div>
                </div>

                <div class="risk-details" style="margin-top: 30px;">
                  <h4>风险详情</h4>
                  <div class="risk-list">
                    <div v-for="risk in riskItems" :key="risk.id" class="risk-item">
                      <div class="risk-header">
                        <el-tag :type="getRiskLevelTagType(risk.level)">
                          {{ risk.level }}
                        </el-tag>
                        <span class="risk-title">{{ risk.title }}</span>
                        <span class="risk-date">{{ risk.date }}</span>
                      </div>
                      <div class="risk-content">
                        <p>{{ risk.description }}</p>
                        <div class="risk-metrics">
                          <span class="risk-metric">
                            发生概率: {{ risk.probability }}%
                          </span>
                          <span class="risk-metric">
                            影响程度: {{ risk.impact }}%
                          </span>
                        </div>
                      </div>
                      <div class="risk-actions">
                        <el-button size="small" @click="handleViewRiskDetail(risk)">
                          查看详情
                        </el-button>
                        <el-button size="small" @click="handleRiskMitigation(risk)">
                          应对措施
                        </el-button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 文档管理 -->
            <el-tab-pane label="文档管理" name="documents">
              <div class="documents-section">
                <div class="section-header">
                  <h4>文档列表</h4>
                  <el-button type="primary" @click="handleUploadDocument">
                    <el-icon><Upload /></el-icon>
                    上传文档
                  </el-button>
                </div>

                <div class="document-categories">
                  <el-tabs v-model="activeDocumentTab">
                    <el-tab-pane label="资质证书" name="certificates">
                      <div class="document-grid">
                        <div v-for="doc in certificateDocuments" :key="doc.id" class="document-card">
                          <div class="doc-icon">
                            <el-icon><Document /></el-icon>
                          </div>
                          <div class="doc-info">
                            <h5>{{ doc.name }}</h5>
                            <p>{{ doc.description }}</p>
                            <span class="doc-date">{{ doc.uploadDate }}</span>
                          </div>
                          <div class="doc-actions">
                            <el-button size="small" @click="handleViewDocument(doc)">
                              查看
                            </el-button>
                            <el-button size="small" @click="handleDownloadDocument(doc)">
                              下载
                            </el-button>
                          </div>
                        </div>
                      </div>
                    </el-tab-pane>

                    <el-tab-pane label="合同文件" name="contracts">
                      <div class="document-grid">
                        <div v-for="doc in contractDocuments" :key="doc.id" class="document-card">
                          <div class="doc-icon">
                            <el-icon><Document /></el-icon>
                          </div>
                          <div class="doc-info">
                            <h5>{{ doc.name }}</h5>
                            <p>{{ doc.description }}</p>
                            <span class="doc-date">{{ doc.uploadDate }}</span>
                          </div>
                          <div class="doc-actions">
                            <el-button size="small" @click="handleViewDocument(doc)">
                              查看
                            </el-button>
                            <el-button size="small" @click="handleDownloadDocument(doc)">
                              下载
                            </el-button>
                          </div>
                        </div>
                      </div>
                    </el-tab-pane>

                    <el-tab-pane label="其他文档" name="others">
                      <div class="document-grid">
                        <div v-for="doc in otherDocuments" :key="doc.id" class="document-card">
                          <div class="doc-icon">
                            <el-icon><Document /></el-icon>
                          </div>
                          <div class="doc-info">
                            <h5>{{ doc.name }}</h5>
                            <p>{{ doc.description }}</p>
                            <span class="doc-date">{{ doc.uploadDate }}</span>
                          </div>
                          <div class="doc-actions">
                            <el-button size="small" @click="handleViewDocument(doc)">
                              查看
                            </el-button>
                            <el-button size="small" @click="handleDownloadDocument(doc)">
                              下载
                            </el-button>
                          </div>
                        </div>
                      </div>
                    </el-tab-pane>
                  </el-tabs>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  User, Edit, Phone, Document, ArrowDown, DataAnalysis, 
  TrendCharts, Upload
} from '@element-plus/icons-vue'
import { useProcurementAdvanced } from '@/composables/useProcurementAdvanced'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  supplierId: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:visible'])

// 组合式函数
const {
  getSupplierDetail,
  getSupplierTransactions,
  getSupplierPerformance,
  getSupplierRisk
} = useProcurementAdvanced()

// 响应式数据
const dialogVisible = computed({
  get: () => props.visible,
  set: (val) => emit('update:visible', val)
})

// 标签页配置
const activeTab = ref('basic')
const activeDocumentTab = ref('certificates')

// 筛选条件
const transactionDateRange = ref([])

// 供应商数据
const supplier = ref({
  id: '',
  name: '供应商A',
  code: 'SUP001',
  status: 'active',
  category: '电子元器件',
  isKey: true,
  avatar: '',
  enterpriseName: '某某科技有限公司',
  address: '北京市朝阳区某某路123号',
  establishedDate: '2015-06-15',
  registeredCapital: '1000万元',
  businessScope: '电子元器件研发、生产、销售',
  contactPerson: '张经理',
  phone: '010-12345678',
  email: 'zhang@supplierA.com',
  fax: '010-87654321',
  website: 'www.supplierA.com',
  businessLicense: '91110000123456789X',
  taxRegistration: '110123456789012',
  organizationCode: '12345678-9',
  certifications: ['ISO9001', 'ISO14001', 'CE认证', 'FCC认证'],
  mainBusiness: '电子元器件制造',
  products: ['电阻器', '电容器', '电感器', '连接器'],
  annualRevenue: '5000万元',
  employeeCount: '200人'
})

// 关键指标
const keyMetrics = ref([
  {
    key: 'totalOrders',
    label: '总订单数',
    value: 156,
    type: 'number',
    trend: 12.5,
    color: '#409eff',
    icon: 'Document'
  },
  {
    key: 'totalAmount',
    label: '总交易额',
    value: 2580000,
    type: 'currency',
    trend: 8.3,
    color: '#67c23a',
    icon: 'Money'
  },
  {
    key: 'onTimeRate',
    label: '准时率',
    value: 95.2,
    type: 'percentage',
    trend: 2.1,
    color: '#e6a23c',
    icon: 'Clock'
  },
  {
    key: 'qualityScore',
    label: '质量评分',
    value: 4.5,
    type: 'rating',
    trend: 0.3,
    color: '#f56c6c',
    icon: 'Star'
  }
])

// 交易记录数据
const transactionData = ref([
  {
    orderId: 'PO202401001',
    orderDate: '2024-01-15',
    productName: '电阻器 10KΩ',
    quantity: 1000,
    unitPrice: 0.5,
    totalAmount: 500,
    deliveryDate: '2024-01-30',
    status: 'completed',
    paymentStatus: 'paid'
  },
  {
    orderId: 'PO202401002',
    orderDate: '2024-01-20',
    productName: '电容器 100μF',
    quantity: 500,
    unitPrice: 2.0,
    totalAmount: 1000,
    deliveryDate: '2024-02-05',
    status: 'shipped',
    paymentStatus: 'pending'
  }
])

// 绩效数据
const performanceScores = ref({
  quality: 4.5,
  delivery: 4.2,
  service: 4.0,
  cost: 3.8,
  overall: 4.1
})

const performanceDetails = ref([
  {
    key: 'defectRate',
    label: '缺陷率',
    value: '0.5%',
    status: 'good',
    description: '产品缺陷率低于行业平均水平'
  },
  {
    key: 'returnRate',
    label: '退货率',
    value: '1.2%',
    status: 'good',
    description: '产品退货率保持在较低水平'
  },
  {
    key: 'responseTime',
    label: '响应时间',
    value: '2小时',
    status: 'good',
    description: '客户咨询响应及时'
  }
])

// 风险数据
const riskScore = ref(65)
const riskLevel = ref('medium')
const riskDescription = ref('供应商整体风险可控，需要关注财务状况和市场变化。')

const riskItems = ref([
  {
    id: 1,
    level: '中',
    title: '财务风险',
    date: '2024-01-15',
    description: '供应商财务杠杆较高，需关注现金流状况',
    probability: 45,
    impact: 60
  },
  {
    id: 2,
    level: '低',
    title: '质量风险',
    date: '2024-01-10',
    description: '质量管理体系健全，质量风险较低',
    probability: 20,
    impact: 40
  }
])

// 文档数据
const certificateDocuments = ref([
  {
    id: 1,
    name: '营业执照',
    description: '企业营业执照扫描件',
    uploadDate: '2024-01-01'
  },
  {
    id: 2,
    name: 'ISO9001证书',
    description: '质量管理体系认证证书',
    uploadDate: '2024-01-01'
  }
])

const contractDocuments = ref([
  {
    id: 1,
    name: '框架采购协议',
    description: '2024年度框架采购协议',
    uploadDate: '2024-01-01'
  }
])

const otherDocuments = ref([
  {
    id: 1,
    name: '产品目录',
    description: '最新产品目录和规格书',
    uploadDate: '2024-01-15'
  }
])

// 工具方法
const formatValue = (value, type) => {
  switch (type) {
    case 'currency':
      return `¥${value.toLocaleString()}`
    case 'percentage':
      return `${value}%`
    case 'rating':
      return `${value}/5.0`
    case 'number':
    default:
      return value.toString()
  }
}

const getStatusTagType = (status) => {
  const statusMap = {
    'active': 'success',
    'inactive': 'info',
    'suspended': 'warning',
    'blacklisted': 'danger'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    'active': '活跃',
    'inactive': '非活跃',
    'suspended': '暂停',
    'blacklisted': '黑名单'
  }
  return statusMap[status] || status
}

const getCategoryTagType = (category) => {
  const categoryMap = {
    '电子元器件': 'primary',
    '机械配件': 'success',
    '化工原料': 'warning',
    '包装材料': 'info'
  }
  return categoryMap[category] || 'info'
}

const getTransactionStatusType = (status) => {
  const statusMap = {
    'pending': 'warning',
    'confirmed': 'primary',
    'shipped': 'info',
    'completed': 'success',
    'cancelled': 'danger'
  }
  return statusMap[status] || 'info'
}

const getTransactionStatusText = (status) => {
  const statusMap = {
    'pending': '待确认',
    'confirmed': '已确认',
    'shipped': '已发货',
    'completed': '已完成',
    'cancelled': '已取消'
  }
  return statusMap[status] || status
}

const getPaymentStatusType = (status) => {
  const statusMap = {
    'pending': 'warning',
    'partial': 'primary',
    'paid': 'success',
    'overdue': 'danger'
  }
  return statusMap[status] || 'info'
}

const getPaymentStatusText = (status) => {
  const statusMap = {
    'pending': '待付款',
    'partial': '部分付款',
    'paid': '已付款',
    'overdue': '逾期'
  }
  return statusMap[status] || status
}

const getRiskLevelText = (level) => {
  const levelMap = {
    'low': '低风险',
    'medium': '中风险',
    'high': '高风险'
  }
  return levelMap[level] || level
}

const getRiskLevelTagType = (level) => {
  const levelMap = {
    '低': 'success',
    '中': 'warning',
    '高': 'danger'
  }
  return levelMap[level] || 'info'
}

// 事件处理
const handleClose = () => {
  dialogVisible.value = false
}

const handleEditSupplier = () => {
  console.log('编辑供应商', supplier.value)
  ElMessage.info('编辑供应商功能')
}

const handleContactSupplier = () => {
  console.log('联系供应商', supplier.value)
  ElMessage.info('联系供应商功能')
}

const handleViewContracts = () => {
  console.log('查看合同', supplier.value)
  ElMessage.info('查看合同功能')
}

const handleViewHistory = () => {
  console.log('查看历史', supplier.value)
  ElMessage.info('查看历史功能')
}

const handleGenerateReport = () => {
  console.log('生成报告', supplier.value)
  ElMessage.info('生成报告功能')
}

const handleExportData = () => {
  console.log('导出数据', supplier.value)
  ElMessage.info('导出数据功能')
}

const handleRiskAssessment = () => {
  console.log('风险评估', supplier.value)
  ElMessage.info('风险评估功能')
}

const handleTransactionDateChange = () => {
  loadTransactionData()
}

const handleViewOrder = (order) => {
  console.log('查看订单', order)
  ElMessage.info(`查看订单: ${order.orderId}`)
}

const handleDownloadInvoice = (order) => {
  console.log('下载发票', order)
  ElMessage.info(`下载发票: ${order.orderId}`)
}

const handleViewRiskDetail = (risk) => {
  console.log('查看风险详情', risk)
  ElMessage.info(`查看风险详情: ${risk.title}`)
}

const handleRiskMitigation = (risk) => {
  console.log('风险应对', risk)
  ElMessage.info(`风险应对: ${risk.title}`)
}

const handleUploadDocument = () => {
  console.log('上传文档', supplier.value)
  ElMessage.info('上传文档功能')
}

const handleViewDocument = (doc) => {
  console.log('查看文档', doc)
  ElMessage.info(`查看文档: ${doc.name}`)
}

const handleDownloadDocument = (doc) => {
  console.log('下载文档', doc)
  ElMessage.info(`下载文档: ${doc.name}`)
}

const loadSupplierData = async () => {
  try {
    if (props.supplierId) {
      const detail = await getSupplierDetail(props.supplierId)
      supplier.value = { ...supplier.value, ...detail }
    }
  } catch (error) {
    console.error('加载供应商数据失败:', error)
  }
}

const loadTransactionData = async () => {
  try {
    if (props.supplierId) {
      const params = {
        supplierId: props.supplierId,
        dateRange: transactionDateRange.value
      }
      const transactions = await getSupplierTransactions(params)
      transactionData.value = transactions
    }
  } catch (error) {
    console.error('加载交易数据失败:', error)
  }
}

const loadPerformanceData = async () => {
  try {
    if (props.supplierId) {
      const performance = await getSupplierPerformance(props.supplierId)
      performanceScores.value = performance.scores
      performanceDetails.value = performance.details
    }
  } catch (error) {
    console.error('加载绩效数据失败:', error)
  }
}

const loadRiskData = async () => {
  try {
    if (props.supplierId) {
      const risk = await getSupplierRisk(props.supplierId)
      riskScore.value = risk.score
      riskLevel.value = risk.level
      riskDescription.value = risk.description
      riskItems.value = risk.items
    }
  } catch (error) {
    console.error('加载风险数据失败:', error)
  }
}

// 监听供应商ID变化
watch(() => props.supplierId, (newId) => {
  if (newId) {
    loadSupplierData()
    loadTransactionData()
    loadPerformanceData()
    loadRiskData()
  }
})

// 初始化
onMounted(() => {
  if (props.supplierId) {
    loadSupplierData()
    loadTransactionData()
    loadPerformanceData()
    loadRiskData()
  }
})
</script>

<style scoped>
.supplier-detail-dialog {
  .supplier-detail-content {
    .supplier-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 8px;
      color: white;
      margin-bottom: 30px;

      .supplier-info {
        display: flex;
        align-items: center;
        gap: 20px;

        .supplier-basic {
          .supplier-name {
            font-size: 24px;
            font-weight: bold;
            margin: 0 0 8px 0;
          }

          .supplier-code {
            margin: 0 0 16px 0;
            opacity: 0.9;
          }

          .supplier-tags {
            display: flex;
            gap: 8px;
            flex-wrap: wrap;
          }
        }
      }

      .supplier-actions {
        display: flex;
        gap: 12px;
      }
    }

    .key-metrics {
      margin-bottom: 30px;

      h3 {
        margin-bottom: 20px;
        color: #303133;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 8px;
      }

      .metric-card {
        display: flex;
        align-items: center;
        padding: 20px;
        background: white;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        transition: transform 0.2s;

        &:hover {
          transform: translateY(-2px);
        }

        .metric-icon {
          width: 48px;
          height: 48px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          color: white;
          font-size: 20px;
        }

        .metric-content {
          flex: 1;

          .metric-value {
            font-size: 24px;
            font-weight: bold;
            color: #303133;
            margin-bottom: 4px;
          }

          .metric-label {
            font-size: 14px;
            color: #606266;
            margin-bottom: 4px;
          }

          .metric-trend {
            font-size: 12px;
            display: flex;
            align-items: center;
            gap: 4px;

            &.positive {
              color: #67c23a;
            }

            &.negative {
              color: #f56c6c;
            }
          }
        }
      }
    }

    .detail-tabs {
      .basic-info-section {
        .info-group {
          background: white;
          padding: 20px;
          border-radius: 8px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

          h4 {
            color: #303133;
            font-weight: 600;
            margin-bottom: 16px;
            padding-bottom: 8px;
            border-bottom: 2px solid #409eff;
          }

          .info-item {
            display: flex;
            margin-bottom: 12px;
            align-items: flex-start;

            .info-label {
              font-weight: 600;
              color: #606266;
              width: 120px;
              flex-shrink: 0;
            }

            .info-value {
              color: #303133;
              flex: 1;
            }

            .cert-list, .product-tags {
              flex: 1;
            }
          }
        }
      }

      .transactions-section {
        .section-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;
        }
      }

      .performance-section {
        .performance-chart {
          background: white;
          padding: 20px;
          border-radius: 8px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
          margin-bottom: 20px;

          h4 {
            color: #303133;
            font-weight: 600;
            margin-bottom: 16px;
          }

          .chart-placeholder {
            height: 300px;
            background: #f5f7fa;
            border-radius: 8px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            color: #909399;

            .el-icon {
              font-size: 48px;
              margin-bottom: 16px;
            }

            p {
              font-size: 18px;
              font-weight: 600;
              margin-bottom: 8px;
            }

            small {
              font-size: 14px;
            }
          }
        }

        .performance-metrics {
          background: white;
          padding: 20px;
          border-radius: 8px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

          h4 {
            color: #303133;
            font-weight: 600;
            margin-bottom: 16px;
          }

          .score-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 16px;

            .score-label {
              color: #606266;
              font-weight: 600;
            }
          }
        }

        .performance-details {
          .detail-card {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            margin-bottom: 16px;

            .detail-header {
              display: flex;
              justify-content: space-between;
              align-items: center;
              margin-bottom: 8px;

              .detail-label {
                font-weight: 600;
                color: #303133;
              }
            }

            .detail-value {
              font-size: 24px;
              font-weight: bold;
              color: #409eff;
              margin-bottom: 8px;
            }

            .detail-description {
              color: #606266;
              font-size: 14px;
            }
          }
        }
      }

      .risk-section {
        .risk-overview {
          .risk-score-card {
            display: flex;
            align-items: center;
            gap: 30px;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

            .risk-score {
              .score-circle {
                width: 120px;
                height: 120px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                position: relative;

                .score-inner {
                  position: absolute;
                  top: 50%;
                  left: 50%;
                  transform: translate(-50%, -50%);
                  text-align: center;

                  .score-value {
                    display: block;
                    font-size: 28px;
                    font-weight: bold;
                    color: #303133;
                  }

                  .score-label {
                    font-size: 12px;
                    color: #606266;
                  }
                }
              }
            }

            .risk-description {
              h4 {
                color: #303133;
                font-weight: 600;
                margin-bottom: 12px;
              }

              p {
                color: #606266;
                line-height: 1.6;
                margin: 0;
              }
            }
          }
        }

        .risk-details {
          .risk-list {
            .risk-item {
              background: white;
              padding: 20px;
              border-radius: 8px;
              box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
              margin-bottom: 16px;

              .risk-header {
                display: flex;
                align-items: center;
                gap: 12px;
                margin-bottom: 12px;

                .risk-title {
                  font-weight: 600;
                  color: #303133;
                  flex: 1;
                }

                .risk-date {
                  font-size: 12px;
                  color: #909399;
                }
              }

              .risk-content {
                margin-bottom: 12px;

                p {
                  color: #606266;
                  margin: 0 0 8px 0;
                  line-height: 1.6;
                }

                .risk-metrics {
                  display: flex;
                  gap: 20px;

                  .risk-metric {
                    font-size: 14px;
                    color: #409eff;
                  }
                }
              }

              .risk-actions {
                display: flex;
                gap: 8px;
              }
            }
          }
        }
      }

      .documents-section {
        .section-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;
        }

        .document-grid {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
          gap: 20px;

          .document-card {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            transition: transform 0.2s;

            &:hover {
              transform: translateY(-2px);
            }

            .doc-icon {
              width: 48px;
              height: 48px;
              background: #409eff;
              border-radius: 8px;
              display: flex;
              align-items: center;
              justify-content: center;
              color: white;
              font-size: 24px;
              margin-bottom: 16px;
            }

            .doc-info {
              margin-bottom: 16px;

              h5 {
                color: #303133;
                font-weight: 600;
                margin: 0 0 8px 0;
              }

              p {
                color: #606266;
                font-size: 14px;
                margin: 0 0 8px 0;
                line-height: 1.6;
              }

              .doc-date {
                font-size: 12px;
                color: #909399;
              }
            }

            .doc-actions {
              display: flex;
              gap: 8px;
            }
          }
        }
      }
    }
  }
}

:deep(.el-dialog__body) {
  padding: 20px 30px;
}

:deep(.el-tabs--border-card) {
  border: 1px solid #e4e7ed;
  border-radius: 6px;
}

:deep(.el-rate) {
  .el-rate__text {
    font-size: 12px;
  }
}
</style>