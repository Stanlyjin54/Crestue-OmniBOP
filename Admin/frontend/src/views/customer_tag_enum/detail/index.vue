<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">客户标签枚举</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、标签名称、是否AI生成。</span>
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
:to="{ path: '/customer_tag_enum' }"
>
客户标签枚举管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
客户标签枚举详情
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
label="客户标签枚举详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"tagName":[{"trigger":"blur","message":"标签名称不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="标签名称" prop="tagName">
        <el-input clearable placeholder="请输入标签名称" v-model="form.tagName" />
    </el-form-item>

    <el-form-item  label="是否AI生成" prop="isAiGenerated">
        <el-input clearable placeholder="请输入是否AI生成" v-model="form.isAiGenerated" />
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
      let res = await proxy.$api.customer_tag_enum['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.customer_tag_enum['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.customer_tag_enum['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.customerTagEnumId){
        getDetail(routerQuery.customerTagEnumId);
    }
    if(detail.value == 'all'){
        if(routerQuery.customerTagEnumId){
            getDetailAll(routerQuery.customerTagEnumId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>