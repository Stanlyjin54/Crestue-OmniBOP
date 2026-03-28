// 获取所有api模块
const modules = import.meta.glob('./**/*.js', {
  eager: true
});

// 构建api对象
const api = {};
Object.keys(modules).forEach((key) => {
  let moduleName = key.replace(/\.\/(.*)\.js/, '$1');
  // 处理嵌套目录，只取最后一个部分作为模块名
  if (moduleName.includes('/')) {
    const parts = moduleName.split('/');
    // 如果是 login 目录下的文件，使用文件名作为模块名
    if (parts[0] === 'login') {
      moduleName = parts[parts.length - 1];
    } else {
      moduleName = parts[parts.length - 1];
    }
  }
  api[moduleName] = modules[key].default || modules[key];
});

// 导出社交媒体相关API
export * from './social-media/index.js';

// 导出政府采购信息采集相关API
export * from './procurement/procurementCrawler.js';

// 导出 page 目录下的模块并添加到 api 对象
import globalVariable from './page/globalVariable.js';
import loading from './page/loading.js';
import qr from './page/qr.js';
import file from './page/file.js';
import navigate from './page/navigate.js';
import toast from './page/toast.js';
import reload from './page/reload.js';
import model from './page/model.js';
import location from './page/location.js';
import interactWithComponent from './page/interactWithComponent.js';

// 添加到 api 对象根级别
api.globalVariable = globalVariable;
api.loading = loading;
api.qr = qr;
api.file = file;
api.navigate = navigate;
api.toast = toast;
api.reload = reload;
api.model = model;
api.location = location;
api.interactWithComponent = interactWithComponent;

// 同时添加到 page 子对象（保持兼容性）
api.page = {
  globalVariable,
  loading,
  qr,
  file,
  navigate,
  toast,
  reload,
  model,
  location,
  interactWithComponent
};

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