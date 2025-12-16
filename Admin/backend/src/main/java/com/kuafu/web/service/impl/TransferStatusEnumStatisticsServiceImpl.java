package com.kuafuweb.web.service.impl;

import java.util.List;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.kuafuweb.web.mapper.TransferStatusEnumStatisticsMapper;
import com.kuafuweb.web.entity.TransferStatusEnum;
import com.kuafuweb.web.service.ITransferStatusEnumStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> transfer_status_enum 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("TransferStatusEnumStatistics")
public class TransferStatusEnumStatisticsServiceImpl extends ServiceImpl<TransferStatusEnumStatisticsMapper, TransferStatusEnum> implements ITransferStatusEnumStatisticsService {
    @Resource
    private TransferStatusEnumStatisticsMapper transferStatusEnumStaticMapper;

                        @Override
                        public List<Object> transfer_status_enum_transfer_status_enum_id_status_statistic_5bfcb35a_count(LambdaQueryWrapper queryWrapper) {
                                return transferStatusEnumStaticMapper.transfer_status_enum_transfer_status_enum_id_status_statistic_5bfcb35a_count(queryWrapper);
                                }


                        @Override
                        public List<Object> transfer_status_enum_transfer_status_enum_id_status_statistic_35b1149a_count(LambdaQueryWrapper queryWrapper) {
                                return transferStatusEnumStaticMapper.transfer_status_enum_transfer_status_enum_id_status_statistic_35b1149a_count(queryWrapper);
                                }


                        @Override
                        public List<Object> transfer_status_enum_transfer_status_enum_id_status_statistic_26443357_count(LambdaQueryWrapper queryWrapper) {
                                return transferStatusEnumStaticMapper.transfer_status_enum_transfer_status_enum_id_status_statistic_26443357_count(queryWrapper);
                                }


                        @Override
                        public List<Object> transfer_status_enum_status_name_status_statistic_29f93e61_count(LambdaQueryWrapper queryWrapper) {
                                return transferStatusEnumStaticMapper.transfer_status_enum_status_name_status_statistic_29f93e61_count(queryWrapper);
                                }


                        @Override
                        public List<Object> transfer_status_enum_status_name_status_statistic_a78fbbbf_count(LambdaQueryWrapper queryWrapper) {
                                return transferStatusEnumStaticMapper.transfer_status_enum_status_name_status_statistic_a78fbbbf_count(queryWrapper);
                                }


                        @Override
                        public List<Object> transfer_status_enum_status_name_status_statistic_58b07edd_count(LambdaQueryWrapper queryWrapper) {
                                return transferStatusEnumStaticMapper.transfer_status_enum_status_name_status_statistic_58b07edd_count(queryWrapper);
                                }


}





