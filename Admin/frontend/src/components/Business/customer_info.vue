<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="客户名称"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="customer_info.import"
table_name="customer_info"
firstSearchData="customerName"
>
<template #collapse>
    <el-form-item  label="客户名称" prop="customerName">
        <el-input clearable placeholder="请输入客户名称" v-model="listQuery.customerName" />
    </el-form-item>

    <el-form-item  label="手机号" prop="phone">
        <el-input clearable placeholder="请输入手机号" v-model="listQuery.phone" />
    </el-form-item>

    <el-form-item  label="邮箱" prop="email">
        <el-input clearable placeholder="请输入邮箱" v-model="listQuery.email" />
    </el-form-item>

    <el-form-item  label="地址" prop="address">
        <el-input clearable placeholder="请输入地址" v-model="listQuery.address" />
    </el-form-item>

    <el-form-item  label="RFM分层" prop="rfmLevelEnumRfmLevelEnumId1">
        <el-input clearable placeholder="请输入RFM分层" v-model="listQuery.rfmLevelEnumRfmLevelEnumId1" />
    </el-form-item>

    <el-form-item  label="客户标签" prop="customerTagEnumCustomerTagEnumId1">
        <el-input clearable placeholder="请输入客户标签" v-model="listQuery.customerTagEnumCustomerTagEnumId1" />
    </el-form-item>

    <el-form-item  label="微信号" prop="wechatId">
        <el-input clearable placeholder="请输入微信号" v-model="listQuery.wechatId" />
    </el-form-item>

    <el-form-item  label="加密微信数据" prop="wechatDataEncrypted">
        <el-input clearable placeholder="请输入加密微信数据" v-model="listQuery.wechatDataEncrypted" />
    </el-form-item>

    <el-form-item  label="客户来源" prop="source">
        <el-input clearable placeholder="请输入客户来源" v-model="listQuery.source" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="customer_info.page"
>
<el-table-column
prop="customerInfoId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="customerName"
label="客户名称"
align="center"
>

</el-table-column>
<el-table-column
prop="phone"
label="手机号"
align="center"
>

</el-table-column>
<el-table-column
prop="email"
label="邮箱"
align="center"
>

</el-table-column>
<el-table-column
prop="address"
label="地址"
align="center"
>

</el-table-column>
<el-table-column
prop="rfmLevelEnumRfmLevelEnumId1"
label="RFM分层"
align="center"
>

</el-table-column>
<el-table-column
prop="customerTagEnumCustomerTagEnumId1"
label="客户标签"
align="center"
>

</el-table-column>
<el-table-column
prop="wechatId"
label="微信号"
align="center"
>

</el-table-column>
<el-table-column
prop="wechatDataEncrypted"
label="加密微信数据"
align="center"
>

</el-table-column>
<el-table-column
prop="source"
label="客户来源"
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
path="/customer_info/detail"
:query="{customerInfoId: scope.row.customerInfoId}"
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
:rules='{"customerName":[{"trigger":"blur","message":"客户名称不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item  label="客户名称" prop="customerName">
        <el-input clearable placeholder="请输入客户名称" v-model="form.customerName" />
    </el-form-item>

    <el-form-item  label="手机号" prop="phone">
        <el-input clearable placeholder="请输入手机号" v-model="form.phone" />
    </el-form-item>

    <el-form-item  label="邮箱" prop="email">
        <el-input clearable placeholder="请输入邮箱" v-model="form.email" />
    </el-form-item>

    <el-form-item  label="地址" prop="address">
        <el-input clearable placeholder="请输入地址" v-model="form.address" />
    </el-form-item>

    <el-form-item  label="RFM分层" prop="rfmLevelEnumRfmLevelEnumId1">
        <el-input clearable placeholder="请输入RFM分层" v-model="form.rfmLevelEnumRfmLevelEnumId1" />
    </el-form-item>

    <el-form-item  label="客户标签" prop="customerTagEnumCustomerTagEnumId1">
        <el-input clearable placeholder="请输入客户标签" v-model="form.customerTagEnumCustomerTagEnumId1" />
    </el-form-item>

    <el-form-item  label="微信号" prop="wechatId">
        <el-input clearable placeholder="请输入微信号" v-model="form.wechatId" />
    </el-form-item>

    <el-form-item  label="加密微信数据" prop="wechatDataEncrypted">
        <el-input clearable placeholder="请输入加密微信数据" v-model="form.wechatDataEncrypted" />
    </el-form-item>

    <el-form-item  label="客户来源" prop="source">
        <el-input clearable placeholder="请输入客户来源" v-model="form.source" />
    </el-form-item>

</el-form>
<base-cell
v-else
label-width="100px"
>
<base-cell-item
label="主键"
>
{{ form.customerInfoId }}
</base-cell-item>
<base-cell-item
label="客户名称"
>
{{ form.customerName }}
</base-cell-item>
<base-cell-item
label="手机号"
>
{{ form.phone }}
</base-cell-item>
<base-cell-item
label="邮箱"
>
{{ form.email }}
</base-cell-item>
<base-cell-item
label="地址"
>
{{ form.address }}
</base-cell-item>
<base-cell-item
label="RFM分层"
>
{{ form.rfmLevelEnumRfmLevelEnumId1 }}
</base-cell-item>
<base-cell-item
label="客户标签"
>
{{ form.customerTagEnumCustomerTagEnumId1 }}
</base-cell-item>
<base-cell-item
label="微信号"
>
{{ form.wechatId }}
</base-cell-item>
<base-cell-item
label="加密微信数据"
>
{{ form.wechatDataEncrypted }}
</base-cell-item>
<base-cell-item
label="客户来源"
>
{{ form.source }}
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
  let res = await proxy.$api.customer_info.delete(row.customerInfoId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}
async function handleExport() {
  try {
    await proxy.$api.customer_info.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.customer_info.downloadExcel({
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
          let res = await proxy.$api.customer_info[dialogStatus.value](form.value);
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