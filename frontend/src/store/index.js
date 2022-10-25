import { createStore } from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import axios from 'axios'

export default createStore({
  state: {
    loginSuccess: false,
    loginError: false,
    userName: null,
    password: null,
    accessToken: null,
    refreshToken: null
  },
  mutations: {
    loginSuccess (state, { userName, userPass, accessToken, refreshToken }) {
      state.loginSuccess = true
      state.loginError = false
      state.userName = userName
      state.password = userPass
      state.accessToken = accessToken
      state.refreshToken = refreshToken
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
      state.accessToken = null
    }
  },
  actions: {
    async login ({ commit }, { user, password }) {
      try {
        const result = await axios.post('/api/authenticate',
          {
            username: user,
            password: password
          }
        )
        if (result.status === 200) {
          commit('loginSuccess', {
            userName: user,
            accessToken: result.data.accessToken,
            refreshToken: result.data.refreshToken
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
    getAccessToken: state => state.accessToken,
    getRefreshToken: state => state.refreshToken
  },
  modules: {
  },
  plugins: [
    createPersistedState({
      storage: window.sessionStorage
    })
  ]
})
