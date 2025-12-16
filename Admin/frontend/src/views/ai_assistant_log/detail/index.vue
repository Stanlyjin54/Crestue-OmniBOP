<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">AI助手日志</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、用户ID、查询内容、AI回复内容、查询时间。</span>
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
:to="{ path: '/ai_assistant_log' }"
>
AI助手日志管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
AI助手日志详情
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
label="AI助手日志详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"responseText":[{"trigger":"blur","message":"AI回复内容不能为空","required":true}],"queryText":[{"trigger":"blur","message":"查询内容不能为空","required":true}],"queryTime":[{"trigger":"blur","message":"查询时间不能为空","required":true}],"userInfoUserInfoId1":[{"trigger":"blur","message":"用户ID不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="用户ID" prop="userInfoUserInfoId1">
        <el-input clearable placeholder="请输入用户ID" v-model="form.userInfoUserInfoId1" />
    </el-form-item>

    <el-form-item  label="查询内容" prop="queryText">
        <el-input clearable placeholder="请输入查询内容" v-model="form.queryText" />
    </el-form-item>

    <el-form-item  label="AI回复内容" prop="responseText">
        <el-input clearable placeholder="请输入AI回复内容" v-model="form.responseText" />
    </el-form-item>

    <el-form-item   label="查询时间" prop="queryTime">
        <el-date-picker placeholder="请输入查询时间" v-model="form.queryTime" type="datetime"
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
      let res = await proxy.$api.ai_assistant_log['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.ai_assistant_log['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.ai_assistant_log['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.aiAssistantLogId){
        getDetail(routerQuery.aiAssistantLogId);
    }
    if(detail.value == 'all'){
        if(routerQuery.aiAssistantLogId){
            getDetailAll(routerQuery.aiAssistantLogId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>