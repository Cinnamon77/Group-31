package com.bookkeeping.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.bookkeeping.core.api.CommonResult;
import com.bookkeeping.core.util.RedisKey;
import com.bookkeeping.entity.dto.CategoryRateDTO;
import com.bookkeeping.entity.dto.CycleBarDTO;
import com.bookkeeping.entity.vo.CategoryRateVO;
import com.bookkeeping.entity.vo.CycleBarVO;
import com.bookkeeping.service.StatisticsService;
import com.bookkeeping.service.impl.StringRedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author bookkeeping
 **/
@Tag(name = "统计相关")
@RequestMapping(value = "api/statistics")
@RestController(value = "StatisticsController")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;
    private final StringRedisService stringRedisService;

    @Operation(summary = "支出占比数据")
    @RequestMapping(value = "expenditure-rate", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<CategoryRateVO>> expenditureRate(@ParameterObject @Validated CategoryRateDTO dto) {
        return CommonResult.success(statisticsService.expenditureRate(StpUtil.getLoginIdAsLong(), dto));
    }

    @Operation(summary = "周期柱状图数据")
    @RequestMapping(value = "cycle-bar", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<CycleBarVO>> cycleBar(@ParameterObject @Validated CycleBarDTO dto) {
        return CommonResult.success(statisticsService.cycleBar(StpUtil.getLoginIdAsLong(), dto));
    }

    @Operation(summary = "天气数据")
    @RequestMapping(value = "weather", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<String> weather() {
        return CommonResult.success(stringRedisService.getString(RedisKey.getWeatherKey()));
    }

    @Operation(summary = "节日数据")
    @RequestMapping(value = "festival", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<String> festival() {
        return CommonResult.success(stringRedisService.getString(RedisKey.getFestival()));
    }
}
