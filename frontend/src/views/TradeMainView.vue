<template>
  <main class="form-signin w-100 m-auto">
    <div>
      <span>선택해주세요(거)</span>
      <select class="form-select mb-3 mt-1" aria-label="Default select example">
        <option selected>Open this select menu</option>
        <option value="1">One</option>
        <option value="2">Two</option>
        <option value="3">Three</option>
      </select>
    </div>
    <div>
      <span>선택해주세요(코)</span>
      <select class="form-select mb-3 mt-1" aria-label="Default select example">
        <option selected>Open this select menu</option>
        <option value="1">One</option>
        <option value="2">Two</option>
        <option value="3">Three</option>
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
  mounted () {
    try {
      axios.get('/api/trade/main',
        {
          headers: {
            Authorization: this.$store.state.token
          }
        })
        .then(function (response) {
          console.log(response)
        })
    } catch (err) {
      throw new Error(err)
    }
  }
}
</script>

<style scoped>

</style>
