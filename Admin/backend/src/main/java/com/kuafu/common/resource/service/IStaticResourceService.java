package com.kuafu.common.resource.service;

import java.util.List;
import java.util.Map;

/**
 * IStaticResourceService - rebuilt service interface
 */
public interface IStaticResourceService {
    
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