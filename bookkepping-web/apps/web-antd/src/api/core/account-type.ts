import { requestClient } from '#/api/request';

export namespace AccountTypeApi {
  /** 账号类型列表结果 */
  export interface AccountTypeResult {
    id: number;
    accountName: string;
    icon: string;
    createdAt: string;
    updatedAt: string;
  }
  /** 新增账号类型参数 */
  export interface AccountTypeParams {
    name: string;
    icon: string;
  }
}

/**
 * 获取账号类型列表
 */
export async function getAccountTypeListApi() {
  return requestClient.get<AccountTypeApi.AccountTypeResult[]>(
    '/account-type/list',
  );
}

/**
 * 新增账号类型
 */
export async function addAccountTypeApi(
  data: AccountTypeApi.AccountTypeParams,
) {
  return requestClient.post<null>('/account-type', data);
}

/**
 * 编辑账号类型
 */
export async function editAccountTypeApi(
  id: number,
  data: AccountTypeApi.AccountTypeParams,
) {
  return requestClient.put<null>(`/account-type/${id}`, data);
}

/**
 * 删除账号类型
 */
export async function deleteAccountTypeApi(id: number) {
  return requestClient.delete<null>(`/account-type/${id}`);
}
