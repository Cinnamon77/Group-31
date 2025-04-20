<script lang="ts">
import type { ChartData } from 'chart.js';

import { defineComponent } from 'vue';
import { Pie } from 'vue-chartjs';

import {
  ArcElement,
  CategoryScale,
  Chart as ChartJS,
  Legend,
  Title,
  Tooltip,
} from 'chart.js';

// 注册 Pie 图表所需的组件
ChartJS.register(
  Title,
  Tooltip,
  Legend,
  ArcElement, // 饼图的核心元素
  CategoryScale,
);

export default defineComponent({
  name: 'PieChart',
  components: { Pie },
  props: {
    chartData: {
      type: Object as () => ChartData<'pie'>,
      required: true,
      default: () => ({
        labels: ['Red', 'Blue', 'Yellow'],
        datasets: [
          {
            data: [30, 50, 20, 30],
            backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56'],
          },
        ],
      }),
    },
    chartOptions: {
      type: Object,
      default: () => ({
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'top', // 图例位置
          },
          tooltip: {
            callbacks: {
              label: (context: any) => {
                const label = context.label || '';
                const value = context.raw || 0;
                const total = context.dataset.data.reduce(
                  (a: number, b: number) => a + b,
                  0,
                );
                const percentage = Math.round((value / total) * 100);
                return `${label}: ${value} (${percentage}%)`;
              },
            },
          },
        },
      }),
    },
  },
});
</script>

<template>
  <div class="chart-container">
    <Pie :data="chartData" :options="chartOptions" />
  </div>
</template>

<style scoped>
.chart-container {
  position: relative;
  width: 100%;
  height: 300px;
}
</style>
