const CompressionPlugin = require('compression-webpack-plugin')

const isProd = process.env.NODE_ENV === 'production'

const compressPlugins = [
  new CompressionPlugin({
    filename: '[path][base].gz',
    algorithm: 'gzip',
    test: /\.(js|css|html|svg)$/,
    threshold: 10240,
    minRatio: 0.8
  }),
  new CompressionPlugin({
    filename: '[path][base].br',
    algorithm: 'brotliCompress',
    test: /\.(js|css|html|svg)$/,
    compressionOptions: {
      level: 11
    },
    threshold: 10240,
    minRatio: 0.8
  })
]

module.exports = {
  // 生产环境不生成 sourceMap，避免额外体积与请求
  productionSourceMap: false,
  devServer: {
    port: 3000, // 设置开发服务器端口
    open: true, // 自动打开浏览器
    proxy: {
     '/app': {
       target: 'http://192.168.101.108:8008/app', // 后端API地址
       changeOrigin: true,
       pathRewrite: {
         '^/app': ''
       }
     },
     '/callComponent': {
       target: 'http://192.168.101.108:8008/callComponent', // 后端API地址
       changeOrigin: true,
       pathRewrite: {
         '^/callComponent': ''
       }
     }
      //  '/app': {
      //    target: 'http://localhost:8008/app', // 后端API地址
      //    changeOrigin: true,
      //    pathRewrite: {
      //      '^/app': ''
      //    }
      //  },
      //  '/callComponent': {
      //    target: 'http://localhost:8008/callComponent', // 后端API地址
      //    changeOrigin: true,
      //    pathRewrite: {
      //      '^/callComponent': ''
      //    }
      //  }
    },
    // 关键：禁止开发服务器对静态资源添加 hash
    historyApiFallback: {
      disableDotRule: true,
      rewrites: [
        { from: /\/img\/.*/, to: function (context) { return context.parsedUrl.pathname; } },
        { from: /\*.xlsx/, to: function (context) { return context.parsedUrl.pathname; } }
      ]
    }
  },
  chainWebpack: config => {
    // 关键：删除 Vue CLI 默认注入的 prefetch 插件
    // 默认插件会为每个异步 chunk 生成 <link rel="prefetch">，导致首屏空闲时并发下载数百个无关页面 chunk
    config.plugins.delete('prefetch')
  },
  configureWebpack: {
    optimization: {
      // 单独抽出 runtime，业务 chunk 变更时不影响 vendor/runtime 缓存
      runtimeChunk: 'single',
      splitChunks: {
        chunks: 'all',
        maxInitialRequests: 8,
        maxAsyncRequests: 20,
        cacheGroups: {
          elementUI: {
            name: 'chunk-elementUI',
            test: /[\\/]node_modules[\\/]element-ui[\\/]/,
            priority: 30,
            chunks: 'all',
            reuseExistingChunk: true
          },
          echarts: {
            name: 'chunk-echarts',
            test: /[\\/]node_modules[\\/](echarts|zrender|vue-echarts)[\\/]/,
            priority: 25,
            chunks: 'all',
            reuseExistingChunk: true
          },
          editors: {
            name: 'chunk-editors',
            test: /[\\/]node_modules[\\/](tinymce|@tinymce|wangeditor)[\\/]/,
            priority: 25,
            chunks: 'all',
            reuseExistingChunk: true
          },
          vendors: {
            name: 'chunk-vendors',
            test: /[\\/]node_modules[\\/]/,
            priority: 10,
            chunks: 'initial'
          }
        }
      }
    },
    // 生产构建产出 .gz / .br 静态压缩产物，配合 Nginx gzip_static/brotli_static
    plugins: isProd ? compressPlugins : []
  }
}
