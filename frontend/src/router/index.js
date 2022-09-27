import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '@/views/LoginView'
import RegisterView from '@/views/RegisterView'
import Protected from '../views/AboutView.vue'

import store from '../store'

const routes = [
  {
    path: '/login',
    component: LoginView
  },
  {
    path: '/Register',
    component: RegisterView
  },
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/protected',
    name: 'Protected',
    component: Protected,
    meta: {
      requiresAuth: true
    }
  }
  // otherwise redirect to home
  // { path: '*', redirect: '/' }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!store.getters.isLoggedIn) {
      next({
        name: 'LoginView'
      })
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router
