package com.kuafu.login.service;

import java.util.List;
import java.util.Map;

/**
 * WxAppService - rebuilt service interface
 */
public interface WxAppService {
    
    /**
     * Find all records
     */
    List<Map<String, Object>> findAll();
    
    /**
     * Find by id
     */
    Map<String, Object> findById(Long id);
    
    /**
     * Save record
     */
    int save(Map<String, Object> record);
}