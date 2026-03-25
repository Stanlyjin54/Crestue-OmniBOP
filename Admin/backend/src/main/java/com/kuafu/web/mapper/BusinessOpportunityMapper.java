package com.kuafu.web.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

/**
 * BusinessOpportunityMapper - rebuilt mapper
 */
@Mapper
public interface BusinessOpportunityMapper {
    
    /**
     * Find all records
     */
    @Select("SELECT * FROM placeholder_table")
    List<Map<String, Object>> findAll();
    
    /**
     * Find by id
     */
    @Select("SELECT * FROM placeholder_table WHERE id = #{id}")
    Map<String, Object> findById(Long id);
    
    /**
     * Insert record
     */
    int insert(Map<String, Object> record);
    
    /**
     * Update record
     */
    int update(Map<String, Object> record);
    
    /**
     * Delete by id
     */
    int deleteById(Long id);
}