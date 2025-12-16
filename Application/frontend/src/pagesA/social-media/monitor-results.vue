<template>
  <base-layout>
    <view class="p-2">
      <!-- 页面标题 -->
      <view class="flex items-center justify-between mb-4">
        <view class="flex items-center gap-2">
          <button 
            class="bg-gray-200 text-gray-700 rounded-full px-3 py-1 text-sm"
            @click="goBack">
            <uni-icons type="arrow-left" size="16" />
            返回
          </button>
          <text class="text-xl font-bold text-primary-700">监控结果</text>
        </view>
        <text class="text-sm text-gray-500">{{ monitorName }}</text>
      </view>

      <!-- 统计概览 -->
      <view class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-4">
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">总发现数</text>
              <text class="block text-2xl font-bold text-gray-900">{{ stats.totalFound }}</text>
            </view>
            <view class="bg-blue-100 rounded-full p-3">
              <uni-icons type="compose" size="24" color="#3b82f6" />
            </view>
          </view>
        </view>
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">商机数</text>
              <text class="block text-2xl font-bold text-gray-900">{{ stats.opportunityCount }}</text>
            </view>
            <view class="bg-green-100 rounded-full p-3">
              <uni-icons type="star" size="24" color="#10b981" />
            </view>
          </view>
        </view>
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">合规率</text>
              <text class="block text-2xl font-bold text-gray-900">{{ stats.complianceRate }}%</text>
            </view>
            <view class="bg-purple-100 rounded-full p-3">
              <uni-icons type="shield-checkmark" size="24" color="#8b5cf6" />
            </view>
          </view>
        </view>
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">转化率</text>
              <text class="block text-2xl font-bold text-gray-900">{{ stats.conversionRate }}%</text>
            </view>
            <view class="bg-yellow-100 rounded-full p-3">
              <uni-icons type="trending-up" size="24" color="#f59e0b" />
            </view>
          </view>
        </view>
      </view>

      <!-- 筛选条件 -->
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <view class="flex flex-wrap gap-4 items-center">
          <view class="flex items-center gap-2">
            <text class="text-sm font-medium text-gray-700">平台:</text>
            <picker 
              :value="filter.platformIndex" 
              :range="platformOptions"
              @change="onPlatformFilterChange">
              <view class="bg-gray-50 rounded-lg px-3 py-1 text-sm">
                {{ platformOptions[filter.platformIndex] }}
              </view>
            </picker>
          </view>
          <view class="flex items-center gap-2">
            <text class="text-sm font-medium text-gray-700">商机:</text>
            <picker 
              :value="filter.opportunityIndex" 
              :range="opportunityOptions"
              @change="onOpportunityFilterChange">
              <view class="bg-gray-50 rounded-lg px-3 py-1 text-sm">
                {{ opportunityOptions[filter.opportunityIndex] }}
              </view>
            </picker>
          </view>
          <view class="flex items-center gap-2">
            <text class="text-sm font-medium text-gray-700">合规:</text>
            <picker 
              :value="filter.complianceIndex" 
              :range="complianceOptions"
              @change="onComplianceFilterChange">
              <view class="bg-gray-50 rounded-lg px-3 py-1 text-sm">
                {{ complianceOptions[filter.complianceIndex] }}
              </view>
            </picker>
          </view>
          <button 
            class="bg-primary-500 text-white rounded-full px-4 py-1 text-sm"
            @click="searchResults">
            搜索
          </button>
          <button 
            class="bg-gray-200 text-gray-700 rounded-full px-4 py-1 text-sm"
            @click="resetFilters">
            重置
          </button>
        </view>
      </view>

      <!-- 监控结果列表 -->
      <view class="bg-white rounded-xl shadow-lg">
        <view class="p-4 border-b border-gray-200 flex items-center justify-between">
          <text class="text-lg font-semibold text-gray-900">监控结果</text>
          <view class="flex items-center gap-2">
            <button 
              class="bg-green-500 text-white rounded-full px-3 py-1 text-xs"
              @click="batchExtractOpportunity"
              :loading="batchLoading">
              批量提取商机
            </button>
            <button 
              class="bg-primary-500 text-white rounded-full px-3 py-1 text-xs"
              @click="exportResults">
              导出结果
            </button>
          </view>
        </view>
        <view v-if="loading" class="p-8 text-center">
          <uni-icons type="spinner-cycle" size="48" color="#6b7280" class="animate-spin" />
          <text class="block text-gray-500 mt-2">加载中...</text>
        </view>
        <view v-else-if="resultList.length === 0" class="p-8 text-center">
          <uni-icons type="info" size="48" color="#9ca3af" />
          <text class="block text-gray-500 mt-2">暂无监控结果</text>
        </view>
        <view v-else>
          <view v-for="item in resultList" :key="item.id" class="p-4 border-b border-gray-100 hover:bg-gray-50">
            <view class="flex items-start justify-between">
              <view class="flex-1">
                <view class="flex items-center gap-2 mb-2">
                  <text class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full">
                    {{ item.platform }}
                  </text>
                  <text class="text-xs text-gray-500">{{ item.foundTime }}</text>
                  <text v-if="item.hasOpportunity" class="bg-green-100 text-green-800 text-xs px-2 py-1 rounded-full">
                    商机
                  </text>
                  <text v-if="item.complianceStatus === 'pass'" class="bg-green-100 text-green-800 text-xs px-2 py-1 rounded-full">
                    合规
                  </text>
                  <text v-else-if="item.complianceStatus === 'warning'" class="bg-yellow-100 text-yellow-800 text-xs px-2 py-1 rounded-full">
                    警告
                  </text>
                  <text v-else class="bg-red-100 text-red-800 text-xs px-2 py-1 rounded-full">
                    违规
                  </text>
                </view>
                <text class="block text-base font-medium text-gray-900 mb-1">{{ item.title }}</text>
                <text class="block text-sm text-gray-600 line-clamp-3 mb-2">{{ item.content }}</text>
                <view class="flex items-center gap-4 text-xs text-gray-500">
                  <text>👤 {{ item.author }}</text>
                  <text>👍 {{ item.likes }}</text>
                  <text>💬 {{ item.comments }}</text>
                  <text>🔗 {{ item.shares }}</text>
                  <text>价值分: {{ item.commercialScore }}</text>
                </view>
              </view>
              <view class="flex flex-col gap-2 ml-4">
                <button 
                  class="bg-primary-500 text-white rounded-full px-3 py-1 text-xs"
                  @click="viewDetail(item)">
                  查看详情
                </button>
                <button 
                  class="bg-green-500 text-white rounded-full px-3 py-1 text-xs"
                  @click="extractOpportunity(item)"
                  v-if="item.hasOpportunity">
                  提取商机
                </button>
                <button 
                  class="bg-orange-500 text-white rounded-full px-3 py-1 text-xs"
                  @click="checkCompliance(item)">
                  合规检查
                </button>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 分页 -->
      <view v-if="totalPages > 1" class="flex justify-center mt-4">
        <view class="flex items-center gap-2">
          <button 
            class="bg-gray-200 text-gray-700 rounded-full px-3 py-1 text-sm"
            @click="changePage(currentPage - 1)"
            :disabled="currentPage <= 1">
            上一页
          </button>
          <text class="text-sm text-gray-600">{{ currentPage }} / {{ totalPages }}</text>
          <button 
            class="bg-gray-200 text-gray-700 rounded-full px-3 py-1 text-sm"
            @click="changePage(currentPage + 1)"
            :disabled="currentPage >= totalPages">
            下一页
          </button>
        </view>
      </view>
    </view>

    <!-- 详情弹窗 -->
    <uni-popup ref="detailModal" type="center">
      <view class="bg-white p-6 w-96 rounded-xl shadow-xl max-h-96 overflow-y-auto">
        <view class="flex items-center justify-between mb-4">
          <text class="text-lg font-bold text-primary-700">内容详情</text>
          <button 
            class="text-gray-400 hover:text-gray-600"
            @click="closeDetailModal">
            <uni-icons type="close" size="20" />
          </button>
        </view>
        
        <view v-if="currentItem" class="space-y-3">
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">标题</text>
            <text class="block text-sm text-gray-900">{{ currentItem.title }}</text>
          </view>
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">内容</text>
            <text class="block text-sm text-gray-600">{{ currentItem.content }}</text>
          </view>
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">作者</text>
            <text class="block text-sm text-gray-900">{{ currentItem.author }}</text>
          </view>
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">平台</text>
            <text class="block text-sm text-gray-900">{{ currentItem.platform }}</text>
          </view>
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">发现时间</text>
            <text class="block text-sm text-gray-900">{{ currentItem.foundTime }}</text>
          </view>
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">互动数据</text>
            <view class="flex gap-4 text-sm text-gray-600">
              <text>👍 {{ currentItem.likes }}</text>
              <text>💬 {{ currentItem.comments }}</text>
              <text>🔗 {{ currentItem.shares }}</text>
            </view>
          </view>
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">商业价值</text>
            <text class="block text-sm text-gray-900">{{ currentItem.commercialScore }}分</text>
          </view>
        </view>
        
        <view class="flex gap-4 mt-6">
          <button 
            class="flex-1 bg-primary-500 text-white rounded-full py-2 text-sm"
            @click="extractOpportunity(currentItem)"
            v-if="currentItem.hasOpportunity">
            提取商机
          </button>
          <button 
            class="flex-1 bg-gray-200 text-gray-700 rounded-full py-2 text-sm"
            @click="closeDetailModal">
            关闭
          </button>
        </view>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { 
  getMonitoringResults,
  extractBusinessOpportunity,
  checkContentCompliance
} from '@/api/social-media/socialMedia.js';

const props = defineProps({
  id: String,
  name: String
});

const loading = ref(false);
const batchLoading = ref(false);

// 监控名称
const monitorName = ref(props.name || '监控任务');

// 统计
const stats = ref({
  totalFound: 0,
  opportunityCount: 0,
  complianceRate: 0,
  conversionRate: 0
});

// 筛选
const filter = reactive({
  platformIndex: 0,
  opportunityIndex: 0,
  complianceIndex: 0
});

const platformOptions = ['全部', 'douyin', 'wechat', 'xiaohongshu'];
const opportunityOptions = ['全部', '有商机', '无商机'];
const complianceOptions = ['全部', '合规', '警告', '违规'];

// 结果列表
const resultList = ref([]);
const currentPage = ref(1);
const totalPages = ref(1);

// 当前项
const currentItem = ref(null);

// 页面加载
onMounted(async () => {
  await loadMonitorResults();
});

// 返回
function goBack() {
  uni.navigateBack();
}

// 平台筛选
function onPlatformFilterChange(e) {
  filter.platformIndex = parseInt(e.detail.value);
}

// 商机筛选
function onOpportunityFilterChange(e) {
  filter.opportunityIndex = parseInt(e.detail.value);
}

// 合规筛选
function onComplianceFilterChange(e) {
  filter.complianceIndex = parseInt(e.detail.value);
}

// 搜索
async function searchResults() {
  currentPage.value = 1;
  await loadMonitorResults();
}

// 重置筛选
function resetFilters() {
  filter.platformIndex = 0;
  filter.opportunityIndex = 0;
  filter.complianceIndex = 0;
  searchResults();
}

// 加载监控结果
async function loadMonitorResults() {
  loading.value = true;
  try {
    // 模拟监控结果数据
    resultList.value = [
      {
        id: 1,
        title: '抖音视频：办公用品推荐',
        content: '这家公司的办公用品真的很不错，质量很好，价格也很实惠。推荐大家购买！',
        platform: 'douyin',
        author: '办公达人小李',
        foundTime: '2024-01-15 14:30',
        likes: 128,
        comments: 23,
        shares: 15,
        commercialScore: 85,
        hasOpportunity: true,
        complianceStatus: 'pass'
      },
      {
        id: 2,
        title: '微信公众号：企业IT设备采购指南',
        content: '企业在采购IT设备时需要注意以下几点：1. 性能要满足需求 2. 价格要合理 3. 售后服务要好...',
        platform: 'wechat',
        author: 'IT采购专家',
        foundTime: '2024-01-15 13:45',
        likes: 89,
        comments: 12,
        shares: 8,
        commercialScore: 78,
        hasOpportunity: true,
        complianceStatus: 'warning'
      },
      {
        id: 3,
        title: '小红书笔记：办公室装修心得',
        content: '分享一下我们公司的办公室装修经验，选择家具的时候要注意环保材料...',
        platform: 'xiaohongshu',
        author: '装修小白',
        foundTime: '2024-01-15 12:20',
        likes: 156,
        comments: 34,
        shares: 21,
        commercialScore: 72,
        hasOpportunity: false,
        complianceStatus: 'pass'
      }
    ];
    
    // 计算统计
    stats.value = {
      totalFound: resultList.value.length,
      opportunityCount: resultList.value.filter(item => item.hasOpportunity).length,
      complianceRate: Math.round((resultList.value.filter(item => item.complianceStatus === 'pass').length / resultList.value.length) * 100),
      conversionRate: Math.round((resultList.value.filter(item => item.hasOpportunity).length / resultList.value.length) * 100)
    };
    
    totalPages.value = 1;
  } catch (error) {
    console.error('加载监控结果失败:', error);
  } finally {
    loading.value = false;
  }
}

// 分页
function changePage(page) {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
    loadMonitorResults();
  }
}

// 批量提取商机
async function batchExtractOpportunity() {
  const opportunityItems = resultList.value.filter(item => item.hasOpportunity);
  if (opportunityItems.length === 0) {
    uni.showToast({
      title: '没有可提取的商机',
      icon: 'none'
    });
    return;
  }
  
  batchLoading.value = true;
  try {
    // 模拟批量提取
    await new Promise(resolve => setTimeout(resolve, 2000));
    
    uni.showToast({
      title: `成功提取 ${opportunityItems.length} 个商机`,
      icon: 'success'
    });
  } catch (error) {
    console.error('批量提取商机失败:', error);
    uni.showToast({
      title: '批量提取失败',
      icon: 'none'
    });
  } finally {
    batchLoading.value = false;
  }
}

// 导出结果
function exportResults() {
  uni.showToast({
    title: '导出功能开发中',
    icon: 'none'
  });
}

// 查看详情
function viewDetail(item) {
  currentItem.value = item;
  this.$refs.detailModal.open();
}

// 关闭详情弹窗
function closeDetailModal() {
  this.$refs.detailModal.close();
  currentItem.value = null;
}

// 提取商机
async function extractOpportunity(item) {
  try {
    const result = await extractBusinessOpportunity({
      contentId: item.id,
      platform: item.platform,
      content: item.content,
      author: item.author
    });
    
    if (result.success) {
      uni.showToast({
        title: '商机提取成功',
        icon: 'success'
      });
      
      // 更新状态
      item.hasOpportunity = false;
    }
  } catch (error) {
    console.error('提取商机失败:', error);
    uni.showToast({
      title: '提取商机失败',
      icon: 'none'
    });
  }
}

// 合规检查
async function checkCompliance(item) {
  try {
    const result = await checkContentCompliance({
      content: item.content,
      platform: item.platform,
      contentType: 'text'
    });
    
    if (result.success) {
      let message = '合规检查结果：';
      if (result.data.overallStatus === 'pass') {
        message += '合规';
      } else if (result.data.overallStatus === 'warning') {
        message += '警告';
      } else {
        message += '违规';
      }
      
      if (result.data.violations && result.data.violations.length > 0) {
        message += '\n违规项：' + result.data.violations.join(', ');
      }
      
      uni.showModal({
        title: '合规检查结果',
        content: message,
        showCancel: false
      });
    }
  } catch (error) {
    console.error('合规检查失败:', error);
    uni.showToast({
      title: '合规检查失败',
      icon: 'none'
    });
  }
}
</script>

<style scoped>
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>