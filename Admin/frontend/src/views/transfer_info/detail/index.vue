<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">调拨信息</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、调出仓库ID、调入仓库ID、商品ID、调拨数量、调拨状态、申请人ID、确认人ID、申请时间、确认时间。</span>
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
:to="{ path: '/transfer_info' }"
>
调拨信息管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
调拨信息详情
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
label="调拨信息详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"fromWarehouseInfoWarehouseInfoId1":[{"trigger":"blur","message":"调出仓库ID不能为空","required":true}],"quantity":[{"trigger":"blur","message":"调拨数量不能为空","required":true}],"applyUserInfoUserInfoId1":[{"trigger":"blur","message":"申请人ID不能为空","required":true}],"productInfoProductInfoId1":[{"trigger":"blur","message":"商品ID不能为空","required":true}],"transferStatusEnumTransferStatusEnumId1":[{"trigger":"blur","message":"调拨状态不能为空","required":true}],"applyDate":[{"trigger":"blur","message":"申请时间不能为空","required":true}],"toWarehouseInfoWarehouseInfoId2":[{"trigger":"blur","message":"调入仓库ID不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="调出仓库ID" prop="fromWarehouseInfoWarehouseInfoId1">
        <el-input clearable placeholder="请输入调出仓库ID" v-model="form.fromWarehouseInfoWarehouseInfoId1" />
    </el-form-item>

    <el-form-item  label="调入仓库ID" prop="toWarehouseInfoWarehouseInfoId2">
        <el-input clearable placeholder="请输入调入仓库ID" v-model="form.toWarehouseInfoWarehouseInfoId2" />
    </el-form-item>

    <el-form-item  label="商品ID" prop="productInfoProductInfoId1">
        <el-input clearable placeholder="请输入商品ID" v-model="form.productInfoProductInfoId1" />
    </el-form-item>

    <el-form-item  label="调拨数量" prop="quantity">
        <el-input clearable placeholder="请输入调拨数量" v-model="form.quantity" />
    </el-form-item>

    <el-form-item  label="调拨状态" prop="transferStatusEnumTransferStatusEnumId1">
        <el-input clearable placeholder="请输入调拨状态" v-model="form.transferStatusEnumTransferStatusEnumId1" />
    </el-form-item>

    <el-form-item  label="申请人ID" prop="applyUserInfoUserInfoId1">
        <el-input clearable placeholder="请输入申请人ID" v-model="form.applyUserInfoUserInfoId1" />
    </el-form-item>

    <el-form-item  label="确认人ID" prop="confirmUserInfoUserInfoId2">
        <el-input clearable placeholder="请输入确认人ID" v-model="form.confirmUserInfoUserInfoId2" />
    </el-form-item>

    <el-form-item   label="申请时间" prop="applyDate">
        <el-date-picker placeholder="请输入申请时间" v-model="form.applyDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item   label="确认时间" prop="confirmDate">
        <el-date-picker placeholder="请输入确认时间" v-model="form.confirmDate" type="datetime"
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
      let res = await proxy.$api.transfer_info['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.transfer_info['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.transfer_info['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.transferInfoId){
        getDetail(routerQuery.transferInfoId);
    }
    if(detail.value == 'all'){
        if(routerQuery.transferInfoId){
            getDetailAll(routerQuery.transferInfoId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>