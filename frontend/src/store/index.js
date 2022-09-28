import { createStore } from 'vuex'
import axios from 'axios'
import createPersistedState from 'vuex-persistedstate'

export default createStore({
  state: {
    loginSuccess: false,
    loginError: false,
    userName: null,
    password: null
  },
  mutations: {
    loginSuccess (state, { user, password }) {
      state.loginSuccess = true
      state.userName = user
      state.password = password
    },
    loginError (state, { user, password }) {
      state.loginError = true
      state.userName = user
      state.password = password
    },
    logout (state) {
      state.loginSuccess = false
      state.loginError = false
      state.userName = null
      state.password = null
    }
  },
  actions: {
    async login ({ commit }, { user, password }) {
      try {
        const result = await axios.get('/api/login', {
          auth: {
            username: user,
            password: password
          }
        })
        if (result.status === 200) {
          commit('loginSuccess', {
            userName: user,
            userPass: password
          })
        }
      } catch (err) {
        commit('loginError', {
          userName: user,
          userPass: password
        })
        throw new Error(err)
      }
    },
    logout ({ commit }) {
      commit('logout')
    }
  },
  getters: {
    isLoggedIn: state => state.loginSuccess,
    hasLoginErrored: state => state.loginError,
    getUserName: state => state.userName,
    getUserPass: state => state.userPass
  },
  modules: {
  },
  plugins: [
    createPersistedState({
      storage: window.sessionStorage,
      whiteList: [],
      blackList: ['isLoggedIn']
    })
  ]
})
