import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'

/**
 * 社会媒体营销管理组合式函数
 * 提供社媒营销活动的创建、管理、分析等功能
 */
export function useSocialMediaMarketing() {
  // 响应式数据
  const loading = ref(false)
  const campaigns = ref([])
  const platforms = ref([])
  const analytics = ref({})
  const contentLibrary = ref([])
  const audienceData = ref({})
  
  // 筛选和搜索参数
  const filterParams = reactive({
    platform: '',
    status: '',
    campaignType: '',
    dateRange: [],
    keyword: ''
  })
  
  // 分页参数
  const pagination = reactive({
    currentPage: 1,
    pageSize: 20,
    total: 0
  })
  
  // 营销活动配置
  const campaignConfig = reactive({
    platforms: [
      { id: 'wechat', name: '微信', icon: 'chat-line-round', color: '#07C160' },
      { id: 'weibo', name: '微博', icon: 'share', color: '#E6162D' },
      { id: 'douyin', name: '抖音', icon: 'video-camera', color: '#000000' },
      { id: 'xiaohongshu', name: '小红书', icon: 'picture', color: '#FF2442' },
      { id: 'linkedin', name: 'LinkedIn', icon: 'connection', color: '#0077B5' },
      { id: 'twitter', name: 'Twitter', icon: 'Promotion', color: '#1DA1F2' }
    ],
    campaignTypes: [
      { value: 'awareness', label: '品牌认知' },
      { value: 'engagement', label: '用户互动' },
      { value: 'conversion', label: '转化推广' },
      { value: 'retention', label: '用户留存' },
      { value: 'viral', label: '病毒营销' }
    ],
    contentTypes: [
      { value: 'text', label: '纯文本' },
      { value: 'image', label: '图片' },
      { value: 'video', label: '视频' },
      { value: 'carousel', label: '轮播图' },
      { value: 'story', label: '故事' }
    ],
    statuses: [
      { value: 'draft', label: '草稿', type: 'info' },
      { value: 'scheduled', label: '已排期', type: 'warning' },
      { value: 'active', label: '进行中', type: 'success' },
      { value: 'paused', label: '已暂停', type: 'warning' },
      { value: 'completed', label: '已完成', type: 'info' },
      { value: 'cancelled', label: '已取消', type: 'danger' }
    ]
  })
  
  // 计算属性
  const filteredCampaigns = computed(() => {
    let result = campaigns.value
    
    if (filterParams.platform) {
      result = result.filter(campaign => 
        campaign.platforms.includes(filterParams.platform)
      )
    }
    
    if (filterParams.status) {
      result = result.filter(campaign => campaign.status === filterParams.status)
    }
    
    if (filterParams.campaignType) {
      result = result.filter(campaign => campaign.type === filterParams.campaignType)
    }
    
    if (filterParams.keyword) {
      result = result.filter(campaign =>
        campaign.name.toLowerCase().includes(filterParams.keyword.toLowerCase()) ||
        campaign.description.toLowerCase().includes(filterParams.keyword.toLowerCase())
      )
    }
    
    return result
  })
  
  const activeCampaigns = computed(() =>
    campaigns.value.filter(campaign => campaign.status === 'active')
  )
  
  const totalReach = computed(() =>
    campaigns.value.reduce((sum, campaign) => sum + (campaign.metrics?.reach || 0), 0)
  )
  
  const totalEngagement = computed(() =>
    campaigns.value.reduce((sum, campaign) => sum + (campaign.metrics?.engagement || 0), 0)
  )
  
  const averageEngagementRate = computed(() => {
    if (totalReach.value === 0) return 0
    return ((totalEngagement.value / totalReach.value) * 100).toFixed(2)
  })
  
  // 模拟数据
  const mockCampaigns = [
    {
      id: '1',
      name: '春季新品推广活动',
      description: '推广2024年春季新品系列',
      type: 'conversion',
      status: 'active',
      platforms: ['wechat', 'xiaohongshu'],
      budget: 50000,
      spent: 32000,
      startDate: '2024-03-01',
      endDate: '2024-03-31',
      createdAt: '2024-02-25T10:00:00Z',
      updatedAt: '2024-03-15T14:30:00Z',
      metrics: {
        reach: 125000,
        engagement: 8750,
        clicks: 3200,
        conversions: 185,
        ctr: 2.56,
        cpc: 10.0,
        roi: 3.2
      },
      content: [
        {
          id: 'c1',
          type: 'image',
          title: '春季新品主图',
          description: '展示新品系列的主要产品',
          imageUrl: '/images/spring-collection.jpg',
          platforms: ['wechat', 'xiaohongshu'],
          scheduledTime: '2024-03-01T09:00:00Z',
          status: 'published'
        }
      ],
      audience: {
        ageRange: '25-40',
        gender: 'all',
        interests: ['时尚', '生活', '美妆'],
        location: ['一线城市', '新一线城市']
      }
    },
    {
      id: '2',
      name: '用户互动活动',
      description: '提升用户参与度和品牌忠诚度',
      type: 'engagement',
      status: 'scheduled',
      platforms: ['weibo', 'douyin'],
      budget: 30000,
      spent: 0,
      startDate: '2024-03-20',
      endDate: '2024-04-05',
      createdAt: '2024-03-10T15:20:00Z',
      updatedAt: '2024-03-18T11:45:00Z',
      metrics: {
        reach: 0,
        engagement: 0,
        clicks: 0,
        conversions: 0,
        ctr: 0,
        cpc: 0,
        roi: 0
      },
      content: [
        {
          id: 'c2',
          type: 'video',
          title: '品牌故事视频',
          description: '讲述品牌发展历程',
          videoUrl: '/videos/brand-story.mp4',
          platforms: ['weibo', 'douyin'],
          scheduledTime: '2024-03-20T10:00:00Z',
          status: 'scheduled'
        }
      ],
      audience: {
        ageRange: '18-35',
        gender: 'all',
        interests: ['生活方式', '娱乐', '时尚'],
        location: ['全国']
      }
    }
  ]
  
  // 方法
  const fetchCampaigns = async (params = {}) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      // 更新分页参数
      if (params.page) pagination.currentPage = params.page
      if (params.pageSize) pagination.pageSize = params.pageSize
      
      // 模拟数据
      campaigns.value = mockCampaigns
      pagination.total = mockCampaigns.length
      
      return {
        campaigns: mockCampaigns,
        total: mockCampaigns.length,
        page: pagination.currentPage,
        pageSize: pagination.pageSize
      }
    } catch (error) {
      ElMessage.error('获取营销活动列表失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const createCampaign = async (campaignData) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1500))
      
      const newCampaign = {
        id: Date.now().toString(),
        ...campaignData,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        metrics: {
          reach: 0,
          engagement: 0,
          clicks: 0,
          conversions: 0,
          ctr: 0,
          cpc: 0,
          roi: 0
        }
      }
      
      campaigns.value.unshift(newCampaign)
      ElMessage.success('营销活动创建成功')
      
      return newCampaign
    } catch (error) {
      ElMessage.error('创建营销活动失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const updateCampaign = async (id, updates) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      const index = campaigns.value.findIndex(c => c.id === id)
      if (index !== -1) {
        campaigns.value[index] = {
          ...campaigns.value[index],
          ...updates,
          updatedAt: new Date().toISOString()
        }
      }
      
      ElMessage.success('营销活动更新成功')
      return campaigns.value[index]
    } catch (error) {
      ElMessage.error('更新营销活动失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const deleteCampaign = async (id) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 800))
      
      const index = campaigns.value.findIndex(c => c.id === id)
      if (index !== -1) {
        campaigns.value.splice(index, 1)
      }
      
      ElMessage.success('营销活动删除成功')
    } catch (error) {
      ElMessage.error('删除营销活动失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const batchDeleteCampaigns = async (ids) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1200))
      
      campaigns.value = campaigns.value.filter(c => !ids.includes(c.id))
      
      ElMessage.success(`成功删除 ${ids.length} 个营销活动`)
    } catch (error) {
      ElMessage.error('批量删除失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const fetchAnalytics = async (params = {}) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      analytics.value = {
        overview: {
          totalCampaigns: campaigns.value.length,
          activeCampaigns: activeCampaigns.value.length,
          totalReach: totalReach.value,
          totalEngagement: totalEngagement.value,
          averageEngagementRate: parseFloat(averageEngagementRate.value),
          totalBudget: campaigns.value.reduce((sum, c) => sum + c.budget, 0),
          totalSpent: campaigns.value.reduce((sum, c) => sum + c.spent, 0)
        },
        platformStats: [
          { platform: 'wechat', campaigns: 8, reach: 85000, engagement: 6800, color: '#07C160' },
          { platform: 'xiaohongshu', campaigns: 6, reach: 62000, engagement: 4960, color: '#FF2442' },
          { platform: 'weibo', campaigns: 4, reach: 45000, engagement: 3150, color: '#E6162D' },
          { platform: 'douyin', campaigns: 5, reach: 78000, engagement: 7020, color: '#000000' }
        ],
        trendData: [
          { date: '2024-03-01', reach: 12000, engagement: 960, conversions: 12 },
          { date: '2024-03-02', reach: 15000, engagement: 1200, conversions: 18 },
          { date: '2024-03-03', reach: 18000, engagement: 1440, conversions: 25 },
          { date: '2024-03-04', reach: 22000, engagement: 1760, conversions: 32 },
          { date: '2024-03-05', reach: 25000, engagement: 2000, conversions: 38 }
        ],
        topContent: [
          { id: 'c1', title: '春季新品主图', reach: 45000, engagement: 3600, ctr: 8.0 },
          { id: 'c3', title: '用户评价视频', reach: 38000, engagement: 3420, ctr: 9.0 },
          { id: 'c5', title: '品牌故事图文', reach: 32000, engagement: 2560, ctr: 8.0 }
        ]
      }
      
      return analytics.value
    } catch (error) {
      ElMessage.error('获取分析数据失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const fetchContentLibrary = async () => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 800))
      
      contentLibrary.value = [
        {
          id: 'content1',
          title: '春季时尚搭配指南',
          type: 'image',
          description: '展示春季时尚搭配技巧',
          imageUrl: '/images/spring-fashion.jpg',
          tags: ['时尚', '搭配', '春季'],
          platforms: ['wechat', 'xiaohongshu'],
          createdAt: '2024-03-01T10:00:00Z',
          usage: 3,
          performance: { reach: 25000, engagement: 2000, ctr: 8.0 }
        },
        {
          id: 'content2',
          title: '产品使用教程视频',
          type: 'video',
          description: '详细展示产品使用方法',
          videoUrl: '/videos/tutorial.mp4',
          tags: ['教程', '产品', '使用'],
          platforms: ['douyin', 'weibo'],
          createdAt: '2024-03-05T14:30:00Z',
          usage: 1,
          performance: { reach: 18000, engagement: 1620, ctr: 9.0 }
        }
      ]
      
      return contentLibrary.value
    } catch (error) {
      ElMessage.error('获取内容库失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const fetchAudienceData = async () => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 900))
      
      audienceData.value = {
        demographics: {
          age: [
            { range: '18-24', percentage: 25 },
            { range: '25-34', percentage: 35 },
            { range: '35-44', percentage: 28 },
            { range: '45+', percentage: 12 }
          ],
          gender: [
            { type: 'male', percentage: 40 },
            { type: 'female', percentage: 60 }
          ],
          location: [
            { city: '北京', percentage: 15 },
            { city: '上海', percentage: 18 },
            { city: '广州', percentage: 12 },
            { city: '深圳', percentage: 14 },
            { city: '杭州', percentage: 10 }
          ]
        },
        interests: [
          { category: '时尚', percentage: 85 },
          { category: '美妆', percentage: 72 },
          { category: '生活', percentage: 68 },
          { category: '旅游', percentage: 45 },
          { category: '美食', percentage: 52 }
        ],
        behavior: {
          peakHours: ['09:00', '12:00', '18:00', '21:00'],
          activeDays: ['周一', '周二', '周三', '周四', '周五', '周六'],
          averageSessionTime: '8.5分钟',
          contentPreference: ['图片', '短视频', '图文']
        }
      }
      
      return audienceData.value
    } catch (error) {
      ElMessage.error('获取受众数据失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const exportCampaigns = async (params = {}) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1500))
      
      // 模拟文件下载
      const blob = new Blob(['模拟的营销活动数据'], { type: 'text/csv' })
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = `营销活动数据_${new Date().toISOString().split('T')[0]}.csv`
      a.click()
      URL.revokeObjectURL(url)
      
      ElMessage.success('数据导出成功')
    } catch (error) {
      ElMessage.error('数据导出失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const schedulePost = async (campaignId, contentId, scheduledTime) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      ElMessage.success('内容排期成功')
    } catch (error) {
      ElMessage.error('内容排期失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  // 内容库管理方法
  const createContent = async (contentData) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1200))
      
      const newContent = {
        id: Date.now().toString(),
        ...contentData,
        createdAt: new Date().toISOString(),
        usage: 0,
        performance: {
          reach: 0,
          engagement: 0,
          ctr: 0
        }
      }
      
      contentLibrary.value.unshift(newContent)
      ElMessage.success('内容创建成功')
      
      return newContent
    } catch (error) {
      ElMessage.error('创建内容失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const updateContent = async (id, updates) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      const index = contentLibrary.value.findIndex(c => c.id === id)
      if (index !== -1) {
        contentLibrary.value[index] = {
          ...contentLibrary.value[index],
          ...updates
        }
      }
      
      ElMessage.success('内容更新成功')
      return contentLibrary.value[index]
    } catch (error) {
      ElMessage.error('更新内容失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const deleteContent = async (id) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 800))
      
      const index = contentLibrary.value.findIndex(c => c.id === id)
      if (index !== -1) {
        contentLibrary.value.splice(index, 1)
      }
      
      ElMessage.success('内容删除成功')
    } catch (error) {
      ElMessage.error('删除内容失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const batchDeleteContents = async (ids) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1200))
      
      contentLibrary.value = contentLibrary.value.filter(c => !ids.includes(c.id))
      
      ElMessage.success(`成功删除 ${ids.length} 个内容`)
    } catch (error) {
      ElMessage.error('批量删除失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const fetchContentCategories = async () => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 600))
      
      return [
        { id: 'fashion', name: '时尚', color: '#FF6B9D' },
        { id: 'beauty', name: '美妆', color: '#FFB6C1' },
        { id: 'lifestyle', name: '生活方式', color: '#98FB98' },
        { id: 'food', name: '美食', color: '#FFA07A' },
        { id: 'travel', name: '旅行', color: '#87CEEB' },
        { id: 'tech', name: '科技', color: '#DDA0DD' },
        { id: 'education', name: '教育', color: '#F0E68C' },
        { id: 'entertainment', name: '娱乐', color: '#FFA500' }
      ]
    } catch (error) {
      ElMessage.error('获取内容分类失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const uploadContent = async (file, contentData) => {
    try {
      loading.value = true
      
      // 模拟文件上传
      await new Promise(resolve => setTimeout(resolve, 2000))
      
      const uploadedContent = {
        id: Date.now().toString(),
        ...contentData,
        fileUrl: `/uploads/${file.name}`,
        fileSize: file.size,
        fileType: file.type,
        createdAt: new Date().toISOString(),
        usage: 0,
        performance: {
          reach: 0,
          engagement: 0,
          ctr: 0
        }
      }
      
      contentLibrary.value.unshift(uploadedContent)
      ElMessage.success('文件上传成功')
      
      return uploadedContent
    } catch (error) {
      ElMessage.error('文件上传失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const duplicateContent = async (id) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 800))
      
      const originalContent = contentLibrary.value.find(c => c.id === id)
      if (originalContent) {
        const duplicatedContent = {
          ...originalContent,
          id: Date.now().toString(),
          title: `${originalContent.title} (副本)`,
          createdAt: new Date().toISOString()
        }
        
        contentLibrary.value.unshift(duplicatedContent)
        ElMessage.success('内容复制成功')
        
        return duplicatedContent
      }
    } catch (error) {
      ElMessage.error('复制内容失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  // 工具方法
  const getStatusType = (status) => {
    const statusMap = {
      draft: 'info',
      scheduled: 'warning',
      active: 'success',
      paused: 'warning',
      completed: 'info',
      cancelled: 'danger'
    }
    return statusMap[status] || 'info'
  }
  
  const getPlatformInfo = (platformId) => {
    return campaignConfig.platforms.find(p => p.id === platformId)
  }
  
  const formatBudget = (amount) => {
    return new Intl.NumberFormat('zh-CN', {
      style: 'currency',
      currency: 'CNY',
      minimumFractionDigits: 0
    }).format(amount)
  }
  
  const formatNumber = (num) => {
    if (num >= 10000) {
      return (num / 10000).toFixed(1) + '万'
    }
    return num.toLocaleString()
  }
  
  return {
    // 响应式数据
    loading: readonly(loading),
    campaigns: readonly(campaigns),
    platforms: readonly(platforms),
    analytics: readonly(analytics),
    contentLibrary: readonly(contentLibrary),
    audienceData: readonly(audienceData),
    filterParams,
    pagination,
    campaignConfig,
    
    // 计算属性
    filteredCampaigns,
    activeCampaigns,
    totalReach,
    totalEngagement,
    averageEngagementRate,
    
    // 方法
    fetchCampaigns,
    createCampaign,
    updateCampaign,
    deleteCampaign,
    batchDeleteCampaigns,
    fetchAnalytics,
    fetchContentLibrary,
    fetchContentCategories,
    fetchAudienceData,
    exportCampaigns,
    schedulePost,
    createContent,
    updateContent,
    deleteContent,
    batchDeleteContents,
    uploadContent,
    duplicateContent,
    
    // 工具方法
    getStatusType,
    getPlatformInfo,
    formatBudget,
    formatNumber
  }
}