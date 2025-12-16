<template>
  <el-dialog
    v-model="dialogVisible"
    title="内容库管理"
    width="1000px"
    top="5vh"
  >
    <div v-loading="loading" class="content-library-container">
      <!-- 内容库头部操作 -->
      <div class="library-header">
        <div class="header-info">
          <h3>营销内容库</h3>
          <p>管理和组织所有营销内容素材</p>
        </div>
        <div class="header-actions">
          <el-button type="primary" :icon="Plus" @click="showCreateDialog = true">
            新建内容
          </el-button>
          <el-button :icon="Upload" @click="handleBatchUpload">
            批量上传
          </el-button>
          <el-button :icon="FolderOpened" @click="showFolderDialog = true">
            分类管理
          </el-button>
        </div>
      </div>

      <!-- 内容筛选 -->
      <div class="content-filters">
        <el-card shadow="never">
          <el-form :model="filterForm" :inline="true" class="filter-form">
            <el-form-item label="内容类型">
              <el-select
                v-model="filterForm.contentType"
                placeholder="选择类型"
                clearable
                style="width: 120px"
              >
                <el-option label="图片" value="image" />
                <el-option label="视频" value="video" />
                <el-option label="文本" value="text" />
                <el-option label="轮播" value="carousel" />
              </el-select>
            </el-form-item>
            <el-form-item label="分类">
              <el-select
                v-model="filterForm.category"
                placeholder="选择分类"
                clearable
                style="width: 120px"
              >
                <el-option
                  v-for="category in contentCategories"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="标签">
              <el-select
                v-model="filterForm.tags"
                multiple
                placeholder="选择标签"
                clearable
                style="width: 200px"
              >
                <el-option
                  v-for="tag in contentTags"
                  :key="tag.value"
                  :label="tag.label"
                  :value="tag.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="平台">
              <el-select
                v-model="filterForm.platform"
                placeholder="选择平台"
                clearable
                style="width: 120px"
              >
                <el-option
                  v-for="platform in platformOptions"
                  :key="platform.id"
                  :label="platform.name"
                  :value="platform.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" @click="handleSearch">
                搜索
              </el-button>
              <el-button :icon="Refresh" @click="handleReset">
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>

      <!-- 内容网格 -->
      <div class="content-grid-section">
        <el-card shadow="never">
          <template #header>
            <div class="grid-header">
              <span>内容列表 ({{ filteredContent.length }})</span>
              <div class="view-toggle">
                <el-button-group>
                  <el-button
                    :type="viewMode === 'grid' ? 'primary' : 'default'"
                    size="small"
                    @click="viewMode = 'grid'"
                  >
                    网格视图
                  </el-button>
                  <el-button
                    :type="viewMode === 'list' ? 'primary' : 'default'"
                    size="small"
                    @click="viewMode = 'list'"
                  >
                    列表视图
                  </el-button>
                </el-button-group>
              </div>
            </div>
          </template>

          <!-- 网格视图 -->
          <div v-if="viewMode === 'grid'" class="content-grid">
            <div
              v-for="content in filteredContent"
              :key="content.id"
              class="content-item"
              @click="handleSelectContent(content)"
            >
              <div class="content-preview">
                <div v-if="content.type === 'image'" class="image-preview">
                  <img :src="content.thumbnail || content.url" :alt="content.title" />
                  <div class="content-overlay">
                    <el-icon><View /></el-icon>
                  </div>
                </div>
                <div v-else-if="content.type === 'video'" class="video-preview">
                  <div class="video-thumbnail">
                    <img :src="content.thumbnail" :alt="content.title" />
                    <div class="play-button">
                      <el-icon><VideoPlay /></el-icon>
                    </div>
                  </div>
                  <div class="video-duration">{{ content.duration }}</div>
                </div>
                <div v-else-if="content.type === 'text'" class="text-preview">
                  <div class="text-content">
                    {{ content.content }}
                  </div>
                </div>
                <div v-else-if="content.type === 'carousel'" class="carousel-preview">
                  <div class="carousel-thumbnail">
                    <el-icon><Picture /></el-icon>
                    <span>{{ content.images?.length || 0 }}张</span>
                  </div>
                </div>
              </div>
              
              <div class="content-info">
                <div class="content-title">{{ content.title }}</div>
                <div class="content-meta">
                  <el-tag size="small">{{ getContentTypeLabel(content.type) }}</el-tag>
                  <span class="content-size">{{ formatFileSize(content.size) }}</span>
                </div>
                <div class="content-platforms">
                  <el-tag
                    v-for="platform in content.platforms"
                    :key="platform"
                    size="small"
                    :style="{ 
                      backgroundColor: getPlatformColor(platform),
                      color: 'white'
                    }"
                  >
                    {{ getPlatformName(platform) }}
                  </el-tag>
                </div>
                <div class="content-stats">
                  <span>使用 {{ content.usageCount }} 次</span>
                  <span>{{ content.createdAt }}</span>
                </div>
              </div>

              <div class="content-actions">
                <el-button size="small" :icon="Edit" @click.stop="handleEdit(content)">
                  编辑
                </el-button>
                <el-button size="small" :icon="CopyDocument" @click.stop="handleDuplicate(content)">
                  复制
                </el-button>
                <el-dropdown @command="(command) => handleCommand(command, content)">
                  <el-button size="small">
                    更多<el-icon class="el-icon--right"><arrow-down /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="preview">预览</el-dropdown-item>
                      <el-dropdown-item command="download">下载</el-dropdown-item>
                      <el-dropdown-item command="share">分享</el-dropdown-item>
                      <el-dropdown-item command="analytics">使用分析</el-dropdown-item>
                      <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </div>
          </div>

          <!-- 列表视图 -->
          <div v-else class="content-list">
            <el-table :data="filteredContent" @row-click="handleSelectContent">
              <el-table-column label="内容" width="300">
                <template #default="{ row }">
                  <div class="list-content-info">
                    <div class="content-thumbnail">
                      <img 
                        v-if="row.type === 'image' || row.type === 'video'"
                        :src="row.thumbnail || row.url" 
                        :alt="row.title"
                      />
                      <div v-else class="content-icon">
                        <el-icon><component :is="getContentTypeIcon(row.type)" /></el-icon>
                      </div>
                    </div>
                    <div class="content-details">
                      <div class="content-title">{{ row.title }}</div>
                      <div class="content-description">{{ row.description || '暂无描述' }}</div>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="type" label="类型" width="100">
                <template #default="{ row }">
                  <el-tag size="small">{{ getContentTypeLabel(row.type) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="平台" width="150">
                <template #default="{ row }">
                  <div class="platform-tags">
                    <el-tag
                      v-for="platform in row.platforms.slice(0, 2)"
                      :key="platform"
                      size="small"
                      :style="{ 
                        backgroundColor: getPlatformColor(platform),
                        color: 'white'
                      }"
                    >
                      {{ getPlatformName(platform) }}
                    </el-tag>
                    <span v-if="row.platforms.length > 2" class="more-platforms">
                      +{{ row.platforms.length - 2 }}
                    </span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="usageCount" label="使用次数" width="100" />
              <el-table-column prop="size" label="文件大小" width="100">
                <template #default="{ row }">
                  {{ formatFileSize(row.size) }}
                </template>
              </el-table-column>
              <el-table-column prop="createdAt" label="创建时间" width="120" />
              <el-table-column label="操作" width="200" fixed="right">
                <template #default="{ row }">
                  <el-button size="small" @click.stop="handleEdit(row)">编辑</el-button>
                  <el-button size="small" @click.stop="handleDuplicate(row)">复制</el-button>
                  <el-dropdown @command="(command) => handleCommand(command, row)">
                    <el-button size="small">
                      更多<el-icon class="el-icon--right"><arrow-down /></el-icon>
                    </el-button>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item command="preview">预览</el-dropdown-item>
                        <el-dropdown-item command="download">下载</el-dropdown-item>
                        <el-dropdown-item command="share">分享</el-dropdown-item>
                        <el-dropdown-item command="analytics">使用分析</el-dropdown-item>
                        <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 分页 -->
          <div class="pagination-section">
            <el-pagination
              v-model:current-page="pagination.currentPage"
              v-model:page-size="pagination.pageSize"
              :page-sizes="[12, 24, 48, 96]"
              :total="pagination.total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handlePageSizeChange"
              @current-change="handleCurrentPageChange"
            />
          </div>
        </el-card>
      </div>
    </div>

    <!-- 内容创建/编辑对话框 -->
    <ContentCreateDialog
      v-model="showCreateDialog"
      :content="selectedContent"
      @updated="handleContentUpdated"
    />

    <!-- 分类管理对话框 -->
    <CategoryManagementDialog
      v-model="showFolderDialog"
    />

    <!-- 批量上传对话框 -->
    <BatchUploadDialog
      v-model="showUploadDialog"
      @uploaded="handleBatchUploaded"
    />

    <!-- 预览对话框 -->
    <ContentPreviewDialog
      v-model="showPreviewDialog"
      :content="previewContent"
    />

    <!-- 使用分析对话框 -->
    <ContentAnalyticsDialog
      v-model="showAnalyticsDialog"
      :content="analyticsContent"
    />
  </el-dialog>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Plus, Upload, FolderOpened, Search, Refresh, View, VideoPlay,
  Picture, Edit, CopyDocument, ArrowDown
} from '@element-plus/icons-vue'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  campaign: {
    type: Object,
    default: null
  }
})

// Emits
const emit = defineEmits(['update:modelValue'])

// 响应式数据
const loading = ref(false)
const viewMode = ref('grid')
const showCreateDialog = ref(false)
const showFolderDialog = ref(false)
const showUploadDialog = ref(false)
const showPreviewDialog = ref(false)
const showAnalyticsDialog = ref(false)
const selectedContent = ref(null)
const previewContent = ref(null)
const analyticsContent = ref(null)

// 筛选表单
const filterForm = ref({
  contentType: '',
  category: '',
  tags: [],
  platform: ''
})

// 分页
const pagination = ref({
  currentPage: 1,
  pageSize: 24,
  total: 0
})

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// 模拟内容数据
const allContent = ref([
  {
    id: 1,
    title: '春季新品推广图片1',
    type: 'image',
    url: 'https://via.placeholder.com/300x200/409EFF/white?text=Image+1',
    thumbnail: 'https://via.placeholder.com/300x200/409EFF/white?text=Image+1',
    description: '春季新品推广的主视觉图片',
    platforms: ['wechat', 'weibo'],
    category: 'product',
    tags: ['spring', 'product', 'promotion'],
    size: 1024 * 1024 * 2.5,
    usageCount: 15,
    createdAt: '2024-01-15'
  },
  {
    id: 2,
    title: '产品介绍视频',
    type: 'video',
    url: 'https://example.com/video.mp4',
    thumbnail: 'https://via.placeholder.com/300x200/67C23A/white?text=Video',
    description: '产品功能介绍视频',
    platforms: ['douyin', 'xiaohongshu'],
    category: 'product',
    tags: ['product', 'tutorial'],
    size: 1024 * 1024 * 15.6,
    duration: '02:34',
    usageCount: 8,
    createdAt: '2024-01-14'
  },
  {
    id: 3,
    title: '品牌故事文案',
    type: 'text',
    content: '我们的品牌致力于为用户提供最优质的产品和服务...',
    platforms: ['wechat', 'zhihu'],
    category: 'brand',
    tags: ['brand', 'story'],
    size: 1024 * 2.3,
    usageCount: 23,
    createdAt: '2024-01-13'
  },
  {
    id: 4,
    title: '产品展示轮播',
    type: 'carousel',
    images: [
      'https://via.placeholder.com/300x200/409EFF/white?text=Slide+1',
      'https://via.placeholder.com/300x200/67C23A/white?text=Slide+2',
      'https://via.placeholder.com/300x200/E6A23C/white?text=Slide+3'
    ],
    platforms: ['wechat', 'xiaohongshu'],
    category: 'product',
    tags: ['product', 'showcase'],
    size: 1024 * 1024 * 5.8,
    usageCount: 12,
    createdAt: '2024-01-12'
  }
])

// 筛选后的内容
const filteredContent = computed(() => {
  let filtered = allContent.value

  if (filterForm.value.contentType) {
    filtered = filtered.filter(item => item.type === filterForm.value.contentType)
  }

  if (filterForm.value.category) {
    filtered = filtered.filter(item => item.category === filterForm.value.category)
  }

  if (filterForm.value.tags.length) {
    filtered = filtered.filter(item => 
      filterForm.value.tags.some(tag => item.tags.includes(tag))
    )
  }

  if (filterForm.value.platform) {
    filtered = filtered.filter(item => 
      item.platforms.includes(filterForm.value.platform)
    )
  }

  return filtered
})

// 静态配置数据
const contentCategories = [
  { id: 'product', name: '产品相关' },
  { id: 'brand', name: '品牌内容' },
  { id: 'promotion', name: '促销活动' },
  { id: 'event', name: '活动内容' },
  { id: 'other', name: '其他' }
]

const contentTags = [
  { value: 'spring', label: '春季' },
  { value: 'summer', label: '夏季' },
  { value: 'product', label: '产品' },
  { value: 'brand', label: '品牌' },
  { value: 'promotion', label: '促销' },
  { value: 'tutorial', label: '教程' },
  { value: 'showcase', label: '展示' },
  { value: 'story', label: '故事' }
]

const platformOptions = [
  { id: 'wechat', name: '微信' },
  { id: 'weibo', name: '微博' },
  { id: 'douyin', name: '抖音' },
  { id: 'xiaohongshu', name: '小红书' },
  { id: 'zhihu', name: '知乎' },
  { id: 'bilibili', name: 'B站' }
]

// 方法
const getContentTypeLabel = (type) => {
  const typeMap = {
    image: '图片',
    video: '视频',
    text: '文本',
    carousel: '轮播'
  }
  return typeMap[type] || type
}

const getContentTypeIcon = (type) => {
  const iconMap = {
    image: 'Picture',
    video: 'VideoPlay',
    text: 'Document',
    carousel: 'Grid'
  }
  return iconMap[type] || 'Document'
}

const getPlatformName = (platformId) => {
  const platformMap = {
    wechat: '微信',
    weibo: '微博',
    douyin: '抖音',
    xiaohongshu: '小红书',
    zhihu: '知乎',
    bilibili: 'B站'
  }
  return platformMap[platformId] || platformId
}

const getPlatformColor = (platformId) => {
  const colorMap = {
    wechat: '#07C160',
    weibo: '#E6162D',
    douyin: '#FE2C55',
    xiaohongshu: '#FF2442',
    zhihu: '#0066FF',
    bilibili: '#00A1D6'
  }
  return colorMap[platformId] || '#909399'
}

const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i]
}

const handleSearch = () => {
  // 搜索逻辑
  pagination.value.currentPage = 1
}

const handleReset = () => {
  Object.assign(filterForm.value, {
    contentType: '',
    category: '',
    tags: [],
    platform: ''
  })
  pagination.value.currentPage = 1
}

const handlePageSizeChange = (size) => {
  pagination.value.pageSize = size
}

const handleCurrentPageChange = (page) => {
  pagination.value.currentPage = page
}

const handleSelectContent = (content) => {
  // 选择内容逻辑
  console.log('选择内容:', content)
}

const handleEdit = (content) => {
  selectedContent.value = content
  showCreateDialog.value = true
}

const handleDuplicate = async (content) => {
  try {
    // 模拟复制操作
    const newContent = {
      ...content,
      id: Date.now(),
      title: content.title + ' (副本)',
      usageCount: 0,
      createdAt: new Date().toISOString().split('T')[0]
    }
    allContent.value.unshift(newContent)
    ElMessage.success('内容复制成功')
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

const handleCommand = async (command, content) => {
  switch (command) {
    case 'preview':
      previewContent.value = content
      showPreviewDialog.value = true
      break
    case 'download':
      // 模拟下载
      ElMessage.success('下载开始')
      break
    case 'share':
      // 模拟分享
      ElMessage.success('分享链接已复制到剪贴板')
      break
    case 'analytics':
      analyticsContent.value = content
      showAnalyticsDialog.value = true
      break
    case 'delete':
      try {
        // 模拟删除确认
        const index = allContent.value.findIndex(item => item.id === content.id)
        if (index > -1) {
          allContent.value.splice(index, 1)
          ElMessage.success('内容删除成功')
        }
      } catch (error) {
        ElMessage.error('删除失败')
      }
      break
  }
}

const handleBatchUpload = () => {
  showUploadDialog.value = true
}

const handleContentUpdated = () => {
  // 内容更新后的处理
  console.log('内容已更新')
}

const handleBatchUploaded = () => {
  // 批量上传完成后的处理
  console.log('批量上传完成')
}

onMounted(() => {
  // 初始化加载内容数据
  pagination.value.total = allContent.value.length
})
</script>

<style scoped>
.content-library-container {
  max-height: 80vh;
  overflow-y: auto;
  padding: 0 8px;
}

.library-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.header-info h3 {
  margin: 0 0 4px 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.header-info p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.content-filters {
  margin-bottom: 20px;
}

.filter-form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.grid-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.view-toggle {
  display: flex;
  gap: 8px;
}

.content-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  padding: 20px 0;
}

.content-item {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
  cursor: pointer;
  background: white;
}

.content-item:hover {
  border-color: #409EFF;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
  transform: translateY(-2px);
}

.content-preview {
  position: relative;
  height: 180px;
  overflow: hidden;
}

.image-preview,
.video-preview {
  width: 100%;
  height: 100%;
}

.image-preview img,
.video-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.content-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.content-item:hover .content-overlay {
  opacity: 1;
}

.content-overlay .el-icon {
  color: white;
  font-size: 32px;
}

.play-button {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.video-duration {
  position: absolute;
  bottom: 8px;
  right: 8px;
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
}

.text-preview {
  padding: 16px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
}

.text-content {
  text-align: center;
  font-size: 14px;
  line-height: 1.5;
  color: #606266;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
}

.carousel-preview {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
}

.carousel-thumbnail {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  color: #909399;
}

.content-info {
  padding: 12px;
}

.content-title {
  font-weight: 500;
  color: #303133;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.content-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.content-size {
  font-size: 12px;
  color: #909399;
}

.content-platforms {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  margin-bottom: 8px;
}

.content-stats {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
}

.content-actions {
  padding: 12px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  gap: 8px;
}

.list-content-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.content-thumbnail {
  width: 60px;
  height: 60px;
  border-radius: 6px;
  overflow: hidden;
  flex-shrink: 0;
}

.content-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.content-icon {
  width: 60px;
  height: 60px;
  background-color: #f5f5f5;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 24px;
}

.content-details {
  flex: 1;
  min-width: 0;
}

.list-content-info .content-title {
  margin-bottom: 4px;
  white-space: nowrap;
}

.content-description {
  font-size: 12px;
  color: #909399;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.platform-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.more-platforms {
  font-size: 12px;
  color: #909399;
}

.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>