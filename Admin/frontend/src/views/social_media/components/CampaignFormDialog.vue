<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? '编辑营销活动' : '创建营销活动'"
    width="800px"
    @closed="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="120px"
      class="campaign-form"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="活动名称" prop="name">
            <el-input
              v-model="form.name"
              placeholder="输入活动名称"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="活动类型" prop="type">
            <el-select
              v-model="form.type"
              placeholder="选择活动类型"
              style="width: 100%"
            >
              <el-option
                v-for="type in campaignTypes"
                :key="type.value"
                :label="type.label"
                :value="type.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="活动描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="输入活动描述"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="目标平台" prop="platforms">
        <el-checkbox-group v-model="form.platforms">
          <el-checkbox
            v-for="platform in platforms"
            :key="platform.id"
            :label="platform.id"
            class="platform-checkbox"
          >
            <div class="platform-option">
              <el-icon :style="{ color: platform.color }">
                <component :is="platform.icon" />
              </el-icon>
              <span>{{ platform.name }}</span>
            </div>
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="开始日期" prop="startDate">
            <el-date-picker
              v-model="form.startDate"
              type="datetime"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              placeholder="选择开始日期"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束日期" prop="endDate">
            <el-date-picker
              v-model="form.endDate"
              type="datetime"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              placeholder="选择结束日期"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算金额" prop="budget">
            <el-input-number
              v-model="form.budget"
              :min="0"
              :precision="2"
              :step="100"
              placeholder="输入预算金额"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算货币" prop="currency">
            <el-select
              v-model="form.currency"
              placeholder="选择货币"
              style="width: 100%"
            >
              <el-option label="人民币 (CNY)" value="CNY" />
              <el-option label="美元 (USD)" value="USD" />
              <el-option label="欧元 (EUR)" value="EUR" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="目标受众">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="年龄范围">
              <el-input
                v-model="form.targetAudience.ageRange"
                placeholder="18-35"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="性别">
              <el-select
                v-model="form.targetAudience.gender"
                placeholder="选择性别"
                style="width: 100%"
              >
                <el-option label="不限" value="all" />
                <el-option label="男性" value="male" />
                <el-option label="女性" value="female" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="地域">
              <el-select
                v-model="form.targetAudience.location"
                placeholder="选择地域"
                style="width: 100%"
              >
                <el-option label="全国" value="china" />
                <el-option label="华北" value="north_china" />
                <el-option label="华东" value="east_china" />
                <el-option label="华南" value="south_china" />
                <el-option label="海外" value="overseas" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form-item>

      <el-form-item label="兴趣标签">
        <el-select
          v-model="form.targetAudience.interests"
          multiple
          filterable
          placeholder="选择兴趣标签"
          style="width: 100%"
        >
          <el-option
            v-for="interest in interestOptions"
            :key="interest.value"
            :label="interest.label"
            :value="interest.value"
          />
        </el-select>
      </el-form-item>

      <el-divider>内容策略</el-divider>

      <el-form-item label="内容主题">
        <el-tag
          v-for="theme in form.contentStrategy.themes"
          :key="theme"
          closable
          @close="removeTheme(theme)"
          style="margin: 4px"
        >
          {{ theme }}
        </el-tag>
        <el-input
          v-if="themeInputVisible"
          ref="themeInputRef"
          v-model="themeInputValue"
          class="ml-1"
          size="small"
          @keyup.enter="handleThemeInputConfirm"
          @blur="handleThemeInputConfirm"
          style="width: 120px"
        />
        <el-button v-else class="ml-1" size="small" @click="showThemeInput">
          + 新增主题
        </el-button>
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="内容类型" prop="contentStrategy.contentType">
            <el-checkbox-group v-model="form.contentStrategy.contentType">
              <el-checkbox label="image">图片</el-checkbox>
              <el-checkbox label="video">视频</el-checkbox>
              <el-checkbox label="text">文本</el-checkbox>
              <el-checkbox label="carousel">轮播</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发布频次" prop="contentStrategy.frequency">
            <el-select
              v-model="form.contentStrategy.frequency"
              placeholder="选择发布频次"
              style="width: 100%"
            >
              <el-option label="每日" value="daily" />
              <el-option label="每周" value="weekly" />
              <el-option label="每月" value="monthly" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="关键信息">
        <el-tag
          v-for="keyword in form.contentStrategy.keywords"
          :key="keyword"
          closable
          @close="removeKeyword(keyword)"
          style="margin: 4px"
        >
          {{ keyword }}
        </el-tag>
        <el-input
          v-if="keywordInputVisible"
          ref="keywordInputRef"
          v-model="keywordInputValue"
          class="ml-1"
          size="small"
          @keyup.enter="handleKeywordInputConfirm"
          @blur="handleKeywordInputConfirm"
          style="width: 120px"
        />
        <el-button v-else class="ml-1" size="small" @click="showKeywordInput">
          + 新增关键词
        </el-button>
      </el-form-item>

      <el-divider>KPI 指标</el-divider>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="目标触达">
            <el-input-number
              v-model="form.kpis.targetReach"
              :min="0"
              :precision="0"
              placeholder="目标触达数"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="目标互动">
            <el-input-number
              v-model="form.kpis.targetEngagement"
              :min="0"
              :precision="0"
              placeholder="目标互动数"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="目标转化率">
            <el-input-number
              v-model="form.kpis.targetConversionRate"
              :min="0"
              :max="100"
              :precision="2"
              :step="0.1"
              placeholder="目标转化率(%)"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="目标点击率">
            <el-input-number
              v-model="form.kpis.targetCTR"
              :min="0"
              :max="100"
              :precision="2"
              :step="0.1"
              placeholder="目标点击率(%)"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="目标ROI">
            <el-input-number
              v-model="form.kpis.targetROI"
              :min="0"
              :precision="2"
              :step="0.1"
              placeholder="目标ROI"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">
          {{ isEdit ? '更新' : '创建' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'

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
const emit = defineEmits(['update:modelValue', 'updated'])

// 响应式数据
const formRef = ref()
const submitting = ref(false)
const themeInputVisible = ref(false)
const themeInputValue = ref('')
const themeInputRef = ref()
const keywordInputVisible = ref(false)
const keywordInputValue = ref('')
const keywordInputRef = ref()

// 表单数据
const form = ref({
  name: '',
  type: '',
  description: '',
  platforms: [],
  startDate: '',
  endDate: '',
  budget: 0,
  currency: 'CNY',
  targetAudience: {
    ageRange: '',
    gender: 'all',
    location: 'china',
    interests: []
  },
  contentStrategy: {
    themes: [],
    contentType: [],
    frequency: 'daily',
    keywords: []
  },
  kpis: {
    targetReach: 0,
    targetEngagement: 0,
    targetConversionRate: 0,
    targetCTR: 0,
    targetROI: 0
  }
})

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const isEdit = computed(() => !!props.campaign)

// 静态配置数据
const campaignTypes = [
  { value: 'awareness', label: '品牌认知' },
  { value: 'engagement', label: '用户互动' },
  { value: 'conversion', label: '转化推广' },
  { value: 'retention', label: '用户留存' },
  { value: 'viral', label: '病毒营销' }
]

const platforms = [
  { id: 'wechat', name: '微信', color: '#07C160', icon: 'ChatDotRound' },
  { id: 'weibo', name: '微博', color: '#E6162D', icon: 'Share' },
  { id: 'douyin', name: '抖音', color: '#FE2C55', icon: 'VideoPlay' },
  { id: 'xiaohongshu', name: '小红书', color: '#FF2442', icon: 'Picture' },
  { id: 'zhihu', name: '知乎', color: '#0066FF', icon: 'ChatDotSquare' },
  { id: 'bilibili', name: 'B站', color: '#00A1D6', icon: 'VideoCamera' }
]

const interestOptions = [
  { value: 'tech', label: '科技数码' },
  { value: 'fashion', label: '时尚美妆' },
  { value: 'food', label: '美食料理' },
  { value: 'travel', label: '旅游度假' },
  { value: 'fitness', label: '健身运动' },
  { value: 'education', label: '教育培训' },
  { value: 'finance', label: '金融理财' },
  { value: 'entertainment', label: '娱乐休闲' }
]

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入活动名称', trigger: 'blur' },
    { min: 2, max: 100, message: '活动名称长度为2-100个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择活动类型', trigger: 'change' }
  ],
  platforms: [
    { type: 'array', required: true, message: '请选择至少一个目标平台', trigger: 'change' }
  ],
  startDate: [
    { required: true, message: '请选择开始日期', trigger: 'change' }
  ],
  endDate: [
    { required: true, message: '请选择结束日期', trigger: 'change' }
  ]
}

// 方法
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    // 验证日期逻辑
    if (form.value.startDate && form.value.endDate && form.value.startDate >= form.value.endDate) {
      ElMessage.warning('结束日期必须晚于开始日期')
      return
    }

    submitting.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    if (isEdit.value) {
      ElMessage.success('活动更新成功')
    } else {
      ElMessage.success('活动创建成功')
    }
    
    emit('updated')
    dialogVisible.value = false
    
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    submitting.value = false
  }
}

const handleClose = () => {
  resetForm()
  themeInputVisible.value = false
  keywordInputVisible.value = false
}

const resetForm = () => {
  form.value = {
    name: '',
    type: '',
    description: '',
    platforms: [],
    startDate: '',
    endDate: '',
    budget: 0,
    currency: 'CNY',
    targetAudience: {
      ageRange: '',
      gender: 'all',
      location: 'china',
      interests: []
    },
    contentStrategy: {
      themes: [],
      contentType: [],
      frequency: 'daily',
      keywords: []
    },
    kpis: {
      targetReach: 0,
      targetEngagement: 0,
      targetConversionRate: 0,
      targetCTR: 0,
      targetROI: 0
    }
  }
}

// 主题管理
const removeTheme = (theme) => {
  const index = form.value.contentStrategy.themes.indexOf(theme)
  if (index > -1) {
    form.value.contentStrategy.themes.splice(index, 1)
  }
}

const showThemeInput = () => {
  themeInputVisible.value = true
  nextTick(() => {
    themeInputRef.value?.focus()
  })
}

const handleThemeInputConfirm = () => {
  if (themeInputValue.value) {
    form.value.contentStrategy.themes.push(themeInputValue.value)
  }
  themeInputVisible.value = false
  themeInputValue.value = ''
}

// 关键词管理
const removeKeyword = (keyword) => {
  const index = form.value.contentStrategy.keywords.indexOf(keyword)
  if (index > -1) {
    form.value.contentStrategy.keywords.splice(index, 1)
  }
}

const showKeywordInput = () => {
  keywordInputVisible.value = true
  nextTick(() => {
    keywordInputRef.value?.focus()
  })
}

const handleKeywordInputConfirm = () => {
  if (keywordInputValue.value) {
    form.value.contentStrategy.keywords.push(keywordInputValue.value)
  }
  keywordInputVisible.value = false
  keywordInputValue.value = ''
}

// 监听props变化，初始化表单数据
watch(() => props.campaign, (newCampaign) => {
  if (newCampaign) {
    form.value = {
      name: newCampaign.name || '',
      type: newCampaign.type || '',
      description: newCampaign.description || '',
      platforms: newCampaign.platforms || [],
      startDate: newCampaign.startDate || '',
      endDate: newCampaign.endDate || '',
      budget: newCampaign.budget || 0,
      currency: newCampaign.currency || 'CNY',
      targetAudience: {
        ageRange: newCampaign.targetAudience?.ageRange || '',
        gender: newCampaign.targetAudience?.gender || 'all',
        location: newCampaign.targetAudience?.location || 'china',
        interests: newCampaign.targetAudience?.interests || []
      },
      contentStrategy: {
        themes: newCampaign.contentStrategy?.themes || [],
        contentType: newCampaign.contentStrategy?.contentType || [],
        frequency: newCampaign.contentStrategy?.frequency || 'daily',
        keywords: newCampaign.contentStrategy?.keywords || []
      },
      kpis: {
        targetReach: newCampaign.kpis?.targetReach || 0,
        targetEngagement: newCampaign.kpis?.targetEngagement || 0,
        targetConversionRate: newCampaign.kpis?.targetConversionRate || 0,
        targetCTR: newCampaign.kpis?.targetCTR || 0,
        targetROI: newCampaign.kpis?.targetROI || 0
      }
    }
  }
}, { immediate: true })
</script>

<style scoped>
.campaign-form {
  max-height: 600px;
  overflow-y: auto;
}

.platform-checkbox {
  margin-right: 24px;
  margin-bottom: 12px;
}

.platform-option {
  display: flex;
  align-items: center;
  gap: 8px;
}

.dialog-footer {
  text-align: right;
}

.ml-1 {
  margin-left: 8px;
}

.el-divider {
  margin: 24px 0;
  border-color: #e4e7ed;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

:deep(.el-checkbox-group) {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

:deep(.el-tag) {
  margin-right: 8px;
}
</style>