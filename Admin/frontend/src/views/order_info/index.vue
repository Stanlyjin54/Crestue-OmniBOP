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
</el-breadcrumb>
<order_info
 />
</base-wrapper>
   
</template>

<script setup>

</script>

<style lang="scss" scoped>
</style>