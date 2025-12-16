<template>
  <base-wrapper>
    <!-- 页面头部 -->
    <div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
      <div class="flex-c-center-start h-full">
        <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">图片全网信息获取</span>
        </div>
        <div class="m-x-20 m-t-4">
          <span style="font-size: 14px; color: #FFFFFF;">上传图片，获取全网信息、OCR识别结果、相似图片等</span>
        </div>
      </div>
      <div class="flex-start-end h-full m-r-10">
        <img src="@/assets/logo/document.png" height="130px" />
      </div>
    </div>
    
    <el-breadcrumb separator="/" class="m-20">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/image_search' }">图片全网信息获取</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧：图片上传区域 -->
      <div class="left-panel">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>图片上传</span>
            </div>
          </template>
          
          <!-- 图片上传 -->
          <div class="upload-area">
            <el-upload
              ref="imageUploadRef"
              drag
              action=""
              :headers="uploadHeaders"
              :auto-upload="false"
              :file-list="fileList"
              :on-change="handleFileChange"
              :on-remove="handleFileRemove"
              accept="image/*"
              :limit="1"
            >
              <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
              <div class="el-upload__text">拖拽图片到此处，或 <em>点击上传</em></div>
              <template #tip>
                <div class="el-upload__tip">
                  支持JPG、PNG、GIF等格式，文件大小不超过50MB
                </div>
              </template>
            </el-upload>
            
            <!-- 或通过URL上传 -->
            <div class="url-upload-section">
              <el-divider content-position="left">或通过URL</el-divider>
              <el-input
                v-model="imageUrl"
                placeholder="输入图片URL"
                clearable
                prefix-icon="Link"
                style="margin-bottom: 15px;"
              />
            </div>
            
            <!-- 操作按钮 -->
            <div class="action-buttons">
              <el-button
                type="primary"
                @click="handleSearch"
                :loading="loading"
                :disabled="!canSearch"
              >
                获取图片信息
              </el-button>
              <el-button
                @click="handleClear"
                :disabled="loading"
              >
                清空
              </el-button>
            </div>
          </div>
        </el-card>

        <!-- 预览区域 -->
        <el-card shadow="hover" v-if="previewImage">
          <template #header>
            <div class="card-header">
              <span>图片预览</span>
            </div>
          </template>
          <div class="preview-area">
            <img :src="previewImage" alt="图片预览" class="preview-image" />
          </div>
        </el-card>
      </div>

      <!-- 右侧：结果展示区域 -->
      <div class="right-panel">
        <el-card shadow="hover" v-if="resultData">
          <template #header>
            <div class="card-header">
              <span>查询结果</span>
            </div>
          </template>

          <!-- 加载状态 -->
          <el-skeleton :rows="10" animated v-if="loadingResult" />

          <!-- 错误信息 -->
          <el-alert
            v-else-if="resultData.error"
            title="获取失败"
            :description="resultData.error"
            type="error"
            show-icon
          />

          <!-- 结果展示 -->
          <div v-else class="result-content">
            <!-- OCR识别结果 -->
            <div class="result-section">
              <h3 class="section-title">
                <el-icon><Document /></el-icon>
                OCR文字识别
              </h3>
              <el-card shadow="never" class="result-card">
                <pre v-if="resultData.ocrResult" class="ocr-result">{{ resultData.ocrResult }}</pre>
                <div v-else class="no-data">未识别到文字</div>
              </el-card>
            </div>

            <!-- 物体识别结果 -->
            <div class="result-section">
              <h3 class="section-title">
                <el-icon><View /></el-icon>
                物体识别
              </h3>
              <el-card shadow="never" class="result-card">
                <div v-if="resultData.objectRecognition" class="object-recognition">
                  <div v-if="resultData.objectRecognition.objects && resultData.objectRecognition.objects.length">
                    <el-tag
                      v-for="(obj, index) in resultData.objectRecognition.objects"
                      :key="index"
                      size="small"
                      class="object-tag"
                    >
                      {{ obj }}
                    </el-tag>
                  </div>
                  <div v-else class="no-data">未识别到物体</div>
                </div>
                <div v-else class="no-data">未识别到物体</div>
              </el-card>
            </div>

            <!-- 相似图片 -->
            <div class="result-section">
              <h3 class="section-title">
                <el-icon><Collection /></el-icon>
                相似图片
              </h3>
              <el-card shadow="never" class="result-card">
                <div v-if="resultData.similarImages" class="similar-images">
                  <div v-if="resultData.similarImages.similarImages && resultData.similarImages.similarImages.length">
                    <div class="image-grid">
                      <div
                        v-for="(img, index) in resultData.similarImages.similarImages"
                        :key="index"
                        class="similar-image-item"
                      >
                        <el-image
                          :src="img"
                          :preview-src-list="[img]"
                          class="similar-image"
                        >
                          <template #error>
                            <div class="image-error">加载失败</div>
                          </template>
                        </el-image>
                      </div>
                    </div>
                  </div>
                  <div v-else class="no-data">{{ resultData.similarImages.message || '未找到相似图片' }}</div>
                </div>
                <div v-else class="no-data">未找到相似图片</div>
              </el-card>
            </div>

            <!-- 图片来源分析 -->
            <div class="result-section">
              <h3 class="section-title">
                <el-icon><Location /></el-icon>
                图片来源分析
              </h3>
              <el-card shadow="never" class="result-card">
                <div v-if="resultData.sourceAnalysis" class="source-analysis">
                  <div v-if="resultData.sourceAnalysis.source" class="source-item">
                    <span class="label">来源：</span>
                    <span class="value">{{ resultData.sourceAnalysis.source }}</span>
                  </div>
                  <div v-if="resultData.sourceAnalysis.exifInfo" class="exif-info">
                    <span class="label">EXIF信息：</span>
                    <span class="value">{{ resultData.sourceAnalysis.exifInfo }}</span>
                  </div>
                  <div v-if="resultData.sourceAnalysis.error" class="error-item">
                    {{ resultData.sourceAnalysis.error }}
                  </div>
                </div>
                <div v-else class="no-data">未获取到来源信息</div>
              </el-card>
            </div>

            <!-- LLM智能分析 -->
            <div class="result-section" v-if="resultData.intelligentAnalysis">
              <h3 class="section-title">
                <el-icon><Brain /></el-icon>
                智能分析
              </h3>
              <el-card shadow="never" class="result-card">
                <div class="intelligent-analysis">
                  {{ resultData.intelligentAnalysis }}
                </div>
              </el-card>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  
  <!-- 图片查看器 -->
  <el-image-viewer
    v-if="previewVisible"
    :url-list="previewImageList"
    :on-close="handlePreviewClose"
  ></el-image-viewer>

  <!-- 提示对话框 -->
  <el-dialog
    v-model="tipsVisible"
    title="使用提示"
    width="500px"
  >
    <div class="tips-content">
      <el-timeline>
        <el-timeline-item>
          <div>
            <el-tag type="primary" size="small">1</el-tag>
            支持本地上传和URL输入两种方式
          </div>
        </el-timeline-item>
        <el-timeline-item>
          <div>
            <el-tag type="primary" size="small">2</el-tag>
            目前仅支持单张图片处理
          </div>
        </el-timeline-item>
        <el-timeline-item>
          <div>
            <el-tag type="primary" size="small">3</el-tag>
            外部API功能需要在后台配置启用
          </div>
        </el-timeline-item>
        <el-timeline-item>
          <div>
            <el-tag type="primary" size="small">4</el-tag>
            处理大型图片可能需要较长时间
          </div>
        </el-timeline-item>
      </el-timeline>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="tipsVisible = false">我知道了</el-button>
      </div>
    </template>
  </el-dialog>
</base-wrapper>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { UploadFilled, Document, View, Collection, Location, Brain } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const { proxy } = getCurrentInstance()

// 响应式数据
const imageUploadRef = ref(null)
const fileList = ref([])
const imageUrl = ref('')
const loading = ref(false)
const loadingResult = ref(false)
const resultData = ref(null)
const previewImage = ref('')
const previewVisible = ref(false)
const previewImageList = ref([])
const tipsVisible = ref(false)

// 上传配置
const baseUrl = import.meta.env.VITE_APP_BASE_API
const uploadHeaders = ref({
  Authorization: 'Bearer ' + localStorage.getItem('token'),
  BackendAddress: baseUrl
})

// 计算属性
const canSearch = computed(() => {
  return fileList.value.length > 0 || imageUrl.value.trim()
})

// 方法
// 文件变化处理
const handleFileChange = (file) => {
  if (fileList.value.length > 0) {
    fileList.value = [file]
  }
  
  // 显示预览
  const reader = new FileReader()
  reader.onload = (e) => {
    previewImage.value = e.target.result
  }
  reader.readAsDataURL(file.raw)
  
  // 清空URL输入
  imageUrl.value = ''
}

// 文件移除处理
const handleFileRemove = () => {
  fileList.value = []
  previewImage.value = ''
}

// 搜索图片信息
const handleSearch = async () => {
  if (!canSearch.value) {
    ElMessage.warning('请上传图片或输入图片URL')
    return
  }
  
  loading.value = true
  loadingResult.value = true
  resultData.value = null
  
  try {
    if (fileList.value.length > 0) {
      // 通过文件上传
      const formData = new FormData()
      formData.append('file', fileList.value[0].raw)
      
      const res = await proxy.$api.image_search.uploadAndSearch(formData)
      resultData.value = res.data
    } else if (imageUrl.value.trim()) {
      // 通过URL获取
      const res = await proxy.$api.image_search.searchByUrl({ imageUrl: imageUrl.value.trim() })
      resultData.value = res.data
    }
    
    ElMessage.success('获取图片信息成功')
  } catch (error) {
    console.error('获取图片信息失败:', error)
    ElMessage.error('获取图片信息失败：' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
    loadingResult.value = false
  }
}

// 清空
const handleClear = () => {
  fileList.value = []
  imageUrl.value = ''
  previewImage.value = ''
  resultData.value = null
}

// 查看大图
const handlePreview = (url) => {
  previewImageList.value = [url]
  previewVisible.value = true
}

// 关闭预览
const handlePreviewClose = () => {
  previewVisible.value = false
}

// 初始化
onMounted(() => {
  // 显示使用提示
  nextTick(() => {
    tipsVisible.value = true
  })
})
</script>

<style lang="scss" scoped>
.main-content {
  display: flex;
  gap: 20px;
  padding: 0 20px 20px;
}

.left-panel {
  width: 400px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.right-panel {
  flex: 1;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.upload-area {
  padding: 20px 0;
}

.url-upload-section {
  margin-top: 20px;
}

.action-buttons {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}

.preview-area {
  display: flex;
  justify-content: center;
  padding: 10px;
  
  .preview-image {
    max-width: 100%;
    max-height: 300px;
    border-radius: 4px;
  }
}

.result-content {
  .result-section {
    margin-bottom: 25px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .section-title {
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 12px;
    color: #333;
    display: flex;
    align-items: center;
    gap: 8px;
  }
  
  .result-card {
    margin-bottom: 10px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .ocr-result {
    white-space: pre-wrap;
    word-break: break-all;
    font-family: monospace;
    padding: 10px;
    background-color: #f8f9fa;
    border-radius: 4px;
    max-height: 200px;
    overflow-y: auto;
  }
  
  .object-recognition {
    .object-tag {
      margin-right: 10px;
      margin-bottom: 10px;
    }
  }
  
  .similar-images {
    .image-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
      gap: 10px;
      
      .similar-image-item {
        cursor: pointer;
        
        .similar-image {
          width: 100%;
          height: 100px;
          object-fit: cover;
          border-radius: 4px;
          transition: transform 0.3s;
          
          &:hover {
            transform: scale(1.05);
          }
        }
      }
    }
  }
  
  .source-analysis {
    .source-item,
    .exif-info {
      margin-bottom: 10px;
      
      .label {
        font-weight: 500;
        color: #606266;
      }
      
      .value {
        color: #303133;
      }
    }
    
    .error-item {
      color: #f56c6c;
    }
  }
  
  .intelligent-analysis {
    line-height: 1.6;
    color: #303133;
  }
  
  .no-data {
    color: #909399;
    text-align: center;
    padding: 20px 0;
  }
}

.tips-content {
  .el-timeline {
    padding-left: 20px;
  }
  
  .el-timeline-item {
    padding-bottom: 20px;
    
    &:last-child {
      padding-bottom: 0;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
