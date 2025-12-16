<template>
  <view class="crawler-config">
    <view class="header">
      <text class="title">爬虫配置管理</text>
      <button class="btn-save" @click="saveConfig">保存配置</button>
    </view>
    
    <view class="config-section">
      <view class="section-title">基础配置</view>
      <view class="config-form">
        <view class="form-item">
          <text class="form-label">爬虫名称:</text>
          <input class="form-input" v-model="config.name" placeholder="请输入爬虫名称" />
        </view>
        <view class="form-item">
          <text class="form-label">目标网站:</text>
          <input class="form-input" v-model="config.targetSite" placeholder="请输入目标网站URL" />
        </view>
        <view class="form-item">
          <text class="form-label">采集类型:</text>
          <picker mode="selector" :range="crawlerTypes" :value="config.typeIndex" @change="onTypeChange">
            <view class="form-picker">{{ crawlerTypes[config.typeIndex] }}</view>
          </picker>
        </view>
        <view class="form-item">
          <text class="form-label">采集频率:</text>
          <picker mode="selector" :range="frequencyOptions" :value="config.frequencyIndex" @change="onFrequencyChange">
            <view class="form-picker">{{ frequencyOptions[config.frequencyIndex] }}</view>
          </picker>
        </view>
      </view>
    </view>
    
    <view class="config-section">
      <view class="section-title">采集规则</view>
      <view class="rule-list">
        <view class="rule-item" v-for="(rule, index) in config.rules" :key="index">
          <view class="rule-header">
            <text class="rule-name">{{ rule.name }}</text>
            <view class="rule-actions">
              <button class="btn-edit" @click="editRule(rule)">编辑</button>
              <button class="btn-delete" @click="deleteRule(index)">删除</button>
            </view>
          </view>
          <view class="rule-content">
            <text class="rule-selector">选择器: {{ rule.selector }}</text>
            <text class="rule-attribute">属性: {{ rule.attribute }}</text>
          </view>
        </view>
        <button class="btn-add-rule" @click="addRule">+ 添加规则</button>
      </view>
    </view>
    
    <view class="config-section">
      <view class="section-title">数据清洗</view>
      <view class="cleaning-options">
        <view class="option-item">
          <text class="option-label">自动去重:</text>
          <switch :checked="config.cleaning.deduplicate" @change="onDeduplicateChange" />
        </view>
        <view class="option-item">
          <text class="option-label">数据验证:</text>
          <switch :checked="config.cleaning.validate" @change="onValidateChange" />
        </view>
        <view class="option-item">
          <text class="option-label">格式标准化:</text>
          <switch :checked="config.cleaning.standardize" @change="onStandardizeChange" />
        </view>
        <view class="option-item">
          <text class="option-label">敏感信息过滤:</text>
          <switch :checked="config.cleaning.filterSensitive" @change="onFilterSensitiveChange" />
        </view>
      </view>
    </view>
    
    <view class="config-section">
      <view class="section-title">代理设置</view>
      <view class="proxy-options">
        <view class="option-item">
          <text class="option-label">使用代理:</text>
          <switch :checked="config.proxy.enabled" @change="onProxyEnabledChange" />
        </view>
        <view v-if="config.proxy.enabled" class="proxy-details">
          <view class="form-item">
            <text class="form-label">代理类型:</text>
            <picker mode="selector" :range="proxyTypes" :value="config.proxy.typeIndex" @change="onProxyTypeChange">
              <view class="form-picker">{{ proxyTypes[config.proxy.typeIndex] }}</view>
            </picker>
          </view>
          <view class="form-item">
            <text class="form-label">代理地址:</text>
            <input class="form-input" v-model="config.proxy.host" placeholder="请输入代理地址" />
          </view>
          <view class="form-item">
            <text class="form-label">代理端口:</text>
            <input class="form-input" v-model="config.proxy.port" placeholder="请输入代理端口" />
          </view>
          <view class="form-item">
            <text class="form-label">用户名:</text>
            <input class="form-input" v-model="config.proxy.username" placeholder="请输入用户名（可选）" />
          </view>
          <view class="form-item">
            <text class="option-label">密码:</text>
            <input class="form-input" type="password" v-model="config.proxy.password" placeholder="请输入密码（可选）" />
          </view>
        </view>
      </view>
    </view>
    
    <view class="config-section">
      <view class="section-title">性能设置</view>
      <view class="performance-options">
        <view class="form-item">
          <text class="form-label">并发数:</text>
          <slider :value="config.performance.concurrency" min="1" max="20" show-value @change="onConcurrencyChange" />
        </view>
        <view class="form-item">
          <text class="form-label">请求间隔(秒):</text>
          <slider :value="config.performance.interval" min="1" max="60" show-value @change="onIntervalChange" />
        </view>
        <view class="form-item">
          <text class="form-label">超时时间(秒):</text>
          <slider :value="config.performance.timeout" min="10" max="300" show-value @change="onTimeoutChange" />
        </view>
        <view class="form-item">
          <text class="form-label">重试次数:</text>
          <slider :value="config.performance.retries" min="0" max="10" show-value @change="onRetriesChange" />
        </view>
      </view>
    </view>
    
    <view class="config-section">
      <view class="section-title">存储配置</view>
      <view class="storage-options">
        <view class="form-item">
          <text class="form-label">存储类型:</text>
          <picker mode="selector" :range="storageTypes" :value="config.storage.typeIndex" @change="onStorageTypeChange">
            <view class="form-picker">{{ storageTypes[config.storage.typeIndex] }}</view>
          </picker>
        </view>
        <view class="form-item">
          <text class="form-label">数据表名:</text>
          <input class="form-input" v-model="config.storage.tableName" placeholder="请输入数据表名" />
        </view>
        <view class="option-item">
          <text class="option-label">自动备份:</text>
          <switch :checked="config.storage.autoBackup" @change="onAutoBackupChange" />
        </view>
        <view v-if="config.storage.autoBackup" class="backup-options">
          <view class="form-item">
            <text class="form-label">备份频率:</text>
            <picker mode="selector" :range="backupFrequencies" :value="config.storage.backupFrequencyIndex" @change="onBackupFrequencyChange">
              <view class="form-picker">{{ backupFrequencies[config.storage.backupFrequencyIndex] }}</view>
            </picker>
          </view>
        </view>
      </view>
    </view>
    
    <view class="config-actions">
      <button class="btn-test" @click="testConfig">测试配置</button>
      <button class="btn-reset" @click="resetConfig">重置配置</button>
    </view>
    
    <!-- 规则编辑弹窗 -->
    <view class="modal" v-if="showRuleModal">
      <view class="modal-content">
        <view class="modal-header">
          <text class="modal-title">编辑采集规则</text>
          <button class="btn-close" @click="closeRuleModal">×</button>
        </view>
        <view class="modal-body">
          <view class="form-item">
            <text class="form-label">规则名称:</text>
            <input class="form-input" v-model="currentRule.name" placeholder="请输入规则名称" />
          </view>
          <view class="form-item">
            <text class="form-label">CSS选择器:</text>
            <input class="form-input" v-model="currentRule.selector" placeholder="请输入CSS选择器" />
          </view>
          <view class="form-item">
            <text class="form-label">提取属性:</text>
            <picker mode="selector" :range="attributes" :value="currentRule.attributeIndex" @change="onAttributeChange">
              <view class="form-picker">{{ attributes[currentRule.attributeIndex] }}</view>
            </picker>
          </view>
          <view class="form-item">
            <text class="form-label">数据类型:</text>
            <picker mode="selector" :range="dataTypes" :value="currentRule.dataTypeIndex" @change="onDataTypeChange">
              <view class="form-picker">{{ dataTypes[currentRule.dataTypeIndex] }}</view>
            </picker>
          </view>
        </view>
        <view class="modal-footer">
          <button class="btn-cancel" @click="closeRuleModal">取消</button>
          <button class="btn-confirm" @click="saveRule">保存</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      config: {
        name: '政府采购网爬虫',
        targetSite: 'https://www.ccgp.gov.cn',
        typeIndex: 0,
        frequencyIndex: 1,
        rules: [
          {
            name: '标题提取',
            selector: '.title',
            attribute: 'text',
            attributeIndex: 0,
            dataType: 'string',
            dataTypeIndex: 0
          },
          {
            name: '发布时间',
            selector: '.publish-time',
            attribute: 'text',
            attributeIndex: 0,
            dataType: 'date',
            dataTypeIndex: 1
          }
        ],
        cleaning: {
          deduplicate: true,
          validate: true,
          standardize: true,
          filterSensitive: false
        },
        proxy: {
          enabled: false,
          typeIndex: 0,
          host: '',
          port: '',
          username: '',
          password: ''
        },
        performance: {
          concurrency: 5,
          interval: 3,
          timeout: 30,
          retries: 3
        },
        storage: {
          typeIndex: 0,
          tableName: 'procurement_data',
          autoBackup: true,
          backupFrequencyIndex: 2
        }
      },
      crawlerTypes: ['政府采购', '招标信息', '企业信息', '新闻资讯', '社交媒体'],
      frequencyOptions: ['每小时', '每2小时', '每4小时', '每8小时', '每天', '每周'],
      proxyTypes: ['HTTP', 'HTTPS', 'SOCKS5'],
      storageTypes: ['MySQL', 'PostgreSQL', 'MongoDB', 'SQLite', '文件存储'],
      backupFrequencies: ['每天', '每周', '每月'],
      attributes: ['text', 'html', 'href', 'src', 'value', 'data-attribute'],
      dataTypes: ['string', 'number', 'date', 'boolean', 'array', 'object'],
      showRuleModal: false,
      currentRule: null,
      isEditingRule: false
    }
  },
  onLoad(options) {
    if (options.name) {
      this.config.name = options.name
      // 根据爬虫名称加载对应配置
      this.loadCrawlerConfig(options.name)
    }
  },
  methods: {
    loadCrawlerConfig(name) {
      // 模拟加载爬虫配置
      uni.showLoading({ title: '加载配置中...' })
      setTimeout(() => {
        uni.hideLoading()
      }, 1000)
    },
    onTypeChange(e) {
      this.config.typeIndex = e.detail.value
    },
    onFrequencyChange(e) {
      this.config.frequencyIndex = e.detail.value
    },
    onDeduplicateChange(e) {
      this.config.cleaning.deduplicate = e.detail.value
    },
    onValidateChange(e) {
      this.config.cleaning.validate = e.detail.value
    },
    onStandardizeChange(e) {
      this.config.cleaning.standardize = e.detail.value
    },
    onFilterSensitiveChange(e) {
      this.config.cleaning.filterSensitive = e.detail.value
    },
    onProxyEnabledChange(e) {
      this.config.proxy.enabled = e.detail.value
    },
    onProxyTypeChange(e) {
      this.config.proxy.typeIndex = e.detail.value
    },
    onConcurrencyChange(e) {
      this.config.performance.concurrency = e.detail.value
    },
    onIntervalChange(e) {
      this.config.performance.interval = e.detail.value
    },
    onTimeoutChange(e) {
      this.config.performance.timeout = e.detail.value
    },
    onRetriesChange(e) {
      this.config.performance.retries = e.detail.value
    },
    onStorageTypeChange(e) {
      this.config.storage.typeIndex = e.detail.value
    },
    onAutoBackupChange(e) {
      this.config.storage.autoBackup = e.detail.value
    },
    onBackupFrequencyChange(e) {
      this.config.storage.backupFrequencyIndex = e.detail.value
    },
    addRule() {
      this.currentRule = {
        name: '',
        selector: '',
        attribute: 'text',
        attributeIndex: 0,
        dataType: 'string',
        dataTypeIndex: 0
      }
      this.isEditingRule = false
      this.showRuleModal = true
    },
    editRule(rule) {
      this.currentRule = { ...rule }
      this.isEditingRule = true
      this.showRuleModal = true
    },
    deleteRule(index) {
      uni.showModal({
        title: '删除规则',
        content: '确定要删除这条规则吗？',
        success: (res) => {
          if (res.confirm) {
            this.config.rules.splice(index, 1)
            uni.showToast({ title: '规则已删除', icon: 'success' })
          }
        }
      })
    },
    onAttributeChange(e) {
      this.currentRule.attributeIndex = e.detail.value
      this.currentRule.attribute = this.attributes[e.detail.value]
    },
    onDataTypeChange(e) {
      this.currentRule.dataTypeIndex = e.detail.value
      this.currentRule.dataType = this.dataTypes[e.detail.value]
    },
    saveRule() {
      if (!this.currentRule.name || !this.currentRule.selector) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' })
        return
      }
      
      if (this.isEditingRule) {
        const index = this.config.rules.findIndex(rule => rule.name === this.currentRule.name)
        if (index > -1) {
          this.config.rules[index] = { ...this.currentRule }
        }
      } else {
        this.config.rules.push({ ...this.currentRule })
      }
      
      this.closeRuleModal()
      uni.showToast({ title: '规则已保存', icon: 'success' })
    },
    closeRuleModal() {
      this.showRuleModal = false
      this.currentRule = null
    },
    saveConfig() {
      uni.showModal({
        title: '保存配置',
        content: '确定要保存当前配置吗？',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({ title: '保存中...' })
            setTimeout(() => {
              uni.hideLoading()
              uni.showToast({ title: '配置已保存', icon: 'success' })
            }, 1500)
          }
        }
      })
    },
    testConfig() {
      uni.showLoading({ title: '测试中...' })
      setTimeout(() => {
        uni.hideLoading()
        uni.showModal({
          title: '测试结果',
          content: '配置测试通过！可以正常连接到目标网站。',
          showCancel: false
        })
      }, 2000)
    },
    resetConfig() {
      uni.showModal({
        title: '重置配置',
        content: '确定要重置所有配置吗？此操作不可恢复。',
        success: (res) => {
          if (res.confirm) {
            this.loadDefaultConfig()
            uni.showToast({ title: '配置已重置', icon: 'success' })
          }
        }
      })
    },
    loadDefaultConfig() {
      this.config = {
        name: '政府采购网爬虫',
        targetSite: 'https://www.ccgp.gov.cn',
        typeIndex: 0,
        frequencyIndex: 1,
        rules: [],
        cleaning: {
          deduplicate: true,
          validate: true,
          standardize: true,
          filterSensitive: false
        },
        proxy: {
          enabled: false,
          typeIndex: 0,
          host: '',
          port: '',
          username: '',
          password: ''
        },
        performance: {
          concurrency: 5,
          interval: 3,
          timeout: 30,
          retries: 3
        },
        storage: {
          typeIndex: 0,
          tableName: 'procurement_data',
          autoBackup: true,
          backupFrequencyIndex: 2
        }
      }
    }
  }
}
</script>

<style>
.crawler-config {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.btn-save {
  background-color: #007aff;
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.config-section {
  background: white;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.config-form {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.form-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.form-label {
  font-size: 28rpx;
  color: #333;
  width: 160rpx;
}

.form-input {
  flex: 1;
  background: #f8f9fa;
  border: 1rpx solid #ddd;
  border-radius: 8rpx;
  padding: 16rpx 20rpx;
  font-size: 24rpx;
}

.form-picker {
  flex: 1;
  background: #f8f9fa;
  border: 1rpx solid #ddd;
  border-radius: 8rpx;
  padding: 16rpx 20rpx;
  font-size: 24rpx;
  color: #333;
}

.rule-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.rule-item {
  background: #f8f9fa;
  border-radius: 8rpx;
  padding: 20rpx;
  border: 1rpx solid #e9ecef;
}

.rule-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10rpx;
}

.rule-name {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
}

.rule-actions {
  display: flex;
  gap: 10rpx;
}

.btn-edit {
  background-color: #17a2b8;
  color: white;
  padding: 8rpx 16rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
}

.btn-delete {
  background-color: #dc3545;
  color: white;
  padding: 8rpx 16rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
}

.rule-content {
  display: flex;
  flex-direction: column;
  gap: 5rpx;
}

.rule-selector,
.rule-attribute {
  font-size: 24rpx;
  color: #666;
}

.btn-add-rule {
  background-color: #28a745;
  color: white;
  padding: 20rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
  margin-top: 20rpx;
}

.cleaning-options,
.storage-options,
.performance-options {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.option-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.option-label {
  font-size: 28rpx;
  color: #333;
}

.proxy-options {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.proxy-details {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
  margin-top: 20rpx;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 8rpx;
  border: 1rpx solid #e9ecef;
}

.config-actions {
  display: flex;
  gap: 20rpx;
  justify-content: center;
  margin-top: 40rpx;
}

.btn-test {
  background-color: #28a745;
  color: white;
  padding: 20rpx 40rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.btn-reset {
  background-color: #6c757d;
  color: white;
  padding: 20rpx 40rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12rpx;
  width: 90%;
  max-width: 600rpx;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #eee;
}

.modal-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.btn-close {
  background: none;
  border: none;
  font-size: 36rpx;
  color: #999;
  padding: 0;
  width: 40rpx;
  height: 40rpx;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-body {
  padding: 30rpx;
}

.modal-footer {
  display: flex;
  gap: 20rpx;
  justify-content: flex-end;
  padding: 30rpx;
  border-top: 1rpx solid #eee;
}

.btn-cancel {
  background-color: #6c757d;
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.btn-confirm {
  background-color: #007aff;
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
}
</style>