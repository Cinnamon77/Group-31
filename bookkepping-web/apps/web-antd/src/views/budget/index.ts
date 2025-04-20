import type { AccountTypeApi } from '#/api/core/account-type';
import type { WalletApi } from '#/api/core/wallet';

import { ref } from 'vue';

import { message } from 'ant-design-vue';

import { getAccountTypeListApi } from '#/api/core/account-type';
import { getBudgetDetailApi, updateBudgetApi } from '#/api/core/budget';
import { addWalletAmountApi, getWalletListApi } from '#/api/core/wallet';
// 资产数据
export const propertyAmount = ref('0.00');
export const breakdownData = ref<WalletApi.WalletResult[]>([]);

// 新增资产弹窗
export const showAddModal = ref(false);
export const addForm = ref({
  accountTypeName: '',
  amount: '',
});

// AI对话
export const aiMessage = ref('');
export const aiPlaceholder = ref('Ask me anything');

// 预算数据
export const monthlyBudget = ref(0);
export const monthlySpending = ref(0);
export const remainingBudget = ref(0);
export const budgetRatio = ref(0);
export const remainingDays = ref(0);

interface DailyStats {
  dailyExpense: number;
  dailyBudget: number;
  dailyRemaining: number;
}
// 日均数据
export const dailyStats = ref<DailyStats>({
  dailyExpense: 0,
  dailyBudget: 0,
  dailyRemaining: 0,
});

// 储蓄计划数据
export const savedAmount = ref('2347.94');
export const remainAmount = ref('2652.06');

// 获取钱包数据
export const getWalletList = async () => {
  getWalletListApi().then((res) => {
    // 金额需要由分转换为元
    breakdownData.value = res.map((item) => ({
      ...item,
      balance: Number.parseFloat(item.balance.toString()) / 100,
    }));
    // calc propertyAmount
    propertyAmount.value = res
      .reduce((acc, curr) => acc + curr.balance / 100, 0)
      .toFixed(2);
  });
};

// 新增资产
export const handleAddProperty = async () => {
  try {
    await addWalletAmountApi({
      accountTypeName: addForm.value.accountTypeName,
      amount: Number.parseFloat(addForm.value.amount) * 100,
    });
    showAddModal.value = false;
    addForm.value = {
      accountTypeName: '',
      amount: '',
    };
    await getWalletList();
  } catch (error) {
    console.error('Failed to add property:', error);
  }
};

// 账号类型
export const accountTypes = ref<AccountTypeApi.AccountTypeResult[]>([]);
// 获取账号类型
export const getAccountTypeList = async () => {
  const res = await getAccountTypeListApi();
  accountTypes.value = res;
};

// 获取预算详情
export const getBudgetDetail = async () => {
  getBudgetDetailApi().then((res) => {
    calcBudgetInfo(res.amount / 100, res.spendingTotalAmount / 100);
  });
};

/** 根据预算总金额和支出总金额计算预算信息 */
export const calcBudgetInfo = (
  budgetAmount: number,
  spendingAmount: number,
) => {
  monthlyBudget.value = budgetAmount;
  monthlySpending.value = spendingAmount;
  remainingBudget.value = budgetAmount - spendingAmount;

  // budgetRatio - 处理预算为0的情况
  budgetRatio.value =
    budgetAmount === 0
      ? 0
      : Math.min((spendingAmount / budgetAmount) * 100, 100);

  // Remaining Days 根据当前时间计算剩余天数
  const currentDate = new Date();
  const endDate = new Date(
    currentDate.getFullYear(),
    currentDate.getMonth() + 1,
    0,
  );

  const timeDiff = endDate.getTime() - currentDate.getTime();
  remainingDays.value = Math.ceil(timeDiff / (1000 * 60 * 60 * 24));

  // 计算日均数据
  const daysInMonthBalance = remainingDays.value || 1;

  // 本月最大号数
  const maxDay = new Date(
    currentDate.getFullYear(),
    currentDate.getMonth() + 1,
    0,
  ).getDate();

  // 每月平均每日开支，本月总开支 / 当前号数
  dailyStats.value.dailyExpense = spendingAmount / currentDate.getDate();
  // 每月平均每日预算，本月总预算 / 本月多少天
  dailyStats.value.dailyBudget = budgetAmount / maxDay;
  // 剩余每日可开支，(本月总预算 - 本月总开支) / 剩余天数
  dailyStats.value.dailyRemaining =
    (budgetAmount - spendingAmount) / daysInMonthBalance;
};

/** 更新预算 */
export const updateBudget = async (amount: number) => {
  calcBudgetInfo(amount, Number(monthlySpending.value));
};

/** 保存预算 */
export const saveBudget = async () => {
  try {
    await updateBudgetApi({
      amount: Number(monthlyBudget.value) * 100,
    }).then(() => {
      message.success('Budget saved successfully');
    });
  } catch (error) {
    console.error('Failed to save budget:', error);
  }
};
