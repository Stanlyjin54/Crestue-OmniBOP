<template>
  <div class="addresses-container">
    <div class="addresses-header">
      <h2>收货地址</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增地址
      </el-button>
    </div>
    
    <div class="addresses-content" v-loading="loading">
      <div v-if="addresses.length === 0" class="empty-addresses">
        <el-empty description="暂无收货地址" />
      </div>
      
      <div v-else class="addresses-list">
        <div 
          v-for="address in addresses" 
          :key="address.id" 
          class="address-card"
          :class="{ 'is-default': address.isDefault }"
        >
          <div class="address-header">
            <div class="address-info">
              <span class="name">{{ address.name }}</span>
              <span class="phone">{{ address.phone }}</span>
              <el-tag v-if="address.isDefault" type="success" size="small">默认</el-tag>
            </div>
            <div class="address-actions">
              <el-button 
                v-if="!address.isDefault" 
                type="text" 
                @click="setDefault(address)"
              >
                设为默认
              </el-button>
              <el-button type="text" @click="handleEdit(address)">编辑</el-button>
              <el-button type="text" @click="handleDelete(address)">删除</el-button>
            </div>
          </div>
          
          <div class="address-detail">
            {{ address.province }} {{ address.city }} {{ address.district }} {{ address.detail }}
          </div>
          
          <div v-if="address.postalCode" class="postal-code">
            邮编：{{ address.postalCode }}
          </div>
        </div>
      </div>
    </div>
    
    <!-- 地址编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑地址' : '新增地址'"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="addressFormRef"
        :model="addressForm"
        :rules="addressRules"
        label-width="80px"
      >
        <el-form-item label="收货人" prop="name">
          <el-input v-model="addressForm.name" placeholder="请输入收货人姓名" />
        </el-form-item>
        
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="addressForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        
        <el-form-item label="所在地区" prop="region">
          <el-cascader
            v-model="addressForm.region"
            :options="regionOptions"
            :props="{ value: 'code', label: 'name' }"
            placeholder="请选择省市区"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="详细地址" prop="detail">
          <el-input
            v-model="addressForm.detail"
            type="textarea"
            :rows="3"
            placeholder="请输入详细地址"
          />
        </el-form-item>
        
        <el-form-item label="邮政编码" prop="postalCode">
          <el-input v-model="addressForm.postalCode" placeholder="请输入邮政编码" />
        </el-form-item>
        
        <el-form-item label="设为默认" prop="isDefault">
          <el-switch v-model="addressForm.isDefault" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
// 图标已在main.ts中全局注册，无需单独导入
import { getAddressList, addAddress, updateAddress, deleteAddress, setDefaultAddress } from '@/api/address'
import type { Address } from '@/types'

const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)

const addresses = ref<Address[]>([])

const addressFormRef = ref()
const addressForm = reactive({
  id: undefined as number | undefined,
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

const addressRules = {
  name: [
    { required: true, message: '请输入收货人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度为2-20个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  region: [
    { required: true, message: '请选择省市区', trigger: 'change' }
  ],
  detail: [
    { required: true, message: '请输入详细地址', trigger: 'blur' },
    { min: 5, max: 200, message: '详细地址长度为5-200个字符', trigger: 'blur' }
  ],
  postalCode: [
    { pattern: /^\d{6}$/, message: '请输入正确的邮政编码', trigger: 'blur' }
  ]
}

// 模拟地区数据，实际项目中应该从后端获取或使用第三方库
const regionOptions = [
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
          { code: '110108', name: '海淀区' }
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
          { code: '310109', name: '虹口区' }
        ]
      }
    ]
  }
]

const fetchAddresses = async () => {
  try {
    loading.value = true
    const response = await getAddressList()
    addresses.value = response
  } catch (error) {
    ElMessage.error('获取地址列表失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (address: Address) => {
  isEdit.value = true
  Object.assign(addressForm, {
    id: address.id,
    name: address.name,
    phone: address.phone,
    region: [address.province, address.city, address.district],
    province: address.province,
    city: address.city,
    district: address.district,
    detail: address.detail,
    postalCode: address.postalCode || '',
    isDefault: address.isDefault
  })
  dialogVisible.value = true
}

const handleDelete = async (address: Address) => {
  try {
    await ElMessageBox.confirm('确定要删除该地址吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteAddress(address.id)
    ElMessage.success('地址删除成功')
    fetchAddresses()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('地址删除失败')
    }
  }
}

const setDefault = async (address: Address) => {
  try {
    await setDefaultAddress(address.id)
    ElMessage.success('设置默认地址成功')
    fetchAddresses()
  } catch (error) {
    ElMessage.error('设置默认地址失败')
  }
}

const submitForm = async () => {
  try {
    await addressFormRef.value.validate()
    submitLoading.value = true
    
    // 处理地区数据
    const [provinceCode, cityCode, districtCode] = addressForm.region
    const province = regionOptions.find(p => p.code === provinceCode)?.name || ''
    const city = regionOptions.find(p => p.code === provinceCode)
      ?.children?.find(c => c.code === cityCode)?.name || ''
    const district = regionOptions.find(p => p.code === provinceCode)
      ?.children?.find(c => c.code === cityCode)
      ?.children?.find(d => d.code === districtCode)?.name || ''
    
    const data = {
      ...addressForm,
      province,
      city,
      district
    }
    
    if (isEdit.value) {
      await updateAddress(data)
      ElMessage.success('地址修改成功')
    } else {
      await addAddress(data)
      ElMessage.success('地址添加成功')
    }
    
    dialogVisible.value = false
    fetchAddresses()
  } catch (error) {
    if (error !== false) {
      ElMessage.error(isEdit.value ? '地址修改失败' : '地址添加失败')
    }
  } finally {
    submitLoading.value = false
  }
}

const resetForm = () => {
  addressFormRef.value?.resetFields()
  Object.assign(addressForm, {
    id: undefined,
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

onMounted(() => {
  fetchAddresses()
})
</script>

<style scoped lang="scss">
.addresses-container {
  padding: 20px;
}

.addresses-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  
  h2 {
    margin: 0;
    font-size: 24px;
    font-weight: 500;
  }
}

.empty-addresses {
  padding: 60px 0;
  text-align: center;
}

.addresses-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
}

.address-card {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 20px;
  background: #fff;
  transition: all 0.3s ease;
  
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
  
  &.is-default {
    border-color: #67c23a;
    background: #f0f9ff;
  }
}

.address-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  
  .address-info {
    display: flex;
    align-items: center;
    gap: 10px;
    
    .name {
      font-weight: 500;
      font-size: 16px;
    }
    
    .phone {
      color: #666;
    }
  }
  
  .address-actions {
    display: flex;
    gap: 10px;
  }
}

.address-detail {
  margin-bottom: 8px;
  line-height: 1.5;
  color: #333;
}

.postal-code {
  color: #999;
  font-size: 14px;
}
</style>