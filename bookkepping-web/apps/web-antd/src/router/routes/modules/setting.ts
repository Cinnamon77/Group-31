import type { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    name: 'Setting',
    path: '/setting',
    component: () => import('#/views/setting/index.vue'),
    meta: {
      title: 'Setting',
      icon: 'lets-icons:setting-line',
    },
  },
];

export default routes;
