package com.bookkeeping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bookkeeping.dao.TransactionRecordDao;
import com.bookkeeping.entity.dto.CategoryRateDTO;
import com.bookkeeping.entity.dto.CycleBarDTO;
import com.bookkeeping.entity.model.TransactionRecord;
import com.bookkeeping.entity.vo.CategoryRateVO;
import com.bookkeeping.entity.vo.CycleBarVO;
import com.bookkeeping.mapper.TransactionRecordMapper;
import com.bookkeeping.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author bookkeeping
 **/
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final TransactionRecordMapper transactionRecordMapper;
    private final TransactionRecordDao transactionRecordDao;

    @Override
    public List<CategoryRateVO> expenditureRate(Long userId, CategoryRateDTO dto) {
        LambdaQueryWrapper<TransactionRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TransactionRecord::getUserId, userId);
        // 计算出年份对应的开始日期和结束日期，带入条件
        queryWrapper.ge(TransactionRecord::getTransactionDate, dto.getYear() + "-01-01");
        queryWrapper.lt(TransactionRecord::getTransactionDate, dto.getYear() + "-12-31");
        List<TransactionRecord> records = transactionRecordMapper.selectList(queryWrapper);

        List<CategoryRateVO> categoryRateVoS = new ArrayList<>();
        Map<String, Long> categoryAmountMap = records.stream()
                .collect(Collectors.groupingBy(TransactionRecord::getCategoryName, Collectors.summingLong(TransactionRecord::getAmount)));
        // 遍历map，将每个分类名称和对应的金额总数封装到CategoryRateVO对象中，并添加到categoryRateVOS列表中
        categoryAmountMap.forEach((categoryName, amountTotal) -> {
            CategoryRateVO categoryRateVO = new CategoryRateVO();
            categoryRateVO.setCategoryName(categoryName);
            categoryRateVO.setAmountTotal(amountTotal);
            categoryRateVoS.add(categoryRateVO);
        });
        return categoryRateVoS;
    }

    @Override
    public List<CycleBarVO> cycleBar(Long userId, CycleBarDTO dto) {
        List<CycleBarVO> result;
        List<CycleBarVO> newResult = new ArrayList<>();
        List<String> periodList;
        switch (dto.getCycle()) {
            case WEEK:
                result = transactionRecordDao.selectWeekData(userId);
                periodList = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
                break;
            case MONTH:
                result = transactionRecordDao.selectMonthData(userId);
                periodList = getDaysInCurrentMonth();
                break;
            case YEAR:
                result = transactionRecordDao.selectYearData(userId);
                periodList = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
                break;
            default:
                result = new ArrayList<>();
                periodList = new ArrayList<>();
                break;
        }
        for (String period : periodList) {
            CycleBarVO cycleBarVO = new CycleBarVO();
            cycleBarVO.setDate(period);
            cycleBarVO.setAmount(0L);
            for (CycleBarVO cycleBarVo1 : result) {
                if (cycleBarVo1.getDate().equals(period)) {
                    cycleBarVO.setAmount(cycleBarVo1.getAmount());
                    break;
                }
            }
            newResult.add(cycleBarVO);
        }
        return newResult;
    }

    private List<String> getDaysInCurrentMonth() {
        LocalDate today = LocalDate.now();
        YearMonth yearMonth = YearMonth.from(today);
        int firstDay = 1;
        int lastDay = yearMonth.lengthOfMonth();

        return IntStream.rangeClosed(firstDay, lastDay)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
    }


}
