<script setup lang="ts">
import { onMounted } from 'vue';

import { IconifyIcon } from '@vben/icons';

import { Button, Card, Input, Modal, Select } from 'ant-design-vue';

import {
  accountTypes,
  addForm,
  aiMessage,
  aiPlaceholder,
  breakdownData,
  budgetRatio,
  dailyStats,
  getAccountTypeList,
  getBudgetDetail,
  getWalletList,
  handleAddProperty,
  monthlyBudget,
  monthlySpending,
  propertyAmount,
  remainingBudget,
  remainingDays,
  saveBudget,
  showAddModal,
  updateBudget,
} from './index';

const handleBudgetChange = (e: Event) => {
  const value = (e.target as HTMLInputElement).value;
  if (value) {
    updateBudget(Number(value));
  }
};

onMounted(async () => {
  getWalletList();
  getAccountTypeList();
  getBudgetDetail();
});
</script>

<template>
  <div class="p-5">
    <div class="flex gap-4">
      <!-- 左侧列：资产统计和AI对话 -->
      <div class="w-1/2 space-y-4">
        <!-- 资产统计 -->
        <Card>
          <div class="mb-4">
            <div class="mb-2 text-lg text-gray-600">Property (dollars)</div>
            <div class="flex items-center">
              <span class="text-3xl font-bold">${{ propertyAmount }}</span>
              <Button type="primary" class="ml-4" @click="showAddModal = true">
                Add
              </Button>
            </div>
          </div>

          <div class="mt-4">
            <div class="mb-2 flex items-center justify-between">
              <span class="text-gray-600">Breakdown</span>
              <span class="cursor-pointer text-gray-400">💳</span>
            </div>
            <div
              v-for="item in breakdownData"
              :key="item.id"
              class="flex items-center justify-between py-2"
            >
              <div class="flex items-center">
                <IconifyIcon :icon="item.accountIcon" class="mr-2 h-5 w-5" />
                <span>{{ item.accountName }}</span>
              </div>
              <span>${{ item.balance.toFixed(2) }}</span>
            </div>
          </div>
        </Card>

        <!-- AI对话 -->
        <Card title="AI Communication">
          <div class="flex h-[480px] flex-col">
            <div class="mb-4 flex-1 rounded-lg bg-gray-50 p-4">
              <div
                class="inline-block rounded-lg bg-blue-100 px-4 py-2 text-sm"
              >
                How much does it cost to travel to Tibet?
              </div>
            </div>
            <div class="flex gap-2">
              <Input
                v-model:value="aiMessage"
                :placeholder="aiPlaceholder"
                class="flex-1"
              />
              <Button>🔄</Button>
              <Button>📷</Button>
              <Button>🎤</Button>
              <Button type="primary">📤</Button>
            </div>
          </div>
        </Card>
      </div>

      <!-- 右侧列：预算管理 -->
      <Card class="w-1/2">
        <div class="mb-6">
          <div class="mb-2 text-gray-600">Monthly Budget</div>
          <div class="mb-4 flex items-center">
            <span class="mr-2">Budget:</span>
            <Input
              :value="monthlyBudget"
              placeholder="Enter your budget"
              class="flex-1"
              type="number"
              step="0.01"
              @change="handleBudgetChange"
            />
          </div>

          <div class="mb-2 text-gray-600">Remaining Monthly Budget</div>
          <div class="mb-4 text-2xl font-bold text-blue-600">
            ${{ remainingBudget.toFixed(2) }}
          </div>

          <div class="mb-4 grid grid-cols-2 gap-4">
            <div>
              <div class="text-gray-500">Monthly Spending</div>
              <div class="font-semibold">${{ monthlySpending.toFixed(2) }}</div>
            </div>
            <div>
              <div class="text-gray-500">Monthly Budget</div>
              <div class="font-semibold">${{ monthlyBudget.toFixed(2) }}</div>
            </div>
            <div>
              <div class="text-gray-500">Budget Remaining Ratio</div>
              <div class="font-semibold">{{ budgetRatio.toFixed(2) }}%</div>
            </div>
            <div>
              <div class="text-gray-500">Remaining Days</div>
              <div class="font-semibold">{{ remainingDays }} Days</div>
            </div>
          </div>

          <div class="space-y-2">
            <!-- 每月平均每日开支 -->
            <div class="flex justify-between">
              <span class="text-gray-600">Monthly Average Daily Expense</span>
              <span class="font-semibold">
                ${{ dailyStats.dailyExpense.toFixed(2) }}
              </span>
            </div>
            <!-- 每月平均每日预算 -->
            <div class="flex justify-between">
              <span class="text-gray-600">Monthly Average Daily Budget</span>
              <span class="font-semibold">
                ${{ dailyStats.dailyBudget.toFixed(2) }}
              </span>
            </div>
            <!-- 剩余每日可开支 -->
            <div class="flex justify-between">
              <span class="text-gray-600">
                Remaining Daily Allowable Spending Amount
              </span>
              <span class="font-semibold">
                ${{ dailyStats.dailyRemaining.toFixed(2) }}
              </span>
            </div>
          </div>
        </div>

        <!-- 储蓄计划 -->
        <div class="rounded-lg bg-blue-50 p-4">
          <div class="mb-4 flex items-center justify-between">
            <div class="flex items-center">
              <span class="mr-2 rounded-lg bg-blue-200 p-2">💰</span>
              <span class="font-semibold">Saving Plan</span>
            </div>
            <Button type="primary" @click="saveBudget">Save</Button>
          </div>
          <div class="flex justify-between">
            <div>
              <div class="text-gray-500">Saved</div>
              <div class="font-semibold">${{ monthlySpending.toFixed(2) }}</div>
            </div>
            <div>
              <div class="text-gray-500">Remain</div>
              <div class="font-semibold">${{ monthlyBudget.toFixed(2) }}</div>
            </div>
          </div>
          <div class="mt-4 h-2 rounded-full bg-blue-100">
            <div
              class="h-full rounded-full bg-blue-500"
              :style="{ width: `${budgetRatio}%` }"
            ></div>
          </div>
        </div>
      </Card>
    </div>
    <div>
      <Modal
        :open="showAddModal"
        title="Add Property"
        @ok="handleAddProperty"
        @cancel="() => (showAddModal = false)"
      >
        <div class="space-y-4">
          <div>
            <div class="mb-2">Account Type</div>
            <Select
              v-model:value="addForm.accountTypeName"
              class="w-full"
              :virtual="false"
              placeholder="Choose account type"
              :dropdown-match-select-width="true"
            >
              <Select.Option
                v-for="item in accountTypes"
                :key="item.id"
                :value="item.accountName"
              >
                <div class="flex items-center gap-2">
                  <IconifyIcon :icon="item.icon" class="h-5 w-5" />
                  <span>{{ item.accountName }}</span>
                </div>
              </Select.Option>
            </Select>
          </div>
          <div>
            <div class="mb-2">Account Balance</div>
            <Input
              v-model:value="addForm.amount"
              class="w-full"
              placeholder="Enter account balance"
              type="number"
              step="0.01"
            />
          </div>
        </div>
      </Modal>
    </div>
  </div>
</template>
