<template>
  <el-dialog
    v-model="dialogVisible"
    title="内容预览"
    width="900px"
    top="5vh"
  >
    <div v-loading="loading" class="preview-container">
      <!-- 预览头部信息 -->
      <div class="preview-header">
        <div class="header-left">
          <h3>{{ content.title }}</h3>
          <div class="content-meta">
            <el-tag size="small">{{ getContentTypeLabel(content.type) }}</el-tag>
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
        </div>
        <div class="header-right">
          <div class="meta-item">
            <span class="label">文件大小:</span>
            <span class="value">{{ formatFileSize(content.size) }}</span>
          </div>
          <div class="meta-item">
            <span class="label">使用次数:</span>
            <span class="value">{{ content.usageCount || 0 }}</span>
          </div>
          <div class="meta-item">
            <span class="label">创建时间:</span>
            <span class="value">{{ content.createdAt }}</span>
          </div>
        </div>
      </div>

      <!-- 内容描述 -->
      <div v-if="content.description" class="content-description">
        <h4>内容描述</h4>
        <p>{{ content.description }}</p>
      </div>

      <!-- 内容预览区域 -->
      <div class="preview-content">
        <el-tabs v-model="activeTab" class="preview-tabs">
          <!-- 主内容预览 -->
          <el-tab-pane label="主内容" name="main">
            <div class="main-preview">
              <!-- 图片预览 -->
              <div v-if="content.type === 'image'" class="image-preview">
                <img 
                  v-for="(image, index) in content.images" 
                  :key="index"
                  :src="image.url" 
                  :alt="`图片 ${index + 1}`"
                  class="preview-image"
                  @click="handleImagePreview(image.url)"
                />
              </div>

              <!-- 视频预览 -->
              <div v-else-if="content.type === 'video'" class="video-preview">
                <div class="video-container">
                  <video 
                    v-if="content.videos && content.videos[0]"
                    :src="content.videos[0].url"
                    controls
                    class="preview-video"
                    :poster="content.thumbnail"
                  >
                    您的浏览器不支持视频播放
                  </video>
                  <div v-else class="video-placeholder">
                    <el-icon><VideoPlay /></el-icon>
                    <p>视频预览</p>
                    <span v-if="content.duration">时长: {{ content.duration }}</span>
                  </div>
                </div>
              </div>

              <!-- 文本预览 -->
              <div v-else-if="content.type === 'text'" class="text-preview">
                <div 
                  class="text-content"
                  :style="{
                    fontSize: textConfig.fontSize,
                    color: textConfig.color,
                    lineHeight: '1.6'
                  }"
                >
                  {{ content.content }}
                </div>
              </div>

              <!-- 轮播预览 -->
              <div v-else-if="content.type === 'carousel'" class="carousel-preview">
                <el-carousel 
                  height="400px" 
                  type="card" 
                  trigger="click"
                  indicator-position="outside"
                >
                  <el-carousel-item 
                    v-for="(image, index) in content.carouselImages" 
                    :key="index"
                  >
                    <img 
                      :src="image.url" 
                      :alt="`轮播图 ${index + 1}`"
                      class="carousel-image"
                      @click="handleImagePreview(image.url)"
                    />
                  </el-carousel-item>
                </el-carousel>
              </div>
            </div>
          </el-tab-pane>

          <!-- 平台适配预览 -->
          <el-tab-pane label="平台适配" name="platform">
            <div class="platform-preview">
              <div 
                v-for="platform in content.platforms" 
                :key="platform"
                class="platform-section"
              >
                <h4>{{ getPlatformName(platform) }}</h4>
                <div class="platform-mockup">
                  <!-- 微信预览 -->
                  <div v-if="platform === 'wechat'" class="wechat-mockup">
                    <div class="wechat-header">
                      <div class="wechat-title">微信</div>
                    </div>
                    <div class="wechat-content">
                      <img 
                        v-if="content.images && content.images[0]"
                        :src="content.images[0].url" 
                        alt="微信预览"
                        class="wechat-image"
                      />
                      <div class="wechat-text">
                        {{ content.title }}
                      </div>
                    </div>
                  </div>

                  <!-- 抖音预览 -->
                  <div v-else-if="platform === 'douyin'" class="douyin-mockup">
                    <div class="douyin-container">
                      <video 
                        v-if="content.videos && content.videos[0]"
                        :src="content.videos[0].url"
                        class="douyin-video"
                        muted
                        loop
                      />
                      <div v-else class="douyin-placeholder">
                        <el-icon><VideoPlay /></el-icon>
                      </div>
                      <div class="douyin-info">
                        <div class="douyin-title">{{ content.title }}</div>
                        <div class="douyin-stats">
                          <span>👍 1.2K</span>
                          <span>💬 89</span>
                          <span>📤 23</span>
                        </div>
                      </div>
                    </div>
                  </div>

                  <!-- 小红书预览 -->
                  <div v-else-if="platform === 'xiaohongshu'" class="xiaohongshu-mockup">
                    <div class="xiaohongshu-card">
                      <div class="xiaohongshu-images">
                        <img 
                          v-for="(image, index) in content.carouselImages?.slice(0, 3)" 
                          :key="index"
                          :src="image.url" 
                          alt="小红书预览"
                          class="xiaohongshu-image"
                        />
                      </div>
                      <div class="xiaohongshu-content">
                        <div class="xiaohongshu-title">{{ content.title }}</div>
                        <div class="xiaohongshu-text">{{ content.description }}</div>
                      </div>
                    </div>
                  </div>

                  <!-- 微博预览 -->
                  <div v-else-if="platform === 'weibo'" class="weibo-mockup">
                    <div class="weibo-post">
                      <div class="weibo-header">
                        <div class="weibo-avatar"></div>
                        <div class="weibo-user">
                          <div class="weibo-name">品牌账号</div>
                          <div class="weibo-time">刚刚</div>
                        </div>
                      </div>
                      <div class="weibo-content">
                        <div class="weibo-text">{{ content.title }}</div>
                        <img 
                          v-if="content.images && content.images[0]"
                          :src="content.images[0].url" 
                          alt="微博预览"
                          class="weibo-image"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <!-- SEO信息 -->
          <el-tab-pane label="SEO信息" name="seo">
            <div class="seo-preview">
              <div class="seo-item">
                <label>SEO标题:</label>
                <div class="seo-value">{{ content.seoTitle || '未设置' }}</div>
              </div>
              <div class="seo-item">
                <label>关键词:</label>
                <div class="seo-value">
                  <el-tag 
                    v-for="keyword in (content.keywords || '').split(',').filter(k => k.trim())" 
                    :key="keyword"
                    size="small"
                  >
                    {{ keyword.trim() }}
                  </el-tag>
                </div>
              </div>
              <div class="seo-item">
                <label>SEO描述:</label>
                <div class="seo-value">{{ content.seoDescription || '未设置' }}</div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleEdit">编辑内容</el-button>
        <el-button type="primary" @click="dialogVisible = false">关闭预览</el-button>
      </div>
    </template>

    <!-- 图片预览对话框 -->
    <el-dialog
      v-model="showImagePreview"
      title="图片预览"
      width="fit-content"
      center
    >
      <img :src="previewImageUrl" alt="预览图片" style="max-width: 100%; max-height: 80vh;" />
    </el-dialog>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { VideoPlay } from '@element-plus/icons-vue'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  content: {
    type: Object,
    default: () => ({})
  }
})

// Emits
const emit = defineEmits(['update:modelValue'])

// 响应式数据
const loading = ref(false)
const activeTab = ref('main')
const showImagePreview = ref(false)
const previewImageUrl = ref('')

// 文本配置
const textConfig = ref({
  fontSize: '16px',
  color: '#333333'
})

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

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
  if (!bytes) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i]
}

const handleImagePreview = (url) => {
  previewImageUrl.value = url
  showImagePreview.value = true
}

const handleEdit = () => {
  // 编辑内容的逻辑
  dialogVisible.value = false
  // 这里可以触发编辑对话框
}

// 监听内容变化，设置默认配置
watch(() => props.content, (newContent) => {
  if (newContent.textConfig) {
    textConfig.value = { ...textConfig.value, ...newContent.textConfig }
  }
}, { immediate: true })
</script>

<style scoped>
.preview-container {
  min-height: 500px;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
  padding: 16px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.header-left h3 {
  margin: 0 0 8px 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.content-meta {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.header-right {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.meta-item .label {
  color: #909399;
}

.meta-item .value {
  color: #606266;
  font-weight: 500;
}

.content-description {
  margin-bottom: 20px;
  padding: 16px;
  background-color: #f5f5f5;
  border-radius: 6px;
}

.content-description h4 {
  margin: 0 0 8px 0;
  color: #409EFF;
  font-size: 14px;
}

.content-description p {
  margin: 0;
  color: #606266;
  line-height: 1.5;
}

.preview-content {
  margin-top: 20px;
}

.main-preview {
  min-height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.image-preview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  width: 100%;
}

.preview-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.3s;
}

.preview-image:hover {
  transform: scale(1.05);
}

.video-preview {
  width: 100%;
  display: flex;
  justify-content: center;
}

.video-container {
  max-width: 600px;
  width: 100%;
}

.preview-video {
  width: 100%;
  max-height: 400px;
  border-radius: 8px;
}

.video-placeholder {
  width: 100%;
  height: 400px;
  background-color: #f5f5f5;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  color: #909399;
}

.video-placeholder .el-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.text-preview {
  width: 100%;
  max-width: 600px;
  padding: 24px;
  background-color: #fafafa;
  border-radius: 8px;
  border-left: 4px solid #409EFF;
}

.text-content {
  word-wrap: break-word;
  white-space: pre-wrap;
}

.carousel-preview {
  width: 100%;
  max-width: 800px;
}

.carousel-image {
  width: 100%;
  height: 400px;
  object-fit: cover;
  cursor: pointer;
}

.platform-preview {
  max-height: 500px;
  overflow-y: auto;
}

.platform-section {
  margin-bottom: 24px;
}

.platform-section h4 {
  margin: 0 0 16px 0;
  color: #409EFF;
  font-size: 16px;
}

.platform-mockup {
  display: flex;
  justify-content: center;
}

/* 微信预览样式 */
.wechat-mockup {
  width: 300px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.wechat-header {
  background-color: #07C160;
  padding: 12px;
  text-align: center;
  color: white;
  font-weight: 600;
}

.wechat-content {
  padding: 12px;
}

.wechat-image {
  width: 100%;
  border-radius: 4px;
  margin-bottom: 8px;
}

.wechat-text {
  font-size: 14px;
  color: #333;
}

/* 抖音预览样式 */
.douyin-mockup {
  width: 250px;
  background-color: #000000;
  border-radius: 8px;
  overflow: hidden;
  position: relative;
}

.douyin-container {
  position: relative;
  width: 100%;
  height: 400px;
}

.douyin-video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.douyin-placeholder {
  width: 100%;
  height: 100%;
  background-color: #1a1a1a;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
}

.douyin-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  color: white;
  padding: 20px 12px 12px;
}

.douyin-title {
  font-size: 14px;
  margin-bottom: 8px;
}

.douyin-stats {
  display: flex;
  gap: 12px;
  font-size: 12px;
}

/* 小红书预览样式 */
.xiaohongshu-mockup {
  width: 320px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.xiaohongshu-images {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2px;
  height: 200px;
}

.xiaohongshu-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.xiaohongshu-content {
  padding: 12px;
}

.xiaohongshu-title {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #333;
}

.xiaohongshu-text {
  font-size: 12px;
  color: #666;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 微博预览样式 */
.weibo-mockup {
  width: 350px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.weibo-post {
  padding: 12px;
}

.weibo-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.weibo-avatar {
  width: 32px;
  height: 32px;
  background-color: #409EFF;
  border-radius: 50%;
}

.weibo-user {
  flex: 1;
}

.weibo-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.weibo-time {
  font-size: 12px;
  color: #909399;
}

.weibo-content {
  margin-left: 40px;
}

.weibo-text {
  font-size: 14px;
  color: #333;
  margin-bottom: 8px;
  line-height: 1.5;
}

.weibo-image {
  width: 100%;
  border-radius: 6px;
}

.seo-preview {
  padding: 20px;
}

.seo-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 16px;
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 6px;
}

.seo-item label {
  min-width: 80px;
  font-weight: 600;
  color: #409EFF;
  margin-right: 12px;
}

.seo-value {
  flex: 1;
  color: #606266;
  line-height: 1.5;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>