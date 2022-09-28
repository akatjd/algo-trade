<template>
  <header class="p-3 text-bg-dark">
    <div class="container">
      <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
        <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
          <li><router-link to="/" class="nav-link px-2 text-secondary">Home</router-link></li>
        </ul>

        <div class="text-end">
          <router-link v-if="!this.$store.state.loginSuccess" to="/login" class="btn btn-outline-light me-2">Login</router-link>
          <button v-if="this.$store.state.loginSuccess" @click="logout()" class="btn btn-outline-light me-2">Logout</button>
          <router-link to="/register" class="btn btn-outline-light me-2">Register</router-link>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import router from '@/router/index.js'

export default {
  name: 'NavComponent',
  methods: {
    logout () {
      this.$store.dispatch('logout')
      router.push('/login')
    }
  }
}
</script>

<style scoped>

</style>
