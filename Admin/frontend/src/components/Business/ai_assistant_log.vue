<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="查询内容"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="ai_assistant_log.import"
table_name="ai_assistant_log"
firstSearchData="queryText"
>
<template #collapse>
    <el-form-item  label="用户ID" prop="userInfoUserInfoId1">
        <el-input clearable placeholder="请输入用户ID" v-model="listQuery.userInfoUserInfoId1" />
    </el-form-item>

    <el-form-item  label="查询内容" prop="queryText">
        <el-input clearable placeholder="请输入查询内容" v-model="listQuery.queryText" />
    </el-form-item>

    <el-form-item  label="AI回复内容" prop="responseText">
        <el-input clearable placeholder="请输入AI回复内容" v-model="listQuery.responseText" />
    </el-form-item>

    <el-form-item   label="查询时间" prop="queryTime">
        <el-date-picker placeholder="请输入查询时间" v-model="listQuery.queryTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="ai_assistant_log.page"
>
<el-table-column
prop="aiAssistantLogId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="userInfoUserInfoId1"
label="用户ID"
align="center"
>

</el-table-column>
<el-table-column
prop="queryText"
label="查询内容"
align="center"
>

</el-table-column>
<el-table-column
prop="responseText"
label="AI回复内容"
align="center"
>

</el-table-column>
<el-table-column
prop="queryTime"
label="查询时间"
align="center"
>
<template #default="scope">
{{ parseTime(scope.row.queryTime,'{y}-{m}-{d} {h}:{i}:{s}') }}
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
path="/ai_assistant_log/detail"
:query="{aiAssistantLogId: scope.row.aiAssistantLogId}"
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
:rules='{"responseText":[{"trigger":"blur","message":"AI回复内容不能为空","required":true}],"queryText":[{"trigger":"blur","message":"查询内容不能为空","required":true}],"queryTime":[{"trigger":"blur","message":"查询时间不能为空","required":true}],"userInfoUserInfoId1":[{"trigger":"blur","message":"用户ID不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item  label="用户ID" prop="userInfoUserInfoId1">
        <el-input clearable placeholder="请输入用户ID" v-model="form.userInfoUserInfoId1" />
    </el-form-item>

    <el-form-item  label="查询内容" prop="queryText">
        <el-input clearable placeholder="请输入查询内容" v-model="form.queryText" />
    </el-form-item>

    <el-form-item  label="AI回复内容" prop="responseText">
        <el-input clearable placeholder="请输入AI回复内容" v-model="form.responseText" />
    </el-form-item>

    <el-form-item   label="查询时间" prop="queryTime">
        <el-date-picker placeholder="请输入查询时间" v-model="form.queryTime" type="datetime"
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
{{ form.aiAssistantLogId }}
</base-cell-item>
<base-cell-item
label="用户ID"
>
{{ form.userInfoUserInfoId1 }}
</base-cell-item>
<base-cell-item
label="查询内容"
>
{{ form.queryText }}
</base-cell-item>
<base-cell-item
label="AI回复内容"
>
{{ form.responseText }}
</base-cell-item>
<base-cell-item
label="查询时间"
>
{{ form.queryTime }}
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
  let res = await proxy.$api.ai_assistant_log.delete(row.aiAssistantLogId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}
async function handleExport() {
  try {
    await proxy.$api.ai_assistant_log.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.ai_assistant_log.downloadExcel({
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
          let res = await proxy.$api.ai_assistant_log[dialogStatus.value](form.value);
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