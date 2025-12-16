<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="商品名称"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="product_info.import"
table_name="product_info"
firstSearchData="productName"
>
<template #collapse>
    <el-form-item  label="商品名称" prop="productName">
        <el-input clearable placeholder="请输入商品名称" v-model="listQuery.productName" />
    </el-form-item>

    <el-form-item  label="条形码" prop="barcode">
        <el-input clearable placeholder="请输入条形码" v-model="listQuery.barcode" />
    </el-form-item>

    <el-form-item  label="商品分类" prop="category">
        <el-input clearable placeholder="请输入商品分类" v-model="listQuery.category" />
    </el-form-item>

    <el-form-item  label="品牌" prop="brand">
        <el-input clearable placeholder="请输入品牌" v-model="listQuery.brand" />
    </el-form-item>

    <el-form-item  label="规格" prop="specification">
        <el-input clearable placeholder="请输入规格" v-model="listQuery.specification" />
    </el-form-item>

    <el-form-item  label="单位" prop="unit">
        <el-input clearable placeholder="请输入单位" v-model="listQuery.unit" />
    </el-form-item>

    <el-form-item  label="序列号" prop="serialNumber">
        <el-input clearable placeholder="请输入序列号" v-model="listQuery.serialNumber" />
    </el-form-item>

    <el-form-item   label="保质期" prop="shelfLife">
        <el-date-picker placeholder="请输入保质期" v-model="listQuery.shelfLife" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item  label="安全库存" prop="minStock">
        <el-input clearable placeholder="请输入安全库存" v-model="listQuery.minStock" />
    </el-form-item>

    <el-form-item  label="商品状态" prop="productStatusEnumProductStatusEnumId1">
        <el-input clearable placeholder="请输入商品状态" v-model="listQuery.productStatusEnumProductStatusEnumId1" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="product_info.page"
>
<el-table-column
prop="productInfoId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="productName"
label="商品名称"
align="center"
>

</el-table-column>
<el-table-column
prop="barcode"
label="条形码"
align="center"
>

</el-table-column>
<el-table-column
prop="category"
label="商品分类"
align="center"
>

</el-table-column>
<el-table-column
prop="brand"
label="品牌"
align="center"
>

</el-table-column>
<el-table-column
prop="specification"
label="规格"
align="center"
>

</el-table-column>
<el-table-column
prop="unit"
label="单位"
align="center"
>

</el-table-column>
<el-table-column
prop="imagesResourceKey"
label="商品图片"
align="center"
>
<template #default="scope">
<image-preview
:height="50"
:src="scope.row.images.map(item => item.url).join(',')"
:width="50"
/>
</template>
</el-table-column>
<el-table-column
prop="serialNumber"
label="序列号"
align="center"
>

</el-table-column>
<el-table-column
prop="shelfLife"
label="保质期"
align="center"
>
<template #default="scope">
{{ parseTime(scope.row.shelfLife,'{y}-{m}-{d} {h}:{i}:{s}') }}
</template>
</el-table-column>
<el-table-column
prop="minStock"
label="安全库存"
align="center"
>

</el-table-column>
<el-table-column
prop="productStatusEnumProductStatusEnumId1"
label="商品状态"
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
path="/product_info/detail"
:query="{productInfoId: scope.row.productInfoId}"
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
:rules='{"productStatusEnumProductStatusEnumId1":[{"trigger":"blur","message":"商品状态不能为空","required":true}],"productName":[{"trigger":"blur","message":"商品名称不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item  label="商品名称" prop="productName">
        <el-input clearable placeholder="请输入商品名称" v-model="form.productName" />
    </el-form-item>

    <el-form-item  label="条形码" prop="barcode">
        <el-input clearable placeholder="请输入条形码" v-model="form.barcode" />
    </el-form-item>

    <el-form-item  label="商品分类" prop="category">
        <el-input clearable placeholder="请输入商品分类" v-model="form.category" />
    </el-form-item>

    <el-form-item  label="品牌" prop="brand">
        <el-input clearable placeholder="请输入品牌" v-model="form.brand" />
    </el-form-item>

    <el-form-item  label="规格" prop="specification">
        <el-input clearable placeholder="请输入规格" v-model="form.specification" />
    </el-form-item>

    <el-form-item  label="单位" prop="unit">
        <el-input clearable placeholder="请输入单位" v-model="form.unit" />
    </el-form-item>

    <el-form-item  label="商品图片" prop="imagesResourceKey">
        <image-upload v-model="form.images" :type="true" />
    </el-form-item>

    <el-form-item  label="序列号" prop="serialNumber">
        <el-input clearable placeholder="请输入序列号" v-model="form.serialNumber" />
    </el-form-item>

    <el-form-item   label="保质期" prop="shelfLife">
        <el-date-picker placeholder="请输入保质期" v-model="form.shelfLife" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item  label="安全库存" prop="minStock">
        <el-input clearable placeholder="请输入安全库存" v-model="form.minStock" />
    </el-form-item>

    <el-form-item  label="商品状态" prop="productStatusEnumProductStatusEnumId1">
        <el-input clearable placeholder="请输入商品状态" v-model="form.productStatusEnumProductStatusEnumId1" />
    </el-form-item>

</el-form>
<base-cell
v-else
label-width="100px"
>
<base-cell-item
label="主键"
>
{{ form.productInfoId }}
</base-cell-item>
<base-cell-item
label="商品名称"
>
{{ form.productName }}
</base-cell-item>
<base-cell-item
label="条形码"
>
{{ form.barcode }}
</base-cell-item>
<base-cell-item
label="商品分类"
>
{{ form.category }}
</base-cell-item>
<base-cell-item
label="品牌"
>
{{ form.brand }}
</base-cell-item>
<base-cell-item
label="规格"
>
{{ form.specification }}
</base-cell-item>
<base-cell-item
label="单位"
>
{{ form.unit }}
</base-cell-item>
<base-cell-item
label="商品图片"
>
{{ form.imagesResourceKey }}
</base-cell-item>
<base-cell-item
label="序列号"
>
{{ form.serialNumber }}
</base-cell-item>
<base-cell-item
label="保质期"
>
{{ form.shelfLife }}
</base-cell-item>
<base-cell-item
label="安全库存"
>
{{ form.minStock }}
</base-cell-item>
<base-cell-item
label="商品状态"
>
{{ form.productStatusEnumProductStatusEnumId1 }}
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
  let res = await proxy.$api.product_info.delete(row.productInfoId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}
async function handleExport() {
  try {
    await proxy.$api.product_info.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.product_info.downloadExcel({
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
          let res = await proxy.$api.product_info[dialogStatus.value](form.value);
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