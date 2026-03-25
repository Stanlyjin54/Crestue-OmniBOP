package com.kuafu.login.service;

import java.util.List;
import java.util.Map;

/**
 * WxWebService - rebuilt service interface
 */
public interface WxWebService {
    
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