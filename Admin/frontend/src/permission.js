import router from '@/router';

import { dynamicRoutes } from '@/router/dynamic'

import NProgress from 'nprogress'; // 导入 nprogress模块
import 'nprogress/nprogress.css'; // 导入样式
NProgress.configure({ showSpinner: true }); // 显示右上角螺旋加载提示

let hasRouter = false;
const useLogin = import.meta.env.VITE_USE_LOGIN === 'true'
const whiteList = ['/login'];

router.beforeEach(async (to, from, next) => {
    console.log('🔍 路由守卫开始:', to.path, 'from:', from.path, 'useLogin:', useLogin);
    console.log('🔑 Token检查:', !!localStorage.getItem("token"));
    
    NProgress.start(); // 开启进度条
    
    try {
        if(useLogin){
            // 判断有没有登录
            if (localStorage.getItem("token")) {
                console.log('✅ 用户已登录');
                //登录成功
                if (to.path === '/login') {
                    console.log('📱 已登录用户访问登录页面，重定向到首页');
                    if (to.fullPath.startsWith('/login?redirect=')) {
                      let lastPath = to.fullPath.replace('/login?redirect=', '');
                      next({ path: lastPath }); // 跳转到上次退出的页面
                    } else {
                      next({ path: '/' }); // 跳转到首页
                    }
                } else {
                    if (hasRouter) {
                        console.log('🚀 动态路由已加载，直接放行');
                        next(); // 放行
                    } else {
                        console.log('⚡ 加载动态路由...');
                        dynamicRoutes.forEach((e) => router.addRoute(e));
                        hasRouter = true;
                        next({ ...to, replace: true });
                    }
                }
            } else {
                console.log('❌ 用户未登录');
                if (whiteList.indexOf(to.path) !== -1) {
                    console.log('✅ 白名单页面，放行:', to.path);
                    next(); // 放行 -- 可以访问白名单页面(eg: 登录页面)
                } else {
                    console.log('🔒 非白名单页面，重定向到登录:', to.path);
                    //next(`/login?redirect=${to.path}`); // 无权限 & 白名单页面未配置  =》 跳转到登录页面
                    next(`/login`);
                }
            }
        }
        else{
            console.log('🔓 登录功能未启用');
            if (hasRouter) {
                console.log('🚀 动态路由已加载，直接放行');
                next(); // 放行
            } else {
                console.log('⚡ 加载动态路由...');
                dynamicRoutes.forEach((e) => router.addRoute(e));
                hasRouter = true;
                next({ ...to, replace: true });
            }
        }
    } catch (error) {
        console.error('❌ 路由守卫出错:', error);
        NProgress.done(); // 确保出错时也停止进度条
        next(); // 出错时放行
    }
});

// 全局后置钩子
router.afterEach((to, from) => {
    console.log('✅ 路由完成:', to.path, 'from:', from.path);
    NProgress.done(); // 完成进度条
});