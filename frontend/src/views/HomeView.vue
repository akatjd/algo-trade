<template>
  <div class="container mt-5 text-center">
    <vue-good-table
      :columns="columns"
      :rows="rows"/>
  </div>
</template>

<script>
import 'vue-good-table-next/dist/vue-good-table-next.css'
import { VueGoodTable } from 'vue-good-table-next'

export default {
  name: 'HomeView',
  components: {
    VueGoodTable
  },
  data () {
    return {
      columns: [
        {
          label: '이름',
          field: 'name'
        },
        {
          label: '가격',
          field: 'age',
          type: 'number'
        },
        {
          label: '김프',
          field: 'score',
          type: 'percentage'
        },
        {
          label: '거래액(일)',
          field: 'amount',
          type: 'number'
        }
      ],
      rows: [
        { id: 1, name: '비트코인', age: 22812000, amount: 3761, score: +0.03343 },
        { id: 2, name: '이더리움', age: 1709000, amount: 4522, score: +0.03343 },
        { id: 3, name: '리플', age: 530, amount: 541, score: -0.03343 },
        { id: 4, name: '앱토스', age: 6775, amount: 133, score: -0.03343 },
        { id: 5, name: '도지코인', age: 133, amount: 555, score: -0.03343 },
        { id: 6, name: '위믹스', age: 1145, amount: 333, score: -0.03343 }
      ]
    }
  }
}
</script>
