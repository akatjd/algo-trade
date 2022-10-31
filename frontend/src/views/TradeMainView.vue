<template>
  <main class="form-signin w-100 m-auto">
    <div>
      <span>거래소 목록</span>
      <select class="form-select mb-3 mt-1" aria-label="Default select example" v-model="cryptoExchangeInfo">
        <option
          v-for="(item, index) in cryptoExchangeInfoList"
          :key="index"
          :value="item.value"
        >{{ item.name }}</option>
      </select>
    </div>
    <div>
      <span>코인 목록</span>
      <select class="form-select mb-3 mt-1" aria-label="Default select example" v-model="tickerInfo">
        <option
          v-for="(item, index) in tickerInfoList"
          :key="index"
          :value="item.value"
        >{{ item.name }}</option>
      </select>
    </div>
    <div>
      <span>RSI(매수)</span>
      <input type="number" min="1" max="99" class="form-control mb-3 mt-1" v-model="buyRsi" id="buyRsi" @input="chkRsi('buyRsi')" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
    </div>
    <div>
      <span>RSI(매도)</span>
      <input type="number" min="1" max="99" class="form-control mb-3 mt-1" v-model="sellRsi" id="sellRsi" @input="chkRsi('sellRsi')" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
    </div>
    <div class="float-end">
      <button type="button" class="btn btn-dark" @click="startTrade()">Start</button>
    </div>
  </main>
</template>

<script>
import axios from 'axios'

export default {
  name: 'TradeMainView',
  methods: {
    chkRsi (selRsi) {
      if (selRsi === 'buyRsi') {
        if (isNaN(this.sellRsi)) {
          alert('숫자를 입력해주세요 1~99')
          this.buyRsi = ''
        } else if ((this.buyRsi < 1 || this.buyRsi > 99) && !(this.buyRsi === '')) {
          alert('정상 범위를 입력해주세요 1~99')
          this.buyRsi = ''
        }
      } else {
        if (isNaN(this.sellRsi)) {
          alert('숫자를 입력해주세요. 1~99')
          this.sellRsi = ''
        } else if ((this.sellRsi < 1 || this.sellRsi > 99) && !(this.sellRsi === '')) {
          alert('정상 범위를 입력해주세요. 1~99')
          this.sellRsi = ''
        }
      }
    },
    startTrade () {
      const accountId = this.$store.getters.getUserName
      const buyRsi = this.buyRsi
      const sellRsi = this.sellRsi
      const cryptoExchangeInfo = this.cryptoExchangeInfo
      const tickerInfo = this.tickerInfo
      if (!cryptoExchangeInfo) {
        alert('거래소를 선택해주세요.')
      } else if (!tickerInfo) {
        alert('코인을 선택해주세요.')
      } else if (!buyRsi) {
        alert('매수 Rsi를 입력해주세요. 1~99')
      } else if (!sellRsi) {
        alert('매도 Rsi를 입력해주세요. 1~99')
      } else if (sellRsi <= buyRsi) {
        alert('매도 Rsi 값은 매수 Rsi 값보다 커야 합니다.')
      } else {
        const body = {
          accountId: accountId,
          buyRsi: buyRsi,
          sellRsi: sellRsi,
          cryptoExchangeInfoSeq: cryptoExchangeInfo,
          tickerInfoSeq: tickerInfo
        }
        console.log(body)
        console.log(this.$store.state.accessToken)
        axios.post('/api/trade/startTrade', body, {
          headers: {
            Authorization: this.$store.state.accessToken,
            Refresh: this.$store.state.refreshToken
          }
        })
      }
    }
  },
  created () {
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
      buyRsi: '',
      sellRsi: ''
    }
  }
}
</script>

<style scoped>

</style>
