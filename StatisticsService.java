package com.bookkeeping.service;

import com.bookkeeping.entity.dto.CategoryRateDTO;
import com.bookkeeping.entity.dto.CycleBarDTO;
import com.bookkeeping.entity.vo.CategoryRateVO;
import com.bookkeeping.entity.vo.CycleBarVO;

import java.util.List;

/**
 * 统计服务
 * @author bookkeeping
 */
public interface StatisticsService {

    /**
     * 获取支出占比统计数据
     * @param userId 用户ID
     * @param dto 查询参数
     * @return 统计数据
     */
    List<CategoryRateVO> expenditureRate(Long userId, CategoryRateDTO dto);

    /**
     * 获取周期柱状图统计数据
     * @param userId 用户ID
     * @param dto 查询参数
     * @return 统计数据
     */
    List<CycleBarVO> cycleBar(Long userId, CycleBarDTO dto);
}
