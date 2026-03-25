package com.kuafu.pay.db.service;

import java.util.List;
import java.util.Map;

/**
 * GeneralOrdersService - rebuilt service interface
 */
public interface GeneralOrdersService {
    
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