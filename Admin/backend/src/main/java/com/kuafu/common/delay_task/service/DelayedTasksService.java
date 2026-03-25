package com.kuafu.common.delay_task.service;

import java.util.List;
import java.util.Map;

/**
 * DelayedTasksService - rebuilt service interface
 */
public interface DelayedTasksService {
    
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