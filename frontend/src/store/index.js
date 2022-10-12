import { createStore } from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import axios from 'axios'

export default createStore({
  state: {
    loginSuccess: false,
    loginError: false,
    userName: null,
    password: null,
    token: null
  },
  mutations: {
    loginSuccess (state, { userName, userPass, token }) {
      console.log(userName)
      console.log(userPass)
      console.log(token)
      state.loginSuccess = true
      state.loginError = false
      state.userName = userName
      state.password = userPass
      state.token = token
    },
    loginError (state, { userName, userPass }) {
      state.loginError = true
      state.userName = userName
      state.password = userPass
    },
    logout (state) {
      state.loginSuccess = false
      state.loginError = false
      state.userName = null
      state.password = null
      state.token = null
    }
  },
  actions: {
    async login ({ commit }, { user, password }) {
      try {
        const result = await axios.post('/api/authenticate', {
          auth: {
            username: user,
            password: password
          }
        })
        if (result.status === 200) {
          console.log(result)
          commit('loginSuccess', {
            userName: user,
            token: result.data.accessToken
          })
        }
      } catch (err) {
        commit('loginError', {
          userName: user
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
    getUserPass: state => state.userPass,
    getToken: state => state.token
  },
  modules: {
  },
  plugins: [
    createPersistedState({
      storage: window.sessionStorage
    })
  ]
})
