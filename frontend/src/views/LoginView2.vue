<template>
  <main class="form-signin w-100 m-auto">
    <form @submit.prevent="submit">
      <h1 class="h3 mb-3 fw-normal">Please sign in</h1>

      <div class="form-floating">
        <input v-model="data.accountId" type="text" class="form-control" id="accountId" placeholder="name@example.com">
        <label for="floatingInput">Email address</label>
      </div>

      <div class="form-floating">
        <input v-model="data.password" type="password" class="form-control" id="password" placeholder="Password">
        <label for="floatingPassword">Password</label>
      </div>

      <button class="w-100 btn btn-lg btn-primary" type="submit">Sign in</button>
    </form>
  </main>
</template>

<script>
import { reactive } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

export default {
  name: 'LoginView2',
  setup () {
    const data = reactive({
      accountId: '',
      password: ''
    })
    const router = useRouter()

    // const axiosConfig = {
    //   headers: {
    //     'Content-type': 'application/x-www-form-urlencoded'
    //   }
    // }

    const submit = async () => {
      console.log(data)
      // const response = await axios.post('http://localhost:80/account/login', data)
      await axios.post('/api/login', data)
      // axios.defaults.headers.common.Authorization = `Bearer ${response.data.token}`

      await router.push('/')
    }

    return {
      data,
      submit
    }
  }
}
</script>

<style scoped>

</style>
