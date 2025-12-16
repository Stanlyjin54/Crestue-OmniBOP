<template>
  <div class="contract-detail-dialog">
    <el-dialog
      v-model="dialogVisible"
      title="合同详情"
      width="1200px"
      :before-close="handleClose"
    >
      <div class="contract-detail-content">
        <!-- 合同基本信息 -->
        <div class="contract-header">
          <div class="contract-info">
            <div class="contract-icon">
              <el-icon><Document /></el-icon>
            </div>
            <div class="contract-basic">
              <h2 class="contract-title">{{ contract.contractNumber }}</h2>
              <p class="contract-name">{{ contract.name }}</p>
              <div class="contract-tags">
                <el-tag :type="getStatusTagType(contract.status)" size="large">
                  {{ getStatusText(contract.status) }}
                </el-tag>
                <el-tag :type="getTypeTagType(contract.type)" size="large">
                  {{ getTypeText(contract.type) }}
                </el-tag>
                <el-tag v-if="contract.isKey" type="warning" size="large">
                  重点合同
                </el-tag>
              </div>
            </div>
          </div>
          <div class="contract-actions">
            <el-button type="primary" @click="handleEditContract">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button @click="handleRenewContract">
              <el-icon><Refresh /></el-icon>
              续约
            </el-button>
            <el-button @click="handleTerminateContract">
              <el-icon><Close /></el-icon>
              终止
            </el-button>
            <el-dropdown>
              <el-button>
                更多<el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleViewHistory">历史记录</el-dropdown-item>
                  <el-dropdown-item @click="handleGenerateReport">生成报告</el-dropdown-item>
                  <el-dropdown-item @click="handleExportContract">导出合同</el-dropdown-item>
                  <el-dropdown-item @click="handleSendReminder" divided>发送提醒</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>

        <!-- 关键信息卡片 -->
        <div class="key-info-cards">
          <el-row :gutter="20">
            <el-col :span="6" v-for="info in keyInfo" :key="info.key">
              <div class="info-card">
                <div class="info-icon" :style="{ backgroundColor: info.color }">
                  <el-icon><component :is="info.icon" /></el-icon>
                </div>
                <div class="info-content">
                  <div class="info-value">{{ formatValue(info.value, info.type) }}</div>
                  <div class="info-label">{{ info.label }}</div>
                  <div class="info-status" :class="info.status">
                    {{ info.statusText }}
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
                      <h4>合同信息</h4>
                      <div class="info-item">
                        <span class="info-label">合同名称:</span>
                        <span class="info-value">{{ contract.name }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">合同编号:</span>
                        <span class="info-value">{{ contract.contractNumber }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">合同类型:</span>
                        <span class="info-value">{{ getTypeText(contract.type) }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">合同状态:</span>
                        <span class="info-value">{{ getStatusText(contract.status) }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">签订日期:</span>
                        <span class="info-value">{{ contract.signDate }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">生效日期:</span>
                        <span class="info-value">{{ contract.effectiveDate }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">到期日期:</span>
                        <span class="info-value">{{ contract.expireDate }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">剩余天数:</span>
                        <span class="info-value">{{ contract.remainingDays }}天</span>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="12">
                    <div class="info-group">
                      <h4>财务信息</h4>
                      <div class="info-item">
                        <span class="info-label">合同金额:</span>
                        <span class="info-value">{{ formatCurrency(contract.amount) }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">已付金额:</span>
                        <span class="info-value">{{ formatCurrency(contract.paidAmount) }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">未付金额:</span>
                        <span class="info-value">{{ formatCurrency(contract.unpaidAmount) }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">付款方式:</span>
                        <span class="info-value">{{ getPaymentMethodText(contract.paymentMethod) }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">付款条件:</span>
                        <span class="info-value">{{ contract.paymentTerms }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">发票类型:</span>
                        <span class="info-value">{{ getInvoiceTypeText(contract.invoiceType) }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">税率:</span>
                        <span class="info-value">{{ contract.taxRate }}%</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">质保金:</span>
                        <span class="info-value">{{ contract.retentionAmount ? formatCurrency(contract.retentionAmount) : '无' }}</span>
                      </div>
                    </div>
                  </el-col>
                </el-row>

                <el-row :gutter="20" style="margin-top: 20px;">
                  <el-col :span="12">
                    <div class="info-group">
                      <h4>甲方信息</h4>
                      <div class="info-item">
                        <span class="info-label">甲方名称:</span>
                        <span class="info-value">{{ contract.partyA.name }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">甲方代表:</span>
                        <span class="info-value">{{ contract.partyA.representative }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">联系电话:</span>
                        <span class="info-value">{{ contract.partyA.phone }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">联系地址:</span>
                        <span class="info-value">{{ contract.partyA.address }}</span>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="12">
                    <div class="info-group">
                      <h4>乙方信息</h4>
                      <div class="info-item">
                        <span class="info-label">乙方名称:</span>
                        <span class="info-value">{{ contract.partyB.name }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">乙方代表:</span>
                        <span class="info-value">{{ contract.partyB.representative }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">联系电话:</span>
                        <span class="info-value">{{ contract.partyB.phone }}</span>
                      </div>
                      <div class="info-item">
                        <span class="info-label">联系地址:</span>
                        <span class="info-value">{{ contract.partyB.address }}</span>
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </el-tab-pane>

            <!-- 合同条款 -->
            <el-tab-pane label="合同条款" name="terms">
              <div class="terms-section">
                <div class="terms-content">
                  <div class="term-group">
                    <h4>主要条款</h4>
                    <div class="term-item">
                      <strong>质量标准:</strong>
                      <p>{{ contract.terms.qualityStandard }}</p>
                    </div>
                    <div class="term-item">
                      <strong>交付要求:</strong>
                      <p>{{ contract.terms.deliveryRequirement }}</p>
                    </div>
                    <div class="term-item">
                      <strong>验收标准:</strong>
                      <p>{{ contract.terms.acceptanceStandard }}</p>
                    </div>
                    <div class="term-item">
                      <strong>服务要求:</strong>
                      <p>{{ contract.terms.serviceRequirement }}</p>
                    </div>
                  </div>

                  <div class="term-group">
                    <h4>责任条款</h4>
                    <div class="term-item">
                      <strong>违约责任:</strong>
                      <p>{{ contract.terms.breachResponsibility }}</p>
                    </div>
                    <div class="term-item">
                      <strong>赔偿责任:</strong>
                      <p>{{ contract.terms.compensationResponsibility }}</p>
                    </div>
                    <div class="term-item">
                      <strong>争议解决:</strong>
                      <p>{{ contract.terms.disputeResolution }}</p>
                    </div>
                  </div>

                  <div class="term-group">
                    <h4>其他条款</h4>
                    <div class="term-item">
                      <strong>保密条款:</strong>
                      <p>{{ contract.terms.confidentiality }}</p>
                    </div>
                    <div class="term-item">
                      <strong>知识产权:</strong>
                      <p>{{ contract.terms.intellectualProperty }}</p>
                    </div>
                    <div class="term-item">
                      <strong>变更条款:</strong>
                      <p>{{ contract.terms.changeClause }}</p>
                    </div>
                    <div class="term-item">
                      <strong>终止条款:</strong>
                      <p>{{ contract.terms.terminationClause }}</p>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 执行进度 -->
            <el-tab-pane label="执行进度" name="progress">
              <div class="progress-section">
                <div class="progress-overview">
                  <div class="progress-circle">
                    <el-progress 
                      type="circle" 
                      :percentage="contract.progress" 
                      :stroke-width="10"
                      :color="getProgressColor(contract.progress)"
                    />
                    <div class="progress-text">
                      <span class="progress-label">整体进度</span>
                      <span class="progress-value">{{ contract.progress }}%</span>
                    </div>
                  </div>
                  <div class="progress-details">
                    <div class="detail-item">
                      <span class="detail-label">已完成任务:</span>
                      <span class="detail-value">{{ completedTasks }}/{{ totalTasks }}</span>
                    </div>
                    <div class="detail-item">
                      <span class="detail-label">时间进度:</span>
                      <span class="detail-value">{{ timeProgress }}%</span>
                    </div>
                    <div class="detail-item">
                      <span class="detail-label">质量评级:</span>
                      <el-rate v-model="qualityRating" disabled />
                    </div>
                  </div>
                </div>

                <div class="milestone-timeline">
                  <h4>里程碑进度</h4>
                  <div class="timeline">
                    <div v-for="milestone in milestones" :key="milestone.id" class="timeline-item">
                      <div class="timeline-marker" :class="milestone.status">
                        <el-icon><Check v-if="milestone.status === 'completed'" /><Clock v-else-if="milestone.status === 'current'" /></el-icon>
                      </div>
                      <div class="timeline-content">
                        <h5>{{ milestone.name }}</h5>
                        <p>{{ milestone.description }}</p>
                        <div class="timeline-meta">
                          <span class="timeline-date">{{ milestone.date }}</span>
                          <span class="timeline-status" :class="milestone.status">
                            {{ getMilestoneStatusText(milestone.status) }}
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 付款记录 -->
            <el-tab-pane label="付款记录" name="payments">
              <div class="payments-section">
                <div class="payment-summary">
                  <el-row :gutter="20">
                    <el-col :span="8">
                      <div class="summary-card">
                        <div class="summary-icon" style="background-color: #67c23a;">
                          <el-icon><Money /></el-icon>
                        </div>
                        <div class="summary-content">
                          <div class="summary-value">{{ formatCurrency(totalPaid) }}</div>
                          <div class="summary-label">已付款总额</div>
                        </div>
                      </div>
                    </el-col>
                    <el-col :span="8">
                      <div class="summary-card">
                        <div class="summary-icon" style="background-color: #e6a23c;">
                          <el-icon><Wallet /></el-icon>
                        </div>
                        <div class="summary-content">
                          <div class="summary-value">{{ formatCurrency(totalUnpaid) }}</div>
                          <div class="summary-label">待付款金额</div>
                        </div>
                      </div>
                    </el-col>
                    <el-col :span="8">
                      <div class="summary-card">
                        <div class="summary-icon" style="background-color: #409eff;">
                          <el-icon><Timer /></el-icon>
                        </div>
                        <div class="summary-content">
                          <div class="summary-value">{{ paymentCount }}</div>
                          <div class="summary-label">付款次数</div>
                        </div>
                      </div>
                    </el-col>
                  </el-row>
                </div>

                <div class="payment-history">
                  <h4>付款记录</h4>
                  <el-table :data="paymentHistory" stripe style="width: 100%" :height="400">
                    <el-table-column prop="paymentDate" label="付款日期" width="120" />
                    <el-table-column prop="paymentAmount" label="付款金额" width="120">
                      <template #default="{ row }">
                        ¥{{ row.paymentAmount.toLocaleString() }}
                      </template>
                    </el-table-column>
                    <el-table-column prop="paymentMethod" label="付款方式" width="100">
                      <template #default="{ row }">
                        {{ getPaymentMethodText(row.paymentMethod) }}
                      </template>
                    </el-table-column>
                    <el-table-column prop="paymentAccount" label="付款账户" width="150" />
                    <el-table-column prop="paymentStatus" label="付款状态" width="100">
                      <template #default="{ row }">
                        <el-tag :type="getPaymentStatusType(row.paymentStatus)">
                          {{ getPaymentStatusText(row.paymentStatus) }}
                        </el-tag>
                      </template>
                    </el-table-column>
                    <el-table-column prop="invoiceNumber" label="发票号码" width="150" />
                    <el-table-column prop="remark" label="备注" width="200" />
                    <el-table-column label="操作" width="120" fixed="right">
                      <template #default="{ row }">
                        <el-button size="small" @click="handleViewPayment(row)">
                          查看
                        </el-button>
                        <el-button size="small" @click="handleDownloadReceipt(row)">
                          收据
                        </el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </div>
            </el-tab-pane>

            <!-- 文档附件 -->
            <el-tab-pane label="文档附件" name="documents">
              <div class="documents-section">
                <div class="section-header">
                  <h4>合同文档</h4>
                  <el-button type="primary" @click="handleUploadDocument">
                    <el-icon><Upload /></el-icon>
                    上传文档
                  </el-button>
                </div>

                <div class="document-categories">
                  <el-tabs v-model="activeDocumentTab">
                    <el-tab-pane label="合同正文" name="main">
                      <div class="document-list">
                        <div v-for="doc in mainDocuments" :key="doc.id" class="document-item">
                          <div class="doc-icon">
                            <el-icon><Document /></el-icon>
                          </div>
                          <div class="doc-info">
                            <h5>{{ doc.name }}</h5>
                            <p>{{ doc.description }}</p>
                            <div class="doc-meta">
                              <span class="doc-date">{{ doc.uploadDate }}</span>
                              <span class="doc-size">{{ doc.size }}</span>
                            </div>
                          </div>
                          <div class="doc-actions">
                            <el-button size="small" @click="handleViewDocument(doc)">
                              <el-icon><View /></el-icon>
                              查看
                            </el-button>
                            <el-button size="small" @click="handleDownloadDocument(doc)">
                              <el-icon><Download /></el-icon>
                              下载
                            </el-button>
                            <el-button size="small" @click="handleSignDocument(doc)">
                              <el-icon><EditPen /></el-icon>
                              签署
                            </el-button>
                          </div>
                        </div>
                      </div>
                    </el-tab-pane>

                    <el-tab-pane label="补充协议" name="supplementary">
                      <div class="document-list">
                        <div v-for="doc in supplementaryDocuments" :key="doc.id" class="document-item">
                          <div class="doc-icon">
                            <el-icon><Document /></el-icon>
                          </div>
                          <div class="doc-info">
                            <h5>{{ doc.name }}</h5>
                            <p>{{ doc.description }}</p>
                            <div class="doc-meta">
                              <span class="doc-date">{{ doc.uploadDate }}</span>
                              <span class="doc-size">{{ doc.size }}</span>
                            </div>
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

                    <el-tab-pane label="发票文件" name="invoices">
                      <div class="document-list">
                        <div v-for="doc in invoiceDocuments" :key="doc.id" class="document-item">
                          <div class="doc-icon">
                            <el-icon><Tickets /></el-icon>
                          </div>
                          <div class="doc-info">
                            <h5>{{ doc.name }}</h5>
                            <p>{{ doc.description }}</p>
                            <div class="doc-meta">
                              <span class="doc-date">{{ doc.uploadDate }}</span>
                              <span class="doc-size">{{ doc.size }}</span>
                            </div>
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
                      <div class="document-list">
                        <div v-for="doc in otherDocuments" :key="doc.id" class="document-item">
                          <div class="doc-icon">
                            <el-icon><FolderOpened /></el-icon>
                          </div>
                          <div class="doc-info">
                            <h5>{{ doc.name }}</h5>
                            <p>{{ doc.description }}</p>
                            <div class="doc-meta">
                              <span class="doc-date">{{ doc.uploadDate }}</span>
                              <span class="doc-size">{{ doc.size }}</span>
                            </div>
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

            <!-- 风险监控 -->
            <el-tab-pane label="风险监控" name="risk">
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

                <div class="risk-alerts" style="margin-top: 30px;">
                  <h4>风险预警</h4>
                  <div class="alert-list">
                    <div v-for="alert in riskAlerts" :key="alert.id" class="alert-item">
                      <div class="alert-icon" :class="alert.level">
                        <el-icon><WarningFilled /></el-icon>
                      </div>
                      <div class="alert-content">
                        <h5>{{ alert.title }}</h5>
                        <p>{{ alert.description }}</p>
                        <div class="alert-meta">
                          <span class="alert-date">{{ alert.date }}</span>
                          <span class="alert-level" :class="alert.level">
                            {{ getAlertLevelText(alert.level) }}
                          </span>
                        </div>
                      </div>
                      <div class="alert-actions">
                        <el-button size="small" @click="handleViewAlert(alert)">
                          查看
                        </el-button>
                        <el-button size="small" @click="handleHandleAlert(alert)">
                          处理
                        </el-button>
                      </div>
                    </div>
                  </div>
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
  Document, Edit, Refresh, Close, ArrowDown, Money, Wallet, Timer,
  Upload, View, Download, EditPen, Tickets, FolderOpened, WarningFilled,
  Check, Clock
} from '@element-plus/icons-vue'
import { useProcurementAdvanced } from '@/composables/useProcurementAdvanced'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  contractId: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:visible'])

// 组合式函数
const {
  getContractDetail,
  getContractPayments,
  getContractProgress
} = useProcurementAdvanced()

// 响应式数据
const dialogVisible = computed({
  get: () => props.visible,
  set: (val) => emit('update:visible', val)
})

// 标签页配置
const activeTab = ref('basic')
const activeDocumentTab = ref('main')

// 合同数据
const contract = ref({
  id: 'CONTRACT001',
  name: '电子元器件采购框架协议',
  contractNumber: 'HT-2024-001',
  type: 'framework',
  status: 'active',
  isKey: true,
  signDate: '2024-01-15',
  effectiveDate: '2024-02-01',
  expireDate: '2025-01-31',
  remainingDays: 365,
  amount: 5000000,
  paidAmount: 3000000,
  unpaidAmount: 2000000,
  paymentMethod: 'bank_transfer',
  paymentTerms: '30天账期，验收合格后付款',
  invoiceType: 'vat_special',
  taxRate: 13,
  retentionAmount: 250000,
  progress: 65,
  partyA: {
    name: '某某科技有限公司',
    representative: '张总',
    phone: '010-12345678',
    address: '北京市朝阳区某某路123号'
  },
  partyB: {
    name: '某某供应商有限公司',
    representative: '李经理',
    phone: '021-87654321',
    address: '上海市浦东新区某某路456号'
  },
  terms: {
    qualityStandard: '按照国家标准GB/T执行，质量等级为A级',
    deliveryRequirement: '分批交货，每批不超过30天，最迟不超过合同有效期',
    acceptanceStandard: '按照国家标准进行验收，合格率不低于99.5%',
    serviceRequirement: '7×24小时技术支持，48小时响应，72小时解决',
    breachResponsibility: '违约方承担合同总额10%的违约金',
    compensationResponsibility: '因违约造成的直接损失，违约方承担赔偿责任',
    disputeResolution: '协商不成，提交北京仲裁委员会仲裁',
    confidentiality: '双方对商业秘密和技术资料承担保密义务',
    intellectualProperty: '知识产权归原权利人所有',
    changeClause: '合同变更需双方书面同意',
    terminationClause: '任一方可提前30天书面通知终止合同'
  }
})

// 关键信息卡片
const keyInfo = ref([
  {
    key: 'progress',
    label: '执行进度',
    value: 65,
    type: 'percentage',
    status: 'normal',
    statusText: '进行中',
    color: '#409eff',
    icon: 'DataAnalysis'
  },
  {
    key: 'remaining',
    label: '剩余天数',
    value: 365,
    type: 'number',
    status: 'good',
    statusText: '充足',
    color: '#67c23a',
    icon: 'Calendar'
  },
  {
    key: 'payment',
    label: '付款进度',
    value: 60,
    type: 'percentage',
    status: 'normal',
    statusText: '正常',
    color: '#e6a23c',
    icon: 'Money'
  },
  {
    key: 'quality',
    label: '质量评级',
    value: 4.5,
    type: 'rating',
    status: 'good',
    statusText: '优秀',
    color: '#f56c6c',
    icon: 'Star'
  }
])

// 执行进度数据
const completedTasks = ref(13)
const totalTasks = ref(20)
const timeProgress = ref(45)
const qualityRating = ref(4.5)

const milestones = ref([
  {
    id: 1,
    name: '合同签订',
    description: '双方正式签署采购框架协议',
    date: '2024-02-01',
    status: 'completed'
  },
  {
    id: 2,
    name: '首批交付',
    description: '完成首批电子元器件交付',
    date: '2024-03-15',
    status: 'completed'
  },
  {
    id: 3,
    name: '中期评估',
    description: '对合同执行情况进行中期评估',
    date: '2024-06-30',
    status: 'current'
  },
  {
    id: 4,
    name: '年度总结',
    description: '完成年度合同执行总结',
    date: '2024-12-31',
    status: 'pending'
  }
])

// 付款记录数据
const totalPaid = ref(3000000)
const totalUnpaid = ref(2000000)
const paymentCount = ref(8)

const paymentHistory = ref([
  {
    id: 1,
    paymentDate: '2024-03-20',
    paymentAmount: 500000,
    paymentMethod: 'bank_transfer',
    paymentAccount: '招商银行***1234',
    paymentStatus: 'completed',
    invoiceNumber: 'INV-2024-001',
    remark: '首批货款'
  },
  {
    id: 2,
    paymentDate: '2024-05-15',
    paymentAmount: 1000000,
    paymentMethod: 'bank_transfer',
    paymentAccount: '招商银行***1234',
    paymentStatus: 'completed',
    invoiceNumber: 'INV-2024-002',
    remark: '二批货款'
  },
  {
    id: 3,
    paymentDate: '2024-07-10',
    paymentAmount: 1500000,
    paymentMethod: 'bank_transfer',
    paymentAccount: '招商银行***1234',
    paymentStatus: 'pending',
    invoiceNumber: 'INV-2024-003',
    remark: '三批货款'
  }
])

// 文档数据
const mainDocuments = ref([
  {
    id: 1,
    name: '合同正文.pdf',
    description: '合同主要条款和内容',
    uploadDate: '2024-01-15',
    size: '2.5MB'
  }
])

const supplementaryDocuments = ref([
  {
    id: 1,
    name: '补充协议001.pdf',
    description: '关于交付时间的补充协议',
    uploadDate: '2024-03-01',
    size: '1.2MB'
  }
])

const invoiceDocuments = ref([
  {
    id: 1,
    name: '发票INV-2024-001.pdf',
    description: '首批货款发票',
    uploadDate: '2024-03-20',
    size: '0.8MB'
  }
])

const otherDocuments = ref([
  {
    id: 1,
    name: '验收报告.pdf',
    description: '首批产品验收报告',
    uploadDate: '2024-03-25',
    size: '1.5MB'
  }
])

// 风险数据
const riskScore = ref(45)
const riskLevel = ref('low')
const riskDescription = ref('合同执行情况良好，风险较低。')

const riskAlerts = ref([
  {
    id: 1,
    level: 'warning',
    title: '付款提醒',
    description: '三批货款即将到期，请及时安排付款',
    date: '2024-07-05',
    status: 'unread'
  },
  {
    id: 2,
    level: 'info',
    title: '进度更新',
    description: '供应商已提交第二批产品交付申请',
    date: '2024-07-01',
    status: 'read'
  }
])

// 工具方法
const formatCurrency = (amount) => {
  return `¥${amount.toLocaleString()}`
}

const formatValue = (value, type) => {
  switch (type) {
    case 'currency':
      return formatCurrency(value)
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
    'draft': 'info',
    'pending': 'warning',
    'active': 'success',
    'completed': 'success',
    'terminated': 'danger',
    'expired': 'info'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    'draft': '草稿',
    'pending': '待确认',
    'active': '生效',
    'completed': '已完成',
    'terminated': '已终止',
    'expired': '已过期'
  }
  return statusMap[status] || status
}

const getTypeTagType = (type) => {
  const typeMap = {
    'framework': 'primary',
    'purchase': 'success',
    'service': 'warning',
    'maintenance': 'info'
  }
  return typeMap[type] || 'info'
}

const getTypeText = (type) => {
  const typeMap = {
    'framework': '框架协议',
    'purchase': '采购合同',
    'service': '服务合同',
    'maintenance': '维护合同'
  }
  return typeMap[type] || type
}

const getPaymentMethodText = (method) => {
  const methodMap = {
    'cash': '现金',
    'bank_transfer': '银行转账',
    'check': '支票',
    'letter_of_credit': '信用证',
    'online_payment': '在线支付'
  }
  return methodMap[method] || method
}

const getInvoiceTypeText = (type) => {
  const typeMap = {
    'vat_general': '增值税普通发票',
    'vat_special': '增值税专用发票',
    'electronic': '电子发票'
  }
  return typeMap[type] || type
}

const getProgressColor = (progress) => {
  if (progress >= 80) return '#67c23a'
  if (progress >= 60) return '#e6a23c'
  if (progress >= 40) return '#f56c6c'
  return '#909399'
}

const getMilestoneStatusText = (status) => {
  const statusMap = {
    'completed': '已完成',
    'current': '进行中',
    'pending': '待开始'
  }
  return statusMap[status] || status
}

const getPaymentStatusType = (status) => {
  const statusMap = {
    'pending': 'warning',
    'processing': 'primary',
    'completed': 'success',
    'failed': 'danger'
  }
  return statusMap[status] || 'info'
}

const getPaymentStatusText = (status) => {
  const statusMap = {
    'pending': '待处理',
    'processing': '处理中',
    'completed': '已完成',
    'failed': '失败'
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

const getAlertLevelText = (level) => {
  const levelMap = {
    'info': '信息',
    'warning': '警告',
    'error': '错误'
  }
  return levelMap[level] || level
}

// 事件处理
const handleClose = () => {
  dialogVisible.value = false
}

const handleEditContract = () => {
  console.log('编辑合同', contract.value)
  ElMessage.info('编辑合同功能')
}

const handleRenewContract = () => {
  console.log('续约合同', contract.value)
  ElMessage.info('续约合同功能')
}

const handleTerminateContract = () => {
  ElMessageBox.confirm('确定要终止此合同吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    console.log('终止合同', contract.value)
    ElMessage.success('合同已终止')
  })
}

const handleViewHistory = () => {
  console.log('查看历史', contract.value)
  ElMessage.info('查看历史功能')
}

const handleGenerateReport = () => {
  console.log('生成报告', contract.value)
  ElMessage.info('生成报告功能')
}

const handleExportContract = () => {
  console.log('导出合同', contract.value)
  ElMessage.info('导出合同功能')
}

const handleSendReminder = () => {
  console.log('发送提醒', contract.value)
  ElMessage.info('发送提醒功能')
}

const handleViewPayment = (payment) => {
  console.log('查看付款', payment)
  ElMessage.info(`查看付款: ${payment.id}`)
}

const handleDownloadReceipt = (payment) => {
  console.log('下载收据', payment)
  ElMessage.info(`下载收据: ${payment.id}`)
}

const handleUploadDocument = () => {
  console.log('上传文档', contract.value)
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

const handleSignDocument = (doc) => {
  console.log('签署文档', doc)
  ElMessage.info(`签署文档: ${doc.name}`)
}

const handleViewAlert = (alert) => {
  console.log('查看预警', alert)
  ElMessage.info(`查看预警: ${alert.title}`)
}

const handleHandleAlert = (alert) => {
  console.log('处理预警', alert)
  ElMessage.info(`处理预警: ${alert.title}`)
}

const loadContractData = async () => {
  try {
    if (props.contractId) {
      const detail = await getContractDetail(props.contractId)
      contract.value = { ...contract.value, ...detail }
    }
  } catch (error) {
    console.error('加载合同数据失败:', error)
  }
}

const loadPaymentData = async () => {
  try {
    if (props.contractId) {
      const payments = await getContractPayments(props.contractId)
      paymentHistory.value = payments
    }
  } catch (error) {
    console.error('加载付款数据失败:', error)
  }
}

const loadProgressData = async () => {
  try {
    if (props.contractId) {
      const progress = await getContractProgress(props.contractId)
      completedTasks.value = progress.completed
      totalTasks.value = progress.total
      milestones.value = progress.milestones
    }
  } catch (error) {
    console.error('加载进度数据失败:', error)
  }
}

// 监听合同ID变化
watch(() => props.contractId, (newId) => {
  if (newId) {
    loadContractData()
    loadPaymentData()
    loadProgressData()
  }
})

// 初始化
onMounted(() => {
  if (props.contractId) {
    loadContractData()
    loadPaymentData()
    loadProgressData()
  }
})
</script>

<style scoped>
.contract-detail-dialog {
  .contract-detail-content {
    .contract-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 8px;
      color: white;
      margin-bottom: 30px;

      .contract-info {
        display: flex;
        align-items: center;
        gap: 20px;

        .contract-icon {
          width: 60px;
          height: 60px;
          background: rgba(255, 255, 255, 0.2);
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 30px;
        }

        .contract-basic {
          .contract-title {
            font-size: 20px;
            font-weight: bold;
            margin: 0 0 8px 0;
          }

          .contract-name {
            margin: 0 0 16px 0;
            opacity: 0.9;
            font-size: 16px;
          }

          .contract-tags {
            display: flex;
            gap: 8px;
            flex-wrap: wrap;
          }
        }
      }

      .contract-actions {
        display: flex;
        gap: 12px;
      }
    }

    .key-info-cards {
      margin-bottom: 30px;

      .info-card {
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

        .info-icon {
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

        .info-content {
          flex: 1;

          .info-value {
            font-size: 24px;
            font-weight: bold;
            color: #303133;
            margin-bottom: 4px;
          }

          .info-label {
            font-size: 14px;
            color: #606266;
            margin-bottom: 4px;
          }

          .info-status {
            font-size: 12px;
            font-weight: 600;

            &.good {
              color: #67c23a;
            }

            &.normal {
              color: #e6a23c;
            }

            &.warning {
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
          }
        }
      }

      .terms-section {
        .terms-content {
          .term-group {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;

            h4 {
              color: #303133;
              font-weight: 600;
              margin-bottom: 16px;
              padding-bottom: 8px;
              border-bottom: 2px solid #409eff;
            }

            .term-item {
              margin-bottom: 16px;

              strong {
                color: #303133;
                display: block;
                margin-bottom: 8px;
              }

              p {
                color: #606266;
                line-height: 1.6;
                margin: 0;
                padding-left: 16px;
                border-left: 3px solid #e4e7ed;
              }
            }
          }
        }
      }

      .progress-section {
        .progress-overview {
          display: flex;
          align-items: center;
          gap: 40px;
          background: white;
          padding: 30px;
          border-radius: 8px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
          margin-bottom: 30px;

          .progress-circle {
            position: relative;

            .progress-text {
              position: absolute;
              top: 50%;
              left: 50%;
              transform: translate(-50%, -50%);
              text-align: center;

              .progress-label {
                display: block;
                font-size: 14px;
                color: #606266;
                margin-bottom: 4px;
              }

              .progress-value {
                display: block;
                font-size: 20px;
                font-weight: bold;
                color: #303133;
              }
            }
          }

          .progress-details {
            flex: 1;

            .detail-item {
              display: flex;
              justify-content: space-between;
              margin-bottom: 16px;

              .detail-label {
                color: #606266;
                font-weight: 600;
              }

              .detail-value {
                color: #303133;
                font-weight: bold;
              }
            }
          }
        }

        .milestone-timeline {
          h4 {
            color: #303133;
            font-weight: 600;
            margin-bottom: 20px;
          }

          .timeline {
            .timeline-item {
              display: flex;
              align-items: flex-start;
              margin-bottom: 30px;
              position: relative;

              &:not(:last-child)::after {
                content: '';
                position: absolute;
                left: 20px;
                top: 50px;
                width: 2px;
                height: calc(100% - 30px);
                background: #e4e7ed;
              }

              .timeline-marker {
                width: 40px;
                height: 40px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                margin-right: 20px;
                flex-shrink: 0;
                position: relative;
                z-index: 1;

                &.completed {
                  background: #67c23a;
                  color: white;
                }

                &.current {
                  background: #409eff;
                  color: white;
                }

                &.pending {
                  background: #f0f0f0;
                  color: #909399;
                }
              }

              .timeline-content {
                flex: 1;
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

                h5 {
                  color: #303133;
                  font-weight: 600;
                  margin: 0 0 8px 0;
                }

                p {
                  color: #606266;
                  margin: 0 0 12px 0;
                  line-height: 1.6;
                }

                .timeline-meta {
                  display: flex;
                  justify-content: space-between;
                  align-items: center;

                  .timeline-date {
                    font-size: 14px;
                    color: #909399;
                  }

                  .timeline-status {
                    font-size: 12px;
                    padding: 4px 8px;
                    border-radius: 12px;

                    &.completed {
                      background: #f0f9ff;
                      color: #67c23a;
                    }

                    &.current {
                      background: #f0f9ff;
                      color: #409eff;
                    }

                    &.pending {
                      background: #f4f4f5;
                      color: #909399;
                    }
                  }
                }
              }
            }
          }
        }
      }

      .payments-section {
        .payment-summary {
          margin-bottom: 30px;

          .summary-card {
            display: flex;
            align-items: center;
            padding: 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

            .summary-icon {
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

            .summary-content {
              .summary-value {
                font-size: 24px;
                font-weight: bold;
                color: #303133;
                margin-bottom: 4px;
              }

              .summary-label {
                font-size: 14px;
                color: #606266;
              }
            }
          }
        }

        .payment-history {
          h4 {
            color: #303133;
            font-weight: 600;
            margin-bottom: 20px;
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

        .document-list {
          .document-item {
            display: flex;
            align-items: center;
            padding: 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            margin-bottom: 16px;
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
              margin-right: 16px;
            }

            .doc-info {
              flex: 1;

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

              .doc-meta {
                display: flex;
                gap: 16px;

                .doc-date,
                .doc-size {
                  font-size: 12px;
                  color: #909399;
                }
              }
            }

            .doc-actions {
              display: flex;
              gap: 8px;
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

        .risk-alerts {
          h4 {
            color: #303133;
            font-weight: 600;
            margin-bottom: 20px;
          }

          .alert-list {
            .alert-item {
              display: flex;
              align-items: flex-start;
              padding: 20px;
              background: white;
              border-radius: 8px;
              box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
              margin-bottom: 16px;

              .alert-icon {
                width: 40px;
                height: 40px;
                border-radius: 8px;
                display: flex;
                align-items: center;
                justify-content: center;
                margin-right: 16px;
                font-size: 20px;

                &.info {
                  background: #f0f9ff;
                  color: #409eff;
                }

                &.warning {
                  background: #fdf6ec;
                  color: #e6a23c;
                }

                &.error {
                  background: #fef0f0;
                  color: #f56c6c;
                }
              }

              .alert-content {
                flex: 1;

                h5 {
                  color: #303133;
                  font-weight: 600;
                  margin: 0 0 8px 0;
                }

                p {
                  color: #606266;
                  line-height: 1.6;
                  margin: 0 0 8px 0;
                }

                .alert-meta {
                  display: flex;
                  justify-content: space-between;
                  align-items: center;

                  .alert-date {
                    font-size: 12px;
                    color: #909399;
                  }

                  .alert-level {
                    font-size: 12px;
                    padding: 4px 8px;
                    border-radius: 12px;

                    &.info {
                      background: #f0f9ff;
                      color: #409eff;
                    }

                    &.warning {
                      background: #fdf6ec;
                      color: #e6a23c;
                    }

                    &.error {
                      background: #fef0f0;
                      color: #f56c6c;
                    }
                  }
                }
              }

              .alert-actions {
                display: flex;
                gap: 8px;
                margin-left: 16px;
              }
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
</style>