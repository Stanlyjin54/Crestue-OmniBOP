package com.kuafu.web.mapper;

import com.kuafu.web.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * RoleMapper - rebuilt mapper
 */
@Mapper
public interface RoleMapper {
    
    /**
     * Find all roles
     */
    @Select("SELECT * FROM role")
    List<Role> findAll();
    
    /**
     * Find by id
     */
    @Select("SELECT * FROM role WHERE id = #{id}")
    Role findById(Long id);
    
    /**
     * Insert role
     */
    int insert(Role role);
    
    /**
     * Update role
     */
    int updateById(Role role);
    
    /**
     * Delete by id
     */
    int deleteById(Long id);
}