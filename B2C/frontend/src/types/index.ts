export interface User {
  id: number
  username: string
  email: string
  phone: string
  avatar: string
  nickname: string
  gender: number
  birthday: string
  bio: string
  level: number
  points: number
  status: number
  createdAt: string
  updatedAt: string
}

export interface UserStats {
  orderCount: number
  totalSpent: number
  favoriteCount: number
  couponCount: number
  points: number
}

export interface Product {
  id: number
  name: string
  description: string
  image: string
  images: string[]
  price: number
  originalPrice: number
  stock: number
  sales: number
  categoryId: number
  categoryName: string
  brand: string
  specs: ProductSpec[]
  skus: ProductSku[]
  attributes: ProductAttribute[]
  status: number
  createdAt: string
  updatedAt: string
  // 扩展字段
  mainImage?: string
  tags?: string[]
  rating?: number
  reviewCount?: number
  // 详情页字段
  subtitle?: string
  discount?: number
  detail?: string
  hasSku?: boolean
}

export interface ProductSpec {
  id: number
  name: string
  values: string[]
}

export interface ProductSku {
  id: number
  skuCode: string
  price: number
  stock: number
  specs: Record<string, string>
  image: string
}

export interface Review {
  id: number
  userId: number
  userName: string
  userAvatar: string
  rating: number
  content: string
  images: string[]
  specs: Record<string, string>
  isLiked: boolean
  likeCount: number
  replyCount: number
  createdAt: string
}

export interface ProductAttribute {
  id: number
  name: string
  value: string
}

export interface CartItem {
  id: number
  productId: number
  skuId: number
  quantity: number
  price: number
  productName: string
  productImage: string
  skuName: string
  stock: number
  selected: boolean
}

export interface Order {
  id: number
  orderNo: string
  userId: number
  totalAmount: number
  status: string
  paymentMethod: string
  paymentStatus: string
  paymentTime: string
  shippingAddress: Address
  items: OrderItem[]
  createdAt: string
  updatedAt: string
}

export interface OrderItem {
  id: number
  orderId: number
  productId: number
  skuId: number
  productName: string
  productImage: string
  skuName: string
  price: number
  quantity: number
  totalAmount: number
}

export interface Address {
  id: number
  userId: number
  name: string
  phone: string
  province: string
  city: string
  district: string
  detail: string
  postalCode: string
  isDefault: boolean
  createdAt: string
  updatedAt: string
}

export interface Favorite {
  id: number
  userId: number
  productId: number
  product: Product
  createdAt: string
}

export interface Category {
  id: number
  name: string
  icon: string
  sort: number
  parentId: number
  level: number
  children: Category[]
  createdAt: string
  updatedAt: string
}

export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

export interface PageRequest {
  page?: number
  pageSize?: number
}

export interface PageResponse<T> {
  list: T[]
  total: number
  current: number
  size: number
}

export interface PageData<T> {
  items: T[]
  total: number
  page: number
  pageSize: number
}

export interface UserInfo {
  id: number
  username: string
  email: string
  phone: string
  avatar: string
  nickname: string
  gender: number
  birthday: string
  bio: string
  level: number
  points: number
  status: number
  createdAt: string
  updatedAt: string
}

export interface LoginRequest {
  username: string
  password: string
  remember?: boolean
}

export interface LoginResponse {
  token: string
  user: UserInfo
}

export interface RegisterRequest {
  username: string
  password: string
  email: string
  phone: string
  captcha?: string
}

export interface LoginForm {
  username: string
  password: string
  remember: boolean
}

export interface RegisterForm {
  username: string
  email: string
  phone: string
  password: string
  confirmPassword: string
  captcha: string
}

export interface UserUpdateForm {
  nickname: string
  email: string
  phone: string
  gender: number
  birthday: string
  bio: string
}

export interface PasswordUpdateForm {
  oldPassword: string
  newPassword: string
  confirmPassword: string
}

export interface ProductQuery {
  keyword?: string
  search?: string
  categoryId?: number
  brand?: string
  minPrice?: number
  maxPrice?: number
  sort?: string
  order?: 'asc' | 'desc'
  page?: number
  pageSize?: number
}

export interface CartAddForm {
  productId: number
  skuId: number
  quantity: number
}

export interface CartItemUpdate {
  quantity: number
}

export interface CartUpdateForm {
  quantity: number
  selected?: boolean
}

export interface OrderCreateForm {
  addressId: number
  paymentMethod: string
  items: {
    productId: number
    skuId: number
    quantity: number
    price: number
  }[]
}

export interface AddressForm {
  name: string
  phone: string
  province: string
  city: string
  district: string
  detail: string
  postalCode?: string
  isDefault: boolean
}

export interface PaymentForm {
  orderId: number
  paymentMethod: string
  amount: number
}

// 兼容性别名（用于某些组件的导入）
export type Sku = ProductSku
export type Spec = ProductSpec

// 订单相关类型
export interface OrderCreateResponse {
  id: number
  orderNo: string
}