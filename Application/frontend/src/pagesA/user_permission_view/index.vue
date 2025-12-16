<template>
  <base-layout>
    <view class="p-2">
      <view class="mb-4 flex items-center gap-2">
        <uni-icons type="person" size="24" color="#0ea5e9"/>
        <text class="text-xl font-bold text-primary-700">用户权限查看</text>
      </view>
      <view class="mb-4">
        <text class="font-semibold text-base">用户：</text>
        <text class="text-primary-700">{{ user.username }}</text>
      </view>
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <view class="mb-2 font-bold text-primary-600">所属角色</view>
        <view v-for="role in userRoles" :key="role.role_id" class="mb-2">
          <uni-tag :text="role.role_name" size="normal" type="primary" circle />
          <text class="text-neutral-400 text-xs ml-2">{{ role.role_desc }}</text>
        </view>
      </view>
      <view class="bg-white rounded-xl shadow-lg p-4 mb-4">
        <view class="mb-2 font-bold text-primary-600">拥有权限</view>
        <view v-for="perm in userPermissions" :key="perm.permission_id" class="mb-2">
          <uni-tag :text="perm.permission_name" size="small" type="success" circle />
          <text class="text-neutral-400 text-xs ml-2">{{ perm.permission_desc }}</text>
        </view>
      </view>
    </view>
  </base-layout>
</template>
<script setup>
const { proxy } = getCurrentInstance();
const user = ref({});
const userRoles = ref([]);
const userPermissions = ref([]);
onLoad(async (option) => {
  const user_info_id = option.user_info_id;
  // 用户信息
  const userRes = await proxy.$cf.table.get({ table_name: 'user_info', param: { user_info_id } });
  if (userRes.success) user.value = userRes.data;
  // 用户所有角色
  const urRes = await proxy.$cf.table.list({ table_name: 'user_role', param: { user_info_id } });
  const roleIds = urRes.success ? urRes.data.map(i => i.role_id) : [];
  // 角色详情
  const roleList = [];
  for (const rid of roleIds) {
    const r = await proxy.$cf.table.get({ table_name: 'role', param: { role_id: rid } });
    if (r.success && r.data) roleList.push(r.data);
  }
  userRoles.value = roleList;
  // 角色-权限
  let permIds = [];
  for (const rid of roleIds) {
    const rpRes = await proxy.$cf.table.list({ table_name: 'role_permission', param: { role_id: rid } });
    if (rpRes.success) permIds = permIds.concat(rpRes.data.map(i => i.permission_id));
  }
  // 权限详情
  const permList = [];
  for (const pid of [...new Set(permIds)]) {
    const p = await proxy.$cf.table.get({ table_name: 'permission', param: { permission_id: pid } });
    if (p.success && p.data) permList.push(p.data);
  }
  userPermissions.value = permList;
});
</script>