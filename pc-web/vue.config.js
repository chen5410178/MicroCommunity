module.exports = {
  devServer: {
    port: 3000, // 设置开发服务器端口
    open: true, // 自动打开浏览器
    proxy: {
    //  '/app': {
    //    target: 'https://demo.homecommunity.cn/app', // 后端API地址
    //    changeOrigin: true,
    //    pathRewrite: {
    //      '^/app': ''
    //    }
    //  },
    //  '/callComponent': {
    //    target: 'https://demo.homecommunity.cn/callComponent', // 后端API地址
    //    changeOrigin: true,
    //    pathRewrite: {
    //      '^/callComponent': ''
    //    }
    //  }
       '/app': {
         target: 'http://localhost:8008/app', // 后端API地址
         changeOrigin: true,
         pathRewrite: {
           '^/app': ''
         }
       },
       '/callComponent': {
         target: 'http://localhost:8008/callComponent', // 后端API地址
         changeOrigin: true,
         pathRewrite: {
           '^/callComponent': ''
         }
       }
    },
    // 关键：禁止开发服务器对静态资源添加 hash
    historyApiFallback: {
      disableDotRule: true,
      rewrites: [
        { from: /\/img\/.*/, to: function (context) { return context.parsedUrl.pathname; } },
        { from: /\*.xlsx/, to: function (context) { return context.parsedUrl.pathname; } }
      ]
    }
  }
} 