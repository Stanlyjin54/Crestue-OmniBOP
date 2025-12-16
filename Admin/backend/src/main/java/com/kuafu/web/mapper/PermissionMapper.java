package com.kuafuweb.web.mapper;

import java.util.List;
import com.kuafuweb.web.entity.Permission;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p> 权限 Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

}





