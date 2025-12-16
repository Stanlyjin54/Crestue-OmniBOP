import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import ErrorState from '@/components/ErrorState.vue'

describe('ErrorState', () => {
  it('应该正确渲染错误状态', () => {
    const wrapper = mount(ErrorState)
    
    expect(wrapper.find('.error-state').exists()).toBe(true)
    expect(wrapper.find('.error-icon').exists()).toBe(true)
    expect(wrapper.find('.error-title').exists()).toBe(true)
    expect(wrapper.find('.error-message').exists()).toBe(true)
  })

  it('应该显示默认错误信息', () => {
    const wrapper = mount(ErrorState)
    
    expect(wrapper.find('.error-title').text()).toBe('出错了')
    expect(wrapper.find('.error-message').text()).toBe('抱歉，出现了一些问题，请稍后重试')
  })

  it('应该显示自定义错误信息', () => {
    const wrapper = mount(ErrorState, {
      props: {
        title: '网络错误',
        message: '网络连接失败，请检查网络设置'
      }
    })
    
    expect(wrapper.find('.error-title').text()).toBe('网络错误')
    expect(wrapper.find('.error-message').text()).toBe('网络连接失败，请检查网络设置')
  })

  it('应该显示重试按钮', () => {
    const wrapper = mount(ErrorState, {
      props: {
        showRetry: true
      }
    })
    
    expect(wrapper.find('.retry-button').exists()).toBe(true)
    expect(wrapper.find('.retry-button').text()).toBe('重试')
  })

  it('应该隐藏重试按钮', () => {
    const wrapper = mount(ErrorState, {
      props: {
        showRetry: false
      }
    })
    
    expect(wrapper.find('.retry-button').exists()).toBe(false)
  })

  it('应该触发重试事件', async () => {
    const wrapper = mount(ErrorState, {
      props: {
        showRetry: true
      }
    })
    
    await wrapper.find('.retry-button').trigger('click')
    expect(wrapper.emitted('retry')).toBeTruthy()
    expect(wrapper.emitted('retry')?.length).toBe(1)
  })

  it('应该应用自定义样式', () => {
    const wrapper = mount(ErrorState, {
      props: {
        customClass: 'custom-error'
      }
    })
    
    expect(wrapper.classes()).toContain('custom-error')
  })

  it('应该处理空状态', () => {
    const wrapper = mount(ErrorState, {
      props: {
        type: 'empty',
        title: '暂无数据',
        message: '没有找到相关数据'
      }
    })
    
    expect(wrapper.find('.error-state').classes()).toContain('empty-state')
    expect(wrapper.find('.error-title').text()).toBe('暂无数据')
  })
})