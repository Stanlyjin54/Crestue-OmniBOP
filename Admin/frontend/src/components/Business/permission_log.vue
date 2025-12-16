<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="操作类型"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="permission_log.import"
table_name="permission_log"
firstSearchData="actionType"
>
<template #collapse>
    <el-form-item  label="操作人" prop="userInfoId">
        <el-input clearable placeholder="请输入操作人" v-model="listQuery.userInfoId" />
    </el-form-item>

    <el-form-item  label="操作类型" prop="actionType">
        <el-input clearable placeholder="请输入操作类型" v-model="listQuery.actionType" />
    </el-form-item>

    <el-form-item  label="目标对象" prop="targetObject">
        <el-input clearable placeholder="请输入目标对象" v-model="listQuery.targetObject" />
    </el-form-item>

    <el-form-item  label="变更内容" prop="changeContent">
        <el-input clearable placeholder="请输入变更内容" v-model="listQuery.changeContent" />
    </el-form-item>

    <el-form-item   label="操作时间" prop="actionTime">
        <el-date-picker placeholder="请输入操作时间" v-model="listQuery.actionTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="permission_log.page"
>
<el-table-column
prop="permissionLogId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="userInfoId"
label="操作人"
align="center"
>

</el-table-column>
<el-table-column
prop="actionType"
label="操作类型"
align="center"
>

</el-table-column>
<el-table-column
prop="targetObject"
label="目标对象"
align="center"
>

</el-table-column>
<el-table-column
prop="changeContent"
label="变更内容"
align="center"
>

</el-table-column>
<el-table-column
prop="actionTime"
label="操作时间"
align="center"
>
<template #default="scope">
{{ parseTime(scope.row.actionTime,'{y}-{m}-{d} {h}:{i}:{s}') }}
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
path="/permission_log/detail"
:query="{permissionLogId: scope.row.permissionLogId}"
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
:rules='{"actionType":[{"trigger":"blur","message":"操作类型不能为空","required":true}],"targetObject":[{"trigger":"blur","message":"目标对象不能为空","required":true}],"actionTime":[{"trigger":"blur","message":"操作时间不能为空","required":true}],"userInfoId":[{"trigger":"blur","message":"操作人不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item  label="操作人" prop="userInfoId">
        <el-input clearable placeholder="请输入操作人" v-model="form.userInfoId" />
    </el-form-item>

    <el-form-item  label="操作类型" prop="actionType">
        <el-input clearable placeholder="请输入操作类型" v-model="form.actionType" />
    </el-form-item>

    <el-form-item  label="目标对象" prop="targetObject">
        <el-input clearable placeholder="请输入目标对象" v-model="form.targetObject" />
    </el-form-item>

    <el-form-item  label="变更内容" prop="changeContent">
        <el-input clearable placeholder="请输入变更内容" v-model="form.changeContent" />
    </el-form-item>

    <el-form-item   label="操作时间" prop="actionTime">
        <el-date-picker placeholder="请输入操作时间" v-model="form.actionTime" type="datetime"
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
{{ form.permissionLogId }}
</base-cell-item>
<base-cell-item
label="操作人"
>
{{ form.userInfoId }}
</base-cell-item>
<base-cell-item
label="操作类型"
>
{{ form.actionType }}
</base-cell-item>
<base-cell-item
label="目标对象"
>
{{ form.targetObject }}
</base-cell-item>
<base-cell-item
label="变更内容"
>
{{ form.changeContent }}
</base-cell-item>
<base-cell-item
label="操作时间"
>
{{ form.actionTime }}
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
  let res = await proxy.$api.permission_log.delete(row.permissionLogId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}
async function handleExport() {
  try {
    await proxy.$api.permission_log.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.permission_log.downloadExcel({
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
          let res = await proxy.$api.permission_log[dialogStatus.value](form.value);
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