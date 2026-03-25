package com.kuafu.pay.business;

import java.util.List;
import java.util.Map;

/**
 * OrderFacadeService - rebuilt service interface
 */
public interface OrderFacadeService {
    
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