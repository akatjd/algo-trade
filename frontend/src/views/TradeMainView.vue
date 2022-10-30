<template>
  <main class="form-signin w-100 m-auto">
    <div>
      <span>선택해주세요(거)</span>
      <select class="form-select mb-3 mt-1" aria-label="Default select example" v-model="cryptoExchangeInfo">
        <option
          v-for="(item, index) in cryptoExchangeInfoList"
          :key="index"
          :value="item.value"
        >{{ item.name }}</option>
      </select>
    </div>
    <div>
      <span>선택해주세요(코)</span>
      <select class="form-select mb-3 mt-1" aria-label="Default select example" v-model="tickerInfo">
        <option
          v-for="(item, index) in tickerInfoList"
          :key="index"
          :value="item.value"
        >{{ item.name }}</option>
      </select>
    </div>
    <div>
      <span>선택해주세요(R수)</span>
      <select class="form-select mb-3 mt-1" aria-label="Default select example">
        <option selected>Open this select menu</option>
        <option value="1">One</option>
        <option value="2">Two</option>
        <option value="3">Three</option>
      </select>
    </div>
    <div>
      <span>선택해주세요(R도)</span>
      <select class="form-select mb-3 mt-1" aria-label="Default select example">
        <option selected>Open this select menu</option>
        <option value="1">One</option>
        <option value="2">Two</option>
        <option value="3">Three</option>
      </select>
    </div>
    <div class="float-end">
      <button type="button" class="btn btn-dark">Start</button>
    </div>
  </main>
</template>

<script>
import axios from 'axios'

export default {
  name: 'TradeMainView',
  created () {
    try {
      console.log('start')
      axios.get('/api/trade/main',
        {
          headers: {
            Authorization: this.$store.state.accessToken,
            Refresh: this.$store.state.refreshToken
          }
        })
        .then(response => {
          const cryptoExchangeInfoDtos = response.data.cryptoExchangeInfoDtos
          const tickerListDtos = response.data.tickerListDtos
          cryptoExchangeInfoDtos.forEach((value, index, array) => {
            this.cryptoExchangeInfoList.push({
              name: value.cryptoExchangeName,
              value: value.cryptoExchangeInfoSeq
            })
          })
          tickerListDtos.forEach((value, index, array) => {
            this.tickerInfoList.push({
              name: value.tickerName,
              value: value.tickerSeq
            })
          })
        })
        .catch(error => {
          if (error.response.status === 401) {
            console.log(error.response.status)
            this.$store.dispatch('logout')
            this.$router.push({ name: 'LoginView' })
          }
        })
    } catch (err) {
      throw new Error(err)
    }
  },
  data () {
    return {
      cryptoExchangeInfo: '',
      cryptoExchangeInfoList: [{
        name: '선택해주세요',
        value: ''
      }],
      tickerInfo: '',
      tickerInfoList: [{
        name: '선택해주세요',
        value: ''
      }]
    }
  }
}
</script>

<style scoped>

</style>
