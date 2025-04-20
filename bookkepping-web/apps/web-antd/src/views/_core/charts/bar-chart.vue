<script lang="ts">
import type { ChartData } from 'chart.js';

import { defineComponent } from 'vue';
import { Bar } from 'vue-chartjs';

import {
  BarElement,
  CategoryScale,
  Chart as ChartJS,
  Legend,
  LinearScale,
  Title,
  Tooltip,
} from 'chart.js';

// 注册 Chart.js 必要组件
ChartJS.register(
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
);

export default defineComponent({
  name: 'BarChart',
  components: { Bar },
  props: {
    chartData: {
      type: Object as () => ChartData<'bar'>,
      required: true,
      default: () => ({
        labels: ['January', 'February', 'March'],
        datasets: [
          {
            label: 'Sales',
            data: [40, 20, 12],
            backgroundColor: '#36A2EB',
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
            position: 'top',
          },
          title: {
            display: true,
            text: 'Monthly Sales Data',
          },
        },
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      }),
    },
  },
});
</script>

<template>
  <div class="chart-container">
    <Bar :data="chartData" :options="chartOptions" />
  </div>
</template>

<style scoped>
.chart-container {
  position: relative;
  width: 100%;
  height: 300px;
}
</style>
