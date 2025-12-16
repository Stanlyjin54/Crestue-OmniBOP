<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">订单信息</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、订单编号、客户ID、下单用户ID、订单状态、订单总金额、利润金额、平台佣金、运费、下单时间、支付订单号。</span>
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
:to="{ path: '/order_info' }"
>
订单信息管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
订单信息详情
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
label="订单信息详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"totalAmount":[{"trigger":"blur","message":"订单总金额不能为空","required":true}],"orderNo":[{"trigger":"blur","message":"订单编号不能为空","required":true}],"customerInfoCustomerInfoId1":[{"trigger":"blur","message":"客户ID不能为空","required":true}],"orderStatusEnumOrderStatusEnumId1":[{"trigger":"blur","message":"订单状态不能为空","required":true}],"userInfoUserInfoId1":[{"trigger":"blur","message":"下单用户ID不能为空","required":true}],"orderDate":[{"trigger":"blur","message":"下单时间不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="订单编号" prop="orderNo">
        <el-input clearable placeholder="请输入订单编号" v-model="form.orderNo" />
    </el-form-item>

    <el-form-item  label="客户ID" prop="customerInfoCustomerInfoId1">
        <el-input clearable placeholder="请输入客户ID" v-model="form.customerInfoCustomerInfoId1" />
    </el-form-item>

    <el-form-item  label="下单用户ID" prop="userInfoUserInfoId1">
        <el-input clearable placeholder="请输入下单用户ID" v-model="form.userInfoUserInfoId1" />
    </el-form-item>

    <el-form-item  label="订单状态" prop="orderStatusEnumOrderStatusEnumId1">
        <el-input clearable placeholder="请输入订单状态" v-model="form.orderStatusEnumOrderStatusEnumId1" />
    </el-form-item>

    <el-form-item  label="订单总金额" prop="totalAmount">
        <el-input clearable placeholder="请输入订单总金额" v-model="form.totalAmount" />
    </el-form-item>

    <el-form-item  label="利润金额" prop="profitAmount">
        <el-input clearable placeholder="请输入利润金额" v-model="form.profitAmount" />
    </el-form-item>

    <el-form-item  label="平台佣金" prop="commission">
        <el-input clearable placeholder="请输入平台佣金" v-model="form.commission" />
    </el-form-item>

    <el-form-item  label="运费" prop="shippingFee">
        <el-input clearable placeholder="请输入运费" v-model="form.shippingFee" />
    </el-form-item>

    <el-form-item   label="下单时间" prop="orderDate">
        <el-date-picker placeholder="请输入下单时间" v-model="form.orderDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item   label="支付订单号" prop="generalOrders">
        <base-select clearable v-model="form.generalOrders" api="order_info_all.get_general_orders_list"></base-select>
    </el-form-item>

</el-form>
<base-cell
header="所属null"
label-width="180px"
class="m-b-10"
>
<base-cell-item
label="订单编号"
>
{{ formAll.orderNoGo }}
</base-cell-item>
<base-cell-item
label="用户ID"
>
{{ formAll.userId }}
</base-cell-item>
<base-cell-item
label="操作ID"
>
{{ formAll.opId }}
</base-cell-item>
<base-cell-item
label="订单类型"
>
{{ formAll.orderType }}
</base-cell-item>
<base-cell-item
label="订单状态"
>
{{ formAll.orderStatus }}
</base-cell-item>
<base-cell-item
label="支付状态"
>
{{ formAll.payStatus }}
</base-cell-item>
<base-cell-item
label="发货状态"
>
{{ formAll.deliveryStatus }}
</base-cell-item>
<base-cell-item
label="商品ID"
>
{{ formAll.productsId }}
</base-cell-item>
<base-cell-item
label="商品数量"
>
{{ formAll.quality }}
</base-cell-item>
<base-cell-item
label="订单总额"
>
{{ formAll.totalAmountGo }}
</base-cell-item>
<base-cell-item
label="优惠金额"
>
{{ formAll.discountAmount }}
</base-cell-item>
<base-cell-item
label="运费"
>
{{ formAll.shippingFeeGo }}
</base-cell-item>
<base-cell-item
label="实付金额"
>
{{ formAll.actualAmount }}
</base-cell-item>
<base-cell-item
label="支付渠道"
>
{{ formAll.paymentChannel }}
</base-cell-item>
<base-cell-item
label="支付订单号"
>
{{ formAll.paymentOrderId }}
</base-cell-item>
<base-cell-item
label="支付时间"
>
{{ formAll.paymentTime }}
</base-cell-item>
<base-cell-item
label="支付金额"
>
{{ formAll.paymentAmount }}
</base-cell-item>
<base-cell-item
label="配送方式"
>
{{ formAll.shippingMethod }}
</base-cell-item>
<base-cell-item
label="配送地址"
>
{{ formAll.shippingAddress }}
</base-cell-item>
<base-cell-item
label="物流公司"
>
{{ formAll.shippingCompany }}
</base-cell-item>
<base-cell-item
label="运单号"
>
{{ formAll.trackingNumber }}
</base-cell-item>
<base-cell-item
label="收件人电话"
>
{{ formAll.tel }}
</base-cell-item>
<base-cell-item
label="收件人"
>
{{ formAll.recipient }}
</base-cell-item>
<base-cell-item
label="创建时间"
>
{{ formAll.createTime }}
</base-cell-item>
<base-cell-item
label="更新时间"
>
{{ formAll.updateTime }}
</base-cell-item>
<base-cell-item
label="取消时间"
>
{{ formAll.cancelTime }}
</base-cell-item>
<base-cell-item
label="完成时间"
>
{{ formAll.completeTime }}
</base-cell-item>
<base-cell-item
label="发货时间"
>
{{ formAll.deliverTime }}
</base-cell-item>
<base-cell-item
label="支付系统退款单号"
>
{{ formAll.refundId }}
</base-cell-item>
<base-cell-item
label="业务系统"
>
{{ formAll.refundNo }}
</base-cell-item>
<base-cell-item
label="退款类型"
>
{{ formAll.refundType }}
</base-cell-item>
<base-cell-item
label="退款金额"
>
{{ formAll.refundAmount }}
</base-cell-item>
<base-cell-item
label="退款原因"
>
{{ formAll.refundReason }}
</base-cell-item>
<base-cell-item
label="退款状态"
>
{{ formAll.refundStatus }}
</base-cell-item>
<base-cell-item
label="备注"
>
{{ formAll.remark }}
</base-cell-item>
<base-cell-item
label="是否删除"
>
{{ formAll.isDeleted }}
</base-cell-item>
<base-cell-item
label="表名"
>
{{ formAll.tableName }}
</base-cell-item>
<base-cell-item
label="字段名"
>
{{ formAll.fieldName }}
</base-cell-item>
</base-cell>
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
let detail = ref('all')

function submitForm() {
  proxy.$refs.dataFormRef.validate(async (valid) => {
    if (valid) {
      let res = await proxy.$api.order_info['update'](form.value);
      proxy.$modal.msgSuccess(res.message);
      if(res.code == 0){
        refresh();
      }
    }
  });
}

async function getDetail(id) {
    let res = await proxy.$api.order_info['get'](id);
    if(res.code == 0){
        form.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

async function getDetailAll(id){
    let res = await proxy.$api.order_info_all['get'](id);
    if(res.code == 0){
        formAll.value = Object.assign({}, res.data);
    }
    else{
        proxy.$modal.msgError(res.message);
    }
}

function refresh(){
    if(routerQuery.orderInfoId){
        getDetail(routerQuery.orderInfoId);
    }
    if(detail.value == 'all'){
        if(routerQuery.orderInfoId){
            getDetailAll(routerQuery.orderInfoId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>