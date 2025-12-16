<template>
  <base-layout>
    <view class="p-2">
      <view class="mb-4 flex items-center gap-2">
        <uni-icons type="list" size="24" color="#0ea5e9"/>
        <text class="text-xl font-bold text-primary-700">权限变更日志</text>
      </view>
      <view>
        <view v-for="item in logList" :key="item.permission_log_id" class="bg-white rounded-xl shadow-lg p-4 mb-4 flex flex-col gap-1">
          <view class="flex gap-2 items-center mb-1">
            <uni-icons type="person" size="18" color="#0ea5e9" />
            <text class="font-bold text-primary-700">{{ item.username }}</text>
            <uni-tag :text="item.action_type" size="small" type="primary" circle />
            <text class="text-neutral-400 text-xs ml-2">{{ item.action_time }}</text>
          </view>
          <view class="text-neutral-700 text-sm">对象：{{ item.target_object }}</view>
          <view class="text-neutral-500 text-xs">内容：{{ item.change_content }}</view>
        </view>
        <view v-if="logList.length === 0" class="text-center text-neutral-400 py-10">
          <uni-icons type="list" size="40" color="#9ca3af" class="mb-2" />
          <view>暂无日志数据</view>
        </view>
      </view>
    </view>
  </base-layout>
</template>
<script setup>
const { proxy } = getCurrentInstance();
const logList = ref([]);
onLoad(async () => {
  const res = await proxy.$cf.table.page({ table_name: 'permission_log', param: { current: 1, pageSize: 20 } });
  if (res.success) logList.value = res.data.records;
});
</script>