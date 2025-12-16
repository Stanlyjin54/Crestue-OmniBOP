<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="订单编号"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="order_info.import"
table_name="order_info"
firstSearchData="orderNo"
>
<template #collapse>
    <el-form-item  label="订单编号" prop="orderNo">
        <el-input clearable placeholder="请输入订单编号" v-model="listQuery.orderNo" />
    </el-form-item>

    <el-form-item  label="客户ID" prop="customerInfoCustomerInfoId1">
        <el-input clearable placeholder="请输入客户ID" v-model="listQuery.customerInfoCustomerInfoId1" />
    </el-form-item>

    <el-form-item  label="下单用户ID" prop="userInfoUserInfoId1">
        <el-input clearable placeholder="请输入下单用户ID" v-model="listQuery.userInfoUserInfoId1" />
    </el-form-item>

    <el-form-item  label="订单状态" prop="orderStatusEnumOrderStatusEnumId1">
        <el-input clearable placeholder="请输入订单状态" v-model="listQuery.orderStatusEnumOrderStatusEnumId1" />
    </el-form-item>

    <el-form-item  label="订单总金额" prop="totalAmount">
        <el-input clearable placeholder="请输入订单总金额" v-model="listQuery.totalAmount" />
    </el-form-item>

    <el-form-item  label="利润金额" prop="profitAmount">
        <el-input clearable placeholder="请输入利润金额" v-model="listQuery.profitAmount" />
    </el-form-item>

    <el-form-item  label="平台佣金" prop="commission">
        <el-input clearable placeholder="请输入平台佣金" v-model="listQuery.commission" />
    </el-form-item>

    <el-form-item  label="运费" prop="shippingFee">
        <el-input clearable placeholder="请输入运费" v-model="listQuery.shippingFee" />
    </el-form-item>

    <el-form-item   label="下单时间" prop="orderDate">
        <el-date-picker placeholder="请输入下单时间" v-model="listQuery.orderDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item v-show="!props.params.generalOrders"  label="支付订单号" prop="generalOrders">
        <base-select clearable v-model="listQuery.generalOrders" api="order_info_all.get_general_orders_list"></base-select>
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="order_info_all.page"
>
<el-table-column
prop="orderInfoId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="orderNo"
label="订单编号"
align="center"
>

</el-table-column>
<el-table-column
prop="customerInfoCustomerInfoId1"
label="客户ID"
align="center"
>

</el-table-column>
<el-table-column
prop="userInfoUserInfoId1"
label="下单用户ID"
align="center"
>

</el-table-column>
<el-table-column
prop="orderStatusEnumOrderStatusEnumId1"
label="订单状态"
align="center"
>

</el-table-column>
<el-table-column
prop="totalAmount"
label="订单总金额"
align="center"
>

</el-table-column>
<el-table-column
prop="profitAmount"
label="利润金额"
align="center"
>

</el-table-column>
<el-table-column
prop="commission"
label="平台佣金"
align="center"
>

</el-table-column>
<el-table-column
prop="shippingFee"
label="运费"
align="center"
>

</el-table-column>
<el-table-column
prop="orderDate"
label="下单时间"
align="center"
>
<template #default="scope">
{{ parseTime(scope.row.orderDate,'{y}-{m}-{d} {h}:{i}:{s}') }}
</template>
</el-table-column>
<el-table-column
prop="paymentOrderId"
label="支付订单号"
align="center"
>

</el-table-column>
<el-table-column
width="220"
fixed="right"
label="操作"
align="center"
>
<template #default="scope">
<base-info-btn
path="/order_info/detail"
:query="{orderInfoId: scope.row.orderInfoId}"
text="详情"
>
</base-info-btn>
<base-edit-btn
@ok="handleUpdate(scope.row)"
>
</base-edit-btn>
<base-delete-btn
@ok="handleDelete(scope.row)"
></base-delete-btn>
</template>
</el-table-column>
</base-table-p>
<base-dialog
v-if="dialogVisible"
:title="dialogTitleObj[dialogStatus]"
width="50%"
style="max-width: 600px;"
v-model="dialogVisible"
>
<el-form
ref='dataFormRef'
v-if='dialogStatus !== "detail"'
:model='form'
label-position='top'
:rules='{"totalAmount":[{"trigger":"blur","message":"订单总金额不能为空","required":true}],"orderNo":[{"trigger":"blur","message":"订单编号不能为空","required":true}],"customerInfoCustomerInfoId1":[{"trigger":"blur","message":"客户ID不能为空","required":true}],"orderStatusEnumOrderStatusEnumId1":[{"trigger":"blur","message":"订单状态不能为空","required":true}],"userInfoUserInfoId1":[{"trigger":"blur","message":"下单用户ID不能为空","required":true}],"orderDate":[{"trigger":"blur","message":"下单时间不能为空","required":true}]}'
label-width='100px'
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

    <el-form-item v-show="!props.params.generalOrders"  label="支付订单号" prop="generalOrders">
        <base-select clearable v-model="form.generalOrders" api="order_info_all.get_general_orders_list"></base-select>
    </el-form-item>

</el-form>
<base-cell
v-else
label-width="100px"
>
<base-cell-item
label="主键"
>
{{ form.orderInfoId }}
</base-cell-item>
<base-cell-item
label="订单编号"
>
{{ form.orderNo }}
</base-cell-item>
<base-cell-item
label="客户ID"
>
{{ form.customerInfoCustomerInfoId1 }}
</base-cell-item>
<base-cell-item
label="下单用户ID"
>
{{ form.userInfoUserInfoId1 }}
</base-cell-item>
<base-cell-item
label="订单状态"
>
{{ form.orderStatusEnumOrderStatusEnumId1 }}
</base-cell-item>
<base-cell-item
label="订单总金额"
>
{{ form.totalAmount }}
</base-cell-item>
<base-cell-item
label="利润金额"
>
{{ form.profitAmount }}
</base-cell-item>
<base-cell-item
label="平台佣金"
>
{{ form.commission }}
</base-cell-item>
<base-cell-item
label="运费"
>
{{ form.shippingFee }}
</base-cell-item>
<base-cell-item
label="下单时间"
>
{{ form.orderDate }}
</base-cell-item>
<base-cell-item
label="支付订单号"
>
{{ form.generalOrders }}
</base-cell-item>
</base-cell>
<template #footer>
<el-button
@click="dialogVisible = false"
round
>取 消</el-button>
<el-button
@click="submitForm"
round
color="#5D5FEF"
type="primary"
>确 定</el-button>
</template>
</base-dialog>
</base-wrapper>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const props = defineProps({
    params: { type: Object, default: () => ({}) },
})

// 计算属性，用于判断 params 的长度
const paramsLength = computed(() =>  Object.keys(props.params).length)

// 计算属性，判断 params 是否为空
const isParamsEmpty = computed(() => paramsLength.value === 0)

let listQuery = ref({});
let form = ref({});
let dialogVisible = ref(false);
let dialogStatus = ref('');
let dialogTitleObj = ref({ update: '编辑', add: '添加', detail: '详情'});
let rules = ref({});
if(isParamsEmpty.value){
  let routerQuery = proxy.$route.query;
  Object.assign(listQuery.value, routerQuery);
}
else{
  Object.assign(listQuery.value, props.params);
}


function refreshTableData() {
  proxy.$refs.baseTableRef.refresh();
}
function handleDetail(row) {
  form.value = Object.assign({}, row);
  dialogStatus.value = 'detail';
  dialogVisible.value = true;
}
function handleAdd() {
  form.value = { ...props.params };
  dialogStatus.value = 'add';
  dialogVisible.value = true;
}
function handleUpdate(row) {
  form.value = Object.assign({}, row);
  dialogStatus.value = 'update';
  dialogVisible.value = true;
}
async function handleDelete(row) {
  let res = await proxy.$api.order_info.delete(row.orderInfoId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}
async function handleExport() {
  try {
    await proxy.$api.order_info_all.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.order_info.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "downloadTemplate", "get")
  }catch (error) {
    console.error("导出失败", error)
  }
}
function submitForm() {
  if(dialogStatus.value == 'detail'){
      dialogVisible.value = false;
  }
  else {
      proxy.$refs.dataFormRef.validate(async (valid) => {
        if (valid) {
          let res = await proxy.$api.order_info[dialogStatus.value](form.value);
          proxy.$modal.msgSuccess(res.message);
          refreshTableData();
          dialogVisible.value = false;
        }
      });
  }
}

function choose(item){
    form.value = Object.assign({}, item);
    dialogVisible.value = true;
    dialogStatus.value = 'update';
}
</script>

<style lang="scss" scoped>
</style>