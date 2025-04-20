import { requestClient } from '#/api/request';

export namespace StatisticsApi {
  /** 支出比例请求参数 */
  export interface ExpenditureRateParams {
    year: string;
  }
  /** 支出比例结果 */
  export interface ExpenditureRateResult {
    categoryName: string;
    amountTotal: number;
  }

  type Cycle = 'MONTH' | 'WEEK' | 'YEAR';
  /**
   * 支出柱状图请求参数
   */
  export interface CycleBarParams {
    // 限制为WEEK/MONTH/YEAR
    cycle: Cycle;
  }
  /** 支出柱状图结果 */
  export interface CycleBarResult {
    date: string;
    amount: number;
  }
}

/**
 * 获取支出比例
 */
export async function getExpenditureRateApi(
  params: StatisticsApi.ExpenditureRateParams,
) {
  return requestClient.get<StatisticsApi.ExpenditureRateResult[]>(
    '/statistics/expenditure-rate',
    {
      params,
    },
  );
}

/**
 * 获取支出柱状图
 */
export async function getCycleBarApi(params: StatisticsApi.CycleBarParams) {
  return requestClient.get<StatisticsApi.CycleBarResult[]>(
    '/statistics/cycle-bar',
    {
      params,
    },
  );
}

/**
 * 获取天气数据
 */
export async function getWeatherApi() {
  return requestClient.get<string>('/statistics/weather');
}

/**
 * 获取节日数据
 */
export async function getFestivalApi() {
  return requestClient.get<string>('/statistics/festival');
}
