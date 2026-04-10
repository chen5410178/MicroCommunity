<template>
  <div class="test-script">
    <h3>全局脚本测试</h3>
    <div class="test-results">
      <p><strong>Qs 库状态:</strong> {{ qsStatus }}</p>
      <p><strong>Qs 版本:</strong> {{ qsVersion }}</p>
      <p><strong>测试结果:</strong> {{ testResult }}</p>
    </div>
    <el-button @click="testQs" type="primary">测试 Qs 功能</el-button>
  </div>
</template>

<script>
export default {
  name: 'TestScript',
  data() {
    return {
      qsStatus: '检查中...',
      qsVersion: '未知',
      testResult: '未测试'
    }
  },
  mounted() {
    this.checkQsStatus()
  },
  methods: {
    checkQsStatus() {
      // 检查全局 Qs
      if (typeof window.Qs !== 'undefined') {
        this.qsStatus = '已加载'
        this.qsVersion = window.Qs.VERSION || '未知版本'
      } else {
        this.qsStatus = '未加载'
      }
      
      // 检查 Vue 原型上的 Qs
      if (this.$qs) {
        this.qsStatus += ' (Vue 原型已挂载)'
      }
    },
    testQs() {
      try {
        // 测试 Qs 功能
        const testObj = { name: 'test', value: 123 }
        const queryString = this.$qs.stringify(testObj)
        const parsedObj = this.$qs.parse(queryString)
        
        this.testResult = `成功! 序列化: ${queryString}, 解析: ${JSON.stringify(parsedObj)}`
        console.log('Qs 测试成功:', { queryString, parsedObj })
      } catch (error) {
        this.testResult = `失败: ${error.message}`
        console.error('Qs 测试失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.test-script {
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin: 20px;
}

.test-results {
  margin: 15px 0;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.test-results p {
  margin: 5px 0;
}
</style> 