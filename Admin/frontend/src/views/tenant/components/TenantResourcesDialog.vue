<template>
  <el-dialog
    v-model="dialogVisible"
    title="租户资源监控"
    width="1000px"
    :close-on-click-modal="false"
  >
    <div class="resources-container">
      <!-- 租户基本信息 -->
      <div class="tenant-info">
        <el-card shadow="never">
          <template #header>
            <span>租户信息</span>
          </template>
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="info-item">
                <span class="info-label">租户名称：</span>
                <span class="info-value">{{ tenant?.tenantName }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <span class="info-label">租户类型：</span>
                <el-tag :color="TENANT_TYPES[tenant?.tenantType]?.color" style="color: white" size="small">
                  {{ TENANT_TYPES[tenant?.tenantType]?.label }}
                </el-tag>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <span class="info-label">状态：</span>
                <el-tag :type="TENANT_STATUS[tenant?.status]?.tagType" size="small">
                  {{ TENANT_STATUS[tenant?.status]?.label }}
                </el-tag>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </div>

      <!-- 资源使用统计 -->
      <div class="resource-stats">
        <el-row :gutter="20">
          <el-col :span="6" v-for="(resource, key) in resourceStats" :key="key">
            <el-card shadow="never">
              <div class="resource-card">
                <div class="resource-icon" :style="{ color: resource.color }">
                  <el-icon><component :is="resource.icon" /></el-icon>
                </div>
                <div class="resource-info">
                  <div class="resource-value">{{ resource.value }}</div>
                  <div class="resource-label">{{ resource.label }}</div>
                  <div class="resource-usage">
                    <el-progress 
                      :percentage="resource.usagePercent" 
                      :stroke-width="8"
                      :show-text="false"
                      :status="resource.usagePercent > 80 ? 'exception' : ''"
                    />
                    <span class="usage-text">{{ resource.usagePercent }}%</span>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 资源使用图表 -->
      <div class="resource-charts">
        <el-tabs v-model="activeChartTab" class="chart-tabs">
          <!-- 存储使用 -->
          <el-tab-pane label="存储使用" name="storage">
            <div class="tab-content">
              <el-row :gutter="20">
                <el-col :span="16">
                  <el-card shadow="never">
                    <template #header>
                      <span>存储使用趋势</span>
                    </template>
                    <div ref="storageChartRef" class="chart-container"></div>
                  </el-card>
                </el-col>
                <el-col :span="8">
                  <el-card shadow="never">
                    <template #header>
                      <span>存储分布</span>
                    </template>
                    <div ref="storageDistChartRef" class="chart-container"></div>
                  </el-card>
                </el-col>
              </el-row>
              <el-row :gutter="20" style="margin-top: 20px;">
                <el-col :span="24">
                  <el-card shadow="never">
                    <template #header>
                      <span>存储详情</span>
                    </template>
                    <el-table :data="storageDetails" stripe>
                      <el-table-column prop="type" label="存储类型" width="150" />
                      <el-table-column prop="used" label="已使用" width="120">
                        <template #default="{ row }">
                          {{ formatStorage(row.used) }}
                        </template>
                      </el-table-column>
                      <el-table-column prop="total" label="总量" width="120">
                        <template #default="{ row }">
                          {{ formatStorage(row.total) }}
                        </template>
                      </el-table-column>
                      <el-table-column prop="usage" label="使用率" width="100">
                        <template #default="{ row }">
                          <el-progress 
                            :percentage="row.usage" 
                            :stroke-width="6"
                            :show-text="false"
                            :status="row.usage > 80 ? 'exception' : ''"
                          />
                          <span style="margin-left: 8px;">{{ row.usage }}%</span>
                        </template>
                      </el-table-column>
                      <el-table-column label="状态" width="80">
                        <template #default="{ row }">
                          <el-tag 
                            :type="row.usage > 80 ? 'danger' : row.usage > 60 ? 'warning' : 'success'" 
                            size="small"
                          >
                            {{ row.usage > 80 ? '紧张' : row.usage > 60 ? '正常' : '充足' }}
                          </el-tag>
                        </template>
                      </el-table-column>
                    </el-table>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>

          <!-- 网络使用 -->
          <el-tab-pane label="网络使用" name="network">
            <div class="tab-content">
              <el-row :gutter="20">
                <el-col :span="16">
                  <el-card shadow="never">
                    <template #header>
                      <span>网络流量趋势</span>
                    </template>
                    <div ref="networkChartRef" class="chart-container"></div>
                  </el-card>
                </el-col>
                <el-col :span="8">
                  <el-card shadow="never">
                    <template #header>
                      <span>流量分布</span>
                    </template>
                    <div ref="networkDistChartRef" class="chart-container"></div>
                  </el-card>
                </el-col>
              </el-row>
              <el-row :gutter="20" style="margin-top: 20px;">
                <el-col :span="24">
                  <el-card shadow="never">
                    <template #header>
                      <span>网络统计</span>
                    </template>
                    <el-descriptions :column="3" border>
                      <el-descriptions-item label="入站流量">
                        <div class="network-stat">
                          <div class="stat-value">{{ formatBandwidth(networkStats.inbound) }}</div>
                          <div class="stat-label">总计</div>
                        </div>
                      </el-descriptions-item>
                      <el-descriptions-item label="出站流量">
                        <div class="network-stat">
                          <div class="stat-value">{{ formatBandwidth(networkStats.outbound) }}</div>
                          <div class="stat-label">总计</div>
                        </div>
                      </el-descriptions-item>
                      <el-descriptions-item label="带宽使用率">
                        <div class="network-stat">
                          <div class="stat-value">{{ networkStats.bandwidthUsage }}%</div>
                          <div class="stat-label">峰值</div>
                        </div>
                      </el-descriptions-item>
                    </el-descriptions>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>

          <!-- 计算资源 -->
          <el-tab-pane label="计算资源" name="compute">
            <div class="tab-content">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-card shadow="never">
                    <template #header>
                      <span>CPU使用率</span>
                    </template>
                    <div ref="cpuChartRef" class="chart-container"></div>
                  </el-card>
                </el-col>
                <el-col :span="12">
                  <el-card shadow="never">
                    <template #header>
                      <span>内存使用率</span>
                    </template>
                    <div ref="memoryChartRef" class="chart-container"></div>
                  </el-card>
                </el-col>
              </el-row>
              <el-row :gutter="20" style="margin-top: 20px;">
                <el-col :span="24">
                  <el-card shadow="never">
                    <template #header>
                      <span>计算资源详情</span>
                    </template>
                    <el-table :data="computeDetails" stripe>
                      <el-table-column prop="instance" label="实例" width="150" />
                      <el-table-column prop="type" label="类型" width="100" />
                      <el-table-column prop="cpu" label="CPU" width="120">
                        <template #default="{ row }">
                          <div class="resource-usage">
                            <el-progress 
                              :percentage="row.cpu" 
                              :stroke-width="6"
                              :show-text="false"
                              :status="row.cpu > 80 ? 'exception' : ''"
                            />
                            <span style="margin-left: 8px;">{{ row.cpu }}%</span>
                          </div>
                        </template>
                      </el-table-column>
                      <el-table-column prop="memory" label="内存" width="120">
                        <template #default="{ row }">
                          <div class="resource-usage">
                            <el-progress 
                              :percentage="row.memory" 
                              :stroke-width="6"
                              :show-text="false"
                              :status="row.memory > 80 ? 'exception' : ''"
                            />
                            <span style="margin-left: 8px;">{{ row.memory }}%</span>
                          </div>
                        </template>
                      </el-table-column>
                      <el-table-column prop="status" label="状态" width="100">
                        <template #default="{ row }">
                          <el-tag 
                            :type="getStatusType(row.status)" 
                            size="small"
                          >
                            {{ getStatusText(row.status) }}
                          </el-tag>
                        </template>
                      </el-table-column>
                    </el-table>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>

          <!-- 数据库资源 -->
          <el-tab-pane label="数据库资源" name="database">
            <div class="tab-content">
              <el-row :gutter="20">
                <el-col :span="24">
                  <el-card shadow="never">
                    <template #header>
                      <span>数据库连接数</span>
                    </template>
                    <div ref="dbConnectionsChartRef" class="chart-container"></div>
                  </el-card>
                </el-col>
              </el-row>
              <el-row :gutter="20" style="margin-top: 20px;">
                <el-col :span="12">
                  <el-card shadow="never">
                    <template #header>
                      <span>数据库统计</span>
                    </template>
                    <el-descriptions :column="1" border>
                      <el-descriptions-item label="数据库实例">
                        {{ databaseStats.instances }} 个
                      </el-descriptions-item>
                      <el-descriptions-item label="当前连接">
                        {{ databaseStats.currentConnections }} / {{ databaseStats.maxConnections }}
                      </el-descriptions-item>
                      <el-descriptions-item label="查询平均耗时">
                        {{ databaseStats.avgQueryTime }} ms
                      </el-descriptions-item>
                      <el-descriptions-item label="每秒查询数">
                        {{ databaseStats.qps }}
                      </el-descriptions-item>
                    </el-descriptions>
                  </el-card>
                </el-col>
                <el-col :span="12">
                  <el-card shadow="never">
                    <template #header>
                      <span>数据库表空间</span>
                    </template>
                    <el-table :data="databaseSpaces" stripe>
                      <el-table-column prop="table" label="表名" />
                      <el-table-column prop="size" label="大小" width="100">
                        <template #default="{ row }">
                          {{ formatStorage(row.size) }}
                        </template>
                      </el-table-column>
                    </el-table>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
        <el-button 
          type="primary" 
          :icon="Download" 
          @click="handleExportResources"
        >
          导出报告
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, nextTick, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  HardDrive, Cpu, Wifi, Database, 
  Server, Download, Connection
} from '@element-plus/icons-vue'
import { useTenant } from '@/composables/useTenant'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  tenant: {
    type: Object,
    default: () => ({})
  }
})

// Emits
const emit = defineEmits(['update:modelValue'])

// 使用租户管理组合式函数
const { TENANT_TYPES, TENANT_STATUS } = useTenant()

// 响应式数据
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const activeChartTab = ref('storage')
const storageChartRef = ref()
const storageDistChartRef = ref()
const networkChartRef = ref()
const networkDistChartRef = ref()
const cpuChartRef = ref()
const memoryChartRef = ref()
const dbConnectionsChartRef = ref()

// 资源统计
const resourceStats = computed(() => [
  {
    label: '存储使用',
    value: '2.5GB / 10GB',
    usagePercent: 25,
    color: '#409EFF',
    icon: HardDrive
  },
  {
    label: 'CPU使用率',
    value: '45%',
    usagePercent: 45,
    color: '#E6A23C',
    icon: Cpu
  },
  {
    label: '网络带宽',
    value: '120MB/s',
    usagePercent: 60,
    color: '#67C23A',
    icon: Wifi
  },
  {
    label: '数据库连接',
    value: '45 / 100',
    usagePercent: 45,
    color: '#F56C6C',
    icon: Database
  }
])

// 存储详情
const storageDetails = ref([
  {
    type: '用户文件存储',
    used: 1.2 * 1024 * 1024 * 1024, // 1.2GB
    total: 5 * 1024 * 1024 * 1024, // 5GB
    usage: 24
  },
  {
    type: '系统数据存储',
    used: 0.8 * 1024 * 1024 * 1024, // 0.8GB
    total: 3 * 1024 * 1024 * 1024, // 3GB
    usage: 27
  },
  {
    type: '日志存储',
    used: 0.5 * 1024 * 1024 * 1024, // 0.5GB
    total: 2 * 1024 * 1024 * 1024, // 2GB
    usage: 25
  }
])

// 网络统计
const networkStats = ref({
  inbound: 12.5 * 1024 * 1024 * 1024, // 12.5GB
  outbound: 8.3 * 1024 * 1024 * 1024, // 8.3GB
  bandwidthUsage: 45
})

// 计算资源详情
const computeDetails = ref([
  {
    instance: 'web-server-01',
    type: 'Web服务器',
    cpu: 45,
    memory: 62,
    status: 'running'
  },
  {
    instance: 'app-server-01',
    type: '应用服务器',
    cpu: 38,
    memory: 55,
    status: 'running'
  },
  {
    instance: 'worker-01',
    type: '工作节点',
    cpu: 28,
    memory: 42,
    status: 'running'
  }
])

// 数据库统计
const databaseStats = ref({
  instances: 2,
  currentConnections: 45,
  maxConnections: 100,
  avgQueryTime: 125,
  qps: 150
})

// 数据库表空间
const databaseSpaces = ref([
  { table: '用户表', size: 512 * 1024 * 1024 }, // 512MB
  { table: '订单表', size: 256 * 1024 * 1024 }, // 256MB
  { table: '日志表', size: 128 * 1024 * 1024 }, // 128MB
  { table: '配置表', size: 64 * 1024 * 1024 } // 64MB
])

// 方法
const formatStorage = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const formatBandwidth = (bytes) => {
  if (bytes === 0) return '0 B/s'
  const k = 1024
  const sizes = ['B/s', 'KB/s', 'MB/s', 'GB/s']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const getStatusType = (status) => {
  const statusMap = {
    running: 'success',
    warning: 'warning',
    error: 'danger'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    running: '运行中',
    warning: '警告',
    error: '错误'
  }
  return statusMap[status] || '未知'
}

const handleExportResources = async () => {
  try {
    // 导出资源报告的逻辑
    ElMessage.success('资源报告导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

// 渲染图表
const renderCharts = async () => {
  await nextTick()
  
  // 这里应该使用实际的图表库（如ECharts、Chart.js等）来渲染图表
  console.log('渲染资源监控图表')
  
  // 示例实现
  if (storageChartRef.value) {
    console.log('渲染存储使用趋势图')
  }
  if (storageDistChartRef.value) {
    console.log('渲染存储分布图')
  }
  if (networkChartRef.value) {
    console.log('渲染网络流量趋势图')
  }
  if (networkDistChartRef.value) {
    console.log('渲染流量分布图')
  }
  if (cpuChartRef.value) {
    console.log('渲染CPU使用率图')
  }
  if (memoryChartRef.value) {
    console.log('渲染内存使用率图')
  }
  if (dbConnectionsChartRef.value) {
    console.log('渲染数据库连接数图')
  }
}

// 监听对话框显示状态
watch(dialogVisible, (newVal) => {
  if (newVal) {
    // 加载资源数据
    renderCharts()
  }
})

// 监听标签页切换
watch(activeChartTab, () => {
  renderCharts()
})

// 生命周期
onMounted(() => {
  if (dialogVisible.value) {
    renderCharts()
  }
})
</script>

<style scoped>
.resources-container {
  max-height: 600px;
  overflow-y: auto;
  padding: 0 10px;
}

.tenant-info {
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.info-label {
  font-weight: 500;
  color: #606266;
}

.info-value {
  color: #303133;
}

.resource-stats {
  margin-bottom: 24px;
}

.resource-card {
  display: flex;
  align-items: center;
  gap: 12px;
}

.resource-icon {
  font-size: 32px;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8f9fa;
}

.resource-info {
  flex: 1;
}

.resource-value {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.resource-label {
  font-size: 13px;
  color: #606266;
  margin-bottom: 8px;
}

.resource-usage {
  display: flex;
  align-items: center;
  gap: 8px;
}

.usage-text {
  font-size: 12px;
  color: #909399;
  min-width: 35px;
}

.chart-tabs {
  margin-top: 20px;
}

.tab-content {
  padding: 20px 0;
}

.chart-container {
  height: 250px;
  width: 100%;
}

.network-stat {
  text-align: center;
}

.stat-value {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #606266;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>