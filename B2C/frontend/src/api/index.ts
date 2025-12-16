
// 从各个API模块导入函数
export * from './user'
export * from './product'
export * from './order'
export * from './payment'
export * from './cart'

// 只导出address模块的特定函数，避免与order模块冲突
export { getAddressList as getUserAddressList, addAddress as addUserAddress, updateAddress as updateUserAddress, deleteAddress as deleteUserAddress, setDefaultAddress as setUserDefaultAddress } from './address'