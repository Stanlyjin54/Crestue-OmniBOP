<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="工具类型"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="fission_tool.import"
table_name="fission_tool"
firstSearchData="toolType"
>
<template #collapse>
    <el-form-item  label="工具类型" prop="toolType">
        <el-input clearable placeholder="请输入工具类型" v-model="listQuery.toolType" />
    </el-form-item>

    <el-form-item  label="活动标题" prop="title">
        <el-input clearable placeholder="请输入活动标题" v-model="listQuery.title" />
    </el-form-item>

    <el-form-item  label="配置内容" prop="config">
        <el-input clearable placeholder="请输入配置内容" v-model="listQuery.config" />
    </el-form-item>

    <el-form-item   label="开始日期" prop="startDate">
        <el-date-picker placeholder="请输入开始日期" v-model="listQuery.startDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item   label="结束日期" prop="endDate">
        <el-date-picker placeholder="请输入结束日期" v-model="listQuery.endDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="fission_tool.page"
>
<el-table-column
prop="fissionToolId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="toolType"
label="工具类型"
align="center"
>

</el-table-column>
<el-table-column
prop="title"
label="活动标题"
align="center"
>

</el-table-column>
<el-table-column
prop="config"
label="配置内容"
align="center"
>

</el-table-column>
<el-table-column
prop="startDate"
label="开始日期"
align="center"
>
<template #default="scope">
{{ parseTime(scope.row.startDate,'{y}-{m}-{d} {h}:{i}:{s}') }}
</template>
</el-table-column>
<el-table-column
prop="endDate"
label="结束日期"
align="center"
>
<template #default="scope">
{{ parseTime(scope.row.endDate,'{y}-{m}-{d} {h}:{i}:{s}') }}
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
path="/fission_tool/detail"
:query="{fissionToolId: scope.row.fissionToolId}"
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
:rules='{"endDate":[{"trigger":"blur","message":"结束日期不能为空","required":true}],"title":[{"trigger":"blur","message":"活动标题不能为空","required":true}],"toolType":[{"trigger":"blur","message":"工具类型不能为空","required":true}],"startDate":[{"trigger":"blur","message":"开始日期不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item  label="工具类型" prop="toolType">
        <el-input clearable placeholder="请输入工具类型" v-model="form.toolType" />
    </el-form-item>

    <el-form-item  label="活动标题" prop="title">
        <el-input clearable placeholder="请输入活动标题" v-model="form.title" />
    </el-form-item>

    <el-form-item  label="配置内容" prop="config">
        <el-input clearable placeholder="请输入配置内容" v-model="form.config" />
    </el-form-item>

    <el-form-item   label="开始日期" prop="startDate">
        <el-date-picker placeholder="请输入开始日期" v-model="form.startDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item   label="结束日期" prop="endDate">
        <el-date-picker placeholder="请输入结束日期" v-model="form.endDate" type="datetime"
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
{{ form.fissionToolId }}
</base-cell-item>
<base-cell-item
label="工具类型"
>
{{ form.toolType }}
</base-cell-item>
<base-cell-item
label="活动标题"
>
{{ form.title }}
</base-cell-item>
<base-cell-item
label="配置内容"
>
{{ form.config }}
</base-cell-item>
<base-cell-item
label="开始日期"
>
{{ form.startDate }}
</base-cell-item>
<base-cell-item
label="结束日期"
>
{{ form.endDate }}
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
  let res = await proxy.$api.fission_tool.delete(row.fissionToolId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}
async function handleExport() {
  try {
    await proxy.$api.fission_tool.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.fission_tool.downloadExcel({
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
          let res = await proxy.$api.fission_tool[dialogStatus.value](form.value);
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