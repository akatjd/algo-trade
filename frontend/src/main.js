import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap'

createApp(App).use(store).use(router).mount('#app')

// axios.interceptors.request.use(config => {
//   if (
//     config.method &&
//     (config.method.toLocaleLowerCase() === 'post' ||
//       config.method.toLocaleLowerCase() === 'put')
//   ) {
//     config.headers['content-type'] =
//       'application/x-www-form-urlencoded;charset=utf-8'
//   }
//
//   return config
// })
//
// const app = createApp(App)
// app.config.globalProperties.axios = axios
// app.use(store).use(router).mount('#app')
