package com.kuafu.web.service;

import java.util.List;
import java.util.Map;

/**
 * IProfitBoardStatisticsService - rebuilt service interface
 */
public interface IProfitBoardStatisticsService {
    
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