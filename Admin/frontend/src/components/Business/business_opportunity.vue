<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="商机名称"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="business_opportunity.import"
table_name="business_opportunity"
firstSearchData="opportunityName"
>
<template #collapse>
    <el-form-item  label="客户ID" prop="customerInfoCustomerInfoId1">
        <el-input clearable placeholder="请输入客户ID" v-model="listQuery.customerInfoCustomerInfoId1" />
    </el-form-item>

    <el-form-item  label="商机名称" prop="opportunityName">
        <el-input clearable placeholder="请输入商机名称" v-model="listQuery.opportunityName" />
    </el-form-item>

    <el-form-item  label="商机状态" prop="status">
        <el-input clearable placeholder="请输入商机状态" v-model="listQuery.status" />
    </el-form-item>

    <el-form-item  label="AI跟进建议" prop="followUpSuggestion">
        <el-input clearable placeholder="请输入AI跟进建议" v-model="listQuery.followUpSuggestion" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="business_opportunity.page"
>
<el-table-column
prop="businessOpportunityId"
label="主键"
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
prop="opportunityName"
label="商机名称"
align="center"
>

</el-table-column>
<el-table-column
prop="status"
label="商机状态"
align="center"
>

</el-table-column>
<el-table-column
prop="followUpSuggestion"
label="AI跟进建议"
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
path="/business_opportunity/detail"
:query="{businessOpportunityId: scope.row.businessOpportunityId}"
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
:rules='{"opportunityName":[{"trigger":"blur","message":"商机名称不能为空","required":true}],"customerInfoCustomerInfoId1":[{"trigger":"blur","message":"客户ID不能为空","required":true}],"status":[{"trigger":"blur","message":"商机状态不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item  label="客户ID" prop="customerInfoCustomerInfoId1">
        <el-input clearable placeholder="请输入客户ID" v-model="form.customerInfoCustomerInfoId1" />
    </el-form-item>

    <el-form-item  label="商机名称" prop="opportunityName">
        <el-input clearable placeholder="请输入商机名称" v-model="form.opportunityName" />
    </el-form-item>

    <el-form-item  label="商机状态" prop="status">
        <el-input clearable placeholder="请输入商机状态" v-model="form.status" />
    </el-form-item>

    <el-form-item  label="AI跟进建议" prop="followUpSuggestion">
        <el-input clearable placeholder="请输入AI跟进建议" v-model="form.followUpSuggestion" />
    </el-form-item>

</el-form>
<base-cell
v-else
label-width="100px"
>
<base-cell-item
label="主键"
>
{{ form.businessOpportunityId }}
</base-cell-item>
<base-cell-item
label="客户ID"
>
{{ form.customerInfoCustomerInfoId1 }}
</base-cell-item>
<base-cell-item
label="商机名称"
>
{{ form.opportunityName }}
</base-cell-item>
<base-cell-item
label="商机状态"
>
{{ form.status }}
</base-cell-item>
<base-cell-item
label="AI跟进建议"
>
{{ form.followUpSuggestion }}
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
  let res = await proxy.$api.business_opportunity.delete(row.businessOpportunityId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}
async function handleExport() {
  try {
    await proxy.$api.business_opportunity.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.business_opportunity.downloadExcel({
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
          let res = await proxy.$api.business_opportunity[dialogStatus.value](form.value);
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