package com.kuafu.api.gateway.service.impl;

import com.kuafu.api.gateway.service.RateLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 限流服务实现，基于令牌桶算法
 */
@Service
@Slf4j
public class RateLimitServiceImpl implements RateLimitService {
    
    // 存储每个限流键的令牌桶信息
    private final Map<String, TokenBucket> tokenBuckets = new ConcurrentHashMap<>();
    
    @Override
    public boolean tryAcquire(String key, int limit) {
        // 获取或创建令牌桶
        TokenBucket bucket = tokenBuckets.computeIfAbsent(key, k -> new TokenBucket(limit));
        
        // 尝试获取令牌
        return bucket.tryAcquire();
    }
    
    @Override
    public void reset(String key) {
        tokenBuckets.remove(key);
    }
    
    @Override
    public long getCurrentCount(String key) {
        TokenBucket bucket = tokenBuckets.get(key);
        return bucket != null ? bucket.getCurrentTokens() : 0;
    }
    
    /**
     * 令牌桶类
     */
    private static class TokenBucket {
        private final int capacity; // 令牌桶容量
        private final AtomicLong tokens; // 当前令牌数
        private long lastRefillTime; // 上次填充令牌时间
        
        public TokenBucket(int capacity) {
            this.capacity = capacity;
            this.tokens = new AtomicLong(capacity);
            this.lastRefillTime = System.currentTimeMillis();
        }
        
        public boolean tryAcquire() {
            // 先填充令牌
            refill();
            
            // 尝试获取令牌
            long currentTokens = tokens.get();
            if (currentTokens > 0) {
                return tokens.compareAndSet(currentTokens, currentTokens - 1);
            }
            
            return false;
        }
        
        private void refill() {
            long now = System.currentTimeMillis();
            long elapsedTime = now - lastRefillTime;
            
            // 如果时间间隔超过1秒，填充令牌
            if (elapsedTime > 1000) {
                // 计算需要填充的令牌数
                int tokensToAdd = (int) (elapsedTime / 1000 * capacity);
                
                // 原子更新令牌数，不超过容量
                long currentTokens = tokens.get();
                long newTokens = Math.min(currentTokens + tokensToAdd, capacity);
                
                if (tokens.compareAndSet(currentTokens, newTokens)) {
                    // 更新上次填充时间
                    lastRefillTime = now;
                }
            }
        }
        
        public long getCurrentTokens() {
            refill();
            return tokens.get();
        }
    }
}
