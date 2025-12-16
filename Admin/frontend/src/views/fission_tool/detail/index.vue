<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">裂变工具</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、工具类型、活动标题、配置内容、开始日期、结束日期。</span>
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
:to="{ path: '/fission_tool' }"
>
裂变工具管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
裂变工具详情
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
label="裂变工具详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"endDate":[{"trigger":"blur","message":"结束日期不能为空","required":true}],"title":[{"trigger":"blur","message":"活动标题不能为空","required":true}],"toolType":[{"trigger":"blur","message":"工具类型不能为空","required":true}],"startDate":[{"trigger":"blur","message":"开始日期不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="工具类型" prop="toolType">
        <el-input clearable placeholder="请输入工具类型" v-model="form.toolType" />
    </el-form-item>

    <el-form-item  label="活动标题" prop="title">
        <el-input clearable placeholder="请输入活动标题" v-model="form.title" />
    </el-form-item>

    <el-form-item  label="配置内容" prop="config">
        <el-input clearable placeholder="请输入配置内容" v-model="form.config" />
    </el-form-item>

    <el-form-item   label="开始日期" prop="startDate">
        <el-date-picker placeholder="请输入开始日期" v-model="form.startDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item   label="结束日期" prop="endDate">
        <el-date-picker placeholder="请输入结束日期" v-model="form.endDate" type="datetime"
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
      let res = await proxy.$api.fission_tool['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.fission_tool['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.fission_tool['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.fissionToolId){
        getDetail(routerQuery.fissionToolId);
    }
    if(detail.value == 'all'){
        if(routerQuery.fissionToolId){
            getDetailAll(routerQuery.fissionToolId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>