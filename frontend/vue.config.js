const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: "../src/main/resources/static",  // 빌드 타겟 디렉토리
  // configureWebpack: {
  //   devServer: {
  //     headers: { "Access-Control-Allow-Origin": "*" }
  //   }
  // },
  devServer: {
    proxy: {
      '/api': {
        // '/api' 로 들어오면 포트 80(스프링 서버)로 보낸다
        target: 'http://127.0.0.1:80',
        changeOrigin: true // cross origin 허용
      }
    }
  }
})
