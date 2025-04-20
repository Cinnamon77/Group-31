import { requestClient } from '#/api/request';

export namespace TransactionCategoryApi {
  /** 交易分类列表结果 */
  export interface TransactionCategoryResult {
    id: number;
    categoryName: string;
    icon: string;
    createdAt: string;
    updatedAt: string;
  }
  /** 新增交易分类参数 */
  export interface TransactionCategoryParams {
    name: string;
    icon: string;
  }
}

/**
 * 获取交易分类列表
 */
export async function getTransactionCategoryListApi() {
  return requestClient.get<TransactionCategoryApi.TransactionCategoryResult[]>(
    '/category/list',
  );
}

/**
 * 新增交易分类
 */
export async function addTransactionCategoryApi(
  data: TransactionCategoryApi.TransactionCategoryParams,
) {
  return requestClient.post<null>('/category', data);
}

/**
 * 编辑交易分类
 */
export async function editTransactionCategoryApi(
  id: number,
  data: TransactionCategoryApi.TransactionCategoryParams,
) {
  return requestClient.put<null>(`/category/${id}`, data);
}

/**
 * 删除交易分类
 */
export async function deleteTransactionCategoryApi(id: number) {
  return requestClient.delete<null>(`/category/${id}`);
}
