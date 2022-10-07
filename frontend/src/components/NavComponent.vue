<template>
  <header class="p-3 text-bg-dark">
    <div class="container">
      <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
        <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
          <li><router-link to="/" class="nav-link px-2 text-white">Home</router-link></li>
          <li class="nav-item dropdown">
            <a v-if="this.$store.state.loginSuccess" class="nav-link dropdown-toggle text-white" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              Trade
            </a>
            <ul class="dropdown-menu dropdown-menu-dark" aria-labelledby="navbarDarkDropdownMenuLink">
              <li><a href="/trade/main" class="dropdown-item">main</a></li>
              <li><a class="dropdown-item" href="#">history</a></li>
              <li><a class="dropdown-item" href="#">Something else here</a></li>
            </ul>
          </li>
        </ul>

        <div class="text-end">
          <router-link v-if="!this.$store.state.loginSuccess" to="/login" class="btn btn-outline-light me-2">Login</router-link>
          <button v-else-if="this.$store.state.loginSuccess" @click="logout()" class="btn btn-outline-light me-2">Logout</button>
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
