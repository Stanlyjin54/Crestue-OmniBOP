<template>
  <el-form
    ref="formRef"
    :model="formData"
    :rules="rules"
    label-width="100px"
  >
    <el-form-item label="收货人" prop="name">
      <el-input 
        v-model="formData.name" 
        placeholder="请输入收货人姓名"
        maxlength="20"
        show-word-limit
      />
    </el-form-item>

    <el-form-item label="手机号" prop="phone">
      <el-input 
        v-model="formData.phone" 
        placeholder="请输入手机号码"
        maxlength="11"
      />
    </el-form-item>

    <el-form-item label="所在地区" prop="region">
      <el-cascader
        v-model="formData.region"
        :options="regionOptions"
        :props="cascaderProps"
        placeholder="请选择省市区"
        style="width: 100%"
        @change="handleRegionChange"
      />
    </el-form-item>

    <el-form-item label="详细地址" prop="detail">
      <el-input
        v-model="formData.detail"
        type="textarea"
        :rows="3"
        placeholder="请输入详细地址，如街道、门牌号等"
        maxlength="200"
        show-word-limit
      />
    </el-form-item>

    <el-form-item label="邮政编码" prop="postalCode">
      <el-input 
        v-model="formData.postalCode" 
        placeholder="请输入邮政编码（选填）"
        maxlength="6"
      />
    </el-form-item>

    <el-form-item label="设为默认" prop="isDefault">
      <el-switch 
        v-model="formData.isDefault"
        active-text="是"
        inactive-text="否"
      />
      <span class="form-tip">设置后该地址将作为默认收货地址</span>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        保存地址
      </el-button>
      <el-button @click="handleCancel">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { ref, reactive, watch, nextTick } from 'vue'
import type { Address } from '@/types'
import type { FormInstance, FormRules, CascaderValue } from 'element-plus'

interface Props {
  address?: Address | null
}

interface Emits {
  (e: 'save', address: Address): void
  (e: 'cancel'): void
}

const props = withDefaults(defineProps<Props>(), {
  address: null
})

const emit = defineEmits<Emits>()

const formRef = ref<FormInstance>()
const loading = ref(false)

// 表单数据
const formData = reactive({
  name: '',
  phone: '',
  region: [] as string[],
  province: '',
  city: '',
  district: '',
  detail: '',
  postalCode: '',
  isDefault: false
})

// 表单验证规则
const rules: FormRules = {
  name: [
    { required: true, message: '请输入收货人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度为2-20个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  region: [
    { required: true, message: '请选择所在地区', trigger: 'change' }
  ],
  detail: [
    { required: true, message: '请输入详细地址', trigger: 'blur' },
    { min: 5, max: 200, message: '详细地址长度为5-200个字符', trigger: 'blur' }
  ],
  postalCode: [
    { pattern: /^\d{6}$/, message: '请输入正确的邮政编码', trigger: 'blur' }
  ]
}

// 地区选择器配置
const cascaderProps = {
  expandTrigger: 'hover' as const,
  checkStrictly: false,
  emitPath: true,
  value: 'code',
  label: 'name',
  children: 'children'
}

// 模拟地区数据（实际项目中应该从API获取）
const regionOptions = ref([
  {
    code: '110000',
    name: '北京市',
    children: [
      {
        code: '110100',
        name: '北京市',
        children: [
          { code: '110101', name: '东城区' },
          { code: '110102', name: '西城区' },
          { code: '110105', name: '朝阳区' },
          { code: '110106', name: '丰台区' },
          { code: '110107', name: '石景山区' },
          { code: '110108', name: '海淀区' },
          { code: '110109', name: '门头沟区' },
          { code: '110111', name: '房山区' },
          { code: '110112', name: '通州区' },
          { code: '110113', name: '顺义区' },
          { code: '110114', name: '昌平区' },
          { code: '110115', name: '大兴区' },
          { code: '110116', name: '怀柔区' },
          { code: '110117', name: '平谷区' },
          { code: '110118', name: '密云区' },
          { code: '110119', name: '延庆区' }
        ]
      }
    ]
  },
  {
    code: '310000',
    name: '上海市',
    children: [
      {
        code: '310100',
        name: '上海市',
        children: [
          { code: '310101', name: '黄浦区' },
          { code: '310104', name: '徐汇区' },
          { code: '310105', name: '长宁区' },
          { code: '310106', name: '静安区' },
          { code: '310107', name: '普陀区' },
          { code: '310109', name: '虹口区' },
          { code: '310110', name: '杨浦区' },
          { code: '310112', name: '闵行区' },
          { code: '310113', name: '宝山区' },
          { code: '310114', name: '嘉定区' },
          { code: '310115', name: '浦东新区' },
          { code: '310116', name: '金山区' },
          { code: '310117', name: '松江区' },
          { code: '310118', name: '青浦区' },
          { code: '310120', name: '奉贤区' },
          { code: '310151', name: '崇明区' }
        ]
      }
    ]
  },
  {
    code: '440000',
    name: '广东省',
    children: [
      {
        code: '440100',
        name: '广州市',
        children: [
          { code: '440103', name: '荔湾区' },
          { code: '440104', name: '越秀区' },
          { code: '440105', name: '海珠区' },
          { code: '440106', name: '天河区' },
          { code: '440111', name: '白云区' },
          { code: '440112', name: '黄埔区' },
          { code: '440113', name: '番禺区' },
          { code: '440114', name: '花都区' },
          { code: '440115', name: '南沙区' },
          { code: '440117', name: '从化区' },
          { code: '440118', name: '增城区' }
        ]
      },
      {
        code: '440300',
        name: '深圳市',
        children: [
          { code: '440303', name: '罗湖区' },
          { code: '440304', name: '福田区' },
          { code: '440305', name: '南山区' },
          { code: '440306', name: '宝安区' },
          { code: '440307', name: '龙岗区' },
          { code: '440308', name: '盐田区' },
          { code: '440309', name: '龙华区' },
          { code: '440310', name: '坪山区' },
          { code: '440311', name: '光明区' },
          { code: '440312', name: '大鹏新区' }
        ]
      }
    ]
  }
])

// 监听地址数据变化，初始化表单
watch(() => props.address, (newAddress) => {
  if (newAddress) {
    // 编辑模式
    Object.assign(formData, {
      name: newAddress.name,
      phone: newAddress.phone,
      region: [], // 需要根据省市区数据反向查找code
      province: newAddress.province,
      city: newAddress.city,
      district: newAddress.district,
      detail: newAddress.detail,
      postalCode: newAddress.postalCode || '',
      isDefault: newAddress.isDefault
    })
    
    // 根据省市区名称反向查找对应的code（这里简化处理）
    nextTick(() => {
      // 实际项目中需要根据省市区名称从完整地区数据中查找对应的code
      // 这里只是示例，假设能找到对应的code
      if (newAddress.province && newAddress.city && newAddress.district) {
        // 这里需要根据实际地区数据结构来查找对应的code数组
        // 暂时留空，实际项目中需要实现完整的地区数据匹配逻辑
      }
    })
  } else {
    // 新增模式，重置表单
    resetForm()
  }
}, { immediate: true })

// 处理地区选择变化
const handleRegionChange = (value: CascaderValue | null | undefined) => {
  // 确保value是数组类型
  if (!Array.isArray(value)) return
  
  if (value.length >= 1) {
    const provinceOption = regionOptions.value.find(item => item.code === value[0])
    formData.province = provinceOption?.name || ''
  }
  
  if (value.length >= 2) {
    const cityOption = regionOptions.value
      .find(item => item.code === value[0])?.children
      ?.find(item => item.code === value[1])
    formData.city = cityOption?.name || ''
  }
  
  if (value.length >= 3) {
    const districtOption = regionOptions.value
      .find(item => item.code === value[0])?.children
      ?.find(item => item.code === value[1])?.children
      ?.find(item => item.code === value[2])
    formData.district = districtOption?.name || ''
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    name: '',
    phone: '',
    region: [],
    province: '',
    city: '',
    district: '',
    detail: '',
    postalCode: '',
    isDefault: false
  })
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
    loading.value = true
    
    // 构建地址数据
    const addressData: Address = {
      id: props.address?.id || 0,
      name: formData.name,
      phone: formData.phone,
      province: formData.province,
      city: formData.city,
      district: formData.district,
      detail: formData.detail,
      postalCode: formData.postalCode,
      isDefault: formData.isDefault,
      userId: 0, // 实际项目中应该从用户信息中获取
      createdAt: props.address?.createdAt || new Date().toISOString(),
      updatedAt: new Date().toISOString()
    }
    
    emit('save', addressData)
    
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    loading.value = false
  }
}

// 取消
const handleCancel = () => {
  emit('cancel')
}

// 暴露方法给父组件
defineExpose({
  resetForm
})
</script>

<style scoped lang="scss">
.form-tip {
  margin-left: 10px;
  color: #909399;
  font-size: 12px;
}

.el-cascader {
  width: 100%;
}

.el-textarea {
  width: 100%;
}
</style>