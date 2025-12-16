<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="批次号"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="inventory_record.import"
table_name="inventory_record"
firstSearchData="batchNumber"
>
<template #collapse>
    <el-form-item  label="商品ID" prop="productInfoProductInfoId1">
        <el-input clearable placeholder="请输入商品ID" v-model="listQuery.productInfoProductInfoId1" />
    </el-form-item>

    <el-form-item  label="仓库ID" prop="warehouseInfoWarehouseInfoId1">
        <el-input clearable placeholder="请输入仓库ID" v-model="listQuery.warehouseInfoWarehouseInfoId1" />
    </el-form-item>

    <el-form-item  label="库存数量" prop="quantity">
        <el-input clearable placeholder="请输入库存数量" v-model="listQuery.quantity" />
    </el-form-item>

    <el-form-item  label="批次号" prop="batchNumber">
        <el-input clearable placeholder="请输入批次号" v-model="listQuery.batchNumber" />
    </el-form-item>

    <el-form-item   label="效期" prop="expireDate">
        <el-date-picker placeholder="请输入效期" v-model="listQuery.expireDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item  label="序列号" prop="serialNumber">
        <el-input clearable placeholder="请输入序列号" v-model="listQuery.serialNumber" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="inventory_record.page"
>
<el-table-column
prop="inventoryRecordId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="productInfoProductInfoId1"
label="商品ID"
align="center"
>

</el-table-column>
<el-table-column
prop="warehouseInfoWarehouseInfoId1"
label="仓库ID"
align="center"
>

</el-table-column>
<el-table-column
prop="quantity"
label="库存数量"
align="center"
>

</el-table-column>
<el-table-column
prop="batchNumber"
label="批次号"
align="center"
>

</el-table-column>
<el-table-column
prop="expireDate"
label="效期"
align="center"
>
<template #default="scope">
{{ parseTime(scope.row.expireDate,'{y}-{m}-{d} {h}:{i}:{s}') }}
</template>
</el-table-column>
<el-table-column
prop="serialNumber"
label="序列号"
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
path="/inventory_record/detail"
:query="{inventoryRecordId: scope.row.inventoryRecordId}"
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
:rules='{"warehouseInfoWarehouseInfoId1":[{"trigger":"blur","message":"仓库ID不能为空","required":true}],"quantity":[{"trigger":"blur","message":"库存数量不能为空","required":true}],"productInfoProductInfoId1":[{"trigger":"blur","message":"商品ID不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item  label="商品ID" prop="productInfoProductInfoId1">
        <el-input clearable placeholder="请输入商品ID" v-model="form.productInfoProductInfoId1" />
    </el-form-item>

    <el-form-item  label="仓库ID" prop="warehouseInfoWarehouseInfoId1">
        <el-input clearable placeholder="请输入仓库ID" v-model="form.warehouseInfoWarehouseInfoId1" />
    </el-form-item>

    <el-form-item  label="库存数量" prop="quantity">
        <el-input clearable placeholder="请输入库存数量" v-model="form.quantity" />
    </el-form-item>

    <el-form-item  label="批次号" prop="batchNumber">
        <el-input clearable placeholder="请输入批次号" v-model="form.batchNumber" />
    </el-form-item>

    <el-form-item   label="效期" prop="expireDate">
        <el-date-picker placeholder="请输入效期" v-model="form.expireDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item  label="序列号" prop="serialNumber">
        <el-input clearable placeholder="请输入序列号" v-model="form.serialNumber" />
    </el-form-item>

</el-form>
<base-cell
v-else
label-width="100px"
>
<base-cell-item
label="主键"
>
{{ form.inventoryRecordId }}
</base-cell-item>
<base-cell-item
label="商品ID"
>
{{ form.productInfoProductInfoId1 }}
</base-cell-item>
<base-cell-item
label="仓库ID"
>
{{ form.warehouseInfoWarehouseInfoId1 }}
</base-cell-item>
<base-cell-item
label="库存数量"
>
{{ form.quantity }}
</base-cell-item>
<base-cell-item
label="批次号"
>
{{ form.batchNumber }}
</base-cell-item>
<base-cell-item
label="效期"
>
{{ form.expireDate }}
</base-cell-item>
<base-cell-item
label="序列号"
>
{{ form.serialNumber }}
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
  let res = await proxy.$api.inventory_record.delete(row.inventoryRecordId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}
async function handleExport() {
  try {
    await proxy.$api.inventory_record.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.inventory_record.downloadExcel({
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
          let res = await proxy.$api.inventory_record[dialogStatus.value](form.value);
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