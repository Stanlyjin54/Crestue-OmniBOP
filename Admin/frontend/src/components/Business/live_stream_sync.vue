<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="平台名称"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="live_stream_sync.import"
table_name="live_stream_sync"
firstSearchData="platform"
>
<template #collapse>
    <el-form-item  label="订单ID" prop="orderInfoOrderInfoId1">
        <el-input clearable placeholder="请输入订单ID" v-model="listQuery.orderInfoOrderInfoId1" />
    </el-form-item>

    <el-form-item  label="平台名称" prop="platform">
        <el-input clearable placeholder="请输入平台名称" v-model="listQuery.platform" />
    </el-form-item>

    <el-form-item   label="同步时间" prop="syncTime">
        <el-date-picker placeholder="请输入同步时间" v-model="listQuery.syncTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="live_stream_sync.page"
>
<el-table-column
prop="liveStreamSyncId"
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
prop="platform"
label="平台名称"
align="center"
>

</el-table-column>
<el-table-column
prop="syncTime"
label="同步时间"
align="center"
>
<template #default="scope">
{{ parseTime(scope.row.syncTime,'{y}-{m}-{d} {h}:{i}:{s}') }}
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
path="/live_stream_sync/detail"
:query="{liveStreamSyncId: scope.row.liveStreamSyncId}"
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
:rules='{"syncTime":[{"trigger":"blur","message":"同步时间不能为空","required":true}],"orderInfoOrderInfoId1":[{"trigger":"blur","message":"订单ID不能为空","required":true}],"platform":[{"trigger":"blur","message":"平台名称不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item  label="订单ID" prop="orderInfoOrderInfoId1">
        <el-input clearable placeholder="请输入订单ID" v-model="form.orderInfoOrderInfoId1" />
    </el-form-item>

    <el-form-item  label="平台名称" prop="platform">
        <el-input clearable placeholder="请输入平台名称" v-model="form.platform" />
    </el-form-item>

    <el-form-item   label="同步时间" prop="syncTime">
        <el-date-picker placeholder="请输入同步时间" v-model="form.syncTime" type="datetime"
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
{{ form.liveStreamSyncId }}
</base-cell-item>
<base-cell-item
label="订单ID"
>
{{ form.orderInfoOrderInfoId1 }}
</base-cell-item>
<base-cell-item
label="平台名称"
>
{{ form.platform }}
</base-cell-item>
<base-cell-item
label="同步时间"
>
{{ form.syncTime }}
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
  let res = await proxy.$api.live_stream_sync.delete(row.liveStreamSyncId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}
async function handleExport() {
  try {
    await proxy.$api.live_stream_sync.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.live_stream_sync.downloadExcel({
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
          let res = await proxy.$api.live_stream_sync[dialogStatus.value](form.value);
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