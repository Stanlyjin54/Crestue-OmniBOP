<template>
  <base-layout>
    <view class="p-2 h-full flex flex-col bg-neutral-50">
      <!-- 顶部标题栏 -->
      <view class="flex items-center mb-4">
        <uni-icons type="back" size="24" class="mr-2 text-primary-500" @click="goHome"/>
        <text class="text-2xl font-bold text-primary-700 flex-1">AI助手</text>
      </view>

      <!-- AI助手形象与简介 -->
      <view class="flex items-center mb-4 bg-white rounded-xl shadow-lg p-3">
        <image src="https://www.codeflying.net/preview/ai-robot.jpg" mode="aspectFill" class="w-14 h-14 rounded-lg mr-3"/>
        <view>
          <text class="block text-lg font-semibold text-primary-700">智能AI助手</text>
          <text class="block text-sm text-neutral-500">随时为你解答库存、客户、商机等问题</text>
        </view>
      </view>

      <!-- 聊天消息历史 -->
      <view class="flex-1 overflow-y-auto mb-4">
        <view v-if="loadingHistory" class="flex justify-center items-center h-24">
          <uni-icons type="spinner-cycle" size="32" color="#4b5563" class="animate-spin"/>
        </view>
        <view v-else>
          <view v-if="chatHistory.length == 0" class="text-center text-neutral-400 mt-10">
            <uni-icons type="chatbubble" size="36" class="mb-2"/>
            <text class="block">暂无对话，快来提问吧！</text>
          </view>
          <view v-else class="flex flex-col gap-4">
            <view v-for="(item, idx) in chatHistory" :key="item.ai_assistant_log_id || idx" class="flex flex-col">
              <!-- 用户提问 -->
              <view class="flex items-end justify-end mb-1">
                <view class="max-w-[70%] bg-primary-100 rounded-xl px-4 py-2 shadow-md">
                  <text class="text-primary-700 text-base">{{ item.query_text }}</text>
                </view>
                <uni-icons type="person-filled" size="28" class="ml-2 text-primary-500"/>
              </view>
              <!-- AI回复 -->
              <view class="flex items-start mb-1">
                <uni-icons type="headphones" size="28" class="mr-2 text-success-500"/>
                <view class="max-w-[80%] bg-white rounded-xl px-4 py-2 shadow-md">
                  <text class="text-neutral-700 text-base whitespace-pre-line">{{ item.response_text }}</text>
                </view>
              </view>
              <view class="flex justify-end">
                <text class="text-xs text-neutral-400">{{ formatTime(item.query_time) }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 输入栏 -->
      <view class="flex items-center gap-2 bg-white rounded-xl shadow-lg px-3 py-2">
        <uni-easyinput
          class="flex-1 box-border"
          type="text"
          v-model="inputText"
          :placeholder="'请输入您的问题，如：库存预警、客户活跃度、商机建议…'"
          @input="onInput"
          :clearable="true"
          @change="onEnter"
        />
        <button
          class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm flex items-center gap-1"
          :loading="sending"
          @click="sendMessage"
        >
          <uni-icons type="paperplane" size="18" color="#fff"/>
          发送
        </button>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const inputText = ref('');
const sending = ref(false);
const loadingHistory = ref(true);
const chatHistory = ref([]);
const loginUser = ref(null);

function formatTime(dt) {
  if (!dt) return '';
  // dt: '2025-10-10 12:12:12'
  const d = typeof dt == 'string' ? dt : '';
  return d.replace('T', ' ').slice(0, 16);
}

// 获取当前登录用户
async function fetchUser() {
  const res = await proxy.$cf.login.getLoginUser();
  if (res.success) {
    loginUser.value = res.data;
  } else {
    proxy.$cf.toast({ message: '请先登录', level: 'error' });
    // 跳转登录页逻辑可加在此
  }
}

// 加载历史对话
async function fetchHistory() {
  loadingHistory.value = true;
  if (!loginUser.value) return;
  const res = await proxy.$cf.table.page({
    table_name: 'ai_assistant_log',
    param: {
      user_info_user_info_id_1: loginUser.value.user_info_id,
      current: 1,
      pageSize: 20
    }
  });
  if (res.success) {
    // 按时间正序排列
    chatHistory.value = (res.data.records || []).sort((a, b) => (a.query_time > b.query_time ? 1 : -1));
  }
  loadingHistory.value = false;
}

// 发送消息
async function sendMessage() {
  if (!inputText.value.trim()) {
    proxy.$cf.toast({ message: '请输入问题', level: 'warning' });
    return;
  }
  if (sending.value) return;
  sending.value = true;

  // 1. 记录用户问题
  const question = inputText.value.trim();
  inputText.value = '';

  // 2. AI回复
  let aiRes;
  try {
    aiRes = await proxy.$cf.text.text2text({
      prompt: '角色：你是企业管理AI助手。任务：根据用户的问题，智能解答库存、客户、商机等相关业务问题，输出简明、专业的回复。输出：直接输出AI回复内容。',
      text: question
    });
  } catch (e) {
    aiRes = { success: false, message: 'AI服务异常' };
  }
  const aiReply = aiRes.success ? aiRes.data : (aiRes.message || 'AI服务异常，请稍后再试');

  // 3. 记录对话到日志
  const now = new Date();
  const pad = n => n.toString().padStart(2, '0');
  const dt = `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())} ${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`;

  const logRes = await proxy.$cf.table.add({
    table_name: 'ai_assistant_log',
    param: {
      user_info_user_info_id_1: loginUser.value.user_info_id,
      query_text: question,
      response_text: aiReply,
      query_time: dt
    }
  });

  if (logRes.success) {
    chatHistory.value.push({
      ai_assistant_log_id: logRes.data,
      user_info_user_info_id_1: loginUser.value.user_info_id,
      query_text: question,
      response_text: aiReply,
      query_time: dt
    });
    // 滚动到底部
    setTimeout(() => {
      uni.pageScrollTo({ scrollTop: 99999, duration: 300 });
    }, 200);
  } else {
    proxy.$cf.toast({ message: '记录对话失败', level: 'error' });
  }
  sending.value = false;
}

// 输入框回车发送
function onEnter(e) {
  // 兼容回车发送
  sendMessage();
}

function onInput(v) {
  inputText.value = v;
}

// 返回首页
function goHome() {
  proxy.$cf.navigate.to({
    url: '/pages/home/index',
    type: 'page'
  });
}

// 页面加载
onLoad(async () => {
  await fetchUser();
  if (loginUser.value) {
    await fetchHistory();
  }
});
</script>