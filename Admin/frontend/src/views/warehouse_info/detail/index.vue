<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">仓库信息</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、仓库名称、仓库位置。</span>
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
:to="{ path: '/warehouse_info' }"
>
仓库信息管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
仓库信息详情
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
label="仓库信息详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"warehouseName":[{"trigger":"blur","message":"仓库名称不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="仓库名称" prop="warehouseName">
        <el-input clearable placeholder="请输入仓库名称" v-model="form.warehouseName" />
    </el-form-item>

    <el-form-item  label="仓库位置" prop="location">
        <el-input clearable placeholder="请输入仓库位置" v-model="form.location" />
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
      let res = await proxy.$api.warehouse_info['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.warehouse_info['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.warehouse_info['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.warehouseInfoId){
        getDetail(routerQuery.warehouseInfoId);
    }
    if(detail.value == 'all'){
        if(routerQuery.warehouseInfoId){
            getDetailAll(routerQuery.warehouseInfoId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>