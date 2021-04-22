module.exports = {
  lintOnSave: true,
  productionSourceMap: false,
  chainWebpack: (config) => {
    //忽略的打包文件
    config.externals({
      'vue': 'Vue',
      'vue-router': 'VueRouter',
      'vuex': 'Vuex',
      'axios': 'axios',
      'element-ui': 'ELEMENT',
    })
    const entry = config.entry('app')
    entry
      .add('babel-polyfill')
      .end()
    entry
      .add('classlist-polyfill')
      .end()
    entry
      .add('@/mock')
      .end()
  },
  devServer: {
    // 端口配置
    port: 1888,
    // 反向代理配置
    proxy: {
      '/api/cnfic-contract-manage/': {
        target: 'http://localhost:8300/',
        ws: true,
        pathRewrite: {
          '^/api/cnfic-contract-manage': '/'
        }
      },
      '/api/cnfic-resource/': {
        target: 'http://localhost:8010/',
        ws: true,
        pathRewrite: {
          '^/api/cnfic-resource': '/'
        }
      },
      '/api/': {
        target: 'http://xhcj.cluster.tongtech.top/api',
        ws: true,
        pathRewrite: {
          '^/api': '/'
        }
      }
    }
  }
}
