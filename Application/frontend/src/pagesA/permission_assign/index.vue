<template>
  <base-layout>
    <view class="p-2">
      <view class="mb-4 flex items-center gap-2">
        <uni-icons type="tune" size="24" color="#0ea5e9"/>
        <text class="text-xl font-bold text-primary-700">权限分配</text>
      </view>
      <view class="mb-4">
        <text class="font-semibold text-base">角色：</text>
        <text class="text-primary-700">{{ role.role_name }}</text>
      </view>
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <view class="mb-2 font-bold text-primary-600">系统功能权限列表</view>
        <view v-for="perm in permissionList" :key="perm.permission_id" class="flex items-center gap-2 mb-2">
          <uni-data-checkbox
            :localdata="[{value: perm.permission_id, text: perm.permission_name}]"
            :multiple="false"
            v-model="checkedPermissions"
            mode="button"
            @change="onCheckChange"
          />
          <text class="text-neutral-400 text-xs">{{ perm.permission_desc }}</text>
        </view>
      </view>
      <button class="w-full bg-primary-500 text-white rounded-full py-2 text-base font-bold shadow" @click="saveAssign">保存分配</button>
    </view>
  </base-layout>
</template>
<script setup>
const { proxy } = getCurrentInstance();
const role = ref({});
const permissionList = ref([]);
const checkedPermissions = ref([]);
let role_id = null;
onLoad(async (option) => {
  role_id = option.role_id;
  // 获取角色信息
  const roleRes = await proxy.$cf.table.get({ table_name: 'role', param: { role_id } });
  if (roleRes.success && roleRes.data) role.value = roleRes.data;
  // 获取所有权限
  const permRes = await proxy.$cf.table.list({ table_name: 'permission', param: {} });
  if (permRes.success) permissionList.value = permRes.data;
  // 获取当前角色已分配权限
  const rpRes = await proxy.$cf.table.list({ table_name: 'role_permission', param: { role_id } });
  if (rpRes.success) checkedPermissions.value = rpRes.data.map(i => i.permission_id);
});
function onCheckChange(val) {
  checkedPermissions.value = val;
}
async function saveAssign() {
  // 先删除所有已分配，再批量新增
  const oldRes = await proxy.$cf.table.list({ table_name: 'role_permission', param: { role_id } });
  if (oldRes.success && oldRes.data) {
    for (const item of oldRes.data) {
      await proxy.$cf.table.delete({ table_name: 'role_permission', param: { role_permission_id: item.role_permission_id } });
    }
  }
  for (const pid of checkedPermissions.value) {
    await proxy.$cf.table.add({ table_name: 'role_permission', param: { role_id, permission_id: pid } });
  }
  proxy.$cf.toast({ message: '权限分配已保存', level: 'success' });
  proxy.$cf.navigate.to({ url: '/pages/permission_manage/index', type: 'page' });
}
</script>