package com.kuafu.web.event;

import java.util.List;
import java.util.Map;

/**
 * MyEventService - rebuilt service interface
 */
public interface MyEventService {
    
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