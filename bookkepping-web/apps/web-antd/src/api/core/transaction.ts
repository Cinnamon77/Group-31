import type { Page } from '#/api/core/page';

import { requestClient } from '#/api/request';

export namespace TransactionApi {
  // 交易记录参数
  export interface TransactionParams {
    amount: number;
    categoryId: number;
    accountTypeId: number;
    date: string;
  }

  // 分页查询参数
  export interface TransactionListParams {
    page: number;
    pageSize: number;
  }
  // 交易记录列表结果
  export interface TransactionResult {
    id: number;
    userId: number;
    username: string;
    amount: number;
    transactionDate: string;
    categoryName: string;
    categoryIcon: string;
    accountName: string;
    accountIcon: string;
    createdAt: string;
    updatedAt: string;
  }
}

/**
 * 新增交易记录
 */
export async function addTransactionApi(
  data: TransactionApi.TransactionParams,
) {
  return requestClient.post<TransactionApi.TransactionParams>(
    '/transaction',
    data,
  );
}

/**
 * 修改交易记录
 */
export async function updateTransactionApi(
  id: number,
  data: TransactionApi.TransactionParams,
) {
  return requestClient.put<TransactionApi.TransactionParams>(
    `/transaction/${id}`,
    data,
  );
}
/**
 * 获取分页数据
 */
export async function getTransactionListApi(
  params: TransactionApi.TransactionListParams,
) {
  return requestClient.get<Page.PageResult<TransactionApi.TransactionResult>>(
    '/transaction/list',
    { params },
  );
}
