<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="维度类型"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="profit_board.import"
table_name="profit_board"
firstSearchData="targetType"
>
<template #collapse>
    <el-form-item  label="维度类型" prop="targetType">
        <el-input clearable placeholder="请输入维度类型" v-model="listQuery.targetType" />
    </el-form-item>

    <el-form-item  label="维度ID" prop="targetId">
        <el-input clearable placeholder="请输入维度ID" v-model="listQuery.targetId" />
    </el-form-item>

    <el-form-item  label="毛利金额" prop="profitAmount">
        <el-input clearable placeholder="请输入毛利金额" v-model="listQuery.profitAmount" />
    </el-form-item>

    <el-form-item  label="平台佣金" prop="commission">
        <el-input clearable placeholder="请输入平台佣金" v-model="listQuery.commission" />
    </el-form-item>

    <el-form-item  label="运费" prop="shippingFee">
        <el-input clearable placeholder="请输入运费" v-model="listQuery.shippingFee" />
    </el-form-item>

    <el-form-item   label="统计日期" prop="statDate">
        <el-date-picker placeholder="请输入统计日期" v-model="listQuery.statDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="profit_board.page"
>
<el-table-column
prop="profitBoardId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="targetType"
label="维度类型"
align="center"
>

</el-table-column>
<el-table-column
prop="targetId"
label="维度ID"
align="center"
>

</el-table-column>
<el-table-column
prop="profitAmount"
label="毛利金额"
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
prop="statDate"
label="统计日期"
align="center"
>
<template #default="scope">
{{ parseTime(scope.row.statDate,'{y}-{m}-{d} {h}:{i}:{s}') }}
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
path="/profit_board/detail"
:query="{profitBoardId: scope.row.profitBoardId}"
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
:rules='{"statDate":[{"trigger":"blur","message":"统计日期不能为空","required":true}],"targetId":[{"trigger":"blur","message":"维度ID不能为空","required":true}],"profitAmount":[{"trigger":"blur","message":"毛利金额不能为空","required":true}],"targetType":[{"trigger":"blur","message":"维度类型不能为空","required":true}]}'
label-width='100px'
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
<base-cell
v-else
label-width="100px"
>
<base-cell-item
label="主键"
>
{{ form.profitBoardId }}
</base-cell-item>
<base-cell-item
label="维度类型"
>
{{ form.targetType }}
</base-cell-item>
<base-cell-item
label="维度ID"
>
{{ form.targetId }}
</base-cell-item>
<base-cell-item
label="毛利金额"
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
label="统计日期"
>
{{ form.statDate }}
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
  let res = await proxy.$api.profit_board.delete(row.profitBoardId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}
async function handleExport() {
  try {
    await proxy.$api.profit_board.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.profit_board.downloadExcel({
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
          let res = await proxy.$api.profit_board[dialogStatus.value](form.value);
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