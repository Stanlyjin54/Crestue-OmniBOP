<template>
  <base-layout>
    <view class="p-2">
      <!-- 页面标题 -->
      <view class="flex items-center justify-between mb-4">
        <text class="text-xl font-bold text-primary-700">社交媒体商机</text>
        <view class="flex gap-2">
          <button 
            class="bg-success-500 text-white rounded-full px-4 py-2 text-sm shadow-lg flex items-center gap-1"
            @click="showComplianceModal = true">
            <uni-icons type="shield-checkmark" size="20" />
            合规检查
          </button>
          <button 
            class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-lg flex items-center gap-1"
            @click="showAnalysisModal = true">
            <uni-icons type="plus" size="20" />
            内容分析
          </button>
        </view>
      </view>

      <!-- 筛选条件 -->
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <view class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">平台</text>
            <picker 
              :value="platformIndex" 
              :range="platformOptions"
              @change="onPlatformChange">
              <view class="bg-gray-50 rounded-lg px-3 py-2 text-sm">
                {{ platformOptions[platformIndex] }}
              </view>
            </picker>
          </view>
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">内容类型</text>
            <picker 
              :value="contentTypeIndex" 
              :range="contentTypeOptions"
              @change="onContentTypeChange">
              <view class="bg-gray-50 rounded-lg px-3 py-2 text-sm">
                {{ contentTypeOptions[contentTypeIndex] }}
              </view>
            </picker>
          </view>
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">商业价值</text>
            <picker 
              :value="valueIndex" 
              :range="valueOptions"
              @change="onValueChange">
              <view class="bg-gray-50 rounded-lg px-3 py-2 text-sm">
                {{ valueOptions[valueIndex] }}
              </view>
            </picker>
          </view>
          <view class="flex items-end">
            <button 
              class="bg-primary-500 text-white rounded-lg px-4 py-2 text-sm flex-1"
              @click="searchOpportunities">
              搜索
            </button>
          </view>
        </view>
      </view>

      <!-- 统计卡片 -->
      <view class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-4">
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">总内容数</text>
              <text class="block text-2xl font-bold text-gray-900">{{ statistics.totalContent }}</text>
            </view>
            <view class="bg-blue-100 rounded-full p-3">
              <uni-icons type="compose" size="24" color="#3b82f6" />
            </view>
          </view>
        </view>
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">商机数量</text>
              <text class="block text-2xl font-bold text-gray-900">{{ statistics.opportunityCount }}</text>
            </view>
            <view class="bg-green-100 rounded-full p-3">
              <uni-icons type="star" size="24" color="#10b981" />
            </view>
          </view>
        </view>
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">平均价值分</text>
              <text class="block text-2xl font-bold text-gray-900">{{ statistics.avgScore }}</text>
            </view>
            <view class="bg-yellow-100 rounded-full p-3">
              <uni-icons type="chart-bar" size="24" color="#f59e0b" />
            </view>
          </view>
        </view>
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">合规率</text>
              <text class="block text-2xl font-bold text-gray-900">{{ statistics.complianceRate }}%</text>
            </view>
            <view class="bg-purple-100 rounded-full p-3">
              <uni-icons type="shield-checkmark" size="24" color="#8b5cf6" />
            </view>
          </view>
        </view>
      </view>

      <!-- 社交媒体内容列表 -->
      <view class="bg-white rounded-xl shadow-lg">
        <view class="p-4 border-b border-gray-200">
          <text class="text-lg font-semibold text-gray-900">内容列表</text>
        </view>
        <view v-if="loading" class="p-8 text-center">
          <uni-icons type="spinner-cycle" size="48" color="#6b7280" class="animate-spin" />
          <text class="block text-gray-500 mt-2">加载中...</text>
        </view>
        <view v-else-if="contentList.length === 0" class="p-8 text-center">
          <uni-icons type="info" size="48" color="#9ca3af" />
          <text class="block text-gray-500 mt-2">暂无社交媒体内容</text>
        </view>
        <view v-else>
          <view v-for="item in contentList" :key="item.id" class="p-4 border-b border-gray-100 hover:bg-gray-50">
            <view class="flex items-start justify-between">
              <view class="flex-1">
                <view class="flex items-center gap-2 mb-2">
                  <text class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full">
                    {{ item.platform }}
                  </text>
                  <text class="bg-gray-100 text-gray-800 text-xs px-2 py-1 rounded-full">
                    {{ item.contentType }}
                  </text>
                  <text class="text-xs text-gray-500">{{ item.publishTime }}</text>
                </view>
                <text class="block text-base font-medium text-gray-900 mb-1">{{ item.title }}</text>
                <text class="block text-sm text-gray-600 mb-2 line-clamp-2">{{ item.content }}</text>
                <view class="flex items-center gap-4 text-xs text-gray-500">
                  <text>👤 {{ item.author }}</text>
                  <text>👍 {{ item.likes }}</text>
                  <text>💬 {{ item.comments }}</text>
                  <text>🔄 {{ item.shares }}</text>
                </view>
              </view>
              <view class="ml-4">
                <view class="flex flex-col gap-2">
                  <view class="text-center">
                    <text class="block text-xs text-gray-500">价值分</text>
                    <text class="block text-lg font-bold" :class="getScoreColor(item.commercialScore)">
                      {{ item.commercialScore }}
                    </text>
                  </view>
                  <view class="text-center">
                    <text class="block text-xs text-gray-500">合规</text>
                    <view class="w-6 h-6 rounded-full flex items-center justify-center"
                          :class="item.complianceStatus ? 'bg-green-100' : 'bg-red-100'">
                      <uni-icons :type="item.complianceStatus ? 'checkmark' : 'close'" 
                                size="16" 
                                :color="item.complianceStatus ? '#10b981' : '#ef4444'" />
                    </view>
                  </view>
                  <button 
                    class="bg-primary-500 text-white rounded-full px-3 py-1 text-xs"
                    @click="extractOpportunity(item)">
                    提取商机
                  </button>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 内容分析弹窗 -->
    <uni-popup ref="analysisModal" type="center" v-model="showAnalysisModal">
      <view class="bg-white p-6 w-96 rounded-xl shadow-xl">
        <text class="block text-lg font-bold text-primary-700 mb-4">社交媒体内容分析</text>
        
        <view class="space-y-4">
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">平台</text>
            <picker 
              :value="analysisForm.platformIndex" 
              :range="platformOptions.slice(1)"
              @change="onAnalysisPlatformChange">
              <view class="bg-gray-50 rounded-lg px-3 py-2 text-sm">
                {{ platformOptions.slice(1)[analysisForm.platformIndex] }}
              </view>
            </picker>
          </view>
          
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">内容标题</text>
            <uni-easyinput
              v-model="analysisForm.title"
              type="text"
              placeholder="请输入内容标题"
              clearable
            />
          </view>
          
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">内容正文</text>
            <uni-easyinput
              v-model="analysisForm.content"
              type="textarea"
              placeholder="请输入内容正文"
              :maxlength="1000"
              :rows="4"
            />
          </view>
          
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">作者信息</text>
            <uni-easyinput
              v-model="analysisForm.author"
              type="text"
              placeholder="请输入作者信息"
              clearable
            />
          </view>
        </view>
        
        <view class="flex gap-4 mt-6">
          <button 
            class="flex-1 bg-primary-500 text-white rounded-full py-2 text-sm"
            @click="analyzeContent"
            :loading="analysisLoading">
            开始分析
          </button>
          <button 
            class="flex-1 bg-gray-200 text-gray-700 rounded-full py-2 text-sm"
            @click="closeAnalysisModal">
            取消
          </button>
        </view>
      </view>
    </uni-popup>

    <!-- 合规检查弹窗 -->
    <uni-popup ref="complianceModal" type="center" v-model="showComplianceModal">
      <view class="bg-white p-6 w-96 rounded-xl shadow-xl">
        <text class="block text-lg font-bold text-primary-700 mb-4">内容合规检查</text>
        
        <view class="space-y-4">
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">平台</text>
            <picker 
              :value="complianceForm.platformIndex" 
              :range="platformOptions.slice(1)"
              @change="onCompliancePlatformChange">
              <view class="bg-gray-50 rounded-lg px-3 py-2 text-sm">
                {{ platformOptions.slice(1)[complianceForm.platformIndex] }}
              </view>
            </picker>
          </view>
          
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">检查内容</text>
            <uni-easyinput
              v-model="complianceForm.content"
              type="textarea"
              placeholder="请输入需要检查的内容"
              :maxlength="2000"
              :rows="6"
            />
          </view>
        </view>
        
        <view class="flex gap-4 mt-6">
          <button 
            class="flex-1 bg-success-500 text-white rounded-full py-2 text-sm"
            @click="checkCompliance"
            :loading="complianceLoading">
            检查合规
          </button>
          <button 
            class="flex-1 bg-gray-200 text-gray-700 rounded-full py-2 text-sm"
            @click="closeComplianceModal">
            取消
          </button>
        </view>
        
        <!-- 检查结果 -->
        <view v-if="complianceResult" class="mt-4 p-4 bg-gray-50 rounded-lg">
          <view class="flex items-center justify-between mb-2">
            <text class="text-sm font-medium">合规状态</text>
            <text class="text-sm font-bold" :class="complianceResult.isCompliant ? 'text-green-600' : 'text-red-600'">
              {{ complianceResult.isCompliant ? '合规' : '不合规' }}
            </text>
          </view>
          <text class="block text-xs text-gray-600">合规分数: {{ complianceResult.complianceScore }}/100</text>
          <view v-if="complianceResult.violations && complianceResult.violations.length > 0" class="mt-2">
            <text class="block text-xs text-red-600 font-medium">违规项:</text>
            <text v-for="violation in complianceResult.violations" :key="violation" class="block text-xs text-red-500">
              • {{ violation }}
            </text>
          </view>
        </view>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { 
  getSocialMediaContents, 
  analyzeDouyinContent,
  analyzeWechatArticle,
  analyzeXiaohongshuNote,
  evaluateCommercialValue,
  checkContentCompliance
} from '@/api/social-media/socialMedia.js';
import { checkContentCompliance as checkCompliance } from '@/api/social-media/compliance.js';

const loading = ref(false);
const contentList = ref([]);

// 筛选条件
const platformIndex = ref(0);
const contentTypeIndex = ref(0);
const valueIndex = ref(0);

const platformOptions = ['全部', 'douyin', 'wechat', 'xiaohongshu'];
const contentTypeOptions = ['全部', 'video', 'article', 'note'];
const valueOptions = ['全部', '高价值(70+)', '中价值(40-69)', '低价值(0-39)'];

// 统计数据
const statistics = ref({
  totalContent: 0,
  opportunityCount: 0,
  avgScore: 0,
  complianceRate: 0
});

// 分析弹窗
const showAnalysisModal = ref(false);
const analysisLoading = ref(false);
const analysisForm = reactive({
  platformIndex: 0,
  title: '',
  content: '',
  author: ''
});

// 合规检查弹窗
const showComplianceModal = ref(false);
const complianceLoading = ref(false);
const complianceResult = ref(null);
const complianceForm = reactive({
  platformIndex: 0,
  content: ''
});

// 页面加载
onMounted(async () => {
  await loadSocialMediaContents();
  await loadStatistics();
});

// 加载社交媒体内容
async function loadSocialMediaContents() {
  loading.value = true;
  try {
    const params = {
      limit: 50,
      processed: true
    };
    
    if (platformIndex.value > 0) {
      params.platform = platformOptions[platformIndex.value];
    }
    
    if (contentTypeIndex.value > 0) {
      params.contentType = contentTypeOptions[contentTypeIndex.value];
    }
    
    if (valueIndex.value > 0) {
      switch (valueIndex.value) {
        case 1: // 高价值
          params.onlyHighValue = true;
          break;
        case 2: // 中价值
          params.minScore = 40;
          params.maxScore = 69;
          break;
        case 3: // 低价值
          params.maxScore = 39;
          break;
      }
    }
    
    const res = await getSocialMediaContents(params);
    if (res.success) {
      contentList.value = res.data.map(item => ({
        ...item,
        complianceStatus: item.complianceScore >= 80
      }));
    }
  } catch (error) {
    console.error('加载社交媒体内容失败:', error);
  } finally {
    loading.value = false;
  }
}

// 加载统计数据
async function loadStatistics() {
  try {
    // 这里可以调用相应的统计API
    // 暂时使用模拟数据
    statistics.value = {
      totalContent: contentList.value.length,
      opportunityCount: contentList.value.filter(item => item.commercialScore >= 70).length,
      avgScore: Math.round(contentList.value.reduce((sum, item) => sum + item.commercialScore, 0) / contentList.value.length),
      complianceRate: Math.round((contentList.value.filter(item => item.complianceStatus).length / contentList.value.length) * 100)
    };
  } catch (error) {
    console.error('加载统计数据失败:', error);
  }
}

// 筛选条件变化
function onPlatformChange(e) {
  platformIndex.value = parseInt(e.detail.value);
}

function onContentTypeChange(e) {
  contentTypeIndex.value = parseInt(e.detail.value);
}

function onValueChange(e) {
  valueIndex.value = parseInt(e.detail.value);
}

// 搜索商机
async function searchOpportunities() {
  await loadSocialMediaContents();
  await loadStatistics();
}

// 获取分数颜色
function getScoreColor(score) {
  if (score >= 70) return 'text-green-600';
  if (score >= 40) return 'text-yellow-600';
  return 'text-red-600';
}

// 提取商机
async function extractOpportunity(item) {
  try {
    let result;
    switch (item.platform) {
      case 'douyin':
        result = await analyzeDouyinContent({
          videoUrl: item.originalUrl,
          videoTitle: item.title,
          videoDescription: item.content,
          authorInfo: { name: item.author }
        });
        break;
      case 'wechat':
        result = await analyzeWechatArticle({
          articleUrl: item.originalUrl,
          articleTitle: item.title,
          articleContent: item.content,
          authorInfo: { name: item.author }
        });
        break;
      case 'xiaohongshu':
        result = await analyzeXiaohongshuNote({
          noteUrl: item.originalUrl,
          noteTitle: item.title,
          noteContent: item.content,
          authorInfo: { name: item.author }
        });
        break;
    }
    
    if (result.success && result.data.length > 0) {
      uni.showToast({
        title: `成功提取${result.data.length}个商机`,
        icon: 'success'
      });
    } else {
      uni.showToast({
        title: '未提取到商机',
        icon: 'none'
      });
    }
  } catch (error) {
    console.error('提取商机失败:', error);
    uni.showToast({
      title: '提取商机失败',
      icon: 'none'
    });
  }
}

// 内容分析
function onAnalysisPlatformChange(e) {
  analysisForm.platformIndex = parseInt(e.detail.value);
}

async function analyzeContent() {
  if (!analysisForm.title || !analysisForm.content) {
    uni.showToast({
      title: '请填写完整信息',
      icon: 'none'
    });
    return;
  }
  
  analysisLoading.value = true;
  try {
    const platform = platformOptions.slice(1)[analysisForm.platformIndex];
    let result;
    
    const data = {
      title: analysisForm.title,
      content: analysisForm.content,
      authorInfo: { name: analysisForm.author || '未知作者' }
    };
    
    switch (platform) {
      case 'douyin':
        result = await analyzeDouyinContent({ ...data, videoUrl: '', videoTitle: data.title, videoDescription: data.content });
        break;
      case 'wechat':
        result = await analyzeWechatArticle({ ...data, articleUrl: '', articleTitle: data.title, articleContent: data.content });
        break;
      case 'xiaohongshu':
        result = await analyzeXiaohongshuNote({ ...data, noteUrl: '', noteTitle: data.title, noteContent: data.content });
        break;
    }
    
    if (result.success) {
      // 评估商业价值
      const commercialResult = await evaluateCommercialValue({
        title: analysisForm.title,
        content: analysisForm.content,
        platform: platform
      });
      
      uni.showToast({
        title: `分析完成，价值分：${commercialResult.data.commercialScore || 0}`,
        icon: 'success'
      });
      
      closeAnalysisModal();
      await loadSocialMediaContents();
    }
  } catch (error) {
    console.error('内容分析失败:', error);
    uni.showToast({
      title: '分析失败',
      icon: 'none'
    });
  } finally {
    analysisLoading.value = false;
  }
}

function closeAnalysisModal() {
  showAnalysisModal.value = false;
  analysisForm.title = '';
  analysisForm.content = '';
  analysisForm.author = '';
  analysisForm.platformIndex = 0;
}

// 合规检查
function onCompliancePlatformChange(e) {
  complianceForm.platformIndex = parseInt(e.detail.value);
}

async function checkCompliance() {
  if (!complianceForm.content) {
    uni.showToast({
      title: '请输入检查内容',
      icon: 'none'
    });
    return;
  }
  
  complianceLoading.value = true;
  try {
    const platform = platformOptions.slice(1)[complianceForm.platformIndex];
    const result = await checkContentCompliance(complianceForm.content, platform);
    
    if (result.success) {
      complianceResult.value = result.data;
    }
  } catch (error) {
    console.error('合规检查失败:', error);
    uni.showToast({
      title: '检查失败',
      icon: 'none'
    });
  } finally {
    complianceLoading.value = false;
  }
}

function closeComplianceModal() {
  showComplianceModal.value = false;
  complianceForm.content = '';
  complianceForm.platformIndex = 0;
  complianceResult.value = null;
}
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>