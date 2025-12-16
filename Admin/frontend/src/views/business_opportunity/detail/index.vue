<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">商机线索</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、客户ID、商机名称、商机状态、AI跟进建议。</span>
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
:to="{ path: '/business_opportunity' }"
>
商机线索管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
商机线索详情
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
label="商机线索详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"opportunityName":[{"trigger":"blur","message":"商机名称不能为空","required":true}],"customerInfoCustomerInfoId1":[{"trigger":"blur","message":"客户ID不能为空","required":true}],"status":[{"trigger":"blur","message":"商机状态不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="客户ID" prop="customerInfoCustomerInfoId1">
        <el-input clearable placeholder="请输入客户ID" v-model="form.customerInfoCustomerInfoId1" />
    </el-form-item>

    <el-form-item  label="商机名称" prop="opportunityName">
        <el-input clearable placeholder="请输入商机名称" v-model="form.opportunityName" />
    </el-form-item>

    <el-form-item  label="商机状态" prop="status">
        <el-input clearable placeholder="请输入商机状态" v-model="form.status" />
    </el-form-item>

    <el-form-item  label="AI跟进建议" prop="followUpSuggestion">
        <el-input clearable placeholder="请输入AI跟进建议" v-model="form.followUpSuggestion" />
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
      let res = await proxy.$api.business_opportunity['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.business_opportunity['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.business_opportunity['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.businessOpportunityId){
        getDetail(routerQuery.businessOpportunityId);
    }
    if(detail.value == 'all'){
        if(routerQuery.businessOpportunityId){
            getDetailAll(routerQuery.businessOpportunityId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>