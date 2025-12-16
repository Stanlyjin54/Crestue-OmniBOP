<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">库存记录</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、商品ID、仓库ID、库存数量、批次号、效期、序列号。</span>
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
:to="{ path: '/inventory_record' }"
>
库存记录管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
库存记录详情
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
label="库存记录详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"warehouseInfoWarehouseInfoId1":[{"trigger":"blur","message":"仓库ID不能为空","required":true}],"quantity":[{"trigger":"blur","message":"库存数量不能为空","required":true}],"productInfoProductInfoId1":[{"trigger":"blur","message":"商品ID不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="商品ID" prop="productInfoProductInfoId1">
        <el-input clearable placeholder="请输入商品ID" v-model="form.productInfoProductInfoId1" />
    </el-form-item>

    <el-form-item  label="仓库ID" prop="warehouseInfoWarehouseInfoId1">
        <el-input clearable placeholder="请输入仓库ID" v-model="form.warehouseInfoWarehouseInfoId1" />
    </el-form-item>

    <el-form-item  label="库存数量" prop="quantity">
        <el-input clearable placeholder="请输入库存数量" v-model="form.quantity" />
    </el-form-item>

    <el-form-item  label="批次号" prop="batchNumber">
        <el-input clearable placeholder="请输入批次号" v-model="form.batchNumber" />
    </el-form-item>

    <el-form-item   label="效期" prop="expireDate">
        <el-date-picker placeholder="请输入效期" v-model="form.expireDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item  label="序列号" prop="serialNumber">
        <el-input clearable placeholder="请输入序列号" v-model="form.serialNumber" />
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
      let res = await proxy.$api.inventory_record['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.inventory_record['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.inventory_record['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.inventoryRecordId){
        getDetail(routerQuery.inventoryRecordId);
    }
    if(detail.value == 'all'){
        if(routerQuery.inventoryRecordId){
            getDetailAll(routerQuery.inventoryRecordId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>