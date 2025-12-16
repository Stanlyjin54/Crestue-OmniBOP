<template>
  <el-dialog
    v-model="dialogVisible"
    title="AI助手日志详情"
    width="800px"
    :close-on-click-modal="false"
  >
    <div v-if="logData" class="log-detail-container">
      <!-- 基本信息 -->
      <div class="info-section">
        <h3 class="section-title">
          <el-icon><InfoFilled /></el-icon>
          基本信息
        </h3>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>日志ID:</label>
              <span>{{ logData.id }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>用户ID:</label>
              <span>{{ logData.userId }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>用户名:</label>
              <span>{{ logData.userName }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>功能类型:</label>
              <el-tag :type="getFeatureTypeTagType(logData.featureType)">
                {{ featureTypes[logData.featureType] || logData.featureType }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>响应时间:</label>
              <span :class="getResponseTimeClass(logData.responseTime)">
                {{ logData.responseTime }}ms
              </span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>状态:</label>
              <el-tag :type="getStatusTagType(logData.status)">
                {{ statusTypes[logData.status] || logData.status }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>创建时间:</label>
              <span>{{ formatDateTime(logData.createdAt) }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>更新时间:</label>
              <span>{{ formatDateTime(logData.updatedAt) }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 请求信息 -->
      <div class="info-section">
        <h3 class="section-title">
          <el-icon><Promotion /></el-icon>
          请求信息
        </h3>
        <div class="content-box">
          <div class="content-label">请求参数:</div>
          <div class="content-value">
            <pre>{{ JSON.stringify(logData.requestParams, null, 2) }}</pre>
          </div>
        </div>
        <div class="content-box">
          <div class="content-label">请求内容:</div>
          <div class="content-value">
            <div class="scrollable-content">
              {{ logData.requestContent || '无' }}
            </div>
          </div>
        </div>
      </div>

      <!-- 响应信息 -->
      <div class="info-section">
        <h3 class="section-title">
          <el-icon><Download /></el-icon>
          响应信息
        </h3>
        <div class="content-box">
          <div class="content-label">响应内容:</div>
          <div class="content-value">
            <div class="scrollable-content">
              {{ logData.responseContent || '无' }}
            </div>
          </div>
        </div>
        <div v-if="logData.errorMessage" class="content-box">
          <div class="content-label">错误信息:</div>
          <div class="content-value error-content">
            <div class="scrollable-content error-text">
              {{ logData.errorMessage }}
            </div>
          </div>
        </div>
      </div>

      <!-- 性能指标 -->
      <div class="info-section">
        <h3 class="section-title">
          <el-icon><TrendCharts /></el-icon>
          性能指标
        </h3>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="metric-item">
              <div class="metric-value" :class="getResponseTimeClass(logData.responseTime)">
                {{ logData.responseTime }}ms
              </div>
              <div class="metric-label">响应时间</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="metric-item">
              <div class="metric-value">
                {{ logData.tokenCount || 0 }}
              </div>
              <div class="metric-label">Token使用量</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="metric-item">
              <div class="metric-value">
                {{ logData.cost || 0 }}元
              </div>
              <div class="metric-label">成本费用</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 标签信息 -->
      <div class="info-section" v-if="logData.tags && logData.tags.length">
        <h3 class="section-title">
          <el-icon><Collection /></el-icon>
          标签信息
        </h3>
        <div class="tags-container">
          <el-tag
            v-for="tag in logData.tags"
            :key="tag"
            type="info"
            class="tag-item"
          >
            {{ tag }}
          </el-tag>
        </div>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
        <el-button
          v-if="hasPermission('ai_assistant:delete')"
          type="danger"
          @click="handleDelete"
        >
          删除日志
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  InfoFilled, Promotion, Download, TrendCharts, Collection
} from '@element-plus/icons-vue'
import { useAiAssistantLogs } from '@/composables/useAiAssistant'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  log: {
    type: Object,
    default: () => null
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'updated'])

// 权限检查
const hasPermission = (permission) => {
  return true // 临时返回true
}

// AI助手日志管理
const aiAssistant = useAiAssistantLogs()

// 响应式数据
const logData = ref(null)
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// 功能类型配置
const featureTypes = {
  CHAT: '聊天对话',
  TEXT_GENERATION: '文本生成',
  TRANSLATION: '翻译',
  SUMMARIZATION: '总结摘要',
  ANALYSIS: '数据分析',
  RECOMMENDATION: '智能推荐',
  OCR: '文字识别',
  SENTIMENT: '情感分析'
}

// 状态类型配置
const statusTypes = {
  SUCCESS: '成功',
  FAILED: '失败',
  TIMEOUT: '超时',
  CANCELLED: '已取消'
}

// 方法
const loadLogDetail = async () => {
  if (!props.log?.id) return
  
  try {
    logData.value = await aiAssistant.fetchLogDetail(props.log.id)
  } catch (error) {
    console.error('加载日志详情失败:', error)
    ElMessage.error('加载日志详情失败')
  }
}

const handleDelete = async () => {
  if (!logData.value) return
  
  try {
    await ElMessageBox.confirm(
      `确定要删除这条AI助手日志吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await aiAssistant.deleteLog(logData.value.id)
    ElMessage.success('删除成功')
    dialogVisible.value = false
    emit('updated')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 工具方法
const getFeatureTypeTagType = (type) => {
  const typeMap = {
    CHAT: '',
    TEXT_GENERATION: 'primary',
    TRANSLATION: 'success',
    SUMMARIZATION: 'warning',
    ANALYSIS: 'info',
    RECOMMENDATION: 'danger',
    OCR: 'primary',
    SENTIMENT: 'success'
  }
  return typeMap[type] || ''
}

const getStatusTagType = (status) => {
  const statusMap = {
    SUCCESS: 'success',
    FAILED: 'danger',
    TIMEOUT: 'warning',
    CANCELLED: 'info'
  }
  return statusMap[status] || ''
}

const getResponseTimeClass = (time) => {
  if (time < 1000) return 'response-time-fast'
  if (time < 3000) return 'response-time-normal'
  return 'response-time-slow'
}

const formatDateTime = (datetime) => {
  if (!datetime) return '-'
  return new Date(datetime).toLocaleString('zh-CN')
}

// 监听对话框显示状态
watch(dialogVisible, (newVal) => {
  if (newVal && props.log) {
    loadLogDetail()
  } else {
    logData.value = null
  }
})

// 监听日志数据变化
watch(() => props.log, (newLog) => {
  if (dialogVisible.value && newLog) {
    loadLogDetail()
  }
})
</script>

<style scoped>
.log-detail-container {
  max-height: 600px;
  overflow-y: auto;
  padding: 0 10px;
}

.info-section {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.info-section:last-child {
  border-bottom: none;
  margin-bottom: 0;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.info-item label {
  min-width: 80px;
  font-weight: 500;
  color: #606266;
  margin-right: 8px;
}

.info-item span {
  color: #303133;
}

.content-box {
  margin-bottom: 16px;
}

.content-label {
  font-weight: 500;
  color: #606266;
  margin-bottom: 8px;
}

.content-value {
  background-color: #f5f5f5;
  border-radius: 4px;
  padding: 12px;
  font-size: 13px;
  line-height: 1.5;
}

.scrollable-content {
  max-height: 200px;
  overflow-y: auto;
  white-space: pre-wrap;
  word-break: break-word;
}

.error-content {
  background-color: #fef0f0;
  border: 1px solid #fde2e2;
}

.error-text {
  color: #f56c6c;
}

pre {
  margin: 0;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.metric-item {
  text-align: center;
  padding: 16px;
  background-color: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 8px;
}

.metric-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.metric-label {
  font-size: 12px;
  color: #606266;
}

.response-time-fast {
  color: #67C23A;
}

.response-time-normal {
  color: #E6A23C;
}

.response-time-slow {
  color: #F56C6C;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-item {
  margin: 0;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>