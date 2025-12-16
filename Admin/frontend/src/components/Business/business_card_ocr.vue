<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="ocr识别结果"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="business_card_ocr.import"
table_name="business_card_ocr"
firstSearchData="ocrResult"
>
<template #collapse>
    <el-form-item  label="客户ID" prop="customerInfoCustomerInfoId1">
        <el-input clearable placeholder="请输入客户ID" v-model="listQuery.customerInfoCustomerInfoId1" />
    </el-form-item>

    <el-form-item  label="OCR识别结果" prop="ocrResult">
        <el-input clearable placeholder="请输入OCR识别结果" v-model="listQuery.ocrResult" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="business_card_ocr.page"
>
<el-table-column
prop="businessCardOcrId"
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
prop="cardImageResourceKey"
label="名片图片"
align="center"
>
<template #default="scope">
<image-preview
:height="50"
:src="scope.row.cardImage.map(item => item.url).join(',')"
:width="50"
/>
</template>
</el-table-column>
<el-table-column
prop="ocrResult"
label="OCR识别结果"
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
path="/business_card_ocr/detail"
:query="{businessCardOcrId: scope.row.businessCardOcrId}"
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
:rules='{"ocrResult":[{"trigger":"blur","message":"OCR识别结果不能为空","required":true}],"customerInfoCustomerInfoId1":[{"trigger":"blur","message":"客户ID不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item  label="客户ID" prop="customerInfoCustomerInfoId1">
        <el-input clearable placeholder="请输入客户ID" v-model="form.customerInfoCustomerInfoId1" />
    </el-form-item>

    <el-form-item  label="名片图片" prop="cardImageResourceKey">
        <image-upload v-model="form.cardImage" :type="true" />
    </el-form-item>

    <el-form-item  label="OCR识别结果" prop="ocrResult">
        <el-input clearable placeholder="请输入OCR识别结果" v-model="form.ocrResult" />
    </el-form-item>

</el-form>
<base-cell
v-else
label-width="100px"
>
<base-cell-item
label="主键"
>
{{ form.businessCardOcrId }}
</base-cell-item>
<base-cell-item
label="客户ID"
>
{{ form.customerInfoCustomerInfoId1 }}
</base-cell-item>
<base-cell-item
label="名片图片"
>
{{ form.cardImageResourceKey }}
</base-cell-item>
<base-cell-item
label="OCR识别结果"
>
{{ form.ocrResult }}
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
  let res = await proxy.$api.business_card_ocr.delete(row.businessCardOcrId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}
async function handleExport() {
  try {
    await proxy.$api.business_card_ocr.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.business_card_ocr.downloadExcel({
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
          let res = await proxy.$api.business_card_ocr[dialogStatus.value](form.value);
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