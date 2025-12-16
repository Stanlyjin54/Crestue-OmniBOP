<template>
  <div class="refund-container">
    <div class="refund-header">
      <h2>申请退款</h2>
      <p class="refund-tips">请填写退款原因，我们将尽快为您处理</p>
    </div>
    
    <div class="refund-form">
      <el-form
        ref="refundFormRef"
        :model="refundForm"
        :rules="refundRules"
        label-width="120px"
        v-loading="loading"
      >
        <el-form-item label="订单信息">
          <div class="order-info">
            <div class="info-item">
              <span class="label">订单号：</span>
              <span class="value">{{ orderInfo.orderNo }}</span>
            </div>
            <div class="info-item">
              <span class="label">订单金额：</span>
              <span class="value amount">￥{{ orderInfo.amount }}</span>
            </div>
            <div class="info-item">
              <span class="label">支付时间：</span>
              <span class="value">{{ formatDate(orderInfo.paymentTime) }}</span>
            </div>
          </div>
        </el-form-item>
        
        <el-form-item label="退款金额" prop="amount">
          <el-input-number
            v-model="refundForm.amount"
            :min="0.01"
            :max="maxRefundAmount"
            :precision="2"
            :step="0.01"
            controls-position="right"
          />
          <span class="refund-amount-tips">最多可退 ￥{{ maxRefundAmount }}</span>
        </el-form-item>
        
        <el-form-item label="退款原因" prop="reason">
          <el-select v-model="refundForm.reason" placeholder="请选择退款原因">
            <el-option label="商品质量问题" value="商品质量问题" />
            <el-option label="商品与描述不符" value="商品与描述不符" />
            <el-option label="发错货" value="发错货" />
            <el-option label="物流问题" value="物流问题" />
            <el-option label="个人原因" value="个人原因" />
            <el-option label="其他原因" value="其他原因" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="详细说明" prop="description">
          <el-input
            v-model="refundForm.description"
            type="textarea"
            :rows="4"
            placeholder="请详细说明退款原因，有助于我们更快地处理您的申请"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="凭证图片" prop="images">
          <el-upload
            ref="uploadRef"
            v-model:file-list="fileList"
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :limit="5"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :on-change="handleChange"
            accept="image/*"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div class="upload-tips">支持jpg、png格式，单张不超过5MB，最多5张</div>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitRefund" :loading="submitLoading">
            提交退款申请
          </el-button>
          <el-button @click="goBack">返回</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 图片预览对话框 -->
    <el-dialog v-model="dialogVisible" title="图片预览">
      <img w-full :src="dialogImageUrl" alt="预览图片" />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
// 图标已在main.ts中全局注册，无需单独导入
import { getOrderDetail as getOrderDetailAPI } from '@/api/order'
import { requestRefund } from '@/api/payment'
import type { UploadFile, UploadFiles } from 'element-plus'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const submitLoading = ref(false)
const refundFormRef = ref()

const orderInfo = reactive({
  id: 0,
  orderNo: '',
  amount: 0,
  paymentTime: '',
  status: ''
})

const refundForm = reactive({
  orderId: 0,
  amount: 0,
  reason: '',
  description: '',
  images: [] as string[]
})

const maxRefundAmount = ref(0)
const fileList = ref<UploadFile[]>([])
const dialogVisible = ref(false)
const dialogImageUrl = ref('')

const refundRules: Record<string, any> = {
  amount: [
    { required: true, message: '请输入退款金额', trigger: 'blur' },
    { type: 'number', min: 0.01, message: '退款金额必须大于0', trigger: 'blur' }
  ],
  reason: [
    { required: true, message: '请选择退款原因', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请详细说明退款原因', trigger: 'blur' },
    { min: 10, max: 500, message: '退款说明长度应在10-500个字符之间', trigger: 'blur' }
  ]
}

const formatDate = (dateString: string) => {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleString('zh-CN')
}

const getOrderDetail = async () => {
  try {
    loading.value = true
    const orderId = route.query.orderId as string
    if (!orderId) {
      ElMessage.error('订单ID不存在')
      router.push('/order/list')
      return
    }
    
    const response = await getOrderDetailAPI(parseInt(orderId))
    Object.assign(orderInfo, response)
    refundForm.orderId = response.id
    maxRefundAmount.value = response.totalAmount
    refundForm.amount = maxRefundAmount.value
  } catch (error) {
    ElMessage.error('获取订单详情失败')
    router.push('/order/list')
  } finally {
    loading.value = false
  }
}

const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url!
  dialogVisible.value = true
}

const handleRemove = (file: UploadFile) => {
  console.log('移除文件:', file)
}

const handleChange = (file: UploadFile, files: UploadFiles) => {
  // 这里可以实现图片上传逻辑
  console.log('文件变化:', file, files)
}

const submitRefund = async () => {
  if (!refundFormRef.value) return
  
  await refundFormRef.value.validate(async (valid: boolean) => {
    if (!valid) return
    
    try {
      submitLoading.value = true
      
      // 确认退款申请
      await ElMessageBox.confirm(
        `确认申请退款 ￥${refundForm.amount} 吗？退款申请提交后将无法撤销。`,
        '确认退款',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
      
      // 提交退款申请
      const response = await requestRefund({
        orderId: refundForm.orderId,
        amount: refundForm.amount,
        reason: `${refundForm.reason} - ${refundForm.description}`
      })
      
      if (response) {
        ElMessage.success('退款申请已提交，我们将尽快为您处理')
        router.push('/order/list')
      } else {
        ElMessage.error('退款申请提交失败，请稍后重试')
      }
    } catch (error) {
      if (error !== 'cancel') {
        ElMessage.error('退款申请提交失败')
      }
    } finally {
      submitLoading.value = false
    }
  })
}

const goBack = () => {
  router.push('/order/list')
}

onMounted(() => {
  getOrderDetail()
})
</script>

<style scoped lang="scss">
.refund-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.refund-header {
  text-align: center;
  margin-bottom: 30px;
  
  h2 {
    color: #333;
    margin-bottom: 10px;
  }
  
  .refund-tips {
    color: #666;
    font-size: 14px;
  }
}

.refund-form {
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.order-info {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 6px;
  
  .info-item {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .label {
      color: #666;
      font-weight: 500;
    }
    
    .value {
      color: #333;
      font-weight: bold;
      
      &.amount {
        color: #f56c6c;
        font-size: 16px;
      }
    }
  }
}

.refund-amount-tips {
  margin-left: 10px;
  color: #999;
  font-size: 12px;
}

.upload-tips {
  margin-top: 8px;
  color: #999;
  font-size: 12px;
}
</style>