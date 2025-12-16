import axios from 'axios';
import router from '@/router';
import {ElMessage, ElMessageBox} from 'element-plus';

// 创建axios实例
const service = axios.create({
    baseURL: import.meta.env.VITE_APP_SERVICE_API || import.meta.env.VITE_APP_BASE_API,
    timeout: 50000, // 请求超时时间：50s
    headers: {'Content-Type': 'application/json;charset=utf-8'},
});

const useLogin = import.meta.env.VITE_USE_LOGIN === 'true'
// 请求拦截器
service.interceptors.request.use(
    (config) => {

        if (!config.headers) {
            throw new Error(`Expected 'config' and 'config.headers' not to be undefined`);
        }

        if (useLogin) {
            config.headers.Authorization = 'Bearer ' + localStorage.getItem('token')
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    },
);

// 响应拦截器
service.interceptors.response.use(
    (response) => {
        const res = response.data;
        // 二进制数据则直接返回
        if (response.request.responseType === 'blob' || response.request.responseType === 'arraybuffer') {
            return response.data
        }
        const {code, message} = res;
        if (code === 0) {
            return res;
        } else {
            if (code === 401 || code === 403) {
                localStorage.removeItem('token');
                location.href = import.meta.env.VITE_BASE;
            } else {
                ElMessage({
                    message: message || '系统出错',
                    type: 'error',
                    duration: 5 * 1000,
                });
            }
            return Promise.reject(new Error(message || 'Error'));
        }
    },
    (error) => {
        console.log('请求异常：', error);
        let message = '网络异常，请稍后再试!';
        
        // 安全地获取错误信息
        if (error.response && error.response.data) {
            message = error.response.data.message || message;
        } else if (error.message) {
            message = error.message;
        }
        
        // 未认证
        if (error.response && error.response.status === 401) {
            localStorage.removeItem('token')
            location.href = import.meta.env.VITE_BASE;
        } else {
            ElMessage({
                message: message,
                type: 'error',
                duration: 5 * 1000,
            });
            return Promise.reject(new Error(message));
        }
    },
);


// 导出实例
export default service;