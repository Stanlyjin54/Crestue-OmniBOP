<template>
  <base-layout>
    <view class="p-2">
      <!-- 页面标题 -->
      <view class="flex items-center justify-between mb-4">
        <text class="text-xl font-bold text-primary-700">社交媒体商机</text>
        <view class="flex gap-2">
          <button 
            class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-lg"
            @click="navigateTo('opportunity')">
            商机管理
          </button>
          <button 
            class="bg-green-500 text-white rounded-full px-4 py-2 text-sm shadow-lg"
            @click="navigateTo('monitor')">
            实时监控
          </button>
          <button 
            class="bg-purple-500 text-white rounded-full px-4 py-2 text-sm shadow-lg"
            @click="navigateTo('compliance')">
            合规检查
          </button>
        </view>
      </view>

      <!-- 快速统计 -->
      <view class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-4">
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">今日商机</text>
              <text class="block text-2xl font-bold text-gray-900">{{ stats.todayOpportunities }}</text>
            </view>
            <view class="bg-green-100 rounded-full p-3">
              <uni-icons type="star" size="24" color="#10b981" />
            </view>
          </view>
        </view>
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">活跃监控</text>
              <text class="block text-2xl font-bold text-gray-900">{{ stats.activeMonitors }}</text>
            </view>
            <view class="bg-blue-100 rounded-full p-3">
              <uni-icons type="eye" size="24" color="#3b82f6" />
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

      <!-- 快速操作 -->
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <text class="block text-lg font-semibold text-gray-900 mb-4">快速操作</text>
        <view class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <button 
            class="bg-gradient-to-r from-blue-500 to-blue-600 text-white rounded-xl p-4 text-center hover:from-blue-600 hover:to-blue-700 transition-all"
            @click="quickAnalyze('douyin')">
            <uni-icons type="videocam" size="32" class="block mb-2" />
            <text class="block text-sm font-medium">抖音内容分析</text>
          </button>
          <button 
            class="bg-gradient-to-r from-green-500 to-green-600 text-white rounded-xl p-4 text-center hover:from-green-600 hover:to-green-700 transition-all"
            @click="quickAnalyze('wechat')">
            <uni-icons type="chat" size="32" class="block mb-2" />
            <text class="block text-sm font-medium">微信文章分析</text>
          </button>
          <button 
            class="bg-gradient-to-r from-red-500 to-red-600 text-white rounded-xl p-4 text-center hover:from-red-600 hover:to-red-700 transition-all"
            @click="quickAnalyze('xiaohongshu')">
            <uni-icons type="image" size="32" class="block mb-2" />
            <text class="block text-sm font-medium">小红书笔记分析</text>
          </button>
          <button 
            class="bg-gradient-to-r from-purple-500 to-purple-600 text-white rounded-xl p-4 text-center hover:from-purple-600 hover:to-purple-700 transition-all"
            @click="quickCheck">
            <uni-icons type="checkmark" size="32" class="block mb-2" />
            <text class="block text-sm font-medium">快速合规检查</text>
          </button>
        </view>
      </view>

      <!-- 最近商机 -->
      <view class="bg-white rounded-xl shadow-lg mb-4">
        <view class="p-4 border-b border-gray-200 flex items-center justify-between">
          <text class="text-lg font-semibold text-gray-900">最近商机</text>
          <button 
            class="text-primary-500 text-sm"
            @click="navigateTo('opportunity')">
            查看全部
          </button>
        </view>
        <view v-if="opportunityLoading" class="p-8 text-center">
          <uni-icons type="spinner-cycle" size="48" color="#6b7280" class="animate-spin" />
          <text class="block text-gray-500 mt-2">加载中...</text>
        </view>
        <view v-else-if="recentOpportunities.length === 0" class="p-8 text-center">
          <uni-icons type="info" size="48" color="#9ca3af" />
          <text class="block text-gray-500 mt-2">暂无商机</text>
        </view>
        <view v-else class="divide-y divide-gray-100">
          <view v-for="item in recentOpportunities" :key="item.id" 
                class="p-4 hover:bg-gray-50">
            <view class="flex items-start justify-between">
              <view class="flex-1">
                <view class="flex items-center gap-2 mb-2">
                  <text class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full">
                    {{ item.platform }}
                  </text>
                  <text class="text-xs text-gray-500">{{ item.foundTime }}</text>
                  <text class="bg-green-100 text-green-800 text-xs px-2 py-1 rounded-full">
                    {{ item.commercialScore }}分
                  </text>
                </view>
                <text class="block text-base font-medium text-gray-900 mb-1">{{ item.title }}</text>
                <text class="block text-sm text-gray-600 line-clamp-2 mb-2">{{ item.content }}</text>
                <view class="flex items-center gap-4 text-xs text-gray-500">
                  <text>👤 {{ item.author }}</text>
                  <text>👍 {{ item.likes }}</text>
                  <text>💬 {{ item.comments }}</text>
                  <text>🔗 {{ item.shares }}</text>
                </view>
              </view>
              <view class="flex flex-col gap-2 ml-4">
                <button 
                  class="bg-primary-500 text-white rounded-full px-3 py-1 text-xs"
                  @click="viewOpportunityDetail(item)">
                  详情
                </button>
                <button 
                  class="bg-green-500 text-white rounded-full px-3 py-1 text-xs"
                  @click="extractOpportunity(item)">
                  提取
                </button>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 监控状态 -->
      <view class="bg-white rounded-xl shadow-lg">
        <view class="p-4 border-b border-gray-200 flex items-center justify-between">
          <text class="text-lg font-semibold text-gray-900">监控状态</text>
          <button 
            class="text-primary-500 text-sm"
            @click="navigateTo('monitor')">
            管理监控
          </button>
        </view>
        <view class="p-4">
          <view v-if="monitorLoading" class="text-center text-gray-500 py-8">
            <uni-icons type="spinner-cycle" size="32" color="#6b7280" class="animate-spin" />
            <text class="block mt-2">加载中...</text>
          </view>
          <view v-else-if="monitorStatus.length === 0" class="text-center text-gray-500 py-8">
            <uni-icons type="info" size="32" color="#9ca3af" />
            <text class="block mt-2">暂无监控任务</text>
          </view>
          <view v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <view v-for="item in monitorStatus" :key="item.id" 
                  class="border border-gray-200 rounded-lg p-3">
              <view class="flex items-center justify-between mb-2">
                <text class="text-sm font-medium text-gray-900">{{ item.name }}</text>
                <view class="w-2 h-2 rounded-full" 
                      :class="item.status === 'active' ? 'bg-green-500' : 'bg-gray-400'"></view>
              </view>
              <view class="text-xs text-gray-600 mb-2">
                <text class="block">平台: {{ item.platform }}</text>
                <text class="block">发现: {{ item.foundCount }} 条</text>
                <text class="block">状态: {{ item.status === 'active' ? '运行中' : '已停止' }}</text>
              </view>
              <view class="flex gap-2">
                <button 
                  class="bg-primary-500 text-white rounded-full px-2 py-1 text-xs flex-1"
                  @click="viewMonitorDetail(item)">
                  查看
                </button>
                <button 
                  class="bg-red-500 text-white rounded-full px-2 py-1 text-xs"
                  @click="stopMonitor(item)"
                  v-if="item.status === 'active'">
                  停止
                </button>
                <button 
                  class="bg-green-500 text-white rounded-full px-2 py-1 text-xs"
                  @click="startMonitor(item)"
                  v-else>
                  开始
                </button>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { 
  getOpportunityStats,
  getRecentOpportunities,
  getMonitorStatus,
  extractBusinessOpportunity
} from '@/api/social-media/socialMedia.js';

// 统计数据
const stats = ref({
  todayOpportunities: 0,
  activeMonitors: 0,
  complianceRate: 0,
  conversionRate: 0
});

// 最近商机
const recentOpportunities = ref([]);
const opportunityLoading = ref(false);

// 监控状态
const monitorStatus = ref([]);
const monitorLoading = ref(false);

// 页面加载
onMounted(async () => {
  await loadDashboardData();
});

// 加载仪表板数据
async function loadDashboardData() {
  try {
    // 加载统计数据
    await loadStats();
    
    // 加载最近商机
    await loadRecentOpportunities();
    
    // 加载监控状态
    await loadMonitorStatus();
  } catch (error) {
    console.error('加载仪表板数据失败:', error);
  }
}

// 加载统计数据
async function loadStats() {
  try {
    // 模拟统计数据
    stats.value = {
      todayOpportunities: 23,
      activeMonitors: 8,
      complianceRate: 92,
      conversionRate: 68
    };
  } catch (error) {
    console.error('加载统计数据失败:', error);
  }
}

// 加载最近商机
async function loadRecentOpportunities() {
  opportunityLoading.value = true;
  try {
    // 模拟最近商机数据
    recentOpportunities.value = [
      {
        id: 1,
        title: '抖音视频：办公用品推荐',
        content: '这家公司的办公用品真的很不错，质量很好，价格也很实惠。推荐大家购买！',
        platform: 'douyin',
        author: '办公达人小李',
        foundTime: '2小时前',
        likes: 128,
        comments: 23,
        shares: 15,
        commercialScore: 85,
        hasOpportunity: true
      },
      {
        id: 2,
        title: '微信公众号：企业IT设备采购指南',
        content: '企业在采购IT设备时需要注意以下几点：1. 性能要满足需求 2. 价格要合理 3. 售后服务要好...',
        platform: 'wechat',
        author: 'IT采购专家',
        foundTime: '3小时前',
        likes: 89,
        comments: 12,
        shares: 8,
        commercialScore: 78,
        hasOpportunity: true
      },
      {
        id: 3,
        title: '小红书笔记：办公室装修心得',
        content: '分享一下我们公司的办公室装修经验，选择家具的时候要注意环保材料...',
        platform: 'xiaohongshu',
        author: '装修小白',
        foundTime: '5小时前',
        likes: 156,
        comments: 34,
        shares: 21,
        commercialScore: 72,
        hasOpportunity: false
      }
    ];
  } catch (error) {
    console.error('加载最近商机失败:', error);
  } finally {
    opportunityLoading.value = false;
  }
}

// 加载监控状态
async function loadMonitorStatus() {
  monitorLoading.value = true;
  try {
    // 模拟监控状态数据
    monitorStatus.value = [
      {
        id: 1,
        name: '办公用品监控',
        platform: 'douyin',
        status: 'active',
        foundCount: 156,
        createTime: '2024-01-15 10:30'
      },
      {
        id: 2,
        name: 'IT设备监控',
        platform: 'wechat',
        status: 'active',
        foundCount: 89,
        createTime: '2024-01-14 15:20'
      },
      {
        id: 3,
        name: '家具监控',
        platform: 'xiaohongshu',
        status: 'stopped',
        foundCount: 234,
        createTime: '2024-01-13 09:15'
      }
    ];
  } catch (error) {
    console.error('加载监控状态失败:', error);
  } finally {
    monitorLoading.value = false;
  }
}

// 页面导航
function navigateTo(page) {
  const pages = {
    'opportunity': '/pagesA/social-media/opportunity',
    'monitor': '/pagesA/social-media/monitor',
    'compliance': '/pagesA/social-media/compliance'
  };
  
  if (pages[page]) {
    uni.navigateTo({
      url: pages[page]
    });
  }
}

// 快速分析
function quickAnalyze(platform) {
  const platformNames = {
    'douyin': '抖音',
    'wechat': '微信',
    'xiaohongshu': '小红书'
  };
  
  uni.showModal({
    title: `${platformNames[platform]}内容分析`,
    content: '请输入要分析的链接或内容',
    editable: true,
    success: async (res) => {
      if (res.confirm && res.content) {
        try {
          // 这里可以调用具体的分析API
          uni.showToast({
            title: '分析中...',
            icon: 'loading'
          });
          
          // 模拟分析过程
          setTimeout(() => {
            uni.showToast({
              title: '分析完成',
              icon: 'success'
            });
            
            // 跳转到商机页面显示结果
            uni.navigateTo({
              url: `/pagesA/social-media/opportunity?platform=${platform}&content=${encodeURIComponent(res.content)}`
            });
          }, 2000);
        } catch (error) {
          console.error('分析失败:', error);
          uni.showToast({
            title: '分析失败',
            icon: 'none'
          });
        }
      }
    }
  });
}

// 快速合规检查
function quickCheck() {
  uni.showModal({
    title: '快速合规检查',
    content: '请输入要检查的内容',
    editable: true,
    success: async (res) => {
      if (res.confirm && res.content) {
        uni.navigateTo({
          url: `/pagesA/social-media/compliance?content=${encodeURIComponent(res.content)}`
        });
      }
    }
  });
}

// 查看商机详情
function viewOpportunityDetail(item) {
  uni.showModal({
    title: '商机详情',
    content: `标题: ${item.title}\n平台: ${item.platform}\n作者: ${item.author}\n商业价值: ${item.commercialScore}分\n点赞: ${item.likes} 评论: ${item.comments} 分享: ${item.shares}`,
    showCancel: false
  });
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
      
      // 更新列表
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

// 查看监控详情
function viewMonitorDetail(item) {
  uni.navigateTo({
    url: `/pagesA/social-media/monitor-results?id=${item.id}&name=${item.name}`
  });
}

// 停止监控
function stopMonitor(item) {
  item.status = 'stopped';
  uni.showToast({
    title: '监控已停止',
    icon: 'success'
  });
}

// 开始监控
function startMonitor(item) {
  item.status = 'active';
  uni.showToast({
    title: '监控已开始',
    icon: 'success'
  });
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