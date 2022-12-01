<template>
  <main class="form-trade-main w-100 m-auto">
    <div>
      <span>거래소</span>
      <select class="form-select mb-3 mt-1" aria-label="Default select example" v-model="cryptoExchangeInfo">
        <option
          v-for="(item, index) in cryptoExchangeInfoList"
          :key="index"
          :value="item.value"
        >{{ item.name }}</option>
      </select>
    </div>
    <div>
      <span>코인</span>
      <select class="form-select mb-3 mt-1" aria-label="Default select example" v-model="tickerInfo">
        <option
          v-for="(item, index) in tickerInfoList"
          :key="index"
          :value="item.value"
        >{{ item.name }}</option>
      </select>
    </div>
    <div>
      <span>매수 RSI</span>
      <input v-model="buyRsi" type="number" class="form-control mb-3 mt-1" min="1" max="100">
    </div>
    <div>
      <span>매도 RSI</span>
      <input v-model="sellRsi" type="number" class="form-control mb-3 mt-1" min="1" max="100">
    </div>
    <div v-if="this.$store.state.tradeStatus === 'y'" class="float-end">
      <button type="button" class="btn btn-danger" @click="startTrade">{{ tradeStop }}</button>
    </div>
    <div v-else class="float-end">
      <button type="button" class="btn btn-dark" @click="startTrade">{{ tradeStart }}</button>
    </div>
  </main>
</template>

<script>
import axios from 'axios'

export default {
  name: 'TradeMainView',
  created () {
    console.log(this.$store.state.tradeStatus)
    try {
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
      }],
      buyRsi: 0,
      sellRsi: 0,
      tradeStart: 'start',
      tradeStop: 'stop'
    }
  },
  methods: {
    startTrade () {
      const tradeInfo = {
        buyRsi: this.buyRsi,
        sellRsi: this.sellRsi,
        selExchange: this.cryptoExchangeInfo,
        selTicker: this.tickerInfo
      }
      this.startTradePostApi(tradeInfo)
    },
    startTradePostApi (body) {
      const headers = {
        headers: {
          Authorization: this.$store.state.accessToken,
          Refresh: this.$store.state.refreshToken
        }
      }
      axios.post('/api/trade/startTrade', body, headers)
        .then(response => {
          console.log(response)
        })
    }
  }
}
</script>

<style scoped>
  @import "../assets/css/trade-main.css";
</style>
