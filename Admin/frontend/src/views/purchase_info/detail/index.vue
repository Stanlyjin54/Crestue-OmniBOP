<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">采购信息</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、采购单号、采购员ID、供应商、采购总金额、采购时间、采购状态。</span>
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
:to="{ path: '/purchase_info' }"
>
采购信息管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
采购信息详情
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
label="采购信息详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"totalAmount":[{"trigger":"blur","message":"采购总金额不能为空","required":true}],"purchaseDate":[{"trigger":"blur","message":"采购时间不能为空","required":true}],"purchaseStatusEnumPurchaseStatusEnumId1":[{"trigger":"blur","message":"采购状态不能为空","required":true}],"purchaseNo":[{"trigger":"blur","message":"采购单号不能为空","required":true}],"userInfoUserInfoId1":[{"trigger":"blur","message":"采购员ID不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="采购单号" prop="purchaseNo">
        <el-input clearable placeholder="请输入采购单号" v-model="form.purchaseNo" />
    </el-form-item>

    <el-form-item  label="采购员ID" prop="userInfoUserInfoId1">
        <el-input clearable placeholder="请输入采购员ID" v-model="form.userInfoUserInfoId1" />
    </el-form-item>

    <el-form-item  label="供应商" prop="supplier">
        <el-input clearable placeholder="请输入供应商" v-model="form.supplier" />
    </el-form-item>

    <el-form-item  label="采购总金额" prop="totalAmount">
        <el-input clearable placeholder="请输入采购总金额" v-model="form.totalAmount" />
    </el-form-item>

    <el-form-item   label="采购时间" prop="purchaseDate">
        <el-date-picker placeholder="请输入采购时间" v-model="form.purchaseDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item  label="采购状态" prop="purchaseStatusEnumPurchaseStatusEnumId1">
        <el-input clearable placeholder="请输入采购状态" v-model="form.purchaseStatusEnumPurchaseStatusEnumId1" />
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
      let res = await proxy.$api.purchase_info['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.purchase_info['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.purchase_info['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.purchaseInfoId){
        getDetail(routerQuery.purchaseInfoId);
    }
    if(detail.value == 'all'){
        if(routerQuery.purchaseInfoId){
            getDetailAll(routerQuery.purchaseInfoId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>