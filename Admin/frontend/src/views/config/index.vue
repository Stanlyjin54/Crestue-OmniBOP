<template>
  <div class="config-page">
    <base-wrapper>
      <!-- 页面头部 -->
      <div class="page-header">
        <div class="header-content">
          <div class="header-title">
            <el-icon class="header-icon"><Setting /></el-icon>
            <h1>系统配置</h1>
          </div>
          <p class="header-description">管理系统业务参数、系统参数、财务参数等各类配置项</p>
        </div>
      </div>

      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item>系统配置</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 配置分类卡片 -->
      <div class="config-categories">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="category in categories" :key="category.key">
            <div class="category-card" @click="selectCategory(category.key)" :class="{ active: currentCategory === category.key }">
              <div class="category-icon" :style="{ backgroundColor: category.color + '20', color: category.color }">
                <el-icon><component :is="category.icon" /></el-icon>
              </div>
              <div class="category-info">
                <h3>{{ category.name }}</h3>
                <p>{{ category.count }} 个配置项</p>
              </div>
              <div class="category-arrow">
                <el-icon><ArrowRight /></el-icon>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 配置列表 -->
      <div class="config-list" v-if="currentCategory">
        <div class="list-header">
          <h3>{{ getCategoryName(currentCategory) }}</h3>
          <div class="list-actions">
            <el-button type="primary" @click="handleExport" :loading="loading">
              <el-icon><Download /></el-icon>
              导出配置
            </el-button>
            <el-button @click="handleImport" :loading="loading">
              <el-icon><Upload /></el-icon>
              导入配置
            </el-button>
            <el-button type="warning" @click="handleResetAll" :loading="loading">
              <el-icon><Refresh /></el-icon>
              重置所有
            </el-button>
          </div>
        </div>

        <el-card class="config-card">
          <el-form :model="configForm" label-width="200px" label-position="left">
            <div v-for="config in currentConfigs" :key="config.key" class="config-item">
              <el-form-item :label="config.name" :prop="config.key">
                <template #label>
                  <div class="config-label">
                    <span>{{ config.name }}</span>
                    <el-tooltip :content="config.description" placement="top">
                      <el-icon class="info-icon"><InfoFilled /></el-icon>
                    </el-tooltip>
                  </div>
                </template>

                <!-- 布尔类型 -->
                <el-switch
                  v-if="config.type === CONFIG_ITEM_TYPES.BOOLEAN"
                  v-model="configForm[config.key]"
                  @change="(val) => handleConfigChange(config.key, val)"
                />

                <!-- 数字类型 -->
                <el-input-number
                  v-else-if="config.type === CONFIG_ITEM_TYPES.NUMBER"
                  v-model="configForm[config.key]"
                  :min="config.min"
                  :max="config.max"
                  :step="config.step || 1"
                  @change="(val) => handleConfigChange(config.key, val)"
                />

                <!-- 选择类型 -->
                <el-select
                  v-else-if="config.type === CONFIG_ITEM_TYPES.SELECT"
                  v-model="configForm[config.key]"
                  @change="(val) => handleConfigChange(config.key, val)"
                >
                  <el-option
                    v-for="option in config.options"
                    :key="option.value"
                    :label="option.label"
                    :value="option.value"
                  />
                </el-select>

                <!-- 多选类型 -->
                <el-select
                  v-else-if="config.type === CONFIG_ITEM_TYPES.MULTI_SELECT"
                  v-model="configForm[config.key]"
                  multiple
                  @change="(val) => handleConfigChange(config.key, val)"
                >
                  <el-option
                    v-for="option in config.options"
                    :key="option.value"
                    :label="option.label"
                    :value="option.value"
                  />
                </el-select>

                <!-- 日期类型 -->
                <el-date-picker
                  v-else-if="config.type === CONFIG_ITEM_TYPES.DATE"
                  v-model="configForm[config.key]"
                  type="date"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  @change="(val) => handleConfigChange(config.key, val)"
                />

                <!-- 时间类型 -->
                <el-time-picker
                  v-else-if="config.type === CONFIG_ITEM_TYPES.TIME"
                  v-model="configForm[config.key]"
                  format="HH:mm:ss"
                  value-format="HH:mm:ss"
                  @change="(val) => handleConfigChange(config.key, val)"
                />

                <!-- 日期时间类型 -->
                <el-date-picker
                  v-else-if="config.type === CONFIG_ITEM_TYPES.DATETIME"
                  v-model="configForm[config.key]"
                  type="datetime"
                  format="YYYY-MM-DD HH:mm:ss"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  @change="(val) => handleConfigChange(config.key, val)"
                />

                <!-- JSON类型 -->
                <el-input
                  v-else-if="config.type === CONFIG_ITEM_TYPES.JSON"
                  v-model="configForm[config.key]"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入JSON格式的配置值"
                  @blur="(e) => handleConfigChange(config.key, e.target.value)"
                />

                <!-- 文本域类型 -->
                <el-input
                  v-else-if="config.type === CONFIG_ITEM_TYPES.TEXTAREA"
                  v-model="configForm[config.key]"
                  type="textarea"
                  :rows="3"
                  @blur="(e) => handleConfigChange(config.key, e.target.value)"
                />

                <!-- 默认文本类型 -->
                <el-input
                  v-else
                  v-model="configForm[config.key]"
                  :placeholder="config.placeholder || '请输入配置值'"
                  @blur="(e) => handleConfigChange(config.key, e.target.value)"
                />

                <!-- 重置按钮 -->
                <el-button
                  v-if="config.value !== config.defaultValue"
                  type="text"
                  size="small"
                  @click="handleReset(config.key)"
                  class="reset-btn"
                >
                  重置
                </el-button>

                <!-- 更新时间 -->
                <div class="config-meta" v-if="config.updatedAt">
                  <span>最后更新: {{ formatDate(config.updatedAt) }}</span>
                  <span>更新人: {{ config.updatedBy }}</span>
                </div>
              </el-form-item>
            </div>
          </el-form>
        </el-card>
      </div>

      <!-- 批量操作 -->
      <div class="batch-actions" v-if="currentCategory">
        <el-button type="primary" @click="handleBatchSave" :loading="loading">
          <el-icon><Check /></el-icon>
          保存修改
        </el-button>
        <el-button @click="handleBatchReset" :loading="loading">
          <el-icon><Refresh /></el-icon>
          重置当前分类
        </el-button>
      </div>
    </base-wrapper>

    <!-- 导入对话框 -->
    <el-dialog
      v-model="importDialog.visible"
      title="导入配置"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="import-content">
        <el-upload
          ref="uploadRef"
          class="upload-area"
          drag
          :limit="1"
          :accept="'.json'"
          :auto-upload="false"
          :on-change="handleFileChange"
          :file-list="importDialog.fileList"
        >
          <el-icon class="upload-icon"><Upload /></el-icon>
          <div class="upload-text">
            <div class="upload-title">拖拽文件到此处或点击上传</div>
            <div class="upload-desc">支持 JSON 格式的配置文件</div>
          </div>
        </el-upload>
      </div>
      <template #footer>
        <el-button @click="importDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="confirmImport" :loading="loading">
          确认导入
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Setting, 
  ArrowRight, 
  Download, 
  Upload, 
  Refresh, 
  Check, 
  InfoFilled,
  Business,
  Money,
  Box,
  User,
  Document,
  CreditCard,
  Truck,
  Bell,
  Lock
} from '@element-plus/icons-vue'
import { useConfig, CONFIG_ITEM_TYPES } from '@/composables/useConfig'

const { 
  loading, 
  configs, 
  categories,
  CONFIG_TYPES,
  getConfigs,
  updateConfig,
  updateConfigs,
  resetConfig,
  resetAllConfigs,
  exportConfigs,
  importConfigs,
  validateConfigValue
} = useConfig()

const currentCategory = ref('')
const configForm = reactive({})
const importDialog = reactive({
  visible: false,
  fileList: []
})

// 当前分类的配置
const currentConfigs = computed(() => {
  return configs.value.filter(config => config.category === currentCategory.value)
})

// 选择分类
const selectCategory = async (category) => {
  currentCategory.value = category
  await getConfigs(category)
  
  // 初始化表单数据
  currentConfigs.value.forEach(config => {
    configForm[config.key] = config.value
  })
}

// 获取分类名称
const getCategoryName = (key) => {
  const category = categories.value.find(c => c.key === key)
  return category ? category.name : ''
}

// 配置变更处理
const handleConfigChange = async (key, value) => {
  try {
    const config = currentConfigs.value.find(c => c.key === key)
    if (!config) return
    
    // 验证配置值
    const validation = validateConfigValue(config, value)
    if (!validation.valid) {
      ElMessage.error(validation.message)
      // 恢复原始值
      configForm[key] = config.value
      return
    }
    
    await updateConfig(key, value)
    
    // 更新本地数据
    config.value = value
    configForm[key] = value
    
  } catch (error) {
    console.error('配置更新失败:', error)
  }
}

// 重置单个配置
const handleReset = async (key) => {
  try {
    await resetConfig(key)
    
    // 更新表单数据
    const config = currentConfigs.value.find(c => c.key === key)
    if (config) {
      configForm[key] = config.defaultValue
    }
    
  } catch (error) {
    console.error('配置重置失败:', error)
  }
}

// 重置当前分类
const handleBatchReset = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要重置 ${getCategoryName(currentCategory.value)} 的所有配置吗？`,
      '确认重置',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 重置当前分类的所有配置
    const updates = currentConfigs.value.map(config => ({
      key: config.key,
      value: config.defaultValue
    }))
    
    await updateConfigs(updates)
    
    // 更新表单数据
    currentConfigs.value.forEach(config => {
      configForm[config.key] = config.defaultValue
    })
    
    ElMessage.success('配置重置成功')
    
  } catch (error) {
    if (error !== 'cancel') {
      console.error('配置重置失败:', error)
    }
  }
}

// 重置所有配置
const handleResetAll = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要重置所有配置吗？此操作不可恢复！',
      '确认重置',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await resetAllConfigs()
    
    // 更新表单数据
    configs.value.forEach(config => {
      configForm[config.key] = config.defaultValue
    })
    
    ElMessage.success('所有配置重置成功')
    
  } catch (error) {
    if (error !== 'cancel') {
      console.error('配置重置失败:', error)
    }
  }
}

// 保存修改
const handleBatchSave = async () => {
  try {
    const updates = currentConfigs.value.map(config => ({
      key: config.key,
      value: configForm[config.key]
    }))
    
    await updateConfigs(updates)
    ElMessage.success('配置保存成功')
    
  } catch (error) {
    console.error('配置保存失败:', error)
  }
}

// 导出配置
const handleExport = async () => {
  try {
    await exportConfigs(currentCategory.value)
  } catch (error) {
    console.error('配置导出失败:', error)
  }
}

// 导入配置
const handleImport = () => {
  importDialog.visible = true
  importDialog.fileList = []
}

// 文件变更处理
const handleFileChange = (file) => {
  importDialog.fileList = [file]
}

// 确认导入
const confirmImport = async () => {
  if (importDialog.fileList.length === 0) {
    ElMessage.error('请选择配置文件')
    return
  }
  
  try {
    const file = importDialog.fileList[0].raw
    await importConfigs(file)
    
    // 重新加载配置
    if (currentCategory.value) {
      await selectCategory(currentCategory.value)
    }
    
    importDialog.visible = false
    
  } catch (error) {
    console.error('配置导入失败:', error)
  }
}

// 格式化日期
const formatDate = (dateString) => {
  return new Date(dateString).toLocaleString('zh-CN')
}

// 初始化
onMounted(async () => {
  // 获取配置分类
  categories.value = getConfigCategories()
})
</script>

<style scoped lang="scss">
.config-page {
  padding: 20px;

  .page-header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 30px;
    border-radius: 12px;
    margin-bottom: 20px;

    .header-content {
      .header-title {
        display: flex;
        align-items: center;
        gap: 15px;
        margin-bottom: 10px;

        .header-icon {
          font-size: 32px;
        }

        h1 {
          margin: 0;
          font-size: 28px;
          font-weight: 600;
        }
      }

      .header-description {
        margin: 0;
        opacity: 0.9;
        font-size: 16px;
      }
    }
  }

  .breadcrumb {
    margin-bottom: 20px;
  }

  .config-categories {
    margin-bottom: 30px;

    .category-card {
      background: white;
      border-radius: 12px;
      padding: 20px;
      display: flex;
      align-items: center;
      gap: 15px;
      cursor: pointer;
      transition: all 0.3s ease;
      border: 2px solid transparent;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
      }

      &.active {
        border-color: #409eff;
        background: #f0f7ff;
      }

      .category-icon {
        width: 50px;
        height: 50px;
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 24px;
      }

      .category-info {
        flex: 1;

        h3 {
          margin: 0 0 5px 0;
          font-size: 16px;
          font-weight: 600;
        }

        p {
          margin: 0;
          color: #909399;
          font-size: 14px;
        }
      }

      .category-arrow {
        color: #c0c4cc;
        font-size: 16px;
      }
    }
  }

  .config-list {
    .list-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      h3 {
        margin: 0;
        font-size: 20px;
        font-weight: 600;
      }

      .list-actions {
        display: flex;
        gap: 10px;
      }
    }

    .config-card {
      .config-item {
        padding: 15px 0;
        border-bottom: 1px solid #f0f0f0;

        &:last-child {
          border-bottom: none;
        }

        .config-label {
          display: flex;
          align-items: center;
          gap: 8px;

          .info-icon {
            color: #909399;
            cursor: help;
          }
        }

        .reset-btn {
          margin-left: 10px;
        }

        .config-meta {
          margin-top: 8px;
          font-size: 12px;
          color: #909399;
          display: flex;
          gap: 15px;
        }
      }
    }
  }

  .batch-actions {
    margin-top: 20px;
    display: flex;
    gap: 10px;
    justify-content: center;
  }

  .import-content {
    .upload-area {
      width: 100%;

      :deep(.el-upload-dragger) {
        width: 100%;
        height: 200px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
      }

      .upload-icon {
        font-size: 48px;
        color: #409eff;
        margin-bottom: 16px;
      }

      .upload-text {
        text-align: center;

        .upload-title {
          font-size: 16px;
          color: #303133;
          margin-bottom: 8px;
        }

        .upload-desc {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }
}

// 深色主题适配
:deep(.dark) {
  .config-page {
    .category-card {
      background: #1f1f1f;
      border: 1px solid #303030;

      &.active {
        border-color: #409eff;
        background: #1a2332;
      }
    }

    .config-card {
      background: #1f1f1f;
      border: 1px solid #303030;

      .config-item {
        border-bottom-color: #303030;
      }
    }
  }
}
</style>