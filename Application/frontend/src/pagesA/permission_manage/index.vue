<template>
  <base-layout>
    <view class="p-2">
      <view class="flex items-center justify-between mb-4">
        <text class="text-xl font-bold text-primary-700">权限管理</text>
        <button class="bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-lg flex items-center gap-1" @click="openRoleEdit()">
          <uni-icons type="plus" size="20" />
          新增角色
        </button>
      </view>
      <view>
        <view v-for="item in roleList" :key="item.role_id" class="bg-white rounded-xl shadow-lg p-4 mb-4 flex items-center justify-between">
          <view class="flex flex-col">
            <text class="font-bold text-base text-primary-700">{{ item.role_name }}</text>
            <text class="text-neutral-500 text-xs mt-1">{{ item.role_desc }}</text>
          </view>
          <view class="flex gap-2">
            <button class="bg-secondary-500 text-white rounded-full px-4 py-1 text-xs flex items-center" @click="openRoleEdit(item)">
              <uni-icons type="compose" size="16" />编辑
            </button>
            <button class="bg-warning-500 text-white rounded-full px-4 py-1 text-xs flex items-center" @click="delRole(item.role_id)">
              <uni-icons type="trash" size="16" />删除
            </button>
            <button class="bg-success-500 text-white rounded-full px-4 py-1 text-xs flex items-center" @click="goPermissionAssign(item.role_id)">
              <uni-icons type="tune" size="16" />权限分配
            </button>
          </view>
        </view>
        <view v-if="roleList.length === 0" class="text-center text-neutral-400 py-10">
          <uni-icons type="list" size="40" color="#9ca3af" class="mb-2" />
          <view>暂无角色数据</view>
        </view>
      </view>
      <uni-popup ref="roleEditPopup" type="center">
        <role_manage ref="roleManageRef" @refreshList="fetchRoleList" />
      </uni-popup>
    </view>
  </base-layout>
</template>
<script setup>
const { proxy } = getCurrentInstance();
const roleList = ref([]);
const roleEditPopup = ref(null);
const roleManageRef = ref(null);
async function fetchRoleList() {
  const res = await proxy.$cf.table.list({ table_name: 'role', param: {} });
  if (res.success) {
    roleList.value = res.data;
  }
}
function openRoleEdit(item = null) {
  proxy.$refs.roleEditPopup.open();
  proxy.$refs.roleManageRef.open(item ? item.role_id : null);
}
async function delRole(role_id) {
  const confirm = await proxy.$cf.model({ title: '删除角色', message: '确定要删除该角色吗？', confirmText: '删除', cancelText: '取消' });
  if (!confirm.confirm) return;
  const res = await proxy.$cf.table.delete({ table_name: 'role', param: { role_id } });
  if (res.success) {
    proxy.$cf.toast({ message: '删除成功', level: 'success' });
    fetchRoleList();
  } else {
    proxy.$cf.toast({ message: res.message || '删除失败', level: 'error' });
  }
}
function goPermissionAssign(role_id) {
  proxy.$cf.navigate.to({ url: `/pages/permission_assign/index?role_id=${role_id}`, type: 'page' });
}
onLoad(() => { fetchRoleList(); });
</script>