<template>
  <div class="price-display" :class="[`price-display--${size}`, { 'price-display--has-tier': hasTierPricing }]">
    <!-- 当前价格 -->
    <div class="current-price">
      <span class="currency" v-if="showCurrency">{{ currencySymbol }}</span>
      <span class="price-value">{{ formatPrice(currentPrice) }}</span>
      <span class="unit" v-if="showUnit && priceData.priceType === 'unit'">/件</span>
    </div>

    <!-- 原价 -->
    <div v-if="showOriginalPrice && originalPrice > currentPrice" class="original-price">
      <span class="currency">{{ currencySymbol }}</span>
      <span class="price-value">{{ formatPrice(originalPrice) }}</span>
    </div>

    <!-- 折扣标签 -->
    <div v-if="showDiscount && discountPercent" class="discount-tag">
      {{ discountPercent }}折
    </div>

    <!-- 阶梯价格展开 -->
    <div v-if="hasTierPricing && showTierPricing" class="tier-pricing">
      <div class="tier-pricing-header" @click="toggleTierPricing">
        <span class="tier-title">
          <el-icon><ArrowDown /></el-icon>
          阶梯价格
        </span>
        <span class="tier-save" v-if="tierDiscount > 0">
          最高省{{ tierDiscount }}%
        </span>
      </div>
      
      <el-collapse-transition>
        <div v-show="isTierPricingExpanded" class="tier-pricing-content">
          <div class="tier-table">
            <div class="tier-header">
              <div class="tier-qty">数量</div>
              <div class="tier-price">单价</div>
              <div class="tier-save">节省</div>
            </div>
            
            <div v-for="(tier, index) in tierPricing" :key="index" 
                 class="tier-row"
                 :class="{ 'tier-row--recommended': index === recommendedTierIndex }">
              <div class="tier-qty">
                <span v-if="tier.minQty && tier.maxQty">
                  {{ tier.minQty }} - {{ tier.maxQty }}件
                </span>
                <span v-else-if="tier.minQty">
                  {{ tier.minQty }}件以上
                </span>
                <span v-else>
                  {{ tier.maxQty || tier.minQty }}件
                </span>
                <el-tag v-if="index === recommendedTierIndex" size="small" type="success" class="recommended-tag">
                  推荐
                </el-tag>
              </div>
              
              <div class="tier-price">
                <span class="tier-unit-price">{{ currencySymbol }}{{ formatPrice(tier.price) }}</span>
                <span v-if="tier.discountPercent" class="tier-discount">
                  ({{ tier.discountPercent }}% off)
                </span>
              </div>
              
              <div class="tier-save">
                <span v-if="tier.totalSavings" class="tier-savings">
                  {{ currencySymbol }}{{ formatPrice(tier.totalSavings) }}
                </span>
                <span v-else class="tier-savings">
                  {{ tier.discountPercent }}%
                </span>
              </div>
            </div>
          </div>
          
          <div class="tier-pricing-note" v-if="tierPricingNote">
            <el-icon><InfoFilled /></el-icon>
            <span>{{ tierPricingNote }}</span>
          </div>
        </div>
      </el-collapse-transition>
    </div>

    <!-- 价格变动提示 -->
    <div v-if="priceChangeInfo" class="price-change-info" :class="[`price-change--${priceChangeInfo.type}`]">
      <el-icon>
        <ArrowUp v-if="priceChangeInfo.type === 'increase'" />
        <ArrowDown v-if="priceChangeInfo.type === 'decrease'" />
        <Warning v-if="priceChangeInfo.type === 'warning'" />
      </el-icon>
      <span>{{ priceChangeInfo.message }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
// 图标已在main.ts中全局注册，无需单独导入

interface TierPrice {
  minQty: number
  maxQty?: number
  price: number
  discountPercent?: number
  totalSavings?: number
}

interface PriceData {
  current: number
  original?: number
  currency?: string
  priceType?: string
  tierPricing?: TierPrice[]
  validUntil?: string
}

export interface Props {
  priceData: PriceData
  size?: 'small' | 'medium' | 'large'
  showCurrency?: boolean
  showOriginalPrice?: boolean
  showDiscount?: boolean
  showTierPricing?: boolean
  showUnit?: boolean
  recommendedQuantity?: number
  tierPricingNote?: string
  priceChangeInfo?: {
    type: 'increase' | 'decrease' | 'warning'
    message: string
  }
}

export interface Emits {
  (e: 'tier-select', tier: TierPrice): void
  (e: 'quantity-change', quantity: number): void
}

const props = withDefaults(defineProps<Props>(), {
  size: 'medium',
  showCurrency: true,
  showOriginalPrice: true,
  showDiscount: true,
  showTierPricing: true,
  showUnit: false,
  recommendedQuantity: 1
})

const emit = defineEmits<Emits>()

const isTierPricingExpanded = ref(false)

const currencySymbol = computed(() => {
  const currency = props.priceData.currency || 'CNY'
  const symbols = {
    'CNY': '¥',
    'USD': '$',
    'EUR': '€',
    'GBP': '£',
    'JPY': '¥'
  }
  return symbols[currency as keyof typeof symbols] || currency
})

const currentPrice = computed(() => {
  return props.priceData.current || 0
})

const originalPrice = computed(() => {
  return props.priceData.original || currentPrice.value
})

const hasTierPricing = computed(() => {
  return props.priceData.tierPricing && props.priceData.tierPricing.length > 0
})

const tierPricing = computed(() => {
  return props.priceData.tierPricing || []
})

const discountPercent = computed(() => {
  if (originalPrice.value > currentPrice.value) {
    return Math.round((currentPrice.value / originalPrice.value) * 10)
  }
  return null
})

const tierDiscount = computed(() => {
  if (!hasTierPricing.value) return 0
  
  const maxDiscount = Math.max(...tierPricing.value.map(tier => tier.discountPercent || 0))
  return maxDiscount
})

const recommendedTierIndex = computed(() => {
  if (!hasTierPricing.value) return -1
  
  const recommendedQty = props.recommendedQuantity
  return tierPricing.value.findIndex(tier => {
    const minOk = !tier.minQty || recommendedQty >= tier.minQty
    const maxOk = !tier.maxQty || recommendedQty <= tier.maxQty
    return minOk && maxOk
  })
})

const formatPrice = (price: number) => {
  if (price < 1) {
    return price.toFixed(3) // 小于1元显示3位小数
  } else if (price < 100) {
    return price.toFixed(2) // 小于100元显示2位小数
  } else {
    return price.toFixed(0) // 大于100元显示整数
  }
}

const toggleTierPricing = () => {
  isTierPricingExpanded.value = !isTierPricingExpanded.value
}
</script>

<style scoped lang="scss">
.price-display {
  display: inline-flex;
  flex-direction: column;
  gap: 4px;
  line-height: 1;

  &--small {
    .current-price {
      font-size: 14px;
      font-weight: 600;
    }
    
    .original-price {
      font-size: 12px;
    }
    
    .discount-tag {
      font-size: 10px;
      padding: 2px 4px;
    }
  }

  &--medium {
    .current-price {
      font-size: 18px;
      font-weight: bold;
    }
    
    .original-price {
      font-size: 14px;
    }
    
    .discount-tag {
      font-size: 12px;
      padding: 3px 6px;
    }
  }

  &--large {
    .current-price {
      font-size: 24px;
      font-weight: bold;
    }
    
    .original-price {
      font-size: 16px;
    }
    
    .discount-tag {
      font-size: 14px;
      padding: 4px 8px;
    }
  }
}

.current-price {
  display: flex;
  align-items: baseline;
  gap: 2px;
  color: #ff6b6b;
  font-weight: bold;

  .currency {
    font-size: 0.8em;
    opacity: 0.9;
  }

  .price-value {
    font-family: 'Arial', sans-serif;
  }
}

.original-price {
  color: #999;
  text-decoration: line-through;
  display: flex;
  align-items: baseline;
  gap: 2px;

  .currency {
    font-size: 0.8em;
  }
}

.discount-tag {
  background: #ff4757;
  color: white;
  border-radius: 4px;
  font-weight: 500;
  align-self: flex-start;
}

.tier-pricing {
  margin-top: 8px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  overflow: hidden;
  background: #f8f9fa;

  .tier-pricing-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px 12px;
    cursor: pointer;
    transition: background-color 0.2s;
    background: #fff;

    &:hover {
      background: #f8f9fa;
    }

    .tier-title {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 13px;
      font-weight: 500;
      color: #333;

      .el-icon {
        transition: transform 0.3s;
      }
    }

    .tier-save {
      font-size: 12px;
      color: #52c41a;
      font-weight: 500;
    }
  }

  .tier-pricing-content {
    border-top: 1px solid #e9ecef;
  }

  .tier-table {
    .tier-header {
      display: grid;
      grid-template-columns: 1fr 1fr 1fr;
      gap: 8px;
      padding: 8px 12px;
      background: #f8f9fa;
      font-size: 12px;
      font-weight: 600;
      color: #666;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }

    .tier-row {
      display: grid;
      grid-template-columns: 1fr 1fr 1fr;
      gap: 8px;
      padding: 10px 12px;
      border-bottom: 1px solid #f0f0f0;
      transition: background-color 0.2s;

      &:hover {
        background: #fafafa;
      }

      &:last-child {
        border-bottom: none;
      }

      &--recommended {
        background: #f6ffed;
        border-color: #b7eb8f;

        &:hover {
          background: #f6ffed;
        }
      }

      .tier-qty {
        position: relative;
        font-size: 13px;
        color: #333;
        font-weight: 500;

        .recommended-tag {
          margin-left: 8px;
        }
      }

      .tier-price {
        display: flex;
        flex-direction: column;
        gap: 2px;

        .tier-unit-price {
          font-size: 14px;
          font-weight: 600;
          color: #ff6b6b;
          font-family: 'Arial', sans-serif;
        }

        .tier-discount {
          font-size: 11px;
          color: #52c41a;
        }
      }

      .tier-save {
        font-size: 13px;
        color: #52c41a;
        font-weight: 500;
      }
    }
  }

  .tier-pricing-note {
    padding: 8px 12px;
    background: #f0f9ff;
    border-top: 1px solid #e6f7ff;
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 12px;
    color: #1890ff;

    .el-icon {
      flex-shrink: 0;
    }
  }
}

.price-change-info {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;

  .el-icon {
    font-size: 14px;
  }

  &.price-change--increase {
    background: #fff2f0;
    color: #ff4d4f;
    border: 1px solid #ffccc7;
  }

  &.price-change--decrease {
    background: #f6ffed;
    color: #52c41a;
    border: 1px solid #b7eb8f;
  }

  &.price-change--warning {
    background: #fffbe6;
    color: #faad14;
    border: 1px solid #ffe58f;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .price-display {
    &--large {
      .current-price {
        font-size: 20px;
      }
    }
  }

  .tier-pricing {
    .tier-table {
      .tier-header,
      .tier-row {
        grid-template-columns: 1fr;
        gap: 4px;
        text-align: left;
      }

      .tier-header {
        display: none;
      }

      .tier-row {
        padding: 8px;

        .tier-qty,
        .tier-price,
        .tier-save {
          display: flex;
          align-items: center;
          gap: 8px;

          &::before {
            content: attr(data-label);
            font-weight: 600;
            color: #666;
            min-width: 50px;
            text-transform: uppercase;
            font-size: 11px;
          }
        }

        .tier-qty::before { content: "数量"; }
        .tier-price::before { content: "单价"; }
        .tier-save::before { content: "节省"; }
      }
    }
  }
}
</style>