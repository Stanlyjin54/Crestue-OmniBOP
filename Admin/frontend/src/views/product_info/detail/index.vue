<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">商品信息</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、商品名称、条形码、商品分类、品牌、规格、单位、商品图片、序列号、保质期、安全库存、商品状态。</span>
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
:to="{ path: '/product_info' }"
>
商品信息管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
商品信息详情
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
label="商品信息详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"productStatusEnumProductStatusEnumId1":[{"trigger":"blur","message":"商品状态不能为空","required":true}],"productName":[{"trigger":"blur","message":"商品名称不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="商品名称" prop="productName">
        <el-input clearable placeholder="请输入商品名称" v-model="form.productName" />
    </el-form-item>

    <el-form-item  label="条形码" prop="barcode">
        <el-input clearable placeholder="请输入条形码" v-model="form.barcode" />
    </el-form-item>

    <el-form-item  label="商品分类" prop="category">
        <el-input clearable placeholder="请输入商品分类" v-model="form.category" />
    </el-form-item>

    <el-form-item  label="品牌" prop="brand">
        <el-input clearable placeholder="请输入品牌" v-model="form.brand" />
    </el-form-item>

    <el-form-item  label="规格" prop="specification">
        <el-input clearable placeholder="请输入规格" v-model="form.specification" />
    </el-form-item>

    <el-form-item  label="单位" prop="unit">
        <el-input clearable placeholder="请输入单位" v-model="form.unit" />
    </el-form-item>

    <el-form-item  label="商品图片" prop="imagesResourceKey">
        <image-upload v-model="form.images" :type="true" />
    </el-form-item>

    <el-form-item  label="序列号" prop="serialNumber">
        <el-input clearable placeholder="请输入序列号" v-model="form.serialNumber" />
    </el-form-item>

    <el-form-item   label="保质期" prop="shelfLife">
        <el-date-picker placeholder="请输入保质期" v-model="form.shelfLife" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item  label="安全库存" prop="minStock">
        <el-input clearable placeholder="请输入安全库存" v-model="form.minStock" />
    </el-form-item>

    <el-form-item  label="商品状态" prop="productStatusEnumProductStatusEnumId1">
        <el-input clearable placeholder="请输入商品状态" v-model="form.productStatusEnumProductStatusEnumId1" />
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
      let res = await proxy.$api.product_info['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.product_info['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.product_info['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.productInfoId){
        getDetail(routerQuery.productInfoId);
    }
    if(detail.value == 'all'){
        if(routerQuery.productInfoId){
            getDetailAll(routerQuery.productInfoId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>