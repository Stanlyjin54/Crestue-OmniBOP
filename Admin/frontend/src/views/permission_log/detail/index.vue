<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">权限变更日志</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、操作人、操作类型、目标对象、变更内容、操作时间。</span>
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
:to="{ path: '/permission_log' }"
>
权限变更日志管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
权限变更日志详情
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
label="权限变更日志详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"actionType":[{"trigger":"blur","message":"操作类型不能为空","required":true}],"targetObject":[{"trigger":"blur","message":"目标对象不能为空","required":true}],"actionTime":[{"trigger":"blur","message":"操作时间不能为空","required":true}],"userInfoId":[{"trigger":"blur","message":"操作人不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="操作人" prop="userInfoId">
        <el-input clearable placeholder="请输入操作人" v-model="form.userInfoId" />
    </el-form-item>

    <el-form-item  label="操作类型" prop="actionType">
        <el-input clearable placeholder="请输入操作类型" v-model="form.actionType" />
    </el-form-item>

    <el-form-item  label="目标对象" prop="targetObject">
        <el-input clearable placeholder="请输入目标对象" v-model="form.targetObject" />
    </el-form-item>

    <el-form-item  label="变更内容" prop="changeContent">
        <el-input clearable placeholder="请输入变更内容" v-model="form.changeContent" />
    </el-form-item>

    <el-form-item   label="操作时间" prop="actionTime">
        <el-date-picker placeholder="请输入操作时间" v-model="form.actionTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
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
      let res = await proxy.$api.permission_log['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.permission_log['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.permission_log['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.permissionLogId){
        getDetail(routerQuery.permissionLogId);
    }
    if(detail.value == 'all'){
        if(routerQuery.permissionLogId){
            getDetailAll(routerQuery.permissionLogId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>