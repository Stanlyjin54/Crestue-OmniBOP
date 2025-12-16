<template>
  <base-layout>
    <view class="p-2">
      <!-- 页面标题 -->
      <view class="flex items-center justify-between mb-4">
        <text class="text-xl font-bold text-primary-700">社交媒体监控</text>
        <button 
          class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-lg flex items-center gap-1"
          @click="showMonitorModal = true">
          <uni-icons type="plus" size="20" />
          新增监控
        </button>
      </view>

      <!-- 监控状态概览 -->
      <view class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-4">
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">活跃监控</text>
              <text class="block text-2xl font-bold text-gray-900">{{ monitorStats.activeCount }}</text>
            </view>
            <view class="bg-green-100 rounded-full p-3">
              <uni-icons type="eye" size="24" color="#10b981" />
            </view>
          </view>
        </view>
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">今日发现</text>
              <text class="block text-2xl font-bold text-gray-900">{{ monitorStats.todayFound }}</text>
            </view>
            <view class="bg-blue-100 rounded-full p-3">
              <uni-icons type="compose" size="24" color="#3b82f6" />
            </view>
          </view>
        </view>
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">商机转化</text>
              <text class="block text-2xl font-bold text-gray-900">{{ monitorStats.opportunityCount }}</text>
            </view>
            <view class="bg-yellow-100 rounded-full p-3">
              <uni-icons type="star" size="24" color="#f59e0b" />
            </view>
          </view>
        </view>
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">合规率</text>
              <text class="block text-2xl font-bold text-gray-900">{{ monitorStats.complianceRate }}%</text>
            </view>
            <view class="bg-purple-100 rounded-full p-3">
              <uni-icons type="shield-checkmark" size="24" color="#8b5cf6" />
            </view>
          </view>
        </view>
      </view>

      <!-- 监控任务列表 -->
      <view class="bg-white rounded-xl shadow-lg mb-4">
        <view class="p-4 border-b border-gray-200">
          <text class="text-lg font-semibold text-gray-900">监控任务</text>
        </view>
        <view v-if="monitorLoading" class="p-8 text-center">
          <uni-icons type="spinner-cycle" size="48" color="#6b7280" class="animate-spin" />
          <text class="block text-gray-500 mt-2">加载中...</text>
        </view>
        <view v-else-if="monitorList.length === 0" class="p-8 text-center">
          <uni-icons type="info" size="48" color="#9ca3af" />
          <text class="block text-gray-500 mt-2">暂无监控任务</text>
        </view>
        <view v-else>
          <view v-for="item in monitorList" :key="item.id" class="p-4 border-b border-gray-100 hover:bg-gray-50">
            <view class="flex items-center justify-between">
              <view class="flex-1">
                <view class="flex items-center gap-2 mb-2">
                  <text class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full">
                    {{ item.platform }}
                  </text>
                  <text class="text-xs text-gray-500">{{ item.createTime }}</text>
                </view>
                <text class="block text-base font-medium text-gray-900 mb-1">{{ item.name }}</text>
                <view class="flex flex-wrap gap-1 mb-2">
                  <text v-for="keyword in item.keywords" :key="keyword" 
                        class="bg-gray-100 text-gray-700 text-xs px-2 py-1 rounded-full">
                    {{ keyword }}
                  </text>
                </view>
                <view class="flex items-center gap-4 text-xs text-gray-500">
                  <text>监控周期: {{ item.monitoringPeriod }}小时</text>
                  <text>状态: 
                    <text :class="item.status === 'active' ? 'text-green-600' : 'text-red-600'">
                      {{ item.status === 'active' ? '运行中' : '已停止' }}
                    </text>
                  </text>
                  <text>发现内容: {{ item.foundCount }}</text>
                </view>
              </view>
              <view class="flex flex-col gap-2 ml-4">
                <button 
                  class="bg-primary-500 text-white rounded-full px-3 py-1 text-xs"
                  @click="viewMonitorResults(item)">
                  查看结果
                </button>
                <button 
                  class="bg-red-500 text-white rounded-full px-3 py-1 text-xs"
                  @click="stopMonitor(item)"
                  v-if="item.status === 'active'">
                  停止监控
                </button>
                <button 
                  class="bg-green-500 text-white rounded-full px-3 py-1 text-xs"
                  @click="startMonitor(item)"
                  v-else>
                  开始监控
                </button>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 实时监控面板 -->
      <view class="bg-white rounded-xl shadow-lg">
        <view class="p-4 border-b border-gray-200 flex items-center justify-between">
          <text class="text-lg font-semibold text-gray-900">实时监控</text>
          <view class="flex items-center gap-2">
            <view class="w-2 h-2 rounded-full" :class="realtimeMonitor.status === 'running' ? 'bg-green-500' : 'bg-gray-400'"></view>
            <text class="text-sm text-gray-600">
              {{ realtimeMonitor.status === 'running' ? '监控中' : '已停止' }}
            </text>
            <button 
              class="bg-primary-500 text-white rounded-full px-3 py-1 text-xs"
              @click="toggleRealtimeMonitor">
              {{ realtimeMonitor.status === 'running' ? '停止' : '开始' }}
            </button>
          </view>
        </view>
        <view class="p-4">
          <view v-if="realtimeMonitor.recentItems.length === 0" class="text-center text-gray-500 py-8">
            <uni-icons type="info" size="32" color="#9ca3af" />
            <text class="block mt-2">暂无实时监控数据</text>
          </view>
          <view v-else class="space-y-3">
            <view v-for="item in realtimeMonitor.recentItems" :key="item.id" 
                  class="p-3 bg-gray-50 rounded-lg hover:bg-gray-100">
              <view class="flex items-start justify-between">
                <view class="flex-1">
                  <view class="flex items-center gap-2 mb-1">
                    <text class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full">
                      {{ item.platform }}
                    </text>
                    <text class="text-xs text-gray-500">{{ item.foundTime }}</text>
                  </view>
                  <text class="block text-sm font-medium text-gray-900 mb-1">{{ item.title }}</text>
                  <text class="block text-xs text-gray-600 line-clamp-2">{{ item.content }}</text>
                  <view class="flex items-center gap-3 mt-2 text-xs text-gray-500">
                    <text>👤 {{ item.author }}</text>
                    <text>价值分: {{ item.commercialScore }}</text>
                    <text class="text-green-600" v-if="item.hasOpportunity">发现商机</text>
                  </view>
                </view>
                <view class="ml-3">
                  <button 
                    class="bg-primary-500 text-white rounded-full px-2 py-1 text-xs"
                    @click="extractOpportunity(item)">
                    提取
                  </button>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 新增监控弹窗 -->
    <uni-popup ref="monitorModal" type="center" v-model="showMonitorModal">
      <view class="bg-white p-6 w-96 rounded-xl shadow-xl">
        <text class="block text-lg font-bold text-primary-700 mb-4">新增监控任务</text>
        
        <view class="space-y-4">
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">监控名称</text>
            <uni-easyinput
              v-model="monitorForm.name"
              type="text"
              placeholder="请输入监控任务名称"
              clearable
            />
          </view>
          
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">监控平台</text>
            <picker 
              :value="monitorForm.platformIndex" 
              :range="platformOptions"
              @change="onMonitorPlatformChange">
              <view class="bg-gray-50 rounded-lg px-3 py-2 text-sm">
                {{ platformOptions[monitorForm.platformIndex] }}
              </view>
            </picker>
          </view>
          
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">关键词</text>
            <uni-easyinput
              v-model="monitorForm.keywordsText"
              type="textarea"
              placeholder="请输入关键词，多个关键词用逗号分隔"
              :rows="3"
            />
          </view>
          
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">监控周期（小时）</text>
            <slider 
              :value="monitorForm.monitoringPeriod" 
              @change="onMonitoringPeriodChange"
              min="1" 
              max="168" 
              step="1" 
              show-value />
          </view>
        </view>
        
        <view class="flex gap-4 mt-6">
          <button 
            class="flex-1 bg-primary-500 text-white rounded-full py-2 text-sm"
            @click="createMonitor"
            :loading="createMonitorLoading">
            创建监控
          </button>
          <button 
            class="flex-1 bg-gray-200 text-gray-700 rounded-full py-2 text-sm"
            @click="closeMonitorModal">
            取消
          </button>
        </view>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import { 
  setMonitoringConfig,
  getMonitoringStatus,
  monitorKeywordMentions,
  analyzeCompetitorPerformance
} from '@/api/social-media/socialMedia.js';

const monitorLoading = ref(false);
const createMonitorLoading = ref(false);
const showMonitorModal = ref(false);

// 监控统计
const monitorStats = ref({
  activeCount: 0,
  todayFound: 0,
  opportunityCount: 0,
  complianceRate: 0
});

// 监控列表
const monitorList = ref([]);

// 实时监控
const realtimeMonitor = ref({
  status: 'stopped',
  recentItems: [],
  interval: null
});

// 表单
const monitorForm = reactive({
  name: '',
  platformIndex: 0,
  keywordsText: '',
  monitoringPeriod: 24
});

const platformOptions = ['全部', 'douyin', 'wechat', 'xiaohongshu'];

// 页面加载
onMounted(async () => {
  await loadMonitorList();
  await loadMonitorStats();
});

// 页面卸载
onUnmounted(() => {
  stopRealtimeMonitor();
});

// 加载监控列表
async function loadMonitorList() {
  monitorLoading.value = true;
  try {
    // 模拟监控列表数据
    monitorList.value = [
      {
        id: 1,
        name: '办公用品监控',
        platform: 'douyin',
        keywords: ['办公用品', '文具', '办公设备'],
        monitoringPeriod: 24,
        status: 'active',
        foundCount: 156,
        createTime: '2024-01-15 10:30'
      },
      {
        id: 2,
        name: 'IT设备监控',
        platform: 'wechat',
        keywords: ['IT设备', '电脑', '服务器'],
        monitoringPeriod: 12,
        status: 'active',
        foundCount: 89,
        createTime: '2024-01-14 15:20'
      }
    ];
  } catch (error) {
    console.error('加载监控列表失败:', error);
  } finally {
    monitorLoading.value = false;
  }
}

// 加载监控统计
async function loadMonitorStats() {
  try {
    // 模拟统计数据
    monitorStats.value = {
      activeCount: monitorList.value.filter(item => item.status === 'active').length,
      todayFound: 45,
      opportunityCount: 12,
      complianceRate: 92
    };
  } catch (error) {
    console.error('加载监控统计失败:', error);
  }
}

// 平台选择
function onMonitorPlatformChange(e) {
  monitorForm.platformIndex = parseInt(e.detail.value);
}

// 监控周期变化
function onMonitoringPeriodChange(e) {
  monitorForm.monitoringPeriod = parseInt(e.detail.value);
}

// 创建监控
async function createMonitor() {
  if (!monitorForm.name || !monitorForm.keywordsText) {
    uni.showToast({
      title: '请填写完整信息',
      icon: 'none'
    });
    return;
  }
  
  createMonitorLoading.value = true;
  try {
    const keywords = monitorForm.keywordsText.split(',').map(k => k.trim()).filter(k => k);
    const platform = platformOptions[monitorForm.platformIndex];
    
    const config = {
      name: monitorForm.name,
      platform: platform === '全部' ? ['douyin', 'wechat', 'xiaohongshu'] : [platform],
      keywords: keywords,
      monitoringPeriod: monitorForm.monitoringPeriod,
      enabled: true
    };
    
    const result = await setMonitoringConfig(config);
    
    if (result.success) {
      uni.showToast({
        title: '监控任务创建成功',
        icon: 'success'
      });
      closeMonitorModal();
      await loadMonitorList();
      await loadMonitorStats();
    }
  } catch (error) {
    console.error('创建监控失败:', error);
    uni.showToast({
      title: '创建监控失败',
      icon: 'none'
    });
  } finally {
    createMonitorLoading.value = false;
  }
}

// 关闭监控弹窗
function closeMonitorModal() {
  showMonitorModal.value = false;
  monitorForm.name = '';
  monitorForm.platformIndex = 0;
  monitorForm.keywordsText = '';
  monitorForm.monitoringPeriod = 24;
}

// 查看监控结果
function viewMonitorResults(item) {
  uni.navigateTo({
    url: `/pagesA/social-media/monitor-results?id=${item.id}&name=${item.name}`
  });
}

// 停止监控
async function stopMonitor(item) {
  try {
    item.status = 'stopped';
    uni.showToast({
      title: '监控已停止',
      icon: 'success'
    });
    await loadMonitorStats();
  } catch (error) {
    console.error('停止监控失败:', error);
  }
}

// 开始监控
async function startMonitor(item) {
  try {
    item.status = 'active';
    uni.showToast({
      title: '监控已开始',
      icon: 'success'
    });
    await loadMonitorStats();
  } catch (error) {
    console.error('开始监控失败:', error);
  }
}

// 切换实时监控
function toggleRealtimeMonitor() {
  if (realtimeMonitor.value.status === 'running') {
    stopRealtimeMonitor();
  } else {
    startRealtimeMonitor();
  }
}

// 开始实时监控
function startRealtimeMonitor() {
  realtimeMonitor.value.status = 'running';
  
  // 模拟实时数据
  realtimeMonitor.value.interval = setInterval(() => {
    const mockData = {
      id: Date.now(),
      platform: ['douyin', 'wechat', 'xiaohongshu'][Math.floor(Math.random() * 3)],
      title: '发现新的' + ['办公用品', 'IT设备', '家具'][Math.floor(Math.random() * 3)] + '相关内容',
      content: '这是一条模拟的实时监控内容，包含了产品需求信息...',
      author: '用户' + Math.floor(Math.random() * 1000),
      foundTime: new Date().toLocaleTimeString(),
      commercialScore: Math.floor(Math.random() * 100),
      hasOpportunity: Math.random() > 0.7
    };
    
    realtimeMonitor.value.recentItems.unshift(mockData);
    
    // 保持最多10条记录
    if (realtimeMonitor.value.recentItems.length > 10) {
      realtimeMonitor.value.recentItems.pop();
    }
  }, 5000);
  
  uni.showToast({
    title: '实时监控已开启',
    icon: 'success'
  });
}

// 停止实时监控
function stopRealtimeMonitor() {
  realtimeMonitor.value.status = 'stopped';
  
  if (realtimeMonitor.value.interval) {
    clearInterval(realtimeMonitor.value.interval);
    realtimeMonitor.value.interval = null;
  }
  
  uni.showToast({
    title: '实时监控已停止',
    icon: 'success'
  });
}

// 提取商机
async function extractOpportunity(item) {
  try {
    // 这里可以调用提取商机的API
    uni.showToast({
      title: '商机提取成功',
      icon: 'success'
    });
  } catch (error) {
    console.error('提取商机失败:', error);
    uni.showToast({
      title: '提取商机失败',
      icon: 'none'
    });
  }
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