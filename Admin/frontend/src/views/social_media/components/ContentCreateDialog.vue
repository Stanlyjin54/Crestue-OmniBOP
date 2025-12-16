<template>
  <el-dialog
    :model-value="modelValue"
    @update:model-value="$emit('update:modelValue', $event)"
    :title="isEdit ? '编辑内容' : '创建内容'"
    width="800px"
    top="5vh"
  >
    <div v-loading="loading" class="content-form-container">
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
        class="content-form"
      >
        <!-- 基本信息 -->
        <el-card shadow="never" class="form-section">
          <template #header>
            <span class="section-title">基本信息</span>
          </template>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="内容标题" prop="title">
                 v-model="form<el-input
                 Data.title"
                  placeholder="请输入内容标题"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="内容类型" prop="type">
                <el-select
                  v-model="formData.type"
                  placeholder="选择内容类型"
                  style="width: 100%"
                  @change="handleTypeChange"
                >
                  <el-option label="图片" value="image" />
                  <el-option label="视频" value="video" />
                  <el-option label="文本" value="text" />
                  <el-option label="轮播" value="carousel" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="内容描述" prop="description">
            <el-input
              v-model="formData.description"
              type="textarea"
              placeholder="请输入内容描述"
              :rows="3"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>

          <el-form-item label="内容分类" prop="category">
            <el-select
              v-model="formData.category"
              placeholder="选择内容分类"
              style="width: 200px"
            >
              <el-option
                v-for="category in contentCategories"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="内容标签" prop="tags">
            <el-select
              v-model="formData.tags"
              multiple
              placeholder="选择或输入标签"
              filterable
              allow-create
              default-first-option
              style="width: 100%"
            >
              <el-option
                v-for="tag in contentTags"
                :key="tag.value"
                :label="tag.label"
                :value="tag.value"
              />
            </el-select>
          </el-form-item>
        </el-card>

        <!-- 平台适配 -->
        <el-card shadow="never" class="form-section">
          <template #header>
            <span class="section-title">平台适配</span>
          </template>
          
          <el-form-item label="适用平台" prop="platforms">
            <el-checkbox-group v-model="formData.platforms">
              <el-checkbox
                v-for="platform in platformOptions"
                :key="platform.id"
                :label="platform.id"
              >
                {{ platform.name }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>

          <div v-if="formData.platforms.includes('wechat')" class="platform-config">
            <h4>微信适配配置</h4>
            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="图片尺寸">
                  <el-select v-model="wechatConfig.imageSize" style="width: 100%">
                    <el-option label="1080x1080 (正方形)" value="1080x1080" />
                    <el-option label="900x500 (横图)" value="900x500" />
                    <el-option label="500x500 (小图)" value="500x500" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="文件大小">
                  <el-input v-model="wechatConfig.maxSize" disabled>
                    <template #append>MB</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <div v-if="formData.platforms.includes('douyin')" class="platform-config">
            <h4>抖音适配配置</h4>
            <el-row :gadge="16">
              <el-col :span="12">
                <el-form-item label="视频比例">
                  <el-select v-model="douyinConfig.videoRatio" style="width: 100%">
                    <el-option label="9:16 (竖屏)" value="9:16" />
                    <el-option label="16:9 (横屏)" value="16:9" />
                    <el-option label="1:1 (正方形)" value="1:1" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="时长限制">
                  <el-input v-model="douyinConfig.maxDuration" disabled>
                    <template #append>秒</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-card>

        <!-- 内容素材 -->
        <el-card shadow="never" class="form-section">
          <template #header>
            <span class="section-title">内容素材</span>
          </template>

          <!-- 图片内容 -->
          <div v-if="formData.type === 'image'">
            <el-form-item label="上传图片" prop="images">
              <el-upload
                ref="imageUploadRef"
                :file-list="imageFileList"
                :on-change="handleImageChange"
                :on-remove="handleImageRemove"
                :before-upload="beforeImageUpload"
                :auto-upload="false"
                list-type="picture-card"
                accept="image/*"
                multiple
                :limit="1"
              >
                <el-icon><Plus /></el-icon>
                <template #tip>
                  <div class="el-upload__tip">
                    支持 jpg/png/gif 格式，单个文件不超过 5MB
                  </div>
                </template>
              </el-upload>
            </el-form-item>
          </div>

          <!-- 视频内容 -->
          <div v-if="formData.type === 'video'">
            <el-form-item label="上传视频" prop="videos">
              <el-upload
                ref="videoUploadRef"
                :file-list="videoFileList"
                :on-change="handleVideoChange"
                :on-remove="handleVideoRemove"
                :before-upload="beforeVideoUpload"
                :auto-upload="false"
                accept="video/*"
                :limit="1"
              >
                <el-button :icon="Upload">选择视频文件</el-button>
                <template #tip>
                  <div class="el-upload__tip">
                    支持 mp4/avi/mov 格式，文件不超过 100MB
                  </div>
                </template>
              </el-upload>
            </el-form-item>

            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="视频封面">
                  <el-upload
                    :file-list="thumbnailFileList"
                    :on-change="handleThumbnailChange"
                    :on-remove="handleThumbnailRemove"
                    :before-upload="beforeImageUpload"
                    :auto-upload="false"
                    accept="image/*"
                    :limit="1"
                    list-type="picture-card"
                  >
                    <el-icon><Plus /></el-icon>
                  </el-upload>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="视频时长">
                  <el-input
                    v-model="formData.duration"
                    placeholder="如：02:30"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <!-- 文本内容 -->
          <div v-if="formData.type === 'text'">
            <el-form-item label="文本内容" prop="content">
              <el-input
                v-model="formData.content"
                type="textarea"
                placeholder="请输入文本内容"
                :rows="6"
                maxlength="2000"
                show-word-limit
              />
            </el-form-item>

            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="字体大小">
                  <el-select v-model="textConfig.fontSize" style="width: 100%">
                    <el-option label="12px" value="12px" />
                    <el-option label="14px" value="14px" />
                    <el-option label="16px" value="16px" />
                    <el-option label="18px" value="18px" />
                    <el-option label="20px" value="20px" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="文本颜色">
                  <el-color-picker v-model="textConfig.color" />
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <!-- 轮播内容 -->
          <div v-if="formData.type === 'carousel'">
            <el-form-item label="轮播图片" prop="carouselImages">
              <el-upload
                ref="carouselUploadRef"
                :file-list="carouselFileList"
                :on-change="handleCarouselChange"
                :on-remove="handleCarouselRemove"
                :before-upload="beforeImageUpload"
                :auto-upload="false"
                list-type="picture-card"
                accept="image/*"
                multiple
                :limit="9"
              >
                <el-icon><Plus /></el-icon>
                <template #tip>
                  <div class="el-upload__tip">
                    支持 1-9 张图片，建议统一尺寸
                  </div>
                </template>
              </el-upload>
            </el-form-item>
          </div>
        </el-card>

        <!-- SEO设置 -->
        <el-card shadow="never" class="form-section">
          <template #header>
            <span class="section-title">SEO 设置</span>
          </template>

          <el-row :gutter="16">
            <el-col :span="12">
              <el-form-item label="SEO 标题">
                <el-input
                  v-model="formData.seoTitle"
                  placeholder="SEO优化标题"
                  maxlength="60"
                  show-word-limit
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="关键词">
                <el-input
                  v-model="formData.keywords"
                  placeholder="用逗号分隔关键词"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="SEO 描述">
            <el-input
              v-model="formData.seoDescription"
              type="textarea"
              placeholder="SEO描述信息"
              :rows="3"
              maxlength="160"
              show-word-limit
            />
          </el-form-item>
        </el-card>
      </el-form>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button @click="handlePreview">预览</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">
          {{ isEdit ? '更新' : '创建' }}
        </el-button>
      </div>
    </template>

    <!-- 内容预览对话框 -->
    <ContentPreviewDialog
      v-model="showPreviewDialog"
      :content="previewData"
    />
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Upload, Plus } from '@element-plus/icons-vue'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  content: {
    type: Object,
    default: null
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'updated'])

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const showPreviewDialog = ref(false)
const formRef = ref(null)
const imageUploadRef = ref(null)
const videoUploadRef = ref(null)
const carouselUploadRef = ref(null)

// 表单数据
const formData = ref({
  title: '',
  type: '',
  description: '',
  category: '',
  tags: [],
  platforms: [],
  images: [],
  videos: [],
  thumbnails: [],
  content: '',
  duration: '',
  carouselImages: [],
  seoTitle: '',
  keywords: '',
  seoDescription: ''
})

// 平台配置
const wechatConfig = ref({
  imageSize: '1080x1080',
  maxSize: '5'
})

const douyinConfig = ref({
  videoRatio: '9:16',
  maxDuration: '60'
})

const textConfig = ref({
  fontSize: '16px',
  color: '#333333'
})

// 文件列表
const imageFileList = ref([])
const videoFileList = ref([])
const thumbnailFileList = ref([])
const carouselFileList = ref([])

// 计算属性
const isEdit = computed(() => !!props.content)

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
  { value: 'autumn', label: '秋季' },
  { value: 'winter', label: '冬季' },
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

const previewData = computed(() => ({
  ...formData.value,
  thumbnail: formData.value.thumbnails[0]?.url || '',
  size: calculateFileSize()
}))

// 表单验证规则
const formRules = {
  title: [
    { required: true, message: '请输入内容标题', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择内容类型', trigger: 'change' }
  ],
  category: [
    { required: true, message: '请选择内容分类', trigger: 'change' }
  ],
  platforms: [
    { type: 'array', required: true, message: '请选择适用平台', trigger: 'change' }
  ],
  images: [
    {
      validator: (rule, value, callback) => {
        if (formData.value.type === 'image' && (!value || value.length === 0)) {
          callback(new Error('请上传图片'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  videos: [
    {
      validator: (rule, value, callback) => {
        if (formData.value.type === 'video' && (!value || value.length === 0)) {
          callback(new Error('请上传视频'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  content: [
    {
      validator: (rule, value, callback) => {
        if (formData.value.type === 'text' && !value) {
          callback(new Error('请输入文本内容'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  carouselImages: [
    {
      validator: (rule, value, callback) => {
        if (formData.value.type === 'carousel' && (!value || value.length === 0)) {
          callback(new Error('请上传轮播图片'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ]
}

// 方法
const handleTypeChange = () => {
  // 清除相关字段
  formData.value.images = []
  formData.value.videos = []
  formData.value.thumbnails = []
  formData.value.content = ''
  formData.value.carouselImages = []
  imageFileList.value = []
  videoFileList.value = []
  thumbnailFileList.value = []
  carouselFileList.value = []
}

const handleImageChange = (file, fileList) => {
  formData.value.images = fileList
}

const handleImageRemove = (file, fileList) => {
  formData.value.images = fileList
}

const handleVideoChange = (file, fileList) => {
  formData.value.videos = fileList
}

const handleVideoRemove = (file, fileList) => {
  formData.value.videos = fileList
}

const handleThumbnailChange = (file, fileList) => {
  formData.value.thumbnails = fileList
}

const handleThumbnailRemove = (file, fileList) => {
  formData.value.thumbnails = fileList
}

const handleCarouselChange = (file, fileList) => {
  formData.value.carouselImages = fileList
}

const handleCarouselRemove = (file, fileList) => {
  formData.value.carouselImages = fileList
}

const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB')
    return false
  }
  return false // 阻止自动上传
}

const beforeVideoUpload = (file) => {
  const isVideo = file.type.startsWith('video/')
  const isLt100M = file.size / 1024 / 1024 < 100

  if (!isVideo) {
    ElMessage.error('只能上传视频文件')
    return false
  }
  if (!isLt100M) {
    ElMessage.error('视频大小不能超过 100MB')
    return false
  }
  return false // 阻止自动上传
}

const calculateFileSize = () => {
  // 模拟计算文件大小
  let totalSize = 0
  formData.value.images.forEach(file => {
    totalSize += file.size || 0
  })
  formData.value.videos.forEach(file => {
    totalSize += file.size || 0
  })
  formData.value.carouselImages.forEach(file => {
    totalSize += file.size || 0
  })
  return totalSize
}

const handleCancel = () => {
  emit('update:modelValue', false)
  resetForm()
}

const handlePreview = () => {
  showPreviewDialog.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true

    // 模拟提交
    setTimeout(() => {
      const submitData = {
        ...formData.value,
        id: props.content?.id || Date.now(),
        createdAt: new Date().toISOString().split('T')[0],
        usageCount: props.content?.usageCount || 0,
        size: calculateFileSize()
      }

      console.log('提交内容数据:', submitData)
      
      ElMessage.success(isEdit.value ? '内容更新成功' : '内容创建成功')
      emit('updated', submitData)
      emit('update:modelValue', false)
      resetForm()
      
      submitting.value = false
    }, 1500)

  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const resetForm = () => {
  Object.assign(formData.value, {
    title: '',
    type: '',
    description: '',
    category: '',
    tags: [],
    platforms: [],
    images: [],
    videos: [],
    thumbnails: [],
    content: '',
    duration: '',
    carouselImages: [],
    seoTitle: '',
    keywords: '',
    seoDescription: ''
  })
  
  Object.assign(wechatConfig.value, {
    imageSize: '1080x1080',
    maxSize: '5'
  })
  
  Object.assign(douyinConfig.value, {
    videoRatio: '9:16',
    maxDuration: '60'
  })
  
  Object.assign(textConfig.value, {
    fontSize: '16px',
    color: '#333333'
  })
  
  imageFileList.value = []
  videoFileList.value = []
  thumbnailFileList.value = []
  carouselFileList.value = []
}

// 监听对话框显示状态
watch(() => props.modelValue, (newVal) => {
  if (newVal && props.content) {
    // 编辑模式，填充表单数据
    Object.assign(formData.value, {
      title: props.content.title || '',
      type: props.content.type || '',
      description: props.content.description || '',
      category: props.content.category || '',
      tags: props.content.tags || [],
      platforms: props.content.platforms || [],
      content: props.content.content || '',
      duration: props.content.duration || '',
      seoTitle: props.content.seoTitle || '',
      keywords: props.content.keywords || '',
      seoDescription: props.content.seoDescription || ''
    })
    
    // 填充文件列表
    if (props.content.type === 'image') {
      imageFileList.value = props.content.images || []
    } else if (props.content.type === 'video') {
      videoFileList.value = props.content.videos || []
      thumbnailFileList.value = props.content.thumbnails || []
    } else if (props.content.type === 'carousel') {
      carouselFileList.value = props.content.carouselImages || []
    }
  } else if (!newVal) {
    // 关闭时重置表单
    resetForm()
  }
})
</script>

<style scoped>
.content-form-container {
  max-height: 70vh;
  overflow-y: auto;
  padding: 0 8px;
}

.form-section {
  margin-bottom: 20px;
}

.section-title {
  font-weight: 600;
  color: #303133;
}

.content-form {
  padding: 0;
}

.platform-config {
  background-color: #f5f7fa;
  padding: 16px;
  border-radius: 6px;
  margin-top: 16px;
}

.platform-config h4 {
  margin: 0 0 16px 0;
  color: #409EFF;
  font-size: 14px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.el-upload__tip {
  color: #909399;
  font-size: 12px;
  line-height: 1.4;
  margin-top: 8px;
}

:deep(.el-upload--picture-card) {
  --el-upload-picture-card-size: 100px;
}

:deep(.el-upload-list--picture-card .el-upload-list__item) {
  --el-upload-picture-card-size: 100px;
}
</style>