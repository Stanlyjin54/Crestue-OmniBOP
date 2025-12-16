<template>
  <div class="search-bar">
    <el-input
      v-model="keyword"
      placeholder="请输入商品名称"
      class="search-input"
      @keyup.enter="handleSearch"
    >
      <template #append>
        <el-button @click="handleSearch">
          <el-icon><Search /></el-icon>
        </el-button>
      </template>
    </el-input>
    
    <div class="search-suggestions" v-if="suggestions.length > 0">
      <div
        v-for="suggestion in suggestions"
        :key="suggestion"
        class="suggestion-item"
        @click="selectSuggestion(suggestion)"
      >
        {{ suggestion }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { debounce } from '@/utils'
import { ElMessage } from 'element-plus'
// 图标已在main.ts中全局注册，无需单独导入
import { getProductList } from '@/api/product'

export interface Emits {
  search: [keyword: string]
}

const emit = defineEmits<Emits>()

const keyword = ref('')
const suggestions = ref<string[]>([])

// 搜索商品
const handleSearch = () => {
  if (!keyword.value.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }
  emit('search', keyword.value.trim())
}

// 获取搜索建议
const getSuggestions = debounce(async () => {
  if (!keyword.value.trim()) {
    suggestions.value = []
    return
  }
  
  try {
    const response = await getProductList({
      keyword: keyword.value.trim(),
      page: 1,
      pageSize: 5
    })
    suggestions.value = response.list.map((product: any) => product.name)
  } catch (error) {
    suggestions.value = []
  }
}, 300)

// 选择搜索建议
const selectSuggestion = (suggestion: string) => {
  keyword.value = suggestion
  handleSearch()
  suggestions.value = []
}

// 监听关键词变化
watch(keyword, () => {
  getSuggestions()
})
</script>

<style scoped lang="scss">
.search-bar {
  position: relative;
  max-width: 600px;
  margin: 0 auto;
}

.search-input {
  width: 100%;
}

.search-suggestions {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: #fff;
  border: 1px solid #e4e7ed;
  border-top: none;
  border-radius: 0 0 4px 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.suggestion-item {
  padding: 10px 15px;
  cursor: pointer;
  transition: background-color 0.3s;

  &:hover {
    background-color: #f5f7fa;
  }
}
</style>