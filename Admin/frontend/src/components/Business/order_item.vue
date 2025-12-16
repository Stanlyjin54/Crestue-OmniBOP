<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="order_item.import"
table_name="order_item"
firstSearchData
>
<template #collapse>
    <el-form-item  label="订单ID" prop="orderInfoOrderInfoId1">
        <el-input clearable placeholder="请输入订单ID" v-model="listQuery.orderInfoOrderInfoId1" />
    </el-form-item>

    <el-form-item  label="商品ID" prop="productInfoProductInfoId1">
        <el-input clearable placeholder="请输入商品ID" v-model="listQuery.productInfoProductInfoId1" />
    </el-form-item>

    <el-form-item  label="数量" prop="quantity">
        <el-input clearable placeholder="请输入数量" v-model="listQuery.quantity" />
    </el-form-item>

    <el-form-item  label="单价" prop="unitPrice">
        <el-input clearable placeholder="请输入单价" v-model="listQuery.unitPrice" />
    </el-form-item>

    <el-form-item  label="总价" prop="totalPrice">
        <el-input clearable placeholder="请输入总价" v-model="listQuery.totalPrice" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="order_item.page"
>
<el-table-column
prop="orderItemId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="orderInfoOrderInfoId1"
label="订单ID"
align="center"
>

</el-table-column>
<el-table-column
prop="productInfoProductInfoId1"
label="商品ID"
align="center"
>

</el-table-column>
<el-table-column
prop="quantity"
label="数量"
align="center"
>

</el-table-column>
<el-table-column
prop="unitPrice"
label="单价"
align="center"
>

</el-table-column>
<el-table-column
prop="totalPrice"
label="总价"
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
path="/order_item/detail"
:query="{orderItemId: scope.row.orderItemId}"
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
:rules='{"unitPrice":[{"trigger":"blur","message":"单价不能为空","required":true}],"quantity":[{"trigger":"blur","message":"数量不能为空","required":true}],"totalPrice":[{"trigger":"blur","message":"总价不能为空","required":true}],"orderInfoOrderInfoId1":[{"trigger":"blur","message":"订单ID不能为空","required":true}],"productInfoProductInfoId1":[{"trigger":"blur","message":"商品ID不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item  label="订单ID" prop="orderInfoOrderInfoId1">
        <el-input clearable placeholder="请输入订单ID" v-model="form.orderInfoOrderInfoId1" />
    </el-form-item>

    <el-form-item  label="商品ID" prop="productInfoProductInfoId1">
        <el-input clearable placeholder="请输入商品ID" v-model="form.productInfoProductInfoId1" />
    </el-form-item>

    <el-form-item  label="数量" prop="quantity">
        <el-input clearable placeholder="请输入数量" v-model="form.quantity" />
    </el-form-item>

    <el-form-item  label="单价" prop="unitPrice">
        <el-input clearable placeholder="请输入单价" v-model="form.unitPrice" />
    </el-form-item>

    <el-form-item  label="总价" prop="totalPrice">
        <el-input clearable placeholder="请输入总价" v-model="form.totalPrice" />
    </el-form-item>

</el-form>
<base-cell
v-else
label-width="100px"
>
<base-cell-item
label="主键"
>
{{ form.orderItemId }}
</base-cell-item>
<base-cell-item
label="订单ID"
>
{{ form.orderInfoOrderInfoId1 }}
</base-cell-item>
<base-cell-item
label="商品ID"
>
{{ form.productInfoProductInfoId1 }}
</base-cell-item>
<base-cell-item
label="数量"
>
{{ form.quantity }}
</base-cell-item>
<base-cell-item
label="单价"
>
{{ form.unitPrice }}
</base-cell-item>
<base-cell-item
label="总价"
>
{{ form.totalPrice }}
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
  let res = await proxy.$api.order_item.delete(row.orderItemId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}
async function handleExport() {
  try {
    await proxy.$api.order_item.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.order_item.downloadExcel({
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
          let res = await proxy.$api.order_item[dialogStatus.value](form.value);
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