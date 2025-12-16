<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="广告标题"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="ad_recommend_info.import"
table_name="ad_recommend_info"
firstSearchData="adTitle"
>
<template #collapse>
    <el-form-item  label="广告标题" prop="adTitle">
        <el-input clearable placeholder="请输入广告标题" v-model="listQuery.adTitle" />
    </el-form-item>

    <el-form-item  label="跳转链接" prop="adUrl">
        <el-input clearable placeholder="请输入跳转链接" v-model="listQuery.adUrl" />
    </el-form-item>

    <el-form-item  label="CPC费用" prop="cpcFee">
        <el-input clearable placeholder="请输入CPC费用" v-model="listQuery.cpcFee" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="ad_recommend_info.page"
>
<el-table-column
prop="adRecommendInfoId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="adTitle"
label="广告标题"
align="center"
>

</el-table-column>
<el-table-column
prop="adImageResourceKey"
label="广告图片"
align="center"
>
<template #default="scope">
<image-preview
:height="50"
:src="scope.row.adImage.map(item => item.url).join(',')"
:width="50"
/>
</template>
</el-table-column>
<el-table-column
prop="adUrl"
label="跳转链接"
align="center"
>

</el-table-column>
<el-table-column
prop="cpcFee"
label="CPC费用"
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
path="/ad_recommend_info/detail"
:query="{adRecommendInfoId: scope.row.adRecommendInfoId}"
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
:rules='{"adTitle":[{"trigger":"blur","message":"广告标题不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item  label="广告标题" prop="adTitle">
        <el-input clearable placeholder="请输入广告标题" v-model="form.adTitle" />
    </el-form-item>

    <el-form-item  label="广告图片" prop="adImageResourceKey">
        <image-upload v-model="form.adImage" :type="true" />
    </el-form-item>

    <el-form-item  label="跳转链接" prop="adUrl">
        <el-input clearable placeholder="请输入跳转链接" v-model="form.adUrl" />
    </el-form-item>

    <el-form-item  label="CPC费用" prop="cpcFee">
        <el-input clearable placeholder="请输入CPC费用" v-model="form.cpcFee" />
    </el-form-item>

</el-form>
<base-cell
v-else
label-width="100px"
>
<base-cell-item
label="主键"
>
{{ form.adRecommendInfoId }}
</base-cell-item>
<base-cell-item
label="广告标题"
>
{{ form.adTitle }}
</base-cell-item>
<base-cell-item
label="广告图片"
>
{{ form.adImageResourceKey }}
</base-cell-item>
<base-cell-item
label="跳转链接"
>
{{ form.adUrl }}
</base-cell-item>
<base-cell-item
label="CPC费用"
>
{{ form.cpcFee }}
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
  let res = await proxy.$api.ad_recommend_info.delete(row.adRecommendInfoId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}
async function handleExport() {
  try {
    await proxy.$api.ad_recommend_info.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.ad_recommend_info.downloadExcel({
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
          let res = await proxy.$api.ad_recommend_info[dialogStatus.value](form.value);
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