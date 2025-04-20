package com.bookkeeping.dao;

import com.bookkeeping.entity.vo.CycleBarVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * @author bookkeeping
 */
public interface TransactionRecordDao {

    // 查询最近7天的交易数据统计
    List<CycleBarVO> selectWeekData(Long userId);

    // 查询本月的交易数据统计
    List<CycleBarVO> selectMonthData(Long userId);

    // 查询本年的交易数据统计
    List<CycleBarVO> selectYearData(Long userId);

    /**
     * 获取用户指定月份支出总额
     */
    Long getSpendingTotalAmount(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
