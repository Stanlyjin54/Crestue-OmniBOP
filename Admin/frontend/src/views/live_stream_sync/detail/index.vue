<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">直播带货同步</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、订单ID、平台名称、同步时间。</span>
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
:to="{ path: '/live_stream_sync' }"
>
直播带货同步管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
直播带货同步详情
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
label="直播带货同步详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"syncTime":[{"trigger":"blur","message":"同步时间不能为空","required":true}],"orderInfoOrderInfoId1":[{"trigger":"blur","message":"订单ID不能为空","required":true}],"platform":[{"trigger":"blur","message":"平台名称不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="订单ID" prop="orderInfoOrderInfoId1">
        <el-input clearable placeholder="请输入订单ID" v-model="form.orderInfoOrderInfoId1" />
    </el-form-item>

    <el-form-item  label="平台名称" prop="platform">
        <el-input clearable placeholder="请输入平台名称" v-model="form.platform" />
    </el-form-item>

    <el-form-item   label="同步时间" prop="syncTime">
        <el-date-picker placeholder="请输入同步时间" v-model="form.syncTime" type="datetime"
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
      let res = await proxy.$api.live_stream_sync['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.live_stream_sync['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.live_stream_sync['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.liveStreamSyncId){
        getDetail(routerQuery.liveStreamSyncId);
    }
    if(detail.value == 'all'){
        if(routerQuery.liveStreamSyncId){
            getDetailAll(routerQuery.liveStreamSyncId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>