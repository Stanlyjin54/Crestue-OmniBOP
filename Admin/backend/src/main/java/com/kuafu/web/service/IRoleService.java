package com.kuafu.web.service;

import java.util.List;
import java.util.Map;

/**
 * IRoleService - rebuilt service interface
 */
public interface IRoleService {
    
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