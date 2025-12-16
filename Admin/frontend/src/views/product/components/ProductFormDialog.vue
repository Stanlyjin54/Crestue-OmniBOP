<template>
  <el-dialog
    v-model="visible"
    :title="isEdit ? '编辑商品' : '新增商品'"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
      class="product-form"
    >
      <!-- 基本信息 -->
      <el-divider content-position="left">基本信息</el-divider>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="商品名称" prop="name">
            <el-input v-model="formData.name" placeholder="请输入商品名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="条形码" prop="barcode">
            <el-input v-model="formData.barcode" placeholder="请输入条形码" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="商品分类" prop="category_id">
            <el-select v-model="formData.category_id" placeholder="请选择分类" style="width: 100%">
              <el-option
                v-for="category in categories"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="品牌" prop="brand_id">
            <el-select v-model="formData.brand_id" placeholder="请选择品牌" style="width: 100%">
              <el-option
                v-for="brand in brands"
                :key="brand.id"
                :label="brand.name"
                :value="brand.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="单位" prop="unit">
            <el-input v-model="formData.unit" placeholder="如：台、件、个、箱" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="序列号" prop="serial_number">
            <el-input v-model="formData.serial_number" placeholder="请输入序列号" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="商品描述" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入商品描述"
        />
      </el-form-item>

      <!-- 商品图片 -->
      <el-divider content-position="left">商品图片</el-divider>
      
      <el-form-item label="商品图片" prop="images">
        <div class="image-upload-container">
          <div
            v-for="(image, index) in formData.images"
            :key="index"
            class="image-item"
          >
            <img :src="image" class="uploaded-image" />
            <div class="image-actions">
              <el-button
                type="danger"
                circle
                size="small"
                @click="removeImage(index)"
              >
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
          </div>
          <div
            v-if="formData.images.length < 5"
            class="image-upload-trigger"
            @click="triggerUpload"
          >
            <el-icon><Plus /></el-icon>
            <span>上传图片</span>
          </div>
        </div>
        <input
          ref="fileInput"
          type="file"
          accept="image/*"
          multiple
          style="display: none"
          @change="handleImageUpload"
        />
        <div class="upload-tips">最多可上传5张图片，建议尺寸：800x800px</div>
      </el-form-item>

      <!-- 规格信息 -->
      <el-divider content-position="left">规格信息</el-divider>
      
      <div class="specifications-section">
        <div class="spec-header">
          <el-button type="primary" size="small" @click="addSpecification">
            <el-icon><Plus /></el-icon>
            添加规格
          </el-button>
          <el-button type="info" size="small" @click="generateSkus">
            <el-icon><Refresh /></el-icon>
            生成SKU
          </el-button>
        </div>
        
        <div v-if="formData.specifications.length > 0" class="spec-list">
          <div
            v-for="(spec, index) in formData.specifications"
            :key="index"
            class="spec-item"
          >
            <el-row :gutter="10">
              <el-col :span="6">
                <el-input v-model="spec.name" placeholder="规格名称" />
              </el-col>
              <el-col :span="6">
                <el-select v-model="spec.type" placeholder="规格类型">
                  <el-option label="颜色" value="color" />
                  <el-option label="尺寸" value="size" />
                  <el-option label="重量" value="weight" />
                  <el-option label="材质" value="material" />
                  <el-option label="其他" value="other" />
                </el-select>
              </el-col>
              <el-col :span="8">
                <el-input
                  v-model="spec.values"
                  placeholder="规格值，用逗号分隔"
                />
              </el-col>
              <el-col :span="4">
                <el-checkbox v-model="spec.required">必填</el-checkbox>
              </el-col>
              <el-col :span="2">
                <el-button
                  type="danger"
                  size="small"
                  circle
                  @click="removeSpecification(index)"
                >
                  <el-icon><Delete /></el-icon>
                </el-button>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>

      <!-- SKU信息 -->
      <el-divider content-position="left">SKU信息</el-divider>
      
      <div v-if="formData.skus.length > 0" class="skus-section">
        <el-table :data="formData.skus" style="width: 100%">
          <el-table-column label="SKU编码" width="150">
            <template #default="{ row }">
              <el-input v-model="row.code" placeholder="SKU编码" />
            </template>
          </el-table-column>
          <el-table-column label="规格" min-width="200">
            <template #default="{ row }">
              <div v-for="spec in row.specifications" :key="spec.name" class="sku-spec">
                <span class="spec-name">{{ spec.name }}:</span>
                <span class="spec-value">{{ spec.value }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="价格" width="120">
            <template #default="{ row }">
              <el-input-number
                v-model="row.price"
                :precision="2"
                :min="0"
                controls-position="right"
                style="width: 100px"
              />
            </template>
          </el-table-column>
          <el-table-column label="库存" width="120">
            <template #default="{ row }">
              <el-input-number
                v-model="row.stock"
                :min="0"
                controls-position="right"
                style="width: 100px"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template #default="{ $index }">
              <el-button
                type="danger"
                size="small"
                circle
                @click="removeSku($index)"
              >
                <el-icon><Delete /></el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 库存信息 -->
      <el-divider content-position="left">库存信息</el-divider>
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="安全库存" prop="safety_stock">
            <el-input-number
              v-model="formData.safety_stock"
              :min="0"
              controls-position="right"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="保质期(天)" prop="shelf_life">
            <el-input-number
              v-model="formData.shelf_life"
              :min="0"
              controls-position="right"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="商品状态" prop="status">
            <el-select v-model="formData.status" placeholder="请选择状态" style="width: 100%">
              <el-option
                v-for="status in Object.values(PRODUCT_STATUS)"
                :key="status.value"
                :label="status.label"
                :value="status.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 备注信息 -->
      <el-divider content-position="left">备注信息</el-divider>
      
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">
        保存
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { PRODUCT_STATUS } from '@/composables/useProduct'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  product: {
    type: Object,
    default: null
  },
  categories: {
    type: Array,
    default: () => []
  },
  brands: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['update:modelValue', 'submit'])

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const formRef = ref()
const fileInput = ref()
const submitting = ref(false)

const formData = reactive({
  id: null,
  name: '',
  barcode: '',
  category_id: '',
  brand_id: '',
  unit: '',
  description: '',
  images: [],
  serial_number: '',
  shelf_life: null,
  safety_stock: 0,
  status: 'active',
  specifications: [],
  skus: [],
  remark: ''
})

const rules = {
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  barcode: [
    { required: true, message: '请输入条形码', trigger: 'blur' }
  ],
  category_id: [
    { required: true, message: '请选择商品分类', trigger: 'change' }
  ],
  brand_id: [
    { required: true, message: '请选择品牌', trigger: 'change' }
  ],
  unit: [
    { required: true, message: '请输入单位', trigger: 'blur' }
  ],
  safety_stock: [
    { required: true, message: '请输入安全库存', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择商品状态', trigger: 'change' }
  ]
}

const isEdit = computed(() => !!props.product)

// 监听商品数据变化
watch(() => props.product, (newVal) => {
  if (newVal) {
    Object.assign(formData, {
      ...newVal,
      images: newVal.images || [],
      specifications: newVal.specifications || [],
      skus: newVal.skus || []
    })
  } else {
    resetForm()
  }
}, { immediate: true })

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    id: null,
    name: '',
    barcode: '',
    category_id: '',
    brand_id: '',
    unit: '',
    description: '',
    images: [],
    serial_number: '',
    shelf_life: null,
    safety_stock: 0,
    status: 'active',
    specifications: [],
    skus: [],
    remark: ''
  })
}

// 图片上传
const triggerUpload = () => {
  fileInput.value.click()
}

const handleImageUpload = (event) => {
  const files = Array.from(event.target.files)
  if (files.length === 0) return

  files.forEach(file => {
    if (formData.images.length >= 5) {
      ElMessage.warning('最多只能上传5张图片')
      return
    }

    // 模拟图片上传
    const reader = new FileReader()
    reader.onload = (e) => {
      formData.images.push(e.target.result)
    }
    reader.readAsDataURL(file)
  })

  // 清空input
  event.target.value = ''
}

const removeImage = (index) => {
  formData.images.splice(index, 1)
}

// 规格管理
const addSpecification = () => {
  formData.specifications.push({
    name: '',
    type: 'other',
    values: '',
    required: false
  })
}

const removeSpecification = (index) => {
  formData.specifications.splice(index, 1)
}

// SKU生成
const generateSkus = () => {
  if (formData.specifications.length === 0) {
    formData.skus = [{
      id: Date.now(),
      code: formData.barcode || '',
      specifications: [],
      price: 0,
      stock: 0,
      image: ''
    }]
    return
  }

  // 解析规格值
  const specs = formData.specifications.map(spec => ({
    ...spec,
    values: spec.values ? spec.values.split(',').map(v => v.trim()).filter(v => v) : []
  })).filter(spec => spec.values.length > 0)

  if (specs.length === 0) {
    ElMessage.warning('请先设置规格值')
    return
  }

  // 生成组合
  const combinations = []
  const generateCombinations = (current, index) => {
    if (index === specs.length) {
      combinations.push([...current])
      return
    }
    
    specs[index].values.forEach(value => {
      generateCombinations([...current, {
        name: specs[index].name,
        value: value,
        type: specs[index].type
      }], index + 1)
    })
  }

  generateCombinations([], 0)

  formData.skus = combinations.map((combo, index) => ({
    id: Date.now() + index,
    code: `${formData.barcode}-${index + 1}`,
    specifications: combo,
    price: 0,
    stock: 0,
    image: ''
  }))
}

const removeSku = (index) => {
  formData.skus.splice(index, 1)
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    // 验证规格
    for (let i = 0; i < formData.specifications.length; i++) {
      const spec = formData.specifications[i]
      if (!spec.name || !spec.values) {
        ElMessage.error(`第${i + 1}个规格信息不完整`)
        return
      }
    }

    // 验证SKU
    for (let i = 0; i < formData.skus.length; i++) {
      const sku = formData.skus[i]
      if (!sku.code || sku.price < 0 || sku.stock < 0) {
        ElMessage.error(`第${i + 1}个SKU信息不完整`)
        return
      }
    }

    submitting.value = true
    
    // 处理规格数据
    const processedSpecs = formData.specifications.map(spec => ({
      ...spec,
      values: spec.values ? spec.values.split(',').map(v => v.trim()).filter(v => v) : []
    }))

    const submitData = {
      ...formData,
      specifications: processedSpecs
    }

    emit('submit', submitData)
    
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    submitting.value = false
  }
}

// 关闭对话框
const handleClose = () => {
  formRef.value.resetFields()
  resetForm()
  visible.value = false
}
</script>

<style lang="scss" scoped>
.product-form {
  max-height: 70vh;
  overflow-y: auto;
  padding-right: 10px;
}

.image-upload-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  
  .image-item {
    position: relative;
    width: 100px;
    height: 100px;
    border: 1px solid #DCDFE6;
    border-radius: 4px;
    overflow: hidden;
    
    .uploaded-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    
    .image-actions {
      position: absolute;
      top: 4px;
      right: 4px;
      opacity: 0;
      transition: opacity 0.3s;
    }
    
    &:hover .image-actions {
      opacity: 1;
    }
  }
  
  .image-upload-trigger {
    width: 100px;
    height: 100px;
    border: 2px dashed #DCDFE6;
    border-radius: 4px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.3s;
    
    &:hover {
      border-color: #409EFF;
      color: #409EFF;
    }
    
    .el-icon {
      font-size: 24px;
      margin-bottom: 4px;
    }
    
    span {
      font-size: 12px;
      color: #909399;
    }
  }
}

.upload-tips {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}

.specifications-section {
  .spec-header {
    display: flex;
    gap: 10px;
    margin-bottom: 15px;
  }
  
  .spec-list {
    .spec-item {
      margin-bottom: 10px;
      padding: 10px;
      background: #F5F7FA;
      border-radius: 4px;
    }
  }
}

.skus-section {
  .sku-spec {
    display: flex;
    align-items: center;
    margin-bottom: 4px;
    
    .spec-name {
      font-weight: 500;
      margin-right: 4px;
      color: #606266;
    }
    
    .spec-value {
      color: #303133;
    }
  }
}
</style>