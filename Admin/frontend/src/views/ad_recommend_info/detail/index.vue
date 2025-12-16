<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">广告推荐</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、广告标题、广告图片、跳转链接、CPC费用。</span>
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
:to="{ path: '/ad_recommend_info' }"
>
广告推荐管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
广告推荐详情
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
label="广告推荐详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"adTitle":[{"trigger":"blur","message":"广告标题不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="广告标题" prop="adTitle">
        <el-input clearable placeholder="请输入广告标题" v-model="form.adTitle" />
    </el-form-item>

    <el-form-item  label="广告图片" prop="adImageResourceKey">
        <image-upload v-model="form.adImage" :type="true" />
    </el-form-item>

    <el-form-item  label="跳转链接" prop="adUrl">
        <el-input clearable placeholder="请输入跳转链接" v-model="form.adUrl" />
    </el-form-item>

    <el-form-item  label="CPC费用" prop="cpcFee">
        <el-input clearable placeholder="请输入CPC费用" v-model="form.cpcFee" />
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
      let res = await proxy.$api.ad_recommend_info['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.ad_recommend_info['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.ad_recommend_info['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.adRecommendInfoId){
        getDetail(routerQuery.adRecommendInfoId);
    }
    if(detail.value == 'all'){
        if(routerQuery.adRecommendInfoId){
            getDetailAll(routerQuery.adRecommendInfoId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>