<template>
  <main class="form-signin w-100 m-auto">
    <form @submit.prevent>
      <h1 class="h3 mb-3 fw-normal">회원가입</h1>

      <div class="form-floating">
        <input v-model="form.accountId" class="form-control" placeholder="Account Id">
        <label>First Name</label>
      </div>

      <div class="form-floating">
        <input v-model="form.email" type="email" class="form-control" placeholder="name@example.com">
        <label>Email address</label>
      </div>

      <div class="form-floating">
        <input v-model="form.password" type="password" class="form-control" placeholder="Password">
        <label>Password</label>
      </div>

      <div class="form-floating">
        <input v-model="form.passwordConfirm" type="password" class="form-control" placeholder="Password Confirm">
        <label>Password Confirm</label>
      </div>

      <button @click="submit" class="w-100 btn btn-lg btn-primary" type="submit">Submit</button>
    </form>
  </main>
</template>

<script>
// import { reactive } from 'vue'
import axios from 'axios'

export default {
  name: 'RegisterView',
  data () {
    return {
      form: {
        accountId: '',
        email: '',
        password: '',
        passwordConfirm: ''
      }
    }
  },
  methods: {
    async submit () {
      try {
        // await axios.get('/api/hello')
        await axios.post('/api/register', this.form)
        // await this.$router.push({ name: 'LoginView' })
      } catch (err) {
        throw new Error(err)
      }
    }
  }
  // setup () {
  //   const data = reactive({
  //     accountId: '',
  //     email: '',
  //     password: '',
  //     passwordConfirm: ''
  //   })
  //   const submit = async () => {
  //     try {
  //       await axios.post('/api/register', data)
  //       // await this.$router.push({ name: 'LoginView' })
  //     } catch (err) {
  //       throw new Error(err)
  //     }
  //   }
  //
  //   return {
  //     data,
  //     submit
  //   }
  // }
}
</script>

<style scoped>

</style>
