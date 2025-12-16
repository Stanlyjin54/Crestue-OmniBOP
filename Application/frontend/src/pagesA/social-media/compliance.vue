<template>
  <base-layout>
    <view class="p-2">
      <!-- 页面标题 -->
      <view class="flex items-center justify-between mb-4">
        <text class="text-xl font-bold text-primary-700">合规检查</text>
        <button 
          class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-lg flex items-center gap-1"
          @click="showCheckModal = true">
          <uni-icons type="checkmark" size="20" />
          内容检查
        </button>
      </view>

      <!-- 合规状态概览 -->
      <view class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-4">
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">今日检查</text>
              <text class="block text-2xl font-bold text-gray-900">{{ complianceStats.todayChecked }}</text>
            </view>
            <view class="bg-blue-100 rounded-full p-3">
              <uni-icons type="eye" size="24" color="#3b82f6" />
            </view>
          </view>
        </view>
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">合规通过</text>
              <text class="block text-2xl font-bold text-gray-900">{{ complianceStats.passed }}</text>
            </view>
            <view class="bg-green-100 rounded-full p-3">
              <uni-icons type="checkmark" size="24" color="#10b981" />
            </view>
          </view>
        </view>
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">警告数量</text>
              <text class="block text-2xl font-bold text-gray-900">{{ complianceStats.warnings }}</text>
            </view>
            <view class="bg-yellow-100 rounded-full p-3">
              <uni-icons type="warning" size="24" color="#f59e0b" />
            </view>
          </view>
        </view>
        <view class="bg-white rounded-xl shadow-lg p-4">
          <view class="flex items-center justify-between">
            <view>
              <text class="block text-sm font-medium text-gray-500">违规数量</text>
              <text class="block text-2xl font-bold text-gray-900">{{ complianceStats.violations }}</text>
            </view>
            <view class="bg-red-100 rounded-full p-3">
              <uni-icons type="close" size="24" color="#ef4444" />
            </view>
          </view>
        </view>
      </view>

      <!-- 合规规则配置 -->
      <view class="bg-white rounded-xl shadow-lg mb-4">
        <view class="p-4 border-b border-gray-200 flex items-center justify-between">
          <text class="text-lg font-semibold text-gray-900">合规规则配置</text>
          <button 
            class="bg-primary-500 text-white rounded-full px-3 py-1 text-sm"
            @click="saveRuleConfig">
            保存配置
          </button>
        </view>
        <view class="p-4 space-y-4">
          <view>
            <view class="flex items-center justify-between mb-2">
              <text class="text-sm font-medium text-gray-700">敏感词检测</text>
              <switch 
                :checked="ruleConfig.sensitiveWordEnabled"
                @change="onSensitiveWordChange" />
            </view>
            <view v-if="ruleConfig.sensitiveWordEnabled" class="mt-2">
              <uni-easyinput
                v-model="ruleConfig.sensitiveWordsText"
                type="textarea"
                placeholder="请输入敏感词，多个词用逗号分隔"
                :rows="3"
                clearable
              />
            </view>
          </view>
          
          <view>
            <view class="flex items-center justify-between mb-2">
              <text class="text-sm font-medium text-gray-700">广告合规检查</text>
              <switch 
                :checked="ruleConfig.adComplianceEnabled"
                @change="onAdComplianceChange" />
            </view>
          </view>
          
          <view>
            <view class="flex items-center justify-between mb-2">
              <text class="text-sm font-medium text-gray-700">KOL合规检查</text>
              <switch 
                :checked="ruleConfig.kolComplianceEnabled"
                @change="onKolComplianceChange" />
            </view>
          </view>
          
          <view>
            <view class="flex items-center justify-between mb-2">
              <text class="text-sm font-medium text-gray-700">平台特定规则</text>
              <switch 
                :checked="ruleConfig.platformSpecificEnabled"
                @change="onPlatformSpecificChange" />
            </view>
          </view>
        </view>
      </view>

      <!-- 检查历史记录 -->
      <view class="bg-white rounded-xl shadow-lg">
        <view class="p-4 border-b border-gray-200 flex items-center justify-between">
          <text class="text-lg font-semibold text-gray-900">检查历史记录</text>
          <button 
            class="bg-red-500 text-white rounded-full px-3 py-1 text-sm"
            @click="clearHistory">
            清空历史
          </button>
        </view>
        <view v-if="historyLoading" class="p-8 text-center">
          <uni-icons type="spinner-cycle" size="48" color="#6b7280" class="animate-spin" />
          <text class="block text-gray-500 mt-2">加载中...</text>
        </view>
        <view v-else-if="historyList.length === 0" class="p-8 text-center">
          <uni-icons type="info" size="48" color="#9ca3af" />
          <text class="block text-gray-500 mt-2">暂无检查记录</text>
        </view>
        <view v-else>
          <view v-for="item in historyList" :key="item.id" class="p-4 border-b border-gray-100 hover:bg-gray-50">
            <view class="flex items-start justify-between">
              <view class="flex-1">
                <view class="flex items-center gap-2 mb-2">
                  <text class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full">
                    {{ item.platform }}
                  </text>
                  <text class="text-xs text-gray-500">{{ item.checkTime }}</text>
                  <text v-if="item.overallStatus === 'pass'" class="bg-green-100 text-green-800 text-xs px-2 py-1 rounded-full">
                    合规
                  </text>
                  <text v-else-if="item.overallStatus === 'warning'" class="bg-yellow-100 text-yellow-800 text-xs px-2 py-1 rounded-full">
                    警告
                  </text>
                  <text v-else class="bg-red-100 text-red-800 text-xs px-2 py-1 rounded-full">
                    违规
                  </text>
                </view>
                <text class="block text-sm text-gray-900 mb-1">{{ item.content }}</text>
                <view v-if="item.violations && item.violations.length > 0" class="mt-2">
                  <text class="block text-xs text-red-600 mb-1">违规项：</text>
                  <view class="flex flex-wrap gap-1">
                    <text v-for="violation in item.violations" :key="violation" 
                          class="bg-red-100 text-red-700 text-xs px-2 py-1 rounded-full">
                      {{ violation }}
                    </text>
                  </view>
                </view>
              </view>
              <view class="ml-4">
                <button 
                  class="bg-primary-500 text-white rounded-full px-2 py-1 text-xs"
                  @click="viewCheckDetail(item)">
                  详情
                </button>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 内容检查弹窗 -->
    <uni-popup ref="checkModal" type="center" v-model="showCheckModal">
      <view class="bg-white p-6 w-96 rounded-xl shadow-xl">
        <text class="block text-lg font-bold text-primary-700 mb-4">内容合规检查</text>
        
        <view class="space-y-4">
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">检查内容</text>
            <uni-easyinput
              v-model="checkForm.content"
              type="textarea"
              placeholder="请输入需要检查的内容"
              :rows="4"
              clearable
            />
          </view>
          
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">平台</text>
            <picker 
              :value="checkForm.platformIndex" 
              :range="platformOptions"
              @change="onCheckPlatformChange">
              <view class="bg-gray-50 rounded-lg px-3 py-2 text-sm">
                {{ platformOptions[checkForm.platformIndex] }}
              </view>
            </picker>
          </view>
          
          <view>
            <text class="block text-sm font-medium text-gray-700 mb-1">内容类型</text>
            <picker 
              :value="checkForm.contentTypeIndex" 
              :range="contentTypeOptions"
              @change="onContentTypeChange">
              <view class="bg-gray-50 rounded-lg px-3 py-2 text-sm">
                {{ contentTypeOptions[checkForm.contentTypeIndex] }}
              </view>
            </picker>
          </view>
        </view>
        
        <view class="flex gap-4 mt-6">
          <button 
            class="flex-1 bg-primary-500 text-white rounded-full py-2 text-sm"
            @click="performCheck"
            :loading="checkLoading">
            开始检查
          </button>
          <button 
            class="flex-1 bg-gray-200 text-gray-700 rounded-full py-2 text-sm"
            @click="closeCheckModal">
            取消
          </button>
        </view>
      </view>
    </uni-popup>

    <!-- 检查结果弹窗 -->
    <uni-popup ref="resultModal" type="center">
      <view class="bg-white p-6 w-96 rounded-xl shadow-xl">
        <view class="flex items-center justify-between mb-4">
          <text class="text-lg font-bold text-primary-700">检查结果</text>
          <button 
            class="text-gray-400 hover:text-gray-600"
            @click="closeResultModal">
            <uni-icons type="close" size="20" />
          </button>
        </view>
        
        <view v-if="checkResult" class="space-y-4">
          <view class="flex items-center justify-center">
            <view class="w-16 h-16 rounded-full flex items-center justify-center"
                 :class="checkResult.overallStatus === 'pass' ? 'bg-green-100' : 
                        checkResult.overallStatus === 'warning' ? 'bg-yellow-100' : 'bg-red-100'">
              <uni-icons 
                :type="checkResult.overallStatus === 'pass' ? 'checkmark' : 
                       checkResult.overallStatus === 'warning' ? 'warning' : 'close'"
                size="32"
                :color="checkResult.overallStatus === 'pass' ? '#10b981' : 
                       checkResult.overallStatus === 'warning' ? '#f59e0b' : '#ef4444'" />
            </view>
          </view>
          
          <view class="text-center">
            <text class="block text-lg font-bold text-gray-900">
              {{ checkResult.overallStatus === 'pass' ? '合规通过' : 
                 checkResult.overallStatus === 'warning' ? '存在警告' : '存在违规' }}
            </text>
            <text class="block text-sm text-gray-600 mt-1">
              总分: {{ checkResult.totalScore }}/100
            </text>
          </view>
          
          <view v-if="checkResult.violations && checkResult.violations.length > 0" class="space-y-2">
            <text class="block text-sm font-medium text-gray-700">违规详情:</text>
            <view v-for="violation in checkResult.violations" :key="violation.type" 
                  class="bg-red-50 p-3 rounded-lg">
              <text class="block text-sm font-medium text-red-800">{{ violation.type }}</text>
              <text class="block text-xs text-red-600 mt-1">{{ violation.description }}</text>
              <text v-if="violation.suggestion" class="block text-xs text-red-500 mt-1">
                建议: {{ violation.suggestion }}
              </text>
            </view>
          </view>
          
          <view v-if="checkResult.warnings && checkResult.warnings.length > 0" class="space-y-2">
            <text class="block text-sm font-medium text-gray-700">警告信息:</text>
            <view v-for="warning in checkResult.warnings" :key="warning.type" 
                  class="bg-yellow-50 p-3 rounded-lg">
              <text class="block text-sm font-medium text-yellow-800">{{ warning.type }}</text>
              <text class="block text-xs text-yellow-600 mt-1">{{ warning.description }}</text>
            </view>
          </view>
        </view>
        
        <view class="flex gap-4 mt-6">
          <button 
            class="flex-1 bg-primary-500 text-white rounded-full py-2 text-sm"
            @click="closeResultModal">
            确定
          </button>
        </view>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { 
  checkContentCompliance,
  batchCheckContentCompliance,
  getComplianceHistory,
  saveComplianceConfig,
  getComplianceConfig
} from '@/api/social-media/compliance.js';

const historyLoading = ref(false);
const checkLoading = ref(false);
const showCheckModal = ref(false);

// 合规统计
const complianceStats = ref({
  todayChecked: 0,
  passed: 0,
  warnings: 0,
  violations: 0
});

// 规则配置
const ruleConfig = reactive({
  sensitiveWordEnabled: true,
  adComplianceEnabled: true,
  kolComplianceEnabled: true,
  platformSpecificEnabled: true,
  sensitiveWordsText: '垃圾,骗子,假货,山寨'
});

// 检查历史
const historyList = ref([]);

// 检查表单
const checkForm = reactive({
  content: '',
  platformIndex: 0,
  contentTypeIndex: 0
});

// 检查结果
const checkResult = ref(null);

const platformOptions = ['douyin', 'wechat', 'xiaohongshu'];
const contentTypeOptions = ['text', 'image', 'video', 'mixed'];

// 页面加载
onMounted(async () => {
  await loadComplianceStats();
  await loadRuleConfig();
  await loadComplianceHistory();
});

// 加载合规统计
async function loadComplianceStats() {
  try {
    // 模拟统计数据
    complianceStats.value = {
      todayChecked: 156,
      passed: 134,
      warnings: 15,
      violations: 7
    };
  } catch (error) {
    console.error('加载合规统计失败:', error);
  }
}

// 加载规则配置
async function loadRuleConfig() {
  try {
    const config = await getComplianceConfig();
    if (config.success) {
      Object.assign(ruleConfig, config.data);
    }
  } catch (error) {
    console.error('加载规则配置失败:', error);
  }
}

// 加载合规历史
async function loadComplianceHistory() {
  historyLoading.value = true;
  try {
    const result = await getComplianceHistory({
      page: 1,
      limit: 20
    });
    
    if (result.success) {
      historyList.value = result.data.list || [];
    }
  } catch (error) {
    console.error('加载合规历史失败:', error);
    // 模拟历史数据
    historyList.value = [
      {
        id: 1,
        content: '这款办公用品真的很好用，推荐大家购买！',
        platform: 'douyin',
        checkTime: '2024-01-15 14:30',
        overallStatus: 'pass',
        violations: [],
        warnings: []
      },
      {
        id: 2,
        content: '我们公司提供最便宜的办公用品，绝对物超所值！',
        platform: 'wechat',
        checkTime: '2024-01-15 13:45',
        overallStatus: 'warning',
        violations: [],
        warnings: [{
          type: '价格宣传',
          description: '使用"最便宜"等绝对化用语'
        }]
      }
    ];
  } finally {
    historyLoading.value = false;
  }
}

// 敏感词开关
function onSensitiveWordChange(e) {
  ruleConfig.sensitiveWordEnabled = e.detail.value;
}

// 广告合规开关
function onAdComplianceChange(e) {
  ruleConfig.adComplianceEnabled = e.detail.value;
}

// KOL合规开关
function onKolComplianceChange(e) {
  ruleConfig.kolComplianceEnabled = e.detail.value;
}

// 平台特定规则开关
function onPlatformSpecificChange(e) {
  ruleConfig.platformSpecificEnabled = e.detail.value;
}

// 保存规则配置
async function saveRuleConfig() {
  try {
    const config = {
      sensitiveWordEnabled: ruleConfig.sensitiveWordEnabled,
      adComplianceEnabled: ruleConfig.adComplianceEnabled,
      kolComplianceEnabled: ruleConfig.kolComplianceEnabled,
      platformSpecificEnabled: ruleConfig.platformSpecificEnabled,
      sensitiveWords: ruleConfig.sensitiveWordsText.split(',').map(word => word.trim()).filter(word => word)
    };
    
    const result = await saveComplianceConfig(config);
    
    if (result.success) {
      uni.showToast({
        title: '配置保存成功',
        icon: 'success'
      });
    }
  } catch (error) {
    console.error('保存规则配置失败:', error);
    uni.showToast({
      title: '保存配置失败',
      icon: 'none'
    });
  }
}

// 清空历史
function clearHistory() {
  uni.showModal({
    title: '确认清空',
    content: '确定要清空所有检查历史记录吗？',
    success: async (res) => {
      if (res.confirm) {
        historyList.value = [];
        uni.showToast({
          title: '历史记录已清空',
          icon: 'success'
        });
      }
    }
  });
}

// 平台选择
function onCheckPlatformChange(e) {
  checkForm.platformIndex = parseInt(e.detail.value);
}

// 内容类型选择
function onContentTypeChange(e) {
  checkForm.contentTypeIndex = parseInt(e.detail.value);
}

// 执行检查
async function performCheck() {
  if (!checkForm.content.trim()) {
    uni.showToast({
      title: '请输入检查内容',
      icon: 'none'
    });
    return;
  }
  
  checkLoading.value = true;
  try {
    const platform = platformOptions[checkForm.platformIndex];
    const contentType = contentTypeOptions[checkForm.contentTypeIndex];
    
    const result = await checkContentCompliance({
      content: checkForm.content,
      platform: platform,
      contentType: contentType
    });
    
    if (result.success) {
      checkResult.value = result.data;
      closeCheckModal();
      this.$refs.resultModal.open();
      
      // 更新统计和历史记录
      await loadComplianceStats();
      await loadComplianceHistory();
    }
  } catch (error) {
    console.error('合规检查失败:', error);
    uni.showToast({
      title: '检查失败',
      icon: 'none'
    });
  } finally {
    checkLoading.value = false;
  }
}

// 关闭检查弹窗
function closeCheckModal() {
  showCheckModal.value = false;
  checkForm.content = '';
  checkForm.platformIndex = 0;
  checkForm.contentTypeIndex = 0;
}

// 关闭结果弹窗
function closeResultModal() {
  this.$refs.resultModal.close();
  checkResult.value = null;
}

// 查看检查详情
function viewCheckDetail(item) {
  checkResult.value = {
    overallStatus: item.overallStatus,
    totalScore: 85,
    violations: item.violations || [],
    warnings: item.warnings || []
  };
  this.$refs.resultModal.open();
}
</script>

<style scoped>
</style>