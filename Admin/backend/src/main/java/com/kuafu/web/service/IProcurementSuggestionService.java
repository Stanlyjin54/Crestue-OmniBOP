package com.kuafu.web.service;

import java.util.List;
import java.util.Map;

/**
 * IProcurementSuggestionService - rebuilt service interface
 */
public interface IProcurementSuggestionService {
    
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