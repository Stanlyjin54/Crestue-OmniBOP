import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import { defineComponent, ref } from 'vue'
import LoadingSpinner from '@/components/LoadingSpinner.vue'

describe('LoadingSpinner', () => {
  it('应该正确渲染加载动画', () => {
    const wrapper = mount(LoadingSpinner)
    
    expect(wrapper.find('.loading-spinner').exists()).toBe(true)
    expect(wrapper.find('.spinner').exists()).toBe(true)
    expect(wrapper.find('.loading-text').exists()).toBe(true)
  })

  it('应该显示默认文本', () => {
    const wrapper = mount(LoadingSpinner)
    
    expect(wrapper.find('.loading-text').text()).toBe('加载中...')
  })

  it('应该显示自定义文本', () => {
    const wrapper = mount(LoadingSpinner, {
      props: {
        text: '自定义加载文本'
      }
    })
    
    expect(wrapper.find('.loading-text').text()).toBe('自定义加载文本')
  })

  it('应该应用自定义大小', () => {
    const wrapper = mount(LoadingSpinner, {
      props: {
        size: 'large'
      }
    })
    
    expect(wrapper.classes()).toContain('large')
  })

  it('应该应用自定义样式', () => {
    const wrapper = mount(LoadingSpinner, {
      props: {
        customClass: 'custom-loading'
      }
    })
    
    expect(wrapper.classes()).toContain('custom-loading')
  })

  it('应该正确显示和隐藏', async () => {
    const wrapper = mount(LoadingSpinner, {
      props: {
        visible: false
      }
    })
    
    expect(wrapper.isVisible()).toBe(false)
    
    await wrapper.setProps({ visible: true })
    expect(wrapper.isVisible()).toBe(true)
  })
})