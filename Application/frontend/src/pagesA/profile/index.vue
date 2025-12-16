<template>
  <base-layout>
    <view class="p-2">
      <!-- 个人信息卡片 -->
      <view class="bg-white rounded-xl shadow-lg p-4 flex flex-col items-center mb-4">
        <image
          v-if="userInfo.avatar && userInfo.avatar.length > 0"
          :src="get_resource_url(userInfo.avatar[0].url)"
          class="w-24 h-24 rounded-full shadow-xl object-cover mb-2"
          mode="aspectFill"
        />
        <view v-else class="w-24 h-24 rounded-full bg-neutral-200 flex items-center justify-center mb-2">
          <uni-icons type="person" size="48" color="#9ca3af" />
        </view>
        <text class="text-xl font-bold text-primary-700 mb-1">{{ userInfo.username || '未设置用户名' }}</text>
        <view class="flex items-center gap-2 mb-1">
          <uni-icons type="phone" size="18" color="#0ea5e9" />
          <text class="text-base text-neutral-600">{{ userInfo.phone_number || '未绑定手机号' }}</text>
        </view>
        <view class="flex items-center gap-2 mb-1">
          <uni-icons type="calendar" size="18" color="#4b5563" />
          <text class="text-sm text-neutral-500">
            注册时间：{{ formatDate(userInfo.registration_date) || '无' }}
          </text>
        </view>
        <button
          class="mt-4 bg-primary-500 text-white rounded-full px-6 py-2 text-base shadow-md"
          @click="openEdit"
        >
          <uni-icons type="compose" size="18" color="#fff" class="mr-1" />
          编辑资料
        </button>
      </view>

      <!-- 其他信息区域可扩展 -->

      <!-- 编辑弹窗 -->
      <uni-popup ref="editPopup" type="center">
        <view class="bg-white p-4 w-80 rounded-xl shadow-lg">
          <profile-edit
            :user-info="userInfo"
            @editSuccess="onEditSuccess"
            @close="closeEdit"
          />
        </view>
      </uni-popup>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const userInfo = ref({
  avatar: [],
  username: '',
  phone_number: '',
  registration_date: ''
});

// 获取当前登录用户信息
async function fetchUserInfo() {
  const res = await proxy.$cf.login.getLoginUser();
  if (res.success && res.data) {
    userInfo.value = {
      ...res.data,
      avatar: (res.data.avatar || []).map(item => ({
        ...item,
        url: proxy.get_resource_url(item.url)
      }))
    };
  } else {
    await proxy.$cf.toast({ message: '获取用户信息失败', level: 'error' });
  }
}

// 格式化日期
function formatDate(dateStr) {
  if (!dateStr) return '';
  // 兼容 YYYY-MM-DD 和 YYYY-MM-DD HH:mm:ss
  return dateStr.length > 10 ? dateStr.slice(0, 10) : dateStr;
}

// 打开编辑弹窗
function openEdit() {
  proxy.$refs.editPopup.open();
}

// 关闭编辑弹窗
function closeEdit() {
  proxy.$refs.editPopup.close();
}

// 编辑成功回调，刷新用户信息
async function onEditSuccess() {
  closeEdit();
  await proxy.$cf.toast({ message: '资料修改成功', level: 'success' });
  fetchUserInfo();
}

// 页面加载时获取用户信息
onLoad(() => {
  fetchUserInfo();
});
</script>