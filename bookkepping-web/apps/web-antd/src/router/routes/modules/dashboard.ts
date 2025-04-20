import type { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    meta: {
      icon: 'lucide:area-chart',
      order: -1,
      title: 'Dashboard',
    },
    name: 'Dashboard',
    path: '/dashboard',
    component: () => import('#/views/dashboard/index.vue'),
    // children: [
    //   {
    //     name: 'Analytics',
    //     path: '/analytics',
    //     component: () => import('#/views/dashboard/analytics/index.vue'),
    //     meta: {
    //       affixTab: true,
    //       icon: 'lucide:area-chart',
    //       title: $t('page.dashboard.analytics'),
    //     },
    //   },
    //   {
    //     name: 'Workspace',
    //     path: '/workspace',
    //     component: () => import('#/views/dashboard/workspace/index.vue'),
    //     meta: {
    //       icon: 'carbon:workspace',
    //       title: $t('page.dashboard.workspace'),
    //     },
    //   },
    // ],
  },
];

export default routes;
