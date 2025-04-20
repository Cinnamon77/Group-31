import { requestClient } from '#/api/request';

export namespace BudgetApi {
  /** 预算详情结果 */
  export interface BudgetResult {
    id: number;
    userId: number;
    username: string;
    amount: number;
    budgetMonth: string;
    startDate: string;
    endDate: string;
    createdAt: string;
    updatedAt: string;
    spendingTotalAmount: number;
  }

  /** 更新预算参数 */
  export interface UpdateBudgetParams {
    amount: number;
  }
}

/**
 * 获取预算详情
 */
export async function getBudgetDetailApi() {
  return requestClient.get<BudgetApi.BudgetResult>('/budget');
}

/**
 * 更新预算
 */
export async function updateBudgetApi(params: BudgetApi.UpdateBudgetParams) {
  return requestClient.put('/budget', params);
}
