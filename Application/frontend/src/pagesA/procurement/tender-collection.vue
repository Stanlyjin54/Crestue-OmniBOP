<template>
  <view class="tender-collection">
    <!-- 页面头部 -->
    <view class="page-header">
      <view class="header-left">
        <text class="page-title">招标信息采集</text>
        <text class="page-subtitle">自动采集各类招标公告信息</text>
      </view>
      <view class="header-actions">
        <button class="btn-primary" @click="startNewCollection">
          <text class="iconfont icon-plus"></text>
          新建采集
        </button>
      </view>
    </view>

    <!-- 搜索筛选区域 -->
    <view class="filter-section">
      <view class="filter-row">
        <view class="filter-item">
          <text class="filter-label">关键词</text>
          <input 
            class="filter-input" 
            v-model="searchForm.keyword" 
            placeholder="请输入关键词搜索"
            placeholder-style="color: #999"
            @confirm="handleSearch"
          />
        </view>
        
        <view class="filter-item">
          <text class="filter-label">地区</text>
          <picker 
            mode="selector" 
            :range="regionOptions" 
            :value="regionIndex"
            @change="onRegionChange"
          >
            <view class="filter-picker">
              <text>{{ regionOptions[regionIndex] || '全部地区' }}</text>
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>
        
        <view class="filter-item">
          <text class="filter-label">类型</text>
          <picker 
            mode="selector" 
            :range="typeOptions" 
            :value="typeIndex"
            @change="onTypeChange"
          >
            <view class="filter-picker">
              <text>{{ typeOptions[typeIndex] }}</text>
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>
      </view>
      
      <view class="filter-row">
        <view class="filter-item">
          <text class="filter-label">时间范围</text>
          <picker 
            mode="date" 
            :value="searchForm.startDate"
            @change="onStartDateChange"
          >
            <view class="filter-picker">
              <text>{{ searchForm.startDate || '开始日期' }}</text>
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>
        
        <view class="filter-item">
          <text class="filter-label">至</text>
          <picker 
            mode="date" 
            :value="searchForm.endDate"
            @change="onEndDateChange"
          >
            <view class="filter-picker">
              <text>{{ searchForm.endDate || '结束日期' }}</text>
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>
        
        <view class="filter-actions">
          <button class="btn-search" @click="handleSearch">
            <text class="iconfont icon-search"></text>
            搜索
          </button>
          <button class="btn-reset" @click="resetSearch">
            <text class="iconfont icon-reset"></text>
            重置
          </button>
        </view>
      </view>
    </view>

    <!-- 采集任务状态 -->
    <view class="collection-status">
      <view class="status-cards">
        <view class="status-card">
          <view class="status-icon running">
            <text class="iconfont icon-running"></text>
          </view>
          <view class="status-info">
            <text class="status-number">{{ statusCounts.running || 0 }}</text>
            <text class="status-label">运行中</text>
          </view>
        </view>
        
        <view class="status-card">
          <view class="status-icon completed">
            <text class="iconfont icon-success"></text>
          </view>
          <view class="status-info">
            <text class="status-number">{{ statusCounts.completed || 0 }}</text>
            <text class="status-label">已完成</text>
          </view>
        </view>
        
        <view class="status-card">
          <view class="status-icon failed">
            <text class="iconfont icon-error"></text>
          </view>
          <view class="status-info">
            <text class="status-number">{{ statusCounts.failed || 0 }}</text>
            <text class="status-label">失败</text>
          </view>
        </view>
        
        <view class="status-card">
          <view class="status-icon total">
            <text class="iconfont icon-document"></text>
          </view>
          <view class="status-info">
            <text class="status-number">{{ statusCounts.total || 0 }}</text>
            <text class="status-label">总任务</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 采集任务列表 -->
    <view class="collection-list">
      <view class="list-header">
        <text class="list-title">采集任务列表</text>
        <view class="list-actions">
          <button class="btn-batch" @click="batchStartCollection">
            <text class="iconfont icon-start"></text>
            批量开始
          </button>
          <button class="btn-batch" @click="batchStopCollection">
            <text class="iconfont icon-stop"></text>
            批量停止
          </button>
        </view>
      </view>
      
      <view class="task-list" v-if="taskList.length > 0">
        <view 
          class="task-item" 
          v-for="task in taskList" 
          :key="task.id"
          @click="viewTaskDetail(task)"
        >
          <view class="task-header">
            <view class="task-title-section">
              <checkbox 
                :checked="selectedTasks.includes(task.id)" 
                @click.stop="toggleTaskSelection(task.id)"
              />
              <text class="task-name">{{ task.name }}</text>
              <text class="task-status" :class="task.status">{{ getStatusText(task.status) }}</text>
            </view>
            <view class="task-actions">
              <button 
                class="btn-action" 
                @click.stop="startCollection(task)"
                v-if="task.status === 'pending' || task.status === 'stopped'"
              >
                <text class="iconfont icon-start"></text>
              </button>
              <button 
                class="btn-action" 
                @click.stop="stopCollection(task)"
                v-if="task.status === 'running'"
              >
                <text class="iconfont icon-stop"></text>
              </button>
              <button class="btn-action" @click.stop="editTask(task)">
                <text class="iconfont icon-edit"></text>
              </button>
              <button class="btn-action" @click.stop="deleteTask(task)">
                <text class="iconfont icon-delete"></text>
              </button>
            </view>
          </view>
          
          <view class="task-info">
            <view class="info-row">
              <text class="info-label">关键词:</text>
              <text class="info-value">{{ task.keyword }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">地区:</text>
              <text class="info-value">{{ task.region }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">类型:</text>
              <text class="info-value">{{ getTypeText(task.type) }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">时间范围:</text>
              <text class="info-value">{{ formatDateRange(task.startDate, task.endDate) }}</text>
            </view>
          </view>
          
          <view class="task-progress" v-if="task.status === 'running'">
            <view class="progress-bar">
              <view 
                class="progress-fill" 
                :style="{ width: task.progress + '%' }"
              ></view>
            </view>
            <text class="progress-text">{{ task.progress }}% ({{ task.collected }}/{{ task.total }})</text>
          </view>
          
          <view class="task-stats">
            <text class="stat-item">采集: {{ task.collected || 0 }}</text>
            <text class="stat-item">匹配: {{ task.matched || 0 }}</text>
            <text class="stat-item">创建: {{ formatDate(task.createTime) }}</text>
          </view>
        </view>
      </view>
      
      <view class="empty-state" v-else>
        <text class="empty-text">暂无采集任务</text>
        <button class="empty-btn" @click="startNewCollection">创建采集任务</button>
      </view>
    </view>

    <!-- 新建/编辑采集任务弹窗 -->
    <uni-popup ref="taskPopup" type="center">
      <view class="popup-content">
        <view class="popup-header">
          <text class="popup-title">{{ isEdit ? '编辑采集任务' : '新建采集任务' }}</text>
          <text class="popup-close" @click="closeTaskPopup">×</text>
        </view>
        
        <view class="popup-body">
          <view class="form-item">
            <text class="form-label">任务名称</text>
            <input 
              class="form-input" 
              v-model="taskForm.name" 
              placeholder="请输入任务名称"
              placeholder-style="color: #999"
            />
          </view>
          
          <view class="form-item">
            <text class="form-label">关键词</text>
            <input 
              class="form-input" 
              v-model="taskForm.keyword" 
              placeholder="请输入关键词，多个关键词用逗号分隔"
              placeholder-style="color: #999"
            />
          </view>
          
          <view class="form-item">
            <text class="form-label">地区</text>
            <picker 
              mode="selector" 
              :range="regionOptions" 
              :value="taskRegionIndex"
              @change="onTaskRegionChange"
            >
              <view class="form-picker">
                <text>{{ regionOptions[taskRegionIndex] || '请选择地区' }}</text>
                <text class="picker-arrow">▼</text>
              </view>
            </picker>
          </view>
          
          <view class="form-item">
            <text class="form-label">采集类型</text>
            <radio-group class="radio-group" v-model="taskForm.type">
              <label class="radio-item">
                <radio value="government" :checked="taskForm.type === 'government'" />
                <text>政府采购</text>
              </label>
              <label class="radio-item">
                <radio value="tender" :checked="taskForm.type === 'tender'" />
                <text>招标公告</text>
              </label>
              <label class="radio-item">
                <radio value="bidding" :checked="taskForm.type === 'bidding'" />
                <text>中标公告</text>
              </label>
            </radio-group>
          </view>
          
          <view class="form-item">
            <text class="form-label">时间范围</text>
            <view class="date-range">
              <picker 
                mode="date" 
                :value="taskForm.startDate"
                @change="onTaskStartDateChange"
              >
                <view class="date-picker">
                  <text>{{ taskForm.startDate || '开始日期' }}</text>
                  <text class="picker-arrow">▼</text>
                </view>
              </picker>
              <text class="date-separator">至</text>
              <picker 
                mode="date" 
                :value="taskForm.endDate"
                @change="onTaskEndDateChange"
              >
                <view class="date-picker">
                  <text>{{ taskForm.endDate || '结束日期' }}</text>
                  <text class="picker-arrow">▼</text>
                </view>
              </picker>
            </view>
          </view>
          
          <view class="form-item">
            <text class="form-label">采集频率</text>
            <picker 
              mode="selector" 
              :range="frequencyOptions" 
              :value="frequencyIndex"
              @change="onFrequencyChange"
            >
              <view class="form-picker">
                <text>{{ frequencyOptions[frequencyIndex] }}</text>
                <text class="picker-arrow">▼</text>
              </view>
            </picker>
          </view>
          
          <view class="form-item">
            <text class="form-label">备注</text>
            <textarea 
              class="form-textarea" 
              v-model="taskForm.remark" 
              placeholder="请输入备注信息（可选）"
              placeholder-style="color: #999"
              :maxlength="200"
            />
          </view>
        </view>
        
        <view class="popup-footer">
          <button class="btn-cancel" @click="closeTaskPopup">取消</button>
          <button class="btn-confirm" @click="saveTask" :loading="saveLoading">
            保存
          </button>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted, onShow } from 'vue'
import { onPullDownRefresh, onReachBottom } from '@dcloudio/uni-app'
import {
  getCrawlTaskList,
  createCrawlTask,
  updateCrawlTask,
  deleteCrawlTask,
  startCrawlTask,
  stopCrawlTask,
  getCrawlTaskStatistics
} from '@/api/procurement/procurementCrawler.js'

// 搜索表单
const searchForm = reactive({
  keyword: '',
  region: '',
  type: '',
  startDate: '',
  endDate: '',
  current: 1,
  pageSize: 10
})

// 任务表单
const taskForm = reactive({
  id: null,
  name: '',
  keyword: '',
  region: '',
  type: 'government',
  startDate: '',
  endDate: '',
  frequency: 'daily',
  remark: ''
})

// 选项数据
const regionOptions = [
  '全部地区', '北京市', '上海市', '天津市', '重庆市', '广东省', '江苏省', '浙江省', '山东省', 
  '河南省', '四川省', '湖北省', '湖南省', '河北省', '山西省', '陕西省', '安徽省', '福建省', 
  '江西省', '辽宁省', '吉林省', '黑龙江省', '云南省', '贵州省', '广西壮族自治区', '甘肃省', 
  '青海省', '宁夏回族自治区', '新疆维吾尔自治区', '西藏自治区', '内蒙古自治区'
]

const typeOptions = ['全部类型', '政府采购', '招标公告', '中标公告']
const frequencyOptions = ['每天', '每周', '每月', '仅一次']

// 状态索引
const regionIndex = ref(0)
const typeIndex = ref(0)
const frequencyIndex = ref(0)
const taskRegionIndex = ref(0)

// 数据列表
const taskList = ref([])
const selectedTasks = ref([])
const statusCounts = ref({
  running: 0,
  completed: 0,
  failed: 0,
  total: 0
})

// 弹窗相关
const taskPopup = ref(null)
const isEdit = ref(false)
const saveLoading = ref(false)

// 分页相关
const total = ref(0)
const loading = ref(false)

// 页面加载时获取数据
onMounted(() => {
  loadTaskList()
  loadTaskStatistics()
})

onShow(() => {
  loadTaskList()
  loadTaskStatistics()
})

// 下拉刷新
onPullDownRefresh(() => {
  Promise.all([loadTaskList(), loadTaskStatistics()]).finally(() => {
    uni.stopPullDownRefresh()
  })
})

// 上拉加载更多
onReachBottom(() => {
  if (searchForm.current * searchForm.pageSize < total.value) {
    searchForm.current++
    loadTaskList(true)
  }
})

// 加载任务列表
async function loadTaskList(append = false) {
  if (loading.value) return
  loading.value = true
  
  try {
    const params = {
      ...searchForm,
      region: regionIndex.value > 0 ? regionOptions[regionIndex.value] : '',
      type: typeIndex.value > 0 ? getTypeValue(typeIndex.value) : ''
    }
    
    const result = await getCrawlTaskList(params)
    if (result.success) {
      const newList = result.data.records || []
      if (append) {
        taskList.value = [...taskList.value, ...newList]
      } else {
        taskList.value = newList
      }
      total.value = result.data.total || 0
    } else {
      uni.showToast({
        title: result.message || '加载失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('加载任务列表失败:', error)
    uni.showToast({
      title: '加载失败',
      icon: 'none'
    })
  } finally {
    loading.value = false
  }
}

// 加载任务统计
async function loadTaskStatistics() {
  try {
    const result = await getCrawlTaskStatistics()
    if (result.success) {
      statusCounts.value = result.data
    }
  } catch (error) {
    console.error('加载任务统计失败:', error)
  }
}

// 搜索处理
function handleSearch() {
  searchForm.current = 1
  loadTaskList()
}

// 重置搜索
function resetSearch() {
  searchForm.keyword = ''
  searchForm.startDate = ''
  searchForm.endDate = ''
  regionIndex.value = 0
  typeIndex.value = 0
  handleSearch()
}

// 选择器变更处理
function onRegionChange(e) {
  regionIndex.value = e.detail.value
  handleSearch()
}

function onTypeChange(e) {
  typeIndex.value = e.detail.value
  handleSearch()
}

function onStartDateChange(e) {
  searchForm.startDate = e.detail.value
  handleSearch()
}

function onEndDateChange(e) {
  searchForm.endDate = e.detail.value
  handleSearch()
}

// 任务选择处理
function toggleTaskSelection(taskId) {
  const index = selectedTasks.value.indexOf(taskId)
  if (index > -1) {
    selectedTasks.value.splice(index, 1)
  } else {
    selectedTasks.value.push(taskId)
  }
}

// 任务操作
function startNewCollection() {
  isEdit.value = false
  resetTaskForm()
  taskPopup.value.open()
}

function editTask(task) {
  isEdit.value = true
  Object.assign(taskForm, {
    id: task.id,
    name: task.name,
    keyword: task.keyword,
    region: task.region,
    type: task.type,
    startDate: task.startDate,
    endDate: task.endDate,
    frequency: task.frequency,
    remark: task.remark
  })
  taskRegionIndex.value = regionOptions.indexOf(task.region) || 0
  frequencyIndex.value = frequencyOptions.indexOf(getFrequencyText(task.frequency)) || 0
  taskPopup.value.open()
}

function viewTaskDetail(task) {
  uni.navigateTo({
    url: `/pagesA/procurement/task-detail?id=${task.id}`
  })
}

async function startCollection(task) {
  try {
    const result = await startCrawlTask(task.id)
    if (result.success) {
      uni.showToast({
        title: '任务开始成功',
        icon: 'success'
      })
      loadTaskList()
      loadTaskStatistics()
    } else {
      uni.showToast({
        title: result.message || '开始失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('开始任务失败:', error)
    uni.showToast({
      title: '开始失败',
      icon: 'none'
    })
  }
}

async function stopCollection(task) {
  try {
    const result = await stopCrawlTask(task.id)
    if (result.success) {
      uni.showToast({
        title: '任务停止成功',
        icon: 'success'
      })
      loadTaskList()
      loadTaskStatistics()
    } else {
      uni.showToast({
        title: result.message || '停止失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('停止任务失败:', error)
    uni.showToast({
      title: '停止失败',
      icon: 'none'
    })
  }
}

async function deleteTask(task) {
  uni.showModal({
    title: '确认删除',
    content: '确定要删除这个采集任务吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          const result = await deleteCrawlTask(task.id)
          if (result.success) {
            uni.showToast({
              title: '删除成功',
              icon: 'success'
            })
            loadTaskList()
            loadTaskStatistics()
          } else {
            uni.showToast({
              title: result.message || '删除失败',
              icon: 'none'
            })
          }
        } catch (error) {
          console.error('删除任务失败:', error)
          uni.showToast({
            title: '删除失败',
            icon: 'none'
          })
        }
      }
    }
  })
}

// 批量操作
async function batchStartCollection() {
  if (selectedTasks.value.length === 0) {
    uni.showToast({
      title: '请选择要开始的任务',
      icon: 'none'
    })
    return
  }
  
  uni.showModal({
    title: '确认开始',
    content: `确定要开始 ${selectedTasks.value.length} 个采集任务吗？`,
    success: async (res) => {
      if (res.confirm) {
        try {
          const promises = selectedTasks.value.map(id => startCrawlTask(id))
          const results = await Promise.allSettled(promises)
          const successCount = results.filter(r => r.status === 'fulfilled' && r.value.success).length
          
          uni.showToast({
            title: `成功开始 ${successCount} 个任务`,
            icon: 'success'
          })
          selectedTasks.value = []
          loadTaskList()
          loadTaskStatistics()
        } catch (error) {
          console.error('批量开始任务失败:', error)
          uni.showToast({
            title: '批量开始失败',
            icon: 'none'
          })
        }
      }
    }
  })
}

async function batchStopCollection() {
  if (selectedTasks.value.length === 0) {
    uni.showToast({
      title: '请选择要停止的任务',
      icon: 'none'
    })
    return
  }
  
  uni.showModal({
    title: '确认停止',
    content: `确定要停止 ${selectedTasks.value.length} 个采集任务吗？`,
    success: async (res) => {
      if (res.confirm) {
        try {
          const promises = selectedTasks.value.map(id => stopCrawlTask(id))
          const results = await Promise.allSettled(promises)
          const successCount = results.filter(r => r.status === 'fulfilled' && r.value.success).length
          
          uni.showToast({
            title: `成功停止 ${successCount} 个任务`,
            icon: 'success'
          })
          selectedTasks.value = []
          loadTaskList()
          loadTaskStatistics()
        } catch (error) {
          console.error('批量停止任务失败:', error)
          uni.showToast({
            title: '批量停止失败',
            icon: 'none'
          })
        }
      }
    }
  })
}

// 弹窗相关
function closeTaskPopup() {
  taskPopup.value.close()
}

function resetTaskForm() {
  Object.assign(taskForm, {
    id: null,
    name: '',
    keyword: '',
    region: '',
    type: 'government',
    startDate: '',
    endDate: '',
    frequency: 'daily',
    remark: ''
  })
  taskRegionIndex.value = 0
  frequencyIndex.value = 0
}

function onTaskRegionChange(e) {
  taskRegionIndex.value = e.detail.value
  taskForm.region = regionOptions[taskRegionIndex.value]
}

function onTaskStartDateChange(e) {
  taskForm.startDate = e.detail.value
}

function onTaskEndDateChange(e) {
  taskForm.endDate = e.detail.value
}

function onFrequencyChange(e) {
  frequencyIndex.value = e.detail.value
  const frequencyMap = ['daily', 'weekly', 'monthly', 'once']
  taskForm.frequency = frequencyMap[frequencyIndex.value]
}

async function saveTask() {
  if (!taskForm.name.trim()) {
    uni.showToast({
      title: '请输入任务名称',
      icon: 'none'
    })
    return
  }
  
  if (!taskForm.keyword.trim()) {
    uni.showToast({
      title: '请输入关键词',
      icon: 'none'
    })
    return
  }
  
  if (!taskForm.region) {
    uni.showToast({
      title: '请选择地区',
      icon: 'none'
    })
    return
  }
  
  if (!taskForm.startDate || !taskForm.endDate) {
    uni.showToast({
      title: '请选择时间范围',
      icon: 'none'
    })
    return
  }
  
  saveLoading.value = true
  
  try {
    let result
    if (isEdit.value) {
      result = await updateCrawlTask(taskForm)
    } else {
      result = await createCrawlTask(taskForm)
    }
    
    if (result.success) {
      uni.showToast({
        title: isEdit.value ? '更新成功' : '创建成功',
        icon: 'success'
      })
      closeTaskPopup()
      loadTaskList()
      loadTaskStatistics()
    } else {
      uni.showToast({
        title: result.message || '保存失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('保存任务失败:', error)
    uni.showToast({
      title: '保存失败',
      icon: 'none'
    })
  } finally {
    saveLoading.value = false
  }
}

// 工具函数
function getStatusText(status) {
  const statusMap = {
    'pending': '待开始',
    'running': '运行中',
    'completed': '已完成',
    'failed': '失败',
    'stopped': '已停止'
  }
  return statusMap[status] || '未知'
}

function getTypeText(type) {
  const typeMap = {
    'government': '政府采购',
    'tender': '招标公告',
    'bidding': '中标公告'
  }
  return typeMap[type] || '未知'
}

function getTypeValue(index) {
  const typeValues = ['', 'government', 'tender', 'bidding']
  return typeValues[index]
}

function getFrequencyText(frequency) {
  const frequencyMap = {
    'daily': '每天',
    'weekly': '每周',
    'monthly': '每月',
    'once': '仅一次'
  }
  return frequencyMap[frequency] || '未知'
}

function formatDateRange(startDate, endDate) {
  if (!startDate && !endDate) return '不限'
  if (!startDate) return `至 ${endDate}`
  if (!endDate) return `${startDate} 起`
  return `${startDate} 至 ${endDate}`
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}
</script>

<style lang="scss" scoped>
.tender-collection {
  padding: 20rpx;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
  
  .header-left {
    flex: 1;
    
    .page-title {
      display: block;
      font-size: 36rpx;
      font-weight: bold;
      color: #303133;
      margin-bottom: 8rpx;
    }
    
    .page-subtitle {
      display: block;
      font-size: 26rpx;
      color: #909399;
    }
  }
  
  .header-actions {
    .btn-primary {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
      border: none;
      border-radius: 12rpx;
      padding: 20rpx 30rpx;
      font-size: 28rpx;
      display: flex;
      align-items: center;
      gap: 10rpx;
      
      .iconfont {
        font-size: 32rpx;
      }
    }
  }
}

.filter-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
  
  .filter-row {
    display: flex;
    align-items: center;
    gap: 20rpx;
    margin-bottom: 20rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .filter-item {
      flex: 1;
      
      .filter-label {
        display: block;
        font-size: 26rpx;
        color: #606266;
        margin-bottom: 12rpx;
      }
      
      .filter-input {
        width: 100%;
        height: 70rpx;
        border: 1rpx solid #dcdfe6;
        border-radius: 8rpx;
        padding: 0 20rpx;
        font-size: 26rpx;
      }
      
      .filter-picker {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        height: 70rpx;
        border: 1rpx solid #dcdfe6;
        border-radius: 8rpx;
        padding: 0 20rpx;
        font-size: 26rpx;
        background: #fff;
        
        .picker-arrow {
          color: #909399;
        }
      }
    }
    
    .filter-actions {
      display: flex;
      gap: 16rpx;
      align-self: flex-end;
      
      .btn-search,
      .btn-reset {
        padding: 20rpx 30rpx;
        border-radius: 8rpx;
        font-size: 26rpx;
        border: none;
        display: flex;
        align-items: center;
        gap: 8rpx;
        
        .iconfont {
          font-size: 28rpx;
        }
      }
      
      .btn-search {
        background: #409eff;
        color: #fff;
      }
      
      .btn-reset {
        background: #f4f4f5;
        color: #606266;
      }
    }
  }
}

.collection-status {
  margin-bottom: 30rpx;
  
  .status-cards {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20rpx;
    
    .status-card {
      background: #fff;
      border-radius: 16rpx;
      padding: 30rpx 20rpx;
      display: flex;
      align-items: center;
      box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
      
      .status-icon {
        width: 60rpx;
        height: 60rpx;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20rpx;
        
        &.running {
          background: #f0f9ff;
          color: #409eff;
        }
        
        &.completed {
          background: #f6ffed;
          color: #52c41a;
        }
        
        &.failed {
          background: #fef0f0;
          color: #f56c6c;
        }
        
        &.total {
          background: #f4f4f5;
          color: #909399;
        }
        
        .iconfont {
          font-size: 32rpx;
        }
      }
      
      .status-info {
        flex: 1;
        
        .status-number {
          display: block;
          font-size: 32rpx;
          font-weight: bold;
          color: #303133;
          margin-bottom: 8rpx;
        }
        
        .status-label {
          display: block;
          font-size: 24rpx;
          color: #909399;
        }
      }
    }
  }
}

.collection-list {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
  
  .list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30rpx;
    
    .list-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #303133;
    }
    
    .list-actions {
      display: flex;
      gap: 16rpx;
      
      .btn-batch {
        padding: 16rpx 24rpx;
        border-radius: 8rpx;
        font-size: 24rpx;
        border: none;
        background: #f4f4f5;
        color: #606266;
        display: flex;
        align-items: center;
        gap: 8rpx;
        
        .iconfont {
          font-size: 24rpx;
        }
      }
    }
  }
  
  .task-list {
    .task-item {
      background: #f8f9fa;
      border-radius: 12rpx;
      padding: 30rpx;
      margin-bottom: 20rpx;
      cursor: pointer;
      transition: all 0.3s ease;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      &:active {
        transform: scale(0.98);
        background: #e9ecef;
      }
      
      .task-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20rpx;
        
        .task-title-section {
          display: flex;
          align-items: center;
          gap: 16rpx;
          flex: 1;
          
          .task-name {
            font-size: 30rpx;
            font-weight: bold;
            color: #303133;
            flex: 1;
          }
          
          .task-status {
            padding: 8rpx 16rpx;
            border-radius: 20rpx;
            font-size: 22rpx;
            
            &.pending {
              background: #fdf6ec;
              color: #e6a23c;
            }
            
            &.running {
              background: #f0f9ff;
              color: #409eff;
            }
            
            &.completed {
              background: #f6ffed;
              color: #52c41a;
            }
            
            &.failed {
              background: #fef0f0;
              color: #f56c6c;
            }
            
            &.stopped {
              background: #f4f4f5;
              color: #909399;
            }
          }
        }
        
        .task-actions {
          display: flex;
          gap: 12rpx;
          
          .btn-action {
            width: 60rpx;
            height: 60rpx;
            border: 1rpx solid #dcdfe6;
            border-radius: 8rpx;
            background: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            
            .iconfont {
              font-size: 28rpx;
              color: #606266;
            }
          }
        }
      }
      
      .task-info {
        margin-bottom: 20rpx;
        
        .info-row {
          display: flex;
          align-items: center;
          margin-bottom: 12rpx;
          
          &:last-child {
            margin-bottom: 0;
          }
          
          .info-label {
            font-size: 26rpx;
            color: #909399;
            width: 120rpx;
            margin-right: 20rpx;
          }
          
          .info-value {
            font-size: 26rpx;
            color: #606266;
            flex: 1;
          }
        }
      }
      
      .task-progress {
        margin-bottom: 20rpx;
        
        .progress-bar {
          width: 100%;
          height: 12rpx;
          background: #f0f2f5;
          border-radius: 6rpx;
          overflow: hidden;
          margin-bottom: 12rpx;
          
          .progress-fill {
            height: 100%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 6rpx;
            transition: width 0.3s ease;
          }
        }
        
        .progress-text {
          font-size: 24rpx;
          color: #909399;
        }
      }
      
      .task-stats {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .stat-item {
          font-size: 24rpx;
          color: #606266;
          
          &:last-child {
            color: #909399;
          }
        }
      }
    }
  }
  
  .empty-state {
    text-align: center;
    padding: 80rpx 0;
    
    .empty-text {
      display: block;
      font-size: 28rpx;
      color: #909399;
      margin-bottom: 30rpx;
    }
    
    .empty-btn {
      background: #409eff;
      color: #fff;
      border: none;
      border-radius: 8rpx;
      padding: 20rpx 40rpx;
      font-size: 28rpx;
      cursor: pointer;
    }
  }
}

// 弹窗样式
.popup-content {
  background: #fff;
  border-radius: 16rpx;
  width: 700rpx;
  max-width: 90vw;
  max-height: 80vh;
  overflow-y: auto;
  
  .popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #ebeef5;
    position: sticky;
    top: 0;
    background: #fff;
    z-index: 10;
    
    .popup-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #303133;
    }
    
    .popup-close {
      font-size: 40rpx;
      color: #909399;
      cursor: pointer;
    }
  }
  
  .popup-body {
    padding: 30rpx;
    
    .form-item {
      margin-bottom: 30rpx;
      
      .form-label {
        display: block;
        font-size: 28rpx;
        color: #303133;
        margin-bottom: 16rpx;
      }
      
      .form-input {
        width: 100%;
        height: 80rpx;
        border: 1rpx solid #dcdfe6;
        border-radius: 8rpx;
        padding: 0 20rpx;
        font-size: 28rpx;
      }
      
      .form-picker {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        height: 80rpx;
        border: 1rpx solid #dcdfe6;
        border-radius: 8rpx;
        padding: 0 20rpx;
        font-size: 28rpx;
        background: #fff;
        
        .picker-arrow {
          color: #909399;
        }
      }
      
      .radio-group {
        display: flex;
        flex-wrap: wrap;
        gap: 30rpx;
        
        .radio-item {
          display: flex;
          align-items: center;
          gap: 12rpx;
          font-size: 28rpx;
          color: #606266;
        }
      }
      
      .date-range {
        display: flex;
        align-items: center;
        gap: 20rpx;
        
        .date-picker {
          flex: 1;
          display: flex;
          justify-content: space-between;
          align-items: center;
          height: 80rpx;
          border: 1rpx solid #dcdfe6;
          border-radius: 8rpx;
          padding: 0 20rpx;
          font-size: 28rpx;
          background: #fff;
          
          .picker-arrow {
            color: #909399;
          }
        }
        
        .date-separator {
          font-size: 28rpx;
          color: #606266;
        }
      }
      
      .form-textarea {
        width: 100%;
        height: 120rpx;
        border: 1rpx solid #dcdfe6;
        border-radius: 8rpx;
        padding: 20rpx;
        font-size: 28rpx;
        resize: none;
      }
    }
  }
  
  .popup-footer {
    display: flex;
    justify-content: flex-end;
    gap: 20rpx;
    padding: 30rpx;
    border-top: 1rpx solid #ebeef5;
    position: sticky;
    bottom: 0;
    background: #fff;
    z-index: 10;
    
    .btn-cancel,
    .btn-confirm {
      padding: 20rpx 40rpx;
      border-radius: 8rpx;
      font-size: 28rpx;
      border: none;
      cursor: pointer;
    }
    
    .btn-cancel {
      background: #f4f4f5;
      color: #606266;
    }
    
    .btn-confirm {
      background: #409eff;
      color: #fff;
    }
  }
}
</style>