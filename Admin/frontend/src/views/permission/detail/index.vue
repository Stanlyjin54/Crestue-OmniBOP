<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">权限</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、权限名称、权限描述、功能标识。</span>
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
:to="{ path: '/permission' }"
>
权限管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
权限详情
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
label="权限详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"featureCode":[{"trigger":"blur","message":"功能标识不能为空","required":true}],"permissionName":[{"trigger":"blur","message":"权限名称不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="权限名称" prop="permissionName">
        <el-input clearable placeholder="请输入权限名称" v-model="form.permissionName" />
    </el-form-item>

    <el-form-item  label="权限描述" prop="permissionDesc">
        <el-input clearable placeholder="请输入权限描述" v-model="form.permissionDesc" />
    </el-form-item>

    <el-form-item  label="功能标识" prop="featureCode">
        <el-input clearable placeholder="请输入功能标识" v-model="form.featureCode" />
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
      let res = await proxy.$api.permission['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.permission['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.permission['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.permissionId){
        getDetail(routerQuery.permissionId);
    }
    if(detail.value == 'all'){
        if(routerQuery.permissionId){
            getDetailAll(routerQuery.permissionId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>