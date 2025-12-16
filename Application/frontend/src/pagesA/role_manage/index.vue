<template>
  <base-layout>
    <uni-popup ref="editPopup" type="center">
      <view class="bg-white p-4 w-80 rounded-xl shadow-xl">
        <view class="mb-4 flex items-center gap-2">
          <uni-icons type="compose" size="24" color="#007BFF"/>
          <text class="text-lg font-bold text-primary-600">{{formData.role_id ? '编辑角色' : '新增角色'}}</text>
        </view>
        <uni-forms :modelValue="formData" label-position="top">
          <uni-forms-item name="role_name" label="角色名称" required>
            <uni-easyinput v-model="formData.role_name" type="text" placeholder="请输入角色名称" clearable />
          </uni-forms-item>
          <uni-forms-item name="role_desc" label="角色描述">
            <uni-easyinput v-model="formData.role_desc" type="text" placeholder="请输入角色描述" clearable />
          </uni-forms-item>
        </uni-forms>
        <view class="flex gap-4 mt-4">
          <button class="flex-1 bg-primary-500 text-white rounded-full px-4 py-2 text-sm shadow-md" @click="handleSave">保存</button>
          <button class="flex-1 bg-neutral-200 text-primary-600 rounded-full px-4 py-2 text-sm" @click="closePopup">取消</button>
        </view>
      </view>
    </uni-popup>
  </base-layout>
</template>
<script setup>
const { proxy } = getCurrentInstance();
const formData = ref({ role_id: null, role_name: '', role_desc: '' });
const editPopup = ref(null);
function open(role_id = null) {
  if (role_id) {
    proxy.$cf.table.get({ table_name: 'role', param: { role_id } }).then(res => {
      if (res.success && res.data) {
        formData.value = { ...res.data };
      }
    });
  } else {
    formData.value = { role_id: null, role_name: '', role_desc: '' };
  }
  proxy.$refs.editPopup.open();
}
function closePopup() { proxy.$refs.editPopup.close(); }
async function handleSave() {
  if (!formData.value.role_name) {
    proxy.$cf.toast({ message: '角色名称不能为空', level: 'error' }); return;
  }
  let res;
  if (formData.value.role_id) {
    res = await proxy.$cf.table.update({ table_name: 'role', param: formData.value });
  } else {
    res = await proxy.$cf.table.add({ table_name: 'role', param: formData.value });
  }
  if (res.success) {
    proxy.$cf.toast({ message: '保存成功', level: 'success' });
    closePopup();
    proxy.$emit && proxy.$emit('refreshList');
  } else {
    proxy.$cf.toast({ message: res.message || '保存失败', level: 'error' });
  }
}
defineExpose({ open });
</script>