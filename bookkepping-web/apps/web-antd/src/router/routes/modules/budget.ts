import type { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    name: 'Budget',
    path: '/budget',
    component: () => import('#/views/budget/index.vue'),
    meta: {
      title: 'Budget',
      icon: 'solar:wallet-money-line-duotone',
    },
  },
];

export default routes;
