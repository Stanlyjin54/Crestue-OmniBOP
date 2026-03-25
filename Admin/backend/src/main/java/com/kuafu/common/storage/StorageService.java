package com.kuafu.common.storage;

import java.util.List;
import java.util.Map;

/**
 * StorageService - rebuilt service interface
 */
public interface StorageService {
    
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