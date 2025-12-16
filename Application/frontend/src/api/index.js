// 获取所有api模块
const modules = import.meta.glob('./**/*.js', {
  eager: true
});

// 构建api对象
const api = {};
Object.keys(modules).forEach((key) => {
  const moduleName = key.replace(/\.\/(.*)\.js/, '$1');
  api[moduleName] = modules[key];
});

// 导出社交媒体相关API
export * from './social-media/index.js';

// 导出政府采购信息采集相关API
export * from './procurement/procurementCrawler.js';

export default api;




//
//
// // 全局加载所有目录下的 js 文件
// const modulesFiles = import.meta.glob('./**/*.js', { import: 'default', eager: true });
//
// // 工具方法：将路径拆分为嵌套结构
// function setDeepObject(obj, pathArray, value) {
//   let current = obj;
//   //  找到最内层的数据
//   for (let i = 0; i < pathArray.length - 1; i++) {
//     const key = pathArray[i];
//     if (!current[key]) current[key] = {};
//     current = current[key];
//   }
//   current[pathArray[pathArray.length - 1]] = value;
// }
//
// const cf_modules = {};
// for (const key in modulesFiles) {
//   // key 示例: './cf/table/table.js'
//   const cleanedPath = key.replace(/^\.\/|\.js$/g, ''); // -> 'cf/table/insert'
//   const pathArray = cleanedPath.split('/');            // -> ['cf', 'table', 'insert']
//   setDeepObject(cf_modules, pathArray, modulesFiles[key]);
// }
//
// export default cf_modules;