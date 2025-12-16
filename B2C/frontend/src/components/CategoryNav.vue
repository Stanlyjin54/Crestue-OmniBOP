<template>
  <div class="category-nav">
    <div class="category-list">
      <div
        v-for="category in categories"
        :key="category.id"
        class="category-item"
        :class="{ active: category.id === activeCategory }"
        @click="selectCategory(category.id)"
      >
        <div class="category-icon">
          <img v-if="category.icon" :src="category.icon" :alt="category.name" />
          <el-icon v-else><Goods /></el-icon>
        </div>
        <span class="category-name">{{ category.name }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'
// 图标已在main.ts中全局注册，无需单独导入

interface Category {
  id: number
  name: string
  icon?: string
  sort?: number
}

interface Props {
  categories: Category[]
  activeCategory?: number
}

interface Emits {
  (e: 'select-category', categoryId: number): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const selectCategory = (categoryId: number) => {
  emit('select-category', categoryId)
}
</script>

<style scoped lang="scss">
.category-nav {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.category-list {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px 20px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 100px;
  border: 2px solid transparent;

  &:hover {
    background-color: #f5f7fa;
    transform: translateY(-2px);
  }

  &.active {
    background-color: #409eff;
    color: #fff;
    border-color: #409eff;

    .category-icon {
      background-color: rgba(255, 255, 255, 0.2);
    }
  }
}

.category-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
  transition: background-color 0.3s;

  img {
    width: 32px;
    height: 32px;
    object-fit: contain;
  }

  .el-icon {
    font-size: 24px;
    color: #909399;
  }
}

.category-name {
  font-size: 14px;
  font-weight: 500;
  text-align: center;
}

@media (max-width: 768px) {
  .category-nav {
    padding: 15px;
  }

  .category-list {
    gap: 10px;
  }

  .category-item {
    min-width: 80px;
    padding: 10px 15px;
  }

  .category-icon {
    width: 40px;
    height: 40px;

    img {
      width: 24px;
      height: 24px;
    }

    .el-icon {
      font-size: 20px;
    }
  }

  .category-name {
    font-size: 12px;
  }
}
</style>