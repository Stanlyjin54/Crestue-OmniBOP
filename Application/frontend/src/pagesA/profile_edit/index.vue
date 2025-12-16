<template>
  <base-layout>
    <!-- 个人信息编辑弹窗 -->
    <uni-popup ref="editPopup" type="center">
      <view class="bg-white p-4 w-80 rounded-xl shadow-xl">
        <view class="mb-4 flex flex-col items-center">
          <view class="mb-2 text-lg font-bold text-primary-700">编辑个人信息</view>
          <!-- 头像展示 -->
          <image
            v-if="formData.avatar.length > 0"
            :src="get_resource_url(formData.avatar[0].url)"
            class="w-20 h-20 rounded-full object-cover shadow-md mb-2"
            mode="aspectFill"
          />
          <view class="mb-2 w-full">
            <base-upload
              :limit="1"
              mode="grid"
              mediaType="image"
              :initialFiles="formData.avatar"
              @success="upload_success"
              @delete="upload_delete"
            ></base-upload>
          </view>
        </view>
        <uni-forms :modelValue="formData" label-position="top">
          <uni-forms-item required label="用户名" name="username">
            <uni-easyinput
              type="text"
              v-model="formData.username"
              placeholder="请输入用户名"
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item required label="手机号" name="phone_number">
            <uni-easyinput
              type="text"
              v-model="formData.phone_number"
              placeholder="请输入手机号"
              class="box-border"
            />
          </uni-forms-item>
          <uni-forms-item required label="密码" name="password">
            <uni-easyinput
              type="password"
              v-model="formData.password"
              placeholder="请输入密码"
              class="box-border"
            />
          </uni-forms-item>
        </uni-forms>
        <button
          class="w-full mt-4 bg-primary-500 text-white rounded-full px-4 py-2 text-base shadow-lg font-bold"
          @click="submitEdit"
        >
          保存
        </button>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const editPopup = ref(null);

const formData = ref({
  user_info_id: null,
  username: '',
  phone_number: '',
  password: '',
  avatar: []
});

// 打开弹窗并初始化数据
async function openEditPopup() {
  // 获取当前登录用户信息
  const res = await proxy.$cf.login.getLoginUser();
  if (res.success && res.data) {
    formData.value.user_info_id = res.data.user_info_id;
    formData.value.username = res.data.username || '';
    formData.value.phone_number = res.data.phone_number || '';
    formData.value.password = res.data.password || '';
    formData.value.avatar = (res.data.avatar && res.data.avatar.length > 0)
      ? res.data.avatar.map(item => ({
        ...item,
        url: proxy.get_resource_url(item.url)
      }))
      : [];
  }
  proxy.$refs.editPopup.open();
}

// 头像上传成功
function upload_success(item) {
  if (item.success) {
    formData.value.avatar = [{
      url: item.data.url
    }];
  }
}

// 头像删除
function upload_delete(e) {
  formData.value.avatar = [];
}

// 保存编辑
async function submitEdit() {
  // 校验必填项
  if (!formData.value.username.trim()) {
    await proxy.$cf.toast({ message: '用户名不能为空', level: 'error' });
    return;
  }
  if (!formData.value.phone_number.trim()) {
    await proxy.$cf.toast({ message: '手机号不能为空', level: 'error' });
    return;
  }
  if (!formData.value.password.trim()) {
    await proxy.$cf.toast({ message: '密码不能为空', level: 'error' });
    return;
  }
  // 更新数据
  const updateRes = await proxy.$cf.table.update({
    table_name: 'user_info',
    param: {
      user_info_id: formData.value.user_info_id,
      username: formData.value.username,
      phone_number: formData.value.phone_number,
      password: formData.value.password,
      avatar: formData.value.avatar
    }
  });
  if (updateRes.success) {
    await proxy.$cf.toast({ message: '保存成功', level: 'success' });
    proxy.$refs.editPopup.close();
    // 通知父页面刷新
    proxy.$emit && proxy.$emit('refreshProfile');
  } else {
    await proxy.$cf.toast({ message: updateRes.message || '保存失败', level: 'error' });
  }
}

// 弹窗打开时自动初始化
onMounted(() => {
  // 若页面首次进入就需要弹窗，则可在此调用openEditPopup
  // openEditPopup();
});

// 提供openEditPopup方法给外部调用
defineExpose({
  openEditPopup
});
</script>