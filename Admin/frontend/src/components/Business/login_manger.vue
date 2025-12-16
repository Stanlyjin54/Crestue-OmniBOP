<template>
  <base-wrapper
  >
    <base-header>
      <search-operation
          :searchData="listQuery"
          @refreshTableData="refreshTableData"
          @addEvent="handleAdd"
          @exportEvent="handleExport"
          @downloadEvent="downloadTemplate"
          firstSearchData="phoneNumber"
          firstSearchComment="手机号/用户名"
      >
        <template #collapse>
          <el-form-item label="微信绑定ID" prop="wxOpenId">
            <el-input placeholder="请输入微信绑定ID" v-model="listQuery.wxOpenId"/>
          </el-form-item>

          <el-form-item label="手机号/用户名" prop="phoneNumber">
            <el-input placeholder="请输入手机号/用户名" v-model="listQuery.phoneNumber"/>
          </el-form-item>

          <el-form-item label="用户昵称" prop="userName">
            <el-input placeholder="请输入用户昵称" v-model="listQuery.userName"/>
          </el-form-item>

        </template>
      </search-operation>
    </base-header>
    <base-table-p
        ref="baseTableRef"
        :params="listQuery"
        api="login_manger.page"
    >
      <el-table-column
          prop="loginId"
          label="主键"
          align="center"
      >

      </el-table-column>
      <el-table-column
          prop="wxOpenId"
          label="微信绑定ID"
          align="center"
      >

      </el-table-column>
      <el-table-column
          prop="phoneNumber"
          label="手机号/用户名"
          align="center"
      >

      </el-table-column>
      <el-table-column
          prop="userName"
          label="用户昵称"
          align="center"
      >

      </el-table-column>
      <el-table-column
          prop="relevanceId"
          label="绑定业务ID"
          align="center"
      >

      </el-table-column>

      <el-table-column
          prop="relevanceTable"
          label="绑定业务"
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

          <base-edit-btn
              @ok="handleUpdate(scope.row)"
          >
          </base-edit-btn>
          <base-delete-btn
              @ok="handleDelete(scope.row)"
          ></base-delete-btn>
          <!--          <div>-->
          <el-button link style="margin-left: 5px; " @click="showDialog(scope.row.loginId)">
            <!--            <el-icon class="el-icon-lock" />-->
            <el-icon class="m-r-6">
              <Lock/>
            </el-icon>
            修改密码
          </el-button>
          <!--          </div>-->
        </template>
      </el-table-column>
    </base-table-p>

    <base-dialog
        :title="dialogTitleObj[dialogStatus]"
        width="50%"
        style="max-width: 600px;"
        v-model="dialogVisible"
        @close="closeDialog"
    >
      <el-form
          ref='dataFormRef'
          v-if='dialogStatus !== "detail"'
          :model='form'
          :rules="rules"
          label-position='top'
          label-width='100px'
      >
        <el-form-item label="微信绑定ID" prop="wxOpenId">
          <el-input placeholder="请输入微信绑定ID" v-model="form.wxOpenId"/>
        </el-form-item>

        <el-form-item label="手机号/用户名" prop="phoneNumber">
          <el-input placeholder="请输入手机号/用户名" v-model="form.phoneNumber"/>
        </el-form-item>

        <el-form-item label="用户昵称" prop="userName">
          <el-input placeholder="请输入用户昵称" v-model="form.userName"/>
        </el-form-item>

        <!--      <el-form-item  v-if='dialogStatus == "add"' label="密码" prop="password">-->
        <!--          <el-input placeholder="请输入身份证号" v-model="form.password" />-->
        <!--      </el-form-item>-->

        <el-form-item v-show="haveParam" label="绑定业务" prop="relevanceTable">
          <base-select v-model="form.relevanceTable" placeholder="请输选择" api="login_manger.relevance_all"
                       @change="relevance_change"></base-select>
        </el-form-item>

        <el-form-item v-show="haveParam" label="绑定业务ID" prop="relevanceId" v-if="is_show_select">
          <!-- <el-input placeholder="请输入绑定业务ID" v-model="form.relevanceId" /> -->
          <base-select v-model="form.relevanceId" placeholder="请输选择" :api="relevance_value_api"></base-select>
        </el-form-item>

      </el-form>

      <template #footer>
        <el-button
            @click="closeDialog"
            round
        >取 消
        </el-button>
        <el-button
            @click="submitForm"
            round
            color="#5D5FEF"
            type="primary"
        >确 定
        </el-button>
      </template>
    </base-dialog>


    <base-dialog
        :title="重置密码"
        width="50%"
        style="max-width: 600px;"
        v-model="update_password_dialog"
    >
      <el-form
          ref='dataFormRef1'
          :model='update_password_form'
          :rules="password_rules"
          label-position='top'
          label-width='100px'
      >
        <el-form-item label="新密码" prop="password">
          <el-input placeholder="请输入密码" v-model="update_password_form.password"/>
        </el-form-item>

      </el-form>

      <template #footer>
        <el-button
            @click="update_password_dialog = false"
            round
        >取 消
        </el-button>
        <el-button
            @click="update_password_function"
            round
            color="#5D5FEF"
            type="primary"
        >确 定
        </el-button>
      </template>
    </base-dialog>
  </base-wrapper>
</template>

<script setup>
const {proxy} = getCurrentInstance();

const props = defineProps({
  params: {type: Object, default: () => ({})},
})

// 计算属性，用于判断 params 的长度
const paramsLength = computed(() => Object.keys(props.params).length)

// 计算属性，判断 params 是否为空
const isParamsEmpty = computed(() => paramsLength.value === 0)

let listQuery = ref({});
let form = ref({});
let dialogVisible = ref(false);
let dialogStatus = ref('');
let dialogTitleObj = ref({update: '编辑', add: '添加', detail: '详情'});
let rules = ref({
  "phoneNumber": [{"trigger": "blur", "message": "手机号/用户名不能为空", "required": true}]
});
let password_rules = ref({
  "password": [{"trigger": "blur", "message": "密码不能为空", "required": true}]
})
if (isParamsEmpty.value) {
  let routerQuery = proxy.$route.query;
  Object.assign(listQuery.value, routerQuery);
  Object.assign(form.value, routerQuery);
} else {
  Object.assign(listQuery.value, props.params);
  Object.assign(form.value, props.params);
}
const haveParam = ref(true)
if (form.value.relevanceId && form.value.relevanceTable) {
  haveParam.value = false
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
  let routerQuery = proxy.$route.query;
  form.value = {...props.params};
  Object.assign(form.value, routerQuery);
  dialogStatus.value = 'add';
  dialogVisible.value = true;
}

const relevance_value_api = ref('');
const is_show_select = ref(false)
const closeDialog = () => {
  dialogVisible.value = false
  is_show_select.value = false
}

async function relevance_change(value) {
  console.log("zhixing", value)
  let res = await proxy.$api.login_manger.relevance_get_value({relevance: value})

  if (res.code == 0) {
    relevance_value_api.value = res.data;
    is_show_select.value = true
    console.log(relevance_value_api.value)
  }
}

function handleUpdate(row) {
  form.value = Object.assign({}, row);
  dialogStatus.value = 'update';
  dialogVisible.value = true;
  // if (dialogStatus.value == "update") {
  // console.log("update", value)
  //  请求数据
  if (form.value.relevanceTable != null && form.value.relevanceTable != '') {
    is_show_select.value = false
    relevance_change(form.value.relevanceTable)
  }

  // }
}

async function handleDelete(row) {
  let res = await proxy.$api.login_manger.delete(row.loginId);
  refreshTableData();
  proxy.$modal.msgSuccess(res.message);
}

async function handleExport() {
  try {
    await proxy.$api.login_manger.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "excel", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}

async function downloadTemplate() {
  try {
    await proxy.$api.login_manger.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "downloadTemplate", "get")
  }catch (error) {
    console.error("导出失败", error)
  }
}


function submitForm() {
  if (dialogStatus.value == 'detail') {
    dialogVisible.value = false;
  } else {
    proxy.$refs.dataFormRef.validate(async (valid) => {


      if (valid) {
        let res = await proxy.$api.login_manger[dialogStatus.value](form.value);
        proxy.$modal.msgSuccess(res.message);
        refreshTableData();
        dialogVisible.value = false;
      }
    });
  }
}

function choose(item) {
  form.value = Object.assign({}, item);
  dialogVisible.value = true;
  dialogStatus.value = 'update';
}


const update_password_form = ref({})
const update_password_dialog = ref(false)
const showDialog = (login_id) => {
  update_password_form.value.loginId = login_id
  update_password_dialog.value = true

}

const refreshTableUpdateData = () => {
  update_password_form.value={}
  update_password_dialog.value = false;
}
/**
 * 更新
 * @returns {Promise<void>}
 */
const update_password_function = async () => {
  proxy.$refs.dataFormRef1.validate(async (valid) => {
    if (valid) {
      console.log(update_password_form.value)
      let res = await proxy.$api.login_manger.update_password(update_password_form.value);
      proxy.$modal.msgSuccess(res.message);
      refreshTableUpdateData();
      update_password_dialog.value = false;
    }
  });
}


</script>

<style lang="scss" scoped></style>