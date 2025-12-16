package com.kuafu.web.mapper;

import java.util.List;
import com.kuafu.web.entity.RolePermissionAll;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

/**
 * <p> 角色权限关联 Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface RolePermissionAllMapper extends BaseMapper<RolePermissionAll> {
    public List<RolePermissionAll> selectListNew(@Param("ew") Wrapper<RolePermissionAll> queryWrapper);
}
