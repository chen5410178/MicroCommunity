<template>
    <el-card>
      <div slot="header">
        <span>{{ $t('mqttLog.search.title') }}</span>
      </div>
      <el-form :inline="true" :model="form" class="search-form">
        <el-form-item :label="$t('mqttLog.search.startTime')">
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            :placeholder="$t('mqttLog.search.startTimePlaceholder')"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>
        <el-form-item :label="$t('mqttLog.search.endTime')">
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            :placeholder="$t('mqttLog.search.endTimePlaceholder')"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </template>
  
  <script>
  export default {
    name: 'MqttLogSearch',
    data() {
      return {
        form: {
          startTime: '',
          endTime: ''
        }
      }
    },
    methods: {
      handleSearch() {
        this.$emit('search', this.form)
      }
    }
  }
  </script>
  
  <style scoped>
  .search-form {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
  }
  </style>
