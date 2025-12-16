<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">角色权限关联</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、角色ID、权限ID。</span>
      </div>
    </div>
    <div class="flex-start-end h-full m-r-10">
        <img src="@/assets/logo/document.png" height="130px" />
    </div>
</div>
<el-breadcrumb
separator="/"
class="m-20"
>
<el-breadcrumb-item
:to="{ path: '/' }"
>
首页
</el-breadcrumb-item>
<el-breadcrumb-item
:to="{ path: '/role_permission' }"
>
角色权限关联管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
角色权限关联详情
</el-breadcrumb-item>
</el-breadcrumb>
<base-wrapper
class="p-10"
>
<el-tabs
style="height: 200px"
class="demo-tabs"
>
<el-tab-pane
label="角色权限关联详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"permissionId":[{"trigger":"blur","message":"权限ID不能为空","required":true}],"roleId":[{"trigger":"blur","message":"角色ID不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="角色ID" prop="roleId">
        <el-input clearable placeholder="请输入角色ID" v-model="form.roleId" />
    </el-form-item>

    <el-form-item  label="权限ID" prop="permissionId">
        <el-input clearable placeholder="请输入权限ID" v-model="form.permissionId" />
    </el-form-item>

</el-form>
<base-layout
style="text-align: center;"
w_full="true"
>
<el-button
@click="submitForm"
round
color="#5D5FEF"
style="width: 200px;"
type="primary"
>保 存</el-button>
</base-layout>
</el-tab-pane>
</el-tabs>
</base-wrapper>
</base-wrapper>
   
</template>

<script setup>

const { proxy } = getCurrentInstance();
let form = ref({});
let formAll = ref({});
let rules = ref({});
let routerQuery = proxy.$route.query;
let detail = ref('normally')

function submitForm() {
  proxy.$refs.dataFormRef.validate(async (valid) => {
    if (valid) {
      let res = await proxy.$api.role_permission['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.role_permission['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.role_permission['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.rolePermissionId){
        getDetail(routerQuery.rolePermissionId);
    }
    if(detail.value == 'all'){
        if(routerQuery.rolePermissionId){
            getDetailAll(routerQuery.rolePermissionId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>