<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="采购单号"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="purchase_info.import"
table_name="purchase_info"
firstSearchData="purchaseNo"
>
<template #collapse>
    <el-form-item  label="采购单号" prop="purchaseNo">
        <el-input clearable placeholder="请输入采购单号" v-model="listQuery.purchaseNo" />
    </el-form-item>

    <el-form-item  label="采购员ID" prop="userInfoUserInfoId1">
        <el-input clearable placeholder="请输入采购员ID" v-model="listQuery.userInfoUserInfoId1" />
    </el-form-item>

    <el-form-item  label="供应商" prop="supplier">
        <el-input clearable placeholder="请输入供应商" v-model="listQuery.supplier" />
    </el-form-item>

    <el-form-item  label="采购总金额" prop="totalAmount">
        <el-input clearable placeholder="请输入采购总金额" v-model="listQuery.totalAmount" />
    </el-form-item>

    <el-form-item   label="采购时间" prop="purchaseDate">
        <el-date-picker placeholder="请输入采购时间" v-model="listQuery.purchaseDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item  label="采购状态" prop="purchaseStatusEnumPurchaseStatusEnumId1">
        <el-input clearable placeholder="请输入采购状态" v-model="listQuery.purchaseStatusEnumPurchaseStatusEnumId1" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="purchase_info.page"
>
<el-table-column
prop="purchaseInfoId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="purchaseNo"
label="采购单号"
align="center"
>

</el-table-column>
<el-table-column
prop="userInfoUserInfoId1"
label="采购员ID"
align="center"
>

</el-table-column>
<el-table-column
prop="supplier"
label="供应商"
align="center"
>

</el-table-column>
<el-table-column
prop="totalAmount"
label="采购总金额"
align="center"
>

</el-table-column>
<el-table-column
prop="purchaseDate"
label="采购时间"
align="center"
>
<template #default="scope">
{{ parseTime(scope.row.purchaseDate,'{y}-{m}-{d} {h}:{i}:{s}') }}
</template>
</el-table-column>
<el-table-column
prop="purchaseStatusEnumPurchaseStatusEnumId1"
label="采购状态"
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
path="/purchase_info/detail"
:query="{purchaseInfoId: scope.row.purchaseInfoId}"
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
:rules='{"totalAmount":[{"trigger":"blur","message":"采购总金额不能为空","required":true}],"purchaseDate":[{"trigger":"blur","message":"采购时间不能为空","required":true}],"purchaseStatusEnumPurchaseStatusEnumId1":[{"trigger":"blur","message":"采购状态不能为空","required":true}],"purchaseNo":[{"trigger":"blur","message":"采购单号不能为空","required":true}],"userInfoUserInfoId1":[{"trigger":"blur","message":"采购员ID不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item  label="采购单号" prop="purchaseNo">
        <el-input clearable placeholder="请输入采购单号" v-model="form.purchaseNo" />
    </el-form-item>

    <el-form-item  label="采购员ID" prop="userInfoUserInfoId1">
        <el-input clearable placeholder="请输入采购员ID" v-model="form.userInfoUserInfoId1" />
    </el-form-item>

    <el-form-item  label="供应商" prop="supplier">
        <el-input clearable placeholder="请输入供应商" v-model="form.supplier" />
    </el-form-item>

    <el-form-item  label="采购总金额" prop="totalAmount">
        <el-input clearable placeholder="请输入采购总金额" v-model="form.totalAmount" />
    </el-form-item>

    <el-form-item   label="采购时间" prop="purchaseDate">
        <el-date-picker placeholder="请输入采购时间" v-model="form.purchaseDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item  label="采购状态" prop="purchaseStatusEnumPurchaseStatusEnumId1">
        <el-input clearable placeholder="请输入采购状态" v-model="form.purchaseStatusEnumPurchaseStatusEnumId1" />
    </el-form-item>

</el-form>
<base-cell
v-else
label-width="100px"
>
<base-cell-item
label="主键"
>
{{ form.purchaseInfoId }}
</base-cell-item>
<base-cell-item
label="采购单号"
>
{{ form.purchaseNo }}
</base-cell-item>
<base-cell-item
label="采购员ID"
>
{{ form.userInfoUserInfoId1 }}
</base-cell-item>
<base-cell-item
label="供应商"
>
{{ form.supplier }}
</base-cell-item>
<base-cell-item
label="采购总金额"
>
{{ form.totalAmount }}
</base-cell-item>
<base-cell-item
label="采购时间"
>
{{ form.purchaseDate }}
</base-cell-item>
<base-cell-item
label="采购状态"
>
{{ form.purchaseStatusEnumPurchaseStatusEnumId1 }}
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
  let res = await proxy.$api.purchase_info.delete(row.purchaseInfoId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}
async function handleExport() {
  try {
    await proxy.$api.purchase_info.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.purchase_info.downloadExcel({
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
          let res = await proxy.$api.purchase_info[dialogStatus.value](form.value);
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