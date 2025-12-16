<template>
  <base-layout>
    <view class="p-2">
      <view class="mb-4 flex items-center gap-2">
        <uni-icons type="person" size="24" color="#0ea5e9"/>
        <text class="text-xl font-bold text-primary-700">用户角色管理</text>
      </view>
      <view class="mb-4">
        <text class="font-semibold text-base">用户：</text>
        <text class="text-primary-700">{{ user.username }}</text>
      </view>
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <view class="mb-2 font-bold text-primary-600">分配角色</view>
        <uni-data-checkbox
          :localdata="roleOptions"
          v-model="checkedRoles"
          mode="button"
          multiple
        />
      </view>
      <button class="w-full bg-primary-500 text-white rounded-full py-2 text-base font-bold shadow" @click="saveUserRole">保存分配</button>
    </view>
  </base-layout>
</template>
<script setup>
const { proxy } = getCurrentInstance();
const user = ref({});
const roleOptions = ref([]);
const checkedRoles = ref([]);
let user_info_id = null;
onLoad(async (option) => {
  user_info_id = option.user_info_id;
  // 用户信息
  const userRes = await proxy.$cf.table.get({ table_name: 'user_info', param: { user_info_id } });
  if (userRes.success) user.value = userRes.data;
  // 所有角色
  const roleRes = await proxy.$cf.table.list({ table_name: 'role', param: {} });
  if (roleRes.success) roleOptions.value = roleRes.data.map(i => ({ value: i.role_id, text: i.role_name }));
  // 已分配角色
  const urRes = await proxy.$cf.table.list({ table_name: 'user_role', param: { user_info_id } });
  if (urRes.success) checkedRoles.value = urRes.data.map(i => i.role_id);
});
async function saveUserRole() {
  // 先删除原有，再批量新增
  const oldRes = await proxy.$cf.table.list({ table_name: 'user_role', param: { user_info_id } });
  if (oldRes.success && oldRes.data) {
    for (const item of oldRes.data) {
      await proxy.$cf.table.delete({ table_name: 'user_role', param: { user_role_id: item.user_role_id } });
    }
  }
  for (const rid of checkedRoles.value) {
    await proxy.$cf.table.add({ table_name: 'user_role', param: { user_info_id, role_id: rid } });
  }
  proxy.$cf.toast({ message: '角色分配已保存', level: 'success' });
}
</script>