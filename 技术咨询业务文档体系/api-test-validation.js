// API测试验证脚本
// 在浏览器控制台中运行此脚本以验证API测试功能

console.log('🚀 开始API测试验证...');

// 检查API测试客户端是否可用
if (typeof window.apiTestClient === 'undefined') {
  console.error('❌ API测试客户端未找到，请确保api-test.js已正确加载');
} else {
  console.log('✅ API测试客户端已加载');
  
  // 测试连接
  window.apiTestClient.testConnection()
    .then(result => {
      console.log('✅ 连接测试成功:', result);
      document.querySelector('.connection-status').textContent = '已连接';
      document.querySelector('.connection-status').style.color = '#4CAF50';
    })
    .catch(error => {
      console.error('❌ 连接测试失败:', error);
      document.querySelector('.connection-status').textContent = '连接失败';
      document.querySelector('.connection-status').style.color = '#f44336';
    });

  // 测试采购列表API
  window.apiTestClient.request('POST', '/api/procurement/list', {
    current: 1,
    pageSize: 10,
    keyword: '测试'
  })
  .then(result => {
    console.log('✅ 采购列表API测试成功:', result);
    if (result.code === 0) {
      console.log(`📊 获取到 ${result.data.total} 条数据`);
    }
  })
  .catch(error => {
    console.error('❌ 采购列表API测试失败:', error);
  });

  // 测试社交媒体API
  window.apiTestClient.request('POST', '/api/social-media/douyin/analyze', {
    content: '这是一个测试的抖音视频内容',
    platform: 'douyin'
  })
  .then(result => {
    console.log('✅ 社交媒体分析API测试成功:', result);
  })
  .catch(error => {
    console.error('❌ 社交媒体分析API测试失败:', error);
  });

  // 测试合规检查API
  window.apiTestClient.request('POST', '/api/social-media/compliance/check', {
    content: '测试内容合规性',
    platform: 'douyin'
  })
  .then(result => {
    console.log('✅ 合规检查API测试成功:', result);
  })
  .catch(error => {
    console.error('❌ 合规检查API测试失败:', error);
  });
}

console.log('📋 API测试验证完成！');

// 添加页面加载完成后的自动测试
window.addEventListener('load', function() {
  console.log('🎯 页面加载完成，开始自动测试...');
  
  // 模拟点击测试按钮
  setTimeout(() => {
    const testButtons = document.querySelectorAll('.test-btn');
    console.log(`找到 ${testButtons.length} 个测试按钮`);
    
    testButtons.forEach((btn, index) => {
      setTimeout(() => {
        console.log(`点击按钮: ${btn.textContent}`);
        btn.click();
      }, index * 1000);
    });
  }, 2000);
});