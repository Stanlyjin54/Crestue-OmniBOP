<template>
  <el-dialog
    :model-value="modelValue"
    :title="title"
    width="500px"
    @update:model-value="$emit('update:modelValue', $event)"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formDataLocal"
      label-width="80px"
      @submit.prevent="handleSubmit"
    >
      <el-form-item label="角色名称" prop="name">
        <el-input v-model="formDataLocal.name" placeholder="请输入角色名称" />
      </el-form-item>

      <el-form-item label="角色代码" prop="code">
        <el-input v-model="formDataLocal.code" placeholder="请输入角色代码" :disabled="!!formData.id" />
      </el-form-item>

      <el-form-item label="角色描述" prop="description">
        <el-input
          v-model="formDataLocal.description"
          type="textarea"
          :rows="3"
          placeholder="请输入角色描述"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: '角色配置'
  },
  formData: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['update:modelValue', 'submit'])

const formRef = ref()
const formDataLocal = ref({})

watch(
  () => props.formData,
  (newVal) => {
    formDataLocal.value = { ...newVal }
  },
  { immediate: true, deep: true }
)

const handleClose = () => {
  emit('update:modelValue', false)
}

const handleSubmit = async () => {
  emit('submit', formDataLocal.value)
  handleClose()
}
</script>
