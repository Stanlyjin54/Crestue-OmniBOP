// 图片全网信息获取API接口
import request from '@/utils/request'

export default {
  // 上传图片并获取全网信息
  uploadAndSearch: (data) => {
    return request({
      url: '/imageSearch/uploadAndSearch',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },
  
  // 根据URL获取图片全网信息
  searchByUrl: (params) => {
    return request({
      url: '/imageSearch/searchByUrl',
      method: 'get',
      params
    })
  },
  
  // 获取图片OCR结果
  getOcrResult: (data) => {
    return request({
      url: '/imageSearch/ocr',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }
}
