<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">客户信息</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、客户名称、手机号、邮箱、地址、RFM分层、客户标签、微信号、加密微信数据、客户来源。</span>
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
:to="{ path: '/customer_info' }"
>
客户信息管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
客户信息详情
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
label="客户信息详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"customerName":[{"trigger":"blur","message":"客户名称不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="客户名称" prop="customerName">
        <el-input clearable placeholder="请输入客户名称" v-model="form.customerName" />
    </el-form-item>

    <el-form-item  label="手机号" prop="phone">
        <el-input clearable placeholder="请输入手机号" v-model="form.phone" />
    </el-form-item>

    <el-form-item  label="邮箱" prop="email">
        <el-input clearable placeholder="请输入邮箱" v-model="form.email" />
    </el-form-item>

    <el-form-item  label="地址" prop="address">
        <el-input clearable placeholder="请输入地址" v-model="form.address" />
    </el-form-item>

    <el-form-item  label="RFM分层" prop="rfmLevelEnumRfmLevelEnumId1">
        <el-input clearable placeholder="请输入RFM分层" v-model="form.rfmLevelEnumRfmLevelEnumId1" />
    </el-form-item>

    <el-form-item  label="客户标签" prop="customerTagEnumCustomerTagEnumId1">
        <el-input clearable placeholder="请输入客户标签" v-model="form.customerTagEnumCustomerTagEnumId1" />
    </el-form-item>

    <el-form-item  label="微信号" prop="wechatId">
        <el-input clearable placeholder="请输入微信号" v-model="form.wechatId" />
    </el-form-item>

    <el-form-item  label="加密微信数据" prop="wechatDataEncrypted">
        <el-input clearable placeholder="请输入加密微信数据" v-model="form.wechatDataEncrypted" />
    </el-form-item>

    <el-form-item  label="客户来源" prop="source">
        <el-input clearable placeholder="请输入客户来源" v-model="form.source" />
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
      let res = await proxy.$api.customer_info['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.customer_info['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.customer_info['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.customerInfoId){
        getDetail(routerQuery.customerInfoId);
    }
    if(detail.value == 'all'){
        if(routerQuery.customerInfoId){
            getDetailAll(routerQuery.customerInfoId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>