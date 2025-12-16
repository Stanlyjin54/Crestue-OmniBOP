<template>
  <base-layout>
    <view class="flex flex-col items-center justify-center min-h-screen bg-neutral-50 p-2">
      <!-- Logo/品牌区块 -->
      <view class="flex flex-col items-center mb-8 mt-12">
        <image
          src="https://www.codeflying.net/preview/ai-robot.jpg"
          mode="aspectFit"
          class="w-20 h-20 rounded-xl shadow-lg mb-4"
        />
        <text class="text-2xl font-bold text-primary-600 mb-1 tracking-wide">智能进销存系统</text>
        <text class="text-base text-neutral-500">高效管理 · 智能分析 · 安全可靠</text>
      </view>

      <!-- 登录/注册表单 -->
      <view class="w-full max-w-md bg-white rounded-xl shadow-xl p-6 box-border">
        <base-login
          login_type="passwd"
          show_title="账号登录 / 注册"
          relevanceTable="user_info"
          @loginSuccess="onLoginSuccess"
          @loginFail="onLoginFail"
        ></base-login>
      </view>

      <!-- 底部版权 -->
      <view class="mt-12 text-center w-full text-xs text-neutral-400">
        <text>© 2024 智能进销存系统 · CodeFlying</text>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance()

// 登录成功后跳转首页
function onLoginSuccess() {
  proxy.$cf.toast({ message: '登录成功，正在进入系统', level: 'success' })
  proxy.$cf.navigate.to({
    url: '/pages/home/index',
    type: 'page'
  })
}

// 登录失败，弹出提示
function onLoginFail() {
  proxy.$cf.toast({ message: '登录失败，请检查账号密码', level: 'error' })
}
</script>