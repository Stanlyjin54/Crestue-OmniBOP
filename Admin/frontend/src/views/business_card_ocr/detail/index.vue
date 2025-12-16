<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">名片OCR建档</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、客户ID、名片图片、OCR识别结果。</span>
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
:to="{ path: '/business_card_ocr' }"
>
名片OCR建档管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
名片OCR建档详情
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
label="名片OCR建档详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"ocrResult":[{"trigger":"blur","message":"OCR识别结果不能为空","required":true}],"customerInfoCustomerInfoId1":[{"trigger":"blur","message":"客户ID不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="客户ID" prop="customerInfoCustomerInfoId1">
        <el-input clearable placeholder="请输入客户ID" v-model="form.customerInfoCustomerInfoId1" />
    </el-form-item>

    <el-form-item  label="名片图片" prop="cardImageResourceKey">
        <image-upload v-model="form.cardImage" :type="true" />
    </el-form-item>

    <el-form-item  label="OCR识别结果" prop="ocrResult">
        <el-input clearable placeholder="请输入OCR识别结果" v-model="form.ocrResult" />
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
      let res = await proxy.$api.business_card_ocr['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.business_card_ocr['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.business_card_ocr['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.businessCardOcrId){
        getDetail(routerQuery.businessCardOcrId);
    }
    if(detail.value == 'all'){
        if(routerQuery.businessCardOcrId){
            getDetailAll(routerQuery.businessCardOcrId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>