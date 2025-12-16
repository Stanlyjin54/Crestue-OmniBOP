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
uploadExcelAPI="transfer_info.import"
table_name="transfer_info"
firstSearchData
>
<template #collapse>
    <el-form-item  label="调出仓库ID" prop="fromWarehouseInfoWarehouseInfoId1">
        <el-input clearable placeholder="请输入调出仓库ID" v-model="listQuery.fromWarehouseInfoWarehouseInfoId1" />
    </el-form-item>

    <el-form-item  label="调入仓库ID" prop="toWarehouseInfoWarehouseInfoId2">
        <el-input clearable placeholder="请输入调入仓库ID" v-model="listQuery.toWarehouseInfoWarehouseInfoId2" />
    </el-form-item>

    <el-form-item  label="商品ID" prop="productInfoProductInfoId1">
        <el-input clearable placeholder="请输入商品ID" v-model="listQuery.productInfoProductInfoId1" />
    </el-form-item>

    <el-form-item  label="调拨数量" prop="quantity">
        <el-input clearable placeholder="请输入调拨数量" v-model="listQuery.quantity" />
    </el-form-item>

    <el-form-item  label="调拨状态" prop="transferStatusEnumTransferStatusEnumId1">
        <el-input clearable placeholder="请输入调拨状态" v-model="listQuery.transferStatusEnumTransferStatusEnumId1" />
    </el-form-item>

    <el-form-item  label="申请人ID" prop="applyUserInfoUserInfoId1">
        <el-input clearable placeholder="请输入申请人ID" v-model="listQuery.applyUserInfoUserInfoId1" />
    </el-form-item>

    <el-form-item  label="确认人ID" prop="confirmUserInfoUserInfoId2">
        <el-input clearable placeholder="请输入确认人ID" v-model="listQuery.confirmUserInfoUserInfoId2" />
    </el-form-item>

    <el-form-item   label="申请时间" prop="applyDate">
        <el-date-picker placeholder="请输入申请时间" v-model="listQuery.applyDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item   label="确认时间" prop="confirmDate">
        <el-date-picker placeholder="请输入确认时间" v-model="listQuery.confirmDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="transfer_info.page"
>
<el-table-column
prop="transferInfoId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="fromWarehouseInfoWarehouseInfoId1"
label="调出仓库ID"
align="center"
>

</el-table-column>
<el-table-column
prop="toWarehouseInfoWarehouseInfoId2"
label="调入仓库ID"
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
label="调拨数量"
align="center"
>

</el-table-column>
<el-table-column
prop="transferStatusEnumTransferStatusEnumId1"
label="调拨状态"
align="center"
>

</el-table-column>
<el-table-column
prop="applyUserInfoUserInfoId1"
label="申请人ID"
align="center"
>

</el-table-column>
<el-table-column
prop="confirmUserInfoUserInfoId2"
label="确认人ID"
align="center"
>

</el-table-column>
<el-table-column
prop="applyDate"
label="申请时间"
align="center"
>
<template #default="scope">
{{ parseTime(scope.row.applyDate,'{y}-{m}-{d} {h}:{i}:{s}') }}
</template>
</el-table-column>
<el-table-column
prop="confirmDate"
label="确认时间"
align="center"
>
<template #default="scope">
{{ parseTime(scope.row.confirmDate,'{y}-{m}-{d} {h}:{i}:{s}') }}
</template>
</el-table-column>
<el-table-column
width="220"
fixed="right"
label="操作"
align="center"
>
<template #default="scope">
<base-info-btn
path="/transfer_info/detail"
:query="{transferInfoId: scope.row.transferInfoId}"
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
:rules='{"fromWarehouseInfoWarehouseInfoId1":[{"trigger":"blur","message":"调出仓库ID不能为空","required":true}],"quantity":[{"trigger":"blur","message":"调拨数量不能为空","required":true}],"applyUserInfoUserInfoId1":[{"trigger":"blur","message":"申请人ID不能为空","required":true}],"productInfoProductInfoId1":[{"trigger":"blur","message":"商品ID不能为空","required":true}],"transferStatusEnumTransferStatusEnumId1":[{"trigger":"blur","message":"调拨状态不能为空","required":true}],"applyDate":[{"trigger":"blur","message":"申请时间不能为空","required":true}],"toWarehouseInfoWarehouseInfoId2":[{"trigger":"blur","message":"调入仓库ID不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item  label="调出仓库ID" prop="fromWarehouseInfoWarehouseInfoId1">
        <el-input clearable placeholder="请输入调出仓库ID" v-model="form.fromWarehouseInfoWarehouseInfoId1" />
    </el-form-item>

    <el-form-item  label="调入仓库ID" prop="toWarehouseInfoWarehouseInfoId2">
        <el-input clearable placeholder="请输入调入仓库ID" v-model="form.toWarehouseInfoWarehouseInfoId2" />
    </el-form-item>

    <el-form-item  label="商品ID" prop="productInfoProductInfoId1">
        <el-input clearable placeholder="请输入商品ID" v-model="form.productInfoProductInfoId1" />
    </el-form-item>

    <el-form-item  label="调拨数量" prop="quantity">
        <el-input clearable placeholder="请输入调拨数量" v-model="form.quantity" />
    </el-form-item>

    <el-form-item  label="调拨状态" prop="transferStatusEnumTransferStatusEnumId1">
        <el-input clearable placeholder="请输入调拨状态" v-model="form.transferStatusEnumTransferStatusEnumId1" />
    </el-form-item>

    <el-form-item  label="申请人ID" prop="applyUserInfoUserInfoId1">
        <el-input clearable placeholder="请输入申请人ID" v-model="form.applyUserInfoUserInfoId1" />
    </el-form-item>

    <el-form-item  label="确认人ID" prop="confirmUserInfoUserInfoId2">
        <el-input clearable placeholder="请输入确认人ID" v-model="form.confirmUserInfoUserInfoId2" />
    </el-form-item>

    <el-form-item   label="申请时间" prop="applyDate">
        <el-date-picker placeholder="请输入申请时间" v-model="form.applyDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item   label="确认时间" prop="confirmDate">
        <el-date-picker placeholder="请输入确认时间" v-model="form.confirmDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

</el-form>
<base-cell
v-else
label-width="100px"
>
<base-cell-item
label="主键"
>
{{ form.transferInfoId }}
</base-cell-item>
<base-cell-item
label="调出仓库ID"
>
{{ form.fromWarehouseInfoWarehouseInfoId1 }}
</base-cell-item>
<base-cell-item
label="调入仓库ID"
>
{{ form.toWarehouseInfoWarehouseInfoId2 }}
</base-cell-item>
<base-cell-item
label="商品ID"
>
{{ form.productInfoProductInfoId1 }}
</base-cell-item>
<base-cell-item
label="调拨数量"
>
{{ form.quantity }}
</base-cell-item>
<base-cell-item
label="调拨状态"
>
{{ form.transferStatusEnumTransferStatusEnumId1 }}
</base-cell-item>
<base-cell-item
label="申请人ID"
>
{{ form.applyUserInfoUserInfoId1 }}
</base-cell-item>
<base-cell-item
label="确认人ID"
>
{{ form.confirmUserInfoUserInfoId2 }}
</base-cell-item>
<base-cell-item
label="申请时间"
>
{{ form.applyDate }}
</base-cell-item>
<base-cell-item
label="确认时间"
>
{{ form.confirmDate }}
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
  let res = await proxy.$api.transfer_info.delete(row.transferInfoId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}
async function handleExport() {
  try {
    await proxy.$api.transfer_info.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.transfer_info.downloadExcel({
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
          let res = await proxy.$api.transfer_info[dialogStatus.value](form.value);
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