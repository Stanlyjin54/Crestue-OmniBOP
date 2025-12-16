package com.kuafuweb.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.common.domin.PageRequest;
import com.kuafuweb.web.entity.AiAssistantLogAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  AI助手日志 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IAiAssistantLogAllService extends IService<AiAssistantLogAll> {
        List<AiAssistantLogAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<AiAssistantLogAll> queryWrapper);
        List<AiAssistantLogAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<AiAssistantLogAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<AiAssistantLogAll> queryWrapper);

        IPage pageNew(IPage<AiAssistantLogAll> page, PageRequest pageRequest, LambdaQueryWrapper<AiAssistantLogAll> queryWrapper);
        }
