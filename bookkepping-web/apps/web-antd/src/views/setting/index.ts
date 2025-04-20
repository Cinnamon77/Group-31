import type { AccountTypeApi } from '#/api/core/account-type';
import type { TransactionCategoryApi } from '#/api/core/transaction-category';

import { ref } from 'vue';

import { message } from 'ant-design-vue';

import {
  addAccountTypeApi,
  deleteAccountTypeApi,
  editAccountTypeApi,
  getAccountTypeListApi,
} from '#/api/core/account-type';
import {
  addTransactionCategoryApi,
  deleteTransactionCategoryApi,
  editTransactionCategoryApi,
  getTransactionCategoryListApi,
} from '#/api/core/transaction-category';

// 交易分类数据
export const categoryData = ref<
  TransactionCategoryApi.TransactionCategoryResult[]
>([]);

export const categoryColumns = [
  {
    title: 'Category Name',
    dataIndex: 'categoryName',
    key: 'categoryName',
  },
  {
    title: 'Icon',
    dataIndex: 'icon',
    key: 'icon',
    width: 100,
  },
  {
    title: 'Action',
    key: 'action',
    width: 200,
  },
];

// 账号类型数据
export const accountData = ref<AccountTypeApi.AccountTypeResult[]>([]);

export const accountColumns = [
  {
    title: 'Account Type',
    dataIndex: 'accountName',
    key: 'accountName',
  },
  {
    title: 'Icon',
    dataIndex: 'icon',
    key: 'icon',
    width: 100,
  },
  {
    title: 'Action',
    key: 'action',
    width: 200,
  },
];

// 模态框状态
export const categoryModalVisible = ref(false);
export const accountModalVisible = ref(false);
export const modalTitle = ref('');
export const currentEditItem = ref<any>(null);
export const formRef = ref();

// 交易分类表单数据
export const formState = ref<TransactionCategoryApi.TransactionCategoryParams>({
  name: '',
  icon: '',
});

// 账号类型表单数据
export const accountFormState = ref<AccountTypeApi.AccountTypeParams>({
  name: '',
  icon: '',
});

// 打开新增/编辑模态框
export const showModal = (type: 'account' | 'category', item?: any) => {
  if (item) {
    modalTitle.value = `Edit ${type}`;
    currentEditItem.value = { ...item };
    if (type === 'category') {
      formState.value = {
        name: item.categoryName || item.name,
        icon: item.icon,
      };
    } else {
      accountFormState.value = {
        name: item.accountName || item.name,
        icon: item.icon,
      };
    }
  } else {
    modalTitle.value = `Add ${type}`;
    currentEditItem.value = null;
    formState.value = { name: '', icon: '' };
    accountFormState.value = { name: '', icon: '' };
  }
  if (type === 'category') {
    categoryModalVisible.value = true;
  } else {
    accountModalVisible.value = true;
  }
};

// 处理表单提交
export const handleSubmit = (type: 'account' | 'category') => {
  formRef.value?.validate().then(() => {
    if (type === 'category') {
      if (currentEditItem.value) {
        editTransactionCategoryApi(
          currentEditItem.value.id,
          formState.value,
        ).then(() => {
          message.success(`${modalTitle.value} successfully`);
          closeModal(type);
          refreshCategoryData();
        });
      } else {
        addTransactionCategoryApi(formState.value).then(() => {
          message.success(`${modalTitle.value} successfully`);
          closeModal(type);
          refreshCategoryData();
        });
      }
    } else {
      if (currentEditItem.value) {
        editAccountTypeApi(
          currentEditItem.value.id,
          accountFormState.value,
        ).then(() => {
          message.success(`${modalTitle.value} successfully`);
          closeModal(type);
          refreshAccountTypeData();
        });
      } else {
        addAccountTypeApi(accountFormState.value).then(() => {
          message.success(`${modalTitle.value} successfully`);
          closeModal(type);
          refreshAccountTypeData();
        });
      }
    }
  });
};

// 关闭模态框
export const closeModal = (type: 'account' | 'category') => {
  if (type === 'category') {
    categoryModalVisible.value = false;
  } else {
    accountModalVisible.value = false;
  }
  formRef.value?.resetFields();
};

// 删除项
export const handleDelete = (type: 'account' | 'category', id: number) => {
  if (type === 'category') {
    deleteTransactionCategoryApi(id).then(() => {
      message.success('Deleted successfully');
      refreshCategoryData();
    });
  } else {
    deleteAccountTypeApi(id).then(() => {
      message.success('Deleted successfully');
      refreshAccountTypeData();
    });
  }
};

const refreshCategoryData = () => {
  getTransactionCategoryListApi().then((res) => {
    categoryData.value = res.map((item) => ({
      id: item.id,
      categoryName: item.categoryName,
      icon: item.icon,
      createdAt: item.createdAt,
      updatedAt: item.updatedAt,
    }));
  });
};

const refreshAccountTypeData = () => {
  getAccountTypeListApi().then((res) => {
    accountData.value = res.map((item) => ({
      id: item.id,
      accountName: item.accountName,
      icon: item.icon,
      createdAt: item.createdAt,
      updatedAt: item.updatedAt,
    }));
  });
};
// 初始化获取列表数据
export const initData = () => {
  refreshCategoryData();
  refreshAccountTypeData();
};
