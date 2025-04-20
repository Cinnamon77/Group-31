import type { TableColumnsType } from 'ant-design-vue';

import type { TransactionApi } from '#/api/core/transaction';

import { ref, watch } from 'vue';

import { message } from 'ant-design-vue';
import dayjs from 'dayjs';

import { getAccountTypeListApi } from '#/api/core/account-type';
import { addNoteApi, deleteNoteApi, getNoteListApi } from '#/api/core/note';
import {
  getCycleBarApi,
  getExpenditureRateApi,
  getFestivalApi,
  getWeatherApi,
} from '#/api/core/statistics';
import {
  addTransactionApi,
  getTransactionListApi,
  updateTransactionApi,
} from '#/api/core/transaction';
import { getTransactionCategoryListApi } from '#/api/core/transaction-category';

export namespace DashboardType {
  // 交易分类
  export interface TransactionCategory {
    value: number;
    label: string;
    icon: string;
  }
  // 账号分类
  export interface AccountCategory {
    value: number;
    label: string;
    icon: string;
  }
}

// 交易记录数据
export const columns: TableColumnsType = [
  {
    title: 'Amount',
    dataIndex: 'amount',
    key: 'amount',
  },
  {
    title: 'Date',
    dataIndex: 'transactionDate',
    key: 'transactionDate',
  },
  {
    title: 'Account',
    dataIndex: 'account',
    key: 'account',
  },
  {
    title: 'Type',
    dataIndex: 'category',
    key: 'category',
  },
  {
    title: 'Action',
    key: 'action',
    width: 100,
  },
];

export const transactionData = ref<TransactionApi.TransactionResult[]>([]);

// 生成随机颜色
const CHART_COLORS = [
  '#41B883', // vue green
  '#E46651', // ember red
  '#00D8FF', // react blue
  '#DD1B16', // angular red
  '#F7DF1E', // javascript yellow
  '#61DAFB', // light blue
  '#FF4785', // pink
  '#7957D5', // purple
  '#34495E', // dark blue
  '#41B883', // green
];

const generateColors = (count: number): string[] => {
  return Array.from(
    { length: count },
    (_, i) => CHART_COLORS[i % CHART_COLORS.length] || CHART_COLORS[0],
  ) as string[];
};

// 支出统计数据
export const pieChartData = ref<{
  datasets: {
    backgroundColor: string[];
    data: number[];
    label: string;
  }[];
  labels: string[];
}>({
  labels: ['VueJs', 'EmberJs', 'ReactJs', 'AngularJs'],
  datasets: [
    {
      backgroundColor: generateColors(4),
      data: [40, 20, 80, 10],
      label: 'My First dataset',
    },
  ],
});

export const barChartData = ref<{
  datasets: {
    backgroundColor: string[];
    borderColor: string[];
    borderWidth: number;
    data: number[];
    label: string;
  }[];
  labels: string[];
}>({
  labels: [],
  datasets: [],
});

// Tab 相关数据
export const tabs = ['Festival', 'Climate', 'Notes'] as const;
export type TabType = (typeof tabs)[number];
export const activeTab = ref<TabType>('Festival');

interface TabItem {
  id?: number;
  content: string;
  class: string;
}

type TabContentRecord = {
  [K in TabType]: TabItem[];
};

export const tabContents = ref<TabContentRecord>({
  Festival: [],
  Climate: [],
  Notes: [],
});

export const notes = ref<TabItem[]>(tabContents.value[activeTab.value]);

// 生成年份选项
const generateYearOptions = () => {
  const currentYear = new Date().getFullYear();
  return Array.from({ length: 10 }, (_, i) => (currentYear - i).toString());
};

// 当前年份默认为今年
export const currentYear = ref(new Date().getFullYear().toString());
export const yearOptions = ref(generateYearOptions());

// 周期选项
export const periodOptions = [
  { label: 'Week', value: 'WEEK' },
  { label: 'Month', value: 'MONTH' },
  { label: 'Year', value: 'YEAR' },
] as const;

export const currentPeriod = ref<'MONTH' | 'WEEK' | 'YEAR'>('WEEK');

// 监听周期变化
watch(currentPeriod, () => {
  getCycleBar();
});

// Note Modal
export const noteModalVisible = ref(false);
export const noteFormRef = ref();
export const noteFormState = ref<{
  account: number | string;
  amount: string;
  category: number | string;
  date: dayjs.Dayjs;
  id: string;
}>({
  id: '',
  amount: '',
  category: '',
  account: '',
  date: dayjs(),
});
export const isEdit = ref(false);

export const categories = ref<DashboardType.TransactionCategory[]>([]);

export const accounts = ref<DashboardType.AccountCategory[]>([]);

export const showNoteModal = (record?: TransactionApi.TransactionResult) => {
  if (record) {
    isEdit.value = true;
    const categoryId = categories.value.find(
      (cat) => cat.label === record.categoryName,
    )?.value;
    const accountId = accounts.value.find(
      (acc) => acc.label === record.accountName,
    )?.value;

    noteFormState.value = {
      id: record.id.toString(),
      amount: (record.amount / 100).toString(),
      category: categoryId || '',
      account: accountId || '',
      date: dayjs(record.transactionDate),
    };
  } else {
    isEdit.value = false;
    noteFormState.value = {
      id: '',
      amount: '',
      category: '',
      account: '',
      date: dayjs(),
    };
  }
  noteModalVisible.value = true;
};

export const handleNoteSubmit = () => {
  noteFormRef.value?.validate().then(() => {
    // 处理表单数据
    const formData = {
      ...noteFormState.value,
      amount: Number.parseFloat(noteFormState.value.amount),
      category:
        typeof noteFormState.value.category === 'string'
          ? Number.parseInt(noteFormState.value.category)
          : noteFormState.value.category,
      account:
        typeof noteFormState.value.account === 'string'
          ? Number.parseInt(noteFormState.value.account)
          : noteFormState.value.account,
    };

    // 由于服务端金额是分，所以需要乘以100
    formData.amount = formData.amount * 100;

    // 根据是否编辑模式调用不同的API
    const apiCall = isEdit.value
      ? updateTransactionApi(Number.parseInt(noteFormState.value.id), {
          amount: formData.amount,
          categoryId: formData.category,
          accountTypeId: formData.account,
          date: formData.date.format('YYYY-MM-DD'),
        })
      : addTransactionApi({
          amount: formData.amount,
          categoryId: formData.category,
          accountTypeId: formData.account,
          date: formData.date.format('YYYY-MM-DD'),
        });

    apiCall.then(() => {
      // message消息
      message.success('Transaction saved successfully');
      // 关闭弹窗并重置表单
      noteModalVisible.value = false;
      noteFormRef.value?.resetFields();
      // 刷新交易记录列表
      getTransactionList();
    });
  });
};

export const closeNoteModal = () => {
  noteModalVisible.value = false;
  noteFormRef.value?.resetFields();
};

// 分页配置
export const currentPage = ref(1);
export const pageSize = ref(5);
export const total = ref(0);

const getTransactionList = () => {
  getTransactionListApi({
    page: currentPage.value,
    pageSize: pageSize.value,
  }).then((res) => {
    transactionData.value = res.records;
    total.value = res.total;
  });
};

export const handlePageChange = (page: number) => {
  currentPage.value = page;
  getTransactionList();
};

const getExpenditureRate = () => {
  getExpenditureRateApi({
    year: currentYear.value,
  }).then((res: { amountTotal: number; categoryName?: string }[]) => {
    // 金额转换为元
    let categoryData = res.map((item) => item.amountTotal / 100);
    // 金额为0的数据排除掉
    categoryData = categoryData.filter((item) => item !== 0);

    if (categoryData.length === 0) {
      // 空数据状态
      pieChartData.value = {
        labels: ['No Data'],
        datasets: [
          {
            backgroundColor: ['#E5E7EBE6'],
            data: [1],
            label: 'Category Statistics',
          },
        ],
      };
      return;
    }

    // 更新饼图数据
    const categoryLabels = res.map((item) => item.categoryName || 'Unknown');
    pieChartData.value = {
      labels: categoryLabels,
      datasets: [
        {
          backgroundColor: generateColors(categoryLabels.length).map(
            (color) => `${color}E6`,
          ),
          data: categoryData,
          label: 'Category Statistics',
        },
      ],
    };
  });
};

const getCycleBar = () => {
  getCycleBarApi({
    cycle: currentPeriod.value as 'MONTH' | 'WEEK' | 'YEAR',
  }).then((res) => {
    barChartData.value = {
      labels: res.map((item) => item.date),
      datasets: [
        {
          // 金额由分转换为元
          data: res.map((item) => item.amount / 100),
          backgroundColor: generateColors(res.length),
          borderColor: generateColors(res.length),
          borderWidth: 1,
          label: 'Expenditure',
        },
      ],
    };
  });
};

// 笔记随机背景颜色
const getRandomBgClass = (): string => {
  const bgClasses: string[] = [
    'bg-blue-50',
    'bg-green-50',
    'bg-purple-50',
    'bg-yellow-50',
    'bg-red-50',
    'bg-gray-50',
  ];
  const randomIndex = Math.floor(Math.random() * bgClasses.length);
  return bgClasses[randomIndex] || 'bg-gray-50'; // Add fallback value
};

// 获取笔记列表
const getNoteList = async () => {
  const res = await getNoteListApi();
  tabContents.value.Notes = res.map((note) => ({
    id: note.id,
    content: note.content,
    class: getRandomBgClass(),
  }));
};

export const handleDeleteNote = async (id: number) => {
  try {
    await deleteNoteApi(id);
    message.success('Note deleted successfully');
    await getNoteList();
  } catch {
    message.error('Failed to delete note');
  }
};

const getWeather = () => {
  getWeatherApi().then((res) => {
    tabContents.value.Climate = [
      {
        content: res,
        class: 'bg-blue-50',
      },
    ];
  });
};

const getFestival = () => {
  getFestivalApi().then((res) => {
    tabContents.value.Festival = [
      {
        content: res,
        class: 'bg-red-50',
      },
    ];
  });
};
export const initData = async () => {
  await getTransactionCategoryListApi().then((res) => {
    categories.value = res.map((item) => ({
      value: item.id,
      label: item.categoryName,
      icon: item.icon,
    }));
  });

  await getAccountTypeListApi().then((res) => {
    accounts.value = res.map((item) => ({
      value: item.id,
      label: item.accountName,
      icon: item.icon,
    }));
  });

  getTransactionList();
  getExpenditureRate();
  getCycleBar();
  getNoteList();
  getWeather();
  getFestival();
};

// 监听年份变化
watch(currentYear, () => {
  getExpenditureRate();
});

export const showAddNoteModal = ref(false);
export const noteContent = ref('');
export const addNoteFormRef = ref();

export const handleAddNote = async () => {
  try {
    await addNoteFormRef.value?.validate();
    await addNoteApi({
      content: noteContent.value,
      class: getRandomBgClass(),
    });
    message.success('Note added successfully');
    showAddNoteModal.value = false;
    noteContent.value = '';
    addNoteFormRef.value?.resetFields();
    // 只刷新note数据
    await getNoteList();
  } catch (error) {
    if (error instanceof Error) {
      message.error(error.message);
    }
  }
};

export const closeAddNoteModal = () => {
  showAddNoteModal.value = false;
  noteContent.value = '';
  addNoteFormRef.value?.resetFields();
};
