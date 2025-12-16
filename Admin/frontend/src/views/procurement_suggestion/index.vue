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
</el-breadcrumb>
<procurement_suggestion
 />
</base-wrapper>
   
</template>

<script setup>

</script>

<style lang="scss" scoped>
</style>