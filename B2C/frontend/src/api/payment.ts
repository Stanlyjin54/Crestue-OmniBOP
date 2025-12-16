import request from '@/utils/request'

export interface PaymentRequest {
  orderId: number
  paymentMethod: 'wechat' | 'alipay'
  amount: number
}

export interface PaymentResponse {
  paymentId: string
  qrCode?: string
  paymentUrl?: string
  status: 'pending' | 'paid' | 'failed'
}

export interface PaymentStatusResponse {
  status: 'pending' | 'paid' | 'failed' | 'cancelled'
  paymentId: string
  paidAt?: string
}

export interface RefundRequest {
  orderId: number
  amount: number
  reason: string
}

export interface RefundResponse {
  refundId: string
  status: 'pending' | 'success' | 'failed'
}

/**
 * 创建支付订单
 */
export const createPayment = (data: PaymentRequest) => {
  return request.post<PaymentResponse>('/generalOrder/createPayment', data)
}

/**
 * 查询支付状态
 */
export const getPaymentStatus = (orderId: number) => {
  return request.get<PaymentStatusResponse>(`/generalOrder/getPaymentStatus?orderId=${orderId}`)
}

/**
 * 发起支付
 */
export const initiatePayment = (paymentMethod: string, orderId: number, amount: number) => {
  return request.post<PaymentResponse>('/generalOrder/initiatePayment', {
    paymentMethod,
    orderId,
    amount
  })
}

/**
 * 处理支付回调
 */
export const handlePaymentCallback = (data: any) => {
  return request.post('/generalOrder/handlePaymentCallback', data)
}

/**
 * 申请退款
 */
export const requestRefund = (data: RefundRequest) => {
  return request.post<RefundResponse>('/generalOrder/requestRefund', data)
}

/**
 * 查询退款状态
 */
export const getRefundStatus = (refundId: string) => {
  return request.get(`/generalOrder/getRefundStatus?refundId=${refundId}`)
}

/**
 * 获取可用的支付方式
 */
export const getAvailablePaymentMethods = () => {
  return request.get<string[]>('/generalOrder/payMethod')
}

/**
 * 检查支付状态（轮询用）
 */
export const checkPayment = (orderId: number) => {
  return request.get<PaymentStatusResponse>(`/generalOrder/checkPayment?orderId=${orderId}`)
}