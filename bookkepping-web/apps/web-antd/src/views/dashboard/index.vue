<script setup lang="ts">
import type { TransactionApi } from '#/api/core/transaction';

import { onMounted, reactive, ref } from 'vue';

import { IconifyIcon } from '@vben/icons';

import {
  Button,
  Card,
  DatePicker,
  Form,
  Input,
  InputNumber,
  message,
  Modal,
  Pagination,
  Select,
  Table,
  Tabs,
} from 'ant-design-vue';

import { addWalletAmountApi } from '#/api/core/wallet';
import BarChart from '#/views/_core/charts/bar-chart.vue';
import PieChart from '#/views/_core/charts/pie-chart.vue';

import {
  accounts,
  activeTab,
  addNoteFormRef,
  barChartData,
  categories,
  closeAddNoteModal,
  closeNoteModal,
  columns,
  currentPage,
  currentPeriod,
  currentYear,
  handleAddNote,
  handleDeleteNote,
  handleNoteSubmit,
  handlePageChange,
  initData,
  noteContent,
  noteFormRef,
  noteFormState,
  noteModalVisible,
  pageSize,
  periodOptions,
  pieChartData,
  showAddNoteModal,
  showNoteModal,
  tabContents,
  total,
  transactionData,
  yearOptions,
} from './index';

// 初始化获取列表数据
onMounted(async () => {
  await initData();
});

const showAddAssetModal = ref(false);
const addAssetForm = reactive({
  name: '',
  balance: '',
  type: 'CASH',
});

const handleAddAsset = async () => {
  try {
    // Convert balance to cents
    const balanceInCents = Math.round(
      Number.parseFloat(addAssetForm.balance) * 100,
    );

    await addWalletAmountApi({
      accountTypeName: addAssetForm.type,
      amount: balanceInCents,
    });

    message.success('Asset added successfully');
    showAddAssetModal.value = false;
    // Reset form
    addAssetForm.name = '';
    addAssetForm.balance = '';
    addAssetForm.type = 'CASH';
  } catch {
    message.error('Failed to add asset');
  }
};

const closeAddAssetModal = () => {
  showAddAssetModal.value = false;
  // Reset form
  addAssetForm.name = '';
  addAssetForm.balance = '';
  addAssetForm.type = 'CASH';
};
</script>

<template>
  <div class="pl-5 pr-5">
    <!-- 第一排 -->
    <div class="mt-5 flex flex-col lg:flex-row">
      <!-- 交易记录 卡片 -->
      <div class="mr-4 w-full lg:w-3/5">
        <Card class="mb-5 h-[450px]" title="Transaction Record">
          <div class="flex h-full flex-col">
            <Table
              :columns="columns"
              :data-source="transactionData"
              :pagination="false"
              size="middle"
              class="flex-1"
              :scroll="{ y: 260 }"
            >
              <template #bodyCell="{ column, record }">
                <template v-if="column.key === 'amount'">
                  <span>{{ (record.amount / 100).toFixed(2) }}</span>
                </template>
                <template v-if="column.key === 'account'">
                  <div class="flex items-center">
                    <IconifyIcon
                      :icon="record.accountIcon"
                      class="mr-2 h-5 w-5"
                    />
                    <span>{{ record.accountName }}</span>
                  </div>
                </template>
                <template v-if="column.key === 'category'">
                  <div class="flex items-center">
                    <IconifyIcon
                      :icon="record.categoryIcon"
                      class="mr-2 h-5 w-5"
                    />
                    <span>{{ record.categoryName }}</span>
                  </div>
                </template>
                <template v-if="column.key === 'action'">
                  <Button
                    type="link"
                    size="small"
                    @click="
                      showNoteModal(record as TransactionApi.TransactionResult)
                    "
                  >
                    Edit
                  </Button>
                </template>
              </template>
            </Table>
            <div class="mt-2 flex items-center justify-between">
              <Button type="primary" @click="showNoteModal()">Note</Button>
              <Pagination
                :total="total"
                v-model:current="currentPage"
                :page-size="pageSize"
                size="small"
                @change="handlePageChange"
              />
            </div>
          </div>
        </Card>
      </div>
      <!-- 支出统计 卡片 饼图 -->
      <div class="w-full lg:w-2/5">
        <Card class="mb-5 h-[450px]" title="Expenditure Statistics">
          <template #extra>
            <Select v-model:value="currentYear" style="width: 100px">
              <Select.Option
                v-for="year in yearOptions"
                :key="year"
                :value="year"
              >
                {{ year }}
              </Select.Option>
            </Select>
          </template>
          <div class="flex h-full flex-col">
            <PieChart :chart-data="pieChartData" />
          </div>
        </Card>
      </div>
    </div>

    <!-- 第二排 -->
    <div class="mt-5 flex flex-col lg:flex-row">
      <!-- 支出统计 卡片 柱状图 -->
      <div class="mr-4 w-full lg:w-3/5">
        <Card class="mb-5 h-[400px]" title="Expenditure Statistics">
          <template #extra>
            <Select v-model:value="currentPeriod" style="width: 100px">
              <Select.Option
                v-for="option in periodOptions"
                :key="option.value"
                :value="option.value"
              >
                {{ option.label }}
              </Select.Option>
            </Select>
          </template>
          <div class="flex h-full flex-col">
            <BarChart :chart-data="barChartData" />
          </div>
        </Card>
      </div>
      <!-- 节日/气候/笔记 -->
      <div class="w-full lg:w-2/5">
        <Card class="mb-5 h-[400px]">
          <div class="flex h-full flex-col">
            <Tabs v-model:active-key="activeTab">
              <template #rightExtra>
                <Button
                  v-if="activeTab === 'Notes'"
                  type="primary"
                  size="small"
                  @click="showAddNoteModal = true"
                >
                  Add
                </Button>
              </template>
              <Tabs.TabPane tab="Festival" key="Festival">
                <div class="h-[300px] overflow-y-auto">
                  <div
                    v-for="note in tabContents.Festival"
                    class="mt-2 rounded-md p-2"
                    :class="`${note.class} whitespace-pre-wrap`"
                    :key="note.content"
                  >
                    {{ note.content }}
                  </div>
                </div>
              </Tabs.TabPane>
              <Tabs.TabPane tab="Climate" key="Climate">
                <div class="h-[300px] overflow-y-auto">
                  <div
                    v-for="note in tabContents.Climate"
                    class="mt-2 rounded-md p-2"
                    :class="`${note.class} whitespace-pre-wrap`"
                    :key="note.content"
                  >
                    <!-- 里边的\n是换行符 -->
                    {{ note.content }}
                  </div>
                </div>
              </Tabs.TabPane>
              <Tabs.TabPane tab="Notes" key="Notes">
                <div class="h-[300px] overflow-y-auto">
                  <div
                    v-for="note in tabContents.Notes"
                    class="mt-2 flex items-center justify-between rounded-md p-2"
                    :class="note.class"
                    :key="note.content"
                  >
                    <span>{{ note.content }}</span>
                    <Button
                      type="text"
                      danger
                      size="small"
                      @click="note.id && handleDeleteNote(note.id)"
                    >
                      <IconifyIcon icon="ant-design:close-outlined" />
                    </Button>
                  </div>
                </div>
              </Tabs.TabPane>
            </Tabs>
          </div>
        </Card>
      </div>
    </div>

    <!-- Note Modal -->
    <Modal
      :open="noteModalVisible"
      title="Add Note"
      @ok="handleNoteSubmit"
      @cancel="closeNoteModal"
    >
      <Form ref="noteFormRef" :model="noteFormState" layout="vertical">
        <Form.Item
          label="Amount"
          name="amount"
          :rules="[
            { required: true, message: 'Please input amount!' },
            { pattern: /^-?\d*\.?\d*$/, message: 'Please input valid number!' },
          ]"
        >
          <Input
            v-model:value="noteFormState.amount"
            prefix="$"
            placeholder="Enter amount"
            @keypress="
              (e) => {
                const charCode = String.fromCharCode(e.keyCode);
                const pattern = /^[0-9.]$/;
                if (!pattern.test(charCode) && e.keyCode !== 45) {
                  e.preventDefault();
                }
                // 只允许一个小数点
                if (charCode === '.' && noteFormState.amount.includes('.')) {
                  e.preventDefault();
                }
                // 负号只能在开头输入
                if (charCode === '-' && noteFormState.amount.length > 0) {
                  e.preventDefault();
                }
              }
            "
          />
        </Form.Item>

        <Form.Item
          label="Category"
          name="category"
          :rules="[{ required: true, message: 'Please select category!' }]"
        >
          <Select
            v-model:value="noteFormState.category"
            placeholder="Select category"
            style="width: 100%"
            show-search
            :filter-option="
              (input, option) => {
                const label = option?.label || '';
                return label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
              }
            "
          >
            <Select.Option
              v-for="cat in categories"
              :key="cat.value"
              :value="cat.value"
              :label="cat.label"
            >
              <div class="flex items-center">
                <IconifyIcon :icon="cat.icon" class="mr-2 h-5 w-5" />
                <span>{{ cat.label }}</span>
              </div>
            </Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          label="Account"
          name="account"
          :rules="[{ required: true, message: 'Please select account!' }]"
        >
          <Select
            v-model:value="noteFormState.account"
            placeholder="Select account"
            style="width: 100%"
          >
            <Select.Option
              v-for="acc in accounts"
              :key="acc.value"
              :value="acc.value"
            >
              <div class="flex items-center">
                <IconifyIcon :icon="acc.icon" class="mr-2 h-5 w-5" />
                <span>{{ acc.label }}</span>
              </div>
            </Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          label="Date"
          name="date"
          :rules="[{ required: true, message: 'Please select date!' }]"
        >
          <DatePicker v-model:value="noteFormState.date" style="width: 100%" />
        </Form.Item>
      </Form>
    </Modal>

    <Button type="primary" @click="showAddAssetModal = true">
      Add Asset
    </Button>

    <Modal
      :open="showAddAssetModal"
      title="Add Asset"
      @ok="handleAddAsset"
      @cancel="closeAddAssetModal"
    >
      <Form :model="addAssetForm" layout="vertical">
        <Form.Item label="Name">
          <Input
            v-model:value="addAssetForm.name"
            placeholder="Enter asset name"
          />
        </Form.Item>

        <Form.Item label="Balance">
          <InputNumber
            v-model:value="addAssetForm.balance"
            :precision="2"
            style="width: 100%"
            placeholder="Enter balance"
          />
        </Form.Item>

        <Form.Item label="Type">
          <Select v-model:value="addAssetForm.type">
            <Select.Option value="CASH">Cash</Select.Option>
            <Select.Option value="BANK">Bank Account</Select.Option>
            <Select.Option value="CREDIT_CARD">Credit Card</Select.Option>
          </Select>
        </Form.Item>
      </Form>
    </Modal>

    <!-- Add Note Modal -->
    <Modal
      :open="showAddNoteModal"
      title="Add Note"
      @ok="handleAddNote"
      @cancel="closeAddNoteModal"
    >
      <Form
        ref="addNoteFormRef"
        :model="{ content: noteContent }"
        layout="vertical"
      >
        <Form.Item
          name="content"
          label="Content"
          :rules="[{ required: true, message: 'Please input note content!' }]"
        >
          <Input.TextArea
            v-model:value="noteContent"
            :rows="4"
            placeholder="Enter note content"
          />
        </Form.Item>
      </Form>
    </Modal>
  </div>
</template>
