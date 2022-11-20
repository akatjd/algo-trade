<template>
  <main class="form-sign w-100 m-auto">
    <div v-if="loginError">
      <h5>
        <span class="badge text-bg-danger">
          아이디 비밀번호를 확인해주세요.
        </span>
      </h5>
      <form @submit.prevent="login()">
        <div class="form-floating">
          <input v-model="user" type="text" class="form-control mb-3" placeholder="ID">
          <label for="floatingInput">ID</label>
        </div>
        <div class="form-floating">
          <input v-model="password" type="password" class="form-control mb-3" id="password" placeholder="Password">
          <label for="floatingPassword">Password</label>
        </div>
        <button class="w-100 btn btn-lg btn-primary" variant="success" type="submit">Sign in</button>
      </form>
    </div>
    <div v-else>
      <h1 class="h3 mb-3 fw-normal">
        <span class="badge text-bg-primary">
          로그인해주세요
        </span>
      </h1>
      <h5>로그인 하지 않았습니다.</h5>
      <form @submit.prevent="login()">
        <div class="form-floating">
          <input v-model="user" type="text" class="form-control mb-3" placeholder="ID">
          <label for="floatingInput">ID</label>
        </div>
        <div class="form-floating">
          <input v-model="password" type="password" class="form-control mb-3" id="password" placeholder="Password">
          <label for="floatingPassword">Password</label>
        </div>
        <button class="w-100 btn btn-lg btn-primary" variant="success" type="submit">Sign in</button>
      </form>
    </div>
  </main>
</template>

<script>
export default {
  name: 'LoginView',
  data () {
    return {
      loginSuccess: false,
      loginError: false,
      user: '',
      password: '',
      error: false
    }
  },
  methods: {
    async login () {
      try {
        await this.$store.dispatch('login', {
          user: this.user,
          password: this.password
        })
        await this.$router.push({ name: 'AboutView' })
      } catch (err) {
        this.loginError = true
        this.error = true
        throw new Error(err)
      }
    }
  }
}
</script>

<style scoped>

</style>
