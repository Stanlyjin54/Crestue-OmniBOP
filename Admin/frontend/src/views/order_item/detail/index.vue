<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">订单商品明细</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、订单ID、商品ID、数量、单价、总价。</span>
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
:to="{ path: '/order_item' }"
>
订单商品明细管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
订单商品明细详情
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
label="订单商品明细详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"unitPrice":[{"trigger":"blur","message":"单价不能为空","required":true}],"quantity":[{"trigger":"blur","message":"数量不能为空","required":true}],"totalPrice":[{"trigger":"blur","message":"总价不能为空","required":true}],"orderInfoOrderInfoId1":[{"trigger":"blur","message":"订单ID不能为空","required":true}],"productInfoProductInfoId1":[{"trigger":"blur","message":"商品ID不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="订单ID" prop="orderInfoOrderInfoId1">
        <el-input clearable placeholder="请输入订单ID" v-model="form.orderInfoOrderInfoId1" />
    </el-form-item>

    <el-form-item  label="商品ID" prop="productInfoProductInfoId1">
        <el-input clearable placeholder="请输入商品ID" v-model="form.productInfoProductInfoId1" />
    </el-form-item>

    <el-form-item  label="数量" prop="quantity">
        <el-input clearable placeholder="请输入数量" v-model="form.quantity" />
    </el-form-item>

    <el-form-item  label="单价" prop="unitPrice">
        <el-input clearable placeholder="请输入单价" v-model="form.unitPrice" />
    </el-form-item>

    <el-form-item  label="总价" prop="totalPrice">
        <el-input clearable placeholder="请输入总价" v-model="form.totalPrice" />
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
      let res = await proxy.$api.order_item['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.order_item['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.order_item['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.orderItemId){
        getDetail(routerQuery.orderItemId);
    }
    if(detail.value == 'all'){
        if(routerQuery.orderItemId){
            getDetailAll(routerQuery.orderItemId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>