import { requestClient } from '#/api/request';

export namespace WalletApi {
  /** 钱包列表结果 */
  export interface WalletResult {
    id: number;
    userId: number;
    username: string;
    accountName: string;
    accountIcon: string;
    balance: number;
    createdAt: string;
    updatedAt: string;
  }
  /** 新增钱包参数 */
  export interface AddWalletParams {
    accountTypeName: string;
    amount: number;
  }
}

/**
 * 获取钱包列表
 */
export async function getWalletListApi() {
  return requestClient.get<WalletApi.WalletResult[]>('/wallet/list');
}

/**
 * 新增钱包
 */
export async function addWalletAmountApi(data: WalletApi.AddWalletParams) {
  return requestClient.post<null>('/wallet/amount', data);
}
