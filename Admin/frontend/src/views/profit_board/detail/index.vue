<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">利润看板</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、维度类型、维度ID、毛利金额、平台佣金、运费、统计日期。</span>
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
:to="{ path: '/profit_board' }"
>
利润看板管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
利润看板详情
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
label="利润看板详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"statDate":[{"trigger":"blur","message":"统计日期不能为空","required":true}],"targetId":[{"trigger":"blur","message":"维度ID不能为空","required":true}],"profitAmount":[{"trigger":"blur","message":"毛利金额不能为空","required":true}],"targetType":[{"trigger":"blur","message":"维度类型不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="维度类型" prop="targetType">
        <el-input clearable placeholder="请输入维度类型" v-model="form.targetType" />
    </el-form-item>

    <el-form-item  label="维度ID" prop="targetId">
        <el-input clearable placeholder="请输入维度ID" v-model="form.targetId" />
    </el-form-item>

    <el-form-item  label="毛利金额" prop="profitAmount">
        <el-input clearable placeholder="请输入毛利金额" v-model="form.profitAmount" />
    </el-form-item>

    <el-form-item  label="平台佣金" prop="commission">
        <el-input clearable placeholder="请输入平台佣金" v-model="form.commission" />
    </el-form-item>

    <el-form-item  label="运费" prop="shippingFee">
        <el-input clearable placeholder="请输入运费" v-model="form.shippingFee" />
    </el-form-item>

    <el-form-item   label="统计日期" prop="statDate">
        <el-date-picker placeholder="请输入统计日期" v-model="form.statDate" type="datetime"
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
      let res = await proxy.$api.profit_board['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.profit_board['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.profit_board['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.profitBoardId){
        getDetail(routerQuery.profitBoardId);
    }
    if(detail.value == 'all'){
        if(routerQuery.profitBoardId){
            getDetailAll(routerQuery.profitBoardId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>