<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">采购建议</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、商品ID、建议采购数量、建议原因、建议日期。</span>
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
:to="{ path: '/procurement_suggestion' }"
>
采购建议管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
采购建议详情
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
label="采购建议详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"suggestQuantity":[{"trigger":"blur","message":"建议采购数量不能为空","required":true}],"suggestDate":[{"trigger":"blur","message":"建议日期不能为空","required":true}],"productInfoProductInfoId1":[{"trigger":"blur","message":"商品ID不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="商品ID" prop="productInfoProductInfoId1">
        <el-input clearable placeholder="请输入商品ID" v-model="form.productInfoProductInfoId1" />
    </el-form-item>

    <el-form-item  label="建议采购数量" prop="suggestQuantity">
        <el-input clearable placeholder="请输入建议采购数量" v-model="form.suggestQuantity" />
    </el-form-item>

    <el-form-item  label="建议原因" prop="reason">
        <el-input clearable placeholder="请输入建议原因" v-model="form.reason" />
    </el-form-item>

    <el-form-item   label="建议日期" prop="suggestDate">
        <el-date-picker placeholder="请输入建议日期" v-model="form.suggestDate" type="datetime"
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
      let res = await proxy.$api.procurement_suggestion['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.procurement_suggestion['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.procurement_suggestion['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.procurementSuggestionId){
        getDetail(routerQuery.procurementSuggestionId);
    }
    if(detail.value == 'all'){
        if(routerQuery.procurementSuggestionId){
            getDetailAll(routerQuery.procurementSuggestionId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>