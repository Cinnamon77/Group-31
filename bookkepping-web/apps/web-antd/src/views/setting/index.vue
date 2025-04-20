<script setup lang="ts">
import { onMounted } from 'vue';

import { IconPicker } from '@vben/common-ui';
import { IconifyIcon } from '@vben/icons';

// import { createIconifyIcon, SvgAvatar1Icon } from '@vben/icons';
import {
  Button,
  Card,
  Form,
  Input,
  Modal,
  Popconfirm,
  Table,
} from 'ant-design-vue';

import {
  accountColumns,
  accountData,
  accountFormState,
  accountModalVisible,
  categoryColumns,
  categoryData,
  categoryModalVisible,
  closeModal,
  formRef,
  formState,
  handleDelete,
  handleSubmit,
  initData,
  modalTitle,
  showModal,
} from './index';

// 初始化获取分类列表数据
onMounted(() => {
  initData();
});
</script>

<template>
  <div class="p-5">
    <!-- 账号类型设置 -->
    <Card title="Account Types">
      <div class="mb-4">
        <Button type="primary" @click="showModal('account')">
          Add Account Type
        </Button>
      </div>
      <Table
        :columns="accountColumns"
        :data-source="accountData"
        :pagination="false"
      >
        <template #bodyCell="{ record, column }">
          <template v-if="column.key === 'icon'">
            <IconifyIcon :icon="record.icon" class="h-9 w-9" />
          </template>
          <template v-if="column.key === 'action'">
            <Button type="link" @click="showModal('account', record)">
              Edit
            </Button>
            <Popconfirm
              title="Are you sure to delete this account type?"
              @confirm="handleDelete('account', record.id)"
            >
              <Button type="link" danger>Delete</Button>
            </Popconfirm>
          </template>
        </template>
      </Table>
    </Card>

    <!-- 交易分类设置 -->
    <Card title="Transaction Categories" class="mb-5">
      <div class="mb-4">
        <Button type="primary" @click="showModal('category')">
          Add Category
        </Button>
      </div>
      <Table
        :columns="categoryColumns"
        :data-source="categoryData"
        :pagination="false"
      >
        <template #bodyCell="{ record, column }">
          <template v-if="column.key === 'icon'">
            <IconifyIcon :icon="record.icon" class="h-9 w-9" />
          </template>
          <template v-if="column.key === 'action'">
            <Button type="link" @click="showModal('category', record)">
              Edit
            </Button>
            <Popconfirm
              title="Are you sure to delete this category?"
              @confirm="handleDelete('category', record.id)"
            >
              <Button type="link" danger>Delete</Button>
            </Popconfirm>
          </template>
        </template>
      </Table>
    </Card>

    <!-- 账号类型编辑模态框 -->
    <Modal
      :visible="accountModalVisible"
      :title="modalTitle"
      @ok="handleSubmit('account')"
      @cancel="closeModal('account')"
    >
      <Form ref="formRef" :model="accountFormState" layout="vertical">
        <Form.Item
          label="Account Type"
          name="name"
          :rules="[{ required: true, message: 'Please input account type!' }]"
        >
          <Input v-model:value="accountFormState.name" />
        </Form.Item>
        <Form.Item
          label="Icon"
          name="icon"
          :rules="[{ required: true, message: 'Please input account icon!' }]"
        >
          <IconPicker
            v-model="accountFormState.icon"
            class="w-[200px]"
            prefix="svg"
          />
        </Form.Item>
      </Form>
    </Modal>

    <!-- 交易分类编辑模态框 -->
    <Modal
      :visible="categoryModalVisible"
      :title="modalTitle"
      @ok="handleSubmit('category')"
      @cancel="closeModal('category')"
    >
      <Form ref="formRef" :model="formState" layout="vertical">
        <Form.Item
          label="Category Name"
          name="name"
          :rules="[{ required: true, message: 'Please input category name!' }]"
        >
          <Input v-model:value="formState.name" />
        </Form.Item>
        <Form.Item
          label="Icon"
          name="icon"
          :rules="[{ required: true, message: 'Please input category icon!' }]"
        >
          <IconPicker v-model="formState.icon" class="w-[200px]" prefix="svg" />
        </Form.Item>
      </Form>
    </Modal>
  </div>
</template>
