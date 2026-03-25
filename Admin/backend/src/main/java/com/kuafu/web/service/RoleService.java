package com.kuafu.web.service;

import com.kuafu.web.entity.Role;
import java.util.List;
import java.util.Map;

/**
 * RoleService - service interface for role management
 */
public interface RoleService {
    
    /**
     * Get all roles
     */
    List<Role> getAllRoles();
    
    /**
     * Get role permissions
     */
    List<Map<String, Object>> getRolePermissions();
    
    /**
     * Get role statistics
     */
    Map<String, Object> getRoleStatistics();
    
    /**
     * Create a new role
     */
    boolean createRole(Role role);
    
    /**
     * Update an existing role
     */
    boolean updateRole(Role role);
    
    /**
     * Delete a role by id
     */
    boolean deleteRole(Long id);
}